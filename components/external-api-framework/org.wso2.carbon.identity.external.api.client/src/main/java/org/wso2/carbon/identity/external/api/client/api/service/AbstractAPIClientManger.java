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

package org.wso2.carbon.identity.external.api.client.api.service;

import org.wso2.carbon.identity.external.api.client.api.exception.APIClientException;
import org.wso2.carbon.identity.external.api.client.api.exception.APIClientResponseException;
import org.wso2.carbon.identity.external.api.client.api.model.APIClientConfig;
import org.wso2.carbon.identity.external.api.client.api.model.APIInvocationConfig;
import org.wso2.carbon.identity.external.api.client.api.model.APIRequestContext;
import org.wso2.carbon.identity.external.api.client.api.model.APIResponse;
import org.wso2.carbon.identity.external.api.client.internal.service.APIClient;

/**
 * Abstract class for API Client Manager implementations.
 */
public abstract class AbstractAPIClientManger {

    APIClientConfig apiClientConfig;
    APIClient apiClient;

    public AbstractAPIClientManger(APIClientConfig apiClientConfig) {

        this.apiClientConfig = apiClientConfig;
        this.apiClient = new APIClient(this.apiClientConfig);
    }

    public APIResponse callAPI(APIRequestContext requestContext, APIInvocationConfig apiInvocationConfig)
            throws APIClientException {

        APIResponse response = apiClient.callAPI(requestContext, apiInvocationConfig);
        return handleResponse(response);
    }

    protected abstract APIResponse handleResponse(APIResponse response) throws APIClientResponseException;
}
