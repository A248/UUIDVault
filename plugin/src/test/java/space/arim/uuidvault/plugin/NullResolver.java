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

import space.arim.uuidvault.api.UUIDResolver;
import space.arim.uuidvault.api.UUIDVault;
import space.arim.uuidvault.api.UUIDVaultRegistration;

public class NullResolver implements UUIDResolver {

	static UUIDVaultRegistration register(UUIDVault uuidVault, byte priority) {
		return uuidVault.register(new NullResolver(), NullResolver.class, priority, "NullResolver");
	}
	
	@Override
	public CompletableFuture<UUID> resolve(String name) {
		return null;
	}

	@Override
	public UUID resolveImmediately(String name) {
		return null;
	}

	@Override
	public CompletableFuture<String> resolve(UUID uuid) {
		return null;
	}

	@Override
	public String resolveImmediately(UUID uuid) {
		return null;
	}

}
