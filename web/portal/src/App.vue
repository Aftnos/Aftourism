<template>
  <el-container class="layout">
    <el-header height="72px" class="header">
      <HeaderNav />
    </el-header>
    <el-main>
      <router-view />
    </el-main>
    <el-footer height="80px" class="footer">
      <FooterBar />
    </el-footer>
  </el-container>
</template>

<script setup lang="ts">
import HeaderNav from '@/components/HeaderNav.vue';
import FooterBar from '@/components/FooterBar.vue';
</script>

<style scoped>
.layout {
  min-height: 100vh;
  background: #f5f7fa;
}

.header {
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  padding: 0 24px;
}

.footer {
  background: #1f2d3d;
  color: #fff;
  text-align: center;
}
</style>
