create database if not exists user_center;

use user_center;

create table if not exists user
(
    username     varchar(256)                       null comment '用户昵称',
    id           bigint auto_increment comment 'id'
        primary key,
    userAccount  varchar(256)                       null comment '账号',
    avatarUrl    varchar(1024)                      null comment '用户头像',
    gender       tinyint                            null comment '性别 0 - 女 1 - 男',
    userPassword varchar(512)                       not null comment '密码',
    phone        varchar(128)                       null comment '电话',
    email        varchar(512)                       null comment '邮箱',
    userStatus   int      default 0                 not null comment '状态 0 - 正常',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete     tinyint  default 0                 not null comment '是否删除',
    userRole     int      default 0                 not null comment '用户角色 0 - 普通用户 1 - 管理员',
    planetCode   varchar(512)                       null comment '星球编号'
)
    comment '用户';

INSERT INTO user_center.user (username, userAccount, avatarUrl, gender, userPassword, phone, email, userStatus, createTime, updateTime, isDelete, userRole, planetCode)
VALUES ('jexhsu', 'jexhsu', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ9pisrl5v6LFRE9Xk2qA_FvYDyLh12Z9vivg&s', 1, 'b0dd3697a192885d7c055db46155b26a', '1779148xxxx', 'jexhsu@gmail.com', 0, '2024-08-25 14:14:22', '2024-08-25 14:39:37', 0, 1, '1');
