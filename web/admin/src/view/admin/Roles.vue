<template>
  <div class="page-card">
    <ElForm inline class="mb">
      <ElFormItem label="选择角色">
        <ElSelect v-model="currentRole" placeholder="请选择">
          <ElOption v-for="role in roleList" :key="role.roleCode" :label="role.roleCode" :value="role.roleCode" />
        </ElSelect>
      </ElFormItem>
      <ElFormItem>
        <ElButton type="primary" v-can="'ROLE_ACCESS:UPDATE'" :disabled="!currentRole" @click="save">保存权限</ElButton>
      </ElFormItem>
    </ElForm>

    <ElAlert type="info" show-icon title="角色权限说明" class="mb">
      选中即代表允许对应资源-动作，超级管理员默认拥有所有权限。
    </ElAlert>

    <ElTable :data="catalog" border>
      <ElTableColumn prop="resourceKey" label="资源" width="160" />
      <ElTableColumn prop="action" label="动作" width="160" />
      <ElTableColumn label="描述">
        <template #default="{ row }">
          {{ row.description }}
        </template>
      </ElTableColumn>
      <ElTableColumn label="允许">
        <template #default="{ row }">
          <ElCheckbox
            :model-value="rolePermissionMap[currentRole]?.includes(row.key)"
            :disabled="!canEdit"
            @change="(val: boolean) => togglePermission(row.key, val as boolean)"
          >
            <span>{{ row.key }}</span>
          </ElCheckbox>
        </template>
      </ElTableColumn>
    </ElTable>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { fetchRoles, fetchPermissionCatalog, saveRolePermissions, type RoleSummary, type PermissionDefinition } from '@/api/admin';
import { useAuthStore } from '@/store/auth';
import { createTraceId } from '@/utils/trace';

const auth = useAuthStore();
const roleList = ref<RoleSummary[]>([]);
const catalog = ref<PermissionDefinition[]>([]);
const currentRole = ref('');
const rolePermissionMap = ref<Record<string, string[]>>({});

const canEdit = computed(() => auth.allow('ROLE_ACCESS:UPDATE'));

function togglePermission(key: string, enable: boolean) {
  const roleCode = currentRole.value;
  if (!roleCode) return;
  const list = rolePermissionMap.value[roleCode] ? [...rolePermissionMap.value[roleCode]] : [];
  if (enable && !list.includes(key)) {
    list.push(key);
  } else if (!enable) {
    const idx = list.indexOf(key);
    if (idx !== -1) list.splice(idx, 1);
  }
  rolePermissionMap.value[roleCode] = list;
}

async function save() {
  if (!currentRole.value) {
    return ElMessage.warning('请先选择角色');
  }
  const permissions = (rolePermissionMap.value[currentRole.value] || []).map((key) => {
    const [resourceKey, action] = key.split(':');
    return { resourceKey, action, allow: true };
  });
  const traceId = createTraceId('role');
  console.info('操作摘要', { traceId, action: 'saveRolePermissions', params: { roleCode: currentRole.value, permissions } });
  await saveRolePermissions({ roleCode: currentRole.value, permissions });
  ElMessage.success('权限已更新');
}

async function loadData() {
  const [roles, perms] = await Promise.all([fetchRoles(), fetchPermissionCatalog()]);
  roleList.value = roles;
  catalog.value = perms;
  rolePermissionMap.value = roles.reduce((acc, role) => {
    acc[role.roleCode] = role.permissions.filter((p) => p.allow).map((p) => `${p.resourceKey}:${p.action}`);
    return acc;
  }, {} as Record<string, string[]>);
  currentRole.value = roles[0]?.roleCode || '';
}

onMounted(loadData);
</script>

<style scoped>
.mb {
  margin-bottom: 16px;
}
</style>
