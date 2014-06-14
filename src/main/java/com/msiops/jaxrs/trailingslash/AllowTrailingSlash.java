package com.msiops.jaxrs.trailingslash;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.ws.rs.NameBinding;

/**
 * Resource allows trailing slash or not in requested URI path.
 */
@Retention(RetentionPolicy.RUNTIME)
@NameBinding
public @interface AllowTrailingSlash {
}
