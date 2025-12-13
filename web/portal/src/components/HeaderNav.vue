<template>
  <div class="nav-wrapper">
    <div class="brand" @click="goHome">
      <span class="brand-title">AfTourism 文旅门户</span>
      <small class="brand-desc">文旅资讯 · 全端适配</small>
    </div>

    <div class="menu-area" v-if="!isMobile">
      <el-menu mode="horizontal" :default-active="activePath" router>
        <el-menu-item v-for="item in mainMenus" :key="item.index" :index="item.index">
          <template v-if="!item.children">{{ item.label }}</template>
          <el-sub-menu v-else :index="item.index">
            <template #title>{{ item.label }}</template>
            <el-menu-item v-for="child in item.children" :key="child.index" :index="child.index">
              {{ child.label }}
            </el-menu-item>
          </el-sub-menu>
        </el-menu-item>
      </el-menu>
    </div>

    <div class="actions" v-if="!isMobile">
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

    <div class="mobile-menu" v-else>
      <el-button circle type="primary" @click="mobileMenuVisible = true" aria-label="展开菜单">
        <i class="iconfont el-icon-more" />
      </el-button>
      <el-drawer v-model="mobileMenuVisible" size="80%" direction="rtl" :with-header="false">
        <div class="mobile-menu-header">
          <div>
            <div class="brand-title">AfTourism</div>
            <div class="brand-desc">随时随地畅享文旅</div>
          </div>
          <el-button link type="primary" @click="mobileMenuVisible = false">关闭</el-button>
        </div>
        <el-menu class="mobile-menu-list" :default-active="activePath" router @select="mobileMenuVisible = false">
          <template v-for="item in mainMenus" :key="item.index">
            <el-menu-item v-if="!item.children" :index="item.index">{{ item.label }}</el-menu-item>
            <el-sub-menu v-else :index="item.index">
              <template #title>{{ item.label }}</template>
              <el-menu-item v-for="child in item.children" :key="child.index" :index="child.index">
                {{ child.label }}
              </el-menu-item>
            </el-sub-menu>
          </template>
        </el-menu>
        <div class="mobile-actions">
          <el-button type="primary" link @click="goFavorites">我的收藏</el-button>
          <el-button type="primary" link @click="goProfile">个人中心</el-button>
          <el-button type="success" plain @click="goApply">活动申报</el-button>
          <el-button v-if="!userStore.isLogin" type="primary" plain @click="goLogin">登录</el-button>
          <el-button v-else type="danger" plain @click="userStore.logout">退出登录</el-button>
        </div>
      </el-drawer>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ArrowDown } from '@element-plus/icons-vue';
import { useUserStore } from '@/store/user';

// 中文注释：顶部导航栏，包含响应式菜单、动效与登录状态展示
const router = useRouter();
const route = useRoute();
const userStore = useUserStore();

// 中文注释：导航菜单配置，统一管理菜单层级
const mainMenus = [
  { index: '/', label: '首页' },
  {
    index: 'info',
    label: '资讯',
    children: [
      { index: '/news', label: '新闻动态' },
      { index: '/notices', label: '通知公告' },
    ],
  },
  { index: '/scenic', label: 'A 级景区' },
  { index: '/venues', label: '场馆' },
  { index: '/activities', label: '特色活动' },
];

const mobileMenuVisible = ref(false);
const isMobile = ref(false);

const activePath = computed(() => (route.path.startsWith('/news') || route.path.startsWith('/notices') ? '/news' : route.path));

const updateIsMobile = () => {
  // 中文注释：监听窗口宽度，切换移动端/桌面端样式
  isMobile.value = window.innerWidth <= 960;
};

onMounted(() => {
  updateIsMobile();
  window.addEventListener('resize', updateIsMobile);
});

onBeforeUnmount(() => {
  window.removeEventListener('resize', updateIsMobile);
});

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
  gap: 12px;
}

.brand {
  font-size: 20px;
  font-weight: 600;
  margin-right: 28px;
  cursor: pointer;
  line-height: 1.2;
}

.brand-title {
  display: block;
}

.brand-desc {
  display: block;
  color: #8492a6;
  font-size: 12px;
  font-weight: 400;
}

.menu-area :deep(.el-menu) {
  border-bottom: none;
}

.actions {
  margin-left: auto;
  display: flex;
  gap: 10px;
  align-items: center;
}

.actions :deep(.el-button) {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.actions :deep(.el-button:hover) {
  transform: translateY(-1px);
  box-shadow: 0 6px 16px rgba(44, 123, 229, 0.18);
}

.mobile-menu {
  margin-left: auto;
}

.mobile-menu-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 4px;
}

.mobile-menu-list {
  border-right: none;
}

.mobile-actions {
  display: grid;
  gap: 10px;
  margin-top: 18px;
}

@media (max-width: 1280px) {
  .nav-wrapper {
    gap: 8px;
  }

  .actions {
    gap: 6px;
  }
}

@media (max-width: 960px) {
  .brand {
    margin-right: 8px;
  }
}
</style>
