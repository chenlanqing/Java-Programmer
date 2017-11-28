一.最大公约数:
1.辗转相除法-欧几里德算法,求出两个正整数的最大公约数
	1.1.欧几里德算法定理:两个正整数a和b(a>b)它们的最大公约数等于a除以b的余数c和b之间的最大公约数;
	1.2.思路:基于上述定理,可以使用递归的方法来把问题逐步简化:
		首先,我们先计算出a除以b的余数c,把问题转化成求出b和c的最大公约数;
		然后计算出b除以c的余数d,把问题转化成求出c和d的最大公约数;
		再然后计算出c除以d的余数e,把问题转化成求出d和e的最大公约数;
		......
		逐渐把两个较大整数之间的运算简化成两个较小整数之间的运算,直到两个数可以整除,或者其中一个数减小到1为止
	1.3.代码一:
		public static int getGreatestCommonDivisor(int a, int b){
			int result = 1;
			if (a > b) {
				result = gcd(a, b);
			} else {
				result = gcd(b, a);
			}
			return result;
		}
		public static int gcd(int a, int b){
			if (a % b == 0) {
				return b;
			} else {
				return gcd(b, a%b);
			}
		}
	1.4.上述代码问题点:当两个整数比较大时,做 a%b 取模运算性能比较低.
2.更相减损术(九章算术),也是求最大公约数的方法:
	2.1.定理:两个正整数a和b(a>b),它们的最大公约数等于a-b的差值c和较小数b的最大公约数
	2.2.思路:我们同样可以通过递归来简化问题.
		首先,我们先计算出a和b的差值c（假设a>b）,把问题转化成求出b和c的最大公约数;
		然后计算出c和b的差值d（假设c>b）,把问题转化成求出b和d的最大公约数;
		再然后计算出b和d的差值e（假设b>d）,把问题转化成求出d和e的最大公约数;
		......
		逐渐把两个较大整数之间的运算简化成两个较小整数之间的运算
	2.3.代码二:
		public static int getGreatestCommonDivisor(int a, int b){
			if (a == b) {
				return b;
			} else if (a > b) {
				getGreatestCommonDivisor(a - b, b);
			} else {
				getGreatestCommonDivisor(b - a, a);
			}
		}
	2.4.相对于欧几里得算法,更相减损术依靠求两数的差方式来递归,运算的次数肯定远大于欧几里德算法.
	2.5.优化思路:在更相减损术基础上使用移位算法:
		对于给定的正整数a和b，不难得到如下的结论。其中gcb(a,b)的意思是a,b的最大公约数函数：
		当a和b均为偶数，gcb(a,b) = 2*gcb(a/2, b/2) = 2*gcb(a>>1, b>>1)
		当a为偶数，b为奇数，gcb(a,b) = gcb(a/2, b) = gcb(a>>1, b) 
		当a为奇数，b为偶数，gcb(a,b) = gcb(a, b/2) = gcb(a, b>>1) 
		当a和b均为奇数，利用更相减损术运算一次，gcb(a,b) = gcb(b, a-b), 此时a-b必然是偶数,又可以继续进行移位运算.
		==> 比如:计算10和25的最大公约数的步骤如下
			整数10通过移位,可以转换成求5和25的最大公约数;
			利用更相减损法,计算出25-5=20,转换成求5和20的最大公约数;
			整数20通过移位,可以转换成求5和10的最大公约数,
			整数10通过移位,可以转换成求5和5的最大公约数
			利用更相减损法,因为两数相等，所以最大公约数是5
	2.6.代码三:
		public static int getData(int a, int b) {
			if (a == b) {
				return a;
			} else if (a < b) {
				return getData(b, a);
			} else {
				if ((a & 1) == 0 && (b & 1) == 0) {
					return getData(a >> 1, b >> 1) << 1;
				} else if ((a & 1) == 0 && (b & 1) != 0) {
					return getData(a >> 1, b);
				} else if ((a & 1) != 0 && (b & 1) == 0) {
					return getData(a, b >> 1);
				} else {
					return getData(b, a - b);
				}
			}
		}
二.最小公倍数:
1.思路:两个正整数a和b的乘积除以两个数的最大公约数-->最小公倍数=两整数的乘积 / 最大公约数














