package com.jexhsu.usercenter.model.domain;

import ch.qos.logback.classic.spi.EventArgUtil;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 用户
 *
 */
@TableName(value = "Users_Prod")
//@TableName(value = "Users_Dev")
@Data
public class User implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    private String user_name;

    /**
     * 账号
     */
    private String user_account;

    /**
     * 用户头像
     */
    private String avatar_url;

    /**
     * 性别 0 - 女 1 - 男
     */
    private Integer gender;

    /**
     * 密码
     */
    private String user_password;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态 0 - 正常
     */
    private Integer user_status;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     *
     */
    private Date update_time;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer is_delete;

    /**
     * 用户角色 0 - 普通用户 1 - 管理员
     */
    private Integer user_role;

    /**
     * 星球编号
     */
    private String planet_code;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
