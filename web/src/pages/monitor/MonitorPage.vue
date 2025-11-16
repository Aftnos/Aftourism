<template>
  <div class="page-card monitor">
    <ElForm :model="form" label-width="110px" class="metric-form">
      <ElFormItem label="主机标识">
        <ElInput v-model="form.host" placeholder="如：admin-node-01" />
      </ElFormItem>
      <ElFormItem label="CPU 使用 %">
        <ElInputNumber v-model="form.cpuUsage" :min="0" :max="100" />
      </ElFormItem>
      <ElFormItem label="内存使用 %">
        <ElInputNumber v-model="form.memoryUsage" :min="0" :max="100" />
      </ElFormItem>
      <ElFormItem label="磁盘使用 %">
        <ElInputNumber v-model="form.diskUsage" :min="0" :max="100" />
      </ElFormItem>
      <ElFormItem label="平均负载">
        <ElInput v-model="form.loadAvg" placeholder="可选，例如 0.34,0.40,0.60" />
      </ElFormItem>
      <ElFormItem label="备注">
        <ElInput v-model="form.remark" type="textarea" rows="2" placeholder="可记录来源等信息" />
      </ElFormItem>
      <ElFormItem>
        <ElButton type="primary" :loading="submitting" @click="handleSubmit">上报指标</ElButton>
      </ElFormItem>
    </ElForm>

    <ElAlert
      v-if="lastPush"
      class="mt"
      type="success"
      :closable="false"
      :title="`最近一次上报：${lastPush.host}`"
      :description="`CPU ${lastPush.cpuUsage}%｜MEM ${lastPush.memoryUsage}%｜Disk ${lastPush.diskUsage}%`"
    />
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { pushSystemMetric } from '@/api/business';
import { createTraceId } from '@/utils/trace';

const form = reactive({ host: '', cpuUsage: 0, memoryUsage: 0, diskUsage: 0, loadAvg: '', remark: '' });
const submitting = ref(false);
const lastPush = ref<{ host: string; cpuUsage: number; memoryUsage: number; diskUsage: number } | null>(null);

async function handleSubmit() {
  if (!form.host) {
    return ElMessage.warning('请填写主机标识');
  }
  submitting.value = true;
  try {
    const traceId = createTraceId('monitor');
    console.info('操作摘要', { traceId, action: 'pushMetric', params: { ...form } });
    await pushSystemMetric({ ...form });
    lastPush.value = { host: form.host, cpuUsage: form.cpuUsage, memoryUsage: form.memoryUsage, diskUsage: form.diskUsage };
    ElMessage.success('指标已上报');
  } finally {
    submitting.value = false;
  }
}
</script>

<style scoped>
.monitor {
  max-width: 640px;
}
.metric-form {
  background: #fff;
  padding: 16px;
  border-radius: 8px;
}
.mt {
  margin-top: 16px;
}
</style>
