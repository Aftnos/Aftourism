-- 后台首页配置菜单注册 SQL，基于 t_menu 表字段顺序。
INSERT INTO `t_menu` (
  `id`, `parent_id`, `name`, `path`, `redirect`, `component`, `title`, `icon`,
  `is_hide`, `is_hide_tab`, `show_badge`, `show_text_badge`, `keep_alive`,
  `fixed_tab`, `active_path`, `link`, `is_iframe`, `is_full_page`, `is_first_level`,
  `parent_path`, `order_num`, `status`, `remark`, `is_deleted`, `create_time`, `update_time`
) VALUES
  (120, 39, 'HomeConfig', 'home-config', NULL, '/system/home-config/index', 'menus.system.homeConfig', 'ri:home-2-line',
   0, 0, 0, NULL, 1, 0, NULL, NULL, 0, 0, 0, '/system', 6, 1, '首页轮播及简介配置', 0, NOW(), NOW());
