/*******************************************************************************
 *  Copyright (c) 2018 Obeo. 
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *   
 *   Contributors:
 *       Obeo - initial API and implementation
 *  
 *******************************************************************************/
package org.obeonetwork.m2doc.html.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIHandler;

/**
 * An {@link URIHandler} that can read resources through <a href="https://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol">HTTP</a> and
 * <a href="https://en.wikipedia.org/wiki/HTTPS">HTTPS</a>.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class HttpURIHandler implements URIHandler {

    /**
     * Not implemented message.
     */
    private static final String NOT_IMPLEMENTED = "Not implemented.";

    /**
     * HTTP protocol.
     */
    private static final String HTTP = "http";

    /**
     * HTTPS protocol.
     */
    private static final String HTTPS = "https";

    @Override
    public boolean canHandle(URI uri) {
        return HTTP.equals(uri.scheme().toLowerCase()) || HTTPS.equals(uri.scheme().toLowerCase());
    }

    @Override
    public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault();) {

            final HttpGet httpget = new HttpGet(uri.toString());
            ResponseHandler<byte[]> responseHandler = new ResponseHandler<byte[]>() {

                @Override
                public byte[] handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                    final byte[] res;

                    int status = response.getStatusLine().getStatusCode();
                    if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
                        final HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            res = EntityUtils.toString(entity).getBytes("UTF-8");
                        } else {
                            res = new byte[0];
                        }
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }

                    return res;
                }

            };
            byte[] buffer = httpclient.execute(httpget, responseHandler);

            return new ByteArrayInputStream(buffer);
        }
    }

    @Override
    public OutputStream createOutputStream(URI uri, Map<?, ?> options) throws IOException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void delete(URI uri, Map<?, ?> options) throws IOException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public Map<String, ?> contentDescription(URI uri, Map<?, ?> options) throws IOException {
        return null;
    }

    @Override
    public boolean exists(URI uri, Map<?, ?> options) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Map<String, ?> getAttributes(URI uri, Map<?, ?> options) {
        return null;
    }

    @Override
    public void setAttributes(URI uri, Map<String, ?> attributes, Map<?, ?> options) throws IOException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

}
