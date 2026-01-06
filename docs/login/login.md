

## 前端登录接口文档

### 1. 接口概述

**接口名称**：管理员登录  
**接口地址**：`/admin/auth/login`  
**请求方式**：POST  
**接口描述**：提供管理端身份认证，返回访问令牌和刷新令牌
**注意事项**：管理端与门户端登录已拆分为独立接口

### 2. 请求参数

| 参数名   | 类型   | 必填 | 说明     | 示例值   |
| -------- | ------ | ---- | -------- | -------- |
| userName | string | 是   | 用户名   | "Super"  |
| password | string | 是   | 用户密码 | "123456" |

**请求示例**：

```javascript
{
  "userName": "Super",
  "password": "123456"
}
```

### 3. 响应数据

| 参数名       | 类型   | 说明                           | 示例值                                    |
| ------------ | ------ | ------------------------------ | ----------------------------------------- |
| code          | number          | 响应状态码         | 200                               |
| data          | object          | 包含用户信息的对象 | {…}               |
| data.token  | string | 访问令牌，用于后续API调用认证  | "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." |
| data.refreshToken | string | 刷新令牌，用于获取新的访问令牌 | "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." |
| msg           | string          | 响应消息           | "请求成功"                        

**成功响应示例**：

```javascript
{
    "code": 200,
    "data": {
        "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoi....",
        "refreshToken": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJuY...."
    },
    "msg": "登录成功"
}
```

### 4. 门户登录接口

**接口名称**：门户用户登录  
**接口地址**：`/portal/auth/login`  
**请求方式**：POST  
**接口描述**：提供门户用户认证，返回访问令牌和刷新令牌

**请求示例**：

```javascript
{
  "userName": "portalUser",
  "password": "123456"
}
```

**成功响应示例**：与管理端登录一致。

### 5. 相关接口

#### 5.1 管理端获取用户信息

**接口地址**：`/admin/auth/info`  
**请求方式**：GET  
**请求头**：需要在 Authorization 中携带 token  
**响应数据**：

| 参数名        | 类型            | 说明               | 示例值                            |
| ------------- | --------------- | ------------------ | --------------------------------- |
| code          | number          | 响应状态码         | 200                               |
| data          | object          | 包含用户信息的对象 | {…}                               |
| data.userId   | string          | 用户 ID            | "1"                               |
| data.userName | string          | 用户名             | "Super"                           |
| data.roles    | array of string | 用户角色列表       | ["R_SUPER"]                       |
| data.buttons  | array of string | 可用按钮编码列表   | ["B_CODE1", "B_CODE2", "B_CODE3"] |
| data.email    | string          | 用户邮箱           | "art.design@gmail.com"            |
| msg           | string          | 响应消息           | "请求成功"                        |

**成功响应示例**：

```javascript
{
    "code": 200,
        "data": {
        "userId": "1",
            "userName": "Super",
            "roles": [
            "R_SUPER"
        ],
            "buttons": [
            "B_CODE1",
            "B_CODE2",
            "B_CODE3"
        ],
            "email": "art.design@gmail.com"
    },
    "msg": "请求成功"
}
```

#### 5.2 门户获取用户信息

**接口地址**：`/portal/auth/info`  
**请求方式**：GET  
**请求头**：需要在 Authorization 中携带 token  
**响应数据**：

| 参数名                        | 类型   | 说明                     | 示例值 |
| ---------------------------- | ------ | ------------------------ | ------ |
| code                         | number | 响应状态码               | 200    |
| data.userId                  | string | 用户 ID                  | "1"    |
| data.userName                | string | 用户名                   | "user" |
| data.nickName                | string | 昵称                     | "小王" |
| data.advancedUser            | boolean | 是否高级用户            | true   |
| data.qualificationStatus     | string | 资质状态                 | "PENDING" |
| data.qualificationRemark     | string | 资质审核备注             | "需补充材料" |
| msg                          | string | 响应消息                 | "请求成功" |

### 6. 门户资质申请接口

**接口地址**：`/portal/auth/qualification/apply`  
**请求方式**：POST  
**请求头**：需要在 Authorization 中携带 token  
**请求参数**：

| 参数名        | 类型   | 必填 | 说明         |
| ------------ | ------ | ---- | ------------ |
| realName     | string | 是   | 申请人姓名   |
| organization | string | 是   | 单位/机构名称 |
| contactPhone | string | 是   | 联系电话     |
| applyReason  | string | 是   | 申请说明     |
| attachmentUrl | string | 否 | 附件链接     |

### 7. 门户资质状态查询接口

**接口地址**：`/portal/auth/qualification/status`  
**请求方式**：GET  
**请求头**：需要在 Authorization 中携带 token  
**响应数据**：

| 参数名                | 类型   | 说明                     | 示例值 |
| -------------------- | ------ | ------------------------ | ------ |
| data.status           | string | 状态：NONE/PENDING/APPROVED/REJECTED | "PENDING" |
| data.auditRemark      | string | 审核备注                 | "补充材料" |
| data.applyTime        | string | 提交时间                 | "2025-01-01T10:00:00" |

### 8. 接口状态码

| 错误码 | 错误信息 |
|:------:|:--------:|
| 200    | 成功 |
| 400    | 错误 |
| 401    | 未授权 |
| 403    | 禁止访问 |
| 404    | 未找到 |
| 405    | 方法不允许 |
| 408    | 请求超时 |
| 500    | 服务器错误 |
| 501    | 未实现 |
| 502    | 网关错误 |
| 503    | 服务不可用 |
| 504    | 网关超时 |
| 505    | HTTP版本不支持 |
