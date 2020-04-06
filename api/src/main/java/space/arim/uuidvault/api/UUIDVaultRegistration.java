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

/**
 * Represents a successfully registered UUIDResolver implementation. <br>
 * See {@link UUIDVault#register(UUIDResolution, org.bukkit.plugin.java.JavaPlugin)}.
 * 
 * @author A248
 *
 */
public interface UUIDVaultRegistration {

	/**
	 * Unregisters the implementation.
	 * 
	 * @return true if the implementation was registered and is now unregistered, false otherwise
	 */
	boolean unregister();
	
}
