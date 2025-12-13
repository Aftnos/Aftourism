<template>
  <div class="page-wrapper">
    <el-card class="login-card">
      <h3>用户注册</h3>
      <el-form :model="form" label-width="100px">
        <el-form-item label="账号">
          <el-input v-model="form.account" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" />
        </el-form-item>
        <el-form-item label="验证码">
          <el-input v-model="form.captcha" placeholder="模拟验证码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submit">注册</el-button>
          <el-button link @click="goLogin">返回登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';

// 中文注释：简单模拟注册流程，成功后跳转登录
const router = useRouter();
const form = reactive({ account: '', password: '', captcha: '' });

const submit = () => {
  if (!form.account || !form.password || !form.captcha) {
    ElMessage.error('请输入完整信息');
    return;
  }
  ElMessage.success('注册成功，请登录');
  router.push('/login');
};

const goLogin = () => router.push('/login');
</script>

<style scoped>
.login-card {
  max-width: 420px;
  margin: 40px auto;
}
</style>
