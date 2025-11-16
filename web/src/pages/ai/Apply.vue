<template>
  <div class="page-card ai-apply">
    <ElForm :model="form" label-width="110px">
      <ElFormItem label="会话 ID (可选)">
        <ElInput v-model="form.conversationId" placeholder="留空将自动创建新会话" />
      </ElFormItem>
      <ElFormItem label="申请描述">
        <ElInput
          v-model="form.content"
          type="textarea"
          rows="5"
          maxlength="2000"
          show-word-limit
          placeholder="请输入要执行的PLAN操作描述，将直接转发给后端"
        />
      </ElFormItem>
      <ElFormItem label="执行备注">
        <ElInput v-model="comment" type="textarea" rows="3" placeholder="可选，作为审批备注传至后端" />
      </ElFormItem>
      <ElFormItem>
        <ElButton type="primary" :loading="loading" @click="handlePlan">生成确认单</ElButton>
      </ElFormItem>
    </ElForm>

    <ElAlert
      title="PLAN 模式提示"
      type="info"
      :closable="false"
      description="本页面始终使用 PLAN 策略，前端先生成 ConfirmSheet，人工确认后才调用 /ai/conversations/{id}/confirm 执行。"
      class="mt"
    />

    <ElCard v-if="ticket" class="mt">
      <p>会话编号：{{ ticket.conversationId }}</p>
      <p>受理单号：{{ ticket.toolCallId }}</p>
      <p>状态：{{ ticket.status }}</p>
      <p v-if="ticket.message">消息：{{ ticket.message }}</p>
    </ElCard>

    <ConfirmDialog v-model="visible" :sheet="sheet" @confirm="handleExecute" />
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import ConfirmDialog from '@/components/confirm/ConfirmDialog.vue';
import { chatApi, confirmTool, createConfirmSheetFromTool, type ConfirmSheet, type AiPendingTool } from '@/api/ai';
import { createTraceId } from '@/utils/trace';

const form = reactive({ conversationId: '', content: '' });
const comment = ref('');
const sheet = ref<ConfirmSheet | null>(null);
const visible = ref(false);
const loading = ref(false);
const planContext = ref<{ conversationId: string; tool: AiPendingTool } | null>(null);
const ticket = ref<{ conversationId: string; toolCallId: string; status: string; message?: string } | null>(null);

async function handlePlan() {
  if (!form.content) {
    return ElMessage.warning('请输入申请内容');
  }
  loading.value = true;
  try {
    const payloadMessage = `[PLAN] ${form.content}`;
    const res = await chatApi({ conversationId: form.conversationId || undefined, message: payloadMessage });
    if (!res.pendingTool) {
      planContext.value = null;
      sheet.value = null;
      ElMessage.warning('当前回复未返回确认单，请检查描述是否正确');
      return;
    }
    planContext.value = { conversationId: res.conversationId, tool: res.pendingTool };
    sheet.value = createConfirmSheetFromTool(res.pendingTool, res.structured);
    form.conversationId = res.conversationId;
    visible.value = true;
  } finally {
    loading.value = false;
  }
}

async function handleExecute() {
  if (!planContext.value) return;
  if (sheet.value?.riskLevel === 'L3') {
    await ElMessageBox.prompt('请输入 YES 以确认执行高风险操作', '高风险确认', {
      inputPattern: /^YES$/,
      inputErrorMessage: '请输入 YES'
    });
  }
  const traceId = createTraceId('ai-plan');
  console.info('操作摘要', {
    traceId,
    action: 'aiPlanExecute',
    params: { conversationId: planContext.value.conversationId, toolCallId: planContext.value.tool.toolCallId }
  });
  const result = await confirmTool(planContext.value.conversationId, {
    toolCallId: planContext.value.tool.toolCallId,
    approved: true,
    comment: comment.value
  });
  ticket.value = {
    conversationId: planContext.value.conversationId,
    toolCallId: planContext.value.tool.toolCallId,
    status: result.success ? 'ACCEPTED' : 'PENDING',
    message: result.message
  };
  visible.value = false;
  ElMessage.success('执行完成，后端将继续处理');
}
</script>

<style scoped>
.ai-apply {
  max-width: 800px;
}
.mt {
  margin-top: 16px;
}
</style>
