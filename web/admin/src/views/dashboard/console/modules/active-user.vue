<template>
  <div class="art-card h-105 p-4 box-border mb-5 max-sm:mb-4">
    <ArtBarChart
      class="box-border p-2"
      barWidth="50%"
      height="13.7rem"
      :showAxisLine="false"
      :data="chartData"
      :xAxisData="xAxisLabels"
    />
    <div class="ml-1">
      <h3 class="mt-5 text-lg font-medium">访问数据</h3>
      <p class="mt-1 text-sm">
        比上周
        <span :class="isGrowthPositive ? 'text-success' : 'text-danger'" class="font-medium">
          {{ growthRate }}
        </span>
      </p>
    </div>
    <div class="flex-b mt-7">
      <div class="flex-1" v-for="(item, index) in list" :key="index">
        <p class="text-2xl text-g-900">{{ item.num }}</p>
        <p class="text-xs text-g-500">{{ item.name }}</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { onMounted, reactive, ref } from 'vue'
  import { fetchPortalOverview, fetchPortalVisitTrend, type PortalOverview, type VisitTrendItem } from '@/api/dashboard'

  interface UserStatItem {
    name: string
    num: string
  }

  // 最近7天日期标签与PV数据
  const xAxisLabels = ref<string[]>([])
  const chartData = ref<number[]>([])
  const growthRate = ref<string>('0%')
  const isGrowthPositive = ref<boolean>(true)

  /**
   * 用户统计数据列表
   * 包含总用户量、总访问量、日访问量和周同比等关键指标
   */
  const list = reactive<UserStatItem[]>([
    { name: '总用户量', num: '0' },
    { name: '总访问量', num: '0' },
    { name: '日访问量', num: '0' },
    { name: '今日独立访客', num: '0' }
  ])

  const loadTrend = async () => {
    try {
      // 获取14天数据用于计算周同比
      const trend: VisitTrendItem[] = await fetchPortalVisitTrend(14)
      
      // 计算周同比
      if (trend.length >= 14) {
        const currentWeek = trend.slice(7, 14)
        const lastWeek = trend.slice(0, 7)
        
        const currentTotal = currentWeek.reduce((sum, item) => sum + item.pvCount, 0)
        const lastTotal = lastWeek.reduce((sum, item) => sum + item.pvCount, 0)
        
        if (lastTotal > 0) {
          const rate = ((currentTotal - lastTotal) / lastTotal) * 100
          growthRate.value = (rate > 0 ? '+' : '') + rate.toFixed(0) + '%'
          isGrowthPositive.value = rate >= 0
        } else if (currentTotal > 0) {
          growthRate.value = '+100%'
          isGrowthPositive.value = true
        }
      }

      // 图表只显示最近7天
      const recentTrend = trend.slice(-7)
      xAxisLabels.value = recentTrend.map((item) => item.statDate.slice(5))
      chartData.value = recentTrend.map((item) => item.pvCount)
      
      if (recentTrend.length > 0) {
        const today = recentTrend[recentTrend.length - 1]
        list[2].num = today.pvCount.toString()
        list[3].num = today.uvCount.toString()
      }
    } catch (error) {
      console.error('加载访问趋势失败', error)
    }
  }

  const loadOverview = async () => {
    try {
      const overview: PortalOverview = await fetchPortalOverview()
      list[0].num = overview.totalUsers.toString()
      list[1].num = overview.totalPv.toString()
      list[2].num = overview.todayPv.toString()
      list[3].num = overview.todayUv.toString()
    } catch (error) {
      console.error('加载门户概览失败', error)
    }
  }

  onMounted(() => {
    loadTrend()
    loadOverview()
  })
</script>
