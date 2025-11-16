<template>
  <div class="layout">
    <aside class="sidebar">
      <SidebarMenu />
    </aside>
    <div class="content">
      <TopHeader />
      <main>
        <RouterView />
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import SidebarMenu from './components/SidebarMenu.vue';
import TopHeader from './components/TopHeader.vue';
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
}
.content {
  flex: 1;
  display: flex;
  flex-direction: column;
}
main {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  background: #f5f7fb;
}
</style>
