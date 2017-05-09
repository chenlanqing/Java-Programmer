package mysqpring;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.dom4j.*;
//import org.dom4j.Document;
//import org.dom4j.Element;
//import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

/**
 * 自定义Spring容器，理解bean的实例化和ID注入
 * @author chenlanqing
 *
 */
public class MyClassPathXmlApllicationContext {
	private List<BeanDefinition> definitions = new ArrayList<BeanDefinition>();
	private Map<String,Object> sigletons = new HashMap<String,Object>();
	public MyClassPathXmlApllicationContext(String filename) {
		this.readXml(filename);
		this.instanceBeans();
		this.injectObject();
	}
	/**
	 * 为bean对象的属性注入值
	 */
	private void injectObject() {
		for(BeanDefinition definition : definitions){
			Object bean = sigletons.get(definition.getId());
			if(bean != null){
				try{
					//返回一个JavaBean 的属性描述数组
					PropertyDescriptor[] ps = 
							Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
					for(PropertyDefinition propertyDefinition : definition.getProperty()){
						for(PropertyDescriptor properdesc : ps){
							if(propertyDefinition.getName().equals(properdesc.getName())){
								//获取属性的setter 方法
								Method setter = properdesc.getWriteMethod();
								if(setter != null){
									Object value = null;
									//判断是依赖对象还是基本类型数据
									if(propertyDefinition.getRef()!=null && !"".equals(propertyDefinition.getRef().trim())){
										value = sigletons.get(propertyDefinition.getRef());										
									}else{
										//使用beanutil包的convert方法将字符串转换为对应类型的值
										value = ConvertUtils.convert(propertyDefinition.getValue(), properdesc.getPropertyType());
									}
									//为了防止获取的setter方法未private的，设置下访问权限；
									setter.setAccessible(true);
									setter.invoke(bean, value);//把引用对象注入到属性；
								}
								break;
							}
						}
					}
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 实例化bean
	 */
	private void instanceBeans() {
		for(BeanDefinition definition : definitions){
			try {
				if(definition.getClassName() != null && !"".equals(definition.getClassName().trim())){
					sigletons.put(definition.getId(), Class.forName(definition.getClassName()).newInstance());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 读取配置文件xml
	 * @param filename
	 */
	private void readXml(String filename) {
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try{
			URL xmlpath = this.getClass().getClassLoader().getResource(filename);
			document = saxReader.read(xmlpath);
			Map<String,String> nsMap = new HashMap<String, String>();
			//加入命名空间
			nsMap.put("ns","http://www.springframework.org/schema/beans");
			//创建beans/bean 的查询路径
			XPath xsub = document.createXPath("//ns:beans/ns:bean");
			//设置命名空间
			xsub.setNamespaceURIs(nsMap);
			//获得文档下的所有bean节点
			List<Element> beans = xsub.selectNodes(document);
			for(Element element : beans){
				String id = element.attributeValue("id");
				String clazz = element.attributeValue("class");
				BeanDefinition beanDefine = new BeanDefinition(id, clazz);
				//创建property的查询路径
				XPath propertysub = document.createXPath("ns:property");
				//设置命名空间
				propertysub.setNamespaceURIs(nsMap);
				//获得文档下的所有property节点
				List<Element> propertys = propertysub.selectNodes(document);
				for(Element property : propertys){
					String name = property.attributeValue("name");
					String ref = property.attributeValue("ref");
					String value = property.attributeValue("value");
					PropertyDefinition propertyDefinition = new PropertyDefinition(name, ref,value);
					beanDefine.getProperty().add(propertyDefinition);
				}
				definitions.add(beanDefine);
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 获取bean对象
	 * @param beanName
	 * @return
	 */
	public Object getBean(String beanName){
		return this.sigletons.get(beanName);
	}
}


















