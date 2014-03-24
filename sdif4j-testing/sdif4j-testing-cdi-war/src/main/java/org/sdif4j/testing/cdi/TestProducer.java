package org.sdif4j.testing.cdi;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

public class TestProducer {
	@Produces
	@Named("key")
	String getTestKey() {
		return "value";
	}

	@Produces
	@Named("key1")
	String getTestKey1() {
		return "value1";
	}
}
