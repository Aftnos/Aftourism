<template>
  <div class="page-card">
    <OpBar>
      <ElButton type="primary" v-can="'ADMIN_ACCOUNT:CREATE'" @click="openEdit()">新增管理员</ElButton>
    </OpBar>
    <SmartTable ref="tableRef" :fetcher="fetchData">
      <template #query="{ form }">
        <ElFormItem label="账号">
          <ElInput v-model="form.username" placeholder="登录账号" clearable />
        </ElFormItem>
        <ElFormItem label="姓名">
          <ElInput v-model="form.realName" placeholder="真实姓名" clearable />
        </ElFormItem>
        <ElFormItem label="状态">
          <ElSelect v-model="form.status" placeholder="全部" clearable>
            <ElOption label="启用" :value="1" />
            <ElOption label="停用" :value="0" />
          </ElSelect>
        </ElFormItem>
      </template>
      <ElTableColumn prop="username" label="账号" width="140" />
      <ElTableColumn prop="realName" label="姓名" width="120" />
      <ElTableColumn label="角色">
        <template #default="{ row }">{{ row.roleCodes?.join('、') || '-' }}</template>
      </ElTableColumn>
      <ElTableColumn label="状态" width="100">
        <template #default="{ row }">
          <ElTag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '停用' }}</ElTag>
        </template>
      </ElTableColumn>
      <ElTableColumn prop="superAdmin" label="超管" width="100">
        <template #default="{ row }">
          <ElTag type="danger" v-if="row.superAdmin">是</ElTag>
          <span v-else>否</span>
        </template>
      </ElTableColumn>
      <ElTableColumn label="操作" width="220">
        <template #default="{ row }">
          <ElButton type="primary" text size="small" v-can="'ADMIN_ACCOUNT:UPDATE'" @click="openEdit(row)">编辑</ElButton>
          <ElButton type="warning" text size="small" v-can="'ADMIN_ACCOUNT:UPDATE'" @click="toggleStatus(row)">
            {{ row.status === 1 ? '停用' : '启用' }}
          </ElButton>
          <ElButton type="danger" text size="small" v-can="'ADMIN_ACCOUNT:DELETE'" @click="handleDelete(row)">删除</ElButton>
        </template>
      </ElTableColumn>
    </SmartTable>

    <ElDialog v-model="visible" :title="current.id ? '编辑管理员' : '新增管理员'" width="520px">
      <ElForm :model="current" label-width="90px">
        <ElFormItem label="账号">
          <ElInput v-model="current.username" :disabled="!!current.id" placeholder="登录账号" />
        </ElFormItem>
        <ElFormItem v-if="!current.id" label="初始密码">
          <ElInput v-model="current.password" type="password" placeholder="不少于6位" />
        </ElFormItem>
        <ElFormItem label="真实姓名">
          <ElInput v-model="current.realName" />
        </ElFormItem>
        <ElFormItem label="联系电话">
          <ElInput v-model="current.phone" />
        </ElFormItem>
        <ElFormItem label="邮箱">
          <ElInput v-model="current.email" />
        </ElFormItem>
        <ElFormItem label="角色编码">
          <ElSelect v-model="current.roleCodes" multiple placeholder="请选择角色">
            <ElOption v-for="role in roleCodes" :key="role" :label="role" :value="role" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="状态">
          <ElSwitch v-model="current.status" :active-value="1" :inactive-value="0" />
        </ElFormItem>
        <ElFormItem label="是否超管" v-if="auth.isSuper">
          <ElSwitch v-model="current.superAdmin" />
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
import { onMounted, reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import SmartTable from '@/components/table/SmartTable.vue';
import OpBar from '@/components/common/OpBar.vue';
import { useAuthStore } from '@/store/auth';
import { createTraceId } from '@/utils/trace';
import {
  fetchAdminPage,
  createAdmin,
  updateAdmin,
  deleteAdmin,
  fetchRoles,
  type AdminAccount,
  type RoleSummary
} from '@/api/admin';

const auth = useAuthStore();
const tableRef = ref<InstanceType<typeof SmartTable>>();
const visible = ref(false);
const current = reactive<AdminAccount>({ username: '', roleCodes: [], status: 1, superAdmin: false });
const roleCodes = ref<string[]>([]);

async function fetchData(params: Record<string, any>) {
  return fetchAdminPage(params);
}

function openEdit(row?: AdminAccount) {
  Object.assign(current, row || { username: '', password: '', realName: '', phone: '', email: '', roleCodes: [], status: 1, superAdmin: false });
  visible.value = true;
}

async function save() {
  if (!current.username) return ElMessage.warning('请填写账号');
  if (!current.id && !current.password) return ElMessage.warning('请设置初始密码');
  const traceId = createTraceId('admin');
  console.info('操作摘要', { traceId, action: current.id ? 'updateAdmin' : 'createAdmin', params: current });
  if (current.id) {
    await updateAdmin(current.id, { ...current });
  } else {
    await createAdmin(current);
  }
  visible.value = false;
  tableRef.value?.fetch();
  ElMessage.success('保存成功');
}

async function toggleStatus(row: AdminAccount) {
  const nextStatus = row.status === 1 ? 0 : 1;
  const traceId = createTraceId('admin');
  console.info('操作摘要', { traceId, action: 'toggleAdminStatus', params: { id: row.id, status: nextStatus } });
  await updateAdmin(row.id!, { status: nextStatus });
  ElMessage.success('状态已更新');
  tableRef.value?.fetch();
}

async function handleDelete(row: AdminAccount) {
  await ElMessageBox.confirm(`确认删除管理员 ${row.username} 吗？`, '提示', { type: 'warning' });
  const traceId = createTraceId('admin');
  console.info('操作摘要', { traceId, action: 'deleteAdmin', params: { id: row.id } });
  await deleteAdmin(row.id!);
  ElMessage.success('已删除');
  tableRef.value?.fetch();
}

async function loadRoles() {
  const list = await fetchRoles();
  roleCodes.value = list.map((item: RoleSummary) => item.roleCode);
}

onMounted(loadRoles);
</script>
