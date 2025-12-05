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
      :page-sizes="pageSizes"
      layout="sizes, prev, pager, next, total"
      background
      @current-change="handlePageChange"
      @size-change="handleSizeChange"
    />
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, watch, onMounted } from 'vue';
import { useRoute } from 'vue-router';

const props = defineProps<{
  fetcher: (params: Record<string, any>) => Promise<Record<string, any>>;
  pageSizes?: number[];
  defaultPageSize?: number;
  preservePageState?: boolean;
}>();

const form = reactive<Record<string, any>>({});
const route = useRoute();
const pageSizes = props.pageSizes ?? [10, 20, 50];
const pager = reactive({ pageNum: 1, pageSize: props.defaultPageSize ?? 10, total: 0 });
const data = ref<any[]>([]);
const loading = ref(false);

async function fetch() {
  loading.value = true;
  try {
    const res = await props.fetcher({ ...form, pageNum: pager.pageNum, pageSize: pager.pageSize });
    const payload = (res as any)?.data ? (res as any).data : res;
    const records = (payload?.list as any[]) || (payload?.records as any[]) || [];
    data.value = records;
    pager.total = payload?.total ?? records.length;
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

function handlePageChange(page: number) {
  pager.pageNum = page;
  fetch();
}

function handleSizeChange(size: number) {
  pager.pageSize = size;
  pager.pageNum = 1;
  fetch();
}

function stateKey() {
  return `smart-table:${route.path}`;
}

function loadState() {
  if (props.preservePageState === false) return;
  try {
    const raw = sessionStorage.getItem(stateKey());
    if (!raw) return;
    const saved = JSON.parse(raw);
    if (typeof saved?.pageNum === 'number') pager.pageNum = saved.pageNum;
    if (typeof saved?.pageSize === 'number') pager.pageSize = saved.pageSize;
    if (saved?.form && typeof saved.form === 'object') {
      Object.assign(form, saved.form);
    }
  } catch {}
}

function saveState() {
  if (props.preservePageState === false) return;
  try {
    const snapshot = { pageNum: pager.pageNum, pageSize: pager.pageSize, form: { ...form } };
    sessionStorage.setItem(stateKey(), JSON.stringify(snapshot));
  } catch {}
}

watch(() => pager.pageNum, saveState);
watch(() => pager.pageSize, saveState);
watch(form, saveState, { deep: true });

onMounted(() => {
  loadState();
  fetch();
});

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
