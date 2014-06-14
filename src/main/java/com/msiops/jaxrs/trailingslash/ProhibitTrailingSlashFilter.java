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

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

/**
 * Filter to allow trailing slash in incoming resource request URI. Injects
 * {@link Policy#PROHIBIT} into the request context.
 */
@Provider
@Priority(100)
public class ProhibitTrailingSlashFilter implements ContainerRequestFilter {

    @Override
    public void filter(final ContainerRequestContext requestContext)
            throws IOException {

        requestContext.setProperty(TrailingSlashEnforcementFilter.REQUEST_KEY,
                Policy.PROHIBIT);

    }

}
