<template>
  <SmartTable :fetcher="fetchData">
    <template #query="{ form }">
      <ElFormItem label="操作者">
        <ElInput v-model="form.operator" placeholder="操作人" />
      </ElFormItem>
      <ElFormItem label="模块">
        <ElInput v-model="form.module" placeholder="模块" />
      </ElFormItem>
      <ElFormItem label="状态">
        <ElSelect v-model="form.success" placeholder="全部">
          <ElOption label="成功" :value="true" />
          <ElOption label="失败" :value="false" />
        </ElSelect>
      </ElFormItem>
    </template>
    <ElTableColumn prop="traceId" label="TraceID" />
    <ElTableColumn prop="operator" label="操作者" />
    <ElTableColumn prop="module" label="模块" />
    <ElTableColumn prop="success" label="结果" />
    <ElTableColumn prop="createdAt" label="时间" />
  </SmartTable>
</template>

<script setup lang="ts">
import SmartTable from '@/components/table/SmartTable.vue';
import { fetchOperationLogs } from '@/api/log';

async function fetchData(params: Record<string, any>) {
  const res = await fetchOperationLogs(params);
  return { list: res.list || [], total: res.total || 0 };
}
</script>
