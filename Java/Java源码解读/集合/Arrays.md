
# 一、sort(int[] a)

## 1、JDK1.6
### 1.1、源码
```java
// int类型数组排序
public static void sort(int[] a) {
    sort1(a, 0, a.length);
}
private static void sort1(int x[], int off, int len) {
    // Insertion sort on smallest arrays
    if (len < 7) {
        for (int i = off; i < len + off; i++)
            for (int j = i; j > off && x[j - 1] > x[j]; j--)
                swap(x, j, j - 1);
        return;
    }
    // Choose a partition element, v
    int m = off + (len >> 1);       // Small arrays, middle element
    if (len > 7) {
        int l = off;
        int n = off + len - 1;
        if (len > 40) {        // Big arrays, pseudomedian of 9
            int s = len / 8;
            l = med3(x, l, l + s, l + 2 * s);
            m = med3(x, m - s, m, m + s);
            n = med3(x, n - 2 * s, n - s, n);
        }
        m = med3(x, l, m, n); // Mid-size, med of 3
    }
    int v = x[m];

    // Establish Invariant: v* (<v)* (>v)* v*
    int a = off, b = a, c = off + len - 1, d = c;
    while (true) {
        while (b <= c && x[b] <= v) {
            if (x[b] == v)
                swap(x, a++, b);
            b++;
        }
        while (c >= b && x[c] >= v) {
            if (x[c] == v)
                swap(x, c, d--);
            c--;
        }
        if (b > c)
            break;
        swap(x, b++, c--);
    }
    // Swap partition elements back to middle
    int s, n = off + len;
    s = Math.min(a - off, b - a);
    vecswap(x, off, b - s, s);
    s = Math.min(d - c, n - d - 1);
    vecswap(x, b, n - s, s);

    // Recursively sort non-partition-elements
    if ((s = b - a) > 1)
        sort1(x, off, s);
    if ((s = d - c) > 1)
        sort1(x, n - s, s);
}
/**
 * Swaps x[a] with x[b].
 */
private static void swap(int x[], int a, int b) {
    int t = x[a];
    x[a] = x[b];
    x[b] = t;
}
/**
 * Swaps x[a .. (a+n-1)] with x[b .. (b+n-1)].
 */
private static void vecswap(int x[], int a, int b, int n) {
    for (int i = 0; i < n; i++, a++, b++)
        swap(x, a, b);
}
/**
 * Returns the index of the median of the three indexed integers.
 */
private static int med3(int x[], int a, int b, int c) {
    return (x[a] < x[b] ?
            (x[b] < x[c] ? b : x[a] < x[c] ? c : a) :
            (x[b] > x[c] ? b : x[a] > x[c] ? c : a));
}
```
### 1.2、分析
- （1）数组长度小于7，那么排序时基于基本的插入排序算法
- （2）数组长度大于7，那么在使用的优化后的快速排序，对应数组长度在7和40之间的数组，取的切分元素相对来说简单点
	
## 2、JDK1.7

### 2.1、源码：
```java
public static void sort(int[] a) {
    DualPivotQuicksort.sort(a);
}

// 下面方法来自：java.util.DualPivotQuicksort#sort(int[])
public static void sort(int[] a) {
    sort(a, 0, a.length - 1);
}
/**
 * If the length of an array to be sorted is less than this
 * constant, Quicksort is used in preference to merge sort.
 */
private static final int QUICKSORT_THRESHOLD = 286;
/**
 * The maximum number of runs in merge sort.
 */
private static final int MAX_RUN_COUNT = 67;
/**
 * The maximum length of run in merge sort.
 */
private static final int MAX_RUN_LENGTH = 33;

public static void sort(int[] a, int left, int right) {
    // Use Quicksort on small arrays
    if (right - left < QUICKSORT_THRESHOLD) {
        sort(a, left, right, true);
        return;
    }

    /*
     * Index run[i] is the start of i-th run
     * (ascending or descending sequence).
     */
    int[] run = new int[MAX_RUN_COUNT + 1];
    int count = 0; run[0] = left;

    // Check if the array is nearly sorted
    for (int k = left; k < right; run[count] = k) {
        if (a[k] < a[k + 1]) { // ascending
            while (++k <= right && a[k - 1] <= a[k]);
        } else if (a[k] > a[k + 1]) { // descending
            while (++k <= right && a[k - 1] >= a[k]);
            for (int lo = run[count] - 1, hi = k; ++lo < --hi; ) {
                int t = a[lo]; a[lo] = a[hi]; a[hi] = t;
            }
        } else { // equal
            for (int m = MAX_RUN_LENGTH; ++k <= right && a[k - 1] == a[k]; ) {
                if (--m == 0) {
                    sort(a, left, right, true);
                    return;
                }
            }
        }

        /*
         * The array is not highly structured,
         * use Quicksort instead of merge sort.
         */
        if (++count == MAX_RUN_COUNT) {
            sort(a, left, right, true);
            return;
        }
    }

    // Check special cases
    if (run[count] == right++) { // The last run contains one element
        run[++count] = right;
    } else if (count == 1) { // The array is already sorted
        return;
    }

    /*
     * Create temporary array, which is used for merging.
     * Implementation note: variable "right" is increased by 1.
     */
    int[] b; byte odd = 0;
    for (int n = 1; (n <<= 1) < count; odd ^= 1);

    if (odd == 0) {
        b = a; a = new int[b.length];
        for (int i = left - 1; ++i < right; a[i] = b[i]);
    } else {
        b = new int[a.length];
    }

    // Merging
    for (int last; count > 1; count = last) {
        for (int k = (last = 0) + 2; k <= count; k += 2) {
            int hi = run[k], mi = run[k - 1];
            for (int i = run[k - 2], p = i, q = mi; i < hi; ++i) {
                if (q >= hi || p < mi && a[p] <= a[q]) {
                    b[i] = a[p++];
                } else {
                    b[i] = a[q++];
                }
            }
            run[++last] = hi;
        }
        if ((count & 1) != 0) {
            for (int i = right, lo = run[count - 1]; --i >= lo;
                 b[i] = a[i]
                    );
            run[++last] = right;
        }
        int[] t = a; a = b; b = t;
    }
}

/**
 * Sorts the specified range of the array by Dual-Pivot Quicksort.
 *
 * @param a the array to be sorted
 * @param left the index of the first element, inclusive, to be sorted
 * @param right the index of the last element, inclusive, to be sorted
 * @param leftmost indicates if this part is the leftmost in the range
 */
private static void sort(int[] a, int left, int right, boolean leftmost){}
```
在JDK7中，排序使用的双轴快速排序，其要比传统的单轴排序要快

- 双轴快速排序：如果数组的长度小于QUICKSORT_THRESHOLD的话就会使用这个双轴快速排序，而这个值是286
```java
if (right - left < QUICKSORT_THRESHOLD) {
    sort(a, left, right, true);
    return;
}
```
## 3、JDK1.8

### 3.1、源码：
```java

```





