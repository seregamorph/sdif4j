package org.sdif4j.testing.cdi;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.sdif4j.Injector;
import org.sdif4j.cdi.CdiInjector;
import org.sdif4j.testing.ITestSingleton;
import org.sdif4j.testing.TestLazySingleton;
import org.sdif4j.testing.TestPrototype;
import org.sdif4j.testing.TestSingleton;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import static org.testng.Assert.*;

/**
 * Simple CDI tests for {@link org.sdif4j.cdi.CdiInjector} inside SE container.
 *
 * @author Pavel Shackih
 */
public class CdiSeTest {

	static final String FOO_BEAN = "fooBean";
	Weld weld;
	WeldContainer container;
	Injector injector;

	@BeforeClass
	public void beforeClass() {
		weld = new Weld();
		container = weld.initialize();
		injector = container.instance().select(Injector.class).get();
	}

	@Test
	public void testInjectorInstance() {
		final Injector injector1 = this.injector;
		assertTrue(injector1 instanceof CdiInjector);
		assertTrue(this.injector.getInstance(Injector.class) == injector1);
	}

	@Test
	public void testNamedService() {
		IService service = injector.getInstance(IService.class, FOO_BEAN);
		assertNotNull(service);
		assertTrue("foo".equals(service.foo()));
	}

	@Test
	public void testNamedString() {
		assertEquals(injector.getInstance(String.class, "key"), "value");
	}

	@Test
	public void testSingleton() {
		assertEquals(TestSingleton.getInstantCount(), 0);
		final ITestSingleton iTestSingleton1 = injector.getInstance(ITestSingleton.class);
		final ITestSingleton iTestSingleton2 = injector.getInstance(ITestSingleton.class);
		final TestSingleton testSingleton1 = injector.getInstance(TestSingleton.class);
		final TestSingleton testSingleton2 = injector.getInstance(TestSingleton.class);
		assertEquals(TestSingleton.getInstantCount(), 1);

		assertNotNull(iTestSingleton1);
		assertTrue(iTestSingleton1 == iTestSingleton2);
		assertTrue(iTestSingleton2 == testSingleton1);
		assertTrue(testSingleton1 == testSingleton2);
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


	@AfterClass
	public void afterClass() {
		weld.shutdown();
	}

	interface IService {
		String foo();
	}

	@Named(FOO_BEAN)
	@SuppressWarnings("unused")
	static class IServiceImpl implements IService {

		public String foo() {
			return "foo";
		}
	}

	@Produces
	@Named("key")
	@SuppressWarnings("unused")
	String keyProduce() {
		return "value";
	}
}
