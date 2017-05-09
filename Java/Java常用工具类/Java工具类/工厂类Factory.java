package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 工厂类：
 * 	依据type(接口名称)返回一个符合该接口要求的对象
 * 可以将type与实现类的对应关系写在配置文件中,Factory读该配置文件,依据配置文件的内容来创建合适的对象
 * dao.properties
 * @author chenlanqing
 *
 */
public class Factory {
	private static Properties props = new Properties();
	
	static{
		/**
		 * 类名.class是找到个class对象
		 * classLoader:类加载器,JVM调用类加载器来完成类的加载:
		 * 	即类加载器要查找类的字节码文件,并且将字节码文件的内容读入到方法区,转换成一个class对象.
		 * 	查找类字节码文件,把字节码文件读到方法区,成为一个class对象
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
		//依据接口名找到类名
		String className = getValue(type);
		System.out.println(className);
		//依据类名,通过反射机制创建一个实例
		try {
			Class c = Class.forName(className);
			//通过class对象创建一个实例
			
			obj = c.newInstance();
			
		} catch (Exception e) {			
			e.printStackTrace();
			throw e;
		}
		return obj;
	}
}
