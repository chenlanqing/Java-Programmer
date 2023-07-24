# 一、@Value注解原理

- [你不得不知道的@Value注解原理](https://juejin.cn/post/6844903904732266510)

## 1、基本流程

- 在spring中是由`AutowiredAnnotationBeanPostProcessor`解析处理@Value注解。AutowiredAnnotationBeanPostProcessor是一个BeanPostProcessor，所以每个类的实例化都过经过AutowiredAnnotationBeanPostProcessor类。
- 当post-processor处理bean时，会解析bean Class的所有属性，在解析时会判断属性上是否标有@Value注解，有就解析这个@Value的属性值，将解析后结果放入AutowiredFieldElement类型InjectionMetaData.checkedElements中；
- 当给属性赋值时会使用checkedElements，从而得到@Value注解的Filed属性，调用AutowiredFieldElement.inject()方法进行解析，解析时会使用DefaultListableBeanFactory(用于解析${})和TypeConverter(用于类型转换)，从而得到age属性的值，最后调用field.set(bean, value)，从而获取的值赋给bean的field

整体调用栈：
```
AbstractAutowireCapableBeanFactory.createBean()                                    
├AbstractAutowireCapableBeanFactory.doCreateBean()                               
├─AbstractAutowireCapableBeanFactory.applyMergedBeanDefinitionPostProcessors()   
├──AutowiredAnnotationBeanPostProcessor.postProcessMergedBeanDefinition()        
├───AutowiredAnnotationBeanPostProcessor.findAutowiringMetadata()                
├────AutowiredAnnotationBeanPostProcessor.buildAutowiringMetadata() 
========以上是存逻辑，以下是用逻辑========
> "存"指把需要解析的数据放到InjectionMetadata中
> "用"指对放到InjectionMetadata的数据进行解析
├─AbstractAutowireCapableBeanFactory.populateBean()                               
├──AutowiredAnnotationBeanPostProcessor.postProcessProperties()                  
├───AutowiredAnnotationBeanPostProcessor.findAutowiringMetadata()                
├────InjectionMetadata.inject()                                                  
├─────(InjectionMetadata.InjectedElement)AutowiredFieldElement.inject()
├──────DefaultListableBeanFactory.resolveDependency()
├───────DefaultListableBeanFactory.doResolveDependency() ---解析依赖上@value(${})的核心入口
├────────AbstractBeanFactory.resolveEmbeddedValue() ---解析所有用到${}的放的核心入口
├─────────StringValueResolver lambda子类.resolveStringValue() ---这个lambda使用方式值得琢磨下
├──────────PropertySourcesPropertyResolver.resolveRequiredPlaceholders()
├───────────AbstractPropertyResolver.getPropertyAsRawString()
├────────────PropertyPlaceholderHelper.replacePlaceholders()
├─────────────PropertySource.getProperty()
├────────────PropertyPlaceholderHelper.parseStringValue() 递归
├───────────AbstractPropertyResolver.resolvePlaceholder()
├────────────PropertySource.getProperty()
├───────SimpleTypeConverter.convertIfNecessary()
├────────TypeConverterDelegate.convertIfNecessarT()
├─────────ConversionService.canConvert()
├─────────ConversionService.convert()
```

# 二、@Autowired原理

- [@Autowired如何注入变量](https://juejin.cn/post/7255305597305733177)

## 1、创建bean

```java
protected Object doCreateBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args)
		throws BeanCreationException {
    //....
	Object exposedObject = bean;
	try {
		populateBean(beanName, mbd, instanceWrapper);
		exposedObject = initializeBean(beanName, exposedObject, mbd);
	}
	//...
	return exposedObject;
}
```
在这个方法中，首先会创建原始的 Bean 对象，创建出来之后，会调用一个 populateBean 方法，这个方法就是给 Bean 的各个属性赋值的方法，标注了 @Autowired 注解的属性被自动赋值也是在这个方法中完成的。

## 2、populateBean方法

```java
protected void populateBean(String beanName, RootBeanDefinition mbd, @Nullable BeanWrapper bw) {
	// Give any InstantiationAwareBeanPostProcessors the opportunity to modify the
	// state of the bean before properties are set. This can be used, for example,
	// to support styles of field injection.
	if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
		for (InstantiationAwareBeanPostProcessor bp : getBeanPostProcessorCache().instantiationAware) {
			if (!bp.postProcessAfterInstantiation(bw.getWrappedInstance(), beanName)) {
				return;
			}
		}
	}
	//...
	if (hasInstantiationAwareBeanPostProcessors()) {
		if (pvs == null) {
			pvs = mbd.getPropertyValues();
		}
        // 会遍历所有相关的后置处理器，尝试通过这些处理器去获取到需要的 value
		for (InstantiationAwareBeanPostProcessor bp : getBeanPostProcessorCache().instantiationAware) {
			PropertyValues pvsToUse = bp.postProcessProperties(pvs, bw.getWrappedInstance(), beanName);
			if (pvsToUse == null) {
				return;
			}
			pvs = pvsToUse;
		}
	}
	boolean needsDepCheck = (mbd.getDependencyCheck() != AbstractBeanDefinition.DEPENDENCY_CHECK_NONE);
	if (needsDepCheck) {
		PropertyDescriptor[] filteredPds = filterPropertyDescriptorsForDependencyCheck(bw, mbd.allowCaching);
		checkDependencies(beanName, mbd, filteredPds, pvs);
	}
	if (pvs != null) {
		applyPropertyValues(beanName, mbd, bw, pvs);
	}
}
```
AutowiredAnnotationBeanPostProcessor(处理被@Autowired注解修饰的bean并注入)；

## 3、postProcessProperties

```java
@Override
public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) {
	InjectionMetadata metadata = findAutowiringMetadata(beanName, bean.getClass(), pvs);
	try {
		metadata.inject(bean, beanName, pvs);
	}
	catch (BeanCreationException ex) {
		throw ex;
	}
	catch (Throwable ex) {
		throw new BeanCreationException(beanName, "Injection of autowired dependencies failed", ex);
	}
	return pvs;
}
```

# 三、@Async原理

- [@Async注解原理剖析](https://www.cnblogs.com/thisiswhy/p/15233243.html)

```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Async {
	/**
	 * A qualifier value for the specified asynchronous operation(s).
	 * <p>May be used to determine the target executor to be used when executing
	 * the asynchronous operation(s), matching the qualifier value (or the bean
	 * name) of a specific {@link java.util.concurrent.Executor Executor} or
	 * {@link org.springframework.core.task.TaskExecutor TaskExecutor}
	 * bean definition.
	 * <p>When specified on a class-level {@code @Async} annotation, indicates that the
	 * given executor should be used for all methods within the class. Method-level use
	 * of {@code Async#value} always overrides any value set at the class level.
	 * @since 3.1.2
	 */
	String value() default "";
}
```

## 1、基本使用

spring中用`@Async`注解标记的方法，称为异步方法，它会在调用方的当前线程之外的独立的线程中执行，其实就相当于`new Thread(() -> System.out.println("Hello world !"));`这样在另一个线程中去执行相应的业务逻辑。

`@Async`注解使用条件
- `@Async`注解一般用在类的方法上，如果用在类上，那么这个类所有的方法都是异步执行的；
- 所使用的`@Async`注解方法的类对象应该是Spring容器管理的bean对象；
- 调用异步方法类上需要配置上注解`@EnableAsync`

使用注意：
- 默认情况下（即`@EnableAsync`注解的`mode=AdviceMode.PROXY`），同一个类内部没有使用`@Async`注解修饰的方法调用`@Async`注解修饰的方法，是不会异步执行的，这点跟 `@Transitional` 注解类似，底层都是通过动态代理实现的。如果想实现类内部自调用也可以异步，则需要切换`@EnableAsync`注解的`mode=AdviceMode.ASPECTJ`
- 任意参数类型都是支持的，但是方法返回值必须是void或者Future类型。当使用Future时，你可以使用 实现了Future接口的ListenableFuture接口或者CompletableFuture类与异步任务做更好的交互。如果异步方法有返回值，没有使用Future类型的话，调用方获取不到返回值；

## 2、默认线程池

`@Async` 注解中有一个 value 属性，根据注释可以推断出是指定自定义线程池的，如果没有指定的话，就走默认线程池：
```java
// org.springframework.aop.interceptor.AsyncExecutionAspectSupport#determineAsyncExecutor
@Nullable
protected AsyncTaskExecutor determineAsyncExecutor(Method method) {
    AsyncTaskExecutor executor = this.executors.get(method);
    if (executor == null) {
        Executor targetExecutor;
        // 获取对应方法上的 @Async 注解的 value 值。这个值其实就是 bean 名称，如果不为空则从 Spring 容器中获取对应的 bean
        String qualifier = getExecutorQualifier(method);
        if (StringUtils.hasLength(qualifier)) {
            targetExecutor = findQualifiedExecutor(this.beanFactory, qualifier);
        }
        else {
            // 默认线程池
            targetExecutor = this.defaultExecutor.get();
        }
        if (targetExecutor == null) {
            return null;
        }
        executor = (targetExecutor instanceof AsyncListenableTaskExecutor ?
                (AsyncListenableTaskExecutor) targetExecutor : new TaskExecutorAdapter(targetExecutor));
        this.executors.put(method, executor);
    }
    return executor;
}
```
获取默认线程池：
```java
// org.springframework.aop.interceptor.AsyncExecutionAspectSupport#getDefaultExecutor
@Nullable
protected Executor getDefaultExecutor(@Nullable BeanFactory beanFactory) {
    if (beanFactory != null) {
        try {
            // Search for TaskExecutor bean... not plain Executor since that would
            // match with ScheduledExecutorService as well, which is unusable for
            // our purposes here. TaskExecutor is more clearly designed for it.
            return beanFactory.getBean(TaskExecutor.class);
        }
       ...
    }
    return null;
}
```
返回的 targetExecutor，其核心线程数配置是 8 ，队列长度应该是 Integer.MAX_VALUE。beanName = 'applicationTaskExecutor'
