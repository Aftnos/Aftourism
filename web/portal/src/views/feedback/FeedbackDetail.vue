<template>
  <div class="page-wrapper">
    <el-card class="content-card" v-loading="loading" shadow="never">
      <template v-if="detail">
        <div class="detail-header">
          <div class="title-area">
            <div class="type-tags">
              <el-tag size="small" :type="statusTagType(detail.status)" effect="plain">
                {{ detail.statusText || statusText(detail.status) }}
              </el-tag>
            </div>
            <h2>{{ detail.title || '未填写标题' }}</h2>
            <p class="meta">
              <span class="user" @click="goUser(detail.userId)">{{ detail.userNickname || '游客' }}</span>
              <span>{{ formatTime(detail.createTime) }}</span>
            </p>
          </div>
          <el-avatar :src="detail.userAvatar" :size="56" class="avatar" @click="goUser(detail.userId)">
            {{ avatarText(detail.userNickname) }}
          </el-avatar>
        </div>
        <el-divider />
        <p class="content">{{ detail.content }}</p>

        <el-divider />
        <section class="comment-section">
          <div class="section-title">
            <h3>评论互动</h3>
            <p class="section-sub">支持点赞、楼中楼回复与删除自己的留言</p>
          </div>
          <div class="comment-input">
            <el-input
              v-model="commentContent"
              type="textarea"
              :rows="3"
              placeholder="发表你的观点"
              maxlength="500"
              show-word-limit
            />
            <div class="comment-actions">
              <el-button type="primary" @click="submitComment()">发布评论</el-button>
              <el-alert v-if="!userStore.isLogin" title="登录后可评论、点赞和删除" type="info" show-icon />
            </div>
          </div>
          <el-empty v-if="comments.length === 0" description="暂无评论" />
          <div v-else class="comment-list" v-loading="commentLoading">
            <div v-for="item in comments" :key="item.id" class="comment-item">
              <el-avatar :src="item.userAvatar" :size="40" class="avatar" @click="goUser(item.userId)">
                {{ avatarText(item.userNickname) }}
              </el-avatar>
              <div class="body">
                <div class="meta-row">
                  <span class="nickname" @click="goUser(item.userId)">{{ item.userNickname || '游客' }}</span>
                  <span class="time">{{ formatTime(item.createTime) }}</span>
                </div>
                <div class="content">{{ item.content }}</div>
                <div class="toolbar">
                  <el-button type="primary" text size="small" @click="like(item)">
                    赞同（{{ item.likeCount || 0 }}）
                  </el-button>
                  <el-button type="success" text size="small" @click="toggleReply(item.id)">
                    回复
                  </el-button>
                  <el-button
                    v-if="canDelete(item.userId)"
                    type="danger"
                    text
                    size="small"
                    @click="removeComment(item.id)"
                  >
                    删除
                  </el-button>
                </div>
                <div v-if="replyingId === item.id" class="reply-box">
                  <el-input
                    v-model="replyContent[item.id]"
                    type="textarea"
                    :rows="2"
                    :placeholder="`回复 ${item.userNickname || '游客'}`"
                    maxlength="500"
                    show-word-limit
                  />
                  <div class="reply-actions">
                    <el-button size="small" @click="toggleReply(null)">取消</el-button>
                    <el-button type="primary" size="small" @click="submitComment(item.id)">发送</el-button>
                  </div>
                </div>
                <div v-if="item.children && item.children.length" class="child-list">
                  <div v-for="child in item.children" :key="child.id" class="comment-item child">
                    <el-avatar :src="child.userAvatar" :size="36" class="avatar" @click="goUser(child.userId)">
                      {{ avatarText(child.userNickname) }}
                    </el-avatar>
                    <div class="body">
                      <div class="meta-row">
                        <span class="nickname" @click="goUser(child.userId)">{{ child.userNickname || '游客' }}</span>
                        <span class="time">{{ formatTime(child.createTime) }}</span>
                      </div>
                      <div class="content">{{ child.content }}</div>
                      <div class="toolbar">
                        <el-button type="primary" text size="small" @click="like(child)">
                          赞同（{{ child.likeCount || 0 }}）
                        </el-button>
                        <el-button
                          v-if="canDelete(child.userId)"
                          type="danger"
                          text
                          size="small"
                          @click="removeComment(child.id)"
                        >
                          删除
                        </el-button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="pager">
              <el-pagination
                background
                layout="prev, pager, next"
                v-model:current-page="commentPager.current"
                :page-size="commentPager.size"
                :total="commentPager.total"
                @current-change="loadComments"
              />
            </div>
          </div>
        </section>
      </template>
      <el-empty v-else description="未找到反馈" />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import {
  fetchMessageFeedbackDetail,
  fetchMessageFeedbackComments,
  postMessageFeedbackComment,
  likeMessageFeedbackComment,
  deleteMessageFeedbackComment,
  type MessageFeedbackItem,
  type MessageFeedbackComment
} from '@/services/portal';
import { useUserStore } from '@/store/user';

// 中文注释：留言反馈详情页，包含评论互动功能
const route = useRoute();
const router = useRouter();
const userStore = useUserStore();
const feedbackId = Number(route.params.id);

const detail = ref<MessageFeedbackItem | null>(null);
const loading = ref(false);

const comments = ref<MessageFeedbackComment[]>([]);
const commentPager = reactive({ current: 1, size: 10, total: 0 });
const commentContent = ref('');
const replyContent = reactive<Record<number, string>>({});
const replyingId = ref<number | null>(null);
const commentLoading = ref(false);
const likingIds = reactive<Set<number>>(new Set());

const formatTime = (time?: string) => (time ? new Date(time).toLocaleString() : '');
const avatarText = (nickname?: string) => (nickname && nickname.length ? nickname[0] : '访');
const statusText = (status?: number) => (status === 1 ? '已反馈' : '待反馈');
const statusTagType = (status?: number) => (status === 1 ? 'success' : 'warning');

const loadDetail = async () => {
  loading.value = true;
  detail.value = await fetchMessageFeedbackDetail(feedbackId);
  loading.value = false;
};

const loadComments = async () => {
  commentLoading.value = true;
  const page = await fetchMessageFeedbackComments(feedbackId, {
    current: commentPager.current,
    size: commentPager.size
  });
  comments.value = page.list || [];
  commentPager.total = page.total || 0;
  commentLoading.value = false;
};

const ensureLogin = () => {
  if (!userStore.isLogin) {
    ElMessage.warning('请先登录后再进行互动');
    return false;
  }
  return true;
};

const submitComment = async (parentId?: number | null) => {
  if (!ensureLogin()) return;
  const content = parentId ? replyContent[parentId] : commentContent.value;
  if (!content || !content.trim()) {
    ElMessage.warning('评论内容不能为空');
    return;
  }
  await postMessageFeedbackComment(feedbackId, { content: content.trim(), parentId: parentId || undefined });
  if (parentId) {
    replyContent[parentId] = '';
    replyingId.value = null;
  } else {
    commentContent.value = '';
  }
  ElMessage.success('评论成功');
  await loadComments();
};

const like = async (item: MessageFeedbackComment) => {
  if (!ensureLogin()) return;
  if (likingIds.has(item.id)) return;
  likingIds.add(item.id);
  await likeMessageFeedbackComment(item.id);
  item.likeCount = (item.likeCount || 0) + 1;
  likingIds.delete(item.id);
};

const toggleReply = (id: number | null) => {
  replyingId.value = id;
};

const removeComment = async (id: number) => {
  if (!ensureLogin()) return;
  await ElMessageBox.confirm('确定删除该条评论吗？删除后将不可恢复。', '提示', { type: 'warning' });
  await deleteMessageFeedbackComment(id);
  ElMessage.success('删除成功');
  await loadComments();
};

const canDelete = (userId?: number) => userStore.profile.userId === userId;
const goUser = (userId?: number) => {
  if (!userId) return;
  router.push(`/user/${userId}`);
};

onMounted(async () => {
  await loadDetail();
  await loadComments();
});
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
  width: min(1100px, 100%);
}

.detail-header {
  display: flex;
  justify-content: space-between;
  gap: 24px;
  align-items: center;
}

.type-tags {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
}

.meta {
  display: flex;
  gap: 16px;
  font-size: 14px;
  color: #64748b;
}

.meta .user {
  cursor: pointer;
  color: #2563eb;
}

.content {
  margin: 0;
  line-height: 1.8;
  color: #334155;
}

.comment-section {
  margin-top: 24px;
}

.comment-input {
  margin: 16px 0;
}

.comment-actions {
  margin-top: 12px;
  display: flex;
  gap: 12px;
  align-items: center;
}

.comment-list {
  margin-top: 16px;
}

.comment-item {
  display: flex;
  gap: 16px;
  padding: 16px 0;
  border-bottom: 1px solid #f1f5f9;
}

.comment-item.child {
  padding-left: 16px;
}

.comment-item .body {
  flex: 1;
}

.meta-row {
  display: flex;
  gap: 12px;
  font-size: 13px;
  color: #94a3b8;
}

.meta-row .nickname {
  color: #1e293b;
  cursor: pointer;
}

.toolbar {
  display: flex;
  gap: 8px;
  margin-top: 8px;
}

.reply-box {
  margin-top: 12px;
  background: #f8fafc;
  padding: 12px;
  border-radius: 8px;
}

.reply-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 8px;
}

.child-list {
  margin-top: 12px;
  border-left: 2px solid #e2e8f0;
  padding-left: 12px;
}

.pager {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
