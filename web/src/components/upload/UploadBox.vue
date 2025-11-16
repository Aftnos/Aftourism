<template>
  <ElUpload
    drag
    :action="action"
    :headers="headers"
    :on-success="handleSuccess"
    :before-upload="beforeUpload"
    :show-file-list="false"
  >
    <i class="el-icon-upload"></i>
    <div class="el-upload__text">拖拽文件或点击上传</div>
  </ElUpload>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { getToken } from '@/auth/token';

const emit = defineEmits<{ (e: 'uploaded', url: string): void }>();
const props = defineProps<{ action: string }>();

const headers = computed(() => ({ Authorization: `Bearer ${getToken()}` }));

function handleSuccess(response: any) {
  emit('uploaded', response.url);
}

function beforeUpload() {
  return true;
}
</script>
