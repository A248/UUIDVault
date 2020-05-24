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
 * All methods are thread safe unless specified otherwise. <br>
 * All name lookups are case insensitive. <br>
 * <br>
 * One should generally begin by checking native server information, namely online players and the offline player cache,
 * for players matching the UUID or name. This may be done at the caller's discretion - either automatically using
 * {@link #resolveNatively(String)} and {@link #resolveNatively(UUID)}, or manually, using environment-specific APIs. <br>
 * <br>
 * Then, one may proceed to check the registered resolvers. If the caller needs an immediate result,
 * {@link #resolveImmediately(String)} and {@link #resolveImmediately(UUID)} may be used. Otherwise, if the caller wants a full
 * result using all information from resolvers, {@link #resolve(String)} and {@link #resolve(UUID)}, will return
 * <code>CompletableFuture</code>s.
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
	 * @throws IndexOutOfBoundsException if the input is not of length 32
	 */
	public static String expandUUID(String uuid) {
		if (uuid.length() != 32) {
			throw new IndexOutOfBoundsException("Cannot expand 32-char " + uuid);
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
	 * return <code>null</code>. <br>
	 * <br>
	 * The specified name is optional; it may be null or empty. However, resolvers are encouraged to use an informative,
	 * user{@literal -}friendly name, which is ideally similar to the name of the plugin. If the resolver generates
	 * an exception, this name will be used in log messages.
	 * 
	 * @param resolver the resolution implementation, must not be null
	 * @param pluginClazz the main class of the associated plugin which stores the mappings, must not be null
	 * @param defaultPriority the byte based priority of the resolver, higher priorities are queried first
	 * @param name a user friendly name for the resolver, can be null or empty but an informative name is encouraged
	 * @return a registration if successfully registered, null if the operation failed for some reason
	 */
	public abstract UUIDVaultRegistration register(UUIDResolution resolver, Class<?> pluginClazz, byte defaultPriority, String name);
	
	/**
	 * Whether the resolveNatively methods must be called from the server's main thread. <br>
	 * See {@link #resolveNatively(String)} and {@link #resolveNatively(UUID)} <br>
	 * <br>
	 * Will return <code>false</code> on servers which allow safe <i>asynchronous</i> calls to the necessary APIs.
	 * 
	 * @return true if resolveNatively methods must be called <i>synchronously</i>, false otherwise
	 */
	public abstract boolean mustCallNativeResolutionSync();
	
	/**
	 * Checks online players, and the offline player cache if applicable,
	 * for players matching the specified name. <br>
	 * If a player is found, the UUID is returned. Else, <code>null</code> is returned. <br>
	 * <br>
	 * <b>Must be called from the main thread if {@link #mustCallNativeResolutionSync()} is true</b> <br>
	 * Note that the offline player cache is not available on proxy servers.
	 * 
	 * @param name the name of the player whose uuid to find
	 * @return a nonnull uuid if found, else <code>null</code>
	 */
	public abstract UUID resolveNatively(String name);
	
	/**
	 * Checks online players, and the offline player cache if applicable,
	 * for players matching the specified UUID. <br>
	 * If a player is found, the name is returned. Else, <code>null</code> is returned. <br>
	 * <br>
	 * <b>Must be called from the main thread if {@link #mustCallNativeResolutionSync()} is true</b>. <br>
	 * Note that the offline player cache is not available on proxy servers.
	 * 
	 * @param uuid the uuid of the player whose name to find
	 * @return a nonnull name if found, else <code>null</code>
	 */
	public abstract String resolveNatively(UUID uuid);
	
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
	 */
	@Override
	public abstract CompletableFuture<UUID> resolve(String name);
	
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
	 */
	@Override
	public abstract UUID resolveImmediately(String name);
	
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
	 */
	@Override
	public abstract CompletableFuture<String> resolve(UUID uuid);
	
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
	 */
	@Override
	public abstract String resolveImmediately(UUID uuid);
	
}
