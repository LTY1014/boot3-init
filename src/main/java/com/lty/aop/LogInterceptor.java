package com.lty.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 请求响应日志 AOP
 *
 * @author lty
 **/
@Aspect
@Component
@Slf4j
public class LogInterceptor {

    /** 敏感字段名（不区分大小写），日志中自动脱敏 */
    private static final List<String> SENSITIVE_KEYS = Arrays.asList(
            "password", "userPassword", "oldPassword", "newPassword", "pwd", "secret"
    );

    /** 参数日志最大长度，超出截断 */
    private static final int MAX_PARAM_LOG_LENGTH = 2000;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 执行拦截
     */
    @Around("execution(* com.lty..controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable {
        // 计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 获取请求路径
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 生成请求唯一 id
        String requestId = UUID.randomUUID().toString();
        String url = httpServletRequest.getRequestURI();
        // 获取请求参数（已脱敏 + 截断）
        String reqParam = sanitizeArgs(point.getArgs());
        // 输出请求日志
        log.info("request start, id: {}, path: {}, ip: {}, params: {}", requestId, url,
                httpServletRequest.getRemoteHost(), reqParam);
        // 执行原方法
        Object result = point.proceed();
        // 输出响应日志
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        log.info("request end, id: {}, cost: {}ms", requestId, totalTimeMillis);
        return result;
    }

    /**
     * 将参数序列化为 JSON 并脱敏，超长时截断
     */
    private String sanitizeArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }
        String result = Arrays.stream(args)
                .map(this::toSafeJson)
                .collect(Collectors.joining(", ", "[", "]"));
        return truncate(result);
    }

    /**
     * 将单个参数转为安全的 JSON 字符串（敏感字段脱敏）
     */
    private String toSafeJson(Object arg) {
        if (arg == null) {
            return "null";
        }
        // HttpServletRequest / HttpServletResponse / BindingResult 等基础设施参数无需记录
        if (arg instanceof jakarta.servlet.ServletRequest
                || arg instanceof jakarta.servlet.ServletResponse
                || arg instanceof org.springframework.validation.BindingResult) {
            return arg.getClass().getSimpleName();
        }
        try {
            String json = OBJECT_MAPPER.writeValueAsString(arg);
            return maskSensitiveFields(json);
        } catch (JsonProcessingException e) {
            // 序列化失败时，仅输出类型名，避免 toString() 泄露敏感字段
            return arg.getClass().getSimpleName();
        }
    }

    /**
     * 替换 JSON 字符串中的敏感字段值
     */
    private String maskSensitiveFields(String json) {
        String result = json;
        for (String key : SENSITIVE_KEYS) {
            // 匹配 "key": "任意值"（不区分大小写），将值替换为 "******"
            result = result.replaceAll(
                    "(?i)\"" + key + "\"\\s*:\\s*\"[^\"]*\"",
                    "\"" + key + "\": \"******\""
            );
        }
        return result;
    }

    /**
     * 超长字符串截断
     */
    private String truncate(String str) {
        if (str != null && str.length() > MAX_PARAM_LOG_LENGTH) {
            return str.substring(0, MAX_PARAM_LOG_LENGTH)
                    + "... (truncated, " + str.length() + " chars)";
        }
        return str;
    }
}

