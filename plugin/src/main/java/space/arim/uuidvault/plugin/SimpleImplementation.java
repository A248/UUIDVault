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

import space.arim.uuidvault.api.UUIDResolver;
import space.arim.uuidvault.api.UUIDVaultRegistration;

public abstract class SimpleImplementation extends ImplementationHelper {

	private final ConcurrentMap<Class<?>, Registration> registrations = new ConcurrentHashMap<>(8, 0.9f, 1);
	/**
	 * If we've finished startup, this list will be nonnull
	 * and we will iterate over it.
	 * 
	 */
	private volatile CopyOnWriteArrayList<UUIDResolver> resolvers;
	
	protected SimpleImplementation(boolean mustCallNativeResolutionSync) {
		super(mustCallNativeResolutionSync);
	}
	
	private <T extends List<UUIDResolver>> T makeResolverList(T resultToPopulate) {

		List<Registration> tempList = new ArrayList<>(registrations.size());
		registrations.forEach((c, r) -> tempList.add(r));
		tempList.sort(Comparator.reverseOrder());

		tempList.forEach((regis) -> resultToPopulate.add(regis.resolver));
		return resultToPopulate;
	}
	
	private List<UUIDResolver> getResolverList() {
		// if we're still starting up, resolvers will be null, so we create a temporary list
		return (resolvers != null) ? resolvers : makeResolverList(new ArrayList<>());
	}
	
	@Override
	void onStartupCompletion() {
		// now that the registrations are finalised, we can cache this list and use it all the time
		resolvers = makeResolverList(new CopyOnWriteArrayList<>());
	}
	
	@Override
	public UUIDVaultRegistration register(UUIDResolver resolver, Class<?> pluginClass, byte defaultPriority, String name) {
		Objects.requireNonNull(resolver, "Resolver must not be null");
		Objects.requireNonNull(pluginClass, "Plugin class must not be null");

		if (!isAcceptingRegistrations()) {
			throw new IllegalStateException("UUIDVault is not accepting registrations!");
		} else if (!verifyNativePluginClass(pluginClass)) {
			throw new IllegalArgumentException("Plugin class is invalid!");
		}
		Registration regis = new Registration(this, pluginClass, resolver, defaultPriority, name);
		Registration existing = registrations.putIfAbsent(pluginClass, regis);
		return (existing == null) ? regis : null;
	}

	boolean unregister(Registration regis) {
		boolean success = registrations.remove(regis.pluginClass, regis);
		if (success && resolvers != null) {
			resolvers.remove(regis.resolver);
		}
		return success;
	}
	
	protected abstract boolean verifyNativePluginClass(Class<?> pluginClass);
	
	protected abstract String getDescriptiveName(Class<?> pluginClass);
	
	protected abstract void logException(String message, Throwable throwable);
	
	@Override
	UUID resolveImmediatelyFromRegistered(String name) {
		for (UUIDResolver resolver : getResolverList()) {

			UUID uuid = resolver.resolveImmediately(name);
			if (uuid != null) {
				return uuid;
			}
		}
		return null;
	}
	
	@Override
	String resolveImmediatelyFromRegistered(UUID uuid) {
		for (UUIDResolver resolver : getResolverList()) {

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
		for (UUIDResolver resolver : getResolverList()) {
			if (result == null) {
				result = safelyHandle(resolver.resolve(name), resolver);
				continue;
			}
			result = result.thenCompose((uuid) -> (uuid != null) ? CompletableFuture.completedFuture(uuid)
					: wrapNullableAsCompletedNull(safelyHandle(resolver.resolve(name), resolver)));
		}
		return wrapNullableAsCompletedNull(result);
	}
	
	@Override
	CompletableFuture<String> resolveLaterFromRegistered(UUID uuid) {
		CompletableFuture<String> result = null;
		for (UUIDResolver resolver : getResolverList()) {
			if (result == null) {
				result = safelyHandle(resolver.resolve(uuid), resolver);
				continue;
			}
			result = result.thenCompose((name) -> (name != null) ? CompletableFuture.completedFuture(name)
					: wrapNullableAsCompletedNull(safelyHandle(resolver.resolve(uuid), resolver)));
		}
		return wrapNullableAsCompletedNull(result);
	}
	
	private static <T> CompletableFuture<T> wrapNullableAsCompletedNull(CompletableFuture<T> nullableFuture) {
		if (nullableFuture == null) {
			return CompletableFuture.completedFuture(null);
		}
		return nullableFuture;
	}
	
	private <T> CompletableFuture<T> safelyHandle(CompletableFuture<T> future, UUIDResolver resolver) {
		if (future == null) {
			return null;
		}
		return future.exceptionally((throwable) -> {
			recordException(resolver, throwable);
			return null;
		});
	}
	
	private void recordException(UUIDResolver resolver, Throwable throwable) {
		Class<?> pluginClass = null;
		String name = null;
		for (Registration registration : registrations.values()) {
			if (registration.resolver == resolver) {
				pluginClass = registration.pluginClass;
				name = registration.name;
				break;
			}
		}
		String message;
		if (pluginClass == null) {
			message = "Unknown resolver encountered an error while resolving a UUID or name";
		} else {
			message = "Resolver '" + ((name == null || name.isEmpty()) ? "Unnamed" : name) + "' from "
					+ getDescriptiveName(pluginClass) + " encountered an error while resolving a UUID or name";
		}
		logException(message, throwable);
	}

}
