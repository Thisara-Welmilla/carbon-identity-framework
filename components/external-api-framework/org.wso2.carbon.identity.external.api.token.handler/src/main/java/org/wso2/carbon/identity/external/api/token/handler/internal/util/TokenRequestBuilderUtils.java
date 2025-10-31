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

package org.wso2.carbon.identity.external.api.token.handler.internal.util;

import org.wso2.carbon.identity.external.api.client.api.exception.APIClientRequestException;
import org.wso2.carbon.identity.external.api.client.api.model.APIAuthentication;
import org.wso2.carbon.identity.external.api.token.handler.api.model.GrantContext;
import org.wso2.carbon.identity.external.api.token.handler.api.model.GrantContext.Property;
import org.wso2.carbon.identity.external.api.token.handler.api.model.TokenRequestContext;

import java.util.Map;

/**
 * Utility class for building token request related objects.
 */
public class TokenRequestBuilderUtils {

    /**
     * Build APIAuthentication for token request based on the grant type context.
     *
     * @param requestContext Token request context.
     * @return APIAuthentication instance.
     * @throws APIClientRequestException If an error occurs while building the authentication.
     */
    public static APIAuthentication buildTokenRequestAPIAuthentication(TokenRequestContext requestContext)
            throws APIClientRequestException {

        GrantContext grantContext = requestContext.getGrantContext();
        switch (grantContext.getGrantType()) {
            case CLIENT_CREDENTIALS:
                return new APIAuthentication.Builder()
                        .authType(APIAuthentication.AuthType.BASIC)
                        .properties(Map.of(
                                APIAuthentication.Property.USERNAME.getName(),
                                    grantContext.getProperties().get(Property.CLIENT_ID.getName()),
                                APIAuthentication.Property.PASSWORD.getName(),
                                    grantContext.getProperties().get(Property.CLIENT_SECRET.getName())
                        ))
                        .build();
            default:
                throw new IllegalArgumentException("Unsupported authentication type: " +
                        requestContext.getGrantContext());
        }
    }

    /**
     * Build token request payload based on the grant type context.
     *
     * @param requestContext Token request context.
     * @return Token request payload as a string.
     */
    public static String buildTokenRequestPayload(TokenRequestContext requestContext) {

        String template;
        GrantContext grantContext = requestContext.getGrantContext();
        switch (grantContext.getGrantType()) {
            case CLIENT_CREDENTIALS:
                template = PayloadTemplateByType.CLIENT_CREDENTIALS.getPayload();
                return String.format(template,
                        grantContext.getProperties().get(Property.CLIENT_ID.getName()),
                        grantContext.getProperties().get(Property.CLIENT_SECRET.getName()),
                        grantContext.getProperties().get(Property.SCOPE.getName()));
            default:
                throw new IllegalArgumentException("Unsupported authentication type: " +
                        requestContext.getGrantContext());
        }
    }

    /**
     * Enum for payload templates by grant type.
     */
    public enum PayloadTemplateByType {

        // refresh grant only for here for templates
        CLIENT_CREDENTIALS("client_credential"),;

        private final String payload;

        PayloadTemplateByType(String payload) {

            this.payload = payload;
        }

        public String getPayload() {

            return payload;
        }
    }
}
