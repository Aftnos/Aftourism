<template>
  <div>
    <ElCard class="mb-5" shadow="never">
      <template #header>全局水印设置</template>
      <ElForm class="watermark-form" label-width="110px" label-position="left">
        <ElFormItem label="水印开关">
          <ElSwitch v-model="watermarkForm.visible" />
        </ElFormItem>
        <ElFormItem label="水印内容">
          <ElInput
            v-model="watermarkForm.content"
            type="textarea"
            :rows="3"
            placeholder="例如：{realName} - {userName}"
          />
        </ElFormItem>
        <ElFormItem label="可用变量">
          <ElTag size="small">{realName}</ElTag>
          <ElTag size="small" class="ml-2">{userName}</ElTag>
          <ElTag size="small" class="ml-2">{email}</ElTag>
        </ElFormItem>
        <ElFormItem label="">
          <ElButton type="primary" @click="handleSaveWatermark">保存水印设置</ElButton>
        </ElFormItem>
      </ElForm>
      <ElDivider>水印预览</ElDivider>
      <ElWatermark
        :content="previewContent"
        :font="{ fontSize: 16, color: 'rgba(128, 128, 128, 0.2)' }"
        :gap="[120, 120]"
      >
        <div class="watermark-preview"></div>
      </ElWatermark>
    </ElCard>
  </div>
</template>

<script setup lang="ts">
  import { computed, onMounted, ref } from 'vue'
  import {
    ElButton,
    ElCard,
    ElDivider,
    ElForm,
    ElFormItem,
    ElInput,
    ElSwitch,
    ElTag,
    ElWatermark,
    ElMessage
  } from 'element-plus'
  import { useSettingStore } from '@/store/modules/setting'
  import { useUserStore } from '@/store/modules/user'
  import { fetchWatermarkSetting, updateWatermarkSetting } from '@/api/system-backend'

  defineOptions({ name: 'BackendWatermark' })

  const settingStore = useSettingStore()
  const userStore = useUserStore()

  const watermarkForm = ref({
    visible: false,
    content: ''
  })

  const previewContent = computed(() => {
    const userInfo = userStore.getUserInfo
    const template = watermarkForm.value.content || '{realName} - {userName}'
    const rendered = template
      .replaceAll('{userName}', userInfo.userName || '')
      .replaceAll('{realName}', userInfo.nickName || '')
      .replaceAll('{email}', userInfo.email || '')
    return rendered.includes('\n') ? rendered.split('\n') : rendered
  })

  onMounted(() => {
    loadWatermarkSetting()
  })

  const loadWatermarkSetting = async () => {
    const data = await fetchWatermarkSetting()
    watermarkForm.value = {
      visible: data.visible,
      content: data.content || ''
    }
  }

  const handleSaveWatermark = async () => {
    await updateWatermarkSetting({
      visible: watermarkForm.value.visible,
      content: watermarkForm.value.content
    })
    settingStore.setWatermarkVisible(watermarkForm.value.visible)
    settingStore.setWatermarkContent(watermarkForm.value.content)
    ElMessage.success('水印设置已保存')
  }
</script>

<style scoped>
  .watermark-preview {
    height: 450px;
  }

  .watermark-form :deep(.el-form-item__content) {
    align-items: center;
  }
</style>
