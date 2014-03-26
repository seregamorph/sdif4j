package org.sdif4j;

import com.google.inject.ImplementedBy;
import org.sdif4j.guice.GuiceInjector;

import javax.inject.Provider;

/**
 * Inject IOC encapsulation class.
 * <p/>
 * In guice IOC by default implemented by {@code org.sdif4j.guice.GuiceInjector}
 * (no additional binding required, auto-bound with @ImplementedBy annotation).
 * <p/>
 * In Spring IOC by default implemented by {@code org.sdif4j.spring.SpringInjector}
 * ("org.sdif4j.spring" auto-scan required or simply add
 * &lt;import resource="classpath:spring-sdif4j-context.xml"/&gt; to your context.xml).
 * <p/>
 * In CDI IOC by default implemented by {@code org.sdif4j.cdi.CdiInjector}
 * (no additional binding required, auto-bound with @Default annotation)
 * <p/>
 * General notice: when feasible, do not use this class and it's methods,
 * use javax.inject.* annotations instead.
 *
 * @author Sergey Chernov
 */
// note, that this is GuiceInjector from sdif4j-guice-stub, not sdif4j-guice, just for compilation.
// it's a hack for default binding and ability to separate maven modules.
// that's why it is highlighted in your IDE
@ImplementedBy(GuiceInjector.class)
public interface Injector {
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
	public <T> Provider<T> getProvider(Class<T> clazz);

	/**
	 * Get class instance via DI implementation.
	 *
	 * @param clazz
	 * @param <T>
	 * @return class instance
	 */
	public <T> T getInstance(Class<T> clazz);

	/**
	 * Get class named instance via DI implementation.
	 * In general, naming is bound with @javax.inject.Named annotation
	 *
	 * @param clazz
	 * @param <T>
	 * @return named class instance
	 */
	public <T> T getInstance(Class<T> clazz, String name);

	/**
	 * Inject instance members
	 *
	 * @param instance
	 */
	public void injectMembers(Object instance);
}
