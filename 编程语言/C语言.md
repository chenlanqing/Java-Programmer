# 一、基本语法

预编译：`gcc -E constant.c -o output.c`

## clion

clion可以写多个可执行c文件：
```
cmake_minimum_required(VERSION 3.10.2)

get_filename_component(ProjectId ${CMAKE_CURRENT_SOURCE_DIR} NAME)
string(REPLACE " " "_" ProjectId ${ProjectId})
project(${ProjectId} C)

set(CMAKE_C_STANDARD 11)

file(GLOB files "${CMAKE_CURRENT_SOURCE_DIR}/*.c")
foreach(file ${files})
    get_filename_component(name ${file} NAME)
    add_executable(${name} ${file})
endforeach()
```
## 函数

### 函数定义

C语言的函数如果不需要任何参数，定义函数时参数列表写个void即可，否则表示不确定有几个参数，且函数声明必须在调用之前
```c
void func(){
} 
void func1(void){
} 
int main(){
    func();
    func(1);// 这样调用也能成功
    func1();
    func1(1); // 这样会报错
    return 0;
}
```

### 函数变长参数

```c
int printf(const char * const _Format, ...); // ... 表示变长参数
```
如何获取变长参数的值呢？
```c
#include <stdio.h>
#include <stdarg.h>

void HandleVarargs(int arg_count, ...) {
  va_list args; // ① 处定义 va_list 类型的变量 args，用于获取变长参数。
  int i;
  va_start(args, arg_count); // ② 处实际上是初始化 args 的值，这里同时需要变长参数前面的最后一个参数 arg_count
  for (i = 0; i < arg_count; ++i) {
    int arg = va_arg(args, int); // ③ 处则是在知道了变长参数的位置之后按照类型一个一个取出参数
    printf("%d: %d\n", i, arg);
  }
  va_end(args); // ④ 清理变长参数的读取工作
}
int main(int argc, char **argv) {
  HandleVarargs(3, 100, 200, 300);
  return 0;
}
```
使用 va_start, va_arg, va_end 这些宏需要导入 stdarg.h 这个头文件；va_list 就像叫号器一样，变长参数从 va_start 之后开始叫号，va_arg 就是叫号过程

## 作用域

static 变量具备文件作用域
```c
void LocalStaticVar(void) {
  // 静态变量
  // 1. 作用域全局，内存不会因函数退出而销毁
  // 2. int 初值默认为 0
  static int static_var;
  // 自动变量
  // 1. 函数、块作用域，随着函数和块退出而销毁
  // 2. 没有默认初值
  int non_static_var;

  printf("static var: %d\n", static_var++);
  printf("non static var: %d\n", non_static_var++);
}
```

## 文件包含

- `#include "xxx.h"`：首先查找当前源文件所在路径；再次查找工程的头文件搜索路径；
- `#include <..h>`：查找工程的头文件搜索路径；

如果想自己定义的目录也加到工程目录下，可以在CMakeLists.txt文件中加入如下代码：
```
....
include_directories("include")
....
```
这样在使用的时候就可以直接使用：`#include <xxx.h>`

## 自定义头文件

比如自定义一个阶乘函数

（1）首先新建两个文件夹：include、src

（2）在include目录下新建head文件：factorial.h，有如下内容：
```h
#ifndef C_LEARNING_1_INCLUDE_FACTORIAL_H_
#define C_LEARNING_1_INCLUDE_FACTORIAL_H_

unsigned int Factorial(unsigned int n);

unsigned int FactorialByIteration(unsigned int n);

#endif //C_LEARNING_1_INCLUDE_FACTORIAL_H_
```
（3）在src新建文件factorial.c，就是阶乘的实现
```c
#include "../include/factorial.h"
unsigned int Factorial(unsigned int n) {
  if (n == 0) {
    return 1; // f(0) = 1
  } else {
    return n * Factorial(n - 1); // f(n) = nf(n - 1)
  }
}
unsigned int FactorialByIteration(unsigned int n) {
  unsigned int result = 1;
  for (unsigned int i = n; i > 0; --i) {
    result *= i;
  }
  return result;
}
```
（4）如果需要使用该函数，可以按照如下方式引入：
```c
#include "include/factorial.h"
```

## 宏函数

### 基本介绍

“宏函数”，仍然在预处理过程中实现对源文件的替换，但不同之处在于它可以接受参数，因此看起来更像函数
```c
#define MAX(a, b) a > b ? a : b
```
使用的时候就直接像调用函数那样就可以了：
```c
int max = MAX(1, 3); 
```
编译之后替换为：
```c
int max = 1 > 3 ? 1 : 3;
```
需要注意的几个问题：
  - **参数的问题**：与函数不同，宏函数的参数是直接把原始的表达式替换到宏定义当中的，而函数则是先把表达式的值求出来，再把值传入函数，因此对于下面看上去没有什么问题的语句：
  ```c
  int max = MAX(1, MAX(3, 10));
  // 其编译替换的结果如下，下面这个表达式是无法正常工作的
  int max = 1 > 3 > 10 ? 3 : 10 ? 1 : 3 > 10 ? 3 : 10;
  ```
  建议尽量不要用复杂的表达式作为宏函数的参数，不过有些时候这是不能避免的，因此我们为了避免宏函数的参数在替换之后被拆散，要给宏函数的参数加括号：
  ```c
  #define MAX(a, b) (a) > (b) ? (a) : (b)
  // 上述
  int max = MAX(1, MAX(3, 10));
  // 其编译替换的结果如下，下面这个表达式是无法正常工作的
  int max = 1 > (3 > 10 ? 3 : 10;) ? (1 : 3 > 10 ? 3 : 10;);
  ```
  分号导致整个语句被切割成了几条语句，这自然是不符合预期的，因此定义宏时通常不加分号。如果要加分号，也要控制好使用场景

- **宏函数的参数的参数可能会多次求值，这与函数的参数的行为不同**
  ```c
  int a = 0;
  int b = 3;
  int max = MAX(a, b++);
  // 宏替换后得到代码
  int a = 0;
  int b = 3;
  int max = a > b++ ? a : b++;
  ```
  因此，不要将有副作用的表达式作为宏函数的参数。

宏函数是不能换行的，如果确实需要换行，可以按照如下写法：
```c
#define  IS_HEX_CHARACTER(ch) \
  (((ch) >= '0' && (ch) <= '9') || \
  ((ch) >= 'A' && (ch) <= 'F') || \
  ((ch) >= 'a' && (ch) <= 'f') )
```

### 宏函数与函数对比

| 属性         | 宏                                                           | 函数                                                         |
| :----------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| 代码长度     | 每次使用时，宏代码都被插入到程序中。除了非常小的宏之外，程序的长度将大幅度增长 | 函数代码只出现于一个地方；每次使用这个函数时，调用用那个地方的用一份代码 |
| 操作符优先级 | 宏参数的求值在所有周围表达式的上下文环境里，除非加上括号，否则邻近的操作符优先级可能会产生不可预料的结果 | 函数参数只在函数调用时求值一次，它的结果值传递给函数。表达式的求值结果更容易预测 |
| 参数求值     | 参数每次用于宏定义时，它们 都将重新求值。由于多次求值，具有负作用域的参数可能产生不可预料的结果 | 参数在函数被调用前只求值一次。在函数中多次使用并不会导致多次求值的问题，参数的副作用不会造成任何特殊的问题 |
| 参数类型     | 宏与类型无关。只要对参数的操作是合法的，它可以适用于任何参数 | 函数的参数是与类型有关的。如果参数的类型不同，就需要使用不同的函数，即使它们执行的任务是相同的 |

### 常见其他宏函数

- `__FILE__` ：预处理时替换为源文件的路径。
- `__LINE__` ：预处理时替换为源代码所在源文件的行号。
- `__FUNCTION__` ：编译后替换为源代码所在的函数名，或者指向函数名的字符串变量，取决于具体的编译器实现。不同于前面的两个宏，由于代码所在函数只要在编译过程中才能解析得到，因此这个宏不会在预处理过程中完成替换
- `__VA_ARGS__`：替代变长参数，`##__VA_ARGS__`，其实 ## 的作用是用于变长参数没有对应的参数传入的情况下，去除前面的逗号的，如果不加 ##，可能在某些编译器下会报错；
- `#value`，比如定义一个变量宏，那么这个`#value`获得变量的名作为运行时的字符串内容

### 练习

自定义一个带换行符的打印函数
```c
#include <stdio.h>
#include <stdarg.h>
// 这是使用函数的方式，通过编程函数来处理
void Printlnf(const char *format, ...) {
  va_list args;
      va_start(args, format);
  vprintf(format, args);
  printf("\n");
      va_end(args);
}
// 这里使用宏来操作
#define PRINTLNF(format, ...) printf("("__FILE__":%d) %s : "format"\n",__LINE__, __FUNCTION__, ##__VA_ARGS__)
// 定义打印变量名的宏
#define PRINT_INT(value) PRINTLNF(#value": %d", value)
int main() {
  int value = 2;
  Printlnf("Hello World! %d", value);
  PRINTLNF("Hello World! %d", value);
  PRINTLNF("Hello World!");
  PRINT_INT(value); // value: 2
  int x = 3;
  PRINT_INT(x);
  PRINT_INT(3 + 4);
  return 0;
}
```

## 条件编译

### 条件宏

比如前面定义的头文件factorial.h，包含如下代码：
```h
#ifndef C_LEARNING_1_INCLUDE_FACTORIAL_H_
#define C_LEARNING_1_INCLUDE_FACTORIAL_H_

#endif //C_LEARNING_1_INCLUDE_FACTORIAL_H_
```
上面这些都是宏，其中：
- `#ifndef`：表示判断如果没有定义某个宏，在这里就是说如果没有定义 C_LEARNING_1_INCLUDE_FACTORIAL_H_ 这个宏，那么就执行下面的代码。
- `#endif`：很明显与 `#ifndef` 是配对的，表示这个条件判断的有效范围。

上面这三句话的目的是：避免头文件重复被包含，除了 *.c 文件可以包含头文件以外，头文件本身也是可以包含其他头文件的，因为头文件与 *.c 本身没有什么不同，只是我们在代码设计的时候为了方便模块之间的引用而专门用头文件来编写声明，用源文件来编写实现。既然头文件可以互相包含，那难免会导致对某些头文件的重复包含

条件编译有如下三种写法：
```
#ifdef 如果定义了
#ifndef 如果没定义
#if 如果 ...
```

### 使用场景

条件宏有一个很好的使用场景，那就是判断当前程序是否运行在 DEBUG 模式下来决定是否执行某些用于调试的代码
```c
#include <stdio.h>

int main() {
#if DEBUG
  printf("[DEBUG]main start.\n");
#endif
  printf("main run ---> \n");

#if DEBUG
  printf("[DEBUG]main end.\n");
#endif
  return 0;
}
```
如果我们不希望输出调试的日志，就把 DEBUG 这个宏的值改为 0 就好了，DEBUG 这个宏的定义也可以在代码编译的时候通过编译器参数来指定，在我们的 CLion 工程中的 CMakeList.txt 当中添加下面的代码即可编译出 DEBUG 版本的程序：
```
...
target_compile_definitions(BasicC PUBLIC DEBUG=1) # 加这一句 
...
```

条件编译还有一个比较有用的地方，就是判断当前 C 语言编译环境，最常用的莫过于判断当前是 C 编译器还是 C++ 编译器了：
```c
#ifdef __cplusplus
extern "C" {
#endif
... // 定义 C 接口函数和变量
#ifdef __cplusplus
}
#endif
```
条件编译还支持 else if 类似的分支，例如判断当前 C 语言的编译器版本，这里会用到一些内置的宏定义：
```c
#include <stdio.h>
int main() {
  #if __STDC_VERSION__ >=  201710L
    printf("We are using C18!\n");
  #elif __STDC_VERSION__ >= 201112L
    printf("We are using C11!\n");
  #elif __STDC_VERSION__ >= 199901L
    printf("We are using C99!\n");
  #elif defined(__STDC__)
    printf("We are using C89/C90!\n");
  #else
    printf("We are using non-std C!\n");
  #endif
  return 0;
}
```

## 数组

C语言不会判断数组是否越界

数组作为参数传递到函数中，同时需要传递数组的长度，数组作为参数仅仅传递是首地址；

```c
int array[] = {0, 1, 2, 3, 4};
int *p = array;
PRINT_INT(*(p + 3));// recommend
PRINT_INT(*(array + 3));
PRINT_INT(*(3 + array));
PRINT_INT(3[array]); // not recommend
PRINT_INT(array[3]); //recommend
PRINT_INT(p[3]);
```

## 字符串

c的字符串是已NULL结尾的

# 二、指针

## 特殊指针

- 不要将硬编码复制给指针，比如：`int *p = (int *)3`
- 空指针 NULL 的用法，比如：`int *p = NULL;`，后面 `*p = 3;`会报错；
- 注意避免产生野指针
  ```c
  int *p;
  void dp(){
    int a = 2;
    p = &a;
  }
  ```
  上面指针p在函数dp执行完之后指向的内存已经被销毁了；

## 指针运算

```c
int array[] = {0, 1, 2, 3, 4};
int *p = array;
PRINT_INT(*(p + 3));// recommend
PRINT_INT(*(array + 3));
PRINT_INT(*(3 + array));
PRINT_INT(3[array]); // not recommend
PRINT_INT(array[3]); //recommend
PRINT_INT(p[3]);
```

# 参考资料

- [C在线编译](https://godbolt.org/)
