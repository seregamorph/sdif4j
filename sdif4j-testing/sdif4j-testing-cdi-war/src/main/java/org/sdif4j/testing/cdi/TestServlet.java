package org.sdif4j.testing.cdi;

import org.sdif4j.InjectContext;
import org.sdif4j.cdi.CdiInjectContext;
import org.sdif4j.testing.ITestSingleton;
import org.sdif4j.testing.TestLazySingleton;
import org.sdif4j.testing.TestPrototype;
import org.sdif4j.testing.TestSingleton;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.testng.Assert.*;

/**
 * TODO: this is simple manual test
 * required to be refactored as standalone testng-based
 */
public class TestServlet extends HttpServlet {
	@Inject
	private BeanManager beanManager;
	@Inject
	private InjectContext injectContext;
	@Inject
	private InjectContext injectContext1;
	@Inject
	@Named("key")
	private String testKey;

	private final AtomicBoolean firstCall = new AtomicBoolean(true);

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	protected synchronized void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		final boolean first = firstCall.compareAndSet(true, false);

		resp.setContentType("text/plain");

		final PrintWriter pw = resp.getWriter();
		try {
			pw.println("testAutoInject");
			testAutoInject();

			pw.println("testInjectContextInstance");
			testInjectContextInstance();

			pw.println("testNamed");
			testNamed();

			pw.println("testSingleton");
			testSingleton(first);

			pw.println("testLazySingleton");
			testLazySingleton(first);

			pw.println("testPrototype");
			testPrototype();

			pw.println("testInjectMembers");
			testInjectMembers();
		} catch (Throwable e) {
			e.printStackTrace(pw);
		}
	}

	//	@Test
	public void testAutoInject() {
		assertEquals(testKey, "value");
	}

	//	@Test
	public void testInjectContextInstance() {
		final InjectContext injectContext = this.injectContext;
		assertTrue(injectContext instanceof CdiInjectContext);
		assertTrue(this.injectContext.getInstance(InjectContext.class) == injectContext);
		assertTrue(this.injectContext1 == injectContext);

		final BeanManager beanManager = this.injectContext.getInstance(BeanManager.class);
		assertTrue(this.beanManager == beanManager);

		final BeanManager lookup = CdiInjectContext.lookupBeanManager();
//		assertTrue(lookup.equals(beanManager));
	}

	//	@Test
	public void testNamed() {
		assertEquals(injectContext.getInstance(String.class, "key"), "value");
	}

	//	@Test
	public void testSingleton(boolean first) {
		assertEquals(TestSingleton.getInstantCount(), first ? 0 : 1);

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

	//	@Test
	public void testLazySingleton(boolean first) {
		assertEquals(TestLazySingleton.getInstantCount(), first ? 0 : 1);
		final TestLazySingleton s1 = injectContext.getInstance(TestLazySingleton.class);
		final TestLazySingleton s2 = injectContext.getInstance(TestLazySingleton.class);
		assertEquals(TestLazySingleton.getInstantCount(), 1);
		assertNotNull(s1);
		assertTrue(s1 == s2);
	}

	//	@Test
	public void testPrototype() {
		final TestPrototype testPrototype1 = injectContext.getInstance(TestPrototype.class);
		final TestPrototype testPrototype2 = injectContext.getInstance(TestPrototype.class);
		assertNotNull(testPrototype1);
		assertNotNull(testPrototype2);
		assertTrue(testPrototype1 != testPrototype2);
	}

	//	@Test
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
