/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.external.api.client.api.model;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Unit tests for APIInvocationConfig class.
 */
public class APIInvocationConfigTest {

    private static final int DEFAULT_RETRY_COUNT = 0;
    private static final int CUSTOM_RETRY_COUNT = 3;
    private static final int NEGATIVE_RETRY_COUNT = -1;
    private static final int ZERO_RETRY_COUNT = 0;
    private static final int MAX_RETRY_COUNT = Integer.MAX_VALUE;

    /**
     * Test successful creation of APIInvocationConfig with default values.
     */
    @Test
    public void testCreateAPIInvocationConfigWithDefaultValues() {

        APIInvocationConfig config = new APIInvocationConfig();

        assertNotNull(config);
        assertEquals(config.getAllowedRetryCount(), DEFAULT_RETRY_COUNT);
    }

    /**
     * Test setting allowed retry count.
     */
    @Test
    public void testSetAllowedRetryCount() {

        APIInvocationConfig config = new APIInvocationConfig();
        config.setAllowedRetryCount(CUSTOM_RETRY_COUNT);

        assertEquals(config.getAllowedRetryCount(), CUSTOM_RETRY_COUNT);
    }

    /**
     * Test setting zero retry count.
     */
    @Test
    public void testSetZeroRetryCount() {

        APIInvocationConfig config = new APIInvocationConfig();
        config.setAllowedRetryCount(ZERO_RETRY_COUNT);

        assertEquals(config.getAllowedRetryCount(), ZERO_RETRY_COUNT);
    }

    /**
     * Test setting negative retry count.
     */
    @Test
    public void testSetNegativeRetryCount() {

        APIInvocationConfig config = new APIInvocationConfig();
        config.setAllowedRetryCount(NEGATIVE_RETRY_COUNT);

        assertEquals(config.getAllowedRetryCount(), NEGATIVE_RETRY_COUNT);
    }

    /**
     * Test setting maximum retry count.
     */
    @Test
    public void testSetMaxRetryCount() {

        APIInvocationConfig config = new APIInvocationConfig();
        config.setAllowedRetryCount(MAX_RETRY_COUNT);

        assertEquals(config.getAllowedRetryCount(), MAX_RETRY_COUNT);
    }

    /**
     * Test getter method returns correct value.
     */
    @Test
    public void testGetAllowedRetryCount() {

        APIInvocationConfig config = new APIInvocationConfig();
        
        // Test default value
        assertEquals(config.getAllowedRetryCount(), DEFAULT_RETRY_COUNT);
        
        // Test after setting custom value
        config.setAllowedRetryCount(CUSTOM_RETRY_COUNT);
        assertEquals(config.getAllowedRetryCount(), CUSTOM_RETRY_COUNT);
    }

    /**
     * Test multiple updates to retry count.
     */
    @Test
    public void testMultipleUpdatesToRetryCount() {

        APIInvocationConfig config = new APIInvocationConfig();
        
        // Initial value
        assertEquals(config.getAllowedRetryCount(), DEFAULT_RETRY_COUNT);
        
        // First update
        config.setAllowedRetryCount(1);
        assertEquals(config.getAllowedRetryCount(), 1);
        
        // Second update
        config.setAllowedRetryCount(5);
        assertEquals(config.getAllowedRetryCount(), 5);
        
        // Third update back to zero
        config.setAllowedRetryCount(0);
        assertEquals(config.getAllowedRetryCount(), 0);
    }

    /**
     * Test that setter modifies the state correctly.
     */
    @Test
    public void testSetterModifiesState() {

        APIInvocationConfig config = new APIInvocationConfig();
        int initialValue = config.getAllowedRetryCount();
        
        config.setAllowedRetryCount(CUSTOM_RETRY_COUNT);
        int newValue = config.getAllowedRetryCount();
        
        // Verify the value has changed
        assertEquals(newValue, CUSTOM_RETRY_COUNT);
        // Verify it's different from initial value (assuming CUSTOM_RETRY_COUNT != DEFAULT_RETRY_COUNT)
        if (CUSTOM_RETRY_COUNT != DEFAULT_RETRY_COUNT) {
            assert initialValue != newValue;
        }
    }

    /**
     * Test object state consistency.
     */
    @Test
    public void testObjectStateConsistency() {

        APIInvocationConfig config = new APIInvocationConfig();
        
        // Test multiple get calls return same value
        int firstCall = config.getAllowedRetryCount();
        int secondCall = config.getAllowedRetryCount();
        assertEquals(firstCall, secondCall);
        
        // Set a value and test consistency
        config.setAllowedRetryCount(CUSTOM_RETRY_COUNT);
        int thirdCall = config.getAllowedRetryCount();
        int fourthCall = config.getAllowedRetryCount();
        assertEquals(thirdCall, fourthCall);
        assertEquals(thirdCall, CUSTOM_RETRY_COUNT);
    }

    /**
     * Test edge case values.
     */
    @Test
    public void testEdgeCaseValues() {

        APIInvocationConfig config = new APIInvocationConfig();
        
        // Test minimum integer value
        config.setAllowedRetryCount(Integer.MIN_VALUE);
        assertEquals(config.getAllowedRetryCount(), Integer.MIN_VALUE);
        
        // Test maximum integer value
        config.setAllowedRetryCount(Integer.MAX_VALUE);
        assertEquals(config.getAllowedRetryCount(), Integer.MAX_VALUE);
        
        // Test boundary values
        config.setAllowedRetryCount(-1);
        assertEquals(config.getAllowedRetryCount(), -1);
        
        config.setAllowedRetryCount(1);
        assertEquals(config.getAllowedRetryCount(), 1);
    }

    /**
     * Test typical use case scenarios.
     */
    @Test
    public void testTypicalUseCaseScenarios() {

        APIInvocationConfig config = new APIInvocationConfig();
        
        // Scenario 1: No retries (default)
        assertEquals(config.getAllowedRetryCount(), 0);
        
        // Scenario 2: Enable retries for resilience
        config.setAllowedRetryCount(3);
        assertEquals(config.getAllowedRetryCount(), 3);
        
        // Scenario 3: High availability scenario with more retries
        config.setAllowedRetryCount(10);
        assertEquals(config.getAllowedRetryCount(), 10);
        
        // Scenario 4: Disable retries again
        config.setAllowedRetryCount(0);
        assertEquals(config.getAllowedRetryCount(), 0);
    }
}
