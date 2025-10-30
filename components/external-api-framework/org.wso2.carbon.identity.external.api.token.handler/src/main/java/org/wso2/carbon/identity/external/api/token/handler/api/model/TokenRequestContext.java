package org.wso2.carbon.identity.external.api.token.handler.api.model;

import org.wso2.carbon.identity.external.api.client.api.model.APIAuthentication;
import org.wso2.carbon.identity.external.api.client.api.model.APIRequestContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Model class for Token Request Context.
 */
public class TokenRequestContext {

    private final APIAuthentication apiAuthentication;
    private final String tokenEndpointUrl;
    private final Map<String, String> headers;
    private final String payload;

    public TokenRequestContext(TokenRequestContext.Builder builder) {

        this.apiAuthentication = builder.apiAuthentication;
        this.tokenEndpointUrl = builder.endpointUrl;
        this.headers = builder.headers;
        this.payload = builder.payload;
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

    public String setPayLoad() {

        return payload;
    }

    public String getPayLoad() {

        return payload;
    }

    public APIRequestContext build(TokenRequestContext tokenRequestContext) {

        APIRequestContext.Builder requestContextBuilder = new APIRequestContext.Builder()
                .setHttpMethod(APIRequestContext.HttpMethod.POST)
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
        private String payload;

        public TokenRequestContext.Builder apiAuthentication(APIAuthentication apiAuthentication) {

            this.apiAuthentication = apiAuthentication;
            return this;
        }

        public TokenRequestContext.Builder endpointUrl(String endpointUrl) {

            this.endpointUrl = endpointUrl;
            return this;
        }

        public TokenRequestContext.Builder headers(Map<String, String> headers) {

            this.headers = headers;
            return this;
        }

        public TokenRequestContext.Builder payload(String payload) {

            this.payload = payload;
            return this;
        }

        public TokenRequestContext build() {

            return new TokenRequestContext(this);
        }
    }
}

