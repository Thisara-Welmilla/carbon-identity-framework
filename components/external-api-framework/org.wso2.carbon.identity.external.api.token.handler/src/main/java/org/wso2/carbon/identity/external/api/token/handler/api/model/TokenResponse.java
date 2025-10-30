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

package org.wso2.carbon.identity.external.api.token.handler.api.model;

/**
 * Model class for Token Response.
 */
public class TokenResponse {

    private final String accessToken;
    private final String refreshToken;

    private TokenResponse(Builder builder) {

        this.accessToken = builder.accessToken;
        this.refreshToken = builder.refreshToken;
    }

    public String getAccessToken() {

        return accessToken;
    }

    public String getRefreshToken() {

        return refreshToken;
    }

    /**
     * Builder class for TokenResponse.
     */
    public static class Builder {

        private String accessToken;
        private String refreshToken;

        public Builder(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }

        public Builder setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public TokenResponse build() {

            return new TokenResponse(this);
        }
    }
}
