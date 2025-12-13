<template>
  <div class="page-wrapper">
    <div class="content-card">
      <div class="section-title">
        <h3>新闻动态</h3>
        <span>按时间倒序展示</span>
      </div>
      <el-table :data="newsList" stripe>
        <el-table-column prop="title" label="标题" min-width="260">
          <template #default="scope">
            <router-link :to="`/news/${scope.row.id}`">{{ scope.row.title }}</router-link>
          </template>
        </el-table-column>
        <el-table-column prop="publishTime" label="发布时间" width="160" />
        <el-table-column label="摘要" min-width="260">
          <template #default="scope">
            <span>{{ scope.row.summary || scope.row.content }}</span>
          </template>
        </el-table-column>
      </el-table>
      <div class="pager">
        <el-pagination
          background
          layout="prev, pager, next"
          :total="total"
          :page-size="pageSize"
          v-model:current-page="current"
          @current-change="loadNews"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { fetchNewsPage, type NewsItem } from '@/services/portal';

// 中文注释：分页展示新闻，默认时间倒序，数据来源于后台接口
const current = ref(1);
const pageSize = 5;
const total = ref(0);
const newsList = ref<NewsItem[]>([]);

const loadNews = async () => {
  const resp = await fetchNewsPage({ current: current.value, size: pageSize });
  newsList.value = resp.list;
  total.value = resp.total;
};

onMounted(loadNews);
</script>

<style scoped>
.pager {
  margin-top: 16px;
  text-align: right;
}
</style>
