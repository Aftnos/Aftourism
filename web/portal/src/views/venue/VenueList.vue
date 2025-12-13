<template>
  <div class="page-wrapper">
    <div class="content-card">
      <div class="section-title">
        <h3>场馆列表</h3>
        <el-input v-model="keyword" placeholder="按名称或地址模糊查询" style="width: 320px" clearable />
      </div>
      <el-row :gutter="12">
        <el-col :span="8" v-for="item in filteredList" :key="item.id">
          <el-card :body-style="{ padding: '12px' }">
            <img :src="item.cover" alt="cover" class="cover" />
            <h4>{{ item.name }}</h4>
            <p>{{ item.category }} ｜ 开放时间：{{ item.openTime }}</p>
            <p>地址：{{ item.address }}</p>
            <p>联系电话：{{ item.phone }}</p>
            <div class="actions">
              <el-button type="primary" link @click="goDetail(item.id)">查看详情</el-button>
              <el-button type="warning" size="small" @click="toggleFavorite(item.id)">
                {{ isFavorite(item.id) ? '取消收藏' : '收藏' }}
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { useRouter } from 'vue-router';
import { venues } from '@/data/mockData';
import { useUserStore } from '@/store/user';

// 中文注释：场馆列表支持模糊搜索与收藏
const keyword = ref('');
const router = useRouter();
const userStore = useUserStore();

const filteredList = computed(() =>
  venues.filter((item) => item.name.includes(keyword.value) || item.address.includes(keyword.value))
);

const goDetail = (id: number) => router.push(`/venues/${id}`);
const toggleFavorite = (id: number) => userStore.toggleFavorite('venue', id);
const isFavorite = (id: number) => userStore.favorites.venue.includes(id);
</script>

<style scoped>
.cover {
  width: 100%;
  height: 160px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 8px;
}

.actions {
  display: flex;
  justify-content: space-between;
  margin-top: 8px;
}
</style>
