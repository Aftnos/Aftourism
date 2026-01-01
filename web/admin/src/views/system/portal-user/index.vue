<!-- 普通用户管理页面 -->
<!-- art-full-height 自动计算出页面剩余高度 -->
<!-- art-table-card 一个符合系统样式的 class，同时自动撑满剩余高度 -->
<template>
  <div class="portal-user-page art-full-height">
    <!-- 搜索栏 -->
    <PortalUserSearch v-model="searchForm" @search="handleSearch" @reset="resetSearchParams" />

    <ElCard class="art-table-card" shadow="never">
      <!-- 表格头部 -->
      <ArtTableHeader v-model:columns="columnChecks" :loading="loading" @refresh="refreshData">
        <template #left>
          <ElSpace wrap>
            <ElButton @click="refreshData" v-ripple>刷新列表</ElButton>
          </ElSpace>
        </template>
      </ArtTableHeader>

      <!-- 表格 -->
      <ArtTable
        :loading="loading"
        :data="data"
        :columns="columns"
        :pagination="pagination"
        @selection-change="handleSelectionChange"
        @pagination:size-change="handleSizeChange"
        @pagination:current-change="handleCurrentChange"
      />

      <!-- 普通用户编辑弹窗 -->
      <PortalUserDialog
        v-model:visible="dialogVisible"
        :user-data="currentUserData"
        @success="handleDialogSubmit"
      />
    </ElCard>
  </div>
</template>

<script setup lang="ts">
  import ArtButtonTable from '@/components/core/forms/art-button-table/index.vue'
  import { useTable } from '@/hooks/core/useTable'
  import { fetchGetPortalUserPage } from '@/api/system-manage'
  import PortalUserSearch from './modules/portal-user-search.vue'
  import PortalUserDialog from './modules/portal-user-dialog.vue'
  import { ElTag } from 'element-plus'
  import { h } from 'vue'

  defineOptions({ name: 'PortalUser' })

  type PortalUserItem = Api.SystemManage.PortalUserItem

  // 弹窗相关
  const dialogVisible = ref(false)
  const currentUserData = ref<Partial<PortalUserItem>>({})

  // 选中行
  const selectedRows = ref<PortalUserItem[]>([])

  // 搜索表单
  const searchForm = ref({
    username: undefined,
    nickname: undefined,
    status: undefined
  })

  // 用户状态配置
  const USER_STATUS_CONFIG = {
    1: { type: 'success' as const, text: '启用' },
    0: { type: 'danger' as const, text: '禁用' }
  } as const

  /**
   * 获取用户状态配置
   */
  const getUserStatusConfig = (status: number) => {
    return USER_STATUS_CONFIG[status as keyof typeof USER_STATUS_CONFIG] || {
      type: 'info' as const,
      text: '未知'
    }
  }

  const {
    columns,
    columnChecks,
    data,
    loading,
    pagination,
    getData,
    searchParams,
    resetSearchParams,
    handleSizeChange,
    handleCurrentChange,
    refreshData,
    refreshUpdate
  } = useTable({
    // 核心配置
    core: {
      apiFn: fetchGetPortalUserPage,
      apiParams: {
        current: 1,
        size: 20,
        ...searchForm.value
      },
      columnsFactory: () => [
        { type: 'selection' },
        { type: 'index', width: 60, label: '序号' },
        {
          prop: 'username',
          label: '账号',
          minWidth: 160
        },
        {
          prop: 'nickname',
          label: '昵称',
          minWidth: 140
        },
        { prop: 'phone', label: '手机号', minWidth: 140 },
        { prop: 'email', label: '邮箱', minWidth: 180 },
        {
          prop: 'roleCode',
          label: '角色编码',
          minWidth: 140,
          formatter: (row) => row.roleCode || '-'
        },
        {
          prop: 'status',
          label: '状态',
          formatter: (row) => {
            const statusConfig = getUserStatusConfig(row.status)
            return h(ElTag, { type: statusConfig.type }, () => statusConfig.text)
          }
        },
        {
          prop: 'createTime',
          label: '创建日期',
          sortable: true
        },
        {
          prop: 'updateTime',
          label: '更新日期',
          sortable: true
        },
        {
          prop: 'operation',
          label: '操作',
          width: 120,
          fixed: 'right',
          formatter: (row) =>
            h('div', [
              h(ArtButtonTable, {
                type: 'edit',
                onClick: () => showDialog(row)
              })
            ])
        }
      ]
    },
    transform: {
      // 后端返回 PageInfo 结构，这里转换为标准分页结构
      responseAdapter: (response: Api.SystemManage.PortalUserPage) => ({
        records: response.list || [],
        total: response.total || 0,
        current: response.pageNum || 1,
        size: response.pageSize || 10
      })
    }
  })

  /**
   * 搜索处理
   * @param params 参数
   */
  const handleSearch = (params: Record<string, any>) => {
    // 搜索参数赋值
    Object.assign(searchParams, params)
    getData()
  }

  /**
   * 显示编辑弹窗
   */
  const showDialog = (row?: PortalUserItem): void => {
    currentUserData.value = row || {}
    nextTick(() => {
      dialogVisible.value = true
    })
  }

  /**
   * 处理弹窗提交事件
   */
  const handleDialogSubmit = async () => {
    dialogVisible.value = false
    currentUserData.value = {}
    refreshUpdate()
  }

  /**
   * 处理表格行选择变化
   */
  const handleSelectionChange = (selection: PortalUserItem[]): void => {
    selectedRows.value = selection
  }
</script>
