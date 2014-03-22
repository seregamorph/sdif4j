package org.sdif4j.guice;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import javax.enterprise.inject.Alternative;
import javax.inject.Provider;

/**
 * Initializes Guice IOC as Injector implementation
 */
@Singleton
@Alternative // ignored by guice
public class GuiceInjector extends AbstractGuiceInjector {
	private final Provider<Injector> injectorProvider;

	// explicit creation is not assumed
	// note that Provider is used instead of direct Injector instance
	// due to CDI class analyze issues (NoClassDefFoundError).
	@Inject
	protected GuiceInjector(Provider<com.google.inject.Injector> injectorProvider) {
		if (injectorProvider == null) {
			throw new IllegalArgumentException("injectorProvider is null");
		}
		this.injectorProvider = injectorProvider;
	}

	@Override
	protected com.google.inject.Injector getGuiceInjector() {
		return injectorProvider.get();
	}
}
