<template>
  <ElTag :type="tagType" effect="dark">{{ label }}</ElTag>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps<{ level?: string }>();

const map: Record<string, { label: string; type: 'info' | 'warning' | 'danger' | 'success' }> = {
  L0: { label: '只读', type: 'info' },
  L1: { label: '轻写', type: 'success' },
  L2: { label: '重要', type: 'warning' },
  L3: { label: '高危', type: 'danger' }
};

const label = computed(() => map[props.level || 'L0']?.label || '未知');
const tagType = computed(() => map[props.level || 'L0']?.type || 'info');
</script>
