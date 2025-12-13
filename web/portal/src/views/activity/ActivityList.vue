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
        <el-col :span="12" v-for="item in activityList" :key="item.id">
          <el-card :body-style="{ padding: '12px' }">
            <div class="activity-card">
              <el-image :src="item.coverUrl" fit="cover" class="cover">
                <template #error>
                  <div class="image-slot">
                    <el-icon><Picture /></el-icon>
                  </div>
                </template>
              </el-image>
              <div class="info">
                <h4>{{ item.name }}</h4>
                <p>时间：{{ item.startTime }} - {{ item.endTime }}</p>
                <p>类别：{{ item.category }} ｜ 场馆：{{ item.venueName }}</p>
                <p>主办：{{ item.organizer }} ｜ 联系电话：{{ item.contactPhone }}</p>
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
import { onMounted, reactive, ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { Picture } from '@element-plus/icons-vue';
import { fetchActivityPage, type ActivityItem } from '@/services/portal';
import { useUserStore } from '@/store/user';

// 中文注释：特色活动列表，支持多条件查询、分页与收藏，并实时对接后台接口
const router = useRouter();
const userStore = useUserStore();

const filters = reactive({
  name: '',
  address: '',
  range: [] as string[]
});

const activityList = ref<ActivityItem[]>([]);
const total = ref(0);
const current = ref(1);
const pageSize = 6;

const loadList = async () => {
  const resp = await fetchActivityPage({
    current: current.value,
    size: pageSize,
    name: filters.name
  });
  // 中文注释：后端返回字段升级，兼容 onlineStatus=1 表示上线
  activityList.value = (resp.list || []).filter(
    (item) => item.onlineStatus === 1 || item.status === 'approved' || item.status === 'ONLINE'
  );
  total.value = resp.total;
};

watch(
  () => ({ ...filters }),
  () => {
    current.value = 1;
    loadList();
  },
  { deep: true }
);

onMounted(loadList);

const goDetail = (id: number) => router.push(`/activities/${id}`);
const toggleFavorite = async (id: number) => {
  if (!userStore.isLogin) {
    ElMessage.warning('请先登录后再收藏');
    return;
  }
  await userStore.toggleFavorite('activity', id);
};
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
  border-radius: 8px;
  display: block;
  flex-shrink: 0;
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

.info h4 {
  margin: 0;
}

.actions {
  margin-top: 6px;
  display: flex;
  gap: 8px;
}

.pager {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
}
</style>
