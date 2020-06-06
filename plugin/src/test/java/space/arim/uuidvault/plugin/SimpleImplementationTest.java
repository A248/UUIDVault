/* 
 * UUIDVault-plugin
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * UUIDVault-plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * UUIDVault-plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with UUIDVault-plugin. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.uuidvault.plugin;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import space.arim.uuidvault.api.UUIDVaultPriority;
import space.arim.uuidvault.api.UUIDVaultRegistration;

public class SimpleImplementationTest {

	private static SimpleImplementation impl;
	
	@BeforeAll
	public static void setup() {
		impl = new TestingImplementation();
	}
	
	@Test
	public void testDuplicateRegistrations() {
		UUIDVaultRegistration nullResolver = impl.register(new NullResolver(), NullResolver.class, UUIDVaultPriority.NORMAL, null);
		UUIDVaultRegistration emptyResolver = impl.register(new EmptyResolver(), EmptyResolver.class, UUIDVaultPriority.NORMAL, "");
		assertNotNull(nullResolver, "Original registration should be nonnull");
		assertNotNull(emptyResolver, "Original registration should be nonnull");

		UUIDVaultRegistration duplicateNullResolver = impl.register(new NullResolver(), NullResolver.class, UUIDVaultPriority.NORMAL, null);
		UUIDVaultRegistration duplicateEmptyResolver = impl.register(new EmptyResolver(), EmptyResolver.class, UUIDVaultPriority.NORMAL, "");
		assertNull(duplicateNullResolver, "Duplicate registration should be null");
		assertNull(duplicateEmptyResolver, "Duplicate registration should be null");
	}
	
}
