<template>
  <ElDialog
    v-model="dialogVisible"
    :title="dialogType === 'add' ? '新增管理员' : '编辑管理员'"
    width="480px"
    align-center
  >
    <ElForm ref="formRef" :model="formData" :rules="rules" label-width="80px">
      <ElFormItem label="用户名" prop="username">
        <ElInput v-model="formData.username" placeholder="请输入用户名" :disabled="dialogType === 'edit'" />
      </ElFormItem>
      <ElFormItem v-if="dialogType === 'add'" label="初始密码" prop="password">
        <ElInput v-model="formData.password" type="password" show-password placeholder="请输入初始密码" />
      </ElFormItem>
      <ElFormItem label="姓名" prop="realName">
        <ElInput v-model="formData.realName" placeholder="请输入姓名" />
      </ElFormItem>
      <ElFormItem label="手机号" prop="phone">
        <ElInput v-model="formData.phone" placeholder="请输入手机号" />
      </ElFormItem>
      <ElFormItem label="邮箱" prop="email">
        <ElInput v-model="formData.email" placeholder="请输入邮箱" />
      </ElFormItem>
      <ElFormItem label="角色" prop="roleCodes">
        <ElSelect v-model="formData.roleCodes" multiple placeholder="请选择角色">
          <ElOption
            v-for="role in roleList"
            :key="role.roleCode"
            :value="role.roleCode"
            :label="role.roleName"
          />
        </ElSelect>
      </ElFormItem>
      <ElFormItem label="状态" prop="status">
        <ElSelect v-model="formData.status" placeholder="请选择状态">
          <ElOption label="启用" :value="1" />
          <ElOption label="禁用" :value="0" />
        </ElSelect>
      </ElFormItem>
      <ElFormItem label="超级管理员" prop="superAdmin">
        <ElSwitch v-model="formData.superAdmin" />
      </ElFormItem>
      <ElFormItem label="备注" prop="remark">
        <ElInput v-model="formData.remark" type="textarea" :rows="2" placeholder="请输入备注" />
      </ElFormItem>
    </ElForm>
    <template #footer>
      <div class="dialog-footer">
        <ElButton @click="dialogVisible = false">取消</ElButton>
        <ElButton type="primary" @click="handleSubmit">提交</ElButton>
      </div>
    </template>
  </ElDialog>
</template>

<script setup lang="ts">
  import type { FormInstance, FormRules } from 'element-plus'
  import {
    fetchCreateAdminAccount,
    fetchGetAdminAccountDetail,
    fetchGetRoleList,
    fetchUpdateAdminAccount
  } from '@/api/system-manage'

  type UserListItem = Api.SystemManage.AdminAccountItem

  interface Props {
    visible: boolean
    type: 'add' | 'edit'
    userData?: Partial<UserListItem>
  }

  interface Emits {
    (e: 'update:visible', value: boolean): void
    (e: 'success', action: 'create' | 'update'): void
  }

  const props = defineProps<Props>()
  const emit = defineEmits<Emits>()

  // 角色列表数据
  const roleList = ref<Api.SystemManage.RoleListItem[]>([])

  // 对话框显示控制
  const dialogVisible = computed({
    get: () => props.visible,
    set: (value) => emit('update:visible', value)
  })

  const dialogType = computed(() => props.type)

  // 表单实例
  const formRef = ref<FormInstance>()

  // 表单数据
  const formData = reactive({
    username: '',
    password: '',
    realName: '',
    phone: '',
    email: '',
    roleCodes: [] as string[],
    status: 1,
    superAdmin: false,
    remark: ''
  })

  // 表单验证规则
  const rules: FormRules = {
    username: [
      { required: true, message: '请输入用户名', trigger: 'blur' },
      { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
    ],
    password: [
      {
        validator: (_rule, value, callback) => {
          if (dialogType.value === 'add' && !value) {
            callback(new Error('请输入初始密码'))
            return
          }
          callback()
        },
        trigger: 'blur'
      }
    ],
    realName: [{ min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }],
    phone: [
      {
        validator: (_rule, value, callback) => {
          if (!value) {
            callback()
            return
          }
          if (!/^1[3-9]\d{9}$/.test(value)) {
            callback(new Error('请输入正确的手机号格式'))
            return
          }
          callback()
        },
        trigger: 'blur'
      }
    ],
    email: [
      { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
    ],
    roleCodes: [{ required: true, message: '请选择角色', trigger: 'blur' }]
  }

  /**
   * 初始化表单数据
   * 根据对话框类型（新增/编辑）填充表单
   */
  const initFormData = async () => {
    const isEdit = props.type === 'edit' && props.userData
    const row = props.userData

    if (isEdit && row?.id) {
      const detail = await fetchGetAdminAccountDetail(row.id)
      Object.assign(formData, {
        username: detail.username || '',
        password: '',
        realName: detail.realName || '',
        phone: detail.phone || '',
        email: detail.email || '',
        roleCodes: Array.isArray(detail.roleCodes) ? detail.roleCodes : [],
        status: detail.status ?? 1,
        superAdmin: detail.superAdmin ?? false,
        remark: detail.remark || ''
      })
      return
    }

    Object.assign(formData, {
      username: '',
      password: '',
      realName: '',
      phone: '',
      email: '',
      roleCodes: [],
      status: 1,
      superAdmin: false,
      remark: ''
    })
  }

  /**
   * 加载角色列表
   */
  const loadRoleList = async () => {
    if (roleList.value.length) return
    const response = await fetchGetRoleList({ current: 1, size: 200 })
    const list = (response as any).list ?? response.records ?? []
    roleList.value = list
  }

  /**
   * 监听对话框状态变化
   * 当对话框打开时初始化表单数据并清除验证状态
   */
  watch(
    () => [props.visible, props.type, props.userData],
    ([visible]) => {
      if (visible) {
        loadRoleList()
        initFormData()
        nextTick(() => {
          formRef.value?.clearValidate()
        })
      }
    },
    { immediate: true }
  )

  /**
   * 提交表单
   * 验证通过后触发提交事件
   */
  const handleSubmit = async () => {
    if (!formRef.value) return

    try {
      await formRef.value.validate()

      if (dialogType.value === 'add') {
        await fetchCreateAdminAccount({
          username: formData.username,
          password: formData.password,
          realName: formData.realName,
          phone: formData.phone,
          email: formData.email,
          roleCodes: formData.roleCodes,
          status: formData.status,
          superAdmin: formData.superAdmin,
          remark: formData.remark
        })
        ElMessage.success('添加成功')
        emit('success', 'create')
      } else if (props.userData?.id) {
        await fetchUpdateAdminAccount(props.userData.id, {
          password: formData.password || undefined,
          realName: formData.realName,
          phone: formData.phone,
          email: formData.email,
          roleCodes: formData.roleCodes,
          status: formData.status,
          superAdmin: formData.superAdmin,
          remark: formData.remark
        })
        ElMessage.success('更新成功')
        emit('success', 'update')
      }

      dialogVisible.value = false
    } catch (error) {
      console.error('提交失败:', error)
    }
  }
</script>
