<template>
  <div class="app-map-container bg-box border-full-d rounded-custom-sm">
    <aside class="sidebar bg-box border-r-d p-3 overflow-auto">
      <div class="header bg-box border-full-d rounded-custom-sm shadow tad-300 p-3 mb-3">
        <h1 class="title text-g-900">景区分布地图</h1>
        <p class="hint text-g-500">
          西藏全境景区分布概览。点击列表或地图点位查看详情。
        </p>
        <div class="row flex-cb gap-2">
          <ElInput class="w-full" v-model="keyword" placeholder="搜索景区名称" clearable />
          <ElButton type="primary" @click="fitBounds" class="tad-200">全景</ElButton>
        </div>
        <div class="count text-g-500">当前展示：{{ filteredList.length }} / {{ list.length }} 个景区</div>
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
            <span>{{ item.level || '普通景区' }}</span>
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

    <main id="scenic-map" class="map-box bg-box border-l-d"></main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed, nextTick, watch } from 'vue'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'
import { fetchGetScenicPage } from '@/api/scenic'
import { useSettingStore } from '@/store/modules/setting'
import { storeToRefs } from 'pinia'
import { ElInput, ElButton, ElScrollbar, ElEmpty } from 'element-plus'

defineOptions({ name: 'DashboardScenicMap' })

const settingStore = useSettingStore()
const { isDark } = storeToRefs(settingStore)

interface MapItem {
  id: number
  name: string
  level?: string
  latitude?: number
  longitude?: number
  intro?: string
  address?: string
  imageUrl?: string
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
  map.value = L.map('scenic-map', { preferCanvas: true }).setView([29.65, 91.11], 7)

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
    // 获取所有数据（分页取大一点，或者循环取全量，这里演示取前1000条）
    const res = await fetchGetScenicPage({ current: 1, size: 1000 })
    if (res && res.list) {
      list.value = res.list.filter(item => item.latitude && item.longitude) as MapItem[] // 仅展示有坐标的点
      renderMarkers()
    }
  } catch (err) {
    console.error('Fetch scenic data failed:', err)
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
      fillColor: '#2FA7FF',
      fillOpacity: 0.18
    }).addTo(layerGroup.value!)

    // 实体点
    const dot = L.circleMarker([item.latitude, item.longitude], {
      radius: 7.5,
      color: '#FFFFFF',
      weight: 2,
      fillColor: '#2FA7FF',
      fillOpacity: 1
    }).addTo(layerGroup.value!)

    // 弹窗
    const popupContent = `
      <div class="map-popup">
        <div class="name">${item.name}</div>
        <div class="meta">等级：${item.level || '-'}</div>
        <div class="desc">${(item.intro || '').slice(0, 50)}...</div>
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
  // 恢复旧的高亮
  if (activeId.value && markerMap.has(activeId.value)) {
    markerMap.get(activeId.value)!.setStyle({ fillColor: '#2FA7FF' })
  }
  
  activeId.value = id
  
  // 设置新的高亮
  if (markerMap.has(id)) {
    markerMap.get(id)!.setStyle({ fillColor: '#00C2FF' })
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
  // 搜索时不需要额外操作，computed会自动更新列表
  // 如果想地图上也只显示搜索结果，可以在这里重新调用 renderMarkers(filteredList.value)
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
  height: calc(100vh - 120px); /* 减去顶部导航高度 */
  display: grid;
  grid-template-columns: 360px 1fr;
  overflow: hidden;
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
