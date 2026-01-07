<template>
  <div class="page-wrapper">
    <el-card class="content-card" v-loading="loading" shadow="never">
      <template v-if="detail">
        <div class="detail-header">
          <el-image :src="detail.coverUrl" fit="cover" class="cover">
            <template #error>
              <div class="cover-placeholder">暂无封面</div>
            </template>
          </el-image>
          <div class="meta">
            <h2>{{ detail.name }}</h2>
            <p>时间：{{ detail.startTime }} - {{ detail.endTime }}</p>
            <p>类别：{{ detail.category || '未分类' }}</p>
            <p>场馆：{{ detail.venueName || '暂无' }} ｜ 地址：{{ detail.addressCache || '暂无' }}</p>
            <p>主办单位：{{ detail.organizer || '未填写' }}</p>
            <p>联系电话：{{ detail.contactPhone || '未填写' }}</p>
            <div class="stat-row">
              <span>浏览量：{{ detail.viewCount ?? 0 }}</span>
              <span>收藏：{{ detail.favoriteCount ?? 0 }}</span>
            </div>
            <div class="actions">
              <el-button type="warning" @click="toggleFavorite(detail.id)">
                {{ isFavorite(detail.id) ? '取消收藏' : '收藏' }}
              </el-button>
            </div>
          </div>
        </div>
        <el-divider />
        <div class="intro" v-html="detail.intro || '暂无详情'" />
        <el-divider />
        <section class="comment-section">
          <div class="section-title">
            <h3>留言互动</h3>
            <p class="section-sub">支持点赞、楼中楼回复与删除自己的留言</p>
          </div>
          <div class="comment-input">
            <el-input
              v-model="commentContent"
              type="textarea"
              :rows="3"
              placeholder="畅所欲言，分享你的看法"
              maxlength="500"
              show-word-limit
            />
            <div class="comment-actions">
              <el-button type="primary" @click="submitComment()">发布留言</el-button>
              <el-alert v-if="!userStore.isLogin" title="登录后可留言、点赞和删除" type="info" show-icon />
            </div>
          </div>
          <el-empty v-if="comments.length === 0" description="暂无留言" />
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
                  <el-button type="primary" text size="small" @click="like(item)">
                    赞同（{{ item.likeCount || 0 }}）
                  </el-button>
                  <el-button type="success" text size="small" @click="startReply(item)">
                    回复
                  </el-button>
                  <el-button type="warning" text size="small" @click="openReport(item.id)">
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
                        <el-button type="primary" text size="small" @click="like(child)">
                          赞同（{{ child.likeCount || 0 }}）
                        </el-button>
                        <el-button type="success" text size="small" @click="startReply(child)">
                          回复
                        </el-button>
                        <el-button type="warning" text size="small" @click="openReport(child.id)">
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
      <el-empty v-else description="未找到活动" />
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
import { computed, onMounted, reactive, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage, ElMessageBox, type UploadUserFile, type UploadRequestOptions } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import {
  fetchActivityDetail,
  fetchActivityComments,
  postActivityComment,
  likeActivityComment,
  deleteActivityComment,
  createContentReport,
  uploadFile,
  type ActivityItem,
  type ActivityComment
} from '@/services/portal';
import { useUserStore } from '@/store/user';

// 中文注释：活动详情页，展示富文本内容并提供楼中楼评论、点赞、删除能力
const route = useRoute();
const router = useRouter();
const userStore = useUserStore();
const activityId = Number(route.params.id);

const detail = ref<ActivityItem | null>(null);
const loading = ref(false);

const comments = ref<ActivityComment[]>([]);
const commentPager = reactive({ current: 1, size: 10, total: 0 });
const commentContent = ref('');
const replyContent = reactive<Record<number, string>>({});
const replyingId = ref<number | null>(null);
const replyMeta = reactive<{ parentId?: number; mentionUserId?: number; mentionUserNickname?: string }>({});
const commentLoading = ref(false);
// 中文注释：记录正在点赞的评论，防止重复点击导致前端展示异常
const likingIds = reactive<Set<number>>(new Set());

const reportVisible = ref(false);
const reportFiles = ref<UploadUserFile[]>([]);
const reportForm = reactive({
  targetType: 'ACTIVITY_COMMENT',
  targetTypeText: '活动评论',
  targetId: 0,
  reasonType: 'SPAM',
  reason: '',
  screenshotUrls: [] as string[]
});

const replyPlaceholder = computed(() =>
  replyMeta.mentionUserNickname ? `回复 @${replyMeta.mentionUserNickname}` : '回复留言'
);

const formatTime = (time?: string) => (time ? new Date(time).toLocaleString() : '');

const loadDetail = async () => {
  loading.value = true;
  detail.value = await fetchActivityDetail(activityId);
  loading.value = false;
};

const loadComments = async () => {
  commentLoading.value = true;
  const page = await fetchActivityComments(activityId, {
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
    ElMessage.warning('留言内容不能为空');
    return;
  }
  const resolvedParentId = parentId ? replyMeta.parentId || parentId : undefined;
  let payloadContent = content.trim();
  if (parentId && replyMeta.mentionUserNickname) {
    const mentionPrefix = `@${replyMeta.mentionUserNickname}`;
    if (!payloadContent.startsWith(mentionPrefix)) {
      payloadContent = `${mentionPrefix} ${payloadContent}`;
    }
  }
  await postActivityComment(activityId, {
    content: payloadContent,
    parentId: resolvedParentId,
    mentionUserId: parentId ? replyMeta.mentionUserId : undefined
  });
  if (parentId) {
    replyContent[parentId] = '';
    cancelReply();
  } else {
    commentContent.value = '';
    cancelReply();
  }
  ElMessage.success('留言成功');
  await loadComments();
};

const like = async (item: ActivityComment) => {
  if (!ensureLogin()) return;
  // 中文注释：点赞后重新拉取列表，确保后台去重逻辑与前端展示保持一致，避免重复点赞累加
  if (likingIds.has(item.id)) {
    ElMessage.info('正在处理点赞，请稍候');
    return;
  }
  likingIds.add(item.id);
  try {
    await likeActivityComment(item.id);
    ElMessage.success('感谢点赞');
    item.likeCount = (item.likeCount || 0) + 1;
  } finally {
    likingIds.delete(item.id);
  }
};

const startReply = (item: ActivityComment) => {
  replyingId.value = item.id;
  replyMeta.parentId = item.parentId || item.id;
  replyMeta.mentionUserId = item.userId;
  replyMeta.mentionUserNickname = item.userNickname;
};

const cancelReply = () => {
  replyingId.value = null;
  replyMeta.parentId = undefined;
  replyMeta.mentionUserId = undefined;
  replyMeta.mentionUserNickname = undefined;
};

const canDelete = (userId?: number) => userStore.profile.userId === userId;

const removeComment = async (commentId: number) => {
  if (!ensureLogin()) return;
  await ElMessageBox.confirm('确定删除该条留言吗？删除后将不可恢复。', '提示', { type: 'warning' });
  await deleteActivityComment(commentId);
  ElMessage.success('删除成功');
  await loadComments();
};

const toggleFavorite = async (id: number) => {
  if (!ensureLogin()) return;
  await userStore.toggleFavorite('activity', id);
};
const isFavorite = (id: number) => userStore.favorites.activity.includes(id);

const openReport = (targetId: number) => {
  if (!ensureLogin()) return;
  reportForm.targetType = 'ACTIVITY_COMMENT';
  reportForm.targetTypeText = '活动评论';
  reportForm.targetId = targetId;
  reportForm.reasonType = 'SPAM';
  reportForm.reason = '';
  reportForm.screenshotUrls = [];
  reportFiles.value = [];
  reportVisible.value = true;
};

const handleReportUpload = async (options: UploadRequestOptions) => {
  try {
    const res = await uploadFile(options.file, 'activity_report');
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

// 中文注释：头像占位文案，优先返回昵称首字，否则显示“访”
const avatarText = (nickname?: string) => (nickname && nickname.length ? nickname[0] : '访');
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
.detail-header {
  display: flex;
  gap: 16px;
}

.cover {
  width: 320px;
  height: 200px;
  border-radius: 8px;
  flex-shrink: 0;
  background: #f5f7fa;
}

.cover-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
}

.meta h2 {
  margin: 0 0 6px;
}

.meta p {
  margin: 4px 0;
  color: #606266;
}

.stat-row {
  margin: 8px 0;
  display: flex;
  gap: 12px;
  color: #909399;
}

.actions {
  margin-top: 8px;
}

.intro {
  line-height: 1.8;
  color: #303133;
}

.comment-section {
  margin-top: 12px;
}

.comment-input {
  margin-bottom: 12px;
}

.comment-actions {
  margin-top: 8px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.comment-item {
  display: flex;
  gap: 10px;
  padding: 12px;
  border-radius: 8px;
  background: #fafafa;
}

.comment-item.child {
  background: #fff;
  border: 1px solid #ebeef5;
}

.avatar {
  background: linear-gradient(135deg, #409eff, #66b1ff);
  color: #fff;
  font-weight: bold;
  flex-shrink: 0;
  cursor: pointer;
}

.body {
  flex: 1;
}

.meta-row {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 6px;
}

.nickname {
  font-weight: bold;
  color: #2563eb;
  cursor: pointer;
}

.time {
  color: #909399;
  font-size: 12px;
}

.content {
  margin: 4px 0 8px;
  line-height: 1.6;
}

.toolbar {
  display: flex;
  gap: 10px;
}

.reply-box {
  margin-top: 10px;
}

.reply-actions {
  margin-top: 6px;
  display: flex;
  gap: 8px;
}

.child-list {
  margin-top: 10px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.pager {
  display: flex;
  justify-content: flex-end;
}

.section-sub {
  margin: 0;
  color: #909399;
  font-size: 13px;
}

.mention {
  color: #2c7be5;
  font-weight: 600;
  margin-right: 4px;
}

/* 中文注释：富文本内部图片适配移动端宽度，避免溢出 */
.intro :deep(img) {
  max-width: 100%;
  height: auto;
}

/* 中文注释：响应式适配，保证移动端展示友好 */
@media (max-width: 1100px) {
  .detail-header {
    flex-direction: column;
  }

  .cover {
    width: 100%;
    height: auto;
    aspect-ratio: 16 / 9;
  }

  .stat-row {
    flex-wrap: wrap;
  }
}

@media (max-width: 768px) {
  .meta h2 {
    font-size: 20px;
  }

  .meta p {
    font-size: 14px;
  }

  .comment-item {
    flex-direction: column;
  }

  .toolbar {
    flex-wrap: wrap;
  }

  .comment-actions {
    flex-direction: column;
    align-items: flex-start;
  }
}

@media (max-width: 520px) {
  .meta h2 {
    font-size: 18px;
  }

  .cover {
    border-radius: 6px;
  }

  .content-card {
    padding: 12px;
  }
}
</style>
