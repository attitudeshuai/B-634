<template>
  <div class="max-w-2xl mx-auto">
    <h2 class="text-3xl font-bold text-gray-800 mb-6">活动签到</h2>
    
    <div class="bg-white rounded-lg shadow-lg p-8">
      <div class="mb-6">
        <label class="block text-sm font-medium text-gray-700 mb-2">报名ID</label>
        <input
          v-model="registrationId"
          type="number"
          class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent text-lg"
          placeholder="请输入报名ID"
          @keyup.enter="handleCheckIn"
        />
        <p class="text-sm text-gray-500 mt-2">
          提示：在"我的报名"页面可查看报名ID
        </p>
      </div>
      
      <button
        @click="handleCheckIn"
        :disabled="!registrationId || checking"
        class="w-full px-6 py-4 bg-primary text-white rounded-lg hover:bg-primary-dark transition-colors font-bold text-lg shadow-md hover:shadow-lg disabled:opacity-50 disabled:cursor-not-allowed"
      >
        {{ checking ? '签到中...' : '确认签到' }}
      </button>
      
      <!-- Success Message -->
      <div v-if="checkInSuccess" class="mt-6 p-4 bg-green-50 border border-green-200 rounded-lg">
        <div class="flex items-center">
          <svg class="w-6 h-6 text-green-600 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          <div>
            <p class="font-medium text-green-800">签到成功！</p>
            <p class="text-sm text-green-600">{{ formatDate(checkInTime) }}</p>
          </div>
        </div>
      </div>
    </div>
    
    <!-- Instructions -->
    <div class="mt-8 bg-blue-50 border border-blue-200 rounded-lg p-6">
      <h3 class="font-bold text-blue-900 mb-3">签到说明</h3>
      <ul class="space-y-2 text-sm text-blue-800">
        <li class="flex items-start">
          <span class="mr-2">1.</span>
          <span>请在"我的报名"页面查看您的报名ID</span>
        </li>
        <li class="flex items-start">
          <span class="mr-2">2.</span>
          <span>在上方输入框中输入报名ID</span>
        </li>
        <li class="flex items-start">
          <span class="mr-2">3.</span>
          <span>点击"确认签到"按钮完成签到</span>
        </li>
        <li class="flex items-start">
          <span class="mr-2">4.</span>
          <span>每个报名ID只能签到一次</span>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { checkInAPI, showToast } from '../api'

const registrationId = ref('')
const checking = ref(false)
const checkInSuccess = ref(false)
const checkInTime = ref(null)

const handleCheckIn = async () => {
  if (!registrationId.value) {
    showToast('请输入报名ID', 'error')
    return
  }
  
  checking.value = true
  checkInSuccess.value = false
  
  try {
    const response = await checkInAPI.checkIn(registrationId.value)
    checkInTime.value = response.data.checkInTime
    checkInSuccess.value = true
    showToast('签到成功！', 'success')
    registrationId.value = ''
  } catch (error) {
    // Error handled by interceptor
    checkInSuccess.value = false
  } finally {
    checking.value = false
  }
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}
</script>
