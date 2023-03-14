import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 加载网络class的ClassLoader
 */
public class NetworkClassLoader extends ClassLoader {
	
	private String rootUrl;

	public NetworkClassLoader(String rootUrl) {
		this.rootUrl = rootUrl;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		Class<?> clazz = null;//this.findLoadedClass(name); // 父类已加载	
		//if (clazz == null) {	//检查该类是否已被加载过
			byte[] classData = getClassData(name);	//根据类的二进制名称,获得该class文件的字节码数组
			if (classData == null) {
				throw new ClassNotFoundException();
			}
			clazz = defineClass(name, classData, 0, classData.length);	//将class的字节码数组转换成Class类的实例
		//} 
		return clazz;
	}

	private byte[] getClassData(String name) {
		InputStream is = null;
		try {
			String path = classNameToPath(name);
			URL url = new URL(path);
			byte[] buff = new byte[1024*4];
			int len = -1;
			is = url.openStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while((len = is.read(buff)) != -1) {
				baos.write(buff,0,len);
			}
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
			   try {
			      is.close();
			   } catch(IOException e) {
			      e.printStackTrace();
			   }
			}
		}
		return null;
	}

	private String classNameToPath(String name) {
		return rootUrl + "/" + name.replace(".", "/") + ".class";
	}

}