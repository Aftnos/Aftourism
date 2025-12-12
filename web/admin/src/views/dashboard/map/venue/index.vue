<template>
  <div class="app-map-container bg-box border-full-d rounded-custom-sm">
    <aside class="sidebar bg-box border-r-d p-3 overflow-auto">
      <div class="header bg-box border-full-d rounded-custom-sm shadow tad-300 p-3 mb-3">
        <h1 class="title text-g-900">场馆分布地图</h1>
        <p class="hint text-g-500">
          西藏全境场馆分布概览。点击列表或地图点位查看详情。
        </p>
        <div class="row flex-cb gap-2">
          <ElInput class="w-full" v-model="keyword" placeholder="搜索场馆名称" clearable />
          <ElButton type="primary" @click="fitBounds" class="tad-200">全景</ElButton>
        </div>
        <div class="count text-g-500">当前展示：{{ filteredList.length }} / {{ list.length }} 个场馆</div>
      </div>

      <ElScrollbar class="list grid gap-2">
        <div
          v-for="item in filteredList"
          :key="item.id"
          class="item bg-box border-full-d rounded-custom-sm tad-200 c-p hover:bg-hover-color"
          :class="{ active: activeId === item.id }"
          @click="handleItemClick(item)"
        >
          <div class="name text-g-900">{{ item.name }}</div>
          <div class="meta text-g-500 flex-cb">
            <span>{{ item.category || '综合场馆' }}</span>
            <span v-if="item.longitude && item.latitude">
              {{ item.latitude.toFixed(2) }}, {{ item.longitude.toFixed(2) }}
            </span>
            <span v-else class="text-g-300">无坐标</span>
          </div>
        </div>
        <template v-if="filteredList.length === 0">
          <div class="p-3">
            <ElEmpty description="无匹配数据" />
          </div>
        </template>
      </ElScrollbar>
    </aside>

    <main id="venue-map" class="map-box bg-box relative">
      <!-- 侧边栏折叠按钮 -->
      <div class="sidebar-toggle bg-box shadow rounded-custom-xs flex-c justify-center c-p tad-200" @click="toggleSidebar">
        <i :class="isSidebarOpen ? 'ri-arrow-left-s-line' : 'ri-arrow-right-s-line'"></i>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed, nextTick, watch } from 'vue'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'
import { fetchGetVenuePage } from '@/api/venue'
import { useSettingStore } from '@/store/modules/setting'
import { storeToRefs } from 'pinia'
import { ElInput, ElButton, ElScrollbar, ElEmpty } from 'element-plus'

defineOptions({ name: 'DashboardVenueMap' })

const settingStore = useSettingStore()
const { isDark } = storeToRefs(settingStore)

const isSidebarOpen = ref(window.innerWidth > 768)

const toggleSidebar = () => {
  isSidebarOpen.value = !isSidebarOpen.value
  setTimeout(() => {
    map.value?.invalidateSize()
  }, 350)
}

interface MapItem {
  id: number
  name: string
  category?: string
  latitude?: number
  longitude?: number
  description?: string
  address?: string
  imageUrl?: string
  isFree?: number
}

const map = ref<L.Map | null>(null)
const tileLayer = ref<L.TileLayer | null>(null)
const list = ref<MapItem[]>([])
const keyword = ref('')
const activeId = ref<number | null>(null)
const markerMap = new Map<number, L.CircleMarker>()
const layerGroup = ref<L.LayerGroup | null>(null)

// 过滤列表
const filteredList = computed(() => {
  const k = keyword.value.trim().toLowerCase()
  if (!k) return list.value
  return list.value.filter(i => (i.name || '').toLowerCase().includes(k))
})

// 监听主题变化切换底图
watch(isDark, (val) => {
  if (!tileLayer.value) return
  const url = val 
    ? 'https://{s}.basemaps.cartocdn.com/rastertiles/dark_all/{z}/{x}/{y}{r}.png'
    : 'https://{s}.basemaps.cartocdn.com/rastertiles/light_all/{z}/{x}/{y}{r}.png'
  tileLayer.value.setUrl(url)
}, { immediate: true })

const initMap = () => {
  if (map.value) return
  
  // 初始化地图，默认中心设为拉萨附近
  map.value = L.map('venue-map', { preferCanvas: true }).setView([29.65, 91.11], 7)

  // 底图
  const url = isDark.value
    ? 'https://{s}.basemaps.cartocdn.com/rastertiles/dark_all/{z}/{x}/{y}{r}.png'
    : 'https://{s}.basemaps.cartocdn.com/rastertiles/light_all/{z}/{x}/{y}{r}.png'
    
  tileLayer.value = L.tileLayer(url, {
    subdomains: 'abcd',
    maxZoom: 20,
    attribution: '&copy; OpenStreetMap &copy; CARTO'
  }).addTo(map.value)

  layerGroup.value = L.layerGroup().addTo(map.value)
}

const loadData = async () => {
  try {
    // 获取所有数据
    const res = await fetchGetVenuePage({ current: 1, size: 1000 })
    if (res && res.list) {
      list.value = res.list.filter(item => item.latitude && item.longitude) as MapItem[]
      renderMarkers()
    }
  } catch (err) {
    console.error('Fetch venue data failed:', err)
  }
}

const renderMarkers = () => {
  if (!layerGroup.value || !map.value) return
  layerGroup.value.clearLayers()
  markerMap.clear()

  list.value.forEach(item => {
    if (!item.latitude || !item.longitude) return

    // 光晕
    L.circleMarker([item.latitude, item.longitude], {
      radius: 18,
      stroke: false,
      fillColor: '#FF9F2F', // 场馆使用橙色系区分
      fillOpacity: 0.18
    }).addTo(layerGroup.value!)

    // 实体点
    const dot = L.circleMarker([item.latitude, item.longitude], {
      radius: 7.5,
      color: '#FFFFFF',
      weight: 2,
      fillColor: '#FF9F2F',
      fillOpacity: 1
    }).addTo(layerGroup.value!)

    // 弹窗
    const popupContent = `
      <div class="map-popup">
        <div class="name">${item.name}</div>
        <div class="meta">类型：${item.category || '-'} <span style="margin-left:8px;color:${item.isFree === 1 ? 'green' : 'orange'}">${item.isFree === 1 ? '免费' : '收费'}</span></div>
        <div class="desc">${(item.description || '').slice(0, 50)}...</div>
        <div class="address">地址：${item.address || '-'}</div>
      </div>
    `
    dot.bindPopup(popupContent, { maxWidth: 300 })

    // 事件
    dot.on('click', () => {
      setActive(item.id)
      scrollToItem(item.id)
    })
    dot.on('mouseover', () => dot.setStyle({ radius: 8.5 }))
    dot.on('mouseout', () => dot.setStyle({ radius: 7.5 }))

    markerMap.set(item.id, dot)
  })

  fitBounds()
}

const setActive = (id: number) => {
  if (activeId.value && markerMap.has(activeId.value)) {
    markerMap.get(activeId.value)!.setStyle({ fillColor: '#FF9F2F' })
  }
  
  activeId.value = id
  
  if (markerMap.has(id)) {
    markerMap.get(id)!.setStyle({ fillColor: '#FF7F00' })
  }
}

const handleItemClick = (item: MapItem) => {
  setActive(item.id)
  if (item.latitude && item.longitude && map.value) {
    map.value.flyTo([item.latitude, item.longitude], 10, { duration: 0.6 })
    const marker = markerMap.get(item.id)
    if (marker) marker.openPopup()
  }
}

const handleSearch = () => {
  // computed自动处理
}

const fitBounds = () => {
  if (!map.value || list.value.length === 0) return
  const latLngs = list.value.map(i => [i.latitude!, i.longitude!] as [number, number])
  map.value.fitBounds(latLngs, { padding: [50, 50] })
}

const scrollToItem = (id: number) => {
  nextTick(() => {
    const el = document.querySelector(`.item.active`)
    if (el) el.scrollIntoView({ behavior: 'smooth', block: 'center' })
  })
}

onMounted(async () => {
  initMap()
  await loadData()
})

onUnmounted(() => {
  if (map.value) {
    map.value.remove()
    map.value = null
  }
})
</script>

<style scoped>
.app-map-container {
  height: calc(100vh - 120px);
  display: grid;
  grid-template-columns: v-bind(isSidebarOpen ? '360px 1fr' : '0px 1fr');
  overflow: hidden;
  transition: grid-template-columns 0.3s ease;
  position: relative;
}

.sidebar {
  min-width: 360px;
  transition: opacity 0.2s;
  opacity: v-bind(isSidebarOpen ? 1 : 0);
}

.sidebar-toggle {
  position: absolute;
  top: 80px;
  left: 12px;
  width: 32px;
  height: 32px;
  z-index: 999;
  font-size: 18px;
  color: var(--art-gray-600);
  border: 1px solid var(--default-border);
}

.sidebar-toggle:hover {
  background-color: var(--art-hover-color);
  color: var(--art-primary);
}

@media (max-width: 768px) {
  .app-map-container {
    display: block;
  }
  
  .sidebar {
    position: absolute;
    top: 0;
    left: 0;
    bottom: 0;
    z-index: 1000;
    width: 280px;
    min-width: 280px;
    transform: translateX(v-bind(isSidebarOpen ? '0' : '-100%'));
    transition: transform 0.3s ease;
    border-right: 1px solid var(--default-border);
  }
}

.title { font-size: 16px; font-weight: 800; margin: 0 0 6px; letter-spacing: .2px; }
.hint { font-size: 12px; margin: 0; line-height: 1.45; }

.row { display: grid; grid-template-columns: 1fr auto; gap: 10px; margin: 12px 0 10px; }

.count { font-size: 12px; margin: 8px 2px 0; }

.list {
  list-style: none;
  padding: 0;
  margin: 0;
  align-content: start;
}

.item {
  padding: 10px;
}
.item:hover {
  transform: translateY(-1px);
}
.item.active {
  border-color: var(--art-primary);
}
.item .name { font-weight: 900; font-size: 14px; margin-bottom: 4px; }
.item .meta { font-size: 12px; }

.map-box { height: 100%; width: 100%; z-index: 1; }

:deep(.map-popup) .name { font-weight: 900; margin-bottom: 4px; font-size: 14px; color: var(--art-gray-900); }
:deep(.map-popup) .meta { font-size: 12px; color: var(--art-gray-500); margin-bottom: 6px; }
:deep(.map-popup) .desc { font-size: 12px; color: var(--art-gray-900); margin-bottom: 8px; line-height: 1.4; }
:deep(.map-popup) .address { font-size: 12px; color: var(--art-gray-500); }

/* 适配暗色模式的 Popup */
:global(.leaflet-popup-content-wrapper), :global(.leaflet-popup-tip) {
  background: var(--default-box-color);
  color: var(--art-gray-900);
  border: 1px solid var(--default-border);
}
</style>
