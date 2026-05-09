<template>
  <div>
    <h2 class="text-3xl font-bold text-gray-800 mb-6">我的报名</h2>
    
    <div class="mb-4">
      <label class="block text-sm font-medium text-gray-700 mb-2">查询学号</label>
      <div class="flex gap-2">
        <input
          v-model="userId"
          type="number"
          class="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
          placeholder="请输入学号 (测试: 1-4)"
        />
        <button
          @click="fetchRegistrations"
          class="px-6 py-2 bg-primary text-white rounded-lg hover:bg-primary-dark transition-colors"
        >
          查询
        </button>
      </div>
    </div>
    
    <!-- Loading State -->
    <div v-if="loading" class="space-y-4">
      <div v-for="i in 3" :key="i" class="bg-white rounded-lg shadow-md p-6 animate-pulse">
        <div class="h-6 bg-gray-200 rounded mb-4"></div>
        <div class="h-4 bg-gray-200 rounded mb-2"></div>
      </div>
    </div>
    
    <!-- Registrations List -->
    <div v-else-if="registrations.length > 0" class="space-y-4">
      <div
        v-for="reg in registrations"
        :key="reg.id"
        class="bg-white rounded-lg shadow-md hover:shadow-lg transition-shadow overflow-hidden"
      >
        <div class="p-6">
          <div class="flex items-start justify-between mb-4">
            <div class="flex-1">
              <h3 class="text-xl font-bold text-gray-800 mb-2">{{ reg.activity.title }}</h3>
              <div class="space-y-2 text-sm text-gray-600">
                <div class="flex items-center">
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                  </svg>
                  {{ reg.activity.location }}
                </div>
                <div class="flex items-center">
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                  </svg>
                  {{ formatDate(reg.activity.startTime) }}
                </div>
                <div class="flex items-center">
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                  报名时间: {{ formatDate(reg.registeredAt) }}
                </div>
              </div>
            </div>
            
            <div class="ml-4 flex flex-col items-end space-y-2">
              <span 
                class="px-3 py-1 rounded-full text-sm font-medium"
                :class="reg.status === 'ACTIVE' ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-700'"
              >
                {{ reg.status === 'ACTIVE' ? '报名成功' : '已取消' }}
              </span>
              
              <span 
                v-if="checkInStatus[reg.id]"
                class="px-3 py-1 rounded-full text-sm font-medium bg-blue-100 text-blue-700"
              >
                已签到
              </span>
            </div>
          </div>
          
          <div class="text-sm text-gray-500 bg-gray-50 px-4 py-2 rounded">
            报名ID: <span class="font-mono font-medium">{{ reg.id }}</span>
          </div>
        </div>
        
        <div class="bg-gray-50 px-6 py-3 border-t flex justify-end space-x-2">
          <button
            @click="$router.push('/activity/' + reg.activity.id)"
            class="px-4 py-2 text-primary hover:bg-primary hover:text-white rounded-lg transition-colors border border-primary"
          >
            查看详情
          </button>
          <button
            v-if="reg.status === 'ACTIVE'"
            @click="handleCancel(reg.id)"
            class="px-4 py-2 text-red-600 hover:bg-red-600 hover:text-white rounded-lg transition-colors border border-red-600"
          >
            取消报名
          </button>
        </div>
      </div>
    </div>
    
    <!-- Empty State -->
    <div v-else class="text-center py-12 bg-white rounded-lg shadow-md">
      <svg class="w-16 h-16 mx-auto text-gray-300 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
      </svg>
      <p class="text-gray-500">暂无报名记录</p>
      <p class="text-sm text-gray-400 mt-2">请先输入学号查询</p>
    </div>
    
    <!-- Custom Confirmation Dialog -->
    <div v-if="showConfirmDialog" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div class="bg-white rounded-lg shadow-2xl max-w-md w-full p-6 transform transition-all">
        <div class="flex items-center mb-4">
          <div class="w-12 h-12 rounded-full bg-red-100 flex items-center justify-center mr-4">
            <svg class="w-6 h-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
            </svg>
          </div>
          <h3 class="text-xl font-bold text-gray-900">确认取消报名</h3>
        </div>
        
        <div class="mb-6">
          <p class="text-gray-700 mb-2">您确定要取消报名以下活动吗？</p>
          <p class="font-medium text-primary text-lg">{{ confirmData.activityTitle }}</p>
          <p class="text-sm text-gray-500 mt-3">⚠️ 取消后将无法恢复，如需再次参加需要重新报名。</p>
        </div>
        
        <div class="flex space-x-3">
          <button
            @click="showConfirmDialog = false"
            class="flex-1 px-4 py-3 border-2 border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors font-medium"
          >
            我再想想
          </button>
          <button
            @click="confirmCancel"
            class="flex-1 px-4 py-3 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors font-medium shadow-md"
          >
            确认取消
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { registrationAPI, checkInAPI, showToast } from '../api'

const userId = ref('')
const registrations = ref([])
const loading = ref(false)
const checkInStatus = ref({})
const showConfirmDialog = ref(false)
const confirmData = ref({ registrationId: null, activityTitle: '' })

const fetchRegistrations = async () => {
  if (!userId.value) {
    showToast('请输入学号', 'error')
    return
  }
  
  loading.value = true
  try {
    const response = await registrationAPI.getAll({ userId: userId.value })
    registrations.value = response.data
    
    // Fetch check-in status for each registration
    for (const reg of registrations.value) {
      if (reg.status === 'ACTIVE') {
        try {
          const statusRes = await checkInAPI.getStatus(reg.id)
          checkInStatus.value[reg.id] = statusRes.data.checkedIn
        } catch (error) {
          checkInStatus.value[reg.id] = false
        }
      }
    }
  } catch (error) {
    console.error('Failed to fetch registrations:', error)
  } finally {
    loading.value = false
  }
}

const handleCancel = async (id) => {
  const registration = registrations.value.find(r => r.id === id)
  const activityTitle = registration?.activity?.title || '该活动'
  
  // Show custom confirmation dialog
  confirmData.value = {
    registrationId: id,
    activityTitle: activityTitle
  }
  showConfirmDialog.value = true
}

const confirmCancel = async () => {
  showConfirmDialog.value = false
  
  try {
    await registrationAPI.cancel(confirmData.value.registrationId)
    showToast('取消报名成功！', 'success')
    await fetchRegistrations()
  } catch (error) {
    // Error handled by interceptor
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
</script>
