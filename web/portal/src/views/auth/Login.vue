<template>
  <div class="page-wrapper">
    <el-card class="login-card">
      <h3>账号密码登录</h3>
      <el-form :model="form" label-width="80px">
        <el-form-item label="账号">
          <el-input v-model="form.account" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submit">登录</el-button>
          <el-button link @click="goRegister">去注册</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { useUserStore } from '@/store/user';

// 中文注释：账号密码登录对接后端接口，成功后刷新用户资料
const router = useRouter();
const userStore = useUserStore();
const form = reactive({ account: '', password: '' });

const submit = async () => {
  if (!form.account || !form.password) {
    ElMessage.error('请输入账号和密码');
    return;
  }
  await userStore.login(form.account, form.password);
  router.push('/');
};

const goRegister = () => router.push('/register');
</script>

<style scoped>
.login-card {
  max-width: 420px;
  margin: 40px auto;
}
</style>
