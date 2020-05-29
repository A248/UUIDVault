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

/**
 * Utility class with convenience methods for manipulating UUIDs. <br>
 * May be useful for resolvers.
 * 
 * @author A248
 *
 */
public class UUIDUtil {

	/**
	 * Expands a shortened version of a UUID. This is the inverse operation of
	 * <code>uuid.toString().replace("-", "")</code> where <i>uuid</i> is a java.util.UUID. <br>
	 * <br>
	 * Each form is unique. However, it is possible to work with short UUIDs
	 * and expand them as needed. <br>
	 * <br>
	 * Example long form (36 chars): ed5f12cd-6007-45d9-a4b9-940524ddaecf <br>
	 * Example short form (32 chars): ed5f12cd600745d9a4b9940524ddaecf
	 * 
	 * @param shortUuid the short uuid string
	 * @return the lengthened uuid string
	 * @throws IndexOutOfBoundsException if the input is not of length 32
	 */
	public static String expand(String shortUuid) {
		if (shortUuid.length() != 32) {
			throw new IndexOutOfBoundsException("Cannot expand 32-char " + shortUuid);
		}
		return shortUuid.substring(0, 8) + "-" + shortUuid.substring(8, 12) + "-" + shortUuid.substring(12, 16)
		+ "-" + shortUuid.substring(16, 20) + "-" + shortUuid.substring(20, 32);
	}
	
	/**
	 * Nearly identical to {@link #expand(String)}, however, this adds the additional
	 * operation of parsing the result string into a full UUID.
	 * 
	 * @param shortUuid the short uuid string
	 * @return the parsed UUID
	 * @throws IndexOutOfBoundsException if the input is not of length 32
	 * @throws IllegalArgumentException if {@link UUID#fromString(String)} threw IAE
	 */
	public static UUID expandAndParse(String shortUuid) {
		return UUID.fromString(expand(shortUuid));
	}
	
}
