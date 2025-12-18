<template>
  <div class="art-card p-5 h-128 overflow-hidden mb-5 max-sm:mb-4">
    <div class="art-card-header">
      <div class="title">
        <h4>新用户</h4>
        <p>本周新增 <span class="text-success">{{ stats.newUsersThisWeek }}</span> 人</p>
      </div>
      <div class="text-sm text-g-500">今日新增 {{ stats.newUsersToday }}</div>
    </div>
    <ArtTable
      class="w-full"
      :data="tableData"
      style="width: 100%"
      size="large"
      :border="false"
      :stripe="false"
      :header-cell-style="{ background: 'transparent' }"
    >
      <template #default>
        <ElTableColumn label="头像" prop="avatar" width="120px">
          <template #default="scope">
            <div class="flex items-center">
              <img class="size-9 rounded-lg" :src="scope.row.avatar || defaultAvatar" alt="avatar" />
              <span class="ml-2">{{ scope.row.username }}</span>
            </div>
          </template>
        </ElTableColumn>
        <ElTableColumn label="昵称" prop="nickname" />
        <ElTableColumn label="性别" prop="gender" width="100px" />
        <ElTableColumn label="电话" prop="phone" />
        <ElTableColumn label="邮箱" prop="email" />
        <ElTableColumn label="注册时间" prop="createTime" min-width="160" />
      </template>
    </ArtTable>
  </div>
</template>

<script setup lang="ts">
  import { onMounted, reactive } from 'vue'
  import { fetchPortalNewUsers, type NewUserItem, type NewUserStats } from '@/api/dashboard'
  import defaultAvatar from '@/assets/images/avatar/avatar1.webp'

  const stats = reactive<NewUserStats>({
    newUsersToday: 0,
    newUsersThisWeek: 0,
    latestUsers: [],
    weeklyTrend: []
  })

  const tableData = reactive<NewUserItem[]>([])

  /**
   * 拉取新用户统计数据
   */
  const loadNewUsers = async () => {
    try {
      const data = await fetchPortalNewUsers()
      stats.newUsersToday = data.newUsersToday || 0
      stats.newUsersThisWeek = data.newUsersThisWeek || 0
      stats.weeklyTrend = data.weeklyTrend || []
      tableData.splice(0, tableData.length, ...(data.latestUsers || []))
    } catch (error) {
      console.error('加载新用户统计失败', error)
    }
  }

  onMounted(loadNewUsers)
</script>
