<template>
  <div class="page-wrapper">
    <div class="content-card">
      <div class="section-title">
        <h3>我的特色活动申报</h3>
        <span>展示本人提交的活动申报进度</span>
      </div>
      <el-empty v-if="!userStore.isLogin" description="请先登录" />
      <template v-else>
        <el-table :data="records" v-loading="loading" v-if="records.length">
          <el-table-column prop="id" label="申报编号" width="120" />
          <el-table-column prop="name" label="活动名称" min-width="200" />
          <el-table-column prop="venueName" label="场馆" min-width="160" />
          <el-table-column prop="startTime" label="开始时间" width="170" />
          <el-table-column prop="endTime" label="结束时间" width="170" />
          <el-table-column label="审核状态" width="120">
            <template #default="{ row }">
              <el-tag :type="statusTagType(row.applyStatus)" effect="light">
                {{ row.applyStatusText || statusText(row.applyStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="申报时间" width="170" />
        </el-table>
        <el-empty v-else description="暂无申报记录" />
        <div v-if="records.length" class="pager">
          <el-pagination
            background
            layout="prev, pager, next"
            v-model:current-page="pager.current"
            :page-size="pager.size"
            :total="pager.total"
            @current-change="loadRecords"
          />
        </div>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watch } from 'vue';
import { useUserStore } from '@/store/user';
import { fetchActivityApplyPage, type ActivityApplyRecord } from '@/services/portal';

// 中文注释：展示用户提交的特色活动申报记录
const userStore = useUserStore();
const records = ref<ActivityApplyRecord[]>([]);
const loading = ref(false);
const pager = reactive({ current: 1, size: 8, total: 0 });

const statusText = (status?: number) => {
  if (status === 1) return '审核通过';
  if (status === 2) return '审核不通过';
  return '待审核';
};

const statusTagType = (status?: number) => {
  if (status === 1) return 'success';
  if (status === 2) return 'danger';
  return 'warning';
};

const loadRecords = async () => {
  if (!userStore.isLogin) return;
  loading.value = true;
  const page = await fetchActivityApplyPage({
    current: pager.current,
    size: pager.size
  });
  records.value = page.list || [];
  pager.total = page.total || 0;
  loading.value = false;
};

onMounted(loadRecords);
watch(
  () => userStore.isLogin,
  (val) => {
    if (val) {
      loadRecords();
    }
  }
);
</script>

<style scoped lang="scss">
.pager {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
