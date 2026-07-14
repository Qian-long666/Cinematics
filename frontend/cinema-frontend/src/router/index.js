import { createRouter, createWebHistory } from 'vue-router'
import { isLoggedIn, isAdmin } from '@/auth.js'
import HomeView from '../views/HomeView.vue'
import MovieQueryView from '../views/MovieQueryView.vue'
import MovieCreateView from '../views/MovieCreateView.vue'
import MovieUpdateView from '../views/MovieUpdateView.vue'
import MovieDeleteView from '../views/MovieDeleteView.vue'
import MultiSearchView from '../views/MultiSearchView.vue'
import MovieDetailView from '../views/MovieDetailView.vue'
import LoginView from '../views/LoginView.vue'
import MyCommentsView from '../views/MyCommentsView.vue'


const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', name: 'login', component: LoginView },
    { path: '/', name: 'home', component: HomeView, meta: { requiresAuth: true } },
    { path: '/admin/movies/query', component: MovieQueryView, meta: { requiresAuth: true, requiresAdmin: true } },
    { path: '/admin/movies/create', component: MovieCreateView, meta: { requiresAuth: true, requiresAdmin: true } },
    { path: '/admin/movies/update/:id', component: MovieUpdateView, meta: { requiresAuth: true, requiresAdmin: true } },
    { path: '/admin/movies/delete/:id', component: MovieDeleteView, meta: { requiresAuth: true, requiresAdmin: true } },
    { path: '/multi-search', component: MultiSearchView, meta: { requiresAuth: true } },
    { path: '/my-comments', component: MyCommentsView, meta: { requiresAuth: true } },
    { path: '/movie/:id', component: MovieDetailView, meta: { requiresAuth: true } }
  ]
})

// ===== 全局路由守卫 =====
router.beforeEach((to, from, next) => {
  // 登录页：已登录 → 跳首页，未登录 → 放行
  if (to.path === '/login') {
    if (isLoggedIn()) {
      return next('/')
    }
    return next()
  }

  // 其他页面：未登录 → 跳登录页
  if (to.meta.requiresAuth && !isLoggedIn()) {
    return next('/login')
  }

  // 管理员页面：非管理员 → 跳首页
  if (to.meta.requiresAdmin && !isAdmin()) {
    return next('/')
  }

  next()
})

export default router
