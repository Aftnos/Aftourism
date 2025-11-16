<template>
  <SmartTable :fetcher="fetchData">
    <template #query="{ form }">
      <ElFormItem label="名称">
        <ElInput v-model="form.keyword" placeholder="姓名/账号" />
      </ElFormItem>
    </template>
    <ElTableColumn prop="username" label="账号" />
    <ElTableColumn prop="roleCode" label="角色" />
    <ElTableColumn prop="isSuper" label="超管" />
    <ElTableColumn label="操作">
      <template #default="{ row }">
        <ElButton type="primary" size="small" v-can="'ADMIN:EDIT'" @click="openEdit(row)">编辑</ElButton>
        <ElButton type="danger" size="small" v-can="'ADMIN:DISABLE'" @click="disable(row)">停用</ElButton>
      </template>
    </ElTableColumn>
  </SmartTable>

  <ElDialog v-model="visible" title="管理员" width="420px">
    <ElForm :model="current" label-width="80px">
      <ElFormItem label="账号">
        <ElInput v-model="current.username" :disabled="!!current.id" />
      </ElFormItem>
      <ElFormItem label="角色编码">
        <ElInput v-model="current.roleCode" />
      </ElFormItem>
      <ElFormItem label="是否超管">
        <ElSwitch v-model="current.isSuper" :disabled="!auth.isSuper" />
      </ElFormItem>
    </ElForm>
    <template #footer>
      <ElButton @click="visible = false">取消</ElButton>
      <ElButton type="primary" @click="save">保存</ElButton>
    </template>
  </ElDialog>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { fetchAdmins, saveAdmin, updateAdmin, disableAdmin } from '@/api/admin';
import SmartTable from '@/components/table/SmartTable.vue';
import { useAuthStore } from '@/store/auth';
import { createTraceId } from '@/utils/trace';

const visible = ref(false);
const current = reactive<any>({});
const auth = useAuthStore();

async function fetchData(params: Record<string, any>) {
  const res = await fetchAdmins(params);
  return { records: res.records || [], total: res.total || 0 };
}

function openEdit(row?: any) {
  Object.assign(current, row || { username: '', roleCode: '', isSuper: false });
  visible.value = true;
}

async function save() {
  const traceId = createTraceId('admin');
  console.info('操作摘要', { traceId, action: current.id ? 'updateAdmin' : 'createAdmin', params: current });
  if (current.id) {
    await updateAdmin(current.id, current);
  } else {
    await saveAdmin(current);
  }
  visible.value = false;
  ElMessage.success('保存成功');
}

async function disable(row: any) {
  await disableAdmin(row.id);
  ElMessage.success('已停用');
}
</script>
