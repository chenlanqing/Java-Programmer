package com.qing.fan.utils;

import org.apache.commons.lang3.tuple.Pair;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

/**
 * 依赖：commons-lang3
 */
public class EncryptDecryptUtils {

    private final static Random RANDOM = new SecureRandom();

    public static Pair<String, String> encryptData(String key, String dataForEncryption) throws Exception {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        byte[] originalData = dataForEncryption.getBytes();

        /* Encryption process started */
        Cipher encCipher = initialize(key, salt, true);
        byte[] encryptedData = encrypt(encCipher, originalData);
        /* Encryption process completed */

        /* Encode encrypted values */
        String finalEncryptedValue = new String(Base64.getEncoder().encode(encryptedData));
        String finalSaltValue = new String(Base64.getEncoder().encode(salt));

        return Pair.of(finalEncryptedValue, finalSaltValue);
    }

    public static String decryptData(String key, String encryptedDataToDecrypt, String encodedSalt) throws Exception {
        /* Decode encrypted values */
        byte[] finalDecodedValue = Base64.getDecoder().decode(encryptedDataToDecrypt);
        byte[] finalDecodedSaltValue = Base64.getDecoder().decode(encodedSalt);

        /* Decryption process started */
        Cipher decCipher = initialize(key, finalDecodedSaltValue, false);
        byte[] decryptedData = decrypt(decCipher, finalDecodedValue);
        /* Decryption process completed */

        return new String(decryptedData);
    }

    public static Cipher initialize(String key, byte[] salt, boolean encryptFlag) throws Exception {
        // Derive the secret key specification with given key
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        // Derive the Iv parameter specification with given salt
        IvParameterSpec ivSpec = new IvParameterSpec(salt);
        if (encryptFlag) {
            Cipher encCipher = Cipher.getInstance("AES/CFB8/NoPadding");
            encCipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            return encCipher;
        } else {
            Cipher decCipher = Cipher.getInstance("AES/CFB8/NoPadding");
            decCipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            return decCipher;
        }
    }

    public static byte[] encrypt(Cipher encCipher, byte[] data) throws Exception {
        return encCipher.doFinal(data);
    }

    public static byte[] decrypt(Cipher decCipher, byte[] data) throws Exception {
        return decCipher.doFinal(data);
    }
}
