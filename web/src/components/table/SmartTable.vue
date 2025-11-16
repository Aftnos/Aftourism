<template>
  <div class="smart-table">
    <ElForm :model="form" inline @submit.prevent>
      <slot name="query" :form="form" />
      <ElFormItem>
        <ElButton type="primary" @click="handleSearch">查询</ElButton>
        <ElButton @click="handleReset">重置</ElButton>
      </ElFormItem>
    </ElForm>
    <ElTable :data="data" border style="width: 100%" :loading="loading">
      <slot />
    </ElTable>
    <ElPagination
      v-model:current-page="pager.pageNum"
      v-model:page-size="pager.pageSize"
      :total="pager.total"
      layout="prev, pager, next, total"
      background
      @current-change="fetch"
      @size-change="fetch"
    />
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';

const props = defineProps<{ fetcher: (params: Record<string, any>) => Promise<Record<string, any>> }>();

const form = reactive<Record<string, any>>({});
const pager = reactive({ pageNum: 1, pageSize: 10, total: 0 });
const data = ref<any[]>([]);
const loading = ref(false);

async function fetch() {
  loading.value = true;
  try {
    const res = await props.fetcher({ ...form, pageNum: pager.pageNum, pageSize: pager.pageSize });
    const records = (res?.list as any[]) || (res?.records as any[]) || [];
    data.value = records;
    pager.total = res?.total ?? records.length;
  } finally {
    loading.value = false;
  }
}

function handleSearch() {
  pager.pageNum = 1;
  fetch();
}

function handleReset() {
  Object.keys(form).forEach((key) => (form[key] = ''));
  pager.pageNum = 1;
  fetch();
}

fetch();

// 暴露刷新方法，业务场景可在保存后主动触发表格重载
defineExpose({ fetch });
</script>

<style scoped>
.smart-table {
  background-color: #fff;
  padding: 16px;
  border-radius: 8px;
}
</style>
