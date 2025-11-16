<template>
  <div class="page-card">
    <SmartTable :fetcher="fetchData">
      <template #query="{ form }">
        <ElFormItem label="名称">
          <ElInput v-model="form.name" placeholder="景区名称" />
        </ElFormItem>
      </template>
      <ElTableColumn prop="name" label="景区" />
      <ElTableColumn prop="level" label="等级" />
      <ElTableColumn prop="status" label="状态" />
    </SmartTable>
  </div>
</template>

<script setup lang="ts">
import SmartTable from '@/components/table/SmartTable.vue';
import { fetchScenic } from '@/api/business';

async function fetchData(params: Record<string, any>) {
  const res = await fetchScenic(params);
  return { records: res.records || [], total: res.total || 0 };
}
</script>
