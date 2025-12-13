<template>
  <div class="page-wrapper">
    <div class="content-card">
      <div class="section-title">
        <h3>新闻动态</h3>
        <span>按时间倒序展示</span>
      </div>
      <el-table :data="pagedNews" stripe>
        <el-table-column prop="title" label="标题" min-width="260">
          <template #default="scope">
            <router-link :to="`/news/${scope.row.id}`">{{ scope.row.title }}</router-link>
          </template>
        </el-table-column>
        <el-table-column prop="publishTime" label="发布时间" width="160" />
        <el-table-column label="摘要" min-width="260">
          <template #default="scope">
            <span>{{ scope.row.content }}</span>
          </template>
        </el-table-column>
      </el-table>
      <div class="pager">
        <el-pagination
          background
          layout="prev, pager, next"
          :total="newsList.length"
          :page-size="pageSize"
          v-model:current-page="current"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { newsList } from '@/data/mockData';

// 中文注释：分页展示新闻，默认时间倒序
const current = ref(1);
const pageSize = 5;
const sortedNews = computed(() => [...newsList].sort((a, b) => (a.publishTime < b.publishTime ? 1 : -1)));
const pagedNews = computed(() => {
  const start = (current.value - 1) * pageSize;
  return sortedNews.value.slice(start, start + pageSize);
});
</script>

<style scoped>
.pager {
  margin-top: 16px;
  text-align: right;
}
</style>
