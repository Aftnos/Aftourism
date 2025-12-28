<template>
  <div class="art-full-height">
    <ArtSearchBar
      ref="searchBarRef"
      v-model="searchForm"
      :items="searchItems"
      :is-expand="false"
      :show-reset-button="true"
      :show-search-button="true"
      @search="handleSearch"
      @reset="handleReset"
    />

    <ElCard class="art-table-card" shadow="never">
      <ArtTableHeader v-model:columns="columnChecks" :loading="loading" @refresh="refreshData" />

      <ArtTable
        :loading="loading"
        :data="data"
        :columns="columns"
        :pagination="pagination"
        @pagination:size-change="handleSizeChange"
        @pagination:current-change="handleCurrentChange"
      >
        <template #type="{ row }">
          <ElTag type="info">{{ typeLabelMap[row.type] }}</ElTag>
        </template>

        <template #operation="{ row }">
          <div class="flex">
            <ArtButtonTable
              icon="ri:refresh-line"
              icon-class="bg-success/12 text-success"
              @click="handleRestore(row)"
            />
            <ArtButtonTable type="delete" @click="handleDelete(row)" />
          </div>
        </template>
      </ArtTable>
    </ElCard>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { ElMessage, ElMessageBox, ElTag } from 'element-plus'
import ArtSearchBar from '@/components/core/forms/art-search-bar/index.vue'
import ArtTableHeader from '@/components/core/tables/art-table-header/index.vue'
import ArtTable from '@/components/core/tables/art-table/index.vue'
import ArtButtonTable from '@/components/core/forms/art-button-table/index.vue'
import { useTable } from '@/hooks/core/useTable'
import { fetchRecycleList, restoreRecycleItem, deleteRecycleItem } from '@/api/recycle'

defineOptions({ name: 'RecycleBin' })

type RecycleItem = Api.Recycle.RecycleItem
type RecycleType = Api.Recycle.RecycleType

// 搜索栏引用
const searchBarRef = ref()
// 搜索表单数据
const searchForm = ref<{
  type?: RecycleType | ''
  keyword?: string
  daterange?: string[]
}>({
  type: '',
  keyword: '',
  daterange: undefined
})

// 类型名称映射
const typeLabelMap: Record<RecycleType, string> = {
  NEWS: '新闻',
  NOTICE: '公告',
  SCENIC: '景区',
  VENUE: '场馆',
  ACTIVITY: '活动'
}

// 搜索栏配置项
const searchItems = computed(() => [
  {
    key: 'type',
    label: '类型',
    type: 'select',
    props: {
      placeholder: '全部类型',
      clearable: true,
      options: [
        { label: '全部', value: '' },
        { label: '新闻', value: 'NEWS' },
        { label: '公告', value: 'NOTICE' },
        { label: '景区', value: 'SCENIC' },
        { label: '场馆', value: 'VENUE' },
        { label: '活动', value: 'ACTIVITY' }
      ]
    }
  },
  {
    key: 'keyword',
    label: '关键词',
    type: 'input',
    props: { placeholder: '标题/名称关键字', clearable: true }
  },
  {
    key: 'daterange',
    label: '删除时间',
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
  refreshData,
  refreshRemove
} = useTable({
  core: {
    apiFn: fetchRecycleList,
    apiParams: {
      current: 1,
      size: 10
    },
    excludeParams: ['daterange'],
    columnsFactory: () => [
      { type: 'globalIndex', width: 60, label: '序号' },
      { prop: 'type', label: '类型', width: 110, useSlot: true },
      { prop: 'title', label: '标题/名称', minWidth: 220, showOverflowTooltip: true },
      { prop: 'deletedTime', label: '删除时间', width: 180 },
      { prop: 'operator', label: '操作人', width: 120 },
      { prop: 'extraInfo', label: '备注', minWidth: 180, showOverflowTooltip: true },
      { prop: 'operation', label: '操作', width: 130, fixed: 'right', useSlot: true }
    ]
  }
})

// 处理搜索
const handleSearch = async () => {
  await searchBarRef.value?.validate?.()
  const { daterange, type, keyword } = searchForm.value
  const [startTime, endTime] = Array.isArray(daterange) ? daterange : [null, null]
  const queryType = type ? (type as RecycleType) : undefined
  Object.assign(searchParams, { type: queryType, keyword, startTime, endTime })
  getData()
}

// 处理重置
const handleReset = () => {
  resetSearchParams()
}

// 恢复记录
const handleRestore = async (row: RecycleItem) => {
  await ElMessageBox.confirm(`确认恢复「${row.title}」吗？`, '提示', { type: 'warning' })
  await restoreRecycleItem(row.type, row.id)
  ElMessage.success('恢复成功')
  refreshRemove()
}

// 彻底删除
const handleDelete = async (row: RecycleItem) => {
  await ElMessageBox.confirm(`确认彻底删除「${row.title}」吗？此操作不可恢复。`, '提示', {
    type: 'warning'
  })
  await deleteRecycleItem(row.type, row.id)
  ElMessage.success('删除成功')
  refreshRemove()
}
</script>
