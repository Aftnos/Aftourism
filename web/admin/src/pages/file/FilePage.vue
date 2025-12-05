<template>
  <div class="page-card">
    <ElForm :inline="true" label-width="80px" class="upload-form">
      <ElFormItem label="业务标识">
        <ElInput v-model="bizTag" placeholder="可用于区分上传来源" />
      </ElFormItem>
    </ElForm>
    <UploadBox :biz-tag="bizTag" @uploaded="handleUploaded" />
    <ElTable :data="files" class="mt" border>
      <ElTableColumn prop="name" label="文件名" />
      <ElTableColumn prop="url" label="地址">
        <template #default="{ row }">
          <a :href="row.url" target="_blank">{{ row.url }}</a>
        </template>
      </ElTableColumn>
      <ElTableColumn prop="bizTag" label="BizTag" width="140" />
      <ElTableColumn prop="uploadedAt" label="上传时间" width="180" />
    </ElTable>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import UploadBox from '@/components/upload/UploadBox.vue';

const files = ref<{ name: string; url: string; bizTag: string; uploadedAt: string }[]>([]);
const bizTag = ref('ADMIN');

function handleUploaded(url: string) {
  const now = new Date();
  files.value.unshift({
    name: `文件${files.value.length + 1}`,
    url,
    bizTag: bizTag.value,
    uploadedAt: now.toLocaleString()
  });
}
</script>

<style scoped>
.mt {
  margin-top: 16px;
}
.upload-form {
  margin-bottom: 8px;
}
</style>
