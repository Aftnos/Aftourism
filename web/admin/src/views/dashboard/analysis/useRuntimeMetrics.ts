import { fetchRuntimeMetrics, type RuntimeDashboard } from '@/api/monitor'
import { ref } from 'vue'

const runtimeMetrics = ref<RuntimeDashboard>()
const loading = ref(false)

/**
 * 拉取 Redis 与后端性能指标的共享逻辑
 */
export function useRuntimeMetrics() {
  const load = async () => {
    loading.value = true
    try {
      runtimeMetrics.value = await fetchRuntimeMetrics()
    } finally {
      loading.value = false
    }
  }

  if (!runtimeMetrics.value && !loading.value) {
    load()
  }

  return {
    runtimeMetrics,
    loading,
    reload: load
  }
}
