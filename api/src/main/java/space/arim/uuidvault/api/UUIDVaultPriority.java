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

/**
 * Simple class containing priority constants for referential use.
 * 
 * @author A248
 *
 */
public class UUIDVaultPriority {

	public static final byte LOWEST = -96;
	public static final byte LOWER = -64;
	public static final byte LOW = -32;
	public static final byte NORMAL = 0;
	public static final byte HIGH = 31;
	public static final byte HIGHER = 63;
	public static final byte HIGHEST = 95;
	
	private UUIDVaultPriority() {
		// prevent instantiation
	}
	
}
