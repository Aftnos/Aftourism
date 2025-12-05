<template>
  <div class="page-card ai-admin">
    <ElRow :gutter="16">
      <ElCol :span="12">
        <ElCard>
          <template #header>系统配置</template>
          <ElForm :model="settings" label-width="130px">
            <ElFormItem label="全局开关">
              <ElSwitch v-model="settings.enabled" active-text="启用" inactive-text="停用" />
            </ElFormItem>
            <ElFormItem label="默认对话模型">
              <ElSelect v-model="settings.defaultChatModel" placeholder="选择对话模型" filterable>
                <ElOption v-for="m in models" :key="m.name" :label="m.name" :value="m.name" />
              </ElSelect>
            </ElFormItem>
            <ElFormItem label="备用对话模型">
              <ElSelect v-model="settings.fallbackChatModel" placeholder="主模型不可用时的备份" filterable>
                <ElOption v-for="m in models" :key="m.name" :label="m.name" :value="m.name" />
              </ElSelect>
            </ElFormItem>
            <ElFormItem label="默认重排序模型">
              <ElSelect v-model="settings.defaultRerankModel" placeholder="可选" clearable filterable>
                <ElOption v-for="m in rerankModels" :key="m.name" :label="m.name" :value="m.name" />
              </ElSelect>
            </ElFormItem>
            <ElFormItem label="默认嵌入模型">
              <ElSelect v-model="settings.defaultEmbeddingModel" placeholder="可选" clearable filterable>
                <ElOption v-for="m in embeddingModels" :key="m.name" :label="m.name" :value="m.name" />
              </ElSelect>
            </ElFormItem>
            <ElFormItem label="场景备用模型">
              <div class="scenario-list">
                <div v-for="(value, key) in scenarioPairs" :key="key" class="scenario-row">
                  <ElInput v-model="scenarioPairs[key]" :placeholder="key" class="scenario-input" />
                  <ElButton text type="danger" @click="removeScenario(key)">删除</ElButton>
                </div>
                <ElButton text type="primary" @click="addScenario">新增场景</ElButton>
              </div>
            </ElFormItem>
            <ElFormItem>
              <ElButton type="primary" :loading="savingSettings" @click="saveSettings">保存配置</ElButton>
            </ElFormItem>
          </ElForm>
        </ElCard>
      </ElCol>
      <ElCol :span="12">
        <ElCard>
          <template #header>AI 服务状态</template>
          <ElAlert
            :type="serviceStatus?.aiEnabled ? 'success' : 'warning'"
            :closable="false"
            :title="serviceStatus?.aiEnabled ? 'AI 服务已启用' : 'AI 已关闭'"
          >
            当前使用模型：{{ serviceStatus?.activeChatModel || '-' }}，备用：{{ serviceStatus?.fallbackChatModel || '-' }}
          </ElAlert>
          <ElDivider />
          <ElTimeline>
            <ElTimelineItem
              v-for="item in serviceStatus?.providers || []"
              :key="item.provider"
              :type="item.status === 'UP' ? 'success' : item.status === 'DOWN' ? 'danger' : 'info'"
            >
              <p>{{ item.provider }} · {{ item.status || 'UNKNOWN' }}</p>
              <small>Endpoint: {{ item.endpoint || '-' }} · Latency: {{ item.latencyMs ?? '-' }}ms</small>
            </ElTimelineItem>
          </ElTimeline>
          <ElDivider />
          <div class="error-list" v-if="serviceStatus?.recentErrors?.length">
            <h4>最近异常</h4>
            <ul>
              <li v-for="err in serviceStatus?.recentErrors" :key="err.occurredAt">
                {{ formatTime(err.occurredAt) }} · {{ err.provider }} · {{ err.message }}
              </li>
            </ul>
          </div>
        </ElCard>
      </ElCol>
    </ElRow>

    <ElRow :gutter="16" class="mt">
      <ElCol :span="12">
        <ElCard>
          <template #header>
            <div class="card-header">
              <span>接口管理</span>
              <ElButton size="small" type="primary" @click="openProviderDialog()">新增服务商</ElButton>
            </div>
          </template>
          <ElTable :data="providers" border height="320px">
            <ElTableColumn prop="name" label="名称" width="160" />
            <ElTableColumn prop="code" label="标识" width="160" />
            <ElTableColumn prop="endpoint" label="Endpoint" />
            <ElTableColumn label="启用" width="120">
              <template #default="{ row }">
                <ElSwitch v-model="row.enabled" @change="onProviderToggle(row)" />
              </template>
            </ElTableColumn>
            <ElTableColumn label="操作" width="220">
              <template #default="{ row }">
                <ElButton text size="small" type="primary" @click="openProviderDialog(row)">编辑</ElButton>
                <ElButton text size="small" type="success" @click="testProvider(row)">测试连接</ElButton>
                <ElButton text size="small" type="danger" @click="removeProvider(row.code)">删除</ElButton>
              </template>
            </ElTableColumn>
          </ElTable>
        </ElCard>
      </ElCol>
      <ElCol :span="12">
        <ElCard>
          <template #header>
            <div class="card-header">
              <span>模型管理</span>
              <ElButton size="small" type="primary" @click="openModelDialog()">新增模型</ElButton>
            </div>
          </template>
          <ElTable :data="models" border height="320px">
            <ElTableColumn prop="name" label="模型" />
            <ElTableColumn prop="provider" label="服务商" />
            <ElTableColumn prop="type" label="类型" width="120" />
            <ElTableColumn label="启用" width="120">
              <template #default="{ row }">
                <ElSwitch v-model="row.enabled" @change="onModelToggle(row)" />
              </template>
            </ElTableColumn>
            <ElTableColumn label="操作" width="180">
              <template #default="{ row }">
                <ElButton text size="small" type="primary" @click="openModelDialog(row)">编辑</ElButton>
                <ElButton text size="small" type="danger" @click="removeModel(row.name)">删除</ElButton>
              </template>
            </ElTableColumn>
          </ElTable>
        </ElCard>
      </ElCol>
    </ElRow>

    <ElRow :gutter="16" class="mt">
      <ElCol :span="12">
        <ElCard>
          <template #header>
            <div class="card-header">
              <span>使用统计</span>
              <div class="filters">
                <ElSelect v-model="statFilter.model" placeholder="模型" clearable size="small" @change="loadStats">
                  <ElOption v-for="m in models" :key="m.name" :label="m.name" :value="m.name" />
                </ElSelect>
                <ElSelect v-model="statFilter.provider" placeholder="服务商" clearable size="small" @change="loadStats">
                  <ElOption v-for="p in providers" :key="p.code" :label="p.name" :value="p.code" />
                </ElSelect>
              </div>
            </div>
          </template>
          <ElTable :data="stats" border height="260px">
            <ElTableColumn prop="model" label="模型" />
            <ElTableColumn prop="provider" label="服务商" />
            <ElTableColumn prop="total" label="调用次数" width="100" />
            <ElTableColumn prop="success" label="成功" width="80" />
            <ElTableColumn prop="failure" label="失败" width="80" />
            <ElTableColumn label="平均耗时(ms)" width="140">
              <template #default="{ row }">{{ row.avgLatencyMs.toFixed(1) }}</template>
            </ElTableColumn>
            <ElTableColumn label="错误率" width="120">
              <template #default="{ row }">{{ (row.errorRate * 100).toFixed(2) }}%</template>
            </ElTableColumn>
          </ElTable>
        </ElCard>
      </ElCol>
      <ElCol :span="12">
        <ElCard>
          <template #header>异常告警</template>
          <ElEmpty v-if="!alerts.length" description="暂无告警" />
          <ElTimeline v-else>
            <ElTimelineItem v-for="alert in alerts" :key="alert.occurredAt" type="danger">
              <p>{{ alert.model }} · {{ alert.metric }}: {{ alert.value.toFixed(2) }} (阈值 {{ alert.threshold }})</p>
              <small>{{ formatTime(alert.occurredAt) }} · {{ alert.message || '自动触发' }}</small>
            </ElTimelineItem>
          </ElTimeline>
        </ElCard>
        <ElCard class="mt">
          <template #header>授权日志</template>
          <ElTable :data="auditLogs" border height="220px">
            <ElTableColumn prop="occurredAt" label="时间" width="180">
              <template #default="{ row }">{{ formatTime(row.occurredAt) }}</template>
            </ElTableColumn>
            <ElTableColumn prop="userId" label="用户" width="100" />
            <ElTableColumn prop="operation" label="操作" />
            <ElTableColumn prop="approved" label="结果" width="100">
              <template #default="{ row }">
                <ElTag :type="row.approved ? 'success' : 'danger'">{{ row.approved ? '同意' : '拒绝' }}</ElTag>
              </template>
            </ElTableColumn>
          </ElTable>
        </ElCard>
      </ElCol>
    </ElRow>

    <ElDialog v-model="providerDialogVisible" title="服务商配置" width="480px">
      <ElForm :model="providerForm" label-width="120px">
        <ElFormItem label="名称">
          <ElInput v-model="providerForm.name" />
        </ElFormItem>
        <ElFormItem label="标识">
          <ElInput v-model="providerForm.code" />
        </ElFormItem>
        <ElFormItem label="Endpoint">
          <ElInput v-model="providerForm.endpoint" />
        </ElFormItem>
        <ElFormItem label="API Key">
          <ElInput v-model="providerForm.apiKey" type="password" />
        </ElFormItem>
        <ElFormItem label="超时(ms)">
          <ElInput v-model.number="providerForm.timeoutMs" type="number" />
        </ElFormItem>
        <ElFormItem label="启用">
          <ElSwitch v-model="providerForm.enabled" />
        </ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton @click="providerDialogVisible = false">取消</ElButton>
        <ElButton type="primary" :loading="savingProvider" @click="saveProvider">保存</ElButton>
      </template>
    </ElDialog>

    <ElDialog v-model="modelDialogVisible" title="模型配置" width="480px">
      <ElForm :model="modelForm" label-width="120px">
        <ElFormItem label="名称">
          <ElInput v-model="modelForm.name" :disabled="!!modelForm.name && editingModel" />
        </ElFormItem>
        <ElFormItem label="服务商">
          <ElSelect v-model="modelForm.provider" placeholder="选择服务商" filterable>
            <ElOption v-for="p in providers" :key="p.code" :label="p.name" :value="p.code" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="类型">
          <ElSelect v-model="modelForm.type">
            <ElOption label="对话" value="CHAT" />
            <ElOption label="重排序" value="RERANK" />
            <ElOption label="嵌入" value="EMBEDDING" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="描述">
          <ElInput v-model="modelForm.description" type="textarea" rows="2" />
        </ElFormItem>
        <ElFormItem label="启用">
          <ElSwitch v-model="modelForm.enabled" />
        </ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton @click="modelDialogVisible = false">取消</ElButton>
        <ElButton type="primary" :loading="savingModel" @click="saveModel">保存</ElButton>
      </template>
    </ElDialog>
  </div>
</template>

<script setup lang="ts">
import { computed, reactive, ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import {
  getAiSettings,
  updateAiSettings,
  getAiServiceStatus,
  getAiStats,
  getAiAlerts,
  getAiProviders,
  saveAiProvider,
  deleteAiProvider,
  testAiProvider,
  getAiModels,
  saveAiModel,
  deleteAiModel,
  toggleAiModel,
  getAuditLogs,
  type AiSettingsDTO,
  type AiServiceStatusDTO,
  type AiModelStatDTO,
  type AiAlertEventDTO,
  type AiAuthorizationLogDTO
} from '@/api/ai';

const settings = reactive<AiSettingsDTO>({
  enabled: true,
  providers: [],
  models: [],
  defaultChatModel: '',
  fallbackChatModel: '',
  defaultEmbeddingModel: '',
  defaultRerankModel: '',
  scenarioFallbacks: {}
});
const serviceStatus = ref<AiServiceStatusDTO | null>(null);
const stats = ref<AiModelStatDTO[]>([]);
const alerts = ref<AiAlertEventDTO[]>([]);
const auditLogs = ref<AiAuthorizationLogDTO[]>([]);
const providers = ref<AiSettingsDTO['providers']>([]);
const models = ref<AiSettingsDTO['models']>([]);
const savingSettings = ref(false);
const savingProvider = ref(false);
const savingModel = ref(false);
const statFilter = reactive<{ model?: string; provider?: string }>({});
const scenarioPairs = reactive<Record<string, string>>({});

const providerDialogVisible = ref(false);
const providerForm = reactive({ name: '', code: '', endpoint: '', apiKey: '', timeoutMs: 15000, enabled: true });
const modelDialogVisible = ref(false);
const modelForm = reactive({ name: '', provider: '', type: 'CHAT', description: '', enabled: true });
const editingModel = ref(false);

const rerankModels = computed(() => models.value.filter((m) => m.type === 'RERANK'));
const embeddingModels = computed(() => models.value.filter((m) => m.type === 'EMBEDDING'));

function formatTime(value?: string) {
  if (!value) return '-';
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return value;
  return `${date.toLocaleDateString()} ${date.toLocaleTimeString()}`;
}

async function loadSettings() {
  const data = await getAiSettings();
  Object.assign(settings, data);
  Object.keys(scenarioPairs).forEach((key) => delete scenarioPairs[key]);
  Object.entries(data.scenarioFallbacks || {}).forEach(([key, value]) => {
    scenarioPairs[key] = value;
  });
}

async function saveSettings() {
  savingSettings.value = true;
  try {
    settings.scenarioFallbacks = { ...scenarioPairs };
    settings.providers = providers.value as any;
    settings.models = models.value as any;
    await updateAiSettings(settings);
    ElMessage.success('配置已保存');
  } finally {
    savingSettings.value = false;
  }
}

async function loadStatus() {
  serviceStatus.value = await getAiServiceStatus();
}

async function loadStats() {
  stats.value = await getAiStats({ model: statFilter.model, provider: statFilter.provider });
}

async function loadAlerts() {
  alerts.value = await getAiAlerts();
}

async function loadProviders() {
  providers.value = await getAiProviders();
}

async function loadModels() {
  models.value = await getAiModels();
}

async function loadAuditLogs() {
  auditLogs.value = await getAuditLogs();
}

async function loadAll() {
  await Promise.all([loadSettings(), loadStatus(), loadProviders(), loadModels(), loadStats(), loadAlerts(), loadAuditLogs()]);
}

function openProviderDialog(row?: (typeof providers.value)[number]) {
  if (row) {
    Object.assign(providerForm, row);
  } else {
    Object.assign(providerForm, { name: '', code: '', endpoint: '', apiKey: '', timeoutMs: 15000, enabled: true });
  }
  providerDialogVisible.value = true;
}

async function saveProvider() {
  if (!providerForm.name) {
    return ElMessage.warning('请输入服务商名称');
  }
  savingProvider.value = true;
  try {
    await saveAiProvider(providerForm as any);
    providerDialogVisible.value = false;
    await loadProviders();
    settings.providers = providers.value as any;
    ElMessage.success('服务商已保存');
  } finally {
    savingProvider.value = false;
  }
}

async function removeProvider(code?: string) {
  if (!code) return;
  await ElMessageBox.confirm('确定删除该服务商吗？', '提示', { type: 'warning' });
  await deleteAiProvider(code);
  await loadProviders();
  settings.providers = providers.value as any;
}

async function testProvider(row: (typeof providers.value)[number]) {
  const res = await testAiProvider(row.code || row.name, row);
  ElMessage[res.success ? 'success' : 'error'](res.message);
  await loadStatus();
}

function onProviderToggle(row: (typeof providers.value)[number]) {
  saveAiProvider(row);
}

function openModelDialog(row?: (typeof models.value)[number]) {
  if (row) {
    Object.assign(modelForm, row);
    editingModel.value = true;
  } else {
    Object.assign(modelForm, { name: '', provider: '', type: 'CHAT', description: '', enabled: true });
    editingModel.value = false;
  }
  modelDialogVisible.value = true;
}

async function saveModel() {
  if (!modelForm.name) {
    return ElMessage.warning('请输入模型名称');
  }
  savingModel.value = true;
  try {
    await saveAiModel(modelForm as any);
    modelDialogVisible.value = false;
    await loadModels();
    settings.models = models.value as any;
  } finally {
    savingModel.value = false;
  }
}

async function removeModel(name?: string) {
  if (!name) return;
  await ElMessageBox.confirm('确定删除该模型吗？', '提示', { type: 'warning' });
  await deleteAiModel(name);
  await loadModels();
  settings.models = models.value as any;
}

function onModelToggle(row: (typeof models.value)[number]) {
  toggleAiModel(row.name, !!row.enabled);
}

function addScenario() {
  const key = `scenario-${Object.keys(scenarioPairs).length + 1}`;
  scenarioPairs[key] = '';
}

function removeScenario(key: string) {
  delete scenarioPairs[key];
}

onMounted(loadAll);
</script>

<style scoped>
.ai-admin .mt {
  margin-top: 16px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.filters {
  display: flex;
  gap: 8px;
}
.scenario-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.scenario-row {
  display: flex;
  gap: 8px;
}
.scenario-input {
  flex: 1;
}
.error-list ul {
  padding-left: 16px;
}
</style>
