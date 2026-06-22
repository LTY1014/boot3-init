package com.lty.constant;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

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
     * 获取项目根目录
     */
    String PROJECT_ROOT_DIRECTORY = Paths.get(System.getProperty("user.dir")).toString();

    /**
     * 获取系统临时文件路径
     */
    String TEMP_DIRECTORY = Paths.get(System.getProperty("java.io.tmpdir")).toString();
}
