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
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import space.arim.uuidvault.api.UUIDVault;

public abstract class ImplementationHelper extends UUIDVault {

	private final boolean mustCallNativeResolutionSync;

	ImplementationHelper(boolean mustCallNativeResolutionSync) {
		this.mustCallNativeResolutionSync = mustCallNativeResolutionSync;
	}
	
	@Override
	public boolean mustCallNativeResolutionSync() {
		return mustCallNativeResolutionSync;
	}
	
	private static boolean fastEscapeInvalidNameArgument(String name) {
		return name.isEmpty() || name.length() > 16 || name.indexOf(' ') != -1;
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
	
	@Override
	public CompletableFuture<UUID> resolve(String name) {
		Objects.requireNonNull(name, "Name must not be null");
		if (fastEscapeInvalidNameArgument(name)) {
			return CompletableFuture.completedFuture(null);
		}

		UUID immediate = resolveImmediatelyFromRegistered(name);
		return (immediate != null) ? CompletableFuture.completedFuture(immediate)
				: resolveLaterFromRegistered(name);
	}

	@Override
	public UUID resolveImmediately(String name) {
		Objects.requireNonNull(name, "Name must not be null");
		if (fastEscapeInvalidNameArgument(name)) {
			return null;
		}

		return resolveImmediatelyFromRegistered(name);
	}

	@Override
	public CompletableFuture<String> resolve(UUID uuid) {
		Objects.requireNonNull(uuid, "UUID must not be null");

		String immediate = resolveImmediatelyFromRegistered(uuid);
		return (immediate != null) ? CompletableFuture.completedFuture(immediate)
				: resolveLaterFromRegistered(uuid);
	}

	@Override
	public String resolveImmediately(UUID uuid) {
		Objects.requireNonNull(uuid, "UUID must not be null");

		return resolveImmediatelyFromRegistered(uuid);
	}

	protected abstract UUID resolveNativelyDirectly(String name);

	protected abstract String resolveNativelyDirectly(UUID uuid);

	abstract UUID resolveImmediatelyFromRegistered(String name);

	abstract String resolveImmediatelyFromRegistered(UUID uuid);

	abstract CompletableFuture<UUID> resolveLaterFromRegistered(String name);

	abstract CompletableFuture<String> resolveLaterFromRegistered(UUID uuid);

}
