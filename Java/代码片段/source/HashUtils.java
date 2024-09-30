import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * hash散列算法
 */
public class HashUtils {

    private static final String MD5 = "MD5";
    private static final String SHA_1 = "SHA-1";
    private static final String SHA_224 = "SHA-224";
    private static final String SHA_256 = "SHA-256";
    private static final String SHA_384 = "SHA-384";
    private static final String SHA_512 = "SHA-512";

    public static String getMD5(String painText, boolean uppercase) {
        return getHash(painText, MD5, uppercase);
    }

    public static String getSHA1(String painText, boolean uppercase) {
        return getHash(painText, SHA_1, uppercase);
    }

    public static String getSHA224(String painText, boolean uppercase) {
        return getHash(painText, SHA_224, uppercase);
    }

    public static String getSHA256(String painText, boolean uppercase) {
        return getHash(painText, SHA_256, uppercase);
    }

    public static String getSHA384(String painText, boolean uppercase) {
        return getHash(painText, SHA_384, uppercase);
    }

    public static String getSHA512(String painText, boolean uppercase) {
        return getHash(painText, SHA_512, uppercase);
    }

    public static String getHash(String plainText, String algorithm, boolean uppercase) {
        // 输入的字符串转换成字节数组
        byte[] bytes = plainText.getBytes(StandardCharsets.UTF_8);
        MessageDigest messageDigest;
        try {
            //获得SHA转换器
            messageDigest = MessageDigest.getInstance(algorithm);
            //bytes是输入字符串转换得到的字节数组
            messageDigest.update(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Exception with " + algorithm + " algorithm", e);
        }
        // 转换并返回结果，也是字节数组，包含16个元素
        byte[] digest = messageDigest.digest();
        // 字符数组转换成字符串返回
        String result = byteArrayToHexString(digest);
        //转换大写
        return uppercase ? result.toUpperCase() : result;
    }

    /**
     * 将字节数组转为16进制字符串
     *
     * @param bytes 要转换的字节数组
     */
    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            // java.lang.Integer.toHexString() 方法的参数是int(32位)类型，
            // 如果输入一个byte(8位)类型的数字，这个方法会把这个数字的高24为也看作有效位，就会出现错误
            // 如果使用& 0XFF操作，可以把高24位置0以避免这样错误
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