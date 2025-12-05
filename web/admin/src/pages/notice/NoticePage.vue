<template>
  <div class="page-card">
    <OpBar>
      <ElButton type="primary" v-can="'NOTICE:CREATE'" @click="openEdit()">新增通知</ElButton>
    </OpBar>
    <SmartTable ref="tableRef" :fetcher="fetchData">
      <template #query="{ form }">
        <ElFormItem label="标题">
          <ElInput v-model="form.title" placeholder="标题" clearable />
        </ElFormItem>
      </template>
      <ElTableColumn prop="title" label="标题" />
      <ElTableColumn prop="author" label="发布人" width="120" />
      <ElTableColumn label="状态" width="100">
        <template #default="{ row }">
          <ElTag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '发布' : '下线' }}</ElTag>
        </template>
      </ElTableColumn>
      <ElTableColumn prop="publishTime" label="发布时间" width="180" />
      <ElTableColumn label="操作" width="200">
        <template #default="{ row }">
          <ElButton text size="small" type="primary" v-can="'NOTICE:UPDATE'" @click="openEdit(row)">编辑</ElButton>
          <ElButton text size="small" type="danger" v-can="'NOTICE:DELETE'" @click="handleDelete(row)">删除</ElButton>
        </template>
      </ElTableColumn>
    </SmartTable>

    <ElDialog v-model="visible" :title="current.id ? '编辑通知' : '新增通知'" width="640px">
      <ElForm ref="formRef" :model="current" :rules="rules" label-width="80px">
        <ElFormItem label="标题" prop="title">
          <ElInput v-model="current.title" />
        </ElFormItem>
        <ElFormItem label="发布人" prop="author">
          <ElInput v-model="current.author" />
        </ElFormItem>
        <ElFormItem label="状态" prop="status">
          <ElSelect v-model="current.status">
            <ElOption label="已发布" :value="1" />
            <ElOption label="已下线" :value="0" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="内容" prop="content">
          <ElInput v-model="current.content" type="textarea" rows="5" />
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
import { fetchNotices, createNotice, updateNotice, deleteNotice, type NoticeItem } from '@/api/business';
import { createTraceId } from '@/utils/trace';

const tableRef = ref<InstanceType<typeof SmartTable>>();
const visible = ref(false);
const current = reactive<NoticeItem>({ title: '', content: '', status: 1, author: '' });
const formRef = ref<FormInstance>();
const rules = reactive<FormRules<NoticeItem>>({
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  author: [{ required: true, message: '请输入发布人', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
});

async function fetchData(params: Record<string, any>) {
  return fetchNotices(params);
}

function openEdit(row?: NoticeItem) {
  Object.assign(current, row || { title: '', content: '', status: 1, author: '' });
  visible.value = true;
}

async function save() {
  if (!formRef.value) return;
  await formRef.value.validate();
  const traceId = createTraceId('notice');
  console.info('操作摘要', { traceId, action: current.id ? 'updateNotice' : 'createNotice', params: current });
  if (current.id) {
    await updateNotice(current.id, current);
  } else {
    await createNotice(current);
  }
  visible.value = false;
  tableRef.value?.fetch();
  ElMessage.success('保存成功');
}

async function handleDelete(row: NoticeItem) {
  await ElMessageBox.confirm(`确认删除通知「${row.title}」吗？`, '提示', { type: 'warning' });
  await deleteNotice(row.id!);
  ElMessage.success('已删除');
  tableRef.value?.fetch();
}
</script>
