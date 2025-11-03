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

package org.wso2.carbon.identity.external.api.client.internal.constant;

/**
 * Constants for API Client component.
 */
public class APIClientConstants {

    // Default HTTP Client configurations
    public static final String DEFAULT_HTTP_READ_TIMEOUT_IN_MILLIS =
            "ExternalAPIClient.HTTPClient.HTTPReadTimeout";
    public static final String DEFAULT_HTTP_CONNECTION_REQUEST_TIMEOUT_IN_MILLIS =
            "ExternalAPIClient.HTTPClient.HTTPConnectionRequestTimeout";
    public static final String DEFAULT_HTTP_CONNECTION_TIMEOUT_IN_MILLIS =
            "ExternalAPIClient.HTTPClient.HTTPConnectionTimeout";
    public static final String DEFAULT_POOL_SIZE_TO_BE_SET =
            "ExternalAPIClient.HTTPClient.HTTPConnectionPoolSize";
}
