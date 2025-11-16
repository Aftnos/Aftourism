import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useAppStore = defineStore('app', () => {
  const safeClosed = ref(false);
  const safeReason = ref('');
  const logs = ref<string[]>([]);

  function markSafeClose(reason: string) {
    safeClosed.value = true;
    safeReason.value = reason;
    logs.value.push(`[${new Date().toISOString()}] 会话因安全策略关闭：${reason}`);
  }

  function resetSafeState() {
    safeClosed.value = false;
    safeReason.value = '';
  }

  return { safeClosed, safeReason, logs, markSafeClose, resetSafeState };
});
