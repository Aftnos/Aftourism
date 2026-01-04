<template>
  <div class="flex gap-2 w-full">
    <ElSelect
      v-model="province"
      placeholder="省份"
      filterable
      clearable
      class="flex-1"
      :loading="loading.province"
      @change="handleProvinceChange"
      @clear="handleProvinceClear"
    >
      <ElOption v-for="item in provinceList" :key="item" :label="item" :value="item" />
    </ElSelect>

    <ElSelect
      v-model="city"
      placeholder="城市"
      filterable
      clearable
      class="flex-1"
      :disabled="!province"
      :loading="loading.city"
      @change="handleCityChange"
      @clear="handleCityClear"
    >
      <ElOption v-for="item in cityList" :key="item" :label="item" :value="item" />
    </ElSelect>

    <ElSelect
      v-model="district"
      placeholder="区县"
      filterable
      clearable
      class="flex-1"
      :disabled="!city"
      :loading="loading.district"
      @change="handleDistrictChange"
    >
      <ElOption v-for="item in districtList" :key="item" :label="item" :value="item" />
    </ElSelect>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, watch, onMounted } from 'vue'
import { ElSelect, ElOption, ElMessage } from 'element-plus'
import { fetchAdminDivision, parseAdminDivisionMsg } from '@/api/admin-division'

const props = defineProps<{
  modelValue?: {
    province: string
    city: string
    district: string
  }
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: { province: string; city: string; district: string }): void
  (e: 'change', value: { province: string; city: string; district: string }): void
}>()

const province = ref('')
const city = ref('')
const district = ref('')

const provinceList = ref<string[]>([])
const cityList = ref<string[]>([])
const districtList = ref<string[]>([])

const loading = reactive({
  province: false,
  city: false,
  district: false
})

// 初始化加载省份
const loadProvinces = async () => {
  try {
    loading.province = true
    const res = await fetchAdminDivision({ type: 1 })
    if (res.code === 200) {
      provinceList.value = parseAdminDivisionMsg(res.msg)
    } else {
      ElMessage.warning(res.msg || '获取省份失败')
    }
  } catch (error) {
    console.error(error)
  } finally {
    loading.province = false
  }
}

const loadCities = async (prov: string) => {
  if (!prov) {
    cityList.value = []
    return
  }
  try {
    loading.city = true
    const res = await fetchAdminDivision({ type: 2, sheng: prov })
    if (res.code === 200) {
      cityList.value = parseAdminDivisionMsg(res.msg)
    }
  } catch (error) {
    console.error(error)
  } finally {
    loading.city = false
  }
}

const loadDistricts = async (c: string) => {
  if (!c || !province.value) {
    districtList.value = []
    return
  }
  try {
    loading.district = true
    const res = await fetchAdminDivision({ type: 3, sheng: province.value, shi: c })
    if (res.code === 200) {
      districtList.value = parseAdminDivisionMsg(res.msg)
    }
  } catch (error) {
    console.error(error)
  } finally {
    loading.district = false
  }
}

const handleProvinceChange = () => {
  city.value = ''
  district.value = ''
  cityList.value = []
  districtList.value = []
  emitUpdate()
  if (province.value) {
    loadCities(province.value)
  }
}

const handleProvinceClear = () => {
  province.value = ''
  handleProvinceChange()
}

const handleCityChange = () => {
  district.value = ''
  districtList.value = []
  emitUpdate()
  if (city.value) {
    loadDistricts(city.value)
  }
}

const handleCityClear = () => {
  city.value = ''
  handleCityChange()
}

const handleDistrictChange = () => {
  emitUpdate()
}

const emitUpdate = () => {
  const val = {
    province: province.value,
    city: city.value,
    district: district.value
  }
  emit('update:modelValue', val)
  emit('change', val)
}

// 监听外部 modelValue 变化（例如回显）
watch(() => props.modelValue, async (val) => {
  if (val) {
    if (val.province !== province.value) {
      province.value = val.province
      if (province.value) await loadCities(province.value)
    }
    if (val.city !== city.value) {
      city.value = val.city
      if (city.value) await loadDistricts(city.value)
    }
    if (val.district !== district.value) {
      district.value = val.district
    }
  }
}, { immediate: true, deep: true })

onMounted(() => {
  loadProvinces()
})
</script>
