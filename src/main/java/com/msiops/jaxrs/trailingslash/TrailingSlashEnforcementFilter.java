/*
 * Licensed to Media Science International (MSI) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. MSI
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.msiops.jaxrs.trailingslash;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

/**
 * <p>
 * Trailing slash enforcement filter. This filter checks for a policy in the
 * well-known context property, {@link #REQUEST_KEY}. The policy is used to
 * reject or pass the request.
 * </p>
 *
 * <p>
 * If a policy is not found, {@link #DEFAULT_POLICY} is used.
 * </p>
 *
 */
@Provider
public final class TrailingSlashEnforcementFilter implements
ContainerRequestFilter {

    public static final Policy DEFAULT_POLICY = PolicyImpl.PROHIBIT;

    public static final String REQUEST_KEY = "com.msiops.jaxrs.TrailingSlashPolicy";

    /**
     * Generate abortive response.
     *
     * @return abort response.
     */
    private static Response abortResponse() {
        return Response.status(Status.NOT_FOUND).build();
    }

    /**
     * Conform configured policy.
     *
     * @param v
     *            proposed policy. Can be null.
     *
     * @return proposed policy if not null, {@link #DEFAULT_POLICY} otherwise.
     */
    private static Policy orDefault(final Object v) {
        /*
         * fail fast (class cast x) if a non-policy object is found.
         */
        return v != null ? (Policy) v : DEFAULT_POLICY;
    }

    @Override
    public void filter(final ContainerRequestContext requestContext)
            throws IOException {

        /*
         * obtain configured policy from well-known policy property.
         */
        final Policy policy = orDefault(requestContext.getProperty(REQUEST_KEY));

        /*
         * apply policy test to resource path.
         */
        if (!policy.pass(requestContext.getUriInfo().getPath())) {
            /*
             * fail! reject request.
             */
            requestContext.abortWith(abortResponse());
        } // else request lives

    }
}
