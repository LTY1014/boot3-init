package com.lty.model.dto.user;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lty
 */
@Data
public class UserAddRequest implements Serializable {

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 角色
     */
    private String userRole;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
