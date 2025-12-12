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
            <ElButton type="primary" @click="showDialog('add')" v-ripple>新增活动</ElButton>
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
        <template #coverUrl="{ row }">
          <ElImage :src="row.coverUrl" fit="cover" style="width:80px;height:50px;border-radius:6px" v-if="row.coverUrl" />
        </template>
        <template #onlineStatus="{ row }">
          <ElTag :type="Number(row.onlineStatus) === 1 ? 'success' : 'info'">{{ Number(row.onlineStatus) === 1 ? '已上线' : '未上线' }}</ElTag>
        </template>
        <template #operation="{ row }">
          <div class="flex">
            <ArtButtonTable type="edit" :row="row" @click="showDialog('edit', row)" />
            <ArtButtonTable type="delete" :row="row" @click="handleDelete(row)" />
          </div>
        </template>
      </ArtTable>

      <ElDialog v-model="dialogVisible" :title="dialogTitle" width="800px">
        <ElForm ref="formRef" :model="current" :rules="rules" label-width="90px">
          <ElRow :gutter="12">
            <ElCol :span="12">
              <ElFormItem label="名称" prop="name">
                <ElInput v-model="current.name" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="12">
              <ElFormItem label="类别" prop="category">
                <ElInput v-model="current.category" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="12">
              <ElFormItem label="开始时间" prop="startTime">
                <ElDatePicker v-model="current.startTime" type="datetime" placeholder="选择开始时间" value-format="YYYY-MM-DD HH:mm:ss" format="YYYY-MM-DD HH:mm:ss" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="12">
              <ElFormItem label="结束时间" prop="endTime">
                <ElDatePicker v-model="current.endTime" type="datetime" placeholder="选择结束时间" value-format="YYYY-MM-DD HH:mm:ss" format="YYYY-MM-DD HH:mm:ss" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="12">
              <ElFormItem label="场馆ID" prop="venueId">
                <ElInputNumber v-model="current.venueId" :min="1" :step="1" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="12">
              <ElFormItem label="上线状态" prop="onlineStatus">
                <ElSelect v-model="current.onlineStatus">
                  <ElOption label="已上线" :value="1" />
                  <ElOption label="未上线" :value="0" />
                </ElSelect>
              </ElFormItem>
            </ElCol>
            <ElCol :span="12">
              <ElFormItem label="主办方" prop="organizer">
                <ElInput v-model="current.organizer" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="12">
              <ElFormItem label="联系电话" prop="contactPhone">
                <ElInput v-model="current.contactPhone" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="24">
              <ElFormItem label="封面" prop="coverUrl">
                <ElUpload :http-request="coverUploadRequest" :show-file-list="false" :limit="1" accept="image/*">
                  <ElButton type="primary">上传封面</ElButton>
                </ElUpload>
                <ElImage v-if="current.coverUrl" :src="current.coverUrl" fit="cover" style="display:block;margin-top:8px;width:160px;height:90px;border-radius:6px" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="24">
              <ElFormItem label="简介" prop="intro">
                <ArtWangEditor v-model="current.intro" height="300px" :upload-config="{ server: '/api/file/upload' }" />
              </ElFormItem>
            </ElCol>
          </ElRow>
        </ElForm>
        <template #footer>
          <ElButton @click="dialogVisible = false">取消</ElButton>
          <ElButton type="primary" @click="handleDialogSubmit">保存</ElButton>
        </template>
      </ElDialog>
    </ElCard>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, nextTick } from 'vue'
import { ElMessage, ElMessageBox, ElImage, ElUpload, ElButton, ElSpace, ElInput, ElInputNumber, ElRow, ElCol, ElTag, ElDatePicker, ElSelect, ElOption, type FormInstance } from 'element-plus'
import ArtSearchBar from '@/components/core/forms/art-search-bar/index.vue'
import ArtTableHeader from '@/components/core/tables/art-table-header/index.vue'
import ArtTable from '@/components/core/tables/art-table/index.vue'
import ArtButtonTable from '@/components/core/forms/art-button-table/index.vue'
import ArtWangEditor from '@/components/core/forms/art-wang-editor/index.vue'
import { useTable } from '@/hooks/core/useTable'
import { fetchGetActivityPage, createActivity, updateActivity, deleteActivity } from '@/api/activity'
import { uploadFile } from '@/api/file'

defineOptions({ name: 'ActivityPage' })

type Activity = Api.Activity.ActivityManageVO

const dialogType = ref<'add' | 'edit'>('add')
const dialogVisible = ref(false)
const current = reactive<Partial<Activity>>({ name: '', coverUrl: '', startTime: '', endTime: '', category: '', venueId: 1, organizer: '', contactPhone: '', intro: '', addressCache: '', onlineStatus: 0 })
const formRef = ref<FormInstance>()

const rules = reactive({
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
  venueId: [{ required: true, message: '请输入场馆ID', trigger: 'blur' }]
})

const searchBarRef = ref()
const searchForm = ref<{ name?: string; category?: string; onlineStatus?: number | string; startRange?: string[] }>({ name: '', category: '', onlineStatus: '', startRange: undefined })

const searchItems = computed(() => [
  { key: 'name', label: '名称', type: 'input', props: { placeholder: '名称关键字' } },
  { key: 'category', label: '类别', type: 'input', props: { placeholder: '类别关键字' } },
  { key: 'onlineStatus', label: '上线状态', type: 'select', options: [ { label: '全部', value: '' }, { label: '已上线', value: 1 }, { label: '未上线', value: 0 } ] },
  { key: 'startRange', label: '开始时间范围', type: 'datetimerange', props: { type: 'datetimerange', valueFormat: 'YYYY-MM-DD HH:mm:ss', startPlaceholder: '开始', endPlaceholder: '结束' } }
])

const { columns, columnChecks, data, loading, pagination, getData, searchParams, resetSearchParams, handleSizeChange, handleCurrentChange, refreshData, refreshCreate, refreshUpdate, refreshRemove } = useTable({
  core: {
    apiFn: async (params) => {
      const res = await fetchGetActivityPage(params)
      return { data: res, records: res.list, total: res.total, current: res.pageNum, size: res.pageSize }
    },
    apiParams: { current: 1, size: 10, ...searchForm.value },
    columnsFactory: () => [
      { type: 'selection', width: 50 },
      { type: 'globalIndex', width: 60, label: '序号' },
      { prop: 'coverUrl', label: '封面', useSlot: true, width: 110 },
      { prop: 'name', label: '名称', minWidth: 160 },
      { prop: 'category', label: '类别', width: 120 },
      { prop: 'venueName', label: '场馆', width: 160 },
      { prop: 'startTime', label: '开始时间', width: 170 },
      { prop: 'endTime', label: '结束时间', width: 170 },
      { prop: 'onlineStatus', label: '上线状态', useSlot: true, width: 100 },
      { prop: 'viewCount', label: '浏览', width: 100 },
      { prop: 'favoriteCount', label: '收藏', width: 100 },
      { prop: 'createTime', label: '创建时间', width: 170 },
      { prop: 'operation', label: '操作', useSlot: true, width: 160, fixed: 'right' }
    ]
  }
})

const dialogTitle = computed(() => (dialogType.value === 'edit' ? '编辑活动' : '新增活动'))

const handleSearch = async () => {
  await searchBarRef.value?.validate?.()
  const params: any = { name: searchForm.value.name, category: searchForm.value.category, onlineStatus: searchForm.value.onlineStatus }
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

const showDialog = (type: 'add' | 'edit', row?: Activity) => {
  dialogType.value = type
  Object.assign(current, row || { name: '', coverUrl: '', startTime: '', endTime: '', category: '', venueId: 1, organizer: '', contactPhone: '', intro: '', addressCache: '', onlineStatus: 0 })
  nextTick(() => (dialogVisible.value = true))
}

const handleDialogSubmit = async () => {
  await formRef.value?.validate?.()
  const payload = { ...current }
  if (current.id) {
    await updateActivity(current.id, payload)
    dialogVisible.value = false
    refreshUpdate()
    ElMessage.success('保存成功')
  } else {
    await createActivity(payload)
    dialogVisible.value = false
    refreshCreate()
    ElMessage.success('保存成功')
  }
}

const handleDelete = async (row: Activity) => {
  await ElMessageBox.confirm(`确认删除活动「${row.name}」吗？`, '提示', { type: 'warning' })
  await deleteActivity(row.id)
  ElMessage.success('删除成功')
  refreshRemove()
}

const coverUploadRequest = async (options: any) => {
  const file = options.file as File
  const res = await uploadFile({ file, bizTag: 'ACTIVITY_COVER' })
  current.coverUrl = res.url
  options.onSuccess?.(res)
}
</script>

<style scoped>
</style>

