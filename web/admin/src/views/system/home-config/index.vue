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
        <ElFormItem label="封面图片">
          <ElInput v-model="formState.coverUrl" placeholder="请输入封面图地址" clearable />
          <p class="text-xs text-slate-500 mt-1">建议使用 16:9 高清图片，留空则展示占位提示。</p>
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
            <ElInput v-model="row.imageUrl" placeholder="请填写图片链接" />
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
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { fetchHomeContent, saveHomeContent } from '@/api/home'

// 表单引用与状态
const formRef = ref<FormInstance>()
const loading = ref(false)

const formState = reactive<Api.Home.HomeContentSave>({
  title: '',
  content: '',
  coverUrl: '',
  banners: []
})

// 校验规则：标题、内容必填，至少一张轮播图需填写图片地址
const rules: FormRules = {
  title: [{ required: true, message: '请输入简介标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入简介内容', trigger: 'blur' }]
}

// 初始化数据
onMounted(async () => {
  loading.value = true
  try {
    const data = await fetchHomeContent()
    formState.title = data.intro?.title || ''
    formState.content = data.intro?.content || ''
    formState.coverUrl = data.intro?.coverUrl || ''
    formState.banners = (data.banners || []).map((item) => ({
      title: item.title,
      imageUrl: item.imageUrl,
      linkUrl: item.linkUrl,
      sort: item.sort ?? 0,
      enabled: item.isEnabled === 1 || item.enabled === true
    }))
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

  loading.value = true
  try {
    await saveHomeContent({
      title: formState.title,
      content: formState.content,
      coverUrl: formState.coverUrl,
      banners: formState.banners
    })
    ElMessage.success('首页配置已保存')
  } finally {
    loading.value = false
  }
}
</script>
