package org.sdif4j.testing;

import org.sdif4j.Injector;

import javax.annotation.Nonnull;
import java.util.LinkedList;

/**
 * TODO javadoc
 */
public class ThreadLocalInjector {
	private static final ThreadLocal<LinkedList<Pair<Object, Injector>>> threadLocalInjectorStack =
			new InheritableThreadLocal<LinkedList<Pair<Object, Injector>>>();

	private ThreadLocalInjector() {
	}

	/**
	 * Get thread local injector from stack tail
	 *
	 * @return
	 * @throws IllegalStateException if not set
	 */
	@Nonnull
	public static Injector getInjector() throws IllegalStateException {
		final LinkedList<Pair<Object, Injector>> stack = threadLocalInjectorStack.get();
		final Pair<Object, Injector> entry;
		if (stack == null || (entry = stack.peekLast()) == null) {
			throw new IllegalStateException("Injector not set in scope");
		}
		return entry.value;
	}

	/**
	 * Push injector to thread local stack
	 *
	 * @param injector
	 * @return pop key for popInjector to prevent illegal pop
	 */
	public static Object pushInjector(@Nonnull Injector injector) {
		if (injector == null) {
			throw new NullPointerException();
		}
		final Object popKey = new Object();
		final Pair<Object, Injector> entry = new Pair<Object, Injector>(popKey, injector);
		LinkedList<Pair<Object, Injector>> stack = threadLocalInjectorStack.get();
		if (stack == null) {
			threadLocalInjectorStack.set(stack = new LinkedList<Pair<Object, Injector>>());
		}
		stack.add(entry);
		return popKey;
	}

	/**
	 * Pop injector from thread local stack
	 *
	 * @param popKey
	 * @return injector
	 * @throws IllegalArgumentException key is not of stack tail
	 * @throws IllegalStateException    if no injector set
	 */
	public static Injector popInjector(Object popKey) throws IllegalStateException, IllegalArgumentException {
		final LinkedList<Pair<Object, Injector>> stack = threadLocalInjectorStack.get();
		// fetch only
		final Pair<Object, Injector> entry;
		if (stack == null || (entry = stack.peekLast()) == null) {
			throw new IllegalStateException("Injector not set in scope");
		}
		if (entry.key != popKey) {
			throw new IllegalArgumentException("Illegal key");
		}
		// remove from stack
		final Pair<Object, Injector> entry2 = stack.pollLast();
		assert entry == entry2;
		return entry2.value;
	}

	private static class Pair<K, V> {
		final K key;
		final V value;

		Pair(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}
}
