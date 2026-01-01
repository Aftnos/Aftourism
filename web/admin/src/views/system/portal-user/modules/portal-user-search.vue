<template>
  <ArtSearchBar
    ref="searchBarRef"
    v-model="formData"
    :items="formItems"
    :rules="rules"
    @reset="handleReset"
    @search="handleSearch"
  />
</template>

<script setup lang="ts">
  // 普通用户搜索栏：负责同步父级查询条件并触发搜索
  interface Props {
    modelValue: Record<string, any>
  }
  interface Emits {
    (e: 'update:modelValue', value: Record<string, any>): void
    (e: 'search', params: Record<string, any>): void
    (e: 'reset'): void
  }
  const props = defineProps<Props>()
  const emit = defineEmits<Emits>()

  // 表单数据双向绑定
  const searchBarRef = ref()
  const formData = computed({
    get: () => props.modelValue,
    set: (val) => emit('update:modelValue', val)
  })

  // 校验规则（当前无需强制校验，可按需扩展）
  const rules = {}

  // 状态选项（用于筛选启用/禁用）
  const statusOptions = [
    { label: '启用', value: 1 },
    { label: '禁用', value: 0 }
  ]

  // 表单配置（用户名、昵称、状态）
  const formItems = computed(() => [
    {
      label: '账号',
      key: 'username',
      type: 'input',
      placeholder: '请输入账号',
      clearable: true
    },
    {
      label: '昵称',
      key: 'nickname',
      type: 'input',
      props: { placeholder: '请输入昵称' }
    },
    {
      label: '状态',
      key: 'status',
      type: 'select',
      props: {
        placeholder: '请选择状态',
        options: statusOptions
      }
    }
  ])

  // 重置事件：通知父级清空搜索条件
  function handleReset() {
    emit('reset')
  }

  // 搜索事件：校验通过后提交搜索条件
  async function handleSearch() {
    await searchBarRef.value.validate()
    emit('search', formData.value)
  }
</script>
