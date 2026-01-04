<template>
  <div class="page-wrapper">
    <div class="content-card" v-if="detail">
      <el-row :gutter="16">
        <el-col :span="10">
          <img :src="coverImage" alt="cover" class="cover" />
          <div class="image-list" v-if="galleryImages.length > 1">
            <el-image
              v-for="img in galleryImages"
              :key="img"
              :src="img"
              fit="cover"
              class="thumb"
            />
          </div>
        </el-col>
        <el-col :span="14">
          <h2>{{ detail.name }}（{{ detail.category }}）</h2>
          <p>是否免费：{{ formatFree(detail) }}</p>
          <p>开放时间：{{ detail.openTime }}</p>
          <p>地址：{{ detail.address }}</p>
          <p>地区：{{ formatRegion(detail) }}</p>
          <p v-if="detail.tags">类型标签：{{ detail.tags }}</p>
          <p>联系电话：{{ detail.phone }}</p>
          <p>官网：{{ detail.website }}</p>
          <el-button type="warning" @click="toggleFavorite(detail.id)">
            {{ isFavorite(detail.id) ? '取消收藏' : '收藏' }}（{{ favoriteCount }}）
          </el-button>
        </el-col>
      </el-row>
      <el-divider />
      <p>{{ detail.description }}</p>
      <el-divider />
      <div>
        <h4>特色活动</h4>
        <el-empty v-if="relatedActivities.length === 0" description="暂无审核通过的活动" />
        <el-timeline v-else>
          <el-timeline-item
            v-for="item in relatedActivities"
            :key="item.id"
            :timestamp="item.startTime"
            placement="top"
          >
            <router-link :to="`/activities/${item.id}`">{{ item.name }}</router-link>
            <p>{{ item.category }} ｜ {{ item.organizer }}</p>
          </el-timeline-item>
        </el-timeline>
      </div>
    </div>
    <el-empty v-else description="未找到场馆" />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import { fetchActivityPage, fetchVenueDetail, type ActivityItem, type VenueItem } from '@/services/portal';
import { useUserStore } from '@/store/user';

// 中文注释：场馆详情展示、收藏与关联活动
const route = useRoute();
const userStore = useUserStore();
const detail = ref<VenueItem>();
const relatedActivities = ref<ActivityItem[]>([]);

onMounted(async () => {
  const venueId = Number(route.params.id);
  detail.value = await fetchVenueDetail(venueId);
  const activityResp = await fetchActivityPage({ current: 1, size: 6, venueId });
  relatedActivities.value = activityResp.list || [];
});

const favoriteCount = computed(() => (detail.value ? userStore.favorites.venue.length : 0));
const isFavorite = (id: number) => userStore.favorites.venue.includes(id);
const toggleFavorite = async (id: number) => {
  if (!userStore.isLogin) {
    ElMessage.warning('请先登录后再收藏');
    return;
  }
  await userStore.toggleFavorite('venue', id);
};

// 中文注释：将图片列表字符串拆分成数组，确保封面展示优先使用主图
const galleryImages = computed(() => {
  const images = detail.value?.imageUrls?.split(';').map(item => item.trim()).filter(Boolean) ?? [];
  if (detail.value?.imageUrl && !images.includes(detail.value.imageUrl)) {
    images.unshift(detail.value.imageUrl);
  }
  return images;
});

const coverImage = computed(() => galleryImages.value[0] || detail.value?.imageUrl || '');

// 中文注释：格式化场馆所属地区，避免出现空值
const formatRegion = (item: VenueItem) => {
  const parts = [item.province, item.city, item.district].filter(Boolean);
  return parts.length > 0 ? parts.join(' / ') : '暂无';
};

// 中文注释：免费状态转换，支持未知状态兜底
const formatFree = (item: VenueItem) => {
  if (item.isFree === 1) return '免费开放';
  if (item.isFree === 0) return '收费';
  return '暂无';
};
</script>

<style scoped>
.cover {
  width: 100%;
  border-radius: 8px;
  object-fit: cover;
}

.image-list {
  margin-top: 8px;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 6px;
}

.thumb {
  width: 100%;
  height: 70px;
  border-radius: 6px;
}
</style>
