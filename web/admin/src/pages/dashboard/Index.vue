<template>
  <div class="page-card">
    <ElRow :gutter="16">
      <ElCol :span="6">
        <ElCard>
          <p>新闻总数</p>
          <h2>{{ metrics.news }}</h2>
        </ElCard>
      </ElCol>
      <ElCol :span="6">
        <ElCard>
          <p>通知总数</p>
          <h2>{{ metrics.notice }}</h2>
        </ElCard>
      </ElCol>
      <ElCol :span="6">
        <ElCard>
          <p>景区总数</p>
          <h2>{{ metrics.scenic }}</h2>
        </ElCard>
      </ElCol>
      <ElCol :span="6">
        <ElCard>
          <p>场馆总数</p>
          <h2>{{ metrics.venue }}</h2>
        </ElCard>
      </ElCol>
    </ElRow>
    <ElDivider />
    <ElCard>
      <template #header>关键指标趋势</template>
      <div class="chart-wrap">
        <svg :width="chart.width" :height="chart.height">
          <path :d="chart.newsPath" stroke="#409EFF" fill="none" />
          <path :d="chart.noticePath" stroke="#67C23A" fill="none" />
          <path :d="chart.scenicPath" stroke="#E6A23C" fill="none" />
          <path :d="chart.venuePath" stroke="#F56C6C" fill="none" />
        </svg>
      </div>
    </ElCard>
    <ElDivider />
    <ElTimeline>
      <ElTimelineItem v-for="log in app.logs" :key="log" type="primary">{{ log }}</ElTimelineItem>
    </ElTimeline>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { useAppStore } from '@/store/app';
import request from '@/api/request';

const metrics = reactive({ news: 0, notice: 0, scenic: 0, venue: 0 });
const series = reactive<{ news: number[]; notice: number[]; scenic: number[]; venue: number[] }>({ news: [], notice: [], scenic: [], venue: [] });
const chart = reactive<{ width: number; height: number; newsPath: string; noticePath: string; scenicPath: string; venuePath: string }>({ width: 720, height: 180, newsPath: '', noticePath: '', scenicPath: '', venuePath: '' });

function buildPath(arr: number[]) {
  const w = chart.width;
  const h = chart.height;
  const n = arr.length;
  if (n === 0) return '';
  const max = Math.max(...arr, 1);
  const stepX = w / 12;
  let d = '';
  for (let i = 0; i < n; i++) {
    const x = i * stepX;
    const y = h - (arr[i] / max) * h;
    d += i === 0 ? `M${x},${y}` : ` L${x},${y}`;
  }
  return d;
}

async function fetchCount(url: string) {
  const res = await request.get<any>(url, { params: { pageNum: 1, pageSize: 1 } });
  const payload = (res as any)?.data ? (res as any).data : res;
  return Number(payload?.total || 0);
}

async function refreshMetrics() {
  try {
    metrics.news = await fetchCount('/admin/news/page');
    metrics.notice = await fetchCount('/admin/notice/page');
    metrics.scenic = await fetchCount('/admin/scenic/page');
    metrics.venue = await fetchCount('/admin/venue/page');
    series.news.push(metrics.news);
    series.notice.push(metrics.notice);
    series.scenic.push(metrics.scenic);
    series.venue.push(metrics.venue);
    if (series.news.length > 12) {
      series.news.shift();
      series.notice.shift();
      series.scenic.shift();
      series.venue.shift();
    }
    chart.newsPath = buildPath(series.news);
    chart.noticePath = buildPath(series.notice);
    chart.scenicPath = buildPath(series.scenic);
    chart.venuePath = buildPath(series.venue);
  } catch {
    // ignore
  }
}

onMounted(() => {
  refreshMetrics();
  setInterval(refreshMetrics, 60000);
});

const app = useAppStore();
</script>

<style scoped>
.chart-wrap {
  width: 100%;
  overflow: hidden;
}
</style>
