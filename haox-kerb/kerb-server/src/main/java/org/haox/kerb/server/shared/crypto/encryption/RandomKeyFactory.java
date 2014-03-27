package org.haox.kerb.server.shared.crypto.encryption;

import org.apache.directory.server.i18n.I18n;
import org.apache.directory.shared.kerberos.exceptions.ErrorType;
import org.apache.directory.shared.kerberos.exceptions.KerberosException;
import org.haox.kerb.spec.KrbException;
import org.haox.kerb.spec.type.common.EncryptionKey;
import org.haox.kerb.spec.type.common.EncryptionType;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * A factory class for producing random keys, suitable for use as session keys.  For a
 * list of desired cipher types, Kerberos random-to-key functions are used to derive
 * keys for DES-, DES3-, AES-, and RC4-based encryption types.
 */
public class RandomKeyFactory
{
    /** A map of default encryption types mapped to cipher names. */
    private static final Map<EncryptionType, String> DEFAULT_CIPHERS;

    static
    {
        Map<EncryptionType, String> map = new HashMap<EncryptionType, String>();

        map.put( EncryptionType.DES_CBC_MD5, "DES" );
        map.put( EncryptionType.DES3_CBC_SHA1_KD, "DESede" );
        map.put( EncryptionType.RC4_HMAC, "RC4" );
        map.put( EncryptionType.AES128_CTS_HMAC_SHA1_96, "AES" );
        map.put( EncryptionType.AES256_CTS_HMAC_SHA1_96, "AES" );

        DEFAULT_CIPHERS = Collections.unmodifiableMap( map );
    }


    /**
     * Get a map of random keys.  The default set of encryption types is used.
     * 
     * @return The map of random keys.
     * @throws org.apache.directory.shared.kerberos.exceptions.KerberosException
     */
    public static Map<EncryptionType, EncryptionKey> getRandomKeys() throws KerberosException, KrbException {
        return getRandomKeys( DEFAULT_CIPHERS.keySet() );
    }


    /**
     * Get a map of random keys for a list of cipher types to derive keys for.
     *
     * @param ciphers The list of ciphers to derive keys for.
     * @return The list of KerberosKey's.
     * @throws org.apache.directory.shared.kerberos.exceptions.KerberosException
     */
    public static Map<EncryptionType, EncryptionKey> getRandomKeys( Set<EncryptionType> ciphers )
            throws KerberosException, KrbException {
        Map<EncryptionType, EncryptionKey> map = new HashMap<EncryptionType, EncryptionKey>();

        for ( EncryptionType encryptionType : ciphers )
        {
            map.put( encryptionType, getRandomKey( encryptionType ) );
        }

        return map;
    }


    /**
     * Get a new random key for a given {@link org.apache.directory.shared.kerberos.codec.types.EncryptionType}.
     *
     * @param encryptionType
     *
     * @return The new random key.
     * @throws org.apache.directory.shared.kerberos.exceptions.KerberosException
     */
    public static EncryptionKey getRandomKey( EncryptionType encryptionType ) throws KerberosException, KrbException {
        String algorithm = DEFAULT_CIPHERS.get( encryptionType );

        if ( algorithm == null )
        {
            throw new KerberosException( ErrorType.KDC_ERR_ETYPE_NOSUPP, I18n.err( I18n.ERR_616,
                encryptionType.name() ) );
        }

        try
        {
            KeyGenerator keyGenerator = KeyGenerator.getInstance( algorithm );

            if ( encryptionType.equals( EncryptionType.AES128_CTS_HMAC_SHA1_96 ) )
            {
                keyGenerator.init( 128 );
            }

            if ( encryptionType.equals( EncryptionType.AES256_CTS_HMAC_SHA1_96 ) )
            {
                keyGenerator.init( 256 );
            }

            SecretKey key = keyGenerator.generateKey();

            byte[] keyBytes = key.getEncoded();

            return EncryptionUtil.createEncryptionKey(encryptionType, keyBytes);
        }
        catch ( NoSuchAlgorithmException nsae )
        {
            throw new KerberosException( ErrorType.KDC_ERR_ETYPE_NOSUPP, nsae );
        }
    }
}
