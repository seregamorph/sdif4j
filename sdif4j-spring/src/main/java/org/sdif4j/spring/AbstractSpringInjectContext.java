package org.sdif4j.spring;

import org.sdif4j.InjectContext;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.ApplicationContext;

import javax.inject.Provider;

/**
 * Base Spring InjectContext implementation
 *
 * @author Sergey Chernov
 */
public abstract class AbstractSpringInjectContext implements InjectContext {
	protected abstract ApplicationContext getContext();

	public <T> Provider<T> getProvider(final Class<T> clazz) {
		return new Provider<T>() {
			public T get() {
				return getInstance(clazz);
			}
		};
	}

	public final <T> T getInstance(Class<T> clazz) {
		return getContext().getBean(clazz);
	}

	public final <T> T getInstance(Class<T> clazz, String name) {
		return getContext().getBean(name, clazz);
	}

	public final void injectMembers(Object instance) {
		final AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
		bpp.setBeanFactory(getContext().getAutowireCapableBeanFactory());
		bpp.processInjection(instance);
	}
}
