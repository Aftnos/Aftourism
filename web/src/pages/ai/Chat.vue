<template>
  <div class="page-card ai-chat">
    <ElForm :model="form" label-width="100px">
      <ElFormItem label="输入问题">
        <ElInput v-model="form.message" type="textarea" rows="3" placeholder="仅作为后台转发到服务器的请求" />
      </ElFormItem>
      <ElFormItem label="模式">
        <ElRadioGroup v-model="form.mode">
          <ElRadioButton label="DIRECT">直接执行</ElRadioButton>
          <ElRadioButton label="PLAN">PLAN（有确认单）</ElRadioButton>
        </ElRadioGroup>
      </ElFormItem>
      <ElFormItem>
        <ElCheckbox v-model="form.ragEnabled">启用检索增强（RAG）</ElCheckbox>
        <ElSwitch v-model="useStream" inline-prompt active-text="流式" inactive-text="普通" class="stream-switch" />
      </ElFormItem>
      <ElFormItem>
        <ElButton type="primary" @click="handleSend">发送</ElButton>
      </ElFormItem>
    </ElForm>

    <ElTabs>
      <ElTabPane label="普通响应" v-if="!useStream">
        <ElTimeline>
          <ElTimelineItem v-for="item in replies" :key="item.id" type="primary">
            <p>{{ item.answer }}</p>
            <ElCollapse v-if="item.citations?.length">
              <ElCollapseItem title="引用来源">
                <p v-for="c in item.citations" :key="c.url">
                  <a :href="c.url" target="_blank">{{ c.title }}</a>
                </p>
              </ElCollapseItem>
            </ElCollapse>
          </ElTimelineItem>
        </ElTimeline>
      </ElTabPane>
      <ElTabPane label="流式响应" v-else>
        <SseViewer endpoint="/ai/chat/stream" :query="{ msg: form.message, mode: form.mode }" @closed="handleStreamClosed" />
      </ElTabPane>
    </ElTabs>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { chatApi } from '@/api/ai';
import SseViewer from '@/components/sse/SseViewer.vue';

const form = reactive({ message: '', ragEnabled: false, mode: 'DIRECT' as 'DIRECT' | 'PLAN' });
const replies = ref<any[]>([]);
const useStream = ref(false);

async function handleSend() {
  if (!form.message) {
    return ElMessage.warning('请先输入问题');
  }
  if (useStream.value) {
    ElMessage.info('已切换为流式输出，请在下方查看');
    return;
  }
  const res = await chatApi(form);
  replies.value.push({ ...res, id: Date.now() });
}

function handleStreamClosed() {
  ElMessage.info('流式响应已结束');
}
</script>

<style scoped>
.ai-chat {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.stream-switch {
  margin-left: 16px;
}
</style>
