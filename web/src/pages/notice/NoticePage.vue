<template>
  <div class="page-card">
    <SmartTable :fetcher="fetchData">
      <template #query="{ form }">
        <ElFormItem label="标题">
          <ElInput v-model="form.title" placeholder="通知标题" />
        </ElFormItem>
      </template>
      <ElTableColumn prop="title" label="标题" />
      <ElTableColumn prop="type" label="类型" />
      <ElTableColumn prop="createdAt" label="创建时间" />
    </SmartTable>
  </div>
</template>

<script setup lang="ts">
import SmartTable from '@/components/table/SmartTable.vue';
import { fetchNotices } from '@/api/business';

async function fetchData(params: Record<string, any>) {
  const res = await fetchNotices(params);
  return { records: res.records || [], total: res.total || 0 };
}
</script>
