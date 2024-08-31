package com.jexhsu.usercenter.model.request;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserUpdateRequest implements Serializable {
    private Long id;

    private Integer user_status;

    private Integer user_role;

}
