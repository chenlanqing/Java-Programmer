package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * �����ࣺ
 * 	����type(�ӿ�����)����һ�����ϸýӿ�Ҫ��Ķ���
 * ���Խ�type��ʵ����Ķ�Ӧ��ϵд�������ļ���,Factory���������ļ�,���������ļ����������������ʵĶ���
 * dao.properties
 * @author chenlanqing
 *
 */
public class Factory {
	private static Properties props = new Properties();
	
	static{
		/**
		 * ����.class���ҵ���class����
		 * classLoader:�������,JVM������������������ļ���:
		 * 	���������Ҫ��������ֽ����ļ�,���ҽ��ֽ����ļ������ݶ��뵽������,ת����һ��class����.
		 * 	�������ֽ����ļ�,���ֽ����ļ�����������,��Ϊһ��class����
		 * ClassLoader loader = Factory.class.getClassLoader();
		 */
		InputStream ips = 
			Factory.class.getClassLoader().getResourceAsStream("util/dao.properties");
		
		try {
			props.load(ips);			
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public static String getValue(String key){
		
		return props.getProperty(key);		
	}
	public static Object getInstance(String type) throws Exception{
		Object obj = null;
		//���ݽӿ����ҵ�����
		String className = getValue(type);
		System.out.println(className);
		//��������,ͨ��������ƴ���һ��ʵ��
		try {
			Class c = Class.forName(className);
			//ͨ��class���󴴽�һ��ʵ��
			
			obj = c.newInstance();
			
		} catch (Exception e) {			
			e.printStackTrace();
			throw e;
		}
		return obj;
	}
}
