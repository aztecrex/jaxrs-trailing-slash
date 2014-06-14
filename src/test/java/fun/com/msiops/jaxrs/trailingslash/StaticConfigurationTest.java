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

import com.msiops.jaxrs.trailingslash.AllowTrailingSlash;
import com.msiops.jaxrs.trailingslash.ProhibitTrailingSlash;
import com.msiops.jaxrs.trailingslash.RequireTrailingSlash;

/**
 * Check that static configuration works.
 */
public class StaticConfigurationTest {

    private static final String BASE_URI = "http://localhost:8092/";

    @BeforeClass
    public static void startServer() {

        /*
         * testing with jersey and JDK HTTP server
         */
        final ResourceConfig config = new ResourceConfig(DataResource.class).packages("com.msiops.jaxrs.trailingslash");
        JdkHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);

    }

    @Test
    public void testAllowPassNoTrailingSlash() {
        final WebTarget target = ClientBuilder.newClient()
                                              .target(BASE_URI)
                                              .path("data/allow");
        assertEquals(Status.OK.getStatusCode(), target.request()
                                                      .get()
                                                      .getStatus());
    }

    @Test
    public void testAllowPassTrailingSlash() {
        final WebTarget target = ClientBuilder.newClient()
                                              .target(BASE_URI)
                                              .path("data/allow/");
        assertEquals(Status.OK.getStatusCode(), target.request()
                                                      .get()
                                                      .getStatus());
    }

    @Test
    public void testProhibitPassNoTrailingSlash() {
        final WebTarget target = ClientBuilder.newClient()
                                              .target(BASE_URI)
                                              .path("data/prohibit");
        assertEquals(Status.OK.getStatusCode(), target.request()
                                                      .get()
                                                      .getStatus());
    }

    @Test
    public void testProhibitRejectTrailingSlash() {
        final WebTarget target = ClientBuilder.newClient()
                                              .target(BASE_URI)
                                              .path("data/prohibit/");
        assertEquals(Status.NOT_FOUND.getStatusCode(), target.request()
                                                             .get()
                                                             .getStatus());
    }

    @Test
    public void testRequirePassTrailingSlash() {
        final WebTarget target = ClientBuilder.newClient()
                                              .target(BASE_URI)
                                              .path("data/require/");
        assertEquals(Status.OK.getStatusCode(), target.request()
                                                      .get()
                                                      .getStatus());
    }

    @Test
    public void testRequireRejectNoTrailingSlash() {
        final WebTarget target = ClientBuilder.newClient()
                                              .target(BASE_URI)
                                              .path("data/require");
        assertEquals(Status.NOT_FOUND.getStatusCode(), target.request()
                                                             .get()
                                                             .getStatus());
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
