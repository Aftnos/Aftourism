<template>
  <ElDialog
    v-model="visible"
    title="角色权限"
    width="520px"
    align-center
    class="el-dialog-border"
    @close="handleClose"
  >
  <ElForm v-if="dialogType === 'add'" class="mb-4">
    <ElFormItem label="角色编码" required>
      <ElInput v-model="roleCode" placeholder="请输入角色编码" />
    </ElFormItem>
    <ElFormItem label="角色类型" required>
      <ElRadioGroup v-model="roleScope">
        <ElRadioButton label="admin">后台管理</ElRadioButton>
        <ElRadioButton label="portal">前台用户</ElRadioButton>
      </ElRadioGroup>
    </ElFormItem>
    <ElFormItem label="角色描述">
      <ElInput v-model="remark" placeholder="请输入角色描述" />
    </ElFormItem>
  </ElForm>
  <ElForm v-else class="mb-4">
    <ElFormItem label="角色类型">
      <ElRadioGroup v-model="roleScope">
        <ElRadioButton label="admin">后台管理</ElRadioButton>
        <ElRadioButton label="portal">前台用户</ElRadioButton>
      </ElRadioGroup>
    </ElFormItem>
  </ElForm>
  <ElScrollbar height="70vh">
      <ElTree
        ref="treeRef"
        :data="permissionTree"
        show-checkbox
        node-key="name"
        :default-expand-all="isExpandAll"
        :props="defaultProps"
        @check="handleTreeCheck"
      >
        <template #default="{ data }">
          <div style="display: flex; align-items: center">
            <span>{{ data.label }}</span>
          </div>
        </template>
      </ElTree>
    </ElScrollbar>
    <template #footer>
      <ElButton @click="toggleExpandAll">{{ isExpandAll ? '全部收起' : '全部展开' }}</ElButton>
      <ElButton @click="toggleSelectAll" style="margin-left: 8px">{{
        isSelectAll ? '取消全选' : '全部选择'
      }}</ElButton>
      <ElButton type="primary" @click="savePermission">保存</ElButton>
    </template>
  </ElDialog>
</template>

<script setup lang="ts">
  import {
    fetchGetPermissionCatalog,
    fetchGetRolePermissions,
    fetchSaveRolePermissions
  } from '@/api/system-manage'
  import {
    portalPermissionCatalog,
    portalPermissionResourceSet,
    type PortalPermissionDefinition
  } from '@/config/portal-permissions'

  type RoleListItem = Api.SystemManage.RoleListItem

  interface Props {
    modelValue: boolean
    dialogType: 'add' | 'edit'
    roleData?: RoleListItem
  }

  interface Emits {
    (e: 'update:modelValue', value: boolean): void
    (e: 'success'): void
  }

  const props = withDefaults(defineProps<Props>(), {
    modelValue: false,
    dialogType: 'edit',
    roleData: undefined
  })

  const emit = defineEmits<Emits>()

  const treeRef = ref()
  const isExpandAll = ref(true)
  const isSelectAll = ref(false)
  const roleScope = ref<'admin' | 'portal'>('admin')
  const roleCode = ref('')
  const remark = ref('')
  const permissionCatalog = ref<Api.SystemManage.PermissionDefinition[]>([])
  const rolePermissions = ref<Api.SystemManage.RolePermissionSummary[]>([])
  const dialogType = computed(() => props.dialogType)
  const portalCatalog = ref<PortalPermissionDefinition[]>(portalPermissionCatalog)

  /**
   * 弹窗显示状态双向绑定
   */
  const visible = computed({
    get: () => props.modelValue,
    set: (value) => emit('update:modelValue', value)
  })

  /**
   * 菜单节点类型
   */
  interface PermissionNode {
    name: string
    label: string
    children?: PermissionNode[]
    isAction?: boolean
    resourceKey?: string
    action?: string
  }

  /**
   * 权限树结构
   */
  const permissionTree = computed<PermissionNode[]>(() => {
    const grouped = new Map<string, PermissionNode>()
    currentPermissionCatalog.value.forEach((permission) => {
      const { resourceKey, action, description } = permission
      const groupKey = getPermissionGroupKey(permission)
      const groupLabel = getPermissionGroupLabel(permission)
      if (!grouped.has(groupKey)) {
        grouped.set(groupKey, {
          name: groupKey,
          label: groupLabel,
          children: []
        })
      }
      const parent = grouped.get(groupKey)
      parent?.children?.push({
        name: `${resourceKey}:${action}`,
        label: description || `${resourceKey}:${action}`,
        isAction: true,
        resourceKey,
        action
      })
    })
    return Array.from(grouped.values())
  })

  const currentPermissionCatalog = computed<Api.SystemManage.PermissionDefinition[]>(() =>
    roleScope.value === 'portal' ? portalCatalog.value : permissionCatalog.value
  )

  const allActionKeys = computed(() =>
    currentPermissionCatalog.value.map((permission) => `${permission.resourceKey}:${permission.action}`)
  )

  /**
   * 获取权限分组键
   */
  const getPermissionGroupKey = (permission: Api.SystemManage.PermissionDefinition) => {
    if ('groupKey' in permission && permission.groupKey) {
      return permission.groupKey
    }
    return permission.resourceKey
  }

  /**
   * 获取权限分组名称
   */
  const getPermissionGroupLabel = (permission: Api.SystemManage.PermissionDefinition) => {
    if ('groupLabel' in permission && permission.groupLabel) {
      return permission.groupLabel
    }
    return permission.resourceKey
  }

  /**
   * 树形组件配置
   */
  const defaultProps = {
    children: 'children',
    label: (data: PermissionNode) => data.label || ''
  }

  /**
   * 初始化权限目录与角色权限
   */
  const initPermissionData = async () => {
    if (!permissionCatalog.value.length) {
      permissionCatalog.value = await fetchGetPermissionCatalog()
    }
    rolePermissions.value = await fetchGetRolePermissions()
  }

  /**
   * 根据角色权限判断角色类型
   */
  const resolveRoleScope = (permissions: Api.SystemManage.RolePermissionItem[]) => {
    return permissions.some((permission) => portalPermissionResourceSet.has(permission.resourceKey))
      ? 'portal'
      : 'admin'
  }

  /**
   * 获取当前角色在指定权限域下的选中权限键
   */
  const getCheckedKeysByScope = (
    permissions: Api.SystemManage.RolePermissionItem[],
    scope: 'admin' | 'portal'
  ) => {
    const filtered = permissions.filter((permission) => permission.allow)
    const scoped = filtered.filter((permission) =>
      scope === 'portal'
        ? portalPermissionResourceSet.has(permission.resourceKey)
        : !portalPermissionResourceSet.has(permission.resourceKey)
    )
    return scoped.map((permission) => `${permission.resourceKey}:${permission.action}`)
  }

  /**
   * 同步树的选中项
   */
  const applyCheckedKeys = (permissions: Api.SystemManage.RolePermissionItem[]) => {
    const checkedKeys = getCheckedKeysByScope(permissions, roleScope.value)
    nextTick(() => {
      treeRef.value?.setCheckedKeys(checkedKeys)
      isSelectAll.value = checkedKeys.length === allActionKeys.value.length && checkedKeys.length > 0
    })
  }

  /**
   * 监听弹窗打开，初始化权限数据
   */
  watch(
    () => props.modelValue,
    async (newVal) => {
      if (!newVal) return
      await initPermissionData()
      roleCode.value = props.dialogType === 'add' ? '' : props.roleData?.roleCode || ''
      remark.value = ''

      const matched = props.roleData?.roleCode
        ? rolePermissions.value.find((item) => item.roleCode === props.roleData?.roleCode)
        : undefined
      const matchedPermissions = matched?.permissions || []
      roleScope.value = resolveRoleScope(matchedPermissions)
      applyCheckedKeys(matchedPermissions)
    }
  )

  /**
   * 监听角色类型切换，刷新树选中状态
   */
  watch(
    () => roleScope.value,
    () => {
      if (!props.modelValue) return
      const matched = props.roleData?.roleCode
        ? rolePermissions.value.find((item) => item.roleCode === props.roleData?.roleCode)
        : undefined
      applyCheckedKeys(matched?.permissions || [])
    }
  )

  /**
   * 关闭弹窗并清空选中状态
   */
  const handleClose = () => {
    visible.value = false
    treeRef.value?.setCheckedKeys([])
    roleCode.value = ''
    remark.value = ''
    isSelectAll.value = false
    roleScope.value = 'admin'
  }

  /**
   * 保存权限配置
   */
  const savePermission = async () => {
    const currentRoleCode = props.dialogType === 'add' ? roleCode.value : props.roleData?.roleCode
    if (!currentRoleCode) {
      ElMessage.warning('请先输入角色编码')
      return
    }

    const tree = treeRef.value
    if (!tree) return

    const selectedKeys = tree.getCheckedKeys().filter((key: string) => key.includes(':'))
    const permissions = selectedKeys.map((key: string) => {
      const [resourceKey, action] = key.split(':')
      return {
        resourceKey,
        action,
        allow: true,
        remark: ''
      }
    })

    if (!permissions.length) {
      ElMessage.warning('请至少选择一个权限点')
      return
    }

    await fetchSaveRolePermissions({
      roleCode: currentRoleCode,
      remark: props.dialogType === 'add' ? remark.value : undefined,
      permissions
    })
    ElMessage.success('权限保存成功')
    emit('success')
    handleClose()
  }

  /**
   * 切换全部展开/收起状态
   */
  const toggleExpandAll = () => {
    const tree = treeRef.value
    if (!tree) return

    const nodes = tree.store.nodesMap
    // 这里保留 any，因为 Element Plus 的内部节点类型较复杂
    Object.values(nodes).forEach((node: any) => {
      node.expanded = !isExpandAll.value
    })

    isExpandAll.value = !isExpandAll.value
  }

  /**
   * 切换全选/取消全选状态
   */
  const toggleSelectAll = () => {
    const tree = treeRef.value
    if (!tree) return

    if (!isSelectAll.value) {
      tree.setCheckedKeys(allActionKeys.value)
    } else {
      tree.setCheckedKeys([])
    }

    isSelectAll.value = !isSelectAll.value
  }

  /**
   * 处理树节点选中状态变化
   * 同步更新全选按钮状态
   */
  const handleTreeCheck = () => {
    const tree = treeRef.value
    if (!tree) return

    const checkedKeys = tree.getCheckedKeys().filter((key: string) => key.includes(':'))
    isSelectAll.value =
      checkedKeys.length === allActionKeys.value.length && allActionKeys.value.length > 0
  }
</script>
