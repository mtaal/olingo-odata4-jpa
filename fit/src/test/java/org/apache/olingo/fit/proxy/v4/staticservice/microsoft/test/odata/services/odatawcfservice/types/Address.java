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
package org.apache.olingo.fit.proxy.v4.staticservice.microsoft.test.odata.services.odatawcfservice.types;

// CHECKSTYLE:OFF (Maven checkstyle)
import org.apache.olingo.ext.proxy.api.Annotatable;

// CHECKSTYLE:ON (Maven checkstyle)

@org.apache.olingo.ext.proxy.api.annotations.Namespace("Microsoft.Test.OData.Services.ODataWCFService")
@org.apache.olingo.ext.proxy.api.annotations.ComplexType(name = "Address",
    isOpenType = false,
    isAbstract = false)
public interface Address
    extends org.apache.olingo.ext.proxy.api.ComplexType<Address>,
    org.apache.olingo.ext.proxy.api.StructuredQuery<Address> {

  @org.apache.olingo.ext.proxy.api.annotations.Property(name = "Street",
      type = "Edm.String",
      nullable = false,
      defaultValue = "",
      maxLenght = Integer.MAX_VALUE,
      fixedLenght = false,
      precision = 0,
      scale = 0,
      unicode = true,
      collation = "",
      srid = "")
  java.lang.String getStreet();

  void setStreet(java.lang.String _street);

  @org.apache.olingo.ext.proxy.api.annotations.Property(name = "City",
      type = "Edm.String",
      nullable = false,
      defaultValue = "",
      maxLenght = Integer.MAX_VALUE,
      fixedLenght = false,
      precision = 0,
      scale = 0,
      unicode = true,
      collation = "",
      srid = "")
  java.lang.String getCity();

  void setCity(java.lang.String _city);

  @org.apache.olingo.ext.proxy.api.annotations.Property(name = "PostalCode",
      type = "Edm.String",
      nullable = false,
      defaultValue = "",
      maxLenght = Integer.MAX_VALUE,
      fixedLenght = false,
      precision = 0,
      scale = 0,
      unicode = true,
      collation = "",
      srid = "")
  java.lang.String getPostalCode();

  void setPostalCode(java.lang.String _postalCode);

  Annotations annotations();

  interface Annotations {

    @org.apache.olingo.ext.proxy.api.annotations.AnnotationsForProperty(name = "Street",
        type = "Edm.String")
    Annotatable getStreetAnnotations();

    @org.apache.olingo.ext.proxy.api.annotations.AnnotationsForProperty(name = "City",
        type = "Edm.String")
    Annotatable getCityAnnotations();

    @org.apache.olingo.ext.proxy.api.annotations.AnnotationsForProperty(name = "PostalCode",
        type = "Edm.String")
    Annotatable getPostalCodeAnnotations();

  }

  Operations operations();

  interface Operations extends org.apache.olingo.ext.proxy.api.Operations {
    // No additional methods needed for now.
  }
}
