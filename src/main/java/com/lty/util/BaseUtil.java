package com.lty.util;

import cn.hutool.core.util.StrUtil;
import com.lty.common.ErrorCode;
import com.lty.exception.BusinessException;
import org.openjdk.jol.vm.VM;

/**
 * @author lty
 */
public class BaseUtil {

    /**
     * 仅支持字母、数字、下划线、空格、逗号、小数点（支持多个字段排序）
     */
    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";

    public static boolean isValidOrderBySql(String value)
    {
        return value.matches(SQL_PATTERN);
    }

    public static <T> long getMemorySize(T bean){
        return VM.current().sizeOf(bean);
    }

    /**
     * 点转斜线 (com.lty.code变为com/lty/code)
     * @param str
     * @return String
     */
    public static String dotToLine(String str) {
        return str.replace(".", "/");
    }

    /**
     * 首字母是否大小写
     * @param name
     * @param isFirstUpper
     * @return
     */
    public static String isFirstUpper(String name, boolean isFirstUpper) {
        if (StrUtil.isBlank(name)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "name不能为空");
        }

        if (name.length() == 1) {
            if (isFirstUpper) {
                return name.toUpperCase();
            } else {
                return name.toLowerCase();
            }
        }

        StringBuffer sb = new StringBuffer();
        if (isFirstUpper) {
            sb.append(Character.toUpperCase(name.charAt(0)));
        } else {
            sb.append(Character.toLowerCase(name.charAt(0)));
        }
        sb.append(name.substring(1));
        return sb.toString();
    }

    /**
     * 驼峰法转下划线
     */
    public static String camel2Underline(String str) {

        if (StrUtil.isBlank(str)) {
            return "";
        }
        if (str.length() == 1) {
            return str.toLowerCase();
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i < str.length(); i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                sb.append("_" + Character.toLowerCase(str.charAt(i)));
            } else {
                sb.append(str.charAt(i));
            }
        }
        return (str.charAt(0) + sb.toString()).toLowerCase();
    }
}
