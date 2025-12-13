<template>
  <!-- 中文注释：详情页统一使用居中宽屏容器，保持响应式 -->
  <div class="page-wrapper">
    <div class="content-card" v-if="news">
      <h2 class="title">{{ news.title }}</h2>
      <p class="meta">发布时间：{{ news.publishTime }}</p>
      <el-divider />
      <div class="rich-content" v-html="news.content" />
    </div>
    <el-empty v-else description="新闻不存在" />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import { fetchNewsDetail, type NewsItem } from '@/services/portal';

// 中文注释：根据路由参数展示新闻详情，首次进入时调用接口拉取数据
const route = useRoute();
const news = ref<NewsItem>();

onMounted(async () => {
  news.value = await fetchNewsDetail(Number(route.params.id));
});
</script>

<style scoped>
.page-wrapper {
  /* 中文注释：宽屏布局并保持居中，避免内容过窄 */
  width: 100%;
  padding: 40px 48px 56px;
  box-sizing: border-box;
  display: flex;
  justify-content: center;
}

.content-card {
  /* 中文注释：支持 1400px 宽度，移动端自动收缩 */
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
  /* 中文注释：保留后台富文本格式，图片自动适配宽度 */
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
