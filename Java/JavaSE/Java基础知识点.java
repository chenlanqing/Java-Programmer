一.Java 泛型:
1.JDK1.5 引入的新特性,允许在定义类和接口的时候使用类型参数(type parameter),泛型最主要的应用是在JDK 5中的新集合类框架中;
2.类型擦除(type erasure):使用泛型的时候加上的类型参数,会被编译器在编译的时候去掉,这个过程就称为类型擦除.
	2.1.Java 中的泛型基本上都是在编译器这个层次来实现的,在生成的 Java 字节代码中是不包含泛型中的类型信息的;		
	2.2.泛型的类型参数不能用在Java异常处理的catch语句中.因为异常处理是由JVM在运行时刻来进行的,
		而由泛型附加的类型信息对JVM来说是不可见的;
	2.2.类型擦除的基本过程:
		(1).首先是找到用来替换类型参数的具体类,这个具体类一般是 Object,如果指定了类型参数的上界的话,则使用这个上界.
			把代码中的类型参数都替换成具体的类,同时去掉出现的类型声明,即去掉<>的内容
		(2).可能需要生成一些桥接方法（bridge method）
	2.3.编译器承担了全部的类型检查工作,编译器禁止某些泛型的使用方式,正是为了确保类型的安全性:
		public void inspect(List<Object> list) {    
		    for (Object obj : list) {        
		        System.out.println(obj);    
		    }    
		    list.add(1); 
		//这个操作在当前方法的上下文是合法的。 
		}
		public void test() {    
		    List<String> strs = new ArrayList<String>();    
		    inspect(strs); 
		//编译错误 
		}
		/**
		 * 假设这样的做法是允许的，那么在inspect方法就可以通过list.add(1)来向集合中添加一个数字。这样在test方法看来，
		 * 其声明为List<String>的集合中却被添加了一个Integer类型的对象。这显然是违反类型安全的原则的，
		 * 在某个时候肯定会抛出ClassCastException
		 */
3.通配符与上下界:
	3.1.在使用泛型类的时候,既可以指定一个具体的类型,也可以用通配符?来表示未知类型,如 List<?> 
 	3.2.通配符所代表的其实是一组类型，但具体的类型是未知的,但是 List<?>并不等同于 List<Object>
 		List<Object> 实际上确定了 List 中包含的是 Object 及其子类，在使用的时候都可以通过 Object 来进行引用。
 		而 List<?>则其中所包含的元素类型是不确定;
 	3.3.对于 List<?>中的元素只能用 Object 来引用，在有些情况下不是很方便.在这些情况下，可以使用上下界来限制未知类型的范围
 		如:List<? extends Number>说明 List 中可能包含的元素类型是 Number 及其子类
 		而:List<? super Number> 则说明 List 中包含的是 Number 及其父类
 		当引入了上界之后，在使用类型的时候就可以使用上界类中定义的方法
 	3.4.关于 <? extends T> 和 <? super T>
 		3.4.1.<? extends T>:表示参数化的类型可能是所指定的类型，或者是此类型的子类
	 		public class DemoGenerice {
	 			public static void main(String[] args) {
	 				List<? extends Season> list = new LinkedList<Season>();
	 				list.add(new Spring()); //  编译错误
	 			}
	 			
	 		}
	 		class Season{}
	 		class Spring extends Season{}
	 		(1).编译错误原因:
	 			List<? extends Season> 表示 “具有任何从 Season 继承类型的列表”，编译器无法确定 List 所持有的类型，
	 			所以无法安全的向其中添加对象。可以添加 null,因为 null 可以表示任何类型。
	 			所以 List 的add 方法不能添加任何有意义的元素;
	 	3.4.2.<? super T>:表示参数化的类型可能是所指定的类型，或者是此类型的父类型，直至Object

	 	3.4.3.PECS原则:
	 		(1).如果要从集合中读取类型T的数据,并且不能写入,可以使用 ? extends 通配符；(Producer Extends)
	 		(2).如果要从集合中写入类型T的数据,并且不需要读取,可以使用 ? super 通配符；(Consumer Super)
	 		(3).如果既要存又要取，那么就不要使用任何通配符
4.Java 类型系统:
	4.1.在 Java 中,通过继承机制而产生的类型体系结构是大家熟悉的.
		根据Liskov替换原则，子类是可以替换父类的,但是反过来的话，即用父类的引用替换子类引用的时候，就需要进行强制类型转换
	4.2.引入泛型后,类型系统增加了两个维度:
		(1).一个是类型参数自身的继承体系结构.List<String>和List<Object>这样的情况,类型参数String是继承自Object的
		(2).一个是泛型类或接口自身的继承体系结构.第二种指的是 List 接口继承自 Collection 接口
		对于这个类型系统,有如下规则:
		==> 相同类型参数的泛型类的关系取决于泛型类自身的继承体系结构,即 List<String>是 Collection<String> 的子类型,
			List<String>可以替换 Collection<String>
		==> 当泛型类的类型声明中使用了通配符的时候,其子类型可以在两个维度上分别展开:
			对于 Collection<? extends Number>来说,
			①.其子类型可以在 Collection 这个维度上展开 List<? extends Number>和 Set<? extends Number>等
			②.也可以在 Number 这个层次上展开，即 Collection<Double>和 Collection<Integer>等
				ArrayList<Long>和 HashSet<Double>等也都算是 Collection<? extends Number>的子类型
5.开发自己的泛型类:
	(1).泛型类与一般的Java类基本相同，只是在类和接口定义上多出来了用<>声明的类型参数
	(2).所声明的类型参数在Java类中可以像一般的类型一样作为方法的参数和返回值，或是作为域和局部变量的类型
	(3).由于类型擦除机制，类型参数并不能用来创建对象或是作为静态变量的类型
		class ClassTest<X extends Number, Y, Z> {    
		    private X x;    
		    private static Y y; 
		//编译错误，不能用在静态变量中    
		    public X getFirst() {		        
		//正确用法        
		        return x;    
		    }    
		    public void wrong() {        
		        Z z = new Z(); 
		//编译错误，不能创建对象    
		    }
		}
6.在使用泛型的时候可以遵循一些基本的原则:
	(1).在代码中避免泛型类和原始类型的混用;
	(2).在使用带通配符的泛型类的时候，需要明确通配符所代表的一组类型的概念
















































































































