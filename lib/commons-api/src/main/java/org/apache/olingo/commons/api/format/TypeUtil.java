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
package org.apache.olingo.commons.api.format;

import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

class TypeUtil {

  static final String MEDIA_TYPE_WILDCARD = "*";
  static final String PARAMETER_Q = "q";

  static final char WHITESPACE_CHAR = ' ';
  static final String PARAMETER_SEPARATOR = ";";
  static final String PARAMETER_KEY_VALUE_SEPARATOR = "=";
  static final String TYPE_SUBTYPE_SEPARATOR = "/";
  static final String TYPE_SUBTYPE_WILDCARD = "*";

  /** Creates a parameter map with predictable order. */
  protected static Map<String, String> createParameterMap() {
    return new TreeMap<String, String>(new Comparator<String>() {
      @Override
      public int compare(final String o1, final String o2) {
        return o1.compareToIgnoreCase(o2);
      }
    });
  }

  /**
   * Valid input are <code>;</code> separated <code>key=value</code> pairs
   * without spaces between key and value.
   * <p>
   * See RFC 7231:
   * The type, subtype, and parameter name tokens are case-insensitive.
   * Parameter values might or might not be case-sensitive, depending on
   * the semantics of the parameter name. The presence or absence of a
   * parameter might be significant to the processing of a media-type,
   * depending on its definition within the media type registry.
   * </p>
   *
   * @param parameters
   * @param parameterMap
   */
  protected static void parseParameters(final String parameters, final Map<String, String> parameterMap) {
    if (parameters != null) {
      for (String parameter : parameters.split(TypeUtil.PARAMETER_SEPARATOR)) {
        final String[] keyValue = parseParameter(parameter);
        parameterMap.put(keyValue[0], keyValue[1]);
      }
    }
  }

  protected static String[] parseParameter(final String parameter) {
    if (parameter.isEmpty()) {
      throw new IllegalArgumentException("An empty parameter is not allowed.");
    }
    String[] keyValue = parameter.trim().split(PARAMETER_KEY_VALUE_SEPARATOR);
    if (keyValue.length != 2) {
      throw new IllegalArgumentException("Parameter '" + parameter + "' must have exactly one '"
          + PARAMETER_KEY_VALUE_SEPARATOR + "' that separates the name and the value.");
    }
    validateParameterNameAndValue(keyValue[0], keyValue[1]);
    keyValue[0] = keyValue[0].toLowerCase(Locale.ENGLISH);
    return keyValue;
  }

  protected static void validateParameterNameAndValue(final String parameterName, final String parameterValue)
      throws IllegalArgumentException {
    if (parameterName == null || parameterName.isEmpty() || parameterName.indexOf(WHITESPACE_CHAR) >= 0) {
      throw new IllegalArgumentException("Illegal parameter name '" + parameterName + "'.");
    }
    if (Character.isWhitespace(parameterValue.charAt(0))) {
      throw new IllegalArgumentException("Value of parameter '" + parameterName + "' starts with whitespace.");
    }
  }
}
