import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

public class MD5Util {

	public static String encrypt(String str) throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("md5");
		byte[] buf = md.digest(str.getBytes());
		//将字节数组转换成一个字符串
		//BASE64Encoder可以将任意字节数组转换成字符串
		BASE64Encoder encoder = new BASE64Encoder();
		String str2 = encoder.encode(buf);
		return str2;
	}
	
	/**
     * 将一个base64编码后的字符串解码
     */
    public static String base64Decode(String encode) {
        return new String(Base64.getDecoder().decode(encode), StandardCharsets.UTF_8);
    }

    /**
     * 对字符串进行Base64编码
     */
    public static String base64Encode(String code) {
        return new String(Base64.getEncoder().encode(code.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }
}
