package day08;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 读取文件内容
 * @author tarena
 *
 */
public class Java04_ReadFile {
	public static void main(String[] args) throws IOException {
		File file = new File("raf.dat");
		RandomAccessFile raf = new RandomAccessFile(file, "r");
			/**
			 * int read():读取一个字节,以int形式返回
			 * 当返回值为-1时,就是读取到了文件末尾;
			 * EOF:end of file
			 * 注意:该int值只有低8位有效
			 */
			//读取一个字节,之前写入的'A'
		char a = (char)raf.read();
		char b = (char)raf.read();
		System.out.println(a + "," + b);
		
		/**
		 * 连续读取4个字节,读int值
		 * raf.dat  	01111111 11111111 11111111 11111111
		 *  num      	00000000 00000000 00000000 00000000
		 *   i			00000000 00000000 00000000 01111111
		 * i<<24		01111111 00000000 00000000 00000000
		 * (i<<24)|num	01111111 00000000 00000000 00000000
		 */
		int num = 0;
		int i = raf.read();
		num = (i<<24)|num;
		
		i = raf.read();			//00000000 00000000 00000000 11111111
		num = (i << 16)|num;	//01111111 11111111 00000000 00000000
		
		i = raf.read();			//00000000 00000000 00000000 11111111
		num = (i << 8)|num;		//01111111 11111111 11111111 00000000
		
		i = raf.read();			//00000000 00000000 00000000 11111111
		num = i|num;			//01111111 11111111 11111111 11111111
		
		System.out.println("int最大值" + num + ";" + (num == Integer.MAX_VALUE));
		/**
		 * 连续读取4个字节,转换为int值返回
		 */
		int min = raf.readInt();
		System.out.println(min);
		/**
		 * 读取字符串
		 */
		int len = raf.readInt();//读取字符串的总字节量
		/**
		 * int read(byte[] data)
		 * 一次性尝试读取data数组长度的字节里,并存入该数组,返回值为实际读取到的字节量
		 * 若返回值为-1,读取到了文件末尾;
		 */
		byte[] data = new byte[len];
		raf.read(data);
		
		String str = new String(data,"GBK");
		String str1 = new String(data,0,len,"utf-8");
		System.out.println(str);
		
		raf.close();	
	}
} 