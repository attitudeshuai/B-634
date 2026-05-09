import axios from 'axios'
import { getToken, logout } from '@/utils/auth'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// Request interceptor - Add JWT token
api.interceptors.request.use(
  config => {
    const token = getToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// Response interceptor - Handle errors
api.interceptors.response.use(
  response => response,
  error => {
    console.error('API Error:', error)
    
    // Handle 401 Unauthorized - redirect to login
    if (error.response?.status === 401) {
      logout()
      window.location.href = '/login'
      return Promise.reject(error)
    }
    
    const message = error.response?.data?.message || error.message || '请求失败'
    showToast(message, 'error')
    return Promise.reject(error)
  }
)

// Simple toast notification with better visibility
function showToast(message, type = 'info') {
  const toast = document.createElement('div')
  const bgColor = type === 'error' ? 'bg-red-500' : type === 'success' ? 'bg-green-500' : 'bg-blue-500'
  
  toast.className = `fixed top-20 right-4 px-8 py-4 rounded-lg shadow-2xl text-white z-50 font-medium text-lg ${bgColor} transform transition-all duration-300 ease-in-out`
  toast.style.minWidth = '300px'
  toast.style.opacity = '0'
  toast.style.transform = 'translateX(100%)'
  
  // Add icon based on type
  const icons = {
    success: '✓',
    error: '✕',
    info: 'ℹ'
  }
  
  toast.innerHTML = `
    <div class="flex items-center gap-3">
      <span class="text-2xl">${icons[type] || icons.info}</span>
      <span>${message}</span>
    </div>
  `
  
  document.body.appendChild(toast)
  
  // Trigger animation
  setTimeout(() => {
    toast.style.opacity = '1'
    toast.style.transform = 'translateX(0)'
  }, 10)
  
  // Remove after 5 seconds (increased from 3)
  setTimeout(() => {
    toast.style.opacity = '0'
    toast.style.transform = 'translateX(100%)'
    setTimeout(() => toast.remove(), 300)
  }, 5000)
}

// Export toast for use in components
export { showToast }

// Activity APIs
export const activityAPI = {
  getAll: (status) => api.get('/activities', { params: { status } }),
  getById: (id) => api.get(`/activities/${id}`),
  create: (data) => api.post('/activities', data),
  update: (id, data) => api.put(`/activities/${id}`, data),
  delete: (id) => api.delete(`/activities/${id}`)
}

// Registration APIs
export const registrationAPI = {
  getAll: (params) => api.get('/registrations', { params }),
  getById: (id) => api.get(`/registrations/${id}`),
  create: (userId, activityId) => api.post('/registrations', { userId, activityId }),
  cancel: (id) => api.delete(`/registrations/${id}`)
}

// CheckIn APIs
export const checkInAPI = {
  checkIn: (registrationId) => api.post('/checkins', { registrationId }),
  getByRegistrationId: (registrationId) => api.get(`/checkins/registration/${registrationId}`),
  getStatus: (registrationId) => api.get(`/checkins/status/${registrationId}`)
}

export default api
