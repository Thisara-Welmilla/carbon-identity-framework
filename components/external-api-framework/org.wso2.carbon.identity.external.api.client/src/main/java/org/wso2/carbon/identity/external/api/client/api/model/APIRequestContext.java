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

import java.util.HashMap;
import java.util.Map;

/**
 * Model class for request context.
 */
public class APIRequestContext {

    private final HttpMethod httpMethod;
    private final APIAuthentication apiAuthentication;
    private final String endpointUrl;
    private final Map<String, String> headers;
    private final String payload;

    public APIRequestContext(APIRequestContext.Builder builder) {

        this.httpMethod = builder.httpMethod;
        this.apiAuthentication = builder.apiAuthentication;
        this.endpointUrl = builder.endpointUrl;
        this.headers = builder.headers;
        this.payload = builder.payload;
    }

    public HttpMethod getHttpMethod() {

        return httpMethod;
    }

    public APIAuthentication getApiAuthentication() {

        return apiAuthentication;
    }

    public String getEndpointUrl() {

        return endpointUrl;
    }
    
    public Map<String, String> getHeaders() {

        return headers;
    }
    
    public String getPayload() {

        return payload;
    }

    /**
     * Builder class for APIRequestContext.
     */
    public static class Builder {

        private HttpMethod httpMethod;
        private APIAuthentication apiAuthentication;
        private String endpointUrl;
        private Map<String, String> headers = new HashMap<>();
        private String payload;

        public APIRequestContext.Builder setHttpMethod(HttpMethod httpMethod) {

            this.httpMethod = httpMethod;
            return this;
        }

        public APIRequestContext.Builder setApiAuthentication(APIAuthentication apiAuthentication) {

            this.apiAuthentication = apiAuthentication;
            return this;
        }

        public APIRequestContext.Builder setEndpointUrl(String endpointUrl) {

            this.endpointUrl = endpointUrl;
            return this;
        }

        public APIRequestContext.Builder setHeaders(Map<String, String> headers) {

            this.headers = headers;
            return this;
        }

        public APIRequestContext.Builder setPayload(String payload) {

            this.payload = payload;
            return this;
        }

        public APIRequestContext build() {

            return new APIRequestContext(this);
        }
    }

    /**
     * Enum for property names.
     */
    public enum HttpMethod {

        POST("post"),
        GET("get");

        private final String name;

        HttpMethod(String name) {

            this.name = name;
        }

        public String getName() {

            return name;
        }
    }
}
