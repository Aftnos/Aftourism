<template>
  <div class="page-wrapper">
    <div class="hero-grid">
      <div class="content-card hero-card">
        <div class="hero-left">
          <p class="hero-eyebrow">智能推荐 · 多端适配</p>
          <h2>沉浸式文旅服务，随时随地畅享</h2>
          <p class="hero-desc">精选 5A 级景区、热门场馆与特色活动，以轻量动效强化浏览体验，为不同屏幕设备提供丝滑访问。</p>
          <div class="hero-actions">
            <el-button type="primary" size="large" @click="goScenic">探索景区</el-button>
            <el-button size="large" plain @click="goActivities">活动日历</el-button>
          </div>
          <div class="hero-stats">
            <div class="stat-item" v-for="item in heroStats" :key="item.label">
              <div class="stat-value">{{ item.value }}</div>
              <div class="stat-label">{{ item.label }}</div>
            </div>
          </div>
        </div>
        <div class="hero-visual">
          <div class="floating-badge">Element Plus · Vite 动效</div>
          <div class="hero-blob" />
          <div class="hero-blob secondary" />
        </div>
      </div>
      <div v-for="item in serviceHighlights" :key="item.title" class="content-card highlight-card">
        <div class="highlight-icon">{{ item.icon }}</div>
        <div>
          <div class="highlight-title">{{ item.title }}</div>
          <div class="highlight-desc">{{ item.desc }}</div>
        </div>
        <el-tag type="success" effect="light">{{ item.tip }}</el-tag>
      </div>
    </div>

    <el-row :gutter="16" class="main-section">
      <el-col :xs="24" :md="16">
        <div class="content-card">
          <div class="section-title">
            <h3>5A 级景区风光轮播</h3>
            <span>展示高品质景区形象</span>
          </div>
          <el-carousel height="320px" indicator-position="outside" :interval="4200" arrow="always">
            <el-carousel-item v-for="item in scenicCarousel" :key="item.id">
              <div class="carousel-item" :style="{ backgroundImage: `url(${item.cover})` }">
                <div class="caption">
                  <h4>{{ item.name }}</h4>
                  <p>{{ item.description }}</p>
                </div>
              </div>
            </el-carousel-item>
          </el-carousel>
        </div>
      </el-col>
      <el-col :xs="24" :md="8">
        <div class="content-card">
          <div class="section-title">
            <h3>新闻动态</h3>
            <router-link class="more-link" to="/news">查看更多</router-link>
          </div>
          <el-timeline class="news-timeline">
            <el-timeline-item
              v-for="item in latestNews"
              :key="item.id"
              :timestamp="item.publishTime"
              placement="top"
            >
              <router-link :to="`/news/${item.id}`" class="title-link">{{ item.title }}</router-link>
              <p class="intro">{{ item.content }}</p>
            </el-timeline-item>
          </el-timeline>
        </div>
        <div class="content-card" style="margin-top: 12px">
          <div class="section-title">
            <h3>特色活动</h3>
            <router-link class="more-link" to="/activities">查看更多</router-link>
          </div>
          <el-timeline class="news-timeline">
            <el-timeline-item
              v-for="item in latestActivities"
              :key="item.id"
              :timestamp="item.startTime"
              placement="top"
              type="success"
            >
              <router-link :to="`/activities/${item.id}`" class="title-link">{{ item.name }}</router-link>
              <p class="intro">{{ item.venueName }} · {{ item.category }}</p>
            </el-timeline-item>
          </el-timeline>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { activities, newsList, scenicSpots } from '@/data/mockData';

// 中文注释：首页展示轮播、新闻、特色活动，同时加入动效与多端布局
const router = useRouter();
const scenicCarousel = scenicSpots.filter((item) => item.level === '5A');
const latestNews = computed(() => newsList.slice(0, 5));
const latestActivities = computed(() => activities.filter((a) => a.status === 'approved').slice(0, 5));

// 中文注释：亮点卡片数据，强调文旅体验亮点
const serviceHighlights = [
  { title: '景区浏览丝滑', desc: '高品质轮播与动效，突出景区卖点', icon: '🌄', tip: '动效升级' },
  { title: '活动跟进及时', desc: '审批通过即刻上架，多端同步提示', icon: '🎉', tip: '实时更新' },
  { title: '资讯阅读便捷', desc: '时间轴布局，突出最新新闻与公告', icon: '📰', tip: '分端适配' },
];

// 中文注释：统计信息，快速反馈平台数据规模
const heroStats = computed(() => [
  { label: '优选景区', value: `${scenicSpots.length} 个` },
  { label: '活动上线', value: `${activities.length} 场` },
  { label: '资讯推送', value: `${newsList.length} 条` },
]);

const goScenic = () => router.push('/scenic');
const goActivities = () => router.push('/activities');
</script>

<style scoped>
.hero-grid {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr;
  gap: 12px;
  margin-bottom: 16px;
}

.hero-card {
  display: grid;
  grid-template-columns: 1.4fr 1fr;
  align-items: center;
  background: linear-gradient(130deg, #ffffff, #eaf3ff);
  overflow: hidden;
  position: relative;
  border: 1px solid rgba(44, 123, 229, 0.12);
}

.hero-left h2 {
  margin: 8px 0 6px;
  font-size: 26px;
}

.hero-eyebrow {
  margin: 0;
  color: #2c7be5;
  font-weight: 600;
  letter-spacing: 1px;
}

.hero-desc {
  color: #4b5563;
  margin: 8px 0 14px;
  line-height: 1.6;
}

.hero-actions {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.hero-stats {
  margin-top: 16px;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
}

.stat-item {
  background: rgba(44, 123, 229, 0.08);
  border-radius: 10px;
  padding: 12px;
  text-align: center;
}

.stat-value {
  font-size: 20px;
  font-weight: 700;
  color: #1f2d3d;
}

.stat-label {
  color: #637185;
  margin-top: 4px;
}

.hero-visual {
  position: relative;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.hero-blob {
  width: 260px;
  height: 260px;
  border-radius: 50%;
  background: radial-gradient(circle at 30% 30%, #93c5fd, #2563eb);
  opacity: 0.6;
  filter: blur(6px);
  animation: pulse 6s ease-in-out infinite;
}

.hero-blob.secondary {
  position: absolute;
  width: 180px;
  height: 180px;
  background: radial-gradient(circle at 70% 70%, #a7f3d0, #22c55e);
  opacity: 0.5;
  animation-delay: 1.2s;
}

.floating-badge {
  position: absolute;
  top: 24px;
  right: 24px;
  padding: 8px 12px;
  background: #1f2d3d;
  color: #fff;
  border-radius: 999px;
  font-size: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.18);
}

.highlight-card {
  display: grid;
  grid-template-columns: 44px 1fr auto;
  gap: 10px;
  align-items: center;
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}

.highlight-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 10px 30px rgba(44, 123, 229, 0.18);
}

.highlight-icon {
  width: 44px;
  height: 44px;
  display: grid;
  place-items: center;
  background: rgba(44, 123, 229, 0.12);
  border-radius: 12px;
  font-size: 20px;
}

.highlight-title {
  font-size: 16px;
  font-weight: 700;
}

.highlight-desc {
  color: #637185;
  margin-top: 4px;
  line-height: 1.4;
}

.main-section .content-card + .content-card {
  margin-top: 12px;
}

.carousel-item {
  height: 320px;
  border-radius: 8px;
  background-size: cover;
  background-position: center;
  position: relative;
  overflow: hidden;
}

.carousel-item::after {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(0, 0, 0, 0) 40%, rgba(0, 0, 0, 0.45));
}

.caption {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 16px;
  color: #fff;
  z-index: 1;
}

.caption h4 {
  margin: 0;
  font-size: 20px;
}

.intro {
  margin: 6px 0 0;
  color: #666;
}

.news-timeline :deep(.el-timeline-item__timestamp) {
  color: #909399;
}

.title-link {
  font-weight: 600;
  color: #1f2d3d;
}

.title-link:hover {
  color: #2c7be5;
}

@keyframes pulse {
  0% {
    transform: scale(0.96);
    opacity: 0.65;
  }
  50% {
    transform: scale(1.04);
    opacity: 0.9;
  }
  100% {
    transform: scale(0.96);
    opacity: 0.65;
  }
}

@media (max-width: 1200px) {
  .hero-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .hero-card {
    grid-column: span 2;
    grid-template-columns: 1fr;
    row-gap: 10px;
  }

  .hero-visual {
    min-height: 200px;
  }
}

@media (max-width: 768px) {
  .hero-grid {
    grid-template-columns: 1fr;
  }

  .hero-stats {
    grid-template-columns: repeat(2, 1fr);
  }

  .highlight-card {
    grid-template-columns: 44px 1fr;
    grid-template-rows: auto auto;
  }

  .highlight-card .el-tag {
    justify-self: start;
  }
}
</style>
