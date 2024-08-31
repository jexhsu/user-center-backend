CREATE DATABASE user_center;

\c user_center

CREATE TABLE IF NOT EXISTS users_dev (
    id BIGSERIAL PRIMARY KEY,  -- 用户ID
    user_name VARCHAR(256),      -- 用户昵称
    user_account VARCHAR(256),  -- 账号
    avatar_url VARCHAR(1024),   -- 用户头像
    gender SMALLINT,            -- 性别 0 - 女 1 - 男
    user_password VARCHAR(512) NOT NULL,  -- 密码
    phone VARCHAR(128),         -- 电话
    email VARCHAR(512),         -- 邮箱
    user_status INT DEFAULT 0 NOT NULL,    -- 状态 0 - 正常
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 创建时间
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 更新时间
    is_delete SMALLINT DEFAULT 0 NOT NULL,           -- 是否删除
    user_role INT DEFAULT 0 NOT NULL,                 -- 用户角色 0 - 普通用户 1 - 管理员
    planet_code VARCHAR(512)                            -- 星球编号
);
