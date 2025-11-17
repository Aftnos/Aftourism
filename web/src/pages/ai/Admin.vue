<template>
  <div class="page-card">
    <ElRow :gutter="16">
      <ElCol :span="12">
        <ElCard>
          <template #header>系统配置</template>
          <ElForm :model="settings" label-width="120px">
            <ElFormItem label="全局开关">
              <ElSwitch v-model="settings.enabled" />
            </ElFormItem>
            <ElFormItem label="默认模型">
              <ElSelect v-model="settings.defaultModel" placeholder="选择默认模型" filterable>
                <ElOption v-for="m in settings.models" :key="m.name" :label="m.name" :value="m.name" />
              </ElSelect>
            </ElFormItem>
            <ElFormItem label="备用模型">
              <ElSelect v-model="settings.backupModel" placeholder="选择备用模型" filterable>
                <ElOption v-for="m in settings.models" :key="m.name" :label="m.name" :value="m.name" />
              </ElSelect>
            </ElFormItem>
            <ElFormItem>
              <ElButton type="primary" :loading="saving" @click="save">保存配置</ElButton>
            </ElFormItem>
          </ElForm>
        </ElCard>
        <ElCard class="mt">
          <template #header>服务状态监控</template>
          <ElAlert :closable="false" type="info" title="后端暂未接入实时状态，待扩展" />
        </ElCard>
      </ElCol>

      <ElCol :span="12">
        <ElCard>
          <template #header>接口管理（多服务商接入）</template>
          <ElTable :data="settings.providers" border style="width:100%">
            <ElTableColumn prop="name" label="名称" width="160" />
            <ElTableColumn label="启用" width="100">
              <template #default="{ row }">
                <ElSwitch v-model="row.enabled" />
              </template>
            </ElTableColumn>
            <ElTableColumn prop="endpoint" label="Endpoint" />
            <ElTableColumn prop="timeoutMs" label="超时(ms)" width="120" />
            <ElTableColumn label="操作" width="160">
              <template #default="{ row }">
                <ElButton text type="primary" size="small" @click="test(row)">连接测试</ElButton>
              </template>
            </ElTableColumn>
          </ElTable>
        </ElCard>

        <ElCard class="mt">
          <template #header>模型管理</template>
          <ElTable :data="settings.models" border style="width:100%">
            <ElTableColumn prop="name" label="模型" />
            <ElTableColumn prop="provider" label="服务商" />
            <ElTableColumn label="启用" width="120">
              <template #default="{ row }">
                <ElSwitch v-model="row.enabled" />
              </template>
            </ElTableColumn>
          </ElTable>
        </ElCard>

        <ElCard class="mt">
          <template #header>使用统计</template>
          <div class="chart-wrap">
            <svg :width="chart.width" :height="chart.height">
              <path :d="chart.path" stroke="#409EFF" fill="none" />
            </svg>
          </div>
          <ElAlert :closable="false" type="info" title="异常调用报警待后端接入告警推送" class="mt" />
        </ElCard>
      </ElCol>
    </ElRow>
  </div>
</template>

<script setup lang="ts">
import { reactive, onMounted, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { getAiSettings, updateAiSettings, testAiProvider, getAiStats, type AiSettingsDTO } from '@/api/ai';

const saving = ref(false);
const settings = reactive<AiSettingsDTO>({ enabled: true, providers: [], models: [], defaultModel: '', backupModel: '' });

const chart = reactive<{ width: number; height: number; path: string }>({ width: 600, height: 160, path: '' });

function buildPath(arr: number[]) {
  const w = chart.width, h = chart.height;
  if (!arr.length) return '';
  const max = Math.max(...arr, 1);
  const stepX = w / Math.max(arr.length - 1, 1);
  let d = '';
  for (let i = 0; i < arr.length; i++) {
    const x = i * stepX;
    const y = h - (arr[i] / max) * h;
    d += i === 0 ? `M${x},${y}` : ` L${x},${y}`;
  }
  return d;
}

async function load() {
  const res = await getAiSettings();
  Object.assign(settings, res as any);
  const stats = await getAiStats();
  const series = Object.values((stats as any)?.models || {}) as number[];
  chart.path = buildPath(series);
}

async function save() {
  saving.value = true;
  try {
    await updateAiSettings(settings);
    ElMessage.success('配置已保存');
  } finally {
    saving.value = false;
  }
}

async function test(row: any) {
  const res = await testAiProvider(row);
  ElMessage[res.success ? 'success' : 'error'](res.message);
}

onMounted(load);
</script>

<style scoped>
.mt { margin-top: 16px; }
.chart-wrap { width: 100%; overflow: hidden; }
</style>