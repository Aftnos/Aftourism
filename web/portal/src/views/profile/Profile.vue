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
            <div class="avatar-badge">
              <el-avatar :size="100" :src="userStore.profile.avatar || undefined" :icon="UserFilled" class="profile-avatar" />
              <span v-if="showBadge" class="avatar-badge__mark">V</span>
            </div>
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

          <div class="qualification-card">
            <div class="section-header">
              <h3>资质认证</h3>
              <el-tag :type="qualificationTagType">{{ qualificationStatusText }}</el-tag>
            </div>
            <p class="qualification-desc">{{ qualificationStatusDesc }}</p>
            <el-alert
              v-if="qualificationRemark"
              type="warning"
              :closable="false"
              class="qualification-alert"
              :title="`审核备注：${qualificationRemark}`"
            />
            <div class="qualification-actions">
              <el-button v-if="canApplyQualification" type="primary" @click="openQualificationDialog">
                提交资质申请
              </el-button>
              <el-button v-else disabled>
                {{ qualificationActionText }}
              </el-button>
            </div>
          </div>

          <div class="action-bar">
            <el-button type="primary" size="large" @click="startEdit">修改资料</el-button>
          </div>
        </div>

        <!-- Favorites Preview -->
        <div v-if="!isEditing" class="favorites-section">
           <div class="section-header">
             <h3>我的收藏</h3>
             <el-button link type="primary" @click="goToFavorites">查看更多 <el-icon><ArrowRight /></el-icon></el-button>
           </div>
           <div v-if="favoritesPreview.length > 0" class="favorites-grid">
             <div v-for="item in favoritesPreview" :key="item.id" class="favorite-item" @click="goToDetail(item)">
               <el-image :src="item.targetCover" fit="cover" class="fav-cover" />
               <div class="fav-info">
                 <div class="fav-name">{{ item.targetName }}</div>
                 <div class="fav-meta">
                   <el-tag size="small" :type="getTypeTag(item.targetType)">{{ getTypeName(item.targetType) }}</el-tag>
                   <span class="fav-time">{{ formatTime(item.createTime) }}</span>
                 </div>
               </div>
             </div>
           </div>
           <el-empty v-else description="暂无收藏" :image-size="80" />
        </div>

        <div v-if="!isEditing" class="favorites-section">
          <div class="section-header">
            <h3>我的交流文章</h3>
            <el-button link type="primary" @click="goToExchange">前往交流区 <el-icon><ArrowRight /></el-icon></el-button>
          </div>
          <div v-if="exchangePreview.length > 0" class="favorites-grid">
            <div v-for="item in exchangePreview" :key="item.id" class="favorite-item" @click="goExchangeDetail(item.id)">
              <div class="fav-info">
                <div class="fav-name">{{ item.title }}</div>
                <div class="fav-meta">
                  <el-tag size="small" type="success">{{ item.statusText || '已提交' }}</el-tag>
                  <span class="fav-time">{{ formatTime(item.createTime) }}</span>
                </div>
              </div>
            </div>
          </div>
          <el-empty v-else description="暂无交流文章" :image-size="80" />
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
            <el-form-item class="form-actions">
              <el-button type="primary" @click="save">保存修改</el-button>
              <el-button @click="cancelEdit">取消</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>

    <el-dialog v-model="qualificationDialogVisible" title="高级用户资质申请" width="520px">
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
import { UserFilled, ArrowRight } from '@element-plus/icons-vue';
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

// 中文注释：个人信息展示与编辑切换
const userStore = useUserStore();
const router = useRouter();
const isEditing = ref(false);
const form = reactive({ name: '', nickName: '', phone: '', email: '', gender: '未知', remark: '', avatar: '' });
const favoritesPreview = ref<FavoriteItem[]>([]);
const exchangePreview = ref<ExchangeArticleItem[]>([]);
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

watch(
  () => userStore.profile.userId,
  (value) => {
    if (value) {
      loadExchangePreview();
    }
  }
);

const loadFavoritesPreview = async () => {
  if (userStore.isLogin) {
    const res = await fetchFavoritePage({ current: 1, size: 4 });
    favoritesPreview.value = res.list;
  }
};

const loadExchangePreview = async () => {
  if (userStore.isLogin && userStore.profile.userId) {
    const res = await fetchExchangeUserPage(userStore.profile.userId, { current: 1, size: 4 });
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
    // 资格状态读取失败时不阻断页面渲染
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
  // 中文注释：保存个人资料，发送到后端并刷新本地用户信息
  try {
    await userStore.updateProfile(form);
    ElMessage.success('已更新个人信息');
    isEditing.value = false;
  } catch (error) {
    // 错误提示已由全局拦截器处理，这里保持静默防止重复提示
  }
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
  if (val === '男') return '男';
  if (val === '女') return '女';
  return '保密';
};

const goToFavorites = () => router.push('/profile/favorites');
const goToExchange = () => router.push('/exchange');
const goExchangeDetail = (id: number) => router.push(`/exchange/${id}`);

const qualificationStatusText = computed(() => {
  switch (qualificationStatus.value) {
    case 'APPROVED':
      return '已通过';
    case 'PENDING':
      return '审核中';
    case 'REJECTED':
      return '已驳回';
    default:
      return '未申请';
  }
});

const showBadge = computed(
  () => userStore.profile.advancedUser && userStore.profile.qualificationStatus === 'APPROVED'
);

const qualificationStatusDesc = computed(() => {
  switch (qualificationStatus.value) {
    case 'APPROVED':
      return '已具备高级用户资质，可申报活动等权限功能。';
    case 'PENDING':
      return '资质申请审核中，请耐心等待。';
    case 'REJECTED':
      return '资质申请未通过，可完善资料后重新提交。';
    default:
      return '完成资质认证后即可申报活动等高级功能。';
  }
});

const qualificationTagType = computed(() => {
  switch (qualificationStatus.value) {
    case 'APPROVED':
      return 'success';
    case 'PENDING':
      return 'warning';
    case 'REJECTED':
      return 'danger';
    default:
      return 'info';
  }
});

const canApplyQualification = computed(() => qualificationStatus.value === 'NONE' || qualificationStatus.value === 'REJECTED');

const qualificationActionText = computed(() => {
  return qualificationStatus.value === 'APPROVED' ? '已完成认证' : '等待审核';
});

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
    // 错误提示由全局拦截器处理
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
  /* 覆盖全局样式限制 */
  max-width: none; 
}

.content-card {
  width: min(1200px, 100%);
  background: #fff;
  padding: 40px 48px;
  box-sizing: border-box;
  border-radius: 16px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.04);
}

.profile-container {
  width: 100%;
  margin: 0;
  padding-top: 20px;
}

.view-mode {
  display: grid;
  grid-template-columns: 280px 1fr;
  gap: 60px;
  align-items: start;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: 32px 24px;
  background: #f8fafc;
  border-radius: 16px;
  border: 1px solid #edf2f7;
}

.profile-avatar {
  background-color: #e2e8f0;
  color: #909399;
  border: 4px solid #fff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.avatar-badge {
  position: relative;
  display: inline-flex;
}

.avatar-badge__mark {
  position: absolute;
  right: 4px;
  bottom: 4px;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: #3b82f6;
  color: #fff;
  font-size: 12px;
  font-weight: 700;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border: 2px solid #fff;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.4);
}

.username {
  margin: 16px 0 8px;
  font-size: 24px;
  font-weight: 600;
  color: #1e293b;
}

.bio {
  color: #64748b;
  font-size: 14px;
  margin: 0;
  line-height: 1.6;
}

.info-card {
  width: 100%;
}

.qualification-card {
  width: 100%;
  padding: 20px;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  background: #fff;
}

.qualification-desc {
  margin: 12px 0 16px;
  color: #64748b;
  line-height: 1.6;
}

.qualification-actions {
  display: flex;
  gap: 12px;
}

.qualification-alert {
  margin: 12px 0;
}

:deep(.el-descriptions__label) {
  width: 100px;
  justify-content: flex-end;
}

.action-bar {
  margin-top: 32px;
  display: flex;
  justify-content: flex-start;
}

.favorites-section {
  margin-top: 40px;
  padding-top: 32px;
  border-top: 1px solid #f1f5f9;
  grid-column: 1 / -1;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h3 {
  font-size: 18px;
  font-weight: 600;
  color: #334155;
  margin: 0;
}

.favorites-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 20px;
}

.favorite-item {
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.2s;
}

.favorite-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.05);
  border-color: #cbd5e1;
}

.fav-cover {
  width: 100%;
  height: 140px;
  display: block;
}

.fav-info {
  padding: 12px;
}

.fav-name {
  font-size: 15px;
  font-weight: 500;
  color: #1e293b;
  margin-bottom: 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.fav-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.fav-time {
  font-size: 12px;
  color: #94a3b8;
}

/* 编辑模式 */
.edit-mode {
  width: 100%;
  max-width: 700px; /* 限制表单最大宽度，保持最佳阅读体验 */
  margin: 0 auto;
}

.form-avatar {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 32px;
}

.avatar-tip {
  margin-top: 12px;
  font-size: 13px;
  color: #94a3b8;
}

.upload-avatar {
  cursor: pointer;
  transition: all 0.3s;
  border: 2px dashed #cbd5e1;
}

.upload-avatar:hover {
  border-color: #2c7be5;
  opacity: 0.9;
}

.form-actions {
  margin-top: 32px;
  display: flex;
  justify-content: center;
  gap: 16px;
}

.form-actions :deep(.el-form-item__content) {
  justify-content: center;
  margin-left: 0 !important;
}

@media (max-width: 960px) {
  .page-wrapper {
    padding: 16px;
  }
  
  .content-card {
    padding: 24px 20px;
    width: 100%;
  }

  .view-mode {
    grid-template-columns: 1fr;
    gap: 32px;
  }
  
  .action-bar {
    justify-content: center;
  }

  .edit-mode {
    max-width: 100%;
  }
}
</style>
