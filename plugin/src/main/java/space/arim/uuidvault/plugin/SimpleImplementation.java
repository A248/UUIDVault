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

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import space.arim.uuidvault.api.UUIDResolver;
import space.arim.uuidvault.api.UUIDVaultRegistration;

public abstract class SimpleImplementation extends ImplementationHelper {
	
	private final AtomicReference<Registration[]> registrations = new AtomicReference<>(new Registration[] {});
	
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
		Registration regisToAdd = new Registration(this, pluginClass, resolver, defaultPriority, name);
		Registration[] existing;
		Registration[] updated;
		do {
			existing = registrations.get();
			for (Registration registration : existing) {
				if (registration.pluginClass == pluginClass) {
					return null;
				}
			}
			updated = Arrays.copyOf(existing, existing.length + 1);
			updated[existing.length]= regisToAdd; 
			Arrays.sort(updated);
		} while (!registrations.compareAndSet(existing, updated));
		return regisToAdd;
	}

	boolean unregister(Registration regisToRemove) {
		Registration[] existing;
		Registration[] updated;
		do {
			existing = registrations.get();
			updated = new Registration[existing.length - 1];
			boolean found = false;
			for (int n = 0; n < existing.length; n++) {
				if (existing[n] == regisToRemove) {
					found = true;
				} else {
					updated[found ? n - 1 : n] = existing[n];
				}
			}
			if (!found) {
				return false;
			}
		} while (!registrations.compareAndSet(existing, updated));
		return true;
	}
	
	protected abstract boolean verifyNativePluginClass(Class<?> pluginClass);
	
	protected abstract String getDescriptiveName(Class<?> pluginClass);
	
	protected abstract void logException(String message, Throwable throwable);
	
	@Override
	UUID resolveImmediatelyFromRegistered(String name) {
		for (Registration registration : registrations.get()) {
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
		for (Registration registration : registrations.get()) {
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
		for (Registration registration : registrations.get()) {
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
		for (Registration registration : registrations.get()) {
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
