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

package org.wso2.carbon.identity.external.api.token.handler.internal.service.impl;

import org.wso2.carbon.identity.external.api.client.api.model.APIAuthentication;
import org.wso2.carbon.identity.external.api.token.handler.api.model.TokenRequestContext;
import org.wso2.carbon.identity.external.api.token.handler.internal.granttypes.clientcredentials.ClientCredTokenRequestConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory class for RequestConfig classes for different grant types.
 */
public class TokenRequestConfigFactory {

    private static final Map<APIAuthentication.Type, Class<? extends TokenRequestContext>> tokenRequestConfigMap
            = new HashMap<>();

    private TokenRequestConfigFactory() {

        tokenRequestConfigMap.put(APIAuthentication.Type.BASIC, ClientCredTokenRequestConfig.class);
    }

    /**
     * Get the RequestConfig class for the given grant type.
     *
     * @param grantType Grant type.
     * @return RequestConfig class.
     */
    public static Class<? extends TokenRequestContext> getTokenRequestConfigClass(APIAuthentication.Type grantType) {

        return tokenRequestConfigMap.get(grantType);
    }
}
