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

import org.wso2.carbon.identity.external.api.client.internal.util.APIClientUtils;

/**
 * Model class for API Client Configuration.
 */
public class APIClientConfig {

    protected int httpReadTimeoutInMillis;
    protected int httpConnectionRequestTimeoutInMillis;
    protected int httpConnectionTimeoutInMillis;
    protected int poolSizeToBeSet;

    public APIClientConfig(Builder builder) {

        this.httpReadTimeoutInMillis = builder.httpReadTimeoutInMillis;
        this.httpConnectionRequestTimeoutInMillis = builder.httpConnectionRequestTimeoutInMillis;
        this.httpConnectionTimeoutInMillis = builder.httpConnectionTimeoutInMillis;
        this.poolSizeToBeSet = builder.poolSizeToBeSet;
    }

    public int getHttpReadTimeoutInMillis() {

        return httpReadTimeoutInMillis;
    }

    public int getHttpConnectionRequestTimeoutInMillis() {

        return httpConnectionRequestTimeoutInMillis;
    }

    public int getHttpConnectionTimeoutInMillis() {

        return httpConnectionTimeoutInMillis;
    }

    public int getPoolSizeToBeSet() {

        return poolSizeToBeSet;
    }

    /**
     * Builder class for APIClientConfig.
     */
    public static class Builder {

        protected int httpReadTimeoutInMillis = APIClientUtils.getDefaultHttpReadTimeoutInMillis();
        protected int httpConnectionRequestTimeoutInMillis =
                APIClientUtils.getDefaultHttpConnectionRequestTimeoutInMillis();
        protected int httpConnectionTimeoutInMillis = APIClientUtils.getDefaultHttpConnectionRequestTimeoutInMillis();
        protected int poolSizeToBeSet = APIClientUtils.getDefaultPoolSizeToBeSet();

        public APIClientConfig.Builder setHttpReadTimeoutInMillis(int httpReadTimeoutInMillis) {

            this.httpReadTimeoutInMillis = httpReadTimeoutInMillis;
            return this;
        }

        public APIClientConfig.Builder setHttpConnectionRequestTimeoutInMillis(
                int connectionRequestTimeoutInMillis) {

            this.httpConnectionRequestTimeoutInMillis = connectionRequestTimeoutInMillis;
            return this;
        }

        public APIClientConfig.Builder setHttpConnectionTimeoutInMillis(int connectionTimeoutInMillis) {

            this.httpConnectionTimeoutInMillis = connectionTimeoutInMillis;
            return this;
        }

        public APIClientConfig.Builder setPoolSizeToBeSet(int poolSizeToBeSet) {

            this.poolSizeToBeSet = poolSizeToBeSet;
            return this;
        }

        public APIClientConfig build() {

            return new APIClientConfig(this);
        }
    }
}
