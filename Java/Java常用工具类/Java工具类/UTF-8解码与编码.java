package bin;

import java.io.UnsupportedEncodingException;

public class UTF8 {
	public static void main(String[] args) throws UnsupportedEncodingException {
		byte[] buf = utf8('��');
		System.out.println(
				Integer.toHexString(buf[0] & 0xff));
		System.out.println(
				Integer.toHexString(buf[1] & 0xff));
		System.out.println(
				Integer.toHexString(buf[2] & 0xff));
		String s = new String(buf, "utf-8");
		System.out.println(s);//"��"
		buf = utf8('��');
		System.out.println(toChar(buf)); 
	}
	//��һ�������ַ�����Ϊutf-8����
	public static byte[] utf8(char c){
		int b1 = 0xe0;
		int b2 = 0x80;
		int b3 = 0x80;
		b1 = b1 | (c >>> 12);
		b2 = b2 | ((c >>> 6) & 0x3f);
		b3 = b3 | (c & 0x3f);
		return new byte[]{(byte)b1, (byte)b2, (byte)b3};
	}
	/** ��һ�������ֵ�utf8����ת��Ϊ�ַ����� */
	public static char toChar(byte[] utf8){
		int b1 = utf8[0];
		int b2 = utf8[1];
		int b3 = utf8[2];
		int c = ((b1&0xf)<<12)+((b2&0x3f)<<6)+(b3&0x3f);
		return (char)c;
	}
}
