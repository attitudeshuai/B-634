<template>
  <div>
    <!-- Loading State -->
    <div v-if="loading" class="max-w-4xl mx-auto">
      <div class="bg-white rounded-lg shadow-md p-8 animate-pulse">
        <div class="h-8 bg-gray-200 rounded mb-4"></div>
        <div class="h-4 bg-gray-200 rounded mb-2"></div>
        <div class="h-4 bg-gray-200 rounded w-3/4"></div>
      </div>
    </div>
    
    <!-- Activity Detail -->
    <div v-else-if="activity" class="max-w-4xl mx-auto">
      <div class="bg-white rounded-lg shadow-lg overflow-hidden">
        <!-- Header -->
        <div class="bg-gradient-to-r from-primary to-primary-dark text-white p-8">
          <div class="flex items-start justify-between mb-4">
            <h1 class="text-3xl font-bold flex-1">{{ activity.title }}</h1>
            <span 
              class="px-4 py-1 rounded-full text-sm font-medium ml-4"
              :class="getStatusColor(activity.status)"
            >
              {{ getStatusText(activity.status) }}
            </span>
          </div>
        </div>
        
        <!-- Content -->
        <div class="p-8">
          <div class="grid md:grid-cols-2 gap-6 mb-8">
            <div class="space-y-4">
              <div class="flex items-start">
                <svg class="w-5 h-5 mr-3 mt-1 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                </svg>
                <div>
                  <div class="text-sm text-gray-500">活动地点</div>
                  <div class="font-medium">{{ activity.location }}</div>
                </div>
              </div>
              
              <div class="flex items-start">
                <svg class="w-5 h-5 mr-3 mt-1 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
                <div>
                  <div class="text-sm text-gray-500">开始时间</div>
                  <div class="font-medium">{{ formatDate(activity.startTime) }}</div>
                </div>
              </div>
              
              <div class="flex items-start">
                <svg class="w-5 h-5 mr-3 mt-1 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <div>
                  <div class="text-sm text-gray-500">结束时间</div>
                  <div class="font-medium">{{ formatDate(activity.endTime) }}</div>
                </div>
              </div>
            </div>
            
            <div class="space-y-4">
              <div class="flex items-start">
                <svg class="w-5 h-5 mr-3 mt-1 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
                </svg>
                <div>
                  <div class="text-sm text-gray-500">参与人数</div>
                  <div class="font-medium" :class="isFull ? 'text-red-600' : ''">
                    {{ activity.currentParticipants || 0 }} / {{ activity.maxParticipants || 0 }} 人
                    <span v-if="isFull" class="ml-2 text-red-600 text-sm font-medium">(已满)</span>
                    <span v-else-if="remainingSpots > 0 && remainingSpots <= 5" class="ml-2 text-orange-500 text-sm font-medium">
                      (仅剩 {{ remainingSpots }} 个名额)
                    </span>
                  </div>
                </div>
              </div>
              
              <div class="bg-gray-50 rounded-lg p-4">
                <div class="text-sm text-gray-500 mb-2">报名进度</div>
                <div class="w-full bg-gray-200 rounded-full h-3">
                  <div 
                    class="h-3 rounded-full transition-all"
                    :class="progressColor"
                    :style="{ width: progressPercentage + '%' }"
                  ></div>
                </div>
                <div class="text-sm text-right mt-1" :class="isFull ? 'text-red-600' : 'text-primary'">
                  {{ progressPercentage.toFixed(1) }}%
                </div>
              </div>
            </div>
          </div>
          
          <div class="border-t pt-6">
            <h2 class="text-xl font-bold mb-3">活动描述</h2>
            <p class="text-gray-700 leading-relaxed whitespace-pre-wrap">{{ activity.description }}</p>
          </div>
        </div>
        
        <!-- Actions -->
        <div class="bg-gray-50 px-8 py-6 border-t flex items-center justify-between">
          <button
            @click="$router.back()"
            class="px-6 py-2 border border-gray-300 rounded-lg hover:bg-gray-100 transition-colors"
          >
            返回
          </button>
          
          <button
            v-if="canRegister && !isFull && activity.maxParticipants"
            @click="showRegisterForm = true"
            class="px-8 py-3 bg-primary text-white rounded-lg hover:bg-primary-dark transition-colors font-medium shadow-md hover:shadow-lg"
          >
            立即报名
            <span v-if="remainingSpots > 0 && remainingSpots <= 5" class="ml-2 text-yellow-200 text-sm">
              (仅剩{{ remainingSpots }}名)
            </span>
          </button>
          
          <span v-else-if="isFull" class="text-red-600 font-medium">
            活动已满员
          </span>
          
          <span v-else-if="activity.status === 'COMPLETED'" class="text-gray-500 font-medium">
            活动已结束
          </span>
          
          <span v-else-if="activity.status === 'CANCELLED'" class="text-red-600 font-medium">
            活动已取消
          </span>
        </div>
      </div>
    </div>
    
    <!-- Registration Modal -->
    <div v-if="showRegisterForm" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
        <h3 class="text-2xl font-bold mb-6">活动报名</h3>
        
        <div class="space-y-4 mb-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">学号</label>
            <input
              v-model="registerForm.userId"
              type="number"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
              placeholder="请输入学号 (测试: 1-4)"
            />
          </div>
        </div>
        
        <div class="flex space-x-3">
          <button
            @click="showRegisterForm = false"
            class="flex-1 px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-100 transition-colors"
          >
            取消
          </button>
          <button
            @click="handleRegister"
            :disabled="!registerForm.userId"
            class="flex-1 px-4 py-2 bg-primary text-white rounded-lg hover:bg-primary-dark transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
          >
            确认报名
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { activityAPI, registrationAPI, showToast } from '../api'

const route = useRoute()
const router = useRouter()

const activity = ref(null)
const loading = ref(true)
const showRegisterForm = ref(false)
const registerForm = ref({ userId: '' })

const isFull = computed(() => {
  if (!activity.value || !activity.value.maxParticipants) return false
  return (activity.value.currentParticipants || 0) >= activity.value.maxParticipants
})

const remainingSpots = computed(() => {
  if (!activity.value || !activity.value.maxParticipants) return 0
  return activity.value.maxParticipants - (activity.value.currentParticipants || 0)
})

const progressPercentage = computed(() => {
  if (!activity.value || !activity.value.maxParticipants) return 0
  if (activity.value.maxParticipants === 0) return 0
  return Math.min(((activity.value.currentParticipants || 0) / activity.value.maxParticipants) * 100, 100)
})

const progressColor = computed(() => {
  if (isFull.value) return 'bg-red-500'
  if (progressPercentage.value >= 80) return 'bg-orange-500'
  return 'bg-primary'
})

const canRegister = computed(() => {
  if (!activity.value) return false
  return activity.value.status === 'UPCOMING' || activity.value.status === 'ONGOING'
})

const fetchActivity = async () => {
  loading.value = true
  try {
    const response = await activityAPI.getById(route.params.id)
    activity.value = response.data
  } catch (error) {
    console.error('Failed to fetch activity:', error)
    router.push('/')
  } finally {
    loading.value = false
  }
}

const handleRegister = async () => {
  try {
    await registrationAPI.create(registerForm.value.userId, activity.value.id)
    showToast('报名成功！', 'success')
    showRegisterForm.value = false
    registerForm.value.userId = ''
    await fetchActivity() // Refresh data
  } catch (error) {
    // Error already handled by interceptor
  }
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

const getStatusText = (status) => {
  const map = {
    UPCOMING: '即将开始',
    ONGOING: '进行中',
    COMPLETED: '已结束',
    CANCELLED: '已取消'
  }
  return map[status] || status
}

const getStatusColor = (status) => {
  const map = {
    UPCOMING: 'bg-blue-500',
    ONGOING: 'bg-green-500',
    COMPLETED: 'bg-gray-500',
    CANCELLED: 'bg-red-500'
  }
  return map[status] || 'bg-gray-500'
}

onMounted(fetchActivity)
</script>
