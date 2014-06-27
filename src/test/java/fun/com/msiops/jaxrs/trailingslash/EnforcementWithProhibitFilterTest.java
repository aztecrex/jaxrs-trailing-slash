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
package fun.com.msiops.jaxrs.trailingslash;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.msiops.jaxrs.trailingslash.ProhibitTrailingSlashFilter;
import com.msiops.jaxrs.trailingslash.TrailingSlashEnforcementFilter;

/**
 * Check behavior when both enforcement and allow filters advise a resource.
 */
@RunWith(Parameterized.class)
public class EnforcementWithProhibitFilterTest {

    private static final String BASE_URI = "http://localhost:8091/";

    @Parameters
    public static Collection<Object[]> cases() {

        return Arrays.<Object[]> asList(

        new Object[] { true, false },

        new Object[] { false, true }

        );
    }

    @BeforeClass
    public static void startServer() {

        /*
         * testing with jersey and JDK HTTP server
         */
        final ResourceConfig config = new ResourceConfig(DataResource.class,
                TrailingSlashEnforcementFilter.class,
                ProhibitTrailingSlashFilter.class);
        JdkHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);

    }

    private final boolean withSlash;

    private final boolean pass;

    public EnforcementWithProhibitFilterTest(final boolean withSlash,
            final boolean pass) {

        this.withSlash = withSlash;
        this.pass = pass;

    }

    /**
     * Ensure that the policy is applied for the provided path when requested in
     * its bare form.
     */
    @Test
    public void testBarePath() {

        final WebTarget target = bareTarget();
        assertEquals(expectedStatusCode(), invokeRemote(target));

    }

    /**
     * Ensure that the policy is applied for the provided path when requested
     * with an appended query part.
     */
    @Test
    public void testPathWithQuery() {

        final WebTarget target = bareTarget().queryParam("a", 7);
        assertEquals(expectedStatusCode(), invokeRemote(target));

    }

    private WebTarget bareTarget() {
        return ClientBuilder.newClient()
                .target(BASE_URI)
                .path("data" + (this.withSlash ? "/" : ""));
    }

    private int expectedStatusCode() {
        return (this.pass ? Status.OK : Status.NOT_FOUND).getStatusCode();
    }

    private int invokeRemote(final WebTarget target) {
        return target.request().get().getStatus();
    }

    @Path("/data")
    @Produces("text/plain")
    public static final class DataResource {

        @GET
        public String getData() {
            return "data";
        }

    }

}
