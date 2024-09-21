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
 * code from here: https://github.com/rosmahajan/java-encryption-decryption
 */
public class EnDecryptSaltUtils {

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

    /**
     * 使用DES加密数据
     *
     * @param input          : 原文
     * @param key            : 密钥(DES,密钥的长度必须是8个字节,如果使用的是AES加密，那么密钥必须是16个字节)
     * @param transformation : 获取Cipher对象的算法
     * @param algorithm      : 获取密钥的算法
     * @return : 密文
     */
    private static String encrypt(String input, String key, String transformation, String algorithm) throws Exception {
        // 获取加密对象
        Cipher cipher = Cipher.getInstance(transformation);
        // 创建加密规则
        // 第一个参数key的字节
        // 第二个参数表示加密算法
        SecretKeySpec sks = new SecretKeySpec(key.getBytes(), algorithm);
        // ENCRYPT_MODE：加密模式
        // DECRYPT_MODE: 解密模式
        // 初始化加密模式和算法
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        // 加密
        byte[] bytes = cipher.doFinal(input.getBytes());
        // 输出加密后的数据
        return Base64.encode(bytes);
    }

    /**
     * 解密
     *
     * @param encryptDES     密文
     * @param key            密钥
     * @param transformation 加密算法
     * @param algorithm      加密类型
     */
    private static String decrypt(String encryptDES, String key, String transformation, String algorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(transformation);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), algorithm);
        //Cipher.DECRYPT_MODE:表示解密
        // 解密规则
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        // 解密，传入密文
        byte[] bytes = cipher.doFinal(Base64.decode(encryptDES));
        return new String(bytes);
    }
}
