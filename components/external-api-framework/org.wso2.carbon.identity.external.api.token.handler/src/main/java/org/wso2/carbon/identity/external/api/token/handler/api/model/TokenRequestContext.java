package org.wso2.carbon.identity.external.api.token.handler.api.model;

import org.wso2.carbon.identity.external.api.client.api.exception.APIClientRequestException;
import org.wso2.carbon.identity.external.api.client.api.model.APIAuthentication;
import org.wso2.carbon.identity.external.api.client.api.model.APIRequestContext;
import org.wso2.carbon.identity.external.api.token.handler.internal.util.TokenRequestBuilderUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Model class for Token Request Context.
 */
public class TokenRequestContext {

    private final GrantContext grantContext;
    private final String tokenEndpointUrl;
    private final Map<String, String> headers;
    private String payload;

    public TokenRequestContext(TokenRequestContext.Builder builder) {

        this.grantContext = builder.grantContext;
        this.tokenEndpointUrl = builder.endpointUrl;
        this.headers = builder.headers;
        this.payload = builder.payload;
    }

    public GrantContext getGrantContext() {

        return grantContext;
    }

    public String getTokenEndpointUrl() {

        return tokenEndpointUrl;
    }

    public Map<String, String> getHeaders() {

        return headers;
    }

    public void setPayLoad(String payload) {

        this.payload = payload;
    }

    public String getPayLoad() {

        return payload;
    }

    public APIRequestContext buildAPIRequestContext() throws APIClientRequestException {

        // if payload null
        APIAuthentication authentication = TokenRequestBuilderUtils.buildTokenRequestAPIAuthentication(this);
        String payload = TokenRequestBuilderUtils.buildTokenRequestPayload(this);
        APIRequestContext.Builder requestContextBuilder = new APIRequestContext.Builder()
                .setHttpMethod(APIRequestContext.HttpMethod.POST)
                .setHeaders(headers)
                .setEndpointUrl(tokenEndpointUrl)
                .setApiAuthentication(authentication)
                .setPayload(payload);

        return requestContextBuilder.build();
    }

    /**
     * Builder class for APIRequestContext.
     */
    public static class Builder {

        private GrantContext grantContext;
        private String endpointUrl;
        private Map<String, String> headers = new HashMap<>();
        private String payload;

        public Builder grantContext(GrantContext grantContext) {

            this.grantContext = grantContext;
            return this;
        }

        public Builder endpointUrl(String endpointUrl) {

            this.endpointUrl = endpointUrl;
            return this;
        }

        public Builder headers(Map<String, String> headers) {

            this.headers = headers;
            return this;
        }

        public Builder payload(String payload) {

            this.payload = payload;
            return this;
        }

        public TokenRequestContext build() {

            // do validations
            return new TokenRequestContext(this);
        }
    }
}

