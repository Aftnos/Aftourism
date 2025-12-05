<template>
  <div class="page-card">
    <OpBar>
      <ElButton type="primary" v-can="'NEWS:CREATE'" @click="openEdit()">新增新闻</ElButton>
    </OpBar>
    <SmartTable ref="tableRef" :fetcher="fetchData">
      <template #query="{ form }">
        <ElFormItem label="标题">
          <ElInput v-model="form.title" placeholder="标题关键字" clearable />
        </ElFormItem>
        <ElFormItem label="状态">
          <ElSelect v-model="form.status" placeholder="全部" clearable>
            <ElOption label="已发布" :value="1" />
            <ElOption label="已下线" :value="0" />
          </ElSelect>
        </ElFormItem>
      </template>
      <ElTableColumn prop="title" label="标题" />
      <ElTableColumn prop="author" label="作者" width="120" />
      <ElTableColumn label="状态" width="100">
        <template #default="{ row }">
          <ElTag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '发布' : '下线' }}</ElTag>
        </template>
      </ElTableColumn>
      <ElTableColumn prop="publishTime" label="发布时间" width="180" />
      <ElTableColumn label="操作" width="200">
        <template #default="{ row }">
          <ElButton text size="small" type="primary" v-can="'NEWS:UPDATE'" @click="openEdit(row)">编辑</ElButton>
          <ElButton text size="small" type="danger" v-can="'NEWS:DELETE'" @click="handleDelete(row)">删除</ElButton>
        </template>
      </ElTableColumn>
    </SmartTable>

    <ElDialog v-model="visible" :title="current.id ? '编辑新闻' : '新增新闻'" width="640px">
      <ElForm ref="formRef" :model="current" :rules="rules" label-width="80px">
        <ElFormItem label="标题" prop="title">
          <ElInput v-model="current.title" />
        </ElFormItem>
        <ElFormItem label="作者" prop="author">
          <ElInput v-model="current.author" />
        </ElFormItem>
        <ElFormItem label="封面" prop="coverUrl">
          <UploadBox biz-tag="NEWS_COVER" @uploaded="handleCoverUploaded" />
          <ElImage v-if="current.coverUrl" :src="current.coverUrl" fit="cover" class="cover-preview" />
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
import UploadBox from '@/components/upload/UploadBox.vue';
import { fetchNews, createNews, updateNews, deleteNews, type NewsItem } from '@/api/business';
import { createTraceId } from '@/utils/trace';

const tableRef = ref<InstanceType<typeof SmartTable>>();
const visible = ref(false);
const current = reactive<NewsItem>({ title: '', content: '', coverUrl: '', status: 1, author: '' });
const formRef = ref<FormInstance>();
const rules = reactive<FormRules<NewsItem>>({
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  author: [{ required: true, message: '请输入作者', trigger: 'blur' }],
  coverUrl: [{ required: true, message: '请上传封面', trigger: 'change' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
});

async function fetchData(params: Record<string, any>) {
  return fetchNews(params);
}

function openEdit(row?: NewsItem) {
  Object.assign(current, row || { title: '', content: '', coverUrl: '', status: 1, author: '' });
  visible.value = true;
}

function handleCoverUploaded(url: string) {
  current.coverUrl = url;
}

async function save() {
  if (!formRef.value) return;
  await formRef.value.validate();
  const traceId = createTraceId('news');
  console.info('操作摘要', { traceId, action: current.id ? 'updateNews' : 'createNews', params: { ...current } });
  if (current.id) {
    await updateNews(current.id, current);
  } else {
    await createNews(current);
  }
  visible.value = false;
  tableRef.value?.fetch();
  ElMessage.success('保存成功');
}

async function handleDelete(row: NewsItem) {
  await ElMessageBox.confirm(`确认删除新闻「${row.title}」吗？`, '提示', { type: 'warning' });
  const traceId = createTraceId('news');
  console.info('操作摘要', { traceId, action: 'deleteNews', params: { id: row.id } });
  await deleteNews(row.id!);
  ElMessage.success('已删除');
  tableRef.value?.fetch();
}
</script>

<style scoped>
.cover-preview {
  display: block;
  margin-top: 8px;
  width: 160px;
  height: 90px;
  border-radius: 6px;
}
</style>
