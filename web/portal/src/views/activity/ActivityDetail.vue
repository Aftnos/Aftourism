<template>
  <div class="page-wrapper">
    <div class="content-card" v-if="detail">
      <el-row :gutter="16">
        <el-col :span="10">
          <img :src="detail.cover" alt="cover" class="cover" />
        </el-col>
        <el-col :span="14">
          <h2>{{ detail.name }}</h2>
          <p>时间：{{ detail.startTime }} - {{ detail.endTime }}</p>
          <p>类别：{{ detail.category }}</p>
          <p>场馆：{{ detail.venueName }} ｜ 地址：{{ detail.address }}</p>
          <p>主办单位：{{ detail.organizer }}</p>
          <p>联系电话：{{ detail.phone }}</p>
          <el-button type="warning" @click="toggleFavorite(detail.id)">
            {{ isFavorite(detail.id) ? '取消收藏' : '收藏' }}（{{ favoriteCount }}）
          </el-button>
        </el-col>
      </el-row>
      <el-divider />
      <p>{{ detail.summary }}</p>
      <el-divider />
      <div>
        <h4>留言互动（登录后可留言）</h4>
        <el-empty v-if="messages.length === 0" description="暂无留言" />
        <el-timeline v-else>
          <el-timeline-item v-for="msg in messages" :key="msg.time" :timestamp="msg.time">
            <p><strong>{{ msg.user }}</strong>：{{ msg.content }}</p>
          </el-timeline-item>
        </el-timeline>
        <el-form v-if="userStore.isLogin" :inline="true" class="comment-form" @submit.prevent>
          <el-form-item label="留言">
            <el-input v-model="newMessage" placeholder="分享你的想法" style="width: 360px" />
          </el-form-item>
          <el-button type="primary" @click="submitMessage">提交</el-button>
        </el-form>
        <el-alert v-else title="登录后可收藏和留言" type="info" show-icon />
      </div>
    </div>
    <el-empty v-else description="未找到活动" />
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { useRoute } from 'vue-router';
import { activities } from '@/data/mockData';
import { useUserStore } from '@/store/user';

// 中文注释：活动详情展示、收藏与留言
const route = useRoute();
const userStore = useUserStore();
const detail = computed(() => activities.find((item) => item.id === Number(route.params.id)));
const favoriteCount = computed(() => (detail.value ? userStore.favorites.activity.length : 0));
const isFavorite = (id: number) => userStore.favorites.activity.includes(id);
const toggleFavorite = (id: number) => userStore.toggleFavorite('activity', id);

const messages = ref<{ user: string; content: string; time: string }[]>([]);
const newMessage = ref('');

const submitMessage = () => {
  if (!newMessage.value) return;
  messages.value.unshift({
    user: userStore.profile.name,
    content: newMessage.value,
    time: new Date().toLocaleString()
  });
  newMessage.value = '';
};
</script>

<style scoped>
.cover {
  width: 100%;
  border-radius: 8px;
  object-fit: cover;
}

.comment-form {
  margin-top: 12px;
}
</style>
