# Aftourism 后端接口文档（BACKEND_API）

## 1. 概述
- **项目名称**：Aftourism-server（Spring Boot 3 / Java 21 / Maven）
- **根包**：`aftourismserver`
- **运行环境**：JDK 21、MySQL 8.0、Redis 7、Elasticsearch 8（可选，用于检索）、MinIO 对象存储。
- **部署形态**：单体后端，前后端分离。所有门户、后台与 AI 交互统一通过该服务网关暴露。
- **统一域名 / 网关**：
  - 生产：`https://api.aftourism.com`
  - 预发：`https://pre-api.aftourism.com`
  - 测试：`https://test-api.aftourism.com`
- **术语**：
  - **管理员**：进入后台（/admin/**）的操作人，分为超级管理员和普通管理员。
  - **普通用户**：门户端登录用户，对应 `portal` 模块。
  - **权限点**：`资源:动作` 格式，如 `news:publish`。
  - **高风险操作**：需二次/多次确认的操作，如强制下线、删除、批量导入。
  - **SSE**：Server-Sent Events，用于 AI 流式响应。

## 2. 认证与安全
- **登录**：通过 `POST /api/auth/login` 交换账号密码/验证码获取 JWT。支持后台管理员与门户用户；返回 access token、refresh token。
- **JWT**：
  - 使用 `Authorization: Bearer <token>` 请求头。
  - Access Token 默认有效期 2 小时，Refresh Token 7 天。
  - 支持刷新：`POST /api/auth/refresh`，需携带 refresh token。
- **统一返回**：`Result<T> { code:int, message:string, data:any }`。
- **错误码约定**：
  - `0`：成功
  - `401xx`：鉴权失败（未登录/无权限/登录过期）
  - `403xx`：禁止访问（无对应权限点、需要高权限确认）
  - `409xx`：并发/幂等冲突
  - `42xxx`：业务错误（如对象不存在、重复数据）
  - `50xxx`：服务器内部异常
- **速率限制**：按 IP + UserId 双维度，默认 60 req/min；AI 模块单独限制 30 次会话/小时。
- **幂等**：PUT/DELETE 接口均需附带 `Idempotency-Key` 头；在 24h 内重复请求将直接返回首次结果。
- **传输安全**：所有环境必须 HTTPS；超级管理员高风险操作需输入二次验证码。
- **内容安全**：AI 会话内含恶意/异常输入直接关闭会话并返回 `42A01`。

## 3. 通用规范
- **分页**：
  - 请求：`page`（默认1）、`size`（默认10，最大100）、`sort`（如`createdAt,desc`）。
  - 返回：`{ list: [...], page:int, size:int, total:int }`。
- **筛选/排序**：统一使用 query 参数（`status`, `keyword`, `dateFrom/dateTo`）。支持多字段排序：`sort=field1,asc;field2,desc`。
- **时间**：统一 ISO8601，UTC+8；JSON 字段示例 `2024-05-01T09:30:00+08:00`。
- **SSE**：`Content-Type: text/event-stream`；AI 流式接口输出事件：`meta`（初始上下文）、`delta`（增量 token）、`tool`（工具执行结果）、`end`（完成/关闭）。客户端需支持断线重连并传递 `Last-Event-ID`。
- **文件上传**：统一 `multipart/form-data`，字段 `file`。返回 `{ url, bucket, objectKey, etag }`。
- **审计字段**：所有资源包含 `createdBy/At`、`updatedBy/At`、`traceId`。

## 4. RBAC 与角色权限
- **角色类型**：
  - 超级管理员：拥有所有权限点；仍记录审计、执行确认。
  - 普通管理员：拥有被授予的权限点；无权访问高风险操作。
  - 普通用户：仅访问门户端业务接口；无后台管理权限。
- **权限点示例**：
  - `news:list` / `news:publish`
  - `activity:audit` / `activity:offline`
  - `ai:conversation` / `ai:review`
  - `monitor:metrics:view`
- **接口授权映射**：详见各模块接口表。未列出的接口默认仅超级管理员可用。

## 5. 接口分组

### 5.1 Auth 鉴权
| 接口 | 方法 | 描述 | 权限 | 备注 |
| --- | --- | --- | --- | --- |
| `/api/auth/login` | POST | 用户/管理员登录 | 公共 | 提供账号密码、验证码（可选）|
| `/api/auth/refresh` | POST | 刷新 access token | 公共 | 需 refresh token |
| `/api/auth/logout` | POST | 注销当前 token | 登录用户 | 失效当前 token |
| `/api/auth/profile` | GET | 获取当前用户资料与权限点 | 登录用户 | 返回角色、权限点列表 |

### 5.2 RBAC 管理
#### 管理员 & 用户
| 接口 | 方法 | 描述 | 权限 |
| --- | --- | --- | --- |
| `/api/admin/users` | GET | 分页查询管理员 | `rbac:admin:list` |
| `/api/admin/users` | POST | 创建管理员 | `rbac:admin:create`（高风险，需确认）|
| `/api/admin/users/{id}` | PUT | 编辑管理员 | `rbac:admin:update` |
| `/api/admin/users/{id}` | DELETE | 删除管理员 | `rbac:admin:delete`（高风险）|
| `/api/portal/users` | GET | 分页查询普通用户 | `rbac:user:list` |
| `/api/portal/users/{id}/status` | PATCH | 启停用户 | `rbac:user:status` |

#### 角色 & 权限
| 接口 | 方法 | 描述 | 权限 |
| --- | --- | --- | --- |
| `/api/rbac/roles` | GET/POST | 列表、创建角色 | `rbac:role:list/create` |
| `/api/rbac/roles/{id}` | PUT/DELETE | 更新、删除角色 | `rbac:role:update/delete` |
| `/api/rbac/roles/{id}/permissions` | PUT | 分配权限点 | `rbac:role:grant` |
| `/api/rbac/permissions/tree` | GET | 获取权限点树 | `rbac:perm:list` |

### 5.3 业务（新闻/通知/景区/场馆/活动）
#### 新闻 & 通知
| 接口 | 方法 | 描述 | 权限 |
| --- | --- | --- | --- |
| `/api/news` | GET | 查询新闻列表（门户） | 公共/登录可见 |
| `/api/admin/news` | GET | 后台分页新闻 | `news:list` |
| `/api/admin/news` | POST | 创建新闻 | `news:create` |
| `/api/admin/news/{id}` | PUT | 更新新闻 | `news:update` |
| `/api/admin/news/{id}/publish` | POST | 发布/下线新闻 | `news:publish`（需确认）|
| `/api/admin/notices` | CRUD | 通知管理 | `notice:*` |

#### 景区 / 场馆
| 接口 | 方法 | 权限 | 备注 |
| `/api/scenic-spots` | GET | 公共 | 门户展示，可带 `keyword`、`tag`、`status` |
| `/api/admin/scenic-spots` | GET | `scenic:list` | 支持过滤 `status`、`level` |
| `/api/admin/scenic-spots` | POST | `scenic:create` | 上传多张图片（文件模块）|
| `/api/admin/scenic-spots/{id}` | PUT/DELETE | `scenic:update/delete` | 删除需确认 |
| `/api/admin/venues` | CRUD | `venue:*` | 与景区类似 |

#### 活动
| 接口 | 方法 | 权限 | 备注 |
| `/api/activities` | GET | 公共 | 支持 `dateFrom/dateTo`、`scenicId` |
| `/api/admin/activities` | GET | `activity:list` | 支持状态过滤 |
| `/api/admin/activities` | POST | `activity:create` | 需传报名配置 |
| `/api/admin/activities/{id}` | PUT | `activity:update` |
| `/api/admin/activities/{id}/audit` | POST | `activity:audit`（高风险，需二次确认）|
| `/api/admin/activities/{id}/status` | PATCH | `activity:online/offline` |

### 5.4 收藏 / 留言
| 接口 | 方法 | 角色 | 描述 |
| --- | --- | --- | --- |
| `/api/collections` | GET/POST/DELETE | 普通用户 | 收藏景区/活动 |
| `/api/messages` | GET/POST | 普通用户 | 留言、查询留言 |
| `/api/admin/messages` | GET/PATCH | 管理员 | 回复、标记状态，权限 `message:reply` |

### 5.5 文件上传
| 接口 | 方法 | 描述 | 权限 |
| --- | --- | --- | --- |
| `/api/files/upload` | POST | 单文件上传 | 登录用户 |
| `/api/files/batch` | POST | 多文件批量上传 | `file:batch` |
| `/api/files/{objectKey}` | DELETE | 删除对象 | `file:delete`（高风险）|

### 5.6 监控统计
| 接口 | 方法 | 权限 | 说明 |
| --- | --- | --- | --- |
| `/api/monitor/overview` | GET | `monitor:overview:view` | 访问量、活跃用户、接口耗时 |
| `/api/monitor/system` | GET | `monitor:system:view` | CPU、内存、磁盘、JVM 指标 |
| `/api/monitor/redis/pressure` | POST | `monitor:redis:pressure` | 发起 Redis 压测（需确认）|
| `/api/monitor/traces/{traceId}` | GET | `monitor:trace:view` | 查询链路追踪 |

### 5.7 AI 模块
- **能力**：对话、流式响应、知识检索、申请（特定流程审批）。所有工具调用均在服务端执行，前端不运行任何函数。
- **安全**：
  - 会话包含敏感内容将触发内容安全审查，直接关闭会话，返回 `42A01`。
  - 超级管理员可查看全部日志；普通管理员仅可查看自己负责的会话。
- **接口**：
| 接口 | 方法 | 描述 | 权限 |
| --- | --- | --- | --- |
| `/api/ai/conversations` | POST | 创建会话并返回首轮回复 | `ai:conversation`（用户/管理员不同模型）|
| `/api/ai/conversations/{id}/messages` | POST | 追加消息 | `ai:conversation` |
| `/api/ai/conversations/{id}/stream` | GET (SSE) | 流式增量回复 | `ai:conversation` |
| `/api/ai/conversations/{id}` | GET | 获取会话详情 | `ai:conversation:view` |
| `/api/ai/conversations/{id}/close` | POST | 手动关闭会话 | `ai:conversation:close` |
| `/api/ai/search` | POST | 可选检索，返回 Top K 参考资料 | `ai:search` |
| `/api/ai/applications` | POST | 提交 AI 相关申请 | `ai:application:create` |
| `/api/ai/applications/{id}/audit` | POST | 审核申请 | `ai:application:audit`（需确认）|

### 5.8 操作日志与可观测
- 所有接口均写入审计日志：`operatorId`、`role`、`ip`、`ua`、`traceId`。
- 客户端需在请求头带 `X-Trace-Id`；若缺失服务端自动生成。
- 关键埋点：登录、RBAC 变更、高风险操作、AI 内容审查。
- 日志查询接口：`GET /api/logs/operations`（`log:operation:list` 权限）。

## 6. 错误码与故障排查
| 错误码 | 描述 | 排查建议 |
| --- | --- | --- |
| `40100` | 未登录或 token 失效 | 检查 Authorization 头，刷新 token |
| `40103` | 权限不足 | 核对 RBAC 配置，确认角色权限点 |
| `40310` | 需要二次确认 | 在确认接口附带 `X-Confirm-Token` |
| `40901` | 幂等冲突 | 使用新的 `Idempotency-Key` |
| `42001` | 资源不存在 | 检查资源 ID 或是否被删除 |
| `42A01` | AI 内容安全触发 | 调整输入内容，查看审计日志 |
| `50000` | 系统异常 | 查看 `traceId` 对应日志，联系后端 |

## 7. 版本与变更日志
- **OpenAPI Version**：`1.0.0`
- **文档版本**：`2024-06-01`
- **最近变更**：
  - 新增 AI 模块接口说明与错误码 `42A01`。
  - 补充 Redis 压测接口幂等策略。
