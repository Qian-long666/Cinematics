<template>
  <div class="page">

    <!-- ================================================================
         背景
         ================================================================ -->
    <div class="bg"></div>

    <!-- ================================================================
         左侧胶卷
         ================================================================ -->
    <div class="film film--left" :class="{ 'film--active': searched && movies.length > 0 }">
      <div class="film-track">
        <div v-for="n in 36" :key="'la'+n" class="film-frame"
             :class="'film-frame--' + ((n + frameOffset) % 5)"></div>
        <div v-for="n in 36" :key="'lb'+n" class="film-frame"
             :class="'film-frame--' + ((n + frameOffset) % 5)"></div>
      </div>
    </div>

    <!-- ================================================================
         右侧胶卷
         ================================================================ -->
    <div class="film film--right" :class="{ 'film--active': searched && movies.length > 0 }">
      <div class="film-track">
        <div v-for="n in 36" :key="'ra'+n" class="film-frame"
             :class="'film-frame--' + ((n + frameOffset + 2) % 5)"></div>
        <div v-for="n in 36" :key="'rb'+n" class="film-frame"
             :class="'film-frame--' + ((n + frameOffset + 2) % 5)"></div>
      </div>
    </div>

    <!-- ================================================================
         卡片容器
         ================================================================ -->
    <div class="container">

      <button class="back-btn" @click="$router.push('/')">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor"
             stroke-width="1.5" stroke-linecap="round"><path d="M19 12H5"/><path d="m12 19-7-7 7-7"/></svg>
        返回
      </button>

      <!-- 标题 -->
      <div class="head">
        <h1>博海拾贝</h1>
        <p>Advanced Movie Search</p>
        <span class="head-line"></span>
      </div>

      <!-- 搜索区域 -->
      <div class="search">
        <div class="search-fields">
          <!-- 名称 -->
          <div class="field">
            <input v-model="form.name" placeholder="电影名称" class="field-input"
                   @keyup.enter="handleSearch" />
            <span class="field-line"></span>
          </div>
          <!-- 年份 -->
          <div class="field field--sm">
            <input v-model="form.time" type="number" placeholder="年份" class="field-input"
                   @keyup.enter="handleSearch" />
            <span class="field-line"></span>
          </div>
          <!-- 大洲 -->
          <div class="field field--sm">
            <select v-model="continent" class="field-input field-select">
              <option value="">全部大洲</option>
              <option v-for="c in continentList" :key="c" :value="c">{{ c }}</option>
            </select>
            <span class="field-line"></span>
          </div>
          <!-- 国家 -->
          <div class="field field--sm">
            <select v-model="form.nation" class="field-input field-select"
                    :disabled="!continent">
              <option value="">{{ continent ? '全部国家' : '请先选大洲' }}</option>
              <option v-for="n in availableCountries" :key="n" :value="n">{{ n }}</option>
            </select>
            <span class="field-line"></span>
          </div>
        </div>
        <button class="search-btn" @click="handleSearch" :disabled="loading">
          <template v-if="!loading">搜索</template>
          <span v-else class="search-btn-spinner"></span>
        </button>
      </div>

      <!-- ================================================================
           结果区域
           ================================================================ -->
      <div class="result">
        <!-- 空态 -->
        <div v-if="!searched && !loading" class="result-empty">
          <svg width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="currentColor"
               stroke-width="1.2" stroke-linecap="round">
            <circle cx="11" cy="11" r="8"/><path d="m21 21-4.35-4.35"/>
          </svg>
          <span>Find your destiny</span>
        </div>

        <!-- 加载态 -->
        <div v-if="loading" class="result-loading">
          <span class="result-loading-dot"></span>
          <span class="result-loading-dot"></span>
          <span class="result-loading-dot"></span>
        </div>

        <!-- 搜索结果列表 -->
        <transition-group name="list" tag="div" class="result-list">
          <div v-if="!loading && movies.length > 0"
               v-for="(m,i) in movies" :key="m.id"
               class="result-card" :style="{ '--i': i }">
            <div class="result-card-left">
              <div class="result-card-top">
                <strong>{{ m.name }}</strong>
                <span class="result-card-year">{{ m.time }}</span>
              </div>
              <div class="result-card-meta">
                <span class="meta-tag">{{ m.nation || '未知国家' }}</span>
                <span v-if="m.genre" class="meta-tag">{{ m.genre }}</span>
                <span v-if="m.director" class="meta-tag">{{ m.director }}</span>
              </div>
            </div>
            <button @click="goToDetail(m.id)" class="result-card-btn">
              详情
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                   stroke-width="1.5" stroke-linecap="round">
                <path d="M5 12h14"/><path d="m12 5 7 7-7 7"/>
              </svg>
            </button>
          </div>
        </transition-group>

        <!-- 未找到 -->
        <transition name="card">
          <div v-if="!loading && searched && movies.length === 0"
               class="result-empty result-empty--fail">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                 stroke-width="1.2" stroke-linecap="round">
              <circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/>
              <line x1="12" y1="16" x2="12.01" y2="16"/>
            </svg>
            <span>未找到匹配的电影</span>
          </div>
        </transition>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { movieService } from '@/services/movieService'

const router = useRouter()
const form       = ref({ name: '', time: '', nation: '' })
const movies     = ref([])
const loading    = ref(false)
const searched   = ref(false)
const continent  = ref('')

/* 画帧 offset —— 让两侧胶卷帧号错开 */
const frameOffset = computed(() => searched.value ? Math.floor(Date.now() / 300) % 5 : 0)

/* 大洲 → 国家映射 */
const continentMap = {
  '亚洲': ['中国','日本','韩国','印度','伊朗'],
  '欧洲': ['英国','法国','德国','意大利','俄罗斯','丹麦','瑞典'],
  '美洲': ['美国','阿根廷','墨西哥'],
  '其他': ['澳大利亚','新西兰'],
}
const continentList = Object.keys(continentMap)

const availableCountries = computed(() => {
  if (!continent.value) return []
  return continentMap[continent.value] || []
})

/* 切换大洲时清空国家 */
watch(continent, () => { form.value.nation = '' })

async function handleSearch() {
  loading.value = true
  searched.value = true
  const params = {}
  if (form.value.name)   params.name   = form.value.name
  if (form.value.time)   params.time   = form.value.time
  if (form.value.nation) params.nation = form.value.nation
  try {
    movies.value = (await movieService.searchMovies(params)) || []
  } catch {
    movies.value = []
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
  --amber:        #D4A574;
  --amber-soft:   rgba(212,165,116,0.25);
  --amber-dim:    rgba(212,165,116,0.10);
  --cream:        #EDE4DB;
  --cream-dim:    rgba(237,228,219,0.50);
  --cream-faint:  rgba(237,228,219,0.12);
  --cream-clear:  rgba(237,228,219,0.05);
  --base:         #0D0B0F;
  --surface:      rgba(23,20,26,0.55);
  --ease-out:     cubic-bezier(0,0,0.2,1);
  --ease-spring:  cubic-bezier(0.34,1.56,0.64,1);

  position: relative;
  width: 100vw; min-height: 100vh;
  display: flex; align-items: flex-start; justify-content: center;
  padding: 80px 80px 60px;
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
   FILM STRIP — 仿 35mm 胶卷
   ================================================================== */
.film {
  position: fixed; top: 0; bottom: 0;
  width: 46px; z-index: 0; overflow: hidden;
  /* 胶片基底 */
  background: #0d0b0c;
  /* 两侧齿孔 */
  border-left:  14px solid #0d0b0c;
  border-right: 14px solid #0d0b0c;
  box-shadow:
    /* 左齿孔列 */
      inset 6px 0 0 #0d0b0c,
        /* 右齿孔列 */
      inset -6px 0 0 #0d0b0c;
}
.film::before,
.film::after {
  content: '';
  position: absolute; top: -20px; bottom: -20px;
  width: 4px; z-index: 2;
  /* 齿孔：透亮小方块 */
  background: repeating-linear-gradient(
      to bottom,
      transparent 0px,
      transparent 8px,
      rgba(237,228,219,0.28) 8px,
      rgba(237,228,219,0.28) 13px,
      transparent 13px,
      transparent 24px
  );
}
.film::before  { left: 2px;  border-radius: 0 1px 1px 0; }
.film::after   { right: 2px; border-radius: 1px 0 0 1px; }

/* === 激活态：齿孔变暖 === */
.film--active::before,
.film--active::after {
  background: repeating-linear-gradient(
      to bottom,
      transparent 0px,
      transparent 8px,
      rgba(212,165,116,0.45) 8px,
      rgba(212,165,116,0.45) 13px,
      transparent 13px,
      transparent 24px
  );
}

.film--left  { left: 18px; }
.film--right { right: 18px; }

/* === 画帧轨道 === */
.film-track {
  position: absolute; top: 0; left: 0; right: 0;
  display: flex; flex-direction: column;
  animation: filmRoll 12s linear infinite;
}
/* 激活：反向 + 加速 */
.film--active .film-track {
  animation-name: filmRollFast;
  animation-duration: 4.5s;
}

@keyframes filmRoll {
  from { transform: translateY(-600px); }
  to   { transform: translateY(0); }
}
@keyframes filmRollFast {
  from { transform: translateY(0); }
  to   { transform: translateY(-600px); }
}

/* === 单个画帧 === */
.film-frame {
  height: 32px; margin: 2px 4px;
  border-radius: 1px;
  flex-shrink: 0;
}
/* 5 种画帧色相，模拟不同场景的胶片画面 */
.film-frame--0 { background: linear-gradient(180deg, #1a1a1c 0%, #222125 50%, #18171a 100%); }
.film-frame--1 { background: linear-gradient(180deg, #1c1a17 0%, #241f1b 50%, #1a1715 100%); }
.film-frame--2 { background: linear-gradient(180deg, #161618 0%, #1c1b1f 50%, #151418 100%); }
.film-frame--3 { background: linear-gradient(180deg, #1a1816 0%, #201d1a 50%, #181614 100%); }
.film-frame--4 { background: linear-gradient(180deg, #181719 0%, #1e1c21 50%, #161419 100%); }

/* 激活：暖色画帧 */
.film--active .film-frame--0 { background: linear-gradient(180deg, #1f1a15 0%, #2a221b 50%, #1c1713 100%); }
.film--active .film-frame--1 { background: linear-gradient(180deg, #201b16 0%, #2d241c 50%, #1d1814 100%); }
.film--active .film-frame--2 { background: linear-gradient(180deg, #1e1915 0%, #282119 50%, #1b1612 100%); }
.film--active .film-frame--3 { background: linear-gradient(180deg, #211c17 0%, #2c231b 50%, #1e1915 100%); }
.film--active .film-frame--4 { background: linear-gradient(180deg, #1d1814 0%, #272018 50%, #1a1511 100%); }

/* ==================================================================
   CONTAINER
   ================================================================== */
.container {
  position: relative; z-index: 1;
  width: 100%; max-width: 680px;
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

.back-btn {
  display: inline-flex; align-items: center; gap: 6px;
  padding: 7px 16px; margin-bottom: 24px;
  border: 1px solid rgba(237,228,219,0.08);
  border-radius: 8px;
  background: rgba(237,228,219,0.03);
  color: var(--cream-dim);
  font-size: 12px; font-family: inherit; letter-spacing: 1px; cursor: pointer;
  transition: all 0.3s var(--ease-out);
}
.back-btn:hover {
  background: rgba(237,228,219,0.06);
  border-color: rgba(237,228,219,0.15);
  color: var(--cream);
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
.search-fields { flex: 1; display: flex; gap: 12px; flex-wrap: wrap; }
.field { flex: 2; position: relative; min-width: 100px; }
.field--sm { flex: 1; }

.field-input {
  width: 100%; padding: 14px 0 10px;
  border: none; outline: none; background: none;
  font-size: 13px; color: var(--cream);
  font-family: inherit; letter-spacing: 1px;
  caret-color: #D4A574;
}
.field-input::placeholder {
  color: rgba(237,228,219,0.18);
  letter-spacing: 1px; font-size: 12px;
}
.field-input::-webkit-outer-spin-button,
.field-input::-webkit-inner-spin-button { -webkit-appearance: none; margin: 0; }
.field-input[type=number] { -moz-appearance: textfield; }

.field-select {
  cursor: pointer;
  appearance: none; -webkit-appearance: none;
  background-image: url("data:image/svg+xml,%3Csvg width='10' height='6' viewBox='0 0 10 6' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M1 1l4 4 4-4' stroke='%23EDE4DB' stroke-opacity='0.3' fill='none' stroke-width='1.2' stroke-linecap='round'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 4px center;
  padding-right: 18px;
}
.field-select:disabled {
  opacity: 0.3; cursor: not-allowed;
}
.field-select option { background: #1E1B22; color: #EDE4DB; }

.field-line {
  position: absolute; bottom: 0; left: 0; right: 0; height: 1px;
  background: var(--cream-faint);
  transition: background 0.3s var(--ease-out);
}
.field-input:focus + .field-line::after {
  content: '';
  position: absolute; bottom: 0; left: 50%; transform: translateX(-50%);
  width: 100%; height: 1.5px;
  background: #D4A574;
  animation: lineExpand 0.4s var(--ease-out) both;
}
@keyframes lineExpand {
  from { width: 0; }
  to   { width: 100%; }
}

.search-btn {
  flex-shrink: 0; position: relative;
  padding: 12px 28px;
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

.result-list { display: flex; flex-direction: column; gap: 8px; }

.result-card {
  display: flex; align-items: center; gap: 16px;
  background: rgba(212,165,116,0.04);
  border: 1px solid rgba(237,228,219,0.06);
  border-radius: 14px;
  padding: 18px 20px;
  transition: background 0.3s var(--ease-out), border-color 0.3s var(--ease-out);
}
.result-card:hover {
  background: rgba(212,165,116,0.07);
  border-color: rgba(237,228,219,0.10);
}
.result-card-left { flex: 1; min-width: 0; }

.result-card-top {
  display: flex; align-items: baseline; gap: 8px;
  margin-bottom: 8px;
}
.result-card-top strong {
  font-size: 16px; font-weight: 700; color: var(--cream);
  letter-spacing: 1px;
  overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
}
.result-card-year {
  font-size: 12px; color: var(--cream-dim);
  font-family: Georgia,serif; flex-shrink: 0;
}
.result-card-meta { display: flex; flex-wrap: wrap; gap: 6px; }
.meta-tag {
  padding: 3px 8px;
  border-radius: 5px;
  font-size: 10px; color: var(--cream-dim);
  background: rgba(237,228,219,0.04);
  border: 1px solid rgba(237,228,219,0.06);
  letter-spacing: 0.5px;
}
.result-card-btn {
  display: inline-flex; align-items: center; gap: 5px; flex-shrink: 0;
  padding: 8px 16px;
  border: 1px solid rgba(212,165,116,0.18);
  border-radius: 10px;
  background: rgba(212,165,116,0.06);
  color: #D4A574;
  font-size: 12px; font-family: inherit; font-weight: 500;
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
   VUE TRANSITIONS
   ================================================================== */
.list-enter-active { transition: all 0.45s var(--ease-spring); }
.list-leave-active { transition: all 0.18s ease-in; }
.list-enter-from  { opacity: 0; transform: translateY(10px) scale(0.97); }
.list-leave-to    { opacity: 0; transform: translateY(-8px); }

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
@media (max-width: 900px) {
  .film { display: none; }
  .page { padding: 40px 16px 40px; }
}

@media (max-width: 640px) {
  .container { padding: 28px 20px; border-radius: 18px; }
  .head h1 { font-size: 20px; letter-spacing: 3px; }
  .search { flex-direction: column; gap: 16px; }
  .search-fields { flex-direction: column; gap: 20px; }
  .field { flex: none; min-width: 0; }
  .search-btn { width: 100%; padding: 13px; }
  .result-card { flex-direction: column; align-items: stretch; gap: 14px; padding: 16px; }
  .result-card-btn { justify-content: center; }
  .result-card-top { flex-direction: column; gap: 2px; }
}
</style>
