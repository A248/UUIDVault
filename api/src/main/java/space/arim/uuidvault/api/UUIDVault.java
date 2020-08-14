/* 
 * UUIDVault-api
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * UUIDVault-api is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * UUIDVault-api is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with UUIDVault-api. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU Lesser General Public License.
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
public abstract class UUIDVault implements CollectiveUUIDResolver {

	private static volatile UUIDVault inst;
	
	protected UUIDVault() {
		
	}
	
	private boolean setInstance0() {
		if (inst == null) {
			synchronized (UUIDVault.class) {
				if (inst == null) {
					inst = this;
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Sets the global instance of UUIDVault to this instance. <br>
	 * Throws {@code IllegalStateException} if the instance is already set
	 * 
	 */
	protected void setInstance() {
		if (!setInstance0()) {
			throw new IllegalStateException("Only 1 UUIDVault global instance allowed!");
		}
	}
	
	/**
	 * If there is no global instance, sets the global instance of UUIDVault. <br>
	 * Otherwise, this is a no-op.
	 * 
	 * @return true if the instance was set, false if there is already one
	 */
	protected boolean setInstancePassive() {
		return setInstance0();
	}
	
	/**
	 * Returns the global instance of UUIDVault
	 * 
	 * @return the instance
	 */
	public static UUIDVault get() {
		return inst;
	}
	
	/**
	 * Registers a {@link UUIDResolver} implementation with the associated plugin's main class. <br>
	 * <br>
	 * The plugin class is used as an identifier and helps ensure no duplicate implementations are registered. <br>
	 * If there is already a registration using the same {@code UUIDResolver} instance or plugin class, the operation will fail
	 * and this will return {@code null}. <br>
	 * <br>
	 * The specified display name should be informative and ideally similar to the name of the plugin. If the resolver generates
	 * an exception, this name will be used in log messages.
	 * 
	 * @param resolver the resolution implementation, must not be null
	 * @param pluginClazz the main class of the associated plugin which stores the mappings, must not be null
	 * @param defaultPriority the byte based priority of the resolver, higher priorities are queried first
	 * @param name a display name for the resolver, must not be null
	 * @return a registration if successfully registered, null if the operation failed for some reason
	 * @throws NullPointerException if either {@code resolver}, {@code pluginClazz}, or {@code name} is null
	 */
	public abstract UUIDVaultRegistration register(UUIDResolver resolver, Class<?> pluginClazz, byte defaultPriority, String name);
	
	/**
	 * Unregisters a resolver. The registration to unregister must have been obtained
	 * with {@link #register(UUIDResolver, Class, byte, String)}. <br>
	 * <br>
	 * If the registration is already unregistered, {@code false} will be returned.
	 * 
	 * @param registration the resolver registration to unregister
	 * @return true if the implementation was registered and is now unregistered, false otherwise
	 */
	public abstract boolean unregister(UUIDVaultRegistration registration);
	
	/**
	 * Whether the resolveNatively methods must be called from the server's main thread. <br>
	 * See {@link #resolveNatively(String)} and {@link #resolveNatively(UUID)} <br>
	 * <br>
	 * Will return {@code false} on servers which allow safe <i>asynchronous</i> calls to the necessary APIs.
	 * 
	 * @return true if resolveNatively methods must be called <i>synchronously</i>, false otherwise
	 */
	public abstract boolean mustCallNativeResolutionSync();
	
	/**
	 * Checks online players, and the offline player cache if applicable,
	 * for players matching the specified name. <br>
	 * If a player is found, the UUID is returned. Else, {@code null} is returned. <br>
	 * <br>
	 * <b>Must be called from the main thread if {@link #mustCallNativeResolutionSync()} is true</b> <br>
	 * Note that the offline player cache is not available on proxy servers.
	 * 
	 * @param name the name of the player whose uuid to find
	 * @return a nonnull uuid if found, else {@code null}
	 * @throws NullPointerException if {@code name} is null
	 */
	public abstract UUID resolveNatively(String name);
	
	/**
	 * Checks online players, and the offline player cache if applicable,
	 * for players matching the specified UUID. <br>
	 * If a player is found, the name is returned. Else, {@code null} is returned. <br>
	 * <br>
	 * <b>Must be called from the main thread if {@link #mustCallNativeResolutionSync()} is true</b>. <br>
	 * Note that the offline player cache is not available on proxy servers.
	 * 
	 * @param uuid the uuid of the player whose name to find
	 * @return a nonnull name if found, else {@code null}
	 * @throws NullPointerException if {@code uuid} is null
	 */
	public abstract String resolveNatively(UUID uuid);
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public abstract UUID resolveImmediately(String name);
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public abstract String resolveImmediately(UUID uuid);
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public abstract CompletableFuture<UUID> resolve(String name);
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public abstract CompletableFuture<String> resolve(UUID uuid);
	
	/**
	 * Creates a {@link CollectiveUUIDResolver} which resolves in the same fashion as UUIDVault would,
	 * except that it will skip or ignore the specified registration. <br>
	 * That is, the resolver of the specified registration will be excluded from the resolver list of
	 * the {@code CollectiveResolver}. <br>
	 * <br>
	 * This is useful for callers who both register an implementation and query UUIDVault for results,
	 * so that they may provide special handling for their own resolver.
	 * 
	 * @param registration the registration whose resolver to exclude from the collective resolver's resolvers
	 * @return a collective resolver with all the resolvers of UUIDVault except that from the specified registration
	 * @throws NullPointerException if {@code registration} is null
	 */
	public abstract CollectiveUUIDResolver createCollectiveResolverIgnoring(UUIDVaultRegistration registration);
	
}
