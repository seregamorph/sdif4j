package org.sdif4j.guice;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Initializes Guice IOC as Injector implementation
 */
@Singleton
public class GuiceInjector extends AbstractGuiceInjector {
	private final com.google.inject.Injector injector;

	@Inject
	protected GuiceInjector(com.google.inject.Injector injector) {
		if (injector == null) {
			throw new IllegalArgumentException("injector is null");
		}
		this.injector = injector;
	}

	@Override
	protected com.google.inject.Injector getGuiceInjector() {
		return injector;
	}
}
