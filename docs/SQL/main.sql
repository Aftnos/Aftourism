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
    `gender`       VARCHAR(10)     NOT NULL DEFAULT '未知' COMMENT '性别：男/女/未知',
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

-- 菜单表（对接前端动态路由）
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu` (
    `id`              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `parent_id`       BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '父级菜单ID，0表示根节点',
    `name`            VARCHAR(100)            COMMENT '路由名称',
    `path`            VARCHAR(255)   NOT NULL COMMENT '前端路由路径',
    `redirect`        VARCHAR(255)            COMMENT '重定向路径',
    `component`       VARCHAR(255)            COMMENT '组件路径',

    `title`           VARCHAR(200)   NOT NULL COMMENT '菜单标题（meta.title）',
    `icon`            VARCHAR(100)            COMMENT '菜单图标',
    `is_hide`         TINYINT(1)     NOT NULL DEFAULT 0 COMMENT '是否在菜单中隐藏',
    `is_hide_tab`     TINYINT(1)     NOT NULL DEFAULT 0 COMMENT '是否在标签页中隐藏',
    `show_badge`      TINYINT(1)     NOT NULL DEFAULT 0 COMMENT '是否展示徽章',
    `show_text_badge` VARCHAR(100)            COMMENT '徽章文本',
    `keep_alive`      TINYINT(1)     NOT NULL DEFAULT 0 COMMENT '是否缓存页面',
    `fixed_tab`       TINYINT(1)     NOT NULL DEFAULT 0 COMMENT '是否固定标签页',
    `active_path`     VARCHAR(255)            COMMENT '激活的路径',
    `link`            VARCHAR(255)            COMMENT '外链',
    `is_iframe`       TINYINT(1)     NOT NULL DEFAULT 0 COMMENT '是否为iframe页面',
    `is_full_page`    TINYINT(1)     NOT NULL DEFAULT 0 COMMENT '是否为全屏页面',
    `is_first_level`  TINYINT(1)     NOT NULL DEFAULT 0 COMMENT '是否一级菜单',
    `parent_path`     VARCHAR(255)            COMMENT '父级路径缓存（meta.parentPath）',

    `order_num`       INT            NOT NULL DEFAULT 0 COMMENT '排序号，越大越靠前',
    `status`          TINYINT(1)     NOT NULL DEFAULT 1 COMMENT '状态：1启用 0禁用',
    `remark`          VARCHAR(255)            COMMENT '备注',
    `is_deleted`      TINYINT(1)     NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否 1是',
    `create_time`     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     TIMESTAMP      NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    KEY `idx_menu_parent`(`parent_id`),
    KEY `idx_menu_path`(`path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='后台菜单表';

-- 菜单按钮/操作权限表（对应前端 meta.authList）
DROP TABLE IF EXISTS `t_menu_permission`;
CREATE TABLE `t_menu_permission` (
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `menu_id`     BIGINT UNSIGNED NOT NULL COMMENT '关联菜单ID',
    `title`       VARCHAR(100)    NOT NULL COMMENT '权限显示名称',
    `auth_mark`   VARCHAR(100)    NOT NULL COMMENT '权限标识',
    `remark`      VARCHAR(255)             COMMENT '备注',
    `sort`        INT             NOT NULL DEFAULT 0 COMMENT '排序值',
    `create_time` TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP       NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_menu_permission_mark`(`menu_id`, `auth_mark`),
    CONSTRAINT `fk_menu_permission_menu` FOREIGN KEY (`menu_id`) REFERENCES `t_menu` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单按钮/操作权限表';

-- 角色-菜单授权表
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu` (
    `role_code`   VARCHAR(100)    NOT NULL COMMENT '角色编码',
    `menu_id`     BIGINT UNSIGNED NOT NULL COMMENT '菜单ID',
    `create_time` TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    PRIMARY KEY (`role_code`, `menu_id`),
    KEY `idx_role_menu_menu`(`menu_id`),
    CONSTRAINT `fk_role_menu_menu` FOREIGN KEY (`menu_id`) REFERENCES `t_menu` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色-菜单授权表';

-- 角色-按钮权限授权表
DROP TABLE IF EXISTS `t_role_menu_permission`;
CREATE TABLE `t_role_menu_permission` (
    `role_code`      VARCHAR(100)    NOT NULL COMMENT '角色编码',
    `permission_id`  BIGINT UNSIGNED NOT NULL COMMENT '按钮/操作权限ID',
    `create_time`    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    PRIMARY KEY (`role_code`, `permission_id`),
    KEY `idx_role_permission_id`(`permission_id`),
    CONSTRAINT `fk_role_permission_permission` FOREIGN KEY (`permission_id`) REFERENCES `t_menu_permission` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色-按钮权限授权表';

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
-- 1.1 RBAC 示例测试数据（对应 docs/RBAC/RBAC.md 完整菜单示例）
-- ===========================

-- 清空相关表，避免外键约束导致插入失败
DELETE FROM `t_role_menu_permission`;
DELETE FROM `t_role_menu`;
DELETE FROM `t_menu_permission`;
DELETE FROM `t_menu`;

-- 菜单示例数据
INSERT INTO `t_menu` (`id`,`parent_id`,`name`,`path`,`redirect`,`component`,`title`,`icon`,`is_hide`,`is_hide_tab`,`show_badge`,`show_text_badge`,`keep_alive`,`fixed_tab`,`active_path`,`link`,`is_iframe`,`is_full_page`,`is_first_level`,`parent_path`,`order_num`) VALUES
(1,0,'Dashboard','/dashboard',NULL,'/index/index','menus.dashboard.title','ri:pie-chart-line',0,0,0,NULL,0,0,NULL,NULL,0,0,0,NULL,1),
(2,1,'Console','console',NULL,'/dashboard/console','menus.dashboard.console','ri:home-smile-2-line',0,0,0,NULL,0,1,NULL,NULL,0,0,0,NULL,1),
(3,1,'Analysis','analysis',NULL,'/dashboard/analysis','menus.dashboard.analysis','ri:align-item-bottom-line',0,0,0,NULL,0,0,NULL,NULL,0,0,0,NULL,2),
(4,1,'Ecommerce','ecommerce',NULL,'/dashboard/ecommerce','menus.dashboard.ecommerce','ri:bar-chart-box-line',0,0,0,NULL,0,0,NULL,NULL,0,0,0,NULL,3),
(5,0,'Template','/template',NULL,'/index/index','menus.template.title','ri:apps-2-line',0,0,0,NULL,0,0,NULL,NULL,0,0,0,NULL,2),
(6,5,'Cards','cards',NULL,'/template/cards','menus.template.cards','ri:wallet-line',0,0,0,NULL,0,0,NULL,NULL,0,0,0,NULL,1),
(7,5,'Banners','banners',NULL,'/template/banners','menus.template.banners','ri:rectangle-line',0,0,0,NULL,0,0,NULL,NULL,0,0,0,NULL,2),
(8,5,'Charts','charts',NULL,'/template/charts','menus.template.charts','ri:bar-chart-box-line',0,0,0,NULL,0,0,NULL,NULL,0,0,0,NULL,3),
(9,5,'Map','map',NULL,'/template/map','menus.template.map','ri:map-pin-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,4),
(10,5,'Chat','chat',NULL,'/template/chat','menus.template.chat','ri:message-3-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,5),
(11,5,'Calendar','calendar',NULL,'/template/calendar','menus.template.calendar','ri:calendar-2-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,6),
(12,5,'Pricing','pricing',NULL,'/template/pricing','menus.template.pricing','ri:money-cny-box-line',0,0,0,NULL,1,0,NULL,NULL,0,1,0,NULL,7),
(13,0,'Widgets','/widgets',NULL,'/index/index','menus.widgets.title','ri:apps-2-add-line',0,0,0,NULL,0,0,NULL,NULL,0,0,0,NULL,3),
(14,13,'Icon','icon',NULL,'/widgets/icon','menus.widgets.icon','ri:palette-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,1),
(15,13,'ImageCrop','image-crop',NULL,'/widgets/image-crop','menus.widgets.imageCrop','ri:screenshot-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,2),
(16,13,'Excel','excel',NULL,'/widgets/excel','menus.widgets.excel','ri:download-2-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,3),
(17,13,'Video','video',NULL,'/widgets/video','menus.widgets.video','ri:vidicon-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,4),
(18,13,'CountTo','count-to',NULL,'/widgets/count-to','menus.widgets.countTo','ri:anthropic-line',0,0,0,NULL,0,0,NULL,NULL,0,0,0,NULL,5),
(19,13,'WangEditor','wang-editor',NULL,'/widgets/wang-editor','menus.widgets.wangEditor','ri:t-box-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,6),
(20,13,'Watermark','watermark',NULL,'/widgets/watermark','menus.widgets.watermark','ri:water-flash-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,7),
(21,13,'ContextMenu','context-menu',NULL,'/widgets/context-menu','menus.widgets.contextMenu','ri:menu-2-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,8),
(22,13,'Qrcode','qrcode',NULL,'/widgets/qrcode','menus.widgets.qrcode','ri:qr-code-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,9),
(23,13,'Drag','drag',NULL,'/widgets/drag','menus.widgets.drag','ri:drag-move-fill',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,10),
(24,13,'TextScroll','text-scroll',NULL,'/widgets/text-scroll','menus.widgets.textScroll','ri:input-method-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,11),
(25,13,'Fireworks','fireworks',NULL,'/widgets/fireworks','menus.widgets.fireworks','ri:magic-line',0,0,0,'Hot',1,0,NULL,NULL,0,0,0,NULL,12),
(26,13,'ElementUI','/outside/iframe/elementui',NULL,'','menus.widgets.elementUI','ri:apps-2-line',0,0,0,NULL,0,0,NULL,'https://element-plus.org/zh-CN/component/overview.html',1,0,0,NULL,13),
(27,0,'Examples','/examples',NULL,'/index/index','menus.examples.title','ri:sparkling-line',0,0,0,NULL,0,0,NULL,NULL,0,0,0,NULL,4),
(28,27,'Permission','permission',NULL,'','menus.examples.permission.title','ri:fingerprint-line',0,0,0,NULL,0,0,NULL,NULL,0,0,0,NULL,1),
(29,28,'PermissionSwitchRole','switch-role',NULL,'/examples/permission/switch-role','menus.examples.permission.switchRole','ri:contacts-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,1),
(30,28,'PermissionButtonAuth','button-auth',NULL,'/examples/permission/button-auth','menus.examples.permission.buttonAuth','ri:mouse-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,2),
(31,28,'PermissionPageVisibility','page-visibility',NULL,'/examples/permission/page-visibility','menus.examples.permission.pageVisibility','ri:user-3-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,3),
(32,27,'Tabs','tabs',NULL,'/examples/tabs','menus.examples.tabs','ri:price-tag-line',0,0,0,NULL,0,0,NULL,NULL,0,0,0,NULL,2),
(33,27,'TablesBasic','tables/basic',NULL,'/examples/tables/basic','menus.examples.tablesBasic','ri:layout-grid-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,3),
(34,27,'Tables','tables',NULL,'/examples/tables','menus.examples.tables','ri:table-3',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,4),
(35,27,'Forms','forms',NULL,'/examples/forms','menus.examples.forms','ri:table-view',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,5),
(36,27,'SearchBar','form/search-bar',NULL,'/examples/forms/search-bar','menus.examples.searchBar','ri:table-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,6),
(37,27,'TablesTree','tables/tree',NULL,'/examples/tables/tree','menus.examples.tablesTree','ri:layout-2-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,7),
(38,27,'SocketChat','socket-chat',NULL,'/examples/socket-chat','menus.examples.socketChat','ri:shake-hands-line',0,0,0,'New',1,0,NULL,NULL,0,0,0,NULL,8),
(39,0,'System','/system',NULL,'/index/index','menus.system.title','ri:user-3-line',0,0,0,NULL,0,0,NULL,NULL,0,0,0,NULL,5),
(40,39,'User','user',NULL,'/system/user','menus.system.user','ri:user-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,1),
(41,39,'Role','role',NULL,'/system/role','menus.system.role','ri:user-settings-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,2),
(42,39,'UserCenter','user-center',NULL,'/system/user-center','menus.system.userCenter','ri:user-line',1,1,0,NULL,1,0,NULL,NULL,0,0,0,NULL,3),
(43,39,'Menus','menu',NULL,'/system/menu','menus.system.menu','ri:menu-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,4),
(44,39,'Nested','nested',NULL,'','menus.system.nested','ri:menu-unfold-3-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,5),
(45,44,'NestedMenu1','menu1',NULL,'/system/nested/menu1','menus.system.menu1','ri:align-justify',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,1),
(46,44,'NestedMenu2','menu2',NULL,'','menus.system.menu2','ri:align-justify',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,2),
(47,46,'NestedMenu2-1','menu2-1',NULL,'/system/nested/menu2','menus.system.menu21','ri:align-justify',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,1),
(48,44,'NestedMenu3','menu3',NULL,'','menus.system.menu3','ri:align-justify',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,3),
(49,48,'NestedMenu3-1','menu3-1',NULL,'/system/nested/menu3','menus.system.menu31',NULL,0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,1),
(50,48,'NestedMenu3-2','menu3-2',NULL,'','menus.system.menu32',NULL,0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,2),
(51,50,'NestedMenu3-2-1','menu3-2-1',NULL,'/system/nested/menu3/menu3-2','menus.system.menu321',NULL,0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,1),
(52,0,'Article','/article',NULL,'/index/index','menus.article.title','ri:book-2-line',0,0,0,NULL,0,0,NULL,NULL,0,0,0,NULL,6),
(53,52,'ArticleList','article-list',NULL,'/article/list','menus.article.articleList','ri:article-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,1),
(54,52,'ArticleDetail','detail/:id',NULL,'/article/detail','menus.article.articleDetail',NULL,1,0,0,NULL,1,0,'/article/article-list',NULL,0,0,0,NULL,2),
(55,52,'ArticleComment','comment',NULL,'/article/comment','menus.article.comment','ri:mail-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,3),
(56,52,'ArticlePublish','publish',NULL,'/article/publish','menus.article.articlePublish','ri:telegram-2-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,4),
(57,0,'Result','/result',NULL,'/index/index','menus.result.title','ri:checkbox-circle-line',0,0,0,NULL,0,0,NULL,NULL,0,0,0,NULL,7),
(58,57,'ResultSuccess','success',NULL,'/result/success','menus.result.success','ri:checkbox-circle-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,1),
(59,57,'ResultFail','fail',NULL,'/result/fail','menus.result.fail','ri:close-circle-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,2),
(60,0,'Exception','/exception',NULL,'/index/index','menus.exception.title','ri:error-warning-line',0,0,0,NULL,0,0,NULL,NULL,0,0,0,NULL,8),
(61,60,'403','403',NULL,'/exception/403','menus.exception.forbidden',NULL,0,0,0,NULL,1,0,NULL,NULL,0,1,0,NULL,1),
(62,60,'404','404',NULL,'/exception/404','menus.exception.notFound',NULL,0,0,0,NULL,1,0,NULL,NULL,0,1,0,NULL,2),
(63,60,'500','500',NULL,'/exception/500','menus.exception.serverError',NULL,0,0,0,NULL,1,0,NULL,NULL,0,1,0,NULL,3),
(64,0,'Safeguard','/safeguard',NULL,'/index/index','menus.safeguard.title','ri:shield-check-line',0,0,0,NULL,0,0,NULL,NULL,0,0,0,NULL,9),
(65,64,'SafeguardServer','server',NULL,'/safeguard/server','menus.safeguard.server','ri:hard-drive-3-line',0,0,0,NULL,1,0,NULL,NULL,0,0,0,NULL,1),
(66,0,'Document','',NULL,'','menus.help.document','ri:bill-line',0,0,0,NULL,0,0,NULL,'https://www.artd.pro/docs/zh/',0,0,1,NULL,10),
(67,0,'LiteVersion','',NULL,'','menus.help.liteVersion','ri:bus-2-line',0,0,0,NULL,0,0,NULL,'https://www.artd.pro/docs/zh/guide/lite-version.html',0,0,1,NULL,11),
(68,0,'ChangeLog','/change/log',NULL,'/change/log','menus.plan.log','ri:gamepad-line',0,0,0,NULL,0,0,NULL,NULL,0,0,1,NULL,12);

-- 菜单按钮示例数据（对应 meta.authList）
INSERT INTO `t_menu_permission` (`id`,`menu_id`,`title`,`auth_mark`,`sort`) VALUES
(1,30,'新增','add',1),
(2,30,'编辑','edit',2),
(3,30,'删除','delete',3),
(4,30,'导出','export',4),
(5,30,'查看','view',5),
(6,30,'发布','publish',6),
(7,30,'配置','config',7),
(8,30,'管理','manage',8),
(9,43,'新增','add',9),
(10,43,'编辑','edit',10),
(11,43,'删除','delete',11),
(12,53,'新增','add',12),
(13,53,'编辑','edit',13),
(14,56,'发布','add',14);

-- 角色-菜单授权示例（默认无 roles 限制的菜单赋予 R_SUPER / R_ADMIN）
INSERT INTO `t_role_menu` (`role_code`,`menu_id`) VALUES
('R_SUPER',1),
('R_ADMIN',1),
('R_SUPER',2),
('R_ADMIN',2),
('R_SUPER',3),
('R_ADMIN',3),
('R_SUPER',4),
('R_ADMIN',4),
('R_SUPER',5),
('R_ADMIN',5),
('R_SUPER',6),
('R_ADMIN',6),
('R_SUPER',7),
('R_ADMIN',7),
('R_SUPER',8),
('R_ADMIN',8),
('R_SUPER',9),
('R_ADMIN',9),
('R_SUPER',10),
('R_ADMIN',10),
('R_SUPER',11),
('R_ADMIN',11),
('R_SUPER',12),
('R_ADMIN',12),
('R_SUPER',13),
('R_ADMIN',13),
('R_SUPER',14),
('R_ADMIN',14),
('R_SUPER',15),
('R_ADMIN',15),
('R_SUPER',16),
('R_ADMIN',16),
('R_SUPER',17),
('R_ADMIN',17),
('R_SUPER',18),
('R_ADMIN',18),
('R_SUPER',19),
('R_ADMIN',19),
('R_SUPER',20),
('R_ADMIN',20),
('R_SUPER',21),
('R_ADMIN',21),
('R_SUPER',22),
('R_ADMIN',22),
('R_SUPER',23),
('R_ADMIN',23),
('R_SUPER',24),
('R_ADMIN',24),
('R_SUPER',25),
('R_ADMIN',25),
('R_SUPER',26),
('R_ADMIN',26),
('R_SUPER',27),
('R_ADMIN',27),
('R_SUPER',28),
('R_ADMIN',28),
('R_SUPER',29),
('R_ADMIN',29),
('R_SUPER',30),
('R_ADMIN',30),
('R_SUPER',31),
('R_SUPER',32),
('R_ADMIN',32),
('R_SUPER',33),
('R_ADMIN',33),
('R_SUPER',34),
('R_ADMIN',34),
('R_SUPER',35),
('R_ADMIN',35),
('R_SUPER',36),
('R_ADMIN',36),
('R_SUPER',37),
('R_ADMIN',37),
('R_SUPER',38),
('R_ADMIN',38),
('R_SUPER',39),
('R_ADMIN',39),
('R_SUPER',40),
('R_ADMIN',40),
('R_SUPER',41),
('R_SUPER',42),
('R_ADMIN',42),
('R_SUPER',43),
('R_SUPER',44),
('R_ADMIN',44),
('R_SUPER',45),
('R_ADMIN',45),
('R_SUPER',46),
('R_ADMIN',46),
('R_SUPER',47),
('R_ADMIN',47),
('R_SUPER',48),
('R_ADMIN',48),
('R_SUPER',49),
('R_ADMIN',49),
('R_SUPER',50),
('R_ADMIN',50),
('R_SUPER',51),
('R_ADMIN',51),
('R_SUPER',52),
('R_ADMIN',52),
('R_SUPER',53),
('R_ADMIN',53),
('R_SUPER',54),
('R_ADMIN',54),
('R_SUPER',55),
('R_ADMIN',55),
('R_SUPER',56),
('R_ADMIN',56),
('R_SUPER',57),
('R_ADMIN',57),
('R_SUPER',58),
('R_ADMIN',58),
('R_SUPER',59),
('R_ADMIN',59),
('R_SUPER',60),
('R_ADMIN',60),
('R_SUPER',61),
('R_ADMIN',61),
('R_SUPER',62),
('R_ADMIN',62),
('R_SUPER',63),
('R_ADMIN',63),
('R_SUPER',64),
('R_ADMIN',64),
('R_SUPER',65),
('R_ADMIN',65),
('R_SUPER',66),
('R_ADMIN',66),
('R_SUPER',67),
('R_ADMIN',67),
('R_SUPER',68),
('R_ADMIN',68);

-- 角色-按钮授权示例（继承菜单访问角色）
INSERT INTO `t_role_menu_permission` (`role_code`,`permission_id`) VALUES
('R_SUPER',1),
('R_ADMIN',1),
('R_SUPER',2),
('R_ADMIN',2),
('R_SUPER',3),
('R_ADMIN',3),
('R_SUPER',4),
('R_ADMIN',4),
('R_SUPER',5),
('R_ADMIN',5),
('R_SUPER',6),
('R_ADMIN',6),
('R_SUPER',7),
('R_ADMIN',7),
('R_SUPER',8),
('R_ADMIN',8),
('R_SUPER',9),
('R_SUPER',10),
('R_SUPER',11),
('R_SUPER',12),
('R_ADMIN',12),
('R_SUPER',13),
('R_ADMIN',13),
('R_SUPER',14),
('R_ADMIN',14);
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

-- 特色活动表
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

    CONSTRAINT `fk_activity_venue` FOREIGN KEY (`venue_id`) REFERENCES `t_venue`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='特色活动表';

-- 活动申报表
DROP TABLE IF EXISTS `t_activity_apply`;
CREATE TABLE `t_activity_apply` (
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
    `address_cache`  VARCHAR(255)             COMMENT '场馆地址快照',
    `apply_user_id`  BIGINT UNSIGNED NOT NULL COMMENT '申报用户ID（前台用户）',
    `apply_status`   TINYINT(1)      NOT NULL DEFAULT 0 COMMENT '审核状态：0待审核 1通过 2不通过',
    `reject_reason`  VARCHAR(255)             COMMENT '不通过原因',
    `audit_remark`   VARCHAR(255)             COMMENT '审核备注',
    `activity_id`    BIGINT UNSIGNED          COMMENT '审核通过后对应的活动ID',
    `is_deleted`     TINYINT(1)      NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否 1是',
    `create_time`    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    TIMESTAMP       NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    KEY `idx_apply_status`(`apply_status`),
    KEY `idx_apply_venue`(`venue_id`),
    KEY `idx_apply_time`(`start_time`,`end_time`),

    CONSTRAINT `fk_apply_venue` FOREIGN KEY (`venue_id`) REFERENCES `t_venue`(`id`),
    CONSTRAINT `fk_apply_user` FOREIGN KEY (`apply_user_id`) REFERENCES `t_user`(`id`),
    CONSTRAINT `fk_apply_activity` FOREIGN KEY (`activity_id`) REFERENCES `t_activity`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动申报审核表';

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
