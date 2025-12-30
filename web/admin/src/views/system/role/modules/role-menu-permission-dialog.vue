<template>
  <ElDialog
    v-model="visible"
    title="菜单与按钮权限"
    width="640px"
    align-center
    class="el-dialog-border"
    @close="handleClose"
  >
    <ElAlert
      v-if="roleLabel"
      type="info"
      :closable="false"
      class="mb-3"
      :title="`当前角色：${roleLabel}`"
    />
    <ElScrollbar height="70vh">
      <ElTree
        ref="treeRef"
        :data="menuTree"
        show-checkbox
        node-key="key"
        :default-expand-all="isExpandAll"
        :props="defaultProps"
        @check="handleTreeCheck"
      >
        <template #default="{ data }">
          <div class="role-menu-node">
            <span>{{ data.label }}</span>
            <ElTag v-if="data.type === 'permission'" size="small" type="info" class="ml-2">
              按钮
            </ElTag>
          </div>
        </template>
      </ElTree>
    </ElScrollbar>
    <template #footer>
      <ElButton @click="toggleExpandAll">{{ isExpandAll ? '全部收起' : '全部展开' }}</ElButton>
      <ElButton @click="toggleSelectAll" style="margin-left: 8px">{{
        isSelectAll ? '取消全选' : '全部选择'
      }}</ElButton>
      <ElButton type="primary" @click="saveAssignments">保存</ElButton>
    </template>
  </ElDialog>
</template>

<script setup lang="ts">
  import type { AppRouteRecord } from '@/types/router'
  import {
    fetchGetMenuManageList,
    fetchGetRoleMenuIds,
    fetchGetRoleMenuPermissionIds,
    fetchSaveRoleMenus,
    fetchSaveRoleMenuPermissions
  } from '@/api/system-manage'

  type RoleListItem = Api.SystemManage.RoleListItem

  interface Props {
    modelValue: boolean
    roleData?: RoleListItem
  }

  interface Emits {
    (e: 'update:modelValue', value: boolean): void
    (e: 'success'): void
  }

  interface MenuTreeNode {
    key: string
    label: string
    type: 'menu' | 'permission'
    menuId?: number
    permissionId?: number
    parentMenuId?: number
    children?: MenuTreeNode[]
  }

  const props = withDefaults(defineProps<Props>(), {
    modelValue: false,
    roleData: undefined
  })

  const emit = defineEmits<Emits>()

  const treeRef = ref()
  const isExpandAll = ref(true)
  const isSelectAll = ref(false)
  const menuTree = ref<MenuTreeNode[]>([])
  const permissionParentMap = ref(new Map<number, number>())

  // 角色展示文本
  const roleLabel = computed(() => {
    if (!props.roleData) return ''
    return `${props.roleData.roleName}（${props.roleData.roleCode}）`
  })

  const visible = computed({
    get: () => props.modelValue,
    set: (value) => emit('update:modelValue', value)
  })

  const defaultProps = {
    children: 'children',
    label: (data: MenuTreeNode) => data.label || ''
  }

  // 全量节点 key 列表，便于全选与状态判断
  const allNodeKeys = computed(() => {
    const keys: string[] = []
    const walk = (nodes: MenuTreeNode[]) => {
      nodes.forEach((node) => {
        keys.push(node.key)
        if (node.children?.length) {
          walk(node.children)
        }
      })
    }
    walk(menuTree.value)
    return keys
  })

  // 构建菜单 + 按钮权限树结构
  const buildMenuTree = (routes: AppRouteRecord[]) => {
    const parentMap = new Map<number, number>()
    const buildNodes = (items: AppRouteRecord[]): MenuTreeNode[] =>
      items.map((item) => {
        const menuId = item.id
        const title = item.meta?.title || item.name || '未命名菜单'
        const children: MenuTreeNode[] = []

        if (item.children?.length) {
          children.push(...buildNodes(item.children))
        }

        if (item.meta?.authList?.length) {
          item.meta.authList.forEach((permission) => {
            if (!permission.id) return
            parentMap.set(permission.id, menuId as number)
            children.push({
              key: `permission-${permission.id}`,
              label: permission.title,
              type: 'permission',
              permissionId: permission.id,
              parentMenuId: menuId
            })
          })
        }

        return {
          key: `menu-${menuId}`,
          label: title,
          type: 'menu',
          menuId: menuId as number,
          children
        }
      })

    permissionParentMap.value = parentMap
    menuTree.value = buildNodes(routes)
  }

  // 初始化菜单树与角色已选数据
  const initData = async () => {
    const roleCode = props.roleData?.roleCode
    if (!roleCode) {
      ElMessage.warning('请先选择角色')
      visible.value = false
      return
    }

    const menus = await fetchGetMenuManageList()
    buildMenuTree(menus)

    const [menuIds, permissionIds] = await Promise.all([
      fetchGetRoleMenuIds(roleCode),
      fetchGetRoleMenuPermissionIds(roleCode)
    ])

    const checkedKeys = [
      ...menuIds.map((id) => `menu-${id}`),
      ...permissionIds.map((id) => `permission-${id}`)
    ]

    nextTick(() => {
      treeRef.value?.setCheckedKeys(checkedKeys)
      isSelectAll.value =
        checkedKeys.length === allNodeKeys.value.length && allNodeKeys.value.length > 0
    })
  }

  watch(
    () => props.modelValue,
    async (open) => {
      if (!open) return
      await initData()
    }
  )

  const handleClose = () => {
    visible.value = false
    treeRef.value?.setCheckedKeys([])
    isSelectAll.value = false
  }

  // 切换全部展开/收起状态
  const toggleExpandAll = () => {
    const tree = treeRef.value
    if (!tree) return

    const nodes = tree.store.nodesMap
    Object.values(nodes).forEach((node: any) => {
      node.expanded = !isExpandAll.value
    })

    isExpandAll.value = !isExpandAll.value
  }

  // 切换全选/取消全选状态
  const toggleSelectAll = () => {
    const tree = treeRef.value
    if (!tree) return

    if (!isSelectAll.value) {
      tree.setCheckedKeys(allNodeKeys.value)
    } else {
      tree.setCheckedKeys([])
    }

    isSelectAll.value = !isSelectAll.value
  }

  // 勾选状态变化时同步全选按钮状态
  const handleTreeCheck = () => {
    const tree = treeRef.value
    if (!tree) return

    const checkedKeys = tree.getCheckedKeys() as string[]
    isSelectAll.value =
      checkedKeys.length === allNodeKeys.value.length && allNodeKeys.value.length > 0
  }

  // 保存菜单与按钮权限分配
  const saveAssignments = async () => {
    const roleCode = props.roleData?.roleCode
    if (!roleCode) {
      ElMessage.warning('请先选择角色')
      return
    }

    const tree = treeRef.value
    if (!tree) return

    const checkedKeys = tree.getCheckedKeys() as string[]
    const halfCheckedKeys = tree.getHalfCheckedKeys() as string[]
    const menuIds = new Set<number>()
    const permissionIds = new Set<number>()

    const collectMenuId = (key: string) => {
      if (!key.startsWith('menu-')) return
      const id = Number(key.replace('menu-', ''))
      if (!Number.isNaN(id)) {
        menuIds.add(id)
      }
    }

    const collectPermissionId = (key: string) => {
      if (!key.startsWith('permission-')) return
      const id = Number(key.replace('permission-', ''))
      if (!Number.isNaN(id)) {
        permissionIds.add(id)
        const parentMenuId = permissionParentMap.value.get(id)
        if (parentMenuId) {
          menuIds.add(parentMenuId)
        }
      }
    }

    checkedKeys.forEach((key) => {
      collectMenuId(key)
      collectPermissionId(key)
    })

    halfCheckedKeys.forEach((key) => {
      collectMenuId(key)
    })

    await Promise.all([
      fetchSaveRoleMenus(roleCode, { menuIds: Array.from(menuIds) }),
      fetchSaveRoleMenuPermissions(roleCode, { permissionIds: Array.from(permissionIds) })
    ])

    ElMessage.success('菜单与按钮权限已保存')
    emit('success')
    handleClose()
  }
</script>

<style scoped lang="scss">
  .role-menu-node {
    display: flex;
    align-items: center;
  }
</style>
