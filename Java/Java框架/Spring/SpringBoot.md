<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、SpringBoot](#%E4%B8%80springboot)
  - [1、简介](#1%E7%AE%80%E4%BB%8B)
  - [2、SpringBoot项目结构](#2springboot%E9%A1%B9%E7%9B%AE%E7%BB%93%E6%9E%84)
- [二、SpringBoot配置](#%E4%BA%8Cspringboot%E9%85%8D%E7%BD%AE)
  - [1、配置文件](#1%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6)
  - [2、YAML语法](#2yaml%E8%AF%AD%E6%B3%95)
  - [3、配置文件值注入](#3%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6%E5%80%BC%E6%B3%A8%E5%85%A5)
  - [4、配置文件占位符](#4%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6%E5%8D%A0%E4%BD%8D%E7%AC%A6)
  - [5、Profile](#5profile)
  - [6、配置文件加载位置](#6%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6%E5%8A%A0%E8%BD%BD%E4%BD%8D%E7%BD%AE)
  - [7、外部配置加载顺序](#7%E5%A4%96%E9%83%A8%E9%85%8D%E7%BD%AE%E5%8A%A0%E8%BD%BD%E9%A1%BA%E5%BA%8F)
  - [8、自动配置原理](#8%E8%87%AA%E5%8A%A8%E9%85%8D%E7%BD%AE%E5%8E%9F%E7%90%86)
- [三、SprinBoot-Web开发](#%E4%B8%89sprinboot-web%E5%BC%80%E5%8F%91)
  - [1、静态资源映射](#1%E9%9D%99%E6%80%81%E8%B5%84%E6%BA%90%E6%98%A0%E5%B0%84)
  - [2、模板引擎](#2%E6%A8%A1%E6%9D%BF%E5%BC%95%E6%93%8E)
  - [3、SpringMVC自动配置](#3springmvc%E8%87%AA%E5%8A%A8%E9%85%8D%E7%BD%AE)
  - [4、SpringBoot国际化](#4springboot%E5%9B%BD%E9%99%85%E5%8C%96)
  - [5、拦截器](#5%E6%8B%A6%E6%88%AA%E5%99%A8)
  - [6、错误处理机制](#6%E9%94%99%E8%AF%AF%E5%A4%84%E7%90%86%E6%9C%BA%E5%88%B6)
  - [7、嵌入式Servlet](#7%E5%B5%8C%E5%85%A5%E5%BC%8Fservlet)
  - [8、使用外置的Servlet容器](#8%E4%BD%BF%E7%94%A8%E5%A4%96%E7%BD%AE%E7%9A%84servlet%E5%AE%B9%E5%99%A8)
- [四、SpringBoot启动配置原理](#%E5%9B%9Bspringboot%E5%90%AF%E5%8A%A8%E9%85%8D%E7%BD%AE%E5%8E%9F%E7%90%86)
- [五、自定义Starter](#%E4%BA%94%E8%87%AA%E5%AE%9A%E4%B9%89starter)
  - [1、自动装配Bean](#1%E8%87%AA%E5%8A%A8%E8%A3%85%E9%85%8Dbean)
  - [2、配置自动装配Bean](#2%E9%85%8D%E7%BD%AE%E8%87%AA%E5%8A%A8%E8%A3%85%E9%85%8Dbean)
  - [3、自动装配顺序](#3%E8%87%AA%E5%8A%A8%E8%A3%85%E9%85%8D%E9%A1%BA%E5%BA%8F)
  - [4、启动器](#4%E5%90%AF%E5%8A%A8%E5%99%A8)
  - [5、实例](#5%E5%AE%9E%E4%BE%8B)
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

 @**Import**(EnableAutoConfigurationImportSelector.class)：给容器中导入组件

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

# 三、SprinBoot-Web开发

## 1、静态资源映射
```java
@ConfigurationProperties(prefix = "spring.resources", ignoreUnknownFields = false)
public class ResourceProperties implements ResourceLoaderAware {
  //可以设置和静态资源有关的参数，缓存时间等
}
```
WebMvcAuotConfiguration
```java
@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
	if (!this.resourceProperties.isAddMappings()) {
		logger.debug("Default resource handling disabled");
		return;
	}
	Integer cachePeriod = this.resourceProperties.getCachePeriod();
	if (!registry.hasMappingForPattern("/webjars/**")) {
		customizeResourceHandlerRegistration(
				registry.addResourceHandler("/webjars/**")
						.addResourceLocations(
								"classpath:/META-INF/resources/webjars/")
				.setCachePeriod(cachePeriod));
	}
	String staticPathPattern = this.mvcProperties.getStaticPathPattern();
	//静态资源文件夹映射
	if (!registry.hasMappingForPattern(staticPathPattern)) {
		customizeResourceHandlerRegistration(
				registry.addResourceHandler(staticPathPattern)
						.addResourceLocations(
								this.resourceProperties.getStaticLocations())
				.setCachePeriod(cachePeriod));
	}
}

//配置欢迎页映射
@Bean
public WelcomePageHandlerMapping welcomePageHandlerMapping(
		ResourceProperties resourceProperties) {
	return new WelcomePageHandlerMapping(resourceProperties.getWelcomePage(),
			this.mvcProperties.getStaticPathPattern());
}

//配置喜欢的图标
@Configuration
@ConditionalOnProperty(value = "spring.mvc.favicon.enabled", matchIfMissing = true)
public static class FaviconConfiguration {

	private final ResourceProperties resourceProperties;

	public FaviconConfiguration(ResourceProperties resourceProperties) {
		this.resourceProperties = resourceProperties;
	}

	@Bean
	public SimpleUrlHandlerMapping faviconHandlerMapping() {
		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		mapping.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
		//所有  **/favicon.ico 
		mapping.setUrlMap(Collections.singletonMap("**/favicon.ico",
				faviconRequestHandler()));
		return mapping;
	}

	@Bean
	public ResourceHttpRequestHandler faviconRequestHandler() {
		ResourceHttpRequestHandler requestHandler = new ResourceHttpRequestHandler();
		requestHandler
				.setLocations(this.resourceProperties.getFaviconLocations());
		return requestHandler;
	}

}
```
- （1）所有 /webjars/** ，都去 classpath:/META-INF/resources/webjars/ 找资源；

	[webjars](http://www.webjars.org/)：以jar包的方式引入静态资源；

	localhost:8080/webjars/jquery/3.3.1/jquery.js
	```xml
		<!--引入jquery-webjar 在访问的时候只需要写webjars下面资源的名称即可  -->
		<dependency>
			<groupId>org.webjars</groupId>
			<artifactId>jquery</artifactId>
			<version>3.3.1</version>
		</dependency>
	```

- （2）"/**" 访问当前项目的任何资源，都去（静态资源的文件夹）找映射
	```
	"classpath:/META-INF/resources/", 
	"classpath:/resources/",
	"classpath:/static/", 
	"classpath:/public/" 
	"/"：当前项目的根路径
	```
	localhost:8080/abc 去静态资源文件夹里面找abc

- （3）欢迎页：静态资源文件夹下的所有index.html页面；被"/**"映射；

- （4）所有的 **/favicon.ico  都是在静态资源文件下找

## 2、模板引擎

### 2.1、引入[thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html)
```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-thymeleaf</artifactId>
	2.1.6
</dependency>
<!-- 切换thymeleaf版本 -->
<properties>
	<thymeleaf.version>3.0.9.RELEASE</thymeleaf.version>
	<!-- 布局功能的支持程序  thymeleaf3主程序  layout2以上版本 -->
	<!-- thymeleaf2   layout1-->
	<thymeleaf-layout-dialect.version>2.2.2</thymeleaf-layout-dialect.version>
</properties>
```

### 2.2、thymeleaf使用
```java
@ConfigurationProperties(prefix = "spring.thymeleaf")
public class ThymeleafProperties {

	private static final Charset DEFAULT_ENCODING = Charset.forName("UTF-8");

	private static final MimeType DEFAULT_CONTENT_TYPE = MimeType.valueOf("text/html");

	public static final String DEFAULT_PREFIX = "classpath:/templates/";

	public static final String DEFAULT_SUFFIX = ".html";
  	//
```
只要我们把HTML页面放在classpath:/templates/，thymeleaf就能自动渲染
- （1）导入thymeleaf的名称空间
	```html
	<html lang="en" xmlns:th="http://www.thymeleaf.org">
	```
- （2）使用thymeleaf语法；

### 2.3、thymeleaf公共页面元素抽取
**2.3.1、基本步骤**

- 抽取公共片段
```html
<div th:fragment="copy">
&copy; 2011 The Good Thymes Virtual Grocery
</div>
```

- 引入公共片段
```html
<div th:insert="~{footer :: copy}"></div>
~{templatename::selector}：模板名::选择器
~{templatename::fragmentname}:模板名::片段名
```
- 默认效果：insert的公共片段在div标签中，如果使用th:insert等属性进行引入，可以不用写~{}；行内写法可以加上：[[~{}]];[(~{})]；

**2.3.2、三种引入公共片段的th属性**
- **th:insert**：将公共片段整个插入到声明引入的元素中
- **th:replace**：将声明引入的元素替换为公共片段
- **th:include**：将被引入的片段的内容包含进这个标签中
```html
<footer th:fragment="copy">
&copy; 2011 The Good Thymes Virtual Grocery
</footer>

<!-- 引入方式 -->
<div th:insert="footer :: copy"></div>
<div th:replace="footer :: copy"></div>
<div th:include="footer :: copy"></div>

<!-- 效果 -->
<div>
    <footer>
    &copy; 2011 The Good Thymes Virtual Grocery
    </footer>
</div>

<footer>
&copy; 2011 The Good Thymes Virtual Grocery
</footer>

<div>
&copy; 2011 The Good Thymes Virtual Grocery
</div>
```
引入片段的时候传入参数，比如动态显示样式
```html
<nav class="col-md-2 d-none d-md-block bg-light sidebar" id="sidebar">
    <div class="sidebar-sticky">
        <ul class="nav flex-column">
            <li class="nav-item">
                <a class="nav-link active"
                   th:class="${activeUri=='main.html'?'nav-link active':'nav-link'}"
                   href="#" th:href="@{/main.html}">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-home">
                        <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path>
                        <polyline points="9 22 9 12 15 12 15 22"></polyline>
                    </svg>
                    Dashboard <span class="sr-only">(current)</span>
                </a>
            </li>

<!--引入侧边栏;传入参数-->
<div th:replace="commons/bar::#sidebar(activeUri='emps')"></div>
```

## 3、SpringMVC自动配置

-[SpringMVC自动配置官方](https://docs.spring.io/spring-boot/docs/1.5.10.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)

### 3.1、Spring MVC auto-configuration
WebMvcAutoConfiguration
- 自动配置了ViewResolver（视图解析器：根据方法的返回值得到视图对象（View），视图对象决定如何渲染（转发？重定向？））；
- ContentNegotiatingViewResolver：组合所有的视图解析器的；
- 如何定制：我们可以自己给容器中添加一个视图解析器；自动的将其组合进来；
- 静态资源文件夹路径,webjars；
- 自动注册了`Converter`, `GenericConverter`, `Formatter` beans
	- Converter：转换器；  public String hello(User user)：类型转换使用Converter；
	- `Formatter`  格式化器；  2017.12.17 -> Date；
		```java
		@Bean
		@ConditionalOnProperty(prefix = "spring.mvc", name = "date-format")//在文件中配置日期格式化的规则
		public Formatter<Date> dateFormatter() {
			return new DateFormatter(this.mvcProperties.getDateFormat());//日期格式化组件
		}	
		```
		自己添加的格式化器转换器，我们只需要放在容器中即可
- HttpMessageConverter：SpringMVC用来转换Http请求和响应的；User---Json；
- `HttpMessageConverters` 是从容器中确定；获取所有的HttpMessageConverter；自己给容器中添加HttpMessageConverter，只需要将自己的组件注册容器中

### 3.2、扩展SpringMVC
```xml
<mvc:view-controller path="/hello" view-name="success"/>
<mvc:interceptors>
	<mvc:interceptor>
		<mvc:mapping path="/hello"/>
		<bean></bean>
	</mvc:interceptor>
</mvc:interceptors>
```
编写一个配置类（@Configuration），是`WebMvcConfigurerAdapter`类型；不能标注`@EnableWebMvc`；既保留了所有的自动配置，也能用我们扩展的配置；
```java
//使用WebMvcConfigurerAdapter可以来扩展SpringMVC的功能
@Configuration
public class MyMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
       // super.addViewControllers(registry);
        //浏览器发送 /bluefish 请求来到 success
        registry.addViewController("/bluefish").setViewName("success");
    }
}
```
**原理：**
- WebMvcAutoConfiguration是SpringMVC的自动配置类；
- 在做其他自动配置时会导入；@Import(**EnableWebMvcConfiguration**.class)
	```java
	@Configuration
	public static class EnableWebMvcConfiguration extends DelegatingWebMvcConfiguration {
      private final WebMvcConfigurerComposite configurers = new WebMvcConfigurerComposite();
	 //从容器中获取所有的WebMvcConfigurer
      @Autowired(required = false)
      public void setConfigurers(List<WebMvcConfigurer> configurers) {
          if (!CollectionUtils.isEmpty(configurers)) {
              this.configurers.addWebMvcConfigurers(configurers);
            	//一个参考实现；将所有的WebMvcConfigurer相关配置都来一起调用；  
            	@Override
             // public void addViewControllers(ViewControllerRegistry registry) {
              //    for (WebMvcConfigurer delegate : this.delegates) {
               //       delegate.addViewControllers(registry);
               //   }
              }
          }
	}
	```
- 容器中所有的WebMvcConfigurer都会一起起作用；
- 自定义配置的类也会被调用：SpringMVC的自动配置和扩展配置都会起作用；

### 3.3、自动配置SpringMVC失效
SpringBoot对SpringMVC的自动配置不需要了，所有都是我们自己配置；所有的SpringMVC的自动配置都失效了，**只需要在配置类中添加@EnableWebMvc即可**

```java
//使用WebMvcConfigurerAdapter可以来扩展SpringMVC的功能
@EnableWebMvc
@Configuration
public class MyMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
       // super.addViewControllers(registry);
        //浏览器发送 /atguigu 请求来到 success
        registry.addViewController("/atguigu").setViewName("success");
    }
}
```
**为什么@EnableWebMvc自动配置就失效了？**
- @EnableWebMvc的核心：
	```java
	@Import(DelegatingWebMvcConfiguration.class)
	public @interface EnableWebMvc {}
	```
	```java
	@Configuration
	public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport {}
	```
- WebMVCAutoConfiguration
	```java
	@Configuration
	@ConditionalOnWebApplication
	@ConditionalOnClass({ Servlet.class, DispatcherServlet.class,
			WebMvcConfigurerAdapter.class })
	//容器中没有这个组件的时候，这个自动配置类才生效
	@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
	@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10)
	@AutoConfigureAfter({ DispatcherServletAutoConfiguration.class,
			ValidationAutoConfiguration.class })
	public class WebMvcAutoConfiguration {}
	```
- @EnableWebMvc将WebMvcConfigurationSupport组件导入进来；
- 导入的WebMvcConfigurationSupport只是SpringMVC最基本的功能；

### 3.4、修改SpringBoot的默认配置
- SpringBoot在自动配置很多组件的时候，先看容器中有没有用户自己配置的（@Bean、@Component）如果有就用用户配置的，如果没有，才自动配置；如果有些组件可以有多个（ViewResolver）将用户配置的和自己默认的组合起来；
- 在SpringBoot中会有非常多的xxxConfigurer帮助我们进行扩展配置；
- 在SpringBoot中会有很多的xxxCustomizer帮助我们进行定制配置；

## 4、SpringBoot国际化
### 4.1、基本步骤
- 编写国际化配置文件
- 使用ResourceBundleMessageSource管理国际化资源文件
- 在页面使用fmt:message取出国际化内容

### 4.2、编写国际化配置文件
![](image/SpringBoot-国际化.png)

### 4.3、SpringBoot自动配置管理国际化资源文件的组件
```java
@ConfigurationProperties(prefix = "spring.messages")
public class MessageSourceAutoConfiguration {
    /**
	 * Comma-separated list of basenames (essentially a fully-qualified classpath
	 * location), each following the ResourceBundle convention with relaxed support for
	 * slash based locations. If it doesn't contain a package qualifier (such as
	 * "org.mypackage"), it will be resolved from the classpath root.
	 */
	private String basename = "messages";  
    //我们的配置文件可以直接放在类路径下叫messages.properties；
    
    @Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		if (StringUtils.hasText(this.basename)) {
            //设置国际化资源文件的基础名（去掉语言国家代码的）
			messageSource.setBasenames(StringUtils.commaDelimitedListToStringArray(
					StringUtils.trimAllWhitespace(this.basename)));
		}
		if (this.encoding != null) {
			messageSource.setDefaultEncoding(this.encoding.name());
		}
		messageSource.setFallbackToSystemLocale(this.fallbackToSystemLocale);
		messageSource.setCacheSeconds(this.cacheSeconds);
		messageSource.setAlwaysUseMessageFormat(this.alwaysUseMessageFormat);
		return messageSource;
	}
```
在application.properties中加入配置，即指定basename
```
spring.messages.basename=i18n.login
```

### 4.4、在页面中使用
如果是thymeleaf模板的话，直接使用：```th:text="#{login.tip}"```

页面根据浏览器设置的语言信息来实现国际化，其springboot实现是有个国际化（Locale-区域信息对象）和LocaleResolver（区域对象解析器）来实现的
```java
@Bean
@ConditionalOnMissingBean
@ConditionalOnProperty(prefix = "spring.mvc", name = "locale")
public LocaleResolver localeResolver() {
	if (this.mvcProperties
			.getLocaleResolver() == WebMvcProperties.LocaleResolver.FIXED) {
		return new FixedLocaleResolver(this.mvcProperties.getLocale());
	}
	AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
	localeResolver.setDefaultLocale(this.mvcProperties.getLocale());
	return localeResolver;
}

// 默认的就是根据请求头带来的区域信息获取Locale进行国际化
// AcceptHeaderLocaleResolver 里面是解析request中的 Accept-Language 来实现的
public class AcceptHeaderLocaleResolver implements LocaleResolver{
	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		Locale defaultLocale = getDefaultLocale();
		if (defaultLocale != null && request.getHeader("Accept-Language") == null) {
			return defaultLocale;
		}
		Locale requestLocale = request.getLocale();
		List<Locale> supportedLocales = getSupportedLocales();
		if (supportedLocales.isEmpty() || supportedLocales.contains(requestLocale)) {
			return requestLocale;
		}
		Locale supportedLocale = findSupportedLocale(request, supportedLocales);
		if (supportedLocale != null) {
			return supportedLocale;
		}
		return (defaultLocale != null ? defaultLocale : requestLocale);
	}
}
```
### 4.5、根据连接来实现切换国际化
```java
/**
 * 可以在连接上携带区域信息
 */
public class MyLocaleResolver implements LocaleResolver {
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String l = request.getParameter("l");
        Locale locale = Locale.getDefault();
        if(!StringUtils.isEmpty(l)){
            String[] split = l.split("_");
            locale = new Locale(split[0],split[1]);
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}

// 在对应的配置类中将 MyLocaleResolver加入到容器中
@Bean
public LocaleResolver localeResolver(){
	return new MyLocaleResolver();
}
```
页面可以进行如下编写：
```html
<a class="btn btn-sm" th:href="@{/index.html(l='zh_CN')}">中文</a>
<a class="btn btn-sm" th:href="@{/index.html(l='en_US')}">English</a>
```

## 5、拦截器
在SpringBoot1.x版本中，静态资源不会被拦截，spring boot 2.x静态资源会被HandlerInterceptor拦截，是因为spring boot 2.x依赖的spring 5.x版本，相对于spring boot 1.5.x依赖的spring 4.3.x版本而言，针对资源的拦截器初始化时有区别，具体源码在WebMvcConfigurationSupport中

spring 4.3.x源码如下
```java
/**
 * Return a handler mapping ordered at Integer.MAX_VALUE-1 with mapped
 * resource handlers. To configure resource handling, override
 * {@link #addResourceHandlers}.
 */
@Bean
public HandlerMapping resourceHandlerMapping() {
    ResourceHandlerRegistry registry = new ResourceHandlerRegistry(this.applicationContext,
				this.servletContext, mvcContentNegotiationManager());
    addResourceHandlers(registry);

    AbstractHandlerMapping handlerMapping = registry.getHandlerMapping();
    if (handlerMapping != null) {
        handlerMapping.setPathMatcher(mvcPathMatcher());
        handlerMapping.setUrlPathHelper(mvcUrlPathHelper());
        // 此处固定添加了一个Interceptor
        handlerMapping.setInterceptors(new ResourceUrlProviderExposingInterceptor(mvcResourceUrlProvider()));
        handlerMapping.setCorsConfigurations(getCorsConfigurations());
		}
    else {
        handlerMapping = new EmptyHandlerMapping();
    }
    return handlerMapping;
}
```

而spring 5.x的源码如下
```java
/**
 * Return a handler mapping ordered at Integer.MAX_VALUE-1 with mapped
 * resource handlers. To configure resource handling, override
 * {@link #addResourceHandlers}.
 */
@Bean
public HandlerMapping resourceHandlerMapping() {
    Assert.state(this.applicationContext != null, "No ApplicationContext set");
    Assert.state(this.servletContext != null, "No ServletContext set");

    ResourceHandlerRegistry registry = new ResourceHandlerRegistry(this.applicationContext,
				this.servletContext, mvcContentNegotiationManager(), mvcUrlPathHelper());
    addResourceHandlers(registry);

    AbstractHandlerMapping handlerMapping = registry.getHandlerMapping();
    if (handlerMapping != null) {
        handlerMapping.setPathMatcher(mvcPathMatcher());
        handlerMapping.setUrlPathHelper(mvcUrlPathHelper());
        // 此处是将所有的HandlerInterceptor都添加了（包含自定义的HandlerInterceptor）
        handlerMapping.setInterceptors(getInterceptors());
        handlerMapping.setCorsConfigurations(getCorsConfigurations());
    }
    else {
        handlerMapping = new EmptyHandlerMapping();
    }
    return handlerMapping;
}
/**
 * Provide access to the shared handler interceptors used to configure
 * {@link HandlerMapping} instances with. This method cannot be overridden,
 * use {@link #addInterceptors(InterceptorRegistry)} instead.
 */
protected final Object[] getInterceptors() {
    if (this.interceptors == null) {
        InterceptorRegistry registry = new InterceptorRegistry();
        // 此处传入新new的registry对象，在配置类当中设置自定义的HandlerInterceptor后即可获取到
        addInterceptors(registry);
        registry.addInterceptor(new ConversionServiceExposingInterceptor(mvcConversionService()));
        registry.addInterceptor(new ResourceUrlProviderExposingInterceptor(mvcResourceUrlProvider()));
        this.interceptors = registry.getInterceptors();
    }
    return this.interceptors.toArray();
}

```

## 6、错误处理机制

### 6.1、SpringBoot默认的错误处理机制
**1、默认效果**
- 浏览器，返回一个默认的错误页面

	![](image/SpringBoot错误页面.png)

	浏览器发送请求的请求头：里面包含```text/html```
	```
	GET /crud/111 HTTP/1.1
	Host: localhost:8080
	Connection: keep-alive
	Cache-Control: max-age=0
	Upgrade-Insecure-Requests: 1
	User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36
	Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
	Accept-Encoding: gzip, deflate, br
	Accept-Language: zh-CN,zh;q=0.9,en;q=0.8
	Cookie: JSESSIONID=A4D817FED29FD908D7530D3D9ED23CBC; _ga=GA1.1.1624944876.1532302839; Idea-d4397549=aacfd2b2-eff8-4dad-ad2b-3e17d81681c4
	```
- 如果是其他客户端，默认响应一个json数据
	```json
	{
		"timestamp": "2018-10-04T01:26:44.051+0000",
		"status": 404,
		"error": "Not Found",
		"message": "No message available",
		"path": "/crud/111"
	}
	```
	其请求头：
	```
	cache-control:"no-cache"
	postman-token:"51992b10-a0af-4499-8445-509736fc46d7"
	user-agent:"PostmanRuntime/7.2.0"
	accept:"*/*"
	host:"localhost:8080"
	accept-encoding:"gzip, deflate"
	```

**2、原理**

参照ErrorMvcAutoConfiguration；错误处理的自动配置；给容器中添加了以下组件
- （1）DefaultErrorAttributes
	```java
	// 帮我们在页面共享信息；
	@Override
	public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes,
			boolean includeStackTrace) {
		Map<String, Object> errorAttributes = new LinkedHashMap<String, Object>();
		errorAttributes.put("timestamp", new Date());
		addStatus(errorAttributes, requestAttributes);
		addErrorDetails(errorAttributes, requestAttributes, includeStackTrace);
		addPath(errorAttributes, requestAttributes);
		return errorAttributes;
	}
	```
- (2）BasicErrorController：处理默认/error请求
	```java
	@Controller
	@RequestMapping("${server.error.path:${error.path:/error}}")
	public class BasicErrorController extends AbstractErrorController {

		@RequestMapping(produces = "text/html")//产生html类型的数据；浏览器发送的请求来到这个方法处理
		public ModelAndView errorHtml(HttpServletRequest request,
				HttpServletResponse response) {
			HttpStatus status = getStatus(request);
			Map<String, Object> model = Collections.unmodifiableMap(getErrorAttributes(
					request, isIncludeStackTrace(request, MediaType.TEXT_HTML)));
			response.setStatus(status.value());
			
			//去哪个页面作为错误页面；包含页面地址和页面内容
			ModelAndView modelAndView = resolveErrorView(request, response, status, model);
			return (modelAndView == null ? new ModelAndView("error", model) : modelAndView);
		}

		@RequestMapping
		@ResponseBody    //产生json数据，其他客户端来到这个方法处理；
		public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
			Map<String, Object> body = getErrorAttributes(request,
					isIncludeStackTrace(request, MediaType.ALL));
			HttpStatus status = getStatus(request);
			return new ResponseEntity<Map<String, Object>>(body, status);
		}
	}
	```
- （3）ErrorPageCustomizer
	```java
	@Value("${error.path:/error}")
	private String path = "/error";  // 系统出现错误以后来到error请求进行处理；（web.xml注册的错误页面规则）
	```

- （4）DefaultErrorViewResolver
	```java
	@Override
	public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status,
			Map<String, Object> model) {
		ModelAndView modelAndView = resolve(String.valueOf(status), model);
		if (modelAndView == null && SERIES_VIEWS.containsKey(status.series())) {
			modelAndView = resolve(SERIES_VIEWS.get(status.series()), model);
		}
		return modelAndView;
	}

	private ModelAndView resolve(String viewName, Map<String, Object> model) {
        //默认SpringBoot可以去找到一个页面？  error/404
		String errorViewName = "error/" + viewName;
        
        //模板引擎可以解析这个页面地址就用模板引擎解析
		TemplateAvailabilityProvider provider = this.templateAvailabilityProviders
				.getProvider(errorViewName, this.applicationContext);
		if (provider != null) {
            //模板引擎可用的情况下返回到errorViewName指定的视图地址
			return new ModelAndView(errorViewName, model);
		}
        //模板引擎不可用，就在静态资源文件夹下找errorViewName对应的页面   error/404.html
		return resolveResource(errorViewName, model);
	}

	```	

**步骤：**

一但系统出现4xx或者5xx之类的错误；ErrorPageCustomizer就会生效（定制错误的响应规则）；就会来到/error请求；就会被**BasicErrorController**处理；

响应页面；去哪个页面是由**DefaultErrorViewResolver**解析得到的；
```java
protected ModelAndView resolveErrorView(HttpServletRequest request,
      HttpServletResponse response, HttpStatus status, Map<String, Object> model) {
    //所有的ErrorViewResolver得到ModelAndView
   for (ErrorViewResolver resolver : this.errorViewResolvers) {
      ModelAndView modelAndView = resolver.resolveErrorView(request, status, model);
      if (modelAndView != null) {
         return modelAndView;
      }
   }
   return null;
}
```

### 6.2、定制错误响应
**6.2.1、定制错误页面**

- 有模板引擎的情况下；error/状态码;【将错误页面命名为  错误状态码.html 放在模板引擎文件夹里面的 error文件夹下】，发生此状态码的错误就会来到对应的页面；可以使用4xx和5xx作为错误页面的文件名来匹配这种类型的所有错误，精确优先（优先寻找精确的状态码.html）；页面能获取的信息：
	```
	​timestamp：时间戳
	status：状态码
	error：错误提示
	exception：异常对象
	message：异常消息
	errors：JSR303数据校验的错误都在这里
	```
- 没有模板引擎（模板引擎找不到这个错误页面），静态资源文件夹下找；
- 以上都没有错误页面，就是默认来到SpringBoot默认的错误提示页面；

**6.2.2、如何定制错误的json数据**

- 自定义异常处理&返回定制json数据；
	```java
	@ControllerAdvice
	public class MyExceptionHandler {

		@ResponseBody
		@ExceptionHandler(UserNotExistException.class)
		public Map<String,Object> handleException(Exception e){
			Map<String,Object> map = new HashMap<>();
			map.put("code","user.notexist");
			map.put("message",e.getMessage());
			return map;
		}
	}
	//没有自适应效果...，其使用户客户端和浏览器访问的效果是一样的，都是返回json数据
	```
- 转发到/error进行自适应响应效果处理
	```java
	@ExceptionHandler(UserNotExistException.class)
    public String handleException(Exception e, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        //传入我们自己的错误状态码  4xx 5xx，否则就不会进入定制错误页面的解析流程
        /**
         * Integer statusCode = (Integer) request
         .getAttribute("javax.servlet.error.status_code");
         */
        request.setAttribute("javax.servlet.error.status_code",500);
        map.put("code","user.notexist");
		map.put("message",e.getMessage());
		
		request.setAttribute("ext",map);
        //转发到/error
        return "forward:/error";
    }
	```

**6.2.3、将定制数据返回到客户端**

出现错误以后，会来到/error请求，会被`BasicErrorController`处理，响应出去可以获取的数据是由`getErrorAttributes`得到的（是`AbstractErrorController（ErrorController）`规定的方法）；

- 完全来编写一个`ErrorController`的实现类【或者是编写`AbstractErrorController的子类`】，放在容器中；但是实现会比较复杂
- 页面上能用的数据，或者是json返回能用的数据都是通过 `errorAttributes.getErrorAttributes` 得到；容器中 `DefaultErrorAttributes.getErrorAttributes()；` 默认进行数据处理的；因此可以自定义`ErrorAttributes`
	```java
	// 给容器中加入我们自己定义的ErrorAttributes
	@Component
	public class MyErrorAttributes extends DefaultErrorAttributes {

		@Override
		public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
			Map<String, Object> map = super.getErrorAttributes(requestAttributes, includeStackTrace);
			map.put("company","atguigu");
			//异常处理器携带的数据
			Map<String,Object> ext = (Map<String, Object>) requestAttributes.getAttribute("ext", 0);
			map.put("ext",ext);
			return map;
		}
	}
	```

## 7、嵌入式Servlet
SpringBoot默认使用Tomcat作为嵌入式的Servlet容器

![](image/SpringBoot-Tomcat依赖.png)

### 7.1、定制和修改Servlet容器的相关配置
- 修改和server有关的配置（ServerProperties）
	```properties
	server.port=8081
	server.context-path=/crud

	server.tomcat.uri-encoding=UTF-8

	//通用的Servlet容器设置
	server.xxx
	//Tomcat的设置
	server.tomcat.xxx
	```
- 编写一个`EmbeddedServletContainerCustomizer`：嵌入式的Servlet容器的定制器；来修改Servlet容器的配置
	```java
	@Bean  //一定要将这个定制器加入到容器中
	public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer(){
		return new EmbeddedServletContainerCustomizer() {

			//定制嵌入式的Servlet容器相关的规则
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				container.setPort(8083);
			}
		};
	}
	```
	***注意：***

	EmbeddedServletContainerCustomizer在SpringBoot2.x之后的版本废弃了，可以使用如下来实现：
	具体可参考文章：https://segmentfault.com/a/1190000014610478
	```java
	@Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer(){
        return new WebServerFactoryCustomizer<ConfigurableWebServerFactory>() {
            @Override
            public void customize(ConfigurableWebServerFactory factory) {
                factory.setPort(8088);
            }
        };
    }
	```
### 7.2、注册Servlet三大组件：Servlet、Filter、Listener
由于SpringBoot默认是以jar包的方式启动嵌入式的Servlet容器来启动SpringBoot的web应用，没有web.xml文件；

注册三大组件用以下方式：ServletRegistrationBean、FilterRegistrationBean、ServletListenerRegistrationBean，相关web.xml配置都可以再这三大组件中进行设置
```java
// 注册serlvet
@Bean
public ServletRegistrationBean myServlet(){
	ServletRegistrationBean registrationBean = new ServletRegistrationBean(new MyServlet(),"/myServlet");
	return registrationBean;
}

// 注册filter
@Bean
public FilterRegistrationBean myFilter(){
	FilterRegistrationBean registrationBean = new FilterRegistrationBean();
	registrationBean.setFilter(new MyFilter());
	registrationBean.setUrlPatterns(Arrays.asList("/hello","/myServlet"));
	return registrationBean;
}

// 注册listener
@Bean
public ServletListenerRegistrationBean myListener(){
	ServletListenerRegistrationBean<MyListener> registrationBean = new ServletListenerRegistrationBean<>(new MyListener());
	return registrationBean;
}
```

SpringBoot在自动配置SpringMVC的时候，自动的注册SpringMVC的前端控制器；DIspatcherServlet；```DispatcherServletAutoConfiguration```中
```java
@Configuration
@Conditional(DispatcherServletRegistrationCondition.class)
@ConditionalOnClass(ServletRegistration.class)
@EnableConfigurationProperties(WebMvcProperties.class)
@Import(DispatcherServletConfiguration.class)
protected static class DispatcherServletRegistrationConfiguration {
	@Bean(name = DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME)
	@ConditionalOnBean(value = DispatcherServlet.class, name = DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
	public ServletRegistrationBean dispatcherServletRegistration(
		DispatcherServlet dispatcherServlet) {
	ServletRegistrationBean registration = new ServletRegistrationBean(
			dispatcherServlet, this.serverProperties.getServletMapping());
		//默认拦截： /  所有请求；包静态资源，但是不拦截jsp请求；   /*会拦截jsp
		//可以通过server.servletPath来修改SpringMVC前端控制器默认拦截的请求路径
	registration.setName(DEFAULT_DISPATCHER_SERVLET_BEAN_NAME);
	registration.setLoadOnStartup(
			this.webMvcProperties.getServlet().getLoadOnStartup());
	if (this.multipartConfig != null) {
		registration.setMultipartConfig(this.multipartConfig);
	}
	return registration;
	}
}
```

### 7.3、替换为其他嵌入式Servlet容器
依赖关系

- 1.5.X

	![](image/SpringBoot-1.5版本.png)

- 2.X

	![](image/SpringBoot-2.0版本.png)

默认支持：
- Tomcat（默认使用）
	```xml
	<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
	<!-- 引入web模块默认就是使用嵌入式的Tomcat作为Servlet容器； -->
	</dependency>
	```

- Jetty
	```xml
	<!-- 引入web模块 -->
	<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
	<exclusions>
		<exclusion>
			<artifactId>spring-boot-starter-tomcat</artifactId>
			<groupId>org.springframework.boot</groupId>
		</exclusion>
	</exclusions>
	</dependency>

	<!--引入其他的Servlet容器-->
	<dependency>
	<artifactId>spring-boot-starter-jetty</artifactId>
	<groupId>org.springframework.boot</groupId>
	</dependency>
	```

- Undertow（不支持JSP）
	```xml
	<!-- 引入web模块 -->
	<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
	<exclusions>
		<exclusion>
			<artifactId>spring-boot-starter-tomcat</artifactId>
			<groupId>org.springframework.boot</groupId>
		</exclusion>
	</exclusions>
	</dependency>

	<!--引入其他的Servlet容器-->
	<dependency>
	<artifactId>spring-boot-starter-undertow</artifactId>
	<groupId>org.springframework.boot</groupId>
	</dependency>
	```

### 7.4、嵌入式Servlet容器自动配置原理

#### 7.4.1、SpringBoot-1.5.x版本
EmbeddedServletContainerAutoConfiguration：嵌入式的Servlet容器自动配置
```java
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@ConditionalOnWebApplication
@Import(BeanPostProcessorsRegistrar.class)
//导入BeanPostProcessorsRegistrar：Spring注解版；给容器中导入一些组件
//导入了EmbeddedServletContainerCustomizerBeanPostProcessor：
//后置处理器：bean初始化前后（创建完对象，还没赋值赋值）执行初始化工作
public class EmbeddedServletContainerAutoConfiguration {
	// 构建tomcat容器
    @Configuration
	@ConditionalOnClass({ Servlet.class, Tomcat.class })//判断当前是否引入了Tomcat依赖；
	//判断当前容器没有用户自己定义EmbeddedServletContainerFactory：嵌入式的Servlet容器工厂；作用：创建嵌入式的Servlet容器
	@ConditionalOnMissingBean(value = EmbeddedServletContainerFactory.class, search = SearchStrategy.CURRENT)
	public static class EmbeddedTomcat {
		@Bean
		public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
			return new TomcatEmbeddedServletContainerFactory();
		}
	}
	// 构建jetty容器
	@Configuration
	@ConditionalOnClass({ Servlet.class, Server.class, Loader.class,WebAppContext.class })
	@ConditionalOnMissingBean(value = EmbeddedServletContainerFactory.class, search = SearchStrategy.CURRENT)
	public static class EmbeddedJetty {
		@Bean
		public JettyEmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory() {
			return new JettyEmbeddedServletContainerFactory();
		}
	}
	// 构建Undertow容器
	@Configuration
	@ConditionalOnClass({ Servlet.class, Undertow.class, SslClientAuthMode.class })
	@ConditionalOnMissingBean(value = EmbeddedServletContainerFactory.class, search = SearchStrategy.CURRENT)
	public static class EmbeddedUndertow {
		@Bean
		public UndertowEmbeddedServletContainerFactory undertowEmbeddedServletContainerFactory() {
			return new UndertowEmbeddedServletContainerFactory();
		}
	}
```
- （1）EmbeddedServletContainerFactory-嵌入式Servlet容器工厂
	```java
	public interface EmbeddedServletContainerFactory {
		EmbeddedServletContainer getEmbeddedServletContainer(ServletContextInitializer... initializers);
	}
	```
	![](image/EmbeddedServletContainerFactory实现类.png)

- （2）EmbeddedServletContainer：（嵌入式的Servlet容器）

	![](image/EmbeddedServletContainer实现类.png)

- （3）以`TomcatEmbeddedServletContainerFactory`为例
	```java
	@Override
	public EmbeddedServletContainer getEmbeddedServletContainer(ServletContextInitializer... initializers) {
		//创建一个Tomcat
		Tomcat tomcat = new Tomcat();
		//配置Tomcat的基本环节
		File baseDir = (this.baseDirectory != null ? this.baseDirectory : createTempDir("tomcat"));
		tomcat.setBaseDir(baseDir.getAbsolutePath());
		Connector connector = new Connector(this.protocol);
		tomcat.getService().addConnector(connector);
		customizeConnector(connector);
		tomcat.setConnector(connector);
		tomcat.getHost().setAutoDeploy(false);
		configureEngine(tomcat.getEngine());
		for (Connector additionalConnector : this.additionalTomcatConnectors) {
			tomcat.getService().addConnector(additionalConnector);
		}
		prepareContext(tomcat.getHost(), initializers);
			
		//将配置好的Tomcat传入进去，返回一个EmbeddedServletContainer；并且启动Tomcat服务器
		return getTomcatEmbeddedServletContainer(tomcat);
	}
	// getTomcatEmbeddedServletContainer 获取tomcat容器
	protected TomcatEmbeddedServletContainer getTomcatEmbeddedServletContainer(Tomcat tomcat) {
		return new TomcatEmbeddedServletContainer(tomcat, getPort() >= 0);
	}

	// TomcatEmbeddedServletContainer 构造方法，初始化
	public TomcatEmbeddedServletContainer(Tomcat tomcat, boolean autoStart) {
		Assert.notNull(tomcat, "Tomcat Server must not be null");
		this.tomcat = tomcat;
		this.autoStart = autoStart;
		initialize();
	}

	// this.tomcat.start() 真正启动tomcat
	private void initialize() throws EmbeddedServletContainerException {
		TomcatEmbeddedServletContainer.logger.info("Tomcat initialized with port(s): " + getPortsDescription(false));
		synchronized (this.monitor) {
			try {
				addInstanceIdToEngineName();
				try {
					// Remove service connectors to that protocol binding doesn't happen
					// yet
					removeServiceConnectors();

					// Start the server to trigger initialization listeners
					this.tomcat.start();

					// We can re-throw failure exception directly in the main thread
					rethrowDeferredStartupExceptions();

					Context context = findContext();
					try {
						ContextBindings.bindClassLoader(context, getNamingToken(context),
								getClass().getClassLoader());
					}
					catch (NamingException ex) {
						// Naming is not enabled. Continue
					}

					// Unlike Jetty, all Tomcat threads are daemon threads. We create a
					// blocking non-daemon to stop immediate shutdown
					startDaemonAwaitThread();
				}
				catch (Exception ex) {
					containerCounter.decrementAndGet();
					throw ex;
				}
			}
			catch (Exception ex) {
				throw new EmbeddedServletContainerException(
						"Unable to start embedded Tomcat", ex);
			}
		}
	}
	```
- （4）对嵌入式容器的配置修改是怎么生效
	ServerProperties、EmbeddedServletContainerCustomizer，定制器帮我们修改了Servlet容器的配置

- （5）配置修改如何生效？容器中导入了`EmbeddedServletContainerCustomizerBeanPostProcessor`

	在`EmbeddedServletContainerAutoConfiguration`声明中有个注解：`@Import(BeanPostProcessorsRegistrar.class)`
	```java
		//初始化之前
		@Override
		public Object postProcessBeforeInitialization(Object bean, String beanName)throws BeansException {
			//如果当前初始化的是一个ConfigurableEmbeddedServletContainer类型的组件
			if (bean instanceof ConfigurableEmbeddedServletContainer) {
				postProcessBeforeInitialization((ConfigurableEmbeddedServletContainer) bean);
			}
			return bean;
		}
		private void postProcessBeforeInitialization(ConfigurableEmbeddedServletContainer bean) {
			//获取所有的定制器，调用每一个定制器的customize方法来给Servlet容器进行属性赋值；
			for (EmbeddedServletContainerCustomizer customizer : getCustomizers()) {
				customizer.customize(bean);
			}
		}

		private Collection<EmbeddedServletContainerCustomizer> getCustomizers() {
			if (this.customizers == null) {
				// Look up does not include the parent context
				this.customizers = new ArrayList<EmbeddedServletContainerCustomizer>(
					this.beanFactory
					//从容器中获取所有这葛类型的组件：EmbeddedServletContainerCustomizer
					//定制Servlet容器，给容器中可以添加一个EmbeddedServletContainerCustomizer类型的组件
					.getBeansOfType(EmbeddedServletContainerCustomizer.class,false, false)
					.values());
				Collections.sort(this.customizers, AnnotationAwareOrderComparator.INSTANCE);
				this.customizers = Collections.unmodifiableList(this.customizers);
			}
			return this.customizers;
		}
		// ServerProperties也是定制器
	```

**详细步骤：**
- （1）SpringBoot根据导入的依赖情况，给容器中添加相应的EmbeddedServletContainerFactory【默认是：TomcatEmbeddedServletContainerFactory】；
- （2）容器中某个组件要创建对象就会惊动后置处理器：EmbeddedServletContainerCustomizerBeanPostProcessor；只要是嵌入式的Servlet容器工厂，后置处理器就工作；
- （3）后置处理器，从容器中获取所有的**EmbeddedServletContainerCustomizer**，调用定制器的定制方法

#### 7.4.2、SpringBoot-2.x版本
- （1）ServletWebServerFactoryAutoConfiguration
```java
// 自动配置类
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnClass(ServletRequest.class)
@ConditionalOnWebApplication(type = Type.SERVLET)
@EnableConfigurationProperties(ServerProperties.class)
// 导入不同的容器，其都是ServletWebServerFactoryConfiguration的静态内部类
@Import({ ServletWebServerFactoryAutoConfiguration.BeanPostProcessorsRegistrar.class,
		ServletWebServerFactoryConfiguration.EmbeddedTomcat.class,
		ServletWebServerFactoryConfiguration.EmbeddedJetty.class,
		ServletWebServerFactoryConfiguration.EmbeddedUndertow.class })
public class ServletWebServerFactoryAutoConfiguration {
	@Bean
	public ServletWebServerFactoryCustomizer servletWebServerFactoryCustomizer(
			ServerProperties serverProperties) {
		return new ServletWebServerFactoryCustomizer(serverProperties);
	}
	@Bean
	@ConditionalOnClass(name = "org.apache.catalina.startup.Tomcat")
	public TomcatServletWebServerFactoryCustomizer tomcatServletWebServerFactoryCustomizer(ServerProperties serverProperties) {
		return new TomcatServletWebServerFactoryCustomizer(serverProperties);
	}
	/**
	 * Registers a {@link WebServerFactoryCustomizerBeanPostProcessor}. Registered via
	 * {@link ImportBeanDefinitionRegistrar} for early registration.
	 */
	public static class BeanPostProcessorsRegistrar
			implements ImportBeanDefinitionRegistrar, BeanFactoryAware {
		private ConfigurableListableBeanFactory beanFactory;
		@Override
		public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
			if (beanFactory instanceof ConfigurableListableBeanFactory) {
				this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
			}
		}

		@Override
		public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
				BeanDefinitionRegistry registry) {
			if (this.beanFactory == null) {
				return;
			}
			registerSyntheticBeanIfMissing(registry,
					"webServerFactoryCustomizerBeanPostProcessor",
					WebServerFactoryCustomizerBeanPostProcessor.class);
			registerSyntheticBeanIfMissing(registry,
					"errorPageRegistrarBeanPostProcessor",
					ErrorPageRegistrarBeanPostProcessor.class);
		}

		private void registerSyntheticBeanIfMissing(BeanDefinitionRegistry registry,
				String name, Class<?> beanClass) {
			if (ObjectUtils.isEmpty(
					this.beanFactory.getBeanNamesForType(beanClass, true, false))) {
				RootBeanDefinition beanDefinition = new RootBeanDefinition(beanClass);
				beanDefinition.setSynthetic(true);
				registry.registerBeanDefinition(name, beanDefinition);
			}
		}

	}

}
```

- （2）ServletWebServerFactoryConfiguration-工厂配置类
```java
@Configuration
class ServletWebServerFactoryConfiguration {
	// tomcat
	@Configuration
	@ConditionalOnClass({ Servlet.class, Tomcat.class, UpgradeProtocol.class })
	@ConditionalOnMissingBean(value = ServletWebServerFactory.class, search = SearchStrategy.CURRENT)
	public static class EmbeddedTomcat {
		@Bean
		public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
			return new TomcatServletWebServerFactory();
		}
	}

	// jetty
	@Configuration
	@ConditionalOnClass({ Servlet.class, Server.class, Loader.class,WebAppContext.class })
	@ConditionalOnMissingBean(value = ServletWebServerFactory.class, search = SearchStrategy.CURRENT)
	public static class EmbeddedJetty {

		@Bean
		public JettyServletWebServerFactory JettyServletWebServerFactory() {
			return new JettyServletWebServerFactory();
		}

	}

	// Undertow
	@Configuration
	@ConditionalOnClass({ Servlet.class, Undertow.class, SslClientAuthMode.class })
	@ConditionalOnMissingBean(value = ServletWebServerFactory.class, search = SearchStrategy.CURRENT)
	public static class EmbeddedUndertow {
		@Bean
		public UndertowServletWebServerFactory undertowServletWebServerFactory() {
			return new UndertowServletWebServerFactory();
		}
	}
}
```
- （3）ServletWebServerFactory：servelet工厂
	```java
		@FunctionalInterface
		public interface ServletWebServerFactory {
			WebServer getWebServer(ServletContextInitializer... initializers);
		}
	```
	![](image/ServletWebServerFactory实现类.png)

- （4）WebServer
	```java
	public interface WebServer {
		void start() throws WebServerException;
		void stop() throws WebServerException;
		int getPort();
	}
	```
	![](image/WebServer实现类.png)

- （5）配置修改默认是通过`TomcatServletWebServerFactoryCustomizer`来实现定制的

### 7.5、嵌入式Servlet容器启动原理
什么时候创建嵌入式的Servlet容器工厂？什么时候获取嵌入式的Servlet容器并启动Tomcat；

**获取嵌入式的Servlet容器工**
- （1）SpringBoot应用启动运行run方法；
- （2）refreshContext(context);SpringBoot刷新IOC容器【创建IOC容器对象，并初始化容器，创建容器中的每一个组件】；如果是web应用创建`AnnotationConfigEmbeddedWebApplicationContext`，否则：`AnnotationConfigApplicationContext**`
- （3）refresh(context); **刷新刚才创建好的ioc容器；**
	```java
	public void refresh() throws BeansException, IllegalStateException {
		synchronized (this.startupShutdownMonitor) {
			// Prepare this context for refreshing.
			prepareRefresh();

			// Tell the subclass to refresh the internal bean factory.
			ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

			// Prepare the bean factory for use in this context.
			prepareBeanFactory(beanFactory);

			try {
				// Allows post-processing of the bean factory in context subclasses.
				postProcessBeanFactory(beanFactory);

				// Invoke factory processors registered as beans in the context.
				invokeBeanFactoryPostProcessors(beanFactory);

				// Register bean processors that intercept bean creation.
				registerBeanPostProcessors(beanFactory);

				// Initialize message source for this context.
				initMessageSource();

				// Initialize event multicaster for this context.
				initApplicationEventMulticaster();

				// Initialize other special beans in specific context subclasses.
				onRefresh();

				// Check for listener beans and register them.
				registerListeners();

				// Instantiate all remaining (non-lazy-init) singletons.
				finishBeanFactoryInitialization(beanFactory);

				// Last step: publish corresponding event.
				finishRefresh();
			}

			catch (BeansException ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("Exception encountered during context initialization - " +
						"cancelling refresh attempt: " + ex);
				}

				// Destroy already created singletons to avoid dangling resources.
				destroyBeans();

				// Reset 'active' flag.
				cancelRefresh(ex);

				// Propagate exception to caller.
				throw ex;
			}

			finally {
				// Reset common introspection caches in Spring's core, since we
				// might not ever need metadata for singleton beans anymore...
				resetCommonCaches();
			}
		}
	}	
	```
- （4）onRefresh(); web的ioc容器重写了onRefresh方法
- （5）webioc容器会创建嵌入式的Servlet容器；**createEmbeddedServletContainer**();
- （6）获取嵌入式的Servlet容器工厂：

	EmbeddedServletContainerFactory containerFactory = getEmbeddedServletContainerFactory();
	
	从ioc容器中获取EmbeddedServletContainerFactory 组件；**TomcatEmbeddedServletContainerFactory**创建对象，后置处理器一看是这个对象，就获取所有的定制器来先定制Servlet容器的相关配置；
- （7）**使用容器工厂获取嵌入式的Servlet容器**：this.embeddedServletContainer = containerFactory.getEmbeddedServletContainer(getSelfInitializer());
- （8）嵌入式的Servlet容器创建对象并启动Servlet容器；

	**先启动嵌入式的Servlet容器，再将ioc容器中剩下没有创建出的对象获取出来**

## 8、使用外置的Servlet容器

### 8.1、嵌入式容器（应用打成可执行的jar）

- 优点：简单、便携
- 缺点：默认不支持JSP、优化定制比较复杂（使用定制器【ServerProperties、自定义EmbeddedServletContainerCustomizer】，自己编写嵌入式Servlet容器的创建工厂【EmbeddedServletContainerFactory】）；

### 8.2、外置Servlet容器

外面安装Tomcat---应用war包的方式打包

配置步骤
- （1）必须创建一个war项目；（利用idea创建好目录结构）

	![](image/Springboot-外置容器配置.png)

	创建好目录结构之后，会在Application同级目录下自动创建一个ServletInitializer，即第三步骤中的需要实现的

- （2）将嵌入式的Tomcat指定为provided；
	```xml
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-tomcat</artifactId>
		<scope>provided</scope>
	</dependency>
	```
- （3）必须编写一个`SpringBootServletInitializer`的子类，并实现configure方法
	```java
	public class ServletInitializer extends SpringBootServletInitializer {
		@Override
		protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
			return application.sources(JspDemoApplication.class);
		}
	}
	```
- （4）配置好tomcat容器启动即可，启动服务器就可以使用
- 配置试图解析器：
	```
	spring.mvc.view.prefix=/WEB-INF/
	spring.mvc.view.suffix=.jsp
	```

### 8.3、外置容器实现原理

jar包：执行SpringBoot主类的main方法，启动ioc容器，创建嵌入式的Servlet容器；

war包：启动服务器，**服务器启动SpringBoot应用**【SpringBootServletInitializer】，启动ioc容器；

servlet3.0有几个规则：
- 服务器启动（web应用启动）会创建当前web应用里面每一个jar包里面ServletContainerInitializer实例
- ServletContainerInitializer的实现放在jar包的META-INF/services文件夹下，有一个名为javax.servlet.ServletContainerInitializer的文件，内容就是ServletContainerInitializer的实现类的全类名
- 可以使用@HandlesTypes，在应用启动的时候加载需要的类；

整体过程：
- （1）启动Tomcat
- （2）Spring下的web模块：spring-web，对应目下有个文件：
	org\springframework\spring-web\4.3.14.RELEASE\spring-web-4.3.14.RELEASE.jar!\META-INF\services\javax.servlet.ServletContainerInitializer

	Spring的web模块里面有这个文件：**org.springframework.web.SpringServletContainerInitializer**
- （3）`SpringServletContainerInitializer`将`@HandlesTypes(WebApplicationInitializer.class)`标注的所有这个类型的类都传入到onStartup方法的Set<Class<?>>；为这些WebApplicationInitializer类型的类创建实例
- （4）每一个WebApplicationInitializer都调用自己的onStartup

	![](image/WebApplicationInitializer实现类.png)

- （5）因为在项目中实现了SpringBootServletInitializer，所以该类的实现类会被创建对象，并执行onStartup方法
- （6）SpringBootServletInitializer实例执行onStartup的时候会createRootApplicationContext；创建容器
	```java
	protected WebApplicationContext createRootApplicationContext(
		ServletContext servletContext) {
		//1、创建SpringApplicationBuilder
		SpringApplicationBuilder builder = createSpringApplicationBuilder();
		StandardServletEnvironment environment = new StandardServletEnvironment();
		environment.initPropertySources(servletContext, null);
		builder.environment(environment);
		builder.main(getClass());
		ApplicationContext parent = getExistingRootWebApplicationContext(servletContext);
		if (parent != null) {
			this.logger.info("Root context already created (using as parent).");
			servletContext.setAttribute(
					WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, null);
			builder.initializers(new ParentContextApplicationContextInitializer(parent));
		}
		builder.initializers(
				new ServletContextApplicationContextInitializer(servletContext));
		builder.contextClass(AnnotationConfigEmbeddedWebApplicationContext.class);
			
			//调用configure方法，子类重写了这个方法，将SpringBoot的主程序类传入了进来
		builder = configure(builder);
			
			//使用builder创建一个Spring应用
		SpringApplication application = builder.build();
		if (application.getSources().isEmpty() && AnnotationUtils
				.findAnnotation(getClass(), Configuration.class) != null) {
			application.getSources().add(getClass());
		}
		Assert.state(!application.getSources().isEmpty(),
				"No SpringApplication sources have been defined. Either override the "
					+ "configure method or add an @Configuration annotation");
		// Ensure error pages are registered
		if (this.registerErrorPageFilter) {
			application.getSources().add(ErrorPageFilterConfiguration.class);
		}
			//启动Spring应用
		return run(application);
	}
	```
- （7）上述最后调用的run(application);其实就是调用 `org.springframework.boot.SpringApplication#run(java.lang.String...)` 的方法

# 四、SpringBoot启动配置原理

## 1、注解
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
public @interface SpringBootApplication {
...
}
```
- `@SpringBootApplication` = `(默认属性)@Configuration + @EnableAutoConfiguration + @ComponentScan`。如果在启动类不配置`@SpringBootApplication`，也可以配置这三个注解，通用能够启动；
- `@Configuration`：JavaConfig形式的Spring Ioc容器的配置类；
- `@ComponentScan`：@ComponentScan的功能其实就是自动扫描并加载符合条件的组件（比如@Component和@Repository等）或者bean定义，最终将这些bean定义加载到IoC容器中
- `@EnableAutoConfiguration`：@EnableAutoConfiguration也是借助@Import的帮助，将所有符合自动配置条件的bean定义加载到IoC容器，仅此而已！

    @EnableAutoConfiguration会根据类路径中的jar依赖为项目进行自动配置，如：添加了spring-boot-starter-web依赖，会自动添加Tomcat和Spring MVC的依赖，Spring Boot会对Tomcat和Spring MVC进行自动配置

	借助于Spring框架原有的一个工具类：SpringFactoriesLoader的支持，SpringFactoriesLoader属于Spring框架私有的一种扩展方案，其主要功能就是从指定的配置文件META-INF/spring.factories加载配置

	从classpath中搜寻所有的`META-INF/spring.factories`配置文件，并将其中`org.springframework.boot.autoconfigure.EnableutoConfiguration`对应的配置项通过反射（Java Refletion）实例化为对应的标注了@Configuration的JavaConfig形式的IoC容器配置类，然后汇总为一个并加载到IoC容器。

	配置在`META-INF/spring.factories`：**ApplicationContextInitializer**、**SpringApplicationRunListener**
	
	只需要放在ioc容器中：**ApplicationRunner**、**CommandLineRunner**

## 2、启动流程

- （1）创建SpringApplication对象，

	SpringBoot1.5版本调用initialize(sources)方法
	```java
	private void initialize(Object[] sources) {
		//保存主配置类
		if (sources != null && sources.length > 0) {
			this.sources.addAll(Arrays.asList(sources));
		}
		//判断当前是否一个web应用
		this.webEnvironment = deduceWebEnvironment();
		//从类路径下找到META-INF/spring.factories配置的所有ApplicationContextInitializer；然后保存起来
		setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
		//从类路径下找到ETA-INF/spring.factories配置的所有ApplicationListener
		setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
		//从多个配置类中找到有main方法的主配置类
		this.mainApplicationClass = deduceMainApplicationClass();
	}
	```
	SpringBoot2.x版本是直接在SpringApplication构造方法中：
	```java
	public SpringApplication(Class<?>... primarySources) {
		this(null, primarySources);
	}

	public SpringApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
		this.resourceLoader = resourceLoader;
		Assert.notNull(primarySources, "PrimarySources must not be null");
		this.primarySources = new LinkedHashSet<>(Arrays.asList(primarySources));
		this.webApplicationType = deduceWebApplicationType();
		setInitializers((Collection) getSpringFactoriesInstances(
				ApplicationContextInitializer.class));
		setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
		this.mainApplicationClass = deduceMainApplicationClass();
	}
	```
- （2）运行run方法
	```java
	public ConfigurableApplicationContext run(String... args) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		ConfigurableApplicationContext context = null;
		FailureAnalyzers analyzers = null;
		configureHeadlessProperty();
			
		//获取SpringApplicationRunListeners；从类路径下META-INF/spring.factories
		SpringApplicationRunListeners listeners = getRunListeners(args);
			//回调所有的获取SpringApplicationRunListener.starting()方法
		listeners.starting();
		try {
			//封装命令行参数
			ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
			//准备环境
			ConfigurableEnvironment environment = prepareEnvironment(listeners,applicationArguments);
			//创建环境完成后回调SpringApplicationRunListener.environmentPrepared()；表示环境准备完成
			
			Banner printedBanner = printBanner(environment);
			
			//创建ApplicationContext；决定创建web的ioc还是普通的ioc
			context = createApplicationContext();
			
			analyzers = new FailureAnalyzers(context);
			//准备上下文环境;将environment保存到ioc中；而且applyInitializers()；
			//applyInitializers()：回调之前保存的所有的ApplicationContextInitializer的initialize方法
			//回调所有的SpringApplicationRunListener的contextPrepared()；
			//
			prepareContext(context, environment, listeners, applicationArguments,printedBanner);
			//prepareContext运行完成以后回调所有的SpringApplicationRunListener的contextLoaded（）；
			
			//刷新容器；ioc容器初始化（如果是web应用还会创建嵌入式的Tomcat）；Spring注解版
			//扫描，创建，加载所有组件的地方；（配置类，组件，自动配置）
			refreshContext(context);
			//从ioc容器中获取所有的ApplicationRunner和CommandLineRunner进行回调
			//ApplicationRunner先回调，CommandLineRunner再回调
			afterRefresh(context, applicationArguments);
			//所有的SpringApplicationRunListener回调finished方法
			listeners.finished(context, null);
			stopWatch.stop();
			if (this.logStartupInfo) {
				new StartupInfoLogger(this.mainApplicationClass)
					.logStarted(getApplicationLog(), stopWatch);
			}
			//整个SpringBoot应用启动完成以后返回启动的ioc容器；
			return context;
		}
		catch (Throwable ex) {
			handleRunFailure(context, listeners, analyzers, ex);
			throw new IllegalStateException(ex);
		}
	}
	```

大体流程：
- 1） 如果我们使用的是SpringApplication的静态run方法，那么，这个方法里面首先要创建一个SpringApplication对象实例，然后调用这个创建好的SpringApplication的实例方法。在SpringApplication实例初始化的时候，它会提前做几件事情：
	- 根据classpath里面是否存在某个特征类（org.springframework.web.context.ConfigurableWebApplicationContext）来决定是否应该创建一个为Web应用使用的ApplicationContext类型。
	- 使用SpringFactoriesLoader在应用的classpath中查找并加载所有可用的ApplicationContextInitializer。
	- 使用SpringFactoriesLoader在应用的classpath中查找并加载所有可用的ApplicationListener。
	- 推断并设置main方法的定义类。
- 2） SpringApplication实例初始化完成并且完成设置后，就开始执行run方法的逻辑了，方法执行伊始，首先遍历执行所有通过SpringFactoriesLoader可以查找到并加载的SpringApplicationRunListener。调用它们的started()方法，告诉这些SpringApplicationRunListener，“嘿，SpringBoot应用要开始执行咯！”。
- 3） 创建并配置当前Spring Boot应用将要使用的Environment（包括配置要使用的PropertySource以及Profile）。
- 4） 遍历调用所有SpringApplicationRunListener的environmentPrepared()的方法，告诉他们：“当前SpringBoot应用使用的Environment准备好了咯！”。
- 5） 如果SpringApplication的showBanner属性被设置为true，则打印banner。
- 6） 根据用户是否明确设置了applicationContextClass类型以及初始化阶段的推断结果，决定该为当前SpringBoot应用创建什么类型的ApplicationContext并创建完成，然后根据条件决定是否添加ShutdownHook，决定是否使用自定义的BeanNameGenerator，决定是否使用自定义的ResourceLoader，当然，最重要的，将之前准备好的Environment设置给创建好的ApplicationContext使用。
- 7） ApplicationContext创建好之后，SpringApplication会再次借助Spring-FactoriesLoader，查找并加载classpath中所有可用的ApplicationContext-Initializer，然后遍历调用这些ApplicationContextInitializer的initialize（applicationContext）方法来对已经创建好的ApplicationContext进行进一步的处理。
- 8） 遍历调用所有SpringApplicationRunListener的contextPrepared()方法。
- 9） 最核心的一步，将之前通过@EnableAutoConfiguration获取的所有配置以及其他形式的IoC容器配置加载到已经准备完毕的ApplicationContext。
- 10） 遍历调用所有SpringApplicationRunListener的contextLoaded()方法。
- 11） 调用ApplicationContext的refresh()方法，完成IoC容器可用的最后一道工序。
- 12） 查找当前ApplicationContext中是否注册有CommandLineRunner，如果有，则遍历执行它们。
- 13） 正常情况下，遍历执行SpringApplicationRunListener的finished()方法、（如果整个过程出现异常，则依然调用所有SpringApplicationRunListener的finished()方法，只不过这种情况下会将异常信息一并传入处理）

总结：

- SpringApplication.run(主程序类)
	- new SpringApplication(主程序类)
		- 判断是否web应用
		- 加载并保存所有ApplicationContextInitializer(`META-INF/spring.factories`)， • 加载并保存所有ApplicationListener
		- 获取到主程序类
	- run()
		- 回调所有的SpringApplicationRunListener(`META-INF/spring.factories`)的starting
		- 获取ApplicationArguments
		- 准备环境&回调所有监听器( SpringApplicationRunListener )的environmentPrepared • 打印banner信息
		- 创建ioc容器对象
	- AnnotationConfigEmbeddedWebApplicationContext(web环境容器) – AnnotationConfigApplicationContext(普通环境容器)

- 准备环境
- 执行ApplicationContextInitializer. initialize()
- 监听器SpringApplicationRunListener回调contextPrepared – 加载主配置类定义信息
- 监听器SpringApplicationRunListener回调contextLoaded
	- 刷新启动IOC容器;
- 扫描加载所有容器中的组件
- 包括从`META-INF/spring.factories`中获取的所有EnableAutoConfiguration组件
	- 回调容器中所有的ApplicationRunner、CommandLineRunner的run方法 • 监听器SpringApplicationRunListener回调finished

*Spring Boot 总是遵循一个标准：容器中有我们自己配置的组件就用我们配置的，没有就用自动配 置默认注册进来的组件;*

## 3、事件监听机制

配置在META-INF/spring.factories

- **ApplicationContextInitializer**
	```java
	public class HelloApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
			System.out.println("ApplicationContextInitializer...initialize..."+applicationContext);
		}
	}
	```

- **SpringApplicationRunListener**
	SpringBoot1.5和SPringBoot2.x版本有不同
	```java
	public class HelloSpringApplicationRunListener implements SpringApplicationRunListener {
		//必须有的构造器
		public HelloSpringApplicationRunListener(SpringApplication application, String[] args){
		}
		@Override
		public void starting() {
			System.out.println("SpringApplicationRunListener...starting...");
		}
		@Override
		public void environmentPrepared(ConfigurableEnvironment environment) {
			Object o = environment.getSystemProperties().get("os.name");
			System.out.println("SpringApplicationRunListener...environmentPrepared.."+o);
		}
		@Override
		public void contextPrepared(ConfigurableApplicationContext context) {
			System.out.println("SpringApplicationRunListener...contextPrepared...");
		}
		@Override
		public void contextLoaded(ConfigurableApplicationContext context) {
			System.out.println("SpringApplicationRunListener...contextLoaded...");
		}
		@Override
		public void finished(ConfigurableApplicationContext context, Throwable exception) {
			System.out.println("SpringApplicationRunListener...finished...");
		}
	}
	```

```
org.springframework.context.ApplicationContextInitializer=\
com.blue.fish.springboot.listener.HelloApplicationContextInitializer

org.springframework.boot.SpringApplicationRunListener=\
com.blue.fish.springboot.listener.HelloSpringApplicationRunListener
```

- **ApplicationRunner**
	```java
	@Component
	public class HelloApplicationRunner implements ApplicationRunner {
		@Override
		public void run(ApplicationArguments args) throws Exception {
			System.out.println("ApplicationRunner...run....");
		}
	}
	```

- **CommandLineRunner**
	```java
	@Component
	public class HelloCommandLineRunner implements CommandLineRunner {
		@Override
		public void run(String... args) throws Exception {
			System.out.println("CommandLineRunner...run..."+ Arrays.asList(args));
		}
	}
	```

# 五、自定义Starter

## 1、自动装配Bean

自动装配使用配置类(@Configuration)结合Spring4 提供的条件判断注解@Conditional及Spring Boot的派生注解如@ConditionOnClass完成;

## 2、配置自动装配Bean

将标注@Configuration的自动配置类，放在classpath下META-INF/spring.factories文件中
```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration,\
org.springframework.boot.autoconfigure.aop.AopAutoConfiguration,\
```

## 3、自动装配顺序
- 在特定自动装配Class之前

	- @AutoConfigureBefore – 在特定自动装配Class之后
	- @AutoConfigureAfter

- 指定顺序：@AutoConfigureOrder

```java
@Configuration  //指定这个类是一个配置类
@ConditionalOnXXX  //在指定条件成立的情况下自动配置类生效
@AutoConfigureAfter  //指定自动配置类的顺序
@Bean  //给容器中添加组件

@ConfigurationPropertie结合相关xxxProperties类来绑定相关的配置
@EnableConfigurationProperties //让xxxProperties生效加入到容器中
```

## 4、启动器

启动器模块是一个空 JAR 文件，仅提供辅助性依赖管理，这些依赖可能用于自动装配或者其他类库

命名规约：
- 推荐使用以下命名规约：

	xxxx-starter -> xxxx-starter-autoconfigurer

- 官方命名空间
	- 前缀:“spring-boot-starter-”
	- 模式:spring-boot-starter-模块名
	- 举例:spring-boot-starter-web、spring-boot-starter-actuator、spring-boot-starter-jdbc

- 自定义命名空间
	- 后缀:“-spring-boot-starter”
	- 模式:模块-spring-boot-starter
	- 举例:mybatis-spring-boot-starter

启动器只用来做依赖导入；

专门来写一个自动配置模块；

启动器依赖自动配置；别人只需要引入启动器（starter）

## 5、实例
自定义starter步骤：

- （1）启动器模块
	```xml
	<?xml version="1.0" encoding="UTF-8"?>
	<project xmlns="http://maven.apache.org/POM/4.0.0"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
		<modelVersion>4.0.0</modelVersion>

		<groupId>com.demo.starter</groupId>
		<artifactId>demo-spring-boot-starter</artifactId>
		<version>1.0-SNAPSHOT</version>

		<dependencies>
			<dependency>
				<groupId>com.demo.starter</groupId>
				<artifactId>demo-spring-boot-starter-autoconfigurer</artifactId>
				<version>0.0.1-SNAPSHOT</version>
			</dependency>
		</dependencies>
	</project>
	```

- （2）自动配置模块
	```xml
	<?xml version="1.0" encoding="UTF-8"?>
	<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
		<modelVersion>4.0.0</modelVersion>

		<groupId>com.demo.starter</groupId>
		<artifactId>demo-spring-boot-starter-autoconfigurer</artifactId>
		<version>0.0.1-SNAPSHOT</version>
		<packaging>jar</packaging>

		<name>demo-spring-boot-starter-autoconfigurer</name>
		<description>Demo project for Spring Boot</description>

		<parent>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-parent</artifactId>
			<version>2.0.5.RELEASE</version>
			<relativePath/> <!-- lookup parent from repository -->
		</parent>

		<properties>
			<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
			<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
			<java.version>1.8</java.version>
		</properties>

		<dependencies>
			<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-web</artifactId>
			</dependency>

		</dependencies>

	</project>

	```
	```java
	import org.springframework.boot.context.properties.ConfigurationProperties;
	@ConfigurationProperties(prefix = "spring.hello")
	public class HelloProperties {

		private String prefix;
		private String suffix;

		public String getPrefix() {
			return prefix;
		}

		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}

		public String getSuffix() {
			return suffix;
		}

		public void setSuffix(String suffix) {
			this.suffix = suffix;
		}
	}

	public class HelloService {
		HelloProperties helloProperties;

		public HelloProperties getHelloProperties() {
			return helloProperties;
		}

		public void setHelloProperties(HelloProperties helloProperties) {
			this.helloProperties = helloProperties;
		}

		public String sayHello(String name){
			return helloProperties.getPrefix()+ "-" +name + "-" + helloProperties.getSuffix();
		}
	}

	@Configuration
	@ConditionalOnWebApplication
	@EnableConfigurationProperties(HelloProperties.class)
	public class HelloServiceAutoConfiguration {

		@Autowired
		HelloProperties helloProperties;
		@Bean
		public HelloService helloService(){
			HelloService service = new HelloService();
			service.setHelloProperties(helloProperties);
			return service;
		}
	}
	```
	spring.factories
	```
	org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
	com.demo.starter.demo.springboot.HelloServiceAutoConfiguration
	```

- （3）引入

# 六、SpringBoot面试题

## 1、SpringApplication.run都做了些什么？

## 2、SpringBoot常用注解

- @SpringBootApplication：包含@Configuration、@EnableAutoConfiguration、@ComponentScan通常用在主类上
	- @Configuration 等同于spring的XML配置文件；使用Java代码可以检查类型安全。
	- @EnableAutoConfiguration 自动配置。
	- @ComponentScan 组件扫描，可自动发现和装配一些Bean

- @MapperScan：开启MyBatis的DAO扫描  

# 七、SOFABoot


https://www.sofastack.tech/sofa-boot/docs/Home

# 参考资料
- [Spring Boot启动流程分析](http://www.cnblogs.com/xinzhao/p/5551828.html)
- [Spring Boot知识清单](https://www.jianshu.com/p/83693d3d0a65)
- [Spring Boot 官方文档](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [SpringBoot1.x升级到2.x指南](http://www.leftso.com/blog/484.html)
- [SpringBoot样例](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-samples)
- [SpringBoot内存泄露](https://mp.weixin.qq.com/s/cs92_dRqsn2_jHAtcEB57g)








