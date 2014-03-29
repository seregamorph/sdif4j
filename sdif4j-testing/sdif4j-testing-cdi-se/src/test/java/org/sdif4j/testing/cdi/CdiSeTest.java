package org.sdif4j.testing.cdi;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.sdif4j.InjectContext;
import org.sdif4j.cdi.CdiInjectContext;
import org.sdif4j.testing.TestEagerSingleton;
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
 * Simple CDI tests for {@link org.sdif4j.cdi.CdiInjectContext} inside SE container.
 *
 * @author Pavel Shackih
 */
public class CdiSeTest {

	static final String FOO_BEAN = "fooBean";
	Weld weld;
	WeldContainer container;
	InjectContext injectContext;

	@BeforeClass
	public void beforeClass() {
		weld = new Weld();
		container = weld.initialize();
		injectContext = container.instance().select(InjectContext.class).get();
	}

	@Test
	public void testInjectContextInstance() {
		final InjectContext injectContext = this.injectContext;
		assertTrue(injectContext instanceof CdiInjectContext);
		assertTrue(this.injectContext.getInstance(InjectContext.class) == injectContext);
	}

	@Test
	public void testNamedService() {
		IService service = injectContext.getInstance(IService.class, FOO_BEAN);
		assertNotNull(service);
		assertTrue("foo".equals(service.foo()));
	}

	@Test
	public void testNamedString() {
		assertEquals(injectContext.getInstance(String.class, "key"), "value");
	}

	@Test
	public void testSingleton() {
		assertEquals(TestEagerSingleton.getInstantCount(), 0);
		final TestSingleton testSingleton1 = injectContext.getInstance(TestSingleton.class);
		final TestSingleton testSingleton2 = injectContext.getInstance(TestSingleton.class);
		final TestEagerSingleton testEagerSingleton1 = injectContext.getInstance(TestEagerSingleton.class);
		final TestEagerSingleton testEagerSingleton2 = injectContext.getInstance(TestEagerSingleton.class);
		assertEquals(TestEagerSingleton.getInstantCount(), 1);

		assertNotNull(testSingleton1);
		assertTrue(testSingleton1 == testSingleton2);
		assertTrue(testSingleton2 == testEagerSingleton1);
		assertTrue(testEagerSingleton1 == testEagerSingleton2);
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
