package org.sdif4j.testing.guice;

import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * This test just shows the InjectContext inheritance via @Guice annotation in super-class.
 * <p/>
 * Note, that this class should have exactly the same InjectContext (both guice and sdif4j) instance.
 */
public class GuiceInheritTest extends GuiceTest {
	@Inject
	@Named("key")
	private String testKey;

	@Test
	public void testInherit() {
		Assert.assertEquals(testKey, "value");
	}

	@Override
	public void testSingleton() {
		// skip
	}

	@Override
	public void testLazySingleton() {
		// skip
	}
}
