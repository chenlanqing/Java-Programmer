import com.alrogrithm.sort.SortInt;
import java.util.Arrays;
import java.util.Random;

public class ArrayUtils {

    /**
     * 生成某个范围内的随机数组
     *
     * @param n      数组大小
     * @param rangeL 数组的数据最小值
     * @param rangeR 数组数据的最大值
     * @return
     */
    public static int[] generateRandomArray(int n, int rangeL, int rangeR) {
        assert rangeL < rangeR;
        int[] arr = new int[n];
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        for (int i = 0; i < n; i++) {
//            arr[i] = random.nextInt((int) seed) % (rangeR - rangeL + 1) + rangeL;
            arr[i] = random.nextInt(rangeR - rangeL) + rangeL;
        }
        return arr;
    }

    /**
     * 生成一个近似有序的数组
     *
     * @param n         数组长度
     * @param swapTimes 打乱排序的次数
     * @return
     */
    public static int[] generateNearySortArray(int n, int swapTimes) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i;
        }
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        for (int i = 0; i < swapTimes; i++) {
            int posX = random.nextInt(n);
            int posY = random.nextInt(n);
            exchange(arr, posX, posY);
        }
        return arr;
    }

    /**
     * 交换数组的两个值<br/>
     * 对于整型数据,比较高效的交换数据的方式是通过位运算的,但是这里会存在一个问题,如果刚好当前位置的数据与最小的数据处于同一位置,运算结果为0;<br/>
     * 比如:arr[] = {8,9,1,6,3,5,4,7,2} 经过多次排序结果为:<br/>
     * arr[] = {1,2,3,4,5,6,7,8,9}; 这时还没排序完<br/>
     * i = 7, min = 7 <br/>
     * arr[i] ^= arr[min]; ==> arr[i] = 0 ,即数组变为:{1,2,3,4,5,6,7,0,9},这时侯 arr[min] = 0.
     *
     * @param arr
     * @param i
     * @param min
     */
    public static void exchange(int[] arr, int i, int min) {
//        int temp = arr[i];
//        arr[i] = arr[min];
//        arr[min] = temp;
        // 这里需要加上如果 i == min 则不需要进行互换
        if (i == min) {
            return;
        }
        arr[i] ^= arr[min];
        arr[min] ^= arr[i];
        arr[i] ^= arr[min];
    }

    /**
     * 是否为升序数组
     *
     * @param arr
     * @param n
     */
    public static boolean isAscSort(int[] arr, int n) {
        for (int i = 0; i < n - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否为升序数组
     *
     * @param arr
     * @param n
     */
    public static boolean isDescSort(int[] arr, int n) {
        for (int i = 0; i < n - 1; i++) {
            if (arr[i] < arr[i + 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 测试排序算法的时间
     *
     * @param sortName 排序算法的名称
     * @param sort     排序算法的具体实现实例对象
     * @param arr      测试用例
     * @param n        数组大小
     */
    public static void testSort(String sortName, SortInt sort, int[] arr, int n) {
        long start = System.currentTimeMillis();
        sort.sort(arr, n);
        long end = System.currentTimeMillis();
        assert isAscSort(arr, n) : "排序不正确";
        System.out.println(sortName + ":" + (end - start) + " ms");
    }

    /**
     * 打印数组
     *
     * @param arr
     * @param n
     */
    public static void print(int[] arr, int n) {
        for (int i = 0; i < n; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }


    public static void toString(int[] arr, int n) {
        for (int i = 0; i < n; i++) {
            System.out.print(arr[i] + "(" + i +") ");
        }
        System.out.println();
    }

    /**
     * 拷贝数组
     *
     * @param arr
     * @param len
     * @return
     */
    public static int[] copy(int[] arr, int len) {
        return Arrays.copyOf(arr, len);
    }
}
