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
package org.apache.olingo.fit.proxy.v4.staticservice.microsoft.test.odata.services.odatawcfservice.types;

import org.apache.olingo.client.api.http.HttpMethod;
import org.apache.olingo.ext.proxy.api.annotations.AnnotationsForProperty;
import org.apache.olingo.ext.proxy.api.annotations.AnnotationsForNavigationProperty;
import org.apache.olingo.ext.proxy.api.annotations.Namespace;
import org.apache.olingo.ext.proxy.api.annotations.EntityType;
import org.apache.olingo.ext.proxy.api.annotations.Key;
import org.apache.olingo.ext.proxy.api.annotations.KeyRef;
import org.apache.olingo.ext.proxy.api.annotations.NavigationProperty;
import org.apache.olingo.ext.proxy.api.annotations.Property;
import org.apache.olingo.ext.proxy.api.annotations.Operation;
import org.apache.olingo.ext.proxy.api.annotations.Parameter;
import org.apache.olingo.ext.proxy.api.Annotatable;
import org.apache.olingo.ext.proxy.api.AbstractOpenType;
import org.apache.olingo.ext.proxy.api.OperationType;
import org.apache.olingo.commons.api.edm.constants.EdmContentKind;
import org.apache.olingo.client.api.edm.ConcurrencyMode;
import org.apache.olingo.fit.proxy.v4.staticservice.microsoft.test.odata.services.odatawcfservice.*;
import org.apache.olingo.fit.proxy.v4.staticservice.microsoft.test.odata.services.odatawcfservice.types.*;

import org.apache.olingo.commons.api.edm.geo.Geospatial;
import org.apache.olingo.commons.api.edm.geo.GeospatialCollection;
import org.apache.olingo.commons.api.edm.geo.LineString;
import org.apache.olingo.commons.api.edm.geo.MultiLineString;
import org.apache.olingo.commons.api.edm.geo.MultiPoint;
import org.apache.olingo.commons.api.edm.geo.MultiPolygon;
import org.apache.olingo.commons.api.edm.geo.Point;
import org.apache.olingo.commons.api.edm.geo.Polygon;
import java.math.BigDecimal;
import java.net.URI;
import java.util.UUID;
import java.io.Serializable;
import java.util.Collection;
import java.util.Calendar;
import javax.xml.datatype.Duration;


@Namespace("Microsoft.Test.OData.Services.ODataWCFService")
@EntityType(name = "Employee",
        openType = false,
        hasStream = false,
        isAbstract = false,
        baseType = "Microsoft.Test.OData.Services.ODataWCFService.Person")
public interface Employee 
  extends Annotatable,org.apache.olingo.fit.proxy.v4.staticservice.microsoft.test.odata.services.odatawcfservice.types.Person {

    
    @Key
    @Property(name = "PersonID", 
                type = "Edm.Int32", 
                nullable = false,
                defaultValue = "",
                maxLenght = Integer.MAX_VALUE,
                fixedLenght = false,
                precision = 0,
                scale = 0,
                unicode = true,
                collation = "",
                srid = "",
                concurrencyMode = ConcurrencyMode.None,
                fcSourcePath = "",
                fcTargetPath = "",
                fcContentKind = EdmContentKind.text,
                fcNSPrefix = "",
                fcNSURI = "",
                fcKeepInContent = false)
    Integer getPersonID();

    void setPersonID(Integer _personID);    
    
    
    @Property(name = "FirstName", 
                type = "Edm.String", 
                nullable = false,
                defaultValue = "",
                maxLenght = Integer.MAX_VALUE,
                fixedLenght = false,
                precision = 0,
                scale = 0,
                unicode = true,
                collation = "",
                srid = "",
                concurrencyMode = ConcurrencyMode.None,
                fcSourcePath = "",
                fcTargetPath = "",
                fcContentKind = EdmContentKind.text,
                fcNSPrefix = "",
                fcNSURI = "",
                fcKeepInContent = false)
    String getFirstName();

    void setFirstName(String _firstName);    
    
    
    @Property(name = "LastName", 
                type = "Edm.String", 
                nullable = false,
                defaultValue = "",
                maxLenght = Integer.MAX_VALUE,
                fixedLenght = false,
                precision = 0,
                scale = 0,
                unicode = true,
                collation = "",
                srid = "",
                concurrencyMode = ConcurrencyMode.None,
                fcSourcePath = "",
                fcTargetPath = "",
                fcContentKind = EdmContentKind.text,
                fcNSPrefix = "",
                fcNSURI = "",
                fcKeepInContent = false)
    String getLastName();

    void setLastName(String _lastName);    
    
    
    @Property(name = "MiddleName", 
                type = "Edm.String", 
                nullable = true,
                defaultValue = "",
                maxLenght = Integer.MAX_VALUE,
                fixedLenght = false,
                precision = 0,
                scale = 0,
                unicode = true,
                collation = "",
                srid = "",
                concurrencyMode = ConcurrencyMode.None,
                fcSourcePath = "",
                fcTargetPath = "",
                fcContentKind = EdmContentKind.text,
                fcNSPrefix = "",
                fcNSURI = "",
                fcKeepInContent = false)
    String getMiddleName();

    void setMiddleName(String _middleName);    
    
    
    @Property(name = "HomeAddress", 
                type = "Microsoft.Test.OData.Services.ODataWCFService.Address", 
                nullable = true,
                defaultValue = "",
                maxLenght = Integer.MAX_VALUE,
                fixedLenght = false,
                precision = 0,
                scale = 0,
                unicode = true,
                collation = "",
                srid = "",
                concurrencyMode = ConcurrencyMode.None,
                fcSourcePath = "",
                fcTargetPath = "",
                fcContentKind = EdmContentKind.text,
                fcNSPrefix = "",
                fcNSURI = "",
                fcKeepInContent = false)
    org.apache.olingo.fit.proxy.v4.staticservice.microsoft.test.odata.services.odatawcfservice.types.Address getHomeAddress();

    void setHomeAddress(org.apache.olingo.fit.proxy.v4.staticservice.microsoft.test.odata.services.odatawcfservice.types.Address _homeAddress);    
        
    
    @Property(name = "Home", 
                type = "Edm.GeographyPoint", 
                nullable = true,
                defaultValue = "",
                maxLenght = Integer.MAX_VALUE,
                fixedLenght = false,
                precision = 0,
                scale = 0,
                unicode = true,
                collation = "",
                srid = "",
                concurrencyMode = ConcurrencyMode.None,
                fcSourcePath = "",
                fcTargetPath = "",
                fcContentKind = EdmContentKind.text,
                fcNSPrefix = "",
                fcNSURI = "",
                fcKeepInContent = false)
    Point getHome();

    void setHome(Point _home);    
    
    
    @Property(name = "Numbers", 
                type = "Edm.String", 
                nullable = false,
                defaultValue = "",
                maxLenght = Integer.MAX_VALUE,
                fixedLenght = false,
                precision = 0,
                scale = 0,
                unicode = true,
                collation = "",
                srid = "",
                concurrencyMode = ConcurrencyMode.None,
                fcSourcePath = "",
                fcTargetPath = "",
                fcContentKind = EdmContentKind.text,
                fcNSPrefix = "",
                fcNSURI = "",
                fcKeepInContent = false)
    Collection<String> getNumbers();

    void setNumbers(Collection<String> _numbers);    
    
    
    @Property(name = "Emails", 
                type = "Edm.String", 
                nullable = true,
                defaultValue = "",
                maxLenght = Integer.MAX_VALUE,
                fixedLenght = false,
                precision = 0,
                scale = 0,
                unicode = true,
                collation = "",
                srid = "",
                concurrencyMode = ConcurrencyMode.None,
                fcSourcePath = "",
                fcTargetPath = "",
                fcContentKind = EdmContentKind.text,
                fcNSPrefix = "",
                fcNSURI = "",
                fcKeepInContent = false)
    Collection<String> getEmails();

    void setEmails(Collection<String> _emails);    
    
    
    @Property(name = "DateHired", 
                type = "Edm.DateTimeOffset", 
                nullable = false,
                defaultValue = "",
                maxLenght = Integer.MAX_VALUE,
                fixedLenght = false,
                precision = 0,
                scale = 0,
                unicode = true,
                collation = "",
                srid = "",
                concurrencyMode = ConcurrencyMode.None,
                fcSourcePath = "",
                fcTargetPath = "",
                fcContentKind = EdmContentKind.text,
                fcNSPrefix = "",
                fcNSURI = "",
                fcKeepInContent = false)
    Calendar getDateHired();

    void setDateHired(Calendar _dateHired);    
    
    
    @Property(name = "Office", 
                type = "Edm.GeographyPoint", 
                nullable = true,
                defaultValue = "",
                maxLenght = Integer.MAX_VALUE,
                fixedLenght = false,
                precision = 0,
                scale = 0,
                unicode = true,
                collation = "",
                srid = "",
                concurrencyMode = ConcurrencyMode.None,
                fcSourcePath = "",
                fcTargetPath = "",
                fcContentKind = EdmContentKind.text,
                fcNSPrefix = "",
                fcNSURI = "",
                fcKeepInContent = false)
    Point getOffice();

    void setOffice(Point _office);    
    
    

    @NavigationProperty(name = "Parent", 
                type = "Microsoft.Test.OData.Services.ODataWCFService.Person", 
                targetSchema = "Microsoft.Test.OData.Services.ODataWCFService", 
                targetContainer = "InMemoryEntities", 
                targetEntitySet = "People",
                containsTarget = false)
    org.apache.olingo.fit.proxy.v4.staticservice.microsoft.test.odata.services.odatawcfservice.types.Person getParent();

    void setParent(org.apache.olingo.fit.proxy.v4.staticservice.microsoft.test.odata.services.odatawcfservice.types.Person _parent);
    
    @NavigationProperty(name = "Company", 
                type = "Microsoft.Test.OData.Services.ODataWCFService.Company", 
                targetSchema = "Microsoft.Test.OData.Services.ODataWCFService", 
                targetContainer = "InMemoryEntities", 
                targetEntitySet = "Company",
                containsTarget = false)
    org.apache.olingo.fit.proxy.v4.staticservice.microsoft.test.odata.services.odatawcfservice.types.Company getCompany();

    void setCompany(org.apache.olingo.fit.proxy.v4.staticservice.microsoft.test.odata.services.odatawcfservice.types.Company _company);
    

        @Override
        Operations operations();

    interface Operations            extends org.apache.olingo.fit.proxy.v4.staticservice.microsoft.test.odata.services.odatawcfservice.types.Person.Operations{
    
        }

        @Override
        ComplexFactory factory();

    interface ComplexFactory            extends org.apache.olingo.fit.proxy.v4.staticservice.microsoft.test.odata.services.odatawcfservice.types.Person.ComplexFactory{
             @Property(name = "HomeAddress",
                   type = "Microsoft.Test.OData.Services.ODataWCFService.Address")
         org.apache.olingo.fit.proxy.v4.staticservice.microsoft.test.odata.services.odatawcfservice.types.Address newHomeAddress();

        }

        @Override
        Annotations annotations();

    interface Annotations            extends org.apache.olingo.fit.proxy.v4.staticservice.microsoft.test.odata.services.odatawcfservice.types.Person.Annotations{

            @AnnotationsForProperty(name = "PersonID",
                   type = "Edm.Int32")
        Annotatable getPersonIDAnnotations();

            @AnnotationsForProperty(name = "FirstName",
                   type = "Edm.String")
        Annotatable getFirstNameAnnotations();

            @AnnotationsForProperty(name = "LastName",
                   type = "Edm.String")
        Annotatable getLastNameAnnotations();

            @AnnotationsForProperty(name = "MiddleName",
                   type = "Edm.String")
        Annotatable getMiddleNameAnnotations();

            @AnnotationsForProperty(name = "HomeAddress",
                   type = "Microsoft.Test.OData.Services.ODataWCFService.Address")
        Annotatable getHomeAddressAnnotations();

            @AnnotationsForProperty(name = "Home",
                   type = "Edm.GeographyPoint")
        Annotatable getHomeAnnotations();

            @AnnotationsForProperty(name = "Numbers",
                   type = "Edm.String")
        Annotatable getNumbersAnnotations();

            @AnnotationsForProperty(name = "Emails",
                   type = "Edm.String")
        Annotatable getEmailsAnnotations();

            @AnnotationsForProperty(name = "DateHired",
                   type = "Edm.DateTimeOffset")
        Annotatable getDateHiredAnnotations();

            @AnnotationsForProperty(name = "Office",
                   type = "Edm.GeographyPoint")
        Annotatable getOfficeAnnotations();

    
    
        @AnnotationsForNavigationProperty(name = "Parent", 
                  type = "Microsoft.Test.OData.Services.ODataWCFService.Person")
        Annotatable getParentAnnotations();
    
        @AnnotationsForNavigationProperty(name = "Company", 
                  type = "Microsoft.Test.OData.Services.ODataWCFService.Company")
        Annotatable getCompanyAnnotations();
        }
}