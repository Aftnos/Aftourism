<!-- 工作台页面 -->
<template>
  <div>
    <CardList></CardList>

    <ElRow :gutter="20">
      <ElCol :sm="24" :md="12" :lg="10">
        <ActiveUser />
      </ElCol>
      <ElCol :sm="24" :md="12" :lg="14">
        <SalesOverview />
      </ElCol>
    </ElRow>

    <ElRow :gutter="20">
      <ElCol :sm="24" :md="12" :lg="12">
        <NewUser />
      </ElCol>
      <ElCol :sm="24" :md="12" :lg="6">
        <ContentBoard title="新闻动态" :items="newsList" />
      </ElCol>
      <ElCol :sm="24" :md="12" :lg="6">
        <ContentBoard title="通知公告" :items="noticeList" />
      </ElCol>
    </ElRow>
  </div>
</template>

<script setup lang="ts">
  import { onMounted, ref } from 'vue'
  import CardList from './modules/card-list.vue'
  import ActiveUser from './modules/active-user.vue'
  import SalesOverview from './modules/sales-overview.vue'
  import NewUser from './modules/new-user.vue'
  import ContentBoard from './modules/content-board.vue'
  import { fetchPortalContentDigest, type ContentBrief } from '@/api/dashboard'

  const newsList = ref<ContentBrief[]>([])
  const noticeList = ref<ContentBrief[]>([])

  const loadContentDigest = async () => {
    try {
      const digest = await fetchPortalContentDigest(6)
      newsList.value = digest.newsList || []
      noticeList.value = digest.noticeList || []
    } catch (error) {
      console.error('加载内容摘要失败', error)
    }
  }

  onMounted(loadContentDigest)

  defineOptions({ name: 'Console' })
</script>
