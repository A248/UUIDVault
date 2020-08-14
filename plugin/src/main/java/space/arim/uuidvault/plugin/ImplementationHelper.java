/* 
 * UUIDVault-plugin
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * UUIDVault-plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * UUIDVault-plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with UUIDVault-plugin. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU Lesser General Public License.
 */
package space.arim.uuidvault.plugin;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import space.arim.uuidvault.api.UUIDResolver;
import space.arim.uuidvault.api.UUIDVault;

abstract class ImplementationHelper extends UUIDVault {

	ImplementationHelper() {

	}
	
	private static boolean fastEscapeInvalidNameArgument(String name) {
		return name.isEmpty() || name.length() > 16 || name.indexOf(' ') != -1;
	}
	
	/*
	 * 
	 * Native resolution
	 * 
	 */
	
	@Override
	public boolean mustCallNativeResolutionSync() {
		// False by default, overriden for Spigot and Sponge
		return false;
	}
	
	@Override
	public UUID resolveNatively(String name) {
		Objects.requireNonNull(name, "Name must not be null");
		if (fastEscapeInvalidNameArgument(name)) {
			return null;
		}

		return resolveNativelyDirectly(name);
	}
	
	@Override
	public String resolveNatively(UUID uuid) {
		Objects.requireNonNull(uuid, "UUID must not be null");

		return resolveNativelyDirectly(uuid);
	}
	
	abstract UUID resolveNativelyDirectly(String name);

	abstract String resolveNativelyDirectly(UUID uuid);
	
	/*
	 * 
	 * Core resolution
	 * 
	 */
	
	UUID resolveImmediately(String name, UUIDResolver skip) {
		Objects.requireNonNull(name, "Name must not be null");
		if (fastEscapeInvalidNameArgument(name)) {
			return null;
		}

		return resolveImmediatelyFromRegistered(name, skip);
	}

	@Override
	public UUID resolveImmediately(String name) {
		return resolveImmediately(name, null);
	}
	
	String resolveImmediately(UUID uuid, UUIDResolver skip) {
		Objects.requireNonNull(uuid, "UUID must not be null");

		return resolveImmediatelyFromRegistered(uuid, skip);
	}
	
	@Override
	public String resolveImmediately(UUID uuid) {
		return resolveImmediately(uuid, null);
	}
	
	CompletableFuture<UUID> resolve(String name, UUIDResolver skip) {
		Objects.requireNonNull(name, "Name must not be null");
		if (fastEscapeInvalidNameArgument(name)) {
			return CompletableFuture.completedFuture(null);
		}

		UUID immediate = resolveImmediatelyFromRegistered(name, skip);
		return (immediate != null) ? CompletableFuture.completedFuture(immediate)
				: resolveLaterFromRegistered(name, skip);
	}
	
	@Override
	public CompletableFuture<UUID> resolve(String name) {
		return resolve(name, null);
	}
	
	CompletableFuture<String> resolve(UUID uuid, UUIDResolver skip) {
		Objects.requireNonNull(uuid, "UUID must not be null");

		String immediate = resolveImmediatelyFromRegistered(uuid, skip);
		return (immediate != null) ? CompletableFuture.completedFuture(immediate)
				: resolveLaterFromRegistered(uuid, skip);
	}

	@Override
	public CompletableFuture<String> resolve(UUID uuid) {
		return resolve(uuid, null);
	}

	abstract UUID resolveImmediatelyFromRegistered(String name, UUIDResolver skip);

	abstract String resolveImmediatelyFromRegistered(UUID uuid, UUIDResolver skip);

	abstract CompletableFuture<UUID> resolveLaterFromRegistered(String name, UUIDResolver skip);

	abstract CompletableFuture<String> resolveLaterFromRegistered(UUID uuid, UUIDResolver skip);

}
