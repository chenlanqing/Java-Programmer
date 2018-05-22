<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
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
参考资料:
* 白帽子将Web安全

# 一、CSRF

# 二、XSS

# 三、点击劫持

# 四、注入攻击
## 1、SQL注入
### 1.1、如何理解SQL注入

SQL注入是一种将SQL代码添加到输入参数中,传递到服务器解析并执行的一种攻击手法

### 1.2、SQL注入如何产生

- Web开发人员无法保证所有输入都已经过滤;
- 攻击者利用发生给SQL服务器的输入数据构造可执行SQL代码;
- 数据库未做相应的安全配置;

### 1.3、如何寻找SQL注入漏洞
- 借助逻辑推理
    - 识别web应用中所有输入点
    - 了解哪些类型的请求会触发异常
    - 检测服务器响应中的异常
- 盲注
### 1.4、如何进行SQL注入攻击

### 1.5、如何防御SQL注入攻击
- 检查遍历数据类型和格式：只要是有固定格式的遍历,在SQL执行之前,应该严格按照格式去校验,确保是我们预想的格式
- 过滤特殊符号：对于无法确定固定格式的变量,一定要进行特殊符号的过滤或者转义处理
- 绑定变量,使用预编译语句-预防SQL注入的最佳方式
- 对于任何服务器的异常信息,不要暴露给到web端,如:异常堆栈信息等

# 五、DDOS

# 六、文件上传