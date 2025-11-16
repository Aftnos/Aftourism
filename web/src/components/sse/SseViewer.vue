<template>
  <div class="sse-viewer">
    <div class="toolbar">
      <ElSwitch v-model="autoReconnect" active-text="自动重连" />
      <ElTag size="small">{{ statusText }}</ElTag>
    </div>
    <div class="stream-box">
      <p v-for="(line, idx) in lines" :key="idx">{{ line }}</p>
    </div>
    <slot name="footer"></slot>
  </div>
</template>

<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref, watch } from 'vue';

const props = defineProps<{ endpoint: string; query: Record<string, any>; disabled?: boolean }>();
const emit = defineEmits<{ (e: 'closed'): void }>();

const lines = ref<string[]>([]);
const autoReconnect = ref(true);
const status = ref<'IDLE' | 'CONNECTING' | 'OPEN' | 'CLOSED'>('IDLE');
let source: EventSource | null = null;
let retry = 0;

const statusText = computed(() => {
  switch (status.value) {
    case 'CONNECTING':
      return '连接中';
    case 'OPEN':
      return '流式进行中';
    case 'CLOSED':
      return '已关闭';
    default:
      return '空闲';
  }
});

function connect() {
  if (props.disabled) return;
  status.value = 'CONNECTING';
  const url = new URL(props.endpoint, window.location.origin);
  Object.entries(props.query).forEach(([key, value]) => {
    url.searchParams.append(key, String(value));
  });
  source = new EventSource(url);
  source.onopen = () => {
    status.value = 'OPEN';
    lines.value = [];
  };
  source.onmessage = (evt) => {
    lines.value.push(evt.data);
  };
  source.onerror = () => {
    status.value = 'CLOSED';
    source?.close();
    emit('closed');
    if (autoReconnect.value && retry < 2) {
      retry += 1;
      setTimeout(() => connect(), 1000 * retry);
    }
  };
}

function close() {
  source?.close();
  status.value = 'CLOSED';
}

onMounted(() => connect());

onBeforeUnmount(() => {
  close();
});

watch(
  () => props.query,
  () => {
    retry = 0;
    close();
    connect();
  },
  { deep: true }
);
</script>

<style scoped>
.sse-viewer {
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 12px;
  background-color: #fff;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.stream-box {
  height: 240px;
  overflow-y: auto;
  background-color: #0f172a;
  color: #d1d5db;
  padding: 12px;
  border-radius: 6px;
}
</style>
