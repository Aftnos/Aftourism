<template>
  <header class="top-header">
    <div class="left">
      <ElButton type="text" class="icon-btn" @click="app.toggleSidebar">
        <el-icon><Menu /></el-icon>
      </ElButton>
      <ElTag type="success">Aftourism 后台</ElTag>
    </div>
    <div class="right">
      <ElTag v-if="app.safeClosed" type="danger">会话已锁定：{{ app.safeReason }}</ElTag>
      <ElTag v-if="auth.profile?.principalType" type="info" class="role-tag">
        {{ auth.profile?.principalType }}
      </ElTag>
      <span class="user-info">{{ auth.profile?.nickname || auth.profile?.username }}</span>
      <ElButton type="text" @click="handleLogout">退出</ElButton>
    </div>
  </header>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/store/auth';
import { useAppStore } from '@/store/app';
import { Menu } from '@element-plus/icons-vue';

const router = useRouter();
const auth = useAuthStore();
const app = useAppStore();

function handleLogout() {
  auth.logout();
  router.push('/login');
}
</script>

<style scoped>
.top-header {
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
  background-color: #fff;
  border-bottom: 1px solid #e5e7eb;
}

.icon-btn {
  margin-right: 8px;
}

.user-info {
  margin: 0 12px;
}

.role-tag {
  margin-left: 12px;
}
</style>
