/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.olingo.client.core.it.v3;

import org.apache.olingo.client.core.http.AbstractBasicAuthHttpClientFactory;
import org.apache.olingo.client.core.http.DefaultHttpClientFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class AuthEntityRetrieveTestITCase extends EntityRetrieveTestITCase {

  @BeforeClass
  public static void enableBasicAuth() {
    client.getConfiguration().setHttpClientFactory(new AbstractBasicAuthHttpClientFactory() {
      private static final long serialVersionUID = 1L;

      @Override
      protected String getUsername() {
        return "odatajclient";
      }

      @Override
      protected String getPassword() {
        return "odatajclient";
      }
    });
  }

  @AfterClass
  public static void disableBasicAuth() {
    client.getConfiguration().setHttpClientFactory(new DefaultHttpClientFactory());
  }

  @Override
  protected String getServiceRoot() {
    return testAuthServiceRootURL;
  }
}