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

import static org.junit.Assert.assertEquals;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.olingo.commons.api.http.HttpMethod;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.edm.provider.EdmProvider;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class ServiceDispatcherTest {
  private Server server;


  public class SampleODataServlet extends HttpServlet {
    private final ServiceHandler handler; // must be stateless
    private final EdmProvider provider; // must be stateless

    public SampleODataServlet(ServiceHandler handler, EdmProvider provider) {
      this.handler = handler;
      this.provider = provider;
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
      OData odata = OData4Impl.newInstance();
      ServiceMetadata metadata = odata.createServiceMetadata(this.provider, Collections.EMPTY_LIST);

      ODataHttpHandler handler = odata.createHandler(metadata);

      handler.register(this.handler);
      handler.process(request, response);
    }
  }

  public int beforeTest(ServiceHandler serviceHandler) throws Exception {
    MetadataParser parser = new MetadataParser();
    EdmProvider edmProvider = parser.buildEdmProvider(new FileReader(
        "src/test/resources/trippin.xml"));

    this.server = new Server();

    ServerConnector connector = new ServerConnector(this.server);
    this.server.setConnectors(new Connector[] { connector });

    ServletContextHandler context = new ServletContextHandler();
    context.setContextPath("/trippin");
    context
        .addServlet(new ServletHolder(new SampleODataServlet(serviceHandler, edmProvider)), "/*");
    this.server.setHandler(context);
    this.server.start();

    return connector.getLocalPort();
  }

  public void afterTest() throws Exception {
    this.server.stop();
  }

  interface TestResult {
    void validate() throws Exception;
  }

  private void helpGETTest(ServiceHandler handler, String path, TestResult validator)
      throws Exception {
    int port = beforeTest(handler);
    HttpClient http = new HttpClient();
    http.start();
    http.GET("http://localhost:" + port + "/" + path);
    validator.validate();
    afterTest();
  }

  private void helpPOSTTest(ServiceHandler handler, String path, TestResult validator)
      throws Exception {
    int port = beforeTest(handler);
    HttpClient http = new HttpClient();
    http.start();
    http.POST("http://localhost:" + port + "/" + path).send();
    validator.validate();
    afterTest();
  }

  @Test
  public void testMetadata() throws Exception {
    final ServiceHandler handler = Mockito.mock(ServiceHandler.class);
    helpGETTest(handler, "trippin/$metadata", new TestResult() {
      @Override
      public void validate() throws Exception {
        ArgumentCaptor<MetadataRequest> arg1 = ArgumentCaptor.forClass(MetadataRequest.class);
        ArgumentCaptor<MetadataResponse> arg2 = ArgumentCaptor.forClass(MetadataResponse.class);
        Mockito.verify(handler).readMetadata(arg1.capture(), arg2.capture());
      }
    });
  }

  @Test
  public void testEntitySet() throws Exception {
    final ServiceHandler handler = Mockito.mock(ServiceHandler.class);
    helpGETTest(handler, "trippin/Airports", new TestResult() {
      @Override
      public void validate() throws Exception {
        ArgumentCaptor<DataRequest> arg1 = ArgumentCaptor.forClass(DataRequest.class);
        ArgumentCaptor<EntityResponse> arg2 = ArgumentCaptor.forClass(EntityResponse.class);
        Mockito.verify(handler).read(arg1.capture(), arg2.capture());

        DataRequest request = arg1.getValue();
        // Need toString on ContextURL class
        // assertEquals("",
        // request.getContextURL(request.getOdata()).toString());
        assertEquals("application/json;odata.metadata=minimal", request.getResponseContentType()
            .toContentTypeString());
      }
    });
  }

  @Test
  public void testEntitySetCount() throws Exception {
    final ServiceHandler handler = Mockito.mock(ServiceHandler.class);
    helpGETTest(handler, "trippin/Airports/$count", new TestResult() {
      @Override
      public void validate() throws Exception {
        ArgumentCaptor<DataRequest> arg1 = ArgumentCaptor.forClass(DataRequest.class);
        ArgumentCaptor<CountResponse> arg2 = ArgumentCaptor.forClass(CountResponse.class);
        Mockito.verify(handler).read(arg1.capture(), arg2.capture());

        DataRequest request = arg1.getValue();
        // Need toString on ContextURL class
        // assertEquals("",
        // request.getContextURL(request.getOdata()).toString());
        assertEquals("text/plain", request.getResponseContentType().toContentTypeString());
      }
    });
  }

  @Test
  public void testEntity() throws Exception {
    final ServiceHandler handler = Mockito.mock(ServiceHandler.class);
    helpGETTest(handler, "trippin/Airports('0')", new TestResult() {
      @Override
      public void validate() throws Exception {
        ArgumentCaptor<DataRequest> arg1 = ArgumentCaptor.forClass(DataRequest.class);
        ArgumentCaptor<EntityResponse> arg2 = ArgumentCaptor.forClass(EntityResponse.class);
        Mockito.verify(handler).read(arg1.capture(), arg2.capture());

        DataRequest request = arg1.getValue();
        assertEquals(1, request.getUriResourceEntitySet().getKeyPredicates().size());
        assertEquals("application/json;odata.metadata=minimal", request.getResponseContentType()
            .toContentTypeString());
      }
    });
  }

  @Test
  public void testReadProperty() throws Exception {
    final ServiceHandler handler = Mockito.mock(ServiceHandler.class);
    helpGETTest(handler, "trippin/Airports('0')/IataCode", new TestResult() {
      @Override
      public void validate() throws Exception {
        ArgumentCaptor<DataRequest> arg1 = ArgumentCaptor.forClass(DataRequest.class);
        ArgumentCaptor<PropertyResponse> arg2 = ArgumentCaptor.forClass(PropertyResponse.class);
        Mockito.verify(handler).read(arg1.capture(), arg2.capture());

        DataRequest request = arg1.getValue();
        assertEquals(true, request.isPropertyRequest());
        assertEquals(false, request.isPropertyComplex());
        assertEquals(1, request.getUriResourceEntitySet().getKeyPredicates().size());
        assertEquals("application/json;odata.metadata=minimal", request.getResponseContentType()
            .toContentTypeString());
      }
    });
  }

  @Test
  public void testReadComplexProperty() throws Exception {
    final ServiceHandler handler = Mockito.mock(ServiceHandler.class);
    helpGETTest(handler, "trippin/Airports('0')/Location", new TestResult() {
      @Override
      public void validate() throws Exception {
        ArgumentCaptor<DataRequest> arg1 = ArgumentCaptor.forClass(DataRequest.class);
        ArgumentCaptor<PropertyResponse> arg2 = ArgumentCaptor.forClass(PropertyResponse.class);
        Mockito.verify(handler).read(arg1.capture(), arg2.capture());

        DataRequest request = arg1.getValue();
        assertEquals(true, request.isPropertyRequest());
        assertEquals(true, request.isPropertyComplex());
        assertEquals(1, request.getUriResourceEntitySet().getKeyPredicates().size());
        assertEquals("application/json;odata.metadata=minimal", request.getResponseContentType()
            .toContentTypeString());
      }
    });
  }

  @Test
  public void testReadProperty$Value() throws Exception {
    final ServiceHandler handler = Mockito.mock(ServiceHandler.class);
    helpGETTest(handler, "trippin/Airports('0')/IataCode/$value", new TestResult() {
      @Override
      public void validate() throws Exception {
        ArgumentCaptor<DataRequest> arg1 = ArgumentCaptor.forClass(DataRequest.class);
        ArgumentCaptor<PrimitiveValueResponse> arg2 = ArgumentCaptor
            .forClass(PrimitiveValueResponse.class);
        Mockito.verify(handler).read(arg1.capture(), arg2.capture());

        DataRequest request = arg1.getValue();
        assertEquals(true, request.isPropertyRequest());
        assertEquals(false, request.isPropertyComplex());
        assertEquals(1, request.getUriResourceEntitySet().getKeyPredicates().size());
        assertEquals("text/plain", request.getResponseContentType().toContentTypeString());
      }
    });
  }

  @Test
  public void testReadPropertyRef() throws Exception {
    final ServiceHandler handler = Mockito.mock(ServiceHandler.class);
    helpGETTest(handler, "trippin/Airports('0')/IataCode/$value", new TestResult() {
      @Override
      public void validate() throws Exception {
        ArgumentCaptor<DataRequest> arg1 = ArgumentCaptor.forClass(DataRequest.class);
        ArgumentCaptor<PrimitiveValueResponse> arg2 = ArgumentCaptor
            .forClass(PrimitiveValueResponse.class);
        Mockito.verify(handler).read(arg1.capture(), arg2.capture());

        DataRequest request = arg1.getValue();
        assertEquals(true, request.isPropertyRequest());
        assertEquals(false, request.isPropertyComplex());
        assertEquals(1, request.getUriResourceEntitySet().getKeyPredicates().size());
        assertEquals("text/plain", request.getResponseContentType().toContentTypeString());
      }
    });
  }

  @Test
  public void testFunctionImport() throws Exception {
    final ServiceHandler handler = Mockito.mock(ServiceHandler.class);
    helpGETTest(handler, "trippin/GetNearestAirport(lat=12.11,lon=34.23)", new TestResult() {
      @Override
      public void validate() throws Exception {
        ArgumentCaptor<FunctionRequest> arg1 = ArgumentCaptor.forClass(FunctionRequest.class);
        ArgumentCaptor<PropertyResponse> arg3 = ArgumentCaptor.forClass(PropertyResponse.class);
        ArgumentCaptor<HttpMethod> arg2 = ArgumentCaptor.forClass(HttpMethod.class);
        Mockito.verify(handler).invoke(arg1.capture(), arg2.capture(), arg3.capture());

        FunctionRequest request = arg1.getValue();
      }
    });
  }

  @Test
  public void testActionImport() throws Exception {
    final ServiceHandler handler = Mockito.mock(ServiceHandler.class);
    helpPOSTTest(handler, "trippin/ResetDataSource", new TestResult() {
      @Override
      public void validate() throws Exception {
        ArgumentCaptor<ActionRequest> arg1 = ArgumentCaptor.forClass(ActionRequest.class);
        ArgumentCaptor<NoContentResponse> arg2 = ArgumentCaptor.forClass(NoContentResponse.class);
        Mockito.verify(handler).invoke(arg1.capture(), Mockito.anyString(), arg2.capture());

        ActionRequest request = arg1.getValue();
      }
    });
  }

  @Test
  public void testReadMedia() throws Exception {
    final ServiceHandler handler = Mockito.mock(ServiceHandler.class);
    helpGETTest(handler, "trippin/Photos(1)/$value", new TestResult() {
      @Override
      public void validate() throws Exception {
        ArgumentCaptor<MediaRequest> arg1 = ArgumentCaptor.forClass(MediaRequest.class);
        ArgumentCaptor<StreamResponse> arg2 = ArgumentCaptor.forClass(StreamResponse.class);
        Mockito.verify(handler).readMediaStream(arg1.capture(), arg2.capture());

        MediaRequest request = arg1.getValue();
        assertEquals("application/octet-stream", request.getResponseContentType()
            .toContentTypeString());
      }
    });
  }
}