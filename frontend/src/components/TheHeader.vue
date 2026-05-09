<template>
  <header v-if="$route.path !== '/login'" class="bg-primary text-white shadow-lg">
    <div class="container mx-auto px-4">
      <div class="flex items-center justify-between h-16">
        <!-- Logo and Title -->
        <div class="flex items-center space-x-3">
          <div class="w-10 h-10 bg-white rounded-full flex items-center justify-center">
            <span class="text-primary text-xl font-bold">校</span>
          </div>
          <h1 class="text-xl font-bold">校园活动报名与签到系统</h1>
        </div>
        
        <!-- Desktop Navigation -->
        <nav class="hidden md:flex items-center space-x-6">
          <router-link 
            to="/" 
            class="hover:text-gray-200 transition-colors px-3 py-2 rounded"
            :class="{ 'bg-primary-dark': $route.path === '/' }"
          >
            活动列表
          </router-link>
          <router-link 
            to="/my-registrations" 
            class="hover:text-gray-200 transition-colors px-3 py-2 rounded"
            :class="{ 'bg-primary-dark': $route.path === '/my-registrations' }"
          >
            我的报名
          </router-link>
          <router-link 
            to="/checkin" 
            class="hover:text-gray-200 transition-colors px-3 py-2 rounded"
            :class="{ 'bg-primary-dark': $route.path === '/checkin' }"
          >
            签到
          </router-link>
          <router-link 
            v-if="user && user.role === 'ADMIN'"
            to="/admin" 
            class="hover:text-gray-200 transition-colors px-3 py-2 rounded"
            :class="{ 'bg-primary-dark': $route.path === '/admin' }"
          >
            活动管理

          </router-link>

          <!-- User Info & Logout -->
          <div class="flex items-center space-x-3 ml-4 pl-4 border-l border-white/30">
            <div class="flex items-center space-x-2">
              <div class="w-8 h-8 bg-white/20 rounded-full flex items-center justify-center">
                <span class="text-sm font-semibold">{{ userInitial }}</span>
              </div>
              <span class="text-sm">{{ user?.username }}</span>
            </div>
            <button
              @click="handleLogout"
              class="px-3 py-1.5 bg-white/10 hover:bg-white/20 rounded transition-colors text-sm"
            >
              登出
            </button>
          </div>
        </nav>
        
        <!-- Mobile menu button -->
        <button 
          @click="mobileMenuOpen = !mobileMenuOpen"
          class="md:hidden p-2 rounded hover:bg-primary-dark transition-colors"
        >
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
          </svg>
        </button>
      </div>
      
      <!-- Mobile Navigation -->
      <nav v-if="mobileMenuOpen" class="md:hidden py-4 border-t border-primary-dark space-y-2">
        <router-link 
          to="/" 
          class="block px-4 py-2 hover:bg-primary-dark rounded transition-colors"
          @click="mobileMenuOpen = false"
        >
          活动列表
        </router-link>
        <router-link 
          to="/my-registrations" 
          class="block px-4 py-2 hover:bg-primary-dark rounded transition-colors"
          @click="mobileMenuOpen = false"
        >
          我的报名
        </router-link>
        <router-link 
          to="/checkin" 
          class="block px-4 py-2 hover:bg-primary-dark rounded transition-colors"
          @click="mobileMenuOpen = false"
        >
          签到
        </router-link>
        <router-link 
          v-if="user && user.role === 'ADMIN'"
          to="/admin" 
          class="block px-4 py-2 hover:bg-primary-dark rounded transition-colors"
          @click="mobileMenuOpen = false"
        >
          活动管理

        </router-link>

        <!-- Mobile User Info -->
        <div class="px-4 py-3 border-t border-white/20 mt-2">
          <div class="flex items-center justify-between">
            <div class="flex items-center space-x-2">
              <div class="w-8 h-8 bg-white/20 rounded-full flex items-center justify-center">
                <span class="text-sm font-semibold">{{ userInitial }}</span>
              </div>
              <span class="text-sm">{{ user?.username }}</span>
            </div>
            <button
              @click="handleLogout"
              class="px-3 py-1.5 bg-white/10 hover:bg-white/20 rounded transition-colors text-sm"
            >
              登出
            </button>
          </div>
        </div>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getUser, logout } from '@/utils/auth'
import { showToast } from '@/api/index'

const router = useRouter()
const route = useRoute()
const mobileMenuOpen = ref(false)
const user = ref(null)

const userInitial = computed(() => {
  return user.value?.username?.charAt(0).toUpperCase() || 'U'
})

const handleLogout = () => {
  logout()
  showToast('已登出', 'info')
  router.push('/login')
}

const updateUser = () => {
  user.value = getUser()
}

// Watch for route changes to update user info
watch(() => route.path, () => {
  updateUser()
  mobileMenuOpen.value = false
})

onMounted(() => {
  updateUser()
})
</script>

