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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

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
	public void testSamePriority() {
		byte priority = randomPriority();
		UUIDVaultRegistration nullResolver = NullResolver.register(impl, priority);
		UUIDVaultRegistration emptyResolver = EmptyResolver.register(impl, priority);
		assertNotNull(nullResolver, "Original registration should be nonnull");
		assertNotNull(emptyResolver, "Original registration should be nonnull");
	}
	
	@Test
	public void testDuplicateRegistrationsAndNullFutures() {
		UUIDVaultRegistration nullResolver = NullResolver.register(impl, randomPriority());
		UUIDVaultRegistration emptyResolver = EmptyResolver.register(impl, randomPriority());
		assertNotNull(nullResolver, "Original registration should be nonnull");
		assertNotNull(emptyResolver, "Original registration should be nonnull");

		assertNotNull(impl.resolve("A248"), "#resolve should return a completed null instead of null future");
		assertNotNull(impl.resolve(new UUID(0, 0)), "#resolve should return a completed null instead of null future");

		UUIDVaultRegistration duplicateNullResolver = NullResolver.register(impl, randomPriority());
		UUIDVaultRegistration duplicateEmptyResolver = EmptyResolver.register(impl, randomPriority());
		assertNull(duplicateNullResolver, "Duplicate registration should be null");
		assertNull(duplicateEmptyResolver, "Duplicate registration should be null");
		
		assertTrue(impl.unregister(nullResolver), "Original registration should unregister properly");
		assertTrue(impl.unregister(emptyResolver), "Original registration should unregister properly");
	}
	
	@Test
	public void testBasicResolutionAndUnregister() {
		UUID uuid = UUID.fromString("ed5f12cd-6007-45d9-a4b9-940524ddaecf");
		String name = "A248";
		SingleImmediateResolver resolver = new SingleImmediateResolver(uuid, name);
		UUIDVaultRegistration registration = impl.register(resolver, SingleImmediateResolver.class, randomPriority(), "SingleImmediateResolver");
		assertEquals(uuid, impl.resolveImmediately(name), "Must immediately resolve to correct uuid");
		assertEquals(name, impl.resolveImmediately(uuid), "Must immediately resolve to correct name");
		assertEquals(uuid, impl.resolve(name).getNow(null), "Must immediately resolve to correct uuid");
		assertEquals(name, impl.resolve(uuid).getNow(null), "Must immediately resolve to correct name");
		impl.unregister(registration);
		assertNull(impl.resolveImmediately(name), "Must not query unregistered resolver for uuid");
		assertNull(impl.resolveImmediately(uuid), "Must not query unregistered resolver for name");
		assertNull(impl.resolve(name).join(), "Must not query unregistered resolver for uuid");
		assertNull(impl.resolve(uuid).join(), "Must not query unregistered resolver for name");
	}
	
	@Test
	public void testConcurrentRegistrationUnregistration() {
		final int numThreads = 20;
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);

		final CountDownLatch latch = new CountDownLatch(1);
		for (int n = 0; n < numThreads; n++) {

			executor.execute(() -> {
				NullResolver resolver = new NullResolver();
				UUIDVaultRegistration regis = null;
				try {
					latch.await();
				} catch (InterruptedException ex) {
					fail(ex);
				}
				for (int iteration = 0; iteration < 30; iteration++) {
					if (regis == null) {
						regis = impl.register(resolver, NullResolver.class, randomPriority(), "NullResolver");
					} else {
						impl.unregister(regis);
						regis = null;
					}
				}
			});
		}
		latch.countDown();
		try {
			executor.shutdown();
			assertTrue(executor.awaitTermination(10L, TimeUnit.SECONDS), "Executor service used for testing should not timeout");
		} catch (InterruptedException ex) {
			fail(ex);
		}
	}
	
}
