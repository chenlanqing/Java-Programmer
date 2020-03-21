# 2、Dubbo扩展点加载机制

## 2.1、加载机制概述

### 2.1.1、Dubbo中SPI概述

[什么是SPI](../../Java基础/Java基础知识.md#二十二JavaSPI机制)

Dubbo中关于SPI的加载，都是用过`ExtensionLoader`类来加载的，使用双重检验锁来解决线程安全问题；Dubbo的SPI的包含负载均衡算法、Filter、集群容错、编码、协议、序列化等，比如LoadBalance（负载均衡）

```java
@SPI(RandomLoadBalance.NAME)
public interface LoadBalance {
    @Adaptive("loadbalance")
    <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException;
}
```

**约定**：在扩展类的 jar 包内，放置扩展点配置文件 `META-INF/dubbo/`接口全限定名，内容为：`配置名=扩展实现类全限定名`，多个实现类用换行符分隔，其配置路径一般有三个：`META-INF/services/`、`META-INF/dubbo/`、`META-INF/dubbo/internal/`

### 2.1.2、Dubbo中SPI与JDK中的SPI区别

- 提升性能：JDK的spi要用for循环，然后if判断才能获取到指定的spi对象；dubbo用指定的key就可以获取；
    ```java
    //返回指定名字的扩展
    public T getExtension(String name){}
    ```
- JDK的spi不支持默认值；dubbo增加了默认值的设计；
- Dubbo的spi增加了IoC、AOP，一个扩展可以直接注入其他扩展；Dubbo支持包装扩展类，一般是把通用的抽象逻辑放到包装类中，用于实现扩展点的APO特性；
- JDK的SPI会一次性实例化扩展点的所有实现，如果有扩展实现则初始化很耗时，如果没有也加载，浪费资源；
- JDK的SPI如果扩展加载失败，则连扩展的名称都获取不到；
- Dubbo的SPI只是加载配置文件中的类，并分成不同的种类缓存在内存中，而不会立即全部初始化，在性能上有更好的表现；

### 2.1.5、扩展点分类与缓存

Dubbo SPI可以分为 Class缓存、实例缓存；这两种缓存又能根据扩展类的种类分为普通扩展类、包装扩展类（Wrapper）、自适应扩展类（Adaptive）等；
- Class缓存：Dubbo SPI获取扩展类时，会从缓存中读取，如果缓存中不存在，则加载配置文件，根据配置把Class缓存到内存中，并不会直接初始化；
- 实例缓存：Dubbo SPI中不仅缓存Class，也缓存Class实例化后的对象；每次获取时，先会从缓存中读取，如果缓存中读不到，则重新加载并缓存起来；Dubbo SPI缓存的Class并不全部实例化，而是按需实例化并缓存；

根据不同的特性分为不同的类别：
- 普通扩展类：配置在SPI配置文件中扩展类实现；
- 包装扩展类：这类Wrapper类没有具体的实现，只是做了通用逻辑的抽象，并且需要抽象方法中传入一个具体的扩展接口的实现；
- 自适应扩展类：一个扩展接口会有多个实现类，具体使用哪个实现类可以不写死在配置或代码中，在运行时，通过传入URL中的某个参数动态来确定，使用Adaptive注解来出咯；
- 其他缓存：扩展类加载器缓存、扩展名缓存；

### 2.1.4、扩展点特性

**1、自动包装**

自动包装在Extension加载扩展时，如果返现这个扩展类包含其他扩展点作为构造函数的参数，则这个扩展类就会被认为是Wrapper

**2、自动加载**

如果某个扩展类是另外一个扩展点类的成员属性，并且拥有setter方法，那么框架也会自动注入对应的扩展点实例。ExtensionLoader在初始化时，会自动通过setter方法注入实现类；

**3、自适应**

在Dubbo中，使用`@Adaptive`注解，可以动态通过URL中参数来确定要使用具体的哪个实现类。从而解决自动加载中实例注入的问题；

**4、自动激活**

使用 `@Aactivate` 注解，可以标记对应的扩展点默认被激活使用。该注解还可以通过传入不同的参数，设置扩展点在不同条件下被自动激活；

## 2.2、扩展点注解

### 2.2.1、扩展点注解：@SPI

该注解可以使用在类、接口和枚举类上，Dubbo中都是使用在接口上的，主要是用来标记这个接口是一个扩展点，可以有多个不同的内置实现或者用定义的实现。运行时会用过配置找到具体的实现类

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {
    // 默认扩展名
    String value() default "";
}
```

### 2.2.2、扩展点自适应注解：@Adaptive

该注解可以标记在类、接口、枚举类和方法上。在整个Dubbo中，只有少数地方用在类级别上，比如 AdaptiveExtensionFactory和 AdaptiveCompiler。

如果标注在接口的方法上，即方法级别注解，则可以用个参数动态获取实现类，在第一个getExtension时会自动生成和编译一个动态的Apdative类，从而达到动态实现的效果，比如如下生成的类：
```java
package com.blue.fish.dubbo.extension.adaptive;
import org.apache.dubbo.common.extension.ExtensionLoader;
public class SimpleExt$Adaptive implements SimpleExt {
    public java.lang.String echo(org.apache.dubbo.common.URL arg0, java.lang.String arg1) {
        if (arg0 == null) throw new IllegalArgumentException("url == null");
        org.apache.dubbo.common.URL url = arg0;
        /**
         * 如果 @Adaptive 注解没有传入key的值，则默认会把类名转换为key，比如：SimpleExt 会转换成 simple.ext
         * 根据key获取对应扩展点实现名称，第一个参数是key，第二个是获取不到时默认值
         * URL中没有“simple.ext”这个key，因此extName 取值为 simpleImpl，
         * 如果 @Adaptive 注解中有key参数，如果 @Adaptive("second")，则会变成 url.getParameter("second", "simpleImpl");
         */
        String extName = url.getParameter("second", "simpleImpl");
        if (extName == null)
            throw new IllegalStateException("Failed to get extension (SimpleExt) name from url (" + url.toString() + ") use keys([second])");
        SimpleExt extension = (SimpleExt) ExtensionLoader.getExtensionLoader(SimpleExt.class).getExtension(extName);
        return extension.echo(arg0, arg1);
    }
}
```

当标注在实现类上，则整个实现类会直接作为默认实现，不再自动生成代码清单。在扩展点接口的多个实现里，只有一个实现上可以加@Adaptive注解。如果多个实现类都有该注解，则会抛出异常

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Adaptive {
    String[] value() default {};
}
```
该注解可以传入参数，是一个数组。Adaptive 可以传入多个值，在初始化 Adaptive 注解的接口时，会先对传入的URL进行key值匹配，第一个key没有匹配上，匹配第二个，以此类推；知道所有的key匹配完毕，如果还没有匹配到，则使用 @SPI 注解中添加的默认值再去匹配。如果 @SPI 注解中也没有填写默认值，则会抛出 IllegalStateException；

如果包装类没有使用 Adaptive 指定的 key值，也没有填写 SPI 默认值，则Dubbo会指定把接口名称根据驼峰大小写分开，并用 `.` 符号链接起来，以此来作为默认实现类的名，比如 `org.apache.dubbo.xxx.YyyInvokerWrapper` 中的 YyyInvokerWrapper 会被转换为 yyy.invoker.wrapper；

放在实现类上的 Adaptive 注解主要是为了固定对应的实现。在代码中会缓存与 Adaptive 有关的对象；


### 2.2.3、扩展点自动激活注解：@Activate

该注解可以标记在类、接口、枚举类和方法上，主要使用在有多个扩展点的实现、需要根据不同的条件被激活的场景，入Filter需要同时激活多个，因为每个FIlter实现的是不同的功能

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Activate {
    // URL中的分组如果匹配则激活，可以设置多个
    String[] group() default {};
    // 查找URL中如果含有该key的值，则会激活
    String[] value() default {};
    @Deprecated
    String[] before() default {};
    @Deprecated
    String[] after() default {};
    // 直接的排序信息
    int order() default 0;
}
```

## 2.3、ExtensionLoader 工作原理

ExtensionLoader 是整个扩展机制的主要逻辑类，实现了配置的加载、扩展类缓存、自适应对象生成等工作

Extension有三个主要逻辑入库：getExtension（获取普通扩展类）、getAdaptiveExtension（获取自适应扩展类）、getActivateExtension（获取自动激活的扩展类），其中getExtension是最核心的方法，是吸纳了一个完整的普通扩展类加载过程，加载过程中的每一步，都会先检查缓存中是否已经存在所需的数据，如果存在，则直接从缓存中读取，没有则重新加载，该方法每次只会根据名称返回一个扩展点实现类

### 2.3.1、ExtensionLoader初始化

其构造方法是私有的，一般同一个静态方法getExtensionLoader来获取ExtensionLoader的实例

```java
// 扩展类与对应的扩展类加载器缓存
private static final ConcurrentMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>(64);
private ExtensionLoader(Class<?> type) {
    this.type = type;
    // 判断构造时是否为 ExtensionFactory 类，一般在使用的时候这个type肯定不是 ExtensionFactory 类，又会回到ExtensionLoader.getExtensionLoader
    objectFactory = (type == ExtensionFactory.class ? null:ExtensionLoader.getExtensionLoader(ExtensionFactory.class).getAdaptiveExtension());
}
@SuppressWarnings("unchecked")
public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
    // 省略判断代码
    // 通过传入的 type 从缓存中获取对应的 ExtensionLoader，如果为空，构造一个新的ExtensionLoader
    ExtensionLoader<T> loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
    if (loader == null) {
        EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<T>(type));
        loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
    }
    return loader;
}
```
上面会去构造一个  ExtensionFactory 的实例，具体如何构造后面会有 ExtensionFactory的原理

### 2.3.2、getExtension实现原理

方法大致流程图：

![](image/Dubbo-ExtensionLoader.getExtension流程.png)

```java
// getExtension
public T getExtension(String name) {
    if (StringUtils.isEmpty(name)) {
        throw new IllegalArgumentException("Extension name == null");
    }
    // 获取默认的扩展
    if ("true".equals(name)) {
        return getDefaultExtension();
    }
    final Holder<Object> holder = getOrCreateHolder(name);
    Object instance = holder.get();
    if (instance == null) {
        synchronized (holder) {
            instance = holder.get();
            if (instance == null) {
                // 创建扩展
                instance = createExtension(name);
                holder.set(instance);
            }
        }
    }
    return (T) instance;
}
```
createExtension：
```java
// createExtension
private T createExtension(String name) {
    Class<?> clazz = getExtensionClasses().get(name);
    // ......
    try {
        T instance = (T) EXTENSION_INSTANCES.get(clazz);
        if (instance == null) {
            // 初始化Class
            EXTENSION_INSTANCES.putIfAbsent(clazz, clazz.newInstance());
            instance = (T) EXTENSION_INSTANCES.get(clazz);
        }
        // 向扩展类中注入依赖的属性
        injectExtension(instance);
        Set<Class<?>> wrapperClasses = cachedWrapperClasses;
        if (CollectionUtils.isNotEmpty(wrapperClasses)) {
            // 遍历扩展点包装类，用于初始化包装类实例
            for (Class<?> wrapperClass : wrapperClasses) {
                // 找到构造方法参数类型为type的包装类，为其注入扩展类实例
                instance = injectExtension((T) wrapperClass.getConstructor(type).newInstance(instance));
            }
        }
        // 如果是 Lifecycle 接口的实现类，会有相应的生命周期的变化
        initExtension(instance);
        return instance;
    } 
}
```
getExtensionClasses
```java
// getExtensionClasses
private Map<String, Class<?>> getExtensionClasses() {
    // 默认从缓存中获取classes，没有的话通过DCL加载Class
    Map<String, Class<?>> classes = cachedClasses.get();
    if (classes == null) {
        synchronized (cachedClasses) {
            classes = cachedClasses.get();
            if (classes == null) {
                // 加载Class
                classes = loadExtensionClasses();
                cachedClasses.set(classes);
            }
        }
    }
    return classes;
}
```
loadExtensionClasses
```java
private static final String SERVICES_DIRECTORY = "META-INF/services/";
private static final String DUBBO_DIRECTORY = "META-INF/dubbo/";
private static final String DUBBO_INTERNAL_DIRECTORY = DUBBO_DIRECTORY + "internal/";
// loadExtensionClasses
private Map<String, Class<?>> loadExtensionClasses() {
    // 缓存默认的扩展名，即通过SPI注解获取其对应的值，保存到属性 cachedDefaultName
    cacheDefaultExtensionName();
    Map<String, Class<?>> extensionClasses = new HashMap<>();
    // 从配置的三个配置文件目录中加载对应的Class，strategies表示的上述三个配置文件目录
    for (LoadingStrategy strategy : strategies) {
        loadDirectory(extensionClasses, strategy.directory(), type.getName(), strategy.preferExtensionClassLoader(), strategy.excludedPackages());
        loadDirectory(extensionClasses, strategy.directory(), type.getName().replace("org.apache", "com.alibaba"), strategy.preferExtensionClassLoader(), strategy.excludedPackages());
    }
    return extensionClasses;
}
```
loadDirectory：
```java
private void loadDirectory(Map<String, Class<?>> extensionClasses, String dir, String type, boolean extensionLoaderClassLoaderFirst, String... excludedPackages) {
    String fileName = dir + type;
    try {
        // 通过一系列方法查找资源文件
        if (urls != null) {
            while (urls.hasMoreElements()) {
                java.net.URL resourceURL = urls.nextElement();
                // 加载对应的资源
                loadResource(extensionClasses, classLoader, resourceURL, excludedPackages);
            }
        }
    } 
}
```
loadResource：
```java
private void loadResource(Map<String, Class<?>> extensionClasses, ClassLoader classLoader,java.net.URL resourceURL, String... excludedPackages) {
    try {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceURL.openStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() > 0) {
                    try {
                        
                        if (line.length() > 0 && !isExcluded(line, excludedPackages)) {
                            // 把Class加载到JVM中，但是并没有做初始化
                            loadClass(extensionClasses, resourceURL, Class.forName(line, true, classLoader), name);
                        }
                    } 
                }
            }
        }
    } catch (Throwable t) {
        logger.error("Exception occurred when loading extension class (interface: " +
                type + ", class file: " + resourceURL + ") in " + resourceURL, t);
    }
}
```
loadClass：
```java
private void loadClass(Map<String, Class<?>> extensionClasses, java.net.URL resourceURL, Class<?> clazz, String name) throws NoSuchMethodException {
    if (clazz.isAnnotationPresent(Adaptive.class)) {
        // 如果是自适应（Adaptive）则缓存，缓存的自适应只有一个，如果存在多个会抛出异常
        cacheAdaptiveClass(clazz);
    } else if (isWrapperClass(clazz)) {
        // 如果是包装类扩展，则直接加入到对应的Set集合中
        cacheWrapperClass(clazz);
    } else {
        clazz.getConstructor();
        if (StringUtils.isEmpty(name)) {
            name = findAnnotationName(clazz);
            if (name.length() == 0) {
                throw new IllegalStateException("No such extension name for the class " + clazz.getName() + " in the config " + resourceURL);
            }
        }
        String[] names = NAME_SEPARATOR.split(name);
        if (ArrayUtils.isNotEmpty(names)) {
            // 如果有自动激活注解（Activate），则缓存到自动激活的缓存中
            cacheActivateClass(clazz, names[0]);
            // 普通的扩展类缓存
            for (String n : names) {
                cacheName(clazz, n);
                saveInExtensionClass(extensionClasses, clazz, n);
            }
        }
    }
}
```

### 2.3.3、getAdaptiveExtension实现原理

获取自适应扩展的流程大致如下：

![](image/Dubbo-ExtensionLoader.getAdaptiveExtension流程.png)

```java
public T getAdaptiveExtension() {
    // 从缓存中拿取数据
    Object instance = cachedAdaptiveInstance.get();
    if (instance == null) {
        if (createAdaptiveInstanceError != null) {
            throw new IllegalStateException("Failed to create adaptive instance: " +
                    createAdaptiveInstanceError.toString(),
                    createAdaptiveInstanceError);
        }
        // DCL
        synchronized (cachedAdaptiveInstance) {
            instance = cachedAdaptiveInstance.get();
            if (instance == null) {
                try {
                    // 创建自适应扩展，并缓存
                    instance = createAdaptiveExtension();
                    cachedAdaptiveInstance.set(instance);
                } catch (Throwable t) {
                    createAdaptiveInstanceError = t;
                    throw new IllegalStateException("Failed to create adaptive instance: " + t.toString(), t);
                }
            }
        }
    }

    return (T) instance;
}
```
createAdaptiveExtension：
```java
private T createAdaptiveExtension() {
    try {
        /*
            主要做了三件时间：
            1、调用 getAdaptiveExtensionClass 方法获取自适应拓展 Class 对象
            2、通过反射进行实例化
            3、调用 injectExtension 方法向拓展实例中注入依赖；Dubbo 中有两种类型的自适应扩展，一种是手工编码的，一种是自动生成的。
            手工编码的自适应扩展中可能存在着一些依赖，而自动生成的 Adaptive 扩展则不会依赖其他类；这里主要是为手工编码的自适应扩展设置依赖
            */
        return injectExtension((T) getAdaptiveExtensionClass().newInstance());
    } catch (Exception e) {
        throw new IllegalStateException("Can't create adaptive extension " + type + ", cause: " + e.getMessage(), e);
    }
}
```
getAdaptiveExtensionClass：
```java
private Class<?> getAdaptiveExtensionClass() {
    // 通过 SPI 获取所有的扩展类
    getExtensionClasses();
    // 检查缓存，如果缓存不为空，则直接返回缓存
    if (cachedAdaptiveClass != null) {
        return cachedAdaptiveClass;
    }
    // 创建自适应扩展类
    return cachedAdaptiveClass = createAdaptiveExtensionClass();
}
private Class<?> createAdaptiveExtensionClass() {
    // 生成自适应扩展代码：构造一个 AdaptiveClassCodeGenerator 实例，需要具体的类型以及缓存的默认扩展，调用generate方法生成自适应扩展代码
    String code = new AdaptiveClassCodeGenerator(type, cachedDefaultName).generate();
    // 获取到对应的类加载器
    ClassLoader classLoader = findClassLoader();
    org.apache.dubbo.common.compiler.Compiler compiler = ExtensionLoader.getExtensionLoader(org.apache.dubbo.common.compiler.Compiler.class).getAdaptiveExtension();
    return compiler.compile(code, classLoader);
}
```
AdaptiveClassCodeGenerator.generate
```java
public String generate() {
    // 判断type是否有 @Adaptive 注解的方法，如果没有，直接抛出异常
    if (!hasAdaptiveMethod()) {
        throw new IllegalStateException("No adaptive method exist on extension " + type.getName() + ", refuse to create the adaptive class!");
    }
    StringBuilder code = new StringBuilder();
    // 生成包名
    code.append(generatePackageInfo());
    // 生成导入信息
    code.append(generateImports());
    // 生成类声明
    code.append(generateClassDeclaration());
    Method[] methods = type.getMethods();
    for (Method method : methods) {
        // 生成方法列表：如果是未注解 Adaptive的方法，生成的方法内容是抛出 UnsupportedOperationException 异常信息
        code.append(generateMethod(method));
    }
    code.append("}");
    if (logger.isDebugEnabled()) {
        logger.debug(code.toString());
    }
    return code.toString();
}
```

### 2.3.4、getActivateExtension实现原理

```java
public List<T> getActivateExtension(URL url, String[] values, String group) {
    List<T> activateExtensions = new ArrayList<>();
    List<String> names = values == null ? new ArrayList<>(0) : Arrays.asList(values);
    // names里面不包含"-default"的value，即如果有该值，则所有的默认的 @Activate都不会被激活，只有URL参数指定的扩展点会被激活
    if (!names.contains(REMOVE_VALUE_PREFIX + DEFAULT_KEY)) {
        getExtensionClasses();
        for (Map.Entry<String, Object> entry : cachedActivates.entrySet()) {
            String name = entry.getKey();
            Object activate = entry.getValue();

            String[] activateGroup, activateValue;

            if (activate instanceof Activate) {
                activateGroup = ((Activate) activate).group();
                activateValue = ((Activate) activate).value();
            } else if (activate instanceof com.alibaba.dubbo.common.extension.Activate) {
                activateGroup = ((com.alibaba.dubbo.common.extension.Activate) activate).group();
                activateValue = ((com.alibaba.dubbo.common.extension.Activate) activate).value();
            } else {
                continue;
            }
            if (isMatchGroup(group, activateGroup)
                    && !names.contains(name)
                    && !names.contains(REMOVE_VALUE_PREFIX + name)
                    && isActive(activateValue, url)) {
                activateExtensions.add(getExtension(name));
            }
        }
        activateExtensions.sort(ActivateComparator.COMPARATOR);
    }
    List<T> loadedExtensions = new ArrayList<>();
    for (int i = 0; i < names.size(); i++) {
        String name = names.get(i);
        // 如果传入的"-"符号开头的扩展点名，则该扩展点也不会被自动激活；
        if (!name.startsWith(REMOVE_VALUE_PREFIX)
                && !names.contains(REMOVE_VALUE_PREFIX + name)) {
            if (DEFAULT_KEY.equals(name)) {
                if (!loadedExtensions.isEmpty()) {
                    activateExtensions.addAll(0, loadedExtensions);
                    loadedExtensions.clear();
                }
            } else {
                loadedExtensions.add(getExtension(name));
            }
        }
    }
    if (!loadedExtensions.isEmpty()) {
        activateExtensions.addAll(loadedExtensions);
    }
    return activateExtensions;
}
```
主线流程主要有四步：
- （1）检查缓存，如果缓存中没有，则初始化所有扩展类实现的集合；
- （2）遍历整个 @Activate 注解集合，根据传入URL匹配条件（匹配group、name等），得到所有符合激活条件的扩展类实现。然后根据 @Activate 中配置的before、after、order等参数进行排序；
- （3）遍历所有与用户自定义扩展类名称，根据用户URL配置的属性，调整扩展点集合顺序；
- （4）返回所有自动激活类集合；

### 2.3.5、ExtensionFactory实现原理

## 2.2、Dubbo中已经实现的扩展

![](image/Dubbo-SPI.png)

```
random=com.alibaba.dubbo.rpc.cluster.loadbalance.RandomLoadBalance
roundrobin=com.alibaba.dubbo.rpc.cluster.loadbalance.RoundRobinLoadBalance
leastactive=com.alibaba.dubbo.rpc.cluster.loadbalance.LeastActiveLoadBalance
consistenthash=com.alibaba.dubbo.rpc.cluster.loadbalance.ConsistentHashLoadBalance
```

## 2.3、自定义扩展的实现，以Filter为例

- 编写Filter的实现类：
    ```java
    public class MyFilter implements Filter {
        @Override
        public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
            return null;
        }
    }
    ```
- 在resources目录下新建META-INF/dubbo/com.alibaba.dubbo.rpc.Filter文件；
- 在该文件中填入如下内容：`MyFilterExt=com.blue.fish.dubbo.provider.filter.MyFilter`，等号后面是自定义Filter的实现类全路径；
- 如果需要引用该Filter的话，在对应的配置上加上即可，比如：`<dubbo:provider filter="MyFilterExt"/>`；

## 2.5、SPI与IOC

objectFactory就是dubbo的IoC提供对象。SpiExtensionFactory