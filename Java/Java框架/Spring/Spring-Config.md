
# 1、IOC容器Bean注册

## 1.1、bean配置

```java
public class Person {
    private String name;
    private Integer age;
    // 省略set、get方法
}
```
### 1.1.1、配置文件配置

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

### 1.1.2、注解方法

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
- `@Bean` 注解对应的就是配置`<bean>`，方法返回值对应的是配置文件中的class，方法名称默认对应的是id，可以通过`@Bean(name = "person")`指定，默认是单实例的;
- `@Scope`：调整作用域
    - `prototype`：ConfigurableBeanFactory#SCOPE_PROTOTYPE，多实例的：ioc容器启动并不会去调用方法创建对象放在容器中。每次获取的时候才会调用方法创建对象；
    - `singleton`：ConfigurableBeanFactory#SCOPE_SINGLETON，单实例的（默认值）：ioc容器启动会调用方法创建对象放到ioc容器中。以后每次获取就是直接从容器（map.get()）中拿
    - `request`：org.springframework.web.context.WebApplicationContext#SCOPE_REQUEST，同一次请求创建一个实例
    - `sesssion`：org.springframework.web.context.WebApplicationContext#SCOPE_SESSION，同一个session创建一个实例
- `@Lazy`：懒加载，主要是单实例bean：默认在容器启动的时候创建对象；懒加载：容器启动不创建对象。第一次使用(获取)Bean创建对象，并初始化；

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
在SpringBoot当中，ComponentScan注解是在被用在SpringBootApplication注解当中的，比如有下启动类：
```java
@SpringBootApplication
public class BeanDefinitionApp {
    public static void main(String[] args) {
        SpringApplication.run(BeanDefinitionApp.class, args);
    }
}
```
SpringBootApplication注解定义如下：
```java
...
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),  @Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {
//省略非关键代码
}
```
当 Spring Boot 启动时，ComponentScan 的启用意味着会去扫描出所有定义的 Bean，那么扫描什么位置呢？这是由 ComponentScan 注解的 basePackages 属性指定的，但是在上面的案例中，直接使用的是 SpringBootApplication 注解定义的 ComponentScan，它的 basePackages 没有指定，所以默认为空（即{}）。此时扫描的是什么包呢？通过`ComponentScanAnnotationParser#parse`，我们可以找到如下代码：
```java
....
if (basePackages.isEmpty()) {
	basePackages.add(ClassUtils.getPackageName(declaringClass));
}
...
```
通过代码调试可以看到：

![](image/SpringBoot-ComponentScan加载.png)

当 basePackages 为空时，扫描的包会是 declaringClass 所在的包，在本案例中，declaringClass 就是 BeanDefinitionApp.class，所以扫描的包其实就是它所在的包，即 `com.qing.fan.app`

> 另外需要注意的是：如果你显示式指定其它包，原来的默认扫描包就被忽略了；

## 1.3、Conditional注解

```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Conditional {
	Class<? extends Condition>[] value();
}

```
`@Conditional({Condition})`：按照一定的条件进行判断，满足条件给容器中注册bean

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

- [Import扩展点](../../源码分析/框架/spring/springboot源码.md#13171@Import注解)

**@Import(要导入到容器中的组件)**；容器中就会自动注册这个组件，id默认是全类名
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

**ImportSelector：** 返回需要导入的组件的全类名数组；
```java
public interface ImportSelector {
    /**
    * Select and return the names of which class(es) should be imported based on
    * the {@link AnnotationMetadata} of the importing @{@link Configuration} class.
    */
    String[] selectImports(AnnotationMetadata importingClassMetadata);
}
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

**ImportBeanDefinitionRegistrar：** 手动注册bean到容器中
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

## 1.6、实现BeanDefinitionRegistryPostProcessor

```java
@Component
public class MyBeanRegister implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();
        rootBeanDefinition.setBeanClass(Blue.class);
        registry.registerBeanDefinition("blue", rootBeanDefinition);
    }
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }
}
```
使用时
```java
@Autowired
@Quanlifier("blue")
private Color color;
```

## 1.7、属性赋值

**1、使用@Value赋值**
- 基本数值：`@Value("张三")`
- 可以写Spring EL表达式； `#{}`，比如：`@Value("#{20-2}")`
- 可以写`${}`；取出配置文件【properties】中的值（在运行环境变量里面的值）：`@Value("${person.nickName}")`

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

- 指定初始化和销毁方法：通过`@Bean`指定`initMethod`和`destroyMethod`；
```
@Bean(initMethod="init",destroyMethod="detory")
```
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
- `@Qualifier("bookDao")`：使用`@Qualifier`指定需要装配的组件的id，而不是使用属性名；使用Qualifier时如果一个类名是以两个大写字母开头的，则首字母不变，其它情况下默认首字母变成小写；

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

- [SpringAOP Reference](https://docs.spring.io/spring-framework/docs/2.0.x/reference/aop.html)

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

AnnotationAwareAspectJAutoProxyCreator实现自`InstantiationAwareBeanPostProcessor`，其作用：

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
        - `List<Object> interceptorList`保存所有拦截器，其中包含一个默认的ExposeInvocationInterceptor和另外4个增强器；
        - 遍历所有的增强器，将其转为Interceptor；`registry.getInterceptors(advisor);`;
        - 将增强器转为`List<MethodInterceptor>`；
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
- AnnotationAwareAspectJAutoProxyCreator 是一个后置处理器；
- 容器的创建流程
    - registerBeanPostProcessors() 注册后置处理器；创建 AnnotationAwareAspectJAutoProxyCreator 对象
    - finishBeanFactoryInitialization() 初始化剩下的单实例bean
        - 创建业务逻辑组件和切面组件；
        - AnnotationAwareAspectJAutoProxyCreator拦截组件的创建过程；
        - 组件创建完之后，判断组件是否需要增强：`是`：切面的通知方法，包装成增强器（Advisor）;给业务逻辑组件创建一个代理对象（cglib）；
- 执行目标方法：
    - 代理对象执行目标方法；
    - `CglibAopProxy.intercept();`
        - 得到目标方法的拦截器链（增强器包装成拦截器MethodInterceptor）
        - 利用拦截器的链式机制，依次进入每一个拦截器进行执行；
        - 效果：
            - 正常执行：`前置通知 -> 目标方法 -> 后置通知 -> 返回通知`
            - 出现异常：`前置通知 -> 目标方法 -> 后置通知 -> 异常通知`

## 4.5、AOP使用问题

### 4.5.1、this调用的当前类方法无法被拦截

在类的内部，通过 this 方式调用的方法，是不会被 Spring AOP 增强的，只有引用的是被动态代理创建出来的对象，才会被 Spring 增强，具备 AOP 该有的功能；

这种方式一般是通过注入自己或者使用`AopContext.currentProxy()`来获取代理类，它的核心就是通过一个 ThreadLocal 来将 Proxy 和线程绑定起来，这样就可以随时拿出当前线程绑定的 Proxy

### 4.5.2、直接访问被拦截类的属性抛空指针异常

有如下代码
```java
@Service
public class AdminUserService {
    public final User adminUser = new User("202101166");
    public void login() {
        System.out.println("admin user login...");
    }
}
@Service
public class ElectricService {
    @Autowired
    private AdminUserService adminUserService;
    public void pay() throws Exception {
        adminUserService.login();
        // 这里会报空指针
        String payNum = adminUserService.adminUser.getPayNum();
        System.out.println("User pay num : " + payNum);
        System.out.println("Pay with alipay ...");
        Thread.sleep(1000);
    }
}
@Aspect
@Service
@Slf4j
public class AopConfig {
    @Before("execution(* com.qing.fan.AdminUserService.login(..)) ")
    public void logAdminLogin(JoinPoint pjp) throws Throwable {
        System.out.println("! admin login ...");
    }
}
```
正常情况下，AdminUserService 只是一个普通的对象，而 AOP 增强过的则是一个 `AdminUserService$$EnhancerBySpringCGLIB$$xxxx`，这个类实际上是 AdminUserService 的一个子类。它会 overwrite 所有 public 和 protected 方法，并在内部将调用委托给原始的 AdminUserService 实例，代理类实例的默认构建方式很特别。总结和对比下通过反射来实例化对象的方式，包括：
- `java.lang.Class.newInstance()`
- `java.lang.reflect.Constructor.newInstance()`
- `sun.reflect.ReflectionFactory.newConstructorForSerialization().newInstance()`

前两种初始化方式都会同时初始化类成员变量，但是最后一种通过 `ReflectionFactory.newConstructorForSerialization().newInstance()` 实例化类则不会初始化类成员变量；

解决方案：
- 在 AdminUserService 里加了个 getUser() 方法；
- 也可以让产生的代理对象的属性值不为 null，修改启动参数 `-Dspring.objenesis.ignore=true`，这样代理类的属性是会被 Spring 初始化的

## 4.6、使用场景

- 用户登录权限的校验实现接口 `HandlerInterceptor` + `WebMvcConfigurer`
- 异常处理使用注解 `@RestControllerAdvice` + `@ExceptionHandler`
- 数据格式返回使用注解 `@ControllerAdvice` 并且实现接口 `ResponseBodyAdvice`	
	
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


### 6.3.3、Spring中的事件

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

### 6.3.4、Event最佳实践

- [Spring Event最佳实践](https://juejin.cn/post/7313387525819973671)

（1）业务系统一定要先实现优雅关闭服务，才能使用 Spring Event

Spring 广播消息时，Spring会在 ApplicationContext 中查找所有的监听者，即需要 getBean 获取 bean 实例。然而 Spring 有个限制————ApplicationContext 关闭期间，不得GetBean 否则会报错

（2）改造系统开启入口流量（Http、MQ、RPC）的时机，确保在Spring 启动完成后开启入口流量

# 7、Spring异步化

## 7.1、本地异步调用

Spring可以通过使用`@Async`注解来实现异步化调用，示例：
```java
@Component
public class AsyncJob {
	@Async
	public Future<String> saveOpLog() throws InterruptedException {
		Thread.sleep(1000L);
		String result = new Date() + "插入操作日志";
		System.out.println(result);
		return new AsyncResult<>(result);
	}
}
```
Async实现原理：其主要实现类是 ThreadPoolTaskExecutor，Spring自己实现的线程池；使用 @Async 注意点：
- @Async 标准的方法必须返回void或者Future；
- 建议将 @Async 标注的方法放到独立的类中去，因为其真正执行的时候是使用代理类来执行的；如果使用的是 this.method 来调用异步方法，异步方法会失效；
- 建议自定义 BlockingQueue 的大小，因为其默认是 Integer.MAX_VALUE，如果没有指定容量；任何大于0的值使用的是LinkedBlockingQueue，否则使用的是 SynchronousQueue；

## 7.2、远程异步调用

Spring5.0 WebClient

# 8、自定义请求体和响应体

## 8.1、请求体：RequestBodyAdvice

### 8.1.1、基本含义

```java
public interface RequestBodyAdvice {
	
	boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType);

	HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException;

	Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType);

	@Nullable
	Object handleEmptyBody(@Nullable Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType);
}
```
该接口表示允许在请求体被读取并转换为一个对象之前定制请求，也允许在结果对象作为`@RequestBody`或`HttpEntity`方法参数传递到控制器方法之前处理它。

这个接口的实现可以直接用`RequestMappingHandlerAdapter`注册，或者更可能的是用`@ControllerAdvice`注释，在这种情况下，都会被自动检测

### 8.1.2、原理

所有`ResponseBodyAdvice`接口的调用是发生在`AbstractMessageConverterMethodArgumentResolver`的`readWithMessageConverters()`中：
```java
protected <T> Object readWithMessageConverters(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType) 
        throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {
    ...
        if (genericConverter != null ? genericConverter.canRead(targetType, contextClass, contentType) :
                (targetClass != null && converter.canRead(targetClass, contentType))) {
            if (message.hasBody()) {
                // 执行的是 RequestBodyAdvice的beforeBodyRead
                HttpInputMessage msgToUse = getAdvice().beforeBodyRead(message, parameter, targetType, converterType);
                body = (genericConverter != null ? genericConverter.read(targetType, contextClass, msgToUse) :
                        ((HttpMessageConverter<T>) converter).read(targetClass, msgToUse));
                // 执行的是 RequestBodyAdvice的afterBodyRead
                body = getAdvice().afterBodyRead(body, msgToUse, parameter, targetType, converterType);
            } else {
                // 如果没有请求参数，则执行的是 handleEmptyBody
                body = getAdvice().handleEmptyBody(null, message, parameter, targetType, converterType);
            }
            break;
        }       
    ... 
}
```
- 如果handler方法的请求参数是非`HttpEntity`对象且handler方法由`@RequestBody`注解修饰，那么`readWithMessageConverters()`的调用发生在`RequestResponseBodyMethodProcessor#readWithMessageConverters()`中；
- 如果handler方法的请求参数是`HttpEntity`对象，那么`readWithMessageConverters()`的调用发生在`org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor#resolveArgument`中

其加载过程类似 ResponseBodyAdvice

## 8.2、响应体：ResponseBodyAdvice

### 8.2.1、基本含义

```java
public interface ResponseBodyAdvice<T> {
	boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType);
	@Nullable
	T beforeBodyWrite(@Nullable T body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response);
}
```
该接口表示允许在执行`@ResponseBody`或`ResponseEntity`控制器方法之后，在`HttpMessageConverter`编写响应体之前定制响应。

实现可以直接用`RequestMappingHandlerAdapter`和`ExceptionHandlerExceptionResolver`注册，或者使用用`@ControllerAdvice`注释，在这种情况下，都会被自动检测

### 8.2.2、原理

所有`ResponseBodyAdvice`接口的调用是发生在`AbstractMessageConverterMethodProcessor`的writeWithMessageConverters()中：
```java
/*
 * @param value 需要写入响应体的值，同时也是ResponseBodyAdvice要处理的值
 * @param returnType the type of the value
 * @param inputMessage the input messages. Used to inspect the {@code Accept} header.
 * @param outputMessage the output message to write to
*/
protected <T> void writeWithMessageConverters(@Nullable T value, MethodParameter returnType, ServletServerHttpRequest inputMessage, ServletServerHttpResponse outputMessage)
        throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
```
- 如果handler方法的返回值是非`ResponseEntity`对象且handler方法由`@ResponseBody`注解修饰，那么`writeWithMessageConverters()`的调用发生在`RequestResponseBodyMethodProcessor#handleReturnValue()`中；
- 如果handler方法的返回值是`ResponseEntity`对象，那么`writeWithMessageConverters()`的调用发生在`HttpEntityMethodProcessor#handleReturnValue()`中

> Spring是如何选择具体的 Handler的？

所有`ResponseBodyAdvice`接口的调用是发生在`AbstractMessageConverterMethodProcessor`的`writeWithMessageConverters()`中，`AbstractMessageConverterMethodProcessor`的`getAdvice()`方法会返回其在构造函数中加载好的`RequestResponseBodyAdviceChain`对象
```java
// AbstractMessageConverterMethodProcessor#writeWithMessageConverters
...
body = getAdvice().beforeBodyWrite(body, returnType, selectedMediaType,(Class<? extends HttpMessageConverter<?>>) converter.getClass(), inputMessage, outputMessage);
...
// org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodArgumentResolver#getAdvice
RequestResponseBodyAdviceChain getAdvice() {
    return this.advice;
}
```
`RequestResponseBodyAdviceChain`的`beforeBodyWrite`方法和`processBody`
```java
@Override
@Nullable
public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType contentType,Class<? extends HttpMessageConverter<?>> converterType,
        ServerHttpRequest request, ServerHttpResponse response) {
    return processBody(body, returnType, contentType, converterType, request, response);
}
private <T> Object processBody(@Nullable Object body, MethodParameter returnType, MediaType contentType,Class<? extends HttpMessageConverter<?>> converterType,
        ServerHttpRequest request, ServerHttpResponse response) {
    // getMatchingAdvice：从加载好的ResponseBodyAdvice中获取适用于当前handler的ResponseBodyAdvice
    for (ResponseBodyAdvice<?> advice : getMatchingAdvice(returnType, ResponseBodyAdvice.class)) {
        if (advice.supports(returnType, converterType)) {
            // 执行ResponseBodyAdvice的beforeBodyWrite()方法以处理handler方法返回值
            body = ((ResponseBodyAdvice<T>) advice).beforeBodyWrite((T) body, returnType, contentType, converterType, request, response);
        }
    }
    return body;
}
private <A> List<A> getMatchingAdvice(MethodParameter parameter, Class<? extends A> adviceType) {
    // 获取ResponseBodyAdvice集合
    List<Object> availableAdvice = getAdvice(adviceType);
    if (CollectionUtils.isEmpty(availableAdvice)) {
        return Collections.emptyList();
    }
    List<A> result = new ArrayList<>(availableAdvice.size());
    for (Object advice : availableAdvice) {
        // 判断ResponseBodyAdvice是否由@ControllerAdvice注解修饰
        if (advice instanceof ControllerAdviceBean) {
            ControllerAdviceBean adviceBean = (ControllerAdviceBean) advice;
            // 判断ResponseBodyAdvice是否适用于当前handler
            if (!adviceBean.isApplicableToBeanType(parameter.getContainingClass())) {
                continue;
            }
            advice = adviceBean.resolveBean();
        }
        if (adviceType.isAssignableFrom(advice.getClass())) {
            result.add((A) advice);
        }
    }
    return result;
}
```
在`RequestResponseBodyAdviceChain`中，`beforeBodyWrite()`方法调用了`processBody()`方法，`processBody()`方法会遍历所有加载好并且适用于当前handler的`ResponseBodyAdvice`并执行，至此，所有由`@ControllerAdvice`注解修饰的`ResponseBodyAdvice`接口会在这里执行

> 由`@ControllerAdvice`注解修饰的`ResponseBodyAdvice`接口会被SpringMVC框架加载到`RequestResponseBodyMethodProcessor`和`HttpEntityMethodProcessor`这两个返回值处理器中，当这两个返回值处理器将返回值写入response前，适用于当前handler的`ResponseBodyAdvice`接口会被调用，从而可以完成对返回值的定制化改造

### 8.2.3、ResponseBodyAdvice加载

ResponseBodyAdvice的加载是发生在`RequestMappingHandlerAdapter`的`afterPropertiesSet()`方法中
```java
public class RequestMappingHandlerAdapter extends AbstractHandlerMethodAdapter implements BeanFactoryAware, InitializingBean {
    @Override
	public void afterPropertiesSet() {
		// 加载ControllerAdviceBean相关内容（同时就会将由@ControllerAdvice注解修饰的ResponseBodyAdvice接口加载）
		initControllerAdviceCache();
		if (this.argumentResolvers == null) {
			List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
			this.argumentResolvers = new HandlerMethodArgumentResolverComposite().addResolvers(resolvers);
		}
		if (this.initBinderArgumentResolvers == null) {
			List<HandlerMethodArgumentResolver> resolvers = getDefaultInitBinderArgumentResolvers();
			this.initBinderArgumentResolvers = new HandlerMethodArgumentResolverComposite().addResolvers(resolvers);
		}
		if (this.returnValueHandlers == null) {
            // 获取返回值处理器，在这里就会完成RequestResponseBodyMethodProcessor和HttpEntityMethodProcessor的初始化，初始化的同时就会完成ResponseBodyAdvice接口的加载
			List<HandlerMethodReturnValueHandler> handlers = getDefaultReturnValueHandlers();
			this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite().addHandlers(handlers);
		}
	}
}
```
`initControllerAdviceCache()`会加载`ControllerAdviceBean`相关内容到`RequestMappingHandlerAdapter`中，这其中就包含由`@ControllerAdvice`注解修饰的`ResponseBodyAdvice`接口。然后在`getDefaultReturnValueHandlers()`方法中会创建返回值处理器，在创建`RequestResponseBodyMethodProcessor`和`HttpEntityMethodProcessor`时会使用加载好的ResponseBodyAdvice接口完成这两个返回值处理器的初始化；
```java
private List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers() {
    List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>(20);
    ...
    //创建并加载HttpEntityMethodProcessor
    handlers.add(new HttpEntityMethodProcessor(getMessageConverters(),this.contentNegotiationManager, this.requestResponseBodyAdvice));
    ...
    //创建并加载RequestResponseBodyMethodProcessor
    handlers.add(new RequestResponseBodyMethodProcessor(getMessageConverters(), this.contentNegotiationManager, this.requestResponseBodyAdvice));
    ...
    return handlers;
}
```
根据`getDefaultReturnValueHandlers()`方法可知，在创建`HttpEntityMethodProcessor`或者`RequestResponseBodyMethodProcessor`时，会将`RequestMappingHandlerAdapter`加载好的`ResponseBodyAdvice`传入构造函数，并且，无论是`HttpEntityMethodProcessor`还是`RequestResponseBodyMethodProcessor`，其构造函数最终都会调用到父类`AbstractMessageConverterMethodArgumentResolver`的构造函数，并在其中初始化一个`RequestResponseBodyAdviceChain`以完成`ResponseBodyAdvice`的加载
```java
public AbstractMessageConverterMethodArgumentResolver(List<HttpMessageConverter<?>> converters, @Nullable List<Object> requestResponseBodyAdvice) {
    Assert.notEmpty(converters, "'messageConverters' must not be empty");
    this.messageConverters = converters;
    this.allSupportedMediaTypes = getAllSupportedMediaTypes(converters);
    this.advice = new RequestResponseBodyAdviceChain(requestResponseBodyAdvice);
}
```

## 8.3、使用场景

- 通用加密和解密操作

# 9、参数解析-HandlerMethodArgumentResolver

通过该接口可以实现自定义参数类型解析，系统已经存在的参数解析器
- `@RequestParam` 解析 `RequestParamMethodArgumentResolver` (基础类型的默认解析器)
- `@PathVariable` 解析 `PathVariableMethodArgumentResolver`
- `@RequestBody` 解析 `RequestResponseBodyMethodProcessor`
- `@CookieValue` 解析 `ServletCookieValueMethodArgumentResolver`

# 10、自定义序列化

- [Jackson序列化-自定义ObjectMapper](../../Java基础/Java基础知识.md#73jackson序列化-自定义objectmapper)
- [Customize the Jackson ObjectMapper](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.spring-mvc.customize-jackson-objectmapper)


增加自定义类型：
```java
@Bean
public Jackson2ObjectMapperBuilderCustomizer customizer(){
    return new Jackson2ObjectMapperBuilderCustomizer() {
        @Override
        public void customize(Jackson2ObjectMapperBuilder builder) {
             builder.serializerByType(Class<?> clazzType, new JsonSerializer());
        }
    };
}
```
比如需要将时间自定义输出为时间戳：
```java
@Configuration
public class TimestampCustom {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.serializerByType(LocalDateTime.class, new LocalDateTimeToTimestamp());
            builder.serializerByType(Date.class, new DateToTimestamp());
        };
    }
    public static class LocalDateTimeToTimestamp extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime localDateTime, JsonGenerator generator, SerializerProvider serializers) throws IOException {
            if (localDateTime == null) {
                return;
            }
            generator.writeNumber(localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli());
        }
    }
    public static class DateToTimestamp extends JsonSerializer<Date> {
        @Override
        public void serialize(Date date, JsonGenerator generator, SerializerProvider serializers) throws IOException {
            if (date == null) {
                return;
            }
            Calendar calendar = Calendar.getInstance();
            int offsetMillis = calendar.get(Calendar.ZONE_OFFSET);
            long result = date.getTime() + offsetMillis;
            generator.writeNumber(result);
        }
    }
}
```

# 11、自定义starter

- [starter-原理](SpringBoot.md#四自定义starter)
- [SpringBoot Configuration Metadata](https://docs.spring.io/spring-boot/docs/current/reference/html/configuration-metadata.html)

## 11.1、基本配置

（1）引入依赖：
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <version>2.7.3</version>
        <scope>provided</scope>
    </dependency>
    <!-- 这里是处理配置的 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
        <version>2.7.3</version>
        <optional>true</optional>
    </dependency>
</dependencies>
```
（2）如果有相应的配置，定义配置类：
```java
@Data
@ConfigurationProperties(prefix = "swagger2.data")
public class Swagger2Properties {
    private boolean enableSwagger;
    private String author;
    private String doc;
}
```
（3）新增自动配置类：
```java
@Configuration
@ComponentScan // 扫描当前包
@EnableConfigurationProperties(Swagger2Properties.class)
public class Swagger2AutoConfiguration {
}
```
（4）在`resources`目录下新增`META-INF/spring.factories`文件：Swagger2AutoConfiguration的全路径
```java
org.springframework.boot.autoconfigure.EnableAutoConfiguration=com.data.swagger2.Swagger2AutoConfiguration
```

## 11.2、starter配置自动提示

如果需要定义的配置类中的配置自动提示，必须引入如下依赖：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <version>2.7.3</version>
    <optional>true</optional>
</dependency>
```
有如下方式实现

### 11.2.1、通过json配置实现

Springboot配置自动提示，IDE是通过读取配置信息的元数据而实现自动提示的。Springboot的元数据就在目录META-INF下。通过starter部分结构：

![](image/Spring-Configuration-Metadata.png)

springboot自动提示元数据就在META-INF中的`spring-configuration-metadata.json`或`additional-spring-configuration-metadata.json`；

那么可以根据springboot自动配置，在上面的基础上在 META-INF 增加文件：`additional-spring-configuration-metadata.json`，配置如下：
```json
{
  "properties": [
    {
      "name": "swagger2.data.enableSwagger",
      "type": "java.lang.Boolean",
      "defaultValue": false,
      "description": "swagger是否生效."
    },
    {
      "name": "swagger2.data.author",
      "type": "java.lang.String",
      "defaultValue": "",
      "description": "作者."
    },
    {
      "name": "swagger2.data.doc",
      "type": "java.lang.String",
      "defaultValue": "",
      "description": "文档简介."
    }
  ]
}
```

最终目录结构如下：
```
│  .gitignore               
│  pom.xml                  
│  swagger2-starter.iml     
└─src
    └─main
        │  └─swagger2
        │      │  Swagger2AutoConfiguration.java
        │      └─config
        │          Swagger2Properties.java
        └─resources
            └─META-INF
                    additional-spring-configuration-metadata.json
                    spring.factories
```

### 11.2.2、通过imports方式

新建一个配置类：
```java
@Data
public class Swagger2Properties {
    private boolean enableSwagger;
    private String author;
    private String doc;
}
public class BeanConfigRegister {
    @Bean
    @ConfigurationProperties(prefix = "swagger2.data")
    public Swagger2Properties getSwagger2() {
        return new Swagger2Properties();
    }
}
```
在META-INF目录下新增文件：`spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`，文件内容为：
```
com.swagger2.config.BeanConfigRegister
```

### 11.2.3、原理分析

# 12、重包装ServletHttpRequest

在拦截器中获取参数主要分两种：
- 接口使用 `@RequestParam` 接收参数；只需要在拦截器中通过request.getParameterMap() 来获得全部 Parameter 参数就可以了；
- 接口使用 `@RequestBody` 接收参数

当接口使用 `@RequestBody` 接收参数时，在拦截器中使用同样的方法获取参数，就会出现流已关闭的异常，也就导致参数读取失败了，这是因为 Spring 已经对 `@RequestBody` 提前进行处理，而 `HttpServletRequest` 获取输入流时仅允许读取一次，所以会报`java.io.IOException: Stream closed`

可以重新构建一个 ServletHttpRequest

（1）定义一个 HttpContextUtils
```java
// 主要读取 body 和 参数的
@Slf4j
public class HttpContextUtils {
    /**
     * 获取 query 参数
     */
    public static Map<String, String> getParameterMapAll(HttpServletRequest request) {
        Enumeration<String> parameters = request.getParameterNames();
        Map<String, String> params = new HashMap<>();
        while (parameters.hasMoreElements()) {
            String parameter = parameters.nextElement();
            String value = request.getParameter(parameter);
            params.put(parameter, value);
        }
        return params;
    }

    /**
     * 获取 body
     */
    public static String getBodyString(ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.error("get body str error:", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("close stream error: {}", e.toString());
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("close stream error: {}", e.toString());
                }
            }
        }
        return sb.toString();
    }
}
```
（2）定义一个 RequestWrapper，包装 HttpServletRequestWrapper
```java
public class RequestWrapper extends HttpServletRequestWrapper {
    private String body;
    public RequestWrapper(HttpServletRequest request) {
        super(request);
        body = getBody(request);
    }
    /**
     * 获取请求体
     */
    private String getBody(HttpServletRequest request) {
        return HttpContextUtils.getBodyString(request);
    }
    public String getBody() {
        return body;
    }
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
    @Override
    public ServletInputStream getInputStream() throws IOException {
        // 创建字节数组输入流
        final ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }
            @Override
            public boolean isReady() {
                return false;
            }
            @Override
            public void setReadListener(ReadListener readListener) {
            }
            @Override
            public int read() throws IOException {
                return arrayInputStream.read();
            }
        };
    }
}
```
（3）定义Filter，重新包装一个ServletHttpRequest
```java
@Component
@WebFilter(filterName = "HttpServletRequestFilter", urlPatterns = "/")
@Order(99)
public class RequestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        if(request instanceof HttpServletRequest) {
            requestWrapper = new RequestWrapper((HttpServletRequest) request);
        }
        // 获取请求中的流，将取出来的字符串，再次转换成流，然后把它放入到新 request对象中
        // 在chain.doFiler方法中传递新的request对象
        if(null == requestWrapper) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(requestWrapper, response);
        }
    }
}
```
**如何使用：**
```java
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request instanceof RequestWrapper) {
            RequestWrapper wrapper = (RequestWrapper) request;
            String body = wrapper.getBody();
            System.out.println(body);
        }
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
```

# 13、Spring异常处理

## 13.1、业务中发生的异常

针对业务中可能发生的异常，可以按照如下方式进行处理：
```java
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 参数类型不匹配是发生的异常
     */
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> methodArgumentTypeMismatchException(HttpServletRequest request, MethodArgumentTypeMismatchException e) {
        log.error("参数类型异常", e);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", "参数类型出错了");
        responseMap.put("code", HttpStatus.BAD_REQUEST.toString());
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }

    /**
     * 参数转换时发生的异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> httpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException e) {
        log.error("参数异常", e);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", "请求出错了");
        responseMap.put("code", HttpStatus.BAD_REQUEST.toString());
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }

    /**
     * 参数异常处理
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> constraintException(HttpServletRequest request, ConstraintViolationException e) {
        log.error("参数校验异常", e);
        Map<String, Object> responseMap = new HashMap<>();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String message = "";
        for (ConstraintViolation<?> violation : violations) {
            message = violation.getMessage();
        }
        responseMap.put("message", message);
        responseMap.put("code", HttpStatus.BAD_REQUEST.toString());
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }

    /**
     * 所有异常处理
     */
    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<Map<String, Object>> throwableHandler(HttpServletRequest request, Throwable e) {
        log.error("未知服务异常", e);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", "未知异常");
        responseMap.put("code", HttpStatus.INTERNAL_SERVER_ERROR.toString());
        return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

## 13.2、自定义请求体和响应体

可以参考上面的：[8、自定义请求体和响应体](#8自定义请求体和响应体)

## 13.3、其他异常

- [Spring优雅的处理404](https://www.cnblogs.com/54chensongxia/p/14007696.html)

### 13.3.1、重写BasicErrorController实现异常处理

比如`404`等异常信息，需要针对这类异常，从上面链接中的文章我们知道，如果是异常处理，其会调用连接：`/error`，该接口对应Controller：`BasicErrorController`，那么可以定制自己的错误跳转连接：
```yaml
server:
  error:
    path: /customError
```
定义自己的Controller来处理数据：
```java
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CustomErrorController extends BasicErrorController {

    public CustomErrorController(ServerProperties serverProperties) {
        super(new DefaultErrorAttributes(), serverProperties.getError());
    }

    /**
     * 覆盖默认的JSON响应
     */
    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        Map<String, Object> responseMap = new HashMap<>(16);
        Map<String, Object> sourceDataMap = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        responseMap.put("code", sourceDataMap.get("status"));
        responseMap.put("msg", sourceDataMap.get("error"));
        return new ResponseEntity<>(responseMap, status);
    }

    /**
     * 覆盖默认的HTML响应
     */
    @Override
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        //请求的状态
        HttpStatus status = getStatus(request);
        response.setStatus(getStatus(request).value());
        Map<String, Object> model = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        // 指定自定义的视图
        return (modelAndView == null ? new ModelAndView("error", model) : modelAndView);
    }
}
```

### 13.3.2、404异常处理

处理使用上面的处理方式外，也可以使用 ExceptionHandler 来处理404相关异常，但是需要做相应的配置：

（1）定义ExceptionHandler
```java
/**
 * 404异常处理
 */
@ExceptionHandler(value = NoHandlerFoundException.class)
public ResponseEntity<Map<String, Object>> exception404(HttpServletRequest request, NoHandlerFoundException e) {
    log.error("{} 未知路径", request.getRequestURI());
    log.error("404异常了", e);
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("message", "找不到路径");
    responseMap.put("code", HttpStatus.NOT_FOUND.toString());
    return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
}
```
（2）增加如下配置：
```yaml
spring:
  mvc:
    throw-exception-if-no-handler-found: true
```
（3）启动类增加注解：`@EnableWebMvc`
```java
@SpringBootApplication
@EnableWebMvc
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
```

# 14、SpringBoot配置过滤器

在Spring中，Filter默认实现是：[OncePerRequestFilter](https://www.cnblogs.com/xfeiyun/p/15673721.html)

在 Spring Boot 中配置 `Filter` 有几种常见的方式，主要包括通过 `@Component` 注解、使用 `FilterRegistrationBean`、以及在传统的 `web.xml` 中配置。下面详细介绍这几种方式：

## 14.1、过滤器配置

### 14.1.1、使用 `@Component` 注解

这是 Spring Boot 中最简单的配置 `Filter` 的方式。你只需实现 `javax.servlet.Filter` 接口，并用 `@Component` 注解标注过滤器类，Spring Boot 会自动注册这个过滤器。
```java
@Component
public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化逻辑
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 过滤器逻辑
        System.out.println("Request intercepted by MyFilter");
        // 继续过滤链
        chain.doFilter(request, response);
    }
    @Override
    public void destroy() {
        // 销毁逻辑
    }
}
```
> **注意**：默认情况下，`@Component` 注解注册的过滤器会拦截所有的请求 (`/*`)，并且按照默认顺序执行。

### 14.1.2、使用 `FilterRegistrationBean` 配置过滤器

如果需要更灵活的配置，比如指定 URL 路径、设置执行顺序、或者决定是否应用于异步请求，可以使用 `FilterRegistrationBean`。
```java
@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<MyFilter> filterRegistrationBean() {
        FilterRegistrationBean<MyFilter> registrationBean = new FilterRegistrationBean<>(new MyFilter());
        // 设置 URL 路径匹配
        registrationBean.addUrlPatterns("/api/*");        
        // 设置过滤器的顺序，数字越小优先级越高
        registrationBean.setOrder(1);        
        // 也可以设置其他参数，如是否支持异步请求
        registrationBean.setAsyncSupported(true);        
        return registrationBean;
    }
}
```

### 14.1.3、使用 `@WebFilter` 和 `@ServletComponentScan`

另一种配置方式是使用标准的 Servlet 注解 `@WebFilter`，但在 Spring Boot 中需要在主应用类上添加 `@ServletComponentScan` 注解来扫描这些过滤器。
```java
@WebFilter(urlPatterns = "/api/*")
public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("Request intercepted by MyFilter");
        chain.doFilter(request, response);
    }
    @Override
    public void destroy() {
    }
}
```
主类需要加上 `@ServletComponentScan` 注解：
```java
@SpringBootApplication
@ServletComponentScan
public class MySpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(MySpringBootApplication.class, args);
    }
}
```

### 14.1.4、在 `web.xml` 中配置过滤器

虽然 Spring Boot 通常不使用 `web.xml`，但如果你有特定的需求，仍然可以通过传统的方式在 `src/main/webapp/WEB-INF/web.xml` 中配置过滤器。
```xml
<filter>
    <filter-name>myFilter</filter-name>
    <filter-class>com.example.MyFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>myFilter</filter-name>
    <url-pattern>/api/*</url-pattern>
</filter-mapping>
```

### 14.1.5、总结

- **@Component**：简单快速，用于默认拦截所有请求。
- **FilterRegistrationBean**：灵活控制过滤器的配置，包括 URL 路径、执行顺序、异步支持等。
- **@WebFilter + @ServletComponentScan**：标准的 Servlet 注解方式，需要启用 `@ServletComponentScan`。
- **web.xml**：传统方式，不常用，但兼容需要使用传统配置的场景。

## 14.2、DispatcherType配置

```java
public enum DispatcherType {
	// 表示过滤器将应用于通过 RequestDispatcher.forward() 方法进行的请求转发
	FORWARD,
    // 表示过滤器将应用于通过 RequestDispatcher.include() 方法进行的请求包含
	INCLUDE,
    // 表示过滤器将应用于普通的客户端请求
	REQUEST,
    // 表示过滤器将应用于从 AsyncContext 派发的异步请求
	ASYNC,
    // 表示过滤器将应用于错误处理过程中
	ERROR

}
```
如果实现自 Filter，默认的 DispacherType 是 REQUEST ，SpringBoot中源码如下：
```java
// org.springframework.boot.web.servlet.AbstractFilterRegistrationBean#configure
protected void configure(FilterRegistration.Dynamic registration) {
    ...
    EnumSet<DispatcherType> dispatcherTypes = this.dispatcherTypes;
    if (dispatcherTypes == null) {
        T filter = getFilter();
        // 如果是 OncePerRequestFilter 的实现类，则是所有 DispatcherType
        if (ClassUtils.isPresent("org.springframework.web.filter.OncePerRequestFilter", filter.getClass().getClassLoader()) && filter instanceof OncePerRequestFilter) {
            dispatcherTypes = EnumSet.allOf(DispatcherType.class);
        } else {
            // 否则就是 REQUEST
            dispatcherTypes = EnumSet.of(DispatcherType.REQUEST);
        }
    }
    ...
}
```
在Servlet的API中也有体现：如果 dispatcherTypes 为 null，则 REQUEST 为默认值
```java
// javax.servlet.FilterRegistration#addMappingForServletNames
/**
 * @param dispatcherTypes the dispatcher types of the filter mapping, or null if the default <tt>DispatcherType.REQUEST</tt> is to be used
 */
public void addMappingForServletNames(EnumSet<DispatcherType> dispatcherTypes, boolean isMatchAfter,  String... servletNames);
```
在WebFilter注解中也有体现：
```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebFilter {
    ...
    DispatcherType[] dispatcherTypes() default {DispatcherType.REQUEST};
    ...
}
```

如何设置DispatcherType：

### 14.2.1、`web.xml`

```xml
<filter-mapping>
    <filter-name>myFilter</filter-name>
    <url-pattern>/*</url-pattern>
    <dispatcher>REQUEST</dispatcher>  <!-- 过滤器应用于普通请求 -->
    <dispatcher>FORWARD</dispatcher>  <!-- 过滤器应用于请求转发 -->
    <dispatcher>ERROR</dispatcher>    <!-- 过滤器应用于错误处理 -->
</filter-mapping>
```
> 说明：这里需要 servlet2.5版本以上：
```xml
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         version="3.0">
<web-app>
```

### 14.2.2、`@WbeFilter`

```java
@WebFilter(
        urlPatterns = "/*",
        dispatcherTypes = {DispatcherType.FORWARD, DispatcherType.REQUEST}
)
public class ComponentFilter implements Filter {}
```

### 14.2.3、代码处理

前面提到可以使用 `FilterRegistrationBean` 注册 Filter，当然 FilterRegistrationBean 也可以设置 DispatcherType
```java
@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<MyFilter> filterRegistrationBean() {
        FilterRegistrationBean<MyFilter> registrationBean = new FilterRegistrationBean<>(new MyFilter());
        ...
        // javax.servlet.DispatcherType: 设置所有 DispatcherType
        registrationBean.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
        return registrationBean;
    }
}
```

### 14.2.4、OncePerRequestFilter

上面三种都是针对实现 Filter 的处理方法， OncePerRequestFilter 是 Spring 中关于 Filter 的默认抽象实现，建议：若是在Spring环境下使用Filter的话，还是继承OncePerRequestFilter吧，而不是直接实现Filter接口，这是一个比较稳妥的选择：

```java
public abstract class OncePerRequestFilter extends GenericFilterBean {
    // 如果要实现过滤器，只需要继承当前类，并实现该方法即可
    protected abstract void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException;
}
```
比如：
```java
@Component("helloFilter")
public class HelloFilter extends OncePerRequestFilter {

    @Override
    protected void initFilterBean() throws ServletException {
        System.out.println("Filter初始化...");
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        FilterConfig filterConfig = super.getFilterConfig();
        ServletContext servletContext = super.getServletContext();
        Environment environment = super.getEnvironment();

        filterChain.doFilter(request, response);
    }
}
```