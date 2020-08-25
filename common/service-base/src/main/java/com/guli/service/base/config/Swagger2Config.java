package com.guli.service.base.config;


import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.function.Predicate;

@Configuration
@EnableSwagger2  // 启用Swagger
public class Swagger2Config {

    @Bean
    public Docket webApiConfig(){
       return new Docket(DocumentationType.SWAGGER_2)
               .groupName("webApi")
               .apiInfo(webApiInfo())
               .select()  // 分组过滤
               .paths(Predicates.and(PathSelectors.regex("/api/.*"))) // Predicates.and() and: 匹配对应路径
               .build();
    }

    @Bean
    public Docket adminApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("adminApi")
                .apiInfo(adminApiInfo())
                .select()  // 分组过滤
                .paths(Predicates.and(PathSelectors.regex("/admin/.*"))) // Predicates.not() not: 匹配此路径以外的路径
                .build();
    }


    private ApiInfo adminApiInfo() {
        return new ApiInfoBuilder()
                .title("admin谷粒学院API")
                .description("...............")
                .version("1.0")
                .contact(new Contact("zwx","http://youngdonkey.cn","donkey98@163.com"))
                .build();
    }

    private ApiInfo webApiInfo() {
        return new ApiInfoBuilder()
                .title("web谷粒学院API")
                .description("...............")
                .version("1.0")
                .contact(new Contact("zwx","http://youngdonkey.cn","donkey98@163.com"))
                .build();
    }
}
