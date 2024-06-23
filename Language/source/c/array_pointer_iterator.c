#include <stdio.h>
#define N 5

/**
 * （3）使用指针变量
 */
void IteratePointer2() {
  int a[N];
  int *p = a;
  printf("请输入%d个整数：\n", N);
  for (int i = 0; i < N; i++)
    scanf("%d", p + i);

  for (p = a; p < (a + N); p++)
    printf("%d ", *p);

  printf("\n");
}

/**
 * （3）使用指针变量
 */
void IteratePointer() {
  int a[N];
  int *p = a;
  printf("请输入%d个整数：\n", N);
  for (int i = 0; i < N; i++)
    scanf("%d", p + i);
  for (int i = 0; i < N; i++)
    printf("%d ", *(p + i));
  printf("\n");
}

/**
 * （2）使用指针（数组名称 + 索引）
 */
void IterateArrayNameIndex() {
  int a[N];
  printf("请输入%d个整数：\n",N);
  for (int i = 0; i < N; i++)
    scanf("%d", &a[i]); //数组元素用数组名和下标表示

  for (int i = 0; i < N; i++)
    printf("%d ", *(a + i));

  printf("\n");
}

/**
 * （1）使用数组下标遍历数组
 */
void IterateIndex() {
  int a[N];
  printf("请输入%d个整数：\n",N);
  for (int i = 0; i < N; i++)
    scanf("%d", &a[i]); //数组元素用数组名和下标表示
  for (int i = 0; i < N; i++)
    printf("%d ", a[i]);

  printf("\n");
}

int main() {
  IteratePointer();
  return 0;
}