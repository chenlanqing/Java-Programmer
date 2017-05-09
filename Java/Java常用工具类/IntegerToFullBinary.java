public class IntegerToFullBinary{
	/**
	 * 将一个int整数转换为完整二进制表示
	 * 如 2=> 00000000 00000000 00000000 00000010
	 * 
	 * @param  num [description]
	 * @return     [description]
	 */
	public static String toFullBinaryString(int num) {
        char[] chs = new char[Integer.SIZE];
        for (int i = 0; i < Integer.SIZE; i++) {
            chs[Integer.SIZE - 1 - i] = (char) (((num >> i) & 1) + '0');
        }
        return new String(chs);
    }
}