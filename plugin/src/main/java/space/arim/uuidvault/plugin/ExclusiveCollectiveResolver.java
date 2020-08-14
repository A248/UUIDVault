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

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import space.arim.uuidvault.api.CollectiveUUIDResolver;
import space.arim.uuidvault.api.UUIDResolver;

class ExclusiveCollectiveResolver implements CollectiveUUIDResolver {

	private final ImplementationHelper plugin;
	private final UUIDResolver skip;
	
	ExclusiveCollectiveResolver(ImplementationHelper plugin, UUIDResolver skip) {
		this.plugin = plugin;
		this.skip = skip;
	}

	@Override
	public UUID resolveImmediately(String name) {
		return plugin.resolveImmediately(name, skip);
	}
	
	@Override
	public String resolveImmediately(UUID uuid) {
		return plugin.resolveImmediately(uuid, skip);
	}
	
	@Override
	public CompletableFuture<UUID> resolve(String name) {
		return plugin.resolve(name, skip);
	}

	@Override
	public CompletableFuture<String> resolve(UUID uuid) {
		return plugin.resolve(uuid, skip);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + System.identityHashCode(plugin);
		result = prime * result + System.identityHashCode(skip);
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof ExclusiveCollectiveResolver)) {
			return false;
		}
		ExclusiveCollectiveResolver other = (ExclusiveCollectiveResolver) object;
		return plugin == other.plugin && skip == other.skip;
	}

	@Override
	public String toString() {
		return "ExclusiveCollectiveResolver [plugin=" + plugin + ", skip=" + skip + "]";
	}
	
}
