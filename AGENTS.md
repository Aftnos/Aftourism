# AGENTS.md - 系统架构与组件定义文档

本文档基于 `Aftourism` 项目实际代码实现编写，旨在定义系统内各核心模块的功能边界、交互协议及技术细节。适用于开发人员、AI 助手及系统维护者。

---

## 项目介绍

Aftourism 是一个基于现代 Web 技术构建的综合性全栈旅游服务平台，旨在为管理员提供高效的管理能力，并为终端用户提供便捷的服务体验。该平台采用前后端分离架构，结合了 Java 企业级开发的稳健性与现代前端框架的灵活性，为旅游行业管理提供了完整的解决方案。

该平台通过独立的应用程序服务于两个不同的用户群体：
- **Admin Dashboard（管理后台）**：面向系统管理员，用于管理内容、用户和运营
- **Portal Application（前台门户）**：面向终端用户，用于浏览旅游资源、参与活动和社区互动

系统管理后台实现了基于角色的访问控制（RBAC），前台门户用户使用角色编码进行权限划分，确保所有操作的安全性和授权正确性。

---

## 1. 项目概览与架构设计

本项目采用 **前后端分离** 架构，后端基于 Spring Boot 单体应用，前端基于 Vue 3。

### 1.1 总体架构

- **后端**: Spring Boot 3.5.7, JDK 21
- **前端**: Vue 3.5+, Vite 7+, TypeScript
- **数据存储**: MySQL 8 (持久化), Redis (缓存/会话)
- **模块化设计**: 单工程多包结构 (`aftnos.aftourismserver.*`)

### 1.2 核心技术栈

| 领域     | 技术组件              | 版本  | 备注                |
| :------- | :-------------------- | :---- | :------------------ |
| **语言** | Java                  | 21    | LTS                 |
| **框架** | Spring Boot           | 3.5.7 | 核心容器            |
| **ORM**  | MyBatis               | 3.0.5 | XML + 注解混合模式  |
| **安全** | Spring Security + JWT | -     | 无状态认证          |
| **缓存** | Spring Data Redis     | -     | Lettuce 连接池      |
| **前端** | Vue 3 + Element Plus  | 3.5+  | Admin/Portal 双端   |
| **构建** | Maven / pnpm          | -     | -                   |

### 1.3 后端技术栈

| 技术                | 版本       | 用途                                           |
| ------------------- | ---------- | ---------------------------------------------- |
| **Java**            | 21         | 具有现代语言特性的核心编程语言                 |
| **Spring Boot**     | 3.5.7      | 提供自动配置和生产就绪特性的应用框架           |
| **MyBatis**         | 3.0.5      | 数据库交互 ORM 框架                            |
| **Spring Security** | Latest     | 处理认证和授权的安全框架                       |
| **JWT**             | 0.11.5     | 无状态 API 安全 Token 认证                     |
| **Redis**           | Latest     | 缓存和会话管理的内存数据存储                   |
| **MySQL**           | 8.0+       | 持久化数据存储的关系型数据库                   |
| **AOP**             | Spring AOP | 操作日志等横切关注点的面向切面编程             |
| **Maven**           | Latest     | 构建和依赖管理工具                             |

来源：[pom.xml](pom.xml#L1-L100)，[README.md](README.md#L40-L55)

### 1.4 前端技术栈

#### 管理后台

| 技术             | 版本    | 用途                                     |
| ---------------- | ------- | ---------------------------------------- |
| **Vue**          | 3.5.21  | 构建用户界面的渐进式 JavaScript 框架      |
| **TypeScript**   | 5.6.3   | JavaScript 的类型化超集                   |
| **Element Plus** | 2.11.2  | Vue 3 组件库                              |
| **Tailwind CSS** | 4.1.14  | 实用优先的 CSS 框架                       |
| **Vite**         | 7.1.5   | 下一代前端构建工具                        |
| **Pinia**        | 3.0.3   | Vue 应用状态管理库                        |
| **Vue Router**   | 4.5.1   | Vue.js 应用的官方路由器                   |
| **ECharts**      | 6.0.0   | 数据可视化库                             |

#### 前台门户

| 技术             | 版本   | 用途                                     |
| ---------------- | ------ | ---------------------------------------- |
| **Vue**          | 3.5.10 | 构建用户界面的渐进式 JavaScript 框架      |
| **TypeScript**   | 5.6.2  | JavaScript 的类型化超集                   |
| **Element Plus** | 2.7.6  | Vue 3 组件库                              |
| **Vite**         | 5.4.0  | 下一代前端构建工具                        |
| **Pinia**        | 3.0.2  | Vue 应用状态管理库                        |
| **Vue Router**   | 4.3.0  | Vue.js 应用的官方路由器                   |
| **Axios**        | 1.6.0  | HTTP 客户端                              |

### 1.5 项目目录结构

```
aftourism/
├── pom.xml                           # Maven 配置
├── README.md                         # 项目说明
├── docs/                             # 文档目录
│   ├── Project.md                    # 项目结构说明
│   ├── SQL/
│   │   └── main.sql                  # 数据库脚本
│   ├── RBAC/                         # RBAC 相关文档
│   ├── PortalAPI/                    # 门户 API 文档
│   └── login/                        # 登录相关文档
├── src/
│   ├── main/
│   │   ├── java/aftnos/aftourismserver/
│   │   │   ├── AftourismServerApplication.java
│   │   │   ├── common/               # 公共模块
│   │   │   ├── admin/                # 后台管理模块
│   │   │   ├── auth/                 # 认证授权模块
│   │   │   ├── portal/               # 前台门户模块
│   │   │   ├── file/                 # 文件管理模块
│   │   │   └── monitor/              # 监控统计模块
│   │   └── resources/
│   │       ├── application.yml       # 主配置文件
│   │       ├── mapper/               # MyBatis XML 映射
│   │       └── logback-spring.xml   # 日志配置
│   └── test/
└── web/
    ├── admin/                        # 管理后台前端
    │   ├── src/
    │   │   ├── api/                  # API 接口
    │   │   ├── components/           # 组件
    │   │   ├── router/               # 路由
    │   │   ├── store/                # 状态管理
    │   │   ├── views/                # 页面
    │   │   └── config/               # 配置
    │   └── package.json
    └── portal/                       # 前台门户前端
        ├── src/
        │   ├── api/
        │   ├── components/
        │   ├── router/
        │   └── views/
        └── package.json
```

---

## 2. 核心 Agent (模块) 定义与边界

本项目将核心功能模块定义为 "Agent"，每个 Agent 负责特定的业务领域。

### 2.1 Common Agent (公共模块)

**功能边界**: 提供跨模块共享的基础设施和工具类，是所有其他模块的依赖基础。

**代码路径**: `src/main/java/aftnos/aftourismserver/common`

**关键功能**:
- **统一响应**: `Result<T>`、`ResultCode`、`PageResponse<T>`
- **异常处理**: `GlobalExceptionHandler`、`BusinessException`、`UnauthorizedException`
- **安全配置**: `SecurityConfig`、`JwtUtils`、`JwtAuthenticationFilter`
- **权限控制**: `AdminPermission`、`RbacAuthorityService`
- **AOP 切面**: `OperationLogAspect`（记录操作日志）
- **配置类**: `WebMvcConfig`、`RedisConfig`、`PasswordEncoderConfig`
- **拦截器**: `SiteVisitStatsInterceptor`（站点访问统计）

**包结构**:
```
common/
├── aop/                      # AOP 切面
├── config/                   # 配置类
├── exception/                # 异常处理
├── interceptor/              # 拦截器
├── result/                   # 统一返回结果
├── security/                 # 安全相关
├── util/                     # 工具类
└── vo/                       # 通用视图对象
```

**交互对象**: 被所有其他模块依赖，不依赖业务模块。

---

### 2.2 Admin Agent (后台管理)

**功能边界**: 负责系统管理员的所有操作，包括内容管理、用户管理、系统配置。

**代码路径**: `src/main/java/aftnos/aftourismserver/admin`

**关键功能**:
- **新闻/公告管理**: `NewsController`、`NoticeController`
- **景点/场地管理**: `ScenicSpotController`、`VenueController`
- **活动管理**: `ActivityController`（发布）、`ActivityManageController`（审核）、`ActivityAuditController`
- **首页内容配置**: `HomeContentService`（Banner、推荐位）
- **回收站**: `RecycleBinService`（逻辑删除数据的恢复）
- **管理员管理**: `AdminAccountService`
- **权限管理**: `RoleAccessController`
- **系统管理**: `SystemManageController`
- **活动评论管理**: `ActivityCommentManageService`
- **前台用户管理**: `PortalUserManageController`
- **门户数据看板**: `PortalDashboardController`

**包结构**:
```
admin/
├── controller/               # 控制器
├── service/                  # 服务层
│   └── impl/                 # 服务实现
├── mapper/                   # MyBatis 映射接口
├── pojo/                     # 实体类
├── dto/                      # 数据传输对象
├── vo/                       # 视图对象
└── enums/                    # 枚举类
```

**交互对象**:
- Database（读写操作）
- Redis（缓存配置）
- Monitor Agent（记录日志）
- File Agent（文件上传）

---

### 2.3 Portal Agent (前台门户)

**功能边界**: 面向普通用户的公共服务接口，提供只读信息展示及用户交互功能。

**代码路径**: `src/main/java/aftnos/aftourismserver/portal`

**关键功能**:
- **公共信息展示**: `HomePortalService`、`NewsPortalService`、`ScenicSpotPortalService`
- **用户认证**: `PortalAuthService`（注册、登录）
- **用户交互**: `UserFavoriteService`（收藏）、`ActivityApplyService`（活动报名）
- **活动评论**: `ActivityCommentService`
- **数据隔离**: 仅能访问状态为 "已发布/已审核" 的数据
- **用户个人中心**: `UserCenterService`

**包结构**:
```
portal/
├── controller/               # 控制器
├── service/                  # 服务层
│   └── impl/                 # 服务实现
├── mapper/                   # MyBatis 映射接口
├── pojo/                     # 实体类
├── dto/                      # 数据传输对象
└── vo/                       # 视图对象
```

**交互对象**:
- Common Agent（安全、工具）
- File Agent（头像上传）
- Monitor Agent（访问统计）

---

### 2.4 Auth Agent (认证与授权)

**功能边界**: 处理所有登录、注册、权限校验及 Token 管理。

**代码路径**: `src/main/java/aftnos/aftourismserver/common/security` 和 `auth` 包

**关键功能**:
- **管理员认证**: `AuthService`（登录、登出）
- **门户用户认证**: `PortalAuthService`（注册、登录）
- **权限控制**: `SecurityConfig`、`AdminPermission`（RBAC 模型）
- **Token 管理**: `JwtUtils`（生成、解析、刷新）
- **用户主体**: `AdminPrincipal`、`PortalUserPrincipal`
- **权限校验**: `RbacAuthorityService`（基于角色编码的权限判断）

**认证流程**:
```
1. 用户提交登录请求 → 
2. AuthService 校验用户名密码 →
3. 生成 JWT Token（包含用户信息和角色）→
4. 返回 Token 给客户端 →
5. 客户端后续请求携带 Token（Header: Authorization: Bearer <token>）→
6. JwtAuthenticationFilter 解析 Token →
7. 设置 SecurityContext →
8. 业务方法使用 @AdminPermission 注解校验权限
```

**配置依赖**: `security.jwt.secret`、`security.jwt.expiration`、`security.jwt.refresh-expiration`

---

### 2.5 File Agent (文件服务)

**功能边界**: 统一处理文件上传、存储及访问链接生成。

**代码路径**: `src/main/java/aftnos/aftourismserver/file`

**关键功能**:
- **本地存储**: `FileStorageService`（目前仅支持本地存储）
- **文件上传**: 支持 jpg、jpeg、png、gif、pdf、mp4 等格式
- **访问控制**: 配置静态资源映射 `WebMvcConfig`
- **文件验证**: 文件类型白名单、文件大小限制（10MB）

**配置依赖**: `file.upload-dir`、`file.base-url`、`file.allowed-types`、`file.storage-type`

**预留扩展**: 预留 OSS 接口，可扩展到阿里云 OSS、腾讯云 COS 等云存储。

---

### 2.6 Monitor Agent (监控与统计)

**功能边界**: 收集系统运行指标、用户行为数据及操作日志。

**代码路径**: `src/main/java/aftnos/aftourismserver/monitor`

**关键功能**:
- **Redis 性能监控**: `RedisBenchmarkRecord`（连接池状态、命令耗时）
- **站点访问统计**: `SiteVisitStats`（PV/UV/IP）
- **操作日志**: `OperationLogAspect`（AOP 切面记录 Admin 操作）

**性能指标**:
- Redis 连接池: `poolActive`、`poolIdle`、`poolWait`
- Redis 命中率: `hitRate`
- 命令延迟: `avgCommandUsec`
- 访问统计: PV（页面浏览量）、UV（独立访客数）

**配置依赖**: `monitor.redis.benchmark-enabled`、`monitor.redis.collect-interval`

---

### 2.7 Frontend Agent (Admin Web)

**功能边界**: 提供可视化的管理界面，与 Backend 进行 RESTful 通信。

**代码路径**: `web/admin`

**关键功能**:
- **动态路由**: 基于后端返回的菜单数据生成路由表（`src/router/core/MenuProcessor.ts`）
- **组件库**: 封装 `art-table`、`art-form` 等业务组件
- **配置管理**: `src/config/setting.ts`（主题、布局默认值）
- **状态管理**: Pinia Store（`menu`、`setting`、`table`、`user`）
- **权限守卫**: 路由守卫（`src/router/guards/beforeEach.ts`）
- **请求拦截**: Axios 拦截器（`src/utils/http/index.ts`）

**目录结构**:
```
web/admin/src/
├── api/                      # API 接口定义
├── components/               # 组件
│   ├── business/            # 业务组件
│   └── core/                # 核心组件
├── router/                  # 路由配置
│   ├── core/                # 路由核心逻辑
│   ├── guards/              # 路由守卫
│   └── modules/             # 路由模块
├── store/                   # 状态管理
├── views/                   # 页面组件
├── config/                  # 配置
├── utils/                   # 工具类
└── types/                   # TypeScript 类型定义
```

---

### 2.8 Frontend Agent (Portal Web)

**功能边界**: 提供前台门户的用户界面，与 Backend 进行 RESTful 通信。

**代码路径**: `web/portal`

**关键功能**:
- **用户认证**: 登录、注册页面
- **内容展示**: 首页、新闻列表、景点展示、活动列表
- **用户交互**: 活动报名、收藏、评论
- **用户中心**: 个人信息管理、活动报名记录

**目录结构**:
```
web/portal/src/
├── api/                      # API 接口定义
├── components/               # 组件
├── router/                  # 路由配置
├── store/                   # 状态管理
└── views/                   # 页面组件
```

---

## 3. 交互协议

### 3.1 前后端交互 (RESTful API)

**协议**: HTTP/1.1

**数据格式**: JSON

**统一响应结构**:
```java
public class Result<T> {
    private Integer code;    // 200: 成功, 其它: 错误码
    private String message;  // 提示信息
    private T data;         // 返回数据
}
```

**分页结构**: `PageResponse<T>`（包含 `list`、`total`、`pageNum`、`pageSize`、`current`、`size`）

**认证方式**: Header `Authorization: Bearer <token>`

**错误码规范**:
- `200`: 成功
- `400`: 请求参数错误
- `401`: 未认证
- `403`: 无权限
- `500`: 服务器内部错误

### 3.2 模块间交互

**方式**: Spring Bean 依赖注入（Service 调用 Service）

**规范**:
- `Portal` 模块可调用 `Common` 模块
- `Admin` 模块可调用 `Monitor` 模块记录日志
- 避免 `Portal` 直接调用 `Admin` 的写操作 Service，应通过共享的底层 Service 或 Domain Service 进行
- Service 层方法命名规范：
  - 查询: `get`、`list`、`page`、`find`、`query`
  - 新增: `add`、`create`、`insert`
  - 修改: `update`、`edit`、`modify`
  - 删除: `delete`、`remove`、`drop`

### 3.3 数据库交互

**ORM**: MyBatis XML Mapper

**分页**: PageHelper 插件（`PageHelper.startPage(pageNum, pageSize)`）

**命名规范**:
- 表名: `t_` 前缀 + 下划线命名（如 `t_user`、`t_admin`）
- 字段名: 下划线命名（如 `user_name`、`create_time`）
- Java 实体类: 驼峰命名（如 `userName`、`createTime`）

**MyBatis 配置**:
- `mapper-locations`: `classpath:mapper/*.xml`
- `type-aliases-package`: `aftnos.aftourismserver`
- `map-underscore-to-camel-case`: `true`

---

## 4. 配置参数说明

### 4.1 后端关键配置

| 配置项                                  | 默认值/示例                       | 作用                   |
| :-------------------------------------- | :-------------------------------- | :--------------------- |
| `server.port`                           | `8080`                            | 服务端口               |
| `spring.datasource.url`                 | `jdbc:mysql://localhost:3306/aftourism_server` | 数据库连接             |
| `spring.data.redis.lettuce.pool.max-active` | `8`                               | Redis 最大连接数       |
| `security.jwt.secret`                    | `Aftnos-server-key-test...`       | JWT 加密密钥           |
| `security.jwt.expiration`                | `24h`                             | Token 有效期           |
| `security.jwt.refresh-expiration`        | `7d`                              | 刷新 Token 有效期      |
| `file.upload-dir`                        | `./uploads`                       | 文件存储物理路径       |
| `file.base-url`                         | `http://localhost:8080/files`     | 文件访问基础 URL       |
| `file.allowed-types`                     | `jpg, png, pdf...`                | 允许上传的扩展名白名单 |
| `monitor.redis.benchmark-enabled`        | `true`                            | 是否启用 Redis 监控    |
| `monitor.redis.collect-interval`         | `60000` (ms)                      | Redis 监控采集频率     |

### 4.2 前端关键配置

| 配置项             | 默认值       | 作用                          |
| :----------------- | :----------- | :---------------------------- |
| `menuType`         | `LEFT`       | 菜单布局模式（左侧/顶部/混合） |
| `systemThemeColor` | (主色调)     | 系统主题色                    |
| `pageTransition`   | `slide-left` | 页面切换动画                  |
| `containerWidth`   | `FULL`       | 内容区域宽度模式              |

---

## 5. 开发规范

### 5.1 代码风格

**Java 代码规范**:
- 使用中文注释，注释要详细
- 类名、方法名使用驼峰命名
- 常量使用全大写下划线命名
- Service 层接口以 `Service` 结尾，实现类以 `ServiceImpl` 结尾
- Controller 层以 `Controller` 结尾
- Mapper 层以 `Mapper` 结尾

**Vue/TypeScript 代码规范**:
- 组件文件使用 PascalCase（如 `UserList.vue`）
- 工具函数使用 camelCase（如 `formatDate.ts`）
- API 接口文件使用 kebab-case（如 `user-api.ts`）
- 使用 TypeScript 类型定义，避免使用 `any`

### 5.2 数据库设计规范

- 严格按照 `docs/SQL/main.sql` 数据库设计程序
- 仅允许修改当前功能涉及的表
- 所有表都要有 `id`（主键）、`create_time`、`update_time` 字段
- 逻辑删除使用 `is_deleted` 字段（0 否，1 是）
- 状态字段使用 `status` 字段（1 启用，0 禁用）

### 5.3 API 设计规范

- RESTful 风格，使用 HTTP 方法语义：
  - `GET`: 查询
  - `POST`: 新增
  - `PUT`: 修改
  - `DELETE`: 删除
- URL 路径使用 kebab-case（如 `/api/admin/activity/page`）
- 统一返回 `Result<T>` 结构
- 分页接口使用 `PageResponse<T>` 返回

---

## 6. 部署配置

### 6.1 环境要求

- **JDK**: 21+
- **Maven**: 3.8+
- **MySQL**: 8.0+
- **Redis**: 6.0+
- **Node.js**: 20.19.0+ (Admin)
- **Node.js**: 18+ (Portal)

### 6.2 后端部署

```bash
# 1. 编译打包
mvn clean package

# 2. 运行应用
java -jar target/Aftourism-server-0.0.1-SNAPSHOT.jar

# 3. 或使用指定配置文件
java -jar target/Aftourism-server-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### 6.3 前端部署

**Admin 部署**:
```bash
cd web/admin

# 安装依赖
pnpm install

# 开发环境
pnpm dev

# 生产构建
pnpm build

# 预览构建结果
pnpm serve
```

**Portal 部署**:
```bash
cd web/portal

# 安装依赖
npm install

# 开发环境
npm run dev

# 生产构建
npm run build

# 预览构建结果
npm run preview
```

---

## 7. 常见问题

### 7.1 如何新增一个业务模块？

1. **后端**:
  - 在 `src/main/java/aftnos/aftourismserver/` 下创建新的包（如 `product`）
  - 创建 `controller`、`service`、`mapper`、`pojo`、`dto`、`vo` 等子包
  - 在 `docs/SQL/main.sql` 中添加数据库表
  - 在 `src/main/resources/mapper/` 中创建 Mapper XML 文件

2. **前端**:
  - 在 `web/admin/src/api/` 中创建 API 接口文件
  - 在 `web/admin/src/views/` 中创建页面组件
  - 在 `web/admin/src/router/modules/` 中创建路由模块

### 7.2 如何添加新的权限？

1. 在数据库 `t_role_access` 表中添加权限定义
2. 在 Controller 方法上添加 `@AdminPermission` 注解
3. 在 `common/security/RbacAuthorityService` 中添加权限判断逻辑
4. 在前端路由中添加 `meta.permission` 配置

### 7.3 如何进行分页查询？

```java
// Controller
@GetMapping("/page")
public Result<PageResponse<ActivityVO>> page(ActivityPageQuery query) {
    PageHelper.startPage(query.getPageNum(), query.getPageSize());
    List<ActivityVO> list = activityService.page(query);
    PageInfo<ActivityVO> pageInfo = new PageInfo<>(list);
    
    PageResponse<ActivityVO> response = PageResponse.<ActivityVO>builder()
        .list(list)
        .total(pageInfo.getTotal())
        .pageNum(pageInfo.getPageNum())
        .pageSize(pageInfo.getPageSize())
        .current((long) pageInfo.getPageNum())
        .size((long) pageInfo.getPageSize())
        .build();
    
    return Result.success(response);
}
```

---

## 8. 维护与更新

- 本文档应随架构变更同步更新
- 涉及 API 变更时，需同步更新 `docs/` 下的 OpenAPI 文档
- 代码编写时需要使用中文编写详细规范的注释
- 严格按照 `docs/SQL/main.sql`与`docs/SQL/Currently all.sql` 数据库（仅允许修改当前功能涉及的表）设计程序
- 项目结构在 [docs/Project.md](docs/Project.md)
- 代码风格按照现有延续即可
