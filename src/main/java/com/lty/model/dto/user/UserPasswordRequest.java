package com.lty.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 修改密码
 * @author lty
 */
@Data
public class UserPasswordRequest implements Serializable {

    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 确认密码
     */
    private String confirmPassword;
}
