<template>
  <div class="page-card">
    <SmartTable ref="tableRef" :fetcher="fetchData">
      <template #query="{ form }">
        <ElFormItem label="账号">
          <ElInput v-model="form.username" placeholder="账号/手机号" clearable />
        </ElFormItem>
        <ElFormItem label="昵称">
          <ElInput v-model="form.nickname" placeholder="昵称" clearable />
        </ElFormItem>
      </template>
      <ElTableColumn prop="username" label="账号" width="160" />
      <ElTableColumn prop="nickname" label="昵称" width="140" />
      <ElTableColumn prop="roleCode" label="角色" width="140" />
      <ElTableColumn label="状态" width="120">
        <template #default="{ row }">
          <ElTag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '禁用' }}</ElTag>
        </template>
      </ElTableColumn>
      <ElTableColumn label="操作" width="160">
        <template #default="{ row }">
          <ElButton text size="small" type="primary" v-can="'PORTAL_USER:UPDATE'" @click="openEdit(row)">调整</ElButton>
        </template>
      </ElTableColumn>
    </SmartTable>

    <ElDialog v-model="visible" title="调整门户用户" width="400px">
      <ElForm :model="current" label-width="90px">
        <ElFormItem label="角色编码">
          <ElInput v-model="current.roleCode" placeholder="如 PORTAL_EDITOR" />
        </ElFormItem>
        <ElFormItem label="状态">
          <ElSwitch v-model="current.status" :active-value="1" :inactive-value="0" />
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
import { ElMessage } from 'element-plus';
import SmartTable from '@/components/table/SmartTable.vue';
import { fetchPortalUsers, updatePortalUser, type PortalUserSummary } from '@/api/admin';

const tableRef = ref<InstanceType<typeof SmartTable>>();
const visible = ref(false);
const current = reactive<PortalUserSummary>({ id: 0, username: '', nickname: '', roleCode: '', status: 1 });

async function fetchData(params: Record<string, any>) {
  return fetchPortalUsers(params);
}

function openEdit(row: PortalUserSummary) {
  Object.assign(current, row);
  visible.value = true;
}

async function save() {
  await updatePortalUser(current.id, { roleCode: current.roleCode, status: current.status });
  ElMessage.success('已保存');
  visible.value = false;
  tableRef.value?.fetch();
}
</script>
