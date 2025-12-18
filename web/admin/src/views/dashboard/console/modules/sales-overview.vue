<template>
  <div class="art-card h-105 p-5 mb-5 max-sm:mb-4">
    <div class="art-card-header">
      <div class="title">
        <h4>访问量</h4>
        <p>今年增长<span class="text-success">+15%</span></p>
      </div>
    </div>
    <ArtLineChart
      height="calc(100% - 56px)"
      :data="data"
      :xAxisData="xAxisData"
      :showAreaColor="true"
      :showAxisLine="false"
    />
  </div>
</template>

<script setup lang="ts">
  import { onMounted, ref } from 'vue'
  import { fetchPortalMonthlyTrend, type VisitTrendItem } from '@/api/dashboard'

  /**
   * 全年访问量数据
   */
  const data = ref<number[]>([])

  /**
   * X 轴月份标签
   */
  const xAxisData = ref<string[]>([])

  const loadMonthlyTrend = async () => {
    try {
      const list: VisitTrendItem[] = await fetchPortalMonthlyTrend()
      xAxisData.value = list.map((item) => item.statDate.slice(5, 7) + '月')
      data.value = list.map((item) => item.pvCount)
    } catch (error) {
      console.error('加载月度访问趋势失败', error)
    }
  }

  onMounted(loadMonthlyTrend)
</script>
