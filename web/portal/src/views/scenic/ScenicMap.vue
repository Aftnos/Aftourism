<template>
  <div class="map-page" :style="{ gridTemplateColumns: isSidebarOpen ? '320px 1fr' : '0px 1fr' }">
    <aside class="sidebar" :class="{ collapsed: !isSidebarOpen }">
      <div class="sidebar-header">
        <h1>景区分布地图</h1>
        <p>西藏全境景区分布概览，点击地图点位直达详情。</p>
        <div class="search-row">
          <el-input v-model="keyword" placeholder="搜索景区名称" clearable />
          <el-button type="primary" @click="fitBounds">全景</el-button>
        </div>
        <div class="count">当前展示：{{ filteredList.length }} / {{ list.length }} 个景区</div>
      </div>

      <el-scrollbar class="list">
        <div
          v-for="item in filteredList"
          :key="item.id"
          class="list-item"
          :class="{ active: activeId === item.id }"
          @click="handleItemClick(item)"
        >
          <div class="name">{{ item.name }}</div>
          <div class="meta">
            <span>{{ item.level || '普通景区' }}</span>
            <span v-if="item.longitude && item.latitude">
              {{ item.latitude.toFixed(2) }}, {{ item.longitude.toFixed(2) }}
            </span>
            <span v-else class="muted">无坐标</span>
          </div>
        </div>
        <div v-if="filteredList.length === 0" class="empty">
          <el-empty description="无匹配数据" />
        </div>
      </el-scrollbar>
    </aside>

    <main id="scenic-map" class="map-box">
      <button class="sidebar-toggle" type="button" @click="toggleSidebar">
        <el-icon>
          <ArrowLeft v-if="isSidebarOpen" />
          <ArrowRight v-else />
        </el-icon>
      </button>
    </main>
  </div>
</template>

<script setup lang="ts">
import { computed, nextTick, onMounted, onUnmounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ArrowLeft, ArrowRight } from '@element-plus/icons-vue';
import { fetchScenicPage, type ScenicItem } from '@/services/portal';
import { loadLeaflet, type LeafletLayerGroup, type LeafletMap, type LeafletTileLayer, type LeafletCircleMarker } from '@/utils/leaflet';

defineOptions({ name: 'ScenicMap' });

interface MapItem extends ScenicItem {
  id: number;
  latitude?: number;
  longitude?: number;
}

const router = useRouter();
const map = ref<LeafletMap | null>(null);
const tileLayer = ref<LeafletTileLayer | null>(null);
const list = ref<MapItem[]>([]);
const keyword = ref('');
const activeId = ref<number | null>(null);
const markerMap = new Map<number, LeafletCircleMarker>();
const layerGroup = ref<LeafletLayerGroup | null>(null);
const isSidebarOpen = ref(window.innerWidth > 768);

const filteredList = computed(() => {
  const k = keyword.value.trim().toLowerCase();
  if (!k) return list.value;
  return list.value.filter((item) => (item.name || '').toLowerCase().includes(k));
});

const toggleSidebar = () => {
  isSidebarOpen.value = !isSidebarOpen.value;
  setTimeout(() => {
    map.value?.invalidateSize();
  }, 320);
};

const initMap = async () => {
  if (map.value) return;
  const L = await loadLeaflet();
  map.value = L.map('scenic-map', { preferCanvas: true }).setView([29.65, 91.11], 7);

  tileLayer.value = L.tileLayer('https://{s}.basemaps.cartocdn.com/rastertiles/light_all/{z}/{x}/{y}{r}.png', {
    subdomains: 'abcd',
    maxZoom: 20,
    attribution: '&copy; OpenStreetMap &copy; CARTO'
  }).addTo(map.value);

  layerGroup.value = L.layerGroup().addTo(map.value);
};

const loadData = async () => {
  const res = await fetchScenicPage({ current: 1, size: 1000 });
  list.value = (res.list || []).filter((item) => item.latitude && item.longitude) as MapItem[];
  await renderMarkers();
};

const renderMarkers = async () => {
  if (!layerGroup.value || !map.value) return;
  const L = await loadLeaflet();
  layerGroup.value.clearLayers();
  markerMap.clear();

  list.value.forEach((item) => {
    if (!item.latitude || !item.longitude) return;

    L.circleMarker([item.latitude, item.longitude], {
      radius: 18,
      stroke: false,
      fillColor: '#2FA7FF',
      fillOpacity: 0.18
    }).addTo(layerGroup.value!);

    const dot = L.circleMarker([item.latitude, item.longitude], {
      radius: 7.5,
      color: '#FFFFFF',
      weight: 2,
      fillColor: '#2FA7FF',
      fillOpacity: 1
    }).addTo(layerGroup.value!);

    const summaryText = item.tags || '暂无标签';
    const popupContent = `
      <div class="map-popup">
        <div class="name">${item.name}</div>
        <div class="meta">等级：${item.level || '-'} </div>
        <div class="desc">${summaryText}</div>
        <div class="address">地址：${item.address || '-'}</div>
      </div>
    `;
    dot.bindPopup(popupContent, { maxWidth: 300 });

    dot.on('click', () => {
      setActive(item.id);
      scrollToItem(item.id);
      router.push(`/scenic/${item.id}`);
    });
    dot.on('mouseover', () => dot.setStyle({ radius: 8.5 }));
    dot.on('mouseout', () => dot.setStyle({ radius: 7.5 }));

    markerMap.set(item.id, dot);
  });

  fitBounds();
};

const setActive = (id: number) => {
  if (activeId.value && markerMap.has(activeId.value)) {
    markerMap.get(activeId.value)!.setStyle({ fillColor: '#2FA7FF' });
  }
  activeId.value = id;
  if (markerMap.has(id)) {
    markerMap.get(id)!.setStyle({ fillColor: '#00C2FF' });
  }
};

const handleItemClick = (item: MapItem) => {
  setActive(item.id);
  if (item.latitude && item.longitude && map.value) {
    map.value.flyTo([item.latitude, item.longitude], 10, { duration: 0.6 });
    const marker = markerMap.get(item.id);
    if (marker) marker.openPopup();
  }
};

const fitBounds = () => {
  if (!map.value || list.value.length === 0) return;
  const latLngs = list.value.map((item) => [item.latitude!, item.longitude!] as [number, number]);
  map.value.fitBounds(latLngs, { padding: [50, 50] });
};

const scrollToItem = (id: number) => {
  nextTick(() => {
    const el = document.querySelector('.list-item.active');
    if (el) el.scrollIntoView({ behavior: 'smooth', block: 'center' });
  });
};

onMounted(async () => {
  await initMap();
  await loadData();
});

onUnmounted(() => {
  if (map.value) {
    map.value.remove();
    map.value = null;
  }
});
</script>

<style scoped lang="scss">
.map-page {
  height: calc(100vh - 180px);
  min-height: 560px;
  display: grid;
  background: #ffffff;
  border-radius: 16px;
  overflow: hidden;
  margin: 24px;
  border: 1px solid #ebeef5;
  transition: grid-template-columns 0.3s ease;
  position: relative;
}

.sidebar {
  border-right: 1px solid #ebeef5;
  background: #fff;
  transition: opacity 0.2s ease;
  opacity: 1;
}

.sidebar.collapsed {
  opacity: 0;
  pointer-events: none;
}

.sidebar-header {
  padding: 16px;
  border-bottom: 1px solid #ebeef5;
  background: #f9fafc;
}

.sidebar-header h1 {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 6px;
  color: #303133;
}

.sidebar-header p {
  margin: 0 0 12px;
  font-size: 12px;
  color: #909399;
}

.search-row {
  display: grid;
  grid-template-columns: 1fr auto;
  gap: 10px;
}

.count {
  margin-top: 10px;
  font-size: 12px;
  color: #909399;
}

.list {
  padding: 12px;
}

.list-item {
  padding: 10px;
  border-radius: 10px;
  border: 1px solid #ebeef5;
  background: #fff;
  margin-bottom: 10px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.list-item:hover {
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
  transform: translateY(-2px);
}

.list-item.active {
  border-color: #409eff;
}

.name {
  font-weight: 600;
  font-size: 14px;
  color: #303133;
}

.meta {
  margin-top: 6px;
  font-size: 12px;
  color: #606266;
  display: flex;
  justify-content: space-between;
  gap: 6px;
  flex-wrap: wrap;
}

.muted {
  color: #c0c4cc;
}

.empty {
  padding: 24px 0;
}

.map-box {
  width: 100%;
  height: 100%;
  position: relative;
}

.sidebar-toggle {
  position: absolute;
  top: 24px;
  left: 12px;
  width: 36px;
  height: 36px;
  border-radius: 10px;
  border: 1px solid #ebeef5;
  background: #fff;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
  color: #606266;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 400;
  transition: all 0.2s ease;
}

.sidebar-toggle:hover {
  color: #409eff;
  border-color: #c6e2ff;
}

:deep(.map-popup) .name {
  font-weight: 600;
  margin-bottom: 4px;
  font-size: 14px;
  color: #303133;
}

:deep(.map-popup) .meta,
:deep(.map-popup) .address {
  font-size: 12px;
  color: #909399;
}

:deep(.map-popup) .desc {
  font-size: 12px;
  color: #606266;
  margin: 6px 0;
  line-height: 1.4;
}

@media (max-width: 768px) {
  .map-page {
    display: block;
    height: calc(100vh - 140px);
    margin: 12px;
  }

  .sidebar {
    position: absolute;
    top: 0;
    left: 0;
    bottom: 0;
    width: 260px;
    transform: translateX(0);
    transition: transform 0.3s ease;
    z-index: 300;
  }

  .sidebar.collapsed {
    transform: translateX(-100%);
  }
}
</style>
