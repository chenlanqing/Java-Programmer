package day08;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * ��д�ļ�
 * @author tarena
 * 
 */
public class Java03_RandomAccessFile {
	public static void main(String[] args) {
		RandomAccessFile raf = null;
		try {
			/**
			 * ���ļ�raf.dat��д������
			 */
			File file = new File("raf.dat");
			if(!file.exists()){				
					file.createNewFile();
			} 	
			/**
			 * �������ڶ�д�ļ���RandomAccessFile
			 */
			raf = new RandomAccessFile(file,"rw");
			/**
			 * write(int data)ע��:
			 *  �÷���д��1���ֽ�,д����data�ĵ�8λ�����Ƶ�����
			 */
			//д���ַ�'A'
			raf.write('A');
			raf.write('B');	
			
			//дһ��int���ֵ
			int max = Integer.MAX_VALUE;
			/**                                      ^^^^^^^^
			 * max        01111111 11111111 11111111 11111111
			 * max >>> 24 00000000 00000000 00000000 01111111
			 * max >>> 16 00000000 00000000 01111111 11111111
			 * max >>> 8  00000000 01111111 11111111 11111111
			 * 
			 */
			//д��int�����8λ
			raf.write(max>>>24);
			raf.write(max>>>16);
			raf.write(max>>>8);
			raf.write(max);
			/**
			 * ����д4���ֽ�,��intֵд���ļ�
			 */
			raf.writeInt(Integer.MIN_VALUE);
			/**
			 * дһ���ַ���,����GBK����
			 */
			String str = "�Ұ������찲��!";
			byte[] data = str.getBytes("GBK");
			/**
			 * write(byte[] data):
			 * һ���Խ�һ���ֽ������е�����ȫ��д��
			 * 
			 * ͨ����д�ַ�����ʱ��,����ֱ�Ӱ��ַ���ת�����ֽ���д��ȥ,������дһ������,�����������ʾ���潫Ҫд��
			 * �ַ��������ֽ���,�������ڶ�ȡ;
			 * �����ڶ�ȡ��ʱ���޷�ȷ��Ҫ��ȡ���ٸ��ֽڲ��������Ķ�ȡ�����ַ���
			 * 
			 */
			raf.writeInt(data.length);
			raf.write(data);
			/**
			 * write(byte[] data,int start,int len)
			 * �Ӹ������ֽ������startλ��д,����дlen���ֽ�
			 * start+len���ܳ������鳤��
			 */
			raf.write(data,0,8);
			
		}catch (IOException e) {				
			e.printStackTrace();
		}finally{
			/**
			 * ʹ�����ùر�
			 */
			try {
				if(raf != null){
					raf.close();
				}
			} catch (IOException e) {				
				
			} 
		}
	}
}