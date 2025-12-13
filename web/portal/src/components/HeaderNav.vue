<template>
  <div class="nav-wrapper">
    <div class="brand" @click="goHome">AfTourism 文旅门户</div>
    <el-menu mode="horizontal" :default-active="activePath" router>
      <el-menu-item index="/">首页</el-menu-item>
      <el-sub-menu index="info">
        <template #title>资讯</template>
        <el-menu-item index="/news">新闻动态</el-menu-item>
        <el-menu-item index="/notices">通知公告</el-menu-item>
      </el-sub-menu>
      <el-menu-item index="/scenic">A 级景区</el-menu-item>
      <el-menu-item index="/venues">场馆</el-menu-item>
      <el-menu-item index="/activities">特色活动</el-menu-item>
    </el-menu>
    <div class="actions">
      <el-button type="primary" link @click="goFavorites">我的收藏</el-button>
      <el-button type="primary" link @click="goProfile">个人中心</el-button>
      <el-button type="success" @click="goApply">活动申报</el-button>
      <el-button v-if="!userStore.isLogin" type="primary" @click="goLogin">登录</el-button>
      <el-dropdown v-else>
        <el-button type="primary">{{ userStore.profile.name }}<el-icon class="el-icon--right"><ArrowDown /></el-icon></el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="goProfile">个人资料</el-dropdown-item>
            <el-dropdown-item @click="userStore.logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ArrowDown } from '@element-plus/icons-vue';
import { useUserStore } from '@/store/user';

// 中文注释：顶部导航栏，包含路由跳转与登录状态展示
const router = useRouter();
const route = useRoute();
const userStore = useUserStore();

const activePath = computed(() => route.path.startsWith('/news') || route.path.startsWith('/notices') ? '/news' : route.path);

const goHome = () => router.push('/');
const goLogin = () => router.push('/login');
const goProfile = () => router.push('/profile/info');
const goFavorites = () => router.push('/profile/favorites');
const goApply = () => router.push('/activities/apply');
</script>

<style scoped>
.nav-wrapper {
  display: flex;
  align-items: center;
  height: 72px;
}

.brand {
  font-size: 20px;
  font-weight: 600;
  margin-right: 28px;
  cursor: pointer;
}

.actions {
  margin-left: auto;
  display: flex;
  gap: 8px;
}
</style>
