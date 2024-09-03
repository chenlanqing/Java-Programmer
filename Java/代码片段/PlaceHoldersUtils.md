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

## 3、工具类

```java
import org.apache.commons.text.StringSubstitutor;
import org.springframework.util.PropertyPlaceholderHelper;
import java.util.Map;
import java.util.Properties;

public class PlaceholderUtil {

    private PlaceholderUtil() {
    }

    private static PropertyPlaceholderHelper helper;

    public static String replacePlaceholders(String text, final Properties properties) {
        return replacePlaceholders(text, "[", "]", ":", properties);
    }
    /**
     * @param text           要替换的文本
     * @param prefix         占位符前缀
     * @param suffix         占位符后缀
     * @param valueSeparator 值分隔符，即占位字符串的默认值，比如：${name:Coco}，表示如果name没有找到，则使用 Coco这个默认值
     * @param properties     需要替换的占位符与值的对应关系
     */
    public static String replacePlaceholders(String text, String prefix, String suffix, String valueSeparator, final Properties properties) {
        if (helper == null) {
            helper = new PropertyPlaceholderHelper(prefix, suffix, valueSeparator, true);
        }
        return helper.replacePlaceholders(text, properties);
    }

    public static String replace(Map<String, Object> valuesMap, String prefix, String suffix, String text) {
        StringSubstitutor substitutor = new StringSubstitutor(valuesMap, prefix, suffix);
        // 设置分隔符，这里是设置默认值的
        substitutor.setValueDelimiter(":");
        String result = substitutor.replace(text);
        substitutor = null;
        return result;
    }
}
```
