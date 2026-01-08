<template>
  <div class="page-content">
    <div class="flex-cb">
      <div>
        <h1 class="text-2xl font-semibold">举报管理</h1>
        <p class="text-g-500 mt-2">处理交流区文章与评论举报</p>
      </div>
    </div>

    <ArtSearchBar
      ref="searchBarRef"
      v-model="searchForm"
      :items="searchItems"
      class="mt-6"
      @search="handleSearch"
      @reset="handleReset"
    />

    <ElTable :data="tableData" class="mt-6" border v-loading="loading">
      <ElTableColumn prop="targetTypeText" label="举报类型" min-width="120" />
      <ElTableColumn label="举报用户" min-width="160">
        <template #default="{ row }">
          <span>{{ row.reporterNickname || row.reporterName || '未知用户' }}</span>
        </template>
      </ElTableColumn>
      <ElTableColumn label="被举报用户" min-width="160">
        <template #default="{ row }">
          <span>{{ row.targetUserNickname || row.targetUserName || '未知用户' }}</span>
        </template>
      </ElTableColumn>
      <ElTableColumn prop="reasonTypeText" label="原因" min-width="140" />
      <ElTableColumn prop="statusText" label="状态" min-width="120">
        <template #default="{ row }">
          <ElTag :type="statusTagType(row.status)" effect="plain">
            {{ row.statusText || statusText(row.status) }}
          </ElTag>
        </template>
      </ElTableColumn>
      <ElTableColumn prop="createTime" label="举报时间" min-width="160" />
      <ElTableColumn label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <ElButton link type="primary" @click="openDrawer(row)">处理</ElButton>
        </template>
      </ElTableColumn>
    </ElTable>

    <div class="mt-4 flex justify-end">
      <ElPagination
        background
        layout="prev, pager, next"
        :current-page="pagination.current"
        :page-size="pagination.size"
        :total="pagination.total"
        @current-change="handleCurrentChange"
      />
    </div>

    <ElDrawer v-model="drawerVisible" title="举报详情" :size="480" :lock-scroll="false">
      <template v-if="currentReport">
        <div class="space-y-3 text-sm text-g-600">
          <div>举报类型：{{ currentReport.targetTypeText || currentReport.targetType }}</div>
          <div>举报原因：{{ currentReport.reasonTypeText || currentReport.reasonType }}</div>
          <div>原因描述：{{ currentReport.reason || '无' }}</div>
          <div>举报人：{{ currentReport.reporterNickname || currentReport.reporterName || '未知用户' }}</div>
          <div>被举报人：{{ currentReport.targetUserNickname || currentReport.targetUserName || '未知用户' }}</div>
          <div class="flex flex-col gap-2" v-if="screenshotList.length">
            <span>截图证据：</span>
            <div class="grid grid-cols-2 gap-2">
              <ElImage v-for="(url, index) in screenshotList" :key="index" :src="url" fit="cover" />
            </div>
          </div>
        </div>

        <ElDivider />

        <ElForm ref="editFormRef" :model="editForm" label-width="90px">
          <ElFormItem label="处理状态">
            <ElSelect v-model="editForm.status" placeholder="请选择">
              <ElOption label="待处理" :value="0" />
              <ElOption label="已处理" :value="1" />
              <ElOption label="已驳回" :value="2" />
            </ElSelect>
          </ElFormItem>
          <ElFormItem label="违规判定">
            <ElRadioGroup v-model="editForm.violationFlag">
              <ElRadio :label="1">违规</ElRadio>
              <ElRadio :label="0">未违规</ElRadio>
            </ElRadioGroup>
          </ElFormItem>
          <ElFormItem label="处理说明">
            <ElInput v-model="editForm.resultRemark" type="textarea" :rows="3" maxlength="200" show-word-limit />
          </ElFormItem>
        </ElForm>

        <div class="flex justify-end gap-3 mt-4">
          <ElButton @click="drawerVisible = false">取消</ElButton>
          <ElButton type="primary" @click="submitUpdate">保存</ElButton>
        </div>
      </template>
    </ElDrawer>
  </div>
</template>

<script setup lang="ts">
  import { computed, reactive, ref, watch } from 'vue'
  import { useRoute } from 'vue-router'
  import ArtSearchBar from '@/components/core/forms/art-search-bar/index.vue'
  import { useTable } from '@/hooks/core/useTable'
  import { fetchExchangeReportPage, updateExchangeReport } from '@/api/exchange'

  defineOptions({ name: 'ExchangeReportManage' })

  type ExchangeReportItem = Api.Exchange.ExchangeReportItem

  const searchBarRef = ref()
  const route = useRoute()
  const searchForm = ref<{ status?: number | null; targetType?: string; keyword?: string }>({
    status: null,
    targetType: '',
    keyword: ''
  })

  const searchItems = computed(() => [
    {
      key: 'targetType',
      label: '举报类型',
      type: 'select',
      props: {
        options: [
          { label: '全部', value: '' },
          { label: '交流文章', value: 'ARTICLE' },
          { label: '交流评论', value: 'COMMENT' },
          { label: '活动评论', value: 'ACTIVITY_COMMENT' }
        ]
      }
    },
    {
      key: 'status',
      label: '状态',
      type: 'select',
      props: {
        options: [
          { label: '全部', value: null },
          { label: '待处理', value: 0 },
          { label: '已处理', value: 1 },
          { label: '已驳回', value: 2 }
        ]
      }
    },
    { key: 'keyword', label: '关键词', type: 'input', props: { placeholder: '原因/用户' } }
  ])

  const { data, loading, pagination, getData, searchParams, resetSearchParams, handleCurrentChange } = useTable({
    core: {
      apiFn: fetchExchangeReportPage,
      apiParams: { current: 1, size: 10 }
    },
    transform: {
      responseAdapter: (response) => ({
        records: response.list,
        total: response.total,
        current: response.pageNum,
        size: response.pageSize
      })
    }
  })

  const tableData = computed(() => data.value || [])
  const openedFromQuery = ref<number | null>(null)

  const statusText = (status?: number) => {
    if (status === 1) return '已处理'
    if (status === 2) return '已驳回'
    return '待处理'
  }

  const statusTagType = (status?: number) => {
    if (status === 1) return 'success'
    if (status === 2) return 'info'
    return 'warning'
  }

  const handleSearch = async () => {
    await searchBarRef.value?.validate?.()
    Object.assign(searchParams, {
      current: 1,
      status: searchForm.value.status ?? undefined,
      targetType: searchForm.value.targetType || undefined,
      keyword: searchForm.value.keyword || undefined
    })
    getData()
  }

  const handleReset = () => {
    resetSearchParams()
    searchForm.value = { status: null, targetType: '', keyword: '' }
    getData()
  }

  const drawerVisible = ref(false)
  const currentReport = ref<ExchangeReportItem | null>(null)
  const editForm = reactive({ status: 0, violationFlag: 0, resultRemark: '' })

  const screenshotList = computed(() => {
    if (!currentReport.value?.screenshotUrls) {
      return []
    }
    try {
      return JSON.parse(currentReport.value.screenshotUrls)
    } catch {
      return []
    }
  })

  const openDrawer = (row: ExchangeReportItem) => {
    currentReport.value = row
    editForm.status = row.status
    editForm.violationFlag = row.violationFlag ?? 0
    editForm.resultRemark = row.resultRemark || ''
    drawerVisible.value = true
  }

  const tryOpenFromQuery = () => {
    const id = Number(route.query.id)
    if (Number.isNaN(id) || !id) return
    if (openedFromQuery.value === id) return
    const target = tableData.value.find((item) => item.id === id)
    if (!target) return
    openDrawer(target)
    openedFromQuery.value = id
  }

  const submitUpdate = async () => {
    if (!currentReport.value) return
    await updateExchangeReport(currentReport.value.id, {
      status: editForm.status,
      violationFlag: editForm.violationFlag,
      resultRemark: editForm.resultRemark || undefined
    })
    drawerVisible.value = false
    getData()
  }

  watch([() => route.query.id, tableData], () => {
    tryOpenFromQuery()
  }, { immediate: true })
</script>

<style scoped lang="scss">
  .page-content {
    padding: 24px;
  }
</style>
