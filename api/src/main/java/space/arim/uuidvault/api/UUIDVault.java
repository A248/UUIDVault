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
 * UUIDVault's main API class. <br>
 * Dependents wishing to utilise UUIDVault should use this class to
 * lookup uuid/name mappings. <br>
 * <br>
 * <b>Usage</b> <br>
 * Most usage will begin with getting the instance via <code>UUIDVault.get()</code>. <br>
 * When finding UUIDs or names, the caller does NOT need to check against the current
 * active playerlist or builtin server caches. UUIDVault will check these automatically.
 * 
 * @author A248
 *
 */
public abstract class UUIDVault implements BaseUUIDResolution {

	private static volatile UUIDVault inst;
	
	protected UUIDVault() {
		if (inst == null) {
			synchronized (UUIDVault.class) {
				if (inst == null) {
					inst = this;
					return;
				}
			}
		}
		throw new IllegalStateException("Only 1 UUIDVault implementation allowed!");
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
	 * Each form is unique. However, it is possible to store UUIDs in short form
	 * and expand them into long form when needed. <br>
	 * <br>
	 * Example long form (36 chars): ed5f12cd-6007-45d9-a4b9-940524ddaecf <br>
	 * Example short form (32 chars): ed5f12cd600745d9a4b9940524ddaecf <br>
	 * <br>
	 * You most likely won't need this. It is here for convenience.
	 * 
	 * @param uuid the string based short uuid
	 * @return the lengthened uuid string
	 * @throws IllegalArgumentException if the input is not of length 32
	 */
	public static String expandUUID(String uuid) {
		if (uuid.length() != 32) {
			throw new IllegalArgumentException("Cannot expand " + uuid);
		}
		return uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16)
		+ "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32);
	}
	
	/**
	 * Whether UUIDVault is accepting more implementation registrations. <br>
	 * If not accepting registrations, calls to {@link #register(UUIDResolution, Class, byte, String)}
	 * will throw an exception.
	 * 
	 * @return true if accepting, false otherwise
	 */
	public abstract boolean isAcceptingRegistrations();
	
	/**
	 * Registers a {@link UUIDResolution} implementation with the associated plugin's main class. <br>
	 * Remember to check {@link #isAcceptingRegistrations()} first! <br>
	 * <br>
	 * The plugin class is used as an identifier and ensures that no duplicate implementations are registered. <br>
	 * If there is already a registration using the same plugin class, the operation will fail and this will
	 * return <code>null</code>.
	 * 
	 * @param resolver the resolution implementation
	 * @param pluginClazz the main class of the associated plugin which takes care of the data
	 * @param defaultPriority the byte based priority of the resolver, higher priorities are queried first
	 * @param name an optional user friendly name for the resolver, may be null or empty
	 * @return a registration if successfully registered, null if the operation failed for some reason
	 */
	public abstract UUIDVaultRegistration register(UUIDResolution resolver, Class<?> pluginClazz, byte defaultPriority, String name);
	
	/**
	 * Begins a full name lookup. <br>
	 * <br>
	 * The completable future, once completed, will produce <code>null</code> if no mapping was found.
	 * The future <i>itself</i> will never be null. <br>
	 * <br>
	 * UUIDVault will draw information from its variety of registered resolution implementations.
	 * 
	 * @param name the name of the player whose uuid to find
	 * @return a nonnull completable future which returns a corresponding uuid or <code>null</code> if none was found
	 */
	@Override
	public abstract CompletableFuture<UUID> resolve(String name);
	
	/**
	 * Resolves a UUID immediately. This will only check in{@literal -}memory caches. <br>
	 * <br>
	 * Returns <code>null</code> to indicate not found.
	 * <br>
	 * UUIDVault will first check against online players, to see if any of them
	 * have the name specified. Then it will turn to resolution implementations.
	 * 
	 * @param name the name of the player whose uuid to find
	 * @return a corresponding uuid or <code>null</code> if not found
	 */
	@Override
	public abstract UUID resolveImmediately(String name);
	
	/**
	 * Begins a full uuid lookup. <br>
	 * <br>
	 * The completable future, once completed, will <code>null</code> if no mapping was found.
	 * The future <i>itself</i> will never be null. <br>
	 * <br>
	 * UUIDVault will draw information from its variety of registered resolution implementations.
	 * 
	 * @param uuid the uuid of the player whose name to find
	 * @return a nonnull completable future which returns a corresponding name or <code>null</code> if none was found
	 */
	@Override
	public abstract CompletableFuture<String> resolve(UUID uuid);
	
	/**
	 * Resolves a name immediately. This will only check in{@literal -}memory caches. <br>
	 * <br>
	 * Returns <code>null</code> to indicate not found.
	 * <br>
	 * UUIDVault will first check against online players, to see if any of them
	 * have the uuid specified. Then it will turn to resolution implementations. <br>
	 * 
	 * @param uuid the uuid of the player whose name to find
	 * @return the corresponding playername or <code>null</code> if not found
	 */
	@Override
	public abstract String resolveImmediately(UUID uuid);
	
}
