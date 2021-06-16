/*
 * Licensed to Machnos under one or more contributor license
 * agreements. Machnos licenses this file to you under the
 * Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.machnos.api.gateway.server.domain.api.variables;

import com.machnos.api.gateway.server.domain.transport.x509.X500Name;

/**
 * <code>VariableHandler</code> implementation that can handle values of a <code>X500Name</code> instance.
 */
public class X500NameVariableHandler extends AbstractVariableHandler<X500Name>{

    /**
     * The distinguished name variable.
     */
    public static final String DISTINGUISHED_NAME = "dn";
    /**
     * The common name variable.
     */
    public static final String COMMON_NAME = "cn";
    /**
     * The country variable.
     */
    public static final String COUNTRY = "c";
    /**
     * The organization variable.
     */
    public static final String ORGANIZATION = "o";
    /**
     * The organizational unit variable.
     */
    public static final String ORGANIZATIONAL_UNIT = "ou";
    /**
     * The locality variable.
     */
    public static final String LOCALITY = "l";
    /**
     * The state variable.
     */
    public static final String STATE = "st";
    /**
     * The street variable.
     */
    public static final String STREET = "street";
    /**
     * The domain component variable.
     */
    public static final String DOMAIN_COMPONENT = "dc";
    /**
     * The email address variable.
     */
    public static final String EMAIL = "e";
    /**
     * The user id variable.
     */
    public static final String USER_ID = "uid";

    @Override
    public Object getValue(String variable, X500Name x500Name) {
        if (NO_VARIABLE.equals(variable)) {
            return x500Name;
        } else if (DISTINGUISHED_NAME.equals(variable)) {
            return x500Name.getDN();
        } else if (COMMON_NAME.equals(variable)) {
            return x500Name.getCN();
        } else if (COUNTRY.equals(variable)) {
            return x500Name.getC();
        } else if (ORGANIZATION.equals(variable)) {
            return x500Name.getO();
        } else if (ORGANIZATIONAL_UNIT.equals(variable)) {
            return x500Name.getOU();
        } else if (LOCALITY.equals(variable)) {
            return x500Name.getL();
        } else if (STATE.equals(variable)) {
            return x500Name.getST();
        } else if (STREET.equals(variable)) {
            return x500Name.getStreet();
        } else if (DOMAIN_COMPONENT.equals(variable)) {
            return x500Name.getDC();
        } else if (EMAIL.equals(variable)) {
            return x500Name.getE();
        } else if (USER_ID.equals(variable)) {
            return x500Name.getUID();
        }
        return null;
    }
}
