<template>
  <div class="page-wrapper">
    <div class="content-card">
      <div class="section-title">
        <h3>场馆列表</h3>
        <el-input
          v-model="keyword"
          placeholder="按名称或地址模糊查询"
          style="width: 320px"
          clearable
          @clear="loadList"
          @keyup.enter="loadList"
        />
      </div>
      <el-row :gutter="12">
        <el-col :xs="24" :sm="12" :lg="8" v-for="item in venueList" :key="item.id">
          <el-card class="venue-card" shadow="hover" :body-style="{ padding: '12px' }">
            <el-image :src="item.imageUrl" fit="cover" class="cover">
              <template #error>
                <div class="image-slot">
                  <el-icon><Picture /></el-icon>
                </div>
              </template>
            </el-image>
            <h4>{{ item.name }}</h4>
            <p>{{ item.category }} ｜ {{ formatFree(item) }} ｜ 开放时间：{{ item.openTime }}</p>
            <p>地址：{{ item.address }}</p>
            <p>地区：{{ formatRegion(item) }}</p>
            <p v-if="item.tags">标签：{{ item.tags }}</p>
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
      <div class="pager">
        <el-pagination
          background
          layout="prev, pager, next"
          v-model:current-page="current"
          :page-size="pageSize"
          :total="total"
          @current-change="loadList"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { Picture } from '@element-plus/icons-vue';
import { fetchVenuePage, type VenueItem } from '@/services/portal';
import { useUserStore } from '@/store/user';

// 中文注释：场馆列表对接后端接口，支持模糊搜索与收藏
const keyword = ref('');
const router = useRouter();
const userStore = useUserStore();
const venueList = ref<VenueItem[]>([]);
const total = ref(0);
const current = ref(1);
const pageSize = 9;

const loadList = async () => {
  const resp = await fetchVenuePage({
    current: current.value,
    size: pageSize,
    name: keyword.value,
    address: keyword.value,
    tags: keyword.value
  });
  venueList.value = resp.list;
  total.value = resp.total;
};

watch(keyword, () => {
  // 如果是清空，立即加载；输入则回车加载（已在 template 绑定）
  if (!keyword.value) {
    current.value = 1;
    loadList();
  }
});

onMounted(loadList);

const goDetail = (id: number) => router.push(`/venues/${id}`);
const toggleFavorite = async (id: number) => {
  if (!userStore.isLogin) {
    ElMessage.warning('请先登录后再收藏');
    return;
  }
  await userStore.toggleFavorite('venue', id);
};
const isFavorite = (id: number) => userStore.favorites.venue.includes(id);

// 中文注释：拼接场馆所属地区信息
const formatRegion = (item: VenueItem) => {
  const parts = [item.province, item.city, item.district].filter(Boolean);
  return parts.length > 0 ? parts.join(' / ') : '暂无';
};

// 中文注释：转换免费状态为可读文本
const formatFree = (item: VenueItem) => {
  if (item.isFree === 1) return '免费开放';
  if (item.isFree === 0) return '收费';
  return '暂无';
};
</script>

<style scoped>
.cover {
  width: 100%;
  height: 160px;
  border-radius: 4px;
  margin-bottom: 8px;
  display: block;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 24px;
}

.venue-card {
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}

.venue-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 30px rgba(44, 123, 229, 0.2);
}

.actions {
  display: flex;
  justify-content: space-between;
  margin-top: 8px;
}

.pager {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
}
</style>
