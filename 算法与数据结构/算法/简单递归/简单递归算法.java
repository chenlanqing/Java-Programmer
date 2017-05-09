1.猴子吃桃问题:
	1.1.问题描述:
		孙悟空第一天摘下若干蟠桃，当即吃了一半，还不过瘾，又多吃了一个。第二天早上，他又将剩下的蟠桃吃掉一半，还不过瘾，
		又多吃了一个。之后每天早上他都吃掉前一天剩下桃子的一半零一个。到第10天早上想再吃时，就只剩下一个蟠桃了。
		求孙悟空第一天共摘了多少个蟠桃？
	1.2.递推:
		10 = (5+1)+4
		前一天剩下的蟠桃 = (前一天剩下的一半+1个) + 当天剩下的蟠桃
	1.3.递归:
		开始调用哪个函数，该函数就压栈；
		调用完毕，该函数就弹栈。
		a.n = 2 * a.(n-1) + 2
		// 实现
		return n==1 ? 1 : eat(n - 1) * 2 + 2;

		Space(N) = Heap(N)+Stack(N)
		Heap(N) =0
		Stack(N) =N
		故而，Space(N) = 0+N = O(N)
		当Stack(N)增长率很快（超过NlogN）的时候，慎用递归！

2.最大公约数与最小公倍数:
	2.1.最大公约数:
		(1).算法:欧几里得算法
			①.令较大数为m，较小数为n；
			②.当m除以n的余数不等于0时，把n作为m，并把余数作为n，进行下一次循环；
			③.当余数等于0时，返回n
		(2).递推实现:时间复杂度 O(lgM),空间复杂度 O(1)
			public int gcd(int m, int n){					
				int a = Math.max(m, n);
				int b = Math.min(m, n);
				m = a;
				n = b;
				int r = m % n;
				while(r != 0){
					m = n;
					n = r;
					r = m % n;
				}
				return n;
			}
			m 	n 	r
			100 44  12
			44  12 	8
			12	8	4
			8	4	0
			每执行一次循环，m或者n至少有一个缩小了2倍，故时间复杂度上限为 log2M。
			对于大量的随机测试样例，每次循环平均能使m与n的值缩小一个10进位，所以平均复杂度为 O(lgM)(以10为底的对数)
		(3).递归实现:
			public int gcd02(int m, int n){
				int a = Math.max(m,n),
					b = Math.min(m,n);
				return a % b == 0 ? b : gcd02(b, a % b);
				或者一行:
				return m >=n ? (m % n == 0 ? n : gcd02(n, m%n)):(n % m == 0 ? m : gcd02(m, n%m));
			}
	2.2.最小公倍数:两个数的乘积除以最大公约数
		60 和 24 的最小公倍数:
			60 * 24 / gcd02(60, 24);
3. 1到100累加的“非主流算法”:求 1 + 2 + 3 + ... + n
	3.1.普通算法:
		(1).for 循环运算:时间复杂度 O(N), 空间复杂度 O(1)
			int sum = 0;
			for(int i = 1;i <= 100; i++){
				sum += i;
			}	
		(2).递归:时间复杂度 O(N), 空间复杂度 O(N)[栈深度为 N,堆深度为 1];
			public int sum(int n){
				return n == 1 ? 1 : sum(n - 1) + n;
			}
	3.2.等差数列:时间复杂度 O(1), 空间复杂度 O(1)[没有进行循环也没有开辟额外的空间]
		n * (a + b) / 2 // a 表示数列的第1项, b 表示数列的最后一项
		public int sum(int n){
			return n * (1 + n) / 2
		}
	3.3.抛出异常算法:捕获异常 ArrayIndexOutOfBoundsException  时间复杂度 O(N), 空间复杂度 O(N)
		(1).条件:不允许使用循环语句,不允许使用选择语句,	不允许使用乘法、除法
		(2).设计递归算法，使用数组存储数据；当发生数组越界异常时，捕获异常并结束递归
			数组第20位置的元素array[20]存储前20项的和：1+2+3+…+20
			public class SumException {
				private int n;
				private int[] array;			
				public SumException() {	}
				public SumException(int n){
					this.n = n;
					this.array = new int[n+1];// 数组的长度为 n+1
				}			
				public int sum(int i){
					try {
						array[i] = array[i-1] + i;
						int k = sum(i + 1);
						return k;
					} catch (ArrayIndexOutOfBoundsException e) {
						return array[n];
					}
				}			
			}
	3.4.构造函数抛出异常:时间复杂度 O(N), 空间复杂度 O(N)
		Heap(N) = 2N，Stack(N) = N
		Space(N) = Heap(N)+Stack(N) = 3N = O(N)
		public class SumExceptionConstructor {
			public static int n;
			public static int[] array;			
			public SumExceptionConstructor(int i){
				try {
					array[i] = array[i-1] + i;
					new SumExceptionConstructor(i+1);
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println(array[n]);
					return;
				}
			}			
			public static void main(String[] args) {
				int n = 100;
				SumExceptionConstructor.n = n;
				SumExceptionConstructor.array = new int[n+1];
				new SumExceptionConstructor(1);
			}			
		}

4.爬楼梯问题:即斐波那契数列问题
	4.1.描述:楼梯一共有n级，每次你只能爬1级或者2级。问：从底部爬到顶部一共有多少种不同的路径?
	4.2.递推公式:
		f1 = 1, f2 = 2, f(n) = f(n-1) + f(n-2);
		由此斐波那契数列:
		a1 = 1, a2 = 2, a3 = 3, a4 = 5, a5 = 8,...
		(1).递归实现:时间复杂度 O(2^N), 空间复杂度 O(N);
			public int fib01(int n){
				if(n == 1 || n == 2){
					return n;
				}else {
					return climb01(n-1) + climb01(n-2);
				}
			}
			public int fib02(int n){
				return n==1||n==2? n : climb01(n-1) + climb01(n-2);
			}
		(2).递归数:
			其弹栈为二叉树的后序遍历序列, 2 1 3 2 4 2 1 3 5
			树的高度 = 栈的最大深度
	4.3.备忘录法:将重复计算的值存入数组, 时间复杂度 O(N), 空间复杂度 O(N);
		如果array[i]不为0，则直接返回；
		如果array[i]为0，array[i]=f(i-1)+f(i-2)，并返回array[i]
		==> 实现:
			public int dfs(int n, int[] array){
				if(array[n] != 0){
					return array[n];
				}else{
					array[n] = dfs(n-1, array) + dfs(n-2, array);
					return array[n];
				}
			}
			public int fib03(int n){
				if(n == 1 || n == 2){
					return n;
				}else{
					int[] array = new int[n+1];
					array[1] = 1;
					array[2] = 2;
					return dfs(n, array);
				}
			}	

	4.4.动态规划法（Dynamic programming）,简称DP:时间复杂度 O(N), 空间复杂度 O(N);
		满足条件:最优子结构[fib(n-1)+fib(n-2)=fib(n)],重叠子问题[由递归树可知]
			public int fib04(int n) {
				if(n == 1 || n == 2){
					return n;
				}else{
					int[] array = new int[n+1];
					array[1] = 1;
					array[2] = 2;
					for(int i=3;i<n+1;i++){
						array[i] = array[i-1] + array[i-2];
					}
					return array[n];
				}
			}
	4.5.状态压缩法,又称滚动数组、滑动窗口(Sliding Window)，用于优化动态规划法的空间复杂度
		时间复杂度 O(N), 空间复杂度 O(1);
			public int fib05(int n){
				if(n == 1 || n == 2){
					return n;
				}else{
					int a = 1;
					int b = 2;
					int t;
					for(int i=3;i<n+1;i++){
						t = a + b;
						a = b;
						b = t;
					}
					return b;
				}
			}
	4.6.通项公式:[斐波那契数列通项公式.emf],时间复杂度 O(log2N), 空间复杂度 O(1);
		public int fib06(int n){
			if(n == 1 || n == 2){
				return n;
			}else{
				double sqrtFive = Math.sqrt(5);
				n++;
				double a = Math.pow((1 + sqrtFive)/2, n);
				double b = Math.pow((1 - sqrtFive)/2, n);
				double result = 1 / sqrtFive * (a - b);
				return (int) Math.floor(result);
			}
		}
	4.7.比较:上述写的六个方法:
		(1).方法对比
			fib01	递归，未做优化
			fib02	递归，一行代码
			fib03	递归，备忘录法
			fib04	非递归，动态规划法
			fib05	非递归，状态压缩法
			fib06	非递归，通项公式法
		(2).面试要求:
			面试官要求			可采取的方法
			代码简洁			fib02
			递归				fib01、fib02、fib03
			非递归				fib04、fib05、fib06
			方便查询			fib03、fib04
			时间复杂度尽量低	fib04、fib05、fib06
			空间复杂度尽量低	fib05、fib06
































