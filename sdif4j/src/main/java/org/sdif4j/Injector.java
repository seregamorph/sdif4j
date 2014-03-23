package org.sdif4j;

import org.sdif4j.guice.GuiceInjector;

import javax.inject.Provider;

/**
 * Inject IOC encapsulation class.
 * In guice IOC by default implemented by GuiceInjector (no additional binding required).
 * In Spring IOC by default implemented by SpringInjector ("org.sdif4j.spring" auto-scan required).
 * <p/>
 * Note, that @ImplementedBy is ignored by Spring IOC (does not matter if Guice presents in classpath or not).
 *
 * @author Sergey Chernov
 */
@com.google.inject.ImplementedBy(GuiceInjector.class)
public abstract class Injector {
	/**
	 * Attribute name for Context binding, e.g. ServletContext
	 */
	public static final String CONTEXT_ATTRIBUTE = Injector.class.getName();

	public abstract <T> Provider<T> getProvider(Class<T> clazz);

	public abstract <T> T getInstance(Class<T> clazz);

	public abstract <T> T getInstance(Class<T> clazz, String name);

	public abstract void injectMembers(Object instance);
}
