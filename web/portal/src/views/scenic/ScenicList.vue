<template>
  <div class="page-wrapper">
    <div class="content-card">
      <div class="section-title">
        <h3>A 级景区</h3>
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
        <el-col :xs="24" :sm="12" :lg="8" v-for="item in scenicList" :key="item.id">
          <el-card class="scenic-card" shadow="hover" :body-style="{ padding: '12px' }">
            <el-image :src="item.imageUrl" fit="cover" class="cover">
              <template #error>
                <div class="image-slot">
                  <el-icon><Picture /></el-icon>
                </div>
              </template>
            </el-image>
            <h4>{{ item.name }}（{{ item.level }}）</h4>
            <p>门票：{{ item.ticketPrice ? `${item.ticketPrice} 元` : '详见景区公示' }} ｜ 开放时间：{{ item.openTime }}</p>
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
import { fetchScenicPage, type ScenicItem } from '@/services/portal';
import { useUserStore } from '@/store/user';

// 中文注释：景区列表对接后端接口，支持模糊搜索、分页与收藏
const keyword = ref('');
const router = useRouter();
const userStore = useUserStore();
const scenicList = ref<ScenicItem[]>([]);
const total = ref(0);
const current = ref(1);
const pageSize = 9;

const loadList = async () => {
  const resp = await fetchScenicPage({
    current: current.value,
    size: pageSize,
    name: keyword.value,
    address: keyword.value
  });
  scenicList.value = resp.list;
  total.value = resp.total;
};

watch(keyword, () => {
  current.value = 1;
  loadList();
});

onMounted(loadList);

const goDetail = (id: number) => router.push(`/scenic/${id}`);
const toggleFavorite = async (id: number) => {
  if (!userStore.isLogin) {
    ElMessage.warning('请先登录后再收藏');
    return;
  }
  await userStore.toggleFavorite('scenic', id);
};
const isFavorite = (id: number) => userStore.favorites.scenic.includes(id);
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

.pager {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
}
</style>
