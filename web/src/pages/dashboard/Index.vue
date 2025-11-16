<template>
  <div class="page-card">
    <ElRow :gutter="16">
      <ElCol :span="6" v-for="item in cards" :key="item.title">
        <ElCard>
          <p>{{ item.title }}</p>
          <h2>{{ item.value }}</h2>
        </ElCard>
      </ElCol>
    </ElRow>
    <ElDivider />
    <ElTimeline>
      <ElTimelineItem v-for="log in app.logs" :key="log" type="primary">{{ log }}</ElTimelineItem>
    </ElTimeline>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useAppStore } from '@/store/app';

const cards = ref([
  { title: '今日访问', value: '1,204' },
  { title: '活动报名', value: '328' },
  { title: 'AI 调用', value: '89' },
  { title: '异常拦截', value: '2' }
]);

const app = useAppStore();
</script>
