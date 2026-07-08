import { createRouter, createWebHistory } from 'vue-router'
import MovieListView from '../views/MovieListView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: MovieListView
    }
  ]
})

export default router