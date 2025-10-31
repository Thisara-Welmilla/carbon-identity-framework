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

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.external.api.client.api.exception.APIClientRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Authentication class which hold supported authentication types and their properties.
 */
public class APIAuthentication {

    private final AuthType authType;
    private final List<APIAuthProperty> properties;


    public APIAuthentication(Builder builder) throws APIClientRequestException {

        this.authType = builder.authType;
        this.properties = builder.properties;
    }

    public AuthType getType() {

        return authType;
    }

    public List<APIAuthProperty> getProperties() {

        return properties;
    }

    public APIAuthProperty getProperty(Property propertyName) {

        return this.properties.stream()
                .filter(property -> propertyName.getName().equals(property.getName()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Builder class for APIAuthentication.
     */
    public static class Builder {

        private AuthType authType;
        private Map<String, String> authPropertiesMap;
        private final List<APIAuthProperty> properties = new ArrayList<>();

        public Builder authType(AuthType authType) {

            this.authType = authType;
            return this;
        }

        public Builder properties(Map<String, String> authPropertiesMap) {

            this.authPropertiesMap = authPropertiesMap;
            return this;
        }

        public APIAuthentication build() throws APIClientRequestException {

            if (authType == null) {
                throw new APIClientRequestException("Authentication authType must be provided for the authentication " +
                        "configuration.");
            }

            switch (authType) {
                case BASIC:
                    properties.add(buildAuthProperty(AuthType.BASIC, Property.USERNAME.getName()));
                    properties.add(buildAuthProperty(AuthType.BASIC, Property.PASSWORD.getName()));
                    break;
                case BEARER:
                    properties.add(buildAuthProperty(AuthType.BEARER, Property.ACCESS_TOKEN.getName()));
                    break;
                case API_KEY:
                    properties.add(buildAuthProperty(AuthType.API_KEY, Property.HEADER.getName()));
                    properties.add(buildAuthProperty(AuthType.API_KEY, Property.VALUE.getName()));
                    break;
                case NONE:
                    break;
                default:
                    throw new APIClientRequestException(String.format("An invalid authentication authType '%s' is " +
                            "provided for the authentication configuration of the endpoint.", authType.name()));
            }
            return new APIAuthentication(this);
        }

        private APIAuthProperty buildAuthProperty(AuthType authType, String propertyName)
                throws APIClientRequestException {

            if (authPropertiesMap != null && authPropertiesMap.containsKey(propertyName)) {
                String propValue = authPropertiesMap.get(propertyName);
                if (StringUtils.isNotBlank(propValue)) {
                    return new APIAuthProperty.Builder(propertyName, propValue).build();
                }
                throw new APIClientRequestException(String.format("The Property %s cannot be blank.", propertyName));
            }

            throw new APIClientRequestException(String.format("The property %s must be provided as an authentication " +
                    "property for the %s authentication authType.", propertyName, authType.name()));
        }
    }

    /**
     * Authentication AuthType.
     */
    public enum AuthType {

        NONE("NONE"),
        BEARER("BEARER"),
        BASIC("BASIC"),
        API_KEY("API_KEY");

        private final String name;

        AuthType(String name) {

            this.name = name;
        }

        public String getName() {

            return name;
        }

        public static AuthType valueOfName(String name) {

            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Authentication authType cannot be null or empty.");
            }

            for (AuthType authType : AuthType.values()) {
                if (authType.name.equalsIgnoreCase(name)) {
                    return authType;
                }
            }
            throw new IllegalArgumentException("Invalid authentication authType: " + name);
        }
    }

    /**
     * Authentication Property Enum.
     */
    public enum Property {

        USERNAME("username"),
        PASSWORD("password"),
        HEADER("header"),
        VALUE("value"),
        ACCESS_TOKEN("accessToken");

        private final String name;

        Property(String name) {

            this.name = name;
        }

        public String getName() {

            return name;
        }
    }
}

