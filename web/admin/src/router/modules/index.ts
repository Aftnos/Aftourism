import { AppRouteRecord } from '@/types/router'
import { dashboardRoutes } from './dashboard'
import { templateRoutes } from './template'
import { widgetsRoutes } from './widgets'
import { examplesRoutes } from './examples'
import { systemRoutes } from './system'
import { userRoutes } from './user'

import { articleRoutes } from './article'
import { resultRoutes } from './result'
import { exceptionRoutes } from './exception'
import { safeguardRoutes } from './safeguard'
import { newsRoutes } from './news'// 新闻管理
import { helpRoutes } from './help'
import { feedbackRoutes } from './feedback'

/**
 * 导出所有模块化路由
 */
export const routeModules: AppRouteRecord[] = [
  dashboardRoutes,
  templateRoutes,
  widgetsRoutes,
  examplesRoutes,
  userRoutes,
  systemRoutes,
  articleRoutes,
  feedbackRoutes,
  resultRoutes,
  exceptionRoutes,
  safeguardRoutes,
  newsRoutes,
  ...helpRoutes
]
