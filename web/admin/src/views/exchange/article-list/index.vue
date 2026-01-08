<template>
  <div class="page-content !mb-5">
    <ElRow justify="space-between" :gutter="10">
      <ElCol :lg="7" :md="8" :sm="12" :xs="16">
        <ElInput
          v-model="searchVal"
          :prefix-icon="Search"
          clearable
          placeholder="输入标题或作者查询"
          @keyup.enter="handleSearch"
        />
      </ElCol>
      <ElCol :lg="8" :md="8" :sm="12" :xs="8">
        <ElSelect v-model="statusVal" clearable placeholder="筛选状态" @change="handleSearch">
          <ElOption :value="null" label="全部" />
          <ElOption :value="0" label="待审核" />
          <ElOption :value="1" label="已发布" />
          <ElOption :value="2" label="已驳回" />
        </ElSelect>
      </ElCol>
      <ElCol :lg="4" :md="4" :sm="0" :xs="0" class="flex justify-end">
        <ElButton @click="handleReset">重置</ElButton>
      </ElCol>
    </ElRow>

    <div class="mt-5">
      <div
        class="grid grid-cols-4 gap-5 max-2xl:grid-cols-3 max-xl:grid-cols-2 max-sm:grid-cols-1"
        v-loading="loading"
      >
        <div
          v-for="item in tableData"
          :key="item.id"
          class="exchange-article-card group overflow-hidden border border-g-300/60 rounded-custom-sm"
        >
          <div class="relative aspect-[16/9.5]">
            <ElImage
              class="flex align-center justify-center w-full h-full object-cover bg-gray-200"
              :src="item.coverUrl || ''"
              fit="cover"
            >
              <template #error>
                <div class="flex h-full w-full items-center justify-center text-xs text-g-500">暂无封面</div>
              </template>
            </ElImage>
            <ElTag class="absolute top-2 right-2" size="small" :type="statusTagType(item.status)">
              {{ item.statusText || statusText(item.status) }}
            </ElTag>
          </div>
          <div class="px-3 py-2">
            <h2 class="text-base text-g-800 font-medium line-clamp-1">{{ item.title }}</h2>
            <div class="mt-1 text-sm text-g-500">作者：{{ item.userNickname || item.userName || '未知用户' }}</div>
            <div class="flex-b w-full h-7 mt-2 text-sm text-g-500">
              <div class="flex items-center gap-3">
                <span>👍 {{ item.likeCount || 0 }}</span>
                <span>💬 {{ item.commentCount || 0 }}</span>
                <span>{{ useDateFormat(item.createTime, 'YYYY-MM-DD') }}</span>
              </div>
              <ElButton
                class="opacity-0 group-hover:opacity-100"
                size="small"
                type="primary"
                @click.stop="openAudit(item)"
              >
                审核
              </ElButton>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div style="margin-top: 16vh" v-if="showEmpty">
      <ElEmpty description="暂无交流文章" />
    </div>

    <div style="display: flex; justify-content: center; margin-top: 20px">
      <ElPagination
        size="default"
        background
        v-model:current-page="pagination.current"
        :page-size="pagination.size"
        :pager-count="9"
        layout="prev, pager, next, total, jumper"
        :total="pagination.total"
        :hide-on-single-page="true"
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
  import { Search } from '@element-plus/icons-vue'
  import { useDateFormat } from '@vueuse/core'
  import { computed, reactive, ref, onMounted, watch } from 'vue'
  import { useRoute } from 'vue-router'
  import { ElMessage, type FormInstance } from 'element-plus'
  import { useTable } from '@/hooks/core/useTable'
  import { fetchExchangeArticleDetail, fetchExchangeArticlePage, auditExchangeArticle } from '@/api/exchange'

  defineOptions({ name: 'ExchangeArticleManage' })

  type ExchangeArticleItem = Api.Exchange.ExchangeArticleItem

  const searchVal = ref('')
  const statusVal = ref<number | null>(null)
  const route = useRoute()

  const { data, loading, pagination, getData, searchParams, handleCurrentChange } = useTable({
    core: {
      apiFn: fetchExchangeArticlePage,
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

  const tableData = computed(() => data.value || [])
  const showEmpty = computed(() => tableData.value.length === 0 && !loading.value)

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

  const handleSearch = () => {
    Object.assign(searchParams, {
      current: 1,
      status: statusVal.value ?? undefined,
      keyword: searchVal.value || undefined
    })
    getData()
  }

  const handleReset = () => {
    searchVal.value = ''
    statusVal.value = null
    Object.assign(searchParams, { current: 1, status: undefined, keyword: undefined })
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

  const openAuditById = async (id: number) => {
    if (!id) return
    const res = await fetchExchangeArticleDetail(id)
    openAudit(res)
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

  const tryOpenFromQuery = () => {
    const id = Number(route.query.id)
    if (Number.isNaN(id) || !id) return
    void openAuditById(id)
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

<style scoped lang="scss">
  .page-content {
    padding: 24px;
  }

  .exchange-article-card {
    transition: transform 0.2s ease, box-shadow 0.2s ease;
  }

  .exchange-article-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 12px 24px rgba(15, 23, 42, 0.08);
  }
</style>
