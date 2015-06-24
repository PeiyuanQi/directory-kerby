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
package org.apache.kerby.kerberos.kerb.server;

import org.apache.kerby.KOptions;
import org.apache.kerby.kerberos.kerb.KrbException;
import org.apache.kerby.kerberos.kerb.identity.IdentityService;

/**
 * An internal KDC server interface.
 */
public interface InternalKdcServer {

    /**
     * Initialize with KDC startup options.
     * @param options
     */
    void init(KOptions options) throws KrbException;

    /**
     * Start the KDC server.
     */
    void start();

    /**
     * Stop the KDC server.
     */
    void stop();

    /**
     * Get KDC setting.
     * @return setting
     */
    KdcSetting getSetting();

    /**
     * Get identity service.
     * @return IdentityService
     */
    IdentityService getIdentityService();
}
