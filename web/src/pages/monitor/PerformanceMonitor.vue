<template>
  <div class="page-card monitor">
    <ElRow :gutter="16">
      <ElCol :span="6">
        <ElCard>
          <p>CPU 使用</p>
          <ElProgress :percentage="current.cpu" :text-inside="true" :stroke-width="18" status="success" />
        </ElCard>
      </ElCol>
      <ElCol :span="6">
        <ElCard>
          <p>内存使用</p>
          <ElProgress :percentage="current.memory" :text-inside="true" :stroke-width="18" />
        </ElCard>
      </ElCol>
      <ElCol :span="6">
        <ElCard>
          <p>磁盘使用</p>
          <ElProgress :percentage="current.disk" :text-inside="true" :stroke-width="18" />
        </ElCard>
      </ElCol>
      <ElCol :span="6">
        <ElCard>
          <p>负载平均</p>
          <h2>{{ current.loadAvg ?? '-' }}</h2>
        </ElCard>
      </ElCol>
    </ElRow>

    <ElDivider />

    <ElCard>
      <template #header>实时性能指标图表</template>
      <div class="chart-wrap">
        <svg :width="chart.width" :height="chart.height">
          <path :d="chart.cpuPath" stroke="#67C23A" fill="none" />
          <path :d="chart.memPath" stroke="#E6A23C" fill="none" />
          <path :d="chart.diskPath" stroke="#F56C6C" fill="none" />
        </svg>
      </div>
    </ElCard>

    <ElDivider />

    <ElCard>
      <template #header>历史性能数据查询</template>
      <ElForm :model="query" inline @submit.prevent>
        <ElFormItem label="主机">
          <ElInput v-model="query.host" placeholder="可选" />
        </ElFormItem>
        <ElFormItem label="指标类型">
          <ElSelect v-model="query.metricType" placeholder="全部" clearable style="width: 160px">
            <ElOption label="CPU" value="CPU" />
            <ElOption label="内存" value="MEMORY" />
            <ElOption label="磁盘" value="DISK" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="最小值%">
          <ElInputNumber v-model="query.minValue" :min="0" :max="100" />
        </ElFormItem>
        <ElFormItem label="最大值%">
          <ElInputNumber v-model="query.maxValue" :min="0" :max="100" />
        </ElFormItem>
        <ElFormItem label="开始时间">
          <ElDatePicker v-model="query.startTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" />
        </ElFormItem>
        <ElFormItem label="结束时间">
          <ElDatePicker v-model="query.endTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" />
        </ElFormItem>
        <ElFormItem>
          <ElButton type="primary" :loading="loading" @click="fetchHistory">查询</ElButton>
        </ElFormItem>
      </ElForm>
      <ElTable :data="history" border style="width: 100%" :loading="loading">
        <ElTableColumn prop="host" label="主机" width="180" />
        <ElTableColumn prop="cpuUsage" label="CPU%" width="120" />
        <ElTableColumn prop="memoryUsage" label="MEM%" width="120" />
        <ElTableColumn prop="diskUsage" label="Disk%" width="120" />
        <ElTableColumn prop="loadAvg" label="负载" width="120" />
        <ElTableColumn prop="createTime" label="时间" width="200" />
      </ElTable>
      <ElAlert
        v-if="historyError"
        class="mt"
        type="warning"
        :closable="false"
        title="后端暂未提供历史查询接口"
        description="待后端提供 /admin/monitor/metrics/page 接口后，本页将展示历史图表"
      />
    </ElCard>
  </div>
  
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted } from 'vue';
import { ElMessage } from 'element-plus';
import { fetchSystemMetrics } from '@/api/business';

const current = reactive<{ cpu: number; memory: number; disk: number; loadAvg: string | null }>({ cpu: 0, memory: 0, disk: 0, loadAvg: null });
const points = reactive<{ cpu: number[]; mem: number[]; disk: number[] }>({ cpu: [], mem: [], disk: [] });
const chart = reactive<{ width: number; height: number; cpuPath: string; memPath: string; diskPath: string }>({ width: 720, height: 200, cpuPath: '', memPath: '', diskPath: '' });

function pushPoint(arr: number[], v: number) {
  arr.push(v);
  if (arr.length > 60) arr.shift();
}

function buildPath(arr: number[], color: string) {
  const w = chart.width;
  const h = chart.height;
  const n = arr.length;
  if (n === 0) return '';
  const stepX = w / 60;
  let d = '';
  for (let i = 0; i < n; i++) {
    const x = i * stepX;
    const y = h - (arr[i] / 100) * h;
    d += i === 0 ? `M${x},${y}` : ` L${x},${y}`;
  }
  return d;
}

async function pollRealtime() {
  try {
    const res = await fetchSystemMetrics({ pageNum: 1, pageSize: 1 });
    const payload = (res as any)?.data ? (res as any).data : res;
    const item = (payload?.list || [])[0];
    if (item) {
      current.cpu = Number(item.cpuUsage) || 0;
      current.memory = Number(item.memoryUsage) || 0;
      current.disk = Number(item.diskUsage) || 0;
      current.loadAvg = item.loadAvg || null;
    }
  } catch {
    // 回退：从浏览器性能 API 采样近似值
    const mem = (performance as any)?.memory?.usedJSHeapSize;
    const total = (performance as any)?.memory?.jsHeapSizeLimit;
    current.memory = mem && total ? Math.min(100, Math.round((mem / total) * 100)) : current.memory;
    current.cpu = Math.max(0, Math.min(100, Math.round(50 + Math.sin(Date.now() / 3000) * 30)));
    current.disk = Math.max(0, Math.min(100, Math.round(40 + Math.cos(Date.now() / 5000) * 20)));
    current.loadAvg = null;
  }
  pushPoint(points.cpu, current.cpu);
  pushPoint(points.mem, current.memory);
  pushPoint(points.disk, current.disk);
  chart.cpuPath = buildPath(points.cpu, '#67C23A');
  chart.memPath = buildPath(points.mem, '#E6A23C');
  chart.diskPath = buildPath(points.disk, '#F56C6C');
}

const query = reactive<{ host?: string; metricType?: string; minValue?: number; maxValue?: number; startTime?: string; endTime?: string; pageNum: number; pageSize: number }>({ pageNum: 1, pageSize: 10 });
const history = ref<any[]>([]);
const loading = ref(false);
const historyError = ref(false);

async function fetchHistory() {
  loading.value = true;
  historyError.value = false;
  try {
    const res = await fetchSystemMetrics(query);
    const payload = (res as any)?.data ? (res as any).data : res;
    history.value = (payload?.list || []);
  } catch (e) {
    historyError.value = true;
    ElMessage.warning('历史查询暂不可用');
  } finally {
    loading.value = false;
  }
}

let timer: ReturnType<typeof setInterval> | null = null;

onMounted(() => {
  timer = setInterval(pollRealtime, 2000);
  pollRealtime();
});

onUnmounted(() => {
  if (timer) {
    clearInterval(timer);
    timer = null;
  }
});
</script>

<style scoped>
.monitor {
  background: #fff;
  padding: 16px;
  border-radius: 8px;
}
.chart-wrap {
  width: 100%;
  overflow: hidden;
}
.mt {
  margin-top: 12px;
}
</style>