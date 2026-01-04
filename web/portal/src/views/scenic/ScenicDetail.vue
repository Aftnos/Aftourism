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
          <h2>{{ detail.name }}（{{ detail.level }}）</h2>
          <p>门票：{{ detail.ticketPrice ? `${detail.ticketPrice} 元` : '详见景区公示' }}</p>
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
      <p>{{ detail.intro }}</p>
    </div>
    <el-empty v-else description="未找到景区" />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import { fetchScenicDetail, type ScenicItem } from '@/services/portal';
import { useUserStore } from '@/store/user';

// 中文注释：景区详情展示与收藏操作，对接后台接口
const route = useRoute();
const userStore = useUserStore();
const detail = ref<ScenicItem>();

onMounted(async () => {
  detail.value = await fetchScenicDetail(Number(route.params.id));
});

const favoriteCount = computed(() => (detail.value ? userStore.favorites.scenic.length : 0));
const isFavorite = (id: number) => userStore.favorites.scenic.includes(id);
const toggleFavorite = async (id: number) => {
  if (!userStore.isLogin) {
    ElMessage.warning('请先登录后再收藏');
    return;
  }
  await userStore.toggleFavorite('scenic', id);
};

// 中文注释：将图片列表字符串拆分成数组，兼容封面图片展示
const galleryImages = computed(() => {
  const images = detail.value?.imageUrls?.split(';').map(item => item.trim()).filter(Boolean) ?? [];
  if (detail.value?.imageUrl && !images.includes(detail.value.imageUrl)) {
    images.unshift(detail.value.imageUrl);
  }
  return images;
});

const coverImage = computed(() => galleryImages.value[0] || detail.value?.imageUrl || '');

// 中文注释：格式化景区所属地区，避免空值导致展示异常
const formatRegion = (item: ScenicItem) => {
  const parts = [item.province, item.city, item.district].filter(Boolean);
  return parts.length > 0 ? parts.join(' / ') : '暂无';
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
