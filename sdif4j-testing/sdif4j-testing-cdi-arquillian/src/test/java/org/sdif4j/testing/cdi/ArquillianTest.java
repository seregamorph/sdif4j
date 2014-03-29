package org.sdif4j.testing.cdi;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.sdif4j.InjectContext;
import org.sdif4j.cdi.CdiInjectContext;
import org.sdif4j.testing.ITestSingleton;
import org.sdif4j.testing.TestLazySingleton;
import org.sdif4j.testing.TestPrototype;
import org.sdif4j.testing.TestSingleton;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Simple CDI tests for {@link org.sdif4j.cdi.CdiInjectContext} inside {@code Arquillian} container.
 *
 * @author Pavel Shackih
 */
public class ArquillianTest extends Arquillian {

	static final String FOO_BEAN = "fooBean";

	@Deployment
	public static JavaArchive createDeployment() {
		return ShrinkWrap.create(JavaArchive.class)
				// we need to build our own jar for container
				.addPackage("org.sdif4j.cdi")
				.addClass(InjectContext.class)
				.addPackage("org.sdif4j.testing")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Inject
	InjectContext injectContext;

	@Test
	public void testInjectContextInstance() {
		final InjectContext injectContext = this.injectContext;
		Assert.assertTrue(injectContext instanceof CdiInjectContext);
		Assert.assertTrue(this.injectContext.getInstance(InjectContext.class) == injectContext);
	}

	@Test
	public void testNamedService() {
		IService service = injectContext.getInstance(IService.class, FOO_BEAN);
		Assert.assertNotNull(service);
		Assert.assertTrue("foo".equals(service.foo()));
	}

	@Test
	public void testNamedString() {
		Assert.assertEquals(injectContext.getInstance(String.class, "key"), "value");
	}

	@Test
	public void testSingleton() {
		Assert.assertEquals(TestSingleton.getInstantCount(), 0);
		final ITestSingleton iTestSingleton1 = injectContext.getInstance(ITestSingleton.class);
		final ITestSingleton iTestSingleton2 = injectContext.getInstance(ITestSingleton.class);
		final TestSingleton testSingleton1 = injectContext.getInstance(TestSingleton.class);
		final TestSingleton testSingleton2 = injectContext.getInstance(TestSingleton.class);
		Assert.assertEquals(TestSingleton.getInstantCount(), 1);

		Assert.assertNotNull(iTestSingleton1);
		Assert.assertTrue(iTestSingleton1 == iTestSingleton2);
		Assert.assertTrue(iTestSingleton2 == testSingleton1);
		Assert.assertTrue(testSingleton1 == testSingleton2);
	}

	@Test
	public void testLazySingleton() {
		Assert.assertEquals(TestLazySingleton.getInstantCount(), 0);
		final TestLazySingleton s1 = injectContext.getInstance(TestLazySingleton.class);
		final TestLazySingleton s2 = injectContext.getInstance(TestLazySingleton.class);
		Assert.assertEquals(TestLazySingleton.getInstantCount(), 1);
		Assert.assertNotNull(s1);
		Assert.assertTrue(s1 == s2);
	}

	@Test
	public void testPrototype() {
		final TestPrototype testPrototype1 = injectContext.getInstance(TestPrototype.class);
		final TestPrototype testPrototype2 = injectContext.getInstance(TestPrototype.class);
		Assert.assertNotNull(testPrototype1);
		Assert.assertNotNull(testPrototype2);
		Assert.assertTrue(testPrototype1 != testPrototype2);
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
		Assert.assertEquals(testInjectable.testKey, "value");
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
