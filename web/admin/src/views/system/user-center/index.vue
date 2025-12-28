<!-- 个人中心页面 -->
<template>
  <div class="w-full h-full p-0 bg-transparent border-none shadow-none">
    <div class="relative flex-b mt-2.5 max-md:block max-md:mt-1">
      <div class="w-112 mr-5 max-md:w-full max-md:mr-0">
        <div class="art-card-sm relative p-9 pb-6 overflow-hidden text-center">
          <img class="absolute top-0 left-0 w-full h-50 object-cover" src="@imgs/user/bg.webp" />
          <img
            class="relative z-10 w-20 h-20 mt-30 mx-auto object-cover border-2 border-white rounded-full"
            :src="avatarUrl"
          />
          <h2 class="mt-5 text-xl font-normal">{{ displayName }}</h2>
          <p class="mt-5 text-sm">{{ profile.introduction || '暂无个人介绍' }}</p>

          <div class="w-75 mx-auto mt-7.5 text-left">
            <div class="mt-2.5">
              <ArtSvgIcon icon="ri:user-3-line" class="text-g-700" />
              <span class="ml-2 text-sm">账号：{{ profile.username || '-' }}</span>
            </div>
            <div class="mt-2.5">
              <ArtSvgIcon icon="ri:mail-line" class="text-g-700" />
              <span class="ml-2 text-sm">邮箱：{{ profile.email || '-' }}</span>
            </div>
            <div class="mt-2.5">
              <ArtSvgIcon icon="ri:phone-line" class="text-g-700" />
              <span class="ml-2 text-sm">电话：{{ profile.phone || '-' }}</span>
            </div>
            <div class="mt-2.5">
              <ArtSvgIcon icon="ri:shield-user-line" class="text-g-700" />
              <span class="ml-2 text-sm">角色：{{ roleText }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="flex-1 overflow-hidden max-md:w-full max-md:mt-3.5">
        <div class="art-card-sm">
          <h1 class="p-4 text-xl font-normal border-b border-g-300">基本设置</h1>

          <ElForm
            ref="ruleFormRef"
            :model="form"
            :rules="rules"
            class="box-border p-5 [&>.el-row_.el-form-item]:w-[calc(50%-10px)] [&>.el-row_.el-input]:w-full [&>.el-row_.el-select]:w-full"
            label-width="86px"
            label-position="top"
          >
            <ElRow>
              <ElFormItem label="账号">
                <ElInput v-model="form.username" disabled />
              </ElFormItem>
              <ElFormItem label="真实姓名" prop="realName" class="ml-5">
                <ElInput v-model="form.realName" :disabled="!isEdit" />
              </ElFormItem>
            </ElRow>

            <ElRow>
              <ElFormItem label="邮箱" prop="email">
                <ElInput v-model="form.email" :disabled="!isEdit" />
              </ElFormItem>
              <ElFormItem label="电话" prop="phone" class="ml-5">
                <ElInput v-model="form.phone" :disabled="!isEdit" />
              </ElFormItem>
            </ElRow>

            <ElRow>
              <ElFormItem label="头像" prop="avatar">
                <div class="flex items-center gap-3">
                  <ElAvatar :src="form.avatar || defaultAvatar" :size="64" />
                  <ElUpload
                    :http-request="avatarUploadRequest"
                    :show-file-list="false"
                    :limit="1"
                    accept="image/*"
                    :disabled="!isEdit"
                  >
                    <ElButton type="primary" :disabled="!isEdit">上传头像</ElButton>
                  </ElUpload>
                </div>
              </ElFormItem>
            </ElRow>

            <ElFormItem label="个人介绍" prop="introduction" class="h-32">
              <ElInput
                v-model="form.introduction"
                type="textarea"
                :rows="4"
                :disabled="!isEdit"
                placeholder="请输入个人介绍"
              />
            </ElFormItem>

            <div class="flex-c justify-end [&_.el-button]:!w-27.5">
              <ElButton type="primary" class="w-22.5" v-ripple @click="handleProfileSubmit">
                {{ isEdit ? '保存' : '编辑' }}
              </ElButton>
            </div>
          </ElForm>
        </div>

        <div class="art-card-sm my-5">
          <h1 class="p-4 text-xl font-normal border-b border-g-300">更改密码</h1>

          <ElForm
            ref="pwdFormRef"
            :model="pwdForm"
            :rules="pwdRules"
            class="box-border p-5"
            label-width="86px"
            label-position="top"
          >
            <ElFormItem label="当前密码" prop="currentPassword">
              <ElInput
                v-model="pwdForm.currentPassword"
                type="password"
                :disabled="!isEditPwd"
                show-password
              />
            </ElFormItem>

            <ElFormItem label="新密码" prop="newPassword">
              <ElInput
                v-model="pwdForm.newPassword"
                type="password"
                :disabled="!isEditPwd"
                show-password
              />
            </ElFormItem>

            <ElFormItem label="确认新密码" prop="confirmPassword">
              <ElInput
                v-model="pwdForm.confirmPassword"
                type="password"
                :disabled="!isEditPwd"
                show-password
              />
            </ElFormItem>

            <div class="flex-c justify-end [&_.el-button]:!w-27.5">
              <ElButton type="primary" class="w-22.5" v-ripple @click="handlePasswordSubmit">
                {{ isEditPwd ? '保存' : '编辑' }}
              </ElButton>
            </div>
          </ElForm>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { computed, onMounted, reactive, ref } from 'vue'
  import type { FormInstance, FormRules, UploadRequestOptions } from 'element-plus'
  import { fetchAdminProfile, updateAdminPassword, updateAdminProfile } from '@/api/admin-profile'
  import { fetchGetUserInfo } from '@/api/auth'
  import { uploadFile } from '@/api/file'
  import { useUserStore } from '@/store/modules/user'
  import defaultAvatar from '@imgs/user/avatar.webp'

  defineOptions({ name: 'UserCenter' })

  const userStore = useUserStore()
  const userInfo = computed(() => userStore.getUserInfo)

  const isEdit = ref(false)
  const isEditPwd = ref(false)
  const ruleFormRef = ref<FormInstance>()
  const pwdFormRef = ref<FormInstance>()
  /**
   * 个人资料展示
   */
  const profile = reactive<Api.AdminProfile.Profile>({
    id: 0,
    username: '',
    realName: '',
    phone: '',
    email: '',
    avatar: '',
    introduction: ''
  })

  /**
   * 用户信息表单
   */
  const form = reactive({
    username: '',
    realName: '',
    phone: '',
    email: '',
    avatar: '',
    introduction: ''
  })

  /**
   * 密码修改表单
   */
  const pwdForm = reactive({
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  })

  /**
   * 表单验证规则
   */
  const rules = reactive<FormRules>({
    realName: [{ max: 50, message: '姓名长度不能超过50个字符', trigger: 'blur' }],
    email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }],
    phone: [{ max: 20, message: '电话号码长度不能超过20个字符', trigger: 'blur' }],
    introduction: [{ max: 255, message: '个人介绍长度不能超过255个字符', trigger: 'blur' }]
  })

  /**
   * 密码验证规则
   */
  const pwdRules = reactive<FormRules>({
    currentPassword: [{ required: true, message: '请输入当前密码', trigger: 'blur' }],
    newPassword: [
      { required: true, message: '请输入新密码', trigger: 'blur' },
      { min: 6, message: '新密码至少6位', trigger: 'blur' }
    ],
    confirmPassword: [
      { required: true, message: '请确认新密码', trigger: 'blur' },
      {
        validator: (_, value, callback) => {
          if (value !== pwdForm.newPassword) {
            callback(new Error('两次输入的密码不一致'))
            return
          }
          callback()
        },
        trigger: 'blur'
      }
    ]
  })

  const displayName = computed(() => profile.realName || profile.username || userInfo.value.userName)
  const roleText = computed(() => userInfo.value.roles?.join(' / ') || '-')
  const avatarUrl = computed(() => profile.avatar || userInfo.value.avatar || defaultAvatar)

  onMounted(() => {
    loadProfile()
  })

  /**
   * 获取管理员个人资料
   */
  const loadProfile = async () => {
    const data = await fetchAdminProfile()
    Object.assign(profile, data)
    syncForm()
  }

  /**
   * 同步表单数据
   */
  const syncForm = () => {
    form.username = profile.username || ''
    form.realName = profile.realName || ''
    form.phone = profile.phone || ''
    form.email = profile.email || ''
    form.avatar = profile.avatar || ''
    form.introduction = profile.introduction || ''
  }

  /**
   * 上传头像
   */
  const avatarUploadRequest = async (options: UploadRequestOptions) => {
    if (!options.file) return
    const res = await uploadFile({ file: options.file as File, bizTag: 'ADMIN_AVATAR' })
    form.avatar = res.url
    options.onSuccess?.(res)
  }

  /**
   * 保存个人资料
   */
  const handleProfileSubmit = async () => {
    if (!isEdit.value) {
      isEdit.value = true
      return
    }
    await ruleFormRef.value?.validate()
    await updateAdminProfile({
      realName: form.realName,
      phone: form.phone,
      email: form.email,
      avatar: form.avatar,
      introduction: form.introduction
    })
    isEdit.value = false
    await loadProfile()
    const userInfoData = await fetchGetUserInfo()
    userStore.setUserInfo(userInfoData)
  }

  /**
   * 修改密码
   */
  const handlePasswordSubmit = async () => {
    if (!isEditPwd.value) {
      isEditPwd.value = true
      return
    }
    await pwdFormRef.value?.validate()
    await updateAdminPassword({
      currentPassword: pwdForm.currentPassword,
      newPassword: pwdForm.newPassword,
      confirmPassword: pwdForm.confirmPassword
    })
    isEditPwd.value = false
    pwdForm.currentPassword = ''
    pwdForm.newPassword = ''
    pwdForm.confirmPassword = ''
  }
</script>
