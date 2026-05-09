import { createRouter, createWebHistory } from 'vue-router'
import { isLoggedIn, isAdmin } from '@/utils/auth'
import ActivityList from '../views/ActivityList.vue'
import ActivityDetail from '../views/ActivityDetail.vue'
import MyRegistrations from '../views/MyRegistrations.vue'
import CheckInPage from '../views/CheckInPage.vue'
import AdminActivities from '../views/AdminActivities.vue'
import Login from '../views/Login.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { requiresGuest: true }
  },
  {
    path: '/',
    name: 'ActivityList',
    component: ActivityList,
    meta: { requiresAuth: true }
  },
  {
    path: '/activity/:id',
    name: 'ActivityDetail',
    component: ActivityDetail,
    meta: { requiresAuth: true }
  },
  {
    path: '/my-registrations',
    name: 'MyRegistrations',
    component: MyRegistrations,
    meta: { requiresAuth: true }
  },
  {
    path: '/checkin',
    name: 'CheckIn',
    component: CheckInPage,
    meta: { requiresAuth: true }
  },
  {
    path: '/admin',
    name: 'Admin',
    component: AdminActivities,
    meta: { requiresAuth: true, requiresAdmin: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Navigation guards
router.beforeEach((to, from, next) => {
  const loggedIn = isLoggedIn()
  const admin = isAdmin()

  // Redirect to login if not authenticated
  if (to.meta.requiresAuth && !loggedIn) {
    next('/login')
  }
  // Redirect to home if already logged in and trying to access login
  else if (to.meta.requiresGuest && loggedIn) {
    next('/')
  }
  // Check admin permission
  else if (to.meta.requiresAdmin && !admin) {
    next('/')
  }
  else {
    next()
  }
})

export default router

