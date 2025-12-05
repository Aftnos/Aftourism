<template>
  <ElUpload
    drag
    :action="finalAction"
    :data="uploadData"
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
import { ElMessage } from 'element-plus';
import { getToken } from '@/auth/token';

// 统一封装上传组件，确保请求头带 token
const emit = defineEmits<{ (e: 'uploaded', url: string): void }>();
const props = defineProps<{ action?: string; bizTag?: string }>();

const headers = computed(() => ({ Authorization: `Bearer ${getToken()}` }));
const finalAction = computed(() => props.action || '/file/upload');
const uploadData = computed(() => (props.bizTag ? { bizTag: props.bizTag } : undefined));

function handleSuccess(response: any) {
  if (response?.code !== 1) {
    ElMessage.error(response?.msg || '上传失败');
    return;
  }
  const url = response.data?.url;
  if (!url) {
    ElMessage.error('返回结果缺少 url 字段');
    return;
  }
  emit('uploaded', url);
}

function beforeUpload() {
  return true;
}
</script>
