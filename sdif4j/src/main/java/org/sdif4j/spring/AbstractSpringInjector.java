package org.sdif4j.spring;

import org.sdif4j.Injector;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.ApplicationContext;

import javax.inject.Provider;

public abstract class AbstractSpringInjector extends Injector {
	protected abstract ApplicationContext getContext();

	@Override
	public <T> Provider<T> getProvider(final Class<T> clazz) {
		return new Provider<T>() {
			public T get() {
				return getInstance(clazz);
			}
		};
	}

	@Override
	public final <T> T getInstance(Class<T> clazz) {
		return getContext().getBean(clazz);
	}

	@Override
	public final <T> T getInstance(Class<T> clazz, String name) {
		return getContext().getBean(name, clazz);
	}

	@Override
	public final void injectMembers(Object instance) {
		final AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
		bpp.setBeanFactory(getContext().getAutowireCapableBeanFactory());
		bpp.processInjection(instance);
	}
}
