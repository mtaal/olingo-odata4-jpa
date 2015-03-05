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
package org.apache.olingo.client.api.communication.request.cud.v3;

import org.apache.olingo.client.api.communication.request.cud.CommonUpdateType;
import org.apache.olingo.commons.api.http.HttpMethod;

/**
 * Update type.
 */
public enum UpdateType implements CommonUpdateType {

  /**
   * Replace all and remove missing attributes.
   */
  REPLACE(HttpMethod.PUT),
  /**
   * Differential update with whole entity as input (non-standard). Differences will be retrieved by the server itself.
   */
  MERGE(HttpMethod.MERGE),
  /**
   * Differential update with only specified input property values to be replaced.
   */
  PATCH(HttpMethod.PATCH);

  private final HttpMethod method;

  private UpdateType(final HttpMethod method) {
    this.method = method;
  }

  /**
   * Gets HTTP request method.
   *
   * @return HTTP request method.
   */
  @Override
  public HttpMethod getMethod() {
    return method;
  }
}