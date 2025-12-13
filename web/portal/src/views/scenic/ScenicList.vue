<template>
  <div class="page-wrapper">
    <div class="content-card">
      <div class="section-title">
        <h3>A 级景区</h3>
        <el-input v-model="keyword" placeholder="按名称或地址模糊查询" style="width: 320px" clearable />
      </div>
      <el-row :gutter="12">
        <el-col :xs="24" :sm="12" :lg="8" v-for="item in filteredList" :key="item.id">
          <el-card class="scenic-card" shadow="hover" :body-style="{ padding: '12px' }">
            <img :src="item.cover" alt="cover" class="cover" />
            <h4>{{ item.name }}（{{ item.level }}）</h4>
            <p>门票：{{ item.ticket }} ｜ 开放时间：{{ item.openTime }}</p>
            <p>地址：{{ item.address }}</p>
            <p>电话：{{ item.phone }}</p>
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
import { scenicSpots } from '@/data/mockData';
import { useUserStore } from '@/store/user';

// 中文注释：景区列表支持模糊搜索与收藏
const keyword = ref('');
const router = useRouter();
const userStore = useUserStore();

const filteredList = computed(() =>
  scenicSpots.filter(
    (item) => item.name.includes(keyword.value) || item.address.includes(keyword.value)
  )
);

const goDetail = (id: number) => router.push(`/scenic/${id}`);
const toggleFavorite = (id: number) => userStore.toggleFavorite('scenic', id);
const isFavorite = (id: number) => userStore.favorites.scenic.includes(id);
</script>

<style scoped>
.cover {
  width: 100%;
  height: 160px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 8px;
}

.scenic-card {
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}

.scenic-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 30px rgba(44, 123, 229, 0.2);
}

.actions {
  display: flex;
  justify-content: space-between;
  margin-top: 8px;
}
</style>
