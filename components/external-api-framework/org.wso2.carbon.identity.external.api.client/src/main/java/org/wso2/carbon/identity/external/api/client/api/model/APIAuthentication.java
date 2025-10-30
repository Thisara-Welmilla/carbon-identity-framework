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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Authentication class which hold supported authentication types and their properties.
 */
public class APIAuthentication {

    private final Type type;
    private List<APIAuthProperty> properties = null;

    public APIAuthentication(BasicAuthBuilder basicAuthBuilder) {

        this.type = basicAuthBuilder.type;
        this.properties = basicAuthBuilder.properties;
    }

    public APIAuthentication(BearerAuthBuilder bearerAuthBuilder) {

        this.type = bearerAuthBuilder.type;
        this.properties = bearerAuthBuilder.properties;
    }

    public APIAuthentication(NoneAuthBuilder noneAuthBuilder) {

        this.type = noneAuthBuilder.type;
        this.properties = noneAuthBuilder.properties;
    }

    public Type getType() {

        return type;
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
     * Basic Authentication builder.
     */
    public static class BasicAuthBuilder {

        private final Type type;
        private final List<APIAuthProperty> properties = new ArrayList<>();

        public BasicAuthBuilder(String username, String password) {
            this.type = Type.BASIC;
            this.properties.add(new APIAuthProperty.AuthPropertyBuilder()
                    .name(Property.USERNAME.getName()).value(username).build());
            this.properties.add(new APIAuthProperty.AuthPropertyBuilder()
                    .name(Property.PASSWORD.getName()).value(password).build());
        }

        public APIAuthentication build() {

            return new APIAuthentication(this);
        }
    }

    /**
     * Bearer Authentication builder.
     */
    public static class BearerAuthBuilder {

        private final Type type;
        private final List<APIAuthProperty> properties = new ArrayList<>();

        public BearerAuthBuilder(String accessToken) {
            this.type = Type.BEARER;
            this.properties.add(new APIAuthProperty.AuthPropertyBuilder()
                    .name(Property.ACCESS_TOKEN.getName()).value(accessToken).build());
        }

        public APIAuthentication build() {

            return new APIAuthentication(this);
        }
    }

    /**
     * None Authentication builder.
     */
    public static class NoneAuthBuilder {

        private final Type type;
        private final List<APIAuthProperty> properties = new ArrayList<>();

        public NoneAuthBuilder() {

            this.type = Type.NONE;
        }

        public APIAuthentication build() {

            return new APIAuthentication(this);
        }
    }

    /**
     * This builder build endpoint by taking the authentication type and properties as input.
     */
    public static class AuthenticationBuilder {

        private Type authType;
        private Map<String, String> authPropertiesMap;

        public AuthenticationBuilder type(Type type) {

            this.authType = type;
            return this;
        }

        public AuthenticationBuilder properties(Map<String, String> authPropertiesMap) {

            this.authPropertiesMap = authPropertiesMap;
            return this;
        }

        public APIAuthentication build() {

            if (authType == null) {
                throw new IllegalArgumentException("Authentication type must be provided for the authentication " +
                        "configuration of the endpoint.");
            }
            switch (authType) {
                case BASIC:
                    return new APIAuthentication.BasicAuthBuilder(
                            getProperty(Type.BASIC, authPropertiesMap, Property.USERNAME.getName()),
                            getProperty(Type.BASIC, authPropertiesMap, Property.PASSWORD.getName())).build();
                case BEARER:
                    return new APIAuthentication.BearerAuthBuilder(
                            getProperty(Type.BEARER, authPropertiesMap, Property.ACCESS_TOKEN.getName())).build();
                case NONE:
                    return new APIAuthentication.NoneAuthBuilder().build();
                default:
                    throw new IllegalArgumentException(String.format("An invalid authentication type '%s' is " +
                            "provided for the authentication configuration of the endpoint.", authType.name()));
            }
        }

        private String getProperty(APIAuthentication.Type authType,  Map<String, String> actionEndpointProperties,
                                   String propertyName) {

            if (actionEndpointProperties != null && actionEndpointProperties.containsKey(propertyName)) {
                String propValue = actionEndpointProperties.get(propertyName);
                if (StringUtils.isNotBlank(propValue)) {
                    return propValue;
                }
                throw new IllegalArgumentException(String.format("The Property %s cannot be blank.", propertyName));
            }

            throw new NoSuchElementException(String.format("The property %s must be provided as an authentication " +
                    "property for the %s authentication type.", propertyName, authType.name()));
        }
    }

    /**
     * Authentication Type.
     */
    public enum Type {

        NONE("none", "NONE"),
        BEARER("bearer", "BEARER"),
        BASIC("basic", "BASIC");

        private final String pathParam;
        private final String name;

        Type(String pathParam, String name) {

            this.pathParam = pathParam;
            this.name = name;
        }

        public String getPathParam() {

            return pathParam;
        }

        public String getName() {

            return name;
        }

        public static Type valueOfName(String name) {

            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Authentication type cannot be null or empty.");
            }

            for (Type type : Type.values()) {
                if (type.name.equalsIgnoreCase(name)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid authentication type: " + name);
        }
    }

    /**
     * Authentication Property Enum.
     */
    public enum Property {

        USERNAME("username"),
        PASSWORD("password"),
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

