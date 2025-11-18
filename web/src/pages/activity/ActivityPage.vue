<template>
  <div class="page-card">
    <SmartTable ref="tableRef" :fetcher="fetchData">
      <template #query="{ form }">
        <ElFormItem label="名称">
          <ElInput v-model="form.name" placeholder="活动名称" clearable />
        </ElFormItem>
        <ElFormItem label="审核状态">
          <ElSelect v-model.number="form.applyStatus" placeholder="全部" clearable>
            <ElOption label="待审核" :value="0" />
            <ElOption label="已通过" :value="1" />
            <ElOption label="已拒绝" :value="2" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="上线状态">
          <ElSelect v-model.number="form.onlineStatus" placeholder="全部" clearable>
            <ElOption label="未上线" :value="0" />
            <ElOption label="已上线" :value="1" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="开始时间">
          <ElDatePicker v-model="form.timeRange" type="datetimerange" range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间" value-format="YYYY-MM-DD HH:mm:ss" />
        </ElFormItem>
      </template>
      <ElTableColumn prop="name" label="活动" />
      <ElTableColumn prop="venueName" label="场馆" width="160" />
      <ElTableColumn prop="category" label="类别" width="120" />
      <ElTableColumn prop="addressCache" label="地址" width="200" />
      <ElTableColumn prop="startTime" label="开始时间" width="180" />
      <ElTableColumn prop="endTime" label="结束时间" width="180" />
      <ElTableColumn label="上线状态" width="120">
        <template #default="{ row }">
          <ElTag :type="row.onlineStatus === 1 ? 'success' : 'info'">{{ row.onlineStatus === 1 ? '已上线' : '未上线' }}</ElTag>
        </template>
      </ElTableColumn>
      <ElTableColumn label="审核状态" width="120">
        <template #default="{ row }">
          <ElTag :type="auditStatusTagType(auditStatusOf(row))">{{ auditStatusText(auditStatusOf(row)) }}</ElTag>
        </template>
      </ElTableColumn>
      <ElTableColumn label="操作" width="320">
        <template #default="{ row }">
          <ElButton text size="small" type="success" v-can="'ACTIVITY_REVIEW:APPROVE'" @click="approve(row)">审核通过</ElButton>
          <ElButton text size="small" type="warning" v-can="'ACTIVITY_REVIEW:REJECT'" @click="reject(row)">驳回</ElButton>
          <ElButton text size="small" type="primary" v-can="'ACTIVITY_REVIEW:ONLINE'" @click="online(row)">上线</ElButton>
          <ElButton text size="small" type="info" v-can="'ACTIVITY_REVIEW:OFFLINE'" @click="offline(row)">下线</ElButton>
        </template>
      </ElTableColumn>
    </SmartTable>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import SmartTable from '@/components/table/SmartTable.vue';
import {
  fetchAuditActivities,
  approveActivity,
  rejectActivity,
  onlineActivity,
  offlineActivity,
  type ActivitySummary
} from '@/api/business';
import { createTraceId } from '@/utils/trace';

const tableRef = ref<InstanceType<typeof SmartTable>>();

function auditStatusOf(row: ActivitySummary) {
  return (row.applyStatus ?? row.onlineStatus) as number | undefined;
}

function auditStatusText(status?: number) {
  if (status === 0) return '待审核';
  if (status === 1) return '已通过';
  if (status === 2) return '已拒绝';
  return '未知';
}

function auditStatusTagType(status?: number) {
  if (status === 1) return 'success';
  if (status === 2) return 'danger';
  if (status === 0) return 'warning';
  return 'info';
}

async function fetchData(params: Record<string, any>) {
  const q: Record<string, any> = { ...params };
  if (Array.isArray(q.timeRange) && q.timeRange.length === 2) {
    q.startTimeFrom = q.timeRange[0];
    q.startTimeTo = q.timeRange[1];
  }
  delete q.timeRange;
  if (typeof q.applyStatus === 'string' && q.applyStatus !== '') q.applyStatus = Number(q.applyStatus);
  if (typeof q.onlineStatus === 'string' && q.onlineStatus !== '') q.onlineStatus = Number(q.onlineStatus);
  return fetchAuditActivities(q);
}

async function approve(row: ActivitySummary) {
  await approveActivity(row.id);
  logAndRefresh('approve', row.id);
}

async function reject(row: ActivitySummary) {
  const { value } = await ElMessageBox.prompt('请输入驳回原因', '驳回活动', { inputPattern: /.+/, inputErrorMessage: '必填' });
  await rejectActivity(row.id, value);
  logAndRefresh('reject', row.id, { reason: value });
}

async function online(row: ActivitySummary) {
  await onlineActivity(row.id);
  logAndRefresh('online', row.id);
}

async function offline(row: ActivitySummary) {
  await offlineActivity(row.id);
  logAndRefresh('offline', row.id);
}

function logAndRefresh(action: string, id: number, extra?: Record<string, any>) {
  const traceId = createTraceId('activity');
  console.info('操作摘要', { traceId, action, params: { id, ...extra } });
  ElMessage.success('操作成功');
  tableRef.value?.fetch();
}
</script>
