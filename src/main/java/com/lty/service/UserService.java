package com.lty.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lty.model.dto.user.UserLoginRequest;
import com.lty.model.dto.user.UserQueryRequest;
import com.lty.model.dto.user.UserAddRequest;
import com.lty.model.dto.user.UserUpdateRequest;
import com.lty.model.entity.User;

/**
 * @author lty
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAddRequest
     * @return long
     */
    long userRegister(UserAddRequest userAddRequest);

    /**
     * 用户登录
     * @param userLoginRequest
     * @return User
     */
    User userLogin(UserLoginRequest userLoginRequest);

    /**
     * 获取当前用户
     * @return User
     */
    User getLoginUser();

    /**
     * 是否为管理员
     * @return boolean
     */
    boolean isAdmin();

    /**
     * 用户注销
     * @return
     */
    boolean userLogout();

    /**
     * 用户更新
     * @param userUpdateRequest
     * @return
     */
    boolean updateUser(UserUpdateRequest userUpdateRequest);

    /**
     * 获取查询条件
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 修改密码
     * @param oldPassword
     * @param newPassword
     * @return
     */
    boolean updatePassword(String oldPassword, String newPassword);

    /**
     * 重置密码
     * @param userId
     * @return
     */
    Boolean resetPassword(long userId);

    /**
     * 新增用户
     * @param userAddRequest
     * @return
     */
    Long addUser(UserAddRequest userAddRequest);
}
