package org.sdif4j.testing.guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.sdif4j.testing.TestEagerSingleton;
import org.sdif4j.testing.TestSingleton;

public class GuiceTestModule extends AbstractModule {
	@Override
	protected void configure() {
		// auto bound via @ImplementedBy
//		install(GuiceInjectContext.MODULE);

		bind(String.class).annotatedWith(Names.named("key")).toInstance("value");

		bind(TestSingleton.class).to(TestEagerSingleton.class).asEagerSingleton();
	}
}
