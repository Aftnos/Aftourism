<!-- 分析页页面 -->
<template>
  <div>
    <ElRow :gutter="20">
      <ElCol :xl="14" :lg="15" :xs="24">
        <RedisOverview />
      </ElCol>
      <ElCol :xl="10" :lg="9" :xs="24">
        <RedisEfficiency />
      </ElCol>
    </ElRow>

    <ElRow :gutter="20">
      <ElCol :xl="14" :lg="15" :xs="24">
        <SystemRuntime />
      </ElCol>
      <ElCol :xl="10" :lg="9" :xs="24">
        <ResourceUsage />
      </ElCol>
    </ElRow>
  </div>
</template>

<script setup lang="ts">
  import RedisEfficiency from './modules/redis-efficiency.vue'
  import RedisOverview from './modules/redis-overview.vue'
  import ResourceUsage from './modules/resource-usage.vue'
  import SystemRuntime from './modules/system-runtime.vue'

  defineOptions({ name: 'Analysis' })
</script>
