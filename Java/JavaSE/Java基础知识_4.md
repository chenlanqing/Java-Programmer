# 三十一、Java编码

* [Java中文编码](https://www.ibm.com/developerworks/cn/java/j-lo-chinesecoding/index.html)

## 1、为什么需要编码

- 计算机中存储信息的最小单元是一个字节即 8 个 bit，所以能表示的字符范围是 0~255 个
- 人类要表示的符号太多，无法用一个字节来完全表示
- 要解决这个矛盾必须需要一个新的数据结构 char，从 char 到 byte 必须编码

## 2、编码方式

计算中提拱了多种翻译方式，常见的有 ASCII、ISO-8859-1、GB2312、GBK、UTF-8、UTF-16 等。它们都可以被看作为字典，它们规定了转化的规则，按照这个规则就可以让计算机正确的表示我们的字符；

- ASCII码：总共有 128 个，用一个字节的低 7 位表示，0~31 是控制字符如换行回车删除等；32~126 是打印字符，可以通过键盘输入并且能够显示出来；

- ISO-8859-1： ISO 组织在 ASCII 码基础上又制定了一些列标准用来扩展 ASCII 编码，它们是 `ISO-8859-1~ISO-8859-15`，其中` ISO-8859-1` 涵盖了大多数西欧语言字符，所有应用的最广泛。ISO-8859-1 仍然是单字节编码，它总共能表示 256 个字符；

- GB2312：它的全称是《信息交换用汉字编码字符集 基本集》，它是双字节编码，总的编码范围是 A1-F7，其中从 A1-A9 是符号区，总共包含 682 个符号，从 B0-F7 是汉字区，包含 6763 个汉字；

- GBK：全称叫《汉字内码扩展规范》，是国家技术监督局为 windows95 所制定的新的汉字内码规范，它的出现是为了扩展 GB2312，加入更多的汉字，它的编码范围是 8140~FEFE（去掉 XX7F）总共有 23940 个码位，它能表示 21003 个汉字，它的编码是和 GB2312 兼容的，也就是说用 GB2312 编码的汉字可以用 GBK 来解码，并且不会有乱码；

- GB18030：全称是《信息交换用汉字编码字符集》，是我国的强制标准，它可能是单字节、双字节或者四字节编码，它的编码与 GB2312 编码兼容，这个虽然是国家标准，但是实际应用系统中使用的并不广泛；

- UTF-16：UTF-16 具体定义了 Unicode 字符在计算机中存取方法。UTF-16 用两个字节来表示 Unicode 转化格式，这个是定长的表示方法，不论什么字符都可以用两个字节表示，两个字节是 16 个 bit，所以叫 UTF-16。UTF-16 表示字符非常方便，每两个字节表示一个字符，这个在字符串操作时就大大简化了操作，这也是 Java 以 UTF-16 作为内存的字符存储格式的一个很重要的原因；

- UTF-8：采用了一种变长技术，每个编码区域有不同的字码长度。不同类型的字符可以是由 1~6 个字节组成；UTF-8 有以下编码规则：
    - 如果一个字节，最高位（第 8 位）为 0，表示这是一个 ASCII 字符（00 - 7F）。可见，所有 ASCII 编码已经是 UTF-8 了。
    - 如果一个字节，以 11 开头，连续的 1 的个数暗示这个字符的字节数，例如：110xxxxx 代表它是双字节 UTF-8 字符的首字节。
    - 如果一个字节，以 10 开始，表示它不是首字节，需要向前查找才能得到当前字符的首字节

## 3、Java中需要编码的场景

### 3.1、I/O 操作中存在的编码

涉及到编码的地方一般都在字符到字节或者字节到字符的转换上，而需要这种转换的场景主要是在 I/O 的时候，这个 I/O 包括磁盘 I/O 和网络 I/O

IO中读写是StreamEncoder 类负责将字符编码成字节，编码格式和默认编码规则与解码是一致的，关系如下：

![](image/InputStreamReaderEcode.png)  <br/>  ![](image/OutputStreamWriterEncide.png) 

IO编码示例：涉及到 I/O 操作时只要注意指定统一的编解码 Charset 字符集，一般不会出现乱码问题
```java
String file = "c:/stream.txt";
String charset = "UTF-8";
// 写字符换转成字节流
FileOutputStream outputStream = new FileOutputStream(file);
OutputStreamWriter writer = new OutputStreamWriter(
        outputStream, charset);
try {
    writer.write("这是要保存的中文字符");
} finally {
    writer.close();
}
// 读取字节转换成字符
FileInputStream inputStream = new FileInputStream(file);
InputStreamReader reader = new InputStreamReader(
        inputStream, charset);
StringBuffer buffer = new StringBuffer();
char[] buf = new char[64];
int count = 0;
try {
    while ((count = reader.read(buf)) != -1) {
        buffer.append(buffer, 0, count);
    }
} finally {
    reader.close();
}
```

### 3.2、内存中操作中的编码

Java 中用 String 表示字符串，所以 String 类就提供转换到字节的方法，也支持将字节转换为字符串的构造函数
```java
String s = "这是一段中文字符串"; 
byte[] b = s.getBytes("UTF-8"); 
String n = new String(b,"UTF-8");
```

Charset 提供 encode 与 decode 分别对应 char[] 到 byte[] 的编码和 byte[] 到 char[] 的解码
```java
Charset charset = Charset.forName("UTF-8"); 
ByteBuffer byteBuffer = charset.encode(string); 
CharBuffer charBuffer = charset.decode(byteBuffer);
```
Java 中还有一个 ByteBuffer 类，它提供一种 char 和 byte 之间的软转换，它们之间转换不需要编码与解码，只是把一个 16bit 的 char 格式，拆分成为 2 个 8bit 的 byte 表示，它们的实际值并没有被修改，仅仅是数据的类型做了转换

## 4、Java中如何编解码

Java编码中需要用到的类图

![](image/Java编码类图.png)

首先根据指定的 charsetName 通过 Charset.forName(charsetName) 设置 Charset 类，然后根据 Charset 创建 CharsetEncoder 对象，再调用 CharsetEncoder.encode 对字符串进行编码，不同的编码类型都会对应到一个类中，实际的编码过程是在这些类中完成的；

# 三十二、加密与解密

数据的安全是基于密钥，而不是算法的保密。算法的是公开

## 1、Java安全

### 1.1、网络安全体系

- OSI安全体系

- TCP安全体系

### 1.2、Java安全

**Java安全组成：**
- JCA(Java Cryptography Arctitecture)：消息摘要
- JCE(Java Cryptography Extension)
- JSSE(Java Secure Socket Extension)：SSL
- JAAS(Java Authentication and Authorization Service)

**JDK默认的加解密：**
```
security.provider.1=sun.security.provider.Sun
security.provider.2=sun.security.rsa.SunRsaSign
security.provider.3=sun.security.ec.SunEC
security.provider.4=com.sun.net.ssl.internal.ssl.Provider
security.provider.5=com.sun.crypto.provider.SunJCE
security.provider.6=sun.security.jgss.SunProvider
security.provider.7=com.sun.security.sasl.Provider
security.provider.8=org.jcp.xml.dsig.internal.dom.XMLDSigRI
security.provider.9=sun.security.smartcardio.SunPCSC
security.provider.10=apple.security.AppleProvider
```

**加载自定义的加解密方式：**
- 修改对应的java.security文件，按照上面的方式增加配置
- 通过编码的方式增加：调用Security类的addProviders或者insertProviderAt

**相关的java包和类：**
- java.security：消息摘要
- javax.crypto：安全消息摘要，消息认证码；
- java.net.ssl：安全套接字

**第三方扩展：**
- Bouncy Castle：支持配置和调用
- Commons Codec：Apache、Base64、二进制、十六进制、字符集编码、URL编码/解码

## 2、Base64加密算法

算法实现
- JDK
- Commons codec
- Bouncy Castle

pom引入：
```xml
<dependency>
    <groupId>commons-codec</groupId>
    <artifactId>commons-codec</artifactId>
    <version>1.12</version>
</dependency>
<dependency>
    <groupId>org.bouncycastle</groupId>
    <artifactId>bcprov-jdk15on</artifactId>
    <version>1.62</version>
</dependency>
```
Java代码实现
```java
private static void bouncyCastleBase64(String src){
    byte[] encode = org.bouncycastle.util.encoders.Base64.encode(src.getBytes());
    System.out.println("encoder:" + new String(encode));

    byte[] decode = org.bouncycastle.util.encoders.Base64.decode(encode);
    System.out.println("decoder:" + new String(decode));

}

private static void commonCodecBase64(String src) {
    byte[] bytes = Base64.encodeBase64(src.getBytes());
    System.out.println("encoder:" + new String(bytes));

    byte[] decode = Base64.decodeBase64(bytes);
    System.out.println("decoder:" + new String(decode));

}

private static void jdkBase64(String src) throws IOException {
    BASE64Encoder encoder = new BASE64Encoder();
    String encode = encoder.encode(src.getBytes());

    System.out.println("encoder:" + encode);

    BASE64Decoder decoder = new BASE64Decoder();
    System.out.println("decoder:" + new String(decoder.decodeBuffer(encode)));
}
```

## 3、消息摘要算法

消息摘要算法：验证数据的完整性，数字签名的核心算法

### 3.1、MD

主要是128位摘要算法。MD2、MD4、MD5

单向摘要算法、128位长度

代码实现：
```java
// MD2、MD5JDK有对应实现，MD4是由bouncycastle来实现的
private static void jdkMd5(String src) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] digest = md.digest(src.getBytes());
    System.out.println("JDK MD5: " + Hex.encodeHexString(digest));
}
// JDK使用MD4来获取摘要信息
private static void bcMd4(String src) throws NoSuchAlgorithmException {
    Security.addProvider(new BouncyCastleProvider());
    MessageDigest md = MessageDigest.getInstance("MD4");
    byte[] digest = md.digest(src.getBytes());
    System.out.println("BC MD4: " + Hex.encodeHexString(digest));
}
// 使用bouncycastle来实现MD5摘要算法
private static void bcMd5(String src) {
    Digest digest = new MD5Digest();
    digest.update(src.getBytes(), 0, src.getBytes().length);
    byte[] md5Bytes = new byte[digest.getDigestSize()];
    digest.doFinal(md5Bytes, 0);
    System.out.println("BC MD5: " + org.bouncycastle.util.encoders.Hex.toHexString(md5Bytes));
}
// 使用apache-common-codec实现的摘要算法
public static void ccMD5(String src){
    System.out.println("Apache MD5: " + DigestUtils.md5Hex(src));
}
```

### 3.2、SHA

安全散列算法、固定长度摘要算法：SHA-1、SHA-2(SHA-224、SHA-256、SHA-384、SHA-512)，其中SHA-224是由Bouny Castle来实现的

代码实现
```java
// 使用JDK来实现的sha1
private static void jdkSha1(String src) throws NoSuchAlgorithmException {
    MessageDigest sha = MessageDigest.getInstance("SHA");
    sha.update(src.getBytes());
    System.out.println("jdk SHA-1" + Hex.encodeHexString(sha.digest()));
}
// 使用Bouny Castle来实现的sha1
private static void bcSha1(String src) throws NoSuchAlgorithmException {
    Digest digest = new SHA1Digest();
    digest.update(src.getBytes(), 0 , src.getBytes().length);
    byte[] sha1 = new byte[digest.getDigestSize()];
    digest.doFinal(sha1, 0);
    System.out.println("bc SHA-1" + org.bouncycastle.util.encoders.Hex.toHexString(sha1));
}
```

### 3.3、MAC

MAC、HMAC-带密钥的MAC






