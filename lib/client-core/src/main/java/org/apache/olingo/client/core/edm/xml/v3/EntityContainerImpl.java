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
package org.apache.olingo.client.core.edm.xml.v3;

import java.util.ArrayList;
import java.util.List;

import org.apache.olingo.client.api.edm.xml.EntitySet;
import org.apache.olingo.client.api.edm.xml.v3.AssociationSet;
import org.apache.olingo.client.api.edm.xml.v3.FunctionImport;
import org.apache.olingo.client.core.edm.xml.AbstractEntityContainer;

public class EntityContainerImpl extends AbstractEntityContainer {

  private static final long serialVersionUID = 2822470162604186366L;

  private final List<EntitySet> entitySets = new ArrayList<EntitySet>();

  private final List<AssociationSet> associationSets = new ArrayList<AssociationSet>();

  private final List<FunctionImport> functionImports = new ArrayList<FunctionImport>();

  @Override
  public EntitySetImpl getEntitySet(final String name) {
    return (EntitySetImpl) super.getEntitySet(name);
  }

  @Override
  public List<EntitySet> getEntitySets() {
    return entitySets;
  }

  public List<AssociationSet> getAssociationSets() {
    return associationSets;
  }

  @Override
  public FunctionImportImpl getFunctionImport(final String name) {
    return (FunctionImportImpl) super.getFunctionImport(name);
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<FunctionImport> getFunctionImports(final String name) {
    return (List<FunctionImport>) super.getFunctionImports(name);
  }

  @Override
  public List<FunctionImport> getFunctionImports() {
    return functionImports;
  }

}