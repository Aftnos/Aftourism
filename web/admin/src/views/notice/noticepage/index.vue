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
            <ElButton type="primary" @click="showDialog('add')" v-ripple>新增通知</ElButton>
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
        <template #status="{ row }">
          <ElTag :type="String(row.status) === '1' ? 'success' : 'info'">
            {{ String(row.status) === '1' ? '发布' : '下线' }}
          </ElTag>
        </template>
        <template #operation="{ row }">
          <div class="flex">
            <ArtButtonTable type="edit" :row="row" @click="showDialog('edit', row)" />
            <ArtButtonTable type="delete" :row="row" @click="handleDelete(row)" />
          </div>
        </template>
      </ArtTable>

      <ElDialog v-model="dialogVisible" :title="dialogTitle" width="740px">
        <ElForm ref="formRef" :model="current" :rules="rules" label-width="90px">
          <ElFormItem label="标题" prop="title">
            <ElInput v-model="current.title" />
          </ElFormItem>
          <ElFormItem label="作者" prop="author">
            <ElInput v-model="current.author" />
          </ElFormItem>
          <ElFormItem label="状态" prop="status">
            <ElSelect v-model="current.status">
              <ElOption label="已发布" :value="1" />
              <ElOption label="已下线" :value="0" />
            </ElSelect>
          </ElFormItem>
          <ElFormItem label="发布时间" prop="publishTime">
            <ElDatePicker v-model="current.publishTime" type="datetime" placeholder="选择发布时间" value-format="YYYY-MM-DD HH:mm:ss" format="YYYY-MM-DD HH:mm:ss" />
          </ElFormItem>
          <ElFormItem label="内容" prop="content">
            <ArtWangEditor v-model="current.content" height="400px" :upload-config="{ server: '/api/file/upload' }" />
          </ElFormItem>
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
import { ElMessage, ElMessageBox, ElTag, ElDatePicker, ElInput, ElSelect, ElOption, ElButton, ElSpace, type FormInstance } from 'element-plus'
import ArtSearchBar from '@/components/core/forms/art-search-bar/index.vue'
import ArtTableHeader from '@/components/core/tables/art-table-header/index.vue'
import ArtTable from '@/components/core/tables/art-table/index.vue'
import ArtButtonTable from '@/components/core/forms/art-button-table/index.vue'
import ArtWangEditor from '@/components/core/forms/art-wang-editor/index.vue'
import { useTable } from '@/hooks/core/useTable'
import { fetchGetNoticePage, createNotice, updateNotice, deleteNotice } from '@/api/notice'

defineOptions({ name: 'NoticePage' })

type NoticeItem = Api.Notice.NoticeItem

const dialogType = ref<'add' | 'edit'>('add')
const dialogVisible = ref(false)
const current = reactive<Partial<NoticeItem>>({ title: '', content: '', author: '', status: 1, publishTime: '' })
const formRef = ref<FormInstance>()

const rules = reactive({
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  author: [{ required: true, message: '请输入作者', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
})

const searchBarRef = ref()
const searchForm = ref<{ title?: string; status?: number | string }>({ title: '', status: '' })

const searchItems = computed(() => [
  { key: 'title', label: '标题', type: 'input', props: { placeholder: '标题关键字' } },
  {
    key: 'status',
    label: '状态',
    type: 'select',
    options: [
      { label: '全部', value: '' },
      { label: '已发布', value: 1 },
      { label: '已下线', value: 0 }
    ]
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
  refreshCreate,
  refreshUpdate,
  refreshRemove
} = useTable({
  core: {
    apiFn: async (params) => {
      const res = await fetchGetNoticePage(params)
      return { data: res, total: res.total, records: res.list, current: res.pageNum, size: res.pageSize }
    },
    apiParams: { current: 1, size: 10, ...searchForm.value },
    columnsFactory: () => [
      { type: 'selection', width: 50 },
      { type: 'globalIndex', width: 60, label: '序号' },
      { prop: 'title', label: '标题' },
      { prop: 'author', label: '作者', width: 120 },
      { prop: 'status', label: '状态', useSlot: true, width: 100 },
      { prop: 'publishTime', label: '发布时间', width: 180 },
      { prop: 'viewCount', label: '浏览量', width: 100 },
      { prop: 'operation', label: '操作', useSlot: true, width: 160, fixed: 'right' }
    ]
  }
})

const dialogTitle = computed(() => (dialogType.value === 'edit' ? '编辑通知' : '新增通知'))

const handleSearch = async () => {
  await searchBarRef.value?.validate?.()
  Object.assign(searchParams, searchForm.value)
  getData()
}

const handleReset = () => {
  resetSearchParams()
}

const showDialog = (type: 'add' | 'edit', row?: NoticeItem) => {
  dialogType.value = type
  Object.assign(current, row || { title: '', content: '', author: '', status: 1, publishTime: '' })
  nextTick(() => (dialogVisible.value = true))
}

const handleDialogSubmit = async () => {
  await formRef.value?.validate?.()
  if (current.id) {
    await updateNotice(current.id, current)
    dialogVisible.value = false
    refreshUpdate()
    ElMessage.success('保存成功')
  } else {
    await createNotice(current)
    dialogVisible.value = false
    refreshCreate()
    ElMessage.success('保存成功')
  }
}

const handleDelete = async (row: NoticeItem) => {
  await ElMessageBox.confirm(`确认删除通知「${row.title}」吗？`, '提示', { type: 'warning' })
  await deleteNotice(row.id)
  ElMessage.success('删除成功')
  refreshRemove()
}
</script>

<style scoped>
</style>
