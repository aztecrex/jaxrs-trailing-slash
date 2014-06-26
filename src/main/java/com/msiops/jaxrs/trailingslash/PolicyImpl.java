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

/**
 * Trailing slash policy implementations. A policy can pass or fail a resource
 * path.
 *
 *
 * @author greg wiley &lt;aztec.rex@jammm.com&gt;
 *
 */
public enum PolicyImpl implements Policy {
    /**
     * Allow trailing slash or not, i.e. always passes.
     */
    ALLOW {
        @Override
        public boolean pass(final String rightHandPath) {
            return true;
        }
    },
    /**
     * Reject path with trailing slash. Pass otherwise.
     */
    PROHIBIT {
        @Override
        public boolean pass(final String rightHandPath) {
            return !rightHandPath.endsWith("/");
        }
    },
    /**
     * Reject path without trailing slash. Pass otherwise.
     */
    REQUIRE {
        @Override
        public boolean pass(final String rightHandPath) {
            return rightHandPath.endsWith("/");
        }
    };

    /*
     * (non-Javadoc)
     * 
     * @see com.msiops.jaxrs.trailingslash.IPolicy#pass(java.lang.String)
     */
    @Override
    public abstract boolean pass(String rightHandPath);
}