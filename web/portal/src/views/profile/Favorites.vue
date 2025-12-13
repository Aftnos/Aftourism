<template>
  <div class="page-wrapper">
    <div class="content-card">
      <div class="section-title">
        <h3>我的收藏</h3>
        <span>收藏的精彩内容</span>
      </div>
      <el-empty v-if="!userStore.isLogin" description="请先登录" />
      <div v-else>
        <div v-if="loading" class="loading-container">
          <el-skeleton :rows="5" animated />
        </div>
        <div v-else-if="favoriteList.length === 0">
          <el-empty description="暂无收藏" />
        </div>
        <div v-else class="favorite-grid">
          <div v-for="item in favoriteList" :key="item.id" class="favorite-item" @click="goToDetail(item)">
            <div class="image-wrapper">
              <img :src="item.targetCover" class="image" loading="lazy" />
              <div class="hover-overlay">
                <el-button type="primary" round size="small">查看详情</el-button>
              </div>
            </div>
            <div class="item-info">
              <div class="item-title" :title="item.targetName">{{ item.targetName }}</div>
              <div class="item-meta">
                <el-tag size="small" :type="getTypeTag(item.targetType)" effect="plain">{{ getTypeName(item.targetType) }}</el-tag>
                <span class="time">{{ formatTime(item.createTime) }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="pagination-container" v-if="total > 0">
          <el-pagination
            background
            layout="prev, pager, next"
            :total="total"
            :page-size="pageSize"
            :current-page="currentPage"
            @current-change="handlePageChange"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import { fetchFavoritePage, type FavoriteItem } from '@/services/portal';
import { useUserStore } from '@/store/user';
import { useRouter } from 'vue-router';

const userStore = useUserStore();
const router = useRouter();
const favoriteList = ref<FavoriteItem[]>([]);
const total = ref(0);
const currentPage = ref(1);
const pageSize = ref(12);
const loading = ref(false);

const loadFavorites = async () => {
  if (!userStore.isLogin) return;
  loading.value = true;
  try {
    const resp = await fetchFavoritePage({ current: currentPage.value, size: pageSize.value });
    favoriteList.value = resp.list;
    total.value = resp.total;
  } finally {
    loading.value = false;
  }
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
  loadFavorites();
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

const formatTime = (time: string) => {
  return time ? time.replace('T', ' ').substring(0, 10) : '';
};

const goToDetail = (item: FavoriteItem) => {
  let path = '';
  if (item.targetType === 'SCENIC') path = `/scenic/${item.targetId}`;
  else if (item.targetType === 'VENUE') path = `/venue/${item.targetId}`;
  else if (item.targetType === 'ACTIVITY') path = `/activity/${item.targetId}`;

  if (path) router.push(path);
};

onMounted(() => {
  loadFavorites();
});

// Watch login state
watch(() => userStore.isLogin, (val) => {
    if (val) loadFavorites();
    else favoriteList.value = [];
});
</script>

<style scoped>
.page-wrapper {
  width: 100%;
  padding: 32px 48px;
  box-sizing: border-box;
  display: flex;
  justify-content: center;
  max-width: none;
}

.content-card {
  width: min(1200px, 100%);
  background: #fff;
  padding: 40px 48px;
  box-sizing: border-box;
  border-radius: 16px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.04);
  min-height: 600px;
}

.favorite-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 24px;
  margin-top: 32px;
}

.favorite-item {
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
}

.favorite-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.06);
  border-color: #cbd5e1;
}

.image-wrapper {
  position: relative;
  width: 100%;
  height: 180px;
  overflow: hidden;
}

.image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.favorite-item:hover .image {
  transform: scale(1.05);
}

.hover-overlay {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.favorite-item:hover .hover-overlay {
  opacity: 1;
}

.item-info {
  padding: 16px;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.item-title {
  font-size: 16px;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 12px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.item-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.time {
  font-size: 12px;
  color: #94a3b8;
}

.pagination-container {
  margin-top: 40px;
  display: flex;
  justify-content: center;
}

.loading-container {
  padding: 40px;
}

@media (max-width: 960px) {
  .page-wrapper {
    padding: 16px;
  }
  
  .content-card {
    padding: 24px 20px;
    width: 100%;
  }

  .favorite-grid {
    grid-template-columns: repeat(auto-fill, minmax(100%, 1fr));
    gap: 16px;
  }

  .image-wrapper {
    height: 200px;
  }
}
</style>
