package com.lty.util;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 客户端工具
 * @author lty
 */
public class ServletUtil {

    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        try {
            return getRequestAttributes().getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取response
     */
    public static HttpServletResponse getResponse() {
        try {
            return getRequestAttributes().getResponse();
        } catch (Exception e) {
            return null;
        }
    }

    public static ServletRequestAttributes getRequestAttributes() {
        try {
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
            return (ServletRequestAttributes) attributes;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 响应数据输出
     * @param code
     * @param message
     * @param data
     * @example ServletUtil.out(ErrorCode.NOT_LOGIN_ERROR.getCode(), "未授权，请登录", null);
     */
    public static void out(Integer code, String message, Object data) throws IOException {
        HttpServletResponse response = getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", code);
        resultMap.put("message", message);
        if (data != null) {
            resultMap.put("data", data);
        }
        response.getWriter().write(JSONUtil.toJsonStr(resultMap));
    }
}
