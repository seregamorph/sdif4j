package org.sdif4j.cdi;

import javax.inject.Named;
import java.io.Serializable;
import java.lang.annotation.Annotation;

/*
Originally based on
https://code.google.com/p/google-guice/source/browse/core/src/com/google/inject/name/NamedImpl.java
*/
class NamedImpl implements Named, Serializable {
	private final String value;

	public NamedImpl(String value) {
		if (value == null) {
			throw new IllegalArgumentException("value is null");
		}
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public int hashCode() {
		// This is specified in java.lang.Annotation.
		return (127 * "value".hashCode()) ^ value.hashCode();
	}

	public boolean equals(Object o) {
		if (!(o instanceof Named)) {
			return false;
		}

		Named other = (Named) o;
		return value.equals(other.value());
	}

	public String toString() {
		return "@" + Named.class.getName() + "(value=" + value + ")";
	}

	public Class<? extends Annotation> annotationType() {
		return Named.class;
	}

	private static final long serialVersionUID = 0;
}
