<template>
  <div class="page-wrapper">
    <div class="content-card">
      <div class="section-title">
        <h3>发布交流文章</h3>
        <span>分享你的旅行攻略与推荐</span>
      </div>

      <el-alert v-if="!userStore.isLogin" title="登录后才能发布文章" type="warning" show-icon />

      <el-form v-else ref="formRef" :model="form" :rules="rules" label-width="80px" class="publish-form">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入文章标题" maxlength="120" show-word-limit />
        </el-form-item>
        <el-form-item label="封面">
          <el-upload class="cover-uploader" action="#" :show-file-list="false" :http-request="handleCoverUpload">
            <img v-if="form.coverUrl" :src="form.coverUrl" class="cover-image" />
            <div v-else class="cover-placeholder">
              <el-icon><Plus /></el-icon>
              <span>上传封面</span>
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <div class="editor-wrapper">
            <Toolbar :editor="editorRef" mode="default" style="border-bottom: 1px solid #dcdfe6" />
            <Editor
              v-model="form.content"
              :default-config="editorConfig"
              mode="default"
              style="height: 360px; overflow-y: hidden"
              @onCreated="handleCreated"
            />
          </div>
        </el-form-item>
        <el-form-item class="form-actions">
          <el-button type="primary" @click="submit">提交审核</el-button>
          <el-button @click="goBack">返回</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage, type FormInstance, type FormRules, type UploadRequestOptions } from 'element-plus';
import { Editor, Toolbar } from '@wangeditor/editor-for-vue';
import type { IDomEditor } from '@wangeditor/editor';
import { Plus } from '@element-plus/icons-vue';
import { createExchangeArticle, uploadFile } from '@/services/portal';
import { useUserStore } from '@/store/user';
import '@wangeditor/editor/dist/css/style.css';

// 中文注释：交流文章发布页，使用富文本编辑器
const router = useRouter();
const userStore = useUserStore();
const formRef = ref<FormInstance>();
const editorRef = ref<IDomEditor | null>(null);

const form = ref({
  title: '',
  content: '',
  coverUrl: ''
});

const rules: FormRules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
};

const editorConfig = {
  placeholder: '请输入交流内容...',
  MENU_CONF: {
    uploadImage: {
      async customUpload(file: File, insertFn: (url: string, alt?: string, href?: string) => void) {
        const res = await uploadFile(file, 'exchange_rich_image');
        insertFn(res.url, '图片', res.url);
      }
    },
    uploadVideo: {
      async customUpload(file: File, insertFn: (url: string, poster?: string) => void) {
        const res = await uploadFile(file, 'exchange_rich_video');
        insertFn(res.url, res.url);
      }
    }
  }
};

const handleCreated = (editor: IDomEditor) => {
  editorRef.value = editor;
};

const handleCoverUpload = async (options: UploadRequestOptions) => {
  try {
    const res = await uploadFile(options.file, 'exchange_cover');
    form.value.coverUrl = res.url;
    ElMessage.success('封面上传成功');
  } catch (error) {
    ElMessage.error('封面上传失败');
  }
};

const submit = async () => {
  if (!formRef.value) return;
  await formRef.value.validate();
  await createExchangeArticle({
    title: form.value.title,
    content: form.value.content,
    coverUrl: form.value.coverUrl || undefined
  });
  ElMessage.success('已提交审核');
  router.push('/exchange');
};

const goBack = () => router.back();
</script>

<style scoped lang="scss">
.page-wrapper {
  width: 100%;
  padding: 32px 48px;
  box-sizing: border-box;
  display: flex;
  justify-content: center;
}

.content-card {
  width: min(1200px, 100%);
  padding: 24px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(15, 23, 42, 0.06);
}

.publish-form {
  margin-top: 16px;
}

.editor-wrapper {
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  overflow: hidden;
}

.cover-uploader {
  display: inline-block;
}

.cover-placeholder {
  width: 160px;
  height: 90px;
  border: 1px dashed #cbd5f5;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #94a3b8;
  gap: 6px;
}

.cover-image {
  width: 160px;
  height: 90px;
  object-fit: cover;
  border-radius: 8px;
}

.form-actions {
  display: flex;
  gap: 12px;
}
</style>
