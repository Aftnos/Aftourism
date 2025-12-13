<template>
  <div class="page-wrapper">
    <el-row :gutter="16">
      <el-col :span="16">
        <div class="content-card">
          <div class="section-title">
            <h3>5A 级景区风光轮播</h3>
            <span>展示高品质景区形象</span>
          </div>
          <el-carousel height="320px">
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
      <el-col :span="8">
        <div class="content-card">
          <div class="section-title">
            <h3>新闻动态</h3>
            <router-link class="more-link" to="/news">查看更多</router-link>
          </div>
          <el-timeline>
            <el-timeline-item
              v-for="item in latestNews"
              :key="item.id"
              :timestamp="item.publishTime"
              placement="top"
            >
              <router-link :to="`/news/${item.id}`">{{ item.title }}</router-link>
              <p class="intro">{{ item.content }}</p>
            </el-timeline-item>
          </el-timeline>
        </div>
        <div class="content-card" style="margin-top: 12px">
          <div class="section-title">
            <h3>特色活动</h3>
            <router-link class="more-link" to="/activities">查看更多</router-link>
          </div>
          <el-timeline>
            <el-timeline-item
              v-for="item in latestActivities"
              :key="item.id"
              :timestamp="item.startTime"
              placement="top"
            >
              <router-link :to="`/activities/${item.id}`">{{ item.name }}</router-link>
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
import { activities, newsList, scenicSpots } from '@/data/mockData';

// 中文注释：首页展示轮播、新闻、特色活动
const scenicCarousel = scenicSpots.filter((item) => item.level === '5A');
const latestNews = computed(() => newsList.slice(0, 5));
const latestActivities = computed(() => activities.filter((a) => a.status === 'approved').slice(0, 5));
</script>

<style scoped>
.carousel-item {
  height: 320px;
  border-radius: 8px;
  background-size: cover;
  background-position: center;
  position: relative;
}

.caption {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 16px;
  color: #fff;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.6), transparent);
}

.caption h4 {
  margin: 0;
  font-size: 20px;
}

.intro {
  margin: 6px 0 0;
  color: #666;
}
</style>
