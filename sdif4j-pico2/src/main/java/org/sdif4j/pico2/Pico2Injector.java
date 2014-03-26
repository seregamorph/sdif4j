package org.sdif4j.pico2;

import org.picocontainer.PicoContainer;

import javax.inject.Singleton;

/**
 * Pico 2 Injector implementation
 * <p/>
 *
 * @author Sergey Chernov
 */
@Singleton
public class Pico2Injector extends AbstractPico2Injector {
	// @org.picocontainer.annotations.Inject does not work
	@javax.inject.Inject
	private PicoContainer picoContainer;

	// public constructor required by PicoContainer
	public Pico2Injector() {
	}

	public Pico2Injector(PicoContainer picoContainer) {
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
}
