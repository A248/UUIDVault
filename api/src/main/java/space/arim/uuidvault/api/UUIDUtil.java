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
 * Utility class for manipulating valid UUIDs. Allows conversions between {@link UUID}s,
 * full UUID strings, short UUID strings, and byte arrays. <br>
 * <br>
 * <b>A Note on Preconditions</b> <br>
 * So that it may be used to operate on large volumes of data, this class does not check preconditions.
 * It assumes all UUIDs, strings, and byte arrays are valid representations. Malformed input may lead
 * to unexpected behaviour. Callers are encouraged to validate their own input. <br>
 * <br>
 * <b>UUID Forms</b> <br>
 * This class recognises 3 forms of UUIDs besides {@code java.util.UUID}: <br>
 * 1. Full UUID strings. This the common string based representation of a UUID as defined by {@link UUID#toString()}.
 * UUIDs in this form may be converted back to a {@code java.util.UUID} via the JDK's {@link UUID#fromString(String)}. <br>
 * 2. Short UUID strings. These are the same as full UUID strings except that they are not hyphenated. <br>
 * 3. Byte arrays. These must be 16 bytes in length. <br>
 * <br>
 * Methods are provided for efficient conversion between: <br>
 * 1. Full UUID strings and short UUID strings. <br>
 * 2. Short UUID strings and {@code java.util.UUID}. <br>
 * 3. Byte arrays and {@code java.util.UUID}. <br>
 * <br>
 * Where applicable, conversion methods are designed to be at least as performant as more roundabout approaches.
 * For example, {@link #fromShortString(String)} should be faster than combining {@link #expandShortString(String)} and
 * {@link UUID#fromString(String)}.
 * 
 * @author A248
 *
 */
public class UUIDUtil {

	/*
	 * 
	 * UUID String Conversions
	 * 
	 */
	
	/**
	 * Expands a shortened version of a UUID to the full string form. Inverse operation of
	 * {@link #contractFullString(String)}
	 * 
	 * @param shortUuid the short uuid string
	 * @return the full uuid string
	 */
	public static String expandShortString(String shortUuid) {
		return new StringBuilder()
				.append(shortUuid, 0, 8).append('-')
				.append(shortUuid, 8, 12).append('-')
				.append(shortUuid, 12, 16).append('-')
				.append(shortUuid, 16, 20).append('-')
				.append(shortUuid, 20, 32).toString();
	}
	
	/**
	 * Contracts the full form of a UUID string to its shortened form. This is the inverse operation
	 * of {@link #expandShortString(String)}, and should be identical to replacing/removing all hyphens
	 * in the full uuid string, e.g. <code>fullUuid.replace("{@literal -}", "")</code> for a valid
	 * full UUID string called {@code fullUuid}.
	 * 
	 * @param fullUuid the full uuid string
	 * @return the short uuid string
	 */
	public static String contractFullString(String fullUuid) {
		return new StringBuilder()
				.append(fullUuid, 0, 8)
				.append(fullUuid, 9, 13)
				.append(fullUuid, 14, 18)
				.append(fullUuid, 19, 23)
				.append(fullUuid, 24, 36).toString();
	}
	
	/*
	 * 
	 * Short form conversions
	 * 
	 */
	
	/**
	 * Converts a {@code UUID} to its short form string representation. Inverse operation of
	 * {@link #fromShortString(String)}
	 * 
	 * @param uuid the UUID
	 * @return the short uuid string
	 */
	public static String toShortString(UUID uuid) {
		return Long.toHexString(uuid.getMostSignificantBits()) + Long.toHexString(uuid.getLeastSignificantBits());
	}
	
	/**
	 * Converts a short form uuid string to a {@code UUID}. Inverse operation of {@link #toShortString(UUID)}
	 * 
	 * @param shortUuid the short uuid string
	 * @return the UUID
	 */
	public static UUID fromShortString(String shortUuid) {
		return new UUID(
				Long.parseUnsignedLong(shortUuid.substring(0, 16), 16),
				Long.parseUnsignedLong(shortUuid.substring(16, 32), 16));
	}
	
	/*
	 * 
	 * Byte Array Conversions
	 * 
	 */
	
	/**
	 * Converts a UUID to a byte array. To convert back, see {@link #fromByteArray(byte[])}.
	 * 
	 * @param uuid the UUID
	 * @return the byte array, will always be length 16
	 */
	public static byte[] toByteArray(UUID uuid) {
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
	 * Converts to a UUID from a byte array. This is the inverse operation of {@link #byteArrayFromUUID(UUID)}.
	 * 
	 * @param data the byte array, should be of length 16
	 * @return the UUID
	 */
	public static UUID fromByteArray(byte[] data) {
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
