/* 
 * ArimAPI-util
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-util is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-util is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-util. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.uuidvault.api;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * A service which maps names to UUIDs and vice versa. <br>
 * Uses names, not display names. <i>Names should ignore case</i>. <br>
 * <br>
 * The general contract regarding UUID/name resolution is that a name may map to several
 * uuids, but a uuid may only map to a single name. This means the behaviour of this name
 * mapping is explicitly nondeterministic: calling name resolution methods ({@link #resolve(String)}
 * and {@link #resolveImmediately(String)}) may produce different results over the span of time. Most likely,
 * this may happen if a new player joins with the given name after the first call, such that the second call
 * returns the new player's uuid instead. <br>
 * <br>
 * On the contrary, calling uuid resolution methods ({@link #resolve(UUID)} and {@link #resolveImmediately(UUID)})
 * should produce the same name provided no name change has been detected. It is still possible that the player
 * has in fact changed his or her name, but this information is not yet available to the implementation. <br>
 * <br>
 * <b>Implementations SHOULD NEVER call the Mojang API</b> as part of their implementation of the 'resolve' methods.
 * If every resolver called the Mojang API, the server would quickly incur rate limiting. This also applies
 * to Mojang API proxies thereof, e.g. <a href="https://github.com/Electroid/mojang-api">Electroid's API</a>.
 * If the plugin stores the UUID/name mappings itself, whether stored in a memory, a database, or a flatfile,
 * <i>then</i> it should have a corresponding implementation of this interface. A good rule of thumb is that
 * if the plugin is the sole accessor of this data store, or the main accessor if it exposes an API,
 * then it should implement this interface. <br>
 * <br>
 * Implementations are not in any way required to update or expire old mappings. Thus, some
 * implementations may continue to store months old mappings. This is a feature, not a bug;
 * it enables plugins to have a wide breadth of information from which to draw on. For example,
 * in punishment plugins, a moderator might find evidence of a rule breaker a month after the event,
 * and desire to punish such player by their name. Ideally, the name of the player maps to a UUID. <br>
 * 
 * @author A248
 *
 */
public interface UUIDResolver extends BaseUUIDResolver {

	/**
	 * Resolves a playername to a UUID. <br>
	 * <br>
	 * Implementations may determine whether to query a database or access any sort of resources available
	 * in order to find a UUID whose corresponding playername is the specified string. <br>
	 * <br>
	 * Note that implementations do NOT need to perform the same lookups they would otherwise
	 * in {@link #resolveImmediately(String)}. This is done automatically. <br>
	 * <br>
	 * If no mapping was found, the returned future or its result may be null.
	 * 
	 * @param name the name of the player whose uuid to find, will never be null
	 * @return a nullable completable future which returns a corresponding uuid or {@code null} if it did not find one
	 */
	@Override
	CompletableFuture<UUID> resolve(String name);
	
	/**
	 * Avoids blocking operations and directly resolves the uuid
	 * from an in-memory cache. This method should never block. <br>
	 * <br>
	 * <b>If an implementation fails to find a uuid immediately, it should simply return {@code null}
	 * and stop all execution.</b> <br>
	 * Implementations SHOULD NOT attempt to continue finding a mapping (e.g., in an async thread)
	 * after returning a null result. If every implementation did this, a single call to UUIDVault
	 * would trigger a burst of thread creation.
	 * 
	 * @param name the name of the player whose uuid to find, will never be null
	 * @return a corresponding uuid or {@code null} if not found without blocking
	 */
	@Override
	UUID resolveImmediately(String name);
	
	/**
	 * Resolves a UUID to a playername. <br>
	 * <br>
	 * Implementations may determine whether to query a database or access any sort of resources available
	 * in order to find the playername of the player represented by this UUID. <br>
	 * <br>
	 * Note that implementations do NOT need to perform the same lookups they would otherwise
	 * in {@link #resolveImmediately(UUID)}. This is done automatically. <br>
	 * <br>
	 * If no mapping was found, the returned future OR its result may be null.
	 * 
	 * @param uuid the uuid of the player whose name to find, will never be null
	 * @return a nullable completable future which returns the corresponding playername or {@code null} if it did not find one
	 */
	@Override
	CompletableFuture<String> resolve(UUID uuid);
	
	/**
	 * Avoids blocking operations and directly resolves the name
	 * from an in-memory cache. This method should never block. <br>
	 * <br>
	 * <b>If an implementation fails to find a name immediately, it should simply return {@code null}
	 * and stop all execution.</b> <br>
	 * Implementations SHOULD NOT attempt to continue finding a mapping (e.g., in an async thread)
	 * after returning a null result. If every implementation did this, a single call to UUIDVault
	 * would trigger a burst of thread creation.
	 * 
	 * @param uuid the uuid of the player whose name to find, will never be null
	 * @return the corresponding playername or {@code null} if not found without blocking
	 */
	@Override
	String resolveImmediately(UUID uuid);
	
}
