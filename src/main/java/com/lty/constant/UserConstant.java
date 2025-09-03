package com.lty.constant;

/**
 * 用户常量
 *
 * @author lty
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "userLoginState";

    //  ------- 权限 --------

    /**
     * 默认权限
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员权限
     */
    String ADMIN_ROLE = "admin";

    /**
     * 盐值，混淆密码
     */
    String SALT = "lty";

    /**
     * 用户账号长度和密码长度
     */
    Integer ACCOUNT_LENGTH=4;
    Integer PASSWORD_LENGTH=6;
    String DEFAULT_PASSWORD="123456";
}
