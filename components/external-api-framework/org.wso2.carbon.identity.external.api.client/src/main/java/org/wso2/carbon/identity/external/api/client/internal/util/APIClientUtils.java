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

package org.wso2.carbon.identity.external.api.client.internal.util;

import org.wso2.carbon.identity.core.util.IdentityConfigParser;

import static org.wso2.carbon.identity.external.api.client.internal.constant.APIClientConstants.DEFAULT_HTTP_CONNECTION_REQUEST_TIMEOUT_IN_MILLIS;
import static org.wso2.carbon.identity.external.api.client.internal.constant.APIClientConstants.DEFAULT_HTTP_CONNECTION_TIMEOUT_IN_MILLIS;
import static org.wso2.carbon.identity.external.api.client.internal.constant.APIClientConstants.DEFAULT_HTTP_READ_TIMEOUT_IN_MILLIS;
import static org.wso2.carbon.identity.external.api.client.internal.constant.APIClientConstants.DEFAULT_POOL_SIZE_TO_BE_SET;

/**
 * Utility class for API Client component.
 */
public class APIClientUtils {

    private static final int httpReadTimeoutInMillis;
    private static final int httpConnectionRequestTimeoutInMillis;
    private static final int httpConnectionTimeoutInMillis;
    private static final int poolSizeToBeSet;

    static {
        httpReadTimeoutInMillis = getProperty(DEFAULT_HTTP_READ_TIMEOUT_IN_MILLIS);
        httpConnectionRequestTimeoutInMillis = getProperty(DEFAULT_HTTP_CONNECTION_REQUEST_TIMEOUT_IN_MILLIS);
        httpConnectionTimeoutInMillis = getProperty(DEFAULT_HTTP_CONNECTION_TIMEOUT_IN_MILLIS);
        poolSizeToBeSet = getProperty(DEFAULT_POOL_SIZE_TO_BE_SET);
    }

    private static int getProperty(String propertyName) {

        return Integer.parseInt(IdentityConfigParser.getInstance().getConfiguration().get(propertyName).toString());
    }

    public static int getDefaultHttpReadTimeoutInMillis() {

        return httpReadTimeoutInMillis;
    }

    public static int getDefaultHttpConnectionRequestTimeoutInMillis() {

        return httpConnectionRequestTimeoutInMillis;
    }

    public static int getDefaultHttpConnectionTimeoutInMillis() {

        return httpConnectionTimeoutInMillis;
    }

    public static int getDefaultPoolSizeToBeSet() {

        return poolSizeToBeSet;
    }
}
