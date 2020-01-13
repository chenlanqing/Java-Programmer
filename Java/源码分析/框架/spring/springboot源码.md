<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
目录

- [一、SpringBoot启动配置原理](#%E4%B8%80springboot%E5%90%AF%E5%8A%A8%E9%85%8D%E7%BD%AE%E5%8E%9F%E7%90%86)
  - [1、注解](#1%E6%B3%A8%E8%A7%A3)
  - [2、启动流程](#2%E5%90%AF%E5%8A%A8%E6%B5%81%E7%A8%8B)
    - [2.1、启动流程分析](#21%E5%90%AF%E5%8A%A8%E6%B5%81%E7%A8%8B%E5%88%86%E6%9E%90)
    - [2.2、SpringFactoriesLoader](#22springfactoriesloader)
  - [3、系统初始化器：ApplicationContextInitializer](#3%E7%B3%BB%E7%BB%9F%E5%88%9D%E5%A7%8B%E5%8C%96%E5%99%A8applicationcontextinitializer)
    - [3.1、自定义初始化器的三种方式](#31%E8%87%AA%E5%AE%9A%E4%B9%89%E5%88%9D%E5%A7%8B%E5%8C%96%E5%99%A8%E7%9A%84%E4%B8%89%E7%A7%8D%E6%96%B9%E5%BC%8F)
    - [3.2、initializer方法执行时机](#32initializer%E6%96%B9%E6%B3%95%E6%89%A7%E8%A1%8C%E6%97%B6%E6%9C%BA)
  - [4、监听器](#4%E7%9B%91%E5%90%AC%E5%99%A8)
    - [4.1、监听器设计模式](#41%E7%9B%91%E5%90%AC%E5%99%A8%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F)
    - [4.2、Spring监听器实现](#42spring%E7%9B%91%E5%90%AC%E5%99%A8%E5%AE%9E%E7%8E%B0)
    - [4.3、监听器事件触发机制](#43%E7%9B%91%E5%90%AC%E5%99%A8%E4%BA%8B%E4%BB%B6%E8%A7%A6%E5%8F%91%E6%9C%BA%E5%88%B6)
    - [4.4、自定义监听器](#44%E8%87%AA%E5%AE%9A%E4%B9%89%E7%9B%91%E5%90%AC%E5%99%A8)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


# 一、SpringBoot启动配置原理

## 1、注解
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = {
        @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
        @Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {
...
}
```
- `@SpringBootApplication` = `(默认属性)@Configuration + @EnableAutoConfiguration + @ComponentScan`。如果在启动类不配置`@SpringBootApplication`，也可以配置这三个注解，通用能够启动；
- `@Configuration`：JavaConfig形式的Spring Ioc容器的配置类；
- `@ComponentScan`：@ComponentScan的功能其实就是自动扫描并加载符合条件的组件（比如@Component和@Repository等）或者bean定义，最终将这些bean定义加载到IOC容器中
- `@EnableAutoConfiguration`：@EnableAutoConfiguration也是借助@Import的帮助，将所有符合自动配置条件的bean定义加载到IOC容器，仅此而已！

    @EnableAutoConfiguration会根据类路径中的jar依赖为项目进行自动配置，如：添加了spring-boot-starter-web依赖，会自动添加Tomcat和Spring MVC的依赖，Spring Boot会对Tomcat和Spring MVC进行自动配置

	借助于Spring框架原有的一个工具类：SpringFactoriesLoader的支持，SpringFactoriesLoader属于Spring框架私有的一种扩展方案，其主要功能就是从指定的配置文件META-INF/spring.factories加载配置

	从classpath中搜寻所有的`META-INF/spring.factories`配置文件，并将其中`org.springframework.boot.autoconfigure.EnableutoConfiguration`对应的配置项通过反射（Java Refletion）实例化为对应的标注了@Configuration的JavaConfig形式的IOC容器配置类，然后汇总为一个并加载到IOC容器。

	配置在`META-INF/spring.factories`：**ApplicationContextInitializer**、**SpringApplicationRunListener**
	
	只需要放在ioc容器中：**ApplicationRunner**、**CommandLineRunner**

## 2、启动流程

### 2.1、启动流程分析

![](image/SpringBoot启动过程.png)

- （1）创建SpringApplication对象，

	SpringBoot1.5版本调用initialize(sources)方法
	```java
	private void initialize(Object[] sources) {
		//保存主配置类
		if (sources != null && sources.length > 0) {
			this.sources.addAll(Arrays.asList(sources));
		}
		//判断当前是否一个web应用
		this.webEnvironment = deduceWebEnvironment();
		//从类路径下找到META-INF/spring.factories配置的所有ApplicationContextInitializer；然后保存起来
		setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
		//从类路径下找到ETA-INF/spring.factories配置的所有ApplicationListener
		setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
		//从多个配置类中找到有main方法的主配置类
		this.mainApplicationClass = deduceMainApplicationClass();
	}
	```
	SpringBoot2.x版本是直接在SpringApplication构造方法中：
	```java
	public SpringApplication(Class<?>... primarySources) {
		this(null, primarySources);
	}

	public SpringApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
		this.resourceLoader = resourceLoader;
		Assert.notNull(primarySources, "PrimarySources must not be null");
		this.primarySources = new LinkedHashSet<>(Arrays.asList(primarySources));
		this.webApplicationType = deduceWebApplicationType();
        //从类路径下找到META-INF/spring.factories配置的所有 ApplicationContextInitializer 然后保存起来
		setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
        //从类路径下找到META-INF/spring.factories配置的所有 ApplicationListener
		setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
		this.mainApplicationClass = deduceMainApplicationClass();
	}
	```
- （2）运行run方法
	```java
	public ConfigurableApplicationContext run(String... args) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		ConfigurableApplicationContext context = null;
		FailureAnalyzers analyzers = null;
		configureHeadlessProperty();
			
		//获取SpringApplicationRunListeners；从类路径下META-INF/spring.factories
		SpringApplicationRunListeners listeners = getRunListeners(args);
			//回调所有的获取SpringApplicationRunListener.starting()方法
		listeners.starting();
		try {
			//封装命令行参数
			ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
			//准备环境
			ConfigurableEnvironment environment = prepareEnvironment(listeners,applicationArguments);
			//创建环境完成后回调SpringApplicationRunListener.environmentPrepared()；表示环境准备完成
			
			Banner printedBanner = printBanner(environment);
			
			//创建ApplicationContext；决定创建web的ioc还是普通的ioc
			context = createApplicationContext();
			
			analyzers = new FailureAnalyzers(context);
			//准备上下文环境;将environment保存到ioc中；而且applyInitializers()；
			//applyInitializers()：回调之前保存的所有的ApplicationContextInitializer的initialize方法
			//回调所有的SpringApplicationRunListener的contextPrepared()；
			prepareContext(context, environment, listeners, applicationArguments,printedBanner);
			//prepareContext运行完成以后回调所有的SpringApplicationRunListener的contextLoaded（）；
			
			//刷新容器；ioc容器初始化（如果是web应用还会创建嵌入式的Tomcat）；Spring注解版
			//扫描，创建，加载所有组件的地方；（配置类，组件，自动配置）
			refreshContext(context);
			//从ioc容器中获取所有的ApplicationRunner和CommandLineRunner进行回调
			//ApplicationRunner先回调，CommandLineRunner再回调
			afterRefresh(context, applicationArguments);
			//所有的SpringApplicationRunListener回调finished方法
			listeners.finished(context, null);
			stopWatch.stop();
			if (this.logStartupInfo) {
				new StartupInfoLogger(this.mainApplicationClass)
					.logStarted(getApplicationLog(), stopWatch);
			}
			//整个SpringBoot应用启动完成以后返回启动的ioc容器；
			return context;
		}
		catch (Throwable ex) {
			handleRunFailure(context, listeners, analyzers, ex);
			throw new IllegalStateException(ex);
		}
	}
	```

**大体流程：**
- 1） 如果我们使用的是SpringApplication的静态run方法，那么，这个方法里面首先要创建一个SpringApplication对象实例，然后调用这个创建好的SpringApplication的实例方法。在SpringApplication实例初始化的时候，它会提前做几件事情：
	- 根据classpath里面是否存在某个特征类（org.springframework.web.context.ConfigurableWebApplicationContext）来决定是否应该创建一个为Web应用使用的ApplicationContext类型。
	- 使用SpringFactoriesLoader在应用的classpath中查找并加载所有可用的ApplicationContextInitializer。
	- 使用SpringFactoriesLoader在应用的classpath中查找并加载所有可用的ApplicationListener。
	- 推断并设置main方法的定义类。
- 2） SpringApplication实例初始化完成并且完成设置后，就开始执行run方法的逻辑了，方法执行伊始，首先遍历执行所有通过SpringFactoriesLoader可以查找到并加载的SpringApplicationRunListener。调用它们的started()方法，告诉这些SpringApplicationRunListener，“嘿，SpringBoot应用要开始执行咯！”。
- 3） 创建并配置当前Spring Boot应用将要使用的Environment（包括配置要使用的PropertySource以及Profile）。
- 4） 遍历调用所有SpringApplicationRunListener的environmentPrepared()的方法，告诉他们：“当前SpringBoot应用使用的Environment准备好了咯！”。
- 5） 如果SpringApplication的showBanner属性被设置为true，则打印banner。
- 6） 根据用户是否明确设置了applicationContextClass类型以及初始化阶段的推断结果，决定该为当前SpringBoot应用创建什么类型的ApplicationContext并创建完成，然后根据条件决定是否添加ShutdownHook，决定是否使用自定义的BeanNameGenerator，决定是否使用自定义的ResourceLoader，当然，最重要的，将之前准备好的Environment设置给创建好的ApplicationContext使用。
- 7） ApplicationContext创建好之后，SpringApplication会再次借助Spring-FactoriesLoader，查找并加载classpath中所有可用的ApplicationContext-Initializer，然后遍历调用这些ApplicationContextInitializer的initialize（applicationContext）方法来对已经创建好的ApplicationContext进行进一步的处理。
- 8） 遍历调用所有SpringApplicationRunListener的contextPrepared()方法。
- 9） 最核心的一步，将之前通过@EnableAutoConfiguration获取的所有配置以及其他形式的IOC容器配置加载到已经准备完毕的ApplicationContext。
- 10） 遍历调用所有SpringApplicationRunListener的contextLoaded()方法。
- 11） 调用ApplicationContext的refresh()方法，完成IOC容器可用的最后一道工序。
- 12） 查找当前ApplicationContext中是否注册有CommandLineRunner，如果有，则遍历执行它们。
- 13） 正常情况下，遍历执行SpringApplicationRunListener的finished()方法、（如果整个过程出现异常，则依然调用所有SpringApplicationRunListener的finished()方法，只不过这种情况下会将异常信息一并传入处理）

**总结：**

- SpringApplication.run(主程序类)
	- new SpringApplication(主程序类)
		- 判断是否web应用
		- 加载并保存所有ApplicationContextInitializer(`META-INF/spring.factories`)， • 加载并保存所有ApplicationListener
		- 获取到主程序类
	- run()
		- 回调所有的SpringApplicationRunListener(`META-INF/spring.factories`)的starting
		- 获取ApplicationArguments
		- 准备环境&回调所有监听器( SpringApplicationRunListener )的environmentPrepared • 打印banner信息
		- 创建ioc容器对象
	- AnnotationConfigEmbeddedWebApplicationContext(web环境容器) – AnnotationConfigApplicationContext(普通环境容器)

- 准备环境
- 执行ApplicationContextInitializer. initialize()
- 监听器SpringApplicationRunListener回调contextPrepared – 加载主配置类定义信息
- 监听器SpringApplicationRunListener回调contextLoaded
	- 刷新启动IOC容器;
- 扫描加载所有容器中的组件
- 包括从`META-INF/spring.factories`中获取的所有EnableAutoConfiguration组件
	- 回调容器中所有的ApplicationRunner、CommandLineRunner的run方法 • 监听器SpringApplicationRunListener回调finished

*Spring Boot 总是遵循一个标准：容器中有我们自己配置的组件就用我们配置的，没有就用自动配 置默认注册进来的组件;*

### 2.2、SpringFactoriesLoader

**介绍：**
- 框架内部使用通用的工厂加载机制；
- 从classpath下多个jar包特定的位置读取文件并初始化类；
- 文件内容必须是kv形式，即properties形式；
- key是全限定名（抽象类|接口），value是实现类的全限定名，如果有多个，使用`,`分隔

**加载类流程**
```java
// SpringApplication类的方法
private <T> Collection<T> getSpringFactoriesInstances(Class<T> type, Class<?>[] parameterTypes, Object... args) {
    ClassLoader classLoader = getClassLoader();
    // Use names and ensure unique to protect against duplicates
    Set<String> names = new LinkedHashSet<>(SpringFactoriesLoader.loadFactoryNames(type, classLoader));
    List<T> instances = createSpringFactoriesInstances(type, parameterTypes, classLoader, args, names);
    AnnotationAwareOrderComparator.sort(instances);
    return instances;
}

// SpringFactoriesLoader的方法
public static List<String> loadFactoryNames(Class<?> factoryType, @Nullable ClassLoader classLoader) {
    String factoryTypeName = factoryType.getName();
    return loadSpringFactories(classLoader).getOrDefault(factoryTypeName, Collections.emptyList());
}
private static Map<String, List<String>> loadSpringFactories(@Nullable ClassLoader classLoader) {
    MultiValueMap<String, String> result = cache.get(classLoader);
    if (result != null) {
        return result;
    }
    try {
        Enumeration<URL> urls = (classLoader != null ?  classLoader.getResources(FACTORIES_RESOURCE_LOCATION) : ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
        // LinkedMultiValueMap 里面有个targetMap，是用LinkedHashMap来实现的，使用的是LinkedList来存储的value值
        result = new LinkedMultiValueMap<>();
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            UrlResource resource = new UrlResource(url);
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            for (Map.Entry<?, ?> entry : properties.entrySet()) {
                String factoryTypeName = ((String) entry.getKey()).trim();
                for (String factoryImplementationName : StringUtils.commaDelimitedListToStringArray((String) entry.getValue())) {
                    result.add(factoryTypeName, factoryImplementationName.trim());
                }
            }
        }
        cache.put(classLoader, result);
        return result;
    } .....
}
```

![](image/SpringFactoriesLoader.loadFactories.png)

## 3、系统初始化器：ApplicationContextInitializer

- 上下文沙墟即refresh方法前调用；
- 用来编码设置一些属性变量，通常用在web环境中；
- 可以通过`@Order`注解进行排序

### 3.1、自定义初始化器的三种方式

自定义初始化器的基本条件：实现接口`ApplicationContextInitializer`
```java
@Order(1)
public class FirstInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();
        Map<String, Object> map = new HashMap<>(1);
        map.put("first", "first");
        MapPropertySource source = new MapPropertySource("firstInitializer", map);
        propertySources.addLast(source);
        System.out.println("Run First Initializer...");
    }
}
```

#### 3.1.1、factories方式

- **添加方法：**

    在`resources`目录下新建目录文件：`META-INF/spring.factories`，配置的key为`org.springframework.context.ApplicationContextInitializer`，value为自定义初始化器的全类名路径
    ```
    org.springframework.context.ApplicationContextInitializer=com.blue.fish.web.initializer.FirstInitializer
    ```

- **实现原理：**

    其实通过SpringFactoriesLoader来加载`META-INF/spring.factories`里面的配置的，并通过Order进行排序处理

#### 3.1.2、在启动类中添加

- **添加方法：**

    在启动类中添加如下代码，替换`SpringApplication.run(SpringBootSourceApplication.class, args);`
    ```java
    @SpringBootApplication
    public class SpringBootSourceApplication {
        public static void main(String[] args) {
            SpringApplication application = new SpringApplication(SpringBootSourceApplication.class);
            application.addInitializers(new SecondFirstInitializer());
            application.run(args);
        }
    }
    ```

- **实现原理：** 

    调用`SpringApplication.addInitializers`方法，将其添加到初始化`SpringApplicatio`里面添加的`initializers`中，手动添加数据；

#### 3.1.3、在application.properties中添加配置

- **添加方法：**

添加如下配置：
```
context.initializer.classes=com.blue.fish.source.initializer.ThirdInitializer
```

- **实现原理：** 

    在`application.properties`中添加配置会被定义成环境变量被`DelegatingApplicationContextInitializer`发现并注册
    ```java
    private static final String PROPERTY_NAME = "context.initializer.classes";
	private int order = 0;
	@Override
	public void initialize(ConfigurableApplicationContext context) {
		ConfigurableEnvironment environment = context.getEnvironment();
        // 从环境变量中获取是否有配置
		List<Class<?>> initializerClasses = getInitializerClasses(environment);
		if (!initializerClasses.isEmpty()) {
			applyInitializerClasses(context, initializerClasses);
		}
	}
	private List<Class<?>> getInitializerClasses(ConfigurableEnvironment env) {
		String classNames = env.getProperty(PROPERTY_NAME);
		List<Class<?>> classes = new ArrayList<>();
		if (StringUtils.hasLength(classNames)) {
			for (String className : StringUtils.tokenizeToStringArray(classNames, ",")) {
				classes.add(getInitializerClass(className));
			}
		}
		return classes;
	}
    private void applyInitializerClasses(ConfigurableApplicationContext context, List<Class<?>> initializerClasses) {
		Class<?> contextClass = context.getClass();
		List<ApplicationContextInitializer<?>> initializers = new ArrayList<>();
        // 实例化在环境变量中配置的initializer类
		for (Class<?> initializerClass : initializerClasses) {
			initializers.add(instantiateInitializer(contextClass, initializerClass));
		}
        // 执行该变量
		applyInitializers(context, initializers);
	}
    // 循环执行该数据
    private void applyInitializers(ConfigurableApplicationContext context,List<ApplicationContextInitializer<?>> initializers) {
        // 会先将application.properties中配置的initializer进行排序
		initializers.sort(new AnnotationAwareOrderComparator());
		for (ApplicationContextInitializer initializer : initializers) {
			initializer.initialize(context);
		}
	}
    ```
    因为`DelegatingApplicationContextInitializer`其的Order=0，`application.properties`其定义的initializer又是由该类触发，所以说`application.properties`中定义的优先于其他方式定义的initializer先执行

#### 3.1.4、注意点

- 都要实现 `ApplicationContextInitializer`接口；
- `@Order`值越小越先执行；
- `application.properties`中定义的优先于其他方式；

### 3.2、initializer方法执行时机

![](image/applyInitializer.png)

调用链： SpringApplication.run ->  prepareContext（上下文准备） -> applyInitializers -> 遍历调用各个Initializer的initialize方法

## 4、监听器

### 4.1、监听器设计模式

**监听器模式四要素：**
- 事件
- 监听器
- 广播器
- 触发机制

**监听器原理**

![](image/监听器模式.png)

### 4.2、Spring监听器实现

#### 4.2.1、spring监听器具体实现

![](image/SpringApplicationEvent类图.png)

Spring中主要有7类事件
事件实现类 | 对应 SpringApplicationRunListener 方法 | 说明
---------|---------------------------------------|--------
ApplicationContextInitializedEvent| contextPrepared |ConfigurableApplicationContext准备完成，对应
ApplicationEnvironmentPreparedEvent|  environmentPrepared   |ConfigurableEnvironment准备完成
ApplicationPreparedEvent|  contextLoaded   |ConfigurableApplicationContext已装载，但是仍未启动
ApplicationReadyEvent|  running   |Spring应用正在运行
ApplicationStartingEvent|  starting   |Spring应用刚启动
ApplicationStartedEvent|  started   |ConfigurableApplicationContext 已启动，此时Spring Bean已经初始化完成
ApplicationFailedEvent|  failed   |Spring应用运行失败

#### 4.2.2、框架事件发送顺序

![](image/SpringApplicationEvent事件发生顺序.png)

#### 4.2.3、SpringApplicationRunListeners

获取SpringApplicationRunListeners：
```java
private SpringApplicationRunListeners getRunListeners(String[] args) {
    Class<?>[] types = new Class<?>[] { SpringApplication.class, String[].class };
    return new SpringApplicationRunListeners(logger, getSpringFactoriesInstances(SpringApplicationRunListener.class, types, this, args));
}
```
SpringApplicationRunListeners 是基于组合模式实现的，内部关联了`SpringApplicationRunListener`集合
```java
class SpringApplicationRunListeners {
	private final List<SpringApplicationRunListener> listeners;
	SpringApplicationRunListeners(Log log, Collection<? extends SpringApplicationRunListener> listeners) {
		this.log = log;
		this.listeners = new ArrayList<>(listeners);
	}
    ...
}
```

#### 4.2.4、SpringApplicationRunListener

Spring应用运行时监听器，其监听方法被 SpringApplicationRunListeners 遍历的执行，主要包含如下方法：
```
starting()
environmentPrepared(ConfigurableEnvironment environment)
contextPrepared(ConfigurableApplicationContext context)
contextLoaded(ConfigurableApplicationContext context)
started(ConfigurableApplicationContext context)
running(ConfigurableApplicationContext context)
failed(ConfigurableApplicationContext context, Throwable exception)
```
`SpringApplicationRunListener`的构造器参数必须依次为：`SpringApplication`和`String[]` 类型，其具体实现也是通过 SpringFactoriesLoader 加载的，其在 `META-INF/spring.factories` 具体的key值为：
```
# Run Listeners
org.springframework.boot.SpringApplicationRunListener=org.springframework.boot.context.event.EventPublishingRunListener
```
`EventPublishingRunListener`为SpringBoot唯一的内建实现，在其构造函数中，其会将根据`SpringApplication`已关联的`ApplicationListener`实例列表动态的添加到`SimpleApplicationEventMulticaster`对象中。`SimpleApplicationEventMulticaster`是实现自 `ApplicationEventMulticaster`接口，用于发布Spring应用事件（ApplicationEvent）。因此`EventPublishingRunListener`为SpringBoot的事件发布者角色；
```java
public EventPublishingRunListener(SpringApplication application, String[] args) {
    this.application = application;
    this.args = args;
    this.initialMulticaster = new SimpleApplicationEventMulticaster();
    for (ApplicationListener<?> listener : application.getListeners()) {
        this.initialMulticaster.addApplicationListener(listener);
    }
}
```

#### 4.2.5、Spring事件/监听器设计

[Spring事件监听器](Spring源码.md#五Spring事件)

### 4.3、监听器事件触发机制

#### 4.3.1、监听器注册

注册监听的方式同系统初始化方法类似的

#### 4.3.2、获取感兴趣的监听器列表

SpringApplication获取到 SpringApplicationRunListeners 后，以其执行starting为例，获取其感兴趣的监听器：
```java
// SpringApplication
public ConfigurableApplicationContext run(String... args) {
    ...
    //  获取到SpringApplicationRunListeners
    SpringApplicationRunListeners listeners = getRunListeners(args);
    listeners.starting();
    ...
}
// SpringApplicationRunListeners
void starting() {
    // SpringApplicationRunListener 具体实现类是：EventPublishingRunListener
    for (SpringApplicationRunListener listener : this.listeners) {
        listener.starting();
    }
}
// EventPublishingRunListener implement SpringApplicationRunListener
private final SimpleApplicationEventMulticaster initialMulticaster;
public void starting() {
    this.initialMulticaster.multicastEvent(new ApplicationStartingEvent(this.application, this.args));
}
// SimpleApplicationEventMulticaster
public void multicastEvent(ApplicationEvent event) {
    multicastEvent(event, resolveDefaultEventType(event));
}
public void multicastEvent(final ApplicationEvent event, @Nullable ResolvableType eventType) {
    ResolvableType type = (eventType != null ? eventType : resolveDefaultEventType(event));
    Executor executor = getTaskExecutor();
    // 根据事件类型获取到所有的当前事件上注册的监听器：getApplicationListeners
    for (ApplicationListener<?> listener : getApplicationListeners(event, type)) {
        if (executor != null) {
            executor.execute(() -> invokeListener(listener, event));
        }
        else {
            invokeListener(listener, event);
        }
    }
}
// AbstractApplicationEventMulticaster 是 SimpleApplicationEventMulticaster 父类
```

具体流程图：

![](image/SpringBoot获取监听器列表.png)

上面流程图中supportsEventType具体实现：

![](image/SpringBoot事件通用触发机制.png)

#### 4.3.3、事件触发条件

上面获取到监听器列表后，会触发事件：
```java
protected void invokeListener(ApplicationListener<?> listener, ApplicationEvent event) {
    ErrorHandler errorHandler = getErrorHandler();
    if (errorHandler != null) {
        try {
            doInvokeListener(listener, event);
        }
        ...
    }
    else {
        doInvokeListener(listener, event);
    }
}
private void doInvokeListener(ApplicationListener listener, ApplicationEvent event) {
    try {
        listener.onApplicationEvent(event);
    }
    ...
}
```

### 4.4、自定义监听器

基本条件：实现`ApplicationListener`

#### 4.4.1、factories方式


- **添加方法：**

    在`resources`目录下新建目录文件：`META-INF/spring.factories`，配置的key为`org.springframework.context.ApplicationListener`，value为自定义初始化器的全类名路径
    ```
    org.springframework.context.ApplicationListener=com.blue.fish.source.listener.FirstListener
    ```

- **实现原理：**

    其实通过SpringFactoriesLoader来加载`META-INF/spring.factories`里面的配置的，并通过Order进行排序处理

#### 4.4.2、在启动类中添加

- **添加方法：**

    在启动类中添加如下代码，替换`SpringApplication.run(SpringBootSourceApplication.class, args);`
    ```java
    @SpringBootApplication
    public class SpringBootSourceApplication {
        public static void main(String[] args) {
            SpringApplication application = new SpringApplication(SpringBootSourceApplication.class);
            application.addListeners(new SecondListener());
            application.run(args);
        }
    }
    ```

- **实现原理：** 

    调用`SpringApplication.addListeners`方法，将其添加到初始化`SpringApplication`里面添加的`listeners`中，手动添加数据；

#### 4.4.3、在application.properties中添加配置

- **添加方法：**

添加如下配置：
```
context.listener.classes=com.blue.fish.source.listener.ThirdListener
```

- **实现原理：** 

    在`application.properties`中添加配置会被定义成环境变量被`DelegatingApplicationListener`发现并注册。

    因为`DelegatingApplicationListener`其的Order=0，`application.properties`其定义的listener又是由该类触发，所以说`application.properties`中定义的优先于其他方式定义的listener先执行

#### 4.4.4、实现SmartApplicationListener接口

```java
public class FourthListener implements SmartApplicationListener {
    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        // 注册自己关注的事件
        return ApplicationStartedEvent.class.isAssignableFrom(eventType)
                || ApplicationPreparedEvent.class.isAssignableFrom(eventType);
    }
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        // 根据上述的supportsEventType关注的事件执行相应的代码；
        System.out.println("Fourth SmartApplicationListener");
    }
}
```
然后根据上述三种方式添加到框架中；

#### 4.4.5、总结

- 实现ApplicationListener接口只针对单一事件监听；
- 实现 SmartApplicationListener 接口可以针对多种事件监听；
- Order值越小越先执行；
- `application.properties`中定义的优先于其他方式；
