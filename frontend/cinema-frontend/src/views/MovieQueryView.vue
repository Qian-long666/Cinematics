<template>
  <div class="page">

    <!-- ================================================================
         背景
         ================================================================ -->
    <div class="bg"></div>

    <!-- ================================================================
         卡片容器
         ================================================================ -->
    <div class="container">
      <!-- 标题 -->
      <div class="head">
        <h1>查询电影</h1>
        <p>Search Movie by Identity</p>
        <span class="head-line"></span>
      </div>

      <!-- 搜索区域 -->
      <div class="search">
        <div class="search-input-wrap">
          <input
              v-model="movieId"
              type="number"
              placeholder="Enter the movie ID"
              class="search-input"
              @keyup.enter="handleQuery"
          />
          <span class="search-input-line"></span>
        </div>
        <button class="search-btn" @click="handleQuery" :disabled="loading">
          <template v-if="!loading">查询</template>
          <span v-else class="search-btn-spinner"></span>
        </button>
      </div>

      <!-- 结果区域 -->
      <div class="result">
        <!-- 空态 -->
        <div v-if="!queried && !loading" class="result-empty">
          <svg width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.2" stroke-linecap="round">
            <circle cx="11" cy="11" r="8"/>
            <path d="m21 21-4.35-4.35"/>
          </svg>
          <span>Find your destiny</span>
        </div>

        <!-- 加载态 -->
        <div v-if="loading" class="result-loading">
          <span class="result-loading-dot"></span>
          <span class="result-loading-dot"></span>
          <span class="result-loading-dot"></span>
        </div>

        <!-- 查询成功 -->
        <transition name="card">
          <div v-if="!loading && movie" class="result-card">
            <div class="result-card-top">
              <strong>{{ movie.name }}</strong>
              <span class="result-card-year">{{ movie.time }}</span>
            </div>
            <div class="result-card-meta">
              <span class="meta-tag">{{ movie.nation || '未知国家' }}</span>
              <span v-if="movie.genre" class="meta-tag">{{ movie.genre }}</span>
              <span v-if="movie.director" class="meta-tag">{{ movie.director }}</span>
            </div>
            <div class="result-card-bottom">
              <span class="result-card-id"># {{ movie.id }}</span>
              <button @click="goToDetail(movie.id)" class="result-card-btn">
                查看详情
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round">
                  <path d="M5 12h14"/><path d="m12 5 7 7-7 7"/>
                </svg>
              </button>
            </div>
          </div>
        </transition>

        <!-- 未找到 -->
        <transition name="card">
          <div v-if="!loading && queried && !movie" class="result-empty result-empty--fail">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.2" stroke-linecap="round">
              <circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/>
            </svg>
            <span>未找到 ID 为 <em>{{ movieId }}</em> 的电影</span>
          </div>
        </transition>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { movieService } from '@/services/movieService'

const router = useRouter()
const movieId = ref('')
const movie = ref(null)
const loading = ref(false)
const queried = ref(false)

async function handleQuery() {
  if (!movieId.value) return
  loading.value = true
  queried.value = true
  try {
    movie.value = await movieService.getMovieById(movieId.value)
  } catch {
    movie.value = null
  } finally {
    loading.value = false
  }
}

function goToDetail(id) {
  router.push(`/movie/${id}`)
}
</script>

<style scoped>
/* ==================================================================
   TOKENS
   ================================================================== */
.page {
  --amber:       #D4A574;
  --amber-soft:  rgba(212,165,116,0.25);
  --amber-dim:   rgba(212,165,116,0.10);
  --cream:       #EDE4DB;
  --cream-dim:   rgba(237,228,219,0.50);
  --cream-faint: rgba(237,228,219,0.12);
  --base:        #0D0B0F;
  --surface:     rgba(23,20,26,0.55);
  --ease-out:    cubic-bezier(0,0,0.2,1);
  --ease-spring: cubic-bezier(0.34,1.56,0.64,1);

  position: relative;
  width: 100vw; min-height: 100vh;
  display: flex; align-items: flex-start; justify-content: center;
  padding: 80px 20px 60px;
  font-family: 'PingFang SC','Microsoft YaHei',Georgia,'Noto Serif SC',serif;
  user-select: none;
  background: var(--base);
}

/* ==================================================================
   BACKGROUND
   ================================================================== */
.bg {
  position: fixed; inset: 0; z-index: 0;
  background:
      radial-gradient(ellipse at 50% 25%, rgba(212,165,116,0.025) 0%, transparent 55%),
      radial-gradient(ellipse at 30% 70%, rgba(139,126,116,0.02) 0%, transparent 45%),
      var(--base);
}

/* ==================================================================
   CONTAINER
   ================================================================== */
.container {
  position: relative; z-index: 1;
  width: 100%; max-width: 580px;
  padding: 40px 44px;
  background: var(--surface);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(237,228,219,0.06);
  border-radius: 24px;
  box-shadow:
      0 20px 60px rgba(0,0,0,0.45),
      inset 0 1px 0 rgba(255,255,255,0.015);
}

/* ==================================================================
   HEAD
   ================================================================== */
.head { text-align: center; margin-bottom: 36px; }
.head h1 {
  margin: 0;
  font-size: 24px; font-weight: 700; color: var(--cream);
  letter-spacing: 4px;
}
.head p {
  margin: 6px 0 0;
  font-size: 10px; color: var(--cream-dim);
  letter-spacing: 5px;
  font-family: Georgia,'Times New Roman',serif;
  text-transform: uppercase;
}
.head-line {
  display: block; width: 48px; height: 1px;
  margin: 16px auto 0;
  background: linear-gradient(90deg, transparent, var(--amber-soft), transparent);
}

/* ==================================================================
   SEARCH
   ================================================================== */
.search { display: flex; gap: 12px; align-items: flex-end; margin-bottom: 32px; }

.search-input-wrap { flex: 1; position: relative; }
.search-input {
  width: 100%; padding: 14px 0 10px;
  border: none; outline: none; background: none;
  font-size: 16px; color: var(--cream);
  font-family: inherit; letter-spacing: 1px;
  caret-color: #D4A574;
}
.search-input::placeholder {
  color: rgba(237,228,219,0.22);
  letter-spacing: 2px; font-size: 14px;
}
.search-input::-webkit-outer-spin-button,
.search-input::-webkit-inner-spin-button { -webkit-appearance: none; margin: 0; }
.search-input[type=number] { -moz-appearance: textfield; }

.search-input-line {
  position: absolute; bottom: 0; left: 0; right: 0; height: 1px;
  background: var(--cream-faint);
  transition: background 0.3s var(--ease-out);
}
.search-input-line::after {
  content: '';
  position: absolute; bottom: 0; left: 50%; transform: translateX(-50%);
  width: 0; height: 1.5px;
  background: #D4A574;
  transition: width 0.4s var(--ease-out);
}
.search-input:focus + .search-input-line::after { width: 100%; }

.search-btn {
  flex-shrink: 0; position: relative;
  padding: 11px 28px;
  border: 1px solid rgba(212,165,116,0.2);
  border-radius: 12px;
  background: rgba(212,165,116,0.08);
  color: #D4A574;
  font-size: 14px; font-family: inherit; font-weight: 600;
  letter-spacing: 2px; cursor: pointer;
  transition: all 0.35s var(--ease-out);
}
.search-btn:hover {
  background: rgba(212,165,116,0.16);
  border-color: rgba(212,165,116,0.4);
  box-shadow: 0 0 24px rgba(212,165,116,0.12);
}
.search-btn:active { transform: scale(0.96); transition: transform 0.08s ease; }
.search-btn:disabled { opacity: 0.5; cursor: not-allowed; pointer-events: none; }

.search-btn-spinner {
  display: block; width: 16px; height: 16px;
  border: 2px solid rgba(212,165,116,0.2);
  border-top-color: #D4A574;
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }

/* ==================================================================
   RESULT
   ================================================================== */
.result { min-height: 120px; }

.result-empty {
  display: flex; flex-direction: column; align-items: center; gap: 12px;
  padding: 36px 0 20px;
  color: rgba(237,228,219,0.15);
  font-size: 13px; letter-spacing: 1.5px;
}
.result-empty--fail { color: rgba(237,228,219,0.25); }
.result-empty--fail em {
  font-style: normal; color: var(--amber-soft); font-weight: 600;
}

.result-loading {
  display: flex; justify-content: center; gap: 8px;
  padding: 36px 0;
}
.result-loading-dot {
  width: 6px; height: 6px; border-radius: 50%;
  background: #D4A574;
  animation: dotBounce 1.1s ease-in-out infinite;
}
.result-loading-dot:nth-child(2) { animation-delay: 0.15s; }
.result-loading-dot:nth-child(3) { animation-delay: 0.30s; }
@keyframes dotBounce {
  0%,80%,100% { transform: translateY(0); opacity: 0.3; }
  40%         { transform: translateY(-10px); opacity: 1; }
}

.result-card {
  background: rgba(212,165,116,0.04);
  border: 1px solid rgba(237,228,219,0.06);
  border-radius: 16px;
  padding: 20px 22px;
}
.result-card-top {
  display: flex; align-items: baseline; gap: 10px;
  margin-bottom: 10px;
}
.result-card-top strong {
  font-size: 18px; font-weight: 700; color: var(--cream);
  letter-spacing: 1px;
}
.result-card-year {
  font-size: 12px; color: var(--cream-dim);
  font-family: Georgia,serif;
}
.result-card-meta {
  display: flex; flex-wrap: wrap; gap: 8px;
  margin-bottom: 18px;
}
.meta-tag {
  padding: 4px 10px;
  border-radius: 6px;
  font-size: 11px; color: var(--cream-dim);
  background: rgba(237,228,219,0.04);
  border: 1px solid rgba(237,228,219,0.06);
  letter-spacing: 0.5px;
}
.result-card-bottom {
  display: flex; justify-content: space-between; align-items: center;
  padding-top: 14px;
  border-top: 1px solid rgba(237,228,219,0.05);
}
.result-card-id {
  font-size: 11px; color: rgba(237,228,219,0.2);
  font-family: Georgia,serif; letter-spacing: 1px;
}
.result-card-btn {
  display: inline-flex; align-items: center; gap: 6px;
  padding: 8px 18px;
  border: 1px solid rgba(212,165,116,0.18);
  border-radius: 10px;
  background: rgba(212,165,116,0.06);
  color: #D4A574;
  font-size: 13px; font-family: inherit; font-weight: 500;
  letter-spacing: 1px; cursor: pointer;
  transition: all 0.3s var(--ease-out);
}
.result-card-btn:hover {
  background: rgba(212,165,116,0.14);
  border-color: rgba(212,165,116,0.35);
  box-shadow: 0 0 16px rgba(212,165,116,0.10);
}
.result-card-btn:hover svg { transform: translateX(2px); }
.result-card-btn svg { transition: transform 0.3s var(--ease-out); }
.result-card-btn:active { transform: scale(0.96); transition: transform 0.08s ease; }

/* ==================================================================
   VUE TRANSITION
   ================================================================== */
.card-enter-active { animation: cardIn 0.45s var(--ease-spring) both; }
.card-leave-active { animation: cardOut 0.18s ease-in both; }
@keyframes cardIn {
  from { opacity: 0; transform: translateY(12px) scale(0.97); }
  to   { opacity: 1; transform: translateY(0) scale(1); }
}
@keyframes cardOut {
  to { opacity: 0; transform: translateY(-8px); }
}

/* ==================================================================
   RESPONSIVE
   ================================================================== */
@media (max-width: 640px) {
  .page { padding: 40px 12px 40px; }
  .container { padding: 28px 22px; border-radius: 18px; }
  .head h1 { font-size: 20px; letter-spacing: 3px; }
  .search { flex-direction: column; gap: 16px; }
  .search-btn { width: 100%; padding: 13px; }
  .result-card { padding: 16px; }
  .result-card-top { flex-direction: column; gap: 2px; }
}
</style>
