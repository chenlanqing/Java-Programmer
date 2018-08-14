<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION， INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、CSRF](#%E4%B8%80csrf)
- [二、XSS](#%E4%BA%8Cxss)
- [三、点击劫持](#%E4%B8%89%E7%82%B9%E5%87%BB%E5%8A%AB%E6%8C%81)
- [四、注入攻击](#%E5%9B%9B%E6%B3%A8%E5%85%A5%E6%94%BB%E5%87%BB)
  - [1、SQL注入](#1sql%E6%B3%A8%E5%85%A5)
    - [1.1、如何理解SQL注入](#11%E5%A6%82%E4%BD%95%E7%90%86%E8%A7%A3sql%E6%B3%A8%E5%85%A5)
    - [1.2、SQL注入如何产生](#12sql%E6%B3%A8%E5%85%A5%E5%A6%82%E4%BD%95%E4%BA%A7%E7%94%9F)
    - [1.3、如何寻找SQL注入漏洞](#13%E5%A6%82%E4%BD%95%E5%AF%BB%E6%89%BEsql%E6%B3%A8%E5%85%A5%E6%BC%8F%E6%B4%9E)
    - [1.5、如何防御SQL注入攻击](#15%E5%A6%82%E4%BD%95%E9%98%B2%E5%BE%A1sql%E6%B3%A8%E5%85%A5%E6%94%BB%E5%87%BB)
- [五、DDOS](#%E4%BA%94ddos)
- [六、文件上传](#%E5%85%AD%E6%96%87%E4%BB%B6%E4%B8%8A%E4%BC%A0)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


# 一、CSRF

# 二、XSS

# 三、点击劫持

# 四、注入攻击
## 1、SQL注入
### 1.1、如何理解SQL注入

SQL注入是一种将SQL代码添加到输入参数中，传递到服务器解析并执行的一种攻击手法

### 1.2、SQL注入如何产生

- Web开发人员无法保证所有输入都已经过滤；
- 攻击者利用发生给SQL服务器的输入数据构造可执行SQL代码；
- 数据库未做相应的安全配置；

### 1.3、如何寻找SQL注入漏洞

- 借助逻辑推理
    - 识别web应用中所有输入点
    - 了解哪些类型的请求会触发异常
    - 检测服务器响应中的异常
    
- 盲注

### 1.4、如何进行SQL注入攻击

### 1.5、如何防御SQL注入攻击
- 检查遍历数据类型和格式：只要是有固定格式的遍历，在SQL执行之前，应该严格按照格式去校验，确保是我们预想的格式
- 过滤特殊符号：对于无法确定固定格式的变量，一定要进行特殊符号的过滤或者转义处理
- 绑定变量，使用预编译语句-预防SQL注入的最佳方式
- 对于任何服务器的异常信息，不要暴露给到web端，如:异常堆栈信息等

## 2、操作系统命令注入
Java语言提供了类似Runtime.exec(....)的API，可以用来执行特定命令；

## 3、XML注入攻击

# 五、DDOS

# 六、文件上传

# 七、中间人攻击（MITM）

# 八、Java安全机制
## 1、运行时安全机制
即限制Java运行时的行为，不做越权或者不靠谱的事情
- 在类加载过程中，进行字节码验证，以防止不合规的代码影响JVM运行或者载入其他恶意代码；
- 类加载器本身也可以对代码之间进行隔离；
- 利用SercurityManager机制和相关的组件，限制代码的运行时行为能力，其中，你可以定制policy文件和各种粒度的权限定义，限制代码的作用域和权限；

    在应用实践中，如果对安全要求非常高，建议打开SercurityManager ```-Djava.sercurity.manager```，请注意其开销，通常只要开启Sercurity，就会导致10%~15%性能下降；

- 从原则上来讲，Java的GC等资源回收管理机制，都可以看作是运行时安全的一部分，如果相应机制失效，就会导致JVM出现OOM等错误；

## 2、Java提供的安全框架API
这些API是构建安全通信的基础；
- 加密、解密API；
- 授权、鉴权API；
- 安全通信相关类库，比如基本的HTTPS通信协议相关标准实现；

## 3、JDK集成的各种安全工具
- [keytool](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/keytool.html)，这是个强大的工具，可以管理安全场景中不可或缺的秘钥、证书等，并且可以管理java程序使用的keystore文件；
- [jarsinger](https://docs.oracle.com/javase/9/tools/jarsigner.htm#JSWOR-GUID-925E7A1B-B3F3-44D2-8B49-0B3FA2C54864)，用于对jar文件进行签名或者验证；

## 4、安全漏洞
任何可以用来绕过安全策略限制的程序瑕疵，都可以算作是安全漏洞；

# 参考资料
* 《白帽子讲Web安全》