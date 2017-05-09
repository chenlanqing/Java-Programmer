1.数组的逆置算法:
	1.1.整型数组的逆置算法:
		(1).给定整形数组，实现数组的逆置；要求时间复杂度为 O(N)，空间复杂度为 O(1)。
			1,2,3,4,5,6,7 ==> 7,6,5,4,3,2,1
		(2).实现1:时间复杂度 O(N), 空间复杂度 O(1)
			public void swap(int[] array, int i, int j){
				int t = array[i];
				array[i] = array[j];
				array[j] = t;
			}
			public void inverse01(int[] array){
				if(array == null || array.length <= 1){
					return;
				}
				for(int i=0,j=array.length-1; i<j; i++,j--){
					swap(array, i, j);
				}
			}
		(3).实现2:省略上述的 j ,循环条件：i<n/2, 时间复杂度 O(N), 空间复杂度 O(1)
			public void inverse02(int[] array){
				if(array == null || array.length <= 1){
					return;
				}
				int n = array.length;
				int half = n / 2;
				for(int i=0; i< half; i++){
					swap(array, i, n-1-i);
				}
			}
	1.2.合理利用泛型:时间复杂度 O(N), 空间复杂度 O(1)
			public class InverseArray<T> {
				public void printArray(T[] array) {
					for(int i=0; i<array.length; i++){
						System.out.print(array[i]);
					}
					System.out.println();
				}
				public void swap(T[] array, int i, int j){
					T t = array[i];
					array[i] = array[j];
					array[j] = t;
				}
				public void inverse01(T[] array){
					if(array == null || array.length <= 1){
						return;
					}
					for(int i=0,j=array.length-1; i<j; i++,j--){
						swap(array, i, j);
					}
				}				
				public void inverse02(T[] array){
					if(array == null || array.length <= 1){
						return;
					}
					for(int i=0; i< array.length / 2; i++){
						swap(array, i, array.length-1-i);
					}
				}
			}
2.旋转数组:
	2.1.描述:
		给定一个数组，长度为n，要求把后k个元素移动至前面，前n-k个元素移动至后面。比如，n=7，k=3
		1,2,3,4,5,6,7 ==> 5,6,7,1,2,3,4
	2.2.分析:
		hello world ==> world hello
		hello world ==> olleh dlrow ==> world hello
		reverse(array,0,n-k-1);//反转前一半
		reverse(array,n-k,n-1);//反转后一半
		reverse(array,0,n-1);//反转整个数组
		==> 边界考虑:
		k=0时，不予考虑
		k有可能大于n，n=7，k=24，此时k=k%n=24%7=3
	2.3.实现:时间复杂度 O(N), 空间复杂度 O(1)
		public void swap(int[] array, int i, int j){
			int t = array[i];
			array[i] = array[j];
			array[j] = t;
		}		
		public void reverse(int[] array, int start, int end){
			if(array == null || array.length <= 1){
				return;
			}
			for(int i=start,j=end; i<j; i++,j--){
				swap(array, i, j);
			}
		}		
		public void rotate(int[] array, int k) {
			if(k == 0){
				return;
			}
			int n = array.length;
			if(k > n){
				k = k % n;
			}
			reverse(array, 0, n-1-k);
			reverse(array, n-k, n-1);
			reverse(array, 0, n-1);
		}

3.找出数组中和为 s 的两个数字:
	3.1.描述:已知一个升序数组array和一个数字s，数组不包含重复数字，在数组中查找两个数，
		使得它们的和正好为s；如果有多对数字的和等于s，则全部输出
		array={1 , 3 , 4 , 5 , 8 , 9 , 11}, s=13
		==> [4,9], [5,8];
	3.2.平方级算法:时间复杂度 O(N^2), 空间复杂度 O(1)
		public void printResult(int a, int b) {
			System.out.println("[" + a + "," + b + "]");
		}		
		public void sumCompare(int[] array, int s){
			int n  = array.length;
			for(int i = 0; i<n;i++){
				for(int j=i+1;j<n;j++){
					if( (array[i] + array[j]) == s){
						printResult(array[i], array[j]);
						break;
					}
				}
			}
		}
	3.3.线性算法:充分利用提供的数组是升序数组
		array={1 , 3 , 4 , 5 , 8 , 9 , 11}, s=13
		(1).分析:利用数组是升序的数组,使用头尾指针来实现
			i = 0, j = array.length-1
			array={1 , 3 , 4 , 5 , 8 , 9 , 11} ==> i=0,	j=6, 1 + 11 = 12
			array={1 , 3 , 4 , 5 , 8 , 9 , 11} ==> i++,	j, 3 + 11 = 14
			array={1 , 3 , 4 , 5 , 8 , 9 , 11} ==> i,j--, 3 + 9 = 12
			array={1 , 3 , 4 , 5 , 8 , 9 , 11} ==> i++,j, 4 + 9 = 13
			array={1 , 3 , 4 , 5 , 8 , 9 , 11} ==> i++,j--, 5 + 8 = 13
		(2).实现:时间复杂度 O(N), 空间复杂度 O(1)
			public void sumCompare1(int[] array, int s) {
				int i = 0;
				int j = array.length - 1;
				while(i < j){
					int sum = array[i] + array[j];
					if(sum == s){
						printResult(array[i] , array[j]);
						i++;
						j--;
					} else if(sum < s){
						i++;
					} else{
						j--;
					}
				}
			}
	3.4.二分查找法:时间复杂度 O(Nlog2N), 空间复杂度 O(1)
		public void sumCompare2(int[] array, int s){
			int n = array.length;
			for(int i=0;i<n-1;i++){
				int another = s - array[i];
				if(Arrays.binarySearch(array, another) >= i+1){
					printResult(array[i] , another);
				}
			}
		}
4.和为 s 连续正整数序列:
	4.1.描述:输入一个正整数s，打印出所有和为s的连续正整数序列（至少含有两个数字）
		输入:21
		输出:1 2 3 4 5 6, 6 7 8, 10 11
		依然是双指针！不过，start指针从 1 开始，end指针从 2 开始
		end=2,start=1	1 + 2 = 3 < 21
		end++			1 + 2 + 3 = 6 < 21 
		end++			1 + 2 + 3 + 4 = 10 < 21
		end++			1 + 2 + 3 + 4 + 5 = 15 < 21
		end++			1 + 2 + 3 + 4 + 5 + 6 = 21 ==21
		end++,start++	2 + 3 + 4 + 5 + 6 + 7 = 27 > 21
		start++			3 + 4 + 5 + 6 + 7 = 25 > 21
		start++			4 + 5 + 6 + 7 = 22 > 21
		start++			5 + 6 + 7 = 18 < 21
		end++			5 + 6 + 7 + 8 = 26 > 21
		start++			6 + 7 + 8 =21 == 21
		...
		start = (s + 1) / 2,算法停止
	4.2.实现:时间复杂度 O(N), 空间复杂度 O(1)
		public void printResult(int start, int end) {
			for(int k = start; k<=end; k++){
				System.out.print(k + " ");
			}
			System.out.println();
		}			
		public void sum(int s) {
			int start = 1,
				end = 2,
				sum = start + end,
				half = (s + 1) / 2;
			while(start < half){
				if(sum == s){
					printResult(start, end);
					sum -= start;
					start++;
					end++;
					sum += end;
				} else if(sum < s){
					end ++;
					sum += end;
				} else{
					sum -= start;
					start ++;
				}
			}
		}

5.移除数组的重复元素:
	5.1.描述:
		给定升序数组array，删除重复元素，并返回新的长度len；使得前len个数字升序，并且不得含有重复数字；
		后面的数字是什么，无所谓
	5.2.借助ArrayList解决问题
		(1).思路:
			新建list
			遍历数组，不断的把不重复的元素添加进list
			把list里边的元素拷贝进array
			返回list.size()
		(2).实现:时间复杂度 O(N), 空间复杂度 O(N)
			public int remove(int[] nums){
				if(nums == null || nums.length == 0){
					return 0;
				}else if(nums.length == 1){
					return 1;
				}else{
					int i = 0;
					int end = nums.length - 1;
					ArrayList<Integer> list = new ArrayList<Integer>();
					while(i <= end){
						if(i == end){
							list.add(nums[i]);
							i++;
						}else{
							int j  = i + 1;
							if(nums[i] == nums[j]){
								while(j <= end && nums[i] == nums[j]){
									j++;
								}
							}
							list.add(nums[i]);
							i = j;
						}
					}
					for(i=0;i<list.size();i++){
						nums[i] = list.get(i);
					}
					return list.size();
				}
			}
	5.3.借助ArrayCopy解决问题:时间复杂度 O(N^2), 空间复杂度 O(N)
		public int remove02(int[] nums){
			if(nums==null||nums.length==0){
				return 0;
			}else if(nums.length==1){
				return 1;
			}else{
				int end=nums.length-1;
				for(int i=0;i<=end;i++){
					if(i<end){
						int j=i+1;
						if(nums[i]==nums[j]){
							while(j<=end&&nums[i]==nums[j]){
								j++;
							}
						}
						System.arraycopy(nums, j, nums, i+1, end-j+1);						
						end-=j-i-1;
					}
				}
				return end+1;
			}
		}
	5.4.借助临时变量解决问题:时间复杂度 O(N), 空间复杂度 O(1)
		public int remove03(int[] nums){
			if(nums==null||nums.length==0){
				return 0;
			}else if(nums.length==1){
				return 1;
			}else{
				int temp=nums[0];
				int len=1;
				for(int i=1;i<nums.length;i++){
					if(temp==nums[i]){
						continue;
					}else{
						temp=nums[i];
						nums[len]=nums[i];
						len++;
					}
				}
				return len;
			}
		}


















