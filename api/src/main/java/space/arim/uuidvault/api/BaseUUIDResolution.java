/* 
 * UUIDVault-api
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * UUIDVault-api is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * UUIDVault-api is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with UUIDVault-api. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.uuidvault.api;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Base class for {@link UUIDResolution} and {@link UUIDVault}.
 * 
 * @author A248
 *
 */
public interface BaseUUIDResolution {

	/**
	 * See respective subinterfaces.
	 * 
	 * @param name the name of the player whose uuid to find
	 * @return a completable future which returns a corresponding uuid or <code>null</code> if it did not find one
	 */
	CompletableFuture<UUID> resolve(String name);
	
	/**
	 * See respective subinterfaces.
	 * 
	 * @param name the name of the player whose uuid to find
	 * @return a corresponding uuid or <code>null</code> if not found without blocking
	 */
	UUID resolveImmediately(String name);
	
	/**
	 * 
	 * 
	 * @param uuid the uuid of the player whose name to find
	 * @return a completable future which returns the corresponding playername or <code>null</code> if it did not find one
	 */
	CompletableFuture<String> resolve(UUID uuid);
	
	/**
	 * 
	 * 
	 * @param uuid the uuid of the player whose name to find
	 * @return the corresponding playername or <code>null</code> if not found without blocking
	 */
	String resolveImmediately(UUID uuid);
	
}