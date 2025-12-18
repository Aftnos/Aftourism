<template>
  <div class="art-card p-5 h-128 overflow-hidden mb-5 max-sm:mb-4">
    <div class="art-card-header">
      <div class="title">
        <h4>{{ title }}</h4>
        <p class="text-sm text-g-500">来自门户最新发布</p>
      </div>
      <slot name="extra"></slot>
    </div>
    <div class="mt-3 space-y-3">
      <div v-for="item in items" :key="item.id" class="flex-b border-b border-g-100 pb-2 last:border-b-0">
        <div class="flex-1 pr-2">
          <p class="text-g-900 text-sm line-clamp-2">{{ item.title }}</p>
          <p class="text-xs text-g-500 mt-1">发布时间：{{ item.publishTime || '未发布' }}</p>
        </div>
        <div class="text-right text-sm text-g-600 whitespace-nowrap">点击 {{ item.viewCount ?? 0 }}</div>
      </div>
      <div v-if="!items.length" class="text-center text-g-500 text-sm">暂无数据</div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import type { ContentBrief } from '@/api/dashboard'

  defineProps<{ title: string; items: ContentBrief[] }>()
</script>
