package org.sdif4j.cdi;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * CDI Injector implementation
 *
 * @author Sergey Chernov
 */
@Singleton
@javax.enterprise.inject.Default
public class CdiInjector extends AbstractCdiInjector {
	private final BeanManager beanManager;
	private final CreationalContext creationalContext;

	@Inject
	protected CdiInjector(BeanManager beanManager) {
		this.beanManager = beanManager;
		this.creationalContext = beanManager.createCreationalContext(null);
	}

	@Override
	protected BeanManager getBeanManager() throws RuntimeException {
		return beanManager;
	}

	@Override
	protected CreationalContext getCreationalContext() {
		return creationalContext;
	}

	public static BeanManager lookupBeanManager() {
		final InitialContext initialContext;
		try {
			initialContext = new InitialContext();
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}

		try {
			// Standard
			return (BeanManager) initialContext.lookup("java:comp/BeanManager");
		} catch (NamingException e) {
			try {
				// Servlet
				return (BeanManager) initialContext.lookup("java:comp/env/BeanManager");
			} catch (NamingException e1) {
				try {
					// Workaround for WELDINT-19
					return (BeanManager) initialContext.lookup("java:app/BeanManager");
				} catch (NamingException e2) {
					try {
						// JBoss
						return (BeanManager) initialContext.lookup("BeanManager");
					} catch (NamingException e3) {
						throw new RuntimeException("Failed to lookup BeanManager");
					}
				}
			}
		}
	}
}
