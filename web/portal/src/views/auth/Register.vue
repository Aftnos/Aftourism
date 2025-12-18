<template>
  <div class="page-wrapper">
    <el-card class="login-card">
      <h3>用户注册</h3>
      <el-form :model="form" label-width="100px">
        <el-form-item label="账号">
          <el-input v-model="form.username" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" placeholder="用于展示的昵称" />
        </el-form-item>
        <el-form-item label="手机">
          <el-input v-model="form.phone" placeholder="用于找回密码" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="接收通知的邮箱" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="submitting" @click="submit">注册</el-button>
          <el-button link @click="goLogin">返回登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { register } from '@/services/portal';

const router = useRouter();
const submitting = ref(false);
const form = reactive({ username: '', password: '', nickname: '', phone: '', email: '' });

const submit = () => {
  if (!form.username || !form.password) {
    ElMessage.error('请输入账号和密码');
    return;
  }
  submitting.value = true;
  register({ ...form })
    .then(() => {
      ElMessage.success('注册成功，请登录');
      router.push('/login');
    })
    .catch((err) => {
      console.error('注册失败', err);
    })
    .finally(() => {
      submitting.value = false;
    });
};

const goLogin = () => router.push('/login');
</script>

<style scoped>
.login-card {
  max-width: 420px;
  margin: 40px auto;
}
</style>
