<template>
  <div class="page-wrapper">
    <div class="content-card">
      <div class="section-title">
        <h3>通知公告</h3>
        <span>按时间倒序展示</span>
      </div>
      <el-table :data="pagedNotices" stripe>
        <el-table-column prop="title" label="标题" min-width="260">
          <template #default="scope">
            <router-link :to="`/notices/${scope.row.id}`">{{ scope.row.title }}</router-link>
          </template>
        </el-table-column>
        <el-table-column prop="publishTime" label="发布时间" width="160" />
        <el-table-column label="内容" min-width="260">
          <template #default="scope">
            <span>{{ scope.row.content }}</span>
          </template>
        </el-table-column>
      </el-table>
      <div class="pager">
        <el-pagination
          background
          layout="prev, pager, next"
          :total="noticeList.length"
          :page-size="pageSize"
          v-model:current-page="current"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { noticeList } from '@/data/mockData';

// 中文注释：分页展示通知公告
const current = ref(1);
const pageSize = 5;
const sortedNotices = computed(() => [...noticeList].sort((a, b) => (a.publishTime < b.publishTime ? 1 : -1)));
const pagedNotices = computed(() => {
  const start = (current.value - 1) * pageSize;
  return sortedNotices.value.slice(start, start + pageSize);
});
</script>

<style scoped>
.pager {
  margin-top: 16px;
  text-align: right;
}
</style>
