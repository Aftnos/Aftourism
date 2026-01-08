<template>
  <div class="art-full-height qualification-audit-page">
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
      <ArtTableHeader v-model:columns="columnChecks" :loading="loading" @refresh="refreshData">
        <template #left>
          <ElSpace wrap>
            <ElButton @click="refreshData">刷新</ElButton>
          </ElSpace>
        </template>
      </ArtTableHeader>

      <ArtTable
        :loading="loading"
        :data="data"
        :columns="columns"
        :pagination="pagination"
        @pagination:size-change="handleSizeChange"
        @pagination:current-change="handleCurrentChange"
      >
        <template #applyStatus="{ row }">
          <ElTag :type="statusTagType(row.applyStatus)">{{ statusText(row.applyStatus) }}</ElTag>
        </template>
        <template #operation="{ row }">
          <div class="flex">
            <ArtButtonTable type="view" :row="row" @click="openDetail(row)" />
            <ElButton size="small" type="success" class="ml-2" @click="handleApprove(row)" :disabled="row.applyStatus !== 0">
              通过
            </ElButton>
            <ElButton size="small" type="danger" class="ml-2" @click="handleReject(row)" :disabled="row.applyStatus !== 0">
              驳回
            </ElButton>
          </div>
        </template>
      </ArtTable>

      <ElDialog v-model="detailVisible" title="资质审核详情" width="820px" top="6vh">
        <div v-if="detail">
          <ElDescriptions :column="2" border>
            <ElDescriptionsItem label="申请账号">{{ detail.userName }}</ElDescriptionsItem>
            <ElDescriptionsItem label="申请人姓名">{{ detail.realName }}</ElDescriptionsItem>
            <ElDescriptionsItem label="单位/机构">{{ detail.organization }}</ElDescriptionsItem>
            <ElDescriptionsItem label="联系电话">{{ detail.contactPhone }}</ElDescriptionsItem>
            <ElDescriptionsItem label="审核状态">
              <ElTag :type="statusTagType(detail.applyStatus)">{{ statusText(detail.applyStatus) }}</ElTag>
            </ElDescriptionsItem>
            <ElDescriptionsItem label="申请时间">{{ detail.createTime }}</ElDescriptionsItem>
            <ElDescriptionsItem label="审核备注" :span="2">{{ detail.auditRemark || '-' }}</ElDescriptionsItem>
            <ElDescriptionsItem label="申请说明" :span="2">
              <div class="reason-box">{{ detail.applyReason }}</div>
            </ElDescriptionsItem>
            <ElDescriptionsItem label="附件链接" :span="2">
              <ElLink v-if="detail.attachmentUrl" :href="detail.attachmentUrl" target="_blank">
                {{ detail.attachmentUrl }}
              </ElLink>
              <span v-else>-</span>
            </ElDescriptionsItem>
          </ElDescriptions>
        </div>
        <template #footer>
          <div class="flex-c">
            <ElButton type="success" @click="handleApprove(detail!)" v-if="detail && detail.applyStatus === 0">通过</ElButton>
            <ElButton type="danger" @click="handleReject(detail!)" v-if="detail && detail.applyStatus === 0">驳回</ElButton>
            <ElButton @click="detailVisible = false">关闭</ElButton>
          </div>
        </template>
      </ElDialog>
    </ElCard>
  </div>
</template>

<script setup lang="ts">
  import { ref, computed, nextTick, onMounted, watch } from 'vue'
  import { useRoute } from 'vue-router'
  import { ElMessage, ElMessageBox, ElSpace, ElButton, ElCard, ElTag, ElDescriptions, ElDescriptionsItem, ElLink } from 'element-plus'
  import ArtSearchBar from '@/components/core/forms/art-search-bar/index.vue'
  import ArtTableHeader from '@/components/core/tables/art-table-header/index.vue'
  import ArtTable from '@/components/core/tables/art-table/index.vue'
  import ArtButtonTable from '@/components/core/forms/art-button-table/index.vue'
  import { useTable } from '@/hooks/core/useTable'
  import { fetchQualificationPage, fetchQualificationDetail, approveQualification, rejectQualification } from '@/api/qualification'

  defineOptions({ name: 'QualificationAudit' })

  type QualificationItem = Api.Qualification.QualificationItem

  // 中文注释：搜索栏与查询条件
  const searchBarRef = ref()
  const searchForm = ref<{ userName?: string; applyStatus?: number | string }>({ userName: '', applyStatus: '' })
  const route = useRoute()

  const searchItems = computed(() => [
    { key: 'userName', label: '账号', type: 'input', props: { placeholder: '账号关键字' } },
    {
      key: 'applyStatus',
      label: '审核状态',
      type: 'select',
      options: [
        { label: '全部', value: '' },
        { label: '待审核', value: 0 },
        { label: '已通过', value: 1 },
        { label: '已驳回', value: 2 }
      ]
    }
  ])

  // 中文注释：表格数据与分页管理
  const { columns, columnChecks, data, loading, pagination, getData, searchParams, resetSearchParams, handleSizeChange, handleCurrentChange, refreshData } =
    useTable({
      core: {
        apiFn: async (params) => {
          const res = await fetchQualificationPage(params)
          return { data: res, records: res.list, total: res.total, current: res.pageNum, size: res.pageSize }
        },
        apiParams: { current: 1, size: 10, ...searchForm.value },
        columnsFactory: () => [
          { type: 'selection', width: 50 },
          { type: 'globalIndex', width: 60, label: '序号' },
          { prop: 'userName', label: '账号', minWidth: 140 },
          { prop: 'realName', label: '姓名', minWidth: 120 },
          { prop: 'organization', label: '单位/机构', minWidth: 160 },
          { prop: 'contactPhone', label: '联系电话', minWidth: 140 },
          { prop: 'applyStatus', label: '审核状态', useSlot: true, width: 100 },
          { prop: 'createTime', label: '申请时间', width: 170 },
          { prop: 'auditRemark', label: '审核备注', minWidth: 180 },
          { prop: 'operation', label: '操作', useSlot: true, width: 220, fixed: 'right' }
        ]
      }
    })

  const statusText = (s: number) => (s === 0 ? '待审核' : s === 1 ? '已通过' : '已驳回')
  const statusTagType = (s: number) => (s === 0 ? 'warning' : s === 1 ? 'success' : 'danger')

  const handleSearch = async () => {
    await searchBarRef.value?.validate?.()
    const params: any = { userName: searchForm.value.userName }
    if (searchForm.value.applyStatus !== '') params.applyStatus = Number(searchForm.value.applyStatus)
    Object.assign(searchParams, params)
    getData()
  }

  const handleReset = () => {
    resetSearchParams()
  }

  // 中文注释：详情弹窗
  const detailVisible = ref(false)
  const detail = ref<QualificationItem | null>(null)

  const openDetail = async (row: QualificationItem) => {
    const res = await fetchQualificationDetail(row.id)
    detail.value = res
    nextTick(() => (detailVisible.value = true))
  }

  const openDetailById = async (id: number) => {
    if (!id) return
    const res = await fetchQualificationDetail(id)
    detail.value = res
    nextTick(() => (detailVisible.value = true))
  }

  const handleApprove = async (row: QualificationItem) => {
    await ElMessageBox.confirm(`确认通过用户「${row.userName}」的资质申请吗？`, '提示', { type: 'warning' })
    await approveQualification(row.id)
    ElMessage.success('已通过')
    detailVisible.value = false
    refreshData()
  }

  const handleReject = async (row: QualificationItem) => {
    const { value } = await ElMessageBox.prompt('请输入驳回原因', '驳回申请', {
      inputType: 'textarea',
      inputPlaceholder: '驳回原因',
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    })
    if (!value || !String(value).trim()) {
      ElMessage.error('请输入驳回原因')
      return
    }
    await rejectQualification(row.id, { auditRemark: String(value).trim() })
    ElMessage.success('已驳回')
    detailVisible.value = false
    refreshData()
  }

  const tryOpenFromQuery = () => {
    const id = Number(route.query.id)
    if (Number.isNaN(id) || !id) return
    void openDetailById(id)
  }

  onMounted(() => {
    tryOpenFromQuery()
  })

  watch(
    () => route.query.id,
    () => {
      tryOpenFromQuery()
    }
  )
</script>

<style scoped>
.ml-2 {
  margin-left: 8px;
}

.reason-box {
  white-space: pre-wrap;
  line-height: 1.6;
  color: var(--el-text-color-regular);
}

.flex-c {
  display: flex;
  justify-content: center;
  gap: 12px;
}
</style>
