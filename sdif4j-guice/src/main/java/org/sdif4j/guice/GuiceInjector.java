package org.sdif4j.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.sdif4j.Injector;

/**
 * Guice Injector implementation.
 * The @ImplementedBy(GuiceInjector.class) annotation on Injector interfaces
 * binds it by default.
 * You can also use for binding MODULE instance, or new GuiceInjector.Module(),
 * first is preffered.
 *
 * @author Sergey Chernov
 */
@Singleton
public class GuiceInjector extends AbstractGuiceInjector {
	private final com.google.inject.Injector injector;

	// explicit creation is not assumed
	@Inject
	protected GuiceInjector(com.google.inject.Injector injector) {
		if (injector == null) {
			throw new IllegalArgumentException("injectorProvider is null");
		}
		this.injector = injector;
	}

	@Override
	protected com.google.inject.Injector getGuiceInjector() {
		return injector;
	}

	/**
	 * Module that initializes Guice Injector implementation
	 */
	public static class Module extends AbstractModule {
		@Override
		protected void configure() {
			bindInjectHelper();
		}

		protected void bindInjectHelper() {
			bind(Injector.class).to(GuiceInjector.class);
		}
	}

	/**
	 * Module instance that initializes Guice Injector implementation.
	 * Note that MODULE instance installation is preffered way to bind Injector (instead of Module class
	 * instance creation in code), when feasible. The reason is that Guice operates Set of modules,
	 * and a single instance can be installed multiple times and duplicates should be simly ignored.
	 * <p/>
	 * In your Module configure method just call
	 * <p/>
	 * {@code install(GuiceInjector.MODULE); }
	 */
	public static final com.google.inject.Module MODULE = new Module();
}
