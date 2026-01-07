<template>
  <div class="page-wrapper">
    <div class="content-card">
      <div class="section-title">
        <div class="title-group">
          <h3>交流区</h3>
          <span class="subtitle">分享旅游攻略、体验与推荐</span>
        </div>
        <div class="action-bar-mobile">
           <el-button v-if="userStore.isLogin" type="primary" circle :icon="Plus" @click="goPublish" class="fab-btn" />
        </div>
      </div>

      <div class="toolbar">
        <div class="search-box">
          <el-input 
            v-model="keyword" 
            placeholder="搜索标题/内容" 
            clearable 
            :prefix-icon="Search"
            @keyup.enter="loadList" 
          />
          <el-button type="primary" @click="loadList">搜索</el-button>
        </div>
        <div class="actions">
          <el-button v-if="userStore.isLogin" type="primary" @click="goPublish">发布文章</el-button>
          <el-button v-else type="primary" plain @click="goLogin">登录后发布</el-button>
        </div>
      </div>

      <el-empty v-if="items.length === 0 && !loading" description="暂无交流文章" />
      <div v-else class="article-grid" v-loading="loading">
        <div
          v-for="item in items"
          :key="item.id"
          class="post-card"
          @click="goDetail(item.id)"
        >
          <div class="post-header">
            <div class="user-info" @click.stop="goUser(item.userId)">
              <el-avatar :size="32" :src="item.userAvatar || undefined" :icon="UserFilled" class="avatar" />
              <div class="meta">
                <span class="nickname">{{ item.userNickname || '游客' }}</span>
                <span class="time">{{ formatTime(item.createTime) }}</span>
              </div>
            </div>
            <el-tag size="small" :type="item.status === 1 ? 'success' : 'info'" effect="plain">
              {{ item.statusText || '已发布' }}
            </el-tag>
          </div>
          
          <div class="post-body">
            <div class="content-area">
              <h4 class="post-title">{{ item.title }}</h4>
              <p class="post-summary">{{ snippet(item.content) }}</p>
            </div>
            <div v-if="item.coverUrl" class="cover-wrapper">
              <el-image :src="item.coverUrl" fit="cover" loading="lazy" class="post-cover" />
            </div>
          </div>

          <div class="post-footer">
            <div class="stat-item">
              <el-icon><ChatDotRound /></el-icon>
              <span>{{ item.commentCount || 0 }}</span>
            </div>
            <div class="stat-item">
              <el-icon><Star /></el-icon>
              <span>{{ item.likeCount || 0 }}</span>
            </div>
          </div>
        </div>
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
import { Search, Plus, UserFilled, ChatDotRound, Star } from '@element-plus/icons-vue';
import { fetchExchangePage, type ExchangeArticleItem } from '@/services/portal';
import { useUserStore } from '@/store/user';

// 中文注释：交流区文章列表，优化为帖子流样式
const router = useRouter();
const userStore = useUserStore();
const keyword = ref('');
const loading = ref(false);
const items = ref<ExchangeArticleItem[]>([]);
const pager = reactive({ current: 1, size: 12, total: 0 }); // 增加每页显示数量

const formatTime = (time?: string) => {
  if (!time) return '-';
  const date = new Date(time);
  const now = new Date();
  const diff = now.getTime() - date.getTime();
  
  // 小于24小时显示相对时间
  if (diff < 24 * 3600 * 1000) {
    if (diff < 3600 * 1000) return `${Math.ceil(diff / 60000)}分钟前`;
    return `${Math.floor(diff / 3600000)}小时前`;
  }
  return date.toLocaleDateString();
};

const snippet = (content: string) => {
  const plain = content.replace(/<[^>]+>/g, '').trim();
  if (!plain) return '';
  return plain.length > 80 ? `${plain.slice(0, 80)}...` : plain;
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
  background-color: var(--bg-body);
}

.content-card {
  width: min(1200px, 100%);
  padding: 24px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(15, 23, 42, 0.04);
}

.section-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24px;
  
  .title-group {
    display: flex;
    flex-direction: column;
    gap: 4px;
    
    h3 {
      margin: 0;
      font-size: 24px;
      color: #1e293b;
    }
    
    .subtitle {
      font-size: 14px;
      color: #64748b;
    }
  }
}

.action-bar-mobile {
  display: none;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  gap: 16px;
  flex-wrap: wrap;

  .search-box {
    display: flex;
    gap: 8px;
    flex: 1;
    max-width: 400px;
  }
}

.article-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: 20px;
}

.post-card {
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  gap: 12px;
  position: relative;
  overflow: hidden;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 12px 24px -10px rgba(0, 0, 0, 0.1);
    border-color: var(--primary-color);
  }
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;

  .user-info {
    display: flex;
    align-items: center;
    gap: 10px;
    
    .avatar {
      flex-shrink: 0;
      background: #f1f5f9;
      color: #94a3b8;
    }

    .meta {
      display: flex;
      flex-direction: column;
      line-height: 1.3;
      
      .nickname {
        font-size: 14px;
        font-weight: 500;
        color: #334155;
        &:hover { color: var(--primary-color); }
      }
      
      .time {
        font-size: 12px;
        color: #94a3b8;
      }
    }
  }
}

.post-body {
  display: flex;
  gap: 16px;
  flex: 1;
  
  .content-area {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 8px;
  }

  .post-title {
    margin: 0;
    font-size: 16px;
    font-weight: 600;
    color: #0f172a;
    line-height: 1.4;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }

  .post-summary {
    margin: 0;
    font-size: 14px;
    color: #64748b;
    line-height: 1.5;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }

  .cover-wrapper {
    flex-shrink: 0;
    width: 96px;
    height: 96px;
    border-radius: 8px;
    overflow: hidden;
    background: #f8fafc;
    
    .post-cover {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.3s;
    }
  }

  &:hover .post-cover {
    transform: scale(1.05);
  }
}

.post-footer {
  display: flex;
  gap: 16px;
  padding-top: 12px;
  border-top: 1px solid #f1f5f9;
  
  .stat-item {
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 13px;
    color: #64748b;
    
    .el-icon {
      font-size: 16px;
    }
    
    &:hover {
      color: var(--primary-color);
    }
  }
}

.pager {
  display: flex;
  justify-content: center;
  margin-top: 32px;
}

/* 响应式适配 */
@media (max-width: 768px) {
  .page-wrapper {
    padding: 16px;
  }
  
  .content-card {
    padding: 16px;
    border-radius: 0;
    box-shadow: none;
    background: transparent;
  }

  .section-title {
    margin-bottom: 16px;
    
    .action-bar-mobile {
      display: block;
    }
  }

  .toolbar {
    .actions {
      display: none; // 移动端隐藏发布按钮，使用FAB
    }
    .search-box {
      max-width: 100%;
    }
  }

  .article-grid {
    grid-template-columns: 1fr; // 单列
    gap: 12px;
  }

  .post-card {
    padding: 12px;
    
    .post-body {
      flex-direction: row; // 保持左文右图
    }
  }

  // 浮动按钮
  .fab-btn {
    position: fixed;
    bottom: 24px;
    right: 24px;
    z-index: 100;
    box-shadow: 0 4px 12px rgba(44, 123, 229, 0.4);
    width: 56px;
    height: 56px;
    font-size: 24px;
  }
}
</style>
