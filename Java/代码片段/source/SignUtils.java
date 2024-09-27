import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public final class SignUtils {
    private SignUtils() {
    }

    /**
     * HMACSHA256签名处理
     *
     * @param data      签名串
     * @param secretKey 与AppId对应的AppSecret
     * @return 根据签名串生成的签名
     */
    public static String signWithHMACSHA256(String data, String secretKey) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        sha256_HMAC.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] array = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
        // 将字节数组转换为十六进制字符串
        return bytesToHex(array);
        // return Hex.encodeHexString(array);
    }

    /**
     * 将字节数组转换为十六进制字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}