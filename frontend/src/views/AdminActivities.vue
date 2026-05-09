<template>
  <div>
    <div class="flex items-center justify-between mb-6">
      <h2 class="text-3xl font-bold text-gray-800">活动管理</h2>
      <button
        @click="showCreateForm = true"
        class="px-6 py-3 bg-primary text-white rounded-lg hover:bg-primary-dark transition-colors font-medium shadow-md hover:shadow-lg"
      >
        + 创建活动
      </button>
    </div>
    
    <!-- Loading State -->
    <div v-if="loading" class="space-y-4">
      <div v-for="i in 3" :key="i" class="bg-white rounded-lg shadow p-6 animate-pulse">
        <div class="h-6 bg-gray-200 rounded mb-4"></div>
        <div class="h-4 bg-gray-200 rounded"></div>
      </div>
    </div>
    
    <!-- Activities Table -->
    <div v-else-if="activities.length > 0" class="bg-white rounded-lg shadow-lg overflow-hidden">
      <div class="overflow-x-auto">
        <table class="w-full">
          <thead class="bg-primary text-white">
            <tr>
              <th class="px-6 py-4 text-left">活动名称</th>
              <th class="px-6 py-4 text-left">地点</th>
              <th class="px-6 py-4 text-left">开始时间</th>
              <th class="px-6 py-4 text-center">报名人数</th>
              <th class="px-6 py-4 text-center">状态</th>
              <th class="px-6 py-4 text-center">操作</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-200">
            <tr v-for="activity in activities" :key="activity.id" class="hover:bg-gray-50 transition-colors">
              <td class="px-6 py-4 font-medium text-gray-900">{{ activity.title }}</td>
              <td class="px-6 py-4 text-gray-600">{{ activity.location }}</td>
              <td class="px-6 py-4 text-gray-600 text-sm">{{ formatDate(activity.startTime) }}</td>
              <td class="px-6 py-4 text-center">
                <span class="font-medium" :class="activity.currentParticipants >= activity.maxParticipants ? 'text-red-600' : 'text-gray-900'">
                  {{ activity.currentParticipants || 0 }}
                </span>
                <span class="text-gray-500"> / {{ activity.maxParticipants || 0 }}</span>
                <span 
                  v-if="activity.maxParticipants && activity.currentParticipants >= activity.maxParticipants" 
                  class="ml-2 inline-block px-2 py-0.5 bg-red-100 text-red-700 rounded text-xs font-medium"
                >
                  已满
                </span>
              </td>
              <td class="px-6 py-4 text-center">
                <span 
                  class="px-3 py-1 rounded-full text-xs font-medium"
                  :class="getStatusColor(activity)"
                >
                  {{ getStatusText(activity) }}
                </span>
              </td>
              <td class="px-6 py-4 text-center">
                <div class="flex items-center justify-center space-x-3">
                  <button
                    @click="viewRegistrations(activity.id)"
                    class="text-primary hover:text-primary-dark font-medium text-sm"
                  >
                    查看报名
                  </button>
                  <button
                    @click="confirmDelete(activity)"
                    class="text-red-500 hover:text-red-700 font-medium text-sm"
                  >
                    删除
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    
    <!-- Empty State -->
    <div v-else class="text-center py-12 bg-white rounded-lg shadow-md">
      <svg class="w-16 h-16 mx-auto text-gray-300 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4" />
      </svg>
      <p class="text-gray-500">暂无活动</p>
    </div>
    
    <!-- Create Activity Modal -->
    <div v-if="showCreateForm" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4 overflow-y-auto">
      <div class="bg-white rounded-lg shadow-xl max-w-2xl w-full p-6 my-8">
        <h3 class="text-2xl font-bold mb-6">创建新活动</h3>
        
        <div class="space-y-4 mb-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">活动名称 <span class="text-red-500">*</span></label>
            <input
              v-model="newActivity.title"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
              placeholder="请输入活动名称"
            />
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">活动描述 <span class="text-red-500">*</span></label>
            <textarea
              v-model="newActivity.description"
              rows="4"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
              placeholder="请输入活动描述"
            ></textarea>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">活动地点 <span class="text-red-500">*</span></label>
            <input
              v-model="newActivity.location"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
              placeholder="请输入活动地点"
            />
          </div>
          
          <div class="grid md:grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">开始时间 <span class="text-red-500">*</span></label>
              <input
                v-model="newActivity.startTime"
                type="datetime-local"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
              />
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">结束时间 <span class="text-red-500">*</span></label>
              <input
                v-model="newActivity.endTime"
                type="datetime-local"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
              />
            </div>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">最大参与人数 <span class="text-red-500">*</span></label>
            <input
              v-model.number="newActivity.maxParticipants"
              type="number"
              min="1"
              :class="[
                'w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent transition-colors',
                newActivity.maxParticipants < 1 && newActivity.maxParticipants !== null ? 'border-red-500 bg-red-50' : 'border-gray-300'
              ]"
              placeholder="请输入最大参与人数"
            />
            <p v-if="newActivity.maxParticipants < 1 && newActivity.maxParticipants !== null" class="mt-1 text-sm text-red-500">
              最大参与人数必须大于0
            </p>
          </div>
        </div>
        
        <div class="flex space-x-3">
          <button
            @click="showCreateForm = false"
            class="flex-1 px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-100 transition-colors"
          >
            取消
          </button>
          <button
            @click="handleCreate"
            :disabled="newActivity.maxParticipants < 1"
            class="flex-1 px-4 py-2 bg-primary text-white rounded-lg hover:bg-primary-dark transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
          >
            创建活动
          </button>
        </div>
      </div>
    </div>
    
    <!-- Registrations Modal -->
    <div v-if="showRegistrations" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div class="bg-white rounded-lg shadow-xl max-w-4xl w-full p-6 max-h-[80vh] overflow-y-auto">
        <div class="flex items-center justify-between mb-6">
          <h3 class="text-2xl font-bold">报名列表</h3>
          <button @click="showRegistrations = false" class="text-gray-500 hover:text-gray-700">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        
        <div v-if="currentRegistrations.length > 0" class="space-y-3">
          <div v-for="reg in currentRegistrations" :key="reg.id" class="border rounded-lg p-4">
            <div class="flex items-center justify-between">
              <div>
                <div class="font-medium">{{ reg.user.username }}</div>
                <div class="text-sm text-gray-500">{{ reg.user.email }}</div>
                <div class="text-sm text-gray-500">报名时间: {{ formatDate(reg.registeredAt) }}</div>
              </div>
              <span 
                class="px-3 py-1 rounded-full text-sm"
                :class="reg.status === 'ACTIVE' ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-700'"
              >
                {{ reg.status === 'ACTIVE' ? '有效' : '已取消' }}
              </span>
            </div>
          </div>
        </div>
        <div v-else class="text-center py-8 text-gray-500">
          暂无报名记录
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { activityAPI, registrationAPI, showToast } from '../api'

const activities = ref([])
const loading = ref(true)
const showCreateForm = ref(false)
const showRegistrations = ref(false)
const currentRegistrations = ref([])

const newActivity = ref({
  title: '',
  description: '',
  location: '',
  startTime: '',
  endTime: '',
  maxParticipants: 100,
  status: 'UPCOMING'
})

const fetchActivities = async () => {
  loading.value = true
  try {
    const response = await activityAPI.getAll()
    activities.value = response.data
  } catch (error) {
    console.error('Failed to fetch activities:', error)
  } finally {
    loading.value = false
  }
}

const validateForm = () => {
  if (!newActivity.value.title) return '请输入活动名称'
  if (!newActivity.value.description) return '请输入活动描述'
  if (!newActivity.value.location) return '请输入活动地点'
  if (!newActivity.value.startTime) return '请选择开始时间'
  if (!newActivity.value.endTime) return '请选择结束时间'
  if (!newActivity.value.maxParticipants) return '请输入最大参与人数'
  if (newActivity.value.maxParticipants < 1) return '最大参与人数必须大于0'
  return null
}

const handleCreate = async () => {
  const error = validateForm()
  if (error) {
    showToast(error, 'error')
    return
  }

  try {
    await activityAPI.create(newActivity.value)
    showToast('活动创建成功', 'success')
    showCreateForm.value = false
    newActivity.value = {
      title: '',
      description: '',
      location: '',
      startTime: '',
      endTime: '',
      maxParticipants: 100,
      status: 'UPCOMING'
    }
    await fetchActivities()
  } catch (error) {
    // Error handled by interceptor
  }
}

const confirmDelete = async (activity) => {
  if (confirm(`确定要删除活动 "${activity.title}" 吗？此操作无法撤销。`)) {
    try {
      await activityAPI.delete(activity.id)
      showToast('活动已删除', 'success')
      await fetchActivities()
    } catch (error) {
      console.error('Failed to delete activity:', error)
      showToast('删除失败', 'error')
    }
  }
}

const viewRegistrations = async (activityId) => {
  try {
    const response = await registrationAPI.getAll({ activityId })
    currentRegistrations.value = response.data
    showRegistrations.value = true
  } catch (error) {
    console.error('Failed to fetch registrations:', error)
  }
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
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

onMounted(fetchActivities)
</script>
