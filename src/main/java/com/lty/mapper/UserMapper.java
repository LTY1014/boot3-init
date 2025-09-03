package com.lty.mapper;

import com.lty.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author lty
*/
public interface UserMapper extends BaseMapper<User> {

    List<User> listUserRole(String role);
}




