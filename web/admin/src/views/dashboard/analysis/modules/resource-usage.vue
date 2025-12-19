<template>
  <div class="art-card h-82 p-5 mb-5 max-sm:mb-4">
    <div class="art-card-header">
      <div class="title">
        <h4>资源占用</h4>
        <p>堆内存与物理内存占用情况</p>
      </div>
    </div>

    <div class="mt-4 space-y-5">
      <div>
        <div class="flex items-center justify-between mb-2">
          <p class="text-g-500 text-sm">堆内存使用</p>
          <span class="text-g-500 text-sm">{{ heapLabel }}</span>
        </div>
        <ElProgress :percentage="Number(heapUsageRate)" :stroke-width="10" :striped="true" :color="'#409EFF'" />
      </div>

      <div>
        <div class="flex items-center justify-between mb-2">
          <p class="text-g-500 text-sm">Redis 内存使用</p>
          <span class="text-g-500 text-sm">{{ redisMemoryLabel }}</span>
        </div>
        <ElProgress :percentage="redisMemoryPercent" :stroke-width="10" :color="'#67C23A'" />
      </div>

      <div class="grid grid-cols-2 gap-4">
        <div class="p-4 border border-g-200 rounded-lg">
          <p class="text-g-500 text-sm">可用物理内存</p>
          <p class="text-lg font-medium">{{ freePhysicalMemory }} MB</p>
        </div>
        <div class="p-4 border border-g-200 rounded-lg">
          <p class="text-g-500 text-sm">物理内存总量</p>
          <p class="text-lg font-medium">{{ totalPhysicalMemory }} MB</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { computed } from 'vue'

  import { useRuntimeMetrics } from '../useRuntimeMetrics'

  const { runtimeMetrics } = useRuntimeMetrics()

  const heapUsageRate = computed(() => runtimeMetrics.value?.systemMetrics.heapUsageRate ?? 0)
  const heapLabel = computed(() => {
    const used = runtimeMetrics.value?.systemMetrics.heapUsedMb ?? 0
    const max = runtimeMetrics.value?.systemMetrics.heapMaxMb ?? 0
    return `${used} MB / ${max} MB`
  })

  const redisMemoryPercent = computed(() => {
    const used = runtimeMetrics.value?.redisMetrics.usedMemoryMb ?? 0
    const total = runtimeMetrics.value?.systemMetrics.totalPhysicalMemoryMb ?? 0
    if (!total) return 0
    return Math.min(100, Number(((used / total) * 100).toFixed(2)))
  })

  const redisMemoryLabel = computed(() => `${runtimeMetrics.value?.redisMetrics.usedMemoryMb ?? 0} MB`)
  const freePhysicalMemory = computed(() => runtimeMetrics.value?.systemMetrics.freePhysicalMemoryMb ?? 0)
  const totalPhysicalMemory = computed(() => runtimeMetrics.value?.systemMetrics.totalPhysicalMemoryMb ?? 0)
</script>
