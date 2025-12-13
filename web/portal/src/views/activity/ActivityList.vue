<template>
  <div class="page-wrapper">
    <div class="content-card">
      <div class="section-title">
        <h3>特色活动</h3>
      </div>
      <el-form :inline="true" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="filters.name" placeholder="按名称查询" clearable />
        </el-form-item>
        <el-form-item label="场馆地址">
          <el-input v-model="filters.address" placeholder="按场馆地址查询" clearable />
        </el-form-item>
        <el-form-item label="起止时间">
          <el-date-picker
            v-model="filters.range"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
      </el-form>
      <el-row :gutter="12">
        <el-col :span="12" v-for="item in filteredActivities" :key="item.id">
          <el-card :body-style="{ padding: '12px' }">
            <div class="activity-card">
              <img :src="item.cover" alt="cover" class="cover" />
              <div class="info">
                <h4>{{ item.name }}</h4>
                <p>时间：{{ item.startTime }} - {{ item.endTime }}</p>
                <p>类别：{{ item.category }} ｜ 场馆：{{ item.venueName }}</p>
                <p>主办：{{ item.organizer }} ｜ 联系电话：{{ item.phone }}</p>
                <div class="actions">
                  <el-button type="primary" link @click="goDetail(item.id)">查看详情</el-button>
                  <el-button type="warning" size="small" @click="toggleFavorite(item.id)">
                    {{ isFavorite(item.id) ? '取消收藏' : '收藏' }}
                  </el-button>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { activities } from '@/data/mockData';
import { useUserStore } from '@/store/user';

// 中文注释：特色活动列表，支持多条件查询与收藏
const router = useRouter();
const userStore = useUserStore();

const filters = reactive({
  name: '',
  address: '',
  range: [] as string[]
});

const filteredActivities = computed(() => {
  return activities
    .filter((item) => item.status === 'approved')
    .filter((item) => item.name.includes(filters.name))
    .filter((item) => (filters.address ? item.address.includes(filters.address) : true))
    .filter((item) => {
      if (filters.range.length === 2) {
        return item.startTime >= filters.range[0] && item.endTime <= `${filters.range[1]} 23:59`;
      }
      return true;
    });
});

const goDetail = (id: number) => router.push(`/activities/${id}`);
const toggleFavorite = (id: number) => userStore.toggleFavorite('activity', id);
const isFavorite = (id: number) => userStore.favorites.activity.includes(id);
</script>

<style scoped>
.activity-card {
  display: flex;
  gap: 12px;
}

.cover {
  width: 180px;
  height: 120px;
  object-fit: cover;
  border-radius: 8px;
}

.info h4 {
  margin: 0;
}

.actions {
  margin-top: 6px;
  display: flex;
  gap: 8px;
}
</style>
