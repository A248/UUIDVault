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

	private volatile boolean accepting = true;

	ImplementationHelper() {
		
	}

	@Override
	public boolean isAcceptingRegistrations() {
		return accepting;
	}
	
	@Override
	public CompletableFuture<UUID> resolve(String name) {
		Objects.requireNonNull(name);
		if (name.isEmpty() || name.indexOf(' ') != -1) {
			return null;
		}

		UUID immediate = resolveImmediately(name);
		return (immediate != null) ? CompletableFuture.completedFuture(immediate)
				: resolveLaterFromRegistered(name);
	}

	@Override
	public UUID resolveImmediately(String name) {
		Objects.requireNonNull(name);
		if (name.isEmpty() || name.indexOf(' ') != -1) {
			return null;
		}

		UUID uuid = resolveNatively(name);
		return (uuid != null) ? uuid : resolveImmediatelyFromRegistered(name);
	}

	@Override
	public CompletableFuture<String> resolve(UUID uuid) {
		Objects.requireNonNull(uuid);

		String immediate = resolveImmediately(uuid);
		return (immediate != null) ? CompletableFuture.completedFuture(immediate)
				: resolveLaterFromRegistered(uuid);
	}

	@Override
	public String resolveImmediately(UUID uuid) {
		Objects.requireNonNull(uuid);

		String name = resolveNatively(uuid);
		return (name != null) ? name : resolveImmediatelyFromRegistered(uuid);
	}

	abstract UUID resolveImmediatelyFromRegistered(String name);

	abstract String resolveImmediatelyFromRegistered(UUID uuid);

	abstract CompletableFuture<UUID> resolveLaterFromRegistered(String name);

	abstract CompletableFuture<String> resolveLaterFromRegistered(UUID uuid);

	abstract void onStartupCompletion();

	protected abstract UUID resolveNatively(String name);

	protected abstract String resolveNatively(UUID uuid);

	/**
	 * Must be called, NOT overridden
	 * 
	 */
	protected void completeNativeStartup() {
		accepting = false;
		onStartupCompletion();
	}

}
