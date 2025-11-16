<template>
  <div class="page-card">
    <ElRow :gutter="16">
      <ElCol :span="10">
        <ElTable :data="roles" height="360" @row-click="selectRole">
          <ElTableColumn prop="code" label="编码" />
          <ElTableColumn prop="name" label="名称" />
        </ElTable>
        <ElButton class="mt" type="primary" v-can="'ROLE:WRITE'" @click="openModal">新增角色</ElButton>
      </ElCol>
      <ElCol :span="14">
        <ElCard>
          <h3>权限点配置</h3>
          <ElAlert v-if="!selectedRole" title="请选择左侧角色" type="info" show-icon />
          <ElCheckboxGroup v-else v-model="selectedPerms">
            <ElCheckbox v-for="item in permissionTree" :key="item.code" :label="item.code">{{ item.label }}</ElCheckbox>
          </ElCheckboxGroup>
          <ElButton class="mt" type="primary" :disabled="!selectedRole" @click="savePerms">保存权限</ElButton>
        </ElCard>
      </ElCol>
    </ElRow>

    <ElDialog v-model="visible" title="角色">
      <ElForm :model="current" label-width="80px">
        <ElFormItem label="编码">
          <ElInput v-model="current.code" />
        </ElFormItem>
        <ElFormItem label="名称">
          <ElInput v-model="current.name" />
        </ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton @click="visible = false">取消</ElButton>
        <ElButton type="primary" @click="saveRoleData">保存</ElButton>
      </template>
    </ElDialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { fetchRoles, saveRole, updateRolePermissions, fetchRolePermissions } from '@/api/admin';

const roles = ref<any[]>([]);
const current = reactive<any>({ code: '', name: '' });
const visible = ref(false);
const selectedRole = ref<any>(null);
const selectedPerms = ref<string[]>([]);

const permissionTree = [
  { code: 'ADMIN:READ', label: '管理员查看' },
  { code: 'ADMIN:EDIT', label: '管理员编辑' },
  { code: 'ROLE:READ', label: '角色查看' },
  { code: 'ROLE:WRITE', label: '角色维护' },
  { code: 'USER:READ', label: '用户查看' },
  { code: 'USER:DISABLE', label: '用户禁用' },
  { code: 'NEWS:READ', label: '新闻模块' },
  { code: 'NOTICE:READ', label: '通知模块' },
  { code: 'SCENIC:READ', label: '景区模块' },
  { code: 'VENUE:READ', label: '场馆模块' },
  { code: 'ACTIVITY:READ', label: '活动模块' },
  { code: 'ACTIVITY:AUDIT', label: '活动审核' },
  { code: 'FILE:WRITE', label: '文件上传' },
  { code: 'MONITOR:READ', label: '监控' },
  { code: 'LOG:READ', label: '操作日志' }
];

onMounted(async () => {
  const res: any = await fetchRoles();
  roles.value = Array.isArray(res) ? res : res.records || [];
});

function selectRole(role: any) {
  selectedRole.value = role;
  loadPerms(role.code);
}

async function loadPerms(code: string) {
  const res = await fetchRolePermissions(code);
  selectedPerms.value = res.permissions || [];
}

function openModal() {
  Object.assign(current, { code: '', name: '' });
  visible.value = true;
}

async function saveRoleData() {
  await saveRole(current);
  ElMessage.success('角色已保存');
  visible.value = false;
}

async function savePerms() {
  if (!selectedRole.value) return;
  await updateRolePermissions(selectedRole.value.code, selectedPerms.value);
  ElMessage.success('权限已更新');
}
</script>

<style scoped>
.mt {
  margin-top: 12px;
}
</style>
