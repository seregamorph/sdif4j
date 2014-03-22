package org.sdif4j.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.enterprise.inject.Alternative;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.concurrent.atomic.AtomicReference;

@Named
@Singleton
@Alternative
public class SpringInjector extends AbstractSpringInjector implements ApplicationContextAware {
	private final AtomicReference<ApplicationContext> context = new AtomicReference<ApplicationContext>();

	// explicit creation is not assumed
	protected SpringInjector() {
	}

	public void setApplicationContext(ApplicationContext context) {
		if (!this.context.compareAndSet(null, context)) {
			throw new IllegalStateException("Already set context");
		}
	}

	@Override
	protected ApplicationContext getContext() {
		final ApplicationContext context = this.context.get();
		if (context == null) {
			throw new IllegalStateException("ApplicationContext not set");
		}
		return context;
	}
}
