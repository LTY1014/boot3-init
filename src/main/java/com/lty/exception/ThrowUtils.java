package com.lty.exception;

import com.lty.common.ErrorCode;

/**
 * 抛异常工具类
 * @author lty
 */
public class ThrowUtils {
    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param runtimeException
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param errorCode
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param errorCode
     * @param message
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }

    /**
     * 判断检查对象是否为空
     * @param object 检查对象
     * @param message 报错信息
     */
    public static <T> void isNull(T object, String message) {
        if (object == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, message);
        }
    }
}
