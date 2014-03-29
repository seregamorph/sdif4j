package org.sdif4j.testing;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class InjectConstructorSingleton {
	private final TestSingleton testSingleton;

	@Inject
	public InjectConstructorSingleton(TestSingleton testSingleton) {
		this.testSingleton = testSingleton;
	}

	public TestSingleton getTestSingleton() {
		return testSingleton;
	}
}
