package org.wso2.carbon.identity.external.api.token.handler.api.model;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Model class for Grant Type Context.
 */
public class GrantContext {

    private final GrantType grantType;
    private final Map<String, String> properties;

    public GrantContext(GrantContext.Builder builder) {

        this.grantType = builder.grantType;
        this.properties = builder.resolvedGrantProperties;
    }

    public GrantType getGrantType() {

        return grantType;
    }

    public Map<String, String> getProperties() {

        return properties;
    }

    /**
     * Builder class for GrantContext.
     */
    public static class Builder {

        private GrantType grantType;
        private Map<String, String> propertiesMap = new HashMap<>();
        private Map<String, String> resolvedGrantProperties = new HashMap<>();

        public Builder grantType(GrantType grantType) {

            this.grantType = grantType;
            return this;
        }

        public Builder properties(Map<String, String> properties) {

            this.propertiesMap = properties;
            return this;
        }

        public GrantContext build() {

            resolvedGrantProperties = new HashMap<>();
            switch (grantType) {
                case CLIENT_CREDENTIALS:
                    resolvedGrantProperties.put(Property.CLIENT_ID.getName(),
                            getProperty(Type.CLIENT_CRED, propertiesMap, Property.CLIENT_ID.getName()));
                    resolvedGrantProperties.put(Property.CLIENT_SECRET.getName(),
                            getProperty(Type.CLIENT_CRED, propertiesMap, Property.CLIENT_SECRET.getName()));
                    resolvedGrantProperties.put(Property.SCOPE.getName(),
                            getProperty(Type.CLIENT_CRED, propertiesMap, Property.SCOPE.getName()));
                    break;
                default:
                    throw new IllegalArgumentException(String.format("An invalid authentication type '%s' is " +
                            "provided for the authentication configuration of the endpoint.", grantType.name()));
            }
            return new GrantContext(this);
        }

        private String getProperty(Type authType, Map<String, String> actionEndpointProperties,
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

        CLIENT_CRED("CLIENT_CREDENTIAL");

        private final String name;

        Type(String name) {

            this.name = name;
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
     * Authentication Grant Type.
     */
    public enum GrantType {

        CLIENT_CREDENTIALS
    }

    /**
     * Authentication Property Enum.
     */
    public enum Property {

        CLIENT_ID("client_id"),
        CLIENT_SECRET("client_secret"),
        SCOPE("scope");

        private final String name;

        Property(String name) {

            this.name = name;
        }

        public String getName() {

            return name;
        }
    }
}
