<template>
  <div class="page-wrapper">
    <el-card class="content-card" v-loading="loading" shadow="never">
      <template v-if="detail">
        <div class="detail-header">
          <div class="title-area">
            <h2>{{ detail.title }}</h2>
            <p class="meta">
              <span class="user" @click="goUser(detail.userId)">{{ detail.userNickname || '游客' }}</span>
              <span>{{ formatTime(detail.createTime) }}</span>
              <span>评论 {{ detail.commentCount || 0 }}</span>
              <span>点赞 {{ detail.likeCount || 0 }}</span>
            </p>
          </div>
          <div class="actions">
            <el-button type="primary" plain @click="likeArticle">点赞</el-button>
            <el-button type="danger" plain @click="openReport('ARTICLE', detail.id)">举报</el-button>
          </div>
        </div>
        <el-divider />
        <div class="article-content" v-html="detail.content" />

        <el-divider />
        <section class="comment-section">
          <div class="section-title">
            <h3>评论互动</h3>
            <p class="section-sub">支持点赞、楼中楼回复与艾特</p>
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
              <el-alert v-if="!userStore.isLogin" title="登录后可评论、点赞和举报" type="info" show-icon />
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
                <div class="content">
                  <span v-if="item.mentionUserNickname" class="mention">@{{ item.mentionUserNickname }}</span>
                  {{ item.content }}
                </div>
                <div class="toolbar">
                  <el-button type="primary" text size="small" @click="likeComment(item)">
                    赞同（{{ item.likeCount || 0 }}）
                  </el-button>
                  <el-button type="success" text size="small" @click="startReply(item)">
                    回复
                  </el-button>
                  <el-button type="warning" text size="small" @click="openReport('COMMENT', item.id)">
                    举报
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
                    :placeholder="replyPlaceholder"
                    maxlength="500"
                    show-word-limit
                  />
                  <div class="reply-actions">
                    <el-button size="small" @click="cancelReply">取消</el-button>
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
                      <div class="content">
                        <span v-if="child.mentionUserNickname" class="mention">@{{ child.mentionUserNickname }}</span>
                        {{ child.content }}
                      </div>
                      <div class="toolbar">
                        <el-button type="primary" text size="small" @click="likeComment(child)">
                          赞同（{{ child.likeCount || 0 }}）
                        </el-button>
                        <el-button type="success" text size="small" @click="startReply(child)">
                          回复
                        </el-button>
                        <el-button type="warning" text size="small" @click="openReport('COMMENT', child.id)">
                          举报
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
                      <div v-if="replyingId === child.id" class="reply-box">
                        <el-input
                          v-model="replyContent[child.id]"
                          type="textarea"
                          :rows="2"
                          :placeholder="replyPlaceholder"
                          maxlength="500"
                          show-word-limit
                        />
                        <div class="reply-actions">
                          <el-button size="small" @click="cancelReply">取消</el-button>
                          <el-button type="primary" size="small" @click="submitComment(child.id)">发送</el-button>
                        </div>
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
      <el-empty v-else description="未找到交流文章" />
    </el-card>

    <el-dialog v-model="reportVisible" title="举报内容" width="520px">
      <el-form :model="reportForm" label-width="90px">
        <el-form-item label="举报类型">
          <el-input v-model="reportForm.targetTypeText" disabled />
        </el-form-item>
        <el-form-item label="原因类型">
          <el-select v-model="reportForm.reasonType" placeholder="请选择">
            <el-option label="垃圾广告" value="SPAM" />
            <el-option label="辱骂攻击" value="ABUSE" />
            <el-option label="不当内容" value="INAPPROPRIATE" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="原因说明">
          <el-input v-model="reportForm.reason" type="textarea" :rows="3" maxlength="500" show-word-limit />
        </el-form-item>
        <el-form-item label="截图">
          <el-upload
            action="#"
            list-type="picture-card"
            :file-list="reportFiles"
            :http-request="handleReportUpload"
            :on-remove="handleReportRemove"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="reportVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReport">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage, ElMessageBox, type UploadUserFile, type UploadRequestOptions } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import {
  fetchExchangeDetail,
  fetchExchangeComments,
  postExchangeComment,
  likeExchangeComment,
  deleteExchangeComment,
  likeExchangeArticle,
  createContentReport,
  uploadFile,
  type ExchangeArticleItem,
  type ExchangeCommentItem
} from '@/services/portal';
import { useUserStore } from '@/store/user';

// 中文注释：交流文章详情页，包含富文本内容与评论互动
const route = useRoute();
const router = useRouter();
const userStore = useUserStore();
const articleId = Number(route.params.id);

const detail = ref<ExchangeArticleItem | null>(null);
const loading = ref(false);

const comments = ref<ExchangeCommentItem[]>([]);
const commentPager = reactive({ current: 1, size: 10, total: 0 });
const commentContent = ref('');
const replyContent = reactive<Record<number, string>>({});
const replyingId = ref<number | null>(null);
const replyMeta = reactive<{ parentId?: number; mentionUserId?: number; mentionUserNickname?: string }>({});
const commentLoading = ref(false);
const likingIds = reactive<Set<number>>(new Set());

const reportVisible = ref(false);
const reportFiles = ref<UploadUserFile[]>([]);
const reportForm = reactive({
  targetType: 'ARTICLE',
  targetTypeText: '交流文章',
  targetId: 0,
  reasonType: 'SPAM',
  reason: '',
  screenshotUrls: [] as string[]
});

const replyPlaceholder = computed(() =>
  replyMeta.mentionUserNickname ? `回复 @${replyMeta.mentionUserNickname}` : '回复评论'
);

const formatTime = (time?: string) => (time ? new Date(time).toLocaleString() : '');
const avatarText = (nickname?: string) => (nickname && nickname.length ? nickname[0] : '访');

const loadDetail = async () => {
  loading.value = true;
  detail.value = await fetchExchangeDetail(articleId);
  loading.value = false;
};

const loadComments = async () => {
  commentLoading.value = true;
  const page = await fetchExchangeComments(articleId, {
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
  await postExchangeComment(articleId, {
    content: content.trim(),
    parentId: parentId || undefined,
    mentionUserId: parentId ? replyMeta.mentionUserId : undefined
  });
  if (parentId) {
    replyContent[parentId] = '';
    cancelReply();
  } else {
    commentContent.value = '';
    cancelReply();
  }
  ElMessage.success('评论成功');
  await loadComments();
};

const likeComment = async (item: ExchangeCommentItem) => {
  if (!ensureLogin()) return;
  if (likingIds.has(item.id)) return;
  likingIds.add(item.id);
  await likeExchangeComment(item.id);
  item.likeCount = (item.likeCount || 0) + 1;
  likingIds.delete(item.id);
};

const likeArticle = async () => {
  if (!ensureLogin()) return;
  await likeExchangeArticle(articleId);
  if (detail.value) {
    detail.value.likeCount = (detail.value.likeCount || 0) + 1;
  }
};

const startReply = (item: ExchangeCommentItem) => {
  replyingId.value = item.id;
  replyMeta.parentId = item.id;
  replyMeta.mentionUserId = item.userId;
  replyMeta.mentionUserNickname = item.userNickname;
};

const cancelReply = () => {
  replyingId.value = null;
  replyMeta.parentId = undefined;
  replyMeta.mentionUserId = undefined;
  replyMeta.mentionUserNickname = undefined;
};

const removeComment = async (id: number) => {
  if (!ensureLogin()) return;
  await ElMessageBox.confirm('确定删除该条评论吗？删除后将不可恢复。', '提示', { type: 'warning' });
  await deleteExchangeComment(id);
  ElMessage.success('删除成功');
  await loadComments();
};

const canDelete = (userId: number) => userStore.profile.userId === userId;

const openReport = (targetType: 'ARTICLE' | 'COMMENT', targetId: number) => {
  if (!ensureLogin()) return;
  reportForm.targetType = targetType;
  reportForm.targetTypeText = targetType === 'ARTICLE' ? '交流文章' : '交流评论';
  reportForm.targetId = targetId;
  reportForm.reasonType = 'SPAM';
  reportForm.reason = '';
  reportForm.screenshotUrls = [];
  reportFiles.value = [];
  reportVisible.value = true;
};

const handleReportUpload = async (options: UploadRequestOptions) => {
  try {
    const res = await uploadFile(options.file, 'exchange_report');
    reportFiles.value.push({ name: res.originalName || res.fileName, url: res.url });
    reportForm.screenshotUrls.push(res.url);
  } catch (error) {
    ElMessage.error('上传失败');
  }
};

const handleReportRemove = (file: UploadUserFile) => {
  reportFiles.value = reportFiles.value.filter((item) => item.url !== file.url);
  reportForm.screenshotUrls = reportForm.screenshotUrls.filter((url) => url !== file.url);
};

const submitReport = async () => {
  if (!ensureLogin()) return;
  await createContentReport({
    targetType: reportForm.targetType,
    targetId: reportForm.targetId,
    reasonType: reportForm.reasonType,
    reason: reportForm.reason || undefined,
    screenshotUrls: reportForm.screenshotUrls.length ? reportForm.screenshotUrls : undefined
  });
  ElMessage.success('举报已提交');
  reportVisible.value = false;
};

const goUser = (id: number) => router.push(`/user/${id}`);

onMounted(() => {
  loadDetail();
  loadComments();
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
  width: min(1200px, 100%);
}

.detail-header {
  display: flex;
  justify-content: space-between;
  gap: 16px;
}

.meta {
  display: flex;
  gap: 12px;
  color: #64748b;
  font-size: 14px;
}

.user {
  color: #2563eb;
  cursor: pointer;
}

.article-content {
  line-height: 1.8;
  color: #1f2937;
}

.comment-section {
  margin-top: 24px;
}

.comment-input {
  margin: 16px 0;
}

.comment-actions {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-top: 12px;
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.comment-item {
  display: flex;
  gap: 12px;
}

.comment-item.child {
  margin-left: 48px;
}

.body {
  flex: 1;
}

.meta-row {
  display: flex;
  gap: 12px;
  font-size: 13px;
  color: #64748b;
}

.content {
  margin: 6px 0;
  line-height: 1.6;
}

.mention {
  color: #2563eb;
  margin-right: 4px;
}

.toolbar {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.reply-box {
  margin-top: 10px;
}

.reply-actions {
  margin-top: 8px;
  display: flex;
  gap: 8px;
}

.pager {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
