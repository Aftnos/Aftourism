<template>
  <SmartTable :fetcher="fetchData">
    <template #query="{ form }">
      <ElFormItem label="关键词">
        <ElInput v-model="form.keyword" placeholder="昵称/手机号" />
      </ElFormItem>
    </template>
    <ElTableColumn prop="nickname" label="昵称" />
    <ElTableColumn prop="mobile" label="手机号" />
    <ElTableColumn prop="status" label="状态" />
    <ElTableColumn label="操作">
      <template #default="{ row }">
        <ElButton type="danger" size="small" v-can="'USER:DISABLE'" @click="disable(row)">禁用</ElButton>
      </template>
    </ElTableColumn>
  </SmartTable>
</template>

<script setup lang="ts">
import { fetchUsers, disableUser } from '@/api/admin';
import SmartTable from '@/components/table/SmartTable.vue';
import { ElMessage } from 'element-plus';

async function fetchData(params: Record<string, any>) {
  const res = await fetchUsers(params);
  return { records: res.records || [], total: res.total || 0 };
}

async function disable(row: any) {
  await disableUser(row.id);
  ElMessage.success('已禁用');
}
</script>
