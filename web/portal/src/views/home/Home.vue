<template>
  <div class="home-container">
    <!-- 全宽 Banner -->
    <div class="full-banner">
      <el-carousel height="600px" indicator-position="none" :interval="5000" arrow="always">
        <el-carousel-item v-for="item in scenicCarousel.slice(0, 3)" :key="item.id">
          <div class="banner-item" :style="{ backgroundImage: `url(${item.imageUrl})` }">
            <div class="banner-overlay">
              <div class="banner-content">
                <h1>古都之韵，文化之城</h1>
                <p>探索千年历史，感受现代魅力</p>
              </div>
            </div>
          </div>
        </el-carousel-item>
        <el-carousel-item v-if="scenicCarousel.length === 0">
          <div class="banner-item empty-banner">
            <div class="empty-content">
              <el-icon :size="64"><Picture /></el-icon>
              <p>暂无推荐内容</p>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 平台简介 -->
    <div class="section-block intro-section">
      <div class="page-wrapper">
        <div class="section-header">
          <h2>文旅简介</h2>
          <div class="divider"></div>
        </div>
        <div class="intro-content">
          <div class="intro-text">
            <p>
              本平台致力于打造一站式智慧文旅服务体验。作为连接游客与城市的桥梁，我们汇聚了本地最优质的 5A 级景区资源、
              最新的文化活动资讯以及便捷的场馆服务。
            </p>
            <p>
              无论是探寻历史古迹的厚重，还是享受现代都市的繁华，这里都能为您提供详尽的指引。
              通过智能推荐与多端适配技术，让您的每一次出行都轻松惬意，随时随地畅享文旅之美。
            </p>
            <el-button type="primary" size="large" class="intro-btn" @click="goScenic">了解更多</el-button>
          </div>
          <div class="intro-media">
            <div class="media-placeholder">
              <el-icon :size="48"><VideoPlay /></el-icon>
              <span>宣传视频虚位以待</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 风景介绍 (Grid) -->
    <div class="section-block scenery-section">
      <div class="page-wrapper">
        <div class="section-header">
          <h2>风景介绍</h2>
          <div class="divider"></div>
        </div>
        <el-row :gutter="24">
          <el-col :xs="24" :sm="12" :md="8" v-for="item in scenicCarousel" :key="item.id">
            <div class="scenic-card" @click="goDetail(item.id)">
              <div class="card-image">
                <el-image :src="item.imageUrl" fit="cover" loading="lazy" />
              </div>
              <div class="card-info">
                <h3>{{ item.name }}</h3>
                <p class="desc">{{ item.description || '暂无简介' }}</p>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 资讯与活动 (双栏) -->
    <div class="section-block info-section">
      <div class="page-wrapper">
        <el-row :gutter="40">
          <el-col :xs="24" :md="12">
            <div class="info-column">
              <div class="column-header">
                <h3>新闻动态</h3>
                <router-link to="/news" class="more-link">查看更多</router-link>
              </div>
              <div class="news-list">
                <div v-for="item in latestNews" :key="item.id" class="news-item" @click="$router.push(`/news/${item.id}`)">
                  <div class="news-date">
                    <span class="day">{{ formatDate(item.publishTime).day }}</span>
                    <span class="month">{{ formatDate(item.publishTime).month }}</span>
                  </div>
                  <div class="news-detail">
                    <h4>{{ item.title }}</h4>
                    <p>{{ item.summary }}</p>
                  </div>
                </div>
              </div>
            </div>
          </el-col>
          <el-col :xs="24" :md="12">
            <div class="info-column">
              <div class="column-header">
                <h3>特色活动</h3>
                <router-link to="/activities" class="more-link">查看更多</router-link>
              </div>
              <div class="activity-list">
                <div v-for="item in latestActivities" :key="item.id" class="activity-item" @click="$router.push(`/activities/${item.id}`)">
                  <el-image :src="item.coverUrl" class="activity-cover" fit="cover" />
                  <div class="activity-info">
                    <h4>{{ item.name }}</h4>
                    <p class="meta">{{ item.startTime }} | {{ item.venueName }}</p>
                  </div>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { Picture, VideoPlay } from '@element-plus/icons-vue';
import {
  fetchActivityPage,
  fetchNewsPage,
  fetchScenicPage,
  type ActivityItem,
  type NewsItem,
  type ScenicItem
} from '@/services/portal';

const router = useRouter();
const scenicCarousel = ref<ScenicItem[]>([]);
const latestNews = ref<NewsItem[]>([]);
const latestActivities = ref<ActivityItem[]>([]);

onMounted(async () => {
  const scenicResp = await fetchScenicPage({ current: 1, size: 6 });
  scenicCarousel.value = scenicResp.list;

  const newsResp = await fetchNewsPage({ current: 1, size: 4 });
  latestNews.value = newsResp.list;

  const activityResp = await fetchActivityPage({ current: 1, size: 4 });
  latestActivities.value = activityResp.list;
});

const goScenic = () => router.push('/scenic');
const goDetail = (id: number) => router.push(`/scenic/${id}`);

const formatDate = (dateStr: string) => {
  const date = new Date(dateStr);
  return {
    day: date.getDate().toString().padStart(2, '0'),
    month: `${date.getMonth() + 1}月`
  };
};
</script>

<style scoped>
.home-container {
  background: #fff;
}

/* Banner Styles */
.full-banner {
  width: 100%;
}

.banner-item {
  height: 100%;
  background-size: cover;
  background-position: center;
  position: relative;
}

.banner-overlay {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
}

.banner-content {
  text-align: center;
  color: #fff;
  animation: fadeUp 1s ease-out;
}

.banner-content h1 {
  font-size: 48px;
  margin: 0 0 16px;
  letter-spacing: 4px;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.5);
}

.banner-content p {
  font-size: 20px;
  opacity: 0.9;
  letter-spacing: 2px;
}

.empty-banner {
  background: #f0f2f5;
  display: flex;
  align-items: center;
  justify-content: center;
}

.empty-content {
  text-align: center;
  color: #909399;
}

/* Section Common Styles */
.section-block {
  padding: 80px 0;
}

.section-header {
  text-align: center;
  margin-bottom: 48px;
}

.section-header h2 {
  font-size: 32px;
  color: #1f2d3d;
  margin: 0 0 16px;
}

.divider {
  width: 60px;
  height: 4px;
  background: #2c7be5;
  margin: 0 auto;
}

/* Intro Section */
.intro-section {
  background: #fff;
}

.intro-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 48px;
  align-items: center;
}

.intro-text p {
  line-height: 1.8;
  color: #5e6d82;
  margin-bottom: 24px;
  font-size: 16px;
}

.intro-media {
  height: 320px;
  background: #2c3e50;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
}

.media-placeholder {
  color: #fff;
  text-align: center;
  opacity: 0.8;
}

/* Scenery Section */
.scenery-section {
  background: #f9fafc;
}

.scenic-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 24px;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
}

.scenic-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.08);
}

.card-image {
  height: 220px;
  overflow: hidden;
}

.card-image .el-image {
  width: 100%;
  height: 100%;
  transition: transform 0.5s;
}

.scenic-card:hover .card-image .el-image {
  transform: scale(1.05);
}

.card-info {
  padding: 20px;
}

.card-info h3 {
  margin: 0 0 8px;
  font-size: 18px;
  color: #1f2d3d;
}

.desc {
  color: #8492a6;
  font-size: 14px;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* Info Section */
.info-section {
  background: #fff;
}

.column-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  border-bottom: 1px solid #ebeef5;
  padding-bottom: 12px;
}

.column-header h3 {
  margin: 0;
  font-size: 22px;
  color: #2c3e50;
}

.more-link {
  color: #909399;
  font-size: 14px;
}

.more-link:hover {
  color: #2c7be5;
}

.news-item {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
  cursor: pointer;
  padding: 12px;
  border-radius: 6px;
  transition: background 0.2s;
}

.news-item:hover {
  background: #f5f7fa;
}

.news-date {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 60px;
  height: 60px;
  background: #f0f2f5;
  border-radius: 4px;
  color: #5e6d82;
}

.news-date .day {
  font-size: 20px;
  font-weight: 700;
  color: #2c7be5;
}

.news-date .month {
  font-size: 12px;
}

.news-detail h4 {
  margin: 0 0 6px;
  font-size: 16px;
  color: #1f2d3d;
}

.news-detail p {
  margin: 0;
  font-size: 13px;
  color: #8492a6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.activity-list {
  display: grid;
  gap: 16px;
}

.activity-item {
  display: flex;
  gap: 16px;
  align-items: center;
  cursor: pointer;
  padding: 10px;
  border-radius: 6px;
  transition: background 0.2s;
}

.activity-item:hover {
  background: #f5f7fa;
}

.activity-cover {
  width: 100px;
  height: 64px;
  border-radius: 4px;
}

.activity-info h4 {
  margin: 0 0 4px;
  font-size: 15px;
  color: #1f2d3d;
}

.activity-info .meta {
  margin: 0;
  font-size: 12px;
  color: #909399;
}

@keyframes fadeUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 768px) {
  .intro-content {
    grid-template-columns: 1fr;
  }
  
  .banner-content h1 {
    font-size: 28px;
  }
  
  .section-block {
    padding: 40px 0;
  }
}
</style>
