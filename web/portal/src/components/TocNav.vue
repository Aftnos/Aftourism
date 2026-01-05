<template>
  <div class="toc-wrapper" v-if="hasContent" ref="tocWrapperRef" :style="tocStyle">
    <div class="toc-title">目录导航</div>
    <div class="toc-content">
      <div 
        v-for="group in groups" 
        :key="group.key"
        :class="['toc-group', { active: activeGroup === group.key }]"
      >
        <div 
          class="group-title" 
          @click="handleGroupClick(group)"
        >
          {{ group.title }}
        </div>
        <div class="group-content">
          <ul class="toc-list" v-if="group.items && group.items.length > 0">
            <li 
              v-for="item in group.items" 
              :key="item.id"
              :class="['toc-item', `level-${item.level}`, { active: activeHeading === item.id }]"
              @click.stop="scrollToHeading(item.id)"
            >
              {{ item.title }}
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, type CSSProperties, watch } from 'vue';

export interface TocItem {
  id: string;
  title: string;
  level: number;
}

export interface TocGroup {
  key: string;
  title: string;
  id?: string; // Optional ID to scroll to when group title is clicked
  items: TocItem[];
}

const props = defineProps<{
  groups: TocGroup[];
}>();

const tocWrapperRef = ref<HTMLElement>();
const tocStyle = ref<CSSProperties>({});
const activeHeading = ref('');
const activeGroup = ref('');
const isManualScroll = ref(false);
let scrollTimeout: any;

const hasContent = computed(() => props.groups.length > 0);

const menuItems = computed(() => {
  const items: (TocItem & { groupKey: string })[] = [];
  props.groups.forEach(group => {
    // Add group itself if it acts as an item (e.g. has an ID and no items or we want to track it)
    // But usually we track the items inside.
    // If a group has an ID, we might want to track it too?
    // Let's follow ScenicDetail logic: flattened list of all trackable items.
    
    // Actually ScenicDetail constructed menuItems separately.
    // Here we derive it from groups.
    
    // If group has items, track items.
    if (group.items.length > 0) {
      group.items.forEach(item => {
        items.push({ ...item, groupKey: group.key });
      });
    } else if (group.id) {
      // If group has no items but has an ID, track the group itself as an item
      items.push({ id: group.id, title: group.title, level: 1, groupKey: group.key });
    }
  });
  return items;
});

// Initialize active group
watch(() => props.groups, (newGroups) => {
  if (newGroups.length > 0 && !activeGroup.value) {
    activeGroup.value = newGroups[0].key;
  }
}, { immediate: true });

const handleGroupClick = (group: TocGroup) => {
  activeGroup.value = group.key;
  if (group.id && group.id !== 'intro-start') {
    scrollToHeading(group.id);
  } else if (group.items.length > 0) {
    scrollToHeading(group.items[0].id);
  }
};

const scrollToHeading = (id: string) => {
  const element = document.getElementById(id);
  if (element) {
    const offset = 100;
    const elementPosition = element.getBoundingClientRect().top;
    const offsetPosition = elementPosition + window.pageYOffset - offset;
    
    isManualScroll.value = true;
    if (scrollTimeout) clearTimeout(scrollTimeout);
    
    window.scrollTo({
      top: offsetPosition,
      behavior: 'smooth'
    });
    activeHeading.value = id;
    
    // Update active group
    const item = menuItems.value.find(i => i.id === id);
    if (item) {
      activeGroup.value = item.groupKey;
    }

    scrollTimeout = setTimeout(() => {
      isManualScroll.value = false;
    }, 1000);
  }
};

const handleTocPosition = () => {
  const footer = document.querySelector('.footer');
  if (footer) {
    const footerRect = footer.getBoundingClientRect();
    const gap = 24;
    const topOffset = 100;
    
    const availableHeight = footerRect.top - gap - topOffset;
    const viewportConstraint = window.innerHeight - 200;
    
    const finalMaxHeight = Math.min(availableHeight, viewportConstraint);
    
    tocStyle.value = { 
      top: `${topOffset}px`,
      maxHeight: `${Math.max(100, finalMaxHeight)}px`
    };
  }
};

const onScroll = () => {
  if (menuItems.value.length === 0 || isManualScroll.value) return;
  
  const scrollY = window.scrollY;
  const innerHeight = window.innerHeight;
  const docHeight = document.documentElement.scrollHeight;
  
  // If at the bottom, force activate the last item
  if (scrollY + innerHeight >= docHeight - 50) {
    const lastItem = menuItems.value[menuItems.value.length - 1];
    if (lastItem) {
      activeHeading.value = lastItem.id;
      activeGroup.value = lastItem.groupKey;
      handleTocPosition();
      return;
    }
  }

  let currentActiveItem = null;
  
  // Find the item currently in view
  for (const item of menuItems.value) {
    const element = document.getElementById(item.id);
    if (element) {
      const rect = element.getBoundingClientRect();
      if (rect.top >= 0 && rect.top <= 180) {
        currentActiveItem = item;
        break;
      }
    }
  }

  // Fallback: look for item just above viewport
  if (!currentActiveItem) {
    for (let i = menuItems.value.length - 1; i >= 0; i--) {
      const item = menuItems.value[i];
      const element = document.getElementById(item.id);
      if (element) {
        const rect = element.getBoundingClientRect();
        if (rect.top < 0) {
          currentActiveItem = item;
          break;
        }
      }
    }
  }

  if (currentActiveItem) {
    activeHeading.value = currentActiveItem.id;
    activeGroup.value = currentActiveItem.groupKey;
  }

  handleTocPosition();
};

onMounted(() => {
  window.addEventListener('scroll', onScroll);
  // Initial position check
  handleTocPosition();
});

onUnmounted(() => {
  window.removeEventListener('scroll', onScroll);
});
</script>

<style scoped>
.toc-wrapper {
  position: fixed;
  top: 100px;
  width: 280px;
  background: #fff;
  padding: 16px;
  border-radius: 8px;
  border: 1px solid #ebeef5;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  max-height: calc(100vh - 120px);
  margin-bottom: 24px;
  z-index: 1000;
  display: flex;
  flex-direction: column;
}

.toc-content {
  display: flex;
  flex-direction: column;
  overflow: hidden;
  flex: 1;
  min-height: 0;
}

.toc-group {
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
  border-bottom: none;
  transition: flex-grow 0.3s ease;
  overflow: hidden;
}

.toc-group:last-child {
  border-bottom: none;
}

.toc-group.active {
  flex: 1;
  min-height: 0;
}

.group-content {
  overflow-y: auto;
  min-height: 0;
  /* Hide scrollbar for nicer look but keep functionality */
  scrollbar-width: thin;
  scrollbar-color: #dcdfe6 transparent;
  
  height: 0;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.toc-group.active .group-content {
  flex: 1;
  height: auto;
  opacity: 1;
}

.group-content::-webkit-scrollbar {
  width: 4px;
}

.group-content::-webkit-scrollbar-thumb {
  background: #dcdfe6;
  border-radius: 2px;
}

.group-content::-webkit-scrollbar-track {
  background: transparent;
}

.group-title {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  padding: 10px 8px;
  margin-bottom: 0;
  cursor: pointer;
  background: #fff;
  z-index: 10;
  border-bottom: 1px dashed #ebeef5;
  transition: background-color 0.2s;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-shrink: 0;
}

.group-title:hover {
  background-color: #f5f7fa;
  color: var(--el-color-primary);
}

.toc-group:not(.active) .group-title {
  border-bottom: 1px solid #ebeef5;
}

/* Indicator icon */
.group-title::after {
  content: '';
  width: 0;
  height: 0;
  border-left: 5px solid transparent;
  border-right: 5px solid transparent;
  border-top: 5px solid #909399;
  transform: rotate(0deg);
  transition: transform 0.3s;
}

.toc-group:not(.active) .group-title::after {
  transform: rotate(-90deg);
}

.toc-list {
  list-style: none;
  padding: 8px 0;
  margin: 0;
}

.toc-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 12px;
  padding-left: 8px;
  border-left: 4px solid var(--el-color-primary);
  flex-shrink: 0;
}

.toc-item {
  padding: 8px 12px;
  cursor: pointer;
  color: #606266;
  font-size: 14px;
  border-radius: 4px;
  transition: all 0.2s;
  margin-bottom: 4px;
}

.toc-item:hover {
  background-color: #f5f7fa;
  color: var(--el-color-primary);
}

.toc-item.active {
  background-color: var(--el-color-primary-light-9);
  color: var(--el-color-primary);
  font-weight: 500;
}

.toc-item.level-2 {
  padding-left: 12px;
}

.toc-item.level-3 {
  padding-left: 24px;
  font-size: 13px;
}
</style>