<template>
  <div class="page-wrapper">
    <div class="content-card" v-if="notice">
      <h2>{{ notice.title }}</h2>
      <p class="meta">发布时间：{{ notice.publishTime }}</p>
      <el-divider />
      <p>{{ notice.content }}</p>
    </div>
    <el-empty v-else description="通知不存在" />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import { fetchNoticeDetail, type NoticeItem } from '@/services/portal';

// 中文注释：根据路由参数展示通知详情，对接后台接口
const route = useRoute();
const notice = ref<NoticeItem>();

onMounted(async () => {
  notice.value = await fetchNoticeDetail(Number(route.params.id));
});
</script>

<style scoped>
.meta {
  color: #909399;
}
</style>
