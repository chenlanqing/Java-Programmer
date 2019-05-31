<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
*目录**

- [1、什么是单例](#1%E4%BB%80%E4%B9%88%E6%98%AF%E5%8D%95%E4%BE%8B)
- [2、懒汉模式](#2%E6%87%92%E6%B1%89%E6%A8%A1%E5%BC%8F)
  - [2.1、基本写法](#21%E5%9F%BA%E6%9C%AC%E5%86%99%E6%B3%95)
  - [2.2、保证线程安全](#22%E4%BF%9D%E8%AF%81%E7%BA%BF%E7%A8%8B%E5%AE%89%E5%85%A8)
  - [2.3、防止反射](#23%E9%98%B2%E6%AD%A2%E5%8F%8D%E5%B0%84)
  - [2.4、防止序列化](#24%E9%98%B2%E6%AD%A2%E5%BA%8F%E5%88%97%E5%8C%96)
  - [2.5、完整懒汉式](#25%E5%AE%8C%E6%95%B4%E6%87%92%E6%B1%89%E5%BC%8F)
- [3、恶汉模式](#3%E6%81%B6%E6%B1%89%E6%A8%A1%E5%BC%8F)
  - [3.1、基本写法](#31%E5%9F%BA%E6%9C%AC%E5%86%99%E6%B3%95)
  - [3.2、完整饿汉式](#32%E5%AE%8C%E6%95%B4%E9%A5%BF%E6%B1%89%E5%BC%8F)
  - [3.3、Runtime类](#33runtime%E7%B1%BB)
- [4、双重校验锁](#4%E5%8F%8C%E9%87%8D%E6%A0%A1%E9%AA%8C%E9%94%81)
  - [4.1、基本实现](#41%E5%9F%BA%E6%9C%AC%E5%AE%9E%E7%8E%B0)
  - [4.2、改进](#42%E6%94%B9%E8%BF%9B)
- [5、静态内部类](#5%E9%9D%99%E6%80%81%E5%86%85%E9%83%A8%E7%B1%BB)
  - [5.1、基本写法](#51%E5%9F%BA%E6%9C%AC%E5%86%99%E6%B3%95)
  - [5.2、防止反射](#52%E9%98%B2%E6%AD%A2%E5%8F%8D%E5%B0%84)
- [6、枚举实现](#6%E6%9E%9A%E4%B8%BE%E5%AE%9E%E7%8E%B0)
- [7、反序列化问题](#7%E5%8F%8D%E5%BA%8F%E5%88%97%E5%8C%96%E9%97%AE%E9%A2%98)
- [8、容器单例](#8%E5%AE%B9%E5%99%A8%E5%8D%95%E4%BE%8B)
- [9、ThreadLocal与单例](#9threadlocal%E4%B8%8E%E5%8D%95%E4%BE%8B)
- [10、单例与JVM垃圾回收](#10%E5%8D%95%E4%BE%8B%E4%B8%8Ejvm%E5%9E%83%E5%9C%BE%E5%9B%9E%E6%94%B6)
  - [10.1、分析思路](#101%E5%88%86%E6%9E%90%E6%80%9D%E8%B7%AF)
- [11、总结](#11%E6%80%BB%E7%BB%93)
- [参考资料](#%E5%8F%82%E8%80%83%E8%B5%84%E6%96%99)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# 1、什么是单例

只能有一个实例、单例类必须自己创建自己的唯一实例、单例类必须给所有其他对象提供这一实例。如：配置文件、线程池、工具类、缓存、日志对象；在应用中如果有两个或者两个以上的实例会引起错误，又或者换句话说，就是这些类，在整个应用中，同一时刻，有且只能有一种状态

单例的实现方式：懒汉、恶汉、双重检验锁、静态内部类、枚举

# 2、懒汉模式

## 2.1、基本写法

- 是否 Lazy 初始化：是
- 是否多线程安全：否
- 实现难度：易
- 最大缺点：不支持多线程

```java
@NonThreadSafe
public class LazySingleton {
    private static LazySingleton lazySingleton;
    private LazySingleton(){}

    public static LazySingleton getInstance(){
        if (lazySingleton == null) {
            lazySingleton = new LazySingleton();
        }
        return lazySingleton;
    }
}
```

## 2.2、保证线程安全

```java
@ThreadSafe
public class LazyThreadSafeSingleton {
    private static LazyThreadSafeSingleton lazySingleton;
    private LazyThreadSafeSingleton(){}
    public synchronized static LazyThreadSafeSingleton getInstance(){
        if (lazySingleton == null) {
            lazySingleton = new LazyThreadSafeSingleton();
        }
        return lazySingleton;
    }
}
```

## 2.3、防止反射

懒汉模式的写法多线程不能完全防止反射的发生

```java
public class LazySingletonAvoidReflection {
    // 主要是构造器中加入判断，如果实例不为空，则抛异常
    private LazySingletonAvoidReflection(){
        if (lazySingleton != null){
            throw new RuntimeException("单例不能通过反射构建实例");
        }
    }
}
```

## 2.4、防止序列化

只需要在对应的单例应用中加入如下方法
```java
private Object readResolve(){
    return lazySingleton;
}
```

## 2.5、完整懒汉式

```java
public class LazySingletonComplete implements Serializable{
    private static LazySingletonComplete instance;
    private LazySingletonComplete(){
        if (instance != null){
            throw new RuntimeException("单例不能通过反射构建实例");
        }
    }
    public static LazySingletonComplete getInstance(){
        synchronized (LazySingletonComplete.class) {
            if (instance == null){
                instance = new LazySingletonComplete();
            }
            return instance;
        }
    }
    private Object readResolve(){
        return instance;
    }
}
```

# 3、恶汉模式

- 是否 Lazy 初始化：否
- 是否多线程安全：是
- 实现难度：易
- 优点：没有加锁，执行效率会提高
- 缺点：类加载时就初始化，浪费内存

## 3.1、基本写法

```java
@ThreadSafe
public class HungrySingleton {
    private final static HungrySingleton hungrySingleton = new HungrySingleton();
    private HungrySingleton(){}
    private static HungrySingleton getInstance(){
        return hungrySingleton;
    }
}
```

## 3.2、完整饿汉式

防止反射与序列化

```java
@ThreadSafe
public class HungrySingleton {
    private final static HungrySingleton hungrySingleton = new HungrySingleton();
    private HungrySingleton(){
        if (hungrySingleton != null){
            throw new RuntimeException("单例不能通过反射构建实例");
        }
    }
    private static HungrySingleton getInstance(){
        return hungrySingleton;
    }
    private Object readResolve(){
        return hungrySingleton;
    }
}
```

## 3.3、Runtime类

JDK中的`Runtime`类使用的就是恶汉模式来实现的
```java
public class Runtime {
    private static Runtime currentRuntime = new Runtime();
    public static Runtime getRuntime() {
        return currentRuntime;
    }
    private Runtime() {}
}
```

# 4、双重校验锁

## 4.1、基本实现

```java
public class LazyDoubleCheckSingleton {
    private static LazyDoubleCheckSingleton lazySingleton;
    private LazyDoubleCheckSingleton(){}
    public static LazyDoubleCheckSingleton getInstance(){
        if (lazySingleton == null) {
            synchronized (LazyDoubleCheckSingleton.class) {
                if (lazySingleton == null) {
                    lazySingleton = new LazyDoubleCheckSingleton();
                }
            }
        }
        return lazySingleton;
    }
}
```
存在问题：
- 上述代码也有问题：`lazySingleton = new LazyDoubleCheckSingleton();` 不是原子操作，其在JVM上大概做了三件事情：
	- （1）给 lazySingleton 分配内存
	- （2）调用 LazyDoubleCheckSingleton 的构造函数来初始化成员变量;
	- （3）将 lazySingleton 对象指向分配的内存空间（执行完这步 singleton 就为非 null 了）

但是在 JVM 的即时编译器中存在指令重排序的优化，上面的第二步骤和第三步骤执行的顺序是不能保障的；如果在步骤3执行完毕，步骤2还未执行之前，被其他线程抢占，这时 lazySingleton 已经是非 null 了但是未被初始化；所以线程会直接返回 lazySingleton 然后使用，然后顺理成章地报错；

## 4.2、改进

volatile 会禁止指令重排序优化

```java
public class LazyDoubleCheckSingleton {
    private volatile static LazyDoubleCheckSingleton lazySingleton;
    private LazyDoubleCheckSingleton(){}
    public static LazyDoubleCheckSingleton getInstance(){
        if (lazySingleton == null) {
            synchronized (LazyDoubleCheckSingleton.class) {
                if (lazySingleton == null) {
                    lazySingleton = new LazyDoubleCheckSingleton();
                }
            }
        }
        return lazySingleton;
    }
}
```

***注意：***
在Java5以前的版本使用了volatile的双检锁还是有问题的，Java5以前的JMM（Java 内存模型）是存在缺陷的，即时将变量声明成volatile也不能完全避免重排序，主要是volatile变量前后的代码仍然存在重排序问题

# 5、静态内部类

推荐的单例写法

## 5.1、基本写法

```java
public class StaticInnerClassSingleton {
    private static class StaticInnerClassSingletonHolder {
        private static final StaticInnerClassSingleton INSTANCE = new StaticInnerClassSingleton();
    }
    private StaticInnerClassSingleton(){}
    public static StaticInnerClassSingleton getInstance(){
        return StaticInnerClassSingletonHolder.INSTANCE;
    }
    // 防止序列化
    private Object readResolve() {
        return getInstance();
    }
}
```

- 这种方式同样利用了classloder的机制来保证初始化instance时只有一个线程，跟饿汉式不同的是：饿汉式只要 StaticInnerClassSingleton 类被装载了，那么instance就会被实例化（没有达到lazy loading效果），而这种方式是 StaticInnerClassSingleton 类被装载了，instance不一定被初始化，因为 StaticInnerClassSingletonHolder 类没有被主动使用，只有显示通过调用getInstance方法时，才会显示装载StaticInnerClassSingletonHolder类，从而实例化instance：

	* 加载一个类时，其内部类不会同时被加载；
	* 一个类被加载，当且仅当其某个静态成员（静态域、构造器、静态方法等）被调用时发生

- 由于对象实例化是在内部类加载的时候创建的，因此是线程安全的（因为在方法中创建，才存在并发问题，静态内部类随着方法调用而被加载，只加载一次，不存在并发问题）
- 反射会破坏上述单例结构

## 5.2、防止反射

只要通过变量来控制的，都无法完全防止反射的发生

```java
public class InnerClassSingleton {
    private static boolean initialized = false;
    private InnerClassSingleton(){
        synchronized (InnerClassSingleton.class){
            if (!initialized){
                initialized = !initialized;
            } else {
                throw new RuntimeException("无法构造单例");
            }
        }
    }
    static class InnerClassSingletonHolder{
        private static final InnerClassSingleton instance = new InnerClassSingleton();
    }
    public static InnerClassSingleton getInstance() {
        return InnerClassSingletonHolder.instance;
    }
}
```

# 6、枚举实现

目前最好的方式，避免了反射的攻击和序列化的问题

```java
public enum SingletonEnum {
	INSTANCE;
	// 在反射时，通过私有构造器newInstance是会抛出非法参数异常：IllegalArgumentException
	// Exception in thread "main" java.lang.IllegalArgumentException: Cannot reflectively create enum objects
	public static void main(String... args)throws Exception{
		Constructor[] array = SingletonEnum.INSTANCE.getClass().getDeclaredConstructors();
		for (Constructor constructor : array) {
			constructor.setAccessible(true);
			constructor.newInstance(null);
		}
	}
}
```

[枚举本质](https://github.com/chenlanqing/learningNote/blob/master/Java/JavaSE/Java%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86_2.md#%E5%8D%81%E5%85%AB%E6%9E%9A%E4%B8%BE%E7%B1%BB)

# 7、反序列化问题

在反序列化的过程中到底发生了什么，使得反序列化后的单例不是唯一的？分析一下 ObjectInputputStream 的readObject 方法执行情况到底是怎样的

- ObjectInputStream 的readObject的调用栈：readObject--->readObject0--->readOrdinaryObject--->checkResolve

- 看下 readOrdinaryObject方法的代码片段			
```java
/**
 * 这里创建的这个obj对象，就是本方法要返回的对象，也可以暂时理解为是ObjectInputStream的readObject返回的对象
 * (1).isInstantiable：如果一个serializable/externalizable的类可以在运行时被实例化，那么该方法就返回true.
 * 针对serializable和externalizable我会在其他文章中介绍.
 * (2).desc.newInstance：该方法通过反射的方式调用无参构造方法新建一个对象
 */
Object obj;
try {
	obj = desc.isInstantiable() ? desc.newInstance() : null;
} catch (Exception ex) {
	throw (IOException) new InvalidClassException(desc.forClass().getName()，"unable to create instance").initCause(ex);
}
```

- 结论：为什么序列化可以破坏单例了？ 序列化会通过反射调用无参数的构造方法创建一个新的对象
```java
/**
 * hasReadResolveMethod:如果实现了serializable 或者 externalizable接口的类中包含readResolve则返回true
 * invokeReadResolve:通过反射的方式调用要被反序列化的类的readResolve方法
 */
if (obj != null &&	handles.lookupException(passHandle) == null &&	desc.hasReadResolveMethod()){
	Object rep = desc.invokeReadResolve(obj);
	if (unshared && rep.getClass().isArray()) {
		rep = cloneArray(rep);
	}
	if (rep != obj) {
		handles.setObject(passHandle， obj = rep);
	}
}
```
- 如何防止序列化/反序列化破坏单例模式：在类中定义readResolve就可以解决该问题，定义的readResolve方法，并在该方法中指定要返回的对象的生成策略，就可以防止单例被破坏
```java
/**
 * 使用双重校验锁方式实现单例
 */
public class Singleton implements Serializable{
	private volatile static Singleton singleton;
	private Singleton (){
		// 防止反射创建新的实例
		if (singleton != null){
			throw new IllegalArgumentException("cannot exist two instance");
		}
	}
	public static Singleton getSingleton() {
		if (singleton == null) {
			synchronized (Singleton.class) {
				if (singleton == null) {
					singleton = new Singleton();
				}
			}
		}
		return singleton;
	}
	private Object readResolve() {
		return singleton;
	}
}
```

# 8、容器单例

```java
public class ContainerSingleton {
    private static Map<String, Object> singleMap = new HashMap<>();
    private ContainerSingleton(){}

    public static void putInstance(String key, Object instance) {
        if (StringUtils.isNotBlank(key) && instance != null) {
            if (!singleMap.containsKey(key)){
                singleMap.put(key,instance);
            }
        }
    }
    public static Object getInstance(String key) {
        return singleMap.get(key);
    }
}
```

可以参考`java.awt.Desktop`，其中有使用该类单例

如果应用中有比较多的单例，可以通过容器来进行管理，类比SPring的IOC容器

# 9、ThreadLocal与单例

ThreadLocal 可以确保在一个线程中对象是唯一的，但是无法保证在整个应用中对象时唯一的，如Mybatis的`ErrorContext`类

```java
public class ThreadLocalSingleton {
    private static ThreadLocal<ThreadLocalSingleton> instance = new ThreadLocal<>(){
        @Override
        protected Object initialValue() {
            return new ThreadLocalSingleton();
        }
    };
    private ThreadLocalSingleton(){}
    public static ThreadLocalSingleton getInstance(){
        return instance.get();
    }
}
```

# 10、单例与JVM垃圾回收

当一个单例的对象长久不用时，会不会被jvm的垃圾收集机制回收?(Hotspot 虚拟机)

## 10.1、分析思路

- Hotspot 垃圾收集算法:GC Root
- 方法区的垃圾收集方法，JVM卸载类的判断方法

## 单例与static

当需要共享的变量很多时，使用static变量占用内存的时间过长，在类的整个生命周期，而对象只是存在于对象的整个生命周期。卸载一个实例比卸载一个类容易；

使用单例模式可以限制对象实例的个数，除了返回实例的操作之外不能被new出来。这在某些需要限制对象访问的场合下是有用的。使用static的话并不能限制对象实例的个数

# 11、总结

有两个问题需要注意

- 如果单例由不同的类装载器装入，那便有可能存在多个单例类的实例。假定不是远端存取，例如一些servlet容器对每个servlet使用完全不同的类装载器，这样的话如果有两个servlet访问一个单例类，它们就都会有各自的实例，解决方案:
```java
private static Class getClass(String classname)throws ClassNotFoundException {
	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		if(classLoader == null)     
			classLoader = Singleton.class.getClassLoader();     

		return (classLoader.loadClass(classname));     
	}     
}  
```
- 如果Singleton实现了java.io.Serializable 接口，那么这个类的实例就可能被序列化和复原。不管怎样，如果你序列化一个单例类的对象，接下来复原多个那个对象，那你就会有多个单例类的实例:
```java
public class Singleton implements java.io.Serializable {     
	public static Singleton INSTANCE = new Singleton(); 
	protected Singleton() { }     
	private Object readResolve() {     
			return INSTANCE;     
	}    
} 
```

- 如果单例接口实现了cloneable接口，那么有可能会破坏单例模式，所以在重写clone方法时需要特殊处理
```java
public class Singleton implements Cloneable{
    private Singleton(){}
    private static Singleton instance = new Singleton();
    public static Singleton getInstance(){
        return instance;
    } 
    protected Object clone() throws UnsupportedException{
        return getInstance();
    }
}
```

# 12、不使用synchronized和lock实现线程安全的单例

上面的代码要么显示或者隐式的使用了synchronized或者lock，饿汉模式和内部类模式是因为类的初始化是由ClassLoader完成的，这其实就是利用了ClassLoader的线程安全机制啊；ClassLoader的loadClass方法在加载类的时候使用了synchronized关键字；

可以使用CAS来实现线程安全的单例；借助CAS（AtomicReference）实现单例模式

```java
public class CASSingleton {
    private static AtomicReference<CASSingleton> INSTANCE = new AtomicReference<>();
    private CASSingleton(){}
    public static CASSingleton getInstance() {
        for (;;){
            CASSingleton instance = INSTANCE.get();
            // 如果singleton不为空，就返回singleton
            if (instance != null) {
                return instance;
            }
            instance = new CASSingleton();
            // CAS操作，预期值是NULL，新值是singleton
            // 如果成功，返回singleton
            // 如果失败，进入第二次循环，singletonAtomicReference.get()就不会为空了
            if (INSTANCE.compareAndSet(null, instance)) {
                return instance;
            }
        }
    }
}
```



# 参考资料

* [单例模式的七种写法](http://www.hollischuang.com/archives/205)
* [实现牛逼的单例模式](http://www.cnblogs.com/rjzheng/p/8946889.html)
* [单例与序列化](http://www.hollischuang.com/archives/1144)
* [单例模式与垃圾回收](http://blog.csdn.net/zhengzhb/article/details/7331354)
