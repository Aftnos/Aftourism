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
            <ElButton type="primary" @click="showDialog('add')" v-ripple>新增新闻</ElButton>
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
          <ElTag :type="row.status == '1' ? 'success' : 'info'">
            {{ row.status == '1' ? '发布' : '下线' }}
          </ElTag>
        </template>
        
        <template #operation="{ row }">
          <div class="flex">
            <ArtButtonTable type="edit" :row="row" @click="showDialog('edit', row)" />
            <ArtButtonTable type="delete" :row="row" @click="handleDelete(row)" />
          </div>
        </template>
      </ArtTable>

      
      <ElDialog v-model="dialogVisible" :title="dialogTitle" width="640px">
        <ElForm ref="formRef" :model="current" :rules="rules" label-width="80px">
          <ElFormItem label="标题" prop="title">
            <ElInput v-model="current.title" />
          </ElFormItem>
          <ElFormItem label="作者" prop="author">
            <ElInput v-model="current.author" />
          </ElFormItem>
          <ElFormItem label="封面" prop="coverUrl">
            <ElUpload
              :http-request="coverUploadRequest"
              :show-file-list="false"
              :limit="1"
              accept="image/*"
            >
              <ElButton type="primary">上传封面</ElButton>
            </ElUpload>
            <ElImage v-if="current.coverUrl" :src="current.coverUrl" fit="cover" class="cover-preview" />
          </ElFormItem>
          <ElFormItem label="状态" prop="status">
            <ElSelect v-model="current.status">
              <ElOption label="已发布" value="1" />
              <ElOption label="已下线" value="0" />
            </ElSelect>
          </ElFormItem>
          <ElFormItem label="内容" prop="content">
            <ArtWangEditor
              v-model="current.content"
              height="400px"
              :upload-config="{ server: '/api/file/upload' }"
            />
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
import { ref, reactive, computed, nextTick, h } from 'vue'
import { ElMessage, ElMessageBox, ElTag, ElImage, ElUpload, ElButton } from 'element-plus'
import ArtSearchBar from '@/components/core/forms/art-search-bar/index.vue'
import ArtTableHeader from '@/components/core/tables/art-table-header/index.vue'
import ArtTable from '@/components/core/tables/art-table/index.vue'
import ArtButtonTable from '@/components/core/forms/art-button-table/index.vue'
import { useTable } from '@/hooks/core/useTable'
import ArtWangEditor from '@/components/core/forms/art-wang-editor/index.vue'
import { uploadFile } from '@/api/file'
import { fetchGetNewsList, createNews, updateNews, deleteNews } from '@/api/news'

defineOptions({ name: 'NewsPage' })

// 新闻项类型定义
type NewsItem = Api.News.NewsItem

// 对话框类型：新增或编辑
const dialogType = ref<'add' | 'edit'>('add')
// 对话框显示状态
const dialogVisible = ref(false)
// 当前编辑的新闻数据
const current = reactive<Partial<NewsItem>>({ title: '', content: '', coverUrl: '', status: '1', author: '' })
// 表单引用
const formRef = ref()

// 表单校验规则
const rules = reactive({
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  author: [{ required: true, message: '请输入作者', trigger: 'blur' }],
  coverUrl: [{ required: true, message: '请填写封面地址', trigger: 'blur' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
})

// 搜索栏引用
const searchBarRef = ref()
// 搜索表单数据
const searchForm = ref<{ title?: string; status?: string }>({ title: '', status: '' })

// 搜索栏配置项
const searchItems = computed(() => [
  { key: 'title', label: '标题', type: 'input', props: { placeholder: '标题关键字' } },
  {
    key: 'status',
    label: '状态',
    type: 'select',
    options: [
      { label: '全部', value: '' },
      { label: '已发布', value: '1' },
      { label: '已下线', value: '0' }
    ]
  }
])

// 表格相关逻辑
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
    apiFn: fetchGetNewsList,
    apiParams: {
      current: 1,
      size: 10,
      ...searchForm.value
    },
    columnsFactory: () => [
      { type: 'selection', width: 50 },
      { type: 'globalIndex', width: 60, label: '序号' },
      { prop: 'title', label: '标题' },
      { prop: 'author', label: '作者', width: 120 },
      { prop: 'status', label: '状态', useSlot: true, width: 100 },
      { prop: 'publishTime', label: '发布时间', width: 180 },
      { prop: 'operation', label: '操作', useSlot: true, width: 160, fixed: 'right' }
    ]
  }
})

// 对话框标题
const dialogTitle = computed(() => (dialogType.value === 'edit' ? '编辑新闻' : '新增新闻'))

// 处理搜索
const handleSearch = async () => {
  await searchBarRef.value?.validate?.()
  const params = { ...searchForm.value }
  Object.assign(searchParams, params)
  getData()
}

// 处理重置
const handleReset = () => {
  resetSearchParams()
}

// 显示对话框（新增或编辑）
const showDialog = (type: 'add' | 'edit', row?: NewsItem) => {
  dialogType.value = type
  if (row) {
    // 确保 status 为字符串，解决后端可能返回数字导致 select 无法回显的问题
    Object.assign(current, { ...row, status: String(row.status) })
  } else {
    Object.assign(current, { title: '', content: '', coverUrl: '', status: '1', author: '' })
  }
  nextTick(() => {
    dialogVisible.value = true
  })
}

// 处理对话框提交
const handleDialogSubmit = async () => {
  await formRef.value?.validate?.()
  if (current.id) {
    await updateNews(current.id, current)
    dialogVisible.value = false
    refreshUpdate()
    ElMessage.success('保存成功')
  } else {
    await createNews(current)
    dialogVisible.value = false
    refreshCreate()
    ElMessage.success('保存成功')
  }
}

// 处理删除
const handleDelete = async (row: NewsItem) => {
  await ElMessageBox.confirm(`确认删除新闻「${row.title}」吗？`, '提示', { type: 'warning' })
  await deleteNews(row.id)
  ElMessage.success('删除成功')
  refreshRemove()
}

const coverUploadRequest = async (options: any) => {
  const file = options.file as File
  const res = await uploadFile({ file, bizTag: 'NEWS_COVER' })
  current.coverUrl = res.url
  options.onSuccess?.(res)
}
</script>

<style scoped>
.cover-preview {
  display: block;
  margin-top: 8px;
  width: 160px;
  height: 90px;
  border-radius: 6px;
}
</style>
