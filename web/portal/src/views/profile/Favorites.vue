<template>
  <div class="page-wrapper">
    <div class="content-card">
      <div class="section-title">
        <h3>我的收藏</h3>
        <span>登录后展示收藏的景区、场馆、活动</span>
      </div>
      <el-empty v-if="!userStore.isLogin" description="请先登录" />
      <template v-else>
        <h4>景区</h4>
        <el-tag v-for="id in userStore.favorites.scenic" :key="id" style="margin-right: 6px" type="success">
          {{ scenicMap.get(id)?.name || '未知景区' }}
        </el-tag>
        <el-divider />
        <h4>场馆</h4>
        <el-tag v-for="id in userStore.favorites.venue" :key="id" style="margin-right: 6px" type="info">
          {{ venueMap.get(id)?.name || '未知场馆' }}
        </el-tag>
        <el-divider />
        <h4>特色活动</h4>
        <el-tag v-for="id in userStore.favorites.activity" :key="id" style="margin-right: 6px" type="warning">
          {{ activityMap.get(id)?.name || '未知活动' }}
        </el-tag>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { activities, scenicSpots, venues, type ActivityItem, type ScenicSpot, type Venue } from '@/data/mockData';
import { useUserStore } from '@/store/user';

// 中文注释：展示用户收藏的各类数据
const userStore = useUserStore();
const scenicMap = computed(() => new Map<number, ScenicSpot>(scenicSpots.map((item: ScenicSpot) => [item.id, item])));
const venueMap = computed(() => new Map<number, Venue>(venues.map((item: Venue) => [item.id, item])));
const activityMap = computed(() => new Map<number, ActivityItem>(activities.map((item: ActivityItem) => [item.id, item])));
</script>
