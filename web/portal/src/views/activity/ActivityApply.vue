<template>
  <div class="page-wrapper">
    <div class="content-card">
      <div class="section-title">
        <h3>特色活动申报</h3>
      </div>
      <el-alert v-if="!userStore.isLogin" title="请先登录，再提交申报" type="warning" show-icon />
      <el-form v-else :model="form" label-width="100px" :rules="rules" ref="formRef">
        <el-form-item label="活动名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="封面图" prop="cover">
          <el-upload
            class="cover-uploader"
            action="#"
            :http-request="handleCoverUpload"
            :show-file-list="false"
            accept="image/*"
          >
            <el-image v-if="form.cover" :src="form.cover" fit="cover" class="cover-preview" />
            <div v-else class="cover-placeholder">点击上传封面</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="起止时间" prop="timeRange">
          <el-date-picker
            v-model="form.timeRange"
            type="datetimerange"
            range-separator="至"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="类别" prop="category">
          <el-select v-model="form.category" placeholder="选择类别">
            <el-option label="体育" value="体育" />
            <el-option label="文化" value="文化" />
            <el-option label="研学" value="研学" />
            <el-option label="展览" value="展览" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属场馆" prop="venueId">
          <el-select v-model="form.venueId" placeholder="选择已有场馆">
            <el-option v-for="item in venueOptions" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="主办单位" prop="organizer">
          <el-input v-model="form.organizer" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="简介" prop="summary">
          <div class="editor-wrapper">
            <Toolbar :editor="editorRef" mode="default" style="border-bottom: 1px solid #dcdfe6" />
            <Editor
              v-model="form.summary"
              :default-config="editorConfig"
              mode="default"
              style="height: 300px; overflow-y: hidden"
              @onCreated="handleCreated"
            />
          </div>
        </el-form-item>
        <el-form-item class="form-actions">
          <el-button type="primary" size="large" @click="submit" style="width: 200px">提交申报</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { Editor, Toolbar } from '@wangeditor/editor-for-vue';
import type { IDomEditor } from '@wangeditor/editor';
import { ElMessage, type FormInstance, type FormRules, type UploadRequestOptions } from 'element-plus';
import { useUserStore } from '@/store/user';
import { applyActivity, fetchVenuePage, uploadFile, type VenueItem } from '@/services/portal';
import '@wangeditor/editor/dist/css/style.css';

// 中文注释：活动申报表单，使用 wangEditor 富文本填写简介
const userStore = useUserStore();
const formRef = ref<FormInstance>();
const editorRef = ref<IDomEditor | null>(null);
const venueOptions = ref<VenueItem[]>([]);

const form = ref({
  name: '',
  cover: '',
  timeRange: [] as string[],
  category: '',
  venueId: undefined as number | undefined,
  organizer: '',
  phone: '',
  summary: ''
});

const rules: FormRules = {
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  timeRange: [{ type: 'array', required: true, message: '请选择时间', trigger: 'change' }],
  category: [{ required: true, message: '请选择类别', trigger: 'change' }],
  venueId: [{ required: true, message: '请选择场馆', trigger: 'change' }]
};

const editorConfig = {
  placeholder: '请输入活动简介...',
  MENU_CONF: {
    uploadImage: {
      async customUpload(file: File, insertFn: (url: string, alt?: string, href?: string) => void) {
        const res = await uploadFile(file, 'activity_rich_image');
        insertFn(res.url, '图片', res.url);
      }
    },
    uploadVideo: {
      async customUpload(file: File, insertFn: (url: string, poster?: string) => void) {
        const res = await uploadFile(file, 'activity_rich_video');
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
    const res = await uploadFile(options.file, 'activity_cover');
    form.value.cover = res.url;
    ElMessage.success('封面上传成功');
  } catch (error) {}
};

const submit = () => {
  if (!formRef.value) return;
  formRef.value.validate(async (valid) => {
    if (!valid) return;
    if (!form.value.timeRange || form.value.timeRange.length !== 2 || !form.value.venueId) return;
    const newId = await applyActivity({
      name: form.value.name,
      coverUrl: form.value.cover,
      startTime: form.value.timeRange[0],
      endTime: form.value.timeRange[1],
      category: form.value.category,
      venueId: form.value.venueId,
      organizer: form.value.organizer,
      contactPhone: form.value.phone,
      intro: form.value.summary
    });
    userStore.addSubmission(newId);
    ElMessage.success('已提交，待管理员审核');
  });
};

// 中文注释：加载可选场馆，默认拉取较多条目供选择
onMounted(async () => {
  const resp = await fetchVenuePage({ current: 1, size: 50 });
  venueOptions.value = resp.list;
});
</script>

<style scoped>
.page-wrapper {
  width: 100%;
  padding: 32px 48px;
  box-sizing: border-box;
  display: flex;
  justify-content: center;
  max-width: none;
}

.content-card {
  width: min(1000px, 100%);
  background: #fff;
  padding: 40px 48px;
  box-sizing: border-box;
  border-radius: 16px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.04);
}

.editor-wrapper {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  width: 100%;
  overflow: hidden;
  z-index: 100; /* Fix toolbar overlay issues */
}

:deep(.cover-uploader) {
  display: inline-block;
  width: 100%;
}
.cover-preview {
  width: 100%;
  height: 180px;
  border-radius: 6px;
  display: block;
}
.cover-placeholder {
  width: 100%;
  height: 180px;
  border: 2px dashed #cbd5e1;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #94a3b8;
}

:deep(.w-e-text-container) {
  min-height: 300px;
}

.form-actions {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

@media (max-width: 960px) {
  .page-wrapper {
    padding: 16px;
  }
  
  .content-card {
    padding: 24px 20px;
    width: 100%;
  }

  /* 移动端表单标签垂直排列 */
  :deep(.el-form-item) {
    flex-direction: column;
    align-items: flex-start;
  }

  :deep(.el-form-item__label) {
    width: auto !important;
    margin-bottom: 8px;
    text-align: left;
  }

  :deep(.el-form-item__content) {
    width: 100%;
    margin-left: 0 !important;
  }

  /* 编辑器在移动端的高度调整 */
  :deep(.w-e-text-container) {
    min-height: 200px;
  }
}
</style>
