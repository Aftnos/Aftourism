<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-left">
        <div class="login-left-content">
          <div class="logo">
            <img src="@/assets/logo.png" alt="Logo" class="logo-img" width="30" height="30" />
            <span class="logo-text">Aftourism</span>
          </div>
          <div class="illustration">
             <!-- 背景图片地 -->
              <img src="@/assets/logo-main.svg" alt="Illustration" class="illustration-img" width="500" height="500" />
          </div>
          <div class="description">
            <h3>业界领先的文旅管理系统</h3>
            <p>©Aftourism 1.0 文旅系统, powered by Aftnos.</p>
          </div>
        </div>
      </div>
      <div class="login-right">
        <div class="login-form-content">
          <h1>欢迎回来</h1>
          <p class="sub-title">输入您的账号和密码登录</p>
          
          <el-form :model="form" size="large" class="login-form" @keyup.enter="submit">
            <el-form-item>
               <el-select v-model="role" placeholder="Select Role" style="width: 100%">
                  <el-option :label="value" :value="key" v-for="(value, key) in principalType" :key="key"/>
               </el-select>
            </el-form-item>

            <el-form-item>
              <el-input v-model="form.username" placeholder="请输入用户名" :prefix-icon="User" />
            </el-form-item>
            
            <el-form-item>
              <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password :prefix-icon="Lock" />
            </el-form-item>

            <div class="form-footer">
              <el-checkbox v-model="rememberMe">记住密码</el-checkbox>
              <el-button link type="primary">忘记密码</el-button>
            </div>

            <el-button type="primary" class="submit-btn" :loading="auth.loading" @click="submit">登录</el-button>
            
            <div class="register-link">
               还没有账号？ <el-button link type="primary">注册</el-button>
            </div>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { useAuthStore } from '@/store/auth';
import { User, Lock, Moon, ArrowRight } from '@element-plus/icons-vue';

const router = useRouter();
const auth = useAuthStore();
const form = reactive({ username: '', password: '' });
const role = ref('user');
const rememberMe = ref(true);
const principalType = ref({ user: '普通用户' , admin: '管理员'});

async function submit() {
  if (!form.username || !form.password) {
     ElMessage.warning('请输入账号和密码');
     return;
  }
  await auth.login(form);
  ElMessage.success('登录成功');
  router.push('/dashboard');
}
</script>

<style scoped lang="scss">
.login-container {
  height: 100vh;
  width: 100vw;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f0f2f5;
}

.login-box {
  display: flex;
  width: 100%;
  height: 100%;
  background: #fff;
  overflow: hidden;
  
  @media (min-width: 1200px) {
    // width: 1200px;
    // height: 700px;
    // border-radius: 10px;
    // box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  }
}

.login-left {
  width: 65%;
  background:#e6e9f0;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: relative;
  
  @media (max-width: 1200px) {
    display: none;
  }

  .login-left-content {
     text-align: center;
     width: 80%;
  }

  .logo {
    position: absolute;
    top: 40px;
    left: 40px;
    display: flex;
    align-items: center;
    gap: 10px;
    
    .logo-text {
      font-size: 24px;
      font-weight: bold;
      color: #333;
    }
  }

  .illustration {
    margin: 40px 0;
    img {
      max-width: 80%;
      height: auto;
    }
  }

  .description {
    // 显示在右下角
    position: absolute;
    bottom: 40px;
    right: 35%;
    h3 {
      font-size: 24px;
      color: #333;
      margin-bottom: 10px;
    }
    p {
      font-size: 14px;
      color: #666;
    }
  }
}

.login-right {
  flex: 1; /* 弹性布局，占据剩余空间 */
  display: flex; /* 弹性盒子布局 */
  flex-direction: column; /* 垂直方向排列 */
  position: relative; /* 相对定位 */
  background: #fff; /* 背景颜色为白色 */
  align-items: center;
}

.login-header {
  padding: 20px;
  display: flex;
  justify-content: flex-end;
}

.login-form-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 0 10px;
  
  @media (max-width: 600px) {
    padding: 0 20px;
  }

  h1 {
    font-size: 40px;
    font-weight: bold;
    color: #333;
    margin-bottom: 2px;
  }

  .sub-title {
    color: #999;
    margin: 0px 0 20px 0;
  }

  .login-form {
    width: 100%;
    max-width: 520px;
  }

  .form-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }

  .submit-btn {
    width: 100%;
    height: 44px;
    font-size: 16px;
    border-radius: 4px;
  }

  .register-link {
    margin-top: 20px;
    text-align: left;
    font-size: 14px;
    color: #666;
  }
}

.slider-captcha {
  width: 100%;
  height: 40px;
  background: #eef1f5;
  border-radius: 4px;
  position: relative;
  border: 1px solid #dcdfe6;
  
  .slider-bg {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
  }

  .slider-text {
    font-size: 12px;
    color: #666;
  }

  .slider-btn {
    position: absolute;
    left: 0;
    top: 0;
    width: 40px;
    height: 38px;
    background: #fff;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: grab;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    
    &:hover {
       background-color: #f5f7fa;
    }
  }
}
</style>
