package org.sdif4j.dagger;

import dagger.ObjectGraph;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Dagger Injector implementation
 *
 * @author Sergey Chernov
 */
@Singleton
public class DaggerInjector extends AbstractDaggerInjector {
	private final ObjectGraph objectGraph;

	protected DaggerInjector(ObjectGraph objectGraph) {
		this.objectGraph = objectGraph;
	}

	@Override
	protected ObjectGraph getObjectGraph() {
		return objectGraph;
	}
}
