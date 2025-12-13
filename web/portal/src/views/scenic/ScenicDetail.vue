<template>
  <div class="page-wrapper">
    <div class="content-card" v-if="detail">
      <el-row :gutter="16">
        <el-col :span="10">
          <img :src="detail.cover" alt="cover" class="cover" />
        </el-col>
        <el-col :span="14">
          <h2>{{ detail.name }}（{{ detail.level }}）</h2>
          <p>门票：{{ detail.ticket }}</p>
          <p>开放时间：{{ detail.openTime }}</p>
          <p>地址：{{ detail.address }}</p>
          <p>联系电话：{{ detail.phone }}</p>
          <p>官网：{{ detail.website }}</p>
          <el-button type="warning" @click="toggleFavorite(detail.id)">
            {{ isFavorite(detail.id) ? '取消收藏' : '收藏' }}（{{ favoriteCount }}）
          </el-button>
        </el-col>
      </el-row>
      <el-divider />
      <p>{{ detail.description }}</p>
    </div>
    <el-empty v-else description="未找到景区" />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import { scenicSpots } from '@/data/mockData';
import { useUserStore } from '@/store/user';

// 中文注释：景区详情展示与收藏操作
const route = useRoute();
const userStore = useUserStore();
const detail = computed(() => scenicSpots.find((item) => item.id === Number(route.params.id)));
const favoriteCount = computed(() => (detail.value ? userStore.favorites.scenic.length : 0));
const isFavorite = (id: number) => userStore.favorites.scenic.includes(id);
const toggleFavorite = (id: number) => userStore.toggleFavorite('scenic', id);
</script>

<style scoped>
.cover {
  width: 100%;
  border-radius: 8px;
  object-fit: cover;
}
</style>
