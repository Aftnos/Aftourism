<template>
  <div class="art-card h-82 p-5 mb-5 overflow-hidden max-lg:h-auto max-sm:mb-4">
    <div class="art-card-header pr-0">
      <div class="title">
        <h4>后端运行状态</h4>
        <p>CPU、线程与运行时长</p>
      </div>
    </div>

    <ElRow :gutter="20">
      <ElCol :xl="12" :lg="12" :xs="24">
        <div class="flex flex-col h-55 border border-g-300/85 rounded-xl p-5 max-lg:mb-4">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-g-500 text-sm">CPU 使用率</p>
              <ArtCountTo class="text-2xl font-medium" :target="cpuUsage" suffix="%" :duration="1000" />
            </div>
            <div class="size-12 rounded-lg flex-cc bg-theme/10">
              <ArtSvgIcon icon="ri:cpu-line" class="text-xl text-theme" />
            </div>
          </div>
          <p class="text-g-500 text-sm mt-4">进程 CPU：{{ processCpuUsage }}% · 核心数：{{ cpuCores }}</p>
        </div>
      </ElCol>
      <ElCol :xl="12" :lg="12" :xs="24">
        <div class="flex flex-col h-55 border border-g-300/85 rounded-xl p-5 max-lg:mb-4">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-g-500 text-sm">运行时长</p>
              <p class="text-2xl font-medium">{{ uptimeLabel }}</p>
            </div>
            <div class="size-12 rounded-lg flex-cc bg-success/10">
              <ArtSvgIcon icon="ri:time-line" class="text-xl text-success" />
            </div>
          </div>
          <p class="text-g-500 text-sm mt-4">活跃线程：{{ threadCount }} · 守护线程：{{ daemonThreadCount }}</p>
        </div>
      </ElCol>
    </ElRow>
  </div>
</template>

<script setup lang="ts">
  import { computed } from 'vue'

  import { useRuntimeMetrics } from '../useRuntimeMetrics'

  const { runtimeMetrics } = useRuntimeMetrics()

  const cpuUsage = computed(() => Number(runtimeMetrics.value?.systemMetrics.systemCpuUsage ?? 0))
  const processCpuUsage = computed(() => Number(runtimeMetrics.value?.systemMetrics.processCpuUsage ?? 0))
  const cpuCores = computed(() => runtimeMetrics.value?.systemMetrics.cpuCores ?? 0)
  const threadCount = computed(() => runtimeMetrics.value?.systemMetrics.threadCount ?? 0)
  const daemonThreadCount = computed(() => runtimeMetrics.value?.systemMetrics.daemonThreadCount ?? 0)

  const uptimeLabel = computed(() => {
    const seconds = runtimeMetrics.value?.systemMetrics.uptimeSeconds ?? 0
    const hours = Math.floor(seconds / 3600)
    const minutes = Math.floor((seconds % 3600) / 60)
    return `${hours} 小时 ${minutes} 分`
  })
</script>
