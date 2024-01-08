package com.ws.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@Configuration
//开启swagger2自动配置
public class SwaggerConfig {

    public static final String NAME = "wangsen";

    public static final String EMAIL = "1376474892@qq.com";

    public static final String TITLE = "Blog博客系统接口文档";

    public static final String DESC = "个人博客系统，用于发布个人博客供大家浏览";

    public static final String VERSION = "1.0";

    public static final String LICENSE = "博客发布";

    public static final String LICENSE_URL = "www.wangsenblog.top";


    //以下为配置信息，如果不配置，swagger会使用默认配置
    //创建一个swagger的bean实例
    @Bean
    public Docket docket() {
        //通过apiInfo对象配置基本信息
        //配置设定的基本信息
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                //配置分组信息：修改组名为 book，多个分组需要多个Docket，一个Docket对应一个分组
                .groupName("blog")
                //配置接口信息：扫描指定包
                .select().apis(RequestHandlerSelectors.basePackage("com.ws.web"))
                .paths(PathSelectors.any())
                .build()
                //添加请求头信息
                .globalOperationParameters(parametersConfig());
    }

    //设定基本信息
    private ApiInfo apiInfo() {

        //作者信息
        Contact contact = new Contact(
                NAME,
                "http://" + LICENSE_URL,
                EMAIL
        );

        return new ApiInfoBuilder()
                .title(TITLE)
                .description(DESC)
                .version(VERSION)
                .license(LICENSE)
                .licenseUrl(LICENSE_URL)
                .contact(contact).build();
    }

    //设置请求头
    public List<Parameter> parametersConfig() {
        ArrayList<Parameter> parameters = new ArrayList<>();
        parameters.add(
                new ParameterBuilder().name("token")
                        .description("token")
                        .modelRef(new ModelRef("String"))
                        .parameterType("header")
                        .defaultValue("default")
                        .hidden(true)
                        .required(false)
                        .build()

        );
        parameters.add(
                new ParameterBuilder().name("token2")
                        .description("token")
                        .modelRef(new ModelRef("String"))
                        .parameterType("header")
                        .defaultValue("")
                        .hidden(true)
                        .required(false)
                        .build()
        );
        return parameters;
    }


}
