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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.RepeatedTest;

public class UUIDUtilTest {
	
	@RepeatedTest(10)
	public void shouldMaintainUUIDToFromByteArray() {
		UUID uuid = UUID.randomUUID();
		byte[] uuidAsByteArray = UUIDUtil.byteArrayFromUUID(uuid);
		UUID uuidBack = UUIDUtil.uuidFromByteArray(uuidAsByteArray);
		assertEquals(uuid, uuidBack);
		assertArrayEquals(uuidAsByteArray, UUIDUtil.byteArrayFromUUID(uuidBack));
	}
	
	@RepeatedTest(10)
	public void shouldMainUUIDToFromShortForm() {
		UUID uuid = UUID.randomUUID();
		String shortUuid = uuid.toString().replace("-", "");
		assertEquals(uuid, UUIDUtil.expandAndParse(shortUuid));
	}
	
}
