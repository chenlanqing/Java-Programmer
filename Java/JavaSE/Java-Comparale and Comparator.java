1.Comparable & Comparator 都是用来实现集合中元素的比较、排序的,
	Comparable 是在集合内部定义的方法实现的排序;
	Comparator 是在集合外部实现的排序
所以,如想实现排序,就需要在集合外定义 Comparator 接口的方法或在集合内实现 Comparable 接口的方法

2.Comparator 位于包 java.util下，而 Comparable 位于包 java.lang下
3.Comparable 是一个对象本身就已经支持自比较所需要实现的接口(如 String、Integer 自己就可以完成比较大小操作，
	已经实现了Comparable接口);
4.而 Comparator 是一个专用的比较器，当这个对象不支持自比较或者自比较函数不能满足你的要求时，
	你可以写一个比较器来完成两个对象之间大小的比较
5.可以说一个是自已完成比较,一个是外部程序实现比较的差别而已
6.用 Comparator 是策略模式(strategy design pattern)，就是不改变对象自身，
Comparable 而用一个策略对象(strategy object)来改变它的行为
7.有时在实现 Comparator 接口时，并没有实现equals方法，可程序并没有报错，原因是实现该接口的类也是Object类的子类，
而Object类已经实现了equals方法
*************************************************************************
1.Comparable:
	一个实现了 Comparable 接口的类,可以让其自身的对象和其他对象进行比较。
	也就是说:同一个类的两个对象之间要想比较，对应的类就要实现 Comparable 接口，并实现compareTo()方法
2.Comparator:在一些情况下，你不希望修改一个原有的类，但是你还想让他可以比较，Comparator接口可以实现这样的功能。
	(1).通过使用Comparator接口，你可以针对其中特定的属性/字段来进行比较。比如，当我们要比较两个人的时候，
	我可能通过年龄比较、也可能通过身高比较。这种情况使用Comparable就无法实现（因为要实现Comparable接口，
	其中的compareTo方法只能有一个，无法实现多种比较）
	(2).通过实现Comparator接口同样要重写一个方法：compare()。接下来的例子就通过这种方式来比较HDTV的大小。
	其实Comparator通常用于排序。Java中的Collections和Arrays中都包含排序的sort方法，该方法可以接收一个Comparator
	的实例（比较器）来进行排序:
			class HDTV {
			    private int size;
			    private String brand;
			    public HDTV(int size, String brand) {
			        this.size = size;
			        this.brand = brand;
			    }
			    public int getSize() {
			        return size;
			    }
			    public void setSize(int size) {
			        this.size = size;
			    }
			    public String getBrand() {
			        return brand;
			    }
			    public void setBrand(String brand) {
			        this.brand = brand;
			    }
			}
			class SizeComparator implements Comparator<HDTV> {
			    @Override
			    public int compare(HDTV tv1, HDTV tv2) {
			        int tv1Size = tv1.getSize();
			        int tv2Size = tv2.getSize();

			        if (tv1Size > tv2Size) {
			            return 1;
			        } else if (tv1Size < tv2Size) {
			            return -1;
			        } else {
			            return 0;
			        }
			    }
			}
			public class Main {
			    public static void main(String[] args) {
			        HDTV tv1 = new HDTV(55, "Samsung");
			        HDTV tv2 = new HDTV(60, "Sony");
			        HDTV tv3 = new HDTV(42, "Panasonic");
			        ArrayList<HDTV> al = new ArrayList<HDTV>();
			        al.add(tv1);
			        al.add(tv2);
			        al.add(tv3);
			        Collections.sort(al, new SizeComparator());
			        for (HDTV a : al) {
			            System.out.println(a.getBrand());
			        }
			    }
			}
	(3).经常会使用 Collections.reverseOrder()来获取一个倒序的 Comparator:
		ArrayList<Integer> al = new ArrayList<Integer>();
		al.add(3);
		al.add(1);
		al.add(2);
		System.out.println(al);
		Collections.sort(al);
		System.out.println(al);
		Comparator<Integer> comparator = Collections.reverseOrder();
		Collections.sort(al,comparator);
		System.out.println(al);
3.如何选择:
	(1).一个类如果实现 Comparable 接口，那么他就具有了可比较性，意思就是说它的实例之间相互直接可以进行比较
	(2).通常在两种情况下会定义一个实现 Comparator 类
		可以把一个Comparator的子类传递给Collections.sort()、Arrays.sort()等方法，用于自定义排序规则。
		用于初始化特定的数据结构。常见的有可排序的Set（TreeSet）和可排序的Map（TreeMap）













































