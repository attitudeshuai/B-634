<template>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-gray-900 via-red-900 to-gray-900 px-4">
    <div class="max-w-md w-full">
      <!-- Logo/Title -->
      <div class="text-center mb-8">
        <h1 class="text-4xl font-bold text-white mb-2">校园活动系统</h1>
        <p class="text-gray-300">Campus Activity Management</p>
      </div>

      <!-- Login Card -->
      <div class="bg-white/10 backdrop-blur-lg rounded-2xl shadow-2xl p-8 border border-white/20">
        <h2 class="text-2xl font-bold text-white mb-6 text-center">登录</h2>

        <form @submit.prevent="handleLogin" class="space-y-6">
          <!-- Username -->
          <div>
            <label class="block text-sm font-medium text-gray-200 mb-2">
              用户名
            </label>
            <input
              v-model="loginForm.username"
              type="text"
              required
              class="w-full px-4 py-3 bg-white/10 border border-white/30 rounded-lg text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-red-500 focus:border-transparent transition"
              placeholder="请输入用户名"
            />
          </div>

          <!-- Password -->
          <div>
            <label class="block text-sm font-medium text-gray-200 mb-2">
              密码
            </label>
            <input
              v-model="loginForm.password"
              type="password"
              required
              class="w-full px-4 py-3 bg-white/10 border border-white/30 rounded-lg text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-red-500 focus:border-transparent transition"
              placeholder="请输入密码"
            />
          </div>

          <!-- Error Message -->
          <div v-if="errorMessage" class="bg-red-500/20 border border-red-500/50 rounded-lg p-3">
            <p class="text-red-200 text-sm text-center">{{ errorMessage }}</p>
          </div>

          <!-- Login Button -->
          <button
            type="submit"
            :disabled="loading"
            class="w-full bg-gradient-to-r from-red-600 to-red-700 hover:from-red-700 hover:to-red-800 text-white font-semibold py-3 px-4 rounded-lg transition duration-200 transform hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed disabled:transform-none shadow-lg"
          >
            <span v-if="loading">登录中...</span>
            <span v-else>登录</span>
          </button>
        </form>

        <!-- Test Accounts Info -->
        <div class="mt-6 pt-6 border-t border-white/20">
          <p class="text-xs text-gray-300 text-center mb-2">测试账号</p>
          <div class="space-y-1 text-xs text-gray-400">
            <p class="text-center">管理员: admin / 123456</p>
            <p class="text-center">学生: student1 / 123456</p>
            <p class="text-center">学生: student2 / 123456</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '@/api/auth'
import { setToken, setUser } from '@/utils/auth'
import { showToast } from '@/api/index'

const router = useRouter()

const loginForm = ref({
  username: '',
  password: ''
})

const loading = ref(false)
const errorMessage = ref('')

const handleLogin = async () => {
  loading.value = true
  errorMessage.value = ''

  try {
    const response = await login(loginForm.value.username, loginForm.value.password)
    const { token, ...user } = response.data

    // Save token and user info
    setToken(token)
    setUser(user)

    showToast('登录成功！', 'success')

    // Redirect to home page
    setTimeout(() => {
      router.push('/')
    }, 500)
  } catch (error) {
    console.error('Login failed:', error)
    errorMessage.value = '用户名或密码错误'
  } finally {
    loading.value = false
  }
}
</script>
