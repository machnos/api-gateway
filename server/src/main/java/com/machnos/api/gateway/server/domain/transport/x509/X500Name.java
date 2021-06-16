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

package com.machnos.api.gateway.server.domain.transport.x509;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;

/**
 * Class representing a X500 name.
 */
public class X500Name {

    /**
     * The <code>X500Name</code> that is wrapped by this class.
     */
    private final org.bouncycastle.asn1.x500.X500Name x500Name;

    /**
     * Constructs a new <code>X500Name</code> instance.
     *
     * @param x500Name The BouncyCastle <code>X500Name</code> that is wrapped by this class
     */
    public X500Name(org.bouncycastle.asn1.x500.X500Name x500Name) {
        this.x500Name = x500Name;
    }

    /**
     * Gives the distinguished name.
     *
     * @return The distinguished name, or <code>null</code> when there is no distinguished name.
     */
    public String getDN() {
        return this.x500Name.toString();
    }

    /**
     * Gives the common name.
     *
     * @return The common name, or <code>null</code> when there is no common name.
     */
    public String getCN() {
        return getValue(BCStyle.CN);
    }

    /**
     * Gives the country.
     *
     * @return The country, or <code>null</code> when there is no country.
     */
    public String getC() {
        return getValue(BCStyle.C);
    }

    /**
     * Gives the organization.
     *
     * @return The organization, or <code>null</code> when there is no organization.
     */
    public String getO() {
        return getValue(BCStyle.O);
    }

    /**
     * Gives the organizational unit.
     *
     * @return The organizational unit, or <code>null</code> when there is no organizational unit.
     */
    public String getOU() {
        return getValue(BCStyle.OU);
    }

    /**
     * Gives the locality.
     *
     * @return The locality, or <code>null</code> when there is no locality.
     */
    public String getL() {
        return getValue(BCStyle.L);
    }

    /**
     * Gives the state or province.
     *
     * @return The state or province, or <code>null</code> when there is no state or province.
     */
    public String getST() {
        return getValue(BCStyle.ST);
    }

    /**
     * Gives the street.
     *
     * @return The street, or <code>null</code> when there is no street.
     */
    public String getStreet() {
        return getValue(BCStyle.STREET);
    }

    /**
     * Gives the domain component.
     *
     * @return The domain component, or <code>null</code> when there is no domain component.
     */
    public String getDC() {
        return getValue(BCStyle.DC);
    }

    /**
     * Gives the email address.
     *
     * @return The email address, or <code>null</code> when there is no email address.
     */
    public String getE() {
        return getValue(BCStyle.E);
    }

    /**
     * Gives the user id.
     *
     * @return The user id, or <code>null</code> when there is no user id.
     */
    public String getUID() {
        return getValue(BCStyle.UID);
    }

    /**
     * Gives the value of an object identifier.
     *
     * @param objectIdentifier The <code>ASN1ObjectIdentifier</code> to find the value for.
     * @return The first value of the object identifier, or <code>null</code> when it does not exists.
     */
    private String getValue(ASN1ObjectIdentifier objectIdentifier) {
        final var  rdNs = x500Name.getRDNs(objectIdentifier);
        if (rdNs == null || rdNs.length == 0) {
            return null;
        }
        return IETFUtils.valueToString(rdNs[0].getFirst().getValue());
    }
}
