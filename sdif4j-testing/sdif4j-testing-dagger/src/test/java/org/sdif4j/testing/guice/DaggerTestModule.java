package org.sdif4j.testing.guice;

import dagger.Module;
import dagger.Provides;
import org.sdif4j.testing.ITestSingleton;
import org.sdif4j.testing.TestSingleton;

import javax.inject.Named;

@Module(complete = true,
		library = true)
public class DaggerTestModule {
	@Provides
	@Named("key")
	public String getKey() {
		return "value";
	}

	@Provides
	public ITestSingleton getTestSingleton() {
		return new TestSingleton();
	}

//	@Provides
//	public Injector getInjector() {
//		return new DaggerInjector() {
//		};
//	}
}
