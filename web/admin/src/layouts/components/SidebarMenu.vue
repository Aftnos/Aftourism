<template>
  <el-menu :default-active="active" background-color="var(--layout-bg)" text-color="#fff" active-text-color="#ffd04b" class="menu" router :collapse="!app.isMobile && app.sidebarCollapsed" :collapse-transition="true">
    <template v-for="item in menuRoutes" :key="item.fullPath">
      <el-sub-menu v-if="item.children?.length" :index="item.fullPath">
        <template #title>
          <el-icon><component :is="resolveIcon(item.meta?.icon as string, item.meta?.title as string)" /></el-icon>
          <span>{{ item.meta?.title }}</span>
        </template>
        <el-menu-item v-for="child in item.children" :key="child.fullPath" :index="child.fullPath">
          <el-icon><component :is="resolveIcon(child.meta?.icon as string, child.meta?.title as string)" /></el-icon>
          <span>{{ child.meta?.title }}</span>
        </el-menu-item>
      </el-sub-menu>
      <el-menu-item v-else :index="item.fullPath">
        <el-icon><component :is="resolveIcon(item.meta?.icon as string, item.meta?.title as string)" /></el-icon>
        <span>{{ item.meta?.title }}</span>
      </el-menu-item>
    </template>
  </el-menu>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import type { RouteRecordRaw } from 'vue-router';
import { useAuthStore } from '@/store/auth';
import { protectedRoutes } from '@/router/routes';
import { useAppStore } from '@/store/app';
import { Odometer, Cpu, ChatDotSquare, Setting, UserFilled, User, Reading, Bell, Location, OfficeBuilding, List, UploadFilled, Delete, DataLine, Menu as MenuIcon, Key } from '@element-plus/icons-vue';

type MenuRecord = RouteRecordRaw & {
  fullPath: string;
  children?: MenuRecord[];
};

const route = useRoute();
const auth = useAuthStore();
const app = useAppStore();

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

function resolveIcon(icon?: string, title?: string) {
  const byName = iconByName(icon);
  if (byName !== MenuIcon || (icon && icon !== 'Menu')) return byName;
  return iconByTitle(title);
}

function joinPath(parent: string, path = '') {
  if (path.startsWith('/')) return path;
  const normalizedParent = parent.endsWith('/') ? parent.slice(0, -1) : parent;
  const combined = `${normalizedParent}/${path}`;
  return combined.replace(new RegExp('/+', 'g'), '/');
}

function buildMenu(routes: RouteRecordRaw[], basePath: string): MenuRecord[] {
  const result: MenuRecord[] = [];
  routes.forEach((item) => {
    const fullPath = joinPath(basePath, item.path || '');
    const children = item.children ? buildMenu(item.children, fullPath) : [];
    const perm = item.meta?.permission as string | string[] | undefined;
    const allowSelf = !perm || auth.allow(perm);
    if (!allowSelf && children.length === 0) {
      return;
    }
    result.push({ ...item, fullPath, children });
  });
  return result.filter((item) => item.meta?.title || item.children?.length);
}

const menuRoutes = computed(() => {
  const baseChildren = protectedRoutes[0]?.children || [];
  return buildMenu(baseChildren, '');
});

const active = computed(() => route.path);
</script>

<style scoped>
.menu {
  border-right: none;
  height: 100%;
}
</style>
