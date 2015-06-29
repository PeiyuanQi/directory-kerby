package org.apache.kerby.kerberos.kerb.server;

import org.apache.kerby.kerberos.kerb.KrbException;
import org.apache.kerby.kerberos.kerb.identity.backend.IdentityBackend;
import org.apache.kerby.kerberos.kerb.identity.backend.MemoryIdentityBackend;

import java.io.File;
import java.io.IOException;

/**
 * KDC side utilities.
 */
public final class KdcUtil {

    private KdcUtil() {}

    public static KdcConfig getKdcConfig(File confDir) throws KrbException {
        File kdcConfFile = new File(confDir, "kdc.conf");
        if (kdcConfFile.exists()) {
            KdcConfig kdcConfig = new KdcConfig();
            try {
                kdcConfig.addIniConfig(kdcConfFile);
            } catch (IOException e) {
                throw new KrbException("Can not load the kdc configuration file "
                        + kdcConfFile.getAbsolutePath());
            }
            return kdcConfig;
        }

        return null;
    }

    public static BackendConfig getBackendConfig(File confDir) throws KrbException {
        File backendConfigFile = new File(confDir, "backend.conf");
        if (backendConfigFile.exists()) {
            BackendConfig backendConfig = new BackendConfig();
            try {
                backendConfig.addIniConfig(backendConfigFile);
            } catch (IOException e) {
                throw new KrbException("Can not load the backend configuration file "
                        + backendConfigFile.getAbsolutePath());
            }
            return backendConfig;
        }

        return null;
    }

    /**
     * Init the identity backend from backend configuration.
     */
    public static IdentityBackend getBackend(
            BackendConfig backendConfig) throws KrbException {
        String backendClassName = backendConfig.getString(
                KdcConfigKey.KDC_IDENTITY_BACKEND);
        if (backendClassName == null) {
            backendClassName = MemoryIdentityBackend.class.getCanonicalName();
        }

        Class<?> backendClass;
        try {
            backendClass = Class.forName(backendClassName);
        } catch (ClassNotFoundException e) {
            throw new KrbException("Failed to load backend class: "
                    + backendClassName);
        }

        IdentityBackend backend;
        try {
            backend = (IdentityBackend) backendClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new KrbException("Failed to create backend: "
                    + backendClassName);
        }

        backend.setConfig(backendConfig);
        backend.initialize();
        return backend;
    }
}