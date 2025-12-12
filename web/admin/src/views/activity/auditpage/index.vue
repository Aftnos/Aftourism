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
        <template #onlineStatus="{ row }">
          <ElTag :type="Number(row.onlineStatus) === 1 ? 'success' : 'info'">{{ Number(row.onlineStatus) === 1 ? '上线' : '下线' }}</ElTag>
        </template>
        <template #operation="{ row }">
          <div class="flex">
            <ArtButtonTable type="view" :row="row" @click="openDetail(row)" />
            <ElButton size="small" type="success" class="ml-2" @click="handleApprove(row)">通过</ElButton>
            <ElButton size="small" type="danger" class="ml-2" @click="handleReject(row)">驳回</ElButton>
          </div>
        </template>
      </ArtTable>

      <ElDialog v-model="detailVisible" title="活动审批详情" width="900px" top="5vh">
        <div v-if="detail">
          <ElDescriptions :column="2" border>
            <ElDescriptionsItem label="名称">{{ detail.name }}</ElDescriptionsItem>
            <ElDescriptionsItem label="类别">{{ detail.category }}</ElDescriptionsItem>
            <ElDescriptionsItem label="场馆">{{ detail.venueName }}</ElDescriptionsItem>
            <ElDescriptionsItem label="联系方式">{{ detail.contactPhone }}</ElDescriptionsItem>
            <ElDescriptionsItem label="主办单位">{{ detail.organizer }}</ElDescriptionsItem>
            <ElDescriptionsItem label="申报人ID">{{ detail.applyUserId }}</ElDescriptionsItem>
            <ElDescriptionsItem label="提交时间">{{ detail.submitTime }}</ElDescriptionsItem>
            <ElDescriptionsItem label="审核状态">
              <ElTag :type="statusTagType(detail.applyStatus)">{{ statusText(detail.applyStatus) }}</ElTag>
            </ElDescriptionsItem>
            <ElDescriptionsItem label="开始时间">{{ detail.startTime }}</ElDescriptionsItem>
            <ElDescriptionsItem label="结束时间">{{ detail.endTime }}</ElDescriptionsItem>
            <ElDescriptionsItem label="地址" :span="2">{{ detail.addressCache }}</ElDescriptionsItem>
            <ElDescriptionsItem label="封面" :span="2" v-if="detail.coverUrl">
              <ElImage :src="detail.coverUrl" fit="cover" style="width:200px;height:120px;border-radius:6px" :preview-src-list="[detail.coverUrl]" />
            </ElDescriptionsItem>
            <ElDescriptionsItem label="简介" :span="2">
              <div v-html="detail.intro" class="rich-content"></div>
            </ElDescriptionsItem>
            <ElDescriptionsItem label="申报备注" :span="2">{{ detail.auditRemark || '-' }}</ElDescriptionsItem>
            <ElDescriptionsItem label="驳回原因" :span="2" v-if="detail.rejectReason">
              <span class="text-danger">{{ detail.rejectReason }}</span>
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
import { ref, reactive, computed, nextTick } from 'vue'
import { ElMessage, ElMessageBox, ElSpace, ElButton, ElTag, ElCard, ElDescriptions, ElDescriptionsItem, ElImage } from 'element-plus'
import ArtSearchBar from '@/components/core/forms/art-search-bar/index.vue'
import ArtTableHeader from '@/components/core/tables/art-table-header/index.vue'
import ArtTable from '@/components/core/tables/art-table/index.vue'
import ArtButtonTable from '@/components/core/forms/art-button-table/index.vue'
import { useTable } from '@/hooks/core/useTable'
import { fetchGetAuditPage, getAuditDetail, approveActivityAudit, rejectActivityAudit } from '@/api/activity-audit'

defineOptions({ name: 'ActivityAuditPage' })

type AuditItem = Api.ActivityAudit.ActivityAuditItemVO
type AuditDetail = Api.ActivityAudit.ActivityAuditDetailVO

const searchBarRef = ref()
const searchForm = ref<{ name?: string; applyStatus?: number | string; onlineStatus?: number | string; startRange?: string[] }>({ name: '', applyStatus: '', onlineStatus: '', startRange: undefined })

const searchItems = computed(() => [
  { key: 'name', label: '名称', type: 'input', props: { placeholder: '名称关键字' } },
  { key: 'applyStatus', label: '审核状态', type: 'select', options: [ { label: '全部', value: '' }, { label: '待审核', value: 0 }, { label: '已通过', value: 1 }, { label: '已拒绝', value: 2 } ] },
  { key: 'onlineStatus', label: '上线状态', type: 'select', options: [ { label: '全部', value: '' }, { label: '上线', value: 1 }, { label: '下线', value: 0 } ] },
  { key: 'startRange', label: '开始时间范围', type: 'datetimerange', props: { type: 'datetimerange', valueFormat: 'YYYY-MM-DD HH:mm:ss', startPlaceholder: '开始', endPlaceholder: '结束' } }
])

const { columns, columnChecks, data, loading, pagination, getData, searchParams, resetSearchParams, handleSizeChange, handleCurrentChange, refreshData } = useTable({
  core: {
    apiFn: async (params) => {
      const res = await fetchGetAuditPage(params)
      return { data: res, records: res.list, total: res.total, current: res.pageNum, size: res.pageSize }
    },
    apiParams: { current: 1, size: 10, ...searchForm.value },
    columnsFactory: () => [
      { type: 'selection', width: 50 },
      { type: 'globalIndex', width: 60, label: '序号' },
      { prop: 'name', label: '名称', minWidth: 160 },
      { prop: 'category', label: '类别', width: 120 },
      { prop: 'venueName', label: '场馆', width: 160 },
      { prop: 'startTime', label: '开始时间', width: 170 },
      { prop: 'endTime', label: '结束时间', width: 170 },
      { prop: 'submitTime', label: '提交时间', width: 170 },
      { prop: 'applyStatus', label: '审核状态', useSlot: true, width: 100 },
      { prop: 'onlineStatus', label: '上线状态', useSlot: true, width: 100 },
      { prop: 'operation', label: '操作', useSlot: true, width: 220, fixed: 'right' }
    ]
  }
})

const statusText = (s: number) => (s === 0 ? '待审核' : s === 1 ? '已通过' : '已拒绝')
const statusTagType = (s: number) => (s === 0 ? 'warning' : s === 1 ? 'success' : 'danger')

const handleSearch = async () => {
  await searchBarRef.value?.validate?.()
  const params: any = { name: searchForm.value.name }
  if (searchForm.value.applyStatus !== '') params.applyStatus = Number(searchForm.value.applyStatus)
  if (searchForm.value.onlineStatus !== '') params.onlineStatus = Number(searchForm.value.onlineStatus)
  if (Array.isArray(searchForm.value.startRange)) {
    const [from, to] = searchForm.value.startRange
    params.startTimeFrom = from
    params.startTimeTo = to
  }
  Object.assign(searchParams, params)
  getData()
}

const handleReset = () => {
  resetSearchParams()
}

const detailVisible = ref(false)
const detail = ref<AuditDetail | null>(null)

const openDetail = async (row: AuditItem) => {
  const res = await getAuditDetail(row.id)
  detail.value = res
  nextTick(() => (detailVisible.value = true))
}

const handleApprove = async (row: AuditItem | AuditDetail) => {
  await ElMessageBox.confirm(`确认通过活动「${row.name}」的审批吗？`, '提示', { type: 'warning' })
  await approveActivityAudit(row.id)
  ElMessage.success('已通过')
  detailVisible.value = false
  refreshData()
}

const handleReject = async (row: AuditItem | AuditDetail) => {
  const { value } = await ElMessageBox.prompt('请输入驳回原因', '驳回审批', { inputType: 'textarea', inputPlaceholder: '驳回原因', confirmButtonText: '确定', cancelButtonText: '取消' })
  if (!value || !String(value).trim()) {
    ElMessage.error('请输入驳回原因')
    return
  }
  await rejectActivityAudit(row.id, { rejectReason: String(value).trim() })
  ElMessage.success('已驳回')
  detailVisible.value = false
  refreshData()
}
</script>

<style scoped>
.ml-2 { margin-left: 8px; }
.rich-content { max-height: 400px; overflow-y: auto; }
.text-danger { color: var(--el-color-danger); }
.flex-c { display: flex; justify-content: center; gap: 12px; }
</style>
