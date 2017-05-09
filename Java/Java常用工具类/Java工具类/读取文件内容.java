package day08;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * ��ȡ�ļ�����
 * @author tarena
 *
 */
public class Java04_ReadFile {
	public static void main(String[] args) throws IOException {
		File file = new File("raf.dat");
		RandomAccessFile raf = new RandomAccessFile(file, "r");
			/**
			 * int read():��ȡһ���ֽ�,��int��ʽ����
			 * ������ֵΪ-1ʱ,���Ƕ�ȡ�����ļ�ĩβ;
			 * EOF:end of file
			 * ע��:��intֵֻ�е�8λ��Ч
			 */
			//��ȡһ���ֽ�,֮ǰд���'A'
		char a = (char)raf.read();
		char b = (char)raf.read();
		System.out.println(a + "," + b);
		
		/**
		 * ������ȡ4���ֽ�,��intֵ
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
		
		System.out.println("int���ֵ" + num + ";" + (num == Integer.MAX_VALUE));
		/**
		 * ������ȡ4���ֽ�,ת��Ϊintֵ����
		 */
		int min = raf.readInt();
		System.out.println(min);
		/**
		 * ��ȡ�ַ���
		 */
		int len = raf.readInt();//��ȡ�ַ��������ֽ���
		/**
		 * int read(byte[] data)
		 * һ���Գ��Զ�ȡdata���鳤�ȵ��ֽ���,�����������,����ֵΪʵ�ʶ�ȡ�����ֽ���
		 * ������ֵΪ-1,��ȡ�����ļ�ĩβ;
		 */
		byte[] data = new byte[len];
		raf.read(data);
		
		String str = new String(data,"GBK");
		String str1 = new String(data,0,len,"utf-8");
		System.out.println(str);
		
		raf.close();	
	}
} 