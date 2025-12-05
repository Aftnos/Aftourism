<template>
  <div class="page-card">
    <div class="toolbar">
      <ElButton type="primary" v-can="'ACTIVITY_MANAGE:CREATE'" @click="openCreate">新增活动</ElButton>
    </div>
    <SmartTable ref="tableRef" :fetcher="fetchData">
      <template #query="{ form }">
        <ElFormItem label="名称">
          <ElInput v-model="form.name" placeholder="活动名称" clearable />
        </ElFormItem>
        <ElFormItem label="类别">
          <ElInput v-model="form.category" placeholder="活动类别" clearable />
        </ElFormItem>
        <ElFormItem label="上线状态">
          <ElSelect v-model.number="form.onlineStatus" placeholder="全部" clearable>
            <ElOption label="未上线" :value="0" />
            <ElOption label="已上线" :value="1" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="开始时间">
          <ElDatePicker
            v-model="form.timeRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始"
            end-placeholder="结束"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </ElFormItem>
      </template>
      <ElTableColumn prop="name" label="活动" min-width="160" />
      <ElTableColumn prop="venueName" label="场馆" width="160" />
      <ElTableColumn prop="category" label="类别" width="120" />
      <ElTableColumn prop="startTime" label="开始时间" width="180" />
      <ElTableColumn prop="endTime" label="结束时间" width="180" />
      <ElTableColumn label="上线状态" width="120">
        <template #default="{ row }">
          <ElTag :type="row.onlineStatus === 1 ? 'success' : 'info'">{{ row.onlineStatus === 1 ? '已上线' : '未上线' }}</ElTag>
        </template>
      </ElTableColumn>
      <ElTableColumn prop="viewCount" label="浏览量" width="120" />
      <ElTableColumn label="操作" width="320">
        <template #default="{ row }">
          <ElButton text size="small" type="primary" @click="viewDetail(row)">查看详情</ElButton>
          <ElButton text size="small" type="success" v-can="'ACTIVITY_MANAGE:UPDATE'" @click="openEdit(row)">编辑</ElButton>
          <ElButton text size="small" type="danger" v-can="'ACTIVITY_MANAGE:DELETE'" @click="removeActivity(row)">
            删除
          </ElButton>
        </template>
      </ElTableColumn>
    </SmartTable>

    <ElDrawer v-model="detailVisible" title="活动详情" size="60%">
      <template #default>
        <div v-if="detailLoading" class="detail-loading">
          <ElSkeleton animated :rows="6" />
        </div>
        <div v-else-if="detail" class="detail-wrapper">
          <div class="detail-cover" v-if="detail.coverUrl">
            <ElImage :src="detail.coverUrl" fit="cover" :preview-src-list="[detail.coverUrl]" />
          </div>
          <div class="detail-status">
            <ElTag :type="detail.onlineStatus === 1 ? 'success' : 'info'">{{ detail.onlineStatus === 1 ? '已上线' : '未上线' }}</ElTag>
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
            <ElDescriptionsItem label="浏览量">{{ detail.viewCount || 0 }}</ElDescriptionsItem>
            <ElDescriptionsItem label="收藏数">{{ detail.favoriteCount || 0 }}</ElDescriptionsItem>
            <ElDescriptionsItem label="创建时间">{{ detail.createTime || '未知' }}</ElDescriptionsItem>
            <ElDescriptionsItem label="更新时间">{{ detail.updateTime || '未知' }}</ElDescriptionsItem>
          </ElDescriptions>
          <div class="detail-intro">
            <h4>活动简介</h4>
            <p>{{ detail.intro || '暂无简介' }}</p>
          </div>
          <ElDivider content-position="left">留言管理</ElDivider>
          <div class="comment-header">
            <span>共 {{ commentTotal }} 条留言</span>
            <ElButton text size="small" type="primary" @click="refreshComments">刷新</ElButton>
          </div>
          <ElTable :data="comments" v-loading="commentsLoading" border>
            <ElTableColumn label="用户" width="200">
              <template #default="{ row }">
                <div class="comment-user">
                  <span class="nickname">{{ row.userNickname || row.userId || '匿名用户' }}</span>
                  <span class="time">{{ row.createTime }}</span>
                </div>
              </template>
            </ElTableColumn>
            <ElTableColumn prop="content" label="留言内容" min-width="200" />
            <ElTableColumn label="回复" width="120" align="center">
              <template #default="{ row }">{{ row.childCount || 0 }}</template>
            </ElTableColumn>
            <ElTableColumn label="操作" width="200">
              <template #default="{ row }">
                <ElButton text size="small" type="primary" :disabled="!row.childCount" @click="openReplyDialog(row)">
                  查看回复
                </ElButton>
                <ElButton text size="small" type="danger" v-can="'ACTIVITY_MANAGE:COMMENT'" @click="removeComment(row)">
                  删除
                </ElButton>
              </template>
            </ElTableColumn>
          </ElTable>
          <div class="comment-pagination">
            <ElPagination
              layout="prev, pager, next, sizes, total"
              :total="commentTotal"
              :current-page="commentQuery.pageNum"
              :page-size="commentQuery.pageSize"
              :page-sizes="[5, 10, 20]"
              @current-change="handleCommentPageChange"
              @size-change="handleCommentSizeChange"
            />
          </div>
        </div>
        <div v-else class="detail-empty">请选择需要查看的活动</div>
      </template>
    </ElDrawer>

    <ElDialog v-model="formVisible" :title="formTitle" width="600px" destroy-on-close>
      <ElForm ref="formRef" :model="formModel" :rules="formRules" label-width="90px">
        <ElFormItem label="活动名称" prop="name">
          <ElInput v-model="formModel.name" placeholder="请输入活动名称" />
        </ElFormItem>
        <ElFormItem label="封面地址" prop="coverUrl">
          <div class="cover-field">
            <div class="cover-source-row">
              <ElRadioGroup v-model="coverSource">
                <ElRadio label="url">远程链接</ElRadio>
                <ElRadio label="upload">上传图片</ElRadio>
              </ElRadioGroup>
            </div>
            <div v-if="coverSource === 'url'" class="cover-input-row">
              <ElInput v-model="formModel.coverUrl" placeholder="https://example.com/cover.png" />
            </div>
            <div v-else class="cover-upload-row">
              <UploadBox biz-tag="ACTIVITY_COVER" @uploaded="onCoverUploaded" />
              <div v-if="formModel.coverUrl" class="cover-preview">
                <ElImage :src="formModel.coverUrl" fit="cover" />
              </div>
            </div>
          </div>
        </ElFormItem>
        <ElFormItem label="活动类别" prop="category">
          <ElInput v-model="formModel.category" placeholder="如节庆/比赛" />
        </ElFormItem>
        <ElFormItem label="所属场馆" prop="venueId">
          <ElInputNumber v-model="formModel.venueId" :min="1" :step="1" />
        </ElFormItem>
        <ElFormItem label="开始时间" prop="startTime">
          <ElDatePicker v-model="formModel.startTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" />
        </ElFormItem>
        <ElFormItem label="结束时间" prop="endTime">
          <ElDatePicker v-model="formModel.endTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" />
        </ElFormItem>
        <ElFormItem label="上线状态" prop="onlineStatus">
          <ElSelect v-model.number="formModel.onlineStatus">
            <ElOption label="未上线" :value="0" />
            <ElOption label="已上线" :value="1" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="主办单位" prop="organizer">
          <ElInput v-model="formModel.organizer" />
        </ElFormItem>
        <ElFormItem label="联系电话" prop="contactPhone">
          <ElInput v-model="formModel.contactPhone" />
        </ElFormItem>
        <ElFormItem label="地址快照" prop="addressCache">
          <ElInput v-model="formModel.addressCache" placeholder="方便前台展示的场地地址" />
        </ElFormItem>
        <ElFormItem label="活动简介" prop="intro">
          <ElInput type="textarea" v-model="formModel.intro" :rows="4" maxlength="2000" show-word-limit />
        </ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton @click="formVisible = false">取消</ElButton>
        <ElButton type="primary" :loading="formSubmitting" @click="submitForm">保存</ElButton>
      </template>
    </ElDialog>

    <ElDialog v-model="replyDialogVisible" width="600px" destroy-on-close>
      <template #header>
        <span>留言回复</span>
      </template>
      <div v-if="replyParent" class="reply-parent">
        <div class="title">主楼 · {{ replyParent.userNickname || replyParent.userId || '匿名用户' }}</div>
        <div class="content">{{ replyParent.content }}</div>
      </div>
      <ElTable :data="replyComments" v-loading="replyLoading" border>
        <ElTableColumn label="用户" width="200">
          <template #default="{ row }">
            <div class="comment-user">
              <span class="nickname">{{ row.userNickname || row.userId || '匿名用户' }}</span>
              <span class="time">{{ row.createTime }}</span>
            </div>
          </template>
        </ElTableColumn>
        <ElTableColumn prop="content" label="留言内容" min-width="200" />
        <ElTableColumn label="操作" width="120">
          <template #default="{ row }">
            <ElButton text size="small" type="danger" v-can="'ACTIVITY_MANAGE:COMMENT'" @click="removeComment(row, true)">
              删除
            </ElButton>
          </template>
        </ElTableColumn>
      </ElTable>
      <template #footer>
        <ElPagination
          layout="prev, pager, next, total"
          :total="replyTotal"
          :current-page="replyPagination.pageNum"
          :page-size="replyPagination.pageSize"
          @current-change="handleReplyPageChange"
        />
      </template>
    </ElDialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import type { FormInstance, FormRules } from 'element-plus';
import { ElMessage, ElMessageBox } from 'element-plus';
import SmartTable from '@/components/table/SmartTable.vue';
import UploadBox from '@/components/upload/UploadBox.vue';
import {
  fetchManagedActivities,
  fetchActivityManageDetail,
  createActivityManage,
  updateActivityManage,
  deleteActivityManage,
  fetchActivityComments,
  deleteActivityComment,
  type ActivityManageItem,
  type ActivityManageDetail,
  type ActivityManagePayload,
  type ActivityCommentItem
} from '@/api/business';
import { createTraceId } from '@/utils/trace';

const tableRef = ref<InstanceType<typeof SmartTable>>();
const detailVisible = ref(false);
const detailLoading = ref(false);
const detail = ref<ActivityManageDetail>();
const formVisible = ref(false);
const formTitle = ref('新增活动');
const formSubmitting = ref(false);
const formRef = ref<FormInstance>();
const editingId = ref<number | null>(null);
const coverSource = ref<'url' | 'upload'>('url');
const formModel = reactive<ActivityManagePayload>({
  name: '',
  coverUrl: '',
  startTime: '',
  endTime: '',
  category: '',
  venueId: 1,
  organizer: '',
  contactPhone: '',
  intro: '',
  addressCache: '',
  onlineStatus: 0
});

const formRules: FormRules = {
  name: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
  venueId: [{ required: true, message: '请输入场馆ID', trigger: 'blur' }]
};

const comments = ref<ActivityCommentItem[]>([]);
const commentTotal = ref(0);
const commentQuery = reactive({ pageNum: 1, pageSize: 10 });
const commentsLoading = ref(false);
const replyDialogVisible = ref(false);
const replyParent = ref<ActivityCommentItem>();
const replyComments = ref<ActivityCommentItem[]>([]);
const replyTotal = ref(0);
const replyPagination = reactive({ pageNum: 1, pageSize: 10 });
const replyLoading = ref(false);

async function fetchData(params: Record<string, any>) {
  const q: Record<string, any> = { ...params };
  if (Array.isArray(q.timeRange) && q.timeRange.length === 2) {
    q.startTimeFrom = q.timeRange[0];
    q.startTimeTo = q.timeRange[1];
  }
  delete q.timeRange;
  if (typeof q.onlineStatus === 'string' && q.onlineStatus !== '') q.onlineStatus = Number(q.onlineStatus);
  return fetchManagedActivities(q);
}

async function viewDetail(row: ActivityManageItem) {
  detailVisible.value = true;
  detailLoading.value = true;
  try {
    detail.value = await fetchActivityManageDetail(row.id);
    commentQuery.pageNum = 1;
    await loadRootComments();
  } finally {
    detailLoading.value = false;
  }
}

function openCreate() {
  editingId.value = null;
  formTitle.value = '新增活动';
  resetForm();
  formVisible.value = true;
}

async function openEdit(row: ActivityManageItem) {
  const info = await fetchActivityManageDetail(row.id);
  editingId.value = row.id;
  formTitle.value = '编辑活动';
  formModel.name = info.name;
  formModel.coverUrl = info.coverUrl || '';
  coverSource.value = 'url';
  formModel.category = info.category || '';
  formModel.startTime = info.startTime || '';
  formModel.endTime = info.endTime || '';
  formModel.venueId = info.venueId || 1;
  formModel.organizer = info.organizer || '';
  formModel.contactPhone = info.contactPhone || '';
  formModel.intro = info.intro || '';
  formModel.addressCache = info.addressCache || '';
  formModel.onlineStatus = info.onlineStatus ?? 0;
  formVisible.value = true;
}

function resetForm() {
  formModel.name = '';
  formModel.coverUrl = '';
  coverSource.value = 'url';
  formModel.category = '';
  formModel.startTime = '';
  formModel.endTime = '';
  formModel.venueId = 1;
  formModel.organizer = '';
  formModel.contactPhone = '';
  formModel.intro = '';
  formModel.addressCache = '';
  formModel.onlineStatus = 0;
}

function onCoverUploaded(url: string) {
  formModel.coverUrl = url;
}

async function submitForm() {
  if (!formRef.value) return;
  await formRef.value.validate();
  formSubmitting.value = true;
  try {
    const payload: ActivityManagePayload = { ...formModel };
    if (editingId.value) {
      await updateActivityManage(editingId.value, payload);
      logOperation('update', editingId.value);
    } else {
      const id = await createActivityManage(payload);
      logOperation('create', id);
    }
    formVisible.value = false;
    tableRef.value?.fetch();
  } finally {
    formSubmitting.value = false;
  }
}

async function removeActivity(row: ActivityManageItem) {
  await ElMessageBox.confirm(`确定删除活动「${row.name}」吗？删除后可在回收站恢复。`, '删除活动', {
    type: 'warning',
    confirmButtonText: '删除',
    cancelButtonText: '取消'
  });
  await deleteActivityManage(row.id);
  logOperation('delete', row.id);
  tableRef.value?.fetch();
}

function logOperation(action: string, id: number | string) {
  const traceId = createTraceId('activity-manage');
  console.info('活动管理操作', { traceId, action, id });
  ElMessage.success('操作成功');
}

async function loadRootComments() {
  if (!detail.value) return;
  commentsLoading.value = true;
  try {
    const page = await fetchActivityComments(detail.value.id, {
      pageNum: commentQuery.pageNum,
      pageSize: commentQuery.pageSize
    });
    comments.value = page.list || [];
    commentTotal.value = page.total || 0;
  } finally {
    commentsLoading.value = false;
  }
}

function refreshComments() {
  loadRootComments();
}

function handleCommentPageChange(page: number) {
  commentQuery.pageNum = page;
  loadRootComments();
}

function handleCommentSizeChange(size: number) {
  commentQuery.pageSize = size;
  commentQuery.pageNum = 1;
  loadRootComments();
}

function openReplyDialog(row: ActivityCommentItem) {
  replyParent.value = row;
  replyDialogVisible.value = true;
  replyPagination.pageNum = 1;
  loadReplyComments();
}

async function loadReplyComments() {
  if (!detail.value || !replyParent.value) return;
  replyLoading.value = true;
  try {
    const page = await fetchActivityComments(detail.value.id, {
      pageNum: replyPagination.pageNum,
      pageSize: replyPagination.pageSize,
      parentId: replyParent.value.id
    });
    replyComments.value = page.list || [];
    replyTotal.value = page.total || 0;
  } finally {
    replyLoading.value = false;
  }
}

function handleReplyPageChange(page: number) {
  replyPagination.pageNum = page;
  loadReplyComments();
}

async function removeComment(row: ActivityCommentItem, isReply = false) {
  await ElMessageBox.confirm('删除后不可恢复，确认删除该留言及其所有回复吗？', '删除留言', {
    type: 'warning',
    confirmButtonText: '删除',
    cancelButtonText: '取消'
  });
  await deleteActivityComment(row.id);
  ElMessage.success('留言已删除');
  if (isReply) {
    await loadReplyComments();
  }
  await loadRootComments();
  if (!isReply && replyParent.value?.id === row.id) {
    replyDialogVisible.value = false;
  }
}
</script>

<style scoped>
.page-card {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.toolbar {
  display: flex;
  justify-content: flex-end;
}

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

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.comment-user {
  display: flex;
  flex-direction: column;
  line-height: 1.4;
}

.comment-user .nickname {
  font-weight: 600;
}

.comment-user .time {
  font-size: 12px;
  color: var(--el-text-color-secondary);
}

.comment-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 8px;
}

.reply-parent {
  background-color: var(--el-fill-color-lighter);
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 12px;
}

.reply-parent .title {
  font-weight: 600;
  margin-bottom: 4px;
}

.detail-loading,
.detail-empty {
  min-height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
}

.cover-field {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.cover-source-row {
  display: flex;
  align-items: center;
}

.cover-input-row :deep(.el-input) {
  width: 100%;
}

.cover-upload-row {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.cover-preview :deep(img) {
  width: 100%;
  border-radius: 6px;
}
</style>
