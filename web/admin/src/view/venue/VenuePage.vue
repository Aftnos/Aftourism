<template>
  <div class="page-card">
    <OpBar>
      <ElButton type="primary" v-can="'VENUE:CREATE'" @click="openEdit()">新增场馆</ElButton>
    </OpBar>
    <SmartTable ref="tableRef" :fetcher="fetchData">
      <template #query="{ form }">
        <ElFormItem label="名称">
          <ElInput v-model="form.name" placeholder="场馆名称" clearable />
        </ElFormItem>
      </template>
      <ElTableColumn prop="name" label="名称" />
      <ElTableColumn prop="category" label="分类" width="120" />
      <ElTableColumn label="是否免费" width="120">
        <template #default="{ row }">
          <ElTag :type="row.isFree ? 'success' : 'info'">{{ row.isFree ? '免费' : '收费' }}</ElTag>
        </template>
      </ElTableColumn>
      <ElTableColumn prop="address" label="地址" />
      <ElTableColumn label="操作" width="200">
        <template #default="{ row }">
          <ElButton text size="small" type="primary" v-can="'VENUE:UPDATE'" @click="openEdit(row)">编辑</ElButton>
          <ElButton text size="small" type="danger" v-can="'VENUE:DELETE'" @click="handleDelete(row)">删除</ElButton>
        </template>
      </ElTableColumn>
    </SmartTable>

    <ElDialog v-model="visible" :title="current.id ? '编辑场馆' : '新增场馆'" width="720px">
      <ElForm ref="formRef" :model="current" :rules="rules" label-width="90px">
        <ElFormItem label="名称" prop="name">
          <ElInput v-model="current.name" />
        </ElFormItem>
        <ElFormItem label="分类" prop="category">
          <ElInput v-model="current.category" />
        </ElFormItem>
        <ElFormItem label="封面图" prop="imageUrl">
          <UploadBox biz-tag="VENUE_IMAGE" @uploaded="handleImageUploaded" />
          <ElImage v-if="current.imageUrl" :src="current.imageUrl" fit="cover" class="cover-preview" />
        </ElFormItem>
        <ElFormItem label="是否免费" prop="isFree">
          <ElSwitch v-model="current.isFree" :active-value="1" :inactive-value="0" />
        </ElFormItem>
        <ElFormItem v-if="!current.isFree" label="票价" prop="ticketPrice">
          <ElInputNumber v-model="current.ticketPrice" :min="0" :precision="2" :step="1" />
        </ElFormItem>
        <ElFormItem label="地址" prop="address">
          <ElInput v-model="current.address" />
        </ElFormItem>
        <ElFormItem label="开放时间" prop="openTime">
          <ElInput v-model="current.openTime" />
        </ElFormItem>
        <ElFormItem label="联系电话">
          <ElInput v-model="current.phone" />
        </ElFormItem>
        <ElFormItem label="简介" prop="description">
          <ElInput v-model="current.description" type="textarea" rows="4" />
        </ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton @click="visible = false">取消</ElButton>
        <ElButton type="primary" @click="save">保存</ElButton>
      </template>
    </ElDialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus';
import OpBar from '@/components/common/OpBar.vue';
import SmartTable from '@/components/table/SmartTable.vue';
import UploadBox from '@/components/upload/UploadBox.vue';
import { fetchVenues, createVenue, updateVenue, deleteVenue, type VenueItem } from '@/api/business';
import { createTraceId } from '@/utils/trace';

const tableRef = ref<InstanceType<typeof SmartTable>>();
const visible = ref(false);
const current = reactive<VenueItem>({ name: '', category: '', isFree: 1, address: '', description: '', imageUrl: '' });
const formRef = ref<FormInstance>();
const rules = reactive<FormRules<VenueItem>>({
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  category: [{ required: true, message: '请输入分类', trigger: 'blur' }],
  imageUrl: [{ required: true, message: '请上传封面图', trigger: 'change' }],
  address: [{ required: true, message: '请输入地址', trigger: 'blur' }],
  openTime: [{ required: true, message: '请输入开放时间', trigger: 'blur' }],
  description: [{ required: true, message: '请输入简介', trigger: 'blur' }],
  ticketPrice: [{ required: false, trigger: 'change' }]
});

async function fetchData(params: Record<string, any>) {
  return fetchVenues(params);
}

function openEdit(row?: VenueItem) {
  Object.assign(current, row || { name: '', category: '', isFree: 1, address: '', description: '', imageUrl: '' });
  visible.value = true;
}

function handleImageUploaded(url: string) {
  current.imageUrl = url;
}

async function save() {
  if (!formRef.value) return;
  await formRef.value.validate();
  if (!current.isFree && (!current.ticketPrice || current.ticketPrice < 0)) {
    return ElMessage.warning('请输入有效的票价');
  }
  const traceId = createTraceId('venue');
  console.info('操作摘要', { traceId, action: current.id ? 'updateVenue' : 'createVenue', params: current });
  if (current.id) {
    await updateVenue(current.id, current);
  } else {
    await createVenue(current);
  }
  visible.value = false;
  tableRef.value?.fetch();
  ElMessage.success('保存成功');
}

async function handleDelete(row: VenueItem) {
  await ElMessageBox.confirm(`确认删除场馆「${row.name}」吗？`, '提示', { type: 'warning' });
  await deleteVenue(row.id!);
  ElMessage.success('已删除');
  tableRef.value?.fetch();
}
</script>

<style scoped>
.cover-preview {
  display: block;
  width: 200px;
  height: 120px;
  margin-top: 8px;
  border-radius: 6px;
}
</style>
