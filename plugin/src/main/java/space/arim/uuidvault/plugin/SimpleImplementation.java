/* 
 * UUIDVault-plugin
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * UUIDVault-plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * UUIDVault-plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with UUIDVault-plugin. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.uuidvault.plugin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

import space.arim.uuidvault.api.UUIDResolution;
import space.arim.uuidvault.api.UUIDVaultRegistration;

public abstract class SimpleImplementation extends ImplementationHelper {
	
	private final ConcurrentMap<Class<?>, Registration> registrations = new ConcurrentHashMap<>();
	/**
	 * If we've finished startup, this list will be nonnull
	 * and we will iterate over it.
	 * 
	 */
	private volatile CopyOnWriteArrayList<UUIDResolution> resolvers;
	
	protected SimpleImplementation(Executor asyncExecutor) {
		super(asyncExecutor);
	}
	
	private List<UUIDResolution> makeResolverList() {

		List<Registration> tempList = new ArrayList<>();
		registrations.forEach((c, r) -> tempList.add(r));
		tempList.sort(Comparator.reverseOrder());

		List<UUIDResolution> result = new ArrayList<>();
		tempList.forEach((regis) -> result.add(regis.resolver));
		return result;
	}
	
	private List<UUIDResolution> getResolverList() {
		// if we're still starting up, resolvers will be null, so we create a temporary list
		return (resolvers != null) ? resolvers : makeResolverList();
	}
	
	@Override
	void onStartupCompletion() {
		// now that the registrations are finalised, we can cache this list and use it all the time
		resolvers = new CopyOnWriteArrayList<>(makeResolverList());
	}
	
	@Override
	public UUIDVaultRegistration register(UUIDResolution resolver, Class<?> pluginClass, byte defaultPriority, String name) {
		Objects.requireNonNull(resolver, "Resolver must not be null");
		Objects.requireNonNull(pluginClass, "Plugin class must not be null");

		if (!isAcceptingRegistrations()) {
			throw new IllegalStateException("UUIDVault is not accepting registrations!");
		} else if (!verifyNativePluginClass(pluginClass)) {
			throw new IllegalArgumentException("Plugin class is invalid!");
		}
		Registration regis = new Registration(this, pluginClass, resolver, defaultPriority);
		Registration existing = registrations.putIfAbsent(pluginClass, regis);
		return (existing == null) ? regis : null;
	}

	boolean unregister(Registration regis) {
		boolean success = registrations.remove(regis.pluginClass, regis);
		if (success) {
			resolvers.remove(regis.resolver);
		}
		return success;
	}
	
	protected abstract boolean verifyNativePluginClass(Class<?> pluginClass);
	
	protected abstract void notifyException(UUIDResolution resolver, Throwable throwable);
	
	@Override
	UUID resolveImmediatelyFromRegistered(String name) {
		for (UUIDResolution resolver : getResolverList()) {

			UUID uuid = resolver.resolveImmediately(name);
			if (uuid != null) {
				return uuid;
			}
		}
		return null;
	}
	
	@Override
	String resolveImmediatelyFromRegistered(UUID uuid) {
		for (UUIDResolution resolver : getResolverList()) {

			String name = resolver.resolveImmediately(uuid);
			if (name != null) {
				return name;
			}
		}
		return null;
	}

	@Override
	CompletableFuture<UUID> resolveLaterFromRegistered(String name) {
		CompletableFuture<UUID> result = null;
		for (UUIDResolution resolver : getResolverList()) {
			if (result == null) {
				result = handle(resolver.resolve(name), resolver);
				continue;
			}
			result = result.thenCompose((uuid) -> (uuid != null) ? CompletableFuture.completedFuture(uuid)
					: wrapNullableAsCompletedNull(handle(resolver.resolve(name), resolver)));
		}
		return wrapNullableAsCompletedNull(result);
	}
	
	@Override
	CompletableFuture<String> resolveLaterFromRegistered(UUID uuid) {
		CompletableFuture<String> result = null;
		for (UUIDResolution resolver : getResolverList()) {
			if (result == null) {
				result = handle(resolver.resolve(uuid), resolver);
				continue;
			}
			result = result.thenCompose((name) -> (name != null) ? CompletableFuture.completedFuture(name)
					: wrapNullableAsCompletedNull(handle(resolver.resolve(uuid), resolver)));
		}
		return wrapNullableAsCompletedNull(result);
	}
	
	private static <T> CompletableFuture<T> wrapNullableAsCompletedNull(CompletableFuture<T> nullableFuture) {
		if (nullableFuture == null) {
			return CompletableFuture.completedFuture(null);
		}
		return nullableFuture;
	}
	
	private <T> CompletableFuture<T> handle(CompletableFuture<T> future, UUIDResolution resolver) {
		if (future == null) {
			return null;
		}
		return future.handle((value, throwable) -> {
			if (throwable != null) {
				notifyException(resolver, throwable);
				return null;
			}
			return value;
		});
	}

}
