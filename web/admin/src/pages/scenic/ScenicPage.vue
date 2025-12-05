<template>
  <div class="page-card">
    <OpBar>
      <ElButton type="primary" v-can="'SCENIC:CREATE'" @click="openEdit()">新增景区</ElButton>
    </OpBar>
    <SmartTable ref="tableRef" :fetcher="fetchData">
      <template #query="{ form }">
        <ElFormItem label="名称">
          <ElInput v-model="form.name" placeholder="景区名称" clearable />
        </ElFormItem>
      </template>
      <ElTableColumn prop="name" label="名称" />
      <ElTableColumn prop="level" label="等级" width="100" />
      <ElTableColumn prop="address" label="地址" />
      <ElTableColumn label="操作" width="200">
        <template #default="{ row }">
          <ElButton text size="small" type="primary" v-can="'SCENIC:UPDATE'" @click="openEdit(row)">编辑</ElButton>
          <ElButton text size="small" type="danger" v-can="'SCENIC:DELETE'" @click="handleDelete(row)">删除</ElButton>
        </template>
      </ElTableColumn>
    </SmartTable>

    <ElDialog v-model="visible" :title="current.id ? '编辑景区' : '新增景区'" width="720px">
      <ElForm ref="formRef" :model="current" :rules="rules" label-width="90px">
        <ElFormItem label="名称" prop="name">
          <ElInput v-model="current.name" />
        </ElFormItem>
        <ElFormItem label="等级" prop="level">
          <ElInput v-model="current.level" />
        </ElFormItem>
        <ElFormItem label="封面图" prop="imageUrl">
          <UploadBox biz-tag="SCENIC_IMAGE" @uploaded="handleImageUploaded" />
          <ElImage v-if="current.imageUrl" :src="current.imageUrl" fit="cover" class="cover-preview" />
        </ElFormItem>
        <ElFormItem label="门票价格" prop="ticketPrice">
          <ElInputNumber v-model="current.ticketPrice" :min="0" :precision="2" :step="1" />
        </ElFormItem>
        <ElFormItem label="地址" prop="address">
          <ElInput v-model="current.address" />
        </ElFormItem>
        <ElFormItem label="开放时间" prop="openTime">
          <ElInput v-model="current.openTime" />
        </ElFormItem>
        <ElFormItem label="联系方式">
          <ElInput v-model="current.phone" />
        </ElFormItem>
        <ElFormItem label="简介" prop="intro">
          <ElInput v-model="current.intro" type="textarea" rows="4" />
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
import { fetchScenicList, createScenic, updateScenic, deleteScenic, type ScenicItem } from '@/api/business';
import { createTraceId } from '@/utils/trace';

const tableRef = ref<InstanceType<typeof SmartTable>>();
const visible = ref(false);
const current = reactive<ScenicItem>({ name: '', level: '', address: '', openTime: '', intro: '', imageUrl: '', ticketPrice: 0 });
const formRef = ref<FormInstance>();
const rules = reactive<FormRules<ScenicItem>>({
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  level: [{ required: true, message: '请输入等级', trigger: 'blur' }],
  imageUrl: [{ required: true, message: '请上传封面图', trigger: 'change' }],
  ticketPrice: [{ required: true, message: '请输入门票价格', trigger: 'change' }],
  address: [{ required: true, message: '请输入地址', trigger: 'blur' }],
  openTime: [{ required: true, message: '请输入开放时间', trigger: 'blur' }],
  intro: [{ required: true, message: '请输入简介', trigger: 'blur' }]
});

async function fetchData(params: Record<string, any>) {
  return fetchScenicList(params);
}

function openEdit(row?: ScenicItem) {
  Object.assign(current, row || { name: '', level: '', address: '', openTime: '', intro: '', imageUrl: '', ticketPrice: 0 });
  visible.value = true;
}

function handleImageUploaded(url: string) {
  current.imageUrl = url;
}

async function save() {
  if (!formRef.value) return;
  await formRef.value.validate();
  const traceId = createTraceId('scenic');
  console.info('操作摘要', { traceId, action: current.id ? 'updateScenic' : 'createScenic', params: current });
  if (current.id) {
    await updateScenic(current.id, current);
  } else {
    await createScenic(current);
  }
  visible.value = false;
  tableRef.value?.fetch();
  ElMessage.success('保存成功');
}

async function handleDelete(row: ScenicItem) {
  await ElMessageBox.confirm(`确认删除景区「${row.name}」吗？`, '提示', { type: 'warning' });
  await deleteScenic(row.id!);
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
