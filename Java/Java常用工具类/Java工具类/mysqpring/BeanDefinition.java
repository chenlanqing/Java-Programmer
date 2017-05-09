package mysqpring;

import java.util.List;

/**
 * 定义bean元素的两个属性：id和class,property
 * @author chenlanqing
 *
 */
public class BeanDefinition {
	private String id;
	private String className;
	private List<PropertyDefinition> property;
	public BeanDefinition(String id, String className) {
		this.id = id;
		this.className = className;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<PropertyDefinition> getProperty() {
		return property;
	}
	public void setProperty(List<PropertyDefinition> property) {
		this.property = property;
	}
	
}
