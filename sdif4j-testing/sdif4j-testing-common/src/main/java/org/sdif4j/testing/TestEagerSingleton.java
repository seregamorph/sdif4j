package org.sdif4j.testing;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.concurrent.atomic.AtomicInteger;

@Named
@Singleton
public class TestEagerSingleton implements TestSingleton {
	private static final AtomicInteger instantCounter = new AtomicInteger();

	public TestEagerSingleton() {
		instantCounter.incrementAndGet();
	}

	public static int getInstantCount() {
		return instantCounter.get();
	}
}
