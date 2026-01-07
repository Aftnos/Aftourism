<template>
  <div>
    <h1 class="text-3xl font-medium mt-5">留言反馈</h1>
    <p class="mt-3 text-g-600">管理用户提交的留言与反馈，支持标记处理状态。</p>

    <ArtSearchBar
      ref="searchBarRef"
      v-model="searchForm"
      :items="searchItems"
      class="mt-6"
      @search="handleSearch"
      @reset="handleReset"
    />

    <ul
      class="mt-6 grid grid-cols-4 gap-5 max-2xl:grid-cols-3 max-xl:grid-cols-2 max-lg:grid-cols-1 mb-5"
      v-loading="loading"
    >
      <li
        class="relative p-4 c-p aspect-16/12 duration-300 hover:-translate-y-1.5 rounded-2xl"
        :style="{ background: item.color }"
        v-for="item in feedbackCards"
        :key="item.id"
        @click="openDrawer(item)"
      >
        <p class="text-g-600 text-sm">{{ formatTime(item.createTime) }}</p>
        <div class="flex items-center gap-2 mt-2">
          <ElTag size="small" :type="item.type === 'FEEDBACK' ? 'warning' : 'info'">
            {{ typeText(item.type) }}
          </ElTag>
          <ElTag size="small" :type="statusTagType(item.status)" effect="plain">
            {{ statusText(item.status) }}
          </ElTag>
        </div>
        <p class="mt-3 text-sm text-gray-800 line-clamp-4">{{ item.content }}</p>
        <div class="absolute bottom-4 left-0 px-4 flex-cb w-full text-xs text-g-600">
          <span>{{ item.userNickname || item.userName || '匿名' }}</span>
          <span>{{ item.title || '未填写标题' }}</span>
        </div>
      </li>
    </ul>

    <div class="flex justify-end" v-if="feedbackCards.length">
      <ElPagination
        background
        layout="prev, pager, next"
        :current-page="pagination.current"
        :page-size="pagination.size"
        :total="pagination.total"
        @current-change="handleCurrentChange"
      />
    </div>

    <ElDrawer
      v-model="drawerVisible"
      :size="420"
      :lock-scroll="false"
      modal-class="comment-modal"
    >
      <template #header>
        <h4>留言反馈详情</h4>
      </template>
      <template #default>
        <div v-if="detail" class="drawer-default">
          <div class="relative p-4 rounded-2xl" :style="{ background: detail.color }">
            <p class="text-g-500 text-sm">{{ formatTime(detail.createTime) }}</p>
            <p class="mt-4 text-sm text-gray-800">{{ detail.content }}</p>
            <div class="absolute bottom-4 left-0 px-4 flex-cb w-full text-xs text-g-600">
              <span>{{ detail.userNickname || detail.userName || '匿名' }}</span>
              <span>{{ detail.title || '未填写标题' }}</span>
            </div>
          </div>

          <ElDivider />

          <ElForm ref="editFormRef" :model="editForm" :rules="editRules" label-width="90px">
            <ElFormItem label="类型" prop="type">
              <ElSelect v-model="editForm.type" placeholder="请选择类型">
                <ElOption label="留言" value="MESSAGE" />
                <ElOption label="反馈" value="FEEDBACK" />
              </ElSelect>
            </ElFormItem>
            <ElFormItem label="标题" prop="title">
              <ElInput v-model="editForm.title" placeholder="可选填写标题" />
            </ElFormItem>
            <ElFormItem label="内容" prop="content">
              <ElInput v-model="editForm.content" type="textarea" :rows="4" maxlength="1000" show-word-limit />
            </ElFormItem>
            <ElFormItem label="联系电话">
              <ElInput v-model="editForm.contactPhone" placeholder="联系方式" />
            </ElFormItem>
            <ElFormItem label="联系邮箱">
              <ElInput v-model="editForm.contactEmail" placeholder="邮箱" />
            </ElFormItem>
            <ElFormItem label="已反馈" prop="status">
              <ElSwitch v-model="editForm.status" :active-value="1" :inactive-value="0" />
            </ElFormItem>
          </ElForm>

          <div class="flex justify-end gap-3">
            <ElButton type="danger" plain @click="handleDelete(detail.id)">删除</ElButton>
            <ElButton type="primary" @click="submitUpdate">保存</ElButton>
          </div>
        </div>
      </template>
    </ElDrawer>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, nextTick } from 'vue'
import { ElMessageBox, type FormInstance } from 'element-plus'
import ArtSearchBar from '@/components/core/forms/art-search-bar/index.vue'
import { useTable } from '@/hooks/core/useTable'
import { fetchFeedbackPage, fetchFeedbackDetail, updateFeedback, deleteFeedback } from '@/api/feedback'

defineOptions({ name: 'FeedbackManage' })

type FeedbackItem = Api.Feedback.FeedbackItem

const COLOR_LIST = ['#D8F8FF', '#FDDFD9', '#FCE6F0', '#D3F8F0', '#FFEABC', '#F5E1FF', '#E1E6FE']

const searchBarRef = ref()
const searchForm = ref<{ type?: string; status?: number | null; keyword?: string }>({
  type: '',
  status: null,
  keyword: ''
})

const searchItems = computed(() => [
  {
    key: 'type',
    label: '类型',
    type: 'select',
    props: {
      options: [
        { label: '全部', value: '' },
        { label: '留言', value: 'MESSAGE' },
        { label: '反馈', value: 'FEEDBACK' }
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
        { label: '待反馈', value: 0 },
        { label: '已反馈', value: 1 }
      ]
    }
  },
  { key: 'keyword', label: '关键词', type: 'input', props: { placeholder: '标题/内容/用户' } }
])

const { data, loading, pagination, getData, searchParams, resetSearchParams, handleCurrentChange } = useTable({
  core: {
    apiFn: fetchFeedbackPage,
    apiParams: { current: 1, size: 12 }
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

const feedbackCards = computed(() => {
  let lastColorIndex = -1
  return (data.value || []).map((item: FeedbackItem) => {
    let newIndex: number
    do {
      newIndex = Math.floor(Math.random() * COLOR_LIST.length)
    } while (newIndex === lastColorIndex && COLOR_LIST.length > 1)
    lastColorIndex = newIndex
    return { ...item, color: COLOR_LIST[newIndex] }
  })
})

const formatTime = (time?: string) => (time ? new Date(time).toLocaleString() : '-')
const typeText = (type?: string) => (type === 'FEEDBACK' ? '反馈' : '留言')
const statusText = (status?: number) => (status === 1 ? '已反馈' : '待反馈')
const statusTagType = (status?: number) => (status === 1 ? 'success' : 'warning')

const handleSearch = async () => {
  await searchBarRef.value?.validate?.()
  Object.assign(searchParams, {
    current: 1,
    type: searchForm.value.type || undefined,
    status: searchForm.value.status ?? undefined,
    keyword: searchForm.value.keyword || undefined
  })
  getData()
}

const handleReset = () => {
  resetSearchParams()
  searchForm.value = { type: '', status: null, keyword: '' }
  getData()
}

const drawerVisible = ref(false)
const detail = ref<FeedbackItem & { color?: string } | null>(null)
const editFormRef = ref<FormInstance>()
const editForm = reactive<Api.Feedback.FeedbackUpdateRequest & { id?: number }>({
  type: 'MESSAGE',
  title: '',
  content: '',
  contactPhone: '',
  contactEmail: '',
  status: 0
})

const editRules = reactive({
  type: [{ required: true, message: '请选择类型', trigger: 'change' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
})

const openDrawer = async (item: FeedbackItem & { color?: string }) => {
  const res = await fetchFeedbackDetail(item.id)
  detail.value = { ...res, color: item.color }
  Object.assign(editForm, {
    id: res.id,
    type: res.type,
    title: res.title || '',
    content: res.content,
    contactPhone: res.contactPhone || '',
    contactEmail: res.contactEmail || '',
    status: res.status
  })
  nextTick(() => (drawerVisible.value = true))
}

const submitUpdate = async () => {
  if (!editFormRef.value) return
  await editFormRef.value.validate()
  if (!editForm.id) return
  await updateFeedback(editForm.id, {
    type: editForm.type,
    title: editForm.title || undefined,
    content: editForm.content,
    contactPhone: editForm.contactPhone || undefined,
    contactEmail: editForm.contactEmail || undefined,
    status: editForm.status
  })
  drawerVisible.value = false
  getData()
}

const handleDelete = async (id?: number) => {
  if (!id) return
  await ElMessageBox.confirm('确定删除该条留言反馈吗？', '提示', { type: 'warning' })
  await deleteFeedback(id)
  drawerVisible.value = false
  getData()
}
</script>
