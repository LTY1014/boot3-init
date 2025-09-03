package com.lty.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 全局跨域配置
 *
 * @author lty
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /** 配置跨域 */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 覆盖所有请求
        registry.addMapping("/**")
                // 允许发送Cookie
                .allowCredentials(true)
                // 设置允许跨域请求的域名
                .allowedOriginPatterns("*")
                // 设置允许的方法
                .allowedMethods("*")
                .allowedHeaders("*")
                .exposedHeaders("*");
    }


    /**
     * 设置静态资源映射 访问接口文档
     *
     * @param registry
     */
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
