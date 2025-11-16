<template>
  <div class="page-card">
    <ElRow :gutter="16">
      <ElCol :span="8">
        <ElCard>
          <p>访问统计</p>
          <h2>{{ data.visits }}</h2>
        </ElCard>
      </ElCol>
      <ElCol :span="8">
        <ElCard>
          <p>Redis</p>
          <h2>{{ data.redis }}</h2>
        </ElCard>
      </ElCol>
      <ElCol :span="8">
        <ElCard>
          <p>系统负载</p>
          <h2>{{ data.cpu }}</h2>
        </ElCard>
      </ElCol>
    </ElRow>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { fetchMonitorData } from '@/api/business';

const data = ref({ visits: '-', redis: '-', cpu: '-' });

onMounted(async () => {
  const res = await fetchMonitorData();
  data.value = res;
});
</script>
