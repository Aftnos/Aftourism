<template>
  <div class="map-page" :class="{ 'sidebar-closed': !isSidebarOpen }">
    <aside class="sidebar">
      <div class="sidebar-header">
        <div class="header-top">
          <h1>场馆分布地图</h1>
          <div class="count-badge">{{ filteredList.length }}</div>
        </div>
        <p>西藏全境场馆分布概览，点击地图点位直达详情。</p>
        <div class="search-row">
          <el-input 
            v-model="keyword" 
            placeholder="搜索场馆名称" 
            clearable 
            prefix-icon="Search"
          />
          <el-button type="primary" plain @click="fitBounds">全景</el-button>
        </div>
      </div>

      <el-scrollbar ref="listScrollbar" class="list">
        <div
          v-for="item in filteredList"
          :key="item.id"
          class="list-item"
          :class="{ active: activeId === item.id }"
          @click="handleItemClick(item)"
        >
          <div class="item-main">
            <div class="name">{{ item.name }}</div>
            <el-tag size="small" :type="item.isFree === 1 ? 'success' : 'warning'" effect="plain">
              {{ item.isFree === 1 ? '免费' : '收费' }}
            </el-tag>
          </div>
          <div class="meta">
            <span>{{ item.category || '综合场馆' }}</span>
            <span class="coord" v-if="item.longitude && item.latitude">
              <el-icon><Location /></el-icon>
              {{ item.latitude.toFixed(2) }}, {{ item.longitude.toFixed(2) }}
            </span>
            <span v-else class="muted">暂无坐标</span>
          </div>
        </div>
        <div v-if="filteredList.length === 0" class="empty">
          <el-empty description="未找到匹配场馆" :image-size="100" />
        </div>
      </el-scrollbar>
    </aside>

    <main id="venue-map" class="map-box">
    </main>

    <div class="map-controls">
      <button class="control-btn toggle-btn" type="button" @click="toggleSidebar" title="切换列表">
        <el-icon size="18">
          <ArrowLeft v-if="isSidebarOpen" />
          <ArrowRight v-else />
        </el-icon>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, nextTick, onMounted, onUnmounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ArrowLeft, ArrowRight, Location, Search } from '@element-plus/icons-vue';
import { fetchVenuePage, type VenueItem } from '@/services/portal';
import { loadLeaflet, type LeafletLayerGroup, type LeafletMap, type LeafletTileLayer, type LeafletCircleMarker } from '@/utils/leaflet';

defineOptions({ name: 'VenueMap' });

interface MapItem extends VenueItem {
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
const listScrollbar = ref<any>(null);

const filteredList = computed(() => {
  const k = keyword.value.trim().toLowerCase();
  if (!k) return list.value;
  return list.value.filter((item) => (item.name || '').toLowerCase().includes(k));
});

const toggleSidebar = () => {
  isSidebarOpen.value = !isSidebarOpen.value;
  setTimeout(() => {
    map.value?.invalidateSize();
  }, 300);
};

const initMap = async () => {
  if (map.value) return;
  const L = await loadLeaflet();
  map.value = L.map('venue-map', { 
    preferCanvas: true,
    zoomControl: false 
  }).setView([29.65, 91.11], 7);

  L.control.zoom({ position: 'topright' }).addTo(map.value);

  tileLayer.value = L.tileLayer('https://{s}.basemaps.cartocdn.com/rastertiles/light_all/{z}/{x}/{y}{r}.png', {
    subdomains: 'abcd',
    maxZoom: 20,
    attribution: '&copy; OpenStreetMap &copy; CARTO'
  }).addTo(map.value);

  layerGroup.value = L.layerGroup().addTo(map.value);

  // Event delegation for popup buttons
  const mapContainer = map.value.getContainer();
  mapContainer.addEventListener('click', (e) => {
    const target = e.target as HTMLElement;
    if (target.closest('.go-detail-btn')) {
      const btn = target.closest('.go-detail-btn') as HTMLElement;
      const id = btn.getAttribute('data-id');
      if (id) {
        router.push(`/venues/${id}`);
      }
    }
  });
};

const loadData = async () => {
  try {
    const res = await fetchVenuePage({ current: 1, size: 1000 });
    list.value = (res.list || []).filter((item) => item.latitude && item.longitude) as MapItem[];
    await renderMarkers();
  } catch (error) {
    console.error('Failed to load venue data:', error);
  }
};

const renderMarkers = async () => {
  if (!layerGroup.value || !map.value) return;
  const L = await loadLeaflet();
  layerGroup.value.clearLayers();
  markerMap.clear();

  list.value.forEach((item) => {
    if (!item.latitude || !item.longitude) return;

    // Halo
    L.circleMarker([item.latitude, item.longitude], {
      radius: 18,
      stroke: false,
      fillColor: '#FF9F2F',
      fillOpacity: 0.15,
      className: 'marker-halo'
    }).addTo(layerGroup.value!);

    // Dot
    const dot = L.circleMarker([item.latitude, item.longitude], {
      radius: 7,
      color: '#FFFFFF',
      weight: 2,
      fillColor: '#FF9F2F',
      fillOpacity: 1,
      className: 'marker-core'
    }).addTo(layerGroup.value!);

    const summaryText = item.tags ? item.tags.split(',').slice(0, 3).join(' · ') : '暂无标签';
    const isFreeTag = item.isFree === 1 ? '<span class="tag-free">免费</span>' : '<span class="tag-paid">收费</span>';
    const imageHtml = item.imageUrl ? `<div class="popup-cover" style="background-image: url('${item.imageUrl}')"></div>` : '';
    
    const popupContent = `
      <div class="map-popup-card venue-popup">
        ${imageHtml}
        <div class="popup-header">
          <h3>${item.name}</h3>
          ${isFreeTag}
        </div>
        <div class="popup-body">
          <div class="meta-row">
            <span class="category">${item.category || '综合'}</span>
          </div>
          <p class="desc">${summaryText}</p>
          <p class="address"><i class="iconfont icon-location"></i> ${item.address || '暂无地址信息'}</p>
        </div>
        <div class="popup-footer">
          <button class="go-detail-btn" data-id="${item.id}">
            查看详情 <svg viewBox="0 0 1024 1024" width="12" height="12"><path d="M338.752 104.704a64 64 0 0 0 0 90.496l316.8 316.8-316.8 316.8a64 64 0 0 0 90.496 90.496l362.048-362.048a64 64 0 0 0 0-90.496L429.248 104.704a64 64 0 0 0-90.496 0z" fill="currentColor"/></svg>
          </button>
        </div>
      </div>
    `;
    
    dot.bindPopup(popupContent, { 
      maxWidth: 280,
      className: 'custom-leaflet-popup',
      closeButton: false
    });

    dot.on('click', () => {
      setActive(item.id);
      dot.openPopup();
    });
    
    dot.on('mouseover', () => {
      dot.setStyle({ radius: 9, fillColor: '#FF7F00' });
    });
    
    dot.on('mouseout', () => {
      if (activeId.value !== item.id) {
        dot.setStyle({ radius: 7, fillColor: '#FF9F2F' });
      }
    });

    markerMap.set(item.id, dot);
  });

  fitBounds();
};

const setActive = (id: number) => {
  if (activeId.value && markerMap.has(activeId.value)) {
    const prev = markerMap.get(activeId.value)!;
    prev.setStyle({ fillColor: '#FF9F2F', radius: 7 });
  }
  
  activeId.value = id;
  scrollToItem(id);
  
  if (markerMap.has(id)) {
    const curr = markerMap.get(id)!;
    curr.setStyle({ fillColor: '#d97706', radius: 9 });
  }
};

const handleItemClick = (item: MapItem) => {
  setActive(item.id);
  if (item.latitude && item.longitude && map.value) {
    map.value.setView([item.latitude, item.longitude], 12, { animate: true, duration: 0.8 });
    const marker = markerMap.get(item.id);
    if (marker) marker.openPopup();
  }
};

const fitBounds = () => {
  if (!map.value || list.value.length === 0) return;
  const latLngs = list.value.map((item) => [item.latitude!, item.longitude!] as [number, number]);
  map.value.fitBounds(latLngs, { padding: [50, 50], maxZoom: 12 });
};

const scrollToItem = (id: number) => {
  nextTick(() => {
    const itemEl = document.querySelector('.list-item.active') as HTMLElement | null;
    const wrapEl = document.querySelector('.list .el-scrollbar__wrap') as HTMLElement | null;
    if (itemEl && wrapEl) {
      const centerOffset = Math.max(0, (wrapEl.clientHeight - itemEl.clientHeight) / 2);
      const top = Math.max(0, itemEl.offsetTop - centerOffset);
      wrapEl.scrollTo({ top, behavior: 'smooth' });
    } else if (itemEl) {
      itemEl.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }
    if (listScrollbar.value?.setScrollTop && itemEl) {
      listScrollbar.value.setScrollTop(Math.max(0, itemEl.offsetTop - 60));
    }
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
  height: calc(100vh - 120px);
  min-height: 600px;
  display: flex;
  background: #ffffff;
  border-radius: 12px;
  overflow: hidden;
  margin: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  border: 1px solid #ebeef5;
  position: relative;
}

.sidebar {
  width: 340px;
  height: 100%;
  border-right: 1px solid #ebeef5;
  background: #fff;
  display: flex;
  flex-direction: column;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 10;
  flex-shrink: 0;
}

.map-page.sidebar-closed .sidebar {
  width: 0;
  opacity: 0;
  overflow: hidden;
  border-right: none;
}

.sidebar-header {
  padding: 20px;
  border-bottom: 1px solid #f0f2f5;
  background: #fff;
  
  .header-top {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 8px;
    
    h1 {
      font-size: 18px;
      font-weight: 700;
      color: #1a1a1a;
      margin: 0;
    }
    
    .count-badge {
      background: #fdf6ec;
      color: #e6a23c;
      font-size: 12px;
      padding: 2px 8px;
      border-radius: 10px;
      font-weight: 600;
    }
  }

  p {
    margin: 0 0 16px;
    font-size: 13px;
    color: #909399;
    line-height: 1.5;
  }
}

.search-row {
  display: flex;
  gap: 8px;
}

.list {
  flex: 1;
  padding: 12px;
  background: #f9fafc;
}

.list-item {
  padding: 12px;
  border-radius: 8px;
  background: #fff;
  margin-bottom: 10px;
  cursor: pointer;
  border: 1px solid transparent;
  transition: all 0.2s ease;
  box-shadow: 0 1px 3px rgba(0,0,0,0.02);

  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    transform: translateY(-1px);
    border-color: #fde2e2;
  }

  &.active {
    border-color: #e6a23c;
    background: #fdf6ec;
    
    .name {
      color: #d97706;
    }
  }
}

.item-main {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 6px;
  
  .name {
    font-weight: 600;
    font-size: 14px;
    color: #303133;
    line-height: 1.4;
    flex: 1;
    margin-right: 8px;
  }
}

.meta {
  font-size: 12px;
  color: #909399;
  display: flex;
  justify-content: space-between;
  align-items: center;
  
  .coord {
    display: flex;
    align-items: center;
    gap: 4px;
    
    .el-icon {
      font-size: 14px;
    }
  }
}

.map-box {
  flex: 1;
  height: 100%;
  position: relative;
  background: #f5f7fa;
  z-index: 1;
}

.map-controls {
  position: absolute;
  top: 20px;
  left: 20px;
  z-index: 1000;
}

.control-btn {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  border: none;
  background: #fff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  color: #606266;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
  
  &:hover {
    color: #e6a23c;
    background: #fdfdfd;
    transform: scale(1.05);
  }
  
  &:active {
    transform: scale(0.95);
  }
}

// Global styles for Leaflet Popup (must be deep or global)
:deep(.leaflet-popup-content-wrapper) {
  padding: 0;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 8px 24px rgba(0,0,0,0.15);
}

:deep(.leaflet-popup-content) {
  margin: 0;
  width: 100% !important;
}

:deep(.map-popup-card) {
  width: 260px;

  .popup-cover {
    height: 140px;
    background-size: cover;
    background-position: center;
    background-color: #f5f7fa;
  }

  &.venue-popup .popup-header {
    background: linear-gradient(135deg, #FF9F2F 0%, #ff7e00 100%);
  }
  
  .popup-header {
    padding: 12px 16px;
    color: #fff;
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    h3 {
      margin: 0;
      font-size: 15px;
      font-weight: 600;
      text-shadow: 0 1px 2px rgba(0,0,0,0.1);
    }
    
    .tag-free {
      background: rgba(255,255,255,0.2);
      padding: 2px 6px;
      border-radius: 4px;
      font-size: 11px;
    }
    .tag-paid {
      background: rgba(0,0,0,0.1);
      padding: 2px 6px;
      border-radius: 4px;
      font-size: 11px;
    }
  }
  
  .popup-body {
    padding: 12px 16px;
    
    .meta-row {
      margin-bottom: 6px;
      font-size: 12px;
      color: #e6a23c;
      font-weight: 600;
    }
    
    .desc {
      margin: 0 0 8px;
      font-size: 13px;
      color: #606266;
      line-height: 1.4;
    }
    
    .address {
      margin: 0;
      font-size: 12px;
      color: #909399;
      display: flex;
      align-items: flex-start;
      gap: 4px;
    }
  }
  
  .popup-footer {
    padding: 0 16px 16px;
    
    .go-detail-btn {
      width: 100%;
      padding: 8px 0;
      border: 1px solid #FF9F2F;
      background: #fff;
      color: #FF9F2F;
      border-radius: 4px;
      cursor: pointer;
      font-size: 13px;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 6px;
      transition: all 0.2s;
      
      &:hover {
        background: #fdf6ec;
      }
    }
  }
}

@media (max-width: 768px) {
  .map-page {
    height: calc(100vh - 100px);
    margin: 10px;
    display: block;
  }

  .sidebar {
    position: absolute;
    top: 0;
    left: 0;
    bottom: 0;
    width: 280px;
    transform: translateX(0);
    box-shadow: 4px 0 16px rgba(0,0,0,0.1);
  }

  .map-page.sidebar-closed .sidebar {
    width: 280px;
    transform: translateX(-100%);
    opacity: 1;
  }

  .map-controls {
    top: 10px;
    left: 10px;
  }

  .map-page:not(.sidebar-closed) .map-controls {
    left: 290px; // 280px sidebar width + 10px gap
  }
}
</style>
