# 1、获取ApplicationContext实例

如果需要获取Spring上下文的时候，先自定义一个存放Application的实体bean：
```java
public class SpringUtils {
    private static ApplicationContext context;
    public static void setContext(ApplicationContext context) {
        SpringUtils.context = context;
    }
    public static ApplicationContext getContext() {
        return SpringUtils.context;
    }
}
```

## 1.1、实现`ApplicationContextInitializer`接口

```java
public class SpringUtilsInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        SpringUtils.setContext(applicationContext);
    }
}
```
需要将这个实现类注入到Spring容器中：
- 在此类上加`@Component`注解；
- 在 `resources/META-INF/spring.factories`文件中添加以下配置：`org.springframework.context.ApplicationContextInitializer=SpringUtilsInitializer`的路径；

## 1.2、实现`ApplicationListener`接口

```java
public class SpringUtilsListener implements ApplicationListener<ApplicationContextEvent> {
    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        SpringUtils.setContext(event.getApplicationContext());
    }
}
```
需要将这个实现类注入到Spring容器中：
- 在此类上加`@Component`注解；
- 在 `resources/META-INF/spring.factories`文件中添加以下配置：`org.springframework.context.ApplicationListener=SpringUtilsListener`的路径；

## 1.3、放在启动类main方法中设置

```java
@SpringBootApplication
public class SpringExampleApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringExampleApplication.class, args);
        SpringUtils.setContext(context);
    }
}
```
因为 `SpringApplication.run` 方法的返回值是 ConfigurableApplicationContext， ConfigurableApplicationContext又继承自 ApplicationContext；

## 1.4、实现`ApplicationContextAware`接口

```java
@Component
public class SpringBeanUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    public  void setApplicationContext(ApplicationContext applicationContext){
        SpringBeanUtils.applicationContext = applicationContext;
    }
}
```