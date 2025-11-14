-- 创建数据库
CREATE DATABASE IF NOT EXISTS `aftourism_server`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE `aftourism_server`;

-- ===========================
-- 1. 用户与管理员
-- ===========================

-- 前台用户表
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
    `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`     VARCHAR(50)     NOT NULL COMMENT '登录账号（唯一）',
    `password`     VARCHAR(100)    NOT NULL COMMENT 'BCrypt 加密密码',
    `nickname`     VARCHAR(50)              COMMENT '昵称/姓名',
    `phone`        VARCHAR(20)              COMMENT '联系电话',
    `email`        VARCHAR(100)             COMMENT '邮箱',
    `avatar`       VARCHAR(255)             COMMENT '头像地址',
    `role_code`    VARCHAR(100)    NOT NULL DEFAULT 'PORTAL_USER' COMMENT '角色编码，默认门户普通用户',
    `status`       TINYINT(1)     NOT NULL DEFAULT 1 COMMENT '状态：1启用 0禁用',
    `remark`       VARCHAR(255)            COMMENT '备注',

    `is_deleted`   TINYINT(1)     NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否 1是',
    `create_time`  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  TIMESTAMP      NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_username`(`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='前台用户表';

-- 管理员表
DROP TABLE IF EXISTS `t_admin`;
CREATE TABLE `t_admin` (
    `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`     VARCHAR(50)     NOT NULL COMMENT '管理员账号（唯一）',
    `password`     VARCHAR(100)    NOT NULL COMMENT 'BCrypt 加密密码',
    `real_name`    VARCHAR(50)              COMMENT '真实姓名',
    `phone`        VARCHAR(20)              COMMENT '联系电话',
    `email`        VARCHAR(100)             COMMENT '邮箱',
    `role_code`    VARCHAR(100)    NOT NULL DEFAULT 'ADMIN' COMMENT '角色编码集合，逗号分隔',
    `is_super`     TINYINT(1)     NOT NULL DEFAULT 0 COMMENT '是否超级管理员：1是 0否',
    `status`       TINYINT(1)     NOT NULL DEFAULT 1 COMMENT '状态：1启用 0禁用',
    `remark`       VARCHAR(255)            COMMENT '备注',

    `is_deleted`   TINYINT(1)     NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否 1是',
    `create_time`  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  TIMESTAMP      NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_admin_username`(`username`),
    KEY `idx_admin_role_code`(`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='后台管理员表';

-- 角色权限定义表（角色 + 资源-动作 授权清单）
DROP TABLE IF EXISTS `t_role_access`;
CREATE TABLE `t_role_access` (
    `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_code`     VARCHAR(100)    NOT NULL COMMENT '角色编码',
    `resource_key`  VARCHAR(100)    NOT NULL COMMENT '资源键，例如 NEWS/NOTICE',
    `action`        VARCHAR(100)    NOT NULL COMMENT '动作键，例如 CREATE/READ',
    `allow`         TINYINT(1)      NOT NULL DEFAULT 1 COMMENT '是否允许：1允许 0拒绝',
    `remark`        VARCHAR(255)             COMMENT '备注信息',
    `create_time`   TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   TIMESTAMP       NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_resource_action`(`role_code`, `resource_key`, `action`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色-资源-动作权限表';

-- ===========================
-- 2. 内容管理（新闻 / 通知）
-- ===========================

-- 新闻表
DROP TABLE IF EXISTS `t_news`;
CREATE TABLE `t_news` (
    `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `title`         VARCHAR(200)    NOT NULL COMMENT '新闻标题',
    `content`       LONGTEXT        NOT NULL COMMENT '新闻内容（富文本）',
    `cover_url`     VARCHAR(255)             COMMENT '封面图地址',
    `publish_time`  DATETIME                 COMMENT '发布时间',
    `author`        VARCHAR(50)              COMMENT '作者',
    `status`        TINYINT(1)     NOT NULL DEFAULT 1 COMMENT '状态：1发布 0下线',
    `view_count`    BIGINT         NOT NULL DEFAULT 0 COMMENT '浏览量',

    `is_deleted`    TINYINT(1)     NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否 1是',
    `create_time`   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   TIMESTAMP      NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    KEY `idx_news_publish_time`(`publish_time`),
    KEY `idx_news_title`(`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='新闻表';

-- 通知公告表
DROP TABLE IF EXISTS `t_notice`;
CREATE TABLE `t_notice` (
    `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `title`         VARCHAR(200)    NOT NULL COMMENT '通知标题',
    `content`       LONGTEXT        NOT NULL COMMENT '通知内容',
    `publish_time`  DATETIME                 COMMENT '发布时间',
    `author`        VARCHAR(50)              COMMENT '发布人/发布单位',
    `status`        TINYINT(1)     NOT NULL DEFAULT 1 COMMENT '状态：1发布 0下线',
    `view_count`    BIGINT         NOT NULL DEFAULT 0 COMMENT '浏览量',

    `is_deleted`    TINYINT(1)     NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否 1是',
    `create_time`   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   TIMESTAMP      NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    KEY `idx_notice_publish_time`(`publish_time`),
    KEY `idx_notice_title`(`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知公告表';

-- ===========================
-- 3. 文旅资源（景区 / 场馆 / 活动）
-- ===========================

-- A级景区表
DROP TABLE IF EXISTS `t_scenic_spot`;
CREATE TABLE `t_scenic_spot` (
    `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`           VARCHAR(100)    NOT NULL COMMENT '景区名称',
    `image_url`      VARCHAR(255)             COMMENT '景区图片',
    `level`          VARCHAR(20)              COMMENT '等级：如5A/4A等',
    `ticket_price`   DECIMAL(10,2)            COMMENT '门票价格',
    `address`        VARCHAR(255)             COMMENT '地址',
    `open_time`      VARCHAR(100)             COMMENT '开放时间描述',
    `intro`          TEXT                     COMMENT '简介',
    `phone`          VARCHAR(20)              COMMENT '联系电话',
    `website`        VARCHAR(255)             COMMENT '景区官网',
    `longitude`      DECIMAL(10,6)            COMMENT '经度',
    `latitude`       DECIMAL(10,6)            COMMENT '纬度',
    `sort`           INT            NOT NULL DEFAULT 0 COMMENT '排序值，越大越靠前',

    `is_deleted`     TINYINT(1)     NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否 1是',
    `create_time`    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    TIMESTAMP      NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    KEY `idx_scenic_name`(`name`),
    KEY `idx_scenic_address`(`address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='A级景区表';

-- 场馆表
DROP TABLE IF EXISTS `t_venue`;
CREATE TABLE `t_venue` (
    `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`           VARCHAR(100)    NOT NULL COMMENT '场馆名称',
    `image_url`      VARCHAR(255)             COMMENT '场馆图片',
    `category`       VARCHAR(50)              COMMENT '类别：博物馆/文化馆/体育馆等',
    `is_free`        TINYINT(1)     NOT NULL DEFAULT 0 COMMENT '是否免费开放：1是 0否',
    `ticket_price`   DECIMAL(10,2)            COMMENT '门票价格（如不免费）',
    `address`        VARCHAR(255)             COMMENT '地址',
    `open_time`      VARCHAR(100)             COMMENT '开放时间描述',
    `description`    TEXT                     COMMENT '描述',
    `phone`          VARCHAR(20)              COMMENT '联系电话',
    `website`        VARCHAR(255)             COMMENT '官网',
    `longitude`      DECIMAL(10,6)            COMMENT '经度',
    `latitude`       DECIMAL(10,6)            COMMENT '纬度',
    `sort`           INT            NOT NULL DEFAULT 0 COMMENT '排序值',

    `is_deleted`     TINYINT(1)     NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否 1是',
    `create_time`    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    TIMESTAMP      NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    KEY `idx_venue_name`(`name`),
    KEY `idx_venue_address`(`address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场馆表';

-- 特色活动表（含申报 + 审核）
DROP TABLE IF EXISTS `t_activity`;
CREATE TABLE `t_activity` (
    `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`           VARCHAR(100)    NOT NULL COMMENT '活动名称',
    `cover_url`      VARCHAR(255)             COMMENT '活动封面图',
    `start_time`     DATETIME        NOT NULL COMMENT '开始时间',
    `end_time`       DATETIME        NOT NULL COMMENT '结束时间',
    `category`       VARCHAR(50)              COMMENT '类别：展览/演出/讲座等',
    `venue_id`       BIGINT UNSIGNED NOT NULL COMMENT '关联场馆ID',
    `organizer`      VARCHAR(100)             COMMENT '主办单位',
    `contact_phone`  VARCHAR(20)              COMMENT '联系电话',
    `intro`          TEXT                     COMMENT '活动简介',
    `address_cache`  VARCHAR(255)             COMMENT '场馆地址快照（冗余，便于按地址查询）',

    `apply_user_id`  BIGINT UNSIGNED NOT NULL COMMENT '申报用户ID（前台用户）',
    `apply_status`   TINYINT(1)      NOT NULL DEFAULT 0 COMMENT '审核状态：0待审核 1通过 2不通过',
    `reject_reason`  VARCHAR(255)             COMMENT '不通过原因',

    `online_status`  TINYINT(1)      NOT NULL DEFAULT 1 COMMENT '启停状态：1启用 0暂停',
    `view_count`     BIGINT          NOT NULL DEFAULT 0 COMMENT '浏览量',
    `favorite_count` BIGINT          NOT NULL DEFAULT 0 COMMENT '收藏数量',

    `is_deleted`     TINYINT(1)      NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否 1是',
    `create_time`    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    TIMESTAMP       NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    KEY `idx_activity_venue`(`venue_id`),
    KEY `idx_activity_time`(`start_time`,`end_time`),
    KEY `idx_activity_name`(`name`),
    KEY `idx_activity_apply_status`(`apply_status`),

    CONSTRAINT `fk_activity_venue` FOREIGN KEY (`venue_id`) REFERENCES `t_venue`(`id`),
    CONSTRAINT `fk_activity_apply_user` FOREIGN KEY (`apply_user_id`) REFERENCES `t_user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='特色活动表（含申报及审核）';

-- ===========================
-- 4. 收藏与留言
-- ===========================

-- 用户收藏表
DROP TABLE IF EXISTS `t_user_favorite`;
CREATE TABLE `t_user_favorite` (
    `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`      BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `target_type`  VARCHAR(20)     NOT NULL COMMENT '收藏类型：SCENIC/ VENUE/ ACTIVITY',
    `target_id`    BIGINT UNSIGNED NOT NULL COMMENT '目标ID：景区/场馆/活动的ID',

    `is_deleted`   TINYINT(1)      NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否 1是',
    `create_time`  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  TIMESTAMP       NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_fav`(`user_id`,`target_type`,`target_id`),
    KEY `idx_user_fav_user`(`user_id`),

    CONSTRAINT `fk_fav_user` FOREIGN KEY (`user_id`) REFERENCES `t_user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏表';

-- 活动留言表
DROP TABLE IF EXISTS `t_activity_comment`;
CREATE TABLE `t_activity_comment` (
    `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `activity_id`  BIGINT UNSIGNED NOT NULL COMMENT '活动ID',
    `user_id`      BIGINT UNSIGNED NOT NULL COMMENT '留言用户ID',
    `content`      VARCHAR(500)    NOT NULL COMMENT '留言内容',
    `parent_id`    BIGINT UNSIGNED          COMMENT '父留言ID（可为空，实现楼中楼）',
    `like_count`   INT             NOT NULL DEFAULT 0 COMMENT '点赞数',

    `is_deleted`   TINYINT(1)      NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否 1是',
    `create_time`  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  TIMESTAMP       NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    KEY `idx_comment_activity`(`activity_id`),
    KEY `idx_comment_user`(`user_id`),

    CONSTRAINT `fk_comment_activity` FOREIGN KEY (`activity_id`) REFERENCES `t_activity`(`id`),
    CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `t_user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动留言表';

-- ===========================
-- 5. 操作日志 & 接口性能
-- ===========================

DROP TABLE IF EXISTS `t_operation_log`;
CREATE TABLE `t_operation_log` (
    `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `trace_id`       VARCHAR(64)              COMMENT '链路ID/追踪ID',
    `operator_id`    BIGINT UNSIGNED          COMMENT '操作人ID（管理员或用户）',
    `operator_type`  VARCHAR(20)              COMMENT '操作者类型：ADMIN/USER/SYSTEM',
    `module_name`    VARCHAR(100)             COMMENT '模块名称：如新闻管理/活动管理',
    `operation_name` VARCHAR(100)             COMMENT '操作名称：新增新闻/删除活动等',
    `request_uri`    VARCHAR(255)             COMMENT '请求URI',
    `request_method` VARCHAR(10)              COMMENT 'HTTP方法：GET/POST等',
    `class_method`   VARCHAR(255)             COMMENT '执行的类方法签名',
    `request_params` TEXT                     COMMENT '请求参数JSON',
    `response_body`  TEXT                     COMMENT '响应结果摘要（可选）',
    `success_flag`   TINYINT(1)      NOT NULL DEFAULT 1 COMMENT '是否成功：1成功 0失败',
    `error_msg`      VARCHAR(500)             COMMENT '错误信息',

    `cost_ms`        INT             NOT NULL DEFAULT 0 COMMENT '接口耗时（毫秒）',
    `ip_address`     VARCHAR(50)              COMMENT '请求IP',
    `user_agent`     VARCHAR(255)             COMMENT 'User-Agent',

    `is_deleted`     TINYINT(1)      NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否 1是',
    `create_time`    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    TIMESTAMP       NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    KEY `idx_oplog_create_time`(`create_time`),
    KEY `idx_oplog_operator`(`operator_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作及接口性能日志表';

-- ===========================
-- 6. 站点访问统计 & 系统监控
-- ===========================

-- 整站访问量统计（日粒度）
DROP TABLE IF EXISTS `t_site_visit_stats`;
CREATE TABLE `t_site_visit_stats` (
    `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `stat_date`      DATE            NOT NULL COMMENT '统计日期',
    `pv_count`       BIGINT          NOT NULL DEFAULT 0 COMMENT 'PV（页面访问量）',
    `uv_count`       BIGINT          NOT NULL DEFAULT 0 COMMENT 'UV（独立访客数）',
    `ip_count`       BIGINT          NOT NULL DEFAULT 0 COMMENT 'IP 数量',
    `remark`         VARCHAR(255)             COMMENT '备注',

    `is_deleted`     TINYINT(1)      NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否 1是',
    `create_time`    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    TIMESTAMP       NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_site_visit_date`(`stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='站点访问量统计表（日统计）';

-- 系统资源监控快照表
DROP TABLE IF EXISTS `t_system_metric`;
CREATE TABLE `t_system_metric` (
    `id`              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `host`            VARCHAR(100)    NOT NULL COMMENT '主机名或IP',
    `cpu_usage`       DECIMAL(5,2)    NOT NULL COMMENT 'CPU使用率(%)',
    `memory_usage`    DECIMAL(5,2)    NOT NULL COMMENT '内存使用率(%)',
    `disk_usage`      DECIMAL(5,2)    NOT NULL COMMENT '磁盘使用率(%)',
    `load_avg`        VARCHAR(50)              COMMENT '系统平均负载（可字符串表示1/5/15分钟）',
    `remark`          VARCHAR(255)             COMMENT '备注',

    `is_deleted`      TINYINT(1)      NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否 1是',
    `create_time`     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     TIMESTAMP       NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    KEY `idx_sys_metric_host_time`(`host`,`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统资源监控快照表';

-- Redis 性能监测历史
DROP TABLE IF EXISTS `t_redis_benchmark`;
CREATE TABLE `t_redis_benchmark` (
    `id`               BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `metric_time`      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '采集时间',
    `pool_active`      INT                      DEFAULT NULL COMMENT '连接池活跃连接数',
    `pool_idle`        INT                      DEFAULT NULL COMMENT '连接池空闲连接数',
    `pool_waiting`     INT                      DEFAULT NULL COMMENT '连接池等待线程数',
    `hit_rate`         DECIMAL(5,2)             DEFAULT NULL COMMENT '命中率(%)',
    `keyspace_hits`    BIGINT                   DEFAULT NULL COMMENT 'Keyspace Hits',
    `keyspace_misses`  BIGINT                   DEFAULT NULL COMMENT 'Keyspace Misses',
    `total_commands`   BIGINT                   DEFAULT NULL COMMENT '命令执行总数',
    `avg_command_usec` DECIMAL(10,2)            DEFAULT NULL COMMENT '平均耗时(微秒)',
    `max_command_usec` DECIMAL(10,2)            DEFAULT NULL COMMENT '最大耗时(微秒)',
    `remark`           VARCHAR(255)             DEFAULT NULL COMMENT '备注',

    `is_deleted`       TINYINT(1)      NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否 1是',
    `create_time`      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      TIMESTAMP       NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    KEY `idx_redis_metric_time`(`metric_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Redis 性能监测历史表';
