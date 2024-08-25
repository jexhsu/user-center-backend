package com.jexhsu.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {
    public String userAccount;
    public String userPassword;
    public String checkPassword;
}
