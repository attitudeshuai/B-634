<template>
  <div>
    <h2 class="text-3xl font-bold text-gray-800 mb-6">校园活动</h2>
    
    <!-- Filter Tabs -->
    <div class="mb-6 flex flex-wrap gap-2">
      <button
        v-for="tab in tabs"
        :key="tab.value"
        @click="currentTab = tab.value"
        class="px-4 py-2 rounded-lg font-medium transition-all"
        :class="currentTab === tab.value 
          ? 'bg-primary text-white shadow-md' 
          : 'bg-white text-gray-700 hover:bg-gray-100 border border-gray-300'"
      >
        {{ tab.label }}
      </button>
    </div>
    
    <!-- Loading State -->
    <div v-if="loading" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div v-for="i in 6" :key="i" class="bg-white rounded-lg shadow-md p-6 animate-pulse">
        <div class="h-6 bg-gray-200 rounded mb-4"></div>
        <div class="h-4 bg-gray-200 rounded mb-2"></div>
        <div class="h-4 bg-gray-200 rounded w-2/3"></div>
      </div>
    </div>
    
    <!-- Activities Grid -->
    <div v-else-if="activities.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div
        v-for="activity in activities"
        :key="activity.id"
        class="bg-white rounded-lg shadow-md hover:shadow-xl transition-shadow cursor-pointer overflow-hidden"
        @click="viewDetail(activity.id)"
      >
        <div class="p-6">
          <div class="flex items-start justify-between mb-3">
            <h3 class="text-xl font-bold text-gray-800 flex-1 line-clamp-2">{{ activity.title }}</h3>
            <span 
              class="ml-2 px-2 py-1 text-xs rounded-full whitespace-nowrap"
              :class="getStatusColor(activity)"
            >
              {{ getStatusText(activity) }}
            </span>
          </div>
          
          <p class="text-gray-600 text-sm mb-4 line-clamp-3">{{ activity.description }}</p>
          
          <div class="space-y-2 text-sm text-gray-500">
            <div class="flex items-center">
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
              </svg>
              {{ activity.location }}
            </div>
            <div class="flex items-center">
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
              {{ formatDate(activity.startTime) }}
            </div>
            <div class="flex items-center">
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
              </svg>
              {{ activity.currentParticipants || 0 }} / {{ activity.maxParticipants || 0 }} 人
              <span 
                v-if="activity.maxParticipants && (activity.currentParticipants || 0) >= activity.maxParticipants" 
                class="ml-2 text-red-500 text-xs font-medium"
              >
                已满
              </span>
              <span 
                v-else-if="activity.maxParticipants && activity.maxParticipants - (activity.currentParticipants || 0) <= 5 && activity.maxParticipants - (activity.currentParticipants || 0) > 0" 
                class="ml-2 text-orange-500 text-xs font-medium"
              >
                仅剩{{ activity.maxParticipants - (activity.currentParticipants || 0) }}名
              </span>
            </div>
          </div>
        </div>
        
        <div class="bg-gray-50 px-6 py-3 border-t border-gray-100">
          <div class="flex items-center justify-between">
            <span class="text-sm text-gray-500">
              <span :class="isActivityFull(activity) ? 'text-red-500 font-medium' : 'text-primary'">
                {{ isActivityFull(activity) ? '已满员' : '还有空位' }}
              </span>
            </span>
            <span class="text-primary text-sm font-medium hover:text-primary-dark">
              查看详情 →
            </span>
          </div>
        </div>
      </div>
    </div>
    
    <!-- Empty State -->
    <div v-else class="text-center py-12">
      <svg class="w-16 h-16 mx-auto text-gray-300 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4" />
      </svg>
      <p class="text-gray-500">暂无活动</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { activityAPI } from '../api'

const router = useRouter()
const activities = ref([])
const loading = ref(true)
const currentTab = ref('all')

const tabs = [
  { label: '全部活动', value: 'all' },
  { label: '即将开始', value: 'UPCOMING' },
  { label: '进行中', value: 'ONGOING' },
  { label: '已结束', value: 'COMPLETED' }
]

const fetchActivities = async () => {
  loading.value = true
  try {
    const status = currentTab.value === 'all' ? null : currentTab.value
    const response = await activityAPI.getAll(status)
    activities.value = response.data
  } catch (error) {
    console.error('Failed to fetch activities:', error)
  } finally {
    loading.value = false
  }
}

const viewDetail = (id) => {
  router.push(`/activity/${id}`)
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const getComputedStatus = (activity) => {
  if (activity.status === 'CANCELLED') return 'CANCELLED'
  
  const now = new Date()
  const start = new Date(activity.startTime)
  const end = new Date(activity.endTime)
  
  if (now < start) return 'UPCOMING'
  if (now >= start && now <= end) return 'ONGOING'
  return 'COMPLETED'
}

const getStatusText = (activity) => {
  const status = getComputedStatus(activity)
  const map = {
    UPCOMING: '即将开始',
    ONGOING: '进行中',
    COMPLETED: '已结束',
    CANCELLED: '已取消'
  }
  return map[status] || status
}

const getStatusColor = (activity) => {
  const status = getComputedStatus(activity)
  const map = {
    UPCOMING: 'bg-blue-100 text-blue-700',
    ONGOING: 'bg-green-100 text-green-700',
    COMPLETED: 'bg-gray-100 text-gray-700',
    CANCELLED: 'bg-red-100 text-red-700'
  }
  return map[status] || 'bg-gray-100 text-gray-700'
}

const isActivityFull = (activity) => {
  if (!activity || !activity.maxParticipants) return false
  return (activity.currentParticipants || 0) >= activity.maxParticipants
}

watch(currentTab, fetchActivities)

onMounted(fetchActivities)
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
