<template>
  <div class="op-bar">
    <slot />
  </div>
</template>

<script setup lang="ts">
// 操作条只是包裹器，实际按钮由使用方结合 v-can 控制
</script>

<style scoped>
.op-bar {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
}
</style>
