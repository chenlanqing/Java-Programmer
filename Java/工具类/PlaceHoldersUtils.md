# 占位符

## 1、Spring占位符

主要类；PlaceHolderResolver、PropertyPlaceholderHelper

## 2、StringSubstitutor

这个是 commons-text 包里面的：
```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-text</artifactId>
</dependency>
```

### 2.1、使用SystemProperty

```java
StringSubstitutor.replaceSystemProperties("You are running with java.version = ${java.version} and os.name = ${os.name}.")
```

