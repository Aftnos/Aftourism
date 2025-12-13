<template>
  <div class="page-wrapper">
    <div class="content-card" v-if="news">
      <h2>{{ news.title }}</h2>
      <p class="meta">发布时间：{{ news.publishTime }}</p>
      <el-divider />
      <p>{{ news.content }}</p>
    </div>
    <el-empty v-else description="新闻不存在" />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import { newsList } from '@/data/mockData';

// 中文注释：根据路由参数展示新闻详情
const route = useRoute();
const news = computed(() => newsList.find((item) => item.id === Number(route.params.id)));
</script>

<style scoped>
.meta {
  color: #909399;
}
</style>
