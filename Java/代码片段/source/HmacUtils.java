import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * <a href='https://en.wikipedia.org/wiki/HMAC'>基于单向散列函数的消息验证码（Hash-based Message Authentication Code，HMAC）</a>工具
 */
public class HmacUtils {

    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String HMAC_SHA384 = "HmacSHA384";

    public static String hmacSHA256(String data, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        return hmac(data, secretKey, HMAC_SHA256);
    }

    public static String hmacSHA384(String data, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        return hmac(data, secretKey, HMAC_SHA384);
    }

    /**
     * hmac计算
     *
     * @param data      待加密数据
     * @param secretKey 密钥
     * @param algorithm hmac算法
     */
    public static String hmac(String data, String secretKey, String algorithm) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac = Mac.getInstance(algorithm);
        hmac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), algorithm));
        byte[] array = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return byteArrayToHexString(array);
    }

    /**
     * 将字节数组转为16进制字符串
     *
     * @param bytes 要转换的字节数组
     */
    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            //java.lang.Integer.toHexString() 方法的参数是int(32位)类型，
            //如果输入一个byte(8位)类型的数字，这个方法会把这个数字的高24为也看作有效位，就会出现错误
            //如果使用& 0XFF操作，可以把高24位置0以避免这样错误
            String temp = Integer.toHexString(b & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                builder.append("0");
            }
            builder.append(temp);
        }
        return builder.toString();
    }
}
