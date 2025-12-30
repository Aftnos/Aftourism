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
      <ElButton @click="outputSelectedData" style="margin-left: 8px">获取选中数据</ElButton>

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
  const roleCode = ref('')
  const permissionCatalog = ref<Api.SystemManage.PermissionDefinition[]>([])
  const rolePermissions = ref<Api.SystemManage.RolePermissionSummary[]>([])
  const dialogType = computed(() => props.dialogType)

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
    permissionCatalog.value.forEach((permission) => {
      const { resourceKey, action, description } = permission
      if (!grouped.has(resourceKey)) {
        grouped.set(resourceKey, {
          name: resourceKey,
          label: resourceKey,
          children: []
        })
      }
      const parent = grouped.get(resourceKey)
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

  const allActionKeys = computed(() =>
    permissionCatalog.value.map((permission) => `${permission.resourceKey}:${permission.action}`)
  )

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
   * 监听弹窗打开，初始化权限数据
   */
  watch(
    () => props.modelValue,
    async (newVal) => {
      if (!newVal) return
      await initPermissionData()
      roleCode.value = props.dialogType === 'add' ? '' : props.roleData?.roleCode || ''

      const matched = props.roleData?.roleCode
        ? rolePermissions.value.find((item) => item.roleCode === props.roleData?.roleCode)
        : undefined
      const checkedKeys = (matched?.permissions || [])
        .filter((permission) => permission.allow)
        .map((permission) => `${permission.resourceKey}:${permission.action}`)

      nextTick(() => {
        treeRef.value?.setCheckedKeys(checkedKeys)
        isSelectAll.value = checkedKeys.length === allActionKeys.value.length && checkedKeys.length > 0
      })
    }
  )

  /**
   * 关闭弹窗并清空选中状态
   */
  const handleClose = () => {
    visible.value = false
    treeRef.value?.setCheckedKeys([])
    roleCode.value = ''
    isSelectAll.value = false
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

  /**
   * 输出选中的权限数据到控制台
   * 用于调试和查看当前选中的权限配置
   */
  const outputSelectedData = () => {
    const tree = treeRef.value
    if (!tree) return

    const selectedData = {
      checkedKeys: tree.getCheckedKeys(),
      halfCheckedKeys: tree.getHalfCheckedKeys(),
      checkedNodes: tree.getCheckedNodes(),
      halfCheckedNodes: tree.getHalfCheckedNodes(),
      totalChecked: tree.getCheckedKeys().length,
      totalHalfChecked: tree.getHalfCheckedKeys().length
    }

    console.log('=== 选中的权限数据 ===', selectedData)
    ElMessage.success(`已输出选中数据到控制台，共选中 ${selectedData.totalChecked} 个节点`)
  }
</script>
