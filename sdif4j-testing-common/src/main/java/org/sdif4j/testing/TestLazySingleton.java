package org.sdif4j.testing;

import org.springframework.context.annotation.Lazy;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.concurrent.atomic.AtomicInteger;

@Named
@Singleton
@Lazy // ignored by Guice
public class TestLazySingleton {
	private static final AtomicInteger instantCounter = new AtomicInteger();

	public TestLazySingleton() {
		instantCounter.incrementAndGet();
	}

	public static int getInstantCount() {
		return instantCounter.get();
	}
}
