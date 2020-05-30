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

import java.util.Objects;
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
	
	/**
	 * Serialises a UUID to a byte array. Since a UUID is 128 bits,
	 * it may be stored in a byte array of length 16.
	 * 
	 * @param uuid the UUID
	 * @return the byte array, will always be length 16
	 * @throws NullPointerException if uuid is null
	 */
	public static byte[] byteArrayFromUUID(UUID uuid) {
		Objects.requireNonNull(uuid, "UUID must not be null");
		return byteArrayFrom2Longs(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
	}
	
	private static byte[] byteArrayFrom2Longs(long msb, long lsb) {
		byte[] result = new byte[16];
		for (int i = 7; i >= 0; i--) {
			result[i] = (byte) (msb & 0xffL);
			msb >>= 8;
		}
		for (int i = 15; i >= 8; i--) {
			result[i] = (byte) (lsb & 0xffL);
			lsb >>= 8;
		}
		return result;
	}
	
	/**
	 * Deserialises a UUID from a byte array. This is the inverse operation
	 * of {@link #byteArrayFromUUID(UUID)}.
	 * 
	 * @param data the byte array, must be of length 16
	 * @return the UUID
	 * @throws NullPointerException if data is null
	 * @throws IndexOutOfBoundsException if the array length is not 16
	 */
	public static UUID uuidFromByteArray(byte[] data) {
		if (Objects.requireNonNull(data, "data must not be null").length != 16) {
			throw new IndexOutOfBoundsException();
		}
		return new UUID(
				longFromBytes(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7]),
				longFromBytes(data[8], data[9], data[10], data[11], data[12], data[13], data[14], data[15]));
	}
	
	private static long longFromBytes(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8) {
		return (b1 & 0xffL) << 56
				| (b2 & 0xffL) << 48
				| (b3 & 0xffL) << 40
				| (b4 & 0xffL) << 32
				| (b5 & 0xffL) << 24
				| (b6 & 0xffL) << 16
				| (b7 & 0xffL) << 8
				| (b8 & 0xffL);
	}
	
}
