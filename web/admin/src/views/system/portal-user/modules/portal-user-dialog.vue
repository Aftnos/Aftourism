<template>
  <ElDialog v-model="dialogVisible" title="编辑普通用户" width="420px" align-center>
    <ElForm ref="formRef" :model="formData" :rules="rules" label-width="90px">
      <ElFormItem label="账号">
        <ElInput v-model="formData.username" disabled />
      </ElFormItem>
      <ElFormItem label="昵称">
        <ElInput v-model="formData.nickname" disabled />
      </ElFormItem>
      <ElFormItem label="角色编码" prop="roleCode">
        <ElInput v-model="formData.roleCode" placeholder="请输入角色编码" />
      </ElFormItem>
      <ElFormItem label="状态" prop="status">
        <ElSelect v-model="formData.status" placeholder="请选择状态">
          <ElOption label="启用" :value="1" />
          <ElOption label="禁用" :value="0" />
        </ElSelect>
      </ElFormItem>
    </ElForm>
    <template #footer>
      <div class="dialog-footer">
        <ElButton @click="dialogVisible = false">取消</ElButton>
        <ElButton type="primary" @click="handleSubmit">保存</ElButton>
      </div>
    </template>
  </ElDialog>
</template>

<script setup lang="ts">
  import type { FormInstance, FormRules } from 'element-plus'
  import { fetchUpdatePortalUser } from '@/api/system-manage'

  // 普通用户编辑弹窗：用于修改角色编码与启用状态
  type PortalUserItem = Api.SystemManage.PortalUserItem

  interface Props {
    visible: boolean
    userData?: Partial<PortalUserItem>
  }

  interface Emits {
    (e: 'update:visible', value: boolean): void
    (e: 'success'): void
  }

  const props = defineProps<Props>()
  const emit = defineEmits<Emits>()

  // 对话框显示控制：v-model 绑定父级显示状态
  const dialogVisible = computed({
    get: () => props.visible,
    set: (value) => emit('update:visible', value)
  })

  // 表单实例：用于校验与重置
  const formRef = ref<FormInstance>()

  // 表单数据：包含展示字段与可编辑字段
  const formData = reactive({
    id: undefined as number | undefined,
    username: '',
    nickname: '',
    roleCode: '',
    status: 1
  })

  // 表单验证规则：限制角色编码长度
  const rules: FormRules = {
    roleCode: [{ max: 100, message: '角色编码长度不能超过 100 字符', trigger: 'blur' }]
  }

  /**
   * 初始化表单数据
   * 将当前行数据同步到弹窗表单
   */
  const initFormData = () => {
    Object.assign(formData, {
      id: props.userData?.id,
      username: props.userData?.username || '',
      nickname: props.userData?.nickname || '',
      roleCode: props.userData?.roleCode || '',
      status: props.userData?.status ?? 1
    })
  }

  /**
   * 监听弹窗开启状态
   * 打开时初始化表单并清理校验信息
   */
  watch(
    () => [props.visible, props.userData],
    ([visible]) => {
      if (!visible) return
      initFormData()
      nextTick(() => {
        formRef.value?.clearValidate()
      })
    },
    { immediate: true }
  )

  /**
   * 提交表单
   * 验证通过后调用接口更新普通用户信息
   */
  const handleSubmit = async () => {
    if (!formRef.value || !formData.id) return
    await formRef.value.validate()
    await fetchUpdatePortalUser(formData.id, {
      roleCode: formData.roleCode || undefined,
      status: formData.status
    })
    emit('success')
  }
</script>
