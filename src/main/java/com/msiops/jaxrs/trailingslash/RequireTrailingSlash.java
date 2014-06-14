package com.msiops.jaxrs.trailingslash;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.ws.rs.NameBinding;

/**
 * Resource requires trailing slash in requested URI path.
 */
@Retention(RetentionPolicy.RUNTIME)
@NameBinding
public @interface RequireTrailingSlash {
}
