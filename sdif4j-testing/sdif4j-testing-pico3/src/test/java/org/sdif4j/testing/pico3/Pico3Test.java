package org.sdif4j.testing.pico3;

import com.picocontainer.Characteristics;
import com.picocontainer.MutablePicoContainer;
import com.picocontainer.PicoBuilder;
import com.picocontainer.PicoContainer;
import com.picocontainer.behaviors.OptInCaching;
import com.picocontainer.injectors.AnnotatedFieldInjection;
import com.picocontainer.injectors.CompositeInjection;
import org.sdif4j.Injector;
import org.sdif4j.pico3.Pico3Injector;
import org.sdif4j.testing.ITestSingleton;
import org.sdif4j.testing.TestLazySingleton;
import org.sdif4j.testing.TestPrototype;
import org.sdif4j.testing.TestSingleton;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.inject.Named;

import static org.testng.Assert.*;

public class Pico3Test {
	private Injector injector;

	@BeforeClass
	public void beforeClass() {
		final MutablePicoContainer picoContainer = createPicoContatiner();

		final PicoContainer picoContainer1 = picoContainer.getComponent(PicoContainer.class);
		final PicoContainer picoContainer2 = picoContainer.getComponent(PicoContainer.class);
		assertTrue(picoContainer == picoContainer1);
		assertTrue(picoContainer1 == picoContainer2);

		final Pico3Injector Pico3Injector = picoContainer.getComponent(Pico3Injector.class);
		assertTrue(Pico3Injector.getPicoContainerAccessor() == picoContainer);

		final Pico3Injector Pico3Injector1 = picoContainer.getComponent(Pico3Injector.class);
		// fail
		assertTrue(Pico3Injector == Pico3Injector1);

		this.injector = picoContainer.getComponent(Injector.class);
		final Injector injector1 = picoContainer.getComponent(Injector.class);
		assertTrue(injector1 == this.injector);
		assertTrue(Pico3Injector == this.injector);
	}

	private MutablePicoContainer createPicoContatiner() {
		final MutablePicoContainer picoContainer = new PicoBuilder()
				.withBehaviors(new OptInCaching())
				.withComponentFactory(new CompositeInjection(new AnnotatedFieldInjection(Inject.class)))
				.build();

		picoContainer.addComponent(PicoContainer.class, picoContainer);
		picoContainer.as(Characteristics.CACHE).addComponent(Pico3Injector.class);

		picoContainer.as(Characteristics.CACHE).addComponent(TestLazySingleton.class);
		picoContainer.as(Characteristics.CACHE).addComponent(TestSingleton.class);
		picoContainer.addComponent(TestPrototype.class);

		picoContainer.addComponent("key", "value");

		return picoContainer;
	}

	@Test
	public void testInjectorInstance() {
		final Injector injector1 = this.injector;
		assertTrue(injector1 instanceof Pico3Injector);
		assertTrue(this.injector.getInstance(Injector.class) == injector1);
	}

	@Test
	public void testNamed() {
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
}
