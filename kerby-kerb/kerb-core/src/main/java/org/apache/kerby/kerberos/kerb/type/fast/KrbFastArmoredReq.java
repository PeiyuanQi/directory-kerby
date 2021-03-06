/**
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.apache.kerby.kerberos.kerb.type.fast;

import org.apache.kerby.asn1.Asn1FieldInfo;
import org.apache.kerby.asn1.EnumType;
import org.apache.kerby.asn1.ExplicitField;
import org.apache.kerby.kerberos.kerb.type.KrbSequenceType;
import org.apache.kerby.kerberos.kerb.type.base.CheckSum;
import org.apache.kerby.kerberos.kerb.type.base.EncryptedData;

/**
 KrbFastArmoredReq ::= SEQUENCE {
     armor        [0] KrbFastArmor OPTIONAL,
     -- Contains the armor that identifies the armor key.
     -- MUST be present in AS-REQ.
     req-checksum [1] Checksum,
     -- For AS, contains the checksum performed over the type
     -- KDC-REQ-BODY for the req-body field of the KDC-REQ
     -- structure;
     -- For TGS, contains the checksum performed over the type
     -- AP-REQ in the PA-TGS-REQ padata.
     -- The checksum key is the armor key, the checksum
     -- type is the required checksum type for the enctype of
     -- the armor key, and the key usage number is
     -- KEY_USAGE_FAST_REQ_CHKSUM.
     enc-fast-req [2] EncryptedData, -- KrbFastReq --
     -- The encryption key is the armor key, and the key usage
     -- number is KEY_USAGE_FAST_ENC.
 }
 */
public class KrbFastArmoredReq extends KrbSequenceType {
    protected enum KrbFastArmoredReqField implements EnumType {
        ARMOR,
        REQ_CHECKSUM,
        ENC_FAST_REQ;

        @Override
        public int getValue() {
            return ordinal();
        }

        @Override
        public String getName() {
            return name();
        }
    }

    private KrbFastReq fastReq;

    static Asn1FieldInfo[] fieldInfos = new Asn1FieldInfo[] {
            new ExplicitField(KrbFastArmoredReqField.ARMOR, KrbFastArmor.class),
            new ExplicitField(KrbFastArmoredReqField.REQ_CHECKSUM, CheckSum.class),
            new ExplicitField(KrbFastArmoredReqField.ENC_FAST_REQ, EncryptedData.class),
    };

    public KrbFastArmoredReq() {
        super(fieldInfos);
    }

    public KrbFastArmor getArmor() {
        return getFieldAs(KrbFastArmoredReqField.ARMOR, KrbFastArmor.class);
    }

    public void setArmor(KrbFastArmor armor) {
        setFieldAs(KrbFastArmoredReqField.ARMOR, armor);
    }

    public CheckSum getReqChecksum() {
        return getFieldAs(KrbFastArmoredReqField.REQ_CHECKSUM, CheckSum.class);
    }

    public void setReqChecksum(CheckSum checkSum) {
        setFieldAs(KrbFastArmoredReqField.REQ_CHECKSUM, checkSum);
    }

    public KrbFastReq getFastReq() {
        return fastReq;
    }

    public void setFastReq(KrbFastReq fastReq) {
        this.fastReq = fastReq;
    }

    public EncryptedData getEncryptedFastReq() {
        return getFieldAs(KrbFastArmoredReqField.ENC_FAST_REQ, EncryptedData.class);
    }

    public void setEncryptedFastReq(EncryptedData encFastReq) {
        setFieldAs(KrbFastArmoredReqField.ENC_FAST_REQ, encFastReq);
    }
}
