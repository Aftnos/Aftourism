<template>
  <div class="page-wrapper">
    <div class="content-card">
      <div class="section-title">
        <h3>交流区</h3>
        <span>分享旅游攻略、体验与推荐</span>
      </div>

      <div class="action-bar">
        <el-input v-model="keyword" placeholder="搜索标题/内容" clearable @keyup.enter="loadList" />
        <el-button type="primary" @click="loadList">搜索</el-button>
        <el-button v-if="userStore.isLogin" type="success" @click="goPublish">发布文章</el-button>
        <el-button v-else type="primary" plain @click="goLogin">登录后发布</el-button>
      </div>

      <el-empty v-if="items.length === 0 && !loading" description="暂无交流文章" />
      <div v-else class="article-grid" v-loading="loading">
        <el-card
          v-for="item in items"
          :key="item.id"
          shadow="hover"
          class="article-card"
          @click="goDetail(item.id)"
        >
          <div class="card-header">
            <h4 class="title">{{ item.title }}</h4>
            <el-tag size="small" type="success">{{ item.statusText || '已发布' }}</el-tag>
          </div>
          <p class="summary">{{ snippet(item.content) }}</p>
          <div class="card-footer">
            <span class="user" @click.stop="goUser(item.userId)">{{ item.userNickname || '游客' }}</span>
            <span class="meta">评论 {{ item.commentCount || 0 }}</span>
            <span class="meta">点赞 {{ item.likeCount || 0 }}</span>
            <span class="meta">{{ formatTime(item.createTime) }}</span>
          </div>
        </el-card>
      </div>

      <div v-if="items.length" class="pager">
        <el-pagination
          background
          layout="prev, pager, next"
          v-model:current-page="pager.current"
          :page-size="pager.size"
          :total="pager.total"
          @current-change="loadList"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { fetchExchangePage, type ExchangeArticleItem } from '@/services/portal';
import { useUserStore } from '@/store/user';

// 中文注释：交流区文章列表
const router = useRouter();
const userStore = useUserStore();
const keyword = ref('');
const loading = ref(false);
const items = ref<ExchangeArticleItem[]>([]);
const pager = reactive({ current: 1, size: 8, total: 0 });

const formatTime = (time?: string) => (time ? new Date(time).toLocaleDateString() : '-');
const snippet = (content: string) => {
  const plain = content.replace(/<[^>]+>/g, '').trim();
  if (!plain) return '';
  return plain.length > 120 ? `${plain.slice(0, 120)}...` : plain;
};

const loadList = async () => {
  loading.value = true;
  const page = await fetchExchangePage({
    current: pager.current,
    size: pager.size,
    keyword: keyword.value || undefined
  });
  items.value = page.list || [];
  pager.total = page.total || 0;
  loading.value = false;
};

const goDetail = (id: number) => router.push(`/exchange/${id}`);
const goPublish = () => router.push('/exchange/publish');
const goLogin = () => router.push('/login');
const goUser = (id: number) => router.push(`/user/${id}`);

onMounted(loadList);
</script>

<style scoped lang="scss">
.page-wrapper {
  width: 100%;
  padding: 32px 48px;
  box-sizing: border-box;
  display: flex;
  justify-content: center;
}

.content-card {
  width: min(1200px, 100%);
  padding: 24px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(15, 23, 42, 0.06);
}

.action-bar {
  display: flex;
  gap: 12px;
  margin: 20px 0;
  align-items: center;
}

.action-bar :deep(.el-input) {
  max-width: 320px;
}

.article-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 20px;
}

.article-card {
  cursor: pointer;
}

.title {
  font-size: 18px;
  margin: 0;
  color: #1e293b;
}

.summary {
  margin: 12px 0 16px;
  color: #64748b;
  line-height: 1.6;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
}

.card-footer {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  font-size: 12px;
  color: #94a3b8;
}

.user {
  color: #2563eb;
  cursor: pointer;
}

.pager {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>
