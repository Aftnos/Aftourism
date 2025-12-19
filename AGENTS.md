# AGENTS.md - 系统架构与组件定义文档

本文档基于 `Aftourism-server` 项目实际代码实现编写，旨在定义系统内各核心模块（Agents）的功能边界、交互协议及技术细节。适用于开发人员、AI 助手及系统维护者。

## 1. 项目概览与架构设计

本项目采用 **前后端分离** 架构，后端基于 Spring Boot 单体应用，前端基于 Vue 3。

### 1.1 总体架构
- **后端 (Backend)**: Spring Boot 3.5.7, JDK 21
- **前端 (Frontend)**: Vue 3.5+, Vite 7+, TypeScript
- **数据存储**: MySQL 8 (持久化), Redis (缓存/会话)
- **模块化设计**: 采用 Maven 多模块或单工程多包结构 (`aftnos.aftourismserver.*`)

### 1.2 核心技术栈
| 领域 | 技术组件 | 版本 | 备注 |
| :--- | :--- | :--- | :--- |
| **语言** | Java | 21 | LTS |
| **框架** | Spring Boot | 3.5.7 | 核心容器 |
| **ORM** | MyBatis | 3.0.5 | XML + 注解混合模式 |
| **安全** | Spring Security + JWT | - | 无状态认证 |
| **缓存** | Spring Data Redis | - | 包含 Lettuce 连接池 |
| **前端** | Vue 3 + Element Plus | 3.5+ | Admin 管理端 |
| **构建** | Maven / pnpm | - | - |

---

## 2. 核心 Agent (模块) 定义与边界

本项目将核心功能模块定义为 "Agent"，每个 Agent 负责特定的业务领域。

### 2.1 Admin Agent (后台管理)
- **功能边界**: 负责系统管理员的所有操作，包括内容管理、用户管理、系统配置。
- **代码路径**: `src/main/java/aftnos/aftourismserver/admin`
- **关键功能**:
  - **新闻/公告管理**: `NewsController`, `NoticeController`
  - **场馆/活动管理**: `VenueController`, `ActivityController` (含审核逻辑)
  - **首页内容配置**: `HomeContentService` (Banner, 推荐位)
  - **回收站**: `RecycleBinService` (逻辑删除数据的恢复)
- **交互对象**: Database (读写), Redis (缓存配置), Monitor Agent (记录日志).

### 2.2 Portal Agent (前台门户)
- **功能边界**: 面向普通用户的公共服务接口，提供只读信息展示及用户交互功能。
- **代码路径**: `src/main/java/aftnos/aftourismserver/portal`
- **关键功能**:
  - **公共信息展示**: `HomePortalService`, `NewsPortalService`
  - **用户交互**: `UserFavoriteService` (收藏), `ActivityApplyService` (活动报名)
  - **数据隔离**: 仅能访问状态为 "已发布/已审核" 的数据。
- **交互对象**: Auth Agent (用户上下文), Admin Agent (读取由 Admin 产生的内容).

### 2.3 Auth Agent (认证与授权)
- **功能边界**: 处理所有登录、注册、权限校验及 Token 管理。
- **代码路径**: `src/main/java/aftnos/aftourismserver/auth`
- **关键功能**:
  - **登录/注册**: `AuthService`, `PortalAuthService`
  - **权限控制**: `SecurityConfig`, `AdminPermission` (RBAC 模型)
  - **Token 管理**: `JwtUtils` (生成/解析/刷新)
- **配置依赖**: `security.jwt.*`

### 2.4 File Agent (文件服务)
- **功能边界**: 统一处理文件上传、存储及访问链接生成。
- **代码路径**: `src/main/java/aftnos/aftourismserver/file`
- **关键功能**:
  - **本地存储**: `FileStorageService` (目前仅支持本地，预留 OSS 接口)
  - **访问控制**: 配置静态资源映射 `WebMvcConfig`.
- **配置依赖**: `file.upload-dir`, `file.allowed-types`

### 2.5 Monitor Agent (监控与统计)
- **功能边界**: 收集系统运行指标、用户行为数据及操作日志。
- **代码路径**: `src/main/java/aftnos/aftourismserver/monitor`
- **关键功能**:
  - **Redis 性能监控**: `RedisBenchmarkRecord` (连接池状态、命令耗时)
  - **站点访问统计**: `SiteVisitStats` (PV/UV/IP)
  - **操作日志**: `OperationLogAspect` (AOP 切面记录 Admin 操作)
- **性能指标**:
  - Redis 连接池: `poolActive`, `poolIdle`
  - Redis 命中率: `hitRate`
  - 命令延迟: `avgCommandUsec`

### 2.6 Frontend Agent (Admin Web)
- **功能边界**: 提供可视化的管理界面，与 Backend 进行 RESTful 通信。
- **代码路径**: `web/admin`
- **关键功能**:
  - **动态路由**: 基于后端返回的菜单数据生成路由表 (`src/router/core/MenuProcessor.ts`).
  - **组件库**: 封装 `art-table`, `art-form` 等业务组件.
  - **配置管理**: `src/config/setting.ts` (主题、布局默认值).

---

## 3. 交互协议

### 3.1 前后端交互 (RESTful API)
- **协议**: HTTP/1.1
- **数据格式**: JSON
- **统一响应结构**:
  ```java
  public class Result<T> {
      private Integer code; // 200: 成功, 其它: 错误码
      private String message;
      private T data;
  }
  ```
- **分页结构**: `PageResponse<T>` (包含 list, total, pageNum, pageSize, current, size).
- **认证方式**: Header `Authorization: Bearer <token>`

### 3.2 模块间交互 (Java Process)
- **方式**: Spring Bean 依赖注入 (Service 调用 Service).
- **规范**:
  - `Portal` 模块可调用 `Common` 模块。
  - `Admin` 模块可调用 `Monitor` 模块记录日志。
  - 避免 `Portal` 直接调用 `Admin` 的写操作 Service，应通过共享的底层 Service 或 Domain Service 进行。

### 3.3 数据库交互
- **ORM**: MyBatis XML Mapper.
- **分页**: PageHelper 插件 (`PageHelper.startPage()`).

---

## 4. 配置参数说明

基于 `src/main/resources/application.yml` 及 `web/admin/src/config/setting.ts`。

### 4.1 后端关键配置
| 配置项 | 默认值/示例 | 作用 |
| :--- | :--- | :--- |
| `server.port` | `8080` | 服务端口 |
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/...` | 数据库连接 |
| `spring.data.redis.lettuce.pool.max-active` | `8` | Redis 最大连接数 |
| `security.jwt.expiration` | `24h` | Token 有效期 |
| `file.upload-dir` | `./uploads` | 文件存储物理路径 |
| `file.allowed-types` | `jpg, png, pdf...` | 允许上传的扩展名白名单 |
| `monitor.redis.collect-interval` | `60000` (ms) | Redis 监控采集频率 |

### 4.2 前端关键配置
| 配置项 | 默认值 | 作用 |
| :--- | :--- | :--- |
| `menuType` | `LEFT` | 菜单布局模式 (左侧/顶部/混合) |
| `systemThemeColor` | (主色调) | 系统主题色 |
| `pageTransition` | `slide-left` | 页面切换动画 |
| `containerWidth` | `FULL` | 内容区域宽度模式 |

---

## 5. 维护与更新
- 本文档应随架构变更同步更新。
- 涉及 API 变更时，需同步更新 `docs/` 下的 OpenAPI 文档。
- 新增 Agent (模块) 时，需在此注册并定义其边界。
- 要有代码注释，且使用中文注释、详细。
- 严格按照docs/SQL/main.sql数据库(仅允许修改当前功能涉及的表)设计程序。
- 项目结构在 docs/Project.md。
- 代码风格按照现有延续即可。