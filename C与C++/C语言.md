

预编译：`gcc -E constant.c -o output.c`

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

# 参考资料

- [C在线编译](https://godbolt.org/)
