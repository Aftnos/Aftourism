<template>
  <div class="page-card">
    <SmartTable ref="tableRef" :fetcher="fetchData">
      <template #query="{ form }">
        <ElFormItem label="类型">
          <ElSelect v-model="form.type" placeholder="全部" clearable>
            <ElOption label="新闻" value="NEWS" />
            <ElOption label="通知" value="NOTICE" />
            <ElOption label="景区" value="SCENIC" />
            <ElOption label="场馆" value="VENUE" />
            <ElOption label="活动" value="ACTIVITY" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="关键词">
          <ElInput v-model="form.keyword" placeholder="标题关键字" clearable />
        </ElFormItem>
      </template>
      <ElTableColumn prop="type" label="类型" width="120" />
      <ElTableColumn prop="title" label="标题" />
      <ElTableColumn prop="operator" label="操作人" width="160" />
      <ElTableColumn prop="deletedTime" label="删除时间" width="200" />
      <ElTableColumn prop="extraInfo" label="补充信息" />
      <ElTableColumn label="操作" width="220">
        <template #default="{ row }">
          <ElButton text type="primary" size="small" v-can="'RECYCLE_BIN:RESTORE'" @click="restore(row)">还原</ElButton>
          <ElButton text type="danger" size="small" v-can="'RECYCLE_BIN:DELETE'" @click="remove(row)">彻底删除</ElButton>
        </template>
      </ElTableColumn>
    </SmartTable>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import SmartTable from '@/components/table/SmartTable.vue';
import { fetchRecycleItems, restoreRecycle, removeRecycle, type RecycleItem } from '@/api/business';
import { createTraceId } from '@/utils/trace';

const tableRef = ref<InstanceType<typeof SmartTable>>();

function fetchData(params: Record<string, any>) {
  return fetchRecycleItems(params);
}

async function restore(row: RecycleItem) {
  await ElMessageBox.confirm(`确认还原 ${row.title} 吗？`, '提示', { type: 'warning' });
  const traceId = createTraceId('recycle');
  console.info('操作摘要', { traceId, action: 'recycleRestore', params: { type: row.type, id: row.id } });
  await restoreRecycle(row.type, row.id);
  ElMessage.success('已还原');
  tableRef.value?.fetch();
}

async function remove(row: RecycleItem) {
  await ElMessageBox.confirm(`确认彻底删除 ${row.title} 吗？`, '提示', { type: 'warning' });
  const traceId = createTraceId('recycle');
  console.info('操作摘要', { traceId, action: 'recycleRemove', params: { type: row.type, id: row.id } });
  await removeRecycle(row.type, row.id);
  ElMessage.success('已删除');
  tableRef.value?.fetch();
}
</script>
