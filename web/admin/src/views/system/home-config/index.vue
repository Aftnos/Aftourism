<!-- 首页内容配置页面 -->
<template>
  <div class="space-y-4">
    <!-- 文旅简介配置卡片 -->
    <ElCard shadow="never">
      <template #header>
        <div class="flex items-center justify-between">
          <div class="font-semibold">文旅简介</div>
          <ElButton type="primary" :loading="loading" @click="handleSubmit">保存配置</ElButton>
        </div>
      </template>
      <ElForm ref="formRef" :model="formState" :rules="rules" label-width="96px" class="space-y-4">
        <ElFormItem label="简介标题" prop="title">
          <ElInput v-model="formState.title" placeholder="请输入简介标题" clearable />
        </ElFormItem>
        <ElFormItem label="封面类型">
          <ElRadioGroup v-model="formState.coverType">
            <ElRadioButton label="IMAGE">图片</ElRadioButton>
            <ElRadioButton label="VIDEO">视频</ElRadioButton>
          </ElRadioGroup>
        </ElFormItem>
        <ElFormItem label="封面素材">
          <div class="space-y-2 w-full">
            <div class="flex items-center gap-3">
              <ElInput v-model="formState.coverUrl" placeholder="请输入封面地址或上传" clearable />
              <ElUpload
                :http-request="handleCoverUpload"
                :show-file-list="false"
                :accept="formState.coverType === 'VIDEO' ? 'video/*' : 'image/*'"
              >
                <ElButton type="primary">上传{{ formState.coverType === 'VIDEO' ? '视频' : '图片' }}</ElButton>
              </ElUpload>
            </div>
            <div class="flex items-center gap-3">
              <template v-if="formState.coverUrl">
                <video
                  v-if="formState.coverType === 'VIDEO'"
                  :src="formState.coverUrl"
                  controls
                  class="rounded-md bg-black"
                  style="width: 320px; height: 180px"
                />
                <ElImage v-else :src="formState.coverUrl" fit="cover" style="width: 320px; height: 180px" />
              </template>
              <p class="text-xs text-slate-500">建议使用 16:9 高清素材，视频格式支持 mp4 等常见编码。</p>
            </div>
          </div>
        </ElFormItem>
        <ElFormItem label="简介内容" prop="content">
          <ElInput
            v-model="formState.content"
            type="textarea"
            :autosize="{ minRows: 4, maxRows: 8 }"
            placeholder="请输入文旅简介，支持换行"
          />
        </ElFormItem>
      </ElForm>
    </ElCard>

    <!-- 轮播图配置 -->
    <ElCard shadow="never">
      <template #header>
        <div class="flex items-center justify-between">
          <div class="font-semibold">轮播图配置</div>
          <ElButton type="primary" plain @click="addBanner">新增轮播</ElButton>
        </div>
      </template>
      <ElTable :data="formState.banners" border stripe style="width: 100%">
        <ElTableColumn label="标题" width="180">
          <template #default="{ row }">
            <ElInput v-model="row.title" placeholder="轮播标题" />
          </template>
        </ElTableColumn>
        <ElTableColumn label="图片地址" min-width="280">
          <template #default="{ row }">
            <div class="space-y-2">
              <div class="flex items-center gap-2">
                <ElInput v-model="row.imageUrl" placeholder="请填写图片链接" />
                <ElUpload :http-request="(opt:any) => handleBannerUpload(row, opt)" :show-file-list="false" accept="image/*">
                  <ElButton type="primary" plain>上传图片</ElButton>
                </ElUpload>
              </div>
              <ElImage v-if="row.imageUrl" :src="row.imageUrl" fit="cover" style="width: 180px; height: 90px" />
            </div>
          </template>
        </ElTableColumn>
        <ElTableColumn label="跳转链接" min-width="220">
          <template #default="{ row }">
            <ElInput v-model="row.linkUrl" placeholder="可选：点击跳转链接" />
          </template>
        </ElTableColumn>
        <ElTableColumn label="排序" width="120" align="center">
          <template #default="{ row }">
            <ElInputNumber v-model="row.sort" :min="0" />
          </template>
        </ElTableColumn>
        <ElTableColumn label="启用" width="100" align="center">
          <template #default="{ row }">
            <ElSwitch v-model="row.enabled" />
          </template>
        </ElTableColumn>
        <ElTableColumn label="操作" width="120" align="center">
          <template #default="{ $index }">
            <ElButton type="danger" text size="small" @click="removeBanner($index)">删除</ElButton>
          </template>
        </ElTableColumn>
      </ElTable>
      <p class="text-xs text-slate-500 mt-3">列表顺序即为展示顺序，排序值越大越靠前。</p>
    </ElCard>

    <!-- 风景展示配置 -->
    <ElCard shadow="never">
      <template #header>
        <div class="flex items-center justify-between">
          <div class="font-semibold">风景展示</div>
          <span class="text-xs text-slate-500">配置首页展示的景区与数量</span>
        </div>
      </template>
      <ElForm label-width="96px" class="space-y-4">
        <ElFormItem label="展示数量">
          <div>
            <ElInputNumber v-model="formState.scenicLimit" :min="1" :max="12" />
            <p class="text-xs text-slate-500 mt-1">最多可配置 12 条，前端将按排序截取前 {{ formState.scenicLimit || 0 }} 条展示。</p>
          </div>
        </ElFormItem>
        <ElFormItem label="选择景区">
          <div class="flex items-center gap-3 w-full">
            <ElSelect
              v-model="scenicSelect"
              placeholder="输入景区名称搜索"
              filterable
              remote
              :remote-method="searchScenicOptions"
              :loading="scenicLoading"
              class="flex-1"
            >
              <ElOption
                v-for="item in scenicOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              >
                <div class="flex items-center gap-2">
                  <ElImage v-if="item.imageUrl" :src="item.imageUrl" fit="cover" style="width: 40px; height: 28px" />
                  <span>{{ item.name }}</span>
                </div>
              </ElOption>
            </ElSelect>
            <ElButton type="primary" plain @click="addScenic(scenicSelect as number)" :disabled="!scenicSelect">添加</ElButton>
          </div>
        </ElFormItem>
      </ElForm>
      <ElTable :data="formState.scenics" border stripe style="width: 100%">
        <ElTableColumn label="景区" min-width="200">
          <template #default="{ row }">
            <div class="flex items-center gap-2">
              <ElImage v-if="row.imageUrl" :src="row.imageUrl" fit="cover" style="width: 80px; height: 48px" />
              <div>
                <div class="font-medium">{{ row.scenicName || '已选景区' }}</div>
                <div class="text-xs text-slate-500">ID：{{ row.scenicId }}</div>
              </div>
            </div>
          </template>
        </ElTableColumn>
        <ElTableColumn label="排序" width="120" align="center">
          <template #default="{ row }">
            <ElInputNumber v-model="row.sort" :min="0" />
          </template>
        </ElTableColumn>
        <ElTableColumn label="启用" width="120" align="center">
          <template #default="{ row }">
            <ElSwitch v-model="row.enabled" />
          </template>
        </ElTableColumn>
        <ElTableColumn label="操作" width="120" align="center">
          <template #default="{ $index }">
            <ElButton type="danger" text size="small" @click="removeScenic($index)">删除</ElButton>
          </template>
        </ElTableColumn>
      </ElTable>
      <p class="text-xs text-slate-500 mt-3">风景列表会按照排序值从高到低展示，未启用的景区不会出现在前台。</p>
    </ElCard>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, type FormInstance, type FormRules, type UploadRequestOptions } from 'element-plus'
import { fetchHomeContent, saveHomeContent } from '@/api/home'
import { uploadFile } from '@/api/file'
import { fetchGetScenicPage } from '@/api/scenic'

// 表单引用与状态
const formRef = ref<FormInstance>()
const loading = ref(false)

const formState = reactive<Api.Home.HomeContentSave>({
  title: '',
  content: '',
  coverUrl: '',
  coverType: 'IMAGE',
  scenicLimit: 6,
  banners: [],
  scenics: []
})

// 校验规则：标题、内容必填，至少一张轮播图需填写图片地址
const rules: FormRules = {
  title: [{ required: true, message: '请输入简介标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入简介内容', trigger: 'blur' }]
}

// 景区下拉选项与加载状态
const scenicOptions = ref<Api.Scenic.ScenicSpotVO[]>([])
const scenicLoading = ref(false)
const scenicSelect = ref<number>()

// 初始化数据
onMounted(async () => {
  loading.value = true
  try {
    const data = await fetchHomeContent()
    formState.title = data.intro?.title || ''
    formState.content = data.intro?.content || ''
    formState.coverUrl = data.intro?.coverUrl || ''
    formState.coverType = data.intro?.coverType || 'IMAGE'
    formState.scenicLimit = data.scenicLimit ?? 6
    formState.banners = (data.banners || []).map((item) => ({
      title: item.title,
      imageUrl: item.imageUrl,
      linkUrl: item.linkUrl,
      sort: item.sort ?? 0,
      enabled: item.isEnabled === 1 || item.enabled === true
    }))
    formState.scenics = (data.scenics || []).map((item) => ({
      scenicId: item.scenicId,
      scenicName: item.scenicName,
      imageUrl: item.imageUrl,
      sort: item.sort ?? 0,
      enabled: item.isEnabled === 1 || item.enabled === true
    }))
    await searchScenicOptions('')
  } finally {
    loading.value = false
  }
})

// 新增轮播
const addBanner = () => {
  formState.banners.push({ title: '', imageUrl: '', linkUrl: '', sort: 0, enabled: true })
}

// 删除轮播
const removeBanner = (index: number) => {
  formState.banners.splice(index, 1)
}

// 轮播图片上传
const handleBannerUpload = async (row: Api.Home.BannerItem, options: UploadRequestOptions) => {
  const file = options.file as File
  const res = await uploadFile({ file, bizTag: 'HOME_BANNER' })
  row.imageUrl = res.url
  options.onSuccess?.(res as any)
}

// 简介封面上传，支持图片/视频
const handleCoverUpload = async (options: UploadRequestOptions) => {
  const file = options.file as File
  const res = await uploadFile({ file, bizTag: formState.coverType === 'VIDEO' ? 'HOME_VIDEO' : 'HOME_COVER' })
  formState.coverUrl = res.url
  options.onSuccess?.(res as any)
}

// 查询可选景区（用于下拉）
const searchScenicOptions = async (keyword: string) => {
  scenicLoading.value = true
  try {
    const resp = await fetchGetScenicPage({ current: 1, size: 20, name: keyword || undefined })
    scenicOptions.value = resp.list || []
  } finally {
    scenicLoading.value = false
  }
}

// 添加风景展示项
const addScenic = (scenicId: number) => {
  if (!scenicId) return
  if (formState.scenics?.some((item) => item.scenicId === scenicId)) {
    ElMessage.warning('该景区已在展示列表中')
    return
  }
  const target = scenicOptions.value.find((item) => item.id === scenicId)
  formState.scenics?.push({
    scenicId,
    scenicName: target?.name,
    imageUrl: target?.imageUrl,
    sort: 0,
    enabled: true
  })
  scenicSelect.value = undefined
}

// 删除风景展示
const removeScenic = (index: number) => {
  formState.scenics?.splice(index, 1)
}

// 提交保存
const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate()

  // 基础校验：至少保留一条有效轮播并具备图片地址
  const invalidBanner = formState.banners.some((item) => !item.imageUrl)
  if (invalidBanner) {
    ElMessage.warning('请补充每条轮播的图片地址')
    return
  }

  if (!formState.scenics || formState.scenics.length === 0) {
    ElMessage.warning('请至少选择1个风景展示')
    return
  }

  loading.value = true
  try {
    await saveHomeContent({
      title: formState.title,
      content: formState.content,
      coverUrl: formState.coverUrl,
      coverType: formState.coverType,
      scenicLimit: formState.scenicLimit,
      banners: formState.banners,
      scenics: formState.scenics?.map((item) => ({
        scenicId: item.scenicId,
        sort: item.sort,
        enabled: item.enabled
      }))
    })
    ElMessage.success('首页配置已保存')
  } finally {
    loading.value = false
  }
}
</script>
