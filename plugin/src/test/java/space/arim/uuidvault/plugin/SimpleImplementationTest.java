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

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import space.arim.uuidvault.api.UUIDVaultRegistration;

public class SimpleImplementationTest {
	
	private SimpleImplementation impl;
	
	@BeforeEach
	public void setup() {
		impl = new TestingImplementation();
	}
	
	private byte randomPriority() {
		return (byte) (ThreadLocalRandom.current().nextInt(-2*Byte.MIN_VALUE) + Byte.MIN_VALUE);
	}
	
	@Test
	public void testDuplicateRegistrationsAndNullFutures() {
		UUIDVaultRegistration nullResolver = impl.register(new NullResolver(), NullResolver.class, randomPriority(), null);
		UUIDVaultRegistration emptyResolver = impl.register(new EmptyResolver(), EmptyResolver.class, randomPriority(), "");
		assertNotNull(nullResolver, "Original registration should be nonnull");
		assertNotNull(emptyResolver, "Original registration should be nonnull");

		assertNotNull(impl.resolve("A248"), "#resolve should return a completed null instead of null future");
		assertNotNull(impl.resolve(new UUID(0, 0)), "#resolve should return a completed null instead of null future");

		UUIDVaultRegistration duplicateNullResolver = impl.register(new NullResolver(), NullResolver.class, randomPriority(), null);
		UUIDVaultRegistration duplicateEmptyResolver = impl.register(new EmptyResolver(), EmptyResolver.class, randomPriority(), "");
		assertNull(duplicateNullResolver, "Duplicate registration should be null");
		assertNull(duplicateEmptyResolver, "Duplicate registration should be null");
		
		assertTrue(nullResolver.unregister(), "Original registration should unregister properly");
		assertTrue(emptyResolver.unregister(), "Original registration should unregister properly");
	}
	
	@Test
	public void testBasicResolutionAndUnregister() {
		UUID uuid = UUID.fromString("ed5f12cd-6007-45d9-a4b9-940524ddaecf");
		String name = "A248";
		SingleImmediateResolver resolver = new SingleImmediateResolver(uuid, name);
		UUIDVaultRegistration registration = impl.register(resolver, SingleImmediateResolver.class, randomPriority(), "");
		assertEquals(uuid, impl.resolveImmediately(name), "Must immediately resolve to correct uuid");
		assertEquals(name, impl.resolveImmediately(uuid), "Must immediately resolve to correct name");
		assertEquals(uuid, impl.resolve(name).getNow(null), "Must immediately resolve to correct uuid");
		assertEquals(name, impl.resolve(uuid).getNow(null), "Must immediately resolve to correct name");
		registration.unregister();
		assertNull(impl.resolveImmediately(name), "Must not query unregistered resolver for uuid");
		assertNull(impl.resolveImmediately(uuid), "Must not query unregistered resolver for name");
		assertNull(impl.resolve(name).join(), "Must not query unregistered resolver for uuid");
		assertNull(impl.resolve(uuid).join(), "Must not query unregistered resolver for name");
	}
	
}
