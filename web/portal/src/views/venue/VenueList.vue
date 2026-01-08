<template>
  <div class="page-wrapper">
    <div class="content-container">
      <div class="page-header">
        <div class="header-left">
          <h3>场馆列表</h3>
          <span class="subtitle">探寻文化足迹，感受艺术魅力</span>
        </div>
        <div class="header-right">
          <el-input
            v-model="keyword"
            placeholder="搜索场馆名称、地址或标签"
            class="search-input"
            clearable
            :prefix-icon="Search"
            @clear="loadList"
            @keyup.enter="loadList"
          />
          <el-button type="primary" @click="goMap">地图展示</el-button>
        </div>
      </div>
      
      <el-row :gutter="24">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="item in venueList" :key="item.id" class="card-col">
          <el-card class="venue-card" shadow="hover" :body-style="{ padding: '0px' }" @click="goDetail(item.id)">
            <div class="card-image-wrapper">
              <el-image :src="item.imageUrl" fit="cover" class="cover">
                <template #error>
                  <div class="image-slot">
                    <el-icon><Picture /></el-icon>
                  </div>
                </template>
              </el-image>
              <div class="category-tag" v-if="item.category">{{ item.category }}</div>
              <div class="favorite-btn" @click.stop="toggleFavorite(item.id)">
                <el-icon :class="{ active: isFavorite(item.id) }">
                  <component :is="isFavorite(item.id) ? StarFilled : Star" />
                </el-icon>
              </div>
            </div>
            
            <div class="card-content">
              <h4 class="card-title" :title="item.name">{{ item.name }}</h4>
              
              <div class="info-row">
                <el-icon><Location /></el-icon>
                <span class="info-text">{{ formatRegion(item) }}</span>
              </div>
              
              <div class="info-row">
                <el-icon><Timer /></el-icon>
                <span class="info-text">{{ item.openTime || '暂无' }}</span>
              </div>
              
              <div class="info-row">
                <el-icon><Phone /></el-icon>
                <span class="info-text">{{ item.phone || '暂无' }}</span>
              </div>

              <div class="info-row">
                <el-icon><Ticket /></el-icon>
                <span class="info-text price">{{ formatFree(item) }}</span>
              </div>

              <div class="tags-row" v-if="item.tags">
                <el-tag 
                  v-for="(tag, index) in splitTags(item.tags).slice(0, 3)" 
                  :key="index" 
                  size="small" 
                  effect="plain"
                  class="custom-tag"
                >
                  {{ tag }}
                </el-tag>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <div class="pager">
        <el-pagination
          background
          layout="prev, pager, next"
          v-model:current-page="current"
          :page-size="pageSize"
          :total="total"
          @current-change="loadList"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { Picture, Location, Timer, Ticket, Phone, Search, Star, StarFilled } from '@element-plus/icons-vue';
import { fetchVenuePage, type VenueItem } from '@/services/portal';
import { useUserStore } from '@/store/user';

// 中文注释：场馆列表对接后端接口，支持模糊搜索与收藏
const keyword = ref('');
const router = useRouter();
const userStore = useUserStore();
const venueList = ref<VenueItem[]>([]);
const total = ref(0);
const current = ref(1);
const pageSize = 12; // 增加每页显示数量

const splitTags = (tags?: string) => {
  if (!tags) return [];
  return tags.split(/[;,，\s]+/).filter(Boolean);
};

const loadList = async () => {
  const resp = await fetchVenuePage({
    current: current.value,
    size: pageSize,
    name: keyword.value,
    address: keyword.value,
    tags: keyword.value
  });
  venueList.value = resp.list;
  total.value = resp.total;
};

watch(keyword, () => {
  // 如果是清空，立即加载；输入则回车加载（已在 template 绑定）
  if (!keyword.value) {
    current.value = 1;
    loadList();
  }
});

onMounted(loadList);

const goDetail = (id: number) => router.push(`/venues/${id}`);
const goMap = () => router.push('/venues/map');
const toggleFavorite = async (id: number) => {
  if (!userStore.isLogin) {
    ElMessage.warning('请先登录后再收藏');
    return;
  }
  await userStore.toggleFavorite('venue', id);
};
const isFavorite = (id: number) => userStore.favorites.venue.includes(id);

// 中文注释：拼接场馆所属地区信息
const formatRegion = (item: VenueItem) => {
  const parts = [item.province, item.city, item.district].filter(Boolean);
  return parts.length > 0 ? parts.join(' / ') : '暂无';
};

// 中文注释：转换免费状态为可读文本
const formatFree = (item: VenueItem) => {
  if (item.isFree === 1) return '免费开放';
  if (item.isFree === 0) return '收费';
  return '暂无';
};

// 中文注释：跳转高德地图地点页面
const goAmap = (amapId?: string) => {
  if (!amapId) {
    ElMessage.warning('暂无高德地图信息');
    return;
  }
  window.open(`https://gaode.com/place/${amapId}`, '_blank');
};
</script>

<style scoped>
.page-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
  flex-wrap: wrap;
  gap: 16px;
}

.header-left h3 {
  font-size: 28px;
  color: #303133;
  margin: 0 0 8px 0;
  font-weight: 600;
}

.subtitle {
  color: #909399;
  font-size: 14px;
}

.search-input {
  width: 320px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.card-col {
  margin-bottom: 24px;
}

.venue-card {
  height: 100%;
  border: none;
  transition: all 0.3s ease;
  cursor: pointer;
  display: flex;
  flex-direction: column;
}

.venue-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 16px 32px rgba(0, 0, 0, 0.08);
}

.card-image-wrapper {
  position: relative;
  width: 100%;
  padding-top: 66.67%; /* 3:2 Aspect Ratio */
  overflow: hidden;
}

.cover {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  transition: transform 0.5s ease;
}

.venue-card:hover .cover {
  transform: scale(1.05);
}

.category-tag {
  position: absolute;
  top: 12px;
  left: 12px;
  background: rgba(0, 0, 0, 0.6);
  color: #fff;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: bold;
  backdrop-filter: blur(4px);
}

.favorite-btn {
  position: absolute;
  top: 12px;
  right: 12px;
  width: 32px;
  height: 32px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  z-index: 2;
}

.favorite-btn:hover {
  transform: scale(1.1);
  background: #fff;
}

.favorite-btn .el-icon {
  font-size: 18px;
  color: #909399;
  transition: color 0.3s;
}

.favorite-btn .el-icon.active {
  color: #e6a23c;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 24px;
}

.card-content {
  padding: 16px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.card-title {
  margin: 0 0 12px 0;
  font-size: 18px;
  color: #303133;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.info-row {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  color: #606266;
  font-size: 13px;
}

.info-row .el-icon {
  margin-right: 6px;
  font-size: 14px;
}

.info-text {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.price {
  color: #67c23a;
  font-weight: bold;
  font-size: 15px;
}

.tags-row {
  display: flex;
  flex-wrap: nowrap;
  gap: 6px;
  margin: 12px 0 0 0;
  height: 24px;
  overflow: hidden;
}

.custom-tag {
  border-radius: 4px;
}

.pager {
  margin-top: 40px;
  margin-bottom: 20px;
  display: flex;
  justify-content: center;
}

/* Mobile Adaptation */
@media screen and (max-width: 768px) {
  .page-wrapper {
    width: 100%;
    padding: 16px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .search-input {
    width: 100%;
  }

  .card-col {
    margin-bottom: 16px;
  }
}
</style>
