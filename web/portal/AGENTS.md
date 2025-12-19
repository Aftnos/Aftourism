# AGENTS.md

## 前端前台（Portal）工作约定

### 技术栈
- `Vue 3.5+`（Composition API）
- `TypeScript`：5.6
- `Vite`：5+
- `包管理器`：npm
- `UI`：Element Plus 2.7+
- `状态管理`：Pinia

### 开发规范
- 统一使用 `<script setup lang="ts">` 与组合式 API。
- 严格类型定义：为组件 `props`、接口响应等编写 `interface`。
- 样式使用 `SCSS`，公共样式置于 `src/styles`。
- 接口调用使用 `axios`，统一在 `src/services` 或 `src/api` 管理。
- 路由使用 `vue-router`，路由模块化管理在 `src/router`。
- 视图按业务模块置于 `src/views`，组件保持小而专注。

### 常用命令
- 安装依赖：`npm install`
- 启动开发：`npm run dev`
- 构建打包：`npm run build`
- 代码检查：`npm run lint`
