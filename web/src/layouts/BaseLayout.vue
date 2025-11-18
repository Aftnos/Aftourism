<template>
  <div class="layout" :class="{ collapsed: app.sidebarCollapsed }">
    <aside v-if="!app.isMobile" class="sidebar">
      <SidebarMenu />
    </aside>
    <el-drawer v-else v-model="app.sidebarOpen" direction="ltr" with-header="false" size="220px" :close-on-click-modal="true">
      <SidebarMenu />
    </el-drawer>
    <div class="content">
      <TopHeader />
      <main>
        <div class="page-title" v-if="currentTitle">
          <el-icon v-if="currentIconComp"><component :is="currentIconComp" /></el-icon>
          <span>{{ currentTitle }}</span>
        </div>
        <RouterView />
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, onBeforeUnmount, computed } from 'vue';
import SidebarMenu from './components/SidebarMenu.vue';
import TopHeader from './components/TopHeader.vue';
import { useAppStore } from '@/store/app';
import { useRoute } from 'vue-router';
import { Odometer, Cpu, ChatDotSquare, Setting, UserFilled, User, Reading, Bell, Location, OfficeBuilding, List, UploadFilled, Delete, DataLine, Menu as MenuIcon, Key } from '@element-plus/icons-vue';

const app = useAppStore();
const route = useRoute();

const currentTitle = computed(() => {
  const matched = route.matched;
  if (!matched.length) return '';
  return matched[matched.length - 1]?.meta?.title as string || '';
});

const currentIcon = computed(() => {
  const matched = route.matched;
  if (!matched.length) return '' as unknown as string;
  return matched[matched.length - 1]?.meta?.icon as unknown as string;
});

function iconByName(name?: string) {
  switch (name) {
    case 'Odometer': return Odometer;
    case 'Cpu': return Cpu;
    case 'ChatDotSquare': return ChatDotSquare;
    case 'Setting': return Setting;
    case 'UserFilled': return UserFilled;
    case 'User': return User;
    case 'Reading': return Reading;
    case 'Bell': return Bell;
    case 'Location': return Location;
    case 'OfficeBuilding': return OfficeBuilding;
    case 'List': return List;
    case 'UploadFilled': return UploadFilled;
    case 'Delete': return Delete;
    case 'DataLine': return DataLine;
    case 'Key': return Key;
    default: return MenuIcon;
  }
}

function iconByTitle(title?: string) {
  if (!title) return MenuIcon;
  if (title.includes('总览')) return Odometer;
  if (title.startsWith('AI')) return Cpu;
  if (title.includes('AI 功能')) return ChatDotSquare;
  if (title.includes('AI 管理')) return Setting;
  if (title.includes('管理员')) return UserFilled;
  if (title.includes('角色')) return Key;
  if (title.includes('门户用户') || title.includes('用户')) return User;
  if (title.includes('新闻')) return Reading;
  if (title.includes('通知')) return Bell;
  if (title.includes('景区')) return Location;
  if (title.includes('场馆')) return OfficeBuilding;
  if (title.includes('活动')) return List;
  if (title.includes('文件')) return UploadFilled;
  if (title.includes('回收站')) return Delete;
  if (title.includes('监控')) return DataLine;
  return MenuIcon;
}

const currentIconComp = computed(() => {
  const iconName = currentIcon.value as unknown as string | undefined;
  const title = currentTitle.value as unknown as string | undefined;
  const byName = iconByName(iconName);
  if (byName !== MenuIcon || (iconName && iconName !== 'Menu')) return byName;
  return iconByTitle(title);
});

function updateMobile() {
  app.setMobile(window.innerWidth < 768);
}

onMounted(() => {
  updateMobile();
  window.addEventListener('resize', updateMobile);
});

onBeforeUnmount(() => {
  window.removeEventListener('resize', updateMobile);
});
</script>

<style scoped>
.layout {
  display: flex;
  height: 100vh;
}
.sidebar {
  width: 220px;
  background: var(--layout-bg);
  color: #fff;
  transition: width 0.25s ease;
}
.layout.collapsed .sidebar {
  width: 64px;
}
.content {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
}
main {
  flex: 1;
  overflow-y: auto;
  overflow-x: auto;
  padding: 16px;
  background: #f5f7fb;
}
.page-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
}
main::-webkit-scrollbar {
  height: 8px;
  width: 8px;
}
main::-webkit-scrollbar-track {
  background: #e5e7eb;
}
main::-webkit-scrollbar-thumb {
  background: #c6c9cf;
  border-radius: 4px;
}
@media (max-width: 767.98px) {
  .layout {
    height: 100vh;
  }
}
</style>
