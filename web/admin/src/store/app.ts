import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useAppStore = defineStore('app', () => {
  const safeClosed = ref(false);
  const safeReason = ref('');
  const logs = ref<string[]>([]);
  const sidebarCollapsed = ref(false);
  const sidebarOpen = ref(false);
  const isMobile = ref(false);

  function markSafeClose(reason: string) {
    safeClosed.value = true;
    safeReason.value = reason;
    logs.value.push(`[${new Date().toISOString()}] 会话因安全策略关闭：${reason}`);
  }

  function resetSafeState() {
    safeClosed.value = false;
    safeReason.value = '';
  }

  function toggleSidebar() {
    if (isMobile.value) {
      sidebarOpen.value = !sidebarOpen.value;
    } else {
      sidebarCollapsed.value = !sidebarCollapsed.value;
    }
  }

  function openSidebar() {
    sidebarOpen.value = true;
  }

  function closeSidebar() {
    sidebarOpen.value = false;
  }

  function setMobile(value: boolean) {
    isMobile.value = value;
    if (value) {
      sidebarCollapsed.value = false;
    }
  }

  return { safeClosed, safeReason, logs, sidebarCollapsed, sidebarOpen, isMobile, markSafeClose, resetSafeState, toggleSidebar, openSidebar, closeSidebar, setMobile };
});
