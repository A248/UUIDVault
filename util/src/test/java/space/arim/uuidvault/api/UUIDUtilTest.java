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

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.Test;

public class UUIDUtilTest {
	
	private final UUID uuid = UUID.randomUUID();
	
	@Test
	public void testUUIDStringConversions() {
		String fullUuid = uuid.toString();
		String shortUuid = fullUuid.replace("-", "");
		assertEquals(shortUuid, UUIDUtil.contractFullString(fullUuid));
		assertEquals(fullUuid, UUIDUtil.expandShortString(shortUuid));
	}
	
	@Test
	public void testUUIDShortFormConversions() {
		String shortUuid = UUIDUtil.toShortString(uuid);
		assertEquals(uuid.toString().replace("-", ""), shortUuid);
		assertEquals(uuid, UUIDUtil.fromShortString(shortUuid));
	}
	
	@Test
	public void testUUIDByteArrayConversions() {
		byte[] byteArray = UUIDUtil.toByteArray(uuid);
		assertTrue(byteArray.length == 16);
		assertEquals(uuid, UUIDUtil.fromByteArray(byteArray));
	}
	
	@Test
	public void testUUIDByteArrayOffsetConversions() {
		int offset = ThreadLocalRandom.current().nextInt(32);
		byte[] byteArray = new byte[16 + offset];
		UUIDUtil.toByteArray(uuid, byteArray, offset);
		assertEquals(uuid, UUIDUtil.fromByteArray(byteArray, offset));
	}
	
}
