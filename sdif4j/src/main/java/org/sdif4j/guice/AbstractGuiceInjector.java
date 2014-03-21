package org.sdif4j.guice;

import com.google.inject.Key;
import com.google.inject.name.Names;
import org.sdif4j.Injector;

import javax.inject.Provider;

public abstract class AbstractGuiceInjector extends Injector {
	protected abstract com.google.inject.Injector getGuiceInjector();

	@Override
	public final <T> Provider<T> getProvider(Class<T> clazz) {
		return getGuiceInjector().getProvider(clazz);
	}

	@Override
	public final <T> T getInstance(Class<T> clazz) {
		return getGuiceInjector().getInstance(clazz);
	}

	@Override
	public final <T> T getInstance(Class<T> clazz, String name) {
		return getGuiceInjector().getInstance(Key.get(clazz, Names.named(name)));
	}

	@Override
	public final void injectMembers(Object instance) {
		getGuiceInjector().injectMembers(instance);
	}
}
