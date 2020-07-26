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
 * An aggregator of resolvers, which checks all its resolvers for results and reports the first result
 * to the caller.
 * 
 * @author A248
 *
 */
public interface CollectiveUUIDResolver extends BaseUUIDResolver {

	/**
	 * Resolves a UUID immediately from in{@literal -}memory caches. <br>
	 * UUIDVault will call each registered resolver's equivalent method
	 * until it finds a result. <br>
	 * <br>
	 * Returns <code>null</code> to indicate not found; that is, if no resolver
	 * was able to find a result.
	 * 
	 * @param name the name of the player whose uuid to find, must not be null
	 * @return a corresponding uuid or <code>null</code> if not found
	 * @throws NullPointerException if {@code name} is null
	 */
	@Override
	UUID resolveImmediately(String name);
	
	/**
	 * Resolves a name immediately from in{@literal -}memory caches. <br>
	 * UUIDVault will call each registered resolver's equivalent method
	 * until it finds a result. <br>
	 * <br>
	 * Returns <code>null</code> to indicate not found; that is, if no resolver
	 * was able to find a result.
	 * 
	 * @param uuid the uuid of the player whose name to find, must not be null
	 * @return the corresponding playername or <code>null</code> if not found
	 * @throws NullPointerException if {@code uuid} is null
	 */
	@Override
	String resolveImmediately(UUID uuid);
	
	/**
	 * Begins a full name lookup, checking all resolvers until one of them finds a result. <br>
	 * <br>
	 * The completable future, once completed, will produce <code>null</code> if no mapping was found.
	 * The future <i>itself</i> will never be null. <br>
	 * <br>
	 * UUIDVault will draw information from its variety of registered resolvers.
	 * 
	 * @param name the name of the player whose uuid to find, must not be null
	 * @return a nonnull completable future which returns a corresponding uuid or <code>null</code> if none was found
	 * @throws NullPointerException if {@code name} is null
	 */
	@Override
	CompletableFuture<UUID> resolve(String name);
	
	/**
	 * Begins a full uuid lookup, checking all resolvers until on of them finds a result. <br>
	 * <br>
	 * The completable future, once completed, will produce <code>null</code> if no mapping was found.
	 * The future <i>itself</i> will never be null. <br>
	 * <br>
	 * UUIDVault will draw information from its variety of registered resolvers.
	 * 
	 * @param uuid the uuid of the player whose name to find, must not be null
	 * @return a nonnull completable future which returns a corresponding name or <code>null</code> if none was found
	 * @throws NullPointerException if {@code uuid} is null
	 */
	@Override
	CompletableFuture<String> resolve(UUID uuid);
	
}
