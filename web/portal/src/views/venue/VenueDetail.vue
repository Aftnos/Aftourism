<template>
  <div class="page-wrapper">
    <div class="content-card" v-if="detail">
      <el-row :gutter="16">
        <el-col :span="10">
          <img :src="detail.cover" alt="cover" class="cover" />
        </el-col>
        <el-col :span="14">
          <h2>{{ detail.name }}（{{ detail.category }}）</h2>
          <p>是否免费：{{ detail.free ? '免费开放' : '收费' }}</p>
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
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import { activities, venues } from '@/data/mockData';
import { useUserStore } from '@/store/user';

// 中文注释：场馆详情展示、收藏与关联活动
const route = useRoute();
const userStore = useUserStore();
const detail = computed(() => venues.find((item) => item.id === Number(route.params.id)));
const relatedActivities = computed(() =>
  activities.filter((act) => act.venueId === Number(route.params.id) && act.status === 'approved')
);
const favoriteCount = computed(() => (detail.value ? userStore.favorites.venue.length : 0));
const isFavorite = (id: number) => userStore.favorites.venue.includes(id);
const toggleFavorite = (id: number) => userStore.toggleFavorite('venue', id);
</script>

<style scoped>
.cover {
  width: 100%;
  border-radius: 8px;
  object-fit: cover;
}
</style>
