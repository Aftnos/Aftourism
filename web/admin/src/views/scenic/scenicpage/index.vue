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
            <ElButton type="primary" @click="showDialog('add')" v-ripple>新增景区</ElButton>
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
        <template #imageUrl="{ row }">
          <ElImage :src="row.imageUrl" fit="cover" style="width:80px;height:50px;border-radius:6px" v-if="row.imageUrl" />
        </template>
        <template #operation="{ row }">
          <div class="flex">
            <ArtButtonTable type="edit" :row="row" @click="showDialog('edit', row)" />
            <ArtButtonTable type="delete" :row="row" @click="handleDelete(row)" />
          </div>
        </template>
      </ArtTable>

      <ElDialog v-model="dialogVisible" :title="dialogTitle" width="760px">
        <ElForm ref="formRef" :model="current" :rules="rules" label-width="90px">
          <ElRow :gutter="12">
            <ElCol :span="12">
              <ElFormItem label="名称" prop="name">
                <ElInput v-model="current.name" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="12">
              <ElFormItem label="高德ID" prop="amapId">
                <ElSpace>
                  <ElInput v-model="current.amapId" placeholder="高德POI ID" />
                  <ElButton :icon="View" circle @click="openAmapPreview" />
                </ElSpace>
              </ElFormItem>
            </ElCol>
            <ElCol :span="12">
              <ElFormItem label="等级" prop="level">
                <ElInput v-model="current.level" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="12">
              <ElFormItem label="类型标签" prop="tags">
                <ElInput v-model="current.tags" placeholder="多个标签用分号分隔" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="12">
              <ElFormItem label="门票" prop="ticketPrice">
                <ElInputNumber v-model="current.ticketPrice" :min="0" :step="1" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="12">
              <ElFormItem label="排序" prop="sort">
                <ElInputNumber v-model="current.sort" :min="0" :step="1" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="24">
              <ElFormItem label="地址" prop="address">
                <ElInput v-model="current.address" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="8">
              <ElFormItem label="省份" prop="province">
                <ElInput v-model="current.province" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="8">
              <ElFormItem label="城市" prop="city">
                <ElInput v-model="current.city" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="8">
              <ElFormItem label="区县" prop="district">
                <ElInput v-model="current.district" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="12">
              <ElFormItem label="电话" prop="phone">
                <ElInput v-model="current.phone" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="12">
              <ElFormItem label="官网" prop="website">
                <ElInput v-model="current.website" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="12">
              <ElFormItem label="经度" prop="longitude">
                <ElInputNumber v-model="current.longitude" :step="0.000001" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="12">
              <ElFormItem label="纬度" prop="latitude">
                <ElInputNumber v-model="current.latitude" :step="0.000001" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="24">
              <ElFormItem label="开放时间" prop="openTime">
                <ElInput v-model="current.openTime" placeholder="如 09:00-17:30" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="24">
              <ElFormItem label="图片" prop="imageUrl">
                <ElUpload :http-request="imageUploadRequest" :show-file-list="false" :limit="1" accept="image/*">
                  <ElButton type="primary">上传图片</ElButton>
                </ElUpload>
                <ElImage v-if="current.imageUrl" :src="current.imageUrl" fit="cover" style="display:block;margin-top:8px;width:160px;height:90px;border-radius:6px" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="24">
              <ElFormItem label="图片列表" prop="imageUrls">
                <div class="image-list-editor">
                  <div v-for="(item, index) in imageUrlList" :key="index" class="image-row">
                    <ElInput v-model="imageUrlList[index]" placeholder="图片链接" />
                    <ElButton :icon="View" circle @click="previewImage(item)" />
                    <ElButton type="danger" circle @click="removeImage(index)">-</ElButton>
                  </div>
                  <ElButton type="primary" plain @click="addImage">添加图片</ElButton>
                </div>
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
      <ElDialog v-model="previewVisible" title="图片预览" width="680px">
        <ElImage v-if="previewUrl" :src="previewUrl" fit="contain" style="width:100%;height:480px" />
      </ElDialog>
    </ElCard>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, nextTick, watch } from 'vue'
import { ElMessage, ElMessageBox, ElImage, ElUpload, ElButton, ElSpace, ElInput, ElInputNumber, ElRow, ElCol, type FormInstance } from 'element-plus'
import { View } from '@element-plus/icons-vue'
import ArtSearchBar from '@/components/core/forms/art-search-bar/index.vue'
import ArtTableHeader from '@/components/core/tables/art-table-header/index.vue'
import ArtTable from '@/components/core/tables/art-table/index.vue'
import ArtButtonTable from '@/components/core/forms/art-button-table/index.vue'
import ArtWangEditor from '@/components/core/forms/art-wang-editor/index.vue'
import { useTable } from '@/hooks/core/useTable'
import { fetchGetScenicPage, createScenic, updateScenic, deleteScenic } from '@/api/scenic'
import { uploadFile } from '@/api/file'

defineOptions({ name: 'ScenicPage' })

type ScenicSpot = Api.Scenic.ScenicSpotVO

const dialogType = ref<'add' | 'edit'>('add')
const dialogVisible = ref(false)
const previewVisible = ref(false)
const previewUrl = ref('')
const current = reactive<Partial<ScenicSpot>>({
  name: '',
  amapId: '',
  tags: '',
  imageUrl: '',
  imageUrls: '',
  level: '',
  ticketPrice: 0,
  address: '',
  province: '',
  city: '',
  district: '',
  openTime: '',
  intro: '',
  phone: '',
  website: '',
  longitude: undefined,
  latitude: undefined,
  sort: 0
})
const formRef = ref<FormInstance>()
const imageUrlList = ref<string[]>([])

const rules = reactive({
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  address: [{ required: true, message: '请输入地址', trigger: 'blur' }]
})

const searchBarRef = ref()
const searchForm = ref<{ name?: string; address?: string; level?: string; tags?: string; city?: string }>({
  name: '',
  address: '',
  level: '',
  tags: '',
  city: ''
})

const searchItems = computed(() => [
  { key: 'name', label: '名称', type: 'input', props: { placeholder: '名称关键字' } },
  { key: 'level', label: '等级', type: 'input', props: { placeholder: '如 5A/4A' } },
  { key: 'tags', label: '标签', type: 'input', props: { placeholder: '类型标签' } },
  { key: 'city', label: '城市', type: 'input', props: { placeholder: '城市关键字' } },
  { key: 'address', label: '地址', type: 'input', props: { placeholder: '地址关键字' } }
])

const { columns, columnChecks, data, loading, pagination, getData, searchParams, resetSearchParams, handleSizeChange, handleCurrentChange, refreshData, refreshCreate, refreshUpdate, refreshRemove } = useTable({
  core: {
    apiFn: async (params) => {
      const res = await fetchGetScenicPage(params)
      return { data: res, records: res.list, total: res.total, current: res.pageNum, size: res.pageSize }
    },
    apiParams: { current: 1, size: 10, ...searchForm.value },
    columnsFactory: () => [
      { type: 'selection', width: 50 },
      { type: 'globalIndex', width: 60, label: '序号' },
      { prop: 'imageUrl', label: '图片', useSlot: true, width: 110 },
      { prop: 'name', label: '名称', minWidth: 160 },
      { prop: 'amapId', label: '高德ID', width: 140 },
      { prop: 'level', label: '等级', width: 80 },
      { prop: 'tags', label: '标签', minWidth: 160 },
      { prop: 'ticketPrice', label: '门票', width: 90 },
      { prop: 'address', label: '地址', minWidth: 200 },
      { prop: 'city', label: '城市', width: 100 },
      { prop: 'district', label: '区县', width: 100 },
      { prop: 'openTime', label: '开放时间', width: 140 },
      { prop: 'website', label: '官网', minWidth: 160 },
      { prop: 'sort', label: '排序', width: 80 },
      { prop: 'createTime', label: '创建时间', width: 160 },
      { prop: 'operation', label: '操作', useSlot: true, width: 160, fixed: 'right' }
    ]
  }
})

const dialogTitle = computed(() => (dialogType.value === 'edit' ? '编辑景区' : '新增景区'))

const handleSearch = async () => {
  await searchBarRef.value?.validate?.()
  Object.assign(searchParams, searchForm.value)
  getData()
}

const handleReset = () => {
  resetSearchParams()
}

const showDialog = (type: 'add' | 'edit', row?: ScenicSpot) => {
  dialogType.value = type
  Object.assign(current, row || {
    name: '',
    amapId: '',
    tags: '',
    imageUrl: '',
    imageUrls: '',
    level: '',
    ticketPrice: 0,
    address: '',
    province: '',
    city: '',
    district: '',
    openTime: '',
    intro: '',
    phone: '',
    website: '',
    longitude: undefined,
    latitude: undefined,
    sort: 0
  })
  setImageUrlList(current.imageUrls)
  nextTick(() => (dialogVisible.value = true))
}

const handleDialogSubmit = async () => {
  await formRef.value?.validate?.()
  syncImageUrlValue()
  const payload = { ...current }
  if (current.id) {
    await updateScenic(current.id, payload)
    dialogVisible.value = false
    refreshUpdate()
    ElMessage.success('保存成功')
  } else {
    await createScenic(payload)
    dialogVisible.value = false
    refreshCreate()
    ElMessage.success('保存成功')
  }
}

const handleDelete = async (row: ScenicSpot) => {
  await ElMessageBox.confirm(`确认删除景区「${row.name}」吗？`, '提示', { type: 'warning' })
  await deleteScenic(row.id)
  ElMessage.success('删除成功')
  refreshRemove()
}

const imageUploadRequest = async (options: any) => {
  const file = options.file as File
  const res = await uploadFile({ file, bizTag: 'SCENIC_IMAGE' })
  current.imageUrl = res.url
  options.onSuccess?.(res)
}

const setImageUrlList = (value?: string) => {
  imageUrlList.value = value
    ? value.split(';').map(item => item.trim()).filter(Boolean)
    : []
}

const syncImageUrlValue = () => {
  current.imageUrls = imageUrlList.value.map(item => item.trim()).filter(Boolean).join(';')
}

watch(imageUrlList, () => {
  syncImageUrlValue()
}, { deep: true })

const addImage = () => {
  imageUrlList.value.push('')
}

const removeImage = (index: number) => {
  imageUrlList.value.splice(index, 1)
}

const previewImage = (url?: string) => {
  const target = url?.trim()
  if (!target) {
    ElMessage.warning('请先输入图片链接')
    return
  }
  previewUrl.value = target
  previewVisible.value = true
}

const openAmapPreview = () => {
  if (!current.amapId) {
    ElMessage.warning('请先填写高德ID')
    return
  }
  window.open(`https://gaode.com/place/${current.amapId}`, '_blank')
}
</script>

<style scoped>
.image-list-editor {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.image-row {
  display: grid;
  grid-template-columns: 1fr auto auto;
  gap: 8px;
  align-items: center;
}
</style>
