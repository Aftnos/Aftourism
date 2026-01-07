<template>
  <div class="page-wrapper">
    <div class="content-card">
      <div class="section-title">
        <h3>留言与反馈</h3>
        <span>欢迎提出建议与想法，我们会认真处理</span>
      </div>
      <el-alert v-if="!userStore.isLogin" title="登录后可提交留言与反馈" type="info" show-icon />
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px" class="feedback-form">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="类型" prop="type">
              <el-select v-model="form.type" placeholder="选择类型" style="width: 100%">
                <el-option label="留言" value="MESSAGE" />
                <el-option label="反馈" value="FEEDBACK" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标题" prop="title">
              <el-input v-model="form.title" placeholder="可填写标题" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="contactPhone">
              <el-input v-model="form.contactPhone" placeholder="方便我们联系" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系邮箱" prop="contactEmail">
              <el-input v-model="form.contactEmail" placeholder="如需回复请留下邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="内容" prop="content">
              <el-input
                v-model="form.content"
                type="textarea"
                :rows="4"
                maxlength="1000"
                show-word-limit
                placeholder="请描述你的留言或反馈"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item>
              <el-button type="primary" @click="submit" :disabled="!userStore.isLogin">提交</el-button>
              <el-button @click="reset">清空</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <el-divider />

      <div class="list-header">
        <h4>最新留言</h4>
        <el-select v-model="filterType" placeholder="全部类型" @change="loadList">
          <el-option label="全部" value="" />
          <el-option label="留言" value="MESSAGE" />
          <el-option label="反馈" value="FEEDBACK" />
        </el-select>
      </div>

      <el-empty v-if="items.length === 0" description="暂无留言" />
      <div v-else class="feedback-grid" v-loading="loading">
        <el-card
          v-for="item in items"
          :key="item.id"
          shadow="hover"
          class="feedback-card"
          @click="goDetail(item.id)"
        >
          <div class="card-header">
            <div class="type">
              <el-tag size="small" :type="item.type === 'FEEDBACK' ? 'warning' : 'info'">
                {{ item.typeText || typeText(item.type) }}
              </el-tag>
              <el-tag
                v-if="item.type === 'FEEDBACK'"
                size="small"
                :type="statusTagType(item.status)"
                effect="plain"
              >
                {{ item.statusText || statusText(item.status) }}
              </el-tag>
            </div>
            <span class="time">{{ formatTime(item.createTime) }}</span>
          </div>
          <h5 class="title">{{ item.title || '未填写标题' }}</h5>
          <p class="content">{{ item.content }}</p>
          <div class="card-footer">
            <span>{{ item.userNickname || '游客' }}</span>
            <span>评论 {{ item.commentCount ?? 0 }}</span>
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
import { ElMessage, type FormInstance, type FormRules } from 'element-plus';
import { useRouter } from 'vue-router';
import { createMessageFeedback, fetchMessageFeedbackPage, type MessageFeedbackItem } from '@/services/portal';
import { useUserStore } from '@/store/user';

// 中文注释：留言反馈列表页，包含提交表单与列表展示
const userStore = useUserStore();
const router = useRouter();
const formRef = ref<FormInstance>();
const loading = ref(false);
const items = ref<MessageFeedbackItem[]>([]);
const pager = reactive({ current: 1, size: 8, total: 0 });
const filterType = ref('');

const form = reactive({
  type: 'MESSAGE',
  title: '',
  content: '',
  contactPhone: '',
  contactEmail: ''
});

const rules: FormRules = {
  type: [{ required: true, message: '请选择类型', trigger: 'change' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
};

const statusText = (status?: number) => {
  if (status === 1) return '已反馈';
  return '待反馈';
};

const typeText = (type?: string) => (type === 'FEEDBACK' ? '反馈' : '留言');
const statusTagType = (status?: number) => (status === 1 ? 'success' : 'warning');
const formatTime = (time?: string) => (time ? new Date(time).toLocaleString() : '-');

const loadList = async () => {
  loading.value = true;
  const page = await fetchMessageFeedbackPage({
    current: pager.current,
    size: pager.size,
    type: filterType.value || undefined
  });
  items.value = page.list || [];
  pager.total = page.total || 0;
  loading.value = false;
};

const submit = () => {
  if (!formRef.value) return;
  formRef.value.validate(async (valid) => {
    if (!valid) return;
    if (!userStore.isLogin) {
      ElMessage.warning('请先登录后再提交');
      return;
    }
    await createMessageFeedback({
      type: form.type,
      title: form.title || undefined,
      content: form.content,
      contactPhone: form.contactPhone || undefined,
      contactEmail: form.contactEmail || undefined
    });
    ElMessage.success('提交成功');
    reset();
    await loadList();
  });
};

const reset = () => {
  form.title = '';
  form.content = '';
  form.contactPhone = '';
  form.contactEmail = '';
  form.type = 'MESSAGE';
};

const goDetail = (id: number) => router.push(`/feedback/${id}`);

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

.feedback-form {
  margin-top: 16px;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.feedback-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 16px;
}

.feedback-card {
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.feedback-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 30px rgba(59, 130, 246, 0.12);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.card-header .type {
  display: flex;
  gap: 8px;
}

.title {
  margin: 0 0 8px;
  font-size: 16px;
  color: #1f2937;
}

.content {
  font-size: 14px;
  color: #475569;
  line-height: 1.6;
  min-height: 44px;
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-footer {
  margin-top: 12px;
  display: flex;
  justify-content: space-between;
  color: #94a3b8;
  font-size: 12px;
}

.pager {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
