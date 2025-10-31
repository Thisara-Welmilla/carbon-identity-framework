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

package org.wso2.carbon.identity.external.api.client.api.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.wso2.carbon.identity.external.api.client.api.exception.APIClientInvocationException;
import org.wso2.carbon.identity.external.api.client.api.model.APIClientConfig;
import org.wso2.carbon.identity.external.api.client.api.model.APIInvocationConfig;
import org.wso2.carbon.identity.external.api.client.api.model.APIRequestContext;
import org.wso2.carbon.identity.external.api.client.api.model.APIResponse;
import org.wso2.carbon.identity.external.api.client.internal.util.APIRequestBuildingUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * This class is responsible for making API calls to the external endpoints.
 */
public class APIClient {

    private static final Log LOG = LogFactory.getLog(APIClient.class);
    private final CloseableHttpClient httpClient;

    /**
     * Constructor to initialize the APIClient with the given configuration.
     *
     * @param apiClientConfig API client configuration.
     */
    public APIClient(APIClientConfig apiClientConfig) {

        int readTimeout = apiClientConfig.getHttpReadTimeoutInMillis();
        int connectionRequestTimeout = apiClientConfig.getHttpConnectionRequestTimeoutInMillis();
        int connectionTimeout = apiClientConfig.getHttpConnectionTimeoutInMillis();

        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(connectionTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(readTimeout)
                .setRedirectsEnabled(false)
                .setRelativeRedirectsAllowed(false)
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(apiClientConfig.getPoolSizeToBeSet());
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).setConnectionManager(connectionManager)
                .build();
    }

    /**
     * Makes a POST API call to the given endpoint URL with the provided request context.
     *
     * @param requestContext  Request context containing endpoint URL, headers, and payload.
     * @return APIResponse containing the response from the API call.
     * @throws APIClientInvocationException if an error occurs during the API call.
     */
    public APIResponse callAPI(APIRequestContext requestContext, APIInvocationConfig apiInvocationConfig)
            throws APIClientInvocationException {

        HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase;
        // may be this part also to utils?
        switch (requestContext.getHttpMethod()) {
            case POST:
                httpEntityEnclosingRequestBase = new HttpPost(requestContext.getEndpointUrl());
                break;
            default:
                throw new APIClientInvocationException("HTTP Method: " + requestContext.getHttpMethod()
                        + " is not supported.");
        }
        setRequestEntity(httpEntityEnclosingRequestBase, requestContext);
        return executeRequest(httpEntityEnclosingRequestBase, apiInvocationConfig);
    }

    private void setRequestEntity(HttpEntityEnclosingRequestBase httpRequestBase, APIRequestContext requestContext) {

        StringEntity entity = new StringEntity(requestContext.getPayload(), StandardCharsets.UTF_8);
        httpRequestBase.setEntity(entity);

        httpRequestBase.setHeader("Accept", "application/json");
        httpRequestBase.setHeader("Content-type", "application/json");
        httpRequestBase.setHeader(
                APIRequestBuildingUtils.buildAuthenticationHeader(requestContext.getApiAuthentication()));
        for (Map.Entry<String, String> header : requestContext.getHeaders().entrySet()) {
            httpRequestBase.setHeader(header.getKey(), header.getValue());
        }
    }

    // keep retry count
    private APIResponse executeRequest(HttpEntityEnclosingRequestBase request, APIInvocationConfig apiInvocationConfig)
            throws APIClientInvocationException {

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return handleResponse(response);
        } catch (ConnectTimeoutException | SocketTimeoutException e) {
            throw new APIClientInvocationException("Request to API: " + request.getURI() + " timed out.", e);
        } catch (IOException e) {
            throw new APIClientInvocationException(
                    "IO error occurred while invoking the API: " + request.getURI() + ".", e);
        } finally {
            request.releaseConnection();
        }
    }

    private APIResponse handleResponse(HttpResponse response) throws IOException {

        int statusCode = response.getStatusLine().getStatusCode();
        HttpEntity responseEntity = response.getEntity();

        APIResponse.Builder apiResponseBuilder = new APIResponse
                .Builder(statusCode, EntityUtils.toString((responseEntity)));
        return apiResponseBuilder.build();
    }
}
