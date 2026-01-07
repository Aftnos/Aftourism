<template>
  <div class="footer-wrapper">
    <div class="footer-container">
      <div class="footer-left">
        <h3 class="footer-title">Aftourism</h3>
        <p class="footer-desc">
          专注于为您提供全方位的文旅资讯服务。无论是热门景区、特色场馆，还是精彩活动，这里都有您想要的一切。
        </p>
        <div class="social-links">
          <!-- 示例社交图标 -->
          <el-icon :size="24" title="微信公众号"><ChatDotRound /></el-icon>
          <el-icon :size="24" title="官方微博"><Share /></el-icon>
          <el-icon :size="24" title="抖音号"><Camera /></el-icon>
        </div>
      </div>

      <div class="footer-links">
        <div class="link-column">
          <h4>关于我们</h4>
          <a href="javascript:;">平台简介</a>
          <a href="javascript:;">加入我们</a>
          <a href="javascript:;">联系方式</a>
        </div>
        <div class="link-column">
          <h4>帮助中心</h4>
          <a href="javascript:;">用户指南</a>
          <a href="javascript:;">常见问题</a>
          <a href="javascript:;">意见反馈</a>
        </div>
        <div class="link-column">
          <h4>法律条款</h4>
          <a href="javascript:;">隐私政策</a>
          <a href="javascript:;">服务协议</a>
          <a href="javascript:;">免责声明</a>
        </div>
      </div>
    </div>

    <div class="footer-bottom">
      <div class="copyright">
        © 2026 Aftourism 文旅服务管理平台 · All Rights Reserved
      </div>
      <div class="beian">
        <a href="https://beian.miit.gov.cn/" target="_blank">渝ICP备6666688888号</a>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ChatDotRound, Camera, Share } from '@element-plus/icons-vue';
// 底部栏组件，包含品牌介绍、快速链接及版权信息
</script>

<style scoped>



.footer-wrapper {
  padding: 40px 0 20px;
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  color: #9ca3af;
}

.footer-container {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 40px;
  padding-bottom: 30px;
  border-bottom: 1px solid #374151;
}

.footer-left {
  flex: 1;
  min-width: 280px;
  max-width: 400px;
}

.footer-title {
  font-size: 24px;
  color: #fff;
  margin-bottom: 16px;
  font-weight: 600;
}

.footer-desc {
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 20px;
  color: #9ca3af;
}

.social-links {
  display: flex;
  gap: 16px;
}

.social-links .el-icon {
  cursor: pointer;
  transition: color 0.3s;
  color: #9ca3af;
}

.social-links .el-icon:hover {
  color: #fff;
}

.footer-links {
  display: flex;
  gap: 60px;
  flex-wrap: wrap;
}

.link-column h4 {
  font-size: 16px;
  color: #fff;
  margin-bottom: 16px;
  font-weight: 600;
}

.link-column a {
  display: block;
  font-size: 14px;
  color: #9ca3af;
  margin-bottom: 10px;
  text-decoration: none;
  transition: color 0.3s;
}

.link-column a:hover {
  color: #fff;
}

.footer-bottom {
  padding-top: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  font-size: 13px;
  gap: 10px;
}

.footer-bottom a {
  color: #9ca3af;
  text-decoration: none;
  transition: color 0.3s;
}

.footer-bottom a:hover {
  color: #fff;
}

/* 响应式适配 */
@media (max-width: 768px) {
  .footer-container {
    flex-direction: column;
    gap: 30px;
  }

  .footer-links {
    gap: 30px;
    width: 100%;
    justify-content: space-between;
  }

  .footer-bottom {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
}
</style>
