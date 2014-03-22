package org.sdif4j.pico3;

import com.picocontainer.PicoContainer;

import javax.inject.Singleton;

/**
 * Pico 3.0 Injector implementation
 * <p/>
 * This implementation is waiting for
 * https://github.com/picocontainer/picocontainer
 *
 * @author Sergey Chernov
 */
@Singleton
public class Pico3Injector extends AbstractPico3Injector {
	//	@org.picocontainer.annotations.Inject
	@javax.inject.Inject
	private PicoContainer picoContainer;

	// public constructor required by PicoContainer
	public Pico3Injector() {
	}

	public Pico3Injector(PicoContainer picoContainer) {
		this.picoContainer = picoContainer;
	}


	@Override
	protected PicoContainer getPicoContainer() {
		final PicoContainer picoContainer = this.picoContainer;
		if (picoContainer == null) {
			throw new IllegalStateException("PicoContainer is not set");
		}
		return picoContainer;
	}

	// only for tests, not public api
	// todo refactor to reflection
	public PicoContainer getPicoContainerAccessor() {
		return picoContainer;
	}
}
