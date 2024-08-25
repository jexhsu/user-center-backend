package com.jexhsu.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {
    public String userAccount;
    public String userPassword;
}
