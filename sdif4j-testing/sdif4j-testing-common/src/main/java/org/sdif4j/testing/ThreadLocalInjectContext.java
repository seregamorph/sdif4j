package org.sdif4j.testing;

import org.sdif4j.InjectContext;

import javax.annotation.Nonnull;
import java.util.LinkedList;

/**
 * TODO javadoc
 */
public class ThreadLocalInjectContext {
	private static final ThreadLocal<LinkedList<Pair<Object, InjectContext>>> threadLocalInjectContextStack =
			new InheritableThreadLocal<LinkedList<Pair<Object, InjectContext>>>();

	private ThreadLocalInjectContext() {
	}

	/**
	 * Get thread local injector from stack tail
	 *
	 * @return
	 * @throws IllegalStateException if not set
	 */
	@Nonnull
	public static InjectContext getInjectContext() throws IllegalStateException {
		final LinkedList<Pair<Object, InjectContext>> stack = threadLocalInjectContextStack.get();
		final Pair<Object, InjectContext> entry;
		if (stack == null || (entry = stack.peekLast()) == null) {
			throw new IllegalStateException("InjectContext not set in scope");
		}
		return entry.value;
	}

	/**
	 * Push injectContext to thread local stack
	 *
	 * @param injectContext
	 * @return pop key for popInjectContext to prevent illegal pop
	 */
	public static Object pushInjectContext(@Nonnull InjectContext injectContext) {
		if (injectContext == null) {
			throw new NullPointerException();
		}
		final Object popKey = new Object();
		final Pair<Object, InjectContext> entry = new Pair<Object, InjectContext>(popKey, injectContext);
		LinkedList<Pair<Object, InjectContext>> stack = threadLocalInjectContextStack.get();
		if (stack == null) {
			threadLocalInjectContextStack.set(stack = new LinkedList<Pair<Object, InjectContext>>());
		}
		stack.add(entry);
		return popKey;
	}

	/**
	 * Pop injectContext from thread local stack
	 *
	 * @param popKey
	 * @return injectContext
	 * @throws IllegalArgumentException key is not of stack tail
	 * @throws IllegalStateException    if no injectContext set
	 */
	public static InjectContext popInjectContext(Object popKey) throws IllegalStateException, IllegalArgumentException {
		final LinkedList<Pair<Object, InjectContext>> stack = threadLocalInjectContextStack.get();
		// fetch only
		final Pair<Object, InjectContext> entry;
		if (stack == null || (entry = stack.peekLast()) == null) {
			throw new IllegalStateException("InjectContext not set in scope");
		}
		if (entry.key != popKey) {
			throw new IllegalArgumentException("Illegal key");
		}
		// remove from stack
		final Pair<Object, InjectContext> entry2 = stack.pollLast();
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
