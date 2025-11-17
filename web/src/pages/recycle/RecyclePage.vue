<template>
  <div class="page-card">
    <SmartTable ref="tableRef" :fetcher="fetchData">
      <template #query="{ form }">
        <ElFormItem label="类型">
          <ElSelect v-model="form.type" placeholder="所有类型" clearable>
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
      <ElTableColumn label="类型" width="120">
        <template #default="{ row }">{{ typeText(row.type) }}</template>
      </ElTableColumn>
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

const TYPE_MAP: Record<string, string> = { NEWS: '新闻', NOTICE: '通知', SCENIC: '景区', VENUE: '场馆', ACTIVITY: '活动' };

function typeText(t?: string) {
  return t ? TYPE_MAP[t] || t : '';
}

async function fetchData(params: Record<string, any>) {
  if (!params.type) {
    const types = ['NEWS', 'NOTICE', 'SCENIC', 'VENUE', 'ACTIVITY'];
    const pageNum = params.pageNum ?? 1;
    const pageSize = params.pageSize ?? 10;
    const all: RecycleItem[] = [];
    let total = 0;
    for (const t of types) {
      try {
        const res = await fetchRecycleItems({ ...params, type: t, pageNum, pageSize });
        const payload = (res as any)?.data ? (res as any).data : res;
        const list = (payload?.list || []) as RecycleItem[];
        total += Number(payload?.total || list.length);
        all.push(...list);
      } catch (e) {
        // ignore single type error
      }
    }
    all.sort((a, b) => new Date(b.deletedTime).getTime() - new Date(a.deletedTime).getTime());
    return { list: all.slice(0, pageSize), total, pageNum, pageSize };
  }
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
