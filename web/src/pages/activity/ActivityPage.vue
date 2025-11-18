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
      <ElTableColumn label="操作" width="280">
        <template #default="{ row }">
          <ElButton text size="small" type="primary" @click="viewDetail(row)">查看详情</ElButton>
          <ElButton text size="small" type="success" v-can="'ACTIVITY_REVIEW:APPROVE'" @click="approve(row)">审核通过</ElButton>
          <ElButton text size="small" type="warning" v-can="'ACTIVITY_REVIEW:REJECT'" @click="reject(row)">驳回</ElButton>
        </template>
      </ElTableColumn>
    </SmartTable>
    <ElDrawer v-model="detailVisible" title="活动详情" size="50%">
      <template #default>
        <div v-if="detailLoading" class="detail-loading">
          <ElSkeleton animated :rows="6" />
        </div>
        <div v-else-if="detail" class="detail-wrapper">
          <div class="detail-cover" v-if="detail.coverUrl">
            <ElImage :src="detail.coverUrl" fit="cover" :preview-src-list="[detail.coverUrl]" />
          </div>
          <div class="detail-status">
            <ElTag :type="auditStatusTagType(detail.applyStatus ?? undefined)">审核：{{ auditStatusText(detail.applyStatus ?? undefined) }}</ElTag>
            <ElTag class="status-tag" :type="detail.onlineStatus === 1 ? 'success' : 'info'">{{ detail.onlineStatus === 1 ? '已上线' : '未上线' }}</ElTag>
          </div>
          <ElDescriptions :column="2" border>
            <ElDescriptionsItem label="活动名称">{{ detail.name }}</ElDescriptionsItem>
            <ElDescriptionsItem label="活动类别">{{ detail.category || '未填写' }}</ElDescriptionsItem>
            <ElDescriptionsItem label="所属场馆">{{ detail.venueName || detail.venueId || '未填写' }}</ElDescriptionsItem>
            <ElDescriptionsItem label="场馆地址">{{ detail.addressCache || '未填写' }}</ElDescriptionsItem>
            <ElDescriptionsItem label="开始时间">{{ detail.startTime }}</ElDescriptionsItem>
            <ElDescriptionsItem label="结束时间">{{ detail.endTime }}</ElDescriptionsItem>
            <ElDescriptionsItem label="主办单位">{{ detail.organizer || '未填写' }}</ElDescriptionsItem>
            <ElDescriptionsItem label="联系电话">{{ detail.contactPhone || '未填写' }}</ElDescriptionsItem>
            <ElDescriptionsItem label="申报人ID">{{ detail.applyUserId || '未知' }}</ElDescriptionsItem>
            <ElDescriptionsItem label="提交时间">{{ detail.submitTime || '未知' }}</ElDescriptionsItem>
            <ElDescriptionsItem label="驳回原因" :span="2">
              <span>{{ detail.rejectReason || '无' }}</span>
            </ElDescriptionsItem>
          </ElDescriptions>
          <div class="detail-intro">
            <h4>活动简介</h4>
            <p>{{ detail.intro || '暂无简介' }}</p>
          </div>
          <ElDivider content-position="left">审核备注</ElDivider>
          <ElForm label-position="top" class="remark-form">
            <ElFormItem label="备注内容">
              <ElInput
                v-model="auditRemarkInput"
                type="textarea"
                :rows="3"
                maxlength="255"
                show-word-limit
                placeholder="记录审核关注点"
              />
            </ElFormItem>
            <ElFormItem>
              <ElButton type="primary" :loading="remarkSaving" v-can="'ACTIVITY_REVIEW:REMARK'" @click="saveRemark">
                保存备注
              </ElButton>
            </ElFormItem>
          </ElForm>
        </div>
        <div v-else class="detail-empty">暂无可展示的数据</div>
      </template>
    </ElDrawer>
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
  fetchAuditActivityDetail,
  updateActivityAuditRemark,
  type ActivitySummary,
  type ActivityAuditDetail
} from '@/api/business';
import { createTraceId } from '@/utils/trace';

const tableRef = ref<InstanceType<typeof SmartTable>>();
const detailVisible = ref(false);
const detailLoading = ref(false);
const detail = ref<ActivityAuditDetail>();
const auditRemarkInput = ref('');
const remarkSaving = ref(false);

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

function logAndRefresh(action: string, id: number, extra?: Record<string, any>) {
  const traceId = createTraceId('activity');
  console.info('操作摘要', { traceId, action, params: { id, ...extra } });
  ElMessage.success('操作成功');
  tableRef.value?.fetch();
}

async function viewDetail(row: ActivitySummary) {
  detailVisible.value = true;
  detailLoading.value = true;
  try {
    detail.value = await fetchAuditActivityDetail(row.id);
    auditRemarkInput.value = detail.value?.auditRemark || '';
  } finally {
    detailLoading.value = false;
  }
}

async function saveRemark() {
  if (!detail.value) return;
  remarkSaving.value = true;
  try {
    await updateActivityAuditRemark(detail.value.id, auditRemarkInput.value ?? '');
    detail.value.auditRemark = auditRemarkInput.value;
    ElMessage.success('备注已保存');
  } finally {
    remarkSaving.value = false;
  }
}
</script>

<style scoped>
.detail-wrapper {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.detail-cover :deep(img) {
  width: 100%;
  border-radius: 8px;
}

.detail-status {
  display: flex;
  gap: 8px;
  align-items: center;
}

.detail-status .status-tag {
  margin-left: 4px;
}

.remark-form {
  margin-bottom: 8px;
}

.detail-intro {
  background-color: #f7f8fa;
  border-radius: 8px;
  padding: 12px 16px;
}

.detail-intro h4 {
  margin: 0 0 8px;
  font-size: 16px;
}

.detail-intro p {
  margin: 0;
  line-height: 1.6;
  white-space: pre-wrap;
}

.detail-loading,
.detail-empty {
  min-height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
}
</style>
