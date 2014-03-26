package org.sdif4j.guice;

import com.google.inject.Key;
import com.google.inject.name.Names;
import org.sdif4j.Injector;

import javax.inject.Provider;

/**
 * Base Guice Injector implementation
 *
 * @author Sergey Chernov
 */
public abstract class AbstractGuiceInjector implements Injector {
	protected abstract com.google.inject.Injector getGuiceInjector();

	public final <T> Provider<T> getProvider(Class<T> clazz) {
		return getGuiceInjector().getProvider(clazz);
	}

	public final <T> T getInstance(Class<T> clazz) {
		return getGuiceInjector().getInstance(clazz);
	}

	public final <T> T getInstance(Class<T> clazz, String name) {
		return getGuiceInjector().getInstance(Key.get(clazz, Names.named(name)));
	}

	public final void injectMembers(Object instance) {
		getGuiceInjector().injectMembers(instance);
	}
}
