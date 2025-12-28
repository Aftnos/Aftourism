<template>
  <div class="art-full-height">
    <ArtSearchBar
      ref="searchBarRef"
      class="mb-4"
      v-model="searchForm"
      :items="searchItems"
      :is-expand="false"
      :show-reset-button="true"
      :show-search-button="true"
      @search="handleSearch"
      @reset="handleReset"
    />

    <ElCard shadow="never" class="art-table-card">
      <template #header>管理员操作日志</template>
      <ArtTableHeader v-model:columns="columnChecks" :loading="loading" @refresh="refreshData" />

      <ArtTable
        :loading="loading"
        :data="data"
        :columns="columns"
        :pagination="pagination"
        @pagination:size-change="handleSizeChange"
        @pagination:current-change="handleCurrentChange"
      >
        <template #successFlag="{ row }">
          <ElTag :type="row.successFlag ? 'success' : 'danger'">
            {{ row.successFlag ? '成功' : '失败' }}
          </ElTag>
        </template>
        <template #operationName="{ row }">
          <span class="truncate" :title="row.operationName">{{ row.operationName || '-' }}</span>
        </template>
        <template #moduleName="{ row }">
          <span class="truncate" :title="row.moduleName">{{ row.moduleName || '-' }}</span>
        </template>
        <template #requestUri="{ row }">
          <span class="truncate" :title="row.requestUri">{{ row.requestUri || '-' }}</span>
        </template>
        <template #errorMsg="{ row }">
          <span class="truncate" :title="row.errorMsg">{{ row.errorMsg || '-' }}</span>
        </template>
      </ArtTable>
    </ElCard>
  </div>
</template>

<script setup lang="ts">
  import { computed, ref } from 'vue'
  import {
    ElCard,
    ElTag
  } from 'element-plus'
  import ArtSearchBar from '@/components/core/forms/art-search-bar/index.vue'
  import ArtTableHeader from '@/components/core/tables/art-table-header/index.vue'
  import ArtTable from '@/components/core/tables/art-table/index.vue'
  import { useTable } from '@/hooks/core/useTable'
  import { fetchOperationLogs } from '@/api/system-backend'

  defineOptions({ name: 'BackendLog' })

  const searchBarRef = ref()
  const searchForm = ref<{
    operatorType?: Api.SystemBackend.OperatorType | ''
    moduleName?: string
    operationName?: string
    successFlag?: '' | boolean
    daterange?: string[]
  }>({
    operatorType: 'ADMIN',
    moduleName: '',
    operationName: '',
    successFlag: '',
    daterange: undefined
  })

  const searchItems = computed(() => [
    {
      key: 'operatorType',
      label: '操作类型',
      type: 'select',
      props: {
        placeholder: '全部类型',
        clearable: true,
        options: [
          { label: '全部', value: '' },
          { label: '管理员', value: 'ADMIN' },
          { label: '超级管理员', value: 'SUPER_ADMIN' },
          { label: '门户用户', value: 'PORTAL_USER' }
        ]
      }
    },
    {
      key: 'moduleName',
      label: '模块名称',
      type: 'input',
      props: { placeholder: '例如: admin/system', clearable: true }
    },
    {
      key: 'operationName',
      label: '操作名称',
      type: 'input',
      props: { placeholder: '例如: 新增/修改', clearable: true }
    },
    {
      key: 'successFlag',
      label: '执行结果',
      type: 'select',
      props: {
        placeholder: '全部状态',
        clearable: true,
        options: [
          { label: '全部', value: '' },
          { label: '成功', value: true },
          { label: '失败', value: false }
        ]
      }
    },
    {
      key: 'daterange',
      label: '操作时间',
      type: 'datetimerange',
      props: {
        type: 'datetimerange',
        valueFormat: 'YYYY-MM-DD HH:mm:ss',
        startPlaceholder: '开始时间',
        endPlaceholder: '结束时间'
      }
    }
  ])

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
    refreshData
  } = useTable({
    core: {
      apiFn: fetchOperationLogs,
      apiParams: {
        current: 1,
        size: 10
      },
      excludeParams: ['daterange'],
      columnsFactory: () => [
        { type: 'globalIndex', width: 60, label: '序号' },
        { prop: 'operatorType', label: '操作人类型', width: 130 },
        { prop: 'operatorId', label: '操作人ID', width: 100 },
        { prop: 'moduleName', label: '模块', minWidth: 140, useSlot: true },
        { prop: 'operationName', label: '操作动作', minWidth: 120, useSlot: true },
        { prop: 'requestMethod', label: '方法', width: 90 },
        { prop: 'requestUri', label: '请求地址', minWidth: 180, useSlot: true },
        { prop: 'successFlag', label: '结果', width: 90, useSlot: true },
        { prop: 'costMs', label: '耗时(ms)', width: 100 },
        { prop: 'ipAddress', label: 'IP地址', width: 140 },
        { prop: 'errorMsg', label: '异常信息', minWidth: 160, useSlot: true },
        { prop: 'createTime', label: '操作时间', width: 170 }
      ]
    }
  })

  const handleSearch = async () => {
    await searchBarRef.value?.validate?.()
    const { daterange, operatorType, moduleName, operationName, successFlag } = searchForm.value
    const [startTime, endTime] = Array.isArray(daterange) ? daterange : [null, null]
    const typeValue = operatorType || undefined
    const resultValue = successFlag === '' ? undefined : successFlag
    Object.assign(searchParams, {
      operatorType: typeValue,
      moduleName,
      operationName,
      successFlag: resultValue,
      startTime,
      endTime
    })
    getData()
  }

  const handleReset = () => {
    resetSearchParams()
  }
</script>

<style scoped>
/* 
  使用 art-full-height + art-table-card 可以实现表格自适应高度 
  如果需要单独滚动，art-full-height 已经提供了基础容器
*/
</style>
