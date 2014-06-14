## JAX-RS Trailing Slash

(This doc is, obviously, work in progress. Expect to finish
it as the design/impl proceeds and will have a final version
ready by the time the module is published on Maven Central)

Enforce a trailing-slash policy in JAX-RS. This package
includes a collection of policies and an enforcing filter
for JAX-RS 2.0.

Per the JAX-RS specification, a request URI with a trailing
slash is treated the same as one without a trailing slash
for the purpose of selecting a handler method.

This can be fine in some applications but problematic in
others. This module provides a uniform means to (TODO
declaratively, and per method) reject or accept an incoming
request depending on whether its resource path has a
trailing slash.

The module defines three policies:

1. 'Allow' lets any request pass. This is the same as
not applying the filter at all.
2. 'Require' passes a request only if the requested
path has a trailing slash.
3. 'Prohibit' passes a request only if the requested path
does not have a trailing slash.

The default filter policy is 'Prohibit.'

## Usage

Include the dependency in your build script.

TODO put in Maven Central and fill out this part.

Include the enforcement filter class in your application
configuration. It is annotated with @Provider so it can
be auto-discovered depending on your JAX-RS implementation.


TODO specify the declarive bit, expecting it to be a name
binding. But until then....

Cause a policy to be injected into the request context
properties before the enforcing filter is invoked. You can
do this with another filter. The key is exported as a
constant by the enforcing filter.


# Build

TODO how to build


