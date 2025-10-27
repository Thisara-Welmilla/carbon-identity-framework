package org.wso2.carbon.identity.external.api.tokenAcquirer.api.models;

import org.apache.hc.core5.http.Method;
import org.wso2.carbon.identity.external.api.client.api.model.APIAuthentication;
import org.wso2.carbon.identity.external.api.client.api.model.APIRequestContext;

import java.util.HashMap;
import java.util.Map;

public class TokenRequestContext {

    private final APIAuthentication apiAuthentication;
    private final String tokenEndpointUrl;
    private final Map<String, String> headers;

    public TokenRequestContext(TokenRequestContext.Builder builder) {

        this.apiAuthentication = builder.apiAuthentication;
        this.tokenEndpointUrl = builder.endpointUrl;
        this.headers = builder.headers;
    }

    public APIAuthentication getApiAuthentication() {

        return apiAuthentication;
    }

    public String getTokenEndpointUrl() {

        return tokenEndpointUrl;
    }

    public Map<String, String> getHeaders() {

        return headers;
    }

    public String getPayLoad() {

        return null;
    }

    public APIRequestContext build(TokenRequestContext tokenRequestContext) {

        APIRequestContext.Builder requestContextBuilder = new APIRequestContext.Builder()
                .setHttpMethod(Method.POST)
                .setHeaders(tokenRequestContext.getHeaders())
                .setEndpointUrl(tokenRequestContext.getTokenEndpointUrl())
                .setApiAuthentication(tokenRequestContext.getApiAuthentication())
                .setPayload(tokenRequestContext.getPayLoad());

        return requestContextBuilder.build();
    }

    /**
     * Builder class for APIRequestContext.
     */
    public static class Builder {

        private APIAuthentication apiAuthentication;
        private String endpointUrl;
        private Map<String, String> headers = new HashMap<>();

        public TokenRequestContext.Builder setApiAuthentication(APIAuthentication apiAuthentication) {

            this.apiAuthentication = apiAuthentication;
            return this;
        }

        public TokenRequestContext.Builder setEndpointUrl(String endpointUrl) {

            this.endpointUrl = endpointUrl;
            return this;
        }

        public TokenRequestContext.Builder setHeaders(Map<String, String> headers) {

            this.headers = headers;
            return this;
        }

        public TokenRequestContext build() {

            return new TokenRequestContext(this);
        }
    }
}

