<template>
  <ElDialog v-model="innerVisible" :title="title" width="520px" @close="handleClose">
    <div class="summary">
      <RiskTag :level="sheet?.riskLevel" />
      <p>{{ sheet?.summary }}</p>
    </div>
    <ElDescriptions title="参数" :column="1" border>
      <ElDescriptionsItem v-for="(value, key) in sheet?.params" :key="key" :label="key">
        {{ formatValue(value) }}
      </ElDescriptionsItem>
    </ElDescriptions>
    <template #footer>
      <div class="footer">
        <ElButton @click="handleClose">取消</ElButton>
        <ElButton type="primary" :loading="loading" @click="handleConfirm">确认执行</ElButton>
      </div>
    </template>
  </ElDialog>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import RiskTag from '@/components/common/RiskTag.vue';
import type { ConfirmSheet } from '@/api/ai';

const props = defineProps<{ modelValue: boolean; sheet: ConfirmSheet | null }>();
const emit = defineEmits<{ (e: 'update:modelValue', value: boolean): void; (e: 'confirm'): void }>();

const innerVisible = ref(props.modelValue);
const loading = ref(false);

watch(
  () => props.modelValue,
  (val) => (innerVisible.value = val)
);

const title = computed(() => {
  if (!props.sheet) return '确认操作';
  const map = {
    L1: '轻写操作确认',
    L2: '重要操作再次确认',
    L3: '高风险操作人工确认'
  } as Record<string, string>;
  return map[props.sheet.riskLevel] || '操作确认';
});

function formatValue(value: unknown) {
  if (typeof value === 'object') {
    return JSON.stringify(value, null, 2);
  }
  return String(value);
}

async function handleConfirm() {
  loading.value = true;
  try {
    await emit('confirm');
  } finally {
    loading.value = false;
  }
}

function handleClose() {
  innerVisible.value = false;
  emit('update:modelValue', false);
}
</script>

<style scoped>
.summary {
  display: flex;
  gap: 8px;
  align-items: center;
  margin-bottom: 12px;
}
.footer {
  text-align: right;
}
</style>
