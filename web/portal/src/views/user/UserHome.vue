<template>
  <div class="page-wrapper">
    <el-card class="content-card" shadow="never" v-loading="loading">
      <template v-if="profile">
        <div class="profile-header">
          <div class="avatar-badge">
            <el-avatar :size="96" :src="profile.avatar || undefined" :icon="UserFilled" />
            <span v-if="showBadge" class="avatar-badge__mark">V</span>
          </div>
          <div class="profile-info">
            <h2>{{ profile.nickName || profile.userName }}</h2>
            <p class="meta">
              <span>性别：{{ formatGender(profile.gender) }}</span>
              <span>注册时间：{{ formatTime(profile.createTime) }}</span>
            </p>
            <p class="remark">{{ profile.remark || '这个人很懒，什么都没有留下...' }}</p>
          </div>
        </div>
        <el-divider />
        <el-tag :type="qualificationTagType">{{ qualificationText }}</el-tag>
      </template>
      <el-empty v-else description="用户不存在或已注销" />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import { UserFilled } from '@element-plus/icons-vue';
import { fetchPortalUserProfile, type PortalUserProfile } from '@/services/portal';

// 中文注释：门户用户主页，展示昵称、头像、资质状态等基础信息
const route = useRoute();
const loading = ref(false);
const profile = ref<PortalUserProfile | null>(null);

const userId = computed(() => Number(route.params.id));
const showBadge = computed(
  () => profile.value?.advancedUser && profile.value?.qualificationStatus === 'APPROVED'
);

const qualificationText = computed(() => {
  if (!profile.value) return '资质未知';
  if (profile.value.qualificationStatus === 'APPROVED') return '资质已认证';
  if (profile.value.qualificationStatus === 'PENDING') return '资质审核中';
  if (profile.value.qualificationStatus === 'REJECTED') return '资质未通过';
  return '未提交资质';
});

const qualificationTagType = computed(() => {
  if (profile.value?.qualificationStatus === 'APPROVED') return 'success';
  if (profile.value?.qualificationStatus === 'PENDING') return 'warning';
  if (profile.value?.qualificationStatus === 'REJECTED') return 'danger';
  return 'info';
});

const formatGender = (gender?: string) => (gender ? gender : '未知');
const formatTime = (time?: string) => (time ? new Date(time).toLocaleDateString() : '-');

const loadProfile = async () => {
  if (Number.isNaN(userId.value)) return;
  loading.value = true;
  try {
    profile.value = await fetchPortalUserProfile(userId.value);
  } catch (error) {
    profile.value = null;
    ElMessage.error('用户信息获取失败');
  } finally {
    loading.value = false;
  }
};

onMounted(loadProfile);
watch(userId, loadProfile);
</script>

<style scoped lang="scss">
.page-wrapper {
  width: 100%;
  padding: 32px 48px;
  box-sizing: border-box;
  display: flex;
  justify-content: center;
}

.content-card {
  width: min(1100px, 100%);
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 24px;
  padding: 16px 8px;
}

.profile-info h2 {
  margin: 0 0 8px;
  font-size: 26px;
  color: #1e293b;
}

.meta {
  display: flex;
  gap: 16px;
  color: #64748b;
  font-size: 14px;
  margin-bottom: 12px;
}

.remark {
  margin: 0;
  color: #475569;
  line-height: 1.6;
}

.avatar-badge {
  position: relative;
  display: inline-flex;
}

.avatar-badge__mark {
  position: absolute;
  right: 2px;
  bottom: 2px;
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
</style>
