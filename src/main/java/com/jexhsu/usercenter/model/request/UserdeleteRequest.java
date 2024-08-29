package com.jexhsu.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserdeleteRequest implements Serializable {
    private Long id;
}
