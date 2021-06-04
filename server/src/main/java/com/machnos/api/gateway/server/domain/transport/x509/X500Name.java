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

public class X500Name {

    private final org.bouncycastle.asn1.x500.X500Name x500Name;

    public X500Name(org.bouncycastle.asn1.x500.X500Name x500Name) {
        this.x500Name = x500Name;
    }

    public String getDN() {
        return this.x500Name.toString();
    }

    public String getCN() {
        return getValue(BCStyle.CN);
    }

    public String getC() {
        return getValue(BCStyle.C);
    }

    public String getO() {
        return getValue(BCStyle.O);
    }

    public String getOU() {
        return getValue(BCStyle.OU);
    }

    public String getL() {
        return getValue(BCStyle.L);
    }

    public String getST() {
        return getValue(BCStyle.ST);
    }

    public String getStreet() {
        return getValue(BCStyle.STREET);
    }

    public String getDC() {
        return getValue(BCStyle.DC);
    }

    public String getUID() {
        return getValue(BCStyle.UID);
    }

    private String getValue(ASN1ObjectIdentifier objectIdentifier) {
        final var  rdNs = x500Name.getRDNs(objectIdentifier);
        if (rdNs == null || rdNs.length == 0) {
            return null;
        }
        return IETFUtils.valueToString(rdNs[0].getFirst().getValue());
    }
}
