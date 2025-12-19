import request from '@/utils/http'

export interface RedisRuntimeMetrics {
  connectedClients: number
  blockedClients: number
  hitRate?: number
  keyspaceHits: number
  keyspaceMisses: number
  totalCommands: number
  avgCommandUsec?: number
  maxCommandUsec?: number
  usedMemoryMb?: number
  memoryFragmentationRatio?: number
  evictedKeys: number
}

export interface SystemRuntimeMetrics {
  uptimeSeconds: number
  cpuCores: number
  systemLoadAverage?: number
  systemCpuUsage?: number
  processCpuUsage?: number
  heapUsedMb?: number
  heapMaxMb?: number
  heapUsageRate?: number
  threadCount: number
  daemonThreadCount: number
  freePhysicalMemoryMb?: number
  totalPhysicalMemoryMb?: number
}

export interface RuntimeDashboard {
  redisMetrics: RedisRuntimeMetrics
  systemMetrics: SystemRuntimeMetrics
}

export function fetchRuntimeMetrics() {
  return request.get<RuntimeDashboard>({
    url: '/api/admin/monitor/runtime'
  })
}
