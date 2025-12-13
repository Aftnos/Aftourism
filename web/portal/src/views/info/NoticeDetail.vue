<template>
  <div class="page-wrapper">
    <div class="content-card" v-if="notice">
      <h2>{{ notice.title }}</h2>
      <p class="meta">发布时间：{{ notice.publishTime }}</p>
      <el-divider />
      <p>{{ notice.content }}</p>
    </div>
    <el-empty v-else description="通知不存在" />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import { noticeList } from '@/data/mockData';

// 中文注释：根据路由参数展示通知详情
const route = useRoute();
const notice = computed(() => noticeList.find((item) => item.id === Number(route.params.id)));
</script>

<style scoped>
.meta {
  color: #909399;
}
</style>
