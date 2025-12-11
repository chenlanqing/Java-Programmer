# Java 与 Go 语言完整对比指南

## 目录
- [1. 代码组织与基本格式](#1-代码组织与基本格式)
- [2. 变量与常量](#2-变量与常量)
- [3. 数据类型](#3-数据类型)
- [4. 流程控制](#4-流程控制)
- [5. 函数与方法](#5-函数与方法)
- [6. 面向对象](#6-面向对象)
- [7. 错误处理](#7-错误处理)
- [8. 并发编程](#8-并发编程)
- [9. 内存管理与指针](#9-内存管理与指针)
- [10. 集合操作](#10-集合操作)
- [11. 字符串操作](#11-字符串操作)
- [12. 文件 I/O](#12-文件-io)
- [13. 关键差异总结](#13-关键差异总结)

---

## 1. 代码组织与基本格式

| 特性 | Java | Go | 核心差异 |
|------|------|-----|----------|
| **行尾分号** | 必须 `;` | 不需要（编译器自动插入） | 一行多条语句才需要 |
| **代码块** | `{ ... }` | `{ ... }` | **左花括号 `{` 不能换行**（必须跟在语句末尾） |
| **入口函数** | `public static void main(String[] args)` | `func main() { }` | 必须位于 `package main`，无参数无返回值 |
| **包声明** | `package com.example.demo;` | `package main` | Go 包名通常只是目录名，不含反向域名 |
| **导入** | `import java.util.*;` | `import "fmt"` | 不支持通配符 `*`；未使用的导入会**编译错误** |
| **导入多个包** | 逐行 import | `import ( "fmt"; "time" )` | 使用括号分组 |
| **访问控制** | `public`, `private`, `protected` | **首字母大小写** | 大写=Public，小写=Private（包内可见） |
| **包私有** | 无修饰符（默认） | 小写字母开头 | 只有 public 和 private 两级 |

---

## 2. 变量与常量

| 特性 | Java | Go | 核心差异 |
|------|------|-----|----------|
| **变量声明** | `int x = 10;` | `var x int = 10` 或 `x := 10` | 类型在变量名**之后** |
| **类型推断** | `var a = 10;` (Java 10+) | `x := 10` | `:=` 只能用于函数内部 |
| **常量** | `final int X = 10;` | `const X = 10` | const 只能是数字、字符串、布尔值 |
| **字符串** | `String s = "hello";` | `var s string = "hello"` 或 `s := "hello"` | string 是基本类型 |
| **默认值（零值）** | 基本类型有值，对象为 `null` | **Zero Value** | `int=0`, `bool=false`, `string=""`, 指针=`nil` |
| **枚举** | `enum Color { RED, BLUE }` | `const ( Red = iota; Blue )` | 用 `iota` 计数器模拟枚举 |
| **未使用变量** | 允许（警告） | **编译错误** | 局部变量未使用会编译失败 |

---

## 3. 数据类型

### 3.1 基本类型

| Java | Go | 说明 |
|------|-----|------|
| `int`, `long`, `boolean`, `byte` | `int`, `int64`, `bool`, `byte`, `rune` | `byte` 是 `uint8` 别名；`rune` 是 `int32`（Unicode 字符） |
| `String`（对象） | `string`（基本类型） | Go 字符串不可变，底层是字节数组 |
| `char` | `rune` | Go 使用 `rune` 表示 Unicode 字符 |

### 3.2 复合类型

| Java | Go | 核心差异 |
|------|-----|----------|
| `int[] arr = {1,2,3};` | `arr := []int{1, 2, 3}` | Go 数组是**值类型**，赋值会拷贝 |
| `int[] arr = new int[5];` | `var arr [5]int` | 长度是类型的一部分 `[5]int` ≠ `[10]int` |
| `List<String> list = new ArrayList<>();` | `list := []string{}` 或 `make([]string, 0)` | Slice（切片）是引用类型 |
| `Map<String, Integer> map = new HashMap<>();` | `m := make(map[string]int)` | 内置类型，需用 `make` 初始化 |
| 无指针概念 | `var p *Person` | 支持指针但不支持指针运算 |

### 3.3 类型转换

| Java | Go | 说明 |
|------|-----|------|
| `int x = (int) y;` | `x := int(y)` | **必须显式转换**（包括 `int` → `int64`） |
| `String s = String.valueOf(123);` | `s := strconv.Itoa(123)` | 数字转字符串 |
| `int x = Integer.parseInt("123");` | `x, err := strconv.Atoi("123")` | 字符串转数字（返回错误） |
| `instanceof` | `v, ok := x.(Type)` | 类型断言 |
| - | `switch v := x.(type) { case int: }` | 类型 switch |

---

## 4. 流程控制

**⚠️ 最容易踩坑的地方：Go 砍掉了很多关键字**

### 4.1 条件语句

| Java | Go | 核心差异 |
|------|-----|----------|
| `if (x > 0) { }` | `if x > 0 { }` | **无括号** |
| `if (x > 0) { } else { }` | `if x > 0 { } else { }` | else 的 `{` 必须与 `}` 同行 |
| 不支持 | `if x := getValue(); x > 0 { }` | 可在条件前初始化变量 |
| `a ? b : c` | **不支持三元运算符** | 必须写 `if-else` |

### 4.2 循环

| Java | Go | 核心差异 |
|------|-----|----------|
| `for (int i = 0; i < 10; i++) { }` | `for i := 0; i < 10; i++ { }` | 无括号 |
| `while (condition) { }` | `for condition { }` | **无 while 关键字** |
| `do { } while (condition);` | 不支持 | 用 `for` + `break` 实现 |
| `for (String item : list) { }` | `for _, item := range list { }` | 用 `range` 遍历 |
| 死循环：`while (true)` | `for { }` | 最简洁的死循环 |
| `a = b++` | `a++`（仅作为语句） | `++` 不能在表达式中，**不支持 `++i`** |

### 4.3 Switch

| Java | Go | 核心差异 |
|------|-----|----------|
| `switch (x) { case 1: break; }` | `switch x { case 1: }` | **默认 break**，无需手动添加 |
| 需 `break` 防止穿透 | 需 `fallthrough` 才穿透 | 行为相反 |
| 仅支持值/枚举/字符串 | `switch { case x > 0: }` | 可替代 if-else 链（无表达式 switch） |

---

## 5. 函数与方法

### 5.1 函数定义

| Java | Go | 核心差异 |
|------|-----|----------|
| `public int add(int a, int b) { return a + b; }` | `func add(a, b int) int { return a + b }` | 参数类型可合并简写 |
| `public void doSomething() { }` | `func doSomething() { }` | 无返回值不写类型 |
| 不支持多返回值 | `func divide(a, b int) (int, error) { }` | **多返回值**是核心特性 |
| 支持方法重载 | **不支持方法重载** | 同包内函数名不能重复 |
| `void varargs(String... args) { }` | `func varargs(args ...string) { }` | 不定参数 |
| 不支持 | `func f() (res int) { res=1; return }` | 命名返回值 |

### 5.2 方法（Receiver）

| Java | Go | 核心差异 |
|------|-----|----------|
| 方法在 class 内部 | `func (p *Person) getName() string { }` | 函数名前加 Receiver |
| 隐式 `this` | 显式 Receiver `p` | Receiver 名称自定义 |
| `person.getName()` | `person.getName()` 或 `person.name` | 直接访问字段或通过方法 |

---

## 6. 面向对象

**⚠️ Go 没有 `class`，没有 `extends`，没有 `implements`**

### 6.1 类与结构体

| Java | Go | 核心差异 |
|------|-----|----------|
| `class Person { private String name; }` | `type Person struct { name string }` | 只有结构体 |
| `public Person(String name) { this.name = name; }` | `func NewPerson(name string) *Person { return &Person{name: name} }` | 用工厂函数代替构造函数 |
| `public class MyClass { }` | `type MyType struct { }` | 大写=Public |
| `private int x;` | `x int` | 小写=Private |
| `protected void method() { }` | **无 protected** | 只有 public 和 private |

### 6.2 继承与组合

| Java | Go | 核心差异 |
|------|-----|----------|
| `class Dog extends Animal { }` | `type Dog struct { Animal }` | **嵌入（Embedding）代替继承** |
| 多态、虚方法 | 组合优于继承 | 直接复用字段和方法 |

### 6.3 接口

| Java | Go | 核心差异 |
|------|-----|----------|
| `interface Runnable { void run(); }` | `type Runnable interface { Run() }` | 方法名首字母大写 |
| `class Dog implements Runnable { }` | **隐式实现接口（鸭子类型）** | 无需 `implements` 关键字 |
| `Animal animal = new Dog();` | `var animal Animal = &Dog{}` | 自动满足接口 |
| 注解：`@Override` | **无注解** | 仅有 Struct Tag 用于元数据 |

### 6.4 泛型

| Java | Go (1.18+) | 说明 |
|------|------------|------|
| `List<String>` | `List[String]` | Go 使用方括号 `[]` |

---

## 7. 错误处理

**⚠️ Go 无 try-catch，采用显式错误处理**

| Java | Go | 核心差异 |
|------|-----|----------|
| `try { } catch (Exception e) { } finally { }` | `if err != nil { }` + `defer` | 通过多返回值返回 `error` |
| `throw new Exception("error");` | `return errors.New("error")` | 返回错误而非抛出 |
| `throws IOException` | `func f() (int, error)` | 错误作为返回值 |
| 资源释放：`finally` | **`defer cleanup()`** | 函数返回前按**后进先出**执行 |
| `throw new RuntimeException()` | `panic("fatal error")` | 导致程序崩溃 |
| `catch` | `defer func() { if r := recover(); r != nil { } }()` | 仅在 defer 中捕获 panic |

---

## 8. 并发编程

**⚠️ Go 的核心优势：原生并发支持**

| Java | Go | 核心差异 |
|------|-----|----------|
| `new Thread(() -> {}).start();` | `go func() { }()` | `go` 关键字启动 Goroutine |
| `ExecutorService executor = Executors.newFixedThreadPool(10);` | 直接使用 goroutine | **无需线程池** |
| `synchronized(lock) { }` | `mutex.Lock(); defer mutex.Unlock()` | 显式加锁 |
| `BlockingQueue<Integer> queue;` | `ch := make(chan int)` | **Channel** 是核心通信机制 |
| `queue.put(1);` | `ch <- 1` | 发送数据 |
| `int value = queue.take();` | `value := <-ch` | 接收数据 |
| `CountDownLatch latch = new CountDownLatch(1);` | `var wg sync.WaitGroup` | 等待组 |
| `Selector` (NIO) | **`select`** | 多路复用 Channel |
| 共享内存（Volatile, Lock） | **Channel** | "通过通信来共享内存" |

---

## 9. 内存管理与指针

### 9.1 内存分配

| Java | Go | 核心差异 |
|------|-----|----------|
| `Person p = new Person();` | `p := &Person{}` 或 `p := new(Person)` | 返回指针 |
| 无指针概念 | `var p *Person` | 显式指针类型 |
| - | `&x` | 取地址 |
| - | `*p` | 解引用 |
| 自动垃圾回收 | 自动垃圾回收 | 都有 GC |
| 无手动内存管理 | 无手动管理（但有 unsafe 包） | - |

### 9.2 值语义与引用语义

| 类型 | Java | Go |
|------|------|-----|
| **基本类型** | 值传递 | 值传递 |
| **对象** | 引用传递 | Struct 是**值传递**（需要指针才是引用） |
| **数组** | 引用传递 | **值传递**（会拷贝） |
| **Slice/Map** | - | 引用传递 |
| **null/nil** | 对象可为 null | 只有指针、接口、Map、Slice、Channel、Function 可为 `nil` |

---

## 10. 集合操作

| Java | Go | 核心差异 |
|------|-----|----------|
| `list.add(item);` | `list = append(list, item)` | **必须赋值回去**（可能扩容） |
| `list.get(0);` | `list[0]` | 直接索引 |
| `list.size();` | `len(list)` | 内置函数 |
| `map.put(key, value);` | `m[key] = value` | 直接赋值 |
| `map.get(key);` | `value := m[key]` 或 `value, ok := m[key]` | 双返回值检查存在 |
| `map.containsKey(key);` | `_, ok := m[key]` | 通过第二返回值 |
| `Collections.sort(list);` | `sort.Ints(list)` 或 `sort.Slice` | 标准库排序 |
| `list.stream().filter(...).collect(...)` | 使用 for 循环 | **无 Stream API** |
| `Optional<String> opt = Optional.of("value");` | 无 Optional | 使用零值或指针 |

---

## 11. 字符串操作

| Java | Go | 包 |
|------|-----|-----|
| `s.length()` | `len(s)` | 内置 |
| `s.substring(0, 5)` | `s[0:5]` | 切片语法 |
| `s.contains("abc")` | `strings.Contains(s, "abc")` | `strings` |
| `s.split(",")` | `strings.Split(s, ",")` | `strings` |
| `String.join(",", list)` | `strings.Join(list, ",")` | `strings` |
| `s.replace("a", "b")` | `strings.Replace(s, "a", "b", -1)` | `strings` |
| `s.trim()` | `strings.TrimSpace(s)` | `strings` |
| `"Hello" + " " + "World"` | `"Hello" + " " + "World"` 或 `fmt.Sprintf` | - |
| `obj.toString()` | `fmt.Sprintf("%v", obj)` | `fmt` |
| `System.out.println("Hello");` | `fmt.Println("Hello")` | `fmt` |

---

## 12. 文件 I/O

| Java | Go | 说明 |
|------|-----|------|
| `new FileReader("file.txt")` | `os.Open("file.txt")` | 打开文件 |
| `new BufferedReader(reader)` | `bufio.NewReader(file)` | 缓冲读取 |
| `try (FileReader fr = ...) { }` | `defer file.Close()` | 资源自动释放 |
| `Files.readAllBytes(path)` | `os.ReadFile("file.txt")` | 读取全部内容 |
| `Files.write(path, bytes)` | `os.WriteFile("file.txt", data, 0644)` | 写入文件 |

---

## 13. 关键差异总结

### 13.1 Go 的 5 大语法"陷阱"

1. **左花括号不能换行**：`func main() \n {` 会编译错误
2. **类型在变量名后面**：`var x int`，不是 `int x`
3. **Error 处理啰嗦**：每次调用都要 `if err != nil`
4. **没有 null 检查对象**：Struct 实例永远不是 `nil`（除非是指针）
5. **切片的 append**：必须赋值回去 `s = append(s, item)`

### 13.2 来自 Java 的思维转换

| Java 思维 | Go 思维 |
|-----------|---------|
| 用继承构建类层次 | **组合优于继承** |
| try-catch 处理异常 | **显式错误处理** |
| 线程池管理并发 | **Goroutine 轻量级并发** |
| 复杂设计模式 | **简单直接的代码** |
| null 需要检查 | **利用零值初始化** |
| 接口臃肿（多方法） | **接口最小化**（1-3 个方法） |
| 共享内存通信 | **通过通信共享内存**（Channel） |

### 13.3 Go 不支持的 Java 特性

- ❌ 类（class）
- ❌ 继承（extends）
- ❌ 方法重载
- ❌ 三元运算符（`? :`）
- ❌ while / do-while 循环
- ❌ try-catch-finally
- ❌ 注解（Annotation）
- ❌ Stream API
- ❌ Optional
- ❌ static 静态导入
- ❌ protected 访问修饰符
- ❌ 构造函数

### 13.4 Go 特有的特性

- ✅ 多返回值
- ✅ defer 延迟执行
- ✅ Goroutine 轻量级协程
- ✅ Channel 通道通信
- ✅ 接口隐式实现
- ✅ 结构体嵌入（组合）
- ✅ 命名返回值
- ✅ select 多路复用
- ✅ iota 常量生成器
- ✅ 切片（Slice）原生支持

---

## 建议

1. **先忘记 OOP**：Go 不是传统面向对象语言
2. **拥抱错误处理**：习惯 `if err != nil` 的模式
3. **学习 Goroutine**：这是 Go 最强大的特性
4. **理解值与指针**：何时用指针，何时用值
5. **接口极简主义**：接口越小越好
6. **利用零值**：不需要显式初始化
7. **代码保持简单**：Go 鼓励直白的代码

--
**记住**：Go 的设计哲学是"少即是多"，不要期望它像 Java 一样功能丰富，而要享受它的简洁和高效！