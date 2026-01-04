-- 数据库 `aftourism_server` 结构导出脚本（仅结构, 不含数据）
-- 导出时间: 自动生成


-- ----------------------------------------
-- 表 `t_activity` 的结构定义
-- ----------------------------------------
CREATE TABLE `t_activity` (
                              `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                              `name` varchar(100) NOT NULL COMMENT '活动名称',
                              `cover_url` varchar(255) DEFAULT NULL COMMENT '活动封面图',
                              `start_time` datetime NOT NULL COMMENT '开始时间',
                              `end_time` datetime NOT NULL COMMENT '结束时间',
                              `category` varchar(50) DEFAULT NULL COMMENT '类别：展览/演出/讲座等',
                              `venue_id` bigint(20) unsigned NOT NULL COMMENT '关联场馆ID',
                              `organizer` varchar(100) DEFAULT NULL COMMENT '主办单位',
                              `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
                              `intro` text COMMENT '活动简介',
                              `address_cache` varchar(255) DEFAULT NULL COMMENT '场馆地址快照（冗余，便于按地址查询）',
                              `online_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '启停状态：1启用 0暂停',
                              `view_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '浏览量',
                              `favorite_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '收藏数量',
                              `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0否 1是',
                              `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`id`),
                              KEY `idx_activity_venue` (`venue_id`),
                              KEY `idx_activity_time` (`start_time`,`end_time`),
                              KEY `idx_activity_name` (`name`),
                              CONSTRAINT `fk_activity_venue` FOREIGN KEY (`venue_id`) REFERENCES `t_venue` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='特色活动表';

-- ----------------------------------------
-- 表 `t_activity_apply` 的结构定义
-- ----------------------------------------
CREATE TABLE `t_activity_apply` (
                                    `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                                    `name` varchar(100) NOT NULL COMMENT '活动名称',
                                    `cover_url` varchar(255) DEFAULT NULL COMMENT '活动封面图',
                                    `start_time` datetime NOT NULL COMMENT '开始时间',
                                    `end_time` datetime NOT NULL COMMENT '结束时间',
                                    `category` varchar(50) DEFAULT NULL COMMENT '类别：展览/演出/讲座等',
                                    `venue_id` bigint(20) unsigned NOT NULL COMMENT '关联场馆ID',
                                    `organizer` varchar(100) DEFAULT NULL COMMENT '主办单位',
                                    `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
                                    `intro` text COMMENT '活动简介',
                                    `address_cache` varchar(255) DEFAULT NULL COMMENT '场馆地址快照',
                                    `apply_user_id` bigint(20) unsigned NOT NULL COMMENT '申报用户ID（前台用户）',
                                    `apply_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '审核状态：0待审核 1通过 2不通过',
                                    `reject_reason` varchar(255) DEFAULT NULL COMMENT '不通过原因',
                                    `audit_remark` varchar(255) DEFAULT NULL COMMENT '审核备注',
                                    `activity_id` bigint(20) unsigned DEFAULT NULL COMMENT '审核通过后对应的活动ID',
                                    `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0否 1是',
                                    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    PRIMARY KEY (`id`),
                                    KEY `idx_apply_status` (`apply_status`),
                                    KEY `idx_apply_venue` (`venue_id`),
                                    KEY `idx_apply_time` (`start_time`,`end_time`),
                                    KEY `fk_apply_user` (`apply_user_id`),
                                    KEY `fk_apply_activity` (`activity_id`),
                                    CONSTRAINT `fk_apply_activity` FOREIGN KEY (`activity_id`) REFERENCES `t_activity` (`id`),
                                    CONSTRAINT `fk_apply_user` FOREIGN KEY (`apply_user_id`) REFERENCES `t_user` (`id`),
                                    CONSTRAINT `fk_apply_venue` FOREIGN KEY (`venue_id`) REFERENCES `t_venue` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='活动申报审核表';

-- ----------------------------------------
-- 表 `t_activity_comment` 的结构定义
-- ----------------------------------------
CREATE TABLE `t_activity_comment` (
                                      `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                                      `activity_id` bigint(20) unsigned NOT NULL COMMENT '活动ID',
                                      `user_id` bigint(20) unsigned NOT NULL COMMENT '留言用户ID',
                                      `content` varchar(500) NOT NULL COMMENT '留言内容',
                                      `parent_id` bigint(20) unsigned DEFAULT NULL COMMENT '父留言ID（可为空，实现楼中楼）',
                                      `like_count` int(11) NOT NULL DEFAULT '0' COMMENT '点赞数',
                                      `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0否 1是',
                                      `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      PRIMARY KEY (`id`),
                                      KEY `idx_comment_activity` (`activity_id`),
                                      KEY `idx_comment_user` (`user_id`),
                                      CONSTRAINT `fk_comment_activity` FOREIGN KEY (`activity_id`) REFERENCES `t_activity` (`id`),
                                      CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='活动留言表';

-- ----------------------------------------
-- 表 `t_admin` 的结构定义
-- ----------------------------------------
CREATE TABLE `t_admin` (
                           `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                           `username` varchar(50) NOT NULL COMMENT '管理员账号（唯一）',
                           `password` varchar(100) NOT NULL COMMENT 'BCrypt 加密密码',
                           `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
                           `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
                           `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
                           `role_code` varchar(100) NOT NULL DEFAULT 'ADMIN' COMMENT '角色编码集合，逗号分隔',
                           `is_super` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否超级管理员：1是 0否',
                           `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：1启用 0禁用',
                           `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                           `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0否 1是',
                           `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           `avatar` varchar(255) DEFAULT NULL COMMENT '头像地址',
                           `introduction` varchar(255) DEFAULT NULL COMMENT '个人介绍',
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `uk_admin_username` (`username`),
                           KEY `idx_admin_role_code` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='后台管理员表';

-- ----------------------------------------
-- 表 `t_home_banner` 的结构定义
-- ----------------------------------------
CREATE TABLE `t_home_banner` (
                                 `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                                 `title` varchar(100) DEFAULT NULL COMMENT '轮播标题',
                                 `image_url` varchar(255) NOT NULL COMMENT '图片地址',
                                 `link_url` varchar(255) DEFAULT NULL COMMENT '跳转链接',
                                 `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序值，越大越靠前',
                                 `is_enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用：1展示 0隐藏',
                                 `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0否 1是',
                                 `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`id`),
                                 KEY `idx_banner_sort` (`sort`),
                                 KEY `idx_banner_enabled` (`is_enabled`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='首页轮播图配置表';

-- ----------------------------------------
-- 表 `t_home_intro` 的结构定义
-- ----------------------------------------
CREATE TABLE `t_home_intro` (
                                `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                                `title` varchar(200) DEFAULT NULL COMMENT '简介标题',
                                `content` text NOT NULL COMMENT '文旅简介内容',
                                `cover_url` varchar(255) DEFAULT NULL COMMENT '简介配图/视频地址',
                                `cover_type` varchar(20) NOT NULL DEFAULT 'IMAGE' COMMENT '封面类型：IMAGE 图片，VIDEO 视频',
                                `scenic_limit` int(11) DEFAULT '6' COMMENT '首页风景展示数量上限',
                                `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0否 1是',
                                `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='门户首页文旅简介';

-- ----------------------------------------
-- 表 `t_home_scenic` 的结构定义
-- ----------------------------------------
CREATE TABLE `t_home_scenic` (
                                 `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                                 `scenic_id` bigint(20) unsigned NOT NULL COMMENT '景区ID',
                                 `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序值，越大越靠前',
                                 `is_enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用：1是 0否',
                                 `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0否 1是',
                                 `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`id`),
                                 KEY `idx_home_scenic_sort` (`sort`),
                                 KEY `idx_home_scenic_ref` (`scenic_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='门户首页风景配置表';

-- ----------------------------------------
-- 表 `t_menu` 的结构定义
-- ----------------------------------------
CREATE TABLE `t_menu` (
                          `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                          `parent_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '父级菜单ID，0表示根节点',
                          `name` varchar(100) DEFAULT NULL COMMENT '路由名称',
                          `path` varchar(255) NOT NULL COMMENT '前端路由路径',
                          `redirect` varchar(255) DEFAULT NULL COMMENT '重定向路径',
                          `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
                          `title` varchar(200) NOT NULL COMMENT '菜单标题（meta.title）',
                          `icon` varchar(100) DEFAULT NULL COMMENT '菜单图标',
                          `is_hide` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否在菜单中隐藏',
                          `is_hide_tab` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否在标签页中隐藏',
                          `show_badge` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否展示徽章',
                          `show_text_badge` varchar(100) DEFAULT NULL COMMENT '徽章文本',
                          `keep_alive` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否缓存页面',
                          `fixed_tab` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否固定标签页',
                          `active_path` varchar(255) DEFAULT NULL COMMENT '激活的路径',
                          `link` varchar(255) DEFAULT NULL COMMENT '外链',
                          `is_iframe` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否为iframe页面',
                          `is_full_page` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否为全屏页面',
                          `is_first_level` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否一级菜单',
                          `parent_path` varchar(255) DEFAULT NULL COMMENT '父级路径缓存（meta.parentPath）',
                          `order_num` int(11) NOT NULL DEFAULT '0' COMMENT '排序号，越大越靠前',
                          `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：1启用 0禁用',
                          `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                          `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0否 1是',
                          `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          PRIMARY KEY (`id`),
                          KEY `idx_menu_parent` (`parent_id`),
                          KEY `idx_menu_path` (`path`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='后台菜单表';

-- ----------------------------------------
-- 表 `t_menu_permission` 的结构定义
-- ----------------------------------------
CREATE TABLE `t_menu_permission` (
                                     `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                                     `menu_id` bigint(20) unsigned NOT NULL COMMENT '关联菜单ID',
                                     `title` varchar(100) NOT NULL COMMENT '权限显示名称',
                                     `auth_mark` varchar(100) NOT NULL COMMENT '权限标识',
                                     `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                                     `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序值',
                                     `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                     PRIMARY KEY (`id`),
                                     UNIQUE KEY `uk_menu_permission_mark` (`menu_id`,`auth_mark`),
                                     CONSTRAINT `fk_menu_permission_menu` FOREIGN KEY (`menu_id`) REFERENCES `t_menu` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单按钮/操作权限表';

-- ----------------------------------------
-- 表 `t_news` 的结构定义
-- ----------------------------------------
CREATE TABLE `t_news` (
                          `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                          `title` varchar(200) NOT NULL COMMENT '新闻标题',
                          `content` longtext NOT NULL COMMENT '新闻内容（富文本）',
                          `cover_url` varchar(255) DEFAULT NULL COMMENT '封面图地址',
                          `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
                          `author` varchar(50) DEFAULT NULL COMMENT '作者',
                          `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：1发布 0下线',
                          `view_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '浏览量',
                          `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0否 1是',
                          `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          PRIMARY KEY (`id`),
                          KEY `idx_news_publish_time` (`publish_time`),
                          KEY `idx_news_title` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='新闻表';

-- ----------------------------------------
-- 表 `t_notice` 的结构定义
-- ----------------------------------------
CREATE TABLE `t_notice` (
                            `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                            `title` varchar(200) NOT NULL COMMENT '通知标题',
                            `content` longtext NOT NULL COMMENT '通知内容',
                            `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
                            `author` varchar(50) DEFAULT NULL COMMENT '发布人/发布单位',
                            `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：1发布 0下线',
                            `view_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '浏览量',
                            `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0否 1是',
                            `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`id`),
                            KEY `idx_notice_publish_time` (`publish_time`),
                            KEY `idx_notice_title` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='通知公告表';

-- ----------------------------------------
-- 表 `t_operation_log` 的结构定义
-- ----------------------------------------
CREATE TABLE `t_operation_log` (
                                   `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                                   `trace_id` varchar(64) DEFAULT NULL COMMENT '链路ID/追踪ID',
                                   `operator_id` bigint(20) unsigned DEFAULT NULL COMMENT '操作人ID（管理员或用户）',
                                   `operator_type` varchar(20) DEFAULT NULL COMMENT '操作者类型：ADMIN/USER/SYSTEM',
                                   `module_name` varchar(100) DEFAULT NULL COMMENT '模块名称：如新闻管理/活动管理',
                                   `operation_name` varchar(100) DEFAULT NULL COMMENT '操作名称：新增新闻/删除活动等',
                                   `request_uri` varchar(255) DEFAULT NULL COMMENT '请求URI',
                                   `request_method` varchar(10) DEFAULT NULL COMMENT 'HTTP方法：GET/POST等',
                                   `class_method` varchar(255) DEFAULT NULL COMMENT '执行的类方法签名',
                                   `request_params` text COMMENT '请求参数JSON',
                                   `response_body` text COMMENT '响应结果摘要（可选）',
                                   `success_flag` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否成功：1成功 0失败',
                                   `error_msg` varchar(500) DEFAULT NULL COMMENT '错误信息',
                                   `cost_ms` int(11) NOT NULL DEFAULT '0' COMMENT '接口耗时（毫秒）',
                                   `ip_address` varchar(50) DEFAULT NULL COMMENT '请求IP',
                                   `user_agent` varchar(255) DEFAULT NULL COMMENT 'User-Agent',
                                   `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0否 1是',
                                   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   PRIMARY KEY (`id`),
                                   KEY `idx_oplog_create_time` (`create_time`),
                                   KEY `idx_oplog_operator` (`operator_id`)
) ENGINE=InnoDB AUTO_INCREMENT=52137 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作及接口性能日志表';

-- ----------------------------------------
-- 表 `t_redis_benchmark` 的结构定义
-- ----------------------------------------
CREATE TABLE `t_redis_benchmark` (
                                     `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                                     `metric_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '采集时间',
                                     `pool_active` int(11) DEFAULT NULL COMMENT '连接池活跃连接数',
                                     `pool_idle` int(11) DEFAULT NULL COMMENT '连接池空闲连接数',
                                     `pool_waiting` int(11) DEFAULT NULL COMMENT '连接池等待线程数',
                                     `hit_rate` decimal(5,2) DEFAULT NULL COMMENT '命中率(%)',
                                     `keyspace_hits` bigint(20) DEFAULT NULL COMMENT 'Keyspace Hits',
                                     `keyspace_misses` bigint(20) DEFAULT NULL COMMENT 'Keyspace Misses',
                                     `total_commands` bigint(20) DEFAULT NULL COMMENT '命令执行总数',
                                     `avg_command_usec` decimal(10,2) DEFAULT NULL COMMENT '平均耗时(微秒)',
                                     `max_command_usec` decimal(10,2) DEFAULT NULL COMMENT '最大耗时(微秒)',
                                     `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                                     `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0否 1是',
                                     `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                     PRIMARY KEY (`id`),
                                     KEY `idx_redis_metric_time` (`metric_time`)
) ENGINE=InnoDB AUTO_INCREMENT=4253 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Redis 性能监测历史表';

-- ----------------------------------------
-- 表 `t_role_access` 的结构定义
-- ----------------------------------------
CREATE TABLE `t_role_access` (
                                 `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                                 `role_code` varchar(100) NOT NULL COMMENT '角色编码',
                                 `resource_key` varchar(100) NOT NULL COMMENT '资源键，例如 NEWS/NOTICE',
                                 `action` varchar(100) NOT NULL COMMENT '动作键，例如 CREATE/READ',
                                 `allow` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否允许：1允许 0拒绝',
                                 `remark` varchar(255) DEFAULT NULL COMMENT '备注信息',
                                 `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `uk_role_resource_action` (`role_code`,`resource_key`,`action`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色-资源-动作权限表';

-- ----------------------------------------
-- 表 `t_role_menu` 的结构定义
-- ----------------------------------------
CREATE TABLE `t_role_menu` (
                               `role_code` varchar(100) NOT NULL COMMENT '角色编码',
                               `menu_id` bigint(20) unsigned NOT NULL COMMENT '菜单ID',
                               `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               PRIMARY KEY (`role_code`,`menu_id`),
                               KEY `idx_role_menu_menu` (`menu_id`),
                               CONSTRAINT `fk_role_menu_menu` FOREIGN KEY (`menu_id`) REFERENCES `t_menu` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色-菜单授权表';

-- ----------------------------------------
-- 表 `t_role_menu_permission` 的结构定义
-- ----------------------------------------
CREATE TABLE `t_role_menu_permission` (
                                          `role_code` varchar(100) NOT NULL COMMENT '角色编码',
                                          `permission_id` bigint(20) unsigned NOT NULL COMMENT '按钮/操作权限ID',
                                          `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                          PRIMARY KEY (`role_code`,`permission_id`),
                                          KEY `idx_role_permission_id` (`permission_id`),
                                          CONSTRAINT `fk_role_permission_permission` FOREIGN KEY (`permission_id`) REFERENCES `t_menu_permission` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色-按钮权限授权表';

-- ----------------------------------------
-- 表 `t_scenic_spot` 的结构定义
-- ----------------------------------------
CREATE TABLE `t_scenic_spot` (
                                 `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                                 `name` varchar(100) NOT NULL COMMENT '景区名称',
                                 `image_url` varchar(255) DEFAULT NULL COMMENT '景区图片',
                                 `level` varchar(20) DEFAULT NULL COMMENT '等级：如5A/4A等',
                                 `ticket_price` decimal(10,2) DEFAULT NULL COMMENT '门票价格',
                                 `address` varchar(255) DEFAULT NULL COMMENT '地址',
                                 `open_time` varchar(100) DEFAULT NULL COMMENT '开放时间描述',
                                 `intro` text COMMENT '简介',
                                 `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
                                 `website` varchar(255) DEFAULT NULL COMMENT '景区官网',
                                 `longitude` decimal(10,6) DEFAULT NULL COMMENT '经度',
                                 `latitude` decimal(10,6) DEFAULT NULL COMMENT '纬度',
                                 `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序值，越大越靠前',
                                 `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0否 1是',
                                 `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 `view_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '浏览量',
                                 PRIMARY KEY (`id`),
                                 KEY `idx_scenic_name` (`name`),
                                 KEY `idx_scenic_address` (`address`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='A级景区表';

-- ----------------------------------------
-- 表 `t_site_visit_stats` 的结构定义
-- ----------------------------------------
CREATE TABLE `t_site_visit_stats` (
                                      `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                                      `stat_date` date NOT NULL COMMENT '统计日期',
                                      `pv_count` bigint(20) NOT NULL DEFAULT '0' COMMENT 'PV（页面访问量）',
                                      `uv_count` bigint(20) NOT NULL DEFAULT '0' COMMENT 'UV（独立访客数）',
                                      `ip_count` bigint(20) NOT NULL DEFAULT '0' COMMENT 'IP 数量',
                                      `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                                      `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0否 1是',
                                      `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `uk_site_visit_date` (`stat_date`)
) ENGINE=InnoDB AUTO_INCREMENT=51389 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='站点访问量统计表（日统计）';

-- ----------------------------------------
-- 表 `t_system_metric` 的结构定义
-- ----------------------------------------
CREATE TABLE `t_system_metric` (
                                   `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                                   `host` varchar(100) NOT NULL COMMENT '主机名或IP',
                                   `cpu_usage` decimal(5,2) NOT NULL COMMENT 'CPU使用率(%)',
                                   `memory_usage` decimal(5,2) NOT NULL COMMENT '内存使用率(%)',
                                   `disk_usage` decimal(5,2) NOT NULL COMMENT '磁盘使用率(%)',
                                   `load_avg` varchar(50) DEFAULT NULL COMMENT '系统平均负载（可字符串表示1/5/15分钟）',
                                   `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                                   `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0否 1是',
                                   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   PRIMARY KEY (`id`),
                                   KEY `idx_sys_metric_host_time` (`host`,`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统资源监控快照表';

-- ----------------------------------------
-- 表 `t_system_setting` 的结构定义
-- ----------------------------------------
CREATE TABLE `t_system_setting` (
                                    `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                                    `setting_key` varchar(100) NOT NULL COMMENT '配置键',
                                    `setting_value` text COMMENT '配置值',
                                    `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                                    `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0否 1是',
                                    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `uk_system_setting_key` (`setting_key`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统配置表';

-- ----------------------------------------
-- 表 `t_user` 的结构定义
-- ----------------------------------------
CREATE TABLE `t_user` (
                          `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                          `username` varchar(50) NOT NULL COMMENT '登录账号（唯一）',
                          `password` varchar(100) NOT NULL COMMENT 'BCrypt 加密密码',
                          `nickname` varchar(50) DEFAULT NULL COMMENT '昵称/姓名',
                          `gender` varchar(10) NOT NULL DEFAULT '未知' COMMENT '性别：男/女/未知',
                          `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
                          `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
                          `avatar` varchar(255) DEFAULT NULL COMMENT '头像地址',
                          `role_code` varchar(100) NOT NULL DEFAULT 'PORTAL_USER' COMMENT '角色编码，默认门户普通用户',
                          `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：1启用 0禁用',
                          `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                          `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0否 1是',
                          `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `uk_user_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='前台用户表';

-- ----------------------------------------
-- 表 `t_user_favorite` 的结构定义
-- ----------------------------------------
CREATE TABLE `t_user_favorite` (
                                   `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                                   `user_id` bigint(20) unsigned NOT NULL COMMENT '用户ID',
                                   `target_type` varchar(20) NOT NULL COMMENT '收藏类型：SCENIC/ VENUE/ ACTIVITY',
                                   `target_id` bigint(20) unsigned NOT NULL COMMENT '目标ID：景区/场馆/活动的ID',
                                   `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0否 1是',
                                   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `uk_user_fav` (`user_id`,`target_type`,`target_id`),
                                   KEY `idx_user_fav_user` (`user_id`),
                                   CONSTRAINT `fk_fav_user` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户收藏表';

-- ----------------------------------------
-- 表 `t_venue` 的结构定义
-- ----------------------------------------
CREATE TABLE `t_venue` (
                           `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                           `name` varchar(100) NOT NULL COMMENT '场馆名称',
                           `image_url` varchar(255) DEFAULT NULL COMMENT '场馆图片',
                           `category` varchar(50) DEFAULT NULL COMMENT '类别：博物馆/文化馆/体育馆等',
                           `is_free` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否免费开放：1是 0否',
                           `ticket_price` decimal(10,2) DEFAULT NULL COMMENT '门票价格（如不免费）',
                           `address` varchar(255) DEFAULT NULL COMMENT '地址',
                           `open_time` varchar(100) DEFAULT NULL COMMENT '开放时间描述',
                           `description` text COMMENT '描述',
                           `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
                           `website` varchar(255) DEFAULT NULL COMMENT '官网',
                           `longitude` decimal(10,6) DEFAULT NULL COMMENT '经度',
                           `latitude` decimal(10,6) DEFAULT NULL COMMENT '纬度',
                           `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序值',
                           `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0否 1是',
                           `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           `view_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '浏览量',
                           PRIMARY KEY (`id`),
                           KEY `idx_venue_name` (`name`),
                           KEY `idx_venue_address` (`address`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='场馆表';
