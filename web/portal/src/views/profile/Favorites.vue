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
        <el-tag v-for="item in scenicList" :key="item.id" style="margin-right: 6px" type="success">
          {{ item.name }}
        </el-tag>
        <el-divider />
        <h4>场馆</h4>
        <el-tag v-for="item in venueList" :key="item.id" style="margin-right: 6px" type="info">
          {{ item.name }}
        </el-tag>
        <el-divider />
        <h4>特色活动</h4>
        <el-tag v-for="item in activityList" :key="item.id" style="margin-right: 6px" type="warning">
          {{ item.name }}
        </el-tag>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { fetchFavoritePage, type ActivityItem, type ScenicItem, type VenueItem } from '@/services/portal';
import { useUserStore } from '@/store/user';

// 中文注释：展示用户收藏的各类数据，首次进入同步后端收藏记录
const userStore = useUserStore();
const scenicList = ref<ScenicItem[]>([]);
const venueList = ref<VenueItem[]>([]);
const activityList = ref<ActivityItem[]>([]);

const loadFavorites = async () => {
  const resp = await fetchFavoritePage({ current: 1, size: 200 });
  scenicList.value = [];
  venueList.value = [];
  activityList.value = [];
  resp.list.forEach((item: any) => {
    if ('ticketPrice' in item) {
      scenicList.value.push(item as ScenicItem);
    } else if ('free' in item) {
      venueList.value.push(item as VenueItem);
    } else {
      activityList.value.push(item as ActivityItem);
    }
  });
  userStore.loadFavorites();
};

onMounted(loadFavorites);
</script>
