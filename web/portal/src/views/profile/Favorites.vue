<template>
  <div class="page-wrapper">
    <div class="content-card">
      <div class="section-title">
        <h3>我的收藏</h3>
        <span>登录后展示收藏的景区、场馆、活动</span>
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
          <el-card v-for="item in favoriteList" :key="item.id" shadow="hover" class="favorite-item" :body-style="{ padding: '0px' }">
            <img :src="item.targetCover" class="image" />
            <div style="padding: 14px">
              <div class="item-title" :title="item.targetName">{{ item.targetName }}</div>
              <div class="item-meta">
                <el-tag size="small" :type="getTypeTag(item.targetType)">{{ getTypeName(item.targetType) }}</el-tag>
                <span class="time">{{ formatTime(item.createTime) }}</span>
              </div>
              <div class="bottom">
                <el-button text type="primary" class="button" @click="goToDetail(item)">查看详情</el-button>
              </div>
            </div>
          </el-card>
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
.favorite-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 20px;
  margin-top: 20px;
}
.favorite-item {
  transition: all 0.3s;
}
.favorite-item:hover {
  transform: translateY(-5px);
}
.image {
  width: 100%;
  height: 160px;
  object-fit: cover;
  display: block;
}
.item-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.item-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}
.time {
  font-size: 12px;
  color: #999;
}
.bottom {
  display: flex;
  justify-content: flex-end;
}
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
.loading-container {
  padding: 20px;
}
</style>
