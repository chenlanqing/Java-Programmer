package day08;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 读写文件
 * @author tarena
 * 
 */
public class Java03_RandomAccessFile {
	public static void main(String[] args) {
		RandomAccessFile raf = null;
		try {
			/**
			 * 向文件raf.dat中写入数据
			 */
			File file = new File("raf.dat");
			if(!file.exists()){				
					file.createNewFile();
			} 	
			/**
			 * 创建用于读写文件的RandomAccessFile
			 */
			raf = new RandomAccessFile(file,"rw");
			/**
			 * write(int data)注意:
			 *  该方法写出1个字节,写的是data的低8位二进制的数据
			 */
			//写个字符'A'
			raf.write('A');
			raf.write('B');	
			
			//写一个int最大值
			int max = Integer.MAX_VALUE;
			/**                                      ^^^^^^^^
			 * max        01111111 11111111 11111111 11111111
			 * max >>> 24 00000000 00000000 00000000 01111111
			 * max >>> 16 00000000 00000000 01111111 11111111
			 * max >>> 8  00000000 01111111 11111111 11111111
			 * 
			 */
			//写入int的最高8位
			raf.write(max>>>24);
			raf.write(max>>>16);
			raf.write(max>>>8);
			raf.write(max);
			/**
			 * 连续写4个字节,将int值写入文件
			 */
			raf.writeInt(Integer.MIN_VALUE);
			/**
			 * 写一个字符串,按照GBK编码
			 */
			String str = "我爱北京天安门!";
			byte[] data = str.getBytes("GBK");
			/**
			 * write(byte[] data):
			 * 一次性将一个字节数组中的数据全部写出
			 * 
			 * 通常在写字符串的时候,不是直接把字符串转换的字节先写出去,而是先写一个整数,把这个整数表示后面将要写的
			 * 字符串的总字节量,这样便于读取;
			 * 否则在读取的时候无法确定要读取多少个字节才能完整的读取所有字符串
			 * 
			 */
			raf.writeInt(data.length);
			raf.write(data);
			/**
			 * write(byte[] data,int start,int len)
			 * 从给定的字节数组的start位置写,连续写len个字节
			 * start+len不能超过数组长度
			 */
			raf.write(data,0,8);
			
		}catch (IOException e) {				
			e.printStackTrace();
		}finally{
			/**
			 * 使用完后得关闭
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