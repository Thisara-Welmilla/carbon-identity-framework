/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.application.common.util;

import org.apache.commons.lang.ArrayUtils;
import org.wso2.carbon.identity.application.common.exception.AuthenticatorMgtClientException;
import org.wso2.carbon.identity.application.common.exception.AuthenticatorMgtServerException;
import org.wso2.carbon.identity.application.common.exception.AuthenticatorMgtServerRuntimeException;

/**
 * Utility class for building authenticator management exceptions.
 */
public class AuthenticatorMgtExceptionBuilder {

    private AuthenticatorMgtExceptionBuilder() {

    }

    public static AuthenticatorMgtClientException buildClientException(AuthenticatorMgtError error, String... data) {

        String description = error.getDescription();
        if (ArrayUtils.isNotEmpty(data)) {
            description = String.format(description, data);
        }

        return new AuthenticatorMgtClientException(error.getCode(), error.getMessage(), description);
    }

    public static AuthenticatorMgtServerException buildServerException(AuthenticatorMgtError error, String... data) {

        String description = error.getDescription();
        if (ArrayUtils.isNotEmpty(data)) {
            description = String.format(description, data);
        }

        return new AuthenticatorMgtServerException(error.getCode(), error.getMessage(), description);
    }

    public static AuthenticatorMgtServerException buildServerException(AuthenticatorMgtError error, Throwable e,
                                                                       String... data) {

        String description = error.getDescription();
        if (ArrayUtils.isNotEmpty(data)) {
            description = String.format(description, data);
        }

        return new AuthenticatorMgtServerException(error.getCode(), error.getMessage(), description, e);
    }

    public static AuthenticatorMgtServerRuntimeException buildRuntimeServerException(AuthenticatorMgtError error,
                Throwable e, String... data) {

        String description = error.getDescription();
        if (ArrayUtils.isNotEmpty(data)) {
            description = String.format(description, data);
        }

        return new AuthenticatorMgtServerRuntimeException(error.getCode(), error.getMessage(), description);
    }

    /**
     * Enum class to represent the rule metadata errors.
     */
    public enum AuthenticatorMgtError {

        // Client errors.
        ERROR_NOT_FOUND_AUTHENTICATOR("60001", "No Authenticator found.",
                "No Authenticator found by given authenticator name: %s."),
        ERROR_OP_ON_SYSTEM_AUTHENTICATOR("60002", "No operations allowed on system authenticators.",
                "Do not allow to perform any operation on system defined authenticator: %s."),
        ERROR_AUTHENTICATOR_ALREADY_EXIST("60003", "An authenticator already exists.",
                "As authenticator already exists for the given name: %s."),
        ERROR_INVALID_AUTHENTICATOR_NAME("60004", "Authenticator name is invalid.",
                "The provided authenticator name %s is not in the expected format %s."),
        ERROR_BLANK_FIELD_VALUE("60004", "Invalid empty or blank value.",
                "Value for %s should not be empty or blank."),

        // Server errors.
        ERROR_WHILE_ADDING_AUTHENTICATOR("65001", "Error while adding authenticator.",
                "Error while persisting authenticator to the system."),
        ERROR_WHILE_UPDATING_AUTHENTICATOR("65002", "Error while updating authenticator.",
                "Error while updating authenticator in the system."),
        ERROR_WHILE_RETRIEVING_AUTHENTICATOR_BY_NAME("65003", "Error while retrieving authenticator.",
                "Error while retrieving authenticator in the system."),
        ERROR_WHILE_DELETING_AUTHENTICATOR("65004", "Error while deleting authenticator.",
                "Error while deleting authenticator in the system."),
        ERROR_CODE_ENDPOINT_CONFIG_MGT("65005", "Error while managing endpoint configurations.",
                "Error while managing endpoint configurations for the user defined local authenticator %s."),
        ERROR_CODE_INVALID_DEFINED_BY_AUTH_PROVIDED("65006", "Error while adding local authenticator.",
                "Only system defined authenticators are allowed to add via this method."),
        ERROR_CODE_NO_AUTHENTICATOR_FOUND("65007", "No authenticator found.",
                "No authenticator found by given authenticator name: %s."),
        ERROR_CODE_NO_ACTION_ID_FOUND("65008", "No action id found.",
                "No action id found for the authenticator: %s.");

        private final String code;
        private final String message;
        private final String description;

        AuthenticatorMgtError(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return code;
        }

        public String getMessage() {

            return message;
        }

        public String getDescription() {

            return description;
        }
    }
}
