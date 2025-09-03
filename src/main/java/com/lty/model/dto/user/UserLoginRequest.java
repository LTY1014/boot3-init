package com.lty.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lty
 */
@Data
public class UserLoginRequest implements Serializable {

    private String userAccount;

    private String userPassword;
}
