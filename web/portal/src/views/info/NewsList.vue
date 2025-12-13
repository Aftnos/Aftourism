<template>
  <div class="page-wrapper">
    <div class="content-card">
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="新闻动态" name="news">
          <div class="list-container">
            <el-table :data="newsList" stripe :show-header="false">
              <el-table-column min-width="400">
                <template #default="{ row }">
                  <div class="list-item" @click="goNewsDetail(row.id)">
                    <div class="item-main">
                      <span class="item-title">{{ row.title }}</span>
                      <p class="item-desc">{{ row.summary || row.content }}</p>
                    </div>
                    <span class="item-date">{{ row.publishTime }}</span>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
        <el-tab-pane label="通知公告" name="notice">
          <div class="list-container">
            <el-table :data="noticeList" stripe :show-header="false">
              <el-table-column min-width="400">
                <template #default="{ row }">
                  <div class="list-item" @click="goNoticeDetail(row.id)">
                    <div class="item-main">
                      <span class="item-title">{{ row.title }}</span>
                      <p class="item-desc">{{ row.content }}</p>
                    </div>
                    <span class="item-date">{{ row.publishTime }}</span>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
      </el-tabs>

      <div class="pager">
        <el-pagination
          background
          layout="prev, pager, next"
          :total="total"
          :page-size="pageSize"
          v-model:current-page="current"
          @current-change="loadData"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { fetchNewsPage, fetchNoticePage, type NewsItem, type NoticeItem } from '@/services/portal';

// 中文注释：合并新闻与公告，支持 Tab 切换，统一展示逻辑
const router = useRouter();
const activeTab = ref('news');
const current = ref(1);
const pageSize = 10;
const total = ref(0);
const newsList = ref<NewsItem[]>([]);
const noticeList = ref<NoticeItem[]>([]);

const loadData = async () => {
  if (activeTab.value === 'news') {
    const resp = await fetchNewsPage({ current: current.value, size: pageSize });
    newsList.value = resp.list;
    total.value = resp.total;
  } else {
    const resp = await fetchNoticePage({ current: current.value, size: pageSize });
    noticeList.value = resp.list;
    total.value = resp.total;
  }
};

const handleTabChange = () => {
  current.value = 1;
  loadData();
};

const goNewsDetail = (id: number) => router.push(`/news/${id}`);
const goNoticeDetail = (id: number) => router.push(`/notices/${id}`);

onMounted(loadData);
</script>

<style scoped>
.page-wrapper {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
}

.content-card {
  min-height: 600px;
  width: 100%;
  padding: 32px;
  box-sizing: border-box;
}

:deep(.el-tabs__nav) {
  width: 100%;
}

:deep(.el-tabs__item) {
  font-size: 18px;
  font-weight: 600;
  height: 50px;
  width: 50%;
  text-align: center;
}

:deep(.el-tabs__active-bar) {
  height: 3px;
  background-color: #2c7be5;
}

:deep(.el-tabs__nav-wrap::after) {
  height: 1px;
  background-color: #e4e7ed;
}

.list-container {
  min-height: 400px;
}

.list-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  cursor: pointer;
}

.item-main {
  flex: 1;
  margin-right: 24px;
}

.item-title {
  font-size: 16px;
  font-weight: 500;
  color: #1f2d3d;
  margin-bottom: 8px;
  display: block;
}

.list-item:hover .item-title {
  color: #2c7be5;
}

.item-desc {
  font-size: 13px;
  color: #909399;
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.item-date {
  font-size: 14px;
  color: #909399;
  font-family: monospace;
}

.pager {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}
</style>
