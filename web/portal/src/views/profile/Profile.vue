<template>
  <div class="page-wrapper">
    <div class="profile-layout">
      <!-- Left Sidebar / Mobile Header -->
      <div class="profile-sidebar">
        <div class="user-card">
          <div class="avatar-section">
            <div class="avatar-wrapper">
              <el-avatar :size="100" :src="userStore.profile.avatar || undefined" :icon="UserFilled" class="profile-avatar" />
              <div v-if="showBadge" class="verified-badge">
                <el-icon><Select /></el-icon>
              </div>
            </div>
            <h2 class="username">{{ userStore.profile.nickName || userStore.profile.name }}</h2>
            <p class="bio">{{ userStore.profile.remark || '这个人很懒，什么都没有留下...' }}</p>
            <div class="user-tags">
              <el-tag v-if="userStore.profile.advancedUser" type="warning" size="small" effect="dark" round>高级用户</el-tag>
              <el-tag v-else type="info" size="small" effect="plain" round>普通用户</el-tag>
              <el-tag :type="qualificationTagType" size="small" effect="plain" round>{{ qualificationStatusText }}</el-tag>
            </div>
          </div>
          
          <div class="sidebar-actions">
            <el-button type="primary" class="action-btn" @click="openEditDialog">
              <el-icon><Edit /></el-icon>编辑资料
            </el-button>
          </div>
        </div>

        <!-- Qualification Status Card (Mini) -->
        <div class="status-card" v-if="qualificationStatus !== 'APPROVED'">
          <div class="status-header">
            <h4>资质认证</h4>
            <el-link type="primary" :underline="false" @click="activeTab = 'info'">查看详情</el-link>
          </div>
          <el-progress 
            :percentage="qualificationProgress" 
            :status="qualificationProgressStatus"
            :format="() => qualificationStatusText"
          />
          <p class="status-desc">{{ qualificationStatusDescMini }}</p>
        </div>
      </div>

      <!-- Main Content Area -->
      <div class="profile-content">
        <div class="content-card">
          <el-tabs v-model="activeTab" class="profile-tabs">
            <el-tab-pane label="基本资料" name="info">
              <div class="tab-content">
                <div class="info-section">
                  <div class="section-header">
                    <h3>个人信息</h3>
                  </div>
                  <el-descriptions :column="1" border class="info-table">
                    <el-descriptions-item label="用户名">{{ userStore.profile.name }}</el-descriptions-item>
                    <el-descriptions-item label="昵称">{{ userStore.profile.nickName || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="性别">{{ formatGender(userStore.profile.gender) }}</el-descriptions-item>
                    <el-descriptions-item label="联系电话">{{ userStore.profile.phone || '未绑定' }}</el-descriptions-item>
                    <el-descriptions-item label="邮箱">{{ userStore.profile.email || '未绑定' }}</el-descriptions-item>
                  </el-descriptions>
                </div>

                <div class="info-section">
                  <div class="section-header">
                    <h3>资质认证</h3>
                    <el-button 
                      v-if="canApplyQualification" 
                      type="primary" 
                      size="small" 
                      @click="openQualificationDialog"
                    >
                      提交申请
                    </el-button>
                  </div>
                  <div class="qualification-detail">
                    <el-alert
                      :type="qualificationTagType"
                      :closable="false"
                      show-icon
                    >
                      <template #title>
                        <span class="auth-title">当前状态：{{ qualificationStatusText }}</span>
                      </template>
                      <template #default>
                        <div class="auth-desc">
                          <p>{{ qualificationStatusDesc }}</p>
                          <p v-if="qualificationRemark" class="auth-remark">审核备注：{{ qualificationRemark }}</p>
                        </div>
                      </template>
                    </el-alert>
                  </div>
                </div>
              </div>
            </el-tab-pane>

            <el-tab-pane label="我的收藏" name="favorites">
              <div class="tab-content">
                <el-empty v-if="favoritesPreview.length === 0" description="暂无收藏" />
                <div v-else class="grid-layout">
                  <div v-for="item in favoritesPreview" :key="item.id" class="grid-item" @click="goToDetail(item)">
                    <div class="item-cover">
                      <el-image :src="item.targetCover" fit="cover" loading="lazy" />
                      <div class="item-tag">
                        <el-tag size="small" :type="getTypeTag(item.targetType)" effect="dark">{{ getTypeName(item.targetType) }}</el-tag>
                      </div>
                    </div>
                    <div class="item-info">
                      <h4 class="item-title">{{ item.targetName }}</h4>
                      <span class="item-time">{{ formatTime(item.createTime) }}</span>
                    </div>
                  </div>
                </div>
                <div class="more-link" v-if="favoritesPreview.length > 0">
                  <el-button link type="primary" @click="goToFavorites">查看全部收藏 <el-icon><ArrowRight /></el-icon></el-button>
                </div>
              </div>
            </el-tab-pane>

            <el-tab-pane label="我的发布" name="posts">
              <div class="tab-content">
                <el-empty v-if="exchangePreview.length === 0" description="暂无发布" />
                <div v-else class="post-list">
                  <div v-for="item in exchangePreview" :key="item.id" class="post-item" @click="goExchangeDetail(item.id)">
                    <div class="post-main">
                      <h4 class="post-title">{{ item.title }}</h4>
                      <p class="post-excerpt">{{ snippet(item.content) }}</p>
                      <div class="post-meta">
                        <el-tag size="small" :type="item.status === 1 ? 'success' : 'info'">{{ item.statusText || '已发布' }}</el-tag>
                        <span>{{ formatTime(item.createTime) }}</span>
                        <span><el-icon><ChatDotRound /></el-icon> {{ item.commentCount || 0 }}</span>
                        <span><el-icon><Star /></el-icon> {{ item.likeCount || 0 }}</span>
                      </div>
                    </div>
                    <div v-if="item.coverUrl" class="post-thumb">
                      <el-image :src="item.coverUrl" fit="cover" />
                    </div>
                  </div>
                </div>
                <div class="more-link" v-if="exchangePreview.length > 0">
                  <el-button link type="primary" @click="goToExchange">前往交流区 <el-icon><ArrowRight /></el-icon></el-button>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </div>

    <!-- Edit Dialog -->
    <el-dialog v-model="editDialogVisible" title="编辑个人资料" width="500px" align-center>
      <el-form :model="form" label-width="80px" class="edit-form">
        <div class="form-avatar-center">
          <el-upload
            class="avatar-uploader"
            action="#"
            :http-request="handleUpload"
            :show-file-list="false"
          >
            <div class="avatar-edit-wrapper">
              <el-avatar :size="80" :src="form.avatar || undefined" :icon="UserFilled" class="upload-avatar" />
              <div class="avatar-overlay"><el-icon><Camera /></el-icon></div>
            </div>
          </el-upload>
        </div>
        <el-form-item label="昵称">
          <el-input v-model="form.nickName" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.gender">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
            <el-radio label="未知">保密</el-radio>
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
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存修改</el-button>
      </template>
    </el-dialog>

    <!-- Qualification Dialog -->
    <el-dialog v-model="qualificationDialogVisible" title="高级用户资质申请" width="520px" align-center>
      <el-form ref="qualificationFormRef" :model="qualificationForm" :rules="qualificationRules" label-width="110px">
        <el-form-item label="申请人姓名" prop="realName">
          <el-input v-model="qualificationForm.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="单位/机构" prop="organization">
          <el-input v-model="qualificationForm.organization" placeholder="请输入单位或机构名称" />
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="qualificationForm.contactPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="申请说明" prop="applyReason">
          <el-input
            v-model="qualificationForm.applyReason"
            type="textarea"
            :rows="4"
            placeholder="请填写资质说明与申请理由"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="附件链接">
          <el-input v-model="qualificationForm.attachmentUrl" placeholder="可填写资质材料链接（选填）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="qualificationDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="qualificationSubmitting" @click="submitQualification">
          提交申请
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, watch, ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage, type FormInstance } from 'element-plus';
import { UserFilled, ArrowRight, Edit, Select, Camera, ChatDotRound, Star } from '@element-plus/icons-vue';
import type { UploadRequestOptions } from 'element-plus';
import { useUserStore } from '@/store/user';
import {
  uploadFile,
  fetchFavoritePage,
  applyQualification,
  fetchQualificationStatus,
  fetchExchangeUserPage,
  type FavoriteItem,
  type ExchangeArticleItem
} from '@/services/portal';

// 中文注释：个人中心页面，包含信息展示、收藏列表、发布历史与资质认证
const userStore = useUserStore();
const router = useRouter();
const activeTab = ref('info');
const editDialogVisible = ref(false);

const form = reactive({ name: '', nickName: '', phone: '', email: '', gender: '未知', remark: '', avatar: '' });
const favoritesPreview = ref<FavoriteItem[]>([]);
const exchangePreview = ref<ExchangeArticleItem[]>([]);

// Qualification related
const qualificationDialogVisible = ref(false);
const qualificationSubmitting = ref(false);
const qualificationStatus = ref('NONE');
const qualificationRemark = ref('');
const qualificationFormRef = ref<FormInstance>();
const qualificationForm = reactive({
  realName: '',
  organization: '',
  contactPhone: '',
  applyReason: '',
  attachmentUrl: ''
});
const qualificationRules = {
  realName: [{ required: true, message: '请输入申请人姓名', trigger: 'blur' }],
  organization: [{ required: true, message: '请输入单位或机构名称', trigger: 'blur' }],
  contactPhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
  applyReason: [{ required: true, message: '请输入申请说明', trigger: 'blur' }]
};

onMounted(() => {
  userStore.fetchProfile();
  loadFavoritesPreview();
  loadExchangePreview();
  loadQualificationStatus();
});

watch(() => userStore.profile.userId, (value) => {
  if (value) loadExchangePreview();
});

const loadFavoritesPreview = async () => {
  if (userStore.isLogin) {
    const res = await fetchFavoritePage({ current: 1, size: 8 });
    favoritesPreview.value = res.list;
  }
};

const loadExchangePreview = async () => {
  if (userStore.isLogin && userStore.profile.userId) {
    const res = await fetchExchangeUserPage(userStore.profile.userId, { current: 1, size: 5 });
    exchangePreview.value = res.list;
  }
};

const loadQualificationStatus = async () => {
  if (!userStore.isLogin) return;
  try {
    const res = await fetchQualificationStatus();
    qualificationStatus.value = res.status;
    qualificationRemark.value = res.auditRemark || '';
  } catch (error) {
    // Silent fail
  }
};

const syncForm = () => {
  const profile = userStore.profile;
  form.name = profile.name;
  form.nickName = profile.nickName || '';
  form.phone = profile.phone || '';
  form.email = profile.email || '';
  form.gender = profile.gender || '未知';
  form.remark = profile.remark || '';
  form.avatar = profile.avatar || '';
};

const openEditDialog = () => {
  syncForm();
  editDialogVisible.value = true;
};

const saveEdit = async () => {
  try {
    await userStore.updateProfile(form);
    ElMessage.success('已更新个人信息');
    editDialogVisible.value = false;
  } catch (error) {
    // Error handled globally
  }
};

const handleUpload = async (options: UploadRequestOptions) => {
  try {
    const res = await uploadFile(options.file, 'user_avatar');
    form.avatar = res.url;
    ElMessage.success('头像上传成功');
  } catch (error) {
    // Error handled globally
  }
};

const formatGender = (val?: string) => {
  if (val === '男') return '男';
  if (val === '女') return '女';
  return '保密';
};

const snippet = (content: string) => {
  const plain = content.replace(/<[^>]+>/g, '').trim();
  if (!plain) return '';
  return plain.length > 60 ? `${plain.slice(0, 60)}...` : plain;
};

const goToFavorites = () => activeTab.value = 'favorites'; // Or router push if separate page
const goToExchange = () => router.push('/exchange');
const goExchangeDetail = (id: number) => router.push(`/exchange/${id}`);

const qualificationStatusText = computed(() => {
  switch (qualificationStatus.value) {
    case 'APPROVED': return '已认证';
    case 'PENDING': return '审核中';
    case 'REJECTED': return '已驳回';
    default: return '未认证';
  }
});

const showBadge = computed(() => userStore.profile.advancedUser && userStore.profile.qualificationStatus === 'APPROVED');

const qualificationStatusDesc = computed(() => {
  switch (qualificationStatus.value) {
    case 'APPROVED': return '您已通过高级用户资质认证，现在可以发布活动并享受高级会员权益。';
    case 'PENDING': return '您的资质申请正在审核中，请耐心等待工作人员处理。';
    case 'REJECTED': return '您的资质申请未通过，请查看备注修改后重新提交。';
    default: return '完成资质认证后即可申报活动等高级功能。';
  }
});

const qualificationStatusDescMini = computed(() => {
    switch (qualificationStatus.value) {
    case 'PENDING': return '申请审核中';
    case 'REJECTED': return '申请被驳回';
    default: return '未申请资质';
  }
});

const qualificationProgress = computed(() => {
    switch (qualificationStatus.value) {
    case 'APPROVED': return 100;
    case 'PENDING': return 60;
    case 'REJECTED': return 100;
    default: return 0;
  }
});

const qualificationProgressStatus = computed(() => {
    switch (qualificationStatus.value) {
    case 'APPROVED': return 'success';
    case 'REJECTED': return 'exception';
    default: return '';
  }
});

const qualificationTagType = computed(() => {
  switch (qualificationStatus.value) {
    case 'APPROVED': return 'success';
    case 'PENDING': return 'warning';
    case 'REJECTED': return 'danger';
    default: return 'info';
  }
});

const canApplyQualification = computed(() => qualificationStatus.value === 'NONE' || qualificationStatus.value === 'REJECTED');

const openQualificationDialog = () => {
  qualificationForm.realName = '';
  qualificationForm.organization = '';
  qualificationForm.contactPhone = '';
  qualificationForm.applyReason = '';
  qualificationForm.attachmentUrl = '';
  qualificationDialogVisible.value = true;
};

const submitQualification = async () => {
  if (qualificationSubmitting.value) return;
  qualificationSubmitting.value = true;
  try {
    if (qualificationFormRef.value) {
      await qualificationFormRef.value.validate();
    }
    await applyQualification({ ...qualificationForm });
    ElMessage.success('资质申请已提交');
    qualificationDialogVisible.value = false;
    await loadQualificationStatus();
    await userStore.fetchProfile();
  } catch (error) {
    // Error handled globally
  } finally {
    qualificationSubmitting.value = false;
  }
};

const goToDetail = (item: FavoriteItem) => {
  let path = '';
  if (item.targetType === 'SCENIC') path = `/scenic/${item.targetId}`;
  else if (item.targetType === 'VENUE') path = `/venues/${item.targetId}`;
  else if (item.targetType === 'ACTIVITY') path = `/activities/${item.targetId}`;
  if (path) router.push(path);
};

const getTypeTag = (type: string) => {
  switch (type) {
    case 'SCENIC': return 'success';
    case 'VENUE': return 'info';
    case 'ACTIVITY': return 'warning';
    default: return '';
  }
};

const getTypeName = (type: string) => {
  switch (type) {
    case 'SCENIC': return '景区';
    case 'VENUE': return '场馆';
    case 'ACTIVITY': return '活动';
    default: return type;
  }
};

const formatTime = (time: string) => time?.split('T')[0] || '';
</script>

<style scoped lang="scss">
.page-wrapper {
  width: 100%;
  padding: 32px 48px;
  box-sizing: border-box;
  display: flex;
  justify-content: center;
  background-color: var(--bg-body);
}

.profile-layout {
  width: min(1200px, 100%);
  display: grid;
  grid-template-columns: 320px 1fr;
  gap: 24px;
  align-items: start;
}

/* Sidebar */
.profile-sidebar {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.user-card {
  background: #fff;
  border-radius: 16px;
  padding: 32px 24px;
  text-align: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.04);
}

.avatar-wrapper {
  position: relative;
  display: inline-block;
  margin-bottom: 16px;
}

.profile-avatar {
  border: 4px solid #f8fafc;
  background-color: #f1f5f9;
}

.verified-badge {
  position: absolute;
  bottom: 4px;
  right: 4px;
  width: 24px;
  height: 24px;
  background: #3b82f6;
  color: #fff;
  border-radius: 50%;
  border: 2px solid #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
}

.username {
  margin: 0 0 8px;
  font-size: 22px;
  font-weight: 600;
  color: #1e293b;
}

.bio {
  font-size: 14px;
  color: #64748b;
  margin: 0 0 16px;
  line-height: 1.5;
}

.user-tags {
  display: flex;
  gap: 8px;
  justify-content: center;
  flex-wrap: wrap;
}

.sidebar-actions {
  margin-top: 24px;
}

.action-btn {
  width: 100%;
}

.status-card {
  background: #fff;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.04);
}

.status-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  
  h4 {
    margin: 0;
    font-size: 16px;
  }
}

.status-desc {
  font-size: 12px;
  color: #94a3b8;
  margin: 8px 0 0;
}

/* Content Area */
.content-card {
  background: #fff;
  border-radius: 16px;
  padding: 8px 32px 32px; /* Top padding reduced as tabs have margin */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.04);
  min-height: 500px;
}

.profile-tabs :deep(.el-tabs__nav-wrap::after) {
  height: 1px;
  background-color: #f1f5f9;
}

.profile-tabs :deep(.el-tabs__item) {
  font-size: 16px;
  height: 56px;
  line-height: 56px;
}

.tab-content {
  padding-top: 24px;
}

/* Info Section */
.info-section {
  margin-bottom: 40px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  
  h3 {
    margin: 0;
    font-size: 18px;
    font-weight: 600;
    color: #334155;
    padding-left: 12px;
    border-left: 4px solid var(--primary-color);
    line-height: 1;
  }
}

.info-table :deep(.el-descriptions__label) {
  width: 120px;
  color: #64748b;
}

.auth-title {
  font-weight: 600;
}

.auth-desc {
  margin-top: 8px;
  font-size: 14px;
}

.auth-remark {
  margin-top: 4px;
  color: #f56c6c;
  font-size: 12px;
}

/* Grid Layouts */
.grid-layout {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.grid-item {
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid #e2e8f0;
  cursor: pointer;
  transition: all 0.2s;
  
  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.06);
  }
}

.item-cover {
  height: 120px;
  position: relative;
  
  .el-image {
    width: 100%;
    height: 100%;
  }
  
  .item-tag {
    position: absolute;
    top: 8px;
    left: 8px;
  }
}

.item-info {
  padding: 12px;
}

.item-title {
  margin: 0 0 8px;
  font-size: 14px;
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: #334155;
}

.item-time {
  font-size: 12px;
  color: #94a3b8;
}

/* Post List */
.post-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.post-item {
  display: flex;
  gap: 16px;
  padding: 16px;
  border: 1px solid #f1f5f9;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;
  
  &:hover {
    border-color: #cbd5e1;
    background: #f8fafc;
  }
}

.post-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.post-title {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #1e293b;
}

.post-excerpt {
  margin: 0;
  font-size: 14px;
  color: #64748b;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.post-meta {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 12px;
  color: #94a3b8;
  margin-top: auto;
  
  .el-icon {
    vertical-align: -1px;
  }
}

.post-thumb {
  width: 120px;
  height: 90px;
  border-radius: 8px;
  overflow: hidden;
  flex-shrink: 0;
  
  .el-image {
    width: 100%;
    height: 100%;
  }
}

.more-link {
  text-align: center;
  margin-top: 24px;
}

/* Edit Form */
.form-avatar-center {
  display: flex;
  justify-content: center;
  margin-bottom: 24px;
}

.avatar-edit-wrapper {
  position: relative;
  width: 80px;
  height: 80px;
  border-radius: 50%;
  overflow: hidden;
  cursor: pointer;
  
  &:hover .avatar-overlay {
    opacity: 1;
  }
}

.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  opacity: 0;
  transition: opacity 0.2s;
  font-size: 24px;
}

/* Responsive */
@media (max-width: 768px) {
  .page-wrapper {
    padding: 16px;
  }
  
  .profile-layout {
    grid-template-columns: 1fr;
    gap: 16px;
  }
  
  .content-card {
    padding: 0 16px 24px;
    border-radius: 12px;
  }
  
  .post-item {
    flex-direction: column-reverse;
    
    .post-thumb {
      width: 100%;
      height: 160px;
    }
  }
}
</style>
