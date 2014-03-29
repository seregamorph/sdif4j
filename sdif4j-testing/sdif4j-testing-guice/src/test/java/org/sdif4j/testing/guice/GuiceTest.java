package org.sdif4j.testing.guice;

import com.google.inject.Injector;
import org.sdif4j.InjectContext;
import org.sdif4j.guice.GuiceInjectContext;
import org.sdif4j.testing.ITestSingleton;
import org.sdif4j.testing.TestLazySingleton;
import org.sdif4j.testing.TestPrototype;
import org.sdif4j.testing.TestSingleton;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.inject.Named;

import static org.testng.Assert.*;

@Guice(modules = GuiceTestModule.class)
public class GuiceTest {
	@Inject
	@Named("key")
	private String testKey;

	@Inject
	private InjectContext injectContext;

	@Test
	public void testAutoInject() {
		assertEquals(testKey, "value");
	}

	@Test
	public void testInjectContextInstance() {
		final InjectContext injectContext = this.injectContext;
		assertTrue(injectContext instanceof GuiceInjectContext);
		assertTrue(this.injectContext.getInstance(InjectContext.class) == injectContext);

		final Injector injector = this.injectContext.getInstance(Injector.class);
		assertTrue(injector.getInstance(InjectContext.class) == this.injectContext);
	}

	@Test
	public void testNamed() {
		assertEquals(injectContext.getInstance(String.class, "key"), "value");
	}

	@Test
	public void testSingleton() {
		// for Guice default mode
		assertEquals(TestSingleton.getInstantCount(), 0);

		final ITestSingleton is1 = injectContext.getInstance(ITestSingleton.class);
		final ITestSingleton is2 = injectContext.getInstance(ITestSingleton.class);
		final TestSingleton s1 = injectContext.getInstance(TestSingleton.class);
		final TestSingleton s2 = injectContext.getInstance(TestSingleton.class);
		assertEquals(TestSingleton.getInstantCount(), 1);
		assertNotNull(is1);
		assertTrue(is1 == is2);
		assertTrue(is2 == s1);
		assertTrue(s1 == s2);
	}

	@Test
	public void testLazySingleton() {
		assertEquals(TestLazySingleton.getInstantCount(), 0);
		final TestLazySingleton s1 = injectContext.getInstance(TestLazySingleton.class);
		final TestLazySingleton s2 = injectContext.getInstance(TestLazySingleton.class);
		assertEquals(TestLazySingleton.getInstantCount(), 1);
		assertNotNull(s1);
		assertTrue(s1 == s2);
	}

	@Test
	public void testPrototype() {
		final TestPrototype testPrototype1 = injectContext.getInstance(TestPrototype.class);
		final TestPrototype testPrototype2 = injectContext.getInstance(TestPrototype.class);
		assertNotNull(testPrototype1);
		assertNotNull(testPrototype2);
		assertTrue(testPrototype1 != testPrototype2);
	}

	@Test
	public void testInjectMembers() {
		class TestInjectable {
			@Inject
			@Named("key")
			String testKey;
		}
		final TestInjectable testInjectable = new TestInjectable();
		injectContext.injectMembers(testInjectable);
		assertEquals(testInjectable.testKey, "value");
	}
}
