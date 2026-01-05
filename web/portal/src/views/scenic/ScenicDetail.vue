<template>
  <div class="page-wrapper">
    <div v-if="detail">
      <el-row :gutter="24">
        <el-col :xs="24" :md="tocGroups.length > 0 ? 18 : 24">
          <div class="content-card" id="detail-top">
            <el-row :gutter="24">
              <el-col :xs="24" :md="10">
                <div class="cover-wrapper">
                  <el-image 
                    :src="currentCover" 
                    fit="contain" 
                    class="main-cover"
                    :preview-src-list="galleryImages"
                    :initial-index="galleryImages.indexOf(currentCover)"
                  />
                </div>
                <div class="thumbnail-wrapper" v-if="galleryImages.length > 1">
                  <el-button 
                    class="nav-btn prev" 
                    circle 
                    size="small"
                    :disabled="thumbnailScrollLeft <= 0"
                    @click="scrollThumbnail('left')"
                  >
                    <el-icon><ArrowLeft /></el-icon>
                  </el-button>
                  
                  <div class="thumbnail-list" ref="thumbnailListRef">
                    <el-image
                      v-for="img in galleryImages"
                      :key="img"
                      :src="img"
                      fit="cover"
                      :class="['thumb', { active: currentCover === img }]"
                      @click="currentCover = img"
                    />
                  </div>

                  <el-button 
                    class="nav-btn next" 
                    circle 
                    size="small"
                    :disabled="isThumbnailEnd"
                    @click="scrollThumbnail('right')"
                  >
                    <el-icon><ArrowRight /></el-icon>
                  </el-button>
                </div>
              </el-col>
              <el-col :xs="24" :md="14">
                <div class="header-section">
                  <h1 class="title">
                    {{ detail.name }}
                    <el-tag type="warning" effect="dark" round class="level-tag">{{ detail.level }}</el-tag>
                  </h1>
                </div>
                
                <div class="info-section">
                  <div class="info-item">
                    <el-icon><Ticket /></el-icon>
                    <span class="label">门票价格：</span>
                    <span class="value price">{{ detail.ticketPrice ? `¥${detail.ticketPrice}` : '详见公示' }}</span>
                  </div>
                  <div class="info-item">
                    <el-icon><Timer /></el-icon>
                    <span class="label">开放时间：</span>
                    <span class="value">{{ detail.openTime || '暂无信息' }}</span>
                  </div>
                  <div class="info-item">
                    <el-icon><Location /></el-icon>
                    <span class="label">详细地址：</span>
                    <span class="value">{{ detail.address }}</span>
                  </div>
                  <div class="info-item">
                    <el-icon><MapLocation /></el-icon>
                    <span class="label">所属地区：</span>
                    <span class="value">{{ formatRegion(detail) }}</span>
                  </div>
                  <div class="info-item" v-if="tagList.length">
                    <el-icon><CollectionTag /></el-icon>
                    <span class="label">类型标签：</span>
                    <div class="tags-wrapper">
                      <el-tag v-for="tag in tagList" :key="tag" size="small" effect="plain">{{ tag }}</el-tag>
                    </div>
                  </div>
                  <div class="info-item">
                    <el-icon><Phone /></el-icon>
                    <span class="label">联系电话：</span>
                    <span class="value">{{ detail.phone || '暂无' }}</span>
                  </div>
                  <div class="info-item" v-if="detail.website">
                    <el-icon><Monitor /></el-icon>
                    <span class="label">官方网站：</span>
                    <a :href="detail.website.startsWith('http') ? detail.website : `http://${detail.website}`" target="_blank" class="link-value">{{ detail.website }}</a>
                  </div>
                </div>

                <div class="action-section">
                  <el-space size="large">
                    <el-button 
                      type="warning" 
                      size="large" 
                      :plain="!isFavorite(detail.id)"
                      @click="toggleFavorite(detail.id)"
                    >
                      <el-icon class="el-icon--left">
                        <StarFilled v-if="isFavorite(detail.id)" />
                        <Star v-else />
                      </el-icon>
                      {{ isFavorite(detail.id) ? '已收藏' : '收藏景区' }}
                      <span class="count" v-if="favoriteCount">({{ favoriteCount }})</span>
                    </el-button>
                    
                    <el-button size="large" @click="handleShare">
                      <el-icon class="el-icon--left"><Share /></el-icon>
                      分享
                    </el-button>
                    
                    <el-button 
                      type="primary" 
                      size="large" 
                      :disabled="!detail.amapId" 
                      @click="goAmap(detail.amapId)"
                    >
                      <el-icon class="el-icon--left"><Position /></el-icon>
                      导航去这里
                    </el-button>
                  </el-space>
                </div>
              </el-col>
            </el-row>
            
            <div class="content-body">
              <el-divider content-position="left">
                <span class="divider-title">景区介绍</span>
              </el-divider>
              
              <div class="intro-container" ref="introContainerRef">
                <div class="rich-content" v-html="detail.intro"></div>
              </div>
            </div>

            <div class="recommend-section" v-if="recommendList.length > 0" id="recommend-section">
              <el-divider content-position="left">
                <span class="divider-title">猜你喜欢</span>
              </el-divider>
              <el-row :gutter="20">
                <el-col :xs="24" :sm="12" :md="6" v-for="item in recommendList" :key="item.id">
                  <el-card class="recommend-card" shadow="hover" :body-style="{ padding: '0px' }" @click="goDetail(item.id)">
                    <div class="recommend-image-wrapper">
                      <el-image :src="item.imageUrl" fit="cover" class="recommend-cover" />
                      <div class="recommend-level" v-if="item.level">{{ item.level }}</div>
                    </div>
                    <div class="recommend-info">
                      <h4 class="recommend-title" :title="item.name">{{ item.name }}</h4>
                      <div class="recommend-meta">
                        <span class="price">{{ item.ticketPrice ? `¥${item.ticketPrice}` : '免费' }}</span>
                        <span class="location">{{ item.city }}</span>
                      </div>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-col>
        <el-col :xs="0" :md="6">
          <TocNav :groups="tocGroups" />
        </el-col>
      </el-row>
    </div>
    <el-empty v-else description="未找到景区信息" />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch, nextTick } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { 
  Ticket, Timer, Location, MapLocation, Phone, Monitor, 
  Star, StarFilled, Share, Position, CollectionTag, ArrowLeft, ArrowRight 
} from '@element-plus/icons-vue';
import { fetchScenicDetail, fetchScenicPage, type ScenicItem } from '@/services/portal';
import { useUserStore } from '@/store/user';
import TocNav, { type TocGroup } from '@/components/TocNav.vue';

// 中文注释：景区详情展示与收藏操作，对接后台接口
const route = useRoute();
const router = useRouter();
const userStore = useUserStore();
const detail = ref<ScenicItem>();
const recommendList = ref<ScenicItem[]>([]);
const currentCover = ref('');
const thumbnailListRef = ref<HTMLElement>();
const thumbnailScrollLeft = ref(0);
const isThumbnailEnd = ref(false);

// TOC related
const introContainerRef = ref<HTMLElement>();

const tocGroups = ref<TocGroup[]>([]);

onMounted(async () => {
  const id = Number(route.params.id);
  detail.value = await fetchScenicDetail(id);
  fetchRecommendations(id);
});

const goDetail = (id: number) => {
  router.push(`/scenic/${id}`);
};

const fetchRecommendations = async (currentId: number) => {
  try {
    const res = await fetchScenicPage({ size: 4, current: 1 });
    // Filter out current item and take top 4
    recommendList.value = res.list.filter(item => item.id !== currentId).slice(0, 4);
  } catch (error) {
    console.error('Failed to fetch recommendations', error);
  }
};

const generateMenu = () => {
  const groups: TocGroup[] = [];
  
  // 1. Info Group
  const infoGroup = {
    id: 'detail-top',
    title: '景区信息',
    key: 'info',
    items: [{ id: 'detail-top', title: '基础信息', level: 1 }]
  };
  groups.push(infoGroup);

  // 2. Intro Groups
  if (introContainerRef.value) {
    const headers = introContainerRef.value.querySelectorAll('h2, h3');
    const introGroup: TocGroup = {
      id: 'intro-start',
      title: '景区介绍',
      key: 'intro',
      items: []
    };
    
    let hasHeaders = false;
    headers.forEach((header, index) => {
      hasHeaders = true;
      const id = `heading-${index}`;
      header.id = id;
      const level = parseInt(header.tagName.substring(1));
      const title = header.textContent || '';
      
      introGroup.items.push({ id, title, level });
    });

    if (hasHeaders) {
      groups.push(introGroup);
    }
  }

  // 3. Recommend Group
  if (recommendList.value.length > 0) {
    const recGroup = {
      id: 'recommend-section',
      title: '猜你喜欢',
      key: 'recommend',
      items: [{ id: 'recommend-section', title: '推荐列表', level: 1 }]
    };
    groups.push(recGroup);
  }

  tocGroups.value = groups;
};

const favoriteCount = computed(() => (detail.value ? userStore.favorites.scenic.length : 0));
const isFavorite = (id: number) => userStore.favorites.scenic.includes(id);
const toggleFavorite = async (id: number) => {
  if (!userStore.isLogin) {
    ElMessage.warning('请先登录后再收藏');
    return;
  }
  await userStore.toggleFavorite('scenic', id);
};

// 中文注释：跳转高德地图地点页面
const goAmap = (amapId?: string) => {
  if (!amapId) {
    ElMessage.warning('暂无高德地图信息');
    return;
  }
  window.open(`https://gaode.com/place/${amapId}`, '_blank');
};

// 中文注释：将图片列表字符串拆分成数组，兼容封面图片展示
const galleryImages = computed(() => {
  const images = detail.value?.imageUrls?.split(';').map(item => item.trim()).filter(Boolean) ?? [];
  if (detail.value?.imageUrl && !images.includes(detail.value.imageUrl)) {
    images.unshift(detail.value.imageUrl);
  }
  return images;
});

const checkScrollStatus = () => {
  if (!thumbnailListRef.value) return;
  const { scrollLeft, scrollWidth, clientWidth } = thumbnailListRef.value;
  thumbnailScrollLeft.value = scrollLeft;
  isThumbnailEnd.value = Math.ceil(scrollLeft + clientWidth) >= scrollWidth;
};

const scrollThumbnail = (direction: 'left' | 'right') => {
  if (!thumbnailListRef.value) return;
  const scrollAmount = 300; // 每次滚动距离
  thumbnailListRef.value.scrollBy({
    left: direction === 'left' ? -scrollAmount : scrollAmount,
    behavior: 'smooth'
  });
  // 滚动结束后检查状态
  setTimeout(checkScrollStatus, 350);
};

watch(galleryImages, (newVal) => {
  if (newVal.length > 0) {
    currentCover.value = newVal[0];
    nextTick(() => {
      checkScrollStatus();
      thumbnailListRef.value?.addEventListener('scroll', checkScrollStatus);
    });
  } else if (detail.value?.imageUrl) {
    currentCover.value = detail.value.imageUrl;
  }
  // Generate menu when detail is ready (assuming images and detail come together roughly)
  nextTick(generateMenu);
}, { immediate: true });

// Watch for recommendations to update menu
watch(recommendList, () => {
  nextTick(generateMenu);
});

// 中文注释：格式化标签列表
const tagList = computed(() => {
  return detail.value?.tags ? detail.value.tags.split(/[;,]/).map(t => t.trim()).filter(Boolean) : [];
});

// 中文注释：分享功能
const handleShare = async () => {
  try {
    await navigator.clipboard.writeText(window.location.href);
    ElMessage.success('链接已复制到剪贴板');
  } catch (err) {
    ElMessage.error('复制失败，请手动复制链接');
  }
};

// 中文注释：格式化景区所属地区，避免空值导致展示异常
const formatRegion = (item: ScenicItem) => {
  const parts = [item.province, item.city, item.district].filter(Boolean);
  return parts.length > 0 ? parts.join(' / ') : '暂无';
};
</script>

<style scoped>
.page-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
}

.content-card {
  background: #fff;
  padding: 32px;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

/* Cover Section */
.cover-wrapper {
  height: 400px;
  background-color: #f5f7fa;
  border-radius: 8px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16px;
  border: 1px solid #ebeef5;
}

.main-cover {
  width: 100%;
  height: 100%;
}

.thumbnail-wrapper {
  display: flex;
  align-items: center;
  gap: 8px;
  position: relative;
}

.thumbnail-list {
  display: flex;
  gap: 12px;
  overflow-x: auto;
  scroll-behavior: smooth;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE/Edge */
  flex: 1;
}

.thumbnail-list::-webkit-scrollbar {
  display: none; /* Chrome/Safari */
}

.thumb {
  width: 100px;
  height: 80px;
  border-radius: 6px;
  cursor: pointer;
  border: 2px solid transparent;
  transition: all 0.3s ease;
  opacity: 0.7;
  flex-shrink: 0;
}

.nav-btn {
  flex-shrink: 0;
  z-index: 2;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.nav-btn.prev {
  left: 0;
}

.nav-btn.next {
  right: 0;
}

.thumb:hover {
  opacity: 1;
}

.thumb.active {
  border-color: var(--el-color-primary);
  opacity: 1;
}

/* Mobile Responsive */
@media screen and (max-width: 768px) {
  .page-wrapper {
    width: 100%;
    padding: 12px;
  }
  
  .content-card {
    padding: 16px;
  }

  .cover-wrapper {
    height: auto;
    aspect-ratio: 16 / 9;
    min-height: 200px;
  }

  .title {
    font-size: 20px;
    flex-wrap: wrap;
    margin-top: 12px;
  }
  
  .action-section .el-space {
    flex-wrap: wrap;
    width: 100%;
  }

  .action-section .el-space :deep(.el-space__item) {
    width: 100%;
    margin-right: 0 !important;
  }
  
  .action-section .el-button {
    width: 100%; /* Full width buttons on mobile */
    margin-left: 0 !important; /* Override element-plus space margin */
  }

  .info-item {
    font-size: 14px;
  }
  
  .info-item .label {
    min-width: 70px;
  }
}

/* Info Section */
.header-section {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #ebeef5;
}

.title {
  font-size: 28px;
  font-weight: 600;
  color: #1f2f3d;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 12px;
}

.level-tag {
  font-weight: normal;
  font-size: 14px;
  vertical-align: middle;
}

.info-section {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 32px;
}

.info-item {
  display: flex;
  align-items: flex-start;
  font-size: 15px;
  line-height: 24px;
  color: #606266;
}

.info-item .el-icon {
  font-size: 18px;
  margin-right: 10px;
  margin-top: 3px;
  color: #909399;
}

.label {
  color: #909399;
  margin-right: 8px;
  min-width: 80px;
  flex-shrink: 0;
}

.value {
  color: #303133;
}

.price {
  color: #f56c6c;
  font-size: 24px;
  font-weight: bold;
  line-height: 1;
}

.link-value {
  color: var(--el-color-primary);
  text-decoration: none;
  transition: color 0.2s;
}

.link-value:hover {
  color: var(--el-color-primary-dark-2);
  text-decoration: underline;
}

.tags-wrapper {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

/* Action Section */
.action-section {
  margin-top: auto;
  padding-top: 20px;
}

.count {
  margin-left: 4px;
  font-size: 0.9em;
}

.content-body {
  margin-top: 24px;
}

/* Recommend Section */
.recommend-section {
  margin-top: 48px;
}

.recommend-card {
  margin-bottom: 24px;
  cursor: pointer;
  transition: transform 0.3s;
  border: none;
}

.recommend-card:hover {
  transform: translateY(-5px);
}

.recommend-image-wrapper {
  position: relative;
  height: 140px;
  overflow: hidden;
}

.recommend-cover {
  width: 100%;
  height: 100%;
}

.recommend-level {
  position: absolute;
  top: 8px;
  left: 8px;
  background: rgba(0, 0, 0, 0.6);
  color: #fff;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 12px;
  backdrop-filter: blur(2px);
}

.recommend-info {
  padding: 12px;
}

.recommend-title {
  margin: 0 0 8px 0;
  font-size: 16px;
  color: #303133;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.recommend-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
}

.recommend-meta .price {
  color: #f56c6c;
  font-weight: bold;
}

.recommend-meta .location {
  color: #909399;
}

/* Intro Section */
.divider-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.intro-container {
  padding: 32px;
  background: #f8fafc;
  border-radius: 8px;
  margin-top: 24px;
}

.rich-content :deep(img) {
  max-width: 100%;
  height: auto;
  display: block;
  margin: 16px auto;
  border-radius: 4px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.rich-content :deep(p) {
  line-height: 1.8;
  margin-bottom: 1.2em;
  font-size: 16px;
  color: #333;
  text-align: justify;
}

.rich-content :deep(h1),
.rich-content :deep(h2),
.rich-content :deep(h3) {
  margin-top: 1.5em;
  margin-bottom: 0.8em;
  color: #1f2f3d;
}
</style>
