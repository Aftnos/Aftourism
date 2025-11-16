<template>
  <el-menu :default-active="active" background-color="var(--layout-bg)" text-color="#fff" active-text-color="#ffd04b" class="menu" router>
    <template v-for="item in menuRoutes" :key="item.path">
      <el-sub-menu v-if="item.children?.length" :index="fullPath(item)">
        <template #title>
          <el-icon><component :is="item.meta?.icon" /></el-icon>
          <span>{{ item.meta?.title }}</span>
        </template>
        <el-menu-item v-for="child in item.children" :key="child.path" :index="fullPath(child)">
          {{ child.meta?.title }}
        </el-menu-item>
      </el-sub-menu>
      <el-menu-item v-else :index="fullPath(item)">
        <el-icon><component :is="item.meta?.icon" /></el-icon>
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

const route = useRoute();
const auth = useAuthStore();

function filterMenu(routes: RouteRecordRaw[]): RouteRecordRaw[] {
  return routes
    .filter((item) => {
      const perm = item.meta?.permission as string | undefined;
      if (perm && !auth.allow(perm)) {
        return false;
      }
      return true;
    })
    .map((item) => ({
      ...item,
      children: item.children ? filterMenu(item.children) : []
    }));
}

const menuRoutes = computed(() => {
  const base = protectedRoutes[0]?.children || [];
  return filterMenu(base);
});

const active = computed(() => route.path);

function fullPath(item: RouteRecordRaw) {
  if (item.path.startsWith('/')) return item.path;
  return `/${item.path}`.replace('//', '/');
}
</script>

<style scoped>
.menu {
  border-right: none;
  height: 100%;
}
</style>
