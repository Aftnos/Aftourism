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
      v-model:current-page="pager.page"
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

const props = defineProps<{ fetcher: (params: Record<string, any>) => Promise<{ records: any[]; total: number }> }>();

const form = reactive<Record<string, any>>({});
const pager = reactive({ page: 1, pageSize: 10, total: 0 });
const data = ref<any[]>([]);
const loading = ref(false);

async function fetch() {
  loading.value = true;
  const { page, pageSize } = pager;
  try {
    const res = await props.fetcher({ ...form, page, pageSize });
    data.value = res.records;
    pager.total = res.total;
  } finally {
    loading.value = false;
  }
}

function handleSearch() {
  pager.page = 1;
  fetch();
}

function handleReset() {
  Object.keys(form).forEach((key) => (form[key] = ''));
  pager.page = 1;
  fetch();
}

fetch();
</script>

<style scoped>
.smart-table {
  background-color: #fff;
  padding: 16px;
  border-radius: 8px;
}
</style>
