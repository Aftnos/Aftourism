<template>
  <div class="layout">
    <NProgressBar />
    <div class="bg-circle circle-1" />
    <div class="bg-circle circle-2" />
    <div class="bg-circle circle-3" />
    <el-container class="content-shell">
      <el-header height="72px" class="header">
        <HeaderNav />
      </el-header>
      <el-main class="main-area">
        <router-view v-slot="{ Component }">
          <transition name="fade-slide">
            <component :is="Component" :key="$route.fullPath" />
          </transition>
        </router-view>
      </el-main>
      <el-footer height="auto" class="footer">
        <FooterBar />
      </el-footer>
    </el-container>
    <el-backtop :bottom="36" :right="28" class="custom-backtop" />
  </div>
</template>

<script setup lang="ts">
import HeaderNav from '@/components/HeaderNav.vue';
import FooterBar from '@/components/FooterBar.vue';
import NProgressBar from '@/components/NProgressBar.vue';
</script>

<style scoped>
.layout {
  min-height: 100vh;
  background: #f5f7fa;
  position: relative;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.content-shell {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  flex: 1;
}

.header {
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  padding: 0 24px;
  position: sticky;
  top: 0;
  z-index: 100;
  flex-shrink: 0;
}

.main-area {
  padding: 0;
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
  scrollbar-gutter: stable both-edges;
}

.footer {
  background: #1f2937;
  color: #9ca3af;
  text-align: center;
  border-top: none;
  flex-shrink: 0;
  backface-visibility: hidden;
  transform: translateZ(0);
}

.bg-circle {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.55;
  animation: float 12s ease-in-out infinite;
}

.circle-1 {
  width: 320px;
  height: 320px;
  background: #9cc7ff;
  top: -120px;
  left: -80px;
}

.circle-2 {
  width: 260px;
  height: 260px;
  background: #ffe5c2;
  bottom: 60px;
  right: 120px;
  animation-delay: 1.6s;
}

.circle-3 {
  width: 220px;
  height: 220px;
  background: #c6f2e6;
  bottom: -80px;
  left: 20%;
  animation-delay: 0.8s;
}

@keyframes float {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-22px);
  }
}

.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: opacity 0.35s ease, transform 0.35s ease;
  will-change: opacity, transform;
}

.fade-slide-enter-from,
.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(12px);
}

@media screen and (max-width: 768px) {
  .custom-backtop {
    bottom: 160px !important;
    right: 20px !important;
  }
}
</style>
