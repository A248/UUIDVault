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

import java.util.Objects;
import java.util.SortedSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListSet;
import space.arim.uuidvault.api.UUIDResolver;
import space.arim.uuidvault.api.UUIDVaultRegistration;

public abstract class SimpleImplementation extends ImplementationHelper {

	private final SortedSet<Registration> registrations = new ConcurrentSkipListSet<>();
	
	protected SimpleImplementation(boolean mustCallNativeResolutionSync) {
		super(mustCallNativeResolutionSync);
	}
	
	@Override
	public UUIDVaultRegistration register(UUIDResolver resolver, Class<?> pluginClass, byte defaultPriority, String name) {
		Objects.requireNonNull(resolver, "Resolver must not be null");
		Objects.requireNonNull(pluginClass, "Plugin class must not be null");

		if (!verifyNativePluginClass(pluginClass)) {
			throw new IllegalArgumentException("Plugin class is invalid!");
		}
		Registration regis = new Registration(this, pluginClass, resolver, defaultPriority, name);
		return (registrations.add(regis)) ? regis : null;
	}

	boolean unregister(Registration regis) {
		return registrations.remove(regis);
	}
	
	protected abstract boolean verifyNativePluginClass(Class<?> pluginClass);
	
	protected abstract String getDescriptiveName(Class<?> pluginClass);
	
	protected abstract void logException(String message, Throwable throwable);
	
	@Override
	UUID resolveImmediatelyFromRegistered(String name) {
		for (Registration registration : registrations) {
			UUIDResolver resolver = registration.resolver;

			UUID uuid = resolver.resolveImmediately(name);
			if (uuid != null) {
				return uuid;
			}
		}
		return null;
	}
	
	@Override
	String resolveImmediatelyFromRegistered(UUID uuid) {
		for (Registration registration : registrations) {
			UUIDResolver resolver = registration.resolver;

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
		for (Registration registration : registrations) {
			UUIDResolver resolver = registration.resolver;

			if (result == null) {
				result = safelyHandle(resolver.resolve(name), registration);
				continue;
			}
			result = result.thenCompose((uuid) -> (uuid != null) ? CompletableFuture.completedFuture(uuid)
					: wrapNullableAsCompletedNull(safelyHandle(resolver.resolve(name), registration)));
		}
		return wrapNullableAsCompletedNull(result);
	}
	
	@Override
	CompletableFuture<String> resolveLaterFromRegistered(UUID uuid) {
		CompletableFuture<String> result = null;
		for (Registration registration : registrations) {
			UUIDResolver resolver = registration.resolver;

			if (result == null) {
				result = safelyHandle(resolver.resolve(uuid), registration);
				continue;
			}
			result = result.thenCompose((name) -> (name != null) ? CompletableFuture.completedFuture(name)
					: wrapNullableAsCompletedNull(safelyHandle(resolver.resolve(uuid), registration)));
		}
		return wrapNullableAsCompletedNull(result);
	}
	
	private static <T> CompletableFuture<T> wrapNullableAsCompletedNull(CompletableFuture<T> nullableFuture) {
		if (nullableFuture == null) {
			return CompletableFuture.completedFuture(null);
		}
		return nullableFuture;
	}
	
	private <T> CompletableFuture<T> safelyHandle(CompletableFuture<T> future, Registration registration) {
		if (future == null) {
			return null;
		}
		return future.exceptionally((throwable) -> {
			Class<?> pluginClass = registration.pluginClass;
			String name = ((registration.name == null || registration.name.isEmpty()) ? "Unnamed" : registration.name);
			logException("Resolver '" + name + "' from " + getDescriptiveName(pluginClass)
					+ " encountered an error while resolving a UUID or name", throwable);
			return null;
		});
	}

}
