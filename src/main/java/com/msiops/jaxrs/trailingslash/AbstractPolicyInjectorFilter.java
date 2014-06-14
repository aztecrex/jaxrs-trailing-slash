package com.msiops.jaxrs.trailingslash;

import java.io.IOException;
import java.util.Objects;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

/**
 * Policy injection filter template.
 */
abstract class AbstractPolicyInjectorFilter implements ContainerRequestFilter {

    private final Policy policy;

    protected AbstractPolicyInjectorFilter(final Policy policy) {
        this.policy = Objects.requireNonNull(policy);
    }

    @Override
    public final void filter(final ContainerRequestContext requestContext)
            throws IOException {
        requestContext.setProperty(TrailingSlashEnforcementFilter.REQUEST_KEY,
                this.policy);
    }

}
