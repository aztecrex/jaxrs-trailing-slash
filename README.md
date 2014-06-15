[![Build Status](https://travis-ci.org/mediascience/jaxrs-trailing-slash.svg?branch=master)](https://travis-ci.org/mediascience/jaxrs-trailing-slash)

## JAX-RS Trailing Slash

Enforce a trailing-slash policy in JAX-RS. This module
includes a collection of policies and an enforcing filter
for JAX-RS 2.0.

Per the JAX-RS specification, a request URI with a trailing
slash is treated the same as one without a trailing slash
for the purpose of selecting a handler method. While
this can be fine in some applications, it is problematic in
others. This module provides a uniform means to declaratively
reject or accept, per resource method, an incoming 
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

### Include Dependencies
Include the dependency in your build config.
```
<dependency>
  <groupId>com.msiops.jaxrs</groupId>
  <artifactId>jaxrs-trailing-slash</artifactId>
  <version>${version}</version>
</dependency>
```

### Configuring Resources

The module provides filters for enforcing trailing slash
policy and injecting policy into a request context as needed
by an application. The three methods for injecting policy
are *global configuration*, *dynamic configuration*, and
*static configuration*.

*Global configuration* is accomplished by registering the enforcement
filter and, optionally, a policy filter in your application (see
manual configurations below).

The filters, `AllowTrailingSlashFilter`, `ProhibitTrailingSlashFilter`,
and `RequireTrailingSlashFilter`, support *dynamic configuration*:
```
public class AllowTrailingSlashForDocumentRequests implements DynamicFeature {
    @Override
    public void configure(ResourceInfo resInf, FeatureContext ctx) {
        if (DocumentResource.class.equals(resInf.getResourceClass())) {
            ctx.register(AllowTrailingSlashFilter.class);
        }
    }
}
```

The annotations, `@AllowTrailingSlash`, `@ProhibitTrailingSlash`, and
`@RequireTrailingSlash` provide *static configuration* for a resource:
```
@Path("data")
public class DataResource {
    @GET
    @RequireTrailingSlash
    String getData() {
        return "data";
    }
}
```

Or:
```
@Path("data")
@RequireTrailingSlash
public class DataResource {
    @GET
    String getData() {
        return "data";
    }
}
```

### Manual Filter Registration

Depending on your JAX-RS implementation, you can register filters
manually. For example, to simply enforce the default policy for all
resources:
```
    // Jersey-specific, explicitly include enforcement filter
    final ResourceConfig config = new ResourceConfig(MyResource.class,
          TrailingSlashEnforcementFilter.class);

    JdkHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);
```

Or, to enforce a specific policy for all resources: 
```
    // Jersey-specific, explicitly enforce trailing slash required on all resources
    final ResourceConfig config = new ResourceConfig(MyResource.class,
          TrailingSlashEnforcementFilter.class,
          RequreTrailingSlashFilter.class);

    JdkHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);
```

Note that if you need dynamic configuration, do not include the policy
filter globally.

For static (annotation) configuration, include just the enforcement filter along
with whatever static implementation filters you need in your application:
```
    // Jersey-specific, explicitly include enforcement and ability to statically
    // configure required trailing slashes on some resources.
    final ResourceConfig config = new ResourceConfig(MyResource.class,
          TrailingSlashEnforcementFilter.class,
          RequreTrailingSlashStaticFilter.class);

    JdkHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);
```

### Automatic Discovery

The enforcement filter and the static override filters can be discovered
automatically by a suitable JAX-RS implementation:
```
    // Jersey-specific, automatic discovery
    final ResourceConfig config = new ResourceConfig(MyResource.class);
    config.packages("com.msiops.jaxrs.trailingslash");

    JdkHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);
```

Note that this does not discover the explicit override filters so that they
do not get automatically applied. They are meant for dynamic registration
in this case.

# Build

This code is currently built using Oracle JDK 1.8 and the Gradle 2.0 release
candidate. My apologies for using an unreleased build tool but since this is
my first real Gradle project, I hesitate to learn the current version then
have to change my understanding for the version that is just around the
corner. This way, I am experiencing the pain of change just once.


