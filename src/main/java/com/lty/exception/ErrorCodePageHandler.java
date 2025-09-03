package com.lty.exception;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * 错误码页面处理
 * @author lty
 */
@Configuration
public class ErrorCodePageHandler implements ErrorPageRegistrar {

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        ErrorPage[] errorPages = new ErrorPage[2];
        // 添加错误页面进行映射
        errorPages[0] = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
        errorPages[1] = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");
        registry.addErrorPages(errorPages);
    }
}
