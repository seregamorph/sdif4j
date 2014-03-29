package org.sdif4j.guice;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

/**
 * Guice InjectContext implementation.
 * The @ImplementedBy(GuiceInjectContext.class) annotation on InjectContext interfaces
 * binds it by default.
 * You can also use for binding MODULE instance, or new GuiceInjectContext.Module(),
 * first is preffered.
 *
 * @author Sergey Chernov
 */
@Singleton
public class GuiceInjectContext extends AbstractGuiceInjectContext {
	private final Injector injector;

	// explicit creation is not assumed
	@Inject
	protected GuiceInjectContext(Injector injector) {
		if (injector == null) {
			throw new IllegalArgumentException("injector is null");
		}
		this.injector = injector;
	}

	@Override
	protected Injector getInjector() {
		return injector;
	}

	// This is not required anymore, 'cause of @ImplementedBy(GuiceInjectContext.class) at InjectContext interface
	// declaration.

//	/**
//	 * Module that initializes Guice InjectContext implementation
//	 */
//	public static class Module extends AbstractModule {
//		@Override
//		protected void configure() {
//			bindInjectHelper();
//		}
//
//		protected void bindInjectHelper() {
//			bind(InjectContext.class).to(GuiceInjectContext.class);
//		}
//	}
//
//	/**
//	 * Module instance that initializes Guice InjectContext implementation.
//	 * Note that MODULE instance installation is preffered way to bind InjectContext (instead of Module class
//	 * instance creation in code), when feasible. The reason is that Guice operates Set of modules,
//	 * and a single instance can be installed multiple times and duplicates should be simly ignored.
//	 * <p/>
//	 * In your Module configure method just call
//	 * <p/>
//	 * {@code install(GuiceInjectContext.MODULE); }
//	 */
//	public static final com.google.inject.Module MODULE = new Module();
}
