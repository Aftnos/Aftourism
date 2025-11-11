# 项目包结构说明

## 一、项目目录结构

```
aftnos.aftourismserver
 ├─ common      # 公共模块
 ├─ admin       # 后台管理模块
 ├─ portal      # 前台门户模块
 ├─ file        # 文件管理模块
 └─ monitor     # 系统监控模块
```

---

## 二、模块说明

| 包路径                              | 模块名称   | 主要内容                         |
| -------------------------------- | ------ | ---------------------------- |
| `aftnos.aftourismserver.common`  | 公共模块   | 公共实体类、统一返回结果、异常处理、工具类、常量定义等  |
| `aftnos.aftourismserver.admin`   | 后台管理系统 | 管理员登录、新闻管理、活动审核、日志与统计        |
| `aftnos.aftourismserver.portal`  | 前台门户网站 | 用户注册、新闻浏览、景区/场馆展示、活动申报、收藏留言  |
| `aftnos.aftourismserver.file`    | 文件管理模块 | 本地文件上传、文件访问路径处理、OSS 上传逻辑（预留） |
| `aftnos.aftourismserver.monitor` | 系统监控模块 | 系统资源性能监控、Redis 性能测试、访问量统计等   |

---

## 三、包结构示例（以 `aftnos.aftourismserver.admin` 为例）

```
aftnos.aftourismserver.admin
 ├─ controller      # 控制层接口
 ├─ service
 │   └─ impl        # 服务实现层
 ├─ mapper          # MyBatis 映射接口
 ├─ pojo            # 实体类
 ├─ dto             # 数据传输对象
 ├─ vo              # 视图对象（返回）
 └─ config          # 模块专属配置
```

---
