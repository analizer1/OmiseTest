package net.analizer.domainlayer.utils;

import java.math.BigInteger;

/**
 * Provide a simple cryptography functionality.
 */
public abstract class CryptoUtils {

    /**
     * A simple decryption.
     *
     * @param cipher a cipher text
     * @return a plain text
     */
    public static String decode(BigInteger cipher) {
        return new String(cipher.toByteArray());
    }
}
