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

package org.wso2.carbon.identity.action.management.dao;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.mockito.MockedStatic;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.action.management.dao.impl.ActionManagementDAOImpl;
import org.wso2.carbon.identity.action.management.exception.ActionMgtException;
import org.wso2.carbon.identity.action.management.internal.ActionMgtServiceComponentHolder;
import org.wso2.carbon.identity.action.management.model.Action;
import org.wso2.carbon.identity.action.management.model.AuthProperty;
import org.wso2.carbon.identity.action.management.model.AuthType;
import org.wso2.carbon.identity.action.management.model.EndpointConfig;
import org.wso2.carbon.identity.common.testng.WithH2Database;
import org.wso2.carbon.identity.core.util.IdentityDatabaseUtil;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.secret.mgt.core.SecretManagerImpl;
import org.wso2.carbon.identity.secret.mgt.core.exception.SecretManagementException;
import org.wso2.carbon.identity.secret.mgt.core.model.SecretType;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

/**
 * This class is a test suite for the ActionManagementDAOImpl class.
 * It contains unit tests to verify the functionality of the methods
 * in the ActionManagementDAOImpl class.
 */
@WithH2Database(files = {"dbscripts/h2.sql"})
public class ActionManagementDAOImplTest {

    private ActionManagementDAOImpl daoImpl;
    private static final Map<String, BasicDataSource> dataSourceMap = new HashMap<>();
    private MockedStatic<IdentityDatabaseUtil> identityDatabaseUtil;
    private MockedStatic<IdentityTenantUtil> identityTenantUtil;
    private static final String DB_NAME = "action_mgt";
    private static final int TENANT_ID = 2;
    private static Action preIssueAccessTokenAction;
    private static Action authenticationAction;


    @BeforeClass
    public void setUpClass() throws Exception {

        daoImpl = new ActionManagementDAOImpl();
        initiateH2Database(getFilePath());
    }

    @BeforeMethod
    public void setUp() throws SQLException, SecretManagementException {

        identityTenantUtil = mockStatic(IdentityTenantUtil.class);
        identityDatabaseUtil = mockStatic(IdentityDatabaseUtil.class);
        SecretManagerImpl secretManager = mock(SecretManagerImpl.class);
        SecretType secretType = mock(SecretType.class);
        ActionMgtServiceComponentHolder.getInstance().setSecretManager(secretManager);
        mockDBConnection();
        identityTenantUtil.when(()-> IdentityTenantUtil.getTenantId(anyString())).thenReturn(TENANT_ID);
        when(secretType.getId()).thenReturn("secretId");
        when(secretManager.getSecretType(any())).thenReturn(secretType);
    }

    @AfterMethod
    public void tearDown() {

        identityTenantUtil.close();
        identityDatabaseUtil.close();
    }

    @AfterClass
    public void wrapUp() throws Exception {

        closeH2Database();
    }

    @DataProvider(name = "actionProvider")
    public Object[][] actionDataProvider() {

        return new Object[][]{
                {Action.ActionTypes.PRE_ISSUE_ACCESS_TOKEN.getActionType(), preIssueAccessTokenAction},
                {Action.ActionTypes.AUTHENTICATION.getActionType(), authenticationAction}
        };
    }

    @Test(priority = 1, dataProvider = "actionProvider")
    public void testAddAction(String actionType, Action action) throws ActionMgtException {

        String id = String.valueOf(UUID.randomUUID());
        Action creatingAction = buildMockAction(
                "PreIssueAccessToken",
                "To configure PreIssueAccessToken",
                "https://example.com",
                 AuthType.AuthenticationType.BASIC,
                buildMockBasicAuthProperties("admin", "admin"));
        action = daoImpl.addAction(actionType, id, creatingAction, TENANT_ID);
        Assert.assertEquals(id, action.getId());
        Assert.assertEquals(creatingAction.getName(), action.getName());
        Assert.assertEquals(creatingAction.getDescription(), action.getDescription());
        Assert.assertEquals(actionType, action.getType().getActionType());
        Assert.assertEquals(Action.Status.ACTIVE, action.getStatus());
        Assert.assertEquals(creatingAction.getEndpoint().getUri(), action.getEndpoint().getUri());
        Assert.assertEquals(creatingAction.getEndpoint().getAuthentication().getType(),
                action.getEndpoint().getAuthentication().getType());
    }

    @Test(priority = 2, dataProvider = "actionProvider", expectedExceptions = ActionMgtException.class,
            expectedExceptionsMessageRegExp = "Error while adding Action.")
    public void testAddActionWithoutName(String actionType) throws ActionMgtException {

        Action action = buildMockAction(
                null,
                "To configure PreIssueAccessToken",
                "https://example.com",
                 AuthType.AuthenticationType.BASIC,
                buildMockBasicAuthProperties("admin", "admin"));
        daoImpl.addAction(actionType, action.getId(), action, TENANT_ID);
    }

    @Test(priority = 3, dataProvider = "actionProvider")
    public void testGetActionsByActionType(String actionType, Action action) throws ActionMgtException, SQLException {

        Assert.assertEquals(1, daoImpl.getActionsByActionType(actionType, TENANT_ID).size());
        mockDBConnection();
        Action result = daoImpl.getActionsByActionType(actionType, TENANT_ID).get(0);
        Assert.assertEquals(action.getId(), result.getId());
        Assert.assertEquals(action.getName(), result.getName());
        Assert.assertEquals(action.getDescription(), result.getDescription());
        Assert.assertEquals(action.getType(), result.getType());
        Assert.assertEquals(action.getStatus(), result.getStatus());
        Assert.assertEquals(action.getEndpoint().getUri(), result.getEndpoint().getUri());
        Assert.assertEquals(action.getEndpoint().getAuthentication().getType(),
                result.getEndpoint().getAuthentication().getType());
    }

    @Test(priority = 4, dataProvider = "actionProvider")
    public void testGetActionByActionId(String actionType, Action action) throws ActionMgtException {

        Action result = daoImpl.getActionByActionId(actionType, action.getId(), TENANT_ID);
        Assert.assertEquals(action.getId(), result.getId());
        Assert.assertEquals(action.getName(), result.getName());
        Assert.assertEquals(action.getDescription(), result.getDescription());
        Assert.assertEquals(action.getType(), result.getType());
        Assert.assertEquals(action.getStatus(), result.getStatus());
        Assert.assertEquals(action.getEndpoint().getUri(), result.getEndpoint().getUri());
        Assert.assertEquals(action.getEndpoint().getAuthentication().getType(),
                result.getEndpoint().getAuthentication().getType());
    }

    @Test(priority = 5, dataProvider = "actionProvider")
    public void testDeleteAction(String actionType, Action action) throws ActionMgtException, SQLException {

        daoImpl.deleteAction(actionType, action.getId(), action, TENANT_ID);
        mockDBConnection();
        Assert.assertNull(daoImpl.getActionByActionId(actionType, action.getId(), TENANT_ID));
    }

    @Test(priority = 6, dataProvider = "actionProvider")
    public void testAddActionWithoutDescription(String actionType, Action action) throws ActionMgtException {

        String id = String.valueOf(UUID.randomUUID());
        Action creatingAction = buildMockAction(
                "PreIssueAccessToken",
                null,
                "https://example.com",
                 AuthType.AuthenticationType.BASIC,
                 buildMockBasicAuthProperties("admin", "admin"));
        action = daoImpl.addAction(actionType, id, creatingAction, TENANT_ID);
        Assert.assertEquals(id, action.getId());
        Assert.assertEquals(creatingAction.getName(), action.getName());
        Assert.assertNull(null, action.getDescription());
        Assert.assertEquals(actionType, action.getType().getActionType());
        Assert.assertEquals(Action.Status.ACTIVE, action.getStatus());
        Assert.assertEquals(creatingAction.getEndpoint().getUri(), action.getEndpoint().getUri());
        Assert.assertEquals(creatingAction.getEndpoint().getAuthentication().getType(),
                action.getEndpoint().getAuthentication().getType());
    }

    @Test(priority = 7, dataProvider = "actionProvider")
    public void testUpdateAction(String actionType, Action action) throws ActionMgtException {

        Action updatingAction = buildMockAction(
                "Pre Issue Access Token",
                "To configure pre issue access token",
                "https://sample.com",
                 AuthType.AuthenticationType.BASIC,
                 buildMockBasicAuthProperties("updatingadmin", "updatingadmin"));
        Action result = daoImpl.updateAction(actionType, action.getId(), updatingAction, action, TENANT_ID);
        Assert.assertEquals(action.getId(), result.getId());
        Assert.assertEquals(updatingAction.getName(), result.getName());
        Assert.assertEquals(updatingAction.getDescription(), result.getDescription());
        Assert.assertEquals(action.getType(), result.getType());
        Assert.assertEquals(action.getStatus(), result.getStatus());
        Assert.assertEquals(updatingAction.getEndpoint().getUri(), result.getEndpoint().getUri());
        Assert.assertEquals(
                updatingAction.getEndpoint().getAuthentication().getType(),
                result.getEndpoint().getAuthentication().getType()
        );
        updateAction(result);
    }

    @Test(priority = 8, dataProvider = "actionProvider")
    public void testUpdateActionWithoutNameAndDescription(String actionType, Action action) throws ActionMgtException {

        // TODO: 'Name' is a required attribute. Thus, DAO layer should throw an exception if name is null.
        //  This should be fixed in DAO layer and test case needs to be updated accordingly.
        Action updatingAction = buildMockAction(
                null,
                null,
                "https://sample.com",
                 AuthType.AuthenticationType.BASIC,
                 buildMockBasicAuthProperties("updatingadmin", "updatingadmin"));
        Action result = daoImpl.updateAction(actionType, action.getId(), updatingAction, action, TENANT_ID);
        Assert.assertEquals(action.getId(), result.getId());
        Assert.assertEquals(action.getName(), result.getName());
        Assert.assertEquals(action.getDescription(), result.getDescription());
        Assert.assertEquals(action.getType(), result.getType());
        Assert.assertEquals(action.getStatus(), result.getStatus());
        Assert.assertEquals(updatingAction.getEndpoint().getUri(), result.getEndpoint().getUri());
        Assert.assertEquals(updatingAction.getEndpoint().getAuthentication().getType(),
                result.getEndpoint().getAuthentication().getType());
    }

    @Test(priority = 9, dataProvider = "actionProvider")
    public void testUpdateActionWithNameAndDescription(String actionType, Action action) throws ActionMgtException {

        // TODO: 'Uri','AuthenticationType','AuthProperties' are required attributes. Thus, DAO layer should throw an
        //  exception if those attributes are null. This should be fixed in DAO layer and test case needs to be updated
        //  accordingly.
        Action updatingAction = buildMockAction(
                "Pre Issue Access Token",
                "To configure pre issue access token",
                null,
                null,
                null);
        Action result = daoImpl.updateAction(actionType, action.getId(), updatingAction, action, TENANT_ID);
        Assert.assertEquals(action.getId(), result.getId());
        Assert.assertEquals(updatingAction.getName(), result.getName());
        Assert.assertEquals(updatingAction.getDescription(), result.getDescription());
        Assert.assertEquals(action.getType(), result.getType());
        Assert.assertEquals(action.getStatus(), result.getStatus());
        Assert.assertEquals(action.getEndpoint().getUri(), result.getEndpoint().getUri());
        Assert.assertEquals(action.getEndpoint().getAuthentication().getType(),
                result.getEndpoint().getAuthentication().getType());
    }

    @Test(priority = 10, dataProvider = "actionProvider")
    public void testUpdateActionEndpointAuthSecretProperties(String actionType, Action action)
            throws ActionMgtException {

        AuthType authType = buildMockAuthType(AuthType.AuthenticationType.BASIC,
                buildMockBasicAuthProperties("newadmin", "newadmin"));
        Action result = daoImpl.updateActionEndpointAuthProperties(actionType,
                action.getId(), authType, TENANT_ID);
        Assert.assertEquals(AuthType.AuthenticationType.BASIC, result.getEndpoint().getAuthentication().getType());
        Assert.assertEquals(action.getEndpoint().getAuthentication().getProperties().get(0).getValue(),
                result.getEndpoint().getAuthentication().getProperties().get(0).getValue());
        Assert.assertEquals(action.getEndpoint().getAuthentication().getProperties().get(1).getValue(),
                result.getEndpoint().getAuthentication().getProperties().get(1).getValue());
    }

    @Test(priority = 11, dataProvider = "actionProvider")
    public void testUpdateActionWithoutEndpointUri(String actionType, Action action) throws ActionMgtException {

        // TODO: 'Uri' is a required attribute. Thus, DAO layer should throw an exception if Uri is null.
        //  This should be fixed in DAO layer and test case needs to be updated accordingly.
        Action updatingAction = buildMockAction(
                "Pre Issue Access Token",
                "To configure pre issue access token",
                null,
                 AuthType.AuthenticationType.BASIC,
                 buildMockBasicAuthProperties("updatingadmin", "updatingadmin"));
        Action result = daoImpl.updateAction(actionType, action.getId(), updatingAction, action, TENANT_ID);
        Assert.assertEquals(action.getId(), result.getId());
        Assert.assertEquals(updatingAction.getName(), result.getName());
        Assert.assertEquals(updatingAction.getDescription(), result.getDescription());
        Assert.assertEquals(action.getType(), result.getType());
        Assert.assertEquals(action.getStatus(), result.getStatus());
        Assert.assertEquals(action.getEndpoint().getUri(), result.getEndpoint().getUri());
        Assert.assertEquals(updatingAction.getEndpoint().getAuthentication().getType(),
                result.getEndpoint().getAuthentication().getType());
    }

    @Test(priority = 12, dataProvider = "actionProvider")
    public void testUpdateActionWithAuthType(String actionType, Action action) throws ActionMgtException {

        Action updatingAction = buildMockAction(
                "Pre Issue Access Token",
                "To configure pre issue access token",
                "https://sample.com",
                AuthType.AuthenticationType.BEARER,
                buildMockBearerAuthProperties("57c7df90-cacc-4f56-9b0a-f14bfbff3076"));
        Action result = daoImpl.updateAction(actionType, action.getId(), updatingAction, action, TENANT_ID);
        Assert.assertEquals(action.getId(), result.getId());
        Assert.assertEquals(action.getName(), result.getName());
        Assert.assertEquals(action.getDescription(), result.getDescription());
        Assert.assertEquals(action.getType(), result.getType());
        Assert.assertEquals(action.getStatus(), result.getStatus());
        Assert.assertEquals(updatingAction.getEndpoint().getUri(), result.getEndpoint().getUri());
        Assert.assertEquals(updatingAction.getEndpoint().getAuthentication().getType(),
                result.getEndpoint().getAuthentication().getType());
        updateAction(result);
    }

    @Test(priority = 13, dataProvider = "actionProvider")
    public void testUpdateActionWithUri(String actionType, Action action) throws ActionMgtException {

        // TODO: 'Name','AuthenticationType' and 'AuthProperties' are required attributes. Thus, DAO layer should throw
        //  an exception if those attributes are null. This should be fixed in DAO layer and test case needs to be
        //  updated accordingly.
        Action updatingAction = buildMockAction(
                null,
                null,
                "https://sample.com",
                null,
                null);
        Action result = daoImpl.updateAction(actionType, action.getId(), updatingAction, action, TENANT_ID);
        Assert.assertEquals(action.getId(), result.getId());
        Assert.assertEquals(action.getName(), result.getName());
        Assert.assertEquals(action.getDescription(), result.getDescription());
        Assert.assertEquals(action.getType(), result.getType());
        Assert.assertEquals(action.getStatus(), result.getStatus());
        Assert.assertEquals(updatingAction.getEndpoint().getUri(), result.getEndpoint().getUri());
        Assert.assertEquals(action.getEndpoint().getAuthentication().getType(),
                result.getEndpoint().getAuthentication().getType());
        updateAction(result);
    }

    @Test(priority = 14, dataProvider = "actionProvider")
    public void testUpdateActionWithAuthTypeWithoutUri(String actionType, Action action) throws ActionMgtException {

        // TODO: 'Uri' is a required attribute. Thus, DAO layer should throw an exception if uri is null.
        //  This should be fixed in DAO layer and test case needs to be updated accordingly.
        Action updatingAction = buildMockAction(
                "Pre Issue Access Token",
                "To configure pre issue access token",
                null,
                AuthType.AuthenticationType.BASIC,
                buildMockBasicAuthProperties("updatingadmin", "updatingadmin"));
        Action result = daoImpl.updateAction(actionType, action.getId(), updatingAction, action, TENANT_ID);
        Assert.assertEquals(action.getId(), result.getId());
        Assert.assertEquals(updatingAction.getName(), result.getName());
        Assert.assertEquals(updatingAction.getDescription(), result.getDescription());
        Assert.assertEquals(action.getType(), result.getType());
        Assert.assertEquals(action.getStatus(), result.getStatus());
        Assert.assertEquals(action.getEndpoint().getUri(), result.getEndpoint().getUri());
        Assert.assertEquals(updatingAction.getEndpoint().getAuthentication().getType(),
                result.getEndpoint().getAuthentication().getType());
    }

    @Test(priority = 15, dataProvider = "actionProvider")
    public void testUpdateActionEndpointAuthNonSecretProperties(String actionType, Action action)
            throws ActionMgtException, SQLException {

        AuthType authType = buildMockAuthType(AuthType.AuthenticationType.API_KEY,
                buildMockAPIKeyAuthProperties("updatingheader", "updatingvalue"));
        Action sampleAction = buildMockAction(
                "Pre Issue Access Token",
                "To configure pre issue access token",
                "https://sample.com",
                 AuthType.AuthenticationType.API_KEY,
                 buildMockAPIKeyAuthProperties("header", "value"));
        Action updatingAction = daoImpl.updateAction(actionType, action.getId(), sampleAction, action, TENANT_ID);
        mockDBConnection();
        Action result = daoImpl.updateActionEndpointAuthProperties(actionType,
                updatingAction.getId(), authType, TENANT_ID);
        Assert.assertEquals(AuthType.AuthenticationType.API_KEY, result.getEndpoint().getAuthentication().getType());
        Assert.assertEquals(authType.getProperties().get(0).getValue(),
                result.getEndpoint().getAuthentication().getProperties().get(0).getValue());
        Assert.assertEquals(updatingAction.getEndpoint().getAuthentication().getProperties().get(1).getValue(),
                result.getEndpoint().getAuthentication().getProperties().get(1).getValue());
    }

    @Test(priority = 16, dataProvider = "actionProvider")
    public void testDeactivateAction(String actionType, Action action) throws ActionMgtException {

        Assert.assertEquals(Action.Status.ACTIVE, action.getStatus());
        Action deactivatedAction = daoImpl.deactivateAction(actionType, action.getId(), TENANT_ID);
        Assert.assertEquals(Action.Status.INACTIVE, deactivatedAction.getStatus());
    }

    @Test(priority = 17, dataProvider = "actionProvider")
    public void testActivateAction(String actionType, Action action) throws ActionMgtException {

        Action result = daoImpl.activateAction(actionType, action.getId(), TENANT_ID);
        Assert.assertEquals(Action.Status.ACTIVE, result.getStatus());
    }

    @Test(priority = 18, dataProvider = "actionProvider")
    public void testUpdateActionEndpoint(String actionType, Action action) throws ActionMgtException {

        EndpointConfig endpointConfig = buildMockEndpointConfig("https://template.com",
                AuthType.AuthenticationType.BEARER,
                buildMockBearerAuthProperties("c7fce95f-3f5b-4cda-8bb1-4cb7b3990f83"));
        Action result = daoImpl.updateActionEndpoint(
                actionType, action.getId(), endpointConfig, action.getEndpoint()
                .getAuthentication(), TENANT_ID);
        Assert.assertNotEquals(action.getEndpoint().getUri(), result.getEndpoint().getUri());
        Assert.assertEquals(AuthType.AuthenticationType.BEARER.getType(),
                result.getEndpoint().getAuthentication().getType().getType());
    }

    @Test(priority = 19, dataProvider = "actionProvider")
    public void testGetActionsCountPerType(String actionType, Action action) throws ActionMgtException {

        Map<String, Integer> actionMap = daoImpl.getActionsCountPerType(TENANT_ID);
        for (Map.Entry<String, Integer> entry: actionMap.entrySet()) {
            Assert.assertEquals(actionType, entry.getKey());
            Assert.assertEquals(1, entry.getValue().intValue());
        }
    }

    private void updateAction(Action updatedAction) {

        switch (updatedAction.getType()) {
            case PRE_ISSUE_ACCESS_TOKEN:
                preIssueAccessTokenAction = updatedAction;
                return;
            case AUTHENTICATION:
                authenticationAction = updatedAction;
                return;
            default:
                return;
        }
    }

    private AuthProperty buildMockAuthProperty(
            AuthType.AuthenticationType.AuthenticationProperty authenticationProperty, String value) {

        return new AuthProperty.AuthPropertyBuilder()
                .name(authenticationProperty.getName())
                .value(value)
                .isConfidential(authenticationProperty.getIsConfidential())
                .build();
    }

    private List<AuthProperty> buildMockBasicAuthProperties(String username, String password) {

        return Arrays.asList(
                buildMockAuthProperty(AuthType.AuthenticationType.AuthenticationProperty.USERNAME, username),
                buildMockAuthProperty(AuthType.AuthenticationType.AuthenticationProperty.PASSWORD, password));
    }

    private List<AuthProperty> buildMockBearerAuthProperties(String accessToken) {

        return Arrays.asList(
                buildMockAuthProperty(AuthType.AuthenticationType.AuthenticationProperty.ACCESS_TOKEN, accessToken));
    }

    private List<AuthProperty> buildMockAPIKeyAuthProperties(String header, String value) {

        return Arrays.asList(
                buildMockAuthProperty(AuthType.AuthenticationType.AuthenticationProperty.HEADER, header),
                buildMockAuthProperty(AuthType.AuthenticationType.AuthenticationProperty.VALUE, value));
    }

    private EndpointConfig buildMockEndpointConfig(String uri, AuthType.AuthenticationType authenticationType,
                                                   List<AuthProperty> authProperties) {

        if (uri == null && authProperties == null) {
            return null;
        }
        return new EndpointConfig.EndpointConfigBuilder()
                .uri(uri)
                .authentication(buildMockAuthType(authenticationType, authProperties))
                .build();
    }

    private AuthType buildMockAuthType(AuthType.AuthenticationType authenticationType,
                                       List<AuthProperty> authProperties) {

        if (authenticationType == null || authProperties == null) {
            return null;
        }
        return new AuthType.AuthTypeBuilder()
                .type(authenticationType)
                .properties(authProperties)
                .build();
    }

    private Action buildMockAction(String name,
                                   String description,
                                   String uri,
                                   AuthType.AuthenticationType authType,
                                   List<AuthProperty> authProperties) {

            return new Action.ActionRequestBuilder()
                    .name(name)
                    .description(description)
                    .endpoint(buildMockEndpointConfig(uri, authType, authProperties))
                    .build();
    }

    private void mockDBConnection() throws SQLException {

        Connection connection = dataSourceMap.get(DB_NAME).getConnection();
        identityDatabaseUtil.when(() -> IdentityDatabaseUtil.getDBConnection(anyBoolean())).thenReturn(connection);
    }

    private void initiateH2Database(String scriptPath) throws Exception {

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUsername("username");
        dataSource.setPassword("password");
        dataSource.setUrl("jdbc:h2:mem:test" + DB_NAME);
        dataSource.setTestOnBorrow(true);
        dataSource.setValidationQuery("select 1");
        try (Connection connection = dataSource.getConnection()) {
            connection.createStatement().executeUpdate("RUNSCRIPT FROM '" + scriptPath + "'");
        }
        dataSourceMap.put(DB_NAME, dataSource);
    }

    private static String getFilePath() {

        if (StringUtils.isNotBlank("h2.sql")) {
            return Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "dbscripts", "h2.sql")
                    .toString();
        }
        throw new IllegalArgumentException("DB Script file name cannot be empty.");
    }

    private static void closeH2Database() throws SQLException {

        BasicDataSource dataSource = dataSourceMap.get(DB_NAME);
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
