package org.sdif4j.cdi;

import org.sdif4j.InjectContext;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Set;

/**
 * Base CDI InjectContext implementation
 *
 * @author Sergey Chernov
 */
public abstract class AbstractCdiInjectContext implements InjectContext {
	protected abstract BeanManager getBeanManager();

	protected abstract CreationalContext getCreationalContext();

	public <T> T getInstance(Class<T> clazz) {
		final BeanManager bm = getBeanManager();
		final Bean<?> bean = getBean(bm, clazz);
		return getReference(bm, clazz, bean);
	}

	public <T> Provider<T> getProvider(final Class<T> clazz) {
		final BeanManager bm = getBeanManager();
		final Bean<?> bean = getBean(bm, clazz);
		return new Provider<T>() {
			public T get() {
				return getReference(bm, clazz, bean);
			}
		};
	}

	public <T> T getInstance(Class<T> clazz, String name) {
		final BeanManager bm = getBeanManager();
		final Bean<?> bean = getBean(bm, clazz, name);
		return getReference(bm, clazz, bean);
	}

	@SuppressWarnings("unchecked")
	public void injectMembers(Object instance) {
		final BeanManager bm = getBeanManager();

		final CreationalContext creationalContext = getCreationalContext();
		InjectionTarget<Object> injectionTarget = (InjectionTarget<Object>) bm.createInjectionTarget(
				bm.createAnnotatedType(instance.getClass()));
		injectionTarget.inject(instance, creationalContext);
	}

	private <T> T getReference(BeanManager bm, Class<T> clazz, Bean<?> bean) {
		final CreationalContext<?> ctx = bm.createCreationalContext(bean);
		return clazz.cast(bm.getReference(bean, clazz, ctx));
		// do NOT call this
		// ctx.release();
	}

	private <T> Bean<?> getBean(BeanManager bm, Class<T> clazz) {
		final Set<Bean<?>> beans = bm.getBeans(clazz);
		if (beans.isEmpty()) {
			// not sure this is good idea, but it should work for jars without beans.xml file
//			return clazz.newInstance();

			throw new IllegalStateException("No beans of type " + clazz);
		}
		final Iterator<Bean<?>> itr = beans.iterator();
		final Bean<?> bean = itr.next();
		if (itr.hasNext()) {
			throw new IllegalStateException("Non-unique bean instance of type " + clazz);
		}
		return bean;
	}

	private <T> Bean<?> getBean(BeanManager bm, Class<T> clazz, String name) {
		final Set<Bean<?>> beans = bm.getBeans(clazz);
		if (beans.isEmpty()) {
			throw new IllegalStateException("No beans of type " + clazz);
		}
		for (Bean<?> bean : beans) {
			final Set<Annotation> qualifiers = bean.getQualifiers();
			if (qualifiers.contains(new NamedImpl(name))) {
				return bean;
			}
		}
		throw new IllegalStateException("No beans of type " + clazz + " named [" + name + "]");
	}
}
