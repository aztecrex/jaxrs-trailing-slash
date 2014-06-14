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
package unit.com.msiops.jaxrs.trailingslash;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.msiops.jaxrs.trailingslash.Policy;

/**
 * Check policy computations.
 *
 */
@RunWith(Parameterized.class)
public class PolicyTest {

    /**
     * Generate test cases.
     *
     * @return cases to test. Includes input and expectations for each case.
     */
    @Parameters
    public static Collection<Object[]> cases() {

        /*
         * triples consist of policy to test, input path, and expected pass
         * result.
         */
        return Arrays.asList(new Object[][] {
                { Policy.REQUIRE, "resource", false },
                { Policy.REQUIRE, "resource/", true },
                { Policy.PROHIBIT, "resource", true },
                { Policy.PROHIBIT, "resource/", false },
                { Policy.ALLOW, "resource", true },
                { Policy.ALLOW, "resource/", true } });

    }

    private final Policy underTest;

    private final String input;

    private final boolean expected;

    public PolicyTest(final Policy underTest, final String input,
            final boolean expected) {
        this.underTest = underTest;
        this.input = input;
        this.expected = expected;
    }

    /**
     * Check pass result for a case.
     */
    @Test
    public void testPass() {

        assertEquals(this.expected, this.underTest.pass(this.input));

    }

}
