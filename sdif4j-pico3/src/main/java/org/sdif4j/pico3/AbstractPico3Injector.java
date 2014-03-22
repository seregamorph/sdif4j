package org.sdif4j.pico3;

import com.picocontainer.PicoContainer;
import org.sdif4j.Injector;

import javax.inject.Inject;
import javax.inject.Provider;
import java.lang.reflect.Field;

/**
 * Base Pico 3.0 Injector implementation
 *
 * @author Sergey Chernov
 */
public abstract class AbstractPico3Injector implements Injector {
	protected abstract PicoContainer getPicoContainer();

	public <T> Provider<T> getProvider(final Class<T> clazz) {
		return new Provider<T>() {
			public T get() {
//				final PicoContainer pc = getPicoContainer();
//				return clazz.cast(pc.getComponentAdapter(clazz).getComponentInstance(pc, clazz));
				return getInstance(clazz);
			}
		};
	}

	public <T> T getInstance(Class<T> clazz) {
		return getPicoContainer().getComponent(clazz);
	}

	public <T> T getInstance(Class<T> clazz, String name) {
		return clazz.cast(getPicoContainer().getComponent(name));
	}

	public void injectMembers(Object instance) {
		final PicoContainer pc = getPicoContainer();
		final Class<?> clazz = instance.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(Inject.class)) {
				Object value = pc.getComponent(field.getType());
				try {
					field.setAccessible(true);
					field.set(instance, value);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException("Error while injecting field " + field.getName(), e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException("Error while injecting field " + field.getName(), e);
				}
			}
		}
	}
}
