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
          <ElImage :src="row.imageUrl" fit="cover" class="w-20 h-12 rounded-md" v-if="row.imageUrl" :preview-src-list="[row.imageUrl]" preview-teleported />
        </template>
        <template #operation="{ row }">
          <div class="flex">
            <ArtButtonTable type="edit" :row="row" @click="showDialog('edit', row)" />
            <ArtButtonTable type="delete" :row="row" @click="handleDelete(row)" />
          </div>
        </template>
      </ArtTable>

      <ElDialog v-model="dialogVisible" :title="dialogTitle" width="800px" destroy-on-close @open="handleDialogOpen">
        <ElForm ref="formRef" :model="current" :rules="rules" label-width="100px" class="py-2">
          <div class="grid grid-cols-2 gap-x-6 gap-y-2">
            <ElFormItem label="名称" prop="name" class="col-span-1">
              <ElInput v-model="current.name" placeholder="请输入景区名称" />
            </ElFormItem>
            
            <ElFormItem label="高德ID" prop="amapId" class="col-span-1">
              <div class="flex w-full gap-2">
                <ElInput v-model="current.amapId" placeholder="高德POI ID" class="flex-1" />
                <ElButton :icon="View" @click="openAmapPreview" title="查看高德详情">预览</ElButton>
              </div>
            </ElFormItem>

            <ElFormItem label="等级" prop="level" class="col-span-1">
              <ElSelect v-model="current.level" placeholder="请选择等级" clearable class="w-full">
                <ElOption v-for="l in ['1A', '2A', '3A', '4A', '5A']" :key="l" :label="l" :value="l" />
              </ElSelect>
            </ElFormItem>

            <ElFormItem label="门票价格" prop="ticketPrice" class="col-span-1">
              <ElInputNumber v-model="current.ticketPrice" :min="0" :step="1" class="w-full" controls-position="right" />
            </ElFormItem>

            <ElFormItem label="排序权重" prop="sort" class="col-span-1">
              <ElInputNumber v-model="current.sort" :min="0" :step="1" class="w-full" controls-position="right" />
            </ElFormItem>

            <ElFormItem label="联系电话" prop="phone" class="col-span-1">
              <ElInput v-model="current.phone" placeholder="景区联系电话" />
            </ElFormItem>

            <ElFormItem label="类型标签" prop="tags" class="col-span-2">
              <div class="flex flex-col gap-2 w-full">
                <div class="flex gap-2">
                  <ElInput v-model="newTag" placeholder="输入标签" @keyup.enter="addTag" />
                  <ElButton type="primary" @click="addTag">添加</ElButton>
                </div>
                <div class="flex flex-wrap gap-1">
                  <ElTag
                    v-for="(tag, index) in tagsList"
                    :key="tag + index"
                    closable
                    @close="removeTag(index)"
                  >
                    {{ tag }}
                  </ElTag>
                </div>
              </div>
            </ElFormItem>

            <!-- 行政区划选择 -->
            <ElFormItem label="行政区划" required class="col-span-2">
              <ArtRegionSelect 
                v-model="regionModel" 
                @change="handleRegionChange"
              />
            </ElFormItem>

            <ElFormItem label="详细地址" prop="address" class="col-span-2">
              <ElInput v-model="current.address" placeholder="街道/门牌号等" />
            </ElFormItem>

            <ElFormItem label="官方网站" prop="website" class="col-span-2">
              <ElInput v-model="current.website" placeholder="http://" />
            </ElFormItem>

            <ElFormItem label="经度" prop="longitude" class="col-span-1">
              <ElInputNumber v-model="current.longitude" :step="0.000001" class="w-full" controls-position="right" placeholder="例如：116.397428" />
            </ElFormItem>

            <ElFormItem label="纬度" prop="latitude" class="col-span-1">
              <ElInputNumber v-model="current.latitude" :step="0.000001" class="w-full" controls-position="right" placeholder="例如：39.90923" />
            </ElFormItem>

            <ElFormItem label="开放时间" prop="openTime" class="col-span-2">
              <ElTimePicker
                v-model="openTimeRange"
                is-range
                range-separator="至"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                format="HH:mm"
                value-format="HH:mm"
                class="!w-full"
                @change="handleTimeChange"
              />
            </ElFormItem>

            <ElFormItem label="封面图片" prop="imageUrl" class="col-span-2">
              <div class="flex gap-4 items-start">
                <div class="w-32 h-20 rounded-lg border border-gray-200 bg-gray-50 flex items-center justify-center flex-shrink-0 overflow-hidden">
                  <ElImage 
                    v-if="current.imageUrl" 
                    :src="current.imageUrl" 
                    fit="cover" 
                    class="w-full h-full" 
                    :preview-src-list="[current.imageUrl]"
                    preview-teleported
                  />
                  <span v-else class="text-gray-400 text-xs">暂无图片</span>
                </div>
                <div class="flex flex-col gap-2 flex-1">
                  <div class="flex gap-2">
                    <ElUpload 
                      :http-request="imageUploadRequest" 
                      :show-file-list="false" 
                      accept="image/*"
                      class="w-fit"
                    >
                      <ElButton type="primary" plain>点击上传</ElButton>
                    </ElUpload>
                    <ElInput v-model="current.imageUrl" placeholder="请输入图片链接" clearable class="flex-1"/>
                  </div>
                  <div class="text-gray-400 text-xs">建议尺寸 16:9，支持 jpg/png 格式</div>
                </div>
              </div>
            </ElFormItem>

            <ElFormItem label="图片列表" prop="imageUrls" class="col-span-2">
              <div class="flex flex-col gap-3 w-full">
                <div class="flex gap-2">
                  <ElButton type="primary" plain @click="addImage">添加链接</ElButton>
                  <ElUpload 
                    :http-request="handleListUpload" 
                    :show-file-list="false" 
                    accept="image/*"
                  >
                    <ElButton type="success" plain>上传并添加</ElButton>
                  </ElUpload>
                </div>
                
                <div class="space-y-2 max-h-60 overflow-y-auto pr-2">
                  <VueDraggable v-model="imageUrlList" :animation="150" handle=".drag-handle">
                    <div v-for="(item, index) in imageUrlList" :key="item + index" class="flex items-center gap-2 p-2 bg-gray-50 rounded border border-gray-100 mb-2">
                      <div class="drag-handle cursor-move text-gray-400 hover:text-gray-600 p-1">
                        <ArtSvgIcon icon="ri:draggable" class="text-lg" />
                      </div>
                      <span class="text-gray-400 w-6 text-center">{{ index + 1 }}</span>
                      <ElImage 
                        :src="item" 
                        fit="cover" 
                        class="w-16 h-10 rounded bg-gray-100 flex-shrink-0"
                        :preview-src-list="imageUrlList"
                        :initial-index="index"
                        preview-teleported
                      >
                        <template #error>
                          <div class="w-full h-full flex items-center justify-center text-gray-300 bg-gray-50 text-xs">无图</div>
                        </template>
                      </ElImage>
                      <ElInput v-model="imageUrlList[index]" placeholder="图片链接" class="flex-1" />
                      <ElButton type="danger" text circle @click="removeImage(index)" title="删除">
                         <ArtSvgIcon icon="ri:delete-bin-line" />
                      </ElButton>
                    </div>
                  </VueDraggable>
                </div>
              </div>
            </ElFormItem>

            <ElFormItem label="景区简介" prop="intro" class="col-span-2">
              <ArtWangEditor v-model="current.intro" height="300px" :upload-config="{ server: '/api/file/upload' }" />
            </ElFormItem>
          </div>
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
import { VueDraggable } from 'vue-draggable-plus'
import { ref, reactive, computed, nextTick, watch } from 'vue'
import { ElMessage, ElMessageBox, ElImage, ElUpload, ElButton, ElSpace, ElInput, ElInputNumber, ElSelect, ElOption, ElTimePicker, ElTag } from 'element-plus'
import { View } from '@element-plus/icons-vue'
import ArtSearchBar from '@/components/core/forms/art-search-bar/index.vue'
import ArtTableHeader from '@/components/core/tables/art-table-header/index.vue'
import ArtTable from '@/components/core/tables/art-table/index.vue'
import ArtButtonTable from '@/components/core/forms/art-button-table/index.vue'
import ArtWangEditor from '@/components/core/forms/art-wang-editor/index.vue'
import ArtSvgIcon from '@/components/core/base/art-svg-icon/index.vue'
import ArtRegionSelect from '@/components/business/art-region-select/index.vue'
import { useTable } from '@/hooks/core/useTable'
import { fetchGetScenicPage, createScenic, updateScenic, deleteScenic } from '@/api/scenic'
import { uploadFile } from '@/api/file'

defineOptions({ name: 'ScenicPage' })

type ScenicSpot = Api.Scenic.ScenicSpotVO

const dialogType = ref<'add' | 'edit'>('add')
const dialogVisible = ref(false)
const current = reactive({
  id: undefined as number | undefined,
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
  longitude: undefined as number | undefined,
  latitude: undefined as number | undefined,
  sort: 0
})
const formRef = ref()
const imageUrlList = ref<string[]>([])
const tagsList = ref<string[]>([])
const newTag = ref('')
const openTimeRange = ref<[string, string] | null>(null)

// 监听 tags 变化同步到 tagsList
watch(() => current.tags, (val) => {
  tagsList.value = val ? val.split(';') : []
}, { immediate: true })

const addTag = () => {
  const tag = newTag.value.trim()
  if (!tag) return
  if (tagsList.value.includes(tag)) {
    ElMessage.warning('标签已存在')
    return
  }
  tagsList.value.push(tag)
  current.tags = tagsList.value.join(';')
  newTag.value = ''
}

const removeTag = (index: number) => {
  tagsList.value.splice(index, 1)
  current.tags = tagsList.value.join(';')
}

// 监听 openTime 变化同步到 openTimeRange
watch(() => current.openTime, (val) => {
  if (val && val.includes('-')) {
    const [start, end] = val.split('-')
    openTimeRange.value = [start, end]
  } else {
    openTimeRange.value = null
  }
}, { immediate: true })

const handleTimeChange = (val: [string, string] | null) => {
  if (val) {
    current.openTime = `${val[0]}-${val[1]}`
  } else {
    current.openTime = ''
  }
}

// 行政区划组合数据
const regionModel = ref({
  province: '',
  city: '',
  district: ''
})

const rules = reactive({
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  address: [{ required: true, message: '请输入地址', trigger: 'blur' }],
  province: [{ required: true, message: '请选择省份', trigger: 'change' }],
  city: [{ required: true, message: '请选择城市', trigger: 'change' }],
  district: [{ required: true, message: '请选择区县', trigger: 'change' }]
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
      { prop: 'province', label: '省份', width: 100 },
      { prop: 'city', label: '城市', width: 100 },
      { prop: 'district', label: '区县', width: 100 },
      { prop: 'openTime', label: '开放时间', width: 140 },
      { prop: 'sort', label: '排序', width: 80 },
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

// 行政区划逻辑
const handleRegionChange = (val: { province: string; city: string; district: string }) => {
  current.province = val.province
  current.city = val.city
  current.district = val.district
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
  
  // 同步到组合控件
  regionModel.value = {
    province: current.province || '',
    city: current.city || '',
    district: current.district || ''
  }
  
  setImageUrlList(current.imageUrls)
  newTag.value = ''
  nextTick(() => (dialogVisible.value = true))
}

const handleDialogOpen = () => {
  nextTick(() => {
    formRef.value?.clearValidate?.()
  })
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

const handleListUpload = async (options: any) => {
  try {
    const file = options.file as File
    const res = await uploadFile({ file, bizTag: 'SCENIC_GALLERY' })
    imageUrlList.value.push(res.url)
    options.onSuccess?.(res)
  } catch (e) {
    options.onError?.(e)
  }
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

const openAmapPreview = () => {
  if (!current.amapId) {
    ElMessage.warning('请先填写高德ID')
    return
  }
  window.open(`https://gaode.com/place/${current.amapId}`, '_blank')
}
</script>

<style scoped>
/* 
  Tailwind CSS has been used for layout. 
  Custom styles can be added here if needed, but preferred Tailwind utility classes.
*/
</style>
