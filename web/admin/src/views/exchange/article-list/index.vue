<template>
  <div class="page-content">
    <div class="flex-cb">
      <div>
        <h1 class="text-2xl font-semibold">交流文章管理</h1>
        <p class="text-g-500 mt-2">审核与管理用户提交的交流文章</p>
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
      <ElTableColumn prop="title" label="标题" min-width="220" />
      <ElTableColumn label="作者" min-width="160">
        <template #default="{ row }">
          <span>{{ row.userNickname || row.userName || '未知用户' }}</span>
        </template>
      </ElTableColumn>
      <ElTableColumn prop="statusText" label="状态" min-width="120">
        <template #default="{ row }">
          <ElTag :type="statusTagType(row.status)" effect="plain">
            {{ row.statusText || statusText(row.status) }}
          </ElTag>
        </template>
      </ElTableColumn>
      <ElTableColumn prop="likeCount" label="点赞" width="80" />
      <ElTableColumn prop="commentCount" label="评论" width="80" />
      <ElTableColumn prop="createTime" label="创建时间" min-width="160" />
      <ElTableColumn label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <ElButton link type="primary" @click="openAudit(row)">审核</ElButton>
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

    <ElDialog v-model="dialogVisible" width="520px" title="交流文章审核">
      <template v-if="currentArticle">
        <div class="mb-4 text-sm text-g-600">
          <div>标题：{{ currentArticle.title }}</div>
          <div>作者：{{ currentArticle.userNickname || currentArticle.userName || '未知用户' }}</div>
        </div>
        <ElForm ref="auditFormRef" :model="auditForm" label-width="90px">
          <ElFormItem label="审核状态">
            <ElRadioGroup v-model="auditForm.status">
              <ElRadio :label="1">通过</ElRadio>
              <ElRadio :label="2">驳回</ElRadio>
            </ElRadioGroup>
          </ElFormItem>
          <ElFormItem label="审核备注">
            <ElInput v-model="auditForm.auditRemark" type="textarea" :rows="3" maxlength="200" show-word-limit />
          </ElFormItem>
        </ElForm>
      </template>
      <template #footer>
        <ElButton @click="dialogVisible = false">取消</ElButton>
        <ElButton type="primary" @click="submitAudit">确认</ElButton>
      </template>
    </ElDialog>
  </div>
</template>

<script setup lang="ts">
  import { computed, reactive, ref } from 'vue'
  import { ElMessage, type FormInstance } from 'element-plus'
  import ArtSearchBar from '@/components/core/forms/art-search-bar/index.vue'
  import { useTable } from '@/hooks/core/useTable'
  import { fetchExchangeArticlePage, auditExchangeArticle } from '@/api/exchange'

  defineOptions({ name: 'ExchangeArticleManage' })

  type ExchangeArticleItem = Api.Exchange.ExchangeArticleItem

  const searchBarRef = ref()
  const searchForm = ref<{ status?: number | null; keyword?: string }>({
    status: null,
    keyword: ''
  })

  const searchItems = computed(() => [
    {
      key: 'status',
      label: '状态',
      type: 'select',
      props: {
        options: [
          { label: '全部', value: null },
          { label: '待审核', value: 0 },
          { label: '已发布', value: 1 },
          { label: '已驳回', value: 2 }
        ]
      }
    },
    { key: 'keyword', label: '关键词', type: 'input', props: { placeholder: '标题/作者' } }
  ])

  const { data, loading, pagination, getData, searchParams, resetSearchParams, handleCurrentChange } = useTable({
    core: {
      apiFn: fetchExchangeArticlePage,
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

  const statusText = (status?: number) => {
    if (status === 1) return '已发布'
    if (status === 2) return '已驳回'
    return '待审核'
  }
  const statusTagType = (status?: number) => {
    if (status === 1) return 'success'
    if (status === 2) return 'danger'
    return 'warning'
  }

  const handleSearch = async () => {
    await searchBarRef.value?.validate?.()
    Object.assign(searchParams, {
      current: 1,
      status: searchForm.value.status ?? undefined,
      keyword: searchForm.value.keyword || undefined
    })
    getData()
  }

  const handleReset = () => {
    resetSearchParams()
    searchForm.value = { status: null, keyword: '' }
    getData()
  }

  const dialogVisible = ref(false)
  const currentArticle = ref<ExchangeArticleItem | null>(null)
  const auditFormRef = ref<FormInstance>()
  const auditForm = reactive({ status: 1, auditRemark: '' })

  const openAudit = (row: ExchangeArticleItem) => {
    currentArticle.value = row
    auditForm.status = row.status === 2 ? 2 : 1
    auditForm.auditRemark = row.auditRemark || ''
    dialogVisible.value = true
  }

  const submitAudit = async () => {
    if (!currentArticle.value) return
    if (!auditFormRef.value) return
    await auditFormRef.value.validate()
    await auditExchangeArticle(currentArticle.value.id, {
      status: auditForm.status,
      auditRemark: auditForm.auditRemark || undefined
    })
    ElMessage.success('审核已更新')
    dialogVisible.value = false
    getData()
  }
</script>

<style scoped lang="scss">
  .page-content {
    padding: 24px;
  }
</style>
