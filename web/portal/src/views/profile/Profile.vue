<template>
  <div class="page-wrapper">
    <div class="content-card">
      <div class="section-title">
        <h3>个人信息管理</h3>
        <span>修改姓名、联系电话、邮箱等信息</span>
      </div>
      <el-empty v-if="!userStore.isLogin" description="请先登录" />
      <el-form v-else :model="form" label-width="100px">
        <el-form-item label="姓名">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="save">保存</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, watch } from 'vue';
import { ElMessage } from 'element-plus';
import { useUserStore } from '@/store/user';

// 中文注释：个人信息编辑与保存
const userStore = useUserStore();
const form = reactive({ name: '', phone: '', email: '' });

watch(
  () => userStore.profile,
  (profile) => {
    form.name = profile.name;
    form.phone = profile.phone;
    form.email = profile.email;
  },
  { immediate: true }
);

const save = () => {
  userStore.updateProfile(form);
  ElMessage.success('已更新个人信息');
};
</script>
