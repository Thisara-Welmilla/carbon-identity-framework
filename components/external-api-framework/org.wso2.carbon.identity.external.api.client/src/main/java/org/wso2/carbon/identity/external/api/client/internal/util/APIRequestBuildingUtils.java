package org.wso2.carbon.identity.external.api.client.internal.util;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.wso2.carbon.identity.external.api.client.api.model.APIAuthentication;

import java.nio.charset.StandardCharsets;

/**
 * Utility class for building API request components.
 */
public class APIRequestBuildingUtils {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    public static Header buildAuthenticationHeader(APIAuthentication apiAuthentication) {

        switch (apiAuthentication.getType()) {
            case BASIC:
                String credentials = apiAuthentication.getProperty(APIAuthentication.Property.USERNAME).getValue()
                        + ":" + apiAuthentication.getProperty(APIAuthentication.Property.PASSWORD).getValue();
                byte[] encodedBytes = java.util.Base64.getEncoder()
                        .encode(credentials.getBytes(StandardCharsets.UTF_8));
                return new BasicHeader(AUTHORIZATION_HEADER,
                        "Basic " + new String(encodedBytes, StandardCharsets.UTF_8));
            case BEARER:
                return new BasicHeader(AUTHORIZATION_HEADER,
                        "Bearer " + apiAuthentication.getProperty(APIAuthentication.Property.ACCESS_TOKEN).getValue());
            case API_KEY:
                return new BasicHeader(apiAuthentication.getProperty(APIAuthentication.Property.HEADER).getValue(),
                        apiAuthentication.getProperty(APIAuthentication.Property.VALUE).getValue());
            default:
                return null;
        }
    }
}
