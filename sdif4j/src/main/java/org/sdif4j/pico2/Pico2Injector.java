package org.sdif4j.pico2;

import org.picocontainer.PicoContainer;

import javax.enterprise.inject.Alternative;
import javax.inject.Singleton;

/**
 * Pico 2 Injector implementation
 * <p/>
 *
 * @author Sergey Chernov
 */
@Alternative // ignored by PicoContainer, required for JavaEE CDI
@Singleton
public class Pico2Injector extends AbstractPico2Injector {
	//	@org.picocontainer.annotations.Inject
	@javax.inject.Inject
	private PicoContainer picoContainer;

	// todo public constructor required by PicoContainer
	public Pico2Injector() {
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
