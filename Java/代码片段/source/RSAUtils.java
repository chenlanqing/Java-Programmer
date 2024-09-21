import javax.crypto.Cipher;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public final class RSAUtils {
    /**
     * 加密算法
     */
    private final static String ALGORITHM = "RSA";

    /**
     * 保存公钥和私钥，把公钥和私钥保存到根目录
     *
     * @param algorithm 算法
     * @param pubPath   公钥路径
     * @param priPath   私钥路径
     */
    private static void generateKeyToFile(String algorithm, String pubPath, String priPath) throws Exception {
        KeyPair keyPair = getKeyPair(algorithm);
        // 生成私钥
        PrivateKey privateKey = keyPair.getPrivate();
        // 生成公钥
        PublicKey publicKey = keyPair.getPublic();
        // 使用base64进行编码
        String privateEncodeString = base64Key(privateKey);
        String publicEncodeString = base64Key(publicKey);
        // 把公钥和私钥保存到根目录
        FileUtils.writeStringToFile(new File(pubPath), publicEncodeString, StandardCharsets.UTF_8);
        FileUtils.writeStringToFile(new File(priPath), privateEncodeString, StandardCharsets.UTF_8);
    }

    /**
     * 生成密钥对
     */
    public static KeyPair getRSAKeyPair() throws Exception {
        return getKeyPair(ALGORITHM);
    }

    /**
     * 生成密钥对
     */
    public static KeyPair getKeyPair(String algorithm) throws Exception {
        // 创建KeyPairGenerator对象，指定算法为RSA
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(algorithm);
        keyPairGen.initialize(2048); // 设置密钥长度
        // 生成密钥对
        return keyPairGen.generateKeyPair();
    }

    /**
     * 对公钥或者私钥进行Base64编码
     */
    public static String base64Key(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static String encrypt(String data, String publicKeyStr) throws Exception {
        // 解码Base64字符串
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyStr);
        // 使用X509EncodedKeySpec来定义公钥的编码
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        // 获取KeyFactory并生成公钥
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return encrypt(data, keyFactory.generatePublic(keySpec));
    }

    public static String encrypt(String data, PublicKey publicKey) throws Exception {
        // 创建Cipher对象，指定算法为RSA
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey); //
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes); // 将加密结果编
    }


    public static String decrypt(String data, String privateKeyStr) throws Exception {
        // 解码Base64字符串
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyStr);
        // 创建私钥key的规则
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        // 获取KeyFactory并生成公钥
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return decrypt(data, keyFactory.generatePrivate(keySpec));
    }

    public static String decrypt(String data, PrivateKey privateKey) throws Exception {
        // 创建Cipher对象，指定算法为RSA
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey); // 设置为解密模式并使用私钥
        // 解密数据
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(data));
        return new String(decryptedBytes);
    }

}
