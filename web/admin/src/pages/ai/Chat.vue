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
          <ElFormItem label="流式模式">
            <ElSwitch v-model="streamMode" inline-prompt active-text="流式" inactive-text="标准" />
          </ElFormItem>
          <ElFormItem>
            <ElButton type="primary" :loading="sending" @click="handleSend">{{ streamMode ? '开始流式对话' : '发送' }}</ElButton>
            <ElButton text type="danger" @click="resetConversation">清空会话</ElButton>
            <ElButton
              v-if="streaming"
              type="warning"
              text
              :loading="streaming"
              @click="stopStreaming"
            >停止流式</ElButton>
          </ElFormItem>
          <ElFormItem label="会话 ID" v-if="conversationId">
            <ElInput v-model="conversationId" readonly />
          </ElFormItem>
          <ElFormItem label="使用模型" v-if="conversation">
            <span>{{ conversation?.modelName || '-' }}（{{ conversation?.providerName || '未知服务商' }}）</span>
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
              <span>待执行操作</span>
              <RiskTag :level="confirmSheet?.riskLevel" />
            </div>
          </template>
          <p class="tool-summary">{{ confirmSheet?.summary || pendingTool.summary || 'AI 建议执行操作' }}</p>
          <ElDescriptions v-if="confirmParamsEntries.length" :column="1" border>
            <ElDescriptionsItem v-for="([key, value]) in confirmParamsEntries" :key="key" :label="key">
              {{ formatParam(value) }}
            </ElDescriptionsItem>
          </ElDescriptions>
          <ElFormItem label="备注" class="mt">
            <ElInput v-model="toolComment" type="textarea" rows="3" placeholder="可选，作为授权备注传至后端" />
          </ElFormItem>
          <div class="tool-actions">
            <ElButton type="warning" :loading="rejecting" @click="denyTool">拒绝执行</ElButton>
            <ElButton type="primary" :loading="approving" @click="approveTool">同意执行</ElButton>
          </div>
        </ElCard>

        <ElCard class="mt">
          <template #header>
            <span>流式响应</span>
          </template>
          <template v-if="streamingReply">
            <p class="streaming-text">{{ streamingReply }}</p>
            <ElProgress :percentage="streaming ? 60 : 100" :status="streaming ? 'active' : 'success'" />
          </template>
          <ElAlert v-else type="info" :closable="false" title="开启流式模式后将在此处实时展示模型回复" />
        </ElCard>
      </ElCol>
    </ElRow>

  </div>
</template>

<script setup lang="ts">
import { computed, reactive, ref, watch } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import RiskTag from '@/components/common/RiskTag.vue';
import {
  chatApi,
  startChatStream,
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
const sending = ref(false);
const conversationId = ref('');
const conversation = ref<AiChatResponse | null>(null);
const confirmSheet = ref<ConfirmSheet | null>(null);
const toolComment = ref('');
const approving = ref(false);
const rejecting = ref(false);
const streamMode = ref(false);
const streaming = ref(false);
const streamingReply = ref('');
let stopController: (() => void) | null = null;

const history = computed(() => conversation.value?.history || []);
const structured = computed(() => conversation.value?.structured);
const pendingTool = computed(() => conversation.value?.pendingTool || null);
const confirmParamsEntries = computed(() => {
  const params = confirmSheet.value?.params || {};
  return Object.entries(params);
});

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
  if (streamMode.value) {
    beginStream();
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
  persistConversation();
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
  stopStreaming();
  cleanupStorage();
}

function beginStream() {
  if (streaming.value) {
    return ElMessage.info('正在流式输出');
  }
  streaming.value = true;
  streamingReply.value = '';
  sending.value = true;
  stopController = startChatStream(
    { conversationId: conversationId.value || undefined, message: decoratedMessage.value },
    (event) => {
      if (event.type === 'chunk') {
        streamingReply.value += event.data?.content || '';
      }
      if (event.type === 'end') {
        applyConversation(event.data as AiChatResponse);
        finishStream();
      }
      if (event.type === 'error') {
        ElMessage.error(event.data?.message || '流式请求失败');
        finishStream();
      }
    }
  );
}

function stopStreaming() {
  if (stopController) {
    stopController();
    stopController = null;
  }
  finishStream();
}

function finishStream() {
  streaming.value = false;
  sending.value = false;
  streamingReply.value = '';
}

function formatParam(value: unknown) {
  if (typeof value === 'object' && value !== null) {
    return JSON.stringify(value, null, 2);
  }
  return value ?? '-';
}

async function ensureHighRiskConfirm() {
  if (confirmSheet.value?.riskLevel !== 'L3') return;
  await ElMessageBox.prompt('请输入 YES 以确认执行高风险操作', '高风险确认', {
    inputPattern: /^YES$/,
    inputErrorMessage: '请输入 YES'
  });
}

async function approveTool() {
  if (!pendingTool.value || !conversationId.value) return;
  await ensureHighRiskConfirm();
  const traceId = createTraceId('ai');
  approving.value = true;
  try {
    console.info('操作摘要', {
      traceId,
      action: 'aiToolApprove',
      params: { conversationId: conversationId.value, toolCallId: pendingTool.value.toolCallId }
    });
    await confirmTool(conversationId.value, {
      toolCallId: pendingTool.value.toolCallId,
      comment: toolComment.value
    });
    ElMessage.success('已同意执行');
    appendAuthLog('approve');
    await reloadConversation();
  } finally {
    approving.value = false;
  }
}

async function denyTool() {
  if (!pendingTool.value || !conversationId.value) return;
  await ElMessageBox.confirm('确认拒绝执行该 AI 建议吗？', '提示', { type: 'warning' });
  const traceId = createTraceId('ai');
  rejecting.value = true;
  try {
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
    appendAuthLog('deny');
    await reloadConversation();
  } finally {
    rejecting.value = false;
  }
}

// 本地存储与自动清理
const STORAGE_PREFIX = 'ai:conv:';
const STORAGE_AUTH_LOGS = 'ai:authLogs';

function persistConversation() {
  if (!conversationId.value || !conversation.value) return;
  const key = STORAGE_PREFIX + conversationId.value;
  const snapshot = { history: conversation.value.history, updatedAt: Date.now() };
  try {
    localStorage.setItem(key, JSON.stringify(snapshot));
  } catch {}
}

function cleanupStorage() {
  const now = Date.now();
  const ttl = 30 * 24 * 60 * 60 * 1000; // 30 天
  try {
    const keys = Object.keys(localStorage).filter((k) => k.startsWith(STORAGE_PREFIX));
    keys.forEach((k) => {
      const raw = localStorage.getItem(k);
      if (!raw) return;
      try {
        const obj = JSON.parse(raw);
        // 清理过期会话
        if (!obj?.updatedAt || now - obj.updatedAt > ttl) {
          localStorage.removeItem(k);
          return;
        }
        // 超长消息裁剪
        if (Array.isArray(obj.history) && obj.history.length > 100) {
          obj.history = obj.history.slice(-100);
          localStorage.setItem(k, JSON.stringify(obj));
        }
      } catch {}
    });
  } catch {}
}

function appendAuthLog(action: 'approve' | 'deny') {
  const entry = {
    ts: new Date().toISOString(),
    action,
    conversationId: conversationId.value,
    toolCallId: pendingTool.value?.toolCallId,
    comment: toolComment.value
  };
  try {
    const raw = localStorage.getItem(STORAGE_AUTH_LOGS);
    const arr = raw ? JSON.parse(raw) : [];
    arr.push(entry);
    // 只保留最近 200 条
    if (arr.length > 200) arr.splice(0, arr.length - 200);
    localStorage.setItem(STORAGE_AUTH_LOGS, JSON.stringify(arr));
  } catch {}
}

watch(conversation, persistConversation);
cleanupStorage();
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
.streaming-text {
  min-height: 80px;
  white-space: pre-wrap;
  margin-bottom: 12px;
}
</style>
