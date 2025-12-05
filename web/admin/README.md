# Aftourism 后台前端（/web/admin）

本目录包含 Aftourism 管理端的 Vue 3 + Vite 前端代码，主要用于后台管理页面的开发与构建。项目使用 TypeScript、Pinia、Vue Router 以及 Element Plus 组件库。

## 目录结构概览

```
web/admin/
├─ src/
│  ├─ api/            # 后端接口封装
│  ├─ auth/           # 登录鉴权相关逻辑
│  ├─ components/     # 可复用的基础组件
│  ├─ layouts/        # 页面布局与导航框架
│  ├─ router/         # 路由配置（引用 view 下的页面组件）
│  ├─ store/          # Pinia 状态管理
│  ├─ styles/         # 全局样式与主题
│  ├─ utils/          # 工具函数
│  └─ view/           # 页面级组件（登录、仪表盘、业务页面等）
├─ package.json       # 项目依赖与脚本
├─ tsconfig*.json     # TypeScript 与路径配置
└─ vite.config.ts     # Vite 配置
```

> 说明：项目展示用的页面统一放在 `src/view` 目录下，路由亦从该目录按需动态导入，方便查找与维护。

## 开发与调试

1. **安装依赖**
   ```bash
   npm install
   ```
2. **本地开发（热重载）**
   ```bash
   npm run dev
   ```
3. **生产构建**
   ```bash
   npm run build
   ```
4. **预览构建结果**
   ```bash
   npm run preview
   ```

## 代码组织与约定

- **页面组件放在 `src/view`**：按业务子目录分类，例如 `src/view/ai/Chat.vue`、`src/view/admin/AdminList.vue`。
- **路由配置集中在 `src/router/routes.ts`**：新增页面时，在 `protectedRoutes` 或 `publicRoutes` 中添加路由，并从 `@/view/...` 导入对应组件。
- **公用组件与布局**：复用元素放在 `src/components`，全局布局放在 `src/layouts`。
- **状态与接口**：Pinia store 位于 `src/store`，接口封装在 `src/api`，保持与后端字段一致的类型定义。

## 常用调试提示

- 如果登录后跳转异常，请检查 `src/auth` 与 `src/router` 中的鉴权逻辑，确保权限标识与后端一致。
- 元素样式或主题调整建议在 `src/styles` 中集中维护，避免分散的内联样式。
- 新增接口时先在 `src/api` 添加对应方法，再在页面组件中调用，以保持代码结构清晰。

如需进一步开发，请以以上目录约定为基础，保持页面与逻辑分层，有助于后续维护和扩展。
