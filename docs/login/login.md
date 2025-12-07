

## 前端登录接口文档

### 1. 接口概述

**接口名称**：用户登录  
**接口地址**：`/auth/login`  
**请求方式**：POST  
**接口描述**：提供用户身份认证，返回访问令牌和刷新令牌
**注意事项**：普通用户、管理用户、等全部用户统一使用该接口

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

### 4. 相关接口

#### 4.1 获取用户信息

**接口地址**：`/user/info`  
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

### 5. 接口状态码

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
