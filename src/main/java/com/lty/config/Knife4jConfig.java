package com.lty.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                // 配置接口文档基本信息
                .info(this.getApiInfo());
    }

    private Info getApiInfo() {
        return new Info()
                // 配置文档标题
                .title("API Document")
                // 配置文档描述
                .description("API文档")
                // 配置作者信息
                .contact(new Contact().name("lty").url("https://gitee.com/liang-tian-yu").email("xxx@email.com"))
                // 概述信息
                .summary("API文档")
                // 配置版本号
                .version("1.0");
    }
}