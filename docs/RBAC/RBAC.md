## 后端菜单接口文档

### 1. 接口基本信息

**接口名称：** 获取用户菜单列表  
**接口路径：** `GET /admin/auth/menus`  
**请求方式：** GET  
**认证要求：** 需要 JWT Token  

### 2. 响应数据结构

```typescript
// 响应格式
{
  "code": 200,
  "message": "success",
  "data": AppRouteRecord[]  // 菜单数据数组
}

// 菜单数据类型
interface AppRouteRecord {
  id?: number              // 菜单ID，后端管理使用
  path: string             // 路由路径
  name?: string            // 路由名称
  redirect?: string        // 重定向路径
  component?: string       // 组件路径
  meta: RouteMeta          // 菜单元数据
  children?: AppRouteRecord[]  // 子菜单（递归结构）
}

// 菜单元数据详细定义
interface RouteMeta {
  // 基础信息
  title: string                    // 【必填】菜单标题
  icon?: string                    // 菜单图标（Element Plus 图标名）
  
  // 显示控制
  isHide?: boolean                 // 是否在菜单中隐藏
  isHideTab?: boolean              // 是否在标签页中隐藏
  showBadge?: boolean              // 是否显示徽章
  showTextBadge?: string           // 徽章文本内容
  
  // 路由特性
  keepAlive?: boolean              // 是否缓存页面
  fixedTab?: boolean               // 是否固定标签页
  activePath?: string              // 激活菜单路径
  
  // 特殊功能
  link?: string                    // 外部链接地址
  isIframe?: boolean               // 是否为iframe页面
  isFullPage?: boolean             // 是否为全屏页面
  
  // 权限控制
  roles?: string[]                 // 可访问的角色列表
  authList?: Array<{               // 操作权限列表
    title: string                  // 权限显示名称
    authMark: string               // 权限标识符
  }>
  authMark?: string                // 页面权限标识
  
  // 层级控制
  isFirstLevel?: boolean           // 是否为一级菜单
  parentPath?: string              // 父级路径
  isAuthButton?: boolean           // 是否为权限按钮行
}
```

### 3. 完整示例数据(前端框架演示的请求示例)

```json
{
    "code": 200,
    "msg": "请求成功",
    "data": [
        {
            "name": "Dashboard",
            "path": "/dashboard",
            "component": "/index/index",
            "meta": {
                "title": "menus.dashboard.title",
                "icon": "ri:pie-chart-line"
            },
            "children": [
                {
                    "path": "console",
                    "name": "Console",
                    "component": "/dashboard/console",
                    "meta": {
                        "title": "menus.dashboard.console",
                        "icon": "ri:home-smile-2-line",
                        "keepAlive": false,
                        "fixedTab": true
                    }
                },
                {
                    "path": "analysis",
                    "name": "Analysis",
                    "component": "/dashboard/analysis",
                    "meta": {
                        "title": "menus.dashboard.analysis",
                        "icon": "ri:align-item-bottom-line",
                        "keepAlive": false
                    }
                },
                {
                    "path": "ecommerce",
                    "name": "Ecommerce",
                    "component": "/dashboard/ecommerce",
                    "meta": {
                        "title": "menus.dashboard.ecommerce",
                        "icon": "ri:bar-chart-box-line",
                        "keepAlive": false
                    }
                }
            ]
        },
        {
            "path": "/template",
            "name": "Template",
            "component": "/index/index",
            "meta": {
                "title": "menus.template.title",
                "icon": "ri:apps-2-line"
            },
            "children": [
                {
                    "path": "cards",
                    "name": "Cards",
                    "component": "/template/cards",
                    "meta": {
                        "title": "menus.template.cards",
                        "icon": "ri:wallet-line",
                        "keepAlive": false
                    }
                },
                {
                    "path": "banners",
                    "name": "Banners",
                    "component": "/template/banners",
                    "meta": {
                        "title": "menus.template.banners",
                        "icon": "ri:rectangle-line",
                        "keepAlive": false
                    }
                },
                {
                    "path": "charts",
                    "name": "Charts",
                    "component": "/template/charts",
                    "meta": {
                        "title": "menus.template.charts",
                        "icon": "ri:bar-chart-box-line",
                        "keepAlive": false
                    }
                },
                {
                    "path": "map",
                    "name": "Map",
                    "component": "/template/map",
                    "meta": {
                        "title": "menus.template.map",
                        "icon": "ri:map-pin-line",
                        "keepAlive": true
                    }
                },
                {
                    "path": "chat",
                    "name": "Chat",
                    "component": "/template/chat",
                    "meta": {
                        "title": "menus.template.chat",
                        "icon": "ri:message-3-line",
                        "keepAlive": true
                    }
                },
                {
                    "path": "calendar",
                    "name": "Calendar",
                    "component": "/template/calendar",
                    "meta": {
                        "title": "menus.template.calendar",
                        "icon": "ri:calendar-2-line",
                        "keepAlive": true
                    }
                },
                {
                    "path": "pricing",
                    "name": "Pricing",
                    "component": "/template/pricing",
                    "meta": {
                        "title": "menus.template.pricing",
                        "icon": "ri:money-cny-box-line",
                        "keepAlive": true,
                        "isFullPage": true
                    }
                }
            ]
        },
        {
            "path": "/widgets",
            "name": "Widgets",
            "component": "/index/index",
            "meta": {
                "title": "menus.widgets.title",
                "icon": "ri:apps-2-add-line"
            },
            "children": [
                {
                    "path": "icon",
                    "name": "Icon",
                    "component": "/widgets/icon",
                    "meta": {
                        "title": "menus.widgets.icon",
                        "icon": "ri:palette-line",
                        "keepAlive": true
                    }
                },
                {
                    "path": "image-crop",
                    "name": "ImageCrop",
                    "component": "/widgets/image-crop",
                    "meta": {
                        "title": "menus.widgets.imageCrop",
                        "icon": "ri:screenshot-line",
                        "keepAlive": true
                    }
                },
                {
                    "path": "excel",
                    "name": "Excel",
                    "component": "/widgets/excel",
                    "meta": {
                        "title": "menus.widgets.excel",
                        "icon": "ri:download-2-line",
                        "keepAlive": true
                    }
                },
                {
                    "path": "video",
                    "name": "Video",
                    "component": "/widgets/video",
                    "meta": {
                        "title": "menus.widgets.video",
                        "icon": "ri:vidicon-line",
                        "keepAlive": true
                    }
                },
                {
                    "path": "count-to",
                    "name": "CountTo",
                    "component": "/widgets/count-to",
                    "meta": {
                        "title": "menus.widgets.countTo",
                        "icon": "ri:anthropic-line",
                        "keepAlive": false
                    }
                },
                {
                    "path": "wang-editor",
                    "name": "WangEditor",
                    "component": "/widgets/wang-editor",
                    "meta": {
                        "title": "menus.widgets.wangEditor",
                        "icon": "ri:t-box-line",
                        "keepAlive": true
                    }
                },
                {
                    "path": "watermark",
                    "name": "Watermark",
                    "component": "/widgets/watermark",
                    "meta": {
                        "title": "menus.widgets.watermark",
                        "icon": "ri:water-flash-line",
                        "keepAlive": true
                    }
                },
                {
                    "path": "context-menu",
                    "name": "ContextMenu",
                    "component": "/widgets/context-menu",
                    "meta": {
                        "title": "menus.widgets.contextMenu",
                        "icon": "ri:menu-2-line",
                        "keepAlive": true
                    }
                },
                {
                    "path": "qrcode",
                    "name": "Qrcode",
                    "component": "/widgets/qrcode",
                    "meta": {
                        "title": "menus.widgets.qrcode",
                        "icon": "ri:qr-code-line",
                        "keepAlive": true
                    }
                },
                {
                    "path": "drag",
                    "name": "Drag",
                    "component": "/widgets/drag",
                    "meta": {
                        "title": "menus.widgets.drag",
                        "icon": "ri:drag-move-fill",
                        "keepAlive": true
                    }
                },
                {
                    "path": "text-scroll",
                    "name": "TextScroll",
                    "component": "/widgets/text-scroll",
                    "meta": {
                        "title": "menus.widgets.textScroll",
                        "icon": "ri:input-method-line",
                        "keepAlive": true
                    }
                },
                {
                    "path": "fireworks",
                    "name": "Fireworks",
                    "component": "/widgets/fireworks",
                    "meta": {
                        "title": "menus.widgets.fireworks",
                        "icon": "ri:magic-line",
                        "keepAlive": true,
                        "showTextBadge": "Hot"
                    }
                },
                {
                    "path": "/outside/iframe/elementui",
                    "name": "ElementUI",
                    "component": "",
                    "meta": {
                        "title": "menus.widgets.elementUI",
                        "icon": "ri:apps-2-line",
                        "keepAlive": false,
                        "link": "https://element-plus.org/zh-CN/component/overview.html",
                        "isIframe": true
                    }
                }
            ]
        },
        {
            "path": "/examples",
            "name": "Examples",
            "component": "/index/index",
            "meta": {
                "title": "menus.examples.title",
                "icon": "ri:sparkling-line"
            },
            "children": [
                {
                    "path": "permission",
                    "name": "Permission",
                    "component": "",
                    "meta": {
                        "title": "menus.examples.permission.title",
                        "icon": "ri:fingerprint-line"
                    },
                    "children": [
                        {
                            "path": "switch-role",
                            "name": "PermissionSwitchRole",
                            "component": "/examples/permission/switch-role",
                            "meta": {
                                "title": "menus.examples.permission.switchRole",
                                "icon": "ri:contacts-line",
                                "keepAlive": true
                            }
                        },
                        {
                            "path": "button-auth",
                            "name": "PermissionButtonAuth",
                            "component": "/examples/permission/button-auth",
                            "meta": {
                                "title": "menus.examples.permission.buttonAuth",
                                "icon": "ri:mouse-line",
                                "keepAlive": true,
                                "authList": [
                                    {
                                        "title": "新增",
                                        "authMark": "add"
                                    },
                                    {
                                        "title": "编辑",
                                        "authMark": "edit"
                                    },
                                    {
                                        "title": "删除",
                                        "authMark": "delete"
                                    },
                                    {
                                        "title": "导出",
                                        "authMark": "export"
                                    },
                                    {
                                        "title": "查看",
                                        "authMark": "view"
                                    },
                                    {
                                        "title": "发布",
                                        "authMark": "publish"
                                    },
                                    {
                                        "title": "配置",
                                        "authMark": "config"
                                    },
                                    {
                                        "title": "管理",
                                        "authMark": "manage"
                                    }
                                ]
                            }
                        },
                        {
                            "path": "page-visibility",
                            "name": "PermissionPageVisibility",
                            "component": "/examples/permission/page-visibility",
                            "meta": {
                                "title": "menus.examples.permission.pageVisibility",
                                "icon": "ri:user-3-line",
                                "keepAlive": true,
                                "roles": [
                                    "R_SUPER"
                                ]
                            }
                        }
                    ]
                },
                {
                    "path": "tabs",
                    "name": "Tabs",
                    "component": "/examples/tabs",
                    "meta": {
                        "title": "menus.examples.tabs",
                        "icon": "ri:price-tag-line"
                    }
                },
                {
                    "path": "tables/basic",
                    "name": "TablesBasic",
                    "component": "/examples/tables/basic",
                    "meta": {
                        "title": "menus.examples.tablesBasic",
                        "icon": "ri:layout-grid-line",
                        "keepAlive": true
                    }
                },
                {
                    "path": "tables",
                    "name": "Tables",
                    "component": "/examples/tables",
                    "meta": {
                        "title": "menus.examples.tables",
                        "icon": "ri:table-3",
                        "keepAlive": true
                    }
                },
                {
                    "path": "forms",
                    "name": "Forms",
                    "component": "/examples/forms",
                    "meta": {
                        "title": "menus.examples.forms",
                        "icon": "ri:table-view",
                        "keepAlive": true
                    }
                },
                {
                    "path": "form/search-bar",
                    "name": "SearchBar",
                    "component": "/examples/forms/search-bar",
                    "meta": {
                        "title": "menus.examples.searchBar",
                        "icon": "ri:table-line",
                        "keepAlive": true
                    }
                },
                {
                    "path": "tables/tree",
                    "name": "TablesTree",
                    "component": "/examples/tables/tree",
                    "meta": {
                        "title": "menus.examples.tablesTree",
                        "icon": "ri:layout-2-line",
                        "keepAlive": true
                    }
                },
                {
                    "path": "socket-chat",
                    "name": "SocketChat",
                    "component": "/examples/socket-chat",
                    "meta": {
                        "title": "menus.examples.socketChat",
                        "icon": "ri:shake-hands-line",
                        "keepAlive": true,
                        "showTextBadge": "New"
                    }
                }
            ]
        },
        {
            "path": "/system",
            "name": "System",
            "component": "/index/index",
            "meta": {
                "title": "menus.system.title",
                "icon": "ri:user-3-line"
            },
            "children": [
                {
                    "path": "user",
                    "name": "User",
                    "component": "/system/user",
                    "meta": {
                        "title": "menus.system.user",
                        "icon": "ri:user-line",
                        "keepAlive": true,
                        "roles": [
                            "R_SUPER",
                            "R_ADMIN"
                        ]
                    }
                },
                {
                    "path": "role",
                    "name": "Role",
                    "component": "/system/role",
                    "meta": {
                        "title": "menus.system.role",
                        "icon": "ri:user-settings-line",
                        "keepAlive": true,
                        "roles": [
                            "R_SUPER"
                        ]
                    }
                },
                {
                    "path": "user-center",
                    "name": "UserCenter",
                    "component": "/system/user-center",
                    "meta": {
                        "title": "menus.system.userCenter",
                        "icon": "ri:user-line",
                        "isHide": true,
                        "keepAlive": true,
                        "isHideTab": true
                    }
                },
                {
                    "path": "menu",
                    "name": "Menus",
                    "component": "/system/menu",
                    "meta": {
                        "title": "menus.system.menu",
                        "icon": "ri:menu-line",
                        "keepAlive": true,
                        "roles": [
                            "R_SUPER"
                        ],
                        "authList": [
                            {
                                "title": "新增",
                                "authMark": "add"
                            },
                            {
                                "title": "编辑",
                                "authMark": "edit"
                            },
                            {
                                "title": "删除",
                                "authMark": "delete"
                            }
                        ]
                    }
                },
                {
                    "path": "nested",
                    "name": "Nested",
                    "component": "",
                    "meta": {
                        "title": "menus.system.nested",
                        "icon": "ri:menu-unfold-3-line",
                        "keepAlive": true
                    },
                    "children": [
                        {
                            "path": "menu1",
                            "name": "NestedMenu1",
                            "component": "/system/nested/menu1",
                            "meta": {
                                "title": "menus.system.menu1",
                                "icon": "ri:align-justify",
                                "keepAlive": true
                            }
                        },
                        {
                            "path": "menu2",
                            "name": "NestedMenu2",
                            "component": "",
                            "meta": {
                                "title": "menus.system.menu2",
                                "icon": "ri:align-justify",
                                "keepAlive": true
                            },
                            "children": [
                                {
                                    "path": "menu2-1",
                                    "name": "NestedMenu2-1",
                                    "component": "/system/nested/menu2",
                                    "meta": {
                                        "title": "menus.system.menu21",
                                        "icon": "ri:align-justify",
                                        "keepAlive": true
                                    }
                                }
                            ]
                        },
                        {
                            "path": "menu3",
                            "name": "NestedMenu3",
                            "component": "",
                            "meta": {
                                "title": "menus.system.menu3",
                                "icon": "ri:align-justify",
                                "keepAlive": true
                            },
                            "children": [
                                {
                                    "path": "menu3-1",
                                    "name": "NestedMenu3-1",
                                    "component": "/system/nested/menu3",
                                    "meta": {
                                        "title": "menus.system.menu31",
                                        "keepAlive": true
                                    }
                                },
                                {
                                    "path": "menu3-2",
                                    "name": "NestedMenu3-2",
                                    "component": "",
                                    "meta": {
                                        "title": "menus.system.menu32",
                                        "keepAlive": true
                                    },
                                    "children": [
                                        {
                                            "path": "menu3-2-1",
                                            "name": "NestedMenu3-2-1",
                                            "component": "/system/nested/menu3/menu3-2",
                                            "meta": {
                                                "title": "menus.system.menu321",
                                                "keepAlive": true
                                            }
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
            ]
        },
        {
            "path": "/article",
            "name": "Article",
            "component": "/index/index",
            "meta": {
                "title": "menus.article.title",
                "icon": "ri:book-2-line"
            },
            "children": [
                {
                    "path": "article-list",
                    "name": "ArticleList",
                    "component": "/article/list",
                    "meta": {
                        "title": "menus.article.articleList",
                        "icon": "ri:article-line",
                        "keepAlive": true,
                        "authList": [
                            {
                                "title": "新增",
                                "authMark": "add"
                            },
                            {
                                "title": "编辑",
                                "authMark": "edit"
                            }
                        ]
                    }
                },
                {
                    "path": "detail/:id",
                    "name": "ArticleDetail",
                    "component": "/article/detail",
                    "meta": {
                        "title": "menus.article.articleDetail",
                        "isHide": true,
                        "keepAlive": true,
                        "activePath": "/article/article-list"
                    }
                },
                {
                    "path": "comment",
                    "name": "ArticleComment",
                    "component": "/article/comment",
                    "meta": {
                        "title": "menus.article.comment",
                        "icon": "ri:mail-line",
                        "keepAlive": true
                    }
                },
                {
                    "path": "publish",
                    "name": "ArticlePublish",
                    "component": "/article/publish",
                    "meta": {
                        "title": "menus.article.articlePublish",
                        "icon": "ri:telegram-2-line",
                        "keepAlive": true,
                        "authList": [
                            {
                                "title": "发布",
                                "authMark": "add"
                            }
                        ]
                    }
                }
            ]
        },
        {
            "path": "/result",
            "name": "Result",
            "component": "/index/index",
            "meta": {
                "title": "menus.result.title",
                "icon": "ri:checkbox-circle-line"
            },
            "children": [
                {
                    "path": "success",
                    "name": "ResultSuccess",
                    "component": "/result/success",
                    "meta": {
                        "title": "menus.result.success",
                        "icon": "ri:checkbox-circle-line",
                        "keepAlive": true
                    }
                },
                {
                    "path": "fail",
                    "name": "ResultFail",
                    "component": "/result/fail",
                    "meta": {
                        "title": "menus.result.fail",
                        "icon": "ri:close-circle-line",
                        "keepAlive": true
                    }
                }
            ]
        },
        {
            "path": "/exception",
            "name": "Exception",
            "component": "/index/index",
            "meta": {
                "title": "menus.exception.title",
                "icon": "ri:error-warning-line"
            },
            "children": [
                {
                    "path": "403",
                    "name": "403",
                    "component": "/exception/403",
                    "meta": {
                        "title": "menus.exception.forbidden",
                        "keepAlive": true,
                        "isFullPage": true
                    }
                },
                {
                    "path": "404",
                    "name": "404",
                    "component": "/exception/404",
                    "meta": {
                        "title": "menus.exception.notFound",
                        "keepAlive": true,
                        "isFullPage": true
                    }
                },
                {
                    "path": "500",
                    "name": "500",
                    "component": "/exception/500",
                    "meta": {
                        "title": "menus.exception.serverError",
                        "keepAlive": true,
                        "isFullPage": true
                    }
                }
            ]
        },
        {
            "path": "/safeguard",
            "name": "Safeguard",
            "component": "/index/index",
            "meta": {
                "title": "menus.safeguard.title",
                "icon": "ri:shield-check-line",
                "keepAlive": false
            },
            "children": [
                {
                    "path": "server",
                    "name": "SafeguardServer",
                    "component": "/safeguard/server",
                    "meta": {
                        "title": "menus.safeguard.server",
                        "icon": "ri:hard-drive-3-line",
                        "keepAlive": true
                    }
                }
            ]
        },
        {
            "name": "Document",
            "path": "",
            "component": "",
            "meta": {
                "title": "menus.help.document",
                "icon": "ri:bill-line",
                "link": "https://www.artd.pro/docs/zh/",
                "isIframe": false,
                "keepAlive": false,
                "isFirstLevel": true
            }
        },
        {
            "name": "LiteVersion",
            "path": "",
            "component": "",
            "meta": {
                "title": "menus.help.liteVersion",
                "icon": "ri:bus-2-line",
                "link": "https://www.artd.pro/docs/zh/guide/lite-version.html",
                "isIframe": false,
                "keepAlive": false,
                "isFirstLevel": true
            }
        },
        {
            "name": "ChangeLog",
            "path": "/change/log",
            "component": "/change/log",
            "meta": {
                "title": "menus.plan.log",
                "icon": "ri:gamepad-line",
                "keepAlive": false,
                "isFirstLevel": true
            }
        }
    ]
}
```

### 4. 字段详细说明

#### 4.1 基础字段

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `id` | number | 否 | 菜单唯一标识，建议后端数据库主键 |
| `path` | string | 是 | 路由路径，一级菜单以 `/` 开头，子菜单不要 |
| `name` | string | 否 | 路由名称，用于命名路由 |
| `redirect` | string | 否 | 父级菜单的重定向路径 |
| `component` | string | 否 | 组件路径，相对于 `src/views/` 目录 |

#### 4.2 meta 核心字段

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `title` | string | 是 | 菜单显示标题 |
| `icon` | string | 否 | Element Plus 图标名称 |
| `isHide` | boolean | 否 | 是否在侧边栏菜单中隐藏 |
| `isHideTab` | boolean | 否 | 是否在浏览器标签页中隐藏 |
| `keepAlive` | boolean | 否 | 是否启用页面缓存 |

#### 4.3 权限控制字段

| 字段 | 类型 | 说明 |
|------|------|------|
| `roles` | string[] | 可访问该菜单的角色列表，为空则所有人可访问 |
| `authList` | Array | 页面内操作按钮权限配置 |
| `authMark` | string | 页面访问权限标识符 |

#### 4.4 特殊功能字段

| 字段 | 类型 | 说明 |
|------|------|------|
| `link` | string | 外部链接地址，设置后忽略 component |
| `isIframe` | boolean | 是否以 iframe 方式嵌入外部页面 |
| `isFullPage` | boolean | 是否全屏显示（隐藏侧边栏和头部） |
| `showBadge/showTextBadge` | boolean/string | 菜单徽章显示控制 |

### 5. 开发注意事项

1. **路径规范**：一级菜单路径以 `/` 开头，子菜单路径不包含 `/`，前端会自动拼接
2. **组件路径**：相对于 `src/views/` 目录，不需要 `.vue` 后缀
3. **权限控制**：与用户登录后返回的 [`roles`](src/types/api/api.d.ts#L51) 字段进行匹配
4. **菜单过滤**：前端会根据用户权限自动过滤菜单，参见 [`MenuProcessor.processBackendMenu()`](src/router/core/MenuProcessor.ts#L49-L53)
5. **递归结构**：支持无限层级嵌套，但建议不超过 3-4 层

### 6. 错误响应示例

```json
{
  "code": 401,
  "message": "未授权访问",
  "data": null
}

{
  "code": 500,
  "message": "服务器内部错误",
  "data": null
}
```



## 后端权限控制介绍：

### 原理

后端生成菜单列表。用户登录后，接口返回菜单数据，前端校验后动态注册路由，实现权限控制。

### 数据结构

菜单数据结构定义位于：`/src/router/routes/asyncRoutes.ts`

```
[
  {
    id: 4,
    path: "/system",
    name: "System",
    component: "/index/index",
    meta: {
      title: "menus.system.title",
      icon: "ri:user-3-line",
      keepAlive: false,
    },
    children: [
      {
        id: 41,
        path: "user",
        name: "User",
        component: "/system/user",
        meta: {
          title: "menus.system.user",
          keepAlive: true,
        },
      },
      {
        id: 42,
        path: "role",
        name: "Role",
        component: "/system/role",
        meta: {
          title: "menus.system.role",
          keepAlive: true,
        },
      },
    ],
  },
];
```

### 注意事项

- 后端返回的菜单数据结构必须与前端定义一致，否则可能导致路由注册失败。

  

## 按钮权限控制

按钮权限控制支持精细化管理，通过用户角色或接口返回的权限码动态控制按钮显示。

### 权限码

权限码适用于前端和后端控制模式：

- **前端控制模式**：登录接口需返回权限码列表。
- **后端控制模式**：菜单列表需包含 `authList` 字段，定义按钮权限。

#### 配置示例（后端控制模式）

```ts
[
  {
    id: 44,
    path: "menu",
    name: "Menus",
    component: "/system/menu",
    meta: {
      title: "menus.system.menu",
      keepAlive: true,
      authList: [
        { id: 441, title: "新增", authMark: "add" },
        { id: 442, title: "编辑", authMark: "edit" },
      ],
    },
  },
];
```

#### 使用方式

通过系统提供的 `hasAuth` 方法控制按钮显示：

```ts
import { useAuth } from "@/composables/useAuth";
const { hasAuth } = useAuth();
```

```html
<ElButton v-if="hasAuth('add')">添加</ElButton>
```

### 自定义指令（v-auth）

在后端控制模式下，可通过自定义指令 `v-auth` 基于 `authList` 的 `authMark` 控制按钮显示。

#### 配置示例

ts

```
[
  {
    id: 44,
    path: "menu",
    name: "Menus",
    component: "/system/menu",
    meta: {
      title: "menus.system.menu",
      keepAlive: true,
      authList: [
        { id: 441, title: "新增", authMark: "add" },
        { id: 442, title: "编辑", authMark: "edit" },
        { id: 443, title: "删除", authMark: "delete" },
      ],
    },
  },
];
```

#### 使用方式

vue

```
<ElButton v-auth="'add'">添加</ElButton>
```

### 自定义指令（v-roles）

可基于用户信息接口中返回的 `roles` 进行权限控制。

#### 用户接口

ts

```
{
    "userId": "1",
    "userName": "Super",
    "roles": [
        "R_SUPER"
    ],
    "buttons": [
        "B_CODE1",
        "B_CODE2",
        "B_CODE3"
    ]
}
```

#### 使用示例

ts

```
 <el-button v-roles="['R_SUPER', 'R_ADMIN']">按钮</el-button>
 <el-button v-roles="'R_ADMIN'">按钮</el-button>
```

## 注意事项

- 确保登录接口返回的角色或权限码与路由表配置一致。
- 后端控制模式下，菜单数据需严格遵循前端定义的结构。
- 测试权限控制时，验证不同角色用户的页面和按钮显示是否符合预期。
