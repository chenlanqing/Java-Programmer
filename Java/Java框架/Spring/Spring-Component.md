# 一、JPA

- [基于Java注解的JPA / SQL 语句生成器](https://gitee.com/Levin-Li/simple-dao)
- [SpringBoot-JPA](https://spring.io/guides/gs/accessing-data-jpa/)
- [JPA REDIS无缝整合精细化缓存](https://gitee.com/shujianhui/SpringJPARedis.git)

# 二、Redis

## 1、关于序列化

Spring 提供的 4 种 RedisSerializer（Redis 序列化器）：
- 默认情况下，RedisTemplate 使用 JdkSerializationRedisSerializer，也就是 JDK 序列化，容易产生 Redis 中保存了乱码的错觉；
- 通常考虑到易读性，可以设置 Key 的序列化器为 StringRedisSerializer。但直接使用 RedisSerializer.string()，相当于使用了 UTF_8 编码的 StringRedisSerializer，需要注意字符集问题；
- 如果希望 Value 也是使用 JSON 序列化的话，可以把 Value 序列化器设置为 Jackson2JsonRedisSerializer。默认情况下，不会把类型信息保存在 Value 中，即使我们定义 RedisTemplate 的 Value 泛型为实际类型，查询出的 Value 也只能是 LinkedHashMap 类型。如果希望直接获取真实的数据类型，你可以启用 Jackson ObjectMapper 的 activateDefaultTyping 方法，把类型信息一起序列化保存在 Value 中；
    ```java
    ...
    Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
    ObjectMapper objectMapper = new ObjectMapper();
    //把类型信息作为属性写入Value
    objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
    jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
    ...
    ```
- 如果希望 Value 以 JSON 保存并带上类型信息，更简单的方式是，直接使用 RedisSerializer.json() 快捷方法来获取序列化器
    ```java
    redisTemplate.setKeySerializer(RedisSerializer.string());
    redisTemplate.setValueSerializer(RedisSerializer.json());
    redisTemplate.setHashKeySerializer(RedisSerializer.string());
    redisTemplate.setHashValueSerializer(RedisSerializer.json());
    ```

# 三、Spring-Retry

- [Spring-Retry官方文档](https://docs.spring.io/spring-batch/docs/current/reference/html/retry.html)
- [Spring-Retry原理](https://blog.51cto.com/u_15127644/2880409)

Spring系列的spring-retry是另一个实用程序模块，它可以以标准方式处理任何特定操作的重试。在spring-retry中，所有配置都是基于简单注解的

## 1、基本使用

（1）pom依赖：
```xml
<dependency>
    <groupId>org.springframework.retry</groupId>
    <artifactId>spring-retry</artifactId>
</dependency>
```
（2）启用`@EnableRetry`
```java
@EnableRetry
@SpringBootApplication
public class HelloApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloApplication.class, args);
    }
}
```
（3）在方法上添加`@Retryable`
```java

```

# 四、定时器

- [动态定时任务](https://github.com/caotinging/simple-demo/tree/master/springboot-dynamic-task)

## 配置定时器不执行

在单元测试场景下，为了定时器不执行对应的任务，可以按照如下方式配置：
- 配置定时器的地方如下配置：
```java
@ConditionalOnProperty(
    value = "app.scheduling.enable", havingValue = "true", matchIfMissing = true
)
@Configuration
@EnableScheduling
public static class SchedulingConfiguration {
    ....
}
```
- 然后在单元测试基类中如下配置：
```java
@TestPropertySource(properties = "app.scheduling.enable=false")
@SpringBootTest(XXX.class)
```
[详细参考配置](https://stackoverflow.com/questions/29014496/disable-enablescheduling-on-spring-tests)


# 五、单元测试

## 1、junit5

在SpringBoot2中，其将junit5作为其单元测试组件，junit5和junit4存在很多不同之处

如果需要做些前置或者后置工作，需要增加`@TestInstance`注解
```java
@SpringBootTest(classes = Application.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestController{
    @BeforeAll
    public void setup() {
        // 做前置工作
    }
}
```

# 六、Spring插件

- [Spring插件式开发框架](https://gitee.com/chenlanqing/springboot-plugin-framework-parent)

## 1、spring-boot-maven-plugin

- [插件源码](https://github.com/spring-projects/spring-boot/tree/main/spring-boot-project/spring-boot-tools/spring-boot-maven-plugin)

# 七、Spring-Retry

- [spring-retry source code](https://github.com/spring-projects/spring-retry)
- [guide to spring retry](https://www.baeldung.com/spring-retry)

spring-retry 是从 spring-batch 中剥离出来的一个重试模块

## 1、引入依赖

```xml
<dependency>
    <groupId>org.springframework.retry</groupId>
    <artifactId>spring-retry</artifactId>
    <version>1.3.4</version>
</dependency>
```

## 2、开启重试

在启动类开启重试机制
```java
@EnableRetry
@SpringBootApplication
public class RetryApp {
    public static void main(String[] args) {
        SpringApplication.run(RetryApp.class, args);
    }
}
```

## 3、使用

### 3.1、通过注解@Retryable实现重试

在需要重试的方法上添加`@Retryable`注解
```java
@Retryable(
        maxAttempts = 4,
        backoff = @Backoff(delay = 500L),
        value = {
                RuntimeException.class
        },
        exclude = {
                Throwable.class
        }
)
public void retryMethod() {
    System.out.println(UUID.randomUUID().toString());
    throw new RuntimeException("Failed Request");
}
```

# 八、Spring-Validation

- [使用Spring-Validation校验后端数据](https://lexburner.github.io/spring-validation/)
- [Spring-Validation使用原理](https://www.redoc.top/article/803-Spring%20Validation%E5%8F%82%E6%95%B0%E6%A0%A1%E9%AA%8C%E7%9A%84%E4%BD%BF%E7%94%A8%E4%B8%8E%E5%8E%9F%E7%90%86)
