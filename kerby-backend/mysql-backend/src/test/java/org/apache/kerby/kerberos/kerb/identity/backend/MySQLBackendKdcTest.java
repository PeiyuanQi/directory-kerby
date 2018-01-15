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
package org.apache.kerby.kerberos.kerb.identity.backend;

import org.apache.kerby.kerberos.kdc.identitybackend.MySQLConfKey;
import org.apache.kerby.kerberos.kerb.KrbException;
import org.apache.kerby.kerberos.kerb.server.KdcConfigKey;
import org.apache.kerby.kerberos.kerb.server.KdcTestBase;
import org.apache.kerby.kerberos.kerb.type.ticket.SgtTicket;
import org.apache.kerby.kerberos.kerb.type.ticket.TgtTicket;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class MySQLBackendKdcTest extends KdcTestBase {
    private static File testDir = new File(System.getProperty("test.dir", "target"));
    private static File dbFile = new File(testDir, "mysqlbackend.mv.db");

    @After
    public void tearDown() {
        if (dbFile.exists() && !dbFile.delete()) {
            System.err.println("Failed to delete the test database file.");
        }
    }

    @Override
    protected void prepareKdc() throws KrbException {
        BackendConfig backendConfig = getKdcServer().getBackendConfig();

        backendConfig.setString(KdcConfigKey.KDC_IDENTITY_BACKEND,
                "org.apache.kerby.kerberos.kdc.identitybackend.MySQLIdentityBackend");
        backendConfig.setString(MySQLConfKey.MYSQL_DRIVER, "org.h2.Driver");
        backendConfig.setString(MySQLConfKey.MYSQL_URL,
                "jdbc:h2:" + testDir.getAbsolutePath() + "/mysqlbackend;MODE=MySQL");
        backendConfig.setString(MySQLConfKey.MYSQL_USER, "root");
        backendConfig.setString(MySQLConfKey.MYSQL_PASSWORD, "123456");

        super.prepareKdc();
    }

    @Test
    public void testKdc() {
        TgtTicket tgt;
        SgtTicket tkt;

        try {
            tgt = getKrbClient().requestTgt(getClientPrincipal(), getClientPassword());
            assertThat(tgt).isNotNull();

            tkt = getKrbClient().requestSgt(tgt, getServerPrincipal());
            assertThat(tkt).isNotNull();
        } catch (Exception e) {
            Assert.fail("Exception occurred with good password. "
                    + e.toString());
        }
    }
}
