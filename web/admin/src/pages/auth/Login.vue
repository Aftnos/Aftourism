<template>
  <div class="login-page">
    <ElCard class="login-card">
      <h2>后台登录</h2>
      <ElForm :model="form" label-width="80px" @keyup.enter="submit">
        <ElFormItem label="账号">
          <ElInput v-model="form.username" placeholder="请输入账号" />
        </ElFormItem>
        <ElFormItem label="密码">
          <ElInput v-model="form.password" type="password" placeholder="请输入密码" />
        </ElFormItem>
        <ElFormItem>
          <ElButton type="primary" :loading="auth.loading" @click="submit">登录</ElButton>
        </ElFormItem>
      </ElForm>
    </ElCard>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { useAuthStore } from '@/store/auth';

const router = useRouter();
const auth = useAuthStore();
const form = reactive({ username: '', password: '' });

async function submit() {
  await auth.login(form);
  ElMessage.success('登录成功');
  router.push('/dashboard');
}
</script>

<style scoped>
.login-page {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(120deg, #1d4ed8, #9333ea);
}
.login-card {
  width: 360px;
}
</style>
