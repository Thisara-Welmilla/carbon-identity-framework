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

package org.wso2.carbon.identity.external.api.token.handler.api;

import org.wso2.carbon.identity.external.api.client.api.exception.APIClientInvocationException;
import org.wso2.carbon.identity.external.api.client.api.exception.APIClientRequestException;
import org.wso2.carbon.identity.external.api.client.api.model.APIClientConfig;
import org.wso2.carbon.identity.external.api.client.api.model.APIInvocationConfig;
import org.wso2.carbon.identity.external.api.client.api.model.APIRequestContext;
import org.wso2.carbon.identity.external.api.client.api.model.APIResponse;
import org.wso2.carbon.identity.external.api.client.api.service.AbstractAPIClientManger;
import org.wso2.carbon.identity.external.api.token.handler.api.model.TokenRequestContext;
import org.wso2.carbon.identity.external.api.token.handler.api.model.TokenResponse;

/**
 * Service class for acquiring tokens using different grant types.
 */
public class TokenAcquirerService extends AbstractAPIClientManger {

    TokenRequestContext tokenRequestContext;
    APIInvocationConfig apiInvocationConfig = new APIInvocationConfig();

    public TokenAcquirerService(APIClientConfig apiClientConfig) {

        super(apiClientConfig);
    }

    public void setTokenRequestContext(TokenRequestContext tokenRequestContext) {

        this.tokenRequestContext = tokenRequestContext;
    }

    public void setApiInvocationConfig(APIInvocationConfig apiInvocationConfig) {

        this.apiInvocationConfig = apiInvocationConfig;
    }

    /**
     * Get the token using the given token request configuration.
     *
     * @return Token response.
     */
    public TokenResponse getNewAccessToken() throws APIClientInvocationException, APIClientRequestException {

        APIRequestContext apiRequestContext = tokenRequestContext.buildAPIRequestContext();
        return (TokenResponse) callAPI(apiRequestContext, apiInvocationConfig);
    }

    public TokenResponse getNewAccessTokenFromRefreshGrant()
            throws APIClientInvocationException, APIClientRequestException {

        APIRequestContext apiRequestContext = tokenRequestContext.buildAPIRequestContext();
        return (TokenResponse) callAPI(apiRequestContext, apiInvocationConfig);
    }

    @Override
    protected APIResponse handleResponse(APIResponse response) {

        return new TokenResponse.Builder(response).build();
    }
}
