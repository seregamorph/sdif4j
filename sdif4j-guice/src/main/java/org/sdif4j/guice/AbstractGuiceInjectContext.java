package org.sdif4j.guice;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import org.sdif4j.InjectContext;

import javax.inject.Provider;

/**
 * Base Guice InjectContext implementation
 *
 * @author Sergey Chernov
 */
public abstract class AbstractGuiceInjectContext implements InjectContext {
	protected abstract Injector getInjector();

	public final <T> Provider<T> getProvider(Class<T> clazz) {
		return getInjector().getProvider(clazz);
	}

	public final <T> T getInstance(Class<T> clazz) {
		return getInjector().getInstance(clazz);
	}

	public final <T> T getInstance(Class<T> clazz, String name) {
		return getInjector().getInstance(Key.get(clazz, Names.named(name)));
	}

	public final void injectMembers(Object instance) {
		getInjector().injectMembers(instance);
	}
}
