<template>
  <el-menu :default-active="active" background-color="var(--layout-bg)" text-color="#fff" active-text-color="#ffd04b" class="menu" router>
    <template v-for="item in menuRoutes" :key="item.fullPath">
      <el-sub-menu v-if="item.children?.length" :index="item.fullPath">
        <template #title>
          <el-icon v-if="item.meta?.icon"><component :is="item.meta?.icon" /></el-icon>
          <span>{{ item.meta?.title }}</span>
        </template>
        <el-menu-item v-for="child in item.children" :key="child.fullPath" :index="child.fullPath">
          {{ child.meta?.title }}
        </el-menu-item>
      </el-sub-menu>
      <el-menu-item v-else :index="item.fullPath">
        <el-icon v-if="item.meta?.icon"><component :is="item.meta?.icon" /></el-icon>
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

type MenuRecord = RouteRecordRaw & {
  fullPath: string;
  children?: MenuRecord[];
};

const route = useRoute();
const auth = useAuthStore();

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
