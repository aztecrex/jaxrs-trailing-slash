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
package test.com.msiops.jaxrs.trailingslash;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.ArgumentMatcher;

import com.msiops.jaxrs.trailingslash.Policy;
import com.msiops.jaxrs.trailingslash.TrailingSlashEnforcementFilter;

/**
 * Check enforcement filter behavior against configured policy.
 */
@RunWith(Parameterized.class)
public class TrailingSlashEnforcementFilterTest {

    private static final String WITH = "resource/";

    private static final String WITHOUT = "resource";

    /**
     * Case factory.
     *
     * @return collection of test cases.
     */
    @Parameters
    public static Collection<Object[]> cases() {

        /*
         * all named policies
         */
        final Stream<Object[]> policies = Arrays.stream(Policy.values()).map(
                v -> new Object[] { v });
        /*
         * no policy
         */
        final Stream<Object[]> notset = Collections.singletonList(
                new Object[] { null }).stream();

        /*
         * combine all cases and emit as case data.
         */
        return Stream.concat(policies, notset).collect(Collectors.toList());

    }

    private final Policy underTest;

    private ContainerRequestFilter filter;

    private ContainerRequestContext mreq;

    private UriInfo muii;

    public TrailingSlashEnforcementFilterTest(final Policy underTest) {

        this.underTest = underTest;

    }

    @Before
    public void setUp() {
        this.filter = new TrailingSlashEnforcementFilter();
        this.mreq = mock(ContainerRequestContext.class);
        this.muii = mock(UriInfo.class);
        when(this.mreq.getUriInfo()).thenReturn(this.muii);
        when(this.mreq.getProperty(TrailingSlashEnforcementFilter.REQUEST_KEY)).thenReturn(
                this.underTest);

    }

    @Test
    public void testWithoutTrailingSlash() {

        config(WITHOUT);

        runFilter();

        check(WITHOUT);

    }

    @Test
    public void testWithTrailingSlash() {

        config(WITH);

        runFilter();

        check(WITH);
    }

    private void check(final String path) {

        /*
         * with no policy configured, ensure that filter enforces default
         * policy.
         */
        final Policy model = this.underTest == null ? TrailingSlashEnforcementFilter.DEFAULT_POLICY
                : this.underTest;

        if (model.pass(path)) {
            verifyPass();
        } else {
            verifyReject();
        }
    }

    private void config(final String path) {
        when(this.muii.getPath()).thenReturn(path);
    }

    private Matcher<Response> matchesStatus(final Status status) {
        return new ArgumentMatcher<Response>() {

            @Override
            public boolean matches(final Object argument) {
                return ((Response) argument).getStatus() == status.getStatusCode();
            }
        };
    }

    private void runFilter() {
        try {
            this.filter.filter(this.mreq);
        } catch (final RuntimeException rtx) {
            throw rtx;
        } catch (final Throwable t) {
            throw new RuntimeException("filter threw checked", t);
        }
    }

    private void verifyPass() {
        verify(this.mreq, never()).abortWith((Response) any());
    }

    private void verifyReject() {

        verify(this.mreq).abortWith(argThat(matchesStatus(Status.NOT_FOUND)));
    }

}
