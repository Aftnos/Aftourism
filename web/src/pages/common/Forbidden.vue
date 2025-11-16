<template>
  <div class="status">
    <h2>403 无权限</h2>
    <p>请联系超级管理员分配权限。</p>
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
