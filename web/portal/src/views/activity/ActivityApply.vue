<template>
  <div class="page-wrapper">
    <div class="content-card">
      <div class="section-title">
        <h3>特色活动申报</h3>
        <span>登录用户可提交活动，等待管理员审核</span>
      </div>
      <el-alert v-if="!userStore.isLogin" title="请先登录，再提交申报" type="warning" show-icon />
      <el-form v-else :model="form" label-width="100px" :rules="rules" ref="formRef">
        <el-form-item label="活动名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="封面图" prop="cover">
          <el-input v-model="form.cover" placeholder="粘贴图片地址" />
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
        <el-form-item>
          <el-button type="primary" @click="submit">提交申报</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { Editor, Toolbar } from '@wangeditor/editor-for-vue';
import type { IDomEditor } from '@wangeditor/editor';
import { ElMessage, type FormInstance, type FormRules } from 'element-plus';
import { useUserStore } from '@/store/user';
import { applyActivity, fetchVenuePage, type VenueItem } from '@/services/portal';
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

const editorConfig = { placeholder: '请输入活动简介...' };

const handleCreated = (editor: IDomEditor) => {
  editorRef.value = editor;
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
.editor-wrapper {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  width: 100%;
  overflow: hidden;
}

:deep(.w-e-text-container) {
  min-height: 300px;
}
</style>
