<template>
  <div class="page-card">
    <SmartTable :fetcher="fetchData">
      <template #query="{ form }">
        <ElFormItem label="场馆">
          <ElInput v-model="form.name" placeholder="场馆名称" />
        </ElFormItem>
      </template>
      <ElTableColumn prop="name" label="场馆" />
      <ElTableColumn prop="capacity" label="容量" />
      <ElTableColumn prop="status" label="状态" />
    </SmartTable>
  </div>
</template>

<script setup lang="ts">
import SmartTable from '@/components/table/SmartTable.vue';
import { fetchVenues } from '@/api/business';

async function fetchData(params: Record<string, any>) {
  const res = await fetchVenues(params);
  return { records: res.records || [], total: res.total || 0 };
}
</script>
