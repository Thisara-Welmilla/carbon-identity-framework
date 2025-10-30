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

/**
 * Model class for API Client Configuration.
 */
public class APIClientConfig {

    protected int httpReadTimeoutInMillis;
    protected int httpConnectionRequestTimeoutInMillis;
    protected int httpConnectionTimeoutInMillis;
    protected int httpRequestRetryCount;
    protected int poolSizeToBeSet;

    public APIClientConfig(Builder builder) {

        this.httpReadTimeoutInMillis = builder.httpReadTimeoutInMillis;
        this.httpConnectionRequestTimeoutInMillis = builder.httpConnectionRequestTimeoutInMillis;
        this.httpConnectionTimeoutInMillis = builder.httpConnectionTimeoutInMillis;
        this.httpRequestRetryCount = builder.httpRequestRetryCount;
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

    public int getHttpRequestRetryCount() {

        return httpRequestRetryCount;
    }

    public int getPoolSizeToBeSet() {

        return poolSizeToBeSet;
    }

    /**
     * Builder class for APIClientConfig.
     */
    public static class Builder {

        protected int httpReadTimeoutInMillis = 10000;
        protected int httpConnectionRequestTimeoutInMillis = 10000;
        protected int httpConnectionTimeoutInMillis = 10000;
        protected int httpRequestRetryCount = 0;
        protected int poolSizeToBeSet = 100;

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

        public APIClientConfig.Builder setHttpRequestRetryCount(int httpRequestRetryCount) {

            this.httpRequestRetryCount = httpRequestRetryCount;
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
