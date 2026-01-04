import axios from 'axios'

/**
 * 行政区划 API 参数接口
 */
export interface AdminDivisionParams {
  /** 开发者ID，例：id=10000000 */
  id: string
  /** 用户中心通讯秘钥，例：key=15he5h15ty854j5sr152hs2 */
  key: string
  /** 查询级别 1=省级，2=市级，3=县级，4=镇级，5=村级 */
  type: 1 | 2 | 3 | 4 | 5
  /** 查询省级以下提供省级名称，URL编码 */
  sheng?: string
  /** 查询市级以下提供市级名称，URL编码 */
  shi?: string
  /** 查询区县级以下提供县级名称，URL编码 */
  xian?: string
  /** 查询乡镇级以下提供镇级名称，URL编码 */
  zhen?: string
  /** 动态秘钥 */
  dkey?: string
  /** 用户IP */
  uip?: string
}

/**
 * 行政区划 API 响应接口
 */
export interface AdminDivisionResponse {
  /** 状态码 200成功，400错误 */
  code: number
  /** 如果状态码返回200则msg返回地名字符串，以 | 分割。否则状态码返回400则msg返回错误信息提示 */
  msg: string
  /** 返回结果数量 */
  num?: string
}


const API_URL = 'https://cn.apihz.cn/api/other/xzqh.php' 
const API_ID = '10011717'
const API_KEY = 'e7711a4b3d2f3008565ba6dd6cb46c53'

/**
 * 获取行政区划数据
 * @param params 查询参数
 */
export const fetchAdminDivision = async (params: Omit<AdminDivisionParams, 'id' | 'key'>) => {
  // 使用 axios 直接请求，绕过项目内部的拦截器，因为外部 API 的响应结构不符合项目内部规范
  const response = await axios.get<AdminDivisionResponse>(API_URL, {
    params: {
      ...params,
      id: API_ID,
      key: API_KEY,
    }
  })
  return response.data
}

/**
 * 解析 API 返回的 msg 字符串为数组
 * @param msg 返回的消息字符串
 */
export const parseAdminDivisionMsg = (msg: string): string[] => {
  if (!msg) return []
  return msg.split('|').filter(Boolean)
}
