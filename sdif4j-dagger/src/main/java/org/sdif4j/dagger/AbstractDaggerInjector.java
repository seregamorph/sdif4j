package org.sdif4j.dagger;

import dagger.ObjectGraph;
import org.sdif4j.Injector;

import javax.inject.Provider;

/**
 * Base Dagger Injector implementation
 *
 * @author Sergey Chernov
 */
public abstract class AbstractDaggerInjector implements Injector {
	protected abstract ObjectGraph getObjectGraph();

	public <T> Provider<T> getProvider(final Class<T> clazz) {
		return new Provider<T>() {
			public T get() {
				return getObjectGraph().get(clazz);
			}
		};
	}

	public <T> T getInstance(Class<T> clazz) {
		return getObjectGraph().get(clazz);
	}

	public <T> T getInstance(Class<T> clazz, String name) {
		// maybe private Binding<?> getInjectableTypeBinding via reflection?
		throw new RuntimeException("not implemented yet");
	}

	public void injectMembers(Object instance) {
		getObjectGraph().inject(instance);
	}
}
