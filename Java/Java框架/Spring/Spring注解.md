<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [1、Spring-IOC容器中注册组件](#1spring-ioc%E5%AE%B9%E5%99%A8%E4%B8%AD%E6%B3%A8%E5%86%8C%E7%BB%84%E4%BB%B6)
  - [1.1、bean配置](#11bean%E9%85%8D%E7%BD%AE)
  - [1.2、包扫描配置](#12%E5%8C%85%E6%89%AB%E6%8F%8F%E9%85%8D%E7%BD%AE)
  - [1.3、Conditional注解](#13conditional%E6%B3%A8%E8%A7%A3)
  - [1.4、@Import-快速给容器中导入一个组件](#14import-%E5%BF%AB%E9%80%9F%E7%BB%99%E5%AE%B9%E5%99%A8%E4%B8%AD%E5%AF%BC%E5%85%A5%E4%B8%80%E4%B8%AA%E7%BB%84%E4%BB%B6)
  - [1.6、属性赋值](#16%E5%B1%9E%E6%80%A7%E8%B5%8B%E5%80%BC)
- [2、Bean的生命周期](#2bean%E7%9A%84%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F)
  - [2.1、Bean的生命周期](#21bean%E7%9A%84%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F)
  - [2.2、初始化和销毁过程](#22%E5%88%9D%E5%A7%8B%E5%8C%96%E5%92%8C%E9%94%80%E6%AF%81%E8%BF%87%E7%A8%8B)
- [3、自动装配](#3%E8%87%AA%E5%8A%A8%E8%A3%85%E9%85%8D)
  - [3.1、@Autowired：自动注入](#31autowired%E8%87%AA%E5%8A%A8%E6%B3%A8%E5%85%A5)
  - [3.2、`@Resource`和`@Inject`](#32resource%E5%92%8Cinject)
  - [3.3、方法与构造器自动装配](#33%E6%96%B9%E6%B3%95%E4%B8%8E%E6%9E%84%E9%80%A0%E5%99%A8%E8%87%AA%E5%8A%A8%E8%A3%85%E9%85%8D)
  - [3.4、自定义组件注入](#34%E8%87%AA%E5%AE%9A%E4%B9%89%E7%BB%84%E4%BB%B6%E6%B3%A8%E5%85%A5)
  - [3.5、Profile](#35profile)
- [4、AOP](#4aop)
  - [4.1、AOP基于注解写法](#41aop%E5%9F%BA%E4%BA%8E%E6%B3%A8%E8%A7%A3%E5%86%99%E6%B3%95)
  - [4.2、AOP原理](#42aop%E5%8E%9F%E7%90%86)
  - [4.4、总结](#44%E6%80%BB%E7%BB%93)

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

## 1.6、属性赋值
**1、使用@Value赋值**

- 基本数值：`@Value("张三")`

- 可以写Spring EL表达式； #{}，比如：`@Value("#{20-2}")`

- 可以写${}；取出配置文件【properties】中的值（在运行环境变量里面的值）：`@Value("${person.nickName}")`

**2、@PrpopertySource加载外部配置文件**

- 之前导入配置文件`<context:property-placeholder location="classpath:person.properties"/>`
- `@PropertySource(value={"classpath:/person.properties"})`使用`@PropertySource`读取外部配置文件中的k/v保存到运行的环境变量中;加载完外部的配置文件以后使用${}取出配置文件的值

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

# 3、自动装配

Spring利用依赖注入（DI），完成对IOC容器中中各个组件的依赖关系赋值

AutowiredAnnotationBeanPostProcessor：解析完成自动装配功能

## 3.1、@Autowired：自动注入
以下面代码为例：
```java
BookService{
    @Autowired
    BookDao  bookDao;
}
```
- 默认优先按照类型去容器中找对应的组件：`applicationContext.getBean(BookDao.class);`找到就赋值；

- 如果找到多个相同类型的组件，再将属性的名称作为组件的id去容器中查找：`applicationContext.getBean("bookDao");`；
    ```java
    @Component
    public class BookDao{

    }

    @ComponentScan("")
    @Configuration
    public class Config{
        @Bean(name="bookDao2")
        public BookDao bookDao(){

        }
    }
    // 按照上述注入的话，会直接使用@Component上，而不是configuration配置的bean
    ```


- `@Qualifier("bookDao")`：使用@Qualifier指定需要装配的组件的id，而不是使用属性名；

- 自动装配默认一定要将属性赋值好，没有就会报错，可以使用`@Autowired(required=false)`；

- `@Primary`：让Spring进行自动装配的时候，默认使用首选的bean；也就是在注入的bean上加上注解，Spring进行装配的时候就自动装配`@Primary`修饰的Bean；当然也可以继续使用`@Qualifier`指定需要装配的bean的名字；


## 3.2、`@Resource`和`@Inject`

Spring还支持使用`@Resource(JSR250)`和`@Inject(JSR330)`【java规范的注解】

- `@Resource`：可以和`@Autowired`一样实现自动装配功能；默认是按照组件名称进行装配的；没有能支持`@Primary`功能，也没有支持@Autowired（reqiured=false）;

- `@Inject`：需要导入javax.inject的包，和Autowired的功能一样。没有required=false的功能；要使用该注解需要导入依赖：
    ```xml
    <dependency>
        <groupId>javax.inject</groupId>
        <artifactId>javax.inject</artifactId>
        <version>1</version>
    </dependency>
    ```

*`@Autowired`：Spring定义的； `@Resource、@Inject`都是java规范*

## 3.3、方法与构造器自动装配
@Autowired：构造器，参数，方法，属性；都是从容器中获取参数组件的值
```java
@Component
public class Boss {
	private Car car;
	//构造器要用的组件，都是从容器中获取
    // @Autowired 
	public Boss(@Autowired Car car){
		this.car = car;
		System.out.println("Boss...有参构造器");
	}
	public Car getCar() {
		return car;
	}
	
	//标注在方法，Spring容器创建当前对象，就会调用方法，完成赋值；
	//方法使用的参数，自定义类型的值从ioc容器中获取
    @Autowired 
	public void setCar(Car car) {
		this.car = car;
	}
	@Override
	public String toString() {
		return "Boss [car=" + car + "]";
	}
}
```

- [标注在方法位置]：@Bean+方法参数；参数从容器中获取；默认不写`@Autowired`效果是一样的；都能自动装配；
    ```java
	// @Bean标注的方法创建对象的时候，方法参数的值从容器中获取
	@Bean
	public Color color(Car car){
		Color color = new Color();
		color.setCar(car);
		return color;
	}
    ```

- [标在构造器上]：如果组件只有一个有参构造器，这个有参构造器的`@Autowired`可以省略，参数位置的组件还是可以自动从容器中获取；

- 放在参数位置
    ```java
    public Boss(@Autowired Car car){
		this.car = car;
		System.out.println("Boss...有参构造器");
	}
    ```

## 3.4、自定义组件注入
定义组件想要使用Spring容器底层的一些组件（ApplicationContext，BeanFactory，xxx），自定义组件实现xxxAware；在创建对象的时候，会调用接口规定的方法注入相关组件；

`Aware`把Spring底层一些组件注入到自定义的Bean中；

ApplicationContextAware --> ApplicationContextAwareProcessor；

## 3.5、Profile

Spring提供一种可以根据当前环境，动态的激活和切换一系列组件的功能

`@Profile`：指定组件在哪个环境的情况下才能被注册到容器中，不指定，任何环境下都能注册这个组件；

- 加了环境标识的bean，只有这个环境被激活的时候才能注册到容器中。默认是default环境；
- 写在配置类上，只有是指定的环境的时候，整个配置类里面的所有配置才能开始生效；
- 没有标注环境标识的bean在，任何环境下都是加载的；

**激活profile**
- 使用命令行动态参数: 在虚拟机参数位置加载 -Dspring.profiles.active=test

- 代码的方式激活某种环境：
    ```java
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    //1、创建一个applicationContext
    //2、设置需要激活的环境
    applicationContext.getEnvironment().setActiveProfiles("dev");
    //3、注册主配置类
    applicationContext.register(MainConfigOfProfile.class);
    //4、启动刷新容器
    applicationContext.refresh();
    ```
    使用无参构造器，`AnnotationConfigApplicationContext`其有参构造器的处理流程
    ```java
    public AnnotationConfigApplicationContext(Class<?>... annotatedClasses) {
		this();
		register(annotatedClasses);
		refresh();
	}
    ```

# 4、AOP

指在程序运行期间动态的将某段代码切入到指定方法指定位置进行运行的编程方式，实际上是动态代理

## 4.1、AOP基于注解写法
- 导入aop模块；Spring AOP：(spring-aspects)；
- 定义一个业务逻辑类；在业务逻辑运行的时候将日志进行打印（方法之前、方法运行结束、方法出现异常，xxx）
- 定义一个日志切面类（LogAspects）：切面类里面的方法需要动态感知MathCalculator.div运行到哪里然后执行；通知方法：
    - 前置通知(@Before)：logStart：在目标方法(div)运行之前运行
    - 后置通知(@After)：logEnd：在目标方法(div)运行结束之后运行（无论方法正常结束还是异常结束）
    - 返回通知(@AfterReturning)：logReturn：在目标方法(div)正常返回之后运行
    - 异常通知(@AfterThrowing)：logException：在目标方法(div)出现异常以后运行
    - 环绕通知(@Around)：动态代理，手动推进目标方法运行（joinPoint.procced()）
- 给切面类的目标方法标注何时何地运行（通知注解）
- 将切面类和业务逻辑类（目标方法所在类）都加入到容器中；
- 必须告诉Spring哪个类是切面类(给切面类上加一个注解：@Aspect)
- 给配置类中加 @EnableAspectJAutoProxy 【开启基于注解的aop模式】
```java
public class MathCalculator {
	public int div(int i,int j){
		System.out.println("MathCalculator...div...");
		return i/j;	
	}
}

@Aspect // 告诉Spring当前类是一个切面类
public class LogAspects {
	// 抽取公共的切入点表达式
	// 1、本类引用
	// 2、其他的切面引用
	@Pointcut("execution(public int com.atguigu.aop.MathCalculator.*(..))")
	public void pointCut(){};
	// @Before在目标方法之前切入；切入点表达式（指定在哪个方法切入）
	@Before("pointCut()")
	public void logStart(JoinPoint joinPoint){
		Object[] args = joinPoint.getArgs();
		System.out.println(""+joinPoint.getSignature().getName()+"运行。。。@Before:参数列表是：{"+Arrays.asList(args)+"}");
	}
	@After("com.atguigu.aop.LogAspects.pointCut()")
	public void logEnd(JoinPoint joinPoint){
		System.out.println(""+joinPoint.getSignature().getName()+"结束。。。@After");
	}
	// JoinPoint一定要出现在参数表的第一位
	@AfterReturning(value="pointCut()",returning="result")
	public void logReturn(JoinPoint joinPoint,Object result){
		System.out.println(""+joinPoint.getSignature().getName()+"正常返回。。。@AfterReturning:运行结果：{"+result+"}");
	}
	@AfterThrowing(value="pointCut()",throwing="exception")
	public void logException(JoinPoint joinPoint,Exception exception){
		System.out.println(""+joinPoint.getSignature().getName()+"异常。。。异常信息：{"+exception+"}");
	}
}
 

@EnableAspectJAutoProxy // 开启基于注解的aop模式
@Configuration
public class MainConfigOfAOP { 
	//业务逻辑类加入容器中
	@Bean
	public MathCalculator calculator(){
		return new MathCalculator();
	}
	//切面类加入到容器中
	@Bean
	public LogAspects logAspects(){
		return new LogAspects();
	}
}
```

## 4.2、AOP原理

### 4.2.1、`@EnableAspectJAutoProxy`是什么
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AspectJAutoProxyRegistrar.class)
public @interface EnableAspectJAutoProxy {
}
```
- （1）`@Import(AspectJAutoProxyRegistrar.class)`：给容器中导入`AspectJAutoProxyRegistrar` 利用`AspectJAutoProxyRegistrar`自定义给容器中注册bean；`BeanDefinetion：internalAutoProxyCreator=AnnotationAwareAspectJAutoProxyCreator`

    给容器中注册一个AnnotationAwareAspectJAutoProxyCreator；

- （2）AnnotationAwareAspectJAutoProxyCreator：
    ```
    AnnotationAwareAspectJAutoProxyCreator，其是一个 InstantiationAwareBeanPostProcessor
        -> AspectJAwareAdvisorAutoProxyCreator
            -> AbstractAdvisorAutoProxyCreator
                -> AbstractAutoProxyCreator implements SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware
                关注后置处理器（在bean初始化完成前后做事情）、自动装配BeanFactory
    ```
    * AbstractAutoProxyCreator.setBeanFactory()
    * AbstractAutoProxyCreator.有后置处理器的逻辑；
    * AbstractAdvisorAutoProxyCreator.setBeanFactory() -> initBeanFactory()
    * AnnotationAwareAspectJAutoProxyCreator.initBeanFactory()

### 4.2.2、整体流程

- （1）传入配置类，创建ioc容器；

- （2）注册配置类，调用refresh（）刷新容器；

- （3）`registerBeanPostProcessors(beanFactory);`注册bean的后置处理器来方便拦截bean的创建；

    - （1）先获取ioc容器已经定义了的需要创建对象的所有BeanPostProcessor；
    - （2）给容器中加别的BeanPostProcessor；
    - （3）优先注册实现了`PriorityOrdered`接口的`BeanPostProcessor`；
    - （4）再给容器中注册实现了Ordered接口的BeanPostProcessor；
    - （4）注册没实现优先级接口的BeanPostProcessor；
    - （5）注册BeanPostProcessor，实际上就是创建BeanPostProcessor对象，保存在容器中；
    - （6）注册BeanPostProcessor，实际上就是创建BeanPostProcessor对象，保存在容器中；
        以`创建internalAutoProxyCreator的BeanPostProcessor【AnnotationAwareAspectJAutoProxyCreator】`为例
        - 创建Bean的实例；
        - populateBean；给bean的各种属性赋值；
        - initializeBean：初始化bean；
            - invokeAwareMethods()：处理Aware接口的方法回调；
            - applyBeanPostProcessorsBeforeInitialization()：应用后置处理器的postProcessBeforeInitialization();
            - invokeInitMethods()；执行自定义的初始化方法；
            - applyBeanPostProcessorsAfterInitialization()；执行后置处理器的postProcessAfterInitialization();
        - `BeanPostProcessor(AnnotationAwareAspectJAutoProxyCreator)`创建成功；--> aspectJAdvisorsBuilder；
    - （7）把BeanPostProcessor注册到BeanFactory中；`beanFactory.addBeanPostProcessor(postProcessor);`

**以上是创建和注册AnnotationAwareAspectJAutoProxyCreator的过程**

---
- （4）`finishBeanFactoryInitialization(beanFactory);`完成BeanFactory初始化工作；创建剩下的单实例bean；
    - （1）遍历获取容器中所有的Bean，依次创建对象`getBean(beanName);`，主要流程：getBean->doGetBean()->getSingleton()
    - （2）创建bean<br/>
        【AnnotationAwareAspectJAutoProxyCreator在所有bean创建之前会有一个拦截，InstantiationAwareBeanPostProcessor，会调用postProcessBeforeInstantiation()】
        - （1）先从缓存中获取当前bean，如果能获取到，说明bean是之前被创建过的，直接使用，否则再创建；只要创建好的Bean都会被缓存起来；
        - （2）`createBean();`创建bean；<br/>
            AnnotationAwareAspectJAutoProxyCreator 会在任何bean创建之前先尝试返回bean的实例【BeanPostProcessor是在Bean对象创建完成初始化前后调用的】【InstantiationAwareBeanPostProcessor是在创建Bean实例之前先尝试用后置处理器返回对象的】
            - resolveBeforeInstantiation(beanName, mbdToUse);解析BeforeInstantiation 希望后置处理器在此能返回一个代理对象；如果能返回代理对象就使用，如果不能就继续；
                - 后置处理器先尝试返回对象；
                    ```java
                    bean = applyBeanPostProcessorsBeforeInstantiation（）：
                    // 拿到所有后置处理器，如果是InstantiationAwareBeanPostProcessor;
                    // 就执行postProcessBeforeInstantiation
                    if (bean != null) {
                        bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
                    }
                    ```
            - doCreateBean(beanName, mbdToUse, args);真正的去创建一个bean实例；

## 4.3、AnnotationAwareAspectJAutoProxyCreator的作用

AnnotationAwareAspectJAutoProxyCreator实现自InstantiationAwareBeanPostProcessor，其作用：

- 1、每一个bean创建之前，调用postProcessBeforeInstantiation()；主要关心目标类以及需要切入的类；
    - 判断当前bean是否在advisedBeans中（保存了所有需要增强bean）；
    - 判断当前bean是否是基础类型的Advice、Pointcut、Advisor、AopInfrastructureBean，或者是否是切面（@Aspect）；
    - 是否需要跳过：
        - 获取候选的增强器（切面里面的通知方法）【List<Advisor> candidateAdvisors】每一个封装的通知方法的增强器是 InstantiationModelAwarePointcutAdvisor；判断每一个增强器是否是 AspectJPointcutAdvisor 类型的；返回true
        - 永远返回false；

- 2、创建对象：postProcessAfterInitialization；`return wrapIfNecessary(bean, beanName, cacheKey);`//包装如果需要的情况下
    - （1）获取当前bean的所有增强器（通知方法）  Object[]  specificInterceptors；
        - 找到候选的所有的增强器（找哪些通知方法是需要切入当前bean方法的）；
        - 获取到能在bean使用的增强器；
        - 给增强器排序
    - （2）保存当前bean在advisedBeans中；
    - （3）如果当前bean需要增强，创建当前bean的代理对象；
        - 获取所有增强器（通知方法）
        - 保存到proxyFactory；
        - 创建代理对象：由Spring自动决定
            ```java
            JdkDynamicAopProxy(config); // jdk动态代理；
            ObjenesisCglibAopProxy(config); // cglib的动态代理；
            ```
    - （4）给容器中返回当前组件使用cglib增强了的代理对象；
    - （5）以后容器中获取到的就是这个组件的代理对象，执行目标方法的时候，代理对象就会执行通知方法的流程；

- 3、目标方法执行：容器中保存了组件的代理对象（cglib增强后的对象），这个对象里面保存了详细信息（比如增强器，目标对象，xxx）；

    - （1）CglibAopProxy.intercept();拦截目标方法的执行
        ```java
        // org.springframework.aop.framework.CglibAopProxy.DynamicAdvisedInterceptor#intercept
        @Override
		public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        }
        ```
    - （2）根据ProxyFactory对象获取将要执行的目标方法拦截器链；`List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);`
        - List<Object> interceptorList保存所有拦截器，其中包含一个默认的ExposeInvocationInterceptor和另外4个增强器；
        - 遍历所有的增强器，将其转为Interceptor；`registry.getInterceptors(advisor);`;
        - 将增强器转为List<MethodInterceptor>；
            - 如果是MethodInterceptor，直接加入到集合中；
            - 如果不是，使用AdvisorAdapter将增强器转为MethodInterceptor；
            - 转换完成返回MethodInterceptor数组；
    - （3）如果没有拦截器链，直接执行目标方法；`拦截器链（每一个通知方法又被包装为方法拦截器，利用MethodInterceptor机制）`；
    - （4）如果有拦截器链，把需要执行的目标对象，目标方法，拦截器链等信息传入创建一个 CglibMethodInvocation 对象，并调用 Object retVal =  mi.proceed();
        ```java
        retVal = new CglibMethodInvocation(proxy, target, method, args, targetClass, chain, methodProxy).proceed();
        ```
    - （5）拦截器链的触发过程：
        ```java
        @Override
        public Object proceed() throws Throwable {
            //	We start with an index of -1 and increment early.
            if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
                return invokeJoinpoint();
            }
            Object interceptorOrInterceptionAdvice =
                    this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
            if (interceptorOrInterceptionAdvice instanceof InterceptorAndDynamicMethodMatcher) {
                // Evaluate dynamic method matcher here: static part will already have
                // been evaluated and found to match.
                InterceptorAndDynamicMethodMatcher dm =
                        (InterceptorAndDynamicMethodMatcher) interceptorOrInterceptionAdvice;
                if (dm.methodMatcher.matches(this.method, this.targetClass, this.arguments)) {
                    return dm.interceptor.invoke(this);
                }
                else {
                    // Dynamic matching failed.
                    // Skip this interceptor and invoke the next in the chain.
                    return proceed();
                }
            }
            else {
                // It's an interceptor, so we just invoke it: The pointcut will have
                // been evaluated statically before this object was constructed.
                return ((MethodInterceptor) interceptorOrInterceptionAdvice).invoke(this);
            }
        }
        ```
        - 如果没有拦截器执行执行目标方法，或者拦截器的索引和拦截器数组-1大小一样（指定到了最后一个拦截器）执行目标方法；
        - 链式获取每一个拦截器，拦截器执行`org.aopalliance.intercept.MethodInterceptor.invoke()`方法，每一个拦截器等待下一个拦截器执行完成返回以后再来执行；拦截器链的机制，保证通知方法与目标方法的执行顺序；
            - ExposeInvocationInterceptor
            - AspectJAfterThrowingAdvice：异常通知
            - AfterReturningAdviceInterceptor：返回通知
            - AspectJAfterAdvice：后置通知
            - MethodBeforeAdviceInterceptor：前置通知，然后调用目标方法
        

## 4.4、总结
- @EnableAspectJAutoProxy 开启AOP功能；
- @EnableAspectJAutoProxy 会给容器中注册一个组件 AnnotationAwareAspectJAutoProxyCreator；
- AnnotationAwareAspectJAutoProxyCreator是一个后置处理器；
- 容器的创建流程
    - registerBeanPostProcessors（）注册后置处理器；创建AnnotationAwareAspectJAutoProxyCreator对象
    - finishBeanFactoryInitialization（）初始化剩下的单实例bean
        - 创建业务逻辑组件和切面组件；
        - AnnotationAwareAspectJAutoProxyCreator拦截组件的创建过程；
        - 组件创建完之后，判断组件是否需要增强：`是`：切面的通知方法，包装成增强器（Advisor）;给业务逻辑组件创建一个代理对象（cglib）；
- 执行目标方法：
    - 代理对象执行目标方法；
    - CglibAopProxy.intercept()；
        - 得到目标方法的拦截器链（增强器包装成拦截器MethodInterceptor）
        - 利用拦截器的链式机制，依次进入每一个拦截器进行执行；
        - 效果：
            - 正常执行：前置通知 -> 目标方法 -> 后置通知 -> 返回通知
            - 出现异常：前置通知 -> 目标方法 -> 后置通知 -> 异常通知

# 5、声明式事务

## 5.1、配置数据源
```java
@EnableTransactionManagement
@ComponentScan("com.blue.fish.spring.tx")
@Configuration
public class TxConfig {
	//数据源
	@Bean
	public DataSource dataSource() throws Exception{
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setUser("root");
		dataSource.setPassword("123456");
		dataSource.setDriverClass("com.mysql.jdbc.Driver");
		dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
		return dataSource;
	}
	@Bean
	public JdbcTemplate jdbcTemplate() throws Exception{
		//Spring对@Configuration类会特殊处理；给容器中加组件的方法，多次调用都只是从容器中找组件
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
		return jdbcTemplate;
	}
	//注册事务管理器在容器中
	@Bean
	public PlatformTransactionManager transactionManager() throws Exception{
		return new DataSourceTransactionManager(dataSource());
	}
}
```
- 给方法上标注 @Transactional 表示当前方法是一个事务方法；
- @EnableTransactionManagement 开启基于注解的事务管理功能；
- 配置事务管理器来控制事务
    ```java
    @Bean
    public PlatformTransactionManager transactionManager(){}
    ```
## 5.2、原理
- （1）`@EnableTransactionManagement`：该注解利用TransactionManagementConfigurationSelector给容器中会导入组件，导入两个组件
    ```java
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Import(TransactionManagementConfigurationSelector.class)
    public @interface EnableTransactionManagement {
        boolean proxyTargetClass() default false;
        // 在TransactionManagementConfigurationSelector里selectImports时需要用到
        AdviceMode mode() default AdviceMode.PROXY;
        int order() default Ordered.LOWEST_PRECEDENCE;
    }

    public class TransactionManagementConfigurationSelector extends AdviceModeImportSelector<EnableTransactionManagement> {
        @Override
        protected String[] selectImports(AdviceMode adviceMode) {
            // 用到了@EnableTransactionManagement注解里的mode属性
            switch (adviceMode) {
                case PROXY:
                    return new String[] {AutoProxyRegistrar.class.getName(), ProxyTransactionManagementConfiguration.class.getName()};
                case ASPECTJ:
                    return new String[] {TransactionManagementConfigUtils.TRANSACTION_ASPECT_CONFIGURATION_CLASS_NAME};
                default:
                    return null;
            }
        }
    }
    ```
    - AutoProxyRegistrar

        给容器中注册一个 InfrastructureAdvisorAutoProxyCreator 组件，其是一个后置处理器，利用后置处理器机制在对象创建以后，包装对象，返回一个代理对象（增强器），代理对象执行方法利用拦截器链进行调用；
        
    - ProxyTransactionManagementConfiguration：给容器中注册事务增强器
        - 事务增强器要用事务注解的信息，AnnotationTransactionAttributeSource解析事务注解
        - 事务拦截器：TransactionInterceptor；保存了事务属性信息，事务管理器；是一个MethodInterceptor，在目标方法执行的时候执行拦截器链；
            ```java
            @Configuration
            public class ProxyTransactionManagementConfiguration extends AbstractTransactionManagementConfiguration {
                // 
                @Bean(name = TransactionManagementConfigUtils.TRANSACTION_ADVISOR_BEAN_NAME)
                @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
                public BeanFactoryTransactionAttributeSourceAdvisor transactionAdvisor() {
                    BeanFactoryTransactionAttributeSourceAdvisor advisor = new BeanFactoryTransactionAttributeSourceAdvisor();
                    advisor.setTransactionAttributeSource(transactionAttributeSource());
                    advisor.setAdvice(transactionInterceptor());
                    advisor.setOrder(this.enableTx.<Integer>getNumber("order"));
                    return advisor;
                }
                // 事务属性
                @Bean
                @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
                public TransactionAttributeSource transactionAttributeSource() {
                    return new AnnotationTransactionAttributeSource();
                }
                // 执行目标方法拦截器
                @Bean
                @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
                public TransactionInterceptor transactionInterceptor() {
                    TransactionInterceptor interceptor = new TransactionInterceptor();
                    interceptor.setTransactionAttributeSource(transactionAttributeSource());
                    if (this.txManager != null) {
                        interceptor.setTransactionManager(this.txManager);
                    }
                    return interceptor;
                }

            }
            ```
            - 先获取事务相关的属性
            - 再获取PlatformTransactionManager，如果事先没有添加指定任何transactionmanger，最终会从容器中按照类型获取一个PlatformTransactionManager；
            - 执行目标方法：如果异常，获取到事务管理器，利用事务管理回滚操作；如果正常，利用事务管理器，提交事务

# 6、Spring扩展

## 6.1、BeanFactoryPostProcessor

beanFactory的后置处理器，在BeanFactory标准初始化之后调用，来定制和修改BeanFactory的内容；所有的bean定义已经保存加载到beanFactory，但是bean的实例还未创建

**原理**
- （1）ioc容器创建对象；
- （2）invokeBeanFactoryPostProcessors(beanFactory);如何找到所有的BeanFactoryPostProcessor并执行他们的方法；
    - 直接在BeanFactory中找到所有类型是BeanFactoryPostProcessor的组件，并执行他们的方法；
    - 在初始化创建其他组件前面执行；



## 6.2、BeanDefinitionRegistryPostProcessor
```java
public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor{
    // BeanDefinitionRegistry的Bean定义信息的保存中心，以后BeanFactory就是按照BeanDefinitionRegistry里面保存的每一个bean定义信息创建bean实例；
    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException;
}
```
在所有bean定义信息将要被加载，bean实例还未创建的；优先于`BeanFactoryPostProcessor`执行；利用`BeanDefinitionRegistryPostProcessor`给容器中再额外添加一些组件；

自定义BeanDefinitionRegistryPostProcessor
```java
@Component
public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor{
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println("MyBeanDefinitionRegistryPostProcessor...bean的数量："+beanFactory.getBeanDefinitionCount());
	}
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		System.out.println("postProcessBeanDefinitionRegistry...bean的数量："+registry.getBeanDefinitionCount());
        // 自定义BeanDefinition
		//RootBeanDefinition beanDefinition = new RootBeanDefinition(Blue.class);
		AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.rootBeanDefinition(Blue.class).getBeanDefinition();
		registry.registerBeanDefinition("hello", beanDefinition);
	}
}
```
**原理**
- （1）ioc创建对象
- （2）refresh() -> invokeBeanFactoryPostProcessors(beanFactory);
- （3）从容器中获取到所有的BeanDefinitionRegistryPostProcessor组件
    具体实现参考：`PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory, List<BeanFactoryPostProcessor>)`
    - 依次触发所有的postProcessBeanDefinitionRegistry()方法；
    - 再来触发postProcessBeanFactory()方法BeanFactoryPostProcessor；
- （4）再来从容器中找到BeanFactoryPostProcessor组件；然后依次触发postProcessBeanFactory()方法

## 6.3、ApplicationListener

监听容器中发布的事件，其实基于事件驱动模型开发，主要监听 ApplicationEvent 及其下面的子事件
```java
// 监听 ApplicationEvent 及其下面的子事件；
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {
	void onApplicationEvent(E event);
}
```
### 6.3.1、步骤
- 写一个监听器（ApplicationListener实现类）来监听某个事件（ApplicationEvent及其子类）
    ```java
    @Component
    public class MyApplicationListener implements ApplicationListener<ApplicationEvent> {
        // 当容器中发布此事件以后，方法触发
        @Override
        public void onApplicationEvent(ApplicationEvent event) {
            System.out.println("收到事件"+event);
        }
    }
    ```
- 把监听器加入到容器；
- 只要容器中有相关事件的发布，就能监听到这个事件
    - ContextRefreshedEvent：容器刷新完成（所有bean都完全创建）会发布这个事件；
    - ContextClosedEvent：关闭容器会发布这个事件；
- 发布事件：`applicationContext.publishEvent();`

### 6.3.2、原理

**ContextRefreshedEvent**
- 容器创建对象：refresh()；
- finishRefresh();容器刷新完成会发布ContextRefreshedEvent事件

**【事件发布流程】**
- 调用finishRefresh();方法发布事件
- publishEvent(new ContextRefreshedEvent(this));
    - 获取事件的多播器（派发器）：getApplicationEventMulticaster();
    - multicastEvent派发事件；
    - 获取到所有的ApplicationListener；
        - for (final ApplicationListener<?> listener : getApplicationListeners(event, type)) {
            - 如果有Executor，可以支持使用Executor进行异步派发：Executor executor = getTaskExecutor();
            - 否则，同步的方式直接执行listener方法；invokeListener(listener, event);拿到listener回调onApplicationEvent方法；

**【获取事件多播器（派发器）】**
- 在创建容器refresh()方法里调用方法：`initApplicationEventMulticaster();`初始化ApplicationEventMulticaster；
    - 先去容器中找有没有id=“applicationEventMulticaster”的组件；
    - 如果没有`this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);`，并且加入到容器中，我们就可以在其他组件要派发事件，自动注入这个applicationEventMulticaster；

**【容器中的监听器】**
- 在创建溶解refresh()方法是调用方法：`registerListeners();`从容器中拿到所有的监听器，把他们注册到applicationEventMulticaster中；
    ```java
    String[] listenerBeanNames = getBeanNamesForType(ApplicationListener.class, true, false);
    //将listener注册到ApplicationEventMulticaster中
    getApplicationEventMulticaster().addApplicationListenerBean(listenerBeanName);
    ```

**使用`@EventListener`**
- 使用`EventListenerMethodProcessor`处理器来解析方法上的@EventListener；
    ```java
    @EventListener(classes={ApplicationEvent.class})
	public void listen(ApplicationEvent event){
		System.out.println("UserService。。监听到的事件："+event);
	}
    ```
-  `EventListenerMethodProcessor`实现了`SmartInitializingSingleton` -> afterSingletonsInstantiated();
    - 在创建溶解refresh()方法是调用方法：`finishBeanFactoryInitialization(beanFactory);初始化剩下的单实例bean；`
        - 先创建所有的单实例bean；getBean();
        - 获取所有创建好的单实例bean，判断是否是SmartInitializingSingleton类型的；如果是就调用afterSingletonsInstantiated();


### 6.3.2、Spring中的事件

Spring 提供了以下五种标准的事件：
- 上下文更新事件（ContextRefreshedEvent）：该事件会在ApplicationContext 被初始化或者更新时发布。也可以在调用ConfigurableApplicationContext 接口中的 #refresh() 方法时被触发。
- 上下文开始事件（ContextStartedEvent）：当容器调用ConfigurableApplicationContext 的 #start() 方法开始/重新开始容器时触发该事件。
- 上下文停止事件（ContextStoppedEvent）：当容器调用 ConfigurableApplicationContext 的 #stop() 方法停止容器时触发该事件。
- 上下文关闭事件（ContextClosedEvent）：当ApplicationContext 被关闭时触发该事件。容器被关闭时，其管理的所有单例 Bean 都被销毁。
- 请求处理事件（RequestHandledEvent）：在 Web应用中，当一个HTTP 请求（request）结束触发该事件

自定义事件：
- 编写自定义事件类：
    ```java
    public class CustomApplicationEvent extends ApplicationEvent{  
        public CustomApplicationEvent(Object source, final String msg) {  
            super(source);
        }  
    }
    ```
- 为了监听这个事件，还需要创建一个监听器
    ```java
    public class CustomEventListener implements ApplicationListener<CustomApplicationEvent> {
        @Override  
        public void onApplicationEvent(CustomApplicationEvent applicationEvent) {  
            // handle event  
        }
    }
    ```
- 之后通过 ApplicationContext 接口的 #publishEvent(Object event) 方法，来发布自定义事件：
    ```java
    // 创建 CustomApplicationEvent 事件
    CustomApplicationEvent customEvent = new CustomApplicationEvent(applicationContext, "Test message");
    // 发布事件
    applicationContext.publishEvent(customEvent);
    ```


