<template>
  <div class="art-card h-82 p-5 mb-5 max-sm:mb-4">
    <div class="art-card-header">
      <div class="title">
        <h4>Redis 命中与耗时</h4>
        <p>命中/未命中对比与命令耗时</p>
      </div>
    </div>
    <ArtLineChart
      height="calc(100% - 30px)"
      :data="chartData"
      :xAxisData="xAxisData"
      :showLegend="true"
      :showAxisLabel="true"
      :showAxisLine="false"
      :showSplitLine="true"
    />
  </div>
</template>

<script setup lang="ts">
  import { computed } from 'vue'
  import type { LineDataItem } from '@/types/component/chart'

  import { useRuntimeMetrics } from '../useRuntimeMetrics'

  const { runtimeMetrics } = useRuntimeMetrics()

  const xAxisData = computed(() => ['命中', '未命中', '淘汰'])

  const chartData = computed<LineDataItem[]>(() => {
    const metrics = runtimeMetrics.value?.redisMetrics
    const hits = metrics?.keyspaceHits ?? 0
    const misses = metrics?.keyspaceMisses ?? 0
    const evicted = metrics?.evictedKeys ?? 0
    const latency = Number(metrics?.avgCommandUsec ?? 0)
    const maxLatency = Number(metrics?.maxCommandUsec ?? 0)

    return [
      {
        name: '命中情况 (次数)',
        data: [hits, misses, evicted]
      },
      {
        name: '命令耗时 (μs)',
        data: [latency, maxLatency, maxLatency]
      }
    ]
  })
</script>
