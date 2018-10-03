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
  - [6、错误页面](#6%E9%94%99%E8%AF%AF%E9%A1%B5%E9%9D%A2)
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

## 6、错误页面

# 参考资料
- [Spring Boot启动流程分析](http://www.cnblogs.com/xinzhao/p/5551828.html)
- [Spring Boot知识清单](https://www.jianshu.com/p/83693d3d0a65)








