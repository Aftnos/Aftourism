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
            <ElButton type="primary" @click="showCreateDialog()" v-ripple>新增评论</ElButton>
            <ElRadioGroup v-model="viewMode" @change="handleViewModeChange">
              <ElRadioButton label="threaded">楼中楼</ElRadioButton>
              <ElRadioButton label="flat">全部评论</ElRadioButton>
            </ElRadioGroup>
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
        <template #operation="{ row }">
          <div class="flex">
            <ArtButtonTable type="view" :row="row" @click="openDetail(row)" />
            <ArtButtonTable type="add" :row="row" @click="showReplyDialog(row)" />
            <ArtButtonTable type="edit" :row="row" @click="showEditDialog(row)" />
            <ArtButtonTable type="delete" :row="row" @click="handleDelete(row)" />
          </div>
        </template>
      </ArtTable>

      <ElDialog v-model="detailVisible" title="评论详情" width="800px">
        <div v-if="detail">
          <ElCard shadow="never" class="mb-3">
            <p>评论ID：{{ detail.comment.id }}</p>
            <p>活动ID：{{ detail.comment.activityId }}</p>
            <p>用户：{{ detail.comment.userNickname }}</p>
            <p>内容：{{ detail.comment.content }}</p>
            <p>时间：{{ detail.comment.createTime }}</p>
          </ElCard>
          <ElCard shadow="never">
            <template #header>
              <div class="flex-cb"><span>子回复（{{ detail.comment.childCount }}）</span></div>
            </template>
            <div>
              <div v-for="item in detail.replies" :key="item.id" class="p-2 border-b">
                <div class="flex-cb">
                  <span>{{ item.userNickname }}（ID:{{ item.id }}）</span>
                  <div class="flex gap-2">
                    <ElButton size="small" @click="showReplyDialog(detail.comment, item)">回复</ElButton>
                    <ElButton size="small" @click="showEditDialog(item)">编辑</ElButton>
                    <ElButton size="small" type="danger" @click="handleDelete(item)">删除</ElButton>
                  </div>
                </div>
                <p class="m-0 mt-1">{{ item.content }}</p>
                <p class="m-0 mt-1 text-g-700">时间：{{ item.createTime }}</p>
              </div>
            </div>
          </ElCard>
        </div>
        <template #footer>
          <ElButton @click="detailVisible = false">关闭</ElButton>
        </template>
      </ElDialog>

      <ElDialog v-model="editVisible" :title="editTitle" width="640px">
        <ElForm ref="editFormRef" :model="editForm" :rules="editRules" label-width="90px">
          <ElFormItem label="评论ID" prop="id">
            <ElInput v-model="editForm.id" disabled />
          </ElFormItem>
          <ElFormItem label="活动ID" prop="activityId">
            <ElInputNumber v-model="editForm.activityId" :min="1" />
          </ElFormItem>
          <ElFormItem label="是否父评论">
            <ElSwitch v-model="isEditParent" @change="handleEditParentSwitch" />
          </ElFormItem>
          <ElFormItem v-if="!isEditParent" label="父评论ID" prop="parentId">
            <ElInputNumber v-model="editForm.parentId" :min="0" />
          </ElFormItem>
          <ElFormItem label="内容" prop="content">
            <ElInput v-model="editForm.content" type="textarea" rows="4" />
          </ElFormItem>
        </ElForm>
        <template #footer>
          <ElButton @click="editVisible = false">取消</ElButton>
          <ElButton type="primary" @click="submitEdit">保存</ElButton>
        </template>
      </ElDialog>

      <ElDialog v-model="createVisible" title="新增评论/回复" width="640px">
        <ElForm ref="createFormRef" :model="createForm" :rules="createRules" label-width="90px">
          <ElFormItem label="活动ID" prop="activityId">
            <ElInputNumber v-model="createForm.activityId" :min="1" />
          </ElFormItem>
          <ElFormItem label="是否父评论">
            <ElSwitch v-model="isParent" @change="handleParentSwitch" />
          </ElFormItem>
          <ElFormItem v-if="!isParent" label="父评论ID" prop="parentId">
            <ElInputNumber v-model="createForm.parentId" :min="0" />
          </ElFormItem>
          <ElFormItem label="用户ID" prop="userId">
            <ElInputNumber v-model="createForm.userId" :min="1" />
          </ElFormItem>
          <ElFormItem label="内容" prop="content">
            <ElInput v-model="createForm.content" type="textarea" rows="4" />
          </ElFormItem>
        </ElForm>
        <template #footer>
          <ElButton @click="createVisible = false">取消</ElButton>
          <ElButton type="primary" @click="submitCreate">提交</ElButton>
        </template>
      </ElDialog>
    </ElCard>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, nextTick } from 'vue'
import { ElMessage, ElMessageBox, ElSpace, ElButton, ElInput, ElInputNumber, ElRadioGroup, ElRadioButton, ElSwitch, type FormInstance } from 'element-plus'
import ArtSearchBar from '@/components/core/forms/art-search-bar/index.vue'
import ArtTableHeader from '@/components/core/tables/art-table-header/index.vue'
import ArtTable from '@/components/core/tables/art-table/index.vue'
import ArtButtonTable from '@/components/core/forms/art-button-table/index.vue'
import { useTable } from '@/hooks/core/useTable'
import { fetchGetAllCommentsPage, fetchGetCommentsByActivity, createComment, getCommentDetail, updateComment, deleteComment, fetchCommentChildren } from '@/api/activity-comment'

defineOptions({ name: 'ActivityCommentPage' })

type CommentVO = Api.Activity.ActivityCommentVO
type CommentDetailVO = Api.Activity.ActivityCommentDetailVO

const viewMode = ref<'threaded' | 'flat'>('threaded')
const handleViewModeChange = () => {
  refreshData()
}

const isParent = ref(true)
const handleParentSwitch = () => {
  if (isParent.value) createForm.parentId = null
}

const searchBarRef = ref()
const searchForm = ref<{ activityId?: number | string; parentId?: number | string; commentId?: number | string }>({ activityId: '', parentId: '', commentId: '' })

const searchItems = computed(() => [
  { key: 'activityId', label: '活动ID', type: 'input', props: { placeholder: '按活动筛选' } },
  { key: 'parentId', label: '父评论ID', type: 'input', props: { placeholder: '按父级筛选' } },
  { key: 'commentId', label: '评论ID', type: 'input', props: { placeholder: '按评论ID查看详情' } }
])

const { columns, columnChecks, data, loading, pagination, getData, searchParams, resetSearchParams, handleSizeChange, handleCurrentChange, refreshData, refreshRemove } = useTable({
  core: {
    apiFn: async (params) => {
      const activityId = Number(searchForm.value.activityId)
      const requestParams: any = { ...params }
      if (searchForm.value.parentId !== '') requestParams.parentId = Number(searchForm.value.parentId)
      if (!Number.isNaN(activityId) && activityId > 0) {
        const res = await fetchGetCommentsByActivity(activityId, requestParams)
        if (viewMode.value === 'flat') {
          const childrenLists = await Promise.all(res.list.map((r: CommentVO) => (r.childCount > 0 ? fetchCommentChildren(r.id) : Promise.resolve([]))))
          const flat = ([] as CommentVO[]).concat(...childrenLists)
          const combined = res.list.concat(flat)
          return { data: res, records: combined, total: combined.length, current: res.pageNum, size: res.pageSize }
        }
        return { data: res, records: res.list, total: res.total, current: res.pageNum, size: res.pageSize }
      } else {
        const res = await fetchGetAllCommentsPage(requestParams)
        if (viewMode.value === 'flat') {
          const childrenLists = await Promise.all(res.list.map((r: CommentVO) => (r.childCount > 0 ? fetchCommentChildren(r.id) : Promise.resolve([]))))
          const flat = ([] as CommentVO[]).concat(...childrenLists)
          const combined = res.list.concat(flat)
          return { data: res, records: combined, total: combined.length, current: res.pageNum, size: res.pageSize }
        }
        return { data: res, records: res.list, total: res.total, current: res.pageNum, size: res.pageSize }
      }
    },
    apiParams: { current: 1, size: 10 },
    columnsFactory: () => [
      { type: 'selection', width: 50 },
      { type: 'globalIndex', width: 60, label: '序号' },
      { prop: 'id', label: '评论ID', width: 100 },
      { prop: 'activityId', label: '活动ID', width: 100 },
      { prop: 'userNickname', label: '用户', width: 140 },
      { prop: 'content', label: '内容', minWidth: 240 },
      { prop: 'parentId', label: '父评论ID', width: 120 },
      { prop: 'childCount', label: '子数', width: 90 },
      { prop: 'likeCount', label: '点赞', width: 90 },
      { prop: 'createTime', label: '时间', width: 170 },
      { prop: 'operation', label: '操作', useSlot: true, width: 220, fixed: 'right' }
    ]
  }
})

const handleSearch = async () => {
  await searchBarRef.value?.validate?.()
  const params: any = {}
  if (searchForm.value.parentId !== '') params.parentId = Number(searchForm.value.parentId)
  Object.assign(searchParams, params)
  if (searchForm.value.commentId !== '') {
    const id = Number(searchForm.value.commentId)
    openDetailById(id)
  } else {
    getData()
  }
}

const handleReset = () => {
  resetSearchParams()
}

const detailVisible = ref(false)
const detail = ref<CommentDetailVO | null>(null)

const openDetail = async (row: CommentVO) => {
  const res = await getCommentDetail(row.id)
  detail.value = res
  detailVisible.value = true
}

const openDetailById = async (commentId: number) => {
  const res = await getCommentDetail(commentId)
  detail.value = res
  detailVisible.value = true
}

const editVisible = ref(false)
const editTitle = computed(() => '编辑评论')
const editFormRef = ref<FormInstance>()
const editForm = reactive<{ id?: number; activityId?: number; parentId?: number | null; content?: string; userId?: number }>({})
const editRules = reactive({ content: [{ required: true, message: '请输入内容', trigger: 'blur' }] })
const isEditParent = ref(true)
const handleEditParentSwitch = () => {
  if (isEditParent.value) editForm.parentId = null
}

const createVisible = ref(false)
const createFormRef = ref<FormInstance>()
const createForm = reactive<{ activityId?: number; parentId?: number | null; userId?: number; content?: string }>({})
const createRules = reactive({ activityId: [{ required: true, message: '请输入活动ID', trigger: 'blur' }], userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }], content: [{ required: true, message: '请输入内容', trigger: 'blur' }] })

const showEditDialog = (row: CommentVO) => {
  Object.assign(editForm, { id: row.id, activityId: row.activityId, parentId: row.parentId ?? null, content: row.content, userId: row.userId })
  isEditParent.value = row.parentId == null
  nextTick(() => (editVisible.value = true))
}

const submitEdit = async () => {
  await editFormRef.value?.validate?.()
  if (!editForm.id) return
  await updateComment(editForm.id, { content: editForm.content, parentId: isEditParent.value ? null : editForm.parentId ?? null, userId: editForm.userId })
  editVisible.value = false
  refreshData()
  ElMessage.success('保存成功')
}

const showCreateDialog = () => {
  Object.assign(createForm, { activityId: undefined, parentId: null, userId: undefined, content: '' })
  isParent.value = true
  nextTick(() => (createVisible.value = true))
}

const showReplyDialog = (parent: CommentVO, _child?: CommentVO) => {
  Object.assign(createForm, { activityId: parent.activityId, parentId: parent.id, userId: undefined, content: '' })
  isParent.value = false
  nextTick(() => (createVisible.value = true))
}

const submitCreate = async () => {
  await createFormRef.value?.validate?.()
  if (!createForm.activityId) return
  await createComment(createForm.activityId, { userId: createForm.userId!, content: createForm.content!, parentId: isParent.value ? null : createForm.parentId ?? null })
  createVisible.value = false
  refreshData()
  ElMessage.success('新增成功')
}

const handleDelete = async (row: CommentVO) => {
  await ElMessageBox.confirm(`确认删除评论「${row.id}」吗？`, '提示', { type: 'warning' })
  await deleteComment(row.id)
  ElMessage.success('删除成功')
  refreshRemove()
}
</script>

<style scoped>
.mb-3 { margin-bottom: 12px; }
.border-b { border-bottom: 1px solid var(--el-border-color); }
</style>
