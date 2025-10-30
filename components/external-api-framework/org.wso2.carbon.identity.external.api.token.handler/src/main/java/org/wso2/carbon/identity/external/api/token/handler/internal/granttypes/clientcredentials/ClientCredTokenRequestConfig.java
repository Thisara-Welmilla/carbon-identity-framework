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

package org.wso2.carbon.identity.external.api.token.handler.internal.granttypes.clientcredentials;

import org.wso2.carbon.identity.external.api.token.handler.api.model.TokenRequestContext;

/**
 * Model class for Client Credentials Token Request Configuration.
 */
public class ClientCredTokenRequestConfig extends TokenRequestContext {

    private String payload;

    public ClientCredTokenRequestConfig(Builder builder) {

        super(builder);
        this.payload = builder.payload;
    }

    @Override
    public String getPayLoad() {

        return payload;
    }

    /**
     * Builder class for ClientCredTokenRequestConfig.
     */
    public static class Builder extends TokenRequestContext.Builder {

        private String payload;

        public TokenRequestContext build() {

            this.payload = "payload";
            return new ClientCredTokenRequestConfig(this);
        }
    }
}
