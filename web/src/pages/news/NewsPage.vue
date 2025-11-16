<template>
  <div class="page-card">
    <OpBar>
      <ElButton type="primary" v-can="'NEWS:WRITE'" @click="openModal()">新增新闻</ElButton>
    </OpBar>
    <SmartTable :fetcher="fetchData">
      <template #query="{ form }">
        <ElFormItem label="标题">
          <ElInput v-model="form.title" placeholder="标题关键字" />
        </ElFormItem>
      </template>
      <ElTableColumn prop="title" label="标题" />
      <ElTableColumn prop="status" label="状态" />
      <ElTableColumn prop="publishAt" label="发布时间" />
    </SmartTable>

    <ElDialog v-model="visible" title="新闻">
      <ElForm :model="current" label-width="80px">
        <ElFormItem label="标题">
          <ElInput v-model="current.title" />
        </ElFormItem>
        <ElFormItem label="内容">
          <ElInput v-model="current.content" type="textarea" rows="4" />
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
import SmartTable from '@/components/table/SmartTable.vue';
import OpBar from '@/components/common/OpBar.vue';
import { fetchNews, saveNews } from '@/api/business';
import { ElMessage } from 'element-plus';

const visible = ref(false);
const current = reactive<any>({ title: '', content: '' });

async function fetchData(params: Record<string, any>) {
  const res = await fetchNews(params);
  return { records: res.records || [], total: res.total || 0 };
}

function openModal() {
  Object.assign(current, { title: '', content: '' });
  visible.value = true;
}

async function save() {
  await saveNews(current);
  ElMessage.success('已保存');
  visible.value = false;
}
</script>
