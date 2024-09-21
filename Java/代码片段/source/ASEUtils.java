import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {

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
