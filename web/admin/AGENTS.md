# AGENTS.md

## 前端后台（Admin）工作约定

### 技术栈
- `Vue 3.5+`（Composition API）
- `TypeScript`：~5.6
- `Vite`：7+
- `包管理器`：pnpm
- `UI`：Element Plus 2.11+，Tailwind CSS 4
- `状态管理`：Pinia（可选持久化 `pinia-plugin-persistedstate`）
- `图表`：ECharts

### 开发规范
- 统一使用 `<script setup lang="ts">` 与组合式 API。
- 严格类型定义，避免使用 `any`。
- 样式规范：
  - 布局与工具类优先使用 `Tailwind CSS`。
  - 组件私有样式使用 `SCSS`。
  - 自定义类名遵循 `BEM`。
- 组件组织：
  - 通用组件置于 `src/components`。
  - 页面专属组件置于 `src/views/<page>/components`。
- 路由与模块：
  - 路由按模块在 `src/router/modules` 拆分维护。
  - 路由守卫在 `src/router/guards`。
- 代码质量：
  - 提交前运行 `pnpm lint`，确保 ESLint/Prettier/Stylelint 无错误。
  - 使用 `husky + lint-staged` 自动化校验与修复（如仓库已配置）。
- 资源与插件：
  - 图标使用 `@iconify/vue` 或 `@element-plus/icons-vue`。
  - 统一在 `src/plugins` 注册第三方库。

### 开发文档
# 项目结构



```
├── src
│   ├── api                     # API 接口相关代码
│   │   ├── auth.ts             # 认证相关的 API 接口定义（如登录、注册、用户信息）
│   │   └── system-manage.ts    # 系统管理相关的 API 接口定义（如菜单、用户、角色管理）
│   ├── App.vue                 # Vue 根组件，定义应用的全局结构和入口
│   ├── assets                  # 静态资源目录
│   │   ├── images              # 图片资源目录
│   │   ├── styles              # 全局样式文件
│   │   │   ├── core            # 核心样式（系统级样式）
│   │   │   ├── custom          # 自定义样式（业务级样式）
│   │   │   └── index.scss      # 样式入口文件
│   │   └── svg                 # SVG 相关资源
│   │       └── loading.ts      # 加载动画 SVG 定义
│   ├── components              # 组件目录
│   │   ├── business            # 业务组件（业务相关的自定义组件）
│   │   │   └── comment-widget  # 评论组件
│   │   └── core                # 核心组件（系统级通用组件库）
│   │       ├── banners         # 横幅组件
│   │       ├── base            # 基础组件
│   │       ├── cards           # 卡片组件
│   │       ├── charts          # 图表组件
│   │       ├── forms           # 表单组件
│   │       ├── layouts         # 布局组件
│   │       ├── media           # 媒体组件
│   │       ├── others          # 其他组件
│   │       ├── tables          # 表格组件
│   │       ├── text-effect     # 文本特效组件
│   │       ├── theme           # 主题相关组件
│   │       ├── views           # 视图组件
│   │       └── widget          # 小部件组件
│   ├── config                  # 项目配置目录
│   │   ├── assets              # 静态资源配置
│   │   │   └── images.ts       # 图片资源路径配置
│   │   ├── modules             # 模块化配置
│   │   │   ├── component.ts    # 组件配置
│   │   │   ├── fastEnter.ts    # 快捷入口配置
│   │   │   ├── festival.ts     # 节日/活动配置
│   │   │   └── headerBar.ts    # 顶部栏配置
│   │   ├── index.ts            # 配置入口文件
│   │   └── setting.ts          # 系统设置配置
│   ├── directives              # Vue 自定义指令
│   │   ├── business            # 业务指令
│   │   │   ├── highlight.ts    # 高亮指令
│   │   │   └── ripple.ts       # 波纹效果指令
│   │   ├── core                # 核心指令
│   │   │   ├── auth.ts         # 认证指令
│   │   │   └── roles.ts        # 角色权限指令
│   │   └── index.ts            # 指令入口文件
│   ├── enums                   # 枚举定义
│   │   ├── appEnum.ts          # 应用级枚举（如主题类型、语言类型）
│   │   └── formEnum.ts         # 表单相关枚举（如表单状态、验证规则）
│   ├── env.d.ts                # TypeScript 环境声明文件
│   ├── hooks                   # Vue 3 Composable 函数（可复用逻辑）
│   │   ├── core                # 核心 Hooks
│   │   │   ├── useAppMode.ts   # 应用模式相关逻辑
│   │   │   ├── useAuth.ts      # 认证相关逻辑
│   │   │   ├── useCeremony.ts  # 节日/仪式相关逻辑
│   │   │   ├── useChart.ts     # 图表相关逻辑
│   │   │   ├── useCommon.ts    # 通用逻辑
│   │   │   ├── useFastEnter.ts # 快捷入口逻辑
│   │   │   ├── useHeaderBar.ts # 顶部栏逻辑
│   │   │   ├── useLayoutHeight.ts # 布局高度计算逻辑
│   │   │   ├── useTable.ts     # 表格逻辑
│   │   │   ├── useTableColumns.ts # 表格列配置逻辑
│   │   │   ├── useTableHeight.ts # 表格高度计算逻辑
│   │   │   └── useTheme.ts     # 主题切换逻辑
│   │   └── index.ts            # Hooks 入口文件
│   ├── locales                 # 国际化（i18n）资源
│   │   ├── index.ts            # 国际化入口文件
│   │   └── langs               # 多语言文件
│   │       ├── en.json         # 英文语言包
│   │       └── zh.json         # 中文语言包
│   ├── main.ts                 # 项目主入口文件
│   ├── mock                    # Mock 数据目录
│   │   ├── json                # JSON 格式的 Mock 数据
│   │   │   └── chinaMap.json   # 中国地图数据
│   │   ├── temp                # 临时 Mock 数据
│   │   │   ├── articleList.ts  # 文章列表数据
│   │   │   ├── commentDetail.ts # 评论详情数据
│   │   │   ├── commentList.ts  # 评论列表数据
│   │   │   └── formData.ts     # 表单数据
│   │   └── upgrade             # 更新日志数据
│   │       └── changeLog.ts    # 变更日志数据
│   ├── plugins                 # 插件配置
│   │   ├── echarts.ts          # ECharts 图表库配置
│   │   └── index.ts            # 插件入口文件
│   ├── router                  # Vue Router 路由相关代码
│   │   ├── core                # 路由核心功能
│   │   │   ├── ComponentLoader.ts # 组件加载器
│   │   │   ├── IframeRouteManager.ts # Iframe 路由管理器
│   │   │   ├── MenuProcessor.ts # 菜单处理器
│   │   │   ├── RouteRegistry.ts # 路由注册器
│   │   │   ├── RouteTransformer.ts # 路由转换器
│   │   │   ├── RouteValidator.ts # 路由验证器
│   │   │   └── index.ts        # 核心功能入口
│   │   ├── guards              # 路由守卫
│   │   │   ├── afterEach.ts    # 全局后置守卫
│   │   │   └── beforeEach.ts   # 全局前置守卫
│   │   ├── modules             # 路由模块定义
│   │   │   ├── article.ts      # 文章模块路由
│   │   │   ├── dashboard.ts    # 仪表盘路由
│   │   │   ├── examples.ts     # 示例页面路由
│   │   │   ├── exception.ts    # 异常页面路由
│   │   │   ├── help.ts         # 帮助页面路由
│   │   │   ├── index.ts        # 路由模块入口
│   │   │   ├── result.ts       # 结果页面路由
│   │   │   ├── safeguard.ts    # 安全防护路由
│   │   │   ├── system.ts       # 系统管理路由
│   │   │   ├── template.ts     # 模板页面路由
│   │   │   └── widgets.ts      # 小组件路由
│   │   ├── routes              # 路由配置
│   │   │   ├── asyncRoutes.ts  # 异步路由（动态路由）
│   │   │   └── staticRoutes.ts # 静态路由（固定路由）
│   │   ├── index.ts            # 路由主入口
│   │   └── routesAlias.ts      # 路由别名定义
│   ├── store                   # Pinia 状态管理
│   │   ├── modules             # 状态管理模块
│   │   │   ├── menu.ts         # 菜单状态管理
│   │   │   ├── setting.ts      # 设置状态管理
│   │   │   ├── table.ts        # 表格状态管理
│   │   │   ├── user.ts         # 用户状态管理
│   │   │   └── worktab.ts      # 工作标签页状态管理
│   │   └── index.ts            # Pinia 入口文件
│   ├── types                   # TypeScript 类型定义
│   │   ├── api                 # API 相关类型
│   │   │   └── api.d.ts        # API 接口类型定义
│   │   ├── common              # 通用类型定义
│   │   │   ├── index.ts        # 通用类型入口
│   │   │   └── response.ts     # 响应类型定义
│   │   ├── component           # 组件相关类型
│   │   │   ├── chart.ts        # 图表组件类型
│   │   │   └── index.ts        # 组件类型入口
│   │   ├── config              # 配置相关类型
│   │   │   └── index.ts        # 配置类型定义
│   │   ├── import              # 自动导入类型声明
│   │   │   ├── auto-imports.d.ts # 自动导入的函数类型
│   │   │   └── components.d.ts # 自动导入的组件类型
│   │   ├── router              # 路由相关类型
│   │   │   └── index.ts        # 路由类型定义
│   │   ├── store               # 状态管理相关类型
│   │   │   └── index.ts        # Store 类型定义
│   │   └── index.ts            # 类型定义总入口
│   ├── utils                   # 工具函数目录
│   │   ├── constants           # 常量定义
│   │   │   ├── index.ts        # 常量入口
│   │   │   └── links.ts        # 链接常量
│   │   ├── form                # 表单相关工具
│   │   │   ├── index.ts        # 表单工具入口
│   │   │   ├── responsive.ts   # 响应式表单工具
│   │   │   └── validator.ts    # 表单验证工具
│   │   ├── http                # HTTP 请求工具
│   │   │   ├── error.ts        # 错误处理
│   │   │   ├── index.ts        # HTTP 工具入口
│   │   │   └── status.ts       # 状态码处理
│   │   ├── navigation          # 导航相关工具
│   │   │   ├── index.ts        # 导航工具入口
│   │   │   ├── jump.ts         # 页面跳转工具
│   │   │   ├── route.ts        # 路由工具
│   │   │   └── worktab.ts      # 工作标签页工具
│   │   ├── storage             # 存储相关工具
│   │   │   ├── index.ts        # 存储工具入口
│   │   │   ├── storage-config.ts # 存储配置
│   │   │   ├── storage-key-manager.ts # 存储键管理
│   │   │   └── storage.ts      # 存储工具实现
│   │   ├── sys                 # 系统相关工具
│   │   │   ├── console.ts      # 控制台工具
│   │   │   ├── error-handle.ts # 错误处理
│   │   │   ├── index.ts        # 系统工具入口
│   │   │   ├── mittBus.ts      # 事件总线
│   │   │   └── upgrade.ts      # 升级相关工具
│   │   ├── table               # 表格相关工具
│   │   │   ├── tableCache.ts   # 表格缓存
│   │   │   ├── tableConfig.ts  # 表格配置
│   │   │   └── tableUtils.ts   # 表格工具函数
│   │   ├── ui                  # UI 相关工具
│   │   │   ├── animation.ts    # 动画工具
│   │   │   ├── colors.ts       # 颜色工具
│   │   │   ├── emojo.ts        # 表情工具
│   │   │   ├── index.ts        # UI 工具入口
│   │   │   ├── loading.ts      # 加载动画工具
│   │   │   └── tabs.ts         # 标签页工具
│   │   ├── index.ts            # 工具函数总入口
│   │   └── router.ts           # 路由工具函数
│   └── views                   # 页面组件目录
├── tsconfig.json               # TypeScript 配置文件
└── vite.config.ts              # Vite 配置文件
```

# 介绍

项目组件库使用 [Element Plus](https://element-plus.org/zh-CN/component/overview.html) 提供强大支持，覆盖了 80% 的常用组件，同时系统内置了丰富的组件、模版，方便快速开发。

为了获得更好的视觉效果，本项目对 Element Plus 部分组件 UI 进行了调优

文件路径：`src/assets/styles/el-ui.scss`

## Element Plus

[![img](https://www.artd.pro/docs/assets/element_cover.CvSnER1T.png)](https://element-plus.org/)

## 系统组件

- 图标库
- 图标选择器
- 图像裁剪
- Excel 导入导出
- 视频播放器
- 数字滚动
- 富文本编辑器
- 水印
- 右键菜单
- 二维码
- 拖拽
- 文字滚动
- 礼花效果

## 系统模版

- 卡片
- 横幅
- 图表
- 地图
- 聊天
- 日历
- 定价

## 其他

如果你有更多需求，可以联系我，我会根据需求开发更多组件。

# 路由和菜单

项目提供了基础的路由系统，可以根据配置文件动态生成菜单结构以及动态注册路由。

## 路由类型

项目中的路由分为两类：**静态路由** 和 **动态路由**。

**静态路由**：在项目启动时就已确定，通常包含登录页、404 等公共页面。

**动态路由**：在用户登录后，根据后端返回的菜单数据动态生成，通常用于控制权限和个性化导航。

### 静态路由

无需权限即可访问的基础页面路由，例如：登录页、注册页、404、500 等。

配置位置：`src/router/routes/staticRoutes.ts`

**路由变量：**

ts

```
const staticRoutes: AppRouteRecordRaw[] = [];
```

### 动态路由

需要权限控制的业务页面路由，例如：用户管理、菜单管理等。

配置位置： `src/router/routes/asyncRoutes.ts`

ts

```
const asyncRoutes: MenuListType[] = [];
```

## 路由定义

静态路由与动态路由的定义方式基本一致，请看下面的示例：

### 静态路由

<details class="details custom-block" style="box-sizing: border-box; border: 1px solid rgba(0, 0, 0, 0); border-radius: 8px; padding: 16px 16px 8px; line-height: 24px; font-size: 14px; color: rgb(60, 60, 67); background-color: rgba(142, 150, 170, 0.14); margin: 16px 0px; font-family: &quot;Punctuation SC&quot;, Inter, ui-sans-serif, system-ui, &quot;PingFang SC&quot;, &quot;Noto Sans CJK SC&quot;, &quot;Noto Sans SC&quot;, &quot;Heiti SC&quot;, &quot;Microsoft YaHei&quot;, DengXian, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;, &quot;Segoe UI Symbol&quot;, &quot;Noto Color Emoji&quot;; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-align: start; text-indent: 0px; text-transform: none; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; white-space: normal; text-decoration-thickness: initial; text-decoration-style: initial; text-decoration-color: initial;"><div class="language-ts vp-adaptive-theme" style="box-sizing: border-box; position: relative; margin: 8px 0px; background-color: rgb(246, 246, 247); overflow-x: auto; transition: background-color 0.5s; border-radius: 8px;"><button title="Copy Code" class="copy" style="box-sizing: border-box; touch-action: manipulation; border-width: 0.727273px; border-style: solid; border-color: rgb(235, 235, 239); border-image: none 100% / 1 / 0 stretch; padding: 0px; line-height: inherit; color: inherit; font-family: inherit; background-color: rgb(246, 246, 247); background-image: url(&quot;data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='none' stroke='rgba(128,128,128,1)' stroke-linecap='round' stroke-linejoin='round' stroke-width='2' viewBox='0 0 24 24'%3E%3Crect width='8' height='4' x='8' y='2' rx='1' ry='1'/%3E%3Cpath d='M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2'/%3E%3C/svg%3E&quot;); cursor: pointer; direction: ltr; position: absolute; top: 12px; right: 12px; z-index: 3; border-radius: 4px; width: 40px; height: 40px; opacity: 0; background-position: 50% center; background-size: 20px; background-repeat: no-repeat; transition: border-color 0.25s, background-color 0.25s, opacity 0.25s;"></button><span class="lang" style="box-sizing: border-box; position: absolute; top: 2px; right: 8px; z-index: 2; font-size: 12px; font-weight: 500; color: rgba(60, 60, 67, 0.56); transition: color 0.4s, opacity 0.4s;"></span><pre class="shiki shiki-themes github-light github-dark vp-code" tabindex="0" style="box-sizing: border-box; font-family: ui-monospace, Menlo, Monaco, Consolas, &quot;Liberation Mono&quot;, &quot;Courier New&quot;, monospace; direction: ltr; text-align: left; white-space: pre; word-spacing: normal; word-break: normal; overflow-wrap: normal; tab-size: 4; hyphens: none; position: relative; z-index: 1; margin: 0px; padding: 20px 0px; background: transparent; overflow-x: auto;"><code style="box-sizing: border-box; font-family: ui-monospace, Menlo, Monaco, Consolas, &quot;Liberation Mono&quot;, &quot;Courier New&quot;, monospace; font-size: 12.25px; background-color: transparent; direction: ltr; text-align: left; white-space: pre; word-spacing: normal; word-break: normal; overflow-wrap: normal; tab-size: 4; hyphens: none; display: block; padding: 0px 24px; width: fit-content; min-width: 100%; line-height: 1.7; color: rgba(60, 60, 67, 0.78); transition: color 0.5s; font-weight: 400;"><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(215, 58, 73); --shiki-light: #D73A49; --shiki-dark: #F97583;"></span><span style="box-sizing: border-box; color: rgb(215, 58, 73); --shiki-light: #D73A49; --shiki-dark: #F97583;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(215, 58, 73); --shiki-light: #D73A49; --shiki-dark: #F97583;"></span><span style="box-sizing: border-box; color: rgb(111, 66, 193); --shiki-light: #6F42C1; --shiki-dark: #B392F0;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(215, 58, 73); --shiki-light: #D73A49; --shiki-dark: #F97583;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(111, 66, 193); --shiki-light: #6F42C1; --shiki-dark: #B392F0;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(215, 58, 73); --shiki-light: #D73A49; --shiki-dark: #F97583;"></span><span style="box-sizing: border-box; color: rgb(215, 58, 73); --shiki-light: #D73A49; --shiki-dark: #F97583;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(111, 66, 193); --shiki-light: #6F42C1; --shiki-dark: #B392F0;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(215, 58, 73); --shiki-light: #D73A49; --shiki-dark: #F97583;"></span><span style="box-sizing: border-box; color: rgb(215, 58, 73); --shiki-light: #D73A49; --shiki-dark: #F97583;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(111, 66, 193); --shiki-light: #6F42C1; --shiki-dark: #B392F0;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(215, 58, 73); --shiki-light: #D73A49; --shiki-dark: #F97583;"></span><span style="box-sizing: border-box; color: rgb(215, 58, 73); --shiki-light: #D73A49; --shiki-dark: #F97583;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(111, 66, 193); --shiki-light: #6F42C1; --shiki-dark: #B392F0;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(215, 58, 73); --shiki-light: #D73A49; --shiki-dark: #F97583;"></span><span style="box-sizing: border-box; color: rgb(215, 58, 73); --shiki-light: #D73A49; --shiki-dark: #F97583;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(111, 66, 193); --shiki-light: #6F42C1; --shiki-dark: #B392F0;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(215, 58, 73); --shiki-light: #D73A49; --shiki-dark: #F97583;"></span><span style="box-sizing: border-box; color: rgb(215, 58, 73); --shiki-light: #D73A49; --shiki-dark: #F97583;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(111, 66, 193); --shiki-light: #6F42C1; --shiki-dark: #B392F0;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(215, 58, 73); --shiki-light: #D73A49; --shiki-dark: #F97583;"></span><span style="box-sizing: border-box; color: rgb(215, 58, 73); --shiki-light: #D73A49; --shiki-dark: #F97583;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(111, 66, 193); --shiki-light: #6F42C1; --shiki-dark: #B392F0;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(215, 58, 73); --shiki-light: #D73A49; --shiki-dark: #F97583;"></span><span style="box-sizing: border-box; color: rgb(215, 58, 73); --shiki-light: #D73A49; --shiki-dark: #F97583;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(106, 115, 125); --shiki-light: #6A737D; --shiki-dark: #6A737D;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(111, 66, 193); --shiki-light: #6F42C1; --shiki-dark: #B392F0;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(215, 58, 73); --shiki-light: #D73A49; --shiki-dark: #F97583;"></span><span style="box-sizing: border-box; color: rgb(215, 58, 73); --shiki-light: #D73A49; --shiki-dark: #F97583;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span></code></pre></div></details>

### 动态路由

<details class="details custom-block" style="box-sizing: border-box; border: 1px solid rgba(0, 0, 0, 0); border-radius: 8px; padding: 16px 16px 8px; line-height: 24px; font-size: 14px; color: rgb(60, 60, 67); background-color: rgba(142, 150, 170, 0.14); margin: 16px 0px; font-family: &quot;Punctuation SC&quot;, Inter, ui-sans-serif, system-ui, &quot;PingFang SC&quot;, &quot;Noto Sans CJK SC&quot;, &quot;Noto Sans SC&quot;, &quot;Heiti SC&quot;, &quot;Microsoft YaHei&quot;, DengXian, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;, &quot;Segoe UI Symbol&quot;, &quot;Noto Color Emoji&quot;; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-align: start; text-indent: 0px; text-transform: none; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; white-space: normal; text-decoration-thickness: initial; text-decoration-style: initial; text-decoration-color: initial;"><div class="language-ts vp-adaptive-theme" style="box-sizing: border-box; position: relative; margin: 8px 0px; background-color: rgb(246, 246, 247); overflow-x: auto; transition: background-color 0.5s; border-radius: 8px;"><button title="Copy Code" class="copy" style="box-sizing: border-box; touch-action: manipulation; border-width: 0.727273px; border-style: solid; border-color: rgb(235, 235, 239); border-image: none 100% / 1 / 0 stretch; padding: 0px; line-height: inherit; color: inherit; font-family: inherit; background-color: rgb(246, 246, 247); background-image: url(&quot;data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='none' stroke='rgba(128,128,128,1)' stroke-linecap='round' stroke-linejoin='round' stroke-width='2' viewBox='0 0 24 24'%3E%3Crect width='8' height='4' x='8' y='2' rx='1' ry='1'/%3E%3Cpath d='M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2'/%3E%3C/svg%3E&quot;); cursor: pointer; direction: ltr; position: absolute; top: 12px; right: 12px; z-index: 3; border-radius: 4px; width: 40px; height: 40px; opacity: 0; background-position: 50% center; background-size: 20px; background-repeat: no-repeat; transition: border-color 0.25s, background-color 0.25s, opacity 0.25s;"></button><span class="lang" style="box-sizing: border-box; position: absolute; top: 2px; right: 8px; z-index: 2; font-size: 12px; font-weight: 500; color: rgba(60, 60, 67, 0.56); transition: color 0.4s, opacity 0.4s;"></span><pre class="shiki shiki-themes github-light github-dark vp-code" tabindex="0" style="box-sizing: border-box; font-family: ui-monospace, Menlo, Monaco, Consolas, &quot;Liberation Mono&quot;, &quot;Courier New&quot;, monospace; direction: ltr; text-align: left; white-space: pre; word-spacing: normal; word-break: normal; overflow-wrap: normal; tab-size: 4; hyphens: none; position: relative; z-index: 1; margin: 0px; padding: 20px 0px; background: transparent; overflow-x: auto;"><code style="box-sizing: border-box; font-family: ui-monospace, Menlo, Monaco, Consolas, &quot;Liberation Mono&quot;, &quot;Courier New&quot;, monospace; font-size: 12.25px; background-color: transparent; direction: ltr; text-align: left; white-space: pre; word-spacing: normal; word-break: normal; overflow-wrap: normal; tab-size: 4; hyphens: none; display: block; padding: 0px 24px; width: fit-content; min-width: 100%; line-height: 1.7; color: rgba(60, 60, 67, 0.78); transition: color 0.5s; font-weight: 400;"><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(215, 58, 73); --shiki-light: #D73A49; --shiki-dark: #F97583;"></span><span style="box-sizing: border-box; color: rgb(215, 58, 73); --shiki-light: #D73A49; --shiki-dark: #F97583;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(215, 58, 73); --shiki-light: #D73A49; --shiki-dark: #F97583;"></span><span style="box-sizing: border-box; color: rgb(111, 66, 193); --shiki-light: #6F42C1; --shiki-dark: #B392F0;"></span><span style="box-sizing: border-box; color: rgb(215, 58, 73); --shiki-light: #D73A49; --shiki-dark: #F97583;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span></code></pre></div></details>

### 嵌套路由

<details class="details custom-block" style="box-sizing: border-box; border: 1px solid rgba(0, 0, 0, 0); border-radius: 8px; padding: 16px 16px 8px; line-height: 24px; font-size: 14px; color: rgb(60, 60, 67); background-color: rgba(142, 150, 170, 0.14); margin: 16px 0px; font-family: &quot;Punctuation SC&quot;, Inter, ui-sans-serif, system-ui, &quot;PingFang SC&quot;, &quot;Noto Sans CJK SC&quot;, &quot;Noto Sans SC&quot;, &quot;Heiti SC&quot;, &quot;Microsoft YaHei&quot;, DengXian, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;, &quot;Segoe UI Symbol&quot;, &quot;Noto Color Emoji&quot;; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-align: start; text-indent: 0px; text-transform: none; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; white-space: normal; text-decoration-thickness: initial; text-decoration-style: initial; text-decoration-color: initial;"><div class="language-ts vp-adaptive-theme" style="box-sizing: border-box; position: relative; margin: 8px 0px; background-color: rgb(246, 246, 247); overflow-x: auto; transition: background-color 0.5s; border-radius: 8px;"><button title="Copy Code" class="copy" style="box-sizing: border-box; touch-action: manipulation; border-width: 0.727273px; border-style: solid; border-color: rgb(235, 235, 239); border-image: none 100% / 1 / 0 stretch; padding: 0px; line-height: inherit; color: inherit; font-family: inherit; background-color: rgb(246, 246, 247); background-image: url(&quot;data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='none' stroke='rgba(128,128,128,1)' stroke-linecap='round' stroke-linejoin='round' stroke-width='2' viewBox='0 0 24 24'%3E%3Crect width='8' height='4' x='8' y='2' rx='1' ry='1'/%3E%3Cpath d='M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2'/%3E%3C/svg%3E&quot;); cursor: pointer; direction: ltr; position: absolute; top: 12px; right: 12px; z-index: 3; border-radius: 4px; width: 40px; height: 40px; opacity: 0; background-position: 50% center; background-size: 20px; background-repeat: no-repeat; transition: border-color 0.25s, background-color 0.25s, opacity 0.25s;"></button><span class="lang" style="box-sizing: border-box; position: absolute; top: 2px; right: 8px; z-index: 2; font-size: 12px; font-weight: 500; color: rgba(60, 60, 67, 0.56); transition: color 0.4s, opacity 0.4s;"></span><pre class="shiki shiki-themes github-light github-dark vp-code" tabindex="0" style="box-sizing: border-box; font-family: ui-monospace, Menlo, Monaco, Consolas, &quot;Liberation Mono&quot;, &quot;Courier New&quot;, monospace; direction: ltr; text-align: left; white-space: pre; word-spacing: normal; word-break: normal; overflow-wrap: normal; tab-size: 4; hyphens: none; position: relative; z-index: 1; margin: 0px; padding: 20px 0px; background: transparent; overflow-x: auto;"><code style="box-sizing: border-box; font-family: ui-monospace, Menlo, Monaco, Consolas, &quot;Liberation Mono&quot;, &quot;Courier New&quot;, monospace; font-size: 12.25px; background-color: transparent; direction: ltr; text-align: left; white-space: pre; word-spacing: normal; word-break: normal; overflow-wrap: normal; tab-size: 4; hyphens: none; display: block; padding: 0px 24px; width: fit-content; min-width: 100%; line-height: 1.7; color: rgba(60, 60, 67, 0.78); transition: color 0.5s; font-weight: 400;"><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(111, 66, 193); --shiki-light: #6F42C1; --shiki-dark: #B392F0;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(111, 66, 193); --shiki-light: #6F42C1; --shiki-dark: #B392F0;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(111, 66, 193); --shiki-light: #6F42C1; --shiki-dark: #B392F0;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(111, 66, 193); --shiki-light: #6F42C1; --shiki-dark: #B392F0;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(111, 66, 193); --shiki-light: #6F42C1; --shiki-dark: #B392F0;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(111, 66, 193); --shiki-light: #6F42C1; --shiki-dark: #B392F0;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(111, 66, 193); --shiki-light: #6F42C1; --shiki-dark: #B392F0;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(111, 66, 193); --shiki-light: #6F42C1; --shiki-dark: #B392F0;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(3, 47, 98); --shiki-light: #032F62; --shiki-dark: #9ECBFF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span></code></pre></div></details>

## 新建页面

你只需要在 /src/views/ 目录下创建一个页面，如：/src/views/safeguard/Server.vue

ts

```
<template>
  <div class="page-content">
    <h1>test page</h1>
  </div>
</template>
```

**tips**：上面的例子中我们添加了一个页面，页面 `class="page-content"` 这个类名可以将盒子最小高度始终撑满屏幕剩余高度

## 路由注册

页面创建完成后需要注册路由才能访问页面

配置文件：`src/router/routes/asyncRoutes.ts`

### 一级路由（一级菜单）：

ts

```
export const asyncRoutes: MenuListType[] = [
  {
    path: "/test/index",
    name: "Test",
    component: "/test/index",
    meta: {
      title: "测试页",
      keepAlive: true,
    },
  },
];
```

完成上面的步骤后就可以访问页面了

到这里路由添加完成，访问 http://localhost:3006/safeguard/server， 如果能够正常访问，则表示路由和菜单定义成功。

### 

### 多级路由（多级菜单）：

ts

```
export const asyncRoutes: MenuListType[] = [
  {
    name: "Form",
    path: "/form",
    component: "/index/index",
    meta: {
      title: "表单",
      icon: "",
      keepAlive: false,
    },
    children: [
      {
        path: "basic",
        name: "Basic",
        component: "/form/basic",
        meta: {
          title: "基础表单",
          keepAlive: true,
        },
      },
      {
        path: "step",
        name: "Step",
        component: "/form/step",
        meta: {
          title: "分步表单",
          keepAlive: true,
        },
      },
    ],
  },
];
```

### 静态路由配置：

配置文件路径： src/router/routes/staticRoutes.ts

ts

```
export const staticRoutes: AppRouteRecordRaw[] = [
  {
    path: "/test",
    name: "Test",
    component: () => import("@views/test/index.vue"),
    meta: { title: "测试页面", isHideTab: true, setTheme: true },
  },
];
```

配置完成后你可以访问：http://localhost:3006/#/test 查看新建的页面，到这里静态路由注册完成。 **tips：** 如果静态路由在动态路由表也配置了，需要把静态路由中的配置去除，因为动态路由会自动注册路由。

## 主页配置

通过配置 `HOME_PAGE_PATH` 属性可以定义主页路由，默认使用菜单第一个有效路径

配置文件路径：`src/router/index.ts`

ts

```
export const HOME_PAGE_PATH = "";
```

## 内嵌页面配置

系统支持通过 iframe 内嵌外部页面，配置示例如下：

ts

```
{
  path: '/outside/iframe/elementui',
  name: 'ElementUI',
  component: '',
  meta: {
    title: 'menus.widgets.elementUI',
    keepAlive: false,
    link: 'https://element-plus.org/zh-CN/component/overview.html',
    isIframe: true
  }
}
```

## 路由和菜单类型

ts

```
export type MenuListType = {
  id: number; // id
  path: string; // 路由路径
  name: string; // 组件名
  component?: string; // 组件路径
  meta: {
    /** 路由标题 */
    title: string;
    /** 路由图标 */
    icon?: string;
    /** 是否显示徽章 */
    showBadge?: boolean;
    /** 文本徽章 */
    showTextBadge?: string;
    /** 是否在菜单中隐藏 */
    isHide?: boolean;
    /** 是否在标签页中隐藏 */
    isHideTab?: boolean;
    /** 外部链接 */
    link?: string;
    /** 是否为iframe */
    isIframe?: boolean;
    /** 是否缓存 */
    keepAlive?: boolean;
    /** 操作权限 */
    authList?: Array<{
      /** 权限名称 */
      title: string;
      /** 权限标识 */
      authMark: string;
    }>;
    /** 是否为一级菜单（不需要手动配置，自动识别） */
    isFirstLevel?: boolean;
    /** 角色权限 */
    roles?: string[];
    /** 是否固定标签页 */
    fixedTab?: boolean;
  };
  children?: MenuListType[]; // 子路由
};
```

### meta

meta 是菜单的元数据，用于定义菜单的显示和行为。包含以下属性：

### title

- 类型：`string`
- 说明：路由标题

### icon

- 类型：`string`
- 说明：路由图标

### showBadge

- 类型：`boolean`
- 说明：是否显示徽章

### showTextBadge

- 类型：`string`
- 说明：文本徽章

### isHide

- 类型：`boolean`
- 说明：是否在菜单中隐藏

### isHideTab

- 类型：`boolean`
- 说明：是否在标签页中隐藏

### link

- 类型：`string`
- 说明：外部链接

### isIframe

- 类型：`boolean`
- 说明：是否为 iframe

### keepAlive

- 类型：`boolean`
- 说明：是否缓存

### authList

- 类型：`Array<{title: string, authMark: string}>`
- 说明：操作权限列表，包含权限名称和权限标识

### isFirstLevel

- 类型：`boolean`
- 说明：是否为一级菜单，不需要手动配置，自动识别

### roles

- 类型：`string[]`
- 说明：角色权限

### fixedTab

- 类型：`boolean`
- 说明：是否固定标签页

### isFullPage

- 类型：`boolean`
- 说明：是否为全屏页面

### activePath

- 类型：`string`
- 说明：用于手动指定当前激活的菜单路径，常用于页面未在菜单中直接显示但需高亮其父级菜单的情况

# 系统配置

系统主题包括菜单样式、顶栏、设置中心、系统主色等，你可以在这里修改他们快速配置想要的主题。

## 系统 Logo 配置

系统 Logo 采用图片形式展示，如需更换 Logo，仅需修改图片资源路径，无需改动组件逻辑。

配置文件：`src/components/core/base/ArtLogo.vue`

ts

```
<template>
  <div class="art-logo">
    <img :style="logoStyle" src="@imgs/common/logo.png" alt="logo" />
  </div>
</template>
```

## 系统名称配置

系统名称统一通过配置文件管理，如需更改，只需修改 systemInfo.name 属性即可实现全局替换。

配置文件：`src/config/index.ts`

ts

```
const appConfig: SystemConfig = {
  systemInfo: {
    name: "Art Design Pro", // 系统名称
  },
};
```

## 全局配置

配置文件路径：`src/config/index.ts`

ts

```
const appConfig: SystemConfig = {
  // 系统信息
  systemInfo: {
    name: "Art Design Pro", // 系统名称
  },
  // 系统主题
  systemThemeStyles: {
    [SystemThemeEnum.LIGHT]: { className: "" },
    [SystemThemeEnum.DARK]: { className: SystemThemeEnum.DARK },
  },
  // 系统主题列表
  settingThemeList: [
    {
      name: "Light",
      theme: SystemThemeEnum.LIGHT,
      color: ["#fff", "#fff"],
      leftLineColor: "#EDEEF0",
      rightLineColor: "#EDEEF0",
      img: configImages.themeStyles.light,
    },
    {
      name: "Dark",
      theme: SystemThemeEnum.DARK,
      color: ["#22252A"],
      leftLineColor: "#3F4257",
      rightLineColor: "#3F4257",
      img: configImages.themeStyles.dark,
    },
    {
      name: "System",
      theme: SystemThemeEnum.AUTO,
      color: ["#fff", "#22252A"],
      leftLineColor: "#EDEEF0",
      rightLineColor: "#3F4257",
      img: configImages.themeStyles.system,
    },
  ],
  // 菜单布局列表
  menuLayoutList: [
    {
      name: "Left",
      value: MenuTypeEnum.LEFT,
      img: configImages.menuLayouts.vertical,
    },
    {
      name: "Top",
      value: MenuTypeEnum.TOP,
      img: configImages.menuLayouts.horizontal,
    },
    {
      name: "Mixed",
      value: MenuTypeEnum.TOP_LEFT,
      img: configImages.menuLayouts.mixed,
    },
    {
      name: "Dual Column",
      value: MenuTypeEnum.DUAL_MENU,
      img: configImages.menuLayouts.dualColumn,
    },
  ],
  // 菜单主题列表
  themeList: [
    {
      theme: MenuThemeEnum.DESIGN,
      background: "#FFFFFF",
      systemNameColor: "var(--art-text-gray-800)",
      iconColor: "#6B6B6B",
      textColor: "#29343D",
      textActiveColor: "#3F8CFF",
      iconActiveColor: "#333333",
      tabBarBackground: "#FAFBFC",
      systemBackground: "#FAFBFC",
      leftLineColor: "#EDEEF0",
      rightLineColor: "#EDEEF0",
      img: configImages.menuStyles.design,
    },
    {
      theme: MenuThemeEnum.DARK,
      background: "#191A23",
      systemNameColor: "#BABBBD",
      iconColor: "#BABBBD",
      textColor: "#BABBBD",
      textActiveColor: "#FFFFFF",
      iconActiveColor: "#FFFFFF",
      tabBarBackground: "#FFFFFF",
      systemBackground: "#F8F8F8",
      leftLineColor: "#3F4257",
      rightLineColor: "#EDEEF0",
      img: configImages.menuStyles.dark,
    },
    {
      theme: MenuThemeEnum.LIGHT,
      background: "#ffffff",
      systemNameColor: "#68758E",
      iconColor: "#6B6B6B",
      textColor: "#29343D",
      textActiveColor: "#3F8CFF",
      iconActiveColor: "#333333",
      tabBarBackground: "#FFFFFF",
      systemBackground: "#F8F8F8",
      leftLineColor: "#EDEEF0",
      rightLineColor: "#EDEEF0",
      img: configImages.menuStyles.light,
    },
  ],
  // 暗黑主题模式左侧菜单样式
  darkMenuStyles: [
    {
      theme: MenuThemeEnum.DARK,
      background: "#161618",
      systemNameColor: "#DDDDDD",
      iconColor: "#BABBBD",
      textColor: "rgba(#FFFFFF, 0.7)",
      textActiveColor: "",
      iconActiveColor: "#FFFFFF",
      tabBarBackground: "#FFFFFF",
      systemBackground: "#F8F8F8",
      leftLineColor: "#3F4257",
      rightLineColor: "#EDEEF0",
    },
  ],
  // 系统主色
  systemMainColor: [
    "#5D87FF",
    "#B48DF3",
    "#1D84FF",
    "#60C041",
    "#38C0FC",
    "#F9901F",
    "#FF80C8",
  ] as const,
```

# 主题配置

本项目基于 **Tailwind CSS v4** 构建，提供了高度灵活的主题定制系统。从整体布局到细节样式，您都可以通过 CSS 变量和 Tailwind 工具类进行个性化定制，打造独特的用户界面和体验。

## 主题配置文件

主题配置位于：`src/assets/styles/core/tailwind.css`

## CSS 主题变量

### 基础颜色变量

**使用 CSS 变量：**

css

```
/* 文字颜色 */
color: var(--art-gray-100);
color: var(--art-gray-900);

/* 边框 */
border: 1px solid var(--default-border);
border: 1px solid var(--default-border-dashed);

/* 背景颜色 */
background-color: var(--default-bg-color); /* 页面底色 */
background-color: var(--default-box-color); /* 卡片/容器背景 */

/* 交互状态 */
background-color: var(--art-hover-color); /* 悬停状态 */
background-color: var(--art-active-color); /* 激活状态 */
```

**使用 Tailwind 工具类：**

html

```
<!-- 文字颜色 -->
<div class="text-g-900">深色文字</div>
<div class="text-g-500">中等文字</div>
<div class="text-g-100">浅色文字</div>

<!-- 背景颜色 -->
<div class="bg-box">卡片背景</div>
<div class="bg-hover-color">悬停背景</div>

<!-- 边框 -->
<div class="border-full-d">完整边框</div>
<div class="border-b-d">底部边框</div>

<!-- 主题色 -->
<div class="bg-primary text-white">主题色背景</div>
<div class="text-primary">主题色文字</div>
```

### 主题色系统

项目使用 OKLCH 色彩空间定义主题色，提供更准确的色彩表现：

css

```
/* 主题色 */
color: var(--art-primary); /* 主色 */
color: var(--art-secondary); /* 次要色 */
color: var(--art-success); /* 成功色 */
color: var(--art-warning); /* 警告色 */
color: var(--art-error); /* 错误色 */
color: var(--art-info); /* 信息色 */
color: var(--art-danger); /* 危险色 */
```

**Element Plus 主题色变体：**

系统自动生成 9 个不同深浅的主题色变体，用于不同场景：

css

```
/* 主题色变浅（数字越大越浅） */
background-color: var(--el-color-primary-light-1); /* 最深 */
background-color: var(--el-color-primary-light-5); /* 中等 */
background-color: var(--el-color-primary-light-9); /* 最浅 */

/* 主题色变深（数字越大越深） */
background-color: var(--el-color-primary-dark-1);
background-color: var(--el-color-primary-dark-5);
background-color: var(--el-color-primary-dark-9);
```

### 灰度色系统

提供 9 个层级的灰度色，自动适配 Light/Dark 模式：

css

```
/* CSS 变量方式 */
color: var(--art-gray-100); /* 最浅 */
color: var(--art-gray-500); /* 中等 */
color: var(--art-gray-900); /* 最深 */

/* Tailwind 工具类方式 */
<div class="text-g-100">最浅文字</div>
<div class="text-g-500">中等文字</div>
<div class="text-g-900">最深文字</div>

<div class="bg-g-100">最浅背景</div>
<div class="bg-g-200">浅背景</div>
```

## 主题变量详解

<details class="details custom-block" style="box-sizing: border-box; border: 1px solid rgba(0, 0, 0, 0); border-radius: 8px; padding: 16px 16px 8px; line-height: 24px; font-size: 14px; color: rgb(60, 60, 67); background-color: rgba(142, 150, 170, 0.14); margin: 16px 0px; font-family: &quot;Punctuation SC&quot;, Inter, ui-sans-serif, system-ui, &quot;PingFang SC&quot;, &quot;Noto Sans CJK SC&quot;, &quot;Noto Sans SC&quot;, &quot;Heiti SC&quot;, &quot;Microsoft YaHei&quot;, DengXian, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;, &quot;Segoe UI Symbol&quot;, &quot;Noto Color Emoji&quot;; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-align: start; text-indent: 0px; text-transform: none; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; white-space: normal; text-decoration-thickness: initial; text-decoration-style: initial; text-decoration-color: initial;"><div class="language-css vp-adaptive-theme" style="box-sizing: border-box; position: relative; margin: 8px 0px; background-color: rgb(246, 246, 247); overflow-x: auto; transition: background-color 0.5s; border-radius: 8px;"><button title="Copy Code" class="copy" style="box-sizing: border-box; touch-action: manipulation; border-width: 0.727273px; border-style: solid; border-color: rgb(235, 235, 239); border-image: none 100% / 1 / 0 stretch; padding: 0px; line-height: inherit; color: inherit; font-family: inherit; background-color: rgb(246, 246, 247); background-image: url(&quot;data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='none' stroke='rgba(128,128,128,1)' stroke-linecap='round' stroke-linejoin='round' stroke-width='2' viewBox='0 0 24 24'%3E%3Crect width='8' height='4' x='8' y='2' rx='1' ry='1'/%3E%3Cpath d='M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2'/%3E%3C/svg%3E&quot;); cursor: pointer; direction: ltr; position: absolute; top: 12px; right: 12px; z-index: 3; border-radius: 4px; width: 40px; height: 40px; opacity: 0; background-position: 50% center; background-size: 20px; background-repeat: no-repeat; transition: border-color 0.25s, background-color 0.25s, opacity 0.25s;"></button><span class="lang" style="box-sizing: border-box; position: absolute; top: 2px; right: 8px; z-index: 2; font-size: 12px; font-weight: 500; color: rgba(60, 60, 67, 0.56); transition: color 0.4s, opacity 0.4s;"></span><pre class="shiki shiki-themes github-light github-dark vp-code" tabindex="0" style="box-sizing: border-box; font-family: ui-monospace, Menlo, Monaco, Consolas, &quot;Liberation Mono&quot;, &quot;Courier New&quot;, monospace; direction: ltr; text-align: left; white-space: pre; word-spacing: normal; word-break: normal; overflow-wrap: normal; tab-size: 4; hyphens: none; position: relative; z-index: 1; margin: 0px; padding: 20px 0px; background: transparent; overflow-x: auto;"><code style="box-sizing: border-box; font-family: ui-monospace, Menlo, Monaco, Consolas, &quot;Liberation Mono&quot;, &quot;Courier New&quot;, monospace; font-size: 12.25px; background-color: transparent; direction: ltr; text-align: left; white-space: pre; word-spacing: normal; word-break: normal; overflow-wrap: normal; tab-size: 4; hyphens: none; display: block; padding: 0px 24px; width: fit-content; min-width: 100%; line-height: 1.7; color: rgba(60, 60, 67, 0.78); transition: color 0.5s; font-weight: 400;"><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(111, 66, 193); --shiki-light: #6F42C1; --shiki-dark: #B392F0;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(106, 115, 125); --shiki-light: #6A737D; --shiki-dark: #6A737D;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(106, 115, 125); --shiki-light: #6A737D; --shiki-dark: #6A737D;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(106, 115, 125); --shiki-light: #6A737D; --shiki-dark: #6A737D;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(106, 115, 125); --shiki-light: #6A737D; --shiki-dark: #6A737D;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(106, 115, 125); --shiki-light: #6A737D; --shiki-dark: #6A737D;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(106, 115, 125); --shiki-light: #6A737D; --shiki-dark: #6A737D;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(106, 115, 125); --shiki-light: #6A737D; --shiki-dark: #6A737D;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(106, 115, 125); --shiki-light: #6A737D; --shiki-dark: #6A737D;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(106, 115, 125); --shiki-light: #6A737D; --shiki-dark: #6A737D;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(106, 115, 125); --shiki-light: #6A737D; --shiki-dark: #6A737D;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(106, 115, 125); --shiki-light: #6A737D; --shiki-dark: #6A737D;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span></code></pre></div></details>

<details class="details custom-block" style="box-sizing: border-box; border: 1px solid rgba(0, 0, 0, 0); border-radius: 8px; padding: 16px 16px 8px; line-height: 24px; font-size: 14px; color: rgb(60, 60, 67); background-color: rgba(142, 150, 170, 0.14); margin: 16px 0px; font-family: &quot;Punctuation SC&quot;, Inter, ui-sans-serif, system-ui, &quot;PingFang SC&quot;, &quot;Noto Sans CJK SC&quot;, &quot;Noto Sans SC&quot;, &quot;Heiti SC&quot;, &quot;Microsoft YaHei&quot;, DengXian, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;, &quot;Segoe UI Symbol&quot;, &quot;Noto Color Emoji&quot;; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-align: start; text-indent: 0px; text-transform: none; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; white-space: normal; text-decoration-thickness: initial; text-decoration-style: initial; text-decoration-color: initial;"><div class="language-css vp-adaptive-theme" style="box-sizing: border-box; position: relative; margin: 8px 0px; background-color: rgb(246, 246, 247); overflow-x: auto; transition: background-color 0.5s; border-radius: 8px;"><button title="Copy Code" class="copy" style="box-sizing: border-box; touch-action: manipulation; border-width: 0.727273px; border-style: solid; border-color: rgb(235, 235, 239); border-image: none 100% / 1 / 0 stretch; padding: 0px; line-height: inherit; color: inherit; font-family: inherit; background-color: rgb(246, 246, 247); background-image: url(&quot;data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='none' stroke='rgba(128,128,128,1)' stroke-linecap='round' stroke-linejoin='round' stroke-width='2' viewBox='0 0 24 24'%3E%3Crect width='8' height='4' x='8' y='2' rx='1' ry='1'/%3E%3Cpath d='M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2'/%3E%3C/svg%3E&quot;); cursor: pointer; direction: ltr; position: absolute; top: 12px; right: 12px; z-index: 3; border-radius: 4px; width: 40px; height: 40px; opacity: 0; background-position: 50% center; background-size: 20px; background-repeat: no-repeat; transition: border-color 0.25s, background-color 0.25s, opacity 0.25s;"></button><span class="lang" style="box-sizing: border-box; position: absolute; top: 2px; right: 8px; z-index: 2; font-size: 12px; font-weight: 500; color: rgba(60, 60, 67, 0.56); transition: color 0.4s, opacity 0.4s;"></span><pre class="shiki shiki-themes github-light github-dark vp-code" tabindex="0" style="box-sizing: border-box; font-family: ui-monospace, Menlo, Monaco, Consolas, &quot;Liberation Mono&quot;, &quot;Courier New&quot;, monospace; direction: ltr; text-align: left; white-space: pre; word-spacing: normal; word-break: normal; overflow-wrap: normal; tab-size: 4; hyphens: none; position: relative; z-index: 1; margin: 0px; padding: 20px 0px; background: transparent; overflow-x: auto;"><code style="box-sizing: border-box; font-family: ui-monospace, Menlo, Monaco, Consolas, &quot;Liberation Mono&quot;, &quot;Courier New&quot;, monospace; font-size: 12.25px; background-color: transparent; direction: ltr; text-align: left; white-space: pre; word-spacing: normal; word-break: normal; overflow-wrap: normal; tab-size: 4; hyphens: none; display: block; padding: 0px 24px; width: fit-content; min-width: 100%; line-height: 1.7; color: rgba(60, 60, 67, 0.78); transition: color 0.5s; font-weight: 400;"><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(111, 66, 193); --shiki-light: #6F42C1; --shiki-dark: #B392F0;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(106, 115, 125); --shiki-light: #6A737D; --shiki-dark: #6A737D;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(106, 115, 125); --shiki-light: #6A737D; --shiki-dark: #6A737D;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(106, 115, 125); --shiki-light: #6A737D; --shiki-dark: #6A737D;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(106, 115, 125); --shiki-light: #6A737D; --shiki-dark: #6A737D;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(106, 115, 125); --shiki-light: #6A737D; --shiki-dark: #6A737D;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(106, 115, 125); --shiki-light: #6A737D; --shiki-dark: #6A737D;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(106, 115, 125); --shiki-light: #6A737D; --shiki-dark: #6A737D;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(106, 115, 125); --shiki-light: #6A737D; --shiki-dark: #6A737D;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(106, 115, 125); --shiki-light: #6A737D; --shiki-dark: #6A737D;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(227, 98, 9); --shiki-light: #E36209; --shiki-dark: #FFAB70;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(0, 92, 197); --shiki-light: #005CC5; --shiki-dark: #79B8FF;"></span><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span><span style="box-sizing: border-box; color: rgb(106, 115, 125); --shiki-light: #6A737D; --shiki-dark: #6A737D;"></span></span><span class="line" style="box-sizing: border-box; color: rgba(60, 60, 67, 0.78);"><span style="box-sizing: border-box; color: rgb(36, 41, 46); --shiki-light: #24292E; --shiki-dark: #E1E4E8;"></span></span></code></pre></div></details>

## Tailwind 工具类扩展

项目扩展了 Tailwind CSS，提供了更多实用的工具类：

### 布局工具类

html

```
<!-- Flexbox 快捷类 -->
<div class="flex-c">
  <!-- flex + items-center -->
  <div class="flex-b">
    <!-- flex + justify-between -->
    <div class="flex-cc">
      <!-- flex + items-center + justify-center -->
      <div class="flex-cb"><!-- flex + items-center + justify-between --></div>
    </div>
  </div>
</div>
```

### 过渡动画

html

```
<div class="tad-200">
  <!-- transition-all duration-200 -->
  <div class="tad-300"><!-- transition-all duration-300 --></div>
</div>
```

### 边框工具类

html

```
<div class="border-full-d">
  <!-- 完整边框 -->
  <div class="border-b-d">
    <!-- 底部边框 -->
    <div class="border-t-d">
      <!-- 顶部边框 -->
      <div class="border-l-d">
        <!-- 左侧边框 -->
        <div class="border-r-d"><!-- 右侧边框 --></div>
      </div>
    </div>
  </div>
</div>
```

### 其他工具类

html

```
<div class="c-p"><!-- cursor-pointer --></div>
```

### 自定义圆角

html

```
<div class="rounded-custom-xs">
  <!-- 小圆角 -->
  <div class="rounded-custom-sm"><!-- 中等圆角 --></div>
</div>
```

## 主题切换

### 使用 useTheme Hook

typescript

```
import { useTheme } from "@/hooks/core/useTheme";
import { SystemThemeEnum } from "@/enums/appEnum";

const { switchThemeStyles } = useTheme();

// 切换到暗色主题
switchThemeStyles(SystemThemeEnum.DARK);

// 切换到亮色主题
switchThemeStyles(SystemThemeEnum.LIGHT);

// 切换到自动模式（跟随系统）
switchThemeStyles(SystemThemeEnum.AUTO);
```

## 自定义主题色

### 修改预设主题色

在 `src/config/index.ts` 中修改预设的主题色列表：

typescript

```
systemMainColor: [
  "#5D87FF", // 默认蓝色
  "#B48DF3", // 紫色
  "#1D84FF", // 天蓝色
  "#60C041", // 绿色
  "#38C0FC", // 青色
  "#F9901F", // 橙色
  "#FF80C8", // 粉色
];
```

### 动态设置主题色

typescript

```
import { setElementThemeColor } from "@/utils/ui/colors";

// 设置自定义主题色
setElementThemeColor("#5D87FF");
```

### 主题色工具函数

项目提供了完整的颜色处理工具（`src/utils/ui/colors.ts`）：

typescript

```
import {
  hexToRgba, // Hex 转 RGBA
  hexToRgb, // Hex 转 RGB
  rgbToHex, // RGB 转 Hex
  getLightColor, // 生成变浅的颜色
  getDarkColor, // 生成变深的颜色
  colourBlend, // 颜色混合
} from "@/utils/ui/colors";

// 生成变浅的颜色
const lightColor = getLightColor("#5D87FF", 0.3);

// 生成变深的颜色
const darkColor = getDarkColor("#5D87FF", 0.3);

// 颜色混合
const blendedColor = colourBlend("#5D87FF", "#FFFFFF", 0.5);
```

## 响应式设计

使用 Tailwind 的响应式前缀：

html

```
<!-- 移动端优先 -->
<div class="text-sm md:text-base lg:text-lg">响应式文字大小</div>

<!-- 不同屏幕下的布局 -->
<div class="flex-col md:flex-row">响应式布局</div>
```

## 最佳实践

### 1. 优先使用 Tailwind 工具类

html

```
<!-- ✅ 推荐 -->
<div class="flex items-center gap-4 p-4 bg-box rounded-lg">
  <!-- ❌ 不推荐 -->
  <div style="display: flex; align-items: center; gap: 1rem;"></div>
</div>
```

### 2. 使用 CSS 变量保持一致性

css

```
/* ✅ 推荐 - 自动适配主题 */
.my-component {
  color: var(--art-gray-900);
  background: var(--default-box-color);
}

/* ❌ 不推荐 - 硬编码颜色 */
.my-component {
  color: #323251;
  background: #ffffff;
}
```

### 3. 使用语义化的颜色变量

css

```
/* ✅ 推荐 */
border-color: var(--default-border);
background: var(--art-hover-color);

/* ❌ 不推荐 */
border-color: #e2e8ee;
background: #f2f4f5;
```

### 4. 利用工具类组合

html

```
<!-- ✅ 推荐 - 使用预定义的组合类 -->
<div class="flex-cb tad-300">
  <!-- ❌ 不推荐 - 重复写完整的类 -->
  <div
    class="flex items-center justify-between transition-all duration-300"
  ></div>
</div>
```

## 主题配置总结

通过合理使用 CSS 变量、Tailwind 工具类和主题切换功能，您可以轻松打造出美观、一致且易于维护的用户界面。

# 图标

## v3.0 版本 - Iconify

v3.0 版本图标库升级为 [Iconify](https://icones.js.org/)，支持海量图标库，包括 Remix Icon、Material Design Icons、Font Awesome 等 150+ 图标集。

### 推荐图标库

为确保系统图标风格统一，项目全部采用 **Remix Icon** 图标库：

- [Iconify - Remix Icon](https://icones.js.org/collection/ri)
- [Remix Icon 官网](https://remixicon.com/)

### 使用方式

使用 `ArtSvgIcon` 组件显示图标：

vue

```
<template>
  <!-- 基础使用 -->
  <ArtSvgIcon icon="ri:home-line" />

  <!-- 自定义大小 -->
  <ArtSvgIcon icon="ri:user-line" class="text-2xl" />

  <!-- 自定义颜色 -->
  <ArtSvgIcon icon="ri:heart-fill" class="text-red-500" />

  <!-- 组合使用 -->
  <ArtSvgIcon icon="ri:star-fill" class="text-4xl text-yellow-500" />
</template>

<script setup>
import ArtSvgIcon from "@/components/core/base/art-svg-icon/index.vue";
</script>
```

### 样式定制

通过 Tailwind CSS 类名控制图标样式：

vue

```
<!-- 大小控制 -->
<ArtSvgIcon icon="ri:home-line" class="text-sm" />
<!-- 小 -->
<ArtSvgIcon icon="ri:home-line" class="text-base" />
<!-- 默认 -->
<ArtSvgIcon icon="ri:home-line" class="text-2xl" />
<!-- 大 -->
<ArtSvgIcon icon="ri:home-line" class="text-4xl" />
<!-- 超大 -->

<!-- 颜色控制 -->
<ArtSvgIcon icon="ri:heart-fill" class="text-red-500" />
<ArtSvgIcon icon="ri:star-fill" class="text-yellow-500" />
<ArtSvgIcon icon="ri:check-line" class="text-green-500" />
<ArtSvgIcon icon="ri:close-line" class="text-gray-500" />

<!-- 主题色 -->
<ArtSvgIcon icon="ri:home-line" class="text-theme" />
<ArtSvgIcon icon="ri:user-line" class="text-primary" />
<ArtSvgIcon icon="ri:settings-line" class="text-secondary" />
```

### 离线图标使用

默认情况下，Iconify 会从 CDN 动态加载图标数据。如果你的项目部署在内网环境，需要启用离线图标支持。

#### 1. 安装离线图标包

根据需要安装对应的图标集：

bash

```
# 安装 Remix Icon（系统必需）
pnpm add -D @iconify-json/ri

# 安装其他图标集（可选）
pnpm add -D @iconify-json/svg-spinners
pnpm add -D @iconify-json/line-md

# 或一次性安装所有图标集（体积较大，不推荐）
pnpm add -D @iconify/json
```

#### 2. 配置离线图标加载器

在 `src/utils/ui/iconify-loader.ts` 中配置需要加载的图标集：

typescript

```
import { addCollection } from "@iconify/vue";

// 导入离线图标数据
import riIcons from "@iconify-json/ri/icons.json";
import lineMd from "@iconify-json/line-md/icons.json";

// 注册离线图标集
addCollection(riIcons);
addCollection(lineMd);
```

#### 3. 启用离线图标

在 `src/main.ts` 中导入离线图标加载器：

typescript

```
import "@utils/ui/iconify-loader"; // 离线图标加载
```

提示

- 离线图标会在构建时打包到项目中，无需网络请求
- 只需安装和注册实际使用的图标集，避免打包体积过大
- 离线模式在内网和外网环境下都能正常工作

注意如果你添加了新的图标集，需要：

1. 安装对应的 `@iconify-json/[icon-set-name]` 包
2. 在 `iconify-loader.ts` 中导入并注册
3. 重新启动开发服务器

------

## v2.x 版本 - Iconfont

项目图标使用 [iconfont](https://www.iconfont.cn/) 提供，内置了 600+ 的图标，可以满足大部分的图标需求。

如果你需要添加或者自定义图标库，可以访问这个链接 [系统图标库](https://www.iconfont.cn/invite?type=project&token=FUL31flXrXQGY216#邀请你加入) ，进入后你可以把它添加到自己的项目中进行使用。

![img](https://www.artd.pro/docs/assets/cover.CqMai3rm.png)

### 使用方式

你可以在菜单中找到 Icon 图标，里面汇集了所有的图标，点击复制可以拿到图标的 Unicode 或 Font class。

**Unicode 用法**

html

```
<i class="iconfont-sys">&#xe649;</i>
```

**Font class 用法**

html

```
<i class="iconfont-sys iconsys-gou"></i>
```

### 图标库目录

图标库目录：`src/assets/icons/system`

注意为了方便用户拓展图标库，系统图默认使用 `iconfont-sys` 类名，而不是 `iconfont` :::

# 环境变量配置

## 说明

环境变量位于项目根目录下 .env、.env.development、.env.production

## .env

- 作用：适用于所有环境，里面定义的变量会在任何环境下都能访问。
- 用法：一般放置一些通用的配置，比如 API 基础地址、应用名称等。

## .env.development

- 作用：仅适用于开发环境。当你运行 pnpm dev 时，Vue 会加载这个文件中的环境变量。
- 用法：适合放置开发阶段的配置，比如本地 API 地址、调试设置等。

## .env.production

- 作用：仅适用于生产环境。当你运行 pnpm build 时，Vue 会加载这个文件中的环境变量。
- 用法：适合放置生产阶段的配置，比如生产 API 地址、禁用调试模式等。

## 自定义环境变量

INFO

自定义环境变量以 VITE_ 开头

**比如：**

VITE_PROT

你可以在项目代码中这样访问它们

ts

```
console.log(import.meta.env.VITE_PROT);
```

## 环境配置说明

.env.env.development.env.production

bash

```
# 【通用】环境变量

#  版本号
VITE_VERSION = 2.4.1.1

# 端口号
VITE_PORT = 3006

# 网站地址前缀
VITE_BASE_URL = /

# 权限模式（ frontend（前端） ｜ backend（后端） ）
VITE_ACCESS_MODE = frontend

# 跨域请求时是否携带 Cookie（开启前需确保后端支持）
VITE_WITH_CREDENTIALS = false

# 是否打开路由信息
VITE_OPEN_ROUTE_INFO = false

# 锁屏加密密钥
VITE_LOCK_ENCRYPT_KEY = s3cur3k3y4adpro
```

# 国际化

项目使用 vue-i18n 插件，目前集成了中文和英文两种语言包

目前对菜单、顶栏、设置中心等组件进行了国际化，其他地方根据需求自行配置

bash

```
├── language
│   ├── index.ts      // 配置文件
│   └── locales       // 语言包目录
│       ├── zh.json   // 中文包
│       └── en.json   // 英文包
```

## 在模版中使用

html

```
<p>{{ $t('setting.color.title') }}</p>
```

## 如何获取当前语言

ts

```
import { useI18n } from "vue-i18n";
const { locale } = useI18n();
```

## 如何配置多语言

修改 src/locales/index.ts 在 messages 中增加你要的配置的语言，然后在 langs 目录新建一个文件，如 en.ts

ts

```
import { createI18n } from "vue-i18n";
import en from "./en";
import zh from "./zh";
import { LanguageEnum } from "@/enums/appEnum";

const lang = createI18n({
  locale: LanguageEnum.ZH, // 设置语言类型
  legacy: false, // 如果要支持compositionAPI，此项必须设置为false;
  globalInjection: true, // 全局注册$t方法
  fallbackLocale: LanguageEnum.ZH, // 设置备用语言
  messages: {
    en,
    zh,
  },
});

export default lang;
```

# 权限说明

本系统支持两种权限控制模式，基于用户角色或菜单列表动态管理页面访问和按钮显示权限。

## 权限控制模式

### 概述

系统提供以下两种权限控制模式：

- **基于角色**：通过接口获取用户角色，控制页面访问和按钮显示权限。
- **基于菜单**：通过接口获取菜单列表，依据菜单结构控制页面访问和按钮权限。

### 配置方式

权限控制模式通过根目录下的 `.env` 文件配置。修改 `VITE_ACCESS_MODE` 的值可切换模式：

- `frontend`：前端控制模式，基于后端返回的角色标识进行权限控制。
- `backend`：后端控制模式，基于后端返回的菜单列表进行权限控制。

env

```
# 权限控制模式（frontend | backend）
VITE_ACCESS_MODE=frontend
```

## 前端控制模式

### 原理

前端维护菜单列表。用户登录后，接口返回角色标识（如 `R_SUPER`）。前端根据角色遍历菜单列表，若菜单的 `roles` 字段包含该角色，则允许访问对应路由。若未设置 `roles`，则默认所有用户可访问。

### 配置示例

菜单配置文件位于：`/src/router/routes/asyncRoutes.ts`

ts

```
[
  {
    id: 4,
    path: "/system",
    name: "System",
    component: "/index/index",
    meta: {
      title: "menus.system.title",
      icon: "ri:user-3-line",
      keepAlive: false,
    },
    children: [
      // 仅 R_SUPER 和 R_ADMIN 角色可访问
      {
        id: 41,
        path: "user",
        name: "User",
        component: "/system/user",
        meta: {
          title: "menus.system.user",
          keepAlive: true,
          roles: ["R_SUPER", "R_ADMIN"],
        },
      },
      // 未设置 roles，所有用户可访问
      {
        id: 42,
        path: "role",
        name: "Role",
        component: "/system/role",
        meta: {
          title: "menus.system.role",
          keepAlive: true,
        },
      },
    ],
  },
];
```

### 注意事项

- 确保接口返回的角色标识与路由表的 `roles` 字段匹配，否则用户无法访问受限页面。

## 后端控制模式

### 原理

后端生成菜单列表。用户登录后，接口返回菜单数据，前端校验后动态注册路由，实现权限控制。

### 数据结构

菜单数据结构定义位于：`/src/router/routes/asyncRoutes.ts`

ts

```
[
  {
    id: 4,
    path: "/system",
    name: "System",
    component: "/index/index",
    meta: {
      title: "menus.system.title",
      icon: "ri:user-3-line",
      keepAlive: false,
    },
    children: [
      {
        id: 41,
        path: "user",
        name: "User",
        component: "/system/user",
        meta: {
          title: "menus.system.user",
          keepAlive: true,
        },
      },
      {
        id: 42,
        path: "role",
        name: "Role",
        component: "/system/role",
        meta: {
          title: "menus.system.role",
          keepAlive: true,
        },
      },
    ],
  },
];
```

### 注意事项

- 后端返回的菜单数据结构必须与前端定义一致，否则可能导致路由注册失败。

## 前后端控制模式对比

- **前端控制模式**：
  - 适用于角色固定的系统。
  - 后端角色变更需同步更新前端路由配置。
  - 实现简单，适合小型项目。
- **后端控制模式**：
  - 适用于权限复杂的系统。
  - 后端返回完整菜单列表，前端动态注册路由。
  - 更灵活，但需确保前后端数据结构一致。

## 按钮权限控制

按钮权限控制支持精细化管理，通过用户角色或接口返回的权限码动态控制按钮显示。

### 权限码

权限码适用于前端和后端控制模式：

- **前端控制模式**：登录接口需返回权限码列表。
- **后端控制模式**：菜单列表需包含 `authList` 字段，定义按钮权限。

#### 配置示例（后端控制模式）

ts

```
[
  {
    id: 44,
    path: "menu",
    name: "Menus",
    component: "/system/menu",
    meta: {
      title: "menus.system.menu",
      keepAlive: true,
      authList: [
        { id: 441, title: "新增", authMark: "add" },
        { id: 442, title: "编辑", authMark: "edit" },
      ],
    },
  },
];
```

#### 使用方式

通过系统提供的 `hasAuth` 方法控制按钮显示：

ts

```
import { useAuth } from "@/composables/useAuth";
const { hasAuth } = useAuth();
```

vue

```
<ElButton v-if="hasAuth('add')">添加</ElButton>
```

### 自定义指令（v-auth）

在后端控制模式下，可通过自定义指令 `v-auth` 基于 `authList` 的 `authMark` 控制按钮显示。

#### 配置示例

ts

```
[
  {
    id: 44,
    path: "menu",
    name: "Menus",
    component: "/system/menu",
    meta: {
      title: "menus.system.menu",
      keepAlive: true,
      authList: [
        { id: 441, title: "新增", authMark: "add" },
        { id: 442, title: "编辑", authMark: "edit" },
        { id: 443, title: "删除", authMark: "delete" },
      ],
    },
  },
];
```

#### 使用方式

vue

```
<ElButton v-auth="'add'">添加</ElButton>
```

### 自定义指令（v-roles）

可基于用户信息接口中返回的 `roles` 进行权限控制。

#### 用户接口

ts

```
{
    "userId": "1",
    "userName": "Super",
    "roles": [
        "R_SUPER"
    ],
    "buttons": [
        "B_CODE1",
        "B_CODE2",
        "B_CODE3"
    ]
}
```

#### 使用示例

ts

```
 <el-button v-roles="['R_SUPER', 'R_ADMIN']">按钮</el-button>
 <el-button v-roles="'R_ADMIN'">按钮</el-button>
```

## 注意事项

- 确保登录接口返回的角色或权限码与路由表配置一致。
- 后端控制模式下，菜单数据需严格遵循前端定义的结构。
- 测试权限控制时，验证不同角色用户的页面和按钮显示是否符合预期。

# useTable 组合式函数

`useTable` 是一个功能强大的 Vue 3 组合式函数，专为现代 Web 应用的表格数据管理而设计。它提供了完整的表格解决方案，包括数据获取、智能缓存、分页控制、搜索功能和多种刷新策略。

## 特性

- **智能缓存** - 基于 LRU 算法的高效缓存机制
- **防抖搜索** - 内置防抖功能，优化搜索体验
- **灵活分页** - 完整的分页控制和状态管理
- **多种刷新策略** - 针对不同业务场景的智能刷新
- **错误处理** - 完善的错误处理和恢复机制

## 快速开始

### 基础用法

vue

```
<template>
  <div>
    <!-- 表格组件 -->
    <ArtTable
      :loading="loading"
      :data="data"
      :columns="columns"
      :pagination="pagination"
      @pagination:size-change="handleSizeChange"
      @pagination:current-change="handleCurrentChange"
    />
  </div>
</template>

<script setup lang="ts">
import { useTable } from "@/composables/useTable";
import { fetchGetUserList } from "@/api/system-manage";

const {
  data,
  loading,
  columns,
  pagination,
  handleSizeChange,
  handleCurrentChange,
} = useTable({
  core: {
    apiFn: fetchGetUserList,
    apiParams: {
      current: 1,
      size: 20,
    },
    columnsFactory: () => [
      { prop: "id", label: "ID" },
      { prop: "name", label: "姓名" },
      { prop: "email", label: "邮箱" },
    ],
  },
});
</script>
```

### 进阶用法

vue

```
<script setup lang="ts">
import { useTable, CacheInvalidationStrategy } from "@/composables/useTable";

const {
  // 数据相关
  data,
  loading,
  error,
  hasData,

  // 分页相关
  pagination,
  handleSizeChange,
  handleCurrentChange,

  // 搜索相关
  searchParams,
  resetSearchParams,

  // 数据操作
  getData,
  getDataDebounced,
  clearData,

  // 刷新策略
  refreshData,
  refreshSoft,
  refreshCreate,
  refreshUpdate,
  refreshRemove,

  // 缓存控制
  cacheInfo,
  clearCache,
  clearExpiredCache,

  // 列配置
  columns,
  columnChecks,
  addColumn,
  removeColumn,
  toggleColumn,
} = useTable<UserListItem>({
  // 核心配置
  core: {
    apiFn: UserService.fetchGetUserList,
    apiParams: {
      current: 1,
      size: 20,
      name: "",
      status: "",
    },
    excludeParams: ["daterange"],
    immediate: true,
    columnsFactory: () => [
      { prop: "name", label: "姓名", sortable: true },
      { prop: "email", label: "邮箱" },
      { prop: "status", label: "状态", useSlot: true },
    ],
  },

  // 数据处理
  transform: {
    dataTransformer: (records) => {
      return records.map((item) => ({
        ...item,
        statusText: item.status === 1 ? "激活" : "禁用",
      }));
    },
  },

  // 性能优化
  performance: {
    enableCache: true,
    cacheTime: 5 * 60 * 1000, // 5分钟
    debounceTime: 300,
    maxCacheSize: 100,
  },

  // 生命周期钩子
  hooks: {
    onSuccess: (data, response) => {
      console.log("数据加载成功:", data.length);
    },
    onError: (error) => {
      console.error("加载失败:", error.message);
    },
    onCacheHit: (data, response) => {
      console.log("缓存命中:", data.length);
    },
  },
});

// 搜索功能
const handleSearch = () => {
  Object.assign(searchParams, {
    name: "John",
    status: 1,
  });
  getData();
};

// CRUD 操作后的刷新
const handleAdd = () => {
  // 新增后回到第一页
  refreshCreate();
};

const handleEdit = () => {
  // 编辑后保持当前页
  refreshUpdate();
};

const handleDelete = () => {
  // 删除后智能处理页码
  refreshRemove();
};
</script>
```

## API 参考

### 配置选项

#### core (核心配置)

| 参数             | 类型       | 默认值                               | 说明                   |
| :--------------- | :--------- | :----------------------------------- | :--------------------- |
| `apiFn`          | `Function` | -                                    | **必需**，API 请求函数 |
| `apiParams`      | `Object`   | `{}`                                 | 默认请求参数           |
| `excludeParams`  | `Array`    | `[]`                                 | 排除的参数字段         |
| `immediate`      | `Boolean`  | `true`                               | 是否立即加载数据       |
| `columnsFactory` | `Function` | -                                    | 列配置工厂函数         |
| `paginationKey`  | `Object`   | `{current: 'current', size: 'size'}` | 分页字段映射           |

#### transform (数据处理)

| 参数              | 类型       | 默认值                   | 说明           |
| :---------------- | :--------- | :----------------------- | :------------- |
| `dataTransformer` | `Function` | -                        | 数据转换函数   |
| `responseAdapter` | `Function` | `defaultResponseAdapter` | 响应数据适配器 |

#### performance (性能优化)

| 参数           | 类型      | 默认值   | 说明             |
| :------------- | :-------- | :------- | :--------------- |
| `enableCache`  | `Boolean` | `false`  | 是否启用缓存     |
| `cacheTime`    | `Number`  | `300000` | 缓存时间（毫秒） |
| `debounceTime` | `Number`  | `300`    | 防抖延迟（毫秒） |
| `maxCacheSize` | `Number`  | `50`     | 最大缓存条数     |

#### hooks (生命周期钩子)

| 参数                | 类型       | 说明             |
| :------------------ | :--------- | :--------------- |
| `onSuccess`         | `Function` | 数据加载成功回调 |
| `onError`           | `Function` | 错误处理回调     |
| `onCacheHit`        | `Function` | 缓存命中回调     |
| `resetFormCallback` | `Function` | 重置表单回调     |

### 返回值

#### 数据相关

| 属性      | 类型                               | 说明         |
| :-------- | :--------------------------------- | :----------- |
| `data`    | `Ref<T[]>`                         | 表格数据     |
| `loading` | `Readonly<Ref<boolean>>`           | 加载状态     |
| `error`   | `Readonly<Ref<TableError | null>>` | 错误状态     |
| `hasData` | `ComputedRef<boolean>`             | 是否有数据   |
| `isEmpty` | `ComputedRef<boolean>`             | 数据是否为空 |

#### 分页相关

| 属性                  | 类型                                   | 说明             |
| :-------------------- | :------------------------------------- | :--------------- |
| `pagination`          | `Readonly<Reactive<PaginationParams>>` | 分页状态         |
| `handleSizeChange`    | `Function`                             | 页面大小变化处理 |
| `handleCurrentChange` | `Function`                             | 当前页变化处理   |

#### 搜索相关

| 属性                | 类型          | 说明         |
| :------------------ | :------------ | :----------- |
| `searchParams`      | `Reactive<P>` | 搜索参数     |
| `resetSearchParams` | `Function`    | 重置搜索参数 |

#### 数据操作

| 方法               | 说明                                        |
| :----------------- | :------------------------------------------ |
| `fetchData`        | 手动加载数据，需要配合 `immediate` 选项使用 |
| `getData`          | 获取数据（重置到第一页）                    |
| `getDataDebounced` | 获取数据（防抖）                            |
| `clearData`        | 清空数据                                    |

#### 刷新策略

| 方法            | 使用场景     | 说明                           |
| :-------------- | :----------- | :----------------------------- |
| `refreshData`   | 手动刷新按钮 | 清空所有缓存，重新获取数据     |
| `refreshSoft`   | 定时刷新     | 仅清空当前搜索条件的缓存       |
| `refreshCreate` | 新增数据后   | 回到第一页并清空分页缓存       |
| `refreshUpdate` | 更新数据后   | 保持当前页，仅清空当前搜索缓存 |
| `refreshRemove` | 删除数据后   | 智能处理页码，避免空页面       |

#### 缓存控制

| 属性/方法           | 说明                     |
| :------------------ | :----------------------- |
| `cacheInfo`         | 缓存统计信息             |
| `clearCache`        | 清除缓存（支持多种策略） |
| `clearExpiredCache` | 清理过期缓存             |

#### 列配置（可选）

| 方法           | 说明           |
| :------------- | :------------- |
| `columns`      | 表格列配置     |
| `columnChecks` | 列显示控制     |
| `addColumn`    | 新增列         |
| `removeColumn` | 删除列         |
| `toggleColumn` | 切换列显示状态 |
| `updateColumn` | 更新列配置     |
| `resetColumns` | 重置列配置     |

## 使用场景

### 1. 基础表格

适用于简单的数据展示场景：

typescript

```
const { data, loading, pagination } = useTable({
  core: {
    apiFn: fetchGetUserList,
    columnsFactory: () => basicColumns,
  },
});
```

### 2. 搜索表格

带搜索功能的表格：

typescript

```
const { searchParams, getData, resetSearchParams } = useTable({
  core: {
    apiFn: fetchGetUserList,
    apiParams: { name: "", status: "" },
  },
  performance: {
    debounceTime: 500, // 搜索防抖
  },
});

// 搜索
const handleSearch = () => {
  Object.assign(searchParams, formData);
  getData();
};
```

### 3. 高性能表格

启用缓存的高性能表格：

typescript

```
const { cacheInfo, clearCache } = useTable({
  core: {
    apiFn: fetchGetUserList,
  },
  performance: {
    enableCache: true,
    cacheTime: 10 * 60 * 1000, // 10分钟缓存
    maxCacheSize: 200,
  },
  hooks: {
    onCacheHit: (data) => {
      console.log("从缓存获取数据:", data.length);
    },
  },
});
```

### 4. CRUD 表格

完整的增删改查表格：

typescript

```
const { refreshCreate, refreshUpdate, refreshRemove } = useTable({
  core: {
    apiFn: fetchGetUserList,
  },
});

// 新增用户后
const handleAddUser = async () => {
  await addUser(userData);
  refreshCreate(); // 回到第一页
};

// 编辑用户后
const handleEditUser = async () => {
  await updateUser(userData);
  refreshUpdate(); // 保持当前页
};

// 删除用户后
const handleDeleteUser = async () => {
  await deleteUser(userId);
  refreshRemove(); // 智能处理页码
};
```

## 高级功能

### 缓存策略

`useTable` 提供了四种缓存清理策略：

typescript

```
import { CacheInvalidationStrategy } from "@/composables/useTable";

// 清空所有缓存
clearCache(CacheInvalidationStrategy.CLEAR_ALL, "手动刷新");

// 只清空当前搜索条件的缓存
clearCache(CacheInvalidationStrategy.CLEAR_CURRENT, "搜索数据");

// 清空分页相关缓存
clearCache(CacheInvalidationStrategy.CLEAR_PAGINATION, "新增数据");

// 不清理任何缓存
clearCache(CacheInvalidationStrategy.KEEP_ALL, "保持缓存");
```

### 自定义响应适配器

处理不同的后端响应格式：

typescript

```
const { data } = useTable({
  core: {
    apiFn: fetchGetUserList,
  },
  transform: {
    responseAdapter: (response) => {
      // 适配自定义响应格式
      return {
        records: response.list,
        total: response.totalCount,
        current: response.pageNum,
        size: response.pageSize,
      };
    },
  },
});
```

### 数据转换

对获取的数据进行转换：

typescript

```
const { data } = useTable({
  core: {
    apiFn: fetchGetUserList,
  },
  transform: {
    dataTransformer: (records) => {
      return records.map((item) => ({
        ...item,
        fullName: `${item.firstName} ${item.lastName}`,
        statusText: item.status === 1 ? "激活" : "禁用",
      }));
    },
  },
});
```

### 动态列配置

运行时动态管理表格列：

typescript

```
const { addColumn, removeColumn, toggleColumn, updateColumn } = useTable({
  core: {
    apiFn: fetchGetUserList,
    columnsFactory: () => initialColumns,
  },
});

// 新增列
addColumn({
  prop: "remark",
  label: "备注",
  width: 150,
});

// 删除列
removeColumn("status");

removeColumn(["status", "score"]);

// 切换列显示
toggleColumn("phone");

// 更新列配置
updateColumn("name", {
  label: "用户姓名",
  width: 200,
});
```

## 错误处理

`useTable` 内置了完善的错误处理机制：

typescript

```
const { error } = useTable({
  core: {
    apiFn: fetchGetUserList,
  },
  hooks: {
    onError: (error) => {
      // 自定义错误处理
      if (error.code === "NETWORK_ERROR") {
        ElMessage.error("网络连接失败，请检查网络");
      } else {
        ElMessage.error(error.message);
      }
    },
  },
});

// 在模板中显示错误
// <div v-if="error" class="error">{{ error.message }}</div>
```

## 调试功能

启用调试模式查看详细日志：

typescript

```
const { cacheInfo } = useTable({
  core: {
    apiFn: fetchGetUserList,
  },
  debug: {
    enableLog: true,
    logLevel: "info",
  },
});

// 查看缓存统计
console.log("缓存信息:", cacheInfo.value);
```

## 最佳实践

### 1. 合理使用缓存

typescript

```
// ✅ 推荐：为频繁访问的数据启用缓存
const { data } = useTable({
  performance: {
    enableCache: true,
    cacheTime: 5 * 60 * 1000, // 5分钟
  },
});

// ❌ 不推荐：为实时性要求高的数据启用缓存
```

### 2. 选择合适的刷新策略

typescript

```
// ✅ 新增数据后使用 refreshCreate
const handleAdd = () => {
  refreshCreate(); // 回到第一页
};

// ✅ 编辑数据后使用 refreshUpdate
const handleEdit = () => {
  refreshUpdate(); // 保持当前页
};

// ✅ 删除数据后使用 refreshRemove
const handleDelete = () => {
  refreshRemove(); // 智能处理页码
};
```

### 3. 优化搜索体验

typescript

```
// ✅ 使用防抖搜索
const { getDataDebounced } = useTable({
  performance: {
    debounceTime: 300,
  },
});

const handleSearch = () => {
  getDataDebounced(); // 自动防抖
};
```

### 4. 错误处理

typescript

```
// ✅ 提供友好的错误提示
const { error } = useTable({
  hooks: {
    onError: (error) => {
      ElMessage.error(error.message || "数据加载失败");
    },
  },
});
```

# ArtSearchBar 搜索栏组件

一个功能强大、高度可配置的表单搜索组件，支持多种表单控件类型、动态显示隐藏、表单验证等特性。

## 特性

- **多种表单控件** - 支持输入框、选择器、日期选择器、级联选择器等 20+种表单控件
- **高度可配置** - 支持自定义布局、标签位置、间距等
- **响应式设计** - 自适应不同屏幕尺寸
- **插槽支持** - 支持自定义组件和插槽渲染
- **表单验证** - 完整的表单验证支持
- **动态控制** - 支持动态显示隐藏表单项

## 基础用法

最简单的搜索栏用法：

vue

```
<template>
  <ArtSearchBar
    v-model="formData"
    :items="formItems"
    @search="handleSearch"
    @reset="handleReset"
  />
</template>

<script setup>
const formData = ref({
  name: "",
  status: "",
});

const formItems = [
  {
    label: "用户名",
    key: "name",
    type: "input",
    placeholder: "请输入用户名",
  },
  {
    label: "状态",
    key: "status",
    type: "select",
    props: {
      options: [
        { label: "启用", value: "1" },
        { label: "禁用", value: "0" },
      ],
    },
  },
];

const handleSearch = () => {
  console.log("搜索参数:", formData.value);
};

const handleReset = () => {
  console.log("重置表单");
};
</script>
```

## 支持的表单控件类型

### 输入类控件

javascript

```
// 普通输入框
{
  label: '用户名',
  key: 'name',
  type: 'input',
  placeholder: '请输入用户名'
}

// 数字输入框
{
  label: '年龄',
  key: 'age',
  type: 'number',
  props: {
    min: 0,
    max: 120
  }
}

// 多行文本
{
  label: '备注',
  key: 'remark',
  type: 'input',
  props: {
    type: 'textarea',
    rows: 3
  }
}
```

### 选择类控件

javascript

```
// 下拉选择
{
  label: '状态',
  key: 'status',
  type: 'select',
  props: {
    options: [
      { label: '启用', value: '1' },
      { label: '禁用', value: '0' }
    ]
  }
}

// 级联选择器
{
  label: '地区',
  key: 'region',
  type: 'cascader',
  props: {
    options: cascaderOptions,
    props: { multiple: true }
  }
}

// 树选择器
{
  label: '部门',
  key: 'department',
  type: 'treeselect',
  props: {
    data: treeData,
    multiple: true,
    showCheckbox: true
  }
}
```

### 日期时间控件

javascript

```
// 日期选择
{
  label: '创建日期',
  key: 'createDate',
  type: 'datetime',
  props: {
    type: 'date',
    valueFormat: 'YYYY-MM-DD'
  }
}

// 日期范围
{
  label: '时间范围',
  key: 'dateRange',
  type: 'datetime',
  props: {
    type: 'daterange',
    rangeSeparator: '至',
    startPlaceholder: '开始日期',
    endPlaceholder: '结束日期'
  }
}

// 时间选择器
{
  label: '时间',
  key: 'time',
  type: 'timepicker',
  props: {
    valueFormat: 'HH:mm:ss'
  }
}
```

### 其他控件

javascript

```
// 开关
{
  label: '是否启用',
  key: 'enabled',
  type: 'switch'
}

// 单选框组
{
  label: '性别',
  key: 'gender',
  type: 'radiogroup',
  props: {
    options: [
      { label: '男', value: '1' },
      { label: '女', value: '2' }
    ]
  }
}

// 复选框组
{
  label: '兴趣爱好',
  key: 'hobbies',
  type: 'checkboxgroup',
  props: {
    options: [
      { label: '读书', value: 'reading' },
      { label: '运动', value: 'sports' }
    ]
  }
}

// 评分
{
  label: '评分',
  key: 'rating',
  type: 'rate'
}

// 滑块
{
  label: '价格区间',
  key: 'priceRange',
  type: 'slider',
  props: {
    range: true,
    max: 1000
  }
}
```

## 自定义组件

### 使用渲染函数

javascript

```
import { h } from 'vue'
import CustomComponent from './CustomComponent.vue'

{
  label: '自定义组件',
  key: 'custom',
  type: () => h(CustomComponent, {
    prop1: 'value1',
    onCustomEvent: handleCustomEvent
  })
}
```

### 使用插槽

vue

```
<template>
  <ArtSearchBar v-model="formData" :items="formItems">
    <template #customSlot="{ item, modelValue }">
      <el-input
        v-model="modelValue[item.key]"
        placeholder="我是插槽渲染的组件"
      />
    </template>
  </ArtSearchBar>
</template>

<script setup>
const formItems = [
  {
    label: "自定义插槽",
    key: "customSlot",
    type: "input", // 这里的type会被插槽覆盖
  },
];
</script>
```

## 表单验证

vue

```
<template>
  <ArtSearchBar
    ref="searchBarRef"
    v-model="formData"
    :items="formItems"
    :rules="rules"
    @search="handleSearch"
  />
</template>

<script setup>
const searchBarRef = ref();

const rules = {
  name: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  phone: [
    { required: true, message: "请输入手机号", trigger: "blur" },
    {
      pattern: /^1[3456789]\d{9}$/,
      message: "请输入正确的手机号",
      trigger: "blur",
    },
  ],
};

const handleSearch = async () => {
  try {
    await searchBarRef.value.validate();
    console.log("验证通过，执行搜索");
  } catch (error) {
    console.log("验证失败");
  }
};
</script>
```

## 动态控制

### 动态显示隐藏

javascript

```
const formItems = computed(() => [
  {
    label: "用户名",
    key: "name",
    type: "input",
  },
  {
    label: "高级选项",
    key: "advanced",
    type: "input",
    hidden: !showAdvanced.value, // 动态控制显示隐藏
  },
]);
```

### 动态更新配置

javascript

```
const userNameItem = ref({
  label: "用户名",
  key: "name",
  type: "input",
  placeholder: "请输入用户名",
});

// 动态修改配置
const updateUserNameConfig = () => {
  userNameItem.value = {
    ...userNameItem.value,
    label: "昵称",
    placeholder: "请输入昵称",
  };
};
```

## 布局配置

### 栅格布局

vue

```
<ArtSearchBar v-model="formData" :items="formItems" :span="8" :gutter="16" />
```

### 标签配置

vue

```
<ArtSearchBar
  v-model="formData"
  :items="formItems"
  label-position="top"
  :label-width="120"
/>
```

### 响应式布局

组件会自动适配不同屏幕尺寸：

- 移动端：每行显示 1 个表单项
- 平板：每行显示 2 个表单项
- 桌面端：根据 `span` 属性控制每行显示的表单项数量

## API

### Props

| 参数            | 说明                   | 类型                       | 默认值    |
| :-------------- | :--------------------- | :------------------------- | :-------- |
| modelValue      | 表单数据对象           | `Record<string, any>`      | `{}`      |
| items           | 表单项配置数组         | `SearchFormItem[]`         | `[]`      |
| span            | 每个表单项占据的栅格数 | `number`                   | `6`       |
| gutter          | 栅格间隔               | `number`                   | `12`      |
| labelPosition   | 标签位置               | `'left' | 'right' | 'top'` | `'right'` |
| labelWidth      | 标签宽度               | `string | number`          | `'70px'`  |
| defaultExpanded | 默认是否展开           | `boolean`                  | `false`   |
| showExpand      | 是否显示展开收起按钮   | `boolean`                  | `true`    |
| showReset       | 是否显示重置按钮       | `boolean`                  | `true`    |
| showSearch      | 是否显示搜索按钮       | `boolean`                  | `true`    |
| disabledSearch  | 是否禁用搜索按钮       | `boolean`                  | `false`   |

### SearchFormItem 配置

| 参数        | 说明             | 类型                        | 默认值    |
| :---------- | :--------------- | :-------------------------- | :-------- |
| key         | 表单项唯一标识   | `string`                    | -         |
| label       | 标签文本         | `string`                    | -         |
| type        | 表单项类型       | `string | (() => VNode)`    | `'input'` |
| hidden      | 是否隐藏         | `boolean`                   | `false`   |
| span        | 栅格占位格数     | `number`                    | -         |
| labelWidth  | 标签宽度         | `string | number`           | -         |
| placeholder | 占位符           | `string`                    | -         |
| props       | 传递给组件的属性 | `Record<string, any>`       | -         |
| slots       | 插槽配置         | `Record<string, () => any>` | -         |

### Events

| 事件名 | 说明               | 参数 |
| :----- | :----------------- | :--- |
| search | 点击搜索按钮时触发 | -    |
| reset  | 点击重置按钮时触发 | -    |

### Methods

| 方法名   | 说明     | 参数                     |
| :------- | :------- | :----------------------- |
| validate | 验证表单 | `() => Promise<boolean>` |
| reset    | 重置表单 | `() => void`             |

### Slots

| 插槽名 | 说明             | 参数                                                        |
| :----- | :--------------- | :---------------------------------------------------------- |
| [key]  | 自定义表单项内容 | `{ item: SearchFormItem, modelValue: Record<string, any> }` |

## 完整示例

vue

```
<template>
  <div class="search-example">
    <ArtSearchBar
      ref="searchBarRef"
      v-model="formData"
      :items="formItems"
      :rules="rules"
      :defaultExpanded="true"
      :labelWidth="100"
      labelPosition="right"
      :span="6"
      :gutter="16"
      @search="handleSearch"
      @reset="handleReset"
    >
      <template #customSlot>
        <el-input
          v-model="formData.customSlot"
          placeholder="我是插槽渲染的组件"
        />
      </template>
    </ArtSearchBar>

    <div class="result">
      <h3>搜索结果：</h3>
      <pre>{{ JSON.stringify(formData, null, 2) }}</pre>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";

const searchBarRef = ref();

const formData = ref({
  name: "",
  phone: "",
  status: "",
  dateRange: [],
  customSlot: "",
});

const rules = {
  name: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  phone: [
    { required: true, message: "请输入手机号", trigger: "blur" },
    {
      pattern: /^1[3456789]\d{9}$/,
      message: "请输入正确的手机号",
      trigger: "blur",
    },
  ],
};

const formItems = [
  {
    label: "用户名",
    key: "name",
    type: "input",
    placeholder: "请输入用户名",
    props: { clearable: true },
  },
  {
    label: "手机号",
    key: "phone",
    type: "input",
    placeholder: "请输入手机号",
    props: { maxlength: 11 },
  },
  {
    label: "状态",
    key: "status",
    type: "select",
    props: {
      placeholder: "请选择状态",
      options: [
        { label: "启用", value: "1" },
        { label: "禁用", value: "0" },
      ],
    },
  },
  {
    label: "日期范围",
    key: "dateRange",
    type: "datetime",
    props: {
      type: "daterange",
      rangeSeparator: "至",
      startPlaceholder: "开始日期",
      endPlaceholder: "结束日期",
      valueFormat: "YYYY-MM-DD",
    },
  },
  {
    label: "自定义插槽",
    key: "customSlot",
    type: "input",
  },
];

const handleSearch = async () => {
  try {
    await searchBarRef.value.validate();
    console.log("搜索参数:", formData.value);
    // 执行搜索逻辑
  } catch (error) {
    console.log("表单验证失败");
  }
};

const handleReset = () => {
  console.log("重置表单");
};
</script>

<style scoped>
.search-example {
  padding: 20px;
}

.result {
  margin-top: 20px;
  padding: 16px;
  background-color: #f5f5f5;
  border-radius: 4px;
}

.result pre {
  margin: 0;
  font-size: 12px;
}
</style>
```

## 注意事项

1. **表单项 key 值必须唯一**，用于表单数据绑定和验证
2. **props 属性会直接传递给对应的 Element Plus 组件**，请参考 Element Plus 官方文档
3. **表单验证规则格式与 Element Plus Form 组件一致**