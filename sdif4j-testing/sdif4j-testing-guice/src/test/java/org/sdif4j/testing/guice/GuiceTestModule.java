package org.sdif4j.testing.guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.sdif4j.guice.GuiceInjector;
import org.sdif4j.testing.ITestSingleton;
import org.sdif4j.testing.TestSingleton;

public class GuiceTestModule extends AbstractModule {
	@Override
	protected void configure() {
		install(GuiceInjector.MODULE);

		bind(String.class).annotatedWith(Names.named("key")).toInstance("value");
		bind(ITestSingleton.class).to(TestSingleton.class);
	}
}
