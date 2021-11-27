

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
