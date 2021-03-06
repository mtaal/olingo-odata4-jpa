/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.olingo.server.core;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.olingo.commons.api.ODataRuntimeException;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpMethod;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ODataLibraryException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.ODataServerError;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.debug.DebugSupport;
import org.apache.olingo.server.api.etag.CustomETagSupport;
import org.apache.olingo.server.api.processor.Processor;
import org.apache.olingo.server.api.serializer.CustomContentTypeSupport;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.core.debug.ServerCoreDebugger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ODataHttpHandlerImpl implements ODataHttpHandler {

  private static final Logger LOG = LoggerFactory.getLogger(ODataHttpHandlerImpl.class);

  private final ODataHandler handler;
  private final ServerCoreDebugger debugger;

  private int split = 0;

  public ODataHttpHandlerImpl(final OData odata, final ServiceMetadata serviceMetadata) {
    debugger = new ServerCoreDebugger(odata);
    handler = new ODataHandler(odata, serviceMetadata, debugger);
  }

  @Override
  public void process(final HttpServletRequest request, final HttpServletResponse response) {
    ODataRequest odRequest = new ODataRequest();
    Exception exception = null;
    ODataResponse odResponse;
    debugger.resolveDebugMode(request);

    int processMethodHandel = debugger.startRuntimeMeasurement("ODataHttpHandlerImpl", "process");
    try {
      int requestHandel = debugger.startRuntimeMeasurement("ODataHttpHandlerImpl", "fillODataRequest");
      fillODataRequest(odRequest, request, split);
      debugger.stopRuntimeMeasurement(requestHandel);

      int responseHandel = debugger.startRuntimeMeasurement("ODataHandler", "process");
      odResponse = handler.process(odRequest);
      debugger.stopRuntimeMeasurement(responseHandel);
      // ALL future methods after process must not throw exceptions!
    } catch (Exception e) {
      exception = e;
      odResponse = handleException(odRequest, e);
    }
    debugger.stopRuntimeMeasurement(processMethodHandel);

    if (debugger.isDebugMode()) {
      Map<String, String> serverEnvironmentVaribles = createEnvironmentVariablesMap(request);
      if (exception == null) {
        // This is to ensure that we have access to the thrown OData Exception
        exception = handler.getLastThrownException();
      }
      odResponse =
          debugger.createDebugResponse(request, exception, odRequest, odResponse, handler.getUriInfo(),
              serverEnvironmentVaribles);
    }

    convertToHttp(response, odResponse);
  }

  private Map<String, String> createEnvironmentVariablesMap(HttpServletRequest request) {
    LinkedHashMap<String, String> environment = new LinkedHashMap<String, String>();
    environment.put("authType", request.getAuthType());
    environment.put("localAddr", request.getLocalAddr());
    environment.put("localName", request.getLocalName());
    environment.put("localPort", getIntAsString(request.getLocalPort()));
    environment.put("pathInfo", request.getPathInfo());
    environment.put("pathTranslated", request.getPathTranslated());
    environment.put("remoteAddr", request.getRemoteAddr());
    environment.put("remoteHost", request.getRemoteHost());
    environment.put("remotePort", getIntAsString(request.getRemotePort()));
    environment.put("remoteUser", request.getRemoteUser());
    environment.put("scheme", request.getScheme());
    environment.put("serverName", request.getServerName());
    environment.put("serverPort", getIntAsString(request.getServerPort()));
    environment.put("servletPath", request.getServletPath());
    return environment;
  }

  private String getIntAsString(final int number) {
    return number == 0 ? "unknown" : Integer.toString(number);
  }

  @Override
  public void setSplit(final int split) {
    this.split = split;
  }

  private ODataResponse handleException(final ODataRequest odRequest, final Exception e) {
    ODataResponse resp = new ODataResponse();
    ODataServerError serverError;
    if (e instanceof ODataHandlerException) {
      serverError = ODataExceptionHelper.createServerErrorObject((ODataHandlerException) e, null);
    } else if (e instanceof ODataLibraryException) {
      serverError = ODataExceptionHelper.createServerErrorObject((ODataLibraryException) e, null);
    } else {
      serverError = ODataExceptionHelper.createServerErrorObject(e);
    }
    handler.handleException(odRequest, resp, serverError, e);
    return resp;
  }

  static void convertToHttp(final HttpServletResponse response, final ODataResponse odResponse) {
    response.setStatus(odResponse.getStatusCode());

    for (Entry<String, String> entry : odResponse.getHeaders().entrySet()) {
      response.setHeader(entry.getKey(), entry.getValue());
    }

    InputStream input = odResponse.getContent();
    if (input != null) {
      OutputStream output = null;
      try {
        output = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int n;
        while (-1 != (n = input.read(buffer))) {
          output.write(buffer, 0, n);
        }
      } catch (IOException e) {
        LOG.error(e.getMessage(), e);
        throw new ODataRuntimeException(e);
      } finally {
        closeStream(output);
        closeStream(input);
      }
    }
  }

  private static void closeStream(final Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (IOException e) {
        LOG.error(e.getMessage(), e);
      }
    }
  }

  private ODataRequest fillODataRequest(final ODataRequest odRequest, final HttpServletRequest httpRequest,
      final int split)
      throws ODataLibraryException {
    try {
      odRequest.setBody(httpRequest.getInputStream());
      odRequest.setProtocol(httpRequest.getProtocol());
      extractHeaders(odRequest, httpRequest);
      extractUri(odRequest, httpRequest, split);
      extractMethod(odRequest, httpRequest);

      return odRequest;
    } catch (final IOException e) {
      throw new SerializerException("An I/O exception occurred.", e,
          SerializerException.MessageKeys.IO_EXCEPTION);
    }
  }

  static void extractMethod(final ODataRequest odRequest, final HttpServletRequest httpRequest)
      throws ODataLibraryException {
    try {
      HttpMethod httpRequestMethod = HttpMethod.valueOf(httpRequest.getMethod());

      if (httpRequestMethod == HttpMethod.POST) {
        String xHttpMethod = httpRequest.getHeader(HttpHeader.X_HTTP_METHOD);
        String xHttpMethodOverride = httpRequest.getHeader(HttpHeader.X_HTTP_METHOD_OVERRIDE);

        if (xHttpMethod == null && xHttpMethodOverride == null) {
          odRequest.setMethod(httpRequestMethod);
        } else if (xHttpMethod == null) {
          odRequest.setMethod(HttpMethod.valueOf(xHttpMethodOverride));
        } else if (xHttpMethodOverride == null) {
          odRequest.setMethod(HttpMethod.valueOf(xHttpMethod));
        } else {
          if (!xHttpMethod.equalsIgnoreCase(xHttpMethodOverride)) {
            throw new ODataHandlerException("Ambiguous X-HTTP-Methods",
                ODataHandlerException.MessageKeys.AMBIGUOUS_XHTTP_METHOD, xHttpMethod, xHttpMethodOverride);
          }
          odRequest.setMethod(HttpMethod.valueOf(xHttpMethod));
        }
      } else {
        odRequest.setMethod(httpRequestMethod);
      }
    } catch (IllegalArgumentException e) {
      throw new ODataHandlerException("Invalid HTTP method" + httpRequest.getMethod(),
          ODataHandlerException.MessageKeys.INVALID_HTTP_METHOD, httpRequest.getMethod());
    }
  }

  static void extractUri(final ODataRequest odRequest, final HttpServletRequest httpRequest, final int split) {
    String rawRequestUri = httpRequest.getRequestURL().toString();

    String rawODataPath;
    if (!"".equals(httpRequest.getServletPath())) {
      int beginIndex;
      beginIndex = rawRequestUri.indexOf(httpRequest.getServletPath());
      beginIndex += httpRequest.getServletPath().length();
      rawODataPath = rawRequestUri.substring(beginIndex);
    } else if (!"".equals(httpRequest.getContextPath())) {
      int beginIndex;
      beginIndex = rawRequestUri.indexOf(httpRequest.getContextPath());
      beginIndex += httpRequest.getContextPath().length();
      rawODataPath = rawRequestUri.substring(beginIndex);
    } else {
      rawODataPath = httpRequest.getRequestURI();
    }

    String rawServiceResolutionUri;
    if (split > 0) {
      rawServiceResolutionUri = rawODataPath;
      for (int i = 0; i < split; i++) {
        int e = rawODataPath.indexOf("/", 1);
        if (-1 == e) {
          rawODataPath = "";
        } else {
          rawODataPath = rawODataPath.substring(e);
        }
      }
      int end = rawServiceResolutionUri.length() - rawODataPath.length();
      rawServiceResolutionUri = rawServiceResolutionUri.substring(0, end);
    } else {
      rawServiceResolutionUri = null;
    }

    String rawBaseUri = rawRequestUri.substring(0, rawRequestUri.length() - rawODataPath.length());

    odRequest.setRawQueryPath(httpRequest.getQueryString());
    odRequest.setRawRequestUri(rawRequestUri
        + (httpRequest.getQueryString() == null ? "" : "?" + httpRequest.getQueryString()));
    odRequest.setRawODataPath(rawODataPath);
    odRequest.setRawBaseUri(rawBaseUri);
    odRequest.setRawServiceResolutionUri(rawServiceResolutionUri);
  }

  static void extractHeaders(final ODataRequest odRequest, final HttpServletRequest req) {
    for (Enumeration<?> headerNames = req.getHeaderNames(); headerNames.hasMoreElements();) {
      String headerName = (String) headerNames.nextElement();
      List<String> headerValues = new ArrayList<String>();
      for (Enumeration<?> headers = req.getHeaders(headerName); headers.hasMoreElements();) {
        String value = (String) headers.nextElement();
        headerValues.add(value);
      }
      odRequest.addHeader(headerName, headerValues);
    }
  }

  @Override
  public void register(final Processor processor) {
    handler.register(processor);
  }

  @Override
  public void register(final CustomContentTypeSupport customContentTypeSupport) {
    handler.register(customContentTypeSupport);
  }

  @Override
  public void register(final CustomETagSupport customConcurrencyControlSupport) {
    handler.register(customConcurrencyControlSupport);
  }

  @Override
  public void register(final DebugSupport debugSupport) {
    debugger.setDebugSupportProcessor(debugSupport);
  }
}
