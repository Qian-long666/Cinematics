import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import MovieQueryView from '../views/MovieQueryView.vue'
import MovieCreateView from '../views/MovieCreateView.vue'
import MovieUpdateView from '../views/MovieUpdateView.vue'
import MovieDeleteView from '../views/MovieDeleteView.vue'
import MultiSearchView from '../views/MultiSearchView.vue'
import MovieDetailView from '../views/MovieDetailView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    { path: '/admin/movies/query', component: MovieQueryView },
    { path: '/admin/movies/create', component: MovieCreateView },
    { path: '/admin/movies/update/:id', component: MovieUpdateView },
    { path: '/admin/movies/delete/:id', component: MovieDeleteView },
    { path: '/multi-search', component: MultiSearchView },
    { path: '/movie/:id', component: MovieDetailView }
  ]
})

export default router