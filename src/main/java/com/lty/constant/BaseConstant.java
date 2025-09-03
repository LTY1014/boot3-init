package com.lty.constant;

import java.nio.charset.StandardCharsets;

/**
 * 通用常量
 * @author lty
 */
public interface BaseConstant {

    /**
     * 升序
     */
    String SORT_ORDER_ASC="ascend";

    /**
     * 降序
     */
    String SORT_ORDER_DESC = "descend";

    /**
     * 分页限制最大值
     */
    Integer PAGE_LIMIT = 50;

    /**
     * 用于DB中的密码加密解密(KEY要十六位)
     */
    byte[] AES_KEY = "123456789abcdefg".getBytes(StandardCharsets.UTF_8);

    /**
     * linux系统分隔符
     */
    String SEPARATOR_SPRIT = "/";
    /**
     * win系统分隔符
     */
    String SEPARATOR_BACKSLASH = "\\\\";

    /**
     * 获取项目根目录
     */
    String PROJECT_ROOT_DIRECTORY = System.getProperty("user.dir")
            .replaceAll("\\\\", SEPARATOR_SPRIT);
}
