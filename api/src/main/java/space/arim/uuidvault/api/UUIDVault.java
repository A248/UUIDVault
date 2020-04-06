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
import java.util.concurrent.Executor;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * UUIDVault main API class. <br>
 * Plugins wishing to utilise UUIDVault should use this class to
 * lookup uuid/name mappings.
 * 
 * @author A248
 *
 */
public abstract class UUIDVault implements BaseUUIDResolution {

	private static UUIDVault inst;
	
	protected UUIDVault() {
		if (inst != null) {
			throw new IllegalStateException("Only 1 UUIDVault implementation allowed!");
		}
		inst = this;
	}
	
	/**
	 * Returns the instance of UUIDVault
	 * 
	 * @return the instance
	 */
	public static UUIDVault get() {
		return inst;
	}
	
	/**
	 * Expands a shortened version of a UUID. <br>
	 * <br>
	 * Each form is unique. However, it is simpler to store UUIDs in short form
	 * and expand them into long form when needed. Accordingly, many plugins
	 * store short forms, and sometimes we need to get the long form. <br>
	 * <br>
	 * Example long form: ed5f12cd-6007-45d9-a4b9-940524ddaecf <br>
	 * Example short form: ed5f12cd600745d9a4b9940524ddaecf <br>
	 * <br>
	 * You most likely won't need  this. It is here for convenience.
	 * 
	 * @param uuid the string based short uuid
	 * @return the lengthened uuid string
	 */
	public static String expandUUID(String uuid) {
		return uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16)
		+ "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32);
	}
	
	/**
	 * Gets the asynchronous Executor on which CompletableFuture's are run. <br>
	 * This is most likely just the Bukkit executor
	 * 
	 * @return the executor
	 */
	public abstract Executor getAsyncExecutor();
	
	/**
	 * Registers a {@link UUIDResolution} implementation with an associated plugin. <br>
	 * <br>
	 * The plugin is used to ensure that no duplicate implementations are registered. <br>
	 * Its details may also be used to display a user-friendly information to server administrators.
	 * 
	 * @param resolver the resolution implementation
	 * @param plugin the associated plugin which takes care of the data
	 * @return a registration if successfully registered, null if the operation failed for some reason
	 */
	public abstract UUIDVaultRegistration register(UUIDResolution resolver, JavaPlugin plugin);
	
	/**
	 * Begins a full name lookup asynchronously. <br>
	 * <br>
	 * The completable future, once completed, will <code>null</code> if no mapping was found. <br>
	 * <br>
	 * UUIDVault will draw information from its variety of registered resolution implementations.
	 * 
	 */
	@Override
	public abstract CompletableFuture<UUID> resolve(String name);
	
	/**
	 * Resolves a UUID immediately. This will only check in-memory caches. <br>
	 * <br>
	 * Returns <code>null</code> to indicate not found.
	 * <br>
	 * UUIDVault will first check against online players, to see if any of them
	 * have the name specified. Then it will turn to resolution implementations.
	 * 
	 */
	@Override
	public abstract UUID resolveImmediately(String name);
	
	/**
	 * Begins a full uuid lookup asynchronously. <br>
	 * <br>
	 * The completable future, once completed, will <code>null</code> if no mapping was found.
	 * <br>
	 * UUIDVault will draw information from its variety of registered resolution implementations.
	 * 
	 */
	@Override
	public abstract CompletableFuture<String> resolve(UUID uuid);
	
	/**
	 * Resolves a name immediately. This will only check in-memory caches. <br>
	 * <br>
	 * Returns <code>null</code> to indicate not found.
	 * <br>
	 * UUIDVault will first check against online players, to see if any of them
	 * have the uuid specified. Then it will turn to resolution implementations. <br>
	 * 
	 */
	@Override
	public abstract String resolveImmediately(UUID uuid);
	
}
