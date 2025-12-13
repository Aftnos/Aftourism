<template>
  <div class="page-wrapper">
    <div class="content-card">
      <div class="section-title">
        <h3>我的特色活动申报</h3>
        <span>仅展示本人提交的申报编号</span>
      </div>
      <el-empty v-if="!userStore.isLogin" description="请先登录" />
      <template v-else>
        <el-table :data="tableData" v-if="tableData.length">
          <el-table-column prop="id" label="申报编号" />
          <el-table-column prop="status" label="审核状态" />
          <el-table-column label="操作">
            <template #default="scope">
              <el-button type="primary" link>修改</el-button>
              <el-button type="danger" link>删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-else description="暂无申报记录" />
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useUserStore } from '@/store/user';

// 中文注释：展示用户提交的特色活动申报编号，模拟审核状态
const userStore = useUserStore();
const tableData = computed(() =>
  userStore.submissions.map((id: number) => ({ id, status: '待审核' }))
);
</script>
