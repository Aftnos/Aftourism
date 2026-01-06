// 中文注释：前台用户权限目录配置，用于后台角色权限页面的前台权限树展示
export interface PortalPermissionDefinition {
  resourceKey: string
  action: string
  description: string
  groupKey: string
  groupLabel: string
}

// 中文注释：按功能模块整理前台权限点
export const portalPermissionCatalog: PortalPermissionDefinition[] = [
  {
    groupKey: 'portal-content',
    groupLabel: '内容浏览',
    resourceKey: 'PORTAL_HOME',
    action: 'READ',
    description: '首页内容-查看'
  },
  {
    groupKey: 'portal-content',
    groupLabel: '内容浏览',
    resourceKey: 'PORTAL_NEWS',
    action: 'READ',
    description: '新闻资讯-查看'
  },
  {
    groupKey: 'portal-content',
    groupLabel: '内容浏览',
    resourceKey: 'PORTAL_NOTICE',
    action: 'READ',
    description: '通知公告-查看'
  },
  {
    groupKey: 'portal-content',
    groupLabel: '内容浏览',
    resourceKey: 'PORTAL_SCENIC',
    action: 'READ',
    description: '景区信息-查看'
  },
  {
    groupKey: 'portal-content',
    groupLabel: '内容浏览',
    resourceKey: 'PORTAL_VENUE',
    action: 'READ',
    description: '场馆信息-查看'
  },
  {
    groupKey: 'portal-content',
    groupLabel: '内容浏览',
    resourceKey: 'PORTAL_ACTIVITY',
    action: 'READ',
    description: '活动信息-查看'
  },
  {
    groupKey: 'portal-interaction',
    groupLabel: '互动功能',
    resourceKey: 'PORTAL_FAVORITE',
    action: 'READ',
    description: '收藏内容-查看'
  },
  {
    groupKey: 'portal-interaction',
    groupLabel: '互动功能',
    resourceKey: 'PORTAL_FAVORITE',
    action: 'CREATE',
    description: '收藏内容-新增'
  },
  {
    groupKey: 'portal-interaction',
    groupLabel: '互动功能',
    resourceKey: 'PORTAL_FAVORITE',
    action: 'DELETE',
    description: '收藏内容-取消'
  },
  {
    groupKey: 'portal-interaction',
    groupLabel: '互动功能',
    resourceKey: 'PORTAL_ACTIVITY_APPLY',
    action: 'CREATE',
    description: '活动报名-提交'
  },
  {
    groupKey: 'portal-interaction',
    groupLabel: '互动功能',
    resourceKey: 'PORTAL_ACTIVITY_COMMENT',
    action: 'CREATE',
    description: '活动评论-发布'
  },
  {
    groupKey: 'portal-interaction',
    groupLabel: '互动功能',
    resourceKey: 'PORTAL_ACTIVITY_COMMENT',
    action: 'DELETE',
    description: '活动评论-删除'
  },
  {
    groupKey: 'portal-profile',
    groupLabel: '个人中心',
    resourceKey: 'PORTAL_PROFILE',
    action: 'READ',
    description: '个人资料-查看'
  },
  {
    groupKey: 'portal-profile',
    groupLabel: '个人中心',
    resourceKey: 'PORTAL_PROFILE',
    action: 'UPDATE',
    description: '个人资料-修改'
  }
]

// 中文注释：快速判断是否属于前台权限资源
export const portalPermissionResourceSet = new Set(
  portalPermissionCatalog.map((item) => item.resourceKey)
)
