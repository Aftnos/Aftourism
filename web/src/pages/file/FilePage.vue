<template>
  <div class="page-card">
    <UploadBox action="/admin/file/upload" @uploaded="handleUploaded" />
    <ElTable :data="files" class="mt">
      <ElTableColumn prop="name" label="文件名" />
      <ElTableColumn prop="url" label="地址">
        <template #default="{ row }">
          <a :href="row.url" target="_blank">{{ row.url }}</a>
        </template>
      </ElTableColumn>
    </ElTable>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import UploadBox from '@/components/upload/UploadBox.vue';

const files = ref<any[]>([]);

function handleUploaded(url: string) {
  files.value.unshift({ name: `文件${files.value.length + 1}`, url });
}
</script>

<style scoped>
.mt {
  margin-top: 16px;
}
</style>
