package org.sdif4j.testing.guice;

import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * This test just shows the com.google.inject.Injector inheritance via @Guice annotation in super-class.
 * <p/>
 * Note, that this class should have exactly the same Injector and InjectContext instances.
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
	public void testEagerSingleton() {
		// skip
	}

	@Override
	public void testLazySingleton() {
		// skip
	}
}
