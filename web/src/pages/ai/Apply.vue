<template>
  <div class="page-card">
    <ElForm :model="form" label-width="100px">
      <ElFormItem label="申请描述">
        <ElInput v-model="form.content" type="textarea" rows="5" placeholder="请详细描述要执行的操作" />
      </ElFormItem>
      <ElFormItem label="模式">
        <ElTag type="warning">PLAN</ElTag>
      </ElFormItem>
      <ElFormItem>
        <ElButton type="primary" :loading="loading" @click="handlePlan">生成确认单</ElButton>
      </ElFormItem>
    </ElForm>

    <ElCard v-if="ticket">
      <p>受理单号：{{ ticket.ticketNo }}</p>
      <p>状态：{{ ticket.status }}</p>
    </ElCard>

    <ConfirmDialog v-model="visible" :sheet="sheet" @confirm="handleExecute" />
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { ElMessageBox, ElMessage } from 'element-plus';
import { applyExecute, applyPlan, type ConfirmSheet } from '@/api/ai';
import ConfirmDialog from '@/components/confirm/ConfirmDialog.vue';
import { createTraceId } from '@/utils/trace';

const form = reactive({ content: '', mode: 'PLAN' as const });
const sheet = ref<ConfirmSheet | null>(null);
const visible = ref(false);
const loading = ref(false);
const ticket = ref<{ ticketNo: string; status: string } | null>(null);

async function handlePlan() {
  if (!form.content) {
    return ElMessageBox.alert('请输入申请内容', '提示');
  }
  loading.value = true;
  const res = await applyPlan(form);
  sheet.value = res.confirmSheet;
  visible.value = true;
  loading.value = false;
}

async function handleExecute() {
  if (!sheet.value) return;
  if (sheet.value.riskLevel === 'L3') {
    await ElMessageBox.prompt('请输入 YES 以继续', '高风险确认', {
      inputPattern: /^YES$/,
      inputErrorMessage: '请输入 YES'
    });
  }
  const traceId = createTraceId('ai');
  console.info('操作摘要', { traceId, action: sheet.value.action, params: sheet.value.params });
  const res = await applyExecute({ content: form.content });
  ticket.value = res;
  visible.value = false;
  ElMessage.success('执行完成');
}
</script>
