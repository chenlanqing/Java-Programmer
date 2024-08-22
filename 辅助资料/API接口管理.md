# 一、Swagger

- [开源集成Swagger](https://swagger.io/tools/open-source/open-source-integrations/)
- [Swagger](https://swagger.io/)
- [Swagger specifications convert to AsciiDoc and PDF](https://blog.devgenius.io/swagger-specifications-convert-to-asciidoc-and-pdf-with-a-custom-font-8e734c6fdd8c)

## 1、集成Swagger-UI

- [Spring Boot整合Swagger-UI](https://javabetter.cn/springboot/swagger.html)
  
（1）在 pom.xml 文件中添加 Swagger 的 starter
```xml
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-boot-starter</artifactId>
    <version>3.0.0</version>
</dependency>
```
- springfox-swagger 是一个基于 Spring 生态系统的，Swagger 规范的实现。
- springfox-boot-starter 是 springfox 针对 Spring Boot 项目提供的一个 starter，简化 Swagger 依赖的导入，否则我们就需要在 pom.xml 文件中添加 springfox-swagger、springfox-swagger-ui 等多个依赖


（2）添加Java配置
```java
@Configuration
@EnableOpenApi
public class SwaggerConfig {
    @Bean
    public Docket docket() {
        Docket docket = new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo()).enable(true)
                .select()
                // apis： 添加swagger接口提取范围
                .apis(RequestHandlerSelectors.basePackage("com.controller"))
                .paths(PathSelectors.any())
                .build();

        return docket;
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SpringBoot+Swagger")
                .description("SpringBoot+Swagger集成项目")
                .contact(new Contact("QingFan", "",""))
                .version("v1.0")
                .build();
    }
}
```

# 参考资料

- [mkdocs-material](https://github.com/squidfunk/mkdocs-material)
- [YApi](https://github.com/YMFE/yapi)
- [hoppscotch-开源API，对标Postman](https://github.com/hoppscotch/hoppscotch)
