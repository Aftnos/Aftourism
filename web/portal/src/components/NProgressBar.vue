<template>
  <div class="nprogress" :class="{ active: active }">
    <div class="bar" />
  </div>
</template>

<script setup lang="ts">
import { storeToRefs } from 'pinia';
import { useUiStore } from '@/store/ui';

const ui = useUiStore();
const { isNavigating: active } = storeToRefs(ui);
</script>

<style scoped>
.nprogress {
  position: fixed;
  z-index: 2000;
  top: 0;
  left: 0;
  width: 100%;
  height: 2px;
  pointer-events: none;
}
.bar {
  position: absolute;
  height: 2px;
  width: 0%;
  left: 0;
  top: 0;
  background: var(--primary-color);
  box-shadow: 0 0 10px var(--primary-color);
  transition: width 0.3s ease;
}
.active .bar {
  width: 80%;
  animation: finish 0.6s ease 0.3s forwards;
}
@keyframes finish {
  to {
    width: 100%;
  }
}
</style>

