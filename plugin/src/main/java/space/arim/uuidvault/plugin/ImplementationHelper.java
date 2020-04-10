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
import java.util.concurrent.Executor;

import space.arim.uuidvault.api.UUIDVault;

public abstract class ImplementationHelper extends UUIDVault {

	private final Executor asyncExecutor;

	ImplementationHelper(Executor asyncExecutor) {
		this.asyncExecutor = asyncExecutor;
	}
	
	@Override
	public Executor getAsyncExecutor() {
		return asyncExecutor;
	}

	@Override
	public CompletableFuture<UUID> resolve(String name) {
		UUID immediate = resolveImmediately(name);
		return (immediate != null) ? CompletableFuture.completedFuture(immediate)
				: CompletableFuture.supplyAsync(() -> resolveAsyncedFromRegistered(name), asyncExecutor);
	}

	@Override
	public UUID resolveImmediately(String name) {
		UUID uuid = resolveNatively(name);
		return (uuid != null) ? uuid : resolveImmediatelyFromRegistered(name);
	}
	
	@Override
	public CompletableFuture<String> resolve(UUID uuid) {
		String immediate = resolveImmediately(uuid);
		return (immediate != null) ? CompletableFuture.completedFuture(immediate)
				: CompletableFuture.supplyAsync(() -> resolveAsyncedFromRegistered(uuid), asyncExecutor);
	}
	
	@Override
	public String resolveImmediately(UUID uuid) {
		String name = resolveNatively(uuid);
		return (name != null) ? name : resolveImmediatelyFromRegistered(uuid);
	}
	
	abstract UUID resolveImmediatelyFromRegistered(String name);
	
	abstract String resolveImmediatelyFromRegistered(UUID uuid);

	abstract UUID resolveAsyncedFromRegistered(String name);

	abstract String resolveAsyncedFromRegistered(UUID uuid);
	
	protected abstract UUID resolveNatively(String name);
	
	protected abstract String resolveNatively(UUID uuid);

}
