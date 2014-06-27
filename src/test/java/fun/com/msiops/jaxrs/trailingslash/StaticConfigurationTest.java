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

import com.msiops.jaxrs.trailingslash.AllowTrailingSlash;
import com.msiops.jaxrs.trailingslash.ProhibitTrailingSlash;
import com.msiops.jaxrs.trailingslash.RequireTrailingSlash;

/**
 * Check that static configuration works.
 */
@RunWith(Parameterized.class)
public class StaticConfigurationTest {

    private static final String BASE_URI = "http://localhost:8092/";

    @Parameters
    public static Collection<Object[]> cases() {

        return Arrays.<Object[]> asList(

                new Object[] { "allow", true },

                new Object[] { "allow/", true },

                new Object[] { "prohibit", true },

                new Object[] { "prohibit/", false },

                new Object[] { "require", false },

                new Object[] { "require/", true }

                );
    }

    @BeforeClass
    public static void startServer() {

        /*
         * testing with jersey and JDK HTTP server
         */
        final ResourceConfig config = new ResourceConfig(DataResource.class).packages("com.msiops.jaxrs.trailingslash");
        JdkHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);

    }

    /**
     * Whether the provided path segment should pass. If true, the service is
     * expected to return a 200 status. If not, the service is expected to
     * return a 404 status.
     */
    private final boolean pass;

    /**
     * passed final path segment. This selects one of the dynamically-configured
     * resource methods in the test resource provider.
     */
    private final String path;

    public StaticConfigurationTest(final String path, final boolean pass) {
        this.path = path;
        this.pass = pass;
    }

    /**
     * Ensure that the static configuration is applied for the provided path
     * when requested in its bare form.
     */
    @Test
    public void testBarePath() {

        final WebTarget target = bareTarget();
        assertEquals(expectedStatusCode(), invokeRemote(target));

    }

    /**
     * Ensure that the static configuration is applied for the provided path
     * when requested with an appended query part.
     */
    @Test
    public void testPathWithQuery() {

        final WebTarget target = bareTarget().queryParam("a", 7);
        assertEquals(expectedStatusCode(), invokeRemote(target));

    }

    private WebTarget bareTarget() {
        return ClientBuilder.newClient()
                .target(BASE_URI)
                .path("data/" + this.path);
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

        @Path("allow")
        @AllowTrailingSlash
        @GET
        public String allowSlash() {
            return "data";
        }

        @Path("prohibit")
        @ProhibitTrailingSlash
        @GET
        public String prohibitSlash() {
            return "data";
        }

        @Path("require")
        @RequireTrailingSlash
        @GET
        public String requireSlash() {
            return "data";
        }

    }

}
