# 一、Java基础

## 1、final

### 1.1、所有的final修饰的字段都是编译期常量吗？

```java
public class Test {
    //编译期常量
    final int i = 1;
    final static int J = 1;
    final int[] a = {1,2,3,4};
    //非编译期常量
    Random r = new Random();
    final int k = r.nextInt();

    public static void main(String[] args) {
    }
}
```
k的值由随机数对象决定，所以不是所有的final修饰的字段都是编译期常量，只是k的值在被初始化后无法被更改

### 1.2、说说final类型的类如何拓展? 

比如String是final类型，我们想写个MyString复用所有String中方法，同时增加一个新的toMyString()的方法，应该如何做?

一种是组合关系。所以当遇到不能用继承的(final修饰的类),应该考虑用组合, 如下代码大概写个组合实现的意思：
```java
class MyString{
    private String innerString;
    // ...init & other methods
    // 支持老的方法
    public int length(){
        return innerString.length(); // 通过innerString调用老的方法
    }
    // 添加新方法
    public String toMyString(){
        //...
    }
}
```

## 2、Stream流中存在问题

- 可读性差：由于Stream流操作过于复杂，可能会导致代码可读性变差，不易维护。尤其是在涉及多个Stream流操作的情况下，代码会变得更加复杂，可能会导致性能问题。
- 性能问题：Stream流的操作需要进行多次迭代，而迭代本身就是一项相对耗时的操作。因此，在处理大数据集合时，Stream流可能会比传统的循环方式慢得多。
- 并行流使用不当可能导致性能问题：虽然Java 8引入了并行流来提高性能，但是并行流不一定总是比顺序流快。在使用并行流时，需要注意避免共享变量、避免阻塞、避免过度分区等问题，否则可能会导致性能问题。
- 对于I/O操作的支持不足：Stream流并没有提供对I/O操作的全面支持，因此在处理I/O操作时，可能需要使用传统的流方式。
- 对于可变数据结构的支持不足：Stream流本身是基于不可变数据结构实现的，因此在对于可变数据结构的处理时，需要进行一些额外的操作

### 2.1、List转Map问题

```java
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private String id;
    private String name;
}
public static void main(String[] args) {
    List<Person> list = new ArrayList<>();
    list.add(Person.builder().id("1").name("A").build());
    list.add(Person.builder().id("2").name("B").build());
    list.add(Person.builder().id("3").name("C").build());
    list.add(Person.builder().id("4").name("D").build());
    list.add(Person.builder().id("3").name("E").build());
    final Map<String, String> map = list.stream().collect(Collectors.toMap(Person::getId, Person::getName);
    System.out.println(map);
}
```
上面main方法在执行时会报错：
```
Exception in thread "main" java.lang.IllegalStateException: Duplicate key 3 (attempted merging values C and E)
	at java.base/java.util.stream.Collectors.duplicateKeyException(Collectors.java:133)
	at java.base/java.util.stream.Collectors.lambda$uniqKeysMapAccumulator$1(Collectors.java:180)
	at java.base/java.util.stream.ReduceOps$3ReducingSink.accept(ReduceOps.java:169)
	at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1654)
	at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
	at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
	at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:913)
	at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
	at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:578)
	at com.blue.fish.example.base.stream.ListToMap.main(ListToMap.java:24)
```
为了解决上述key重复的问题，需要对Collectors.toMap方法增加第三个参数：
```java
final Map<String, String> map = list.stream()
.collect(Collectors.toMap(Person::getId, Person::getName, (oldValue, newValue) -> newValue));
```
`(oldValue, newValue) -> oldValue` 表示：如果key是重复的，你选择oldKey or newKey?

## 3、注解支持继承吗？

`@Inherited` 只能实现类上的注解继承。要想实现方法上注解的继承，你可以通过反射在继承链上找到方法上的注解。但这样实现起来很繁琐，而且需要考虑桥接方法。

Spring 提供了 AnnotatedElementUtils 类，来方便我们处理注解的继承问题。这个类的 findMergedAnnotation 工具方法，可以帮助我们找出父类和接口、父类方法和接口方法上的注解，并可以处理桥接方法，实现一键找到继承链的注解：
```java

Child child = new Child();
log.info("ChildClass:{}", getAnnotationValue(AnnotatedElementUtils.findMergedAnnotation(child.getClass(), MyAnnotation.class)));
log.info("ChildMethod:{}", getAnnotationValue(AnnotatedElementUtils.findMergedAnnotation(child.getClass().getMethod("foo"), MyAnnotation.class)));
```
总结：自定义注解可以通过标记元注解 @Inherited 实现注解的继承，不过这只适用于类。如果要继承定义在接口或方法上的注解，可以使用 Spring 的工具类 AnnotatedElementUtils，并注意各种 getXXX 方法和 findXXX 方法的区别，详情查看

**关于注解继承问题，你觉得 Spring 的常用注解 @Service、@Controller 是否支持继承呢？**

Spring 的常用注解 @Service、@Controller，不支持继承。这些注解只支持放到具体的（非接口非抽象）顶层类上（来让它们成为 Bean），如果支持继承会非常不灵活而且容易出错。

## 4、synthetic 方法

泛型类型擦除后会生成一个 bridge 方法，这个方法同时又是 synthetic 方法。除了泛型类型擦除，你知道还有什么情况编译器会生成 synthetic 方法吗？

Synthetic 方法是编译器自动生成的方法（在源码中不出现）。除了文中提到的泛型类型擦除外，Synthetic 方法还可能出现的一个比较常见的场景，是内部类和顶层类需要相互访问对方的 private 字段或方法的时候；

编译后的内部类和普通类没有区别，遵循 private 字段或方法对外部类不可见的原则，但语法上内部类和顶层类的私有字段需要可以相互访问。为了解决这个矛盾，编译器就只能生成桥接方法，也就是 Synthetic 方法，来把 private 成员转换为 package 级别的访问限制

比如如下代码，InnerClassApplication 类的 test 方法需要访问内部类 MyInnerClass 类的私有字段 name，而内部类 MyInnerClass 类的 test 方法需要访问外部类 InnerClassApplication 类的私有字段 gender：
```java
public class InnerClassApplication {
    private String gender = "male";
    public static void main(String[] args) throws Exception {
        InnerClassApplication application = new InnerClassApplication();
        application.test();
    }
    private void test(){
        MyInnerClass myInnerClass = new MyInnerClass();
        System.out.println(myInnerClass.name);
        myInnerClass.test();
    }
    class MyInnerClass {
        private String name = "zhuye";
        void test(){
            System.out.println(gender);
        }
    }
}
```
编译器会为 InnerClassApplication 和 MyInnerClass 都生成桥接方法

## 5、Collectors 类提供了很多现成的收集器，有没有办法实现自定义的收集器

比如，实现一个 MostPopularCollector，来得到 List 中出现次数最多的元素，满足下面两个测试用例：
```java
assertThat(Stream.of(1, 1, 2, 2, 2, 3, 4, 5, 5).collect(new MostPopularCollector<>()).get(), is(2));
assertThat(Stream.of('a', 'b', 'c', 'c', 'c', 'd').collect(new MostPopularCollector<>()).get(), is('c'));
```
实现思路：通过一个 HashMap 来保存元素的出现次数，最后在收集的时候找出 Map 中出现次数最多的元素：
```java
public class MostPopularCollector<T> implements Collector<T, Map<T, Integer>, Optional<T>> {
    //使用HashMap保存中间数据
    @Override
    public Supplier<Map<T, Integer>> supplier() {
        return HashMap::new;
    }
    //每次累积数据则累加Value
    @Override
    public BiConsumer<Map<T, Integer>, T> accumulator() {
        return (acc, elem) -> acc.merge(elem, 1, (old, value) -> old + value);
    }
    //合并多个Map就是合并其Value
    @Override
    public BinaryOperator<Map<T, Integer>> combiner() {
        return (a, b) -> Stream.concat(a.entrySet().stream(), b.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, summingInt(Map.Entry::getValue)));
    }
    //找出Map中Value最大的Key
    @Override
    public Function<Map<T, Integer>, Optional<T>> finisher() {
        return (acc) -> acc.entrySet().stream().reduce(BinaryOperator.maxBy(Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey);
    }
    @Override
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }
}
```

## 6、a=a+b和a+=b的区别

- [复合运算符](https://docs.oracle.com/javase/specs/jls/se14/html/jls-15.html#jls-15.26.2)

`a+=b` 等价于 `a = (type of a) (a + b)`; type of a 表示的是a的类型

- `a+=b` 会自动转换为其进行类型转换；
- `a=a+b` 需要显示的为其进行类型的转换；

## 7、如何快速判断一个值是否等于某值

 现在我有9个int类型组成一个数组，{2, 2, 6, 6, 5, 8, 8, 1, 1}
如何用最快的方式找出唯一的一个5
答：亦或算法

## 8、如何进行深拷贝，除了clone

## 9、创建对象的方式

在 Java 程序中，拥有多种新建对象的方式。除了最为常见的 new 语句之外，还可以通过
- 反射机制：使用Class类的newInstance()方法创建一个对象（Java9以后是过期的方法）、使用 java.lang.reflect.Constructor#newInstance 方法
- Object.clone 方法
- 反序列化
- Unsafe.allocateInstance 方法来新建对象。

其中，Object.clone 方法和反序列化通过直接复制已有的数据，来初始化新建对象的实例字段。Unsafe.allocateInstance 方法则没有初始化实例字段，而 new 语句和反射机制，则是通过调用构造器来初始化实例字段。
```java
/**
 * 通过序列化与反序列化创建对象
 */
public static void createBySerializable() throws Exception{
    // 构建对象
    Person person = new Person(UUID.randomUUID().toString(), "Serializable");
    // 将对象写入到字节数组
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
    objectOutputStream.writeObject(person);
    objectOutputStream.close();
    // 读取数据
    ByteArrayInputStream in = new ByteArrayInputStream(outputStream.toByteArray());
    ObjectInputStream inputStream = new ObjectInputStream(in);
    Person object = (Person) inputStream.readObject();
}
/**
 * Unsafe.allocateInstance 创建镀锡
 */
public static void createByUnsafe() throws Exception{
    Field f = Unsafe.class.getDeclaredField("theUnsafe");
    f.setAccessible(true);
    Unsafe unsafe = (Unsafe) f.get(null);
    Object person = unsafe.allocateInstance(Person.class);
    System.out.println(person);
}
/**
 * 反射创建对象
 */
public static void createInstanceByReflect() throws Exception {
    // 有参构造函数
    Constructor<Person> constructor = Person.class.getConstructor(String.class, String.class);
    Person person = constructor.newInstance(UUID.randomUUID().toString(), "Reflect");
    // 无参构造函数
    constructor = Person.class.getConstructor();
    person = constructor.newInstance();
}
```

# 二、集合

## 1、HashMap、Hashtable、LinkedHashMap

### 1.1、get和put的原理？JDK8

**put 流程**
- （1）HashMap 是懒惰创建数组的，首次使用才创建数组
- （2）计算索引（桶下标）
- （3）如果桶下标还没人占用，创建 Node 占位返回
- （4）如果桶下标已经有人占用
   - 已经是 TreeNode 走红黑树的添加或更新逻辑
   - 是普通 Node，走链表的添加或更新逻辑，如果链表长度超过树化阈值，走树化逻辑
- （5）返回前检查容量是否超过阈值，一旦超过进行扩容；

**1.7 与 1.8 的区别**
- 链表插入节点时，1.7 是头插法，1.8 是尾插法
- 1.7 是大于等于阈值且没有空位时才扩容，而 1.8 是大于阈值就扩容
- 1.8 在扩容计算 Node 索引时，会优化；

### 1.2、你知道hash的实现吗？为什么要这样实现？

在Java 1.8的实现中，是通过hashCode()的高16位异或低16位实现的：`(h = k.hashCode()) ^ (h >>> 16)`；计算下标`( n-1 & hash)`
- 主要是从速度、功效、质量来考虑的，这么做可以在bucket的n比较小的时候，也能保证考虑到高低bit都参与到hash的计算中，同时不会有太大的开销;
- hash的算法是``(h = k.hashCode()) ^ (h >>> 16)`，为了使得计算出的hash值更分散，所以选择将h无符号右移16位，然后再与h异或时，就能达到h的高16位和低16位都能参与计算，减少碰撞的可能性；
- 使用`&`操作，是为了提高处理器处理的数据；
- 数组的大小是2的幂次方，是因为只有大小是2的幂次方时，才能够使得`( n-1 & hash)`公式成立

**为什么要二次hash：**

二次 hash() 是为了综合高位数据，让哈希分布更为均匀；

二次 hash 是为了配合 **容量是 2 的 n 次幂** 这一设计前提，如果 hash 表的容量不是 2 的 n 次幂，则不必二次 hash

**有哪些解决hash冲突的方法：**

- hash的算法尽可能避免的hash冲突；
- 自动扩容，当数组大小快满的时候，采取自动扩容，可以减少hash冲突；
- hash冲突发生时，采用链表来解决；
- hash冲突严重时，链表会自动转换成红黑树，提高查询速度；

### 1.3、容量是如何处理的

- 如果HashMap的大小超过了负载因子(load factor)定义的容量。如何处理？如果超过了负载因子(默认0.75)。则会重新resize一个原来长度两倍的HashMap。并且重新调用hash方法。
- 如果指定了HashMap的容量，如：new HashMap(17)，那么其容量会变为32。

### 1.4、为什么 JDK8 的 HashMap 使用的跟以往不同的实现

- 一直到JDK7为止，HashMap 的结构都是这么简单，基于一个数组以及多个链表的实现，hash 值冲突时就将对应节点以链表形式存储。这样的 HashMap 在性能上存在问题：如果很多节点在hash时发生碰撞，存储在一个链表中，那么如果要查找其中一个节点时，不可避免要花费O(N)的时间；
- 在JDK8中，使用红黑树来解决问题。在最坏的情况下，链表的查找时间复杂度是O(N)，而红黑树一直是O(logN)。JDK7 中HashMap采用的是位桶+链表的方式，即我们常说的散列链表的方式；而 JDK8 中采用的是`位桶+链表/红黑树`也是非线程安全的。当某个位桶的链表的长度达到某个阀值的时候，这个链表就将转换成红黑树

### 1.5、为什么HashMap默认的加载因子是0.75

- 5.1、加载因子：表示hash表中元素填满的程度.
	* 加载因子越大，填满的元素越多，空间利用率越高,但冲突的机会加大；
	* 反之,加载因子越小，填满的元素越少，冲突的机会减少，但空间利用率不高。冲突的机会越大，则查找的成本越高；反之。查找的成本越小。需要在"冲突的机会" 和 "空间利用率上" 寻找平衡；出于容量和性能之间平衡的结果

- 5.2、为什么HashMap的默认加载因子是0.75：在理想情况下，使用随机哈希码，节点出现的频率在hash桶中遵循泊松分布，同时给出了桶中元素个数和概率的对照表.
	```
	0: 0.60653066
	1: 0.30326533
	2: 0.07581633
	3: 0.01263606
	4: 0.00157952
	5: 0.00015795
	6: 0.00001316
	7: 0.00000094
	8: 0.00000006
	```
	从上面的表中可以看到当桶中元素到达8个的时候，概率已经变得非常小，也就是说用0.75作为加载因子，每个碰撞位置的链表长度超过８个是几乎不可能的

### 1.6、为什么HashMap的默认初始容量是16，且容量必须是 2的幂

之所以是选择16是为了服务于从 key 映射到 index 的 hash 算法。从key映射到HashMap 数组对应的位置，会用到一个hash函数。实现高效的hash算法，HashMap 中使用位运算。`index = hashcode(key) & (length - 1)`。hash算法最终得到的index结果，完全取决于Key的Hashcode值的最后几位。长度是2的幂不仅提高了性能，因为`length - 1`的二进制值位全是1，这种情况下，index的结果等同于Hashcode后几位的值，只要输入hashcode均匀分布，hash算法的结果就是均匀的。
- 计算索引时效率更高：如果是 2 的 n 次幂可以使用位与运算代替取模
- 扩容时重新计算索引效率更高： `hash & oldCap == 0` 的元素留在原来位置 ，否则`新位置 = 旧位置 + oldCap`

### 1.7、泊松分布与指数分布

#### 1.7.1、泊松分布

Poisson分布，是一种统计与概率论中常见的离散概率分布，其适合于描述单位时间内随机事件发生的次数的概率分布。

如某一服务设施在一定时间内受到的服务请求的次数，电话交换机接到呼叫的次数、汽车站台的候客人数、机器出现的故障数、自然灾害发生的次数、DNA序列的变异数、放射性原子核的衰变数、激光的光子数分布等等；

#### 1.7.2、指数分布

指数分布（Exponential distribution）是一种连续概率分布。指数分配可以用来表示独立随机事件发生的时间间隔，比如旅客进入机场的时间间隔、打进客服中心电话的时间间隔、中文维基百科新条目出现的时间间隔等等；

与泊松分布相比，其最大的差异就是指数分布是针对连续随机变量定义，即时间这个变量。时间必须是连续的。而泊松分布是针对随机事件发生次数定义的，发生次数是离散的。粗略地可以认为这两个分布之间有一种“倒数”的关系；

### 1.8、如果HashMap在put的时候，如果数组已有某个key，不想覆盖怎么办？取值时，如果得到的value是空时，如何返回默认值；

- 如果数组有了key，但是不想覆盖value，可以选择`putIfAbsent`方法，这个方法有个内置变量`onlyIfAbsent`，内置是true，就不会覆盖；在平时使用put的时候，内置onlyIfAbsent是false，允许覆盖；
	```java
	@Override
    public V putIfAbsent(K key, V value) {
        return putVal(hash(key), key, value, true, true);
    }	
	```
- 取值时，如果为空，想返回默认值，可以使用`getOrDefault`方法，第一个参数为key，第二个参数为想返回的默认值；
	```java
	@Override
    public V getOrDefault(Object key, V defaultValue) {
        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? defaultValue : e.value;
    }
	```
	***上述方法都是在JDK1.8之后才有的***

### 1.9、高并发下 HashMap 的使用的问题

- 扩容-resize()：影响resize发生的因素
	- capacity：HashMap当前的长度(2的幂);
	- loadfactor：加载因子,默认是0.75f衡量HashMap是否进行resize条件: HashMap.size >= capacity * loadfactor.
- 扩容步骤
	- （1）扩容：创建一个新的entry数组，长度是原来数组的两倍；
	- （2）rehash：遍历原entry数组，把所有的entry重写hash到新的数组。为什么需要重新hash？因为长度扩大异以后，hash规则也随之改变；`index =  HashCode(Key)&(Length - 1)` 当原数组长度为8时，Hash 运算是 和 111B做与运算；新数组长度为16，Hash 运算是和1111B做与运算。
- 在单线程下上述步骤执行没有任何问题；在多线程环境下，reHash在并发的情况下可能会形成链表环。此时问题并没有直接产生。当调用Get查找一个不存在的Key，而这个Key的Hash结果恰好等于某个值的时候，由于位置该值带有环形链表，所以程序将会进入死循环，从而报内存溢出。
- 在高并发环境下，通常使用 `ConcurrentHashMap`，兼顾了线程安全和性能；
- 下面代码只在JDK7以前的版本有效，jdk8之后就不存在这种问题了。因为JDK8中扩容的时候不存在rehash操作。
	```java
	private static Map<Long, Set<Integer>> setMap = new ConcurrentHashMap<>();
	public static void main(String[] args) throws InterruptedException {
		final long key = 1L;
		setMap.put(key, new HashSet<Integer>());
		for (int i = 0; i < 100; i++) {
			setMap.get(key).add(i);
		}
		Thread a = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int j = 100; j < 200000; j++) {
					setMap.get(key).add(j);
				}
			}
		});
		Thread b = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int j = 200000; j < (200000 + 200000); j++) {
					setMap.get(key).add(j);
				}
			}
		});
		a.start();
		b.start();
		Thread.sleep(1000 * 10);
		System.out.println(setMap.toString()); // 报java.lang.OutOfMemoryError: Java heap space
	}
	```

### 1.10、HashMap的key使用哪种数据结构性能高

首先影响HashMap的性能点：
- 哈希冲突导致单个哈希桶元素数量过多。操作元素的时间复杂度甚至 退化成O(N)，经红黑树改进后，也得O(logN)。
- 扩容，为啥扩容？还是为了降低哈希冲突！

主要可以从hash碰撞考虑，hash函数设计的时候是调用对应的类的hashcode方法，对应Integer、Long 等整型类型的数据，其hashcode方法返回的就是对应的整数值，为了减少hash冲突，尽量使用HashCode递增的值作为key，例如递增的int值，这样可以尽可能减少哈希冲突；
```java
public final class Integer extends Number implements Comparable<Integer> {
	@Override
	public int hashCode() {
		return Integer.hashCode(value);
	}
	public static int hashCode(int value) {
        return value;
    }
}
```

### 1.11、红黑树与链表的转换规则

**为什么使用红黑树？**

之所以选择红黑树是为了解决二叉查找树的缺陷：二叉查找树在特殊情况下会变成一条线性结构（这就跟原来使用链表结构一样了，造成层次很深的问题），遍历查找会非常慢。而红黑树在插入新数据后可能需要通过左旋、右旋、变色这些操作来保持平衡。引入红黑树就是为了查找数据快，解决链表查询深度的问题；

红黑树的查询性能略微逊色于AVL树，因为他比avl树会稍微不平衡最多一层，也就是说红黑树的查询性能只比相同内容的avl树最多多一次比较，但是，红黑树在插入和删除上完爆avl树，avl树每次插入删除会进行大量的平衡度计算，而红黑树为了维持红黑性质所做的红黑变换和旋转的开销，相较于avl树为了维持平衡的开销要小得多；

**为什么一上来不用红黑树？**

正常情况下，一个hashmap的树化条件是比较苛刻的，可能是发生攻击的情况下，防止链表查询性能问题导致的，所以树化应该是偶然的情况；
- hash表的查找，更新的时间复杂度是$O(1)$，而红黑树的查找、更新的时间复杂度是 $O(log_2⁡N )$，TreeNode 占用空间也比普通 Node 的大，如非必要，尽量还是使用链表；
- hash 值如果足够随机，则在 hash 表内按泊松分布，在负载因子 0.75 的情况下，长度超过 8 的链表出现概率是 0.00000006，树化阈值选择 8 就是为了让树化几率足够小；

**树化的条件：**

当链表长度超过树化阈值 8 时，先尝试扩容来减少链表长度，如果数组容量已经 >=64，才会进行树化

**何时树退化为链表**
- 情况1：在扩容时如果拆分树时，树元素个数 <= 6 则会退化链表
- 情况2：remove 树节点时，若 root、root.left、root.right、root.left.left 有一个为 null ，也会退化为链表

### 1.12、HashMap 在使用时需要注意什么地方

至少说出四点：
- 在使用的时候指定对应的容量，避免后续大小调整带来的数据消耗；
- 尽量使用Integer、Long等数字类型作为key，避免因为hash冲突形成链表带来性能的影响；
- 避免在多线程环境下使用HashMap，否则会产生意料之外的情况；
- 遍历HashMap时使用EntrySet

## 2、ConcurrentHashMap

### 2.1、使用ConcurrentHashMap中，如何避免组合操作的线程安全问题

可以使用replace方法

### 2.2、ConcurrentHashMap JDK1.8是如何扩容的? 

tryPresize 

- 如果新增节点之后，所在链表的元素个数达到了阈值 8，则会调用 treeifyBin方法把链表转换成红黑树，不过在结构转换之前，会对数组长度进行判断，如果数组长度n小于阈值 MIN_TREEIFY_CAPACITY，默认是64，则会调用 tryPresize方法把数组长度扩大到原来的两倍，并触发 transfer方法，重新调整节点的位置；

- 新增节点之后，会调用 addCount方法记录元素个数，并检查是否需要进行扩容，当数组元素个数达到阈值时，会触发 transfer方法，重新调整节点的位置；

### 2.3、JDK1.8链表转红黑树的时机是什么? 临界值为什么是8? 

- 首先是链表的长度是否为大于等于8，如果是大于等于8的话，再判断数组的长度是否大于64，如果小于64，则进行数组扩容操作；否则会转为红黑树；

**为什么一开始不用红黑树？**

单个 TreeNode 需要占用的空间大约是普通 Node 的两倍，所以只有当包含足够多的 Nodes 时才会转成 TreeNodes，而是否足够多就是由 `TREEIFY_THRESHOLD` 的值决定的。而当桶中节点数由于移除或者 resize 变少后，又会变回普通的链表的形式，以便节省空间；

**为什么是8？**

如果 hashCode 分布良好，也就是 hash 计算的结果离散好的话，那么红黑树这种形式是很少会被用到的，因为各个值都均匀分布，很少出现链表很长的情况。在理想情况下，链表长度符合泊松分布，各个长度的命中概率依次递减，当长度为 8 的时候，概率仅为 0.00000006。这是一个小于千万分之一的概率，通常我们的 Map 里面是不会存储这么多的数据的，所以通常情况下，并不会发生从链表向红黑树的转换；

链表长度超过 8 就转为红黑树的设计，更多的是为了防止用户自己实现了不好的哈希算法时导致链表过长，从而导致查询效率低，而此时转为红黑树更多的是一种保底策略，用来保证极端情况下查询的效率；

**什么时候红黑树会还原成链表？**

当红黑树元素个数小于等于6的时候；

### 2.4、JDK1.8是如何进行数据迁移的? 

transfer 

将原来的 tab 数组的元素迁移到新的 nextTab 数组中

并发操作的机制：原数组长度为 n，所以我们有 n 个迁移任务，让每个线程每次负责一个小任务是最简单的，每做完一个任务再检测是否有其他没做完的任务，帮助迁移就可以了，而 Doug Lea 使用了一个 stride，简单理解就是步长，每个线程每次负责迁移其中的一部分，如每次迁移 16 个小任务。所以，我们就需要一个全局的调度者来安排哪个线程执行哪几个任务，这个就是属性 transferIndex 的作用；

第一个发起数据迁移的线程会将 transferIndex 指向原数组最后的位置，然后从后往前的 stride 个任务属于第一个线程，然后将 transferIndex 指向新的位置，再往前的 stride 个任务属于第二个线程，依此类推。当然，这里说的第二个线程不是真的一定指代了第二个线程，也可以是同一个线程。其实就是将一个大的迁移任务分为了一个个任务包；

### 2.5、ConcurrentHashMap在jdk8中的bug

https://juejin.cn/post/6844904191077384200

### 2.6、是先CAS还是synchronized

以put为例
```java
final V putVal(K key, V value, boolean onlyIfAbsent) {
    if (key == null || value == null) throw new NullPointerException();
    int hash = spread(key.hashCode());
    int binCount = 0;
    for (Node<K,V>[] tab = table;;) {
        Node<K,V> f; int n, i, fh;
        if (tab == null || (n = tab.length) == 0)
            tab = initTable();
        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
            // cas
            if (casTabAt(tab, i, null, new Node<K,V>(hash, key, value, null)))
                break;                   // no lock when adding to empty bin
        }
        else if ((fh = f.hash) == MOVED)
            tab = helpTransfer(tab, f);
        else {
            V oldVal = null;
            synchronized (f) {
                if (tabAt(tab, i) == f) {
                    .....
                }
            }
            if (binCount != 0) {
                if (binCount >= TREEIFY_THRESHOLD)
                    treeifyBin(tab, i);
                if (oldVal != null)
                    return oldVal;
                break;
            }
        }
    }
    addCount(1L, binCount);
    return null;
}
```
可以看到上面的CAS是在数组中索引元素为空时，通过CAS填充数据，如果是有元素，即hash冲突了，需要通过synchronized锁住当前链表节点来实现

### 2.7、为什么ConcurrentHashMap用红黑树

之所以选择红黑树是为了解决二叉查找树的缺陷：二叉查找树在特殊情况下会变成一条线性结构（这就跟原来使用链表结构一样了，造成层次很深的问题），遍历查找会非常慢。而红黑树在插入新数据后可能需要通过左旋、右旋、变色这些操作来保持平衡。引入红黑树就是为了查找数据快，解决链表查询深度的问题；

红黑树的查询性能略微逊色于AVL树，因为它比AVL树会稍微不平衡最多一层，也就是说红黑树的查询性能只比相同内容的avl树最多多一次比较，但是，红黑树在插入和删除上完爆avl树，avl树每次插入删除会进行大量的平衡度计算，而红黑树为了维持红黑性质所做的红黑变换和旋转的开销，相较于avl树为了维持平衡的开销要小得多；

### 2.8、为什么用CAS替代分段锁

**关于分段锁：**

Segment继承了重入锁ReentrantLock，有了锁的功能，每个锁控制的是一段，当每个Segment越来越大时，锁的粒度就变得有些大了。
- 分段锁的优势在于保证在操作不同段 map 的时候可以并发执行，操作同段 map 的时候，进行锁的竞争和等待。这相对于直接对整个map同步synchronized是有优势的。
- 缺点在于分成很多段时会比较浪费内存空间(不连续，碎片化)；操作map时竞争同一个分段锁的概率非常小时，分段锁反而会造成更新等操作的长时间等待; 当某个段很大时，分段锁的性能会下降

**CAS+synchronized**
- 减少内存开销：如果使用ReentrantLock则需要节点继承AQS来获得同步支持，增加内存开销，而1.8中只有头节点需要进行同步。
- 内部优化：synchronized则是JVM直接支持的，JVM能够在运行时作出相应的优化措施：锁粗化、锁消除、锁自旋等等；

使用 CAS + synchronized 方式时 加锁的对象是每个链条的头结点，也就是 锁定 的是冲突的链表，所以再次提高了并发度，并发度等于链表的条数或者说 桶的数量；

它是Node链表里的每一个Node,也就是说,Synchronized是将每一个Node对象作为了一个锁,这样做的好处是什么呢?将锁细化了,也就是说,除非两个线程同时操作一个Node,注意,是一个Node而不是一个Node链表哦,那么才会争抢同一把锁；

### 2.9、ConcurrentHashMap一定是线程安全的吗？

ConcurrentHashMap 只能保证提供的原子性读写操作是线程安全的。ConcurrentHashMap 对外提供的方法或能力的限制：
- 使用了 ConcurrentHashMap，不代表对它的多个操作之间的状态是一致的，是没有其他线程在操作它的，如果需要确保需要手动加锁；
- 诸如 size、isEmpty 和 containsValue 等聚合方法，在并发情况下可能会反映 ConcurrentHashMap 的中间状态。因此在并发情况下，这些方法的返回值只能用作参考，而不能用于流程控制。显然，利用 size 方法计算差异值，是一个流程控制；
- 诸如 putAll 这样的聚合方法也不能确保原子性，在 putAll 的过程中去获取数据可能会获取到部分数据。

### 2.10、扩容是怎么不阻塞读操作的

扩容的时候，以链表为单位从后向前迁移链表，迁移完成的将旧数组头节点替换为 ForwardingNode
* 根据是否为 ForwardingNode 来决定是在新数组查找还是在旧数组查找，不会阻塞
* 如果链表长度超过 1，则需要对节点进行复制（创建新节点），怕的是节点迁移后 next 指针改变
* 如果链表最后几个元素扩容后索引不变，则节点无需复制

## 3、TreeMap

### 3.1、LinkedHashMap与TreeMap区别

- 这两者都是能够保证一定的顺序的，其中LinkedHashMap是保证key的插入顺序的，而TreeMap是按照key的自然排序的升序来实现的；
- LinkedHashMap是通过双向链表实现，其还继承自HashMap；TreeMap是基于红黑树来实现的；

ConcurrentSkipListMap   基于跳表实现的

EnumMap 基于位运算实现的

## 4、ArrayList与LinkedList

### 4.1、两者区别

**LinkedList**

- 基于双向链表，无需连续内存
- 随机访问慢（要沿着链表遍历）
- 头尾插入删除性能高，如果是往链表的中间插入，性能也不高；因为链表中间插入是需要遍历的过程；
- 内存占用多，因为LinkedList内部对象是一个Node，包含值、前驱指针、后驱指针等；

 **ArrayList**
- 基于数组，需要连续内存
- 随机访问快（指根据下标访问），其实现了 RandomAccess 接口
- 尾部插入、删除性能可以，其它部分插入、删除都会移动数据，因此性能会低
- 可以利用 cpu 缓存，局部性原理；

### 4.2、ArrayList扩容机制

ArrayList 初始默认是空数组，待需要添加元素时，判断是否需要扩容；新增元素主要有两步：
- 判断是否需要扩容，如果需要执行扩容操作；
- 扩容主要是：扩容的数组容量以及数组的拷贝操作；

扩容时存在的线程安全问题，看如下代码，
```java
public static void main(String[] args) {
    List<Integer> list = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
        int finalI = i;
        new Thread(() -> {
            list.add(finalI + 1);
        }).start();

    }
    System.out.println(list.size());
    System.out.println(list.toString());
}
```
可能出现的结果如下：
```java
9
[null, 2, 3, 4, 5, 6, 7, 8, 9, 10]
9
[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
9
[null, 1, 3, 4, 5, 6, 7, 8, 9, 10]
```
为什么会出现null值？由于ArrayList是基于数组实现，由于数组大小一旦确定就无法更改，所以其每次扩容都是将旧数组容器的元素拷贝到新大小的数组中(Arrays.copyOf函数)，由于我们通过new ArrayList<>()实例的对象初始化的大小是0，所以第一次插入就会扩容，由于ArrayList并非线程安全，第二次插入时，第一次扩容可能并没完成，于是也会进行一次扩容(第二次扩容)，这次扩容所拿到list的elementDate是旧的，并不是第一次扩容后对象，于是会因为第一次插入的值并不在旧的elementDate中，而将null值更新到新的数组中。

比如现在有线程A和B分别要插入元素1和2：
- 当线程A调用add方法时size是0，于是会进行一次扩容，此时线程B调用add方法时size仍然是0，所以也会进行扩容；
- 假设此时线程A比线程B扩容先完成，此时list的elementDate是新的数组对象(由线程A构建)，然后开始执行`elementDate[size++] = 1`的程序，这个过程中线程B扩容拿到的数组仍然是旧的elementDate，于是线程B构造一个新的数组(数据全部为null)；
- 然后使list的elementDate指向线程B构造的对象，那么线程A之前构造的elementDate也就被丢掉了，但是由于size已经自增，所以线程B会在索引为1的位置赋予2，那么此时数组元素就成了`[null,2]`，当然如果线程B扩容比线程A先完成那么就可能为`[null,1]`

还有一种错误就是覆盖：这种情况是因为size++并不是原子性的，所以可能线程A自增的时候，线程B也进行一次自增，但是两次自增的结果是一样的，所以先完成的线程更新的数据会被后完成的线程覆盖掉；

### 4.3、ArrayList线程不安全的体现

ArrayList 的线程不安全体现在多线程调用 add 方法的时候。具体有两个表现：
- 当在需要进行数组扩容的临界点时，如果有两个线程同时来进行插入，可能会导致数组下标越界异常。
- 由于往数组中添加元素不是原子操作，所以可能会导致元素覆盖的情况发生

## 5、哪些map的key可以为null、value可以为null？为什么

ConcurrentMaps（ConcurrentHashMaps，ConcurrentSkipListMaps）不允许使用null的主要原因是，无法容纳在非并行映射中几乎无法容忍的歧义。最主要的是，如果map.get(key)return null，则无法检测到该键是否显式映射到null该键。在非并行映射中，您可以通过进行检查 map.contains(key)，但在并行映射中，两次调用之间的映射可能已更改；

hashtable也是线程安全的，所以也是key和value也是不可以null的

treeMap 线程不安全，但是因为需要排序，进行key的compareTo方法，所以key是不能null中，value是可以的

## 6、Spring 的 ConcurrentReferenceHashMap，针对 Key 和 Value 支持软引用和弱引用两种方式。你觉得哪种方式更适合做缓存呢？

# 三、IO与NIO

## 1、文件拷贝实现方式

主要关注以下几点：
- 不同的copy方式，底层机制有什么区别？
- 为什么零拷贝可能有性能优势？
- Buffer的分类与使用；
- Direct Buffer对垃圾收集有哪些方面的影响

### 1.1、不同的拷贝方式底层机制的实现

- 关于两个概念：用户态空间（User Space）和内核态空间（Kernel Space）这是操作系统层面的概念，操作系统内核、硬件驱动等运行在内核状态空间，具有相对高的特权；而用户态空间，则给普通应用和服务使用

- 基于流读写：当我们使用输入输出流进行读写时，实际上是进行了多次上下文切换，比如应用读取数据时先将内核态数据从磁盘读取到内核缓存，再切换到用户态将数据从内核缓存中读取到用户缓存，这种方式会带来一定的额外开销，可能会降低IO效率

- 基于NIO：基于NIO的transfer的实现方式，在Linux和Unix上，则会使用零拷贝技术，数据传输并不需要用户态参与，省去了上下文切换的开销和不必要的拷贝，进而可能提高应用拷贝性能

### 1.2、Files.copy 方法

最终实现是本地方法实现的[UnixCopyFile.c](http://hg.openjdk.java.net/jdk/jdk/file/f84ae8aa5d88/src/java.base/unix/native/libnio/fs/UnixCopyFile.c)，其内部明确说明了只是简单的用户态空间拷贝，所以该方法不是利用transfer来实现的，而是本地技术实现的用户态拷贝

### 1.3、基于流的读写

```java
public static void copyFileByStream(File source, File dest) throws Exception {
	try (InputStream is = new FileInputStream(source);
			OutputStream os = new FileOutputStream(dest)) {
		byte[] buffer = new byte[1024];
		int len;
		while ((len = is.read(buffer)) > 0) {
			os.write(buffer, 0, len);
		}
	}
}
```

### 1.4、基于NIO实现

- 基于基本NIO操作实现
```java
public static void main(String[] args) throws Exception {
	FileInputStream in = new FileInputStream("temp/test1.txt");
	FileOutputStream out = new FileOutputStream("temp/test1-copy.txt");
	FileChannel inChannel = in.getChannel();
	FileChannel outChannel = out.getChannel();
	ByteBuffer buffer = ByteBuffer.allocate(1024);
	int read = inChannel.read(buffer);
	if (read == -1) {
		return;
	}
	buffer.flip();
	outChannel.write(buffer);
}
```

- 基于Nio的transferTo或者transferFrom
```java
// 使用java nio 的transferTo 或 transferFrom来实现，其copy速度相对来说更快点，因为其更能利用现代操作系统底层机制，避免不必要的拷贝和上下文切换
public static void copyFileByChannel(File source, File dest) throws Exception {
	try (FileChannel sourceChannel = new FileInputStream(source).getChannel();
			FileChannel targetChannel = new FileOutputStream(dest).getChannel()) {
		for (long count = sourceChannel.size(); count
				> 0; ) {
			long transferred = sourceChannel.transferTo(sourceChannel.position(), count, targetChannel);
			sourceChannel.position(sourceChannel.position() + transferred);
			count -= transferred;
		}
	}
}
```

- 基于MappdByteBuffer实现，中间使用了编码
```java
public static void copyFileByMappedByteBuffer(String source, String dest) throws Exception {
	RandomAccessFile input = new RandomAccessFile(source, "r");
	RandomAccessFile output = new RandomAccessFile(dest, "rw");
	long length = new File(source).length();
	FileChannel inputChannel = input.getChannel();
	FileChannel outputChannel = output.getChannel();
	MappedByteBuffer inputData = inputChannel.map(FileChannel.MapMode.READ_ONLY, 0, length);
	Charset charset = Charset.forName("UTF-8");
	CharsetDecoder decoder = charset.newDecoder();
	CharsetEncoder encoder = charset.newEncoder();
	CharBuffer charBuffer = decoder.decode(inputData);
	ByteBuffer outputData = encoder.encode(charBuffer);
	outputChannel.write(outputData);
}
```

### 1.4、如何提高拷贝效率

- 在程序中，使用缓存机制，合理减少IO次数；
- 使用transfer等机制，减少上下文切换和额外IO操作；
- 尽量减少不必要的转换过程，比如编解码；对象序列化与反序列化；

## 2、DirectBuffer 与 MappedByteBuffer

### 2.1、概述

- DirectBuffer：其定义了isDirect方法，返回当前buffer是不是Direct类型。因为Java提供了堆内和堆外（Direct）Buffer，我们可以以他的allocat 或者 allocatDirect方法直接创建；
- MappedByteBuffer：将文件按照指定大小直接映射为内存区域，当程序访问这个内存区域时直接将操作这块文件数据，省去了将数据从内核空间向用户空间传输的损耗；可以使用FileChannel.map创建，本质上也是DirectBuffer；

在实际使用时，Java会对DirectBuffer仅做本地iO操作，对于很多大数据量的IO密集操作，可能会带来非常大的优势：
- DirectBuffer生命周期内内存地址都不会再发生改变，进而内核可以安全的对其进行访问，很对IO操作很搞笑；
- 减少了堆内对象存储的可能额外维护工作，所以访问效率可能有所提高；

但是值得注意的是，DirectBuffer创建和销毁过程中，都会比一般的堆内存Buffer增加部分开销，通常建议用于长期使用、数据较大的场景

因为DirectBuffer不在堆上，所以其参数设置大小可以用如下参数：`-XX:MaxDirectMemorySize=512M`；意味着在计算Java可以使用的内存大小的时候，不能只考虑堆的需要，还有DirectBuffer等一系列堆外因素，如果出现内存不足，堆外内存占用也是一种可能性；

另外，大多数垃圾收集过程中，都不会主动收集DirectBuffer，它的垃圾收集过程，是基于Cleaner和幻象引用机制，其本身不是public类型，内部实现了一个Deallocator负责销毁的逻辑，对它的销毁往往需要到FullGC的时候，使用不当的话很容易引起OOM

关于DirectBuffer的回收，注意以下几点：
- 在应用程序中，显示调用System.gc()来强制触发；
- 在大量使用DirectBuffer的部分框架中，框架自己在程序中调用释放方法，Netty的实现即如此；
- 重复使用DirectBuffer

### 2.2、跟踪与诊断DirectBuffer内存占用

通常的垃圾收集日志等记录，并不包含Directbuffer等信息，在JDK8之后的版本，可以使用native memory tracking特性进行诊断：```-XX:NativeMemoryTracking={summary|detail}```

注意激活NMT通常都会导致JVM出现5%~10%性能下降

```
// 打印 NMT信息
jcmd <pid> VM.native_memory detail
// 进行baseline，以对比分配内存变化
jcmd <pid> VM.native_memory baseline
// 进行baseline，以对比分配内存变化
jcmd <pid> VM.native_memory detail.diff
```

## 3、使用Java读取大文件

- （1）文件流边读边用，使用文件流的read()方法每次读取指定长度的数据到内存中，具体代码如下
    ```java
    public static void readMethod1(String filePath) throws Exception{
        BufferedInputStream reader = new BufferedInputStream(new FileInputStream(filePath));
        int bytes = -1;
        do {
            byte[] byteArray = new byte[8192];
            bytes = reader.read(byteArray);
            if (bytes != -1) {
                String s = new String(byteArray);
                System.out.println(s);
            }
        } while (bytes > 0);
    
        reader.close();
    }
    ```
- （2）对大文件建立NIO的FileChannel，每次调用read()方法时会先将文件数据读取到已分配的固定长度的java.nio.ByteBuffer，接着从中获取读取的数据。这种方式比传统的流方式要快点
    ```java
    public static void fileChannelMethod(String filePath) throws Exception {
        FileInputStream in = new FileInputStream(filePath);
        ByteBuffer byteBuffer = ByteBuffer.allocate(65535);
        FileChannel fileChannel = in.getChannel();
        int b = -1;
        do {
            b = fileChannel.read(byteBuffer);
            if (b != -1) {
                byte[] array = new byte[b];
                byteBuffer.flip();
                byteBuffer.get(array);
                byteBuffer.clear();
                System.out.println(new String(array));
            }
        } while (b > 0);
        in.close();
        fileChannel.close();
    }
    
    ```
- （3）内存文件映射，就是把文件内容映射到虚拟内存的一块区域中，从而可以直接操作内存当中的数据而无需每次都通过IO去物理硬盘读取文件，

    ```java
    public static void memoryMappingMethod(String filePath) throws Exception {
        FileInputStream in = new FileInputStream(filePath);
        FileChannel fileChannel = in.getChannel();
        MappedByteBuffer mapperBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
        boolean end = false;
        do {
            int limit = mapperBuffer.limit();
            int position = mapperBuffer.position();
            if (position >= limit) {
                end = true;
            }
            int maxSize = 2048;
            if (limit - position < maxSize) {
                maxSize = limit - position;
            }
            byte[] array = new byte[maxSize];
            mapperBuffer.get(array);
            System.out.println(new String(array));
    
        } while (!end);
        in.close();
        fileChannel.close();
    }
    ```
    这种方式存在致命问题，就是无法读取超大文件（大于Integer.Max_value），因为 FileChannel的map方法中 size 参数会有大小限制，源码中发现该参数值大于 Integer.MAX_VALUE 时会直接抛出 IllegalArgumentException("Size exceeds Integer.MAX_VALUE") 异常，所以对于特别大的文件其依然不适合。
	```java
	// sun.nio.ch.FileChannelImpl#map
	public MappedByteBuffer map(MapMode var1, long var2, long var4) throws IOException {
        this.ensureOpen();
        if (var1 == null) {
            throw new NullPointerException("Mode is null");
        } else if (var2 < 0L) {
            throw new IllegalArgumentException("Negative position");
        } else if (var4 < 0L) {
            throw new IllegalArgumentException("Negative size");
        } else if (var2 + var4 < 0L) {
            throw new IllegalArgumentException("Position + size overflow");
        } else if (var4 > 2147483647L) {
			throw new IllegalArgumentException("Size exceeds Integer.MAX_VALUE");
		} else {
			....
		}
	}
	```
    本质上是由于 java.nio.MappedByteBuffer 直接继承自 java.nio.ByteBuffer ，而 ByteBuffer 的索引是 int 类型的，所以 MappedByteBuffer 也只能最大索引到 Integer.MAX_VALUE 的位置，所以 FileChannel 的 map 方法会做参数合法性检查。

## 4、NIO消息传输错误

### 4.1、存在问题的情况

- 多消息粘包：
- 单消息不完整：接收端buffer容量不够
- 消息到达提醒重复触发（读消息时未设置取消监听）

### 4.2、如何解决

- 数据传输加上开始结束标记
- 数据传输使用固定头部的方案；
- 混合方案：固定头、数据加密、数据描述

## 5、关于BIO、NIO等现场问题

**基于BIO实现的Server端，当建立了100个连接时，会有多少个线程？如果基于NIO，又会是多少个线程？ 为什么？**

BIO由于不是NIO那样的事件机制，在连接的IO读取上，无论是否真的有读/写发生，都需要阻塞住当前的线程，对于基于BIO实现的Server端，通常的实现方法都是用一个线程去accept连接，当连接建立后，将这个连接的IO读写放到一个专门的处理线程，所以当建立100个连接时，通常会产生1个Accept线程 + 100个处理线程。

NIO通过事件来触发，这样就可以实现在有需要读/写的时候才处理，不用阻塞当前线程，NIO在处理IO的读写时，当从网卡缓冲区读或写入缓冲区时，这个过程是串行的，所以用太多线程处理IO事件其实也没什么意义，连接事件由于通常处理比较快，用1个线程去处理就可以，IO事件呢，通常会采用cpu core数+1或cpu core数 * 2，这个的原因是IO线程通常除了从缓冲区读写外，还会做些比较轻量的例如解析协议头等，这些是可以并发的，为什么不只用1个线程处理，是因为当并发的IO事件非常多时，1个线程的效率不足以发挥出多core的CPU的能力，从而导致这个地方成为瓶颈，这种在分布式cache类型的场景里会比较明显，按照这个，也就更容易理解为什么在基于Netty等写程序时，不要在IO线程里直接做过多动作，而应该把这些动作转移到另外的线程池里去处理，就是为了能保持好IO事件能被高效处理

## 6、Channel和Socket区别

Socket、SocketChannel二者的实质都是一样的，都是为了实现客户端与服务器端的连接而存在的。
- 所属包不同：Socket在java.net包中，而SocketChannel在java.nio包中；
- 异步方式不同：Socket是阻塞连接（当然我们可以自己实现非阻塞），SocketChannel可以设置非阻塞连接；
- 性能不同：一般来说使用SocketChannel会有更好的性能。其实，Socket实际应该比SocketChannel更高效，不过由于使用者设计等原因，效率反而比直接使用SocketChannel低；
- 使用方式不同：
    - Socket、ServerSocket类可以传入不同参数直接实例化对象并绑定ip和端口：
        ```java
        Socket socket = new Socket("127.0.0.1", "8000");
        ServerSocket serverSocket = new ServerSocket("8000")
        ```
    - 而SocketChannel、ServerSocketChannel类需要借助Selector类控制

## 7、Java中怎么快速把InputStream转化为String

### 7.1、使用 commons包的工具类 IOUtils

```java
StringWriter writer = new StringWriter();
IOUtils.copy(in, writer, encoding);
String str = writer.toString();
// 或者
String str = IOUtils.toString(in, encoding);
```

### 7.2、使用guava

`CharStreams.toString(new InputStreamReader(in, encoding));`

### 7.3、使用Scanner

```java
Scanner scanner = new Scanner(in).useDelimiter("\\A");
String str = scanner.hasNext() ? scanner.next() : "";
```

### 7.4、使用Stream API

`String str = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));`

`String str = new BufferedReader(new InputStreamReader(in)).lines().parallel().collect(Collectors.joining("\n"));`

### 7.5、使用InputStreamReader and StringBuilder

```java
final int bufferSize = 1024;
final char[] buffer = new char[bufferSize];
final StringBuilder sb = new StringBuilder();
Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
int charRead;
while ( (charRead = reader.read(buffer, 0, buffer.length)) > 0) {
    sb.append(buffer, 0 , charRead);
}
return sb.toString();
```

## 8、输入流与输出流

### 8.1、输出入流转换为字节数组

```java
public static byte[] getFileContent(String filePath) throws Throwable {
    InputStream stream = SameLoaderLoadOneClassMore.class.getClassLoader().getResourceAsStream(filePath);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024 * 4];
    int n = 0;
    assert stream != null;
    while ((n = stream.read(buffer)) != -1) {
        out.write(buffer, 0, n);
    }
    return out.toByteArray();
}
```

## 9、为什么数据库连接池不采用多路IO复用

https://mp.weixin.qq.com/s/gYv2F-RFH5xrIbpXDwtgyA

## 10、如何快速的从一个1G的大文件中读取10字节内容

现在我有一个1g的大文件，如何快速的从中间读取10个字节内容，比如我就是想从1g的文件中读取中间
我就是512m往后的10个字节，用什么方式来做

答：  RandomAccessFile

# 四、并发与多线程

## 1、为什么线程池的底层数据接口采用HashSet来实现

HashSet:自动消除重复的数据，确保不会出现单个线程有多个 workers，并且保持高效率

## 2、使用模拟真正的并发请求

使用CountDownLatch

[模拟超过5W的并发用户](https://mp.weixin.qq.com/s/2BondePBWkfUNSwNyTMcTA)

## 3、可重入锁

### 3.1、为什么可以防止死锁

```java
public class Widget {
    public synchronized void doSomething(){
        // do something
    }
}
public class LoggingWidget extends Widget {
    public synchronized void doSomething() {
        super.doSomething();
    }
}
```
如果synchronized 不是可重入锁，那么LoggingWidget 的super.dosomething()；无法获得Widget对象的锁，因为会死锁

这里涉及到Java的重写；子类LoggingWidget 的doSomething方法，重写了父类Widget 的doSomething方法，但是子类对象如果要调用父类的doSomething方法，那么就需要用到super关键字了。因为实例方法的调用是Java虚拟机在运行时动态绑定的，子类LoggingWidget 的对象调用doSomething方法，一定是绑定到子类自身的doSomething方法，必须用super关键字告诉虚拟机，这里要调用的是父类的doSomething方法；

super关键字并没有新建一个父类的对象，比如说widget，然后再去调用widget.doSomething方法，实际上调用父类doSomething方法的还是我们的子类对象；

如果一个线程有子类对象的引用loggingWidget，然后调用loggingWidget.doSomething方法的时候，会请求子类对象loggingWidget 的对象锁；又因为loggingWidget 的doSomething方法中调用的父类的doSomething方法，实际上还是要请求子类对象loggingWidget 的对象锁，那么如果synchronized 关键字不是个可重入锁的话，就会在子类对象持有的父类doSomething方法上产生死锁了。正因为synchronized 关键字的可重入锁，当前线程因为已经持有了子类对象loggingWidget 的对象锁，后面再遇到请求loggingWidget 的对象锁就可以畅通无阻地执行同步方法了；

### 3.2、可重入锁如何实现

通过AQS实现可重入锁，大概思路：
- 调用 getState方法，判断当前 state 是否为0；如果为 0，加锁成功，返回；
- 如果不等于0，则判断占用线程是否为当前线程，如果是，cas(state, state+1)；

可以参考 ReentrantLock 的实现方式：
```java
// 非公平方式获取
final boolean nonfairTryAcquire(int acquires) {
    // 当前线程
    final Thread current = Thread.currentThread();
    // 获取状态
    int c = getState();
    if (c == 0) { // 表示没有线程正在竞争该锁
        if (compareAndSetState(0, acquires)) { // 比较并设置状态成功，状态0表示锁没有被占用
            // 设置当前线程独占
            setExclusiveOwnerThread(current); 
            return true; // 成功
        }
    } else if (current == getExclusiveOwnerThread()) { // 当前线程拥有该锁
        int nextc = c + acquires; // 增加重入次数
        if (nextc < 0) // overflow
            throw new Error("Maximum lock count exceeded");
        // 设置状态
        setState(nextc); 
        // 成功
        return true; 
    }
    // 失败
    return false;
}
// 公平锁
protected final boolean tryAcquire(int acquires) {
    // 获取当前线程
    final Thread current = Thread.currentThread();
    // 获取状态
    int c = getState();
    if (c == 0) { // 状态为0
        if (!hasQueuedPredecessors() && compareAndSetState(0, acquires)) { // 不存在已经等待更久的线程并且比较并且设置状态成功
            // 设置当前线程独占
            setExclusiveOwnerThread(current);
            return true;
        }
    } else if (current == getExclusiveOwnerThread()) { // 状态不为0，即资源已经被线程占据
        // 下一个状态
        int nextc = c + acquires;
        if (nextc < 0) // 超过了int的表示范围
            throw new Error("Maximum lock count exceeded");
        // 设置状态
        setState(nextc);
        return true;
    }
    return false;
}
```

## 4、队列相关面试题

### 4.1、什么是队列？队列与集合的区别

**队列：**
- 首先队列本身也是个容器，底层也会有不同的数据结构，比如 LinkedBlockingQueue 是底层是链表结构，所以可以维持先入先出的顺序，比如 DelayQueue 底层可以是队列或堆栈，所以可以保证先入先出，或者先入后出的顺序等等，底层的数据结构不同，也造成了操作实现不同；
- 部分队列（比如 LinkedBlockingQueue ）提供了暂时存储的功能，我们可以往队列里面放数据，同时也可以从队列里面拿数据，两者可以同时进行；
- 队列把生产数据的一方和消费数据的一方进行解耦，生产者只管生产，消费者只管消费，两者之间没有必然联系，队列就像生产者和消费者之间的数据通道一样，如 LinkedBlockingQueue；
- 队列还可以对消费者和生产者进行管理，比如队列满了，有生产者还在不停投递数据时，队列可以使生产者阻塞住，让其不再能投递，比如队列空时，有消费者过来拿数据时，队列可以让消费者 hodler 住，等有数据时，唤醒消费者，让消费者拿数据返回，如 ArrayBlockingQueue；
- 队列还提供阻塞的功能，比如我们从队列拿数据，但队列中没有数据时，线程会一直阻塞到队列有数据可拿时才返回

**队列与集合的区别：**
- 和集合的相同点，队列（部分例外）和集合都提供了数据存储的功能，底层的储存数据结构是有些相似的，比如说 LinkedBlockingQueue 和 LinkedHashMap 底层都使用的是链表，ArrayBlockingQueue 和 ArrayList 底层使用的都是数组。
- 和集合的区别：
    - 部分队列和部分集合底层的存储结构很相似的，但两者为了完成不同的事情，提供的 API 和其底层的操作实现是不同的。
    - 队列提供了阻塞的功能，能对消费者和生产者进行简单的管理，队列空时，会阻塞消费者，有其他线程进行 put 操作后，会唤醒阻塞的消费者，让消费者拿数据进行消费，队列满时亦然。
    - 解耦了生产者和消费者，队列就像是生产者和消费者之间的管道一样，生产者只管往里面丢，消费者只管不断消费，两者之间互不关心

### 4.2、队列是如何阻塞的

队列主要提供了两种阻塞功能，如下：
- LinkedBlockingQueue 链表阻塞队列和 ArrayBlockingQueue 数组阻塞队列是一类，前者容量是 Integer 的最大值，后者数组大小固定，两个阻塞队列都可以指定容量大小，当队列满时，如果有线程 put 数据，线程会阻塞住，直到有其他线程进行消费数据后，才会唤醒阻塞线程继续 put，当队列空时，如果有线程 take 数据，线程会阻塞到队列不空时，继续 take。
- SynchronousQueue 同步队列，当线程 put 时，必须有对应线程把数据消费掉，put 线程才能返回，当线程 take 时，需要有对应线程进行 put 数据时，take 才能返回，反之则阻塞，举个例子，线程 A put 数据 A1 到队列中了，此时并没有任何的消费者，线程 A 就无法返回，会阻塞住，直到有线程消费掉数据 A1 时，线程 A 才能返回；

### 4.3、队列阻塞的实现原理

队列本身并没有实现阻塞的功能，而是利用 Condition 的等待唤醒机制，阻塞底层实现就是更改线程的状态为睡眠；

[各个队列实现原理](../Java/Java基础/Java并发与多线程.md#74阻塞队列-BlockingQueue)

### 4.4、往队列里面 put 数据是线程安全的么？为什么？

是线程安全的，在 put 之前，队列会自动加锁，put 完成之后，锁会自动释放，保证了同一时刻只会有一个线程能操作队列的数据，以 LinkedBlockingQueue 为例子，put 时，会加 put 锁，并只对队尾 tail 进行操作，take 时，会加 take 锁，并只对队头 head 进行操作，remove 时，会同时加 put 和 take 锁，所以各种操作都是线程安全的

### 4.5、take 与 put 方法

**take 的时候也会加锁么？**
- 是的，take 时也会加锁的，像 LinkedBlockingQueue 在执行 take 方法时，在拿数据的同时，会把当前数据删除掉，就改变了链表的数据结构，所以需要加锁来保证线程安全。

**既然 put 和 take 都会加锁，是不是同一时间只能运行其中一个方法？**
- 这个需要看情况而言，对于 LinkedBlockingQueue 来说，队列的 put 和 take 都会加锁，但两者的锁是不一样的，所以两者互不影响，可以同时进行的，对于 ArrayBlockingQueue 而言，put 和 take 是同一个锁，所以同一时刻只能运行一个方法

**使用队列的 put、take 方法有什么危害，如何避免**
- 当队列满时，使用 put 方法，会一直阻塞到队列不满为止。
- 当队列空时，使用 take 方法，会一直阻塞到队列有数据为止
- 两个方法都是无限（永远、没有超时时间的意思）阻塞的方法，容易使得线程全部都阻塞住，大流量时，导致机器无线程可用，所以建议在流量大时，使用 offer 和 poll 方法来代替两者，我们只需要设置好超时阻塞时间，这两个方法如果在超时时间外，还没有得到数据的话，就会返回默认值（LinkedBlockingQueue 为例），这样就不会导致流量大时，所有的线程都阻塞住了

### 4.6、SynchronousQueue

假设 SynchronousQueue 底层使用的是堆栈，线程 1 执行 take 操作阻塞住了，然后有线程 2 执行 put 操作，问此时线程 2 是如何把 put 的数据传递给 take 的？

首先线程 1 被阻塞住，此时堆栈头就是线程 1 了，此时线程 2 执行 put 操作，会把 put 的数据赋值给堆栈头的 match 属性，并唤醒线程 1，线程 1 被唤醒后，拿到堆栈头中的 match 属性，就能够拿到 put 的数据了。

严格上说并不是 put 操作直接把数据传递给了 take，而是 put 操作改变了堆栈头的数据，从而 take 可以从堆栈头上直接拿到数据，堆栈头是 take 和 put 操作之间的沟通媒介；

### 4.7、如何实现一个不会发生OOM的队列

两种思路：
- 一种是限制整个队列能占多数内存，即 MemoryLimited，每次往队列添加添加数据，都需要记录当前对了占的总内存大小，如果超过了，则不允许提交数据到队列；可以使用 Instrumentation 来实现；一句话：限制的是这个队列最多能使用多少空间，是站在队列的角度
- 另一种是定义当前最大剩余内存，即限制的是 JVM 里面的剩余空间。比如默认就是当整个 JVM 只剩下 256M 可用内存的时候，再往队列里面加元素我就不让你加了，因为整个内存都比较吃紧了，队列就不能无限制的继续添加了，从这个角度来规避了 OOM 的风险，可以用过 ManagementFactory 获取内存的运行状态；

上面两种核心思路都是限制内存，但是实际上：
- MemoryLimitedLBQ 是限制每个队列对象的使用内存大小，但是使用 Instrumetation.getObjectSize() 只能检查这个对象的占用，这个对象引用的对象的内存占用没有算进去；
- MemorySafeLBQ 通过 MemoryMXBean 查看当前堆内存实际占用，但是没有考虑 GC 的因素，当前内存占用也许大部分都是可以回收的的，也许 YoungGC 之后就有那么多内存可以使用了。
  
总体来看，MemorySafeLBQ 限制并没有限制住，MemoryLimitedLBQ 限制的可能太死。这个可能还是需要底层 JDK + JNI 去实现，查看队列中的对象的存活时间，结合整个堆对象的存活时间以及占用情况，判断是否真的内存紧缺了。或者是限制一个队列用的内存大小，但是检查里面放入对象的本身内存以及所有引用内存
 

## 5、线程池相关面试

### 5.1、threadpoolexecutor的内部数据结构是什么样子的



### 5.2、线程池的运行状态有多少种

如下代码所示，线程池的状态分为了
- RUNNING：接受新的任务和处理队列中的任务
- SHUTDOWN：拒绝新的任务，但是处理队列中的任务
- STOP：拒绝新的任务，不处理队列中的任务，并且中断在执行的任务
- TIDYING：所有任务已经终止（terminated），workerCount=0，线程
- TERMINATED：terminated已完成

线程池的状态按如下方式进行转换
- RUNNING->SHUTDOWN 调用`shutdown()`方法
- (RUNNING or SHUTDOWN) -> STOP，调用`shutdownNow()`
- SHUTDOWN -> TIDYING，当队列和线程池都为空
- STOP->TIDYING,线程池为空
- TIDYING -> TERMINATED，当`terminated()` hook method 执行完毕，所有线程都在`awaitTermination()`中等待线程池状态到达TERMINATED。

### 5.3、工作线程数是怎么存储的

工作线程一般是存储在HashSet中的，在addWorker时，如果 compareAndIncrementWorkerCount 成功，则会构建一个Worker，并添加到 workers集合中，如果添加到集合中成功，则立刻执行线程；

### 5.4、Worker对象里面的数据结构是什么样子的

Worker是一个继承AQS并实现了Runnable接口的内部类，主要有 Thread的成员变量表示当前正在执行的线程，Runnable表示需要运行的任务，可能为 null

“线程池中的线程”，其实就是Worker；等待队列中的元素，是我们提交的Runnable任务；

构造方法传入 Runnable，代表第一个执行的任务，可以为空。构造方法中新建一个线程；构造函数主要是做三件事：
- 设置同步状态state为-1，同步状态大于0表示就已经获取了锁；
- 设置将当前任务task设置为firstTask；
- 利用Worker本身对象this和ThreadFactory创建线程对象。

### 5.5、execute里面主要做了什么事情

- 核心线程数小于corePoolSize，则需要创建新的工作线程来执行任务
- 核心线程数大于等于corePoolSize，需要将线程放入到阻塞任务队列中等待执行
- 队列满时需要创建非核心线程来执行任务，所有工作线程（核心线程+非核心线程）数量要小于等于maximumPoolSize
- 如果工作线程数量已经达到maximumPoolSize，则拒绝任务，执行拒绝策略

### 5.6、addWorker是做什么事情

主要是创建一个线程，并且线程开始运行；
- 首先会判断线程池状态，如果正常，通过CAS增加运行的线程数（该方法有两个参数：需要运行的任务、是否为核心线程数）
- 然后创建一个Worker对象，需要运行的任务作为构造方法的参数；
- 如果需要运行的任务不为空，则通过Lock，是否启动该线程；

### 5.7、runWorker里面是如何执行处理的

- 提交任务时如果**工作线程**数量小于核心线程数量，则`firstTask != null`，一路顺利执行然后阻塞在队列的poll上。
- 提交任务时如果**工作线程**数量大于等于核心线程数量，则`firstTask == null`，需要从任务队列中poll一个任务执行，执行完毕之后继续阻塞在队列的poll上。
- 提交任务时如果**工作线程**数量大于等于核心线程数量并且任务队列已满，需要创建一个**非核心线程**来执行任务，则`firstTask != null`，执行完毕之后继续阻塞在队列的poll上，不过注意这个poll是允许超时的，最多等待时间为`keepAliveTime`。
- 工作线程在跳出循环之后，线程池会移除该线程对象，并且试图终止线程池（因为需要考量shutdown的情况）
- `ThreadPoolExecutor`提供了任务执行前和执行后的钩子方法，分别为`beforeExecute`和`afterExecute`。
- 工作线程通过实现`AQS`来保证线程安全（每次执行任务的时候都会`lock`和`unlock`）

### 5.8、线程的回收

**核心线程数会被回收吗？需要什么设置？**

核心线程数默认是不会被回收的，如果需要回收核心线程数，需要调用方法：allowCoreThreadTimeout，其对应的参数默认值时false；

**空闲线程如何回收**

超过corePoolSize的空闲线程由线程池回收，线程池Worker启动跑第一个任务之后就一直循环遍历线程池任务队列，超过指定超时时间获取不到任务就remove Worker，最后由垃圾回收器回收；

在runWorker方法中，如果循环中没有从队列中获取数据，则跳出循环：
```java
final void runWorker(Worker w) {
    Thread wt = Thread.currentThread();
    Runnable task = w.firstTask;
    w.firstTask = null;
    w.unlock(); // allow interrupts
    boolean completedAbruptly = true;
    try {
        while (task != null || (task = getTask()) != null) {
            ....
        }
        completedAbruptly = false;
    } finally {
        processWorkerExit(w, completedAbruptly);
    }
}
private Runnable getTask() {
    boolean timedOut = false; // Did the last poll() time out?
    for (;;) {
        int c = ctl.get();
        ....
        // allowCoreThreadTimeOut 表示是否允许核心线程超时，默认是false，如果调用 allowCoreThreadTimeOut(boolean value)，传入true，表示核心线程数需要回收，
        boolean timed = allowCoreThreadTimeOut || wc > corePoolSize;
        ... 
        try {
            // timed为true，调用poll方法：任务队列取任务了，带了timeOut参数的poll方法超时未能从任务队列获取任务即返回null，从而实现最终的线程回收
            Runnable r = timed ? workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) :
                workQueue.take();
            if (r != null)
                return r;
            timedOut = true;
        } catch (InterruptedException retry) {
            timedOut = false;
        }
    }
}
private void processWorkerExit(Worker w, boolean completedAbruptly) {
    if (completedAbruptly) // If abrupt, then workerCount wasn't adjusted
        decrementWorkerCount();
    final ReentrantLock mainLock = this.mainLock;
    mainLock.lock();
    try {
        completedTaskCount += w.completedTasks;
        // 回收线程
        workers.remove(w);
    } finally {
        mainLock.unlock();
    }
}
```

### 5.9、线程池被创建后里面有线程吗？如果没有的话，你知道有什么方法对线程池进行预热吗？

线程池被创建后如果没有任务过来，里面是不会有线程的。如果需要预热的话可以调用下面的两个方法：
- 创建全部核心线程：preStartAllCoreThread
- 创建一个核心线程：preStartCoreThread

### 5.10、如果线程池队列满了，仍要执行任务该如何处理？

可以将其拒绝策略设置为 CallerRunsPolicy，在线程池没有关闭（调用shut Down）的情况下，直接由调用线程来执行该任务。当触发拒绝策略时，只要线程池没有关闭，就由提交任务的当前线程处理

### 5.11、如果线程池处理任务过程中Java进程突然宕掉了，数据丢失了怎么办？

如果要提交一个任务到线程池里去，在提交之前，可以将当前任务信息插入数据库，更新其状态：未提交、已提交、已完成。提交成功后，更新他的状态为已提交状态。

系统重启，后台线程去扫描数据库里的未提交和已提交状态的任务，可以把任务信息读出来，重提交到线程池里，继续进行执行；

但是重新执行的时候需要注意：提交任务时需要保证任务执行的幂等性，不要重复执行；

如果是在写入数据时宕机，原始信息没存入数据库，也丢了，这种一般有好的方法处理吗? 每一个操作进行封装，如果写入数据库的时候宕机了，认为此任务没有提交成功，直接返回失败状态就行了，比如你买东西，请求到后台了，写数据库的时候宕机了，返回一个false，你没买到就得了

### 5.12、线程池的优点与弊端

优点：
- 线程是稀缺资源，使用线程池可以减少创建和销毁线程的次数，每个工作线程都可以重复使用。
- 可以根据系统的承受能力，调整线程池中工作线程的数量，防止因为消耗过多内存导致服务器崩溃。

弊端：
- 死锁：任何多线程应用程序都有死锁风险。当一组进程或线程中的每一个都在等待一个只有该组中另一个进程才能引起的事件时，我们就说这组进程或线程 死锁了。死锁的最简单情形是：线程 A 持有对象 X 的独占锁，并且在等待对象 Y 的锁，而线程 B 持有对象 Y 的独占锁，却在等待对象 X 的锁。除非有某种方法来打破对锁的等待（Java 锁定不支持这种方法），否则死锁的线程将永远等下去；

- 资源不足：如果线程池太大，那么被那些线程消耗的资源可能严重地影响系统性能。在线程之间进行切换将会浪费时间，而且使用超出比您实际需要的线程可能会引起资源匮乏问题，因为池线程正在消耗一些资源，而这些资源可能会被其它任务更有效地利用。

    除了线程自身所使用的资源以外，服务请求时所做的工作可能需要其它资源，例如 JDBC 连接、套接字或文件，这些也都是有限资源，有太多的并发请求也可能引起失效，例如不能分配 JDBC 连接

- 并发错误：线程池和其它排队机制依靠使用 wait() 和 notify()，如果编码不正确，那么可能丢失通知，导致线程保持空闲状态，尽管队列中有工作要处理；

- 线程泄漏：各种类型的线程池中一个严重的风险是线程泄漏，当从池中除去一个线程以执行一项任务，而在任务完成后该线程却没有返回池时，会发生这种情况。发生线程泄漏的一种情形出现在任务抛出一个 RuntimeException 或一个 Error 时；

- 请求过载

### 5.13、空闲线程过多会有什么问题

首先，比较普通的一部分，一个线程的内存模型：
- 虚拟机栈
- 本地方法栈
- 程序计数器

需要注意以下几个内存的占用：
- ThreadLocal：业务代码是否使用了ThreadLocal？就算没有，Spring框架中也大量使用了ThreadLocal，你所在公司的框架可能也是一样。
- 局部变量：线程处于阻塞状态，肯定还有栈帧没有出栈，栈帧中有局部变量表，凡是被局部变量表引用的内存都不能回收。所以如果这个线程创建了比较大的局部变量，那么这一部分内存无法GC。
- TLAB机制：如果你的应用线程数处于高位，那么新的线程初始化可能因为Eden没有足够的空间分配TLAB而触发YoungGC

所以该问题可以如下作答：
- 线程池保持空闲的核心线程是它的默认配置，一般来讲是没有问题的，因为它占用的内存一般不大。怕的就是业务代码中使用ThreadLocal缓存的数据过大又不清理。
- 如果你的应用线程数处于高位，那么需要观察一下YoungGC的情况，估算一下Eden大小是否足够。如果不够的话，可能要谨慎地创建新线程，并且让空闲的线程终止；必要的时候，可能需要对JVM进行调参

### 5.14、keepAliveTime=0会怎么样

在JDK1.8中，`keepAliveTime=0`表示非核心线程执行完立刻终止。

默认情况下，keepAliveTime小于0，初始化的时候才会报错；但如果`allowsCoreThreadTimeOut`，keepAliveTime必须大于0，不然初始化报错

### 5.15、Spring中有哪些和ThreadPoolExecutor类似的工具

- SimpleAsyncTaskExecutor：每次请求新开线程，没有最大线程数设置.不是真的线程池，这个类不重用线程，每次调用都会创建一个新的线程。
- SyncTaskExecutor：不是异步的线程。同步可以用SyncTaskExecutor，但这个可以说不算一个线程池，因为还在原线程执行。这个类没有实现异步调用，只是一个同步操作。
- ConcurrentTaskExecutor：Executor的适配类，不推荐使用。如果ThreadPoolTaskExecutor不满足要求时，才用考虑使用这个类。
- SimpleThreadPoolTaskExecutor：监听Spring’s lifecycle callbacks，并且可以和Quartz的Component兼容.是Quartz的SimpleThreadPool的类。线程池同时被quartz和非quartz使用，才需要使用此类。

> Spring中使用的`@Async`注解，底层就是基于 SimpleAsyncTaskExecutor 去执行任务，只不过它不是线程池，而是每次都新开一个线程

### 5.14、任务执行过程中发生异常怎么处理？

如果某个任务执行出现异常，那么执行任务的线程会被关闭，而不是继续接收其他任务。然后会启动一个新的线程来代替它

### 5.15、如何实现优先开启更多的线程，而把队列当成一个后备方案呢

比如有这么一个例子，任务执行得很慢，需要 10 秒，如果线程池可以优先扩容到 5 个最大线程，那么这些任务最终都可以完成，而不会因为线程池扩容过晚导致慢任务来不及处理；

大致的实现思路：
- 由于线程池在工作队列满了无法入队的情况下会扩容线程池，那么我们是否可以重写队列的 offer 方法，造成这个队列已满的假象呢？
- 由于我们 Hack 了队列，在达到了最大线程后势必会触发拒绝策略，那么能否实现一个自定义的拒绝策略处理程序，这个时候再把任务真正插入队列呢？

Tomcat 线程池也实现了类似的效果：[ThreadPoolExecutor](https://github.com/apache/tomcat/blob/main/java/org/apache/tomcat/util/threads/ThreadPoolExecutor.java)

### 5.16、工作中线程池提交有两种方式，execute和submit；他们的区别是什么，在使用的时候，需要注意什么问题

### 5.17、使用线程池的时候，并且程序运行起来了，那么有什么办法能够在不停止服务的情况下改变这个core size



## 6、FutureTask

### 6.1、FutureTask里面有多少种状态

有7种状态：
```java
private static final int NEW          = 0; // 新建
private static final int COMPLETING   = 1; // 正在处理
private static final int NORMAL       = 2; // 正常结束，最终态
private static final int EXCEPTIONAL  = 3; // 异常
private static final int CANCELLED    = 4; // 被取消
private static final int INTERRUPTING = 5; // 中断中
private static final int INTERRUPTED  = 6; // 已被中断
```
* NEW -> COMPLETING -> NORMAL
* NEW -> COMPLETING -> EXCEPTIONAL
* NEW -> CANCELLED
* NEW -> INTERRUPTING -> INTERRUPTED

### 6.2、里面是什么数据结构

WaitNode内部类，记录当前线程以及下一个需要执行的任务；

### 6.3、在执行task的时候都做了什么事情

task运行实际上执行的是 run方法，

### 6.4、nodewaiters是干什么的



## 7、synchronized 无法禁止指令重排序，确能够保证有序性？

主要考察点：Java内存模型、并发编程有序性问题、指令重排、`synchronized`锁、可重入锁、排它锁、`as-if-serial`语义、单线程&多线程

答案点：
- 为了进一步提升计算机各方面能力，在硬件层面做了很多优化，如处理器优化和指令重排等，但是这些技术的引入就会导致有序性问题；synchronized是无法禁止指令重排和处理器优化的
- 最好的解决有序性问题的办法，就是禁止处理器优化和指令重排，就像volatile中使用内存屏障一样；
- 虽然很多硬件都会为了优化做一些重排，但是在Java中，不管怎么排序，都不能影响单线程程序的执行结果。这就是`as-if-serial`语义，所有硬件优化的前提都是必须遵守`as-if-serial`语义；
- `synchronized`，是Java提供的锁，可以通过其对Java中的对象加锁，并且他是一种排他的、可重入的锁；其是JVM层面上实现的锁；
- 当某个线程执行到一段被`synchronized`修饰的代码之前，会先进行加锁，执行完之后再进行解锁。在加锁之后，解锁之前，其他线程是无法再次获得锁的，只有这条加锁线程可以重复获得该锁；
- `synchronized`通过排他锁的方式就保证了同一时间内，被`synchronized`修饰的代码是单线程执行的。所以呢，这就满足了`as-if-serial`语义的一个关键前提，那就是单线程，因为有`as-if-serial`语义保证，单线程的有序性就天然存在了；

## 8、为什么Integer、String等对象不适合用作锁

因为这些类中都用到了享元设计模式，这会导致锁看上去是私有的，但是实际上是共有的；不过可以直接使用new这些来创建新的对象，不使用其内部的对象池，这样创建出来的对象就不会共有
```java
class A {
  Long al=Long.valueOf(1); // 可以使用 new Long
  public void setAX(){
    synchronized (al) {
      //省略代码无数
    }
  }
}
class B {
  Long bl=Long.valueOf(1);
  public void setBY(){
    synchronized (bl) {
      //省略代码无数
    }
  }
}
```

## 9、锁调优策略

- 减少锁持有的时间；
- 锁的细粒度化：ConcurrentHashMap
- 锁粗化
- 锁分离：读写分离、操作分离；
- 无锁（CAS）

## 10、synchronized

### 10.1、synchronized 与 Lock 的区别

- 区别：
	- synchronized 是Java的一个关键字，其是在JVM层面上实现的，如果线程执行时发生异常，JVM 会自动释放锁。因此不会导致死锁现象发生；Lock 是接口，通过代码实现的，在发生异常时，如果没有主动通过unLock()去释放锁，则很可能造成死锁现象，因此使用 Lock时需要在finally块中释放锁；
	- Lock 可以让等待锁的线程响应中断，而 synchronized 使用时等待的线程会一直等待下去，不能响应中断；
	- 通过 Lock 可以知道有没有成功获取锁，而 synchronized 不行；
	- 在资源竞争不是很激烈的情况下， synchronized 的性能要优于 Lock，但是在资源竞争很激烈的情况下，synchronized性能会下降几十倍，但是 Lock 是保持常态的；
	- 在 JDK1.5 之后 synchronized 作了很多优化，在性能上已经有很大提升.	如：自旋锁、锁消除、锁粗化、轻量级锁、偏向锁
	- synchronized 和 ReentrantLock 都是可重入锁；
	- 公平锁：即尽量以请求锁的顺序来获取锁，synchronized 是非公平锁，无法保证等待的线程获取锁的顺序；ReentrantLock和ReentrantReadWriteLock，默认情况下是非公平锁，但是可以设置为 公平锁；
	- synchronized的锁状态是无法在代码中直接判断的，但是ReentrantLock可以通过ReentrantLock#isLocked判断；
	- 机制：synchronized是操作Mark Word，Lock是调用Unsafe类的park方法。

- Lock 适用场景：
	- 某个线程在等待一个锁的控制权的这段时间需要中断；
	- 需要分开处理一些wait-notify，ReentrantLock 里面的 Condition应用，能够控制notify哪个线程，锁可以绑定多个条件
	- 具有公平锁功能，每个到来的线程都将排队等候

- 如何选择Lock和synchronized
	- 尽可能避免使用者两者，可以使用java.util.concurrent包下的；
	- 如果可以使用synchronized，就使用，因为使用lock会增加代码复杂度；

### 10.2、synchronized使用时需要注意：

- 锁对象不能为空：因为锁的信息时保存在对象头中的，如果对象都没有，锁信息无法保存
- 作用域不宜过大：synchronized包裹的范围，会导致性能下降；
- 避免死锁：相互等待锁导致死锁

### 10.3、`synchronized`是无法禁止指令重排和处理器优化的，那`synchronized`如何保证有序性？

Java中天然有序性：如果在本线程内观察，所有操作都是天然有序的。如果在一个线程中观察另一个线程，所有操作都是无序的。
- `as-if-serial`语义：不管怎么重排序（编译器和处理器为了提高并行度），单线程程序的执行结果都不能被改变。编译器和处理器无论如何优化，都必须遵守as-if-serial语义。也就是说`as-if-serial`语义保证了单线程中，指令重排是有一定的限制的，而只要编译器和处理器都遵守了这个语义，那么就可以认为单线程程序是按照顺序执行。

由于`synchronized`修饰的代码，同一时间只能被同一线程访问。那么也就是单线程执行的。所以，可以保证其有序性

## 11、AQS

### 11.1、同步队列入队、出队时机

**同步队列入队时机：**
- 多个线程请求锁，获取不到锁的线程需要到同步队列中排队阻塞；
- 条件队列中的节点被唤醒，会从条件队列中转移到同步队列中来。

**同步队列出队时机：**
- 锁释放时，头节点出队；
- 获得锁的线程，进入条件队列时，会释放锁，同步队列头节点开始竞争锁。

四个时机的过程可以参考 AQS 源码：1-参考 acquire 方法执行过程、2-参考 signal 方法、3-参考 release 方法、4-参考 await 方法

### 11.2、为什么有同步对还需要条件队列

的确，一般情况下，我们只需要有同步队列就好了，但在上锁后，需要操作队列的场景下，一个同步队列就搞不定了，需要条件队列进行功能补充，比如当队列满时，执行 put 操作的线程会进入条件队列等待，当队列空时，执行 take 操作的线程也会进入条件队列中等待，从一定程度上来看，条件队列是对同步队列的场景功能补充

### 11.3、条件队列中的元素入队和出队的时机和过程

- 入队时机：执行 await 方法时，当前线程会释放锁，并进入到条件队列。

- 出队时机：执行 signal、signalAll 方法时，节点会从条件队列中转移到同步队列中。

具体的执行过程，可以参考源码解析中 await 和 signal 方法

### 11.4、条件队列中的节点转移到同步队列中去的时机和过程

**转移时机：**当有线程执行 signal、signalAll 方法时，从条件队列的头节点开始，转移到同步队列中去。

**转移过程主要是以下几步：**
- 找到条件队列的头节点，头节点 next 属性置为 null，从条件队列中移除了；
- 头节点追加到同步队列的队尾；
- 头节点状态（waitStatus）从 CONDITION 修改成 0（初始化状态）；
- 将节点的前一个节点状态置为 SIGNAL

### 11.5、线程入条件队列时，为什么需要释放持有的锁

如果当前线程不释放锁，一旦跑去条件队里中阻塞了，后续所有的线程都无法获得锁；

正确的场景应该是：当前线程释放锁，到条件队列中去阻塞后，其他线程仍然可以获得当前锁。

### 11.6、自定义锁，大概的实现思路是什么样子的

可以参考ReentrantLock的实现来描述
- 新建内部类继承 AQS，并实现 AQS 的 tryAcquire 和 tryRelease 两个方法，在 tryAcquire 方法里面实现控制能否获取锁，比如当同步器状态 state 是 0 时，即可获得锁，在 tryRelease 方法里面控制能否释放锁，比如将同步器状态递减到 0 时，即可释放锁；
- 对外提供 lock、release 两个方法，lock 表示获得锁的方法，底层调用 AQS 的 acquire 方法，release 表示释放锁的方法，底层调用 AQS 的 release 方法

### 11.7、AQS用来做什么

### 11.8、AQS如何工作的

### 11.9、手写程序：如何使用AQS实现Mutex

### 11.10、AQS如何实现公平性

### 11.11、CAS在AQS中的作用

### 11.12、AQS内部的CHL算法的工作原理

### 11.13、AQS的核心思想是什么

AQS核心思想是，如果被请求的共享资源空闲，则将当前请求资源的线程设置为有效的工作线程，并且将共享资源设置为锁定状态。如果被请求的共享资源被占用，那么就需要一套线程阻塞等待以及被唤醒时锁分配的机制，这个机制AQS是用CLH队列锁实现的，即将暂时获取不到锁的线程加入到队列中

## 12、阻塞队列

### 12.1、有界队列与无界队列

有界：缓冲区大小恒定（ArrayBlockingQueue）

无界：缓冲区大小无限

### 12.2、LinkedBlockingQueue 的双向队列与 SynchronousQueue 的双向队列有什么区别

有无match操作；生产者与消费者是否匹配

## 13、线程同步的方式

Java 中实现线程同步的方式有很多，大体可以分为以下 8 类。
- 使用 Object 类中的 wait、notify、notifyAll 等函数。由于这种编程模型非常复杂，现在已经很少用了。这里有一个关键点，那就是对于这些函数的调用，必须放在同步代码块里才能正常运行；
- 使用 ThreadLocal 线程局部变量的方式，每个线程一个变量；
- 使用 synchronized 关键字修饰方法或者代码块。这是 Java 中最常见的方式，有锁升级的概念；
- 使用 Concurrent 包里的可重入锁 ReentrantLock。使用 CAS 方式实现的可重入锁；
- 使用 volatile 关键字控制变量的可见性，这个关键字保证了变量的可见性，但不能保证它的原子性；
- 使用线程安全的阻塞队列完成线程同步。比如，使用 LinkedBlockingQueue 实现一个简单的生产者消费者；
- 使用原子变量。Atomic* 系列方法，也是使用 CAS 实现的；
- 使用 Thread 类的 join 方法，可以让多线程按照指定的顺序执行；

## 14、锁优化方式

- 减少锁粒度：把资源进行抽象，针对每类资源使用单独的锁进行保护；
- 减少锁持有时间：通过让锁资源尽快地释放，减少锁持有的时间，其他线程可更迅速地获取锁资源，进行其他业务的处理；
- 锁分级
- 锁分离：读写锁，读写锁适合读多写少的场景；
- 锁消除：通过 JIT 编译器，JVM 可以消除某些对象的加锁操作；

## 15、LockSupport

AQS框架借助于两个类：Unsafe(提供CAS操作)和LockSupport(提供park/unpark操作)

写出分别通过wait/notify和LockSupport的park/unpark实现同步? 

LockSupport.park()会释放锁资源吗? 那么Condition.await()呢? 

如果在wait()之前执行了notify()会怎样? 

如果在park()之前执行了unpark()会怎样? 

### Thread.sleep()和Object.wait()的区别

首先，我们先来看看Thread.sleep()和Object.wait()的区别，这是一个烂大街的题目了，大家应该都能说上来两点。
- Thread.sleep()不会释放占有的锁，Object.wait()会释放占有的锁；
- Thread.sleep()必须传入时间，Object.wait()可传可不传，不传表示一直阻塞下去；
- Thread.sleep()到时间了会自动唤醒，然后继续执行；
- Object.wait()不带时间的，需要另一个线程使用Object.notify()唤醒；
- Object.wait()带时间的，假如没有被notify，到时间了会自动唤醒，这时又分好两种情况，一是立即获取到了锁，线程自然会继续执行；二是没有立即获取锁，线程进入同步队列等待获取锁；

其实，他们俩最大的区别就是Thread.sleep()不会释放锁资源，Object.wait()会释放锁资源。

### Thread.sleep()和Condition.await()的区别

Object.wait()和Condition.await()的原理是基本一致的，不同的是Condition.await()底层是调用LockSupport.park()来实现阻塞当前线程的。

实际上，它在阻塞当前线程之前还干了两件事，一是把当前线程添加到条件队列中，二是“完全”释放锁，也就是让state状态变量变为0，然后才是调用LockSupport.park()阻塞当前线程。

### Thread.sleep()和LockSupport.park()的区别

LockSupport.park()还有几个兄弟方法——parkNanos()、parkUtil()等，我们这里说的park()方法统称这一类方法。

- 从功能上来说，Thread.sleep()和LockSupport.park()方法类似，都是阻塞当前线程的执行，且都不会释放当前线程占有的锁资源；
- Thread.sleep()没法从外部唤醒，只能自己醒过来；
- LockSupport.park()方法可以被另一个线程调用LockSupport.unpark()方法唤醒；
- Thread.sleep()方法声明上抛出了InterruptedException中断异常，所以调用者需要捕获这个异常或者再抛出；
- LockSupport.park()方法不需要捕获中断异常；
- Thread.sleep()本身就是一个native方法；
- LockSupport.park()底层是调用的Unsafe的native方法；

### Object.wait()和LockSupport.park()的区别

二者都会阻塞当前线程的运行，他们有什么区别呢? 经过上面的分析相信你一定很清楚了，真的吗? 往下看！

- Object.wait()方法需要在synchronized块中执行；
- LockSupport.park()可以在任意地方执行；
- Object.wait()方法声明抛出了中断异常，调用者需要捕获或者再抛出；
- LockSupport.park()不需要捕获中断异常；
- Object.wait()不带超时的，需要另一个线程执行notify()来唤醒，但不一定继续执行后续内容；
- LockSupport.park()不带超时的，需要另一个线程执行unpark()来唤醒，一定会继续执行后续内容；
- 如果在wait()之前执行了notify()会怎样? 抛出IllegalMonitorStateException异常；
- 如果在park()之前执行了unpark()会怎样? 线程不会被阻塞，直接跳过park()，继续执行后续内容；

park()/unpark()底层的原理是“二元信号量”，你可以把它相像成只有一个许可证的Semaphore，只不过这个信号量在重复执行unpark()的时候也不会再增加许可证，最多只有一个许可证。

### LockSupport.park()会释放锁资源吗?

不会，它只负责阻塞当前线程，释放锁资源实际上是在Condition的await()方法中实现的。

## 16、为什么任意一个 Java 对象都能成为锁对象呢

Java 中的每个对象都派生自 Object 类，而每个 Java Object 在 JVM 内部都有一个 native 的 C++对象 oop/oopDesc 进行对应。其次，线程在获取锁的时候，实际上就是获得一个监视器对象(monitor) ,monitor 可以认为是一个同步对象，所有的 Java 对象是天生携带 monitor。

多个线程访问同步代码块时，相当于去争抢对象监视器修改对象中的锁标识, ObjectMonitor 这个对象和线程争抢锁的逻辑有密切的关系

## 17、ThreadLocalRandom可以把它的实例设置到静态变量中，在多线程情况下重用吗？

是不能重用的，ThreadLocalRandom 文档里有这么一条：
```
Usages of this class should typically be of the form: ThreadLocalRandom.current().nextX(…) (where X is Int, Long, etc). When all usages are of this form, it is never possible to accidently share a ThreadLocalRandom across multiple threads.
```
current() 的时候初始化一个初始化种子到线程，每次 nextseed 再使用之前的种子生成新的种子：
```java
UNSAFE.putLong(t = Thread.currentThread(), SEED, r = UNSAFE.getLong(t, SEED) + GAMMA);
```
如果你通过主线程调用一次 current 生成一个 ThreadLocalRandom 的实例保存起来，那么其它线程来获取种子的时候必然取不到初始种子，必须是每一个线程自己用的时候初始化一个种子到线程;

## 18、Synchronized 会发生进程间的上下文切换吗？具体又会发生在哪些环节呢？

锁的竞争太激烈会导致锁升级为重量级锁，未抢到锁的线程会进入monitor，而monitor依赖于底层操作系统的mutex lock，获取锁时会发生用户态和内核态之间的切换，所以会发生进程间的上下文切换

## 19、Java线程创建调用的系统函数

在Linux系统中
- 创建进程的话，调用的系统调用是fork，在copy_process函数里面，会将五大结构files_struct、fs_struct、sighand_struct、signal_struct、mm_struct 都复制一遍，从此父进程和子进程各用个的数据结构；
- 创建线程的话，调用的是系统调用clone，在copy_process函数里面，五大结构仅仅是引用计数加一，也就是线程共享进程的数据结构；

## 20、公平锁、非公平锁、自旋锁的区别，分别有啥优缺点

大目老师答：锁就是大家在抢着去上厕所一个人在蹲坑，一帮人在等他拉完出来
公平锁：在外面等着上厕所的人排队，先排队的先进去 
非公平锁：就是可能会插队
synchronized就是非公平的，reentrantlock可以指定公平或者非公平

## 21、Redis、堆内存、堆外内存在并发中转换问题

大家最开始抗并发，首先会想到redis ，然后过了很久 就会发现redis不合适，就会用堆内内存，然后过了很久又发现不合适，就会选择堆外内存存储啦，问3者之间为什么会转换？
阿神老师答：第一阶段：并发在几千上万的时候，你的热数据都会想到使用缓存来存储也就是我们常说的redis，
但是过了一段时间，业务高峰期上涨发现redis经常被打满扩容已经解决不了问题了，尤其是流量入口qps很高的，用redis做缓存不太合适。这个时候程序就应该经过优化存储到堆内了，我们不做redis请求直接请求内存性能高。redis只做一个兜底策略，或者快速降级，很开心又玩了一段时间……
又过了一段时间，可能由于热数据和非热数据经常转换，发现应用服务频繁GC导致系统性能严重下降，这时候可能会选择LRU去做淘汰策略，但是有时候又发现不太合适具体根据业务，所以就变成了堆外存储
本荒提问对堆存储是什么：
内存：操作系统16G内存你分了8G给你的应用，剩下8G是OS的也可以称为堆外。

## 12、多线程面试题

https://segmentfault.com/a/1190000013813740

https://segmentfault.com/a/1190000013896476

```
什么是线程？
什么是线程安全和线程不安全？
什么是自旋锁？
什么是Java内存模型？
什么是CAS？
什么是乐观锁和悲观锁？
什么是AQS？
什么是原子操作？在Java Concurrency API中有哪些原子类(atomic classes)？
什么是Executors框架？
什么是阻塞队列？如何使用阻塞队列来实现生产者-消费者模型？
什么是Callable和Future?
什么是FutureTask?
什么是同步容器和并发容器的实现？
什么是多线程？优缺点？
什么是多线程的上下文切换？
ThreadLocal的设计理念与作用？
ThreadPool（线程池）用法与优势？
Concurrent包里的其他东西：ArrayBlockingQueue、CountDownLatch等等。
synchronized和ReentrantLock的区别？
Semaphore有什么作用？
Java Concurrency API中的Lock接口(Lock interface)是什么？对比同步它有什么优势？
Hashtable的size()方法中明明只有一条语句”return count”，为什么还要做同步？
ConcurrentHashMap的并发度是什么？
ReentrantReadWriteLock读写锁的使用？
CyclicBarrier和CountDownLatch的用法及区别？
LockSupport工具？
Condition接口及其实现原理？
Fork/Join框架的理解?
wait()和sleep()的区别?
线程的五个状态（五种状态，创建、就绪、运行、阻塞和死亡）?
start()方法和run()方法的区别？
Runnable接口和Callable接口的区别？
volatile关键字的作用？
Java中如何获取到线程dump文件？
线程和进程有什么区别？
线程实现的方式有几种（四种）？
高并发、任务执行时间短的业务怎样使用线程池？并发不高、任务执行时间长的业务怎样使用线程池？并发高、业务执行时间长的业务怎样使用线程池？
如果你提交任务时，线程池队列已满，这时会发生什么？
锁的等级：方法锁、对象锁、类锁?
如果同步块内的线程抛出异常会发生什么？
并发编程（concurrency）并行编程（parallellism）有什么区别？
如何保证多线程下 i++ 结果正确？
一个线程如果出现了运行时异常会怎么样?
如何在两个线程之间共享数据?
生产者消费者模型的作用是什么?
怎么唤醒一个阻塞的线程?
Java中用到的线程调度算法是什么
单例模式的线程安全性?
线程类的构造方法、静态块是被哪个线程调用的?
同步方法和同步块，哪个是更好的选择?
如何检测死锁？怎么预防死锁？
```

# 五、JVM虚拟机

- [Java虚拟机常问问题](http://www.reins.altervista.org/java/gc1.4.2_faq.html)

## 1、同一个类加载器对象是否可以加载同一个类文件多次并且得到多个Class对象而都可以被java层使用吗

可以通过`Unsafe`的`defineAnonymousClass`来实现同一个类文件被同一个类加载器对象加载多遍的效果，因为并没有将其放到`SystemDictionary`里，因此我们可以无穷次加载同一个类；
- 正常的类加载：在JVM里有一个数据结构叫做SystemDictionary，这个结构主要就是用来检索我们常说的类信息，这些类信息对应的结构是klass，对SystemDictionary的理解，可以认为就是一个Hashtable，key是类加载器对象+类的名字，value是指向klass的地址；这样当我们任意一个类加载器去正常加载类的时候，就会到这个SystemDictionary中去查找，看是否有这么一个klass可以返回，如果有就返回它，否则就会去创建一个新的并放到结构里；

- defineAnonymousClass：创建了一个匿名的类，不过这种匿名的概念和我们理解的匿名是不太一样的。这种类的创建通常会有一个宿主类，也就是第一个参数指定的类，这样一来，这个创建的类会使用这个宿主类的定义类加载器来加载这个类，最关键的一点是这个类被创建之后并不会丢到上述的SystemDictionary里，也就是说我们通过正常的类查找，比如Class.forName等api是无法去查到这个类是否被定义过的。因此过度使用这种api来创建这种类在一定程度上会带来一定的内存泄露；

	jvm通过invokeDynamic可以支持动态类型语言，这样一来其实我们可以提供一个类模板，在运行的时候加载一个类的时候先动态替换掉常量池中的某些内容，这样一来，同一个类文件，我们通过加载多次，并且传入不同的一些cpPatches，也就是defineAnonymousClass的第三个参数， 这样就能做到运行时产生不同的效果
	
	```java
	public static void main(String args[]) throws Throwable {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(null);
        String filePath = "A.class";
        byte[] buffer = getFileContent(filePath); // 读取class文件
        Class<?> c1 = unsafe.defineAnonymousClass(SameLoaderLoadOneClassMore.class, buffer, null);
        Class<?> c2 = unsafe.defineAnonymousClass(SameLoaderLoadOneClassMore.class, buffer, null);
        System.out.println(c1 == c2);
    }
	```

## 2、JVM理论上最多支持多少个线程

能创建的线程数的具体计算：(MaxProcessMemory - JVMMemory - ReservedOsMemory) / (ThreadStackSize) = Number of threads
- MaxProcessMemory 	指的是一个进程的最大内存
- JVMMemory         JVM内存
- ReservedOsMemory  保留的操作系统内存
- ThreadStackSize   线程栈的大小-

[一个Java进程创建多少个线程](https://club.perfma.com/article/244079)

**如何运行更多线程：**
- 减少Xss配置；
- 栈能够分配的内存：机器总内存 - 操作系统内存 - 堆内存 - 方法区内存 - 程序计数器内存 - 直接内存
- 尽量杀死其他程序；
- 操作系统对线程数目的限制：
	- `cat /proc/sys/kernel/threads-max`
		- 作用：系统支持的最大线程数，表示物理内存决定的理论系统进程数上限，一般会很大
		- 修改：sysctl -w kernel.threads-max=7726
	- `cat /proc/sys/kernel/pid_max`
		- 作用：查看系统限制某用户下最多可以运行多少进程或线程
		- 修改：sysctl -w kernel.pid_max=65535
	- `cat /proc/sys/vm/max_map_count`
		- 作用：限制一个进程可以拥有的VMA(虚拟内存区域)的数量，虚拟内存区域是一个连续的虚拟地址空间区域。在进程的生命周期中，每当程序尝试在内存中映射文件，链接到共享内存段，或者分配堆空间的时候，这些区域将被创建。
		- 修改：sysctl -w vm.max_map_count=262144
	- `ulimit –u`
		- 作用：查看用户最多可启动的进程数目
		- 修改：ulimit -u 65535

## 3、进程分配内存不够时向Linux申请内存时，Linux系统如何处理

## 4、JDK7、8、9 默认垃圾收集器分别是什么？

`java -XX:+PrintCommandLineFlags -version` 在各个版本下查看jdk的默认参数

**JDK7：**
```
$ java -XX:+PrintCommandLineFlags -version
-XX:InitialHeapSize=268435456 -XX:MaxHeapSize=4294967296 -XX:+PrintCommandLineFlags -XX:+UseCompressedOops -XX:+UseParallelGC
java version "1.7.0_80"
Java(TM) SE Runtime Environment (build 1.7.0_80-b15)
Java HotSpot(TM) 64-Bit Server VM (build 24.80-b11, mixed mode)
```
**JDK8：**
```
$ java -XX:+PrintCommandLineFlags -version
-XX:InitialHeapSize=268435456 -XX:MaxHeapSize=4294967296 -XX:+PrintCommandLineFlags -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseParallelGC
java version "1.8.0_151"
Java(TM) SE Runtime Environment (build 1.8.0_151-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.151-b12, mixed mode)
```
**JDK9：**
```

```

https://juejin.cn/post/6897977584005431310
https://juejin.cn/post/6936390496122044423

TLAB：https://juejin.cn/post/6925217498723778568

## 5、一个线程OOM后，其他线程还能运行吗

java中OOM又分很多类型，比如：
- 堆溢出（“java.lang.OutOfMemoryError: Java heap space”）
- 永久带溢出（“java.lang.OutOfMemoryError:Permgen space”）
- 不能创建线程（“java.lang.OutOfMemoryError:Unable to create new native thread”）

其实是还能运行的，下面验证结果：
```java
// jvm启动参数：-Xms16m -Xmx32m
public class JvmOomThread {
    public static void main(String[] args) {
        new Thread(() -> {
            List<byte[]> list = new ArrayList<>();
            while (true) {
                System.out.println(new Date().toString() + Thread.currentThread() + "==");
                byte[] b = new byte[1024 * 1024];
                list.add(b);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            while (true) {
                System.out.println(new Date().toString() + Thread.currentThread() + "==");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
```
运行结果如下
```
Sun Apr 11 20:53:47 CST 2021Thread[Thread-1,5,main]==
Sun Apr 11 20:53:47 CST 2021Thread[Thread-0,5,main]==
Exception in thread "Thread-0" java.lang.OutOfMemoryError: Java heap space
	at com.blue.fish.example.jvm.JvmOomThread.lambda$main$0(JvmOomThread.java:18)
	at com.blue.fish.example.jvm.JvmOomThread$$Lambda$1/2093631819.run(Unknown Source)
	at java.lang.Thread.run(Thread.java:748)
Sun Apr 11 20:53:48 CST 2021Thread[Thread-1,5,main]==
```
通过jvisualvm查看到其堆的变化：

![](image/Java堆内存-OOM变化情况.png)

上图是JVM对空间的边界，观察到在20:53:48~20:53:50之间曲线变化，发现使用堆的数量急剧下滑，这里可以表面一点：当一个线程抛出OOM异常后，它所占据的内存资源会全部被释放掉，从而不影响其他线程的运行；上面是对内存异常的情况，如果是栈溢出，结果也是一样的

总结：发生OOM的线程一般情况下会死亡，也就是会被终结掉，该线程持有的对象占用的heap都会被gc，释放内存。因为发生OOM之前都要进行GC，就算其他线程能够正常工作，也会因为频繁GC产生较大的影响；

## 6、JVM的内存布局

## 7、JVM中Object有哪些数据

## 8、JVM运行时数据有哪些

堆、栈、方法区、本地方法栈、本地内存

## 9、什么是STW

进行垃圾回收的过程中，会涉及对象的移动。为了保证对象引用更新的正确性，必须暂停所有的用户线程，像这样的停顿，虚拟机设计者形象描述为Stop The World

什么是jvm的 stop the world  影响jvm stw的因素有哪些，遇到过哪些线上出现stw的问题，怎么排查和解决的

## 10、如何提高throughput（吞吐量）

GC的吞吐量：程序工作时间占比，`-XX:GCTimeRatio=99`，意味着吞吐量占比99%

如何提高throughput
- 给更大的内存，提高GC的工作效率；
- 更高GC的算法，优化算法；
- 多线程能否提高throughput：阿姆达定律

什么应用需要高吞吐量
- 离线任务
- 抢购服务
- 竞技游戏服务
- 音视频服务

## 11、延迟（Latency）

指GC造成的停顿（STW）时间

内存大也能减少延迟

## 12、高吞吐量、低延迟和低FootPrint可以兼得吗

## 13、CMS与G1

CMS的优缺点：
- 优点：并发收集，低停顿
- 缺点：
	- CMS 收集器对CPU资源非常敏感，导致应用吞吐量的降低
	- CMS 收集无法处理浮动垃圾(Floating Garbage)，可能出现 Concurrent Mode Failure 失败而导致一次 Full GC 的产生
	- CMS 基于标记-清除算法实现的，那么垃圾收集结束后会产生大量的空间碎片，空间碎片过多时，将会给大对象的分配带来很大麻烦，往往出现老年代还有很大空间剩余，但是无法找到足够大的连续空间来分配当前对象们，不得不提前触发一次 Full GC

## 14、哪些对象可以作为GC Roots

- 虚拟机栈（栈桢中的本地变量表）中引用的对象：类加载器、Thread等
- 方法区中类静态属性引用的对象；
- 方法区中常量引用的对象；
- 本地方法栈中 JNI（即一般说的 native 方法）引用的对象；
- 活跃线程的引用对象
- Java虚拟机内部的引用，如基本类型对应的class对象，一些常驻的异常对象，还有系统类加载器；
- 所有被同步锁持有的对象；
- 反应被Java虚拟机内部情况的JMXBean，JVMTI中注册的回调、本地代码缓存等；

## 15、JVM内存大小

如何确定堆大小，JVM参数预估参考：
- 每秒有多少次请求；
- 每次请求耗时；
- 每个请求大概需要多大的内存空间
- 每秒发起的请求对内存的占用

在 JDK1.7 中，默认情况下年轻代和老年代的比例是 1:2，我们可以通过–XX:NewRatio 重置该配置项。年轻代中的 Eden 和 To Survivor、From Survivor 的比例是 8:1:1，我们可以通过 `-XX:SurvivorRatio` 重置该配置项；

在 JDK1.7 中如果开启了 `-XX:+UseAdaptiveSizePolicy` 配置项，JVM 将会动态调整 Java 堆中各个区域的大小以及进入老年代的年龄，–XX:NewRatio 和 `-XX:SurvivorRatio` 将会失效，而 JDK1.8 是默认开启 `-XX:+UseAdaptiveSizePolicy` 配置项的

## 16、编写程序

请写一段程序，让其运行时的表现为触发5次YGC，然后3次FGC，然后3次YGC，然后1次FGC，请给出代码以及启动参数
```java
/**
 * VM设置：-Xms41m -Xmx41m -Xmn10m -XX:+UseParallelGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps
 * -Xms41m 				堆最小值
 * -Xmx41m 				堆最大值
 * -Xmn10m 				新生代大小大小(推荐 3/8)
 * -XX:+UseParallelGC   使用并行收集器
 *
 * <p>
 * 初始化时：835k(堆内存)
 * 第一次add：3907k
 * 第二次add：6979k
 * 第三次add: eden + survivor1 = 9216k < 6979k + 3072k,区空间不够，开始 YGC
 * YGC  6979k -> 416k(9216k) 表示年轻代 GC前为6979，GC后426k.年轻代总大小9216k
 */
public class ControlYgcAndFgc {
    private static final int _1_MB = 1024 * 1024;
    public static void main(String[] args) {
        List caches = new ArrayList();
        System.out.println("--初始化时已用堆值:" + ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / 1024 + "k");
        for (int i = 1; i <= 12; i++) {
            caches.add(new byte[3 * _1_MB]);
        }
        // 释放空间，重新添加 ,如果不释放空间，会报错：java.lang.OutOfMemoryError: Java heap space 【这里这样做，主要为了防止数组对象实际大小超过堆大小】
        caches.remove(0);
        caches.add(new byte[3 * _1_MB]);
        // 这里是为了下次FGC后，直接减少老年代的内存大小，从而正常YGC
        for (int i = 0; i < 8; i++) {
            caches.remove(0);
        }
        caches.add(new byte[3 * _1_MB]);
        for (int i = 0; i < 6; i++) {
            caches.add(new byte[3 * _1_MB]);
        }
    }
}
```
运行，控制台打印请如下：
```java
--初始化时已用堆值:1319k
0.175: [GC (Allocation Failure) [PSYoungGen: 7463K->586K(9216K)] 7463K->6738K(41984K), 0.0046075 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
0.180: [GC (Allocation Failure) [PSYoungGen: 6890K->634K(9216K)] 13042K->12938K(41984K), 0.0030904 secs] [Times: user=0.02 sys=0.01, real=0.00 secs] 
0.184: [GC (Allocation Failure) [PSYoungGen: 7075K->570K(9216K)] 19379K->19018K(41984K), 0.0027370 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
0.187: [GC (Allocation Failure) [PSYoungGen: 6855K->618K(9216K)] 25303K->25210K(41984K), 0.0035804 secs] [Times: user=0.02 sys=0.00, real=0.00 secs] 
0.191: [GC (Allocation Failure) [PSYoungGen: 6910K->554K(9216K)] 31502K->31290K(41984K), 0.0029389 secs] [Times: user=0.01 sys=0.01, real=0.00 secs] 
0.194: [Full GC (Ergonomics) [PSYoungGen: 554K->0K(9216K)] [ParOldGen: 30736K->31173K(32768K)] 31290K->31173K(41984K), [Metaspace: 2772K->2772K(1056768K)], 0.0079522 secs] [Times: user=0.05 sys=0.00, real=0.01 secs] 
0.203: [Full GC (Ergonomics) [PSYoungGen: 6296K->3072K(9216K)] [ParOldGen: 31173K->31173K(32768K)] 37469K->34245K(41984K), [Metaspace: 2774K->2774K(1056768K)], 0.0064756 secs] [Times: user=0.03 sys=0.00, real=0.01 secs] 
0.210: [Full GC (Ergonomics) [PSYoungGen: 6144K->0K(9216K)] [ParOldGen: 31173K->12741K(32768K)] 37317K->12741K(41984K), [Metaspace: 2774K->2774K(1056768K)], 0.0043703 secs] [Times: user=0.02 sys=0.00, real=0.00 secs] 
0.215: [GC (Allocation Failure) [PSYoungGen: 6298K->0K(9216K)] 19039K->18885K(41984K), 0.0011114 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
0.217: [GC (Allocation Failure) [PSYoungGen: 6272K->0K(9216K)] 25157K->25029K(41984K), 0.0010150 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.219: [GC (Allocation Failure) [PSYoungGen: 6283K->0K(9216K)] 31313K->31173K(41984K), 0.0008821 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
0.219: [Full GC (Ergonomics) [PSYoungGen: 0K->0K(9216K)] [ParOldGen: 31173K->31173K(32768K)] 31173K->31173K(41984K), [Metaspace: 2774K->2774K(1056768K)], 0.0024537 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
Heap
 PSYoungGen      total 9216K, used 3236K [0x00000007bf600000, 0x00000007c0000000, 0x00000007c0000000)
  eden space 8192K, 39% used [0x00000007bf600000,0x00000007bf9290e0,0x00000007bfe00000)
  from space 1024K, 0% used [0x00000007bff00000,0x00000007bff00000,0x00000007c0000000)
  to   space 1024K, 0% used [0x00000007bfe00000,0x00000007bfe00000,0x00000007bff00000)
 ParOldGen       total 32768K, used 31173K [0x00000007bd600000, 0x00000007bf600000, 0x00000007bf600000)
  object space 32768K, 95% used [0x00000007bd600000,0x00000007bf471520,0x00000007bf600000)
 Metaspace       used 2781K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 297K, capacity 386K, committed 512K, reserved 1048576K
```

## 17、JVM为什么称为 machine

它封装了一组自定义的字节码指令集，有自己的程序计数器和执行引擎，像 CPU 一样，可以执行运算指令。它还像操作系统一样有自己的程序装载与运行机制，内存管理机制，线程及栈管理机制，看起来就像是一台完整的计算机，这就是 JVM 被称作 machine（机器）的原因

## 18、Java程序执行过程 

通过 Java 命令启动 JVM，JVM 的类加载器根据 Java 命令的参数到指定的路径加载.class 类文件，类文件被加载到内存后，存放在专门的方法区。然后 JVM 创建一个主线程执行这个类文件的 main 方法，main 方法的输入参数和方法内定义的变量被压入 Java 栈。如果在方法内创建了一个对象实例，这个对象实例信息将会被存放到堆里，而对象实例的引用，也就是对象实例在堆中的地址信息则会被记录在栈里。堆中记录的对象实例信息主要是成员变量信息，因为类方法内的可执行代码存放在方法区，而方法内的局部变量存放在线程的栈里。

程序计数寄存器一开始存放的是 main 方法的第一行代码位置，JVM 的执行引擎根据这个位置去方法区的对应位置加载这行代码指令，将其解释为自身所在平台的 CPU 指令后交给 CPU 执行。如果在 main 方法里调用了其他方法，那么在进入其他方法的时候，会在 Java 栈中为这个方法创建一个新的栈帧，当线程在这个方法内执行的时候，方法内的局部变量都存放在这个栈帧里。当这个方法执行完毕退出的时候，就把这个栈帧从 Java 栈中出栈，这样当前栈帧，也就是堆栈的栈顶就又回到了 main 方法的栈帧，使用这个栈帧里的变量，继续执行 main 方法。这样，即使 main 方法和 f 方法都定义相同的变量，JVM 也不会弄错

## 19、Groovy如何避免OOM

当我们需要动态执行一些表达式时，可以使用 Groovy 动态语言实现：new 出一个 GroovyShell 类，然后调用 evaluate 方法动态执行脚本。这种方式的问题是，会重复产生大量的类，增加 Metaspace 区的 GC 负担，有可能会引起 OOM。如何避免这个问题呢？

调用 evaluate 方法动态执行脚本会产生大量的类，要避免可能因此导致的 OOM 问题，我们可以把脚本包装为一个函数，先调用 parse 函数来得到 Script 对象，然后缓存起来，以后直接使用 invokeMethod 方法调用这个函数即可：
```java
public static final ConcurrentHashMap<String, Script> SCRIPT_CACHE = new ConcurrentHashMap<>();
private Object rightGroovy(String script, String method, Object... args) {
    Script scriptObject;
    if (SCRIPT_CACHE.containsKey(script)) {
        //如果脚本已经生成过Script则直接使用
        scriptObject = SCRIPT_CACHE.get(script);
    } else {
        //否则把脚本解析为Script
        scriptObject = shell.parse(script);
        SCRIPT_CACHE.put(script, scriptObject);
    }
    return scriptObject.invokeMethod(method, args);
}
```

## 20、为什么会发生OOM

- 业务正常运行时就需要比较多的内存，而给JVM设置的内存过小：具体表现就是程序跑不起来，或者跑一会就挂了；
- GC回收内存的速度赶不上程序运行消耗内存的速度：出现这种情况一般就是往list、map中填充大量数据，内存紧张时JVM拆东墙补西墙补不过来了。所以查询记得分页啊！不需要的字段，尤其是数据量大的字段，就不要返回了！比如文章的内容；
- 存在内存泄漏情况，久而久之也会造成OOM：哪些情况会造成内存泄漏呢？比如打开文件不释放、创建网络连接不关闭、不再使用的对象未断开引用关系、使用静态变量持有大对象引用；

## 21、生产环境如何快速定位OOM

[Java线上OOM问题排查方法](../Java/问题排查/Java线上问题排查方法.md)

有五个区域会发生OOM：直接内存、元空间、本地方法栈、虚拟机栈、元空间；
- 本地方法栈与虚拟机栈的OOM基本线上不会发生，因为这两个区域的OOM你在开发阶段或在测试阶段就能发现，所以这两个区域的OOM是不会生成dump文件的；
- 需要确定是否被Linux OOM Killer了；
- 看有没有生成dump文件。如果生成了，要么是堆OOM，要么是元空间OOM；如果没生成，直接可以确定是直接内存导致的OOM；
- 如果生成了dump文件，下载dump文件，比如用visualvm分析：
    - 如果发现发生OOM的位置是创建对象，调用构造方法之类的代码，那一定是堆OOM。`<init>`就是构造方法的字节码格式；
    - 如果发现发生OOM的位置是类加载器那些方法，那一定是元空间OOM；

## 22、调优建议

- 调优参数务必加上`-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=`，发生OOM让JVM自动dump出内存，方便后续分析问题解决问题

## 23、如果有一个数据结构需要在多个线程中访问，可以把它放在栈上吗？为什么

不能，栈上的数据会随着当前线程的函数调用栈而回收，多个线程访问须在堆上开辟；

在多线程场景下，每个线程的生命周期是不固定的，无法在编译期知道谁先结束谁后结束，所以你不能把属于某个线程 A 调用栈上的内存共享给线程 B，因为 A 可能先于 B 结束。这时候，只能使用堆内存。这里有个例外，如果结束的顺序是确定的，那么可以共享，比如 scoped thread；2. 而同一个调用栈下，main() 调用 hello()，再调用 world()，编译器很清楚，world() 会先结束，之后是 hello()，最后是 main()。所以在 world() 下用指针引用 hello() 或者 main() 内部的变量没有问题，这个指针必然先于它指向的值结束。这个两个问题的实质是我们要搞明白哪些东西在编译期可以确定它们的关系或者因果，哪些只能在运行期确定

> 栈上存放的数据是静态的，固定大小，固定生命周期；堆上存放的数据是动态的，不固定大小，不固定生命周期

## 24、为啥 Java 程序越执行越快呢？


## 25、volatile问题

1. volatile修饰的user对象，里面有两个属性，int a=1和int b=2.（注意:a，b没有被volatile修饰） 这个user对象是另外一个对象Tasker的成员变量。然后tasker对象已经在程序中运行起来了（一个线程运行，我们叫A线程吧）。紧接着又有另外一个线程（B线程）修改了user对象里的a属性，把1修改成了3；那么请问，A线程能否第一时间感知到a属性发生变化呢，也就是知道他变成了3。

答：线程副本里面保存的是对象,所以是知道的（忘记老师答得还是同学答的了，大家知道的，麻烦在群里解答一下。）

不一定，可以参考：https://rules.sonarsource.com/java/RSPEC-3077?search=voltaile

注意一点：happens-before关系

## 26、堆外内存与JVM内存

我们知道内存最简单可分为堆内堆外两块，一般jvm是控制堆内内存，Linux又分用户态内核态和操作系统调度，那么有个关于访问内存的问题，那为什么操作系统不直接访问Java堆内的内存区域？
严格说Intel cpu提供Ring0-Ring3 四种级别的运行模式，Ring0级别最高，Ring3最低；Linux使用了Ring3级别运行用户态，Ring0作为内核态。Ring3状态不能访问Ring0的地址空间，包括代码和数据；因此用户态是没有权限去操作内核态的资源的，它只能通过系统调用外完成用户态到内核态的切换，然后在完成相关操作后再有内核态切换回用户态
因为如果操作系统（JNI  java native interface）直接访问 堆内内存，java 在这个时候自己做了GC 就会导致出现问题 比如内存数据乱套了这种。。

# 六、设计模式

## 1、动态代理与静态代理区别以及使用场景

## 2、单例对象会被jvm的gc时回收吗

# 七、编程题

## 1、实现一个容器，提供两个方法，add，size 写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束

### 1.1、使用wait和notify实现

```java
private final List<Integer> list = new ArrayList<>();
public void add(int i) {
    list.add(i);
}
public int getSize() {
    return list.size();
}
public static void main(String[] args) {
    TwoThreadDemo d = new TwoThreadDemo();
    Object lock = new Object();

    new Thread(() -> {
        synchronized (lock) {
            System.out.println("t2 启动");
            if (d.getSize() != 5) {
                try {
                    /**会释放锁*/
                    lock.wait();
                    System.out.println("t2 结束");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lock.notify();
        }
    }, "t2").start();

    new Thread(() -> {
        synchronized (lock) {
            System.out.println("t1 启动");
            for (int i = 0; i < 9; i++) {
                d.add(i);
                System.out.println("add" + i);
                if (d.getSize() == 5) {
                    lock.notify();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }, "t1").start();
}
```

### 1.2、使用countDownLatch

```java
private final List<Integer> list = new ArrayList<>();
public void add(int i) {
    list.add(i);
}
public int getSize() {
    return list.size();
}
public static void main(String[] args) {
    TwoThreadDemoCountdown d = new TwoThreadDemoCountdown();
    CountDownLatch countDownLatch = new CountDownLatch(1);

    new Thread(() -> {
        System.out.println("t2启动");
        if (d.getSize() != 5) {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("t2结束");
    }, "t2").start();

    new Thread(() -> {
        System.out.println("t1启动");
        for (int i = 0; i < 10; i++) {
            d.add(i);
            System.out.println("add_" + i);
            if (d.getSize() == 5) {
                countDownLatch.countDown();
            }
        }
        System.out.println("t1结束");
    }, "t1").start();
}
```


# 参考资料

- [Java资深面试题](https://mp.weixin.qq.com/s/kzyfdj0MpCo31D55MY1O5A) 
- [Java面经](https://juejin.im/post/6844904161612398600)
- [抖音面试题](https://juejin.im/post/5ef5284ce51d453483425da9)
- [全栈知识体系总览](https://pdai.tech/md/outline/x-outline.html#%E5%85%A8%E6%A0%88%E7%9F%A5%E8%AF%86%E4%BD%93%E7%B3%BB%E6%80%BB%E8%A7%88)
- [Java面试题汇总](https://mp.weixin.qq.com/s/crv7m7A12-xjnjWeqZh82g)
- [Java面试题](https://zwmst.com/)
- [吴师兄](https://www.cxyxiaowu.com/)
- [必备干货](https://segmentfault.com/a/1190000017115722)
- [技术文章整理](https://segmentfault.com/a/1190000018855112)
- [面试八股文](https://mp.weixin.qq.com/s/2IUaDr5XRgWoVaGgPGsAxA)
- [面试offer](https://mp.weixin.qq.com/s/C5QMjwEb6pzXACqZsyqC4A)
