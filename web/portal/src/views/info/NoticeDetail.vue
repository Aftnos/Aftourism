<template>
  <!-- 中文注释：通知详情页使用统一宽屏容器，兼顾大屏展示效果 -->
  <div class="page-wrapper">
    <div class="content-card" v-if="notice">
      <h2 class="title">{{ notice.title }}</h2>
      <p class="meta">发布时间：{{ notice.publishTime }}</p>
      <el-divider />
      <div class="rich-content" v-html="notice.content" />
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
.page-wrapper {
  /* 中文注释：与新闻详情保持一致的宽屏居中布局 */
  width: 100%;
  padding: 40px 48px 56px;
  box-sizing: border-box;
  display: flex;
  justify-content: center;
}

.content-card {
  width: min(1400px, 100%);
  background: #fff;
  padding: 32px 44px;
  box-sizing: border-box;
  border-radius: 18px;
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.05);
}

.title {
  font-size: 26px;
  margin: 0 0 12px;
  line-height: 1.4;
  color: #1f2d3d;
}

.meta {
  color: #909399;
  font-size: 14px;
  margin-bottom: 12px;
}

.rich-content {
  /* 中文注释：保持后台富文本格式并处理长行、图片自适应 */
  line-height: 1.8;
  color: #2c3e50;
  white-space: pre-wrap;
  word-break: break-word;
}

.rich-content img {
  max-width: 100%;
  height: auto;
}

@media (max-width: 1200px) {
  .page-wrapper {
    padding: 32px 28px 44px;
  }

  .content-card {
    padding: 28px 30px;
    border-radius: 14px;
  }
}

@media (max-width: 768px) {
  .page-wrapper {
    padding: 18px 16px 32px;
  }

  .content-card {
    padding: 20px 16px;
    box-shadow: none;
  }

  .title {
    font-size: 22px;
  }

  .meta {
    font-size: 13px;
  }
}
</style>
