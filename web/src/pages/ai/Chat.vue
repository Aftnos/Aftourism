<template>
  <div class="page-card ai-chat">
    <ElRow :gutter="16">
      <ElCol :lg="14" :md="24">
        <ElForm :model="form" label-width="100px" class="chat-form">
          <ElFormItem label="输入问题">
            <ElInput
              v-model="form.message"
              type="textarea"
              rows="4"
              maxlength="2000"
              show-word-limit
              placeholder="所有输入都会原样发送至 /ai/conversations/chat"
            />
          </ElFormItem>
          <ElFormItem label="模式">
            <ElRadioGroup v-model="form.mode">
              <ElRadioButton label="DIRECT">DIRECT（实时调度）</ElRadioButton>
              <ElRadioButton label="PLAN">PLAN（偏向计划/确认）</ElRadioButton>
            </ElRadioGroup>
          </ElFormItem>
          <ElFormItem>
            <ElButton type="primary" :loading="sending" @click="handleSend">发送</ElButton>
            <ElButton text type="danger" @click="resetConversation">清空会话</ElButton>
          </ElFormItem>
          <ElFormItem label="会话 ID" v-if="conversationId">
            <ElInput v-model="conversationId" readonly />
          </ElFormItem>
        </ElForm>

        <ElCard class="mt">
          <template #header>
            <div class="card-header">
              <span>会话记录</span>
              <ElButton v-if="conversationId" size="small" text type="primary" @click="reloadConversation">刷新</ElButton>
            </div>
          </template>
          <ElTimeline v-if="history.length">
            <ElTimelineItem v-for="(msg, idx) in history" :key="idx" :type="msg.role === 'assistant' ? 'primary' : 'info'">
              <p class="msg-role">{{ msg.role === 'assistant' ? 'AI' : '管理员' }} · {{ formatTime(msg.timestamp) }}</p>
              <p class="msg-content">{{ msg.content }}</p>
            </ElTimelineItem>
          </ElTimeline>
          <ElEmpty v-else description="暂无对话" />
        </ElCard>
      </ElCol>

      <ElCol :lg="10" :md="24">
        <ElAlert
          v-if="conversation?.closed"
          type="warning"
          :closable="false"
          title="会话已被后端关闭，原因："
          class="mb"
        >
          {{ conversation?.closeReason || '未知' }}
        </ElAlert>
        <ElCard>
          <template #header>
            <span>AI 回复摘要</span>
          </template>
          <div v-if="structured">
            <p>{{ structured.reply || '暂无摘要' }}</p>
            <ElDivider v-if="structured.actions?.length" />
            <div v-if="structured.actions?.length" class="actions">
              <h4>建议动作</h4>
              <ul>
                <li v-for="(action, idx) in structured.actions" :key="idx">{{ action.type }}：{{ action.detail }}</li>
              </ul>
            </div>
            <ElAlert
              v-if="structured.securityNotice"
              type="error"
              :closable="false"
              class="mt"
              :title="structured.securityNotice"
            />
          </div>
          <ElEmpty v-else description="暂无数据" />
        </ElCard>

        <ElCard v-if="pendingTool" class="mt">
          <template #header>
            <div class="card-header">
              <span>待确认操作</span>
              <RiskTag :level="confirmSheet?.riskLevel" />
            </div>
          </template>
          <p class="tool-summary">{{ pendingTool.summary || '后端要求人工确认操作' }}</p>
          <ElDescriptions :column="1" border>
            <ElDescriptionsItem label="工具">{{ pendingTool.toolName }}</ElDescriptionsItem>
            <ElDescriptionsItem label="状态">{{ pendingTool.status }}</ElDescriptionsItem>
            <ElDescriptionsItem label="生成时间">{{ formatTime(pendingTool.createdAt) }}</ElDescriptionsItem>
            <ElDescriptionsItem label="描述">{{ pendingTool.description || '-' }}</ElDescriptionsItem>
          </ElDescriptions>
          <ElFormItem label="备注" class="mt">
            <ElInput v-model="toolComment" type="textarea" rows="3" placeholder="给后端的审批备注" />
          </ElFormItem>
          <div class="tool-actions">
            <ElButton type="warning" @click="denyTool">拒绝执行</ElButton>
            <ElButton type="primary" @click="openConfirm">确认执行</ElButton>
          </div>
        </ElCard>

        <ElCard class="mt">
          <template #header>
            <div class="card-header">
              <span>流式响应</span>
              <ElSwitch v-model="useStream" active-text="启用" inactive-text="关闭" />
            </div>
          </template>
          <SseViewer
            v-if="useStream"
            endpoint="/ai/conversations/chat/stream"
            :query="streamQuery"
            :disabled="!form.message"
            @closed="handleStreamClosed"
          />
          <ElEmpty v-else description="启用后可查看打字机效果" />
        </ElCard>
      </ElCol>
    </ElRow>

    <ConfirmDialog v-model="confirmVisible" :sheet="confirmSheet" @confirm="approveTool" />
  </div>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import SseViewer from '@/components/sse/SseViewer.vue';
import ConfirmDialog from '@/components/confirm/ConfirmDialog.vue';
import RiskTag from '@/components/common/RiskTag.vue';
import {
  chatApi,
  confirmTool,
  fetchConversation,
  createConfirmSheetFromTool,
  type AiChatResponse,
  type ConfirmSheet
} from '@/api/ai';
import { createTraceId } from '@/utils/trace';

const form = reactive({
  message: '',
  mode: 'DIRECT' as 'DIRECT' | 'PLAN'
});
const useStream = ref(false);
const sending = ref(false);
const conversationId = ref('');
const conversation = ref<AiChatResponse | null>(null);
const confirmVisible = ref(false);
const confirmSheet = ref<ConfirmSheet | null>(null);
const toolComment = ref('');
const streamSeed = ref(0);

const history = computed(() => conversation.value?.history || []);
const structured = computed(() => conversation.value?.structured);
const pendingTool = computed(() => conversation.value?.pendingTool || null);

const streamQuery = computed(() => ({
  message: decoratedMessage.value,
  conversationId: conversationId.value,
  seed: streamSeed.value
}));

const decoratedMessage = computed(() => (form.mode === 'PLAN' ? `[PLAN] ${form.message}` : form.message));

function formatTime(input?: string) {
  if (!input) return '-';
  const date = new Date(input);
  if (Number.isNaN(date.getTime())) return input;
  const pad = (num: number) => String(num).padStart(2, '0');
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(
    date.getMinutes()
  )}:${pad(date.getSeconds())}`;
}

async function handleSend() {
  if (!form.message) {
    return ElMessage.warning('请输入问题描述');
  }
  if (useStream.value) {
    streamSeed.value = Date.now();
    ElMessage.info('已进入流式模式，请在右侧查看输出');
    return;
  }
  sending.value = true;
  try {
    const res = await chatApi({ conversationId: conversationId.value || undefined, message: decoratedMessage.value });
    applyConversation(res);
  } finally {
    sending.value = false;
  }
}

function applyConversation(res: AiChatResponse) {
  conversation.value = res;
  conversationId.value = res.conversationId;
  toolComment.value = '';
  confirmSheet.value = pendingTool.value ? createConfirmSheetFromTool(pendingTool.value, res.structured) : null;
}

async function reloadConversation() {
  if (!conversationId.value) return;
  const res = await fetchConversation(conversationId.value);
  applyConversation(res);
}

function resetConversation() {
  conversationId.value = '';
  conversation.value = null;
  confirmSheet.value = null;
  toolComment.value = '';
}

function openConfirm() {
  if (!pendingTool.value) return;
  confirmSheet.value = createConfirmSheetFromTool(pendingTool.value, structured.value || undefined);
  confirmVisible.value = true;
}

async function approveTool() {
  if (!pendingTool.value || !conversationId.value) return;
  const traceId = createTraceId('ai');
  console.info('操作摘要', {
    traceId,
    action: 'aiToolApprove',
    params: { conversationId: conversationId.value, toolCallId: pendingTool.value.toolCallId }
  });
  await confirmTool(conversationId.value, {
    toolCallId: pendingTool.value.toolCallId,
    approved: true,
    comment: toolComment.value
  });
  ElMessage.success('已确认执行');
  confirmVisible.value = false;
  await reloadConversation();
}

async function denyTool() {
  if (!pendingTool.value || !conversationId.value) return;
  await ElMessageBox.confirm('确认拒绝执行该 AI 建议吗？', '提示', { type: 'warning' });
  const traceId = createTraceId('ai');
  console.info('操作摘要', {
    traceId,
    action: 'aiToolReject',
    params: { conversationId: conversationId.value, toolCallId: pendingTool.value.toolCallId }
  });
  await confirmTool(conversationId.value, {
    toolCallId: pendingTool.value.toolCallId,
    approved: false,
    comment: toolComment.value
  });
  ElMessage.success('已拒绝执行');
  await reloadConversation();
}

function handleStreamClosed() {
  ElMessage.info('流式响应已结束');
}
</script>

<style scoped>
.ai-chat {
  display: flex;
  flex-direction: column;
}
.chat-form {
  background: #fff;
  padding: 16px;
  border-radius: 8px;
}
.mt {
  margin-top: 16px;
}
.mb {
  margin-bottom: 16px;
}
.msg-role {
  color: #64748b;
  font-size: 12px;
  margin-bottom: 4px;
}
.msg-content {
  white-space: pre-line;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.tool-summary {
  margin-bottom: 8px;
}
.tool-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 12px;
}
.actions ul {
  padding-left: 16px;
}
</style>
