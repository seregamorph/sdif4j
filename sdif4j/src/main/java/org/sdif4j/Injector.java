package org.sdif4j;

import org.sdif4j.guice.GuiceInjector;

import javax.inject.Provider;

/**
 * Inject IOC encapsulation class.
 *
 * In guice IOC by default implemented by {@code org.sdif4j.guice.GuiceInjector}
 * (no additional binding required, auto-bound with @ImplementedBy annotation).
 *
 * In Spring IOC by default implemented by {@code org.sdif4j.spring.SpringInjector}
 * ("org.sdif4j.spring" auto-scan required).
 *
 * In CDI IOC by default implemented by {@code org.sdif4j.cdi.CdiInjector}
 * (no additional binding required, auto-bound with @Default annotation)
 *
 * General notice: when feasible, do not use this class and it's methods,
 * use javax.inject.* annotations instead.
 *
 * Note, that @ImplementedBy is ignored by Spring and CDI IOC
 * (does not matter if Guice presents in classpath or not).
 *
 * @author Sergey Chernov
 */
@com.google.inject.ImplementedBy(GuiceInjector.class)
public abstract class Injector {
	/**
	 * Attribute name for Context binding, e.g. ServletContext
	 */
	public static final String CONTEXT_ATTRIBUTE = Injector.class.getName();

	/**
	 * Get class instance Provider via DI implementation.
	 *
	 * @param clazz
	 * @param <T>
	 * @return class instance Provider
	 */
	public abstract <T> Provider<T> getProvider(Class<T> clazz);

	/**
	 * Get class instance via DI implementation.
	 *
	 * @param clazz
	 * @param <T>
	 * @return class instance
	 */
	public abstract <T> T getInstance(Class<T> clazz);

	/**
	 * Get class named instance via DI implementation.
	 * In general, naming is bound with @javax.inject.Named annotation
	 *
	 * @param clazz
	 * @param <T>
	 * @return named class instance
	 */
	public abstract <T> T getInstance(Class<T> clazz, String name);

	/**
	 * Inject instance members
	 *
	 * @param instance
	 */
	public abstract void injectMembers(Object instance);
}
