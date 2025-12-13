<template>
  <div class="page-wrapper">
    <div class="content-card">
      <div class="section-title">
        <h3>个人信息管理</h3>
        <span>{{ isEditing ? '编辑个人资料' : '查看个人资料' }}</span>
      </div>
      <el-empty v-if="!userStore.isLogin" description="请先登录" />
      <div v-else class="profile-container">
        <!-- Display Mode -->
        <div v-if="!isEditing" class="view-mode">
          <div class="avatar-section">
            <el-avatar :size="100" :src="userStore.profile.avatar || undefined" :icon="UserFilled" class="profile-avatar" />
            <h2 class="username">{{ userStore.profile.nickName || userStore.profile.name }}</h2>
            <p class="bio">{{ userStore.profile.remark || '这个人很懒，什么都没有留下...' }}</p>
          </div>
          
          <div class="info-card">
            <el-descriptions :column="1" border>
              <el-descriptions-item label="用户名">{{ userStore.profile.name }}</el-descriptions-item>
              <el-descriptions-item label="性别">{{ formatGender(userStore.profile.gender) }}</el-descriptions-item>
              <el-descriptions-item label="联系电话">{{ userStore.profile.phone || '未绑定' }}</el-descriptions-item>
              <el-descriptions-item label="邮箱">{{ userStore.profile.email || '未绑定' }}</el-descriptions-item>
            </el-descriptions>
          </div>

          <div class="action-bar">
            <el-button type="primary" size="large" @click="startEdit">修改资料</el-button>
          </div>
        </div>

        <!-- Edit Mode -->
        <div v-else class="edit-mode">
          <el-form :model="form" label-width="80px" class="edit-form">
            <div class="form-avatar">
              <el-upload
                class="avatar-uploader"
                action="#"
                :http-request="handleUpload"
                :show-file-list="false"
              >
                <el-avatar :size="80" :src="form.avatar || undefined" :icon="UserFilled" class="upload-avatar" />
              </el-upload>
               <span class="avatar-tip">点击头像进行修改</span>
            </div>
            <el-form-item label="用户名">
              <el-input v-model="form.name" disabled />
            </el-form-item>
            <el-form-item label="昵称">
              <el-input v-model="form.nickName" placeholder="请输入昵称" />
            </el-form-item>
            <el-form-item label="性别">
              <el-radio-group v-model="form.gender">
                <el-radio label="1">男</el-radio>
                <el-radio label="2">女</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="联系电话">
              <el-input v-model="form.phone" placeholder="请输入电话号码" />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="form.email" placeholder="请输入邮箱地址" />
            </el-form-item>
            <el-form-item label="个人简介">
              <el-input 
                v-model="form.remark" 
                type="textarea" 
                :rows="4" 
                placeholder="介绍一下自己吧..."
                maxlength="200"
                show-word-limit
              />
            </el-form-item>
            <el-form-item class="form-actions">
              <el-button type="primary" @click="save">保存修改</el-button>
              <el-button @click="cancelEdit">取消</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, watch, ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { UserFilled, Plus } from '@element-plus/icons-vue';
import type { UploadRequestOptions } from 'element-plus';
import { useUserStore } from '@/store/user';
import { uploadFile } from '@/services/portal';

// 中文注释：个人信息展示与编辑切换
const userStore = useUserStore();
const isEditing = ref(false);
const form = reactive({ name: '', nickName: '', phone: '', email: '', gender: '', remark: '', avatar: '' });

onMounted(() => {
  userStore.fetchProfile();
});

const syncForm = () => {
  const profile = userStore.profile;
  form.name = profile.name;
  form.nickName = profile.nickName || '';
  form.phone = profile.phone || '';
  form.email = profile.email || '';
  form.gender = profile.gender || '';
  form.remark = profile.remark || '';
  form.avatar = profile.avatar || '';
};

watch(
  () => userStore.profile,
  syncForm,
  { immediate: true, deep: true }
);

const startEdit = () => {
  syncForm();
  isEditing.value = true;
};

const cancelEdit = () => {
  isEditing.value = false;
};

const save = async () => {
  await userStore.updateProfile(form);
  ElMessage.success('已更新个人信息');
  isEditing.value = false;
};

const handleUpload = async (options: UploadRequestOptions) => {
  try {
    const res = await uploadFile(options.file, 'user_avatar');
    form.avatar = res.url;
    ElMessage.success('头像上传成功');
  } catch (error) {
    // Error is usually handled by http interceptor
  }
};

const formatGender = (val?: string) => {
  if (val === '1') return '男';
  if (val === '2') return '女';
  return '保密';
};
</script>

<style scoped>
.profile-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px 0;
}

.view-mode {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.avatar-section {
  text-align: center;
  margin-bottom: 40px;
}

.profile-avatar {
  background-color: #f0f2f5;
  color: #909399;
}

.username {
  margin: 16px 0 8px;
  font-size: 24px;
  color: #303133;
}

.bio {
  color: #909399;
  font-size: 14px;
  margin: 0;
  max-width: 400px;
  line-height: 1.6;
}

.info-card {
  width: 100%;
  max-width: 600px;
  margin-bottom: 30px;
}

.edit-mode {
  max-width: 600px;
  margin: 0 auto;
}

.form-avatar {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 30px;
}

.avatar-tip {
  margin-top: 10px;
  font-size: 12px;
  color: #909399;
}

.upload-avatar {
  cursor: pointer;
  transition: opacity 0.3s;
}

.upload-avatar:hover {
  opacity: 0.8;
}

.form-actions {
  margin-top: 30px;
  margin-bottom: 0;
}
</style>
