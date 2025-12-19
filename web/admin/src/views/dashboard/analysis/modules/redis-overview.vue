<template>
  <div class="art-card h-82 p-5 mb-5 overflow-hidden max-lg:h-auto max-sm:mb-4">
    <div class="art-card-header pr-0">
      <div class="title">
        <h4>Redis 实时概览</h4>
        <p>连接状态与命中效率</p>
      </div>
      <div class="flex items-center space-x-2">
        <ElButton size="small" :loading="loading" @click="reload">刷新</ElButton>
      </div>
    </div>

    <div class="mt-2">
      <ElRow :gutter="20">
        <ElCol :span="6" :xs="24" v-for="item in redisCards" :key="item.label">
          <div
            class="flex px-5 flex-col justify-center h-55 border border-g-300/85 rounded-xl max-lg:mb-4 max-sm:flex-row max-sm:justify-between max-sm:items-center max-sm:h-40"
          >
            <div class="size-12 rounded-lg flex-cc bg-theme/10">
              <ArtSvgIcon :icon="item.icon" class="text-xl text-theme" />
            </div>

            <div class="max-sm:ml-4 mt-3.5 max-sm:mt-0 max-sm:text-end">
              <ArtCountTo class="text-2xl font-medium" :target="item.value" :duration="1000" />
              <p class="mt-2 text-base text-g-600 max-sm:mt-1">{{ item.label }}</p>
              <small class="text-g-500 mt-1 max-sm:mt-0.5">{{ item.desc }}</small>
            </div>
          </div>
        </ElCol>
      </ElRow>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { computed } from 'vue'

  import { useRuntimeMetrics } from '../useRuntimeMetrics'

  interface RedisCardItem {
    label: string
    value: number
    desc: string
    icon: string
  }

  const { runtimeMetrics, loading, reload } = useRuntimeMetrics()

  const redisCards = computed<RedisCardItem[]>(() => {
    const metrics = runtimeMetrics.value?.redisMetrics
    return [
      {
        label: '连接客户端',
        value: metrics?.connectedClients ?? 0,
        desc: `阻塞客户端 ${metrics?.blockedClients ?? 0} 个`,
        icon: 'ri:links-line'
      },
      {
        label: '命中率 (%)',
        value: Number(metrics?.hitRate ?? 0),
        desc: `命中 ${metrics?.keyspaceHits ?? 0} / 未命中 ${metrics?.keyspaceMisses ?? 0}`,
        icon: 'ri:target-line'
      },
      {
        label: '处理命令数',
        value: metrics?.totalCommands ?? 0,
        desc: '累计处理总命令数',
        icon: 'ri:stack-line'
      },
      {
        label: '平均耗时 (μs)',
        value: Number(metrics?.avgCommandUsec ?? 0),
        desc: `最大耗时 ${metrics?.maxCommandUsec ?? 0} μs`,
        icon: 'ri:timer-flash-line'
      }
    ]
  })
</script>
