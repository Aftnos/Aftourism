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

### 常用命令
- 安装依赖：`pnpm install`
- 启动开发：`pnpm dev`
- 构建打包：`pnpm build`
- 代码检查：`pnpm lint`
