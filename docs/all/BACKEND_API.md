# Aftourism 后端接口文档（BACKEND_API）

> 适用仓库：`/workspace/Aftourism-server`（Spring Boot 3.5.7 / Java 21 / Maven）。本文件覆盖代码库中 `aftnos.aftourismserver` 根包的所有 HTTP 接口，以最新主干实现为准。

## 1. 概述
| 项 | 说明 |
| --- | --- |
| 运行环境 | JDK 21、Spring Boot 3.5.7、MySQL 8.0（`spring.datasource.*`）、Redis 7（`spring.data.redis.*`）、本地文件系统（`file.upload-dir`）、Spring AI（OpenAI 兼容 API）。|
| 模块划分 | `common`（配置、异常、文件、审计）、`auth`（登录注册）、`admin`（后台业务）、`portal`（门户业务）、`monitor`（指标、站点统计、Redis 压测）、`ai`（对话与工具）。|
| 网关/域名 | 默认开发地址 `http://localhost:8080`，实际部署可通过网关层转发 `/admin/**`、`/portal/**`、`/ai/**`、`/file/**` 等路径。|
| 术语 | **管理员**：访问 `/admin/**` 的主体，含超级管理员与普通管理员；**门户用户**：访问 `/portal/**` 的终端用户；**权限点**：`资源:动作` 格式（见 §4）；**高风险操作**：删除、强制上下线、回收站彻底清除等需要前端二次确认的接口。|

## 2. 认证与安全
### 2.1 登录/注册接口
| 场景 | 方法 & Path | 描述 |
| --- | --- | --- |
| 后台登录 | `POST /admin/auth/login` | 账号密码登录，返回 `LoginResponse`（携带 JWT、角色/权限集等）。【代码：`AdminAuthController#login`】|
| 门户注册 | `POST /portal/auth/register` | 注册新门户用户，需提供用户名、密码、可选昵称/电话/邮箱等。【`PortalAuthController#register`】|
| 门户登录 | `POST /portal/auth/login` | 账号密码登录，返回与后台一致的 `LoginResponse`，`principalType=PORTAL_USER`。【`PortalAuthController#login`】|

`LoginResponse` 字段来自 `auth/dto/LoginResponse.java`，含 `principalId`、`principalType`（`PORTAL_USER`/`ADMIN`/`SUPER_ADMIN`）、`username`、`nickname`、`avatar`、`roles`、`permissions`、`token`、`expiresAt` 等。

### 2.2 JWT 与请求头
- 鉴权头：`Authorization: Bearer <token>`。JWT 由 `JwtUtils` 生成，载荷包含 `pid`（主体 ID）、`pt`（主体类型，枚举 `PrincipalType`）。`security.jwt.expiration` 默认为 24 小时。
- 认证失败会抛出 `UnauthorizedException` → `ResultCode.NOT_LOGIN`（401），授权失败抛 `AuthorizationDeniedException` → `ResultCode.PERMISSION_DENIED`（403）。
- 所有受保护接口需要后端解析 JWT，Portal 与 Admin 共享一套 Token 字段；AI 模块在 `AiConversationService#resolvePrincipal` 内自动识别当前主体。

### 2.3 错误处理
- 统一返回 `Result<T>`（`code`/`msg`/`data`）。除 AI 对话接口外，控制器均使用 `Result.success(...)` 包装；异常由 `GlobalExceptionHandler` 处理，并映射到 `ResultCode`。
- 常见错误：
  - `ResultCode.DATA_INCORRECT (1010)`：校验失败（`MethodArgumentNotValidException`）。
  - `ResultCode.BUSINESS_EXCEPTION (1011)`：业务规则被拒绝（如收藏重复、活动状态非法）。
  - `ResultCode.PATH_ERROR (1012)`：访问不存在的路径（`NoResourceFoundException`）。

### 2.4 内容安全与会话关闭
AI 请求在 `AiSafetyService#ensureSafe` 中检查恶意/越狱/PII 关键词（含手机号/身份证正则）；命中后会抛出 `AiSafetyException` 并关闭当前会话（`AiConversation#close`）。

## 3. 通用规范
### 3.1 返回体 & 分页
- `Result<T>`：默认 `code=1` 表示成功，`msg="success"`。
- 分页参数统一为 `current`（默认 1，@Min=1）与 `size`（默认 10，@Max=100）。分页响应使用 PageHelper 的 `PageInfo`，关键字段：`list`、`pageNum`、`pageSize`、`total`、`pages`。
- 典型响应：
```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "pageNum": 1,
    "pageSize": 10,
    "total": 42,
    "list": [ { "id": 1, "title": "..." } ]
  }
}
```

### 3.2 过滤、排序与时间
- 查询参数：`keyword`/`title`/`name`/`status` 等在各 DTO 中明确定义。例如 `NewsPageQuery` 支持 `title`、`status`；`ScenicSpotPageQuery` 支持 `name`、`address`。
- 所有时间字段使用 `yyyy-MM-dd HH:mm:ss`（见 `@JsonFormat` 注解），数据库时区遵循 `serverTimezone=UTC`。

### 3.3 文件上传
- 端点：`POST /file/upload`（`FileUploadController`），multipart 字段 `file`，可选 `bizTag` 作为二级目录。
- 权限：`AdminPermission.FILE_UPLOAD`。白名单扩展名来自 `FileStorageProperties.allowedTypes`（默认 `jpg/jpeg/png/gif/pdf`）。
- 响应 `FileUploadVO`：`url`、`fileName`、`originalName`、`size`。

### 3.4 流式/SSE 约定
- `AiChatRequest.stream` 为布尔预留字段，当前实现仍一次性返回 `AiChatResponse`；若未来扩展 SSE，客户端需在请求中设置 `stream=true` 并处理 `text/event-stream`。

### 3.5 审计与站点统计
- `OperationLogAspect` 拦截 `aftnos.aftourismserver.*.controller..*`，写入 `t_operation_log`，字段见 `OperationLog`（`traceId`、`operatorId`、`moduleName`、`requestUri`、`costMs` 等）。现阶段 `traceId` 需由上游通过日志 MDC 写入或在切面增强中补充，接口本身未处理 `X-Trace-Id`。
- `SiteVisitStatsInterceptor` 对所有非 OPTIONS 请求统计 PV/UV/IP，并将上传目录映射为 `/files/**` 静态资源（`WebMvcConfig`）。

## 4. RBAC 与角色/权限点
- **超级管理员**：`AdminPrincipal#isSuperAdmin()` 为 true，自动放行所有权限，并在角色列表中包含 `SUPER_ADMIN`。仍记录操作日志。
- **普通管理员**：权限由 `t_role_access`（`RoleAccessMapper`）配置，`@PreAuthorize` 通过 `RbacAuthorityService#hasPermission` 校验。
- **门户用户**：`PortalUserPrincipal` 仅拥有 `ROLE_PORTAL_USER`，门户接口需手动校验是否登录。

`AdminPermission` 列表：
| 权限键 | 描述 | 关联接口 |
| --- | --- | --- |
| `NEWS:CREATE/UPDATE/DELETE/READ` | 新闻 CRUD | `/admin/news` 相关接口 |
| `NOTICE:CREATE/UPDATE/DELETE/READ` | 通知 CRUD | `/admin/notice` 系列 |
| `SCENIC:*` | 景区 CRUD | `/admin/scenic/**` |
| `VENUE:*` | 场馆 CRUD | `/admin/venue/**` |
| `ACTIVITY_REVIEW:APPROVE/REJECT` | 活动审核（通过/驳回） | `/admin/activity/{id}/...` |
| `ACTIVITY_MANAGE:COMMENT` | 活动留言管理 | `/admin/activity/manage/{id}/comment/page`、`/admin/activity/manage/{id}/comment/all`、`/admin/activity/manage/comment/{commentId}` |
| `FILE:UPLOAD` | 文件上传 | `/file/upload` |
| `RECYCLE_BIN:READ/RESTORE/DELETE` | 回收站分页、恢复、彻底删除 | `/admin/recycle/**` |
| `MONITOR:SYSTEM_METRIC` | 上报系统指标 | `/admin/monitor/metrics/push` |
| `ADMIN_ACCOUNT:CREATE/UPDATE/DELETE/READ` | 管理员管理 | `/admin/rbac/admins/**` |
| `PORTAL_USER:READ/UPDATE` | 门户用户管理 | `/admin/rbac/users/**` |
| `ROLE_ACCESS:READ/UPDATE` | 角色 & 权限配置 | `/admin/rbac/roles*`、`/admin/rbac/permissions/catalog` |
| `AI_ASSIST:USE` | AI 工具调度 | `/ai/conversations/**` |

## 5. 接口分组
以下按模块列出路径、参数、示例及权限要求。

### 5.1 Auth 鉴权
#### 5.1.1 `POST /admin/auth/login`
- **请求体**：`{ "username": "admin", "password": "***" }`（`LoginRequest`）。
- **响应**：`Result<LoginResponse>`。
- **备注**：密码经 `PasswordEncoder` 校验；停用/删除账号会返回 `BusinessException("账号或密码错误")` 或 "账号已停用"。

#### 5.1.2 `POST /portal/auth/register`
- **请求体**（`RegisterRequest`）：
```json
{
  "username": "tourist01",
  "password": "******",
  "nickname": "游客",
  "phone": "138****",
  "email": "foo@example.com",
  "avatar": "https://...",
  "remark": "自驾游"
}
```
- **响应**：`Result<Void>`。
- **校验**：用户名唯一、密码 6-100 位、邮箱格式 `@Email`。

#### 5.1.3 `POST /portal/auth/login`
- 与后台登录一致，但 `principalType=PORTAL_USER`，`roles` 仅包含用户自身 `roleCode`（默认为 `PORTAL_USER`）。

### 5.2 RBAC 管理
#### 5.2.1 管理员账号（`/admin/rbac/admins`）
| Path | 方法 | 权限 | 请求参数/体 | 响应 |
| --- | --- | --- | --- | --- |
| `/page` | GET | `ADMIN_ACCOUNT:READ` | `AdminAccountPageQuery`：`current/size/username/realName/status` | `Result<PageInfo<AdminAccountVO>>` |
| `/` | POST | `ADMIN_ACCOUNT:CREATE` | `AdminAccountCreateRequest`：用户名、密码、角色编码列表、superAdmin 标记等 | `Result<Long>`（新 ID） |
| `/{id}` | GET | `ADMIN_ACCOUNT:READ` | 路径变量 id | `Result<AdminAccountVO>` |
| `/{id}` | PUT | `ADMIN_ACCOUNT:UPDATE` | `AdminAccountUpdateRequest`（可选密码、角色、状态、备注） | `Result<Void>` |
| `/{id}` | DELETE | `ADMIN_ACCOUNT:DELETE` | 路径变量 id | `Result<Void>` |

#### 5.2.2 门户用户后台（`/admin/rbac/users`）
- `GET /page`：分页查询门户用户（`PortalUserPageQuery`，支持用户名/昵称/状态），返回 `PortalUserVO`。
- `PUT /{id}`：调整 `roleCode`（字符串）或 `status`（1/0）。权限 `PORTAL_USER:UPDATE`。

#### 5.2.3 角色 & 权限目录（`RoleAccessController`）
- `GET /admin/rbac/roles`：返回每个角色的权限明细（`RoleSummaryVO` → `roleCode` + `RolePermissionVO` 列表）。
- `GET /admin/rbac/permissions/catalog`：枚举 `AdminPermission`。
- `POST /admin/rbac/roles/permissions`：提交 `RolePermissionUpsertRequest`（`roleCode` + 权限数组），重复 `resourceKey+action` 会去重。

#### 5.2.4 回收站（`/admin/recycle`）
- `GET /page`（权限 `RECYCLE_BIN:READ`）：查询已删除的数据，`RecycleQueryDTO` 包含 `type`（枚举 `NEWS/NOTICE/SCENIC/VENUE/ACTIVITY`）、`keyword`、`startTime`、`endTime`、分页参数。
- `PUT /{type}/{id}/restore`：恢复记录，权限 `RECYCLE_BIN:RESTORE`。
- `DELETE /{type}/{id}`：彻底删除，权限 `RECYCLE_BIN:DELETE`。前端应二次确认。

### 5.3 内容管理
#### 5.3.1 新闻（`/admin/news` & `/portal/news`）
- **后台**：
  - `POST /admin/news`（`NewsDTO`）：标题、内容、封面、作者、`status`（0/1）、可选 `publishTime`、`viewCount`。权限 `NEWS:CREATE`。
  - `PUT /admin/news/{id}`：编辑新闻。权限 `NEWS:UPDATE`。
  - `DELETE /admin/news/{id}`：逻辑删除。权限 `NEWS:DELETE`。
  - `GET /admin/news/page`：分页查询（`NewsPageQuery`），返回 `NewsVO`（含 `statusText`、`publishTime`、`createTime`/`updateTime`）。权限 `NEWS:READ`。
- **门户**：
  - `GET /portal/news/page`：匿名访问，`NewsPortalPageQuery` 支持 `keyword`。
  - `GET /portal/news/{id}`：返回 `NewsPortalVO`（含 `summary`、`content`）。

#### 5.3.2 通知公告（`/admin/notice` & `/portal/notice`）
与新闻类似，DTO 为 `NoticeDTO`；门户侧提供 `NoticeSummaryVO`/`NoticeDetailVO`。

#### 5.3.3 景区（`/admin/scenic`、`/portal/scenic`）
- 后台 `ScenicSpotDTO` 字段：`name`（必填）、`imageUrl`、`level`、`ticketPrice`（>=0，两位小数）、`address`、`openTime`、`intro`、`phone`（≤20）、`website`、经纬度（6 位小数）、`sort`。
- 接口：`POST` 创建、`PUT /{id}` 修改、`DELETE /{id}` 逻辑删、`GET /page` 分页、`GET /{id}` 详情。权限对应 `SCENIC:*`。
- 门户：`GET /portal/scenic/page`（`ScenicSpotPortalPageQuery` 支持名称/地址），`GET /portal/scenic/{id}` 详情（`ScenicSpotDetailVO`）。

#### 5.3.4 场馆（`/admin/venue`、`/portal/venue`）
- `VenueDTO` 字段：`name`、`imageUrl`、`category`、`isFree`（0/1）、`ticketPrice`（收费场馆填写）、`address`、`openTime`、`description`、`phone`、`website`、经纬度、`sort`。
- 接口与景区一致；门户端返回 `VenueSummaryVO`/`VenueDetailVO`。

### 5.4 活动运营
活动运营模块拆分为“活动审核”和“活动管理”两个子域：审核端仅处理用户提交的活动申报（通过/驳回，查看申报备注），
活动管理端负责后台自建活动的增删查改及留言维护，二者接口互不干扰。
#### 5.4.1 后台活动审核（`/admin/activity/{id}/...`）
| Path | 方法 | 权限 | 说明 |
| --- | --- | --- | --- |
| `/approve` | PUT | `ACTIVITY_REVIEW:APPROVE` | 将 `applyStatus` 置为审核通过并清空 `rejectReason`。|
| `/reject` | PUT | `ACTIVITY_REVIEW:REJECT` | 请求体 `ActivityRejectDTO`（`rejectReason` 必填），并强制下线。|

#### 5.4.2 后台活动管理（`/admin/activity/manage`）
| Path | 方法 | 权限 | 说明 |
| --- | --- | --- | --- |
| `/page` | GET | `ACTIVITY_MANAGE:READ` | 条件分页查询全部活动，支持名称/类别/上线状态/时间段筛选。|
| `/` | POST | `ACTIVITY_MANAGE:CREATE` | 新建活动，字段同 `ActivityManageDTO`。|
| `/{id}` | GET | `ACTIVITY_MANAGE:READ` | 查看活动详情，返回 `ActivityManageDetailVO`。|
| `/{id}` | PUT | `ACTIVITY_MANAGE:UPDATE` | 编辑活动。|
| `/{id}` | DELETE | `ACTIVITY_MANAGE:DELETE` | 逻辑删除活动，回收站可恢复。|
| `/{id}/comment/page` | GET | `ACTIVITY_MANAGE:COMMENT` | 分页查看指定活动下的留言，可通过 `parentId` 切换楼层。|
| `/{id}/comment` | POST | `ACTIVITY_MANAGE:COMMENT` | 新增留言/回复，`ActivityCommentManageDTO` 需要 `userId`、`content`，可选 `parentId`。|
| `/comment/{commentId}` | GET | `ACTIVITY_MANAGE:COMMENT` | 查看留言详情与楼中楼回复列表，返回 `ActivityCommentDetailVO`。|
| `/comment/{commentId}` | PUT | `ACTIVITY_MANAGE:COMMENT` | 更新留言内容、关联用户或父级关系，校验不能选择自身/子层级为父级。|
| `/comment/{commentId}` | DELETE | `ACTIVITY_MANAGE:COMMENT` | 逻辑删除留言，并级联删除其所有回复。|

#### 5.4.3 门户活动申报/留言（`ActivityPortalController`）
| Path | 方法 | 登录 | 请求体/参 | 响应 |
| --- | --- | --- | --- | --- |
| `/portal/activity/apply` | POST | 必须（`SecurityUtils.currentPortalUserIdOrThrow`） | `ActivityApplyDTO`：`name`、`coverUrl`、`startTime`、`endTime`、`category`、`venueId`、`organizer`、`contactPhone`、`intro`、`auditRemark`（申报人对审核的补充说明，≤255 字符） | `Result<Long>`（申报记录 ID） |
| `/portal/activity/page` | GET | 否 | `ActivityPortalPageQuery`：`name`、`venueId`、`startTimeFrom`/`startTimeTo`、分页 | `Result<PageInfo<ActivitySummaryVO>>` |
| `/portal/activity/{id}/comment` | POST | 必须 | `ActivityCommentCreateDTO`（`content`≤500、可选 `parentId`） | `Result<Long>`（留言 ID） |
| `/portal/activity/{id}/comment/page` | GET | 否 | `ActivityCommentPageQuery`（`parentId` 可为空表示一级楼层） | `Result<PageInfo<ActivityCommentVO>>` |

### 5.5 门户收藏（`UserFavoriteController`）
- `POST /portal/fav/{type}/{id}`：添加收藏，`type` 取 `SCENIC`/`VENUE`/`ACTIVITY`（不区分大小写，参见 `FavoriteTargetTypeEnum`）。返回 `Result<Long>`。
- `DELETE /portal/fav/{type}/{id}`：取消收藏。
- `GET /portal/fav/page`：分页查询当前登录用户的收藏记录（可传 `type` 过滤）。返回 `UserFavoriteVO`，包含 `targetName` 与 `targetCover`。

### 5.6 文件上传
详见 §3.3。`bizTag` 会经过白名单清洗，只保留字母/数字/`/_-`，最终目录为 `bizTag/yyyy/MM/dd/<uuid>.<ext>`。

### 5.7 监控统计
#### 5.7.1 系统指标上报
- `POST /admin/monitor/metrics/push`（权限 `MONITOR:SYSTEM_METRIC`）。
- 请求体 `SystemMetricPushRequest`：`host`、`cpuUsage`、`memoryUsage`、`diskUsage`（0-100 的 `BigDecimal`）、可选 `loadAvg`、`remark`。
- 控制器会拷贝到 `SystemMetric` 并持久化（Mapper 位于 `monitor/mapper/SystemMetricMapper.java`）。

#### 5.7.2 访问量 & Redis
- `SiteVisitStatsService` 通过拦截器自动调用 `upsertVisitStats(today, pv, uv, ip)`，Mapper 负责 `INSERT ... ON DUPLICATE KEY UPDATE`（详见 `monitor/mapper/SiteVisitStatsMapper.xml`）。无需额外接口。
- `RedisMetricsScheduler`（`@Scheduled`) 每分钟（默认）调用 `RedisMetricsService#collectAndSaveMetrics`，统计命令耗时、命中率并写入 `t_redis_benchmark`。如需手动压测，可在服务端调整 `monitor.redis.benchmark-enabled`。

### 5.8 AI 模块（`AiChatController`）
| Path | 方法 | 登录要求 | 说明 |
| --- | --- | --- | --- |
| `/ai/conversations/chat` | POST | 必须（自动识别 Portal/Admin） | 请求 `AiChatRequest`（`conversationId` 可为空，`message` 必填，`stream` 可选）。返回 `AiChatResponse`：`conversationId`、`closed`、`closeReason`、`structured`（`AiStructuredReply`，含 `reply`、`actions`、`needsConfirmation`、`pendingToolId`、`securityNotice`）、`history`（`AiMessageDTO` 列表）、`pendingTool`（若有待确认工具）。|
| `/ai/conversations/{id}` | GET | 必须 | 返回会话详情（同 `AiChatResponse`）。|
| `/ai/conversations/{id}/confirm` | POST | 必须 | 人工确认工具执行，`AiToolConfirmationRequest` 包含 `toolCallId`、`approved`、`comment`。返回 `AiToolConfirmationResponse`（`pendingTool`、`success`、`message`）。|

- 工具调用均在服务端执行（`AiToolManager`），LLM 回调由 `Spring AI ChatClient` 触发。敏感或恶意输入会关闭会话。

### 5.9 操作日志与可观测
- 所有控制器均会写操作日志。可根据 `moduleName`（URI 第一段）、`operationName`（方法前缀推断）、`traceId`（字段预留）、`costMs` 等进行排查。当前仓库尚未暴露日志查询接口；如需，可在 admin 模块新增 `GET /admin/logs/operations` 并复用 `OperationLogMapper`。
- `SiteVisitStats` 记录 `pv/uv/ipCount`，可在大屏展示日访问趋势。
- 结合 `RedisBenchmarkRecord` 可监控 Redis 命中率、平均/最大命令耗时，排查缓存瓶颈。

## 6. 错误码与排查
源于 `ResultCode` 枚举：
| code | 名称 | 说明 |
| --- | --- | --- |
| `1` | SUCCESS | 请求成功 |
| `0` | FAILURE | 未指定失败 |
| `1001` | PARAM_ERROR | 请求参数格式不符 |
| `1002` | PERMISSION_DENIED | 权限不足/`@PreAuthorize` 拒绝 |
| `1003` | NOT_LOGIN | Token 缺失/无效 |
| `1004` | DATA_NOT_FOUND | 资源不存在 |
| `1005` | INTERNAL_ERROR | 服务异常或弱密钥等内部错误 |
| `1006` | REQUEST_TIMEOUT | 请求超时（预留） |
| `1007` | DATA_EXISTS | 约束重复（`DuplicateKeyException`） |
| `1008` | DATA_USED | 依赖数据存在，禁止删除 |
| `1009` | DATA_INCOMPLETE | 请求体缺少字段 |
| `1010` | DATA_INCORRECT | 校验失败 |
| `1011` | BUSINESS_EXCEPTION | 业务异常通用（收藏重复、状态非法等） |
| `1012` | PATH_ERROR | 路径不存在 |

排查建议：
1. 先检查 `Result.code`；若为业务异常，可根据 `msg` 定位具体逻辑（大部分直接透传 `BusinessException` 消息）。
2. 关注操作日志表 `t_operation_log` 中的 `errorMsg`、`requestParams`、`operatorType`，结合 `site_visit_stats` 可确定是否为恶意访问。
3. AI 模块的 `closeReason` 表示内容安全或会话状态；若多次触发，请检查输入是否含黑名单关键字。

## 7. 版本与变更
| 项 | 值 |
| --- | --- |
| OpenAPI 版本 | 1.0.0（见 `docs/all/openapi.yaml`） |
| 文档版本 | 2024-12-16（根据最新源码重新梳理） |
| 变更摘要 | 依据实际 Controller/DTO/Service，重新核对接口路径、参数、权限映射；补充 AI、监控、收藏、回收站等模块细节，移除代码未实现的刷新 Token/SSE 接口。|
