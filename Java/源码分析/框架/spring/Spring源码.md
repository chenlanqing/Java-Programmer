<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
目录

- [一、IOC](#%E4%B8%80ioc)
  - [1、IOC的生命周期](#1ioc%E7%9A%84%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F)
  - [2、IOC生命周期](#2ioc%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F)
    - [2.1、BeanFactory Bean生命周期-面向Spring本身](#21beanfactory-bean%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F-%E9%9D%A2%E5%90%91spring%E6%9C%AC%E8%BA%AB)
    - [2.2、BeanFactory Bean生命周期-面向Spring本身](#22beanfactory-bean%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F-%E9%9D%A2%E5%90%91spring%E6%9C%AC%E8%BA%AB)
  - [3、IOC源码体系](#3ioc%E6%BA%90%E7%A0%81%E4%BD%93%E7%B3%BB)
  - [4、IOC容器的启动过程](#4ioc%E5%AE%B9%E5%99%A8%E7%9A%84%E5%90%AF%E5%8A%A8%E8%BF%87%E7%A8%8B)
  - [5、Bean加载过程](#5bean%E5%8A%A0%E8%BD%BD%E8%BF%87%E7%A8%8B)
  - [6、refresh方法源码](#6refresh%E6%96%B9%E6%B3%95%E6%BA%90%E7%A0%81)
    - [6.1、prepareRefresh()：刷新前预处理](#61preparerefresh%E5%88%B7%E6%96%B0%E5%89%8D%E9%A2%84%E5%A4%84%E7%90%86)
    - [6.2、obtainFreshBeanFactory()：获取BeanFactory](#62obtainfreshbeanfactory%E8%8E%B7%E5%8F%96beanfactory)
    - [6.3、prepareBeanFactory(beanFactory)](#63preparebeanfactorybeanfactory)
    - [6.4、postProcessBeanFactory(beanFactory);](#64postprocessbeanfactorybeanfactory)
    - [6.5、invokeBeanFactoryPostProcessors(beanFactory);](#65invokebeanfactorypostprocessorsbeanfactory)
    - [6.6、registerBeanPostProcessors(beanFactory);](#66registerbeanpostprocessorsbeanfactory)
    - [6.7、initMessageSource();](#67initmessagesource)
    - [6.8、initApplicationEventMulticaster();](#68initapplicationeventmulticaster)
    - [6.9、onRefresh()](#69onrefresh)
    - [6.10、registerListeners();](#610registerlisteners)
    - [6.11、finishBeanFactoryInitialization(beanFactory)](#611finishbeanfactoryinitializationbeanfactory)
    - [6.12、finishRefresh();](#612finishrefresh)
- [二、AOP](#%E4%BA%8Caop)
- [三、spring事务](#%E4%B8%89spring%E4%BA%8B%E5%8A%A1)
  - [1、Spring事务管理方式](#1spring%E4%BA%8B%E5%8A%A1%E7%AE%A1%E7%90%86%E6%96%B9%E5%BC%8F)
  - [2、Spring的事务特性](#2spring%E7%9A%84%E4%BA%8B%E5%8A%A1%E7%89%B9%E6%80%A7)
    - [2.1、Spring的事务管理策略](#21spring%E7%9A%84%E4%BA%8B%E5%8A%A1%E7%AE%A1%E7%90%86%E7%AD%96%E7%95%A5)
    - [2.2、Spring的事务隔离级别](#22spring%E7%9A%84%E4%BA%8B%E5%8A%A1%E9%9A%94%E7%A6%BB%E7%BA%A7%E5%88%AB)
    - [2.3、Spring事务传播行为](#23spring%E4%BA%8B%E5%8A%A1%E4%BC%A0%E6%92%AD%E8%A1%8C%E4%B8%BA)
    - [2.4、事务超时时间](#24%E4%BA%8B%E5%8A%A1%E8%B6%85%E6%97%B6%E6%97%B6%E9%97%B4)
    - [2.5、事务回滚规则](#25%E4%BA%8B%E5%8A%A1%E5%9B%9E%E6%BB%9A%E8%A7%84%E5%88%99)
  - [3、Spring事务实现原理](#3spring%E4%BA%8B%E5%8A%A1%E5%AE%9E%E7%8E%B0%E5%8E%9F%E7%90%86)
- [四、SpringFactoriesLoader](#%E5%9B%9Bspringfactoriesloader)
- [五、Spring事件](#%E4%BA%94spring%E4%BA%8B%E4%BB%B6)
  - [1、理解Spring事件、监听机制](#1%E7%90%86%E8%A7%A3spring%E4%BA%8B%E4%BB%B6%E7%9B%91%E5%90%AC%E6%9C%BA%E5%88%B6)
  - [2、Spring事件发布](#2spring%E4%BA%8B%E4%BB%B6%E5%8F%91%E5%B8%83)
    - [2.1、ApplicationEventMulticaster注册 ApplicationListener](#21applicationeventmulticaster%E6%B3%A8%E5%86%8C-applicationlistener)
- [参考资料](#%E5%8F%82%E8%80%83%E8%B5%84%E6%96%99)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# 一、IOC

## 1、IOC的生命周期

Spring的ioc容器功能非常强大，负责Spring的Bean的创建和管理等功能。`BeanFactory`和`ApplicationContext`是Spring两种很重要的容器，前者提供了最基本的依赖注入的支持，而后者在继承前者的基础进行了功能的拓展，例如增加了事件传播、资源访问和国际化的消息访问等功能；

## 2、IOC生命周期

### 2.1、BeanFactory Bean生命周期-面向Spring本身

![image](image/BeanFactory.png)

`BeanFactoty`容器中， Bean的生命周期如上图所示，与`ApplicationContext`相比，有如下几点不同：

- `BeanFactory`容器中，不会调用`ApplicationContextAware`接口的`setApplicationContext()`方法
- `BeanPostProcessor`接口的`postProcessBeforeInitialization`方法和`postProcessAfterInitialization`方法不会自动调用，必须自己通过代码手动注册
- `BeanFactory`容器启动的时候，不会去实例化所有bean，包括所有scope为singleton且非延迟加载的bean也是一样，而是在调用的时候去实例化

### 2.2、BeanFactory Bean生命周期-面向开发者

![image](image/ApplicationContext-Bean的生命周期.png)

面向开发者的，几乎大部分应用场景都是直接使用`ApplicationContex`t 而非底层的`BeanFactory`

- （1）Bean的实例化：
	* 首先容器启动后，会对`scope`为`singleton`且非懒加载的bean进行实例化;
	* 容器在内部实现的时候，采用`策略模式`来决定采用何种方式初始化bean实例.通常，可以通过反射或者CGLIB动态字节码生成来初始化相应的bean实例或者动态生成其子类默认情况下，容器内部采用 `CglibSubclassingInstantiationStrategy`。容器只要根据相应bean定义的`BeanDefinitio`n取得实例化信息，结合`CglibSubclassingInstantiationStrategy`以及不同的bean定义类型，就可以返回实例化完成的对象实例。但不是直接返回构造完成的对象实例，而是以BeanWrapper对构	造完成的对象实例进行包裹，返回相应的`BeanWrapper`实例，这个`BeanWrapper`的实现类`BeanWrapperImpl`是对某个bean进行包裹，然后对包裹后的bean进行操作，比如设置或获取bean的相应属性值；

- （2）设置对象属性：

	`BeanWrapper`继承了`PropertyAccessor`接口，可以以同一的方式对对象属性进行访问，同时又继承了`PropertyEditorRegistry`和`TypeConverter`接口，然后`BeanWrapper`就可以很方便地对bean注入属性了；

- （3）如果`Bean`实现了`BeanNameAware`接口，会回调该接口的`setBeanName()`方法，传入该bean的id，此时该Bean就获得了自己在配置文件中的id；

- （4）如果`Bean`实现了`BeanFactoryAware`接口，会回调该接口的`setBeanFactory()`方法，传入该Bean的BeanFactory，这样该Bean就获得了自己所在的BeanFactory

- （5）如果`Bean`实现了`ApplicationContextAware`接口，会回调该接口的`setApplicationContext()`方法，传入该Bean的`ApplicationContext`，这样该Bean就获得了自己所在的`ApplicationContext`

- （6）如果有一个`Bean`实现了`BeanPostProcessor`接口，并将该接口配置到配置文件中，则会调用该接口的`postProcessBeforeInitialization()`方法

- （7）如果`Bean`实现了`InitializingBean`接口，则会回调该接口的`afterPropertiesSet()`方法

- （8）如果`Bean`配置了`init-method`方法，则会执行`init-method`配置的方法；

- （9）如果有一个`Bean`实现了`BeanPostProcessor`接口，并将该接口配置到配置文件中，则会调用该接口的`postProcessAfterInitialization`方法；

- （10）经过步骤9之后，就可以正式使用该Bean了，对于scope为singleton的Bean，Spring IoC容器会缓存一份该Bean的实例，而对于scope为prototype的Bean，每次被调用都回new一个对象，而且生命周期也交给调用方管理了，不再是Spring容器进行管理了；

- （11）容器关闭后，如果`Bean`实现了`DisposableBean`接口，则会调用该接口的`destroy()`方法；

- （12）如果Bean配置了`destroy-method`方法，则会执行`destroy-method`配置的方法.至此，整个Bean生命周期结束；

## 3、IOC源码体系

- Resource 体系：`org.springframework.core.io.Resource`，对资源的抽象。它的每一个实现类都代表了一种资源的访问策略，如 ClassPathResource、RLResource、FileSystemResource 等；

- ResourceLoader 体系：有了资源，就应该有资源加载，Spring 利用 `org.springframework.core.io.ResourceLoader` 来进行统一资源加载

- BeanFactory 体系：`org.springframework.beans.factory.BeanFactory`，是一个非常纯粹的 bean 容器，它是 IoC 必备的数据结构，其中 BeanDefinition 是它的基本结构。BeanFactory 内部维护着一个BeanDefinition map ，并可根据 BeanDefinition 的描述进行 bean 的创建和管理；
    - BeanFactory 有三个直接子类 ListableBeanFactory、HierarchicalBeanFactory 和 AutowireCapableBeanFactory 。
    - DefaultListableBeanFactory 为最终默认实现，它实现了所有接口

- BeanDefinition 体系：`org.springframework.beans.factory.config.BeanDefinition`，用来描述 Spring 中的 Bean 对象；

- BeanDefinitionReader 体系：`org.springframework.beans.factory.support.BeanDefinitionReader` 的作用是读取 Spring 的配置文件的内容，并将其转换成 Ioc 容器内部的数据结构 ：BeanDefinition；

- ApplicationContext 体系：org.springframework.context.ApplicationContext ，Spring 容器，它叫做应用上下文。它继承 BeanFactory ，所以它是 BeanFactory 的扩展升级版，由于 ApplicationContext 的结构就决定了它与 BeanFactory 的不同，其主要区别有：
    - 继承 `org.springframework.context.MessageSource` 接口，提供国际化的标准访问策略。
    - 继承 `org.springframework.context.ApplicationEventPublisher` 接口，提供强大的事件机制。
    - 扩展 ResourceLoader ，可以用来加载多种 Resource ，可以灵活访问不同的资源。
    - 对 Web 应用的支持

## 4、IOC容器的启动过程

web环境下`Spring\SpringMVC`容器启动过程

- （1）对于一个web应用，其部署在web容器中，web容器提供一个全局的上下文环境，即`ServletContext`，其为后面的`SpringIOC`容器提宿主环境；

- （2）`web.xml`中配置`ContextLoaderListener`在web容器启动时，会触发容器初始化事件，`ContextLoaderListener`会监听到这个事件，其`contextInitialized()`方法被调用，在这个方法中，spring会初始化一个启动上下文，这个上下文被称为`根上下文`，即`WebApplicationContext`。其实际实现类是`XmlWebApplicationContext`。这个就是Spring的IOC容器。其对应的Bean定义的配置由web.xml中的context-param标签指定.在这个IoC容器初始化完毕后，spring容器以`WebApplicationContext.ROOTWEBAPPLICATIONCONTEXTATTRIBUTE`为属性Key，将其存储到`ServletContext`中，便于获取；

- （3）`ContextLoaderListener`监听器初始化完毕后，始初始化`web.xml`中配置的`Servlet`，可以有多个。以最常见的`DispatcherServlet`为例（Spring MVC，这个servlet实际上是一个标准的前端控制器，用以转发、匹配、处理每个servlet请求。`DispatcherServlet上下文`在初始化的时候会建立自己的IoC上下文容器，用以持有spring mvc相关的bean，这个servlet自己持有的上下文默认实现类也是`XmlWebApplicationContext`。在建立`DispatcherServlet`自己的IoC上下文时，会利用`WebApplicationContext.ROOTWEBAPPLICATIONCONTEXTATTRIBUTE`先从`ServletContext`中获取之前的根上下文（即`WebApplicationContext`）作为自己上下文的parent上下文）即第2步中初始化的`XmlWebApplicationContext`作为自己的父容器）.有了这个parent上下文之后，再初始化自己持有的上下文（这个`DispatcherServlet`初始化自己上下文的工作在其`initStrategies`方法中可以看到，大概的工作就是初始化处理器映射、视图解析等）。初始化完毕后，spring以与servlet的名字相关（此处不是简单的以servlet名为Key，而是通过一些转换）的属性为属性Key，也将其存到`ServletContext`中，以便后续使用.这样每个servlet就持有自己的上下文，即拥有自己独立的bean空间，同时各个servlet共享相同的bean，即根上下文定义的那些bean

## 5、Bean加载过程

![image](image/Spring-Bean加载过程.png)

- `ResourceLoader`从存储介质中加载Spring配置信息，并使用Resource表示这个配置文件的资源。

- `BeanDefinitionReader`读取`Resource`所指向的配置文件资源，然后解析配置文件。配置文件中每一个`<bean>`解析成一个`BeanDefinition`对象，并保存到`BeanDefinitionRegistry`中；

- 容器扫描`BeanDefinitionRegistry`中的`BeanDefinition`，使用Java的反射机制自动识别出Bean工厂后处理后器（实现`BeanFactoryPostProcessor`接口）的Bean，然后调用这些Bean工厂后处理器对`BeanDefinitionRegistry`中的`BeanDefinition`进行加工处理.主要完成以下两项工作：
	* 对使用到占位符的`<bean>`元素标签进行解析，得到最终的配置值，这意味对一些半成品式的`BeanDefinition`对象进行加工处理并得到成品的`BeanDefinition`对象;
	* 对`BeanDefinitionRegistry`中的`BeanDefinition`进行扫描，通过Java反射机制找出所有属性编辑器的Bean（实现`java.beans.PropertyEditor`接口的Bean），并自动将它们注册到Spring容器的属性编辑器注册表中（`PropertyEditorRegistry`）

- Spring容器从`BeanDefinitionRegistry`中取出加工后的`BeanDefinition`，并调用`InstantiationStrategy`着手进行Bean实例化的工作；

- 在实例化Bean时，Spring容器使用`BeanWrapper`对Bean进行封装，`BeanWrapper`提供了很多以Java反射机制操作Bean的方法，它将结合该Bean的`BeanDefinition`以及容器中属性编辑器，完成Bean属性的设置工作；

- 利用容器中注册的Bean后处理器(实现BeanPostProcessor接口的Bean)对已经完成属性设置工作的Bean进行后续加工，直接装配出一个准备就绪的Bean

## 6、refresh方法源码

`AbstractApplicationContext.refresh()`，其实一个同步方法
```java
// org.springframework.context.support.AbstractApplicationContext.refresh() 方法实现如下：
@Override
public void refresh() throws BeansException, IllegalStateException {
    synchronized (this.startupShutdownMonitor) {
        // Prepare this context for refreshing.
        prepareRefresh();
        // Tell the subclass to refresh the internal bean factory.
        ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
        // Prepare the bean factory for use in this context.
        prepareBeanFactory(beanFactory);
        try {
            // Allows post-processing of the bean factory in context subclasses.
            postProcessBeanFactory(beanFactory);
            // Invoke factory processors registered as beans in the context.
            invokeBeanFactoryPostProcessors(beanFactory);
            // Register bean processors that intercept bean creation.
            registerBeanPostProcessors(beanFactory);
            // Initialize message source for this context.
            initMessageSource();
            // Initialize event multicaster for this context.
            initApplicationEventMulticaster();
            // Initialize other special beans in specific context subclasses.
            onRefresh();
            // Check for listener beans and register them.
            registerListeners();
            // Instantiate all remaining (non-lazy-init) singletons.
            finishBeanFactoryInitialization(beanFactory);
            // Last step: publish corresponding event.
            finishRefresh();
        }
        catch (BeansException ex) {
            ...
            // Destroy already created singletons to avoid dangling resources.
            destroyBeans();
            // Reset 'active' flag.
            cancelRefresh(ex);
            // Propagate exception to caller.
            throw ex;
        }
        finally {
            // Reset common introspection caches in Spring's core, since we might not ever need metadata for singleton beans anymore...
            resetCommonCaches();
        }
    }
}
```

### 6.1、prepareRefresh()：刷新前预处理

```java
protected void prepareRefresh() {
    this.startupDate = System.currentTimeMillis();
    this.closed.set(false);
    this.active.set(true);
    if (logger.isInfoEnabled()) {
        logger.info("Refreshing " + this);
    }
    // Initialize any placeholder property sources in the context environment
    initPropertySources();
    // Validate that all properties marked as required are resolvable
    // see ConfigurablePropertyResolver#setRequiredProperties
    getEnvironment().validateRequiredProperties();
    // Allow for the collection of early ApplicationEvents,
    // to be published once the multicaster is available...
    this.earlyApplicationEvents = new LinkedHashSet<ApplicationEvent>();
}
```
- `initPropertySources()`：初始化一些属性设置；子类自定义个性化的属性设置方法；这个方法是由子类来实现的
- `getEnvironment().validateRequiredProperties();`检验属性的合法等;
- `earlyApplicationEvents= new LinkedHashSet<ApplicationEvent>();`保存容器中的一些早期的事件；

### 6.2、obtainFreshBeanFactory()：获取BeanFactory

```java
protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
    refreshBeanFactory();
    ConfigurableListableBeanFactory beanFactory = getBeanFactory();
    if (logger.isDebugEnabled()) {
        logger.debug("Bean factory for " + getDisplayName() + ": " + beanFactory);
    }
    return beanFactory;
}
```
- `refreshBeanFactory();`刷新【创建】BeanFactory；创建了一个 `this.beanFactory = new DefaultListableBeanFactory();`设置id；
- `getBeanFactory();`返回刚才GenericApplicationContext创建的BeanFactory对象；
- 将创建的BeanFactory即`DefaultListableBeanFactory`返回；

### 6.3、prepareBeanFactory(beanFactory)

`BeanFactory`的预准备工作（BeanFactory进行一些设置）；
- 设置BeanFactory的类加载器、支持表达式解析器...
- 添加部分`BeanPostProcessor【ApplicationContextAwareProcessor】`
- 设置忽略的自动装配的接口`EnvironmentAware、EmbeddedValueResolverAware、xxx；`
- 注册可以解析的自动装配；我们能直接在任何组件中自动注入：`BeanFactory、ResourceLoader、ApplicationEventPublisher、ApplicationContext`
- 添加`BeanPostProcessor【ApplicationListenerDetector】`
- 添加编译时的AspectJ；
- 给BeanFactory中注册一些能用的组件；
    - environment【ConfigurableEnvironment】、
    - systemProperties【`Map<String, Object>`】、
    - systemEnvironment【`Map<String, Object>`】

### 6.4、postProcessBeanFactory(beanFactory);

BeanFactory准备工作完成后进行的后置处理工作；子类通过重写这个方法来在BeanFactory创建并预准备完成以后做进一步的设置

---
***=================以上是BeanFactory的创建及预准备工作=================***

### 6.5、invokeBeanFactoryPostProcessors(beanFactory);

```java
protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
    PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());
    // Detect a LoadTimeWeaver and prepare for weaving, if found in the meantime
    // (e.g. through an @Bean method registered by ConfigurationClassPostProcessor)
    if (beanFactory.getTempClassLoader() == null && beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
        beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
        beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
    }
}
```
执行`BeanFactoryPostProcessor`的方法；`BeanFactoryPostProcessor：BeanFactory`的后置处理器。在BeanFactory标准初始化之后执行的；

两个接口：`BeanFactoryPostProcessor、BeanDefinitionRegistryPostProcessor`；执行BeanFactoryPostProcessor的方法，其具体调用的方法是：`PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(oConfigurableListableBeanFactory, List<BeanFactoryPostProcessor>)`

**先执行BeanDefinitionRegistryPostProcessor**

- 获取所有的BeanDefinitionRegistryPostProcessor；
- 先执行实现了`PriorityOrdered`优先级接口的`BeanDefinitionRegistryPostProcessor`，`postProcessor.postProcessBeanDefinitionRegistry(registry)`;
- 在执行实现了`Ordered`顺序接口的`BeanDefinitionRegistryPostProcessor`；`postProcessor.postProcessBeanDefinitionRegistry(registry)`；
- 最后执行没有实现任何优先级或者是顺序接口的`BeanDefinitionRegistryPostProcessors；postProcessor.postProcessBeanDefinitionRegistry(registry);`

**再执行BeanFactoryPostProcessor的方法**
- 获取所有的BeanFactoryPostProcessor；
- 先执行实现了PriorityOrdered优先级接口的`BeanFactoryPostProcessor、postProcessor.postProcessBeanFactory()`
- 在执行实现了Ordered顺序接口的`BeanFactoryPostProcessor；postProcessor.postProcessBeanFactory()`
- 最后执行没有实现任何优先级或者是顺序接口的`BeanFactoryPostProcessor；postProcessor.postProcessBeanFactory()；`

### 6.6、registerBeanPostProcessors(beanFactory);

```java
// Separate between BeanPostProcessors that implement PriorityOrdered,
// Ordered, and the rest.
List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<BeanPostProcessor>();
List<BeanPostProcessor> internalPostProcessors = new ArrayList<BeanPostProcessor>();
List<String> orderedPostProcessorNames = new ArrayList<String>();
List<String> nonOrderedPostProcessorNames = new ArrayList<String>();
```
注册BeanPostProcessor（Bean的后置处理器）【 intercept bean creation】，不同接口类型的BeanPostProcessor；在Bean创建前后的执行时机是不一样的，实际调用方法：`PostProcessorRegistrationDelegate.registerBeanPostProcessors(ConfigurableListableBeanFactory, AbstractApplicationContext)`

`BeanPostProcessor、DestructionAwareBeanPostProcessor、InstantiationAwareBeanPostProcessor、SmartInstantiationAwareBeanPostProcessor、MergedBeanDefinitionPostProcessor【internalPostProcessors】、`

- 获取所有的 BeanPostProcessor；后置处理器都默认可以通过PriorityOrdered、Ordered接口来执行优先级;
- 先注册PriorityOrdered优先级接口的BeanPostProcessor；把每一个BeanPostProcessor；添加到BeanFactory中,`beanFactory.addBeanPostProcessor(postProcessor);`
- 再注册Ordered接口的;
- 最后注册没有实现任何优先级接口的;
- 最终注册MergedBeanDefinitionPostProcessor；
- 注册一个ApplicationListenerDetector；来在Bean创建完成后检查是否是ApplicationListener，如果是`applicationContext.addApplicationListener((ApplicationListener<?>) bean);`

### 6.7、initMessageSource();

初始化MessageSource组件（做国际化功能；消息绑定，消息解析）；
- 获取BeanFactory；
- 看容器中是否有id为messageSource的，类型是MessageSource的组件，如果有赋值给messageSource；如果没有自己创建一个`DelegatingMessageSource；MessageSource`：取出国际化配置文件中的某个key的值；能按照区域信息获取；
- 把创建好的MessageSource注册在容器中，以后获取国际化配置文件的值的时候，可以自动注入MessageSource；
```java
beanFactory.registerSingleton(MESSAGE_SOURCE_BEAN_NAME, this.messageSource);	
MessageSource.getMessage(String code, Object[] args, String defaultMessage, Locale locale);
```

### 6.8、initApplicationEventMulticaster();

初始化事件派发器；主要是针对事件的处理
```java
protected void initApplicationEventMulticaster() {
    ConfigurableListableBeanFactory beanFactory = getBeanFactory();
    // 从BeanFactory中获取applicationEventMulticaster的ApplicationEventMulticaster；
    if (beanFactory.containsLocalBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME)) {
        this.applicationEventMulticaster =
                beanFactory.getBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, ApplicationEventMulticaster.class);
    }
    else {
        // 如果上一步没有配置；创建一个SimpleApplicationEventMulticaster
        this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        // 将创建的ApplicationEventMulticaster添加到BeanFactory中，以后其他组件直接自动注入
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, this.applicationEventMulticaster);
    }
}
```

### 6.9、onRefresh()

子类重写这个方法，在容器刷新的时候可以自定义逻辑；在web应用中，主要是创建web容器
```java
protected void onRefresh() throws BeansException {
    // For subclasses: do nothing by default.
}
```

### 6.10、registerListeners();

给容器中将所有项目里面的ApplicationListener注册进来；派发早期事件
```java
protected void registerListeners() {
    // 从容器中拿到所有的ApplicationListener，并将每个监听器添加到事件派发器中；
    for (ApplicationListener<?> listener : getApplicationListeners()) {
        getApplicationEventMulticaster().addApplicationListener(listener);
    }
    // Do not initialize FactoryBeans here: We need to leave all regular beans
    // uninitialized to let post-processors apply to them!
    String[] listenerBeanNames = getBeanNamesForType(ApplicationListener.class, true, false);
    for (String listenerBeanName : listenerBeanNames) {
        getApplicationEventMulticaster().addApplicationListenerBean(listenerBeanName);
    }
    // 派发之前步骤产生的事件
    Set<ApplicationEvent> earlyEventsToProcess = this.earlyApplicationEvents;
    this.earlyApplicationEvents = null;
    if (earlyEventsToProcess != null) {
        for (ApplicationEvent earlyEvent : earlyEventsToProcess) {
            getApplicationEventMulticaster().multicastEvent(earlyEvent);
        }
    }
}
```

### 6.11、finishBeanFactoryInitialization(beanFactory)

初始化所有剩下的单实例bean；这一步骤是比较繁琐的
```java
protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
    // Initialize conversion service for this context.
    if (beanFactory.containsBean(CONVERSION_SERVICE_BEAN_NAME) &&  beanFactory.isTypeMatch(CONVERSION_SERVICE_BEAN_NAME, ConversionService.class)) {
        beanFactory.setConversionService(beanFactory.getBean(CONVERSION_SERVICE_BEAN_NAME, ConversionService.class));
    }
    // Register a default embedded value resolver if no bean post-processor
    // (such as a PropertyPlaceholderConfigurer bean) registered any before:
    // at this point, primarily for resolution in annotation attribute values.
    if (!beanFactory.hasEmbeddedValueResolver()) {
        beanFactory.addEmbeddedValueResolver(new StringValueResolver() {
            @Override
            public String resolveStringValue(String strVal) {
                return getEnvironment().resolvePlaceholders(strVal);
            }
        });
    }
    // Initialize LoadTimeWeaverAware beans early to allow for registering their transformers early.
    String[] weaverAwareNames = beanFactory.getBeanNamesForType(LoadTimeWeaverAware.class, false, false);
    for (String weaverAwareName : weaverAwareNames) {
        getBean(weaverAwareName);
    }
    // Stop using the temporary ClassLoader for type matching.
    beanFactory.setTempClassLoader(null);
    // Allow for caching all bean definition metadata, not expecting further changes.
    beanFactory.freezeConfiguration();
    // Instantiate all remaining (non-lazy-init) singletons.
    beanFactory.preInstantiateSingletons();
}
```
- `org.springframework.beans.factory.support.DefaultListableBeanFactory#preInstantiateSingletons`
- `org.springframework.beans.factory.support.AbstractBeanFactory#doGetBean`

- 获取容器中的所有Bean，依次进行初始化和创建对象;
- 获取Bean的定义信息；RootBeanDefinition;
- Bean不是抽象的，是单实例的，是懒加载；
    - 判断是否是FactoryBean；是否是实现FactoryBean接口的Bean；
    - 不是工厂Bean。利用getBean(beanName);创建对象
        - `getBean(beanName)； ioc.getBean();`
        - `doGetBean(name, null, null, false);`
        - 先获取缓存中保存的单实例Bean。如果能获取到说明这个Bean之前被创建过（所有创建过的单实例Bean都会被缓存起来）
            从`private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>(256);`获取的；
        - 缓存中获取不到，开始Bean的创建对象流程；
        - 标记当前bean已经被创建；
        - 获取Bean的定义信息；
        - 【获取当前Bean依赖的其他Bean;如果有按照getBean()把依赖的Bean先创建出来；】
        - 启动单实例Bean的创建流程：
            - createBean(beanName, mbd, args);
            - `Object bean = resolveBeforeInstantiation(beanName, mbdToUse);`让BeanPostProcessor先拦截返回代理对象；
				- 【InstantiationAwareBeanPostProcessor】：提前执行；先触发：postProcessBeforeInstantiation()；如果有返回值：触发postProcessAfterInitialization()；；
            - 如果前面的InstantiationAwareBeanPostProcessor没有返回代理对象；调用下面步骤
            - `Object beanInstance = doCreateBean(beanName, mbdToUse, args);`创建Bean
                - 【创建Bean实例】；createBeanInstance(beanName, mbd, args);利用工厂方法或者对象的构造器创建出Bean实例；
                - `applyMergedBeanDefinitionPostProcessors(mbd, beanType, beanName); `调用`MergedBeanDefinitionPostProcessor的postProcessMergedBeanDefinition(mbd, beanType, beanName);`;
                - 【Bean属性赋值】populateBean(beanName, mbd, instanceWrapper);
                    
                    赋值之前
                    - 拿到InstantiationAwareBeanPostProcessor后置处理器；postProcessAfterInstantiation()；
                    - 拿到InstantiationAwareBeanPostProcessor后置处理器；postProcessPropertyValues()；
                    - 应用Bean属性的值；为属性利用setter方法等进行赋值；applyPropertyValues(beanName, mbd, bw, pvs);
                - 【Bean初始化】initializeBean(beanName, exposedObject, mbd);
                    - 【执行Aware接口方法】invokeAwareMethods(beanName, bean);执行xxxAware接口的方法：`BeanNameAware\BeanClassLoaderAware\BeanFactoryAware`
                    - 【执行后置处理器初始化之前】`applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);``BeanPostProcessor.postProcessBeforeInitialization（）;`
                    - 【执行初始化方法】invokeInitMethods(beanName, wrappedBean, mbd);
                        - 是否是InitializingBean接口的实现；执行接口规定的初始化；
                        - 是否自定义初始化方法；
                    - 【执行后置处理器初始化之后】applyBeanPostProcessorsAfterInitialization；BeanPostProcessor.postProcessAfterInitialization()；
                - 注册Bean的销毁方法；
            - 将创建的Bean添加到缓存中singletonObjects；

        ioc容器就是这些Map；很多的Map里面保存了单实例Bean，环境信息。。。。
    
    所有Bean都利用getBean创建完成以后；检查所有的Bean是否是SmartInitializingSingleton接口的；如果是；就执行afterSingletonsInstantiated()；

getBean的全流程：

![](image/getBean过程.jpg)

### 6.12、finishRefresh();

完成BeanFactory的初始化创建工作；IOC容器就创建完成；
```java
protected void finishRefresh() {
    // Initialize lifecycle processor for this context.
    initLifecycleProcessor();

    // Propagate refresh to lifecycle processor first.
    getLifecycleProcessor().onRefresh();

    // Publish the final event.
    publishEvent(new ContextRefreshedEvent(this));

    // Participate in LiveBeansView MBean, if active.
    LiveBeansView.registerApplicationContext(this);
}
```
- `initLifecycleProcessor();`初始化和生命周期有关的后置处理器；LifecycleProcessor；默认从容器中找是否有lifecycleProcessor的组件【LifecycleProcessor】；如果没有new DefaultLifecycleProcessor();加入到容器；

- `getLifecycleProcessor().onRefresh();`拿到前面定义的生命周期处理器（BeanFactory）；回调onRefresh()；
- `publishEvent(new ContextRefreshedEvent(this));`发布容器刷新完成事件；
- `liveBeansView.registerApplicationContext(this);`

## 7、注册钩子函数

上面refresh函数式在refreshContext调用的，执行完refresh函数后回去注册shutdownhook，即钩子函数
```java
private void refreshContext(ConfigurableApplicationContext context) {
    refresh(context);
    if (this.registerShutdownHook) {
        try {
            context.registerShutdownHook();
        }
        catch (AccessControlException ex) {
            // Not allowed in some environments.
        }
    }
}
// AbstractApplicationContext
@Override
public void registerShutdownHook() {
    if (this.shutdownHook == null) {
        // No shutdown hook registered yet.
        this.shutdownHook = new Thread(SHUTDOWN_HOOK_THREAD_NAME) {
            @Override
            public void run() {
                synchronized (startupShutdownMonitor) {
                    doClose();
                }
            }
        };
        Runtime.getRuntime().addShutdownHook(this.shutdownHook);
    }
}
```

# 二、AOP

AOP的实现：jdkProxy和Cglib
- 有AopProxyFactory根据AdvisedSupport对象的配置来决定；
- 默认策略如果目标类是接口，则用JDKProxy来实现，否则用后者；
- JDKProxy的核心：invocationHandler接口和Proxy类；使用Java的反射机制来实现的
- CGlib：以继承的方式动态生成目标类的代理；借助ASM实现

# 三、spring事务

## 1、Spring事务管理方式

- 编程式事务：使用TransactionTemplate，粒度控制在代码块，手动提交
- 声明式事务：xml，注解，粒度只能控制在public方法中

## 2、Spring的事务特性

### 2.1、Spring的事务管理策略

都是基于 org.springframework.transaction.PlatformTransactionManager一般使用的都是 DataSourceTransactionManager，也就是基于数据源的事务管理。DataSourceTransactionManager 实现了两个接口：PlatformTransactionManager和InitializingBean

* 实现了PlatformTransactionManager说明这个类主要功能是进行事务管理;
* 实现了InitializingBean接口，DataSourceTransactionManager进行事务管理的前提是DataSource已经成功注入.

TransactionDefinition接口里面定义了事务的隔离级别和事务的传播行为

### 2.2、Spring的事务隔离级别

在Spring的事务管理中一样，TransactionDefinition定义了5种隔离级别

```java
//底层数据库默认的隔离级别，这个与具体数据库有关系
int ISOLATION_DEFAULT = -1;
// 未提交读
int ISOLATION_READ_UNCOMMITTED = Connection.TRANSACTION_READ_UNCOMMITTED;
// 提交读：只能读取别人commit了的数据
int ISOLATION_READ_COMMITTED = Connection.TRANSACTION_READ_COMMITTED;
// 可重复读：存在幻读，不过MySQL通过MVCC解决这个问题
int ISOLATION_REPEATABLE_READ = Connection.TRANSACTION_REPEATABLE_READ;
// 串行化
int ISOLATION_SERIALIZABLE = Connection.TRANSACTION_SERIALIZABLE;
```

### 2.3、Spring事务传播行为

事务传播行为是指如果在开始当前事务之前，一个事务上下文已经存在了，此时有若干选项可以指定一个事务性方法的执行行为。在TransactionDefinition中同样定义了如下几种事务传播行为

```java
// 如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。这是默认值。
int PROPAGATION_REQUIRED = 0;
// 如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行。
int PROPAGATION_SUPPORTS = 1;
// 如果当前存在事务，则加入该事务；如果当前没有事务，则抛出异常。
int PROPAGATION_MANDATORY = 2;
// 创建一个新的事务，如果当前存在事务，则把当前事务挂起。
int PROPAGATION_REQUIRES_NEW = 3;
// 以非事务方式运行，如果当前存在事务，则把当前事务挂起。
int PROPAGATION_NOT_SUPPORTED = 4;
// 以非事务方式运行，如果当前存在事务，则抛出异常。
int PROPAGATION_NEVER = 5;
// 如果当前存在事务，则创建一个事务作为当前事务的嵌套事务来运行；
// 如果当前没有事务，则该取值等价于TransactionDefinition.PROPAGATION_REQUIRED。
int PROPAGATION_NESTED = 6;
```	
### 2.4、事务超时时间

一个事务允许执行的最长时间，如果超过这个限制但是事务还没有完成，则自动回滚事务。在TransactionDefinition 以int值表示超时时间，其单位是秒。默认设置为底层事务系统的超时值，如果底层数据库事务系统没有设置超时值，那么就是none，没有超时限制；

### 2.5、事务回滚规则

Spring事务管理器会捕捉任何未处理的异常，然后依据规则决定是否回滚抛出异常的事务。默认配置：spring只有在抛出的异常为运行时unchecked异常时才回滚该事务，也就是抛出的异常为RuntimeException的子类(Errors也会导致事务回滚)，而抛出checked异常则不会导致事务回滚

## 3、Spring事务实现原理


# 四、SpringFactoriesLoader

# 五、Spring事件

## 1、理解Spring事件、监听机制

Spring事件监听机制属于事件/监听模式，可以视为观察者模式的扩展。

在Java中，事件监听器模式发布的内容有类型限制，它必须是EventObject对象。所以Spring的事件抽象类 ApplicationEvent 必然扩展 EventObject
```java
public abstract class ApplicationEvent extends EventObject {
	public ApplicationEvent(Object source) {
		super(source);
		this.timestamp = System.currentTimeMillis();
	}
	public final long getTimestamp() {
		return this.timestamp;
	}
}
```

EventObject不提供默认构造器，需要外部传入一个名为 source 的构造器参数，用于记录并跟踪事件的来源，同时Java事件的监听者必须是 EventListener的扩展，不过 EventListener只是一个标签接口，没有提供任何实现方法；
```
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {
	void onApplicationEvent(E event);
}
```
早期 ApplicationListener不支持泛型监听，在SPring3.0之后，支持泛型监听，无需借助instanceof方式来过滤事件。但是由于泛型化的 ApplicationListener 无法监听不同类型的 ApplicationEvent，因此又引入了 SmartApplicationListener接口：
```java
public interface SmartApplicationListener extends ApplicationListener<ApplicationEvent>, Ordered {
	boolean supportsEventType(Class<? extends ApplicationEvent> eventType);
	default boolean supportsSourceType(@Nullable Class<?> sourceType) {
		return true;
	}
	@Override
	default int getOrder() {
		return LOWEST_PRECEDENCE;
	}
}
```
该接口通过supports*方法过滤需要监听的 ApplicationEvent类型和事件源类型，从而达到监听不同类型的 ApplicationEvent

## 2、Spring事件发布

ApplicationEventMulticaster 接口负责关联 ApplicationListener和广播 ApplicationEvent：
```java
public interface ApplicationEventMulticaster {
	void addApplicationListener(ApplicationListener<?> listener);
	void addApplicationListenerBean(String listenerBeanName);
	void removeApplicationListener(ApplicationListener<?> listener);
	void removeApplicationListenerBean(String listenerBeanName);
	void removeAllListeners();
	void multicastEvent(ApplicationEvent event);
	void multicastEvent(ApplicationEvent event, @Nullable ResolvableType eventType);
}
```

### 2.1、ApplicationEventMulticaster注册 ApplicationListener

该接口前半部分与 ApplicationListener有关，添加和移除ApplicationListener。该类有个抽象实现类：AbstractApplicationEventMulticaster，有一个具体子类：SimpleApplicationEventMulticaster。

AbstractApplicationEventMulticaster 其并未直接关联 ApplicationListener，而是通过两个属性：defaultRetriever 和 retrieverCache 关联，映射数量分别是：`0..1` 和 `0..N`
```
private final ListenerRetriever defaultRetriever = new ListenerRetriever(false);
final Map<ListenerCacheKey, ListenerRetriever> retrieverCache = new ConcurrentHashMap<>(64);
```
其中 ListenerRetriever、ListenerCacheKey 为 AbstractApplicationEventMulticaster 内部类；

按照 AbstractApplicationEventMulticaster 定义的两个属性， AbstractApplicationEventMulticaster与 ApplicationListener应该是一堆多的关系。AbstractApplicationEventMulticaster 对 ApplicationListener 做了分类，集合 retrieverCache 的定义，它是一个 ListenerCacheKey 为key、ListenerRetriever为value的缓存，同时 ListenerCacheKey 关联了事件类型和数据源类型
```java
private static final class ListenerCacheKey implements Comparable<ListenerCacheKey> {
    private final ResolvableType eventType;
    @Nullable
    private final Class<?> sourceType;
    public ListenerCacheKey(ResolvableType eventType, @Nullable Class<?> sourceType) {
        Assert.notNull(eventType, "Event type must not be null");
        this.eventType = eventType;
        this.sourceType = sourceType;
    }
}
```
 
# 参考资料

* [Spring AOP原理](https://mp.weixin.qq.com/s/f-Nnov2knru68KT6gWtvBQ)
* [源码解读Spring IOC原理](https://www.cnblogs.com/ITtangtang/p/3978349.html)
* [tiny-spring](https://github.com/code4craft/tiny-spring)
* [源代码](https://github.com/spring-projects/spring-framework)
* [IoC容器及Bean的生命周期](https://www.cnblogs.com/IvySue/p/6484599.html)
* [IOC容器源码分析](https://javadoop.com/post/spring-ioc)
* [SpringIOC原理](https://zhuanlan.zhihu.com/p/29344811)
* [Spring加载应用程序Bean类分析](https://blog.csdn.net/u013095337/article/details/53609398)
* [Spring中Bean的this调用导致AOP失效的原因](https://my.oschina.net/guangshan/blog/1807721)
* [死磕Spring源码系列](http://cmsblogs.com/?p=4047)
