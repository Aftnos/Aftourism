<template>
  <div class="page-wrapper">
    <div class="content-card">
      <div class="section-title">
        <h3>消息通知</h3>
        <span>查看点赞、回复、举报与审核提醒</span>
      </div>
      <el-alert v-if="!userStore.isLogin" title="登录后查看通知" type="info" show-icon />
      <div v-else>
        <div class="action-bar">
          <el-button type="primary" plain @click="loadList">刷新</el-button>
          <el-button type="success" plain @click="markAllRead">全部已读</el-button>
        </div>
        <el-empty v-if="items.length === 0 && !loading" description="暂无通知" />
        <div v-else class="notification-list" v-loading="loading">
          <el-card v-for="item in items" :key="item.id" class="notification-card" shadow="hover">
            <div class="card-header">
              <el-tag size="small" :type="tagType(item.type)">{{ item.typeText || item.type }}</el-tag>
              <span class="time">{{ formatTime(item.createTime) }}</span>
            </div>
            <h4 class="title">{{ item.title }}</h4>
            <p class="content">{{ item.content }}</p>
            <div class="card-footer">
              <el-button v-if="item.isRead !== 1" link type="primary" @click="markRead(item.id)">标记已读</el-button>
              <span v-else class="read-text">已读</span>
            </div>
          </el-card>
        </div>
        <div v-if="items.length" class="pager">
          <el-pagination
            background
            layout="prev, pager, next"
            v-model:current-page="pager.current"
            :page-size="pager.size"
            :total="pager.total"
            @current-change="loadList"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { fetchNotificationPage, readNotification, readAllNotifications, type PortalNotificationItem } from '@/services/portal';
import { useUserStore } from '@/store/user';

// 中文注释：用户通知列表
const userStore = useUserStore();
const loading = ref(false);
const items = ref<PortalNotificationItem[]>([]);
const pager = reactive({ current: 1, size: 10, total: 0 });

const formatTime = (time?: string) => (time ? new Date(time).toLocaleString() : '-');
const tagType = (type?: string) => {
  if (type === 'LIKE') return 'success';
  if (type === 'VIOLATION') return 'danger';
  if (type === 'AUDIT') return 'warning';
  if (type === 'REPORT') return 'primary';
  return 'info';
};

const loadList = async () => {
  if (!userStore.isLogin) return;
  loading.value = true;
  const page = await fetchNotificationPage({
    current: pager.current,
    size: pager.size
  });
  items.value = page.list || [];
  pager.total = page.total || 0;
  loading.value = false;
};

const markRead = async (id: number) => {
  await readNotification(id);
  const item = items.value.find((i) => i.id === id);
  if (item) item.isRead = 1;
};

const markAllRead = async () => {
  await readAllNotifications();
  ElMessage.success('已全部标记为已读');
  items.value.forEach((item) => (item.isRead = 1));
};

onMounted(loadList);
</script>

<style scoped lang="scss">
.page-wrapper {
  width: 100%;
  padding: 32px 48px;
  box-sizing: border-box;
  display: flex;
  justify-content: center;
}

.content-card {
  width: min(1100px, 100%);
  padding: 24px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(15, 23, 42, 0.06);
}

.action-bar {
  display: flex;
  gap: 12px;
  margin: 16px 0;
}

.notification-list {
  display: grid;
  gap: 16px;
}

.notification-card {
  border-radius: 12px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #94a3b8;
  font-size: 12px;
}

.title {
  margin: 8px 0;
  font-size: 16px;
  color: #1f2937;
}

.content {
  color: #64748b;
  line-height: 1.6;
}

.card-footer {
  margin-top: 8px;
  display: flex;
  justify-content: flex-end;
}

.read-text {
  color: #94a3b8;
  font-size: 12px;
}

.pager {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
