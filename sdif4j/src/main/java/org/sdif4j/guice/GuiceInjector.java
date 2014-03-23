package org.sdif4j.guice;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import javax.inject.Provider;

/**
 * Guice Injector implementation.
 * This implementation is bound auto via @ImplementedBy Injector annotation.
 *
 * @author Sergey Chernov
 */
@Singleton
@javax.enterprise.inject.Alternative // note: ignored by guice
public class GuiceInjector extends AbstractGuiceInjector {
	private final Provider<Injector> injectorProvider;

	// explicit creation is not assumed
	// note that Provider is used instead of direct Injector instance
	// due to CDI class analyze issues (NoClassDefFoundError: com.google.inject.Injector).
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
