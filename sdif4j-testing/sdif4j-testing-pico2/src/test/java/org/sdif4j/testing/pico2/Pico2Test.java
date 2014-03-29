package org.sdif4j.testing.pico2;

import org.picocontainer.Characteristics;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.picocontainer.PicoContainer;
import org.picocontainer.behaviors.OptInCaching;
import org.picocontainer.injectors.AnnotatedFieldInjection;
import org.picocontainer.injectors.CompositeInjection;
import org.sdif4j.InjectContext;
import org.sdif4j.pico2.Pico2InjectContext;
import org.sdif4j.testing.TestEagerSingleton;
import org.sdif4j.testing.TestLazySingleton;
import org.sdif4j.testing.TestPrototype;
import org.sdif4j.testing.TestSingleton;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.inject.Named;

import static org.testng.Assert.*;

public class Pico2Test {
	private InjectContext injectContext;

	@BeforeClass
	public void beforeClass() {
		final MutablePicoContainer picoContainer = createPicoContatiner();

		final PicoContainer picoContainer1 = picoContainer.getComponent(PicoContainer.class);
		final PicoContainer picoContainer2 = picoContainer.getComponent(PicoContainer.class);
		assertTrue(picoContainer == picoContainer1);
		assertTrue(picoContainer1 == picoContainer2);

		final Pico2InjectContext pico2InjectContext = picoContainer.getComponent(Pico2InjectContext.class);
//		assertTrue(pico2InjectContext.getPicoContainerAccessor() == picoContainer);

		final Pico2InjectContext pico2InjectContext1 = picoContainer.getComponent(Pico2InjectContext.class);
		assertTrue(pico2InjectContext == pico2InjectContext1);

		this.injectContext = picoContainer.getComponent(InjectContext.class);
		final InjectContext injectContext = picoContainer.getComponent(InjectContext.class);
		assertTrue(injectContext == this.injectContext);
		assertTrue(pico2InjectContext == this.injectContext);
	}

	private MutablePicoContainer createPicoContatiner() {
		final MutablePicoContainer picoContainer = new PicoBuilder()
				.withBehaviors(new OptInCaching())
				.withComponentFactory(new CompositeInjection(new AnnotatedFieldInjection(Inject.class)))
				.build();

		picoContainer.addComponent(PicoContainer.class, picoContainer);
		picoContainer.as(Characteristics.CACHE).addComponent(Pico2InjectContext.class);

		picoContainer.as(Characteristics.CACHE).addComponent(TestLazySingleton.class);
		picoContainer.as(Characteristics.CACHE).addComponent(TestEagerSingleton.class);
		picoContainer.addComponent(TestPrototype.class);

		picoContainer.addComponent("key", "value");

		return picoContainer;
	}

	@Test
	public void testInjectContextInstance() {
		final InjectContext injectContext = this.injectContext;
		assertTrue(injectContext instanceof Pico2InjectContext);
		assertTrue(this.injectContext.getInstance(InjectContext.class) == injectContext);
	}

	@Test
	public void testNamed() {
		assertEquals(injectContext.getInstance(String.class, "key"), "value");
	}

	@Test
	public void testEagerSingleton() {
		// todo 1
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
}
