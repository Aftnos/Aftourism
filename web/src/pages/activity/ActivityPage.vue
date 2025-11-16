<template>
  <div class="page-card">
    <SmartTable :fetcher="fetchData">
      <template #query="{ form }">
        <ElFormItem label="活动">
          <ElInput v-model="form.name" placeholder="活动名称" />
        </ElFormItem>
      </template>
      <ElTableColumn prop="name" label="活动" />
      <ElTableColumn prop="status" label="状态" />
      <ElTableColumn label="操作">
        <template #default="{ row }">
          <ElButton size="small" type="success" v-can="'ACTIVITY:AUDIT'" @click="audit(row, 'APPROVED')">通过</ElButton>
          <ElButton size="small" type="danger" v-can="'ACTIVITY:AUDIT'" @click="audit(row, 'REJECTED')">拒绝</ElButton>
        </template>
      </ElTableColumn>
    </SmartTable>
  </div>
</template>

<script setup lang="ts">
import SmartTable from '@/components/table/SmartTable.vue';
import { fetchActivities, reviewActivity } from '@/api/business';
import { ElMessage } from 'element-plus';
import { createTraceId } from '@/utils/trace';

async function fetchData(params: Record<string, any>) {
  const res = await fetchActivities(params);
  return { records: res.records || [], total: res.total || 0 };
}

async function audit(row: any, status: string) {
  const traceId = createTraceId('activity');
  console.info('操作摘要', { traceId, action: 'reviewActivity', params: { id: row.id, status } });
  await reviewActivity(row.id, status);
  ElMessage.success('已提交审核结果');
}
</script>
