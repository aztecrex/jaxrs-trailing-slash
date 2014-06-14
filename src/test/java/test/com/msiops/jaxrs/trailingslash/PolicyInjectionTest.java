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

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.msiops.jaxrs.trailingslash.AllowTrailingSlashFilter;
import com.msiops.jaxrs.trailingslash.Policy;
import com.msiops.jaxrs.trailingslash.ProhibitTrailingSlashFilter;
import com.msiops.jaxrs.trailingslash.RequireTrailingSlashFilter;
import com.msiops.jaxrs.trailingslash.TrailingSlashEnforcementFilter;

/**
 * Check that injecting filters do.
 */
@RunWith(Parameterized.class)
public class PolicyInjectionTest {

    @Parameters
    public static Collection<Object[]> cases() {

        return Arrays.asList(new Object[][] {
                { new AllowTrailingSlashFilter(), Policy.ALLOW },
                { new ProhibitTrailingSlashFilter(), Policy.PROHIBIT },
                { new RequireTrailingSlashFilter(), Policy.REQUIRE } });

    }

    private final ContainerRequestFilter underTest;

    private final Policy expectedPolicy;

    private ContainerRequestContext mreq;

    public PolicyInjectionTest(final ContainerRequestFilter underTest,
            final Policy expectedPolicy) {

        this.underTest = underTest;
        this.expectedPolicy = expectedPolicy;

    }

    @Before
    public void setup() {
        this.mreq = mock(ContainerRequestContext.class);
    }

    /**
     * Ensure that the expected policy is injected by the filter.
     */
    @Test
    public void testInjectedPolicy() {

        runFilter();

        checkPolicy();

    }

    private void checkPolicy() {

        verify(this.mreq).setProperty(
                TrailingSlashEnforcementFilter.REQUEST_KEY, this.expectedPolicy);

    }

    private void runFilter() {
        try {
            this.underTest.filter(this.mreq);
        } catch (final RuntimeException rtx) {
            throw rtx;
        } catch (final Throwable t) {
            throw new RuntimeException("filter threw checked", t);
        }
    }

}
