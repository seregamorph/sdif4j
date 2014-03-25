package org.sdif4j.testing.guice;

import dagger.ObjectGraph;
import org.sdif4j.Injector;
import org.sdif4j.dagger.DaggerInjector;
import org.sdif4j.testing.ITestSingleton;
import org.sdif4j.testing.TestLazySingleton;
import org.sdif4j.testing.TestPrototype;
import org.sdif4j.testing.TestSingleton;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.inject.Named;

import static org.testng.Assert.*;

public class DaggerTest {
	private Injector injector;

	@BeforeClass
	public void initInjector() {
		final ObjectGraph objectGraph = ObjectGraph.create(new DaggerTestModule());
		this.injector = objectGraph.get(Injector.class);
//		this.injector = objectGraph.get(DaggerInjector.class);
	}

	@Test
	public void testInjectorInstance() {
		final Injector injector1 = this.injector;
		assertTrue(injector1 instanceof DaggerInjector);
		assertTrue(this.injector.getInstance(Injector.class) == injector1);

		final ObjectGraph objectGraph = this.injector.getInstance(ObjectGraph.class);
		assertTrue(objectGraph.get(Injector.class) == this.injector);
	}

	@Test
	public void testNamed() {
		assertEquals(injector.getInstance(String.class, "key"), "value");
	}

	@Test
	public void testSingleton() {
		// for Guice default mode
		assertEquals(TestSingleton.getInstantCount(), 0);

		final ITestSingleton is1 = injector.getInstance(ITestSingleton.class);
		final ITestSingleton is2 = injector.getInstance(ITestSingleton.class);
		final TestSingleton s1 = injector.getInstance(TestSingleton.class);
		final TestSingleton s2 = injector.getInstance(TestSingleton.class);
		assertEquals(TestSingleton.getInstantCount(), 1);
		assertNotNull(is1);
		assertTrue(is1 == is2);
		assertTrue(is2 == s1);
		assertTrue(s1 == s2);
	}

	@Test
	public void testLazySingleton() {
		assertEquals(TestLazySingleton.getInstantCount(), 0);
		final TestLazySingleton s1 = injector.getInstance(TestLazySingleton.class);
		final TestLazySingleton s2 = injector.getInstance(TestLazySingleton.class);
		assertEquals(TestLazySingleton.getInstantCount(), 1);
		assertNotNull(s1);
		assertTrue(s1 == s2);
	}

	@Test
	public void testPrototype() {
		final TestPrototype testPrototype1 = injector.getInstance(TestPrototype.class);
		final TestPrototype testPrototype2 = injector.getInstance(TestPrototype.class);
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
		injector.injectMembers(testInjectable);
		assertEquals(testInjectable.testKey, "value");
	}
}
