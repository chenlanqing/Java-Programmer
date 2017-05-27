1.Java 代码执行顺序:
	1.1.Java 类初始化过程:
		(1).首先,初始化父类中的静态成员变量和静态代码块,按照在程序中出现的顺序初始化； 
		(2).然后,初始化子类中的静态成员变量和静态代码块,按照在程序中出现的顺序初始化； 
		(3).其次,初始化父类的普通成员变量和代码块,在执行父类的构造方法；
		(4).最后,初始化子类的普通成员变量和代码块,在执行子类的构造方法；
		Java 中赋值顺序： 
			(1).父类的静态变量赋值 
			(2).自身的静态变量赋值 
			(3).父类成员变量赋值 
			(4).父类块赋值 
			(5).父类构造函数赋值 
			(6).自身成员变量赋值 
			(7).自身块赋值 
			(8).自身构造函数赋值
	1.2.Java 代码执行顺序:
		public class TestExecuteCode {
		    public static void main(String[] args) {
		        System.out.println(new B().getValue());
		    }
		    static class A {
		        protected int value;
		        public A(int v){
		            setValue(v);
		        }
		        public void setValue(int value) { this.value = value;}
		        public int getValue() {
		            try {
		                value++;
		                return value;
		            } finally {
		                this.setValue(value);
		                System.out.println(value);
		            }
		        }
		    }
		    static class B extends A {
		        public B(){
		            super(5);
		            setValue(getValue() - 3);
		        }
		        public void setValue(int value) {super.setValue(2 * value);}
		    }
		}
	==> 执行结果:22,34,17
		(1).子类 B 中重写了父类 A 中的setValue方法:
				super(5) // 调用了父类构造器,其中构造函数里面的setValue(value),调用的是子类的setValue方法
			finally块中的
				this.setValue(value) //调用的也是子类的setValue方法
			而子类setValue方法中的
				super.setValue(2*value); //调用的是父类A的setValue方法
		(2).try...catch...finally块中有return返回值的情况
			finally 块中虽然改变了value的值，但 try 块中返回的应该是 return 之前存储的值.
	==> 父类执行时如果有子类的方法重写了父类的方法,调用的子类的重写方法
2.非运算:
	-n = ~n+1  =>  -n-1 = ~n

3.赋值表达式:一般情况下返回的是右边的值
4.关于形参:
	(1).形式参数就是函数定义时设定的参数.例如函数头 int min(int x,int y,int z) 中 x,y,z 就是形参。
		实际参数是调用函数时所使用的实际的参数,真正被传递的是实参
	(2).对于形式参数只能用 final 修饰符,其它任何修饰符都会引起编译器错误.但是用这个修饰符也有一定的限制，
		就是在方法中不能对参数做任何修改。 不过一般情况下，一个方法的形参不用final修饰。
		只有在特殊情况下，那就是：方法内部类
		// 一个方法内的内部类如果使用了这个方法的参数或者局部变量的话，这个参数或局部变量应该是final
	(3).只有在被调用时才分配内存单元,在调用结束时, 即刻释放所分配的内存单元,因此，形参只有在函数内部有效;
	(4).实参和形参在数量上，类型上，顺序上应严格一致， 否则会发生"类型不匹配"的错误
	(5).函数调用中发生的数据传送是单向的。 即只能把实参的值传送给形参，而不能把形参的值反向地传送给实参。 
		因此在函数调用过程中，形参的值发生改变，而实参中的值不会变化;












































