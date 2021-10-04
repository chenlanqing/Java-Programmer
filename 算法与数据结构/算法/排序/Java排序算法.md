# 1、冒泡排序

## 1.1、基本思想

在要排序的一组数中，对当前还未排好序的范围内的全部数，自上而下对相邻的两个数依次进行比较和调整，让较大的数往下沉，较小的往上冒。即：每当两相邻的数比较后发现它们的排序与排序要求相反时，就将它们互换；

时间复杂度 O(n^2)

## 1.2、实现

```java
public void bubble(int[] A) {
	for (int i = A.length - 1; i >= 0; i--) {
		// 找到0-i间的最大元素放到A[i]
		bubble(A, 0, i + 1);
	}
}
private void bubble(int[] A, int i, int j) {
	for (int k = 0; k < j - 1; k++) {
		if (A[k] > A[k + 1]) {
			ArrayUtils.exchange(A, k, k + 1);
		}
	}
}
// 优化
public void sort(int[] arr， int n) {
	for (int i = 0; i < n; i++) {
		for (int j = 0; j < n - i - 1; j++) { // 每排序一趟，则必然后面有一个已经有序，可以缩小排序的范围
			if (arr[j] > arr[j + 1]) {
				ArrayUtils.exchange(arr， j， j + 1);
			}
		}
	}
}
```

## 1.3、优化方案

加一个标记来判断每一趟排序时是否交换过数据，如果哪一趟排序没有交换数据，则这时就已经有序了

```java
public void sort(int[] arr， int n) {
	boolean flag;
	for (int i = 0; i < n; i++) {
		flag = true;
		for (int j = 0; j < n - i - 1; j++) {
			if (arr[j] > arr[j + 1]) {
				ArrayUtils.exchange(arr, j, j + 1);// 交换数据
				flag = false;
			}
		}
		if (flag){
			break;
		}
	}
}
```

# 2、选择排序：O(n^2)

## 2.1、基本思想

在要排序的一组数中，选出最小的一个数与第一个位置的数交换；然后在剩下的数当中再找最小的与第二个位置的数交换，如此循环到倒数第二个数和最后一个数比较为止。选择排序算法的时间复杂度为O(n^2)，空间复杂度为 O(1)

## 2.2、实现

```java
public void sort(int[] arr， int n) {
	if (arr == null || arr.length != n) {
		return;
	}
	// 外层循环从0开始，区间为[i, len)
	for (int i = 0; i < n; i++) {
		// 以外层循环索引值认为其是最小值
		int min = i;
		// 内层循环从外层循环索引 + 1作为起始位置，从[i+1, len)开始寻找最小位置的索引
		for (int j = i + 1; j < n; j++) {
			if (arr[j] < arr[min]) {
				min = j;
			}
		}
		ArrayUtils.exchange(arr, i, min);
	}
}
```
实现2：
```java
public void sort(int[] A) {
    for(int i = A.length - 1; i >= 0; i--) {
        // 0 - A[i]
        int j = maxIndex(A, 0, i+1);
        ArrayUtils.exchange(A, i, j);
    }
}
static private int maxIndex(int[] A, int i, int j) {
    int max = Integer.MIN_VALUE;
    int maxIndex = j-1;
    for(int k = j-1; k >= i; k--) {
        if(max < A[k]) {
            max = A[k];
            maxIndex = k;
        }
    }
    return maxIndex;
}
```

# 3、插入排序：O(n^2)

## 3.1、基本思想

在要排序的一组数中，假设前面(n-1)[n>=2] 个数已经是排好顺序的，现在要把第n个数插到前面的有序数中，使得这n个数也是排好顺序的。如此反复循环，直到全部排好顺序

## 3.2、基本实现

```java
public static int[] insertionSort(int[] A, int n) {
	if(A == null || A.length < 2){
		return A;
	}				
	int index = 0,
		len = A.length;				
	for(int i = 0; i < len; i++){
		index = i;
		while(index > 0){
			if(A[index-1] > A[index]){
				int temp = A[index - 1];
				A[index - 1] = A[index];
				A[index] = temp;
				index --;
			}else{
				break;
			}
		}
	}
	return A;
}
```

## 3.3、插入排序

```java
public void sort(int[] arr， int n) {
	// 从第二个元素开始比较，默认认为第一个元素是有序，
	for (int i = 1; i < n; i++) {
		// 查找到 arr[i]元素的合适位置
		for (int j = i; j > 0 && arr[j] < arr[j - 1]; j--) {
			arr[j] ^= arr[j-1];
			arr[j-1] ^= arr[j];
			arr[j] ^= arr[j-1];
		}
	}
}
```

## 3.4、插入排序优化

```java
public void sort(int[] arr， int n) {
	for (int i = 1; i < n; i++) {
		int j;
		int temp = arr[i];
		for (j = i; j > 0 && arr[j - 1] > temp; j--) {
			arr[j] = arr[j - 1];
		}
		arr[j] = temp;
	}
}
```

## 3.5、插入排序性能

- 对于无序的数组，其性能是O(N^2)；
- 对于有序的数组，其性能是O(N)

# 4、归并排序-O(n * log n)

## 4.1、基本思路

归并排序使用了一种叫做"分治"的思想来解决排序问题：也就是把一个大问题分解为众多子问题，而后分别得到每个子问题的解，最终以某种方式合并这些子问题的解就可以得到原问题的解；将待排序数组递归的分解成两半，分别对它们进行排序，然后将结果"归并"(递归的合并)起来归并排序的时间复杂度为 $O(N\log\N)$， 它的主要缺点是所需的额外空间与待排序数组的尺寸成正比

## 4.2、实现

```java
public void sort(int[] A) {
	mergeSort(A, 0, A.length);
}
private static void mergeSort(int[] A, int l, int r) {
	// 边界条件
	if (r - l <= 1) {
		return;
	}
	int mid = (l + r) / 2;
	/*
	* 数组元素：55 54 87 73 29 47 50 89 20 78，长度：10
	* mid = (5)
	*/
	mergeSort(A, l, mid); // [0,5)
	mergeSort(A, mid, r); // [5,10)

	merge(A, l, mid, r);
}
// 其实最终是合并两个有序的数组
private static void merge(int[] A, int l, int mid, int r) {
	int[] B = copy(A, l, mid + 1);
	int[] C = copy(A, mid, r + 1);
	// 超过一个元素，如果越界会补0；
//        int[] B = Arrays.copyOfRange(A, l, mid + 1);
//        int[] C = Arrays.copyOfRange(A, mid, r + 1);

	// 临时数组最后一个元素加入哨兵
	B[B.length - 1] = C[C.length - 1] = Integer.MAX_VALUE;
	int i = 0, j = 0;
	for (int k = l; k < r; k++) {
		if (B[i] > C[j]) {
			A[k] = C[j++];
		} else {
			A[k] = B[i++];
		}
	}
}
private static int[] copy(int[] A, int l, int r) {
	int[] res = new int[r - l];
	int j = 0;
	for (int i = l; i < r - 1; i++) {
		res[j++] = A[i];
	}
	return res;
}
```

```java
public class MergeSort {
	public static void main(String[] args) {
		int[] A = new int[]{1，5，9，0，3，10，8};
		System.out.println(Arrays.toString(A));
		int[] sorted = mergeSort(A， A.length);
		System.out.println(Arrays.toString(sorted));
	}			
	public static int[] mergeSort(int[] A， int n){
		if(A == null || A.length < 2){
			return A;
		}
		mergeSort(A， 0， A.length - 1);
		return A;
	}
	private static void mergeSort(int[] a， int left， int right) {
		if(left == right){
			return;
		}
		int mid = (left + right) / 2;
		mergeSort(a， left， mid);
		mergeSort(a， mid+1， right);
		mergeSort(a， left， mid， right);
	}
	private static void mergeSort(int[] a， int left， int mid， int right) {
		int[] help = new int[right - left + 1];
		int l = left;
		int r = mid + 1;
		int index = 0;
		while(l <= mid && r <= right){
			if(a[l] <= a[r]){
				help[index++] = a[l++];
			}else{
				help[index++] = a[r++];
			}
		}
		while(l <= mid){
			help[index++] = a[l++];
		}
		while(r <= right){
			help[index++] = a[r++];
		}
		for(int i=0;i<help.length;i++){
			a[left+i] = help[i];
		}
	}
}
```

## 4.3、归并排序的优化

```java
public void sort(int[] arr， int n) {
	mergeSort(arr， 0， n - 1);
}
private void mergeSort(int[] arr， int left， int right) {
	// 如果数组的元素个数小于某个值时，那么其尽可能在有序的范围内的话，可以使用插入排序来实现
	if (right - left <= 15) {
		sort(arr， left， right);
		return;
	}
	int mid = (right + left) / 2;
	mergeSort(arr， left， mid);
	mergeSort(arr， mid + 1， right);
	// 优化思路：如果mid的值小于mid+1的值说明已经有序了
	if (arr[mid] > arr[mid + 1]) {
		mergeSort(arr， left， mid， right);
	}
}
private void mergeSort(int[] arr， int left， int mid， int right) {
	int[] help = new int[right - left + 1];

	for (int i = left; i <= right; i++) {
		help[i - left] = arr[i];
	}
	int i = left， j = mid + 1;
	for (int k = left; k <= right; k++) {
		if (i > mid) {
			arr[k] = help[j - left];
			j++;
		} else if (j > right) {
			arr[k] = help[i - left];
			i++;
		} else if (help[i - left] > help[j - left]) {
			arr[k] = help[j - left];
			j++;
		} else {
			arr[k] = help[i - left];
			i++;
		}
	}
}
public static void sort(int[] arr， int left， int right) {
	if (arr == null || left > right || left > arr.length || right > arr.length) {
		return;
	}
	for (int i = left+1; i <= right; i++) {
		int j;
		int temp = arr[i];
		for (j = i; j > left && arr[j - 1] > temp; j--) {
			arr[j] = arr[j-1];
		}
		arr[j] = temp;
	}
}
```

## 4.4、有k个有序数组，将他们归并为一个有序数组

Leetcode-23

# 5、快速排序-类似于归并排序

通常情况下，快速排序的时间复杂度为$O(N\log\N)$，但在最坏情况下它的时间复杂度会退化至$O(n^2)$

## 5.1、思路

假设待排序数组为`a[0..N-1]`，递归的对该数组执行以下过程：选取一个切分元素，而后通过数组元素的交换将这个切分元素移动到位置j，使得所有`a[0..j-1]`的元素都小于等于`a[j]`，所有`a[j+1..N-1]`的元素都大于等于`a[j]`。在快速排序中，切分元素的选取很关键，通常我们可以选取输入数组的第一个元素作为切分元素，然后把它交换到数组中的合适位置使得它左边的元素都小于等于它，右边的元素都大于等于它，而后对其左右两边的子数组递归执行切分过程，即可完成对整个数组的排序；

## 5.2、基本实现

```java
public void sort(int[] arr， int n) {
	quickSort(arr， 0， n - 1);
}

public void quickSort(int[] arr， int left， int right) {
	if (left >= right) {
		return;
	}
	int p = partition(arr， left， right);
	quickSort(arr， left， p - 1);
	quickSort(arr， p + 1， right);
}

/**
	* 对arr[l...r]部分进行partition操作
	* 返回p，使得arr[l...p-1] < arr[p] ; arr[p+1...r] > arr[p]
	* @return
	*/
private int partition(int[] arr， int left， int right) {
	int temp = arr[left];
	// 使得 arr[left+1...j] < temp， arr[j+1...right] > temp
	int j = left;
	for (int i = left + 1; i <= right; i++) {
		if (arr[i] < temp) {
			ArrayUtils.exchange(arr， j+1， i);
			j++;
		}
	}
	ArrayUtils.exchange(arr， j， left);
	return j;
}
```

## 5.3、快速排序优化1

随机化快速排序，在近乎有序的数组中，快速排序的算法时间复杂度会退化成 O(n^2)。这里可以在数组中随机选取一个数作为切分数组的元素；

```java
/*
	* 对arr[l...r]部分进行partition操作
	* 返回p，使得arr[l...p-1] < arr[p] ; arr[p+1...r] > arr[p]
	* @return
	*/
private int partition(int[] arr， int left， int right) {
	// 随机获取标的，
	int random = left + (int) (Math.random()*(right - left + 1 ));
	ArrayUtils.exchange(arr， left， random);
	int temp = arr[left];
	// 使得 arr[left+1...j] < temp， arr[j+1...right] > temp
	int j = left;
	for (int i = left + 1; i <= right; i++) {
		if (arr[i] < temp) {
			ArrayUtils.exchange(arr， j+1， i);
			j++;
		}
	}
	ArrayUtils.exchange(arr， j， left);
	return j;
}
```

## 5.4、快速排序优化2

一个数组中如果有大量的重复元素存在的化，快速排序的算法时间复杂度会退化成 O(n^2)。可以有通过两个双索引，一个从前往后遍历处理大于于中间值的数据，一个从后往前遍历处理小于中间值的数据。

```java
private int partition2(int[] arr， int left， int right) {
	// 随机化取几个索引值
	int random = left + (int) (Math.random() * (right - left + 1));
	ArrayUtils.exchange(arr， left， random);
	int temp = arr[left];
	// 使得 arr[left+1...i) < temp， arr(j...right] > temp
	int i = left + 1， j = right;
	while (true) {
		// 右边遍历
		while (i <= right && arr[i] < temp) {
			i++;
		}
		// 左边遍历
		while (j >= left + 1 && arr[j] > temp) {
			j--;
		}
		if (i > j) {
			break;
		}
		ArrayUtils.exchange(arr， i， j);
		i++;
		j--;
	}
	ArrayUtils.exchange(arr， j， left);
	return j;
}
```

## 5.5、优化：三路排序

即小于中间值，大于中间值，等于中间值，分三部分来排序

```java
public void sort(int[] arr， int n) {
		quickSort3Way(arr， 0， n - 1);
	}
private void quickSort3Way(int[] arr， int left， int right) {
	if (left > right) {
		return;
	}
	if (right - left <= 15) {
		InsertSelectIntOpt.sort(arr， left， right);
		return;
	}

	// 三路快速排序
	int random = left + (int) (Math.random() * (right - left + 1));
	ArrayUtils.exchange(arr， random， left);
	int temp = arr[left];

	int lt = left， // arr[left+1，lt] < temp
			gt = right + 1，// arr[gt，r] > temp
			i = left + 1;
	while (i < gt) {
		if (arr[i] < temp) {
			ArrayUtils.exchange(arr， i， lt + 1);
			lt++;
			i++;
		} else if (arr[i] > temp){
			// gt 和 i 交换完数据之后，i 位置的元素还没有处理过，所以 i不需要 ++
			ArrayUtils.exchange(arr， gt-1， i);
			gt--;
		} else {
			// arr[i] == temp
			i++;
		}
	}
	ArrayUtils.exchange(arr， left， lt);
	quickSort3Way(arr， left， lt-1);
	quickSort3Way(arr， gt， right);
}
```

# 6、归并排序和快速排序衍生的问题

- 找出数组中的逆序对(归并排序的思路)
- 数组中第 n 大的元素(可以排序后取，也可以使用 快速排序的思想)

# 7、堆排序：堆就是完全二叉树，堆是优先队列

## 7.1、思路

数组元素构建堆，根节点最大，删除根节点得到最大值，剩下的元素再次构建堆，接着再删除根节点，得到第二大元素，剩下的元素再次构建堆，依次类推，得到一组排好序的数据。为了更好地利用空间，我们把删除的元素不使用新的空间，而是使用堆的最后一位保存删除的数据；

## 7.2、实现

```java
public class HeapSort {
	public static void main(String[] args) {
		int[] arr = Utils.generateArray(10， 50);
		System.out.println(Arrays.toString(arr));
		new HeapSort().heapSort(arr， arr.length);
		System.out.println(Arrays.toString(arr));
	}			
	public int[] heapSort(int[] A， int n){
		if(A == null || A.length < 2){
			return A;
		}
		for(int i = A.length / 2; i>= 0; i--){
			buildHeap(A， i， A.length);
		}
		for(int i = A.length - 1;i>0;i--){
			swap(A， 0， i);
			buildHeap(A， 0， i);
		}
		return A;
	}
	private void swap(int[] a， int i， int j) {
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
	private void buildHeap(int[] a， int i， int len) {
		int leftChild = leftChild(i);
		int temp = a[i];
		for(;leftChild < len;){
			if(leftChild != len - 1 && a[leftChild] < a[leftChild+1]){
				leftChild++;
			}
			if(temp < a[leftChild]){
				a[i] = a[leftChild];
			}else{
				break;
			}
			i = leftChild;
			leftChild = leftChild(i);
		}
		a[i] = temp;
	}			
	private int leftChild(int i){
		return 2 * i + 1;
	}
}
```

# 8、跟堆相关的问题

- 使用堆实现有限队列(索引堆)
- 在N个元素中选出前M个元素
- 最大最小队列
- 二项堆，斐波那契堆

# 9、希尔排序

也叫缩减增量排序，其中增量的设置影响着程序的性能

## 9.1、思路

最好的增量的设置为1，3，5，7，11，。。。这样一组素数，并且各个元素之间没有公因子.这样的一组增量 叫做Hibbard增量		

## 9.2、实现
```java
public class ShellSort {
	public static void main(String[] args) {
		int[] A = Utils.generateArray(10， 100);
		System.out.println(Arrays.toString(A));
		int k;
		for (int div = A.length/2; div>0; div/=2) {
			for (int j = div; j < A.length; j++) {
				int temp = A[j];
				for (k=j; k>=div && temp<A[k-div] ; k-=div) {
					A[k] = A[k-div];
				}
				A[k] = temp;
			}
		}
		System.out.println(Arrays.toString(A));
	}
}
```

# 10、排序算法比较

## 1、基本比较

|排序方法|平均情况|最好情况|最坏情况|辅助空间|稳定性|
|-------|--------|-------|-------|--------|-----|
|冒泡排序|O(n<sup>2</sup>)|O(n)|O(n<sup>2</sup>)|O(1)|稳定|
|选择排序|O(n<sup>2</sup>)|O(n<sup>2</sup>)|O(n<sup>2</sup>)|O(1)|不稳定|
|插入排序|O(n<sup>2</sup>)|O(n)|O(n<sup>2</sup>)|O(1)|稳定|
|希尔排序|O(nlogn)~O(n<sup>2</sup>)|O(n<sup>1.3</sup>)|O(n<sup>2</sup>)|O(1)|不稳定|
|堆排序|O(nlogn)|O(nlogn)|O(nlogn)|O(1)|不稳定|
|归并排序|O(nlogn)|O(nlogn)|O(nlogn)|O(n)|稳定|
|快速排序|O(nlogn)|O(nlogn)|O(n<sup>2</sup>)|O(logn)~O(n)|不稳定|

## 2、关于稳定性与非稳定性
- 排序前后两个相等的数相对位置不变，则算法稳定；从一个键上排序，然后再从另一个键上排序，第一个键排序的结果可以为第二个键排序所用

# 11、计数排序

计数排序是适用于一定范围内的帧数排序，在取值范围不是很大的情况下，它的性能甚至快过复杂度为O(NlogN)的排序算法；

## 11.1、思路

假设数组中有20个随机整数，取值范围是0~10，要求用最快的速度把这20个整数从小到大排序；

考虑到这些整数只能够在0、1、2、3、4、5、6、7、8、9、10，这11个数中取值，取值范围有限；所以根据其有限范围，建立一个长度为11的数组。数组下标从1到10，元素初始值全为0
```
0	0	0	0	0	0	0	0	0	0	0
0	1	2	3	4	5	6	7	8	9	10
```
给到一个数组，开始遍历整个无序的随机数组，每个整数值按照其值对号入座，同时对应数组下标的元素进行加1操作；当遍历完数组后，该数组中每一个下标位置的值代表数组中对应整数出现的次数；

有了统计结果之后，直接遍历数组，输出数组元素的下标值，元素的值是几，就输出几次；

## 11.2、基本实现

```java
public static int[] countSort(int[] array) {
	int max = array[0];
	int len = array.length;
	// 找出最大的元素，其作为计数数组的索引最大值
	for (int i = 1; i < len; i++) {
		if (max < array[i]) {
			max = array[i];
		}
	}
	// 遍历原数组，
	int[] countArr = new int[max + 1];
	for (int i = 0; i < len; i++) {
		countArr[array[i]]++;
	}
	// 迭代数组
	int[] result = new int[len];
	int index = 0;
	for (int i = 0; i < max + 1; i++) {
		for (int j = 0; j < countArr[i]; j++) {
			result[index++] = i;
		}
	}
	return result;
}
```

## 11.3、优化

上面的实现存在比较大的问题，比如有个数组：`[95, 94, 91, 98, 99, 90, 93, 91, 92]`，这个数组的最大值是99，但是最小的整数是92，如果创建长度为100的数组，那么前面0-92空间位置就浪费了；

解决思路：在创建数组的时候，只要不再以`输入数组的最大值 + 1` 作为统计数组的长度，而是`以数组最大值 - 最小值 + 1` 作为统计数组的长度即可，同时，数组的最小值作为一个偏移量，用于计算整数在统计数组中的下标；

以上面的数组为例，统计出数组的长度为 `99 - 90 + 1 = 10`, 偏移量等于数组的最小值90，对于第一个整数95，对应的统计数组下标是 `95 - 90 = 5`；

# 12、桶排序

https://mp.weixin.qq.com/s/qrboxA5SwN7AbAcpZ_dpNQ

# 13、基数排序


# 参考资料

* [快速排序算法实现原理](http://www.cnblogs.com/nullzx/p/5880191.html)
* [计数排序](https://mp.weixin.qq.com/s/WGqndkwLlzyVOHOdGK7X4Q)




























