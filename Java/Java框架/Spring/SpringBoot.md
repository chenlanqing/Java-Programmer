<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
*目录**

- [一、SpringBoot](#%E4%B8%80springboot)
  - [1、简介](#1%E7%AE%80%E4%BB%8B)
  - [2、SpringBoot项目结构](#2springboot%E9%A1%B9%E7%9B%AE%E7%BB%93%E6%9E%84)
    - [2.1、pom文件](#21pom%E6%96%87%E4%BB%B6)
    - [2.2、启动器](#22%E5%90%AF%E5%8A%A8%E5%99%A8)
    - [2.3、主程序](#23%E4%B8%BB%E7%A8%8B%E5%BA%8F)
- [二、SpringBoot配置](#%E4%BA%8Cspringboot%E9%85%8D%E7%BD%AE)
  - [1、配置文件](#1%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6)
  - [2、YAML语法](#2yaml%E8%AF%AD%E6%B3%95)
  - [3、配置文件值注入](#3%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6%E5%80%BC%E6%B3%A8%E5%85%A5)
    - [3.1、注入方式](#31%E6%B3%A8%E5%85%A5%E6%96%B9%E5%BC%8F)
    - [3.2、@Value获取值和@ConfigurationProperties获取值比较](#32value%E8%8E%B7%E5%8F%96%E5%80%BC%E5%92%8Cconfigurationproperties%E8%8E%B7%E5%8F%96%E5%80%BC%E6%AF%94%E8%BE%83)
    - [3.3、@PropertySource&@ImportResource&@Bean](#33propertysourceimportresourcebean)
  - [4、配置文件占位符](#4%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6%E5%8D%A0%E4%BD%8D%E7%AC%A6)
    - [4.1、随机数](#41%E9%9A%8F%E6%9C%BA%E6%95%B0)
    - [4.2、占位符获取之前配置的值，如果没有可以是用“:”指定默认值](#42%E5%8D%A0%E4%BD%8D%E7%AC%A6%E8%8E%B7%E5%8F%96%E4%B9%8B%E5%89%8D%E9%85%8D%E7%BD%AE%E7%9A%84%E5%80%BC%E5%A6%82%E6%9E%9C%E6%B2%A1%E6%9C%89%E5%8F%AF%E4%BB%A5%E6%98%AF%E7%94%A8%E6%8C%87%E5%AE%9A%E9%BB%98%E8%AE%A4%E5%80%BC)
  - [5、Profile](#5profile)
    - [5.1、多Profile文件](#51%E5%A4%9Aprofile%E6%96%87%E4%BB%B6)
    - [5.2、yml支持多文档块方式](#52yml%E6%94%AF%E6%8C%81%E5%A4%9A%E6%96%87%E6%A1%A3%E5%9D%97%E6%96%B9%E5%BC%8F)
    - [5.3、激活指定profile](#53%E6%BF%80%E6%B4%BB%E6%8C%87%E5%AE%9Aprofile)
  - [6、配置文件加载位置](#6%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6%E5%8A%A0%E8%BD%BD%E4%BD%8D%E7%BD%AE)
  - [7、外部配置加载顺序](#7%E5%A4%96%E9%83%A8%E9%85%8D%E7%BD%AE%E5%8A%A0%E8%BD%BD%E9%A1%BA%E5%BA%8F)
  - [8、自动配置原理](#8%E8%87%AA%E5%8A%A8%E9%85%8D%E7%BD%AE%E5%8E%9F%E7%90%86)
    - [8.1、自动配置原理](#81%E8%87%AA%E5%8A%A8%E9%85%8D%E7%BD%AE%E5%8E%9F%E7%90%86)
    - [8.2、@Conditional派生注解](#82conditional%E6%B4%BE%E7%94%9F%E6%B3%A8%E8%A7%A3)
- [三、](#%E4%B8%89)
- [参考资料](#%E5%8F%82%E8%80%83%E8%B5%84%E6%96%99)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->




# 一、SpringBoot
## 1、简介

简化Spring应用开发的一个框架；

整个Spring技术栈的一个大整合；

J2EE开发的一站式解决方案；

## 2、SpringBoot项目结构
### 2.1、pom文件
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>1.5.9.RELEASE</version>
</parent>
<!-- 其父项目是 -->
<parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-dependencies</artifactId>
  <version>1.5.9.RELEASE</version>
  <relativePath>../../spring-boot-dependencies</relativePath>
</parent>
<!-- 真正管理Spring Boot应用里面的所有依赖版本； -->
```
导入依赖默认是不需要写版本；

### 2.2、启动器
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```
spring-boot-starter：spring-boot场景启动器；导入了web模块正常运行所依赖的组件；

Spring Boot将所有的功能场景都抽取出来，做成一个个的starters（启动器），只需要在项目里面引入这些starter相关场景的所有依赖都会导入进来。要用什么功能就导入什么场景的启动器

### 2.3、主程序
```java
/**
 *  @SpringBootApplication 来标注一个主程序类，说明这是一个Spring Boot应用
 */
@SpringBootApplication
public class HelloWorldMainApplication {
    public static void main(String[] args) {
        // Spring应用启动起来
        SpringApplication.run(HelloWorldMainApplication.class,args);
    }
}
```

@**SpringBootApplication**：Spring Boot应用标注在某个类上说明这个类是SpringBoot的主配置类，SpringBoot就应该运行这个类的main方法来启动SpringBoot应用；

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = {
      @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
      @Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {}
```
- @**SpringBootConfiguration**：Spring Boot的配置类；注在某个类上，表示这是一个Spring Boot的配置类；

- @**Configuration**：配置类上来标注这个注解；配置类 <----->  配置文件；配置类也是容器中的一个组件；@Component

- @**EnableAutoConfiguration**：开启自动配置功能；以前需要配置的东西，SpringBoot自动配置；@**EnableAutoConfiguration** 告诉SpringBoot开启自动配置功能；这样自动配置才能生效；
	```java
	@AutoConfigurationPackage
	@Import(EnableAutoConfigurationImportSelector.class)
	public @interface EnableAutoConfiguration {}
	```
	- @**AutoConfigurationPackage**：自动配置包

	- @**Import**(AutoConfigurationPackages.Registrar.class)：Spring的底层注解@Import，给容器中导入一个组件；导入的组件由AutoConfigurationPackages.Registrar.class；

		将主配置类（@SpringBootApplication标注的类）的所在包及下面所有子包里面的所有组件扫描到Spring容器；

- @**Import**(EnableAutoConfigurationImportSelector.class)：给容器中导入组件

	EnableAutoConfigurationImportSelector 导入哪些组件的选择器；将所有需要导入的组件以全类名的方式返回；这些组件会被添加到容器中；会给容器中导入非常多的自动配置类```（xxxAutoConfiguration）```就是给容器中导入这个场景需要的所有组件，并配置好这些组件；有了自动配置类，免去了我们手动编写配置注入功能组件等的工作；

	```java
	SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class,classLoader)；
	```

	SpringBoot在启动的时候从类路径下的 ```META-INF/spring.factories``` 中获取```EnableAutoConfiguration```指定的值，将这些值作为自动配置类导入到容器中，自动配置类就生效，帮我们进行自动配置工作；以前我们需要自己配置的东西，自动配置类都帮我们；

	**J2EE的整体整合解决方案和自动配置都在spring-boot-autoconfigure-1.5.9.RELEASE.jar；**

# 二、SpringBoot配置

## 1、配置文件

SpringBoot使用一个全局的配置文件，配置文件名是固定的；
- application.properties
- application.yml

配置文件的作用：修改SpringBoot自动配置的默认值；SpringBoot在底层都给我们自动配置好；

## 2、YAML语法

## 3、配置文件值注入

### 3.1、注入方式
有如下配置文件：
```yaml
person:
    lastName: hello
    age: 18
    boss: false
    birth: 2017/12/12
    maps: {k1: v1,k2: 12}
    lists:
      - lisi
      - zhaoliu
    dog:
      name: 小狗
      age: 12
```
其对于JavaBean如下：
```java
/**
 * 将配置文件中配置的每一个属性的值，映射到这个组件中
 * @ConfigurationProperties：告诉SpringBoot将本类中的所有属性和配置文件中相关的配置进行绑定；prefix = "person"：配置文件中哪个下面的所有属性进行一一映射
 * 只有这个组件是容器中的组件，才能容器提供的@ConfigurationProperties功能；
 */
@Component
@ConfigurationProperties(prefix = "person")
public class Person {
    private String lastName;
    private Integer age;
    private Boolean boss;
    private Date birth;

    private Map<String,Object> maps;
    private List<Object> lists;
    private Dog dog;
```
可以导入配置文件处理器，以后编写配置就有提示了
```xml
<!--导入配置文件处理器，配置文件进行绑定就会有提示-->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-configuration-processor</artifactId>
	<optional>true</optional>
</dependency>
```

### 3.2、@Value获取值和@ConfigurationProperties获取值比较
|            | @ConfigurationProperties | @Value |
| ---------- | ------------------------ | ------ |
| 功能         | 批量注入配置文件中的属性             | 一个个指定  |
| 松散绑定（松散语法） | 支持                       | 不支持    |
| SpEL       | 不支持                      | 支持     |
| JSR303数据校验 | 支持                       | 不支持    |
| 复杂类型封装     | 支持                       | 不支持    |

- 配置文件yml还是properties他们都能获取到值；
- 如果说，我们只是在某个业务逻辑中需要获取一下配置文件中的某项值，使用@Value；
- 如果说，我们专门编写了一个javaBean来和配置文件进行映射，我们就直接使用@ConfigurationProperties；

### 3.3、@PropertySource&@ImportResource&@Bean

- @**PropertySource**：加载指定的配置文件；
	```java
	@PropertySource(value = {"classpath:person.properties"})
	@Component
	@ConfigurationProperties(prefix = "person")
	public class Person {}
	```
- @**ImportResource**：导入Spring的配置文件，让配置文件里面的内容生效；
	```java
	@ImportResource(locations = {"classpath:beans.xml"})
	// 导入Spring的配置文件让其生效
	```

SpringBoot推荐给容器中添加组件的方式
- 1、配置类@**Configuration** ------> Spring配置文件
- 2、使用@**Bean**给容器中添加组件
	```java
	/**
	* @Configuration：指明当前类是一个配置类；就是来替代之前的Spring配置文件
	* 在配置文件中用<bean><bean/>标签添加组件
	*/
	@Configuration
	public class MyAppConfig {
		//将方法的返回值添加到容器中；容器中这个组件默认的id就是方法名
		@Bean
		public HelloService helloService02(){
			System.out.println("配置类@Bean给容器中添加组件了...");
			return new HelloService();
		}
	}
	```

## 4、配置文件占位符

### 4.1、随机数
```java
${random.value}、${random.int}、${random.long}
${random.int(10)}、${random.int[1024,65536]}
```

### 4.2、占位符获取之前配置的值，如果没有可以是用“:”指定默认值
```properties
person.last-name=张三${random.uuid}
person.age=${random.int}
person.birth=2017/12/15
person.boss=false
person.maps.k1=v1
person.maps.k2=14
person.lists=a,b,c
# person.hello 是没有配置的，其会使用:后面的hello代替
person.dog.name=${person.hello:hello}_dog
person.dog.age=15
```

## 5、Profile

### 5.1、多Profile文件

在主配置文件编写的时候，文件名可以是   application-{profile}.properties/yml；默认使用application.properties的配置；

### 5.2、yml支持多文档块方式
```yaml
server:
  port: 8081
spring:
  profiles:
    active: prod
---
server:
  port: 8083
spring:
  profiles: dev
---
server:
  port: 8084
spring:
  profiles: prod  #指定属于哪个环境
```

### 5.3、激活指定profile

- 在配置文件中指定：spring.profiles.active=dev
- 命令行：
​	java -jar springboot-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev；
​	可以直接在测试的时候，配置传入命令行参数
- 虚拟机参数：
​	-Dspring.profiles.active=dev

## 6、配置文件加载位置

springboot 启动会扫描以下位置的application.properties或者application.yml文件作为Spring boot的默认配置文件

- file:./config/
- file:./
- classpath:/config/
- classpath:/

优先级由高到底，高优先级的配置会覆盖低优先级的配置；SpringBoot会从这四个位置全部加载主配置文件；**互补配置**；

还可以通过spring.config.location来改变默认的配置文件位置

**项目打包好以后，我们可以使用命令行参数的形式，启动项目的时候来指定配置文件的新位置；指定配置文件和默认加载的这些配置文件共同起作用形成互补配置；**

java -jar springboot-02-0.0.1-SNAPSHOT.jar --spring.config.location=G:/application.properties

## 7、外部配置加载顺序
SpringBoot也可以从以下位置加载配置； 优先级从高到低；高优先级的配置覆盖低优先级的配置，所有的配置会形成互补配置
- 命令行参数：所有的配置都可以在命令行上进行指定：

	java -jar springboot-02-0.0.1-SNAPSHOT.jar --server.port=8087  --server.context-path=/abc

	多个配置用空格分开； --配置项=值
- 来自java:comp/env的JNDI属性
- Java系统属性（System.getProperties()）
- 操作系统环境变量
- RandomValuePropertySource配置的random.*属性值；
- jar包外部的application-{profile}.properties或application.yml(带spring.profile)配置文件；
- jar包内部的application-{profile}.properties或application.yml(带spring.profile)配置文件；
- jar包外部的application.properties或application.yml(不带spring.profile)配置文件；
- jar包内部的application.properties或application.yml(不带spring.profile)配置文件；
- @Configuration注解类上的@PropertySource
- 通过SpringApplication.setDefaultProperties指定的默认属性

**由jar包外向jar包内进行寻找；优先加载带profile，再来加载不带profile**

[官方配置参考](https://docs.spring.io/spring-boot/docs/1.5.9.RELEASE/reference/htmlsingle/#boot-features-external-config)

## 8、自动配置原理

### 8.1、自动配置原理
[配置文件属性参考](https://docs.spring.io/spring-boot/docs/1.5.9.RELEASE/reference/htmlsingle/#common-application-properties)

- （1）SpringBoot启动的时候加载主配置类，开启了自动配置功能@**EnableAutoConfiguration**
- （2）@**EnableAutoConfiguration** 作用：
	- 利用EnableAutoConfigurationImportSelector给容器中导入一些组件；
	- 可以查看selectImports()方法的内容；
	- List<String> configurations = getCandidateConfigurations(annotationMetadata,attributes);获取候选的配置
		```
		SpringFactoriesLoader.loadFactoryNames()
		扫描所有jar包类路径下  META-INF/spring.factories
		把扫描到的这些文件的内容包装成properties对象
		从properties中获取到EnableAutoConfiguration.class类（类名）对应的值，然后把他们添加在容器中
		```
	将类路径下```META-INF/spring.factories```里面配置的所有EnableAutoConfiguration的值加入到了容器中：
	```
	# Auto Configure
	org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
	org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration,\
	org.springframework.boot.autoconfigure.aop.AopAutoConfiguration,\
	org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration,\
	org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration,\
	org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration,\
	org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration,\
	org.springframework.boot.autoconfigure.cloud.CloudAutoConfiguration,\
	```
	每一个这样的  xxxAutoConfiguration类都是容器中的一个组件，都加入到容器中；用他们来做自动配置；
- （3）每一个自动配置类进行自动配置功能；
- （4）以 **HttpEncodingAutoConfiguration**（Http编码自动配置）为例解释自动配置原理；
	```java
	//表示这是一个配置类，以前编写的配置文件一样，也可以给容器中添加组件
	@Configuration
	//启动指定类的ConfigurationProperties功能；将配置文件中对应的值和HttpEncodingProperties绑定起来；并把HttpEncodingProperties加入到ioc容器中
	@EnableConfigurationProperties(HttpEncodingProperties.class)  
	//Spring底层@Conditional注解（Spring注解版），根据不同的条件，如果满足指定的条件，整个配置类里面的配置就会生效；    判断当前应用是否是web应用，如果是，当前配置类生效
	@ConditionalOnWebApplication 
	//判断当前项目有没有这个类CharacterEncodingFilter；SpringMVC中进行乱码解决的过滤器；
	@ConditionalOnClass(CharacterEncodingFilter.class) 
	//判断配置文件中是否存在某个配置  spring.http.encoding.enabled；如果不存在，判断也是成立的，即使我们配置文件中不配置pring.http.encoding.enabled=true，也是默认生效的；
	@ConditionalOnProperty(prefix = "spring.http.encoding", value = "enabled", matchIfMissing = true)  
	public class HttpEncodingAutoConfiguration {
		//已经和SpringBoot的配置文件映射了
		private final HttpEncodingProperties properties;
		//只有一个有参构造器的情况下，参数的值就会从容器中拿
		public HttpEncodingAutoConfiguration(HttpEncodingProperties properties) {
			this.properties = properties;
		}
	
		@Bean   //给容器中添加一个组件，这个组件的某些值需要从properties中获取
		@ConditionalOnMissingBean(CharacterEncodingFilter.class) //判断容器没有这个组件？
		public CharacterEncodingFilter characterEncodingFilter() {
			CharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
			filter.setEncoding(this.properties.getCharset().name());
			filter.setForceRequestEncoding(this.properties.shouldForce(Type.REQUEST));
			filter.setForceResponseEncoding(this.properties.shouldForce(Type.RESPONSE));
			return filter;
		}
	```
	根据当前不同的条件判断，决定这个配置类是否生效。一但这个配置类生效；这个配置类就会给容器中添加各种组件；这些组件的属性是从对应的properties类中获取的，这些类里面的每一个属性又是和配置文件绑定的；
- （5）所有在配置文件中能配置的属性都是在xxxxProperties类中封装者‘；配置文件能配置什么就可以参照某个功能对应的这个属性类
	```java
	@ConfigurationProperties(prefix = "spring.http.encoding")  //从配置文件中获取指定的值和bean的属性进行绑定
	public class HttpEncodingProperties {
		public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	}
	```

**总结几点**

xxxxAutoConfigurartion：自动配置类；给容器中添加组件；xxxxProperties:封装配置文件中相关属性；

### 8.2、@Conditional派生注解

作用：必须是@Conditional指定的条件成立，才给容器中添加组件，配置配里面的所有内容才生效；

| @Conditional扩展注解                | 作用（判断是否满足当前指定条件）               |
| ------------------------------- | ------------------------------ |
| @ConditionalOnJava              | 系统的java版本是否符合要求                |
| @ConditionalOnBean              | 容器中存在指定Bean；                   |
| @ConditionalOnMissingBean       | 容器中不存在指定Bean；                  |
| @ConditionalOnExpression        | 满足SpEL表达式指定                    |
| @ConditionalOnClass             | 系统中有指定的类                       |
| @ConditionalOnMissingClass      | 系统中没有指定的类                      |
| @ConditionalOnSingleCandidate   | 容器中只有一个指定的Bean，或者这个Bean是首选Bean |
| @ConditionalOnProperty          | 系统中指定的属性是否有指定的值                |
| @ConditionalOnResource          | 类路径下是否存在指定资源文件                 |
| @ConditionalOnWebApplication    | 当前是web环境                       |
| @ConditionalOnNotWebApplication | 当前不是web环境                      |
| @ConditionalOnJndi              | JNDI存在指定项                      |

**自动配置类必须在一定的条件下才能生效；**

可以通过启用  debug=true属性；来让控制台打印自动配置报告，这样就可以很方便的知道哪些自动配置类生效；

```
============================
CONDITIONS EVALUATION REPORT
============================

Positive matches:
-----------------

   CodecsAutoConfiguration matched:
      - @ConditionalOnClass found required class 'org.springframework.http.codec.CodecConfigurer'; @ConditionalOnMissingClass did not find unwanted class (OnClassCondition)

   CodecsAutoConfiguration.JacksonCodecConfiguration matched:
      - @ConditionalOnClass found required class 'com.fasterxml.jackson.databind.ObjectMapper'; @ConditionalOnMissingClass did not find unwanted class (OnClassCondition)

   CodecsAutoConfiguration.JacksonCodecConfiguration#jacksonCodecCustomizer matched:
      - @ConditionalOnBean (types: com.fasterxml.jackson.databind.ObjectMapper; SearchStrategy: all) found bean 'jacksonObjectMapper' (OnBeanCondition)
	...
Negative matches:
-----------------

   ActiveMQAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'javax.jms.ConnectionFactory', 'org.apache.activemq.ActiveMQConnectionFactory' (OnClassCondition)

   AopAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.aspectj.lang.annotation.Aspect', 'org.aspectj.lang.reflect.Advice', 'org.aspectj.weaver.AnnotatedElement' (OnClassCondition)
	...
Exclusions:
-----------
    None

Unconditional classes:
----------------------

    org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration

    org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration

    org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration

    org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration

    org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration

```

# 三、


# 参考资料
- [Spring Boot启动流程分析](http://www.cnblogs.com/xinzhao/p/5551828.html)
- [Spring Boot知识清单](https://www.jianshu.com/p/83693d3d0a65)








