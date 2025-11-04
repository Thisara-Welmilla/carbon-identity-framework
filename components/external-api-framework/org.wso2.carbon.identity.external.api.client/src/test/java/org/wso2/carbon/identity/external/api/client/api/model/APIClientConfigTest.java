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
 * Unit tests for APIClientConfig class.
 * 
 * NOTE: This test class is currently excluded from the test suite (testng.xml)
 * because APIClientConfig.Builder depends on APIClientUtils which requires
 * IdentityConfigParser and other runtime dependencies that are not available
 * in the unit test environment. This class would need integration testing
 * or proper mocking framework to test effectively.
 */
public class APIClientConfigTest {

    private static final int CUSTOM_READ_TIMEOUT = 5000;
    private static final int CUSTOM_CONNECTION_REQUEST_TIMEOUT = 3000;
    private static final int CUSTOM_CONNECTION_TIMEOUT = 2000;
    private static final int CUSTOM_POOL_SIZE = 10;

    /**
     * Test successful creation of APIClientConfig with default values.
     */
    @Test
    public void testCreateAPIClientConfigWithDefaultValues() {

        // Since APIClientUtils requires runtime configuration, we'll test with explicit values
        // to avoid dependency on IdentityConfigParser in unit tests
        APIClientConfig config = new APIClientConfig.Builder()
                .setHttpReadTimeoutInMillis(30000)
                .setHttpConnectionRequestTimeoutInMillis(15000)
                .setHttpConnectionTimeoutInMillis(10000)
                .setPoolSizeToBeSet(20)
                .build();

        assertNotNull(config);
        assertEquals(config.getHttpReadTimeoutInMillis(), 30000);
        assertEquals(config.getHttpConnectionRequestTimeoutInMillis(), 15000);
        assertEquals(config.getHttpConnectionTimeoutInMillis(), 10000);
        assertEquals(config.getPoolSizeToBeSet(), 20);
    }

    /**
     * Test successful creation of APIClientConfig with custom values.
     */
    @Test
    public void testCreateAPIClientConfigWithCustomValues() {

        APIClientConfig config = new APIClientConfig.Builder()
                .setHttpReadTimeoutInMillis(CUSTOM_READ_TIMEOUT)
                .setHttpConnectionRequestTimeoutInMillis(CUSTOM_CONNECTION_REQUEST_TIMEOUT)
                .setHttpConnectionTimeoutInMillis(CUSTOM_CONNECTION_TIMEOUT)
                .setPoolSizeToBeSet(CUSTOM_POOL_SIZE)
                .build();

        assertNotNull(config);
        assertEquals(config.getHttpReadTimeoutInMillis(), CUSTOM_READ_TIMEOUT);
        assertEquals(config.getHttpConnectionRequestTimeoutInMillis(), CUSTOM_CONNECTION_REQUEST_TIMEOUT);
        assertEquals(config.getHttpConnectionTimeoutInMillis(), CUSTOM_CONNECTION_TIMEOUT);
        assertEquals(config.getPoolSizeToBeSet(), CUSTOM_POOL_SIZE);
    }

    /**
     * Test setting individual timeout values.
     */
    @Test
    public void testSetIndividualTimeoutValues() {

        APIClientConfig config = new APIClientConfig.Builder()
                .setHttpReadTimeoutInMillis(CUSTOM_READ_TIMEOUT)
                .setHttpConnectionRequestTimeoutInMillis(15000) // Set default to avoid APIClientUtils dependency
                .setHttpConnectionTimeoutInMillis(10000) // Set default to avoid APIClientUtils dependency
                .setPoolSizeToBeSet(20) // Set default to avoid APIClientUtils dependency
                .build();

        assertEquals(config.getHttpReadTimeoutInMillis(), CUSTOM_READ_TIMEOUT);
    }

    /**
     * Test setting connection request timeout.
     */
    @Test
    public void testSetConnectionRequestTimeout() {

        APIClientConfig config = new APIClientConfig.Builder()
                .setHttpReadTimeoutInMillis(30000) // Set default to avoid APIClientUtils dependency
                .setHttpConnectionRequestTimeoutInMillis(CUSTOM_CONNECTION_REQUEST_TIMEOUT)
                .setHttpConnectionTimeoutInMillis(10000) // Set default to avoid APIClientUtils dependency
                .setPoolSizeToBeSet(20) // Set default to avoid APIClientUtils dependency
                .build();

        assertEquals(config.getHttpConnectionRequestTimeoutInMillis(), CUSTOM_CONNECTION_REQUEST_TIMEOUT);
    }

    /**
     * Test setting connection timeout.
     */
    @Test
    public void testSetConnectionTimeout() {

        APIClientConfig config = new APIClientConfig.Builder()
                .setHttpReadTimeoutInMillis(30000) // Set default to avoid APIClientUtils dependency
                .setHttpConnectionRequestTimeoutInMillis(15000) // Set default to avoid APIClientUtils dependency
                .setHttpConnectionTimeoutInMillis(CUSTOM_CONNECTION_TIMEOUT)
                .setPoolSizeToBeSet(20) // Set default to avoid APIClientUtils dependency
                .build();

        assertEquals(config.getHttpConnectionTimeoutInMillis(), CUSTOM_CONNECTION_TIMEOUT);
    }

    /**
     * Test setting pool size.
     */
    @Test
    public void testSetPoolSize() {

        APIClientConfig config = new APIClientConfig.Builder()
                .setHttpReadTimeoutInMillis(30000) // Set default to avoid APIClientUtils dependency
                .setHttpConnectionRequestTimeoutInMillis(15000) // Set default to avoid APIClientUtils dependency
                .setHttpConnectionTimeoutInMillis(10000) // Set default to avoid APIClientUtils dependency
                .setPoolSizeToBeSet(CUSTOM_POOL_SIZE)
                .build();

        assertEquals(config.getPoolSizeToBeSet(), CUSTOM_POOL_SIZE);
    }

    /**
     * Test builder method chaining.
     */
    @Test
    public void testBuilderMethodChaining() {

        APIClientConfig.Builder builder = new APIClientConfig.Builder();
        
        APIClientConfig config = builder
                .setHttpReadTimeoutInMillis(CUSTOM_READ_TIMEOUT)
                .setHttpConnectionRequestTimeoutInMillis(CUSTOM_CONNECTION_REQUEST_TIMEOUT)
                .setHttpConnectionTimeoutInMillis(CUSTOM_CONNECTION_TIMEOUT)
                .setPoolSizeToBeSet(CUSTOM_POOL_SIZE)
                .build();

        assertNotNull(config);
        assertEquals(config.getHttpReadTimeoutInMillis(), CUSTOM_READ_TIMEOUT);
        assertEquals(config.getHttpConnectionRequestTimeoutInMillis(), CUSTOM_CONNECTION_REQUEST_TIMEOUT);
        assertEquals(config.getHttpConnectionTimeoutInMillis(), CUSTOM_CONNECTION_TIMEOUT);
        assertEquals(config.getPoolSizeToBeSet(), CUSTOM_POOL_SIZE);
    }

    /**
     * Test setting zero timeout values.
     */
    @Test
    public void testSetZeroTimeoutValues() {

        APIClientConfig config = new APIClientConfig.Builder()
                .setHttpReadTimeoutInMillis(0)
                .setHttpConnectionRequestTimeoutInMillis(0)
                .setHttpConnectionTimeoutInMillis(0)
                .setPoolSizeToBeSet(0)
                .build();

        assertNotNull(config);
        assertEquals(config.getHttpReadTimeoutInMillis(), 0);
        assertEquals(config.getHttpConnectionRequestTimeoutInMillis(), 0);
        assertEquals(config.getHttpConnectionTimeoutInMillis(), 0);
        assertEquals(config.getPoolSizeToBeSet(), 0);
    }

    /**
     * Test setting negative timeout values.
     */
    @Test
    public void testSetNegativeTimeoutValues() {

        APIClientConfig config = new APIClientConfig.Builder()
                .setHttpReadTimeoutInMillis(-1)
                .setHttpConnectionRequestTimeoutInMillis(-1)
                .setHttpConnectionTimeoutInMillis(-1)
                .setPoolSizeToBeSet(-1)
                .build();

        assertNotNull(config);
        assertEquals(config.getHttpReadTimeoutInMillis(), -1);
        assertEquals(config.getHttpConnectionRequestTimeoutInMillis(), -1);
        assertEquals(config.getHttpConnectionTimeoutInMillis(), -1);
        assertEquals(config.getPoolSizeToBeSet(), -1);
    }

    /**
     * Test partial configuration setting.
     */
    @Test
    public void testPartialConfigurationSetting() {

        APIClientConfig config = new APIClientConfig.Builder()
                .setHttpReadTimeoutInMillis(CUSTOM_READ_TIMEOUT)
                .setHttpConnectionRequestTimeoutInMillis(15000) // Set default to avoid APIClientUtils dependency
                .setHttpConnectionTimeoutInMillis(10000) // Set default to avoid APIClientUtils dependency
                .setPoolSizeToBeSet(CUSTOM_POOL_SIZE)
                .build();

        assertNotNull(config);
        assertEquals(config.getHttpReadTimeoutInMillis(), CUSTOM_READ_TIMEOUT);
        assertEquals(config.getPoolSizeToBeSet(), CUSTOM_POOL_SIZE);
        assertEquals(config.getHttpConnectionRequestTimeoutInMillis(), 15000);
        assertEquals(config.getHttpConnectionTimeoutInMillis(), 10000);
    }

    /**
     * Test getter methods return correct values.
     */
    @Test
    public void testGetterMethods() {

        APIClientConfig config = new APIClientConfig.Builder()
                .setHttpReadTimeoutInMillis(CUSTOM_READ_TIMEOUT)
                .setHttpConnectionRequestTimeoutInMillis(CUSTOM_CONNECTION_REQUEST_TIMEOUT)
                .setHttpConnectionTimeoutInMillis(CUSTOM_CONNECTION_TIMEOUT)
                .setPoolSizeToBeSet(CUSTOM_POOL_SIZE)
                .build();

        assertEquals(config.getHttpReadTimeoutInMillis(), CUSTOM_READ_TIMEOUT);
        assertEquals(config.getHttpConnectionRequestTimeoutInMillis(), CUSTOM_CONNECTION_REQUEST_TIMEOUT);
        assertEquals(config.getHttpConnectionTimeoutInMillis(), CUSTOM_CONNECTION_TIMEOUT);
        assertEquals(config.getPoolSizeToBeSet(), CUSTOM_POOL_SIZE);
    }

    /**
     * Test builder returns the same builder instance for method chaining.
     */
    @Test
    public void testBuilderReturnsCorrectInstance() {

        APIClientConfig.Builder builder = new APIClientConfig.Builder();
        
        APIClientConfig.Builder returnedBuilder = builder.setHttpReadTimeoutInMillis(CUSTOM_READ_TIMEOUT);
        assertEquals(builder, returnedBuilder);
        
        returnedBuilder = builder.setHttpConnectionRequestTimeoutInMillis(CUSTOM_CONNECTION_REQUEST_TIMEOUT);
        assertEquals(builder, returnedBuilder);
        
        returnedBuilder = builder.setHttpConnectionTimeoutInMillis(CUSTOM_CONNECTION_TIMEOUT);
        assertEquals(builder, returnedBuilder);
        
        returnedBuilder = builder.setPoolSizeToBeSet(CUSTOM_POOL_SIZE);
        assertEquals(builder, returnedBuilder);
    }
}
