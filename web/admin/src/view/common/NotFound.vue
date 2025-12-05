<template>
  <div class="status">
    <h2>404 页面不存在</h2>
    <ElButton type="primary" @click="$router.push('/dashboard')">返回首页</ElButton>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.status {
  height: 80vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
</style>
