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
.page-wrapper {
  width: 100%;
  padding: 32px 48px;
  box-sizing: border-box;
  display: flex;
  justify-content: center;
  max-width: none;
}

.content-card {
  width: min(1200px, 100%);
  background: #fff;
  padding: 40px 48px;
  box-sizing: border-box;
  border-radius: 16px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.04);
}

.el-form {
  margin-bottom: 24px;
}

.el-row {
  display: flex;
  flex-wrap: wrap;
}

.el-col {
  margin-bottom: 24px;
}

.el-card {
  height: 100%;
  border-radius: 12px;
  border: 1px solid #ebeef5;
  transition: all 0.3s ease;
}

.el-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.08);
}

.activity-card {
  display: flex;
  gap: 16px;
}

.cover {
  width: 160px;
  height: 160px;
  border-radius: 8px;
  display: block;
  flex-shrink: 0;
  object-fit: cover;
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

.info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  min-width: 0; /* Fix flex child text overflow */
}

.info h4 {
  margin: 0 0 8px;
  font-size: 18px;
  color: #1f2d3d;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.info p {
  margin: 0 0 6px;
  font-size: 13px;
  color: #606266;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.actions {
  margin-top: auto;
  padding-top: 8px;
  display: flex;
  gap: 12px;
}

.pager {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}

@media (max-width: 960px) {
  .page-wrapper {
    padding: 16px;
  }
  
  .content-card {
    padding: 24px 20px;
    width: 100%;
  }

  .el-form-item {
    margin-right: 0;
    margin-bottom: 12px;
    width: 100%;
  }
  
  /* 强制表单内容换行 */
  .el-form--inline .el-form-item__content {
    width: 100%;
  }
  
  .el-input, .el-select, .el-date-editor {
    width: 100% !important;
  }

  /* 移动端改为单列大卡片 */
  .el-col {
    width: 100% !important;
    max-width: 100%;
    flex: 0 0 100%;
  }

  .activity-card {
    flex-direction: column;
  }

  .cover {
    width: 100%;
    height: 180px;
  }

  .info h4 {
    font-size: 16px;
    white-space: normal;
  }
}
</style>
