<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [1、Spring-IOC容器中注册组件](#1spring-ioc%E5%AE%B9%E5%99%A8%E4%B8%AD%E6%B3%A8%E5%86%8C%E7%BB%84%E4%BB%B6)
  - [1.1、bean配置](#11bean%E9%85%8D%E7%BD%AE)
  - [1.2、包扫描配置](#12%E5%8C%85%E6%89%AB%E6%8F%8F%E9%85%8D%E7%BD%AE)
  - [1.3、Conditional注解](#13conditional%E6%B3%A8%E8%A7%A3)
  - [1.4、@Import-快速给容器中导入一个组件](#14import-%E5%BF%AB%E9%80%9F%E7%BB%99%E5%AE%B9%E5%99%A8%E4%B8%AD%E5%AF%BC%E5%85%A5%E4%B8%80%E4%B8%AA%E7%BB%84%E4%BB%B6)
- [2、Bean的生命周期](#2bean%E7%9A%84%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F)
  - [2.1、Bean的生命周期](#21bean%E7%9A%84%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F)
  - [2.2、初始化和销毁过程](#22%E5%88%9D%E5%A7%8B%E5%8C%96%E5%92%8C%E9%94%80%E6%AF%81%E8%BF%87%E7%A8%8B)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


# 1、Spring-IOC容器中注册组件

## 1.1、bean配置
- 定义一个Bean
    ```java
    public class Person {
        private String name;
        private Integer age;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public Integer getAge() {
            return age;
        }
        public void setAge(Integer age) {
            this.age = age;
        }
        public Person() {
        }
        public Person(String name, Integer age) {
            this.name = name;
            this.age = age;
        }
    }
    ```
- 配置文件配置：
    ```xml
    <bean id="person" class="com.blue.fish.annotation.bean.Person">
        <property name="name" value="Zhang San" />
        <property name="age" value="20" />
    </bean>
    ```
    获取方式
    ```java
    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
    Person bean = context.getBean(Person.class);
    ```
- 注解方法
    ```java
    // Spring知道一个配置类
    @Configuration
    public class MainConfig {
        @Scope
        @Lazy
        @Bean
        public Person person(){
            return new Person("Jayden", 30);
        }
    }
    ```
    - @Bean 注解对应的就是配置`<bean>`，方法返回值对应的是配置文件中的class，方法名称默认对应的是id，可以通过@Bean(name = "person")指定，默认是单实例的;
    - @Scope：调整作用域
        - prototype：ConfigurableBeanFactory#SCOPE_PROTOTYPE，多实例的：ioc容器启动并不会去调用方法创建对象放在容器中。每次获取的时候才会调用方法创建对象；
        - singleton：ConfigurableBeanFactory#SCOPE_SINGLETON，单实例的（默认值）：ioc容器启动会调用方法创建对象放到ioc容器中。以后每次获取就是直接从容器（map.get()）中拿
        - request：org.springframework.web.context.WebApplicationContext#SCOPE_REQUEST，同一次请求创建一个实例
        - sesssion：org.springframework.web.context.WebApplicationContext#SCOPE_SESSION，同一个session创建一个实例
    - @Lazy：懒加载，主要是单实例bean：默认在容器启动的时候创建对象；懒加载：容器启动不创建对象。第一次使用(获取)Bean创建对象，并初始化；

    使用方式
    ```java
    // MainConfig 即对应其配置类
    ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig.class);
    Person bean = (Person) context.getBean("person");
    String[] beanDefinitionNames = context.getBeanDefinitionNames();
    ```

## 1.2、包扫描配置
- 配置文件配置
    ```xml
    <context:component-scan base-package="com.blue.fish.annotation" use-default-filters="false">
        <!--<context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>-->
        <context:include-filter type="annotation" expression="org.springframework.stereotype.Service"/>
    </context:component-scan>
    ```
- 注解方式：`@ComponentScan`（JDK8后可以配置多个）或者 `@ComponentScans`，@ComponentScans其value是个`ComponentScan[] value();`
    ```java
    @Configuration
    @ComponentScan(value = "com.blue.fish.annotation",
            excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Controller.class})})
    // @ComponentScan  value:指定要扫描的包
    // excludeFilters = Filter[] ：指定扫描的时候按照什么规则排除那些组件
    // includeFilters = Filter[] ：指定扫描的时候只需要包含哪些组件
    // FilterType.ANNOTATION：按照注解
    // FilterType.ASSIGNABLE_TYPE：按照给定的类型；
    // FilterType.ASPECTJ：使用ASPECTJ表达式
    // FilterType.REGEX：使用正则指定
    // FilterType.CUSTOM：使用自定义规则
    public class MainConfig {
    }
    ```
    FilterType.CUSTOM，自定义规则，必须实现接口TypeFilter
    ```java
    public class MyTypeFilter implements TypeFilter {
        /**
        * metadataReader：读取到的当前正在扫描的类的信息
        * metadataReaderFactory:可以获取到其他任何类信息的
        */
        @Override
        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
            //获取当前类注解的信息
            AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
            //获取当前正在扫描的类的类信息
            ClassMetadata classMetadata = metadataReader.getClassMetadata();
            //获取当前类资源（类的路径）
            Resource resource = metadataReader.getResource();
            
            String className = classMetadata.getClassName();
            System.out.println("--->"+className);
            if(className.contains("er")){
                return true;
            }
            return false;
        }
    }
    ```

## 1.3、Conditional注解

```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Conditional {
	Class<? extends Condition>[] value();
}

```
@Conditional({Condition})：按照一定的条件进行判断，满足条件给容器中注册bean

类中组件统一设置。满足当前条件，这个类中配置的所有bean注册才能生效；如果配置在方法中，则表示该方法满足当前当前条件才能生效；

可以自定义Condition，自定义Condition必须实现接口`org.springframework.context.annotation.Condition`
```java
/**
* ConditionContext：判断条件能使用的上下文（环境）
* AnnotatedTypeMetadata：注释信息
*/
@Override
public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

    //1、能获取到ioc使用的beanfactory
    ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
    //2、获取类加载器
    ClassLoader classLoader = context.getClassLoader();
    //3、获取当前环境信息
    Environment environment = context.getEnvironment();
    //4、获取到bean定义的注册类
    BeanDefinitionRegistry registry = context.getRegistry();
    String property = environment.getProperty("os.name");
    //可以判断容器中的bean注册情况，也可以给容器中注册bean
    boolean definition = registry.containsBeanDefinition("person");
    if(property.contains("linux")){
        return true;
    }
    return false;
}
```

## 1.4、@Import-快速给容器中导入一个组件
- @Import(要导入到容器中的组件)；容器中就会自动注册这个组件，id默认是全类名
    ```java
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Import {
        // {@link Configuration}, {@link ImportSelector}, {@link ImportBeanDefinitionRegistrar} or regular component classes to import.
        Class<?>[] value();
    }
    ```
    ```java
    @Import({Color.class,Red.class,MyImportSelector.class,MyImportBeanDefinitionRegistrar.class})
    //@Import导入组件，id默认是组件的全类名
    public class MainConfig2 {
    }
    ```

- ImportSelector：返回需要导入的组件的全类名数组；
    ```java
    public interface ImportSelector {
        /**
        * Select and return the names of which class(es) should be imported based on
        * the {@link AnnotationMetadata} of the importing @{@link Configuration} class.
        */
        String[] selectImports(AnnotationMetadata importingClassMetadata);
    }

    ```
    ```java
    //自定义逻辑返回需要导入的组件，将其用在@Import注解上
    public class MyImportSelector implements ImportSelector {
        //返回值，就是到导入到容器中的组件全类名
        //AnnotationMetadata:当前标注@Import注解的类的所有注解信息
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            //importingClassMetadata
            //方法不要返回null值
            return new String[]{"com.blue.fish.bean.Blue","com.blue.fish.bean.Yellow"};
        }
    }
    ```

- ImportBeanDefinitionRegistrar：手动注册bean到容器中
    ```java
    public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
        /**
        * AnnotationMetadata：当前类的注解信息
        * BeanDefinitionRegistry:BeanDefinition注册类；把所有需要添加到容器中的bean；调用BeanDefinitionRegistry.registerBeanDefinition手工注册进来
        */
        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            boolean definition = registry.containsBeanDefinition("com.atguigu.bean.Red");
            boolean definition2 = registry.containsBeanDefinition("com.atguigu.bean.Blue");
            if(definition && definition2){
                //指定Bean定义信息；（Bean的类型，Bean。。。）
                RootBeanDefinition beanDefinition = new RootBeanDefinition(RainBow.class);
                //注册一个Bean，指定bean名
                registry.registerBeanDefinition("rainBow", beanDefinition);
            }
        }

    }
    ```
## 1.5、使用Spring提供的 FactoryBean（工厂Bean）
- 默认获取到的是工厂bean调用getObject创建的对象
- 要获取工厂Bean本身，我们需要给id前面加一个&，写法如下：`&colorFactoryBean`

    context.getBean("&colorFactoryBean);

```java
//创建一个Spring定义的FactoryBean
public class ColorFactoryBean implements FactoryBean<Color> {
	//返回一个Color对象，这个对象会添加到容器中
	@Override
	public Color getObject() throws Exception {
		System.out.println("ColorFactoryBean...getObject...");
		return new Color();
	}

	@Override
	public Class<?> getObjectType() {
		return Color.class;
	}

	//是单例？
	//true：这个bean是单实例，在容器中保存一份
	//false：多实例，每次获取都会创建一个新的bean；
	@Override
	public boolean isSingleton() {
		return false;
	}
}
```
将上述定义的FactoryBean加入到容器中
```java
@Bean
public ColorFactoryBean colorFactoryBean(){
    return new ColorFactoryBean();
}
```

# 2、Bean的生命周期

## 2.1、Bean的生命周期

bean创建---初始化----销毁的过程

容器管理bean的生命周期，容器在bean进行到当前生命周期的时候来调用自定义的初始化和销毁方法

- 创建对象
    - 单实例：在容器启动的时候创建对象
    - 多实例：在每次获取的时候创建对象
- 初始化：
    - BeanPostProcessor.postProcessBeforeInitialization
    - 对象创建完成，并赋值好，调用初始化方法。。。
    - BeanPostProcessor.postProcessAfterInitialization
- 销毁：
    - 单实例：容器关闭的时候
    - 多实例：容器不会管理这个bean；容器不会调用销毁方法；

## 2.2、初始化和销毁过程

- 指定初始化和销毁方法：通过@Bean指定initMethod和destroyMethod；
`
@Bean(initMethod="init",destroyMethod="detory")
`

- 通过让Bean实现`InitializingBean`（定义初始化逻辑）、`DisposableBean`（定义销毁逻辑）
    ```java
    @Component
    public class Cat implements InitializingBean,DisposableBean {
        public Cat(){
            System.out.println("cat constructor...");
        }
        @Override
        public void destroy() throws Exception {
            System.out.println("cat...destroy...");
        }
        @Override
        public void afterPropertiesSet() throws Exception {
            System.out.println("cat...afterPropertiesSet...");
        }
    }
    ```

- 使用JSR250：`@PostConstruct` 在bean创建完成并且属性赋值完成；来执行初始化方法；`@PreDestroy` 在容器销毁bean之前通知我们进行清理工作
    ```java
    @Component
    public class Dog {
        public Dog() {
            System.out.println("...dog... constructor");
        }
        @PostConstruct
        public void init(){
            System.out.println("...dog... init");
        }
        @PreDestroy
        public void destroy(){
            System.out.println("...dog... indestroyit");
        }
    }
    ```

- bean的后置处理器：`BeanPostProcessor`，在bean初始化前后进行一些处理工作
    - postProcessBeforeInitialization:在初始化之前工作
    - postProcessAfterInitialization:在初始化之后工作
    ```java
    @Component
    public class MyBeanPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            System.out.println("...postProcessBeforeInitialization..." + beanName + "---->" + bean);
            return bean;
        }
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            System.out.println("...postProcessAfterInitialization..." + beanName + "---->" + bean);
            return bean;
        }
    }
    ```

    Spring底层对 BeanPostProcessor 的使用：bean赋值，注入其他组件，@Autowired，生命周期注解功能，@Async,xxx BeanPostProcessor;
    ---
    ***BeanPostProcessor执行原理：***

    - `Object beanInstance = doCreateBean(beanName, mbdToUse, args);`
        ```java
        populateBean(beanName, mbd, instanceWrapper); // 给bean进行属性赋值
        if (exposedObject != null) {
            exposedObject = initializeBean(beanName, exposedObject, mbd);// 初始化bean
        }
        ```
    - `initializeBean(beanName, exposedObject, mbd);`
        ```java
        Object wrappedBean = bean;
		if (mbd == null || !mbd.isSynthetic()) {
            // 执行BeanPostProcessor.postProcessBeforeInitialization 方法
			wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
		}

		try {
			invokeInitMethods(beanName, wrappedBean, mbd);
		}
		catch (Throwable ex) {
			throw new BeanCreationException(
					(mbd != null ? mbd.getResourceDescription() : null),
					beanName, "Invocation of init method failed", ex);
		}

		if (mbd == null || !mbd.isSynthetic()) {
            // 执行BeanPostProcessor.postProcessAfterInitialization方法
			wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
		}
        ```
    - 遍历得到容器中所有的BeanPostProcessor；顺序执行beforeInitialization，一但返回null，跳出for循环，不会执行后面的BeanPostProcessor.postProcessorsBeforeInitialization；
        同样初始化后也会顺序执行postProcessAfterInitialization，一但返回null，跳出for循环，不会执行后面的BeanPostProcessor.postProcessAfterInitialization
        ```java
        public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
			throws BeansException {
            Object result = existingBean;
            for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
                result = beanProcessor.postProcessBeforeInitialization(result, beanName);
                if (result == null) {
                    return result;
                }
            }
            return result;
        }

        @Override
        public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
                throws BeansException {

            Object result = existingBean;
            for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
                result = beanProcessor.postProcessAfterInitialization(result, beanName);
                if (result == null) {
                    return result;
                }
            }
            return result;
        }
        ```
    ---



