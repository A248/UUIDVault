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

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;

import space.arim.uuidvault.api.UUIDResolution;
import space.arim.uuidvault.api.UUIDVaultRegistration;

public abstract class SimpleImplementation extends ImplementationHelper {

	private final ConcurrentMap<Class<?>, UUIDResolution> resolvers = new ConcurrentHashMap<>();
	
	protected SimpleImplementation(Executor asyncExecutor) {
		super(asyncExecutor);
	}
	
	@Override
	public UUIDVaultRegistration register(UUIDResolution resolver, Class<?> pluginClass) {
		if (!verifyNativePluginClass(pluginClass)) {
			throw new IllegalStateException("Plugin class is invalid!");
		}
		UUIDResolution existing = resolvers.putIfAbsent(pluginClass, resolver);
		return (existing == null) ?  new Registration(this, pluginClass, resolver) : null;
	}
	
	protected abstract boolean verifyNativePluginClass(Class<?> pluginClass);

	boolean unregister(Class<?> pluginClass, UUIDResolution resolver) {
		return resolvers.remove(pluginClass, resolver);
	}
	
	@Override
	UUID resolveImmediatelyFromRegistered(String name) {
		for (UUIDResolution resolver : resolvers.values()) {
			UUID uuid = resolver.resolveImmediately(name);
			if (uuid != null) {
				return uuid;
			}
		}
		return null;
	}
	
	@Override
	String resolveImmediatelyFromRegistered(UUID uuid) {
		for (UUIDResolution resolver : resolvers.values()) {
			String name = resolver.resolveImmediately(uuid);
			if (name != null) {
				return name;
			}
		}
		return null;
	}

	@Override
	UUID resolveAsyncedFromRegistered(String name) {
		for (UUIDResolution resolver : resolvers.values()) {

			CompletableFuture<UUID> future = resolver.resolve(name);
			if (future != null) {
				UUID uuid = future.join();
				if (uuid != null) {
					return uuid;
				}
			}

		}
		return null;
	}

	@Override
	String resolveAsyncedFromRegistered(UUID uuid) {
		for (UUIDResolution resolver : resolvers.values()) {

			CompletableFuture<String> future = resolver.resolve(uuid);
			if (future != null) {
				String name = future.join();
				if (name != null) {
					return name;
				}
			}
		}
		return null;
	}

}
