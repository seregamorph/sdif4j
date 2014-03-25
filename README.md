sdif4j is a Simple Dependency Injection Facade for Java
======

This project aims to encapsulate different dependency injection frameworks with own abstraction.
It can be helpful on developing frameworks or any reusable in DI context code.

Currently supported frameworks:
* Google Guice 3.0
* Spring 3.0
* JavaEE CDI 6
* PicoContainer 2.0
* PicoContainer 3.0 [support is in progress, dev/pico branch]
Tapestry and Dagger support are planned.

All of these DI frameworks support JSR-330.

You can find examples in sdif4j-testing* subprojects.

### How it works
1. Everything is in a small single maven module without additional dependencies
2. The org.sdif4j.Injector has @ImplementedBy annotation (that is the reason why guice and api cannot be in different maven modules), that binds it to GuiceInjector, no via module binding required. All other frameworks that are already supported simply ignores this annotation (does not matter if guice presents in class path or not). Note that GuiceInjector accepts the Provider interface instead of Injector, it is issue of CDI class/constructor scanning.
3. For Spring: the org.sdif4j.spring component-scan is required
4. For CDI - @Default and @Alternative annotations are used (also ignored by Guice/Spring)
5. For Pico - manual binding is required.
6. All implementations are two level - Abstract and concrete, because in general case there can be a custom Injector instance provider like ThreadLocal (i had such in my test environment).


This note is incomplete, project in progress.
