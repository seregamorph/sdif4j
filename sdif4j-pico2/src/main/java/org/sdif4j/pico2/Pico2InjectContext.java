package org.sdif4j.pico2;

import org.picocontainer.PicoContainer;

import javax.inject.Singleton;

/**
 * Pico 2 InjectContext implementation
 * <p/>
 *
 * @author Sergey Chernov
 */
@Singleton
public class Pico2InjectContext extends AbstractPico2InjectContext {
	// @org.picocontainer.annotations.Inject does not work
	@javax.inject.Inject
	private PicoContainer picoContainer;

	// public constructor required by PicoContainer
	public Pico2InjectContext() {
	}

	public Pico2InjectContext(PicoContainer picoContainer) {
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
