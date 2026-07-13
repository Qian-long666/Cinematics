<template>
  <div class="page">

    <div class="bg"></div>

    <!-- ================================================================
         加载态
         ================================================================ -->
    <div v-if="loading" class="state">
      <span class="state-dot"></span><span class="state-dot"></span><span class="state-dot"></span>
      <p>加载中...</p>
    </div>

    <!-- ================================================================
         未找到
         ================================================================ -->
    <div v-else-if="!movie" class="state">
      <svg width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="currentColor"
           stroke-width="1.2" stroke-linecap="round">
        <circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/>
        <line x1="12" y1="16" x2="12.01" y2="16"/>
      </svg>
      <p>未找到该电影</p>
    </div>

    <!-- ================================================================
         右侧导航栏
         ================================================================ -->
    <nav v-if="movie" class="nav">
      <div class="nav-track">
        <span class="nav-track-fill" :style="{ height: navProgress + '%' }"></span>
      </div>
      <button v-for="item in navItems" :key="item.id"
              class="nav-dot"
              :class="{ 'nav-dot--active': item.id === activeSection }"
              :aria-label="item.label"
              @click="scrollTo(item)">
        <span class="nav-dot-label">{{ item.label }}</span>
      </button>
    </nav>

    <!-- ================================================================
         详情内容
         ================================================================ -->
    <div v-if="movie" class="detail">

      <!-- ════════════════════════════════════════════════════════════
           一、HERO · 左信息 / 右海报
           ════════════════════════════════════════════════════════════ -->
      <section ref="secHero" class="sec hero-card">

        <button class="back-btn" @click="goBack">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor"
               stroke-width="1.5" stroke-linecap="round"><path d="M19 12H5"/><path d="m12 19-7-7 7-7"/></svg>
          返回
        </button>

        <div class="hero-body">
          <!-- 左：信息 -->
          <div class="hero-info">
            <div class="hero-title-block">
              <span class="hero-title-accent"></span>
              <div>
                <h1>{{ movie.name }}</h1>
                <span class="hero-title-en">{{ movie.name || 'Film Title' }}</span>
              </div>
            </div>

            <div class="hero-tags">
              <span class="hero-tag">{{ movie.time || '未知年份' }}</span>
              <span class="hero-tag">{{ movie.type || '未分类' }}</span>
              <span class="hero-tag">{{ movie.nation || '未知国家' }}</span>
            </div>

            <div class="hero-credits">
              <div class="hero-credit">
                <span class="hero-credit-label">导 演</span>
                <span class="hero-credit-name">{{ movie.director || '未知' }}</span>
              </div>
              <div class="hero-credit">
                <span class="hero-credit-label">主 演</span>
                <span class="hero-credit-name">{{ movie.actor || '未知' }}</span>
              </div>
            </div>
          </div>

          <!-- 右：海报 -->
          <div class="hero-poster">
            <div class="hero-poster-frame">
              <img v-if="movie.poster_url" :src="movie.poster_url" alt="电影海报" />
              <div v-else class="hero-poster-fb">
                <svg width="56" height="56" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                     stroke-width="0.8"><rect x="2" y="2" width="20" height="20" rx="2.5"/>
                  <circle cx="8.5" cy="8.5" r="1.5"/><path d="m21 15-5-5L5 21"/></svg>
                <span>暂无海报</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <span class="sec-divider"></span>

      <!-- ════════════════════════════════════════════════════════════
           二、经典台词 · 翻牌
           ════════════════════════════════════════════════════════════ -->
      <section ref="secQuotes" class="sec">
        <div class="sec-head"><h2>经典台词</h2><p>Memorable Lines</p></div>
        <div v-if="movie.lines && movie.lines.length" class="quotes-grid">
          <div v-for="line in movie.lines" :key="line.id" class="quote-card">
            <div class="quote-inner">
              <div class="quote-front">
                <span class="quote-mark">"</span>
                <span class="quote-text">{{ line.quoteOri }}</span>
              </div>
              <div class="quote-back">
                <span class="quote-text-zh">{{ line.quoteZh }}</span>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="empty-hint">暂无收录台词</div>
      </section>

      <span class="sec-divider"></span>

      <!-- ════════════════════════════════════════════════════════════
           三、经典镜头 · 瀑布流
           ════════════════════════════════════════════════════════════ -->
      <section ref="secScenes" class="sec">
        <div class="sec-head"><h2>经典镜头</h2><p>Iconic Scenes</p></div>
        <div v-if="movie.scenes && movie.scenes.length" class="scenes-wall">
          <div v-for="scene in movie.scenes" :key="scene.id" class="scene-brick">
            <img :src="scene.imageUrl" :alt="scene.caption || '经典画面'" />
            <span v-if="scene.caption" class="scene-cap">{{ scene.caption }}</span>
          </div>
        </div>
        <div v-else class="empty-hint">暂无收录画面</div>
      </section>

      <span class="sec-divider"></span>

      <!-- ════════════════════════════════════════════════════════════
           四、优秀创作 · 行人街道
           ════════════════════════════════════════════════════════════ -->
      <section ref="secLinks" class="sec">
        <div class="sec-head"><h2>优秀创作</h2><p>Creative Works</p></div>
        <div v-if="movie.links && movie.links.length" class="links-street">
          <a v-for="(link, i) in movie.links" :key="link.id"
             :href="link.url" target="_blank" class="link-card"
             :style="{ animationDelay: (i*0.35).toFixed(1)+'s', animationDuration: (3.2+i*0.4).toFixed(1)+'s' }">
            <span class="link-card-icon">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                   stroke-width="1.5" stroke-linecap="round"><path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6"/>
                <polyline points="15 3 21 3 21 9"/><line x1="10" y1="14" x2="21" y2="3"/></svg>
            </span>
            <span class="link-card-text">{{ link.title || link.platform || '链接' }}</span>
          </a>
        </div>
        <div v-else class="empty-hint">暂无相关创作</div>
      </section>

      <span class="sec-divider"></span>

      <!-- ════════════════════════════════════════════════════════════
           五、评论 · 外壳
           ════════════════════════════════════════════════════════════ -->
      <section ref="secComments" class="sec">
        <div class="sec-head"><h2>评论</h2><p>Comments &amp; Reviews</p></div>
        <div class="comment-form">
          <input placeholder="你的名字（可选）" class="comment-input comment-input--name" />
          <input placeholder="写评论..." class="comment-input comment-input--body" />
          <button class="comment-submit">发布</button>
        </div>
        <div v-if="movie.comments && movie.comments.length" class="comment-list">
          <div v-for="c in movie.comments" :key="c.id" class="comment-item">
            <div class="comment-item-head"><strong>{{ c.username || '匿名' }}</strong><span>{{ c.createdAt || '' }}</span></div>
            <p>{{ c.content }}</p>
          </div>
        </div>
        <div v-else class="empty-hint">还没有评论，来说点什么吧</div>
      </section>

    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { movieService } from '@/services/movieService'

const route = useRoute()
const router = useRouter()
const movie = ref(null)
const loading = ref(true)

const secHero     = ref(null)
const secQuotes   = ref(null)
const secScenes   = ref(null)
const secLinks    = ref(null)
const secComments = ref(null)

const navItems = [
  { id: 'hero',     ref: secHero,     label: '概览' },
  { id: 'quotes',   ref: secQuotes,   label: '台词' },
  { id: 'scenes',   ref: secScenes,   label: '镜头' },
  { id: 'links',    ref: secLinks,    label: '创作' },
  { id: 'comments', ref: secComments, label: '评论' },
]

const activeSection = ref('hero')
const navProgress = ref(0)

function onScroll() {
  const y = window.scrollY + window.innerHeight / 3
  let a = navItems[0].id
  for (const item of navItems) {
    const el = item.ref
    if (el && el.offsetTop <= y) a = item.id
  }
  activeSection.value = a
  const h = document.documentElement.scrollHeight - window.innerHeight
  navProgress.value = h > 0 ? Math.min(100, (window.scrollY / h) * 100) : 0
}

function scrollTo(item) {
  const el = item.ref
  if (el) {
    el.scrollIntoView({ behavior: 'smooth', block: 'start' })
  }
}

onMounted(async () => {
  try {
    movie.value = await movieService.getMovieById(route.params.id)
  } catch {
    movie.value = null
  } finally {
    loading.value = false
  }
  window.addEventListener('scroll', onScroll, { passive: true })
  onScroll()
})

onUnmounted(() => {
  window.removeEventListener('scroll', onScroll)
})

function goBack() { router.back() }
</script>

<style scoped>
/* ==================================================================
   TOKENS
   ================================================================== */
.page {
  --amber:        #D4A574;
  --amber-soft:   rgba(212,165,116,0.30);
  --amber-dim:    rgba(212,165,116,0.12);
  --cream:        #EDE4DB;
  --cream-dim:    rgba(237,228,219,0.50);
  --cream-faint:  rgba(237,228,219,0.12);
  --cream-clear:  rgba(237,228,219,0.05);
  --base:         #0D0B0F;
  --surface:      rgba(23,20,26,0.50);
  --ease-out:     cubic-bezier(0,0,0.2,1);
  --ease-spring:  cubic-bezier(0.34,1.56,0.64,1);

  position: relative;
  width: 100%; min-height: 100vh;
  overflow-x: hidden;
  font-family: 'PingFang SC','Microsoft YaHei',Georgia,'Noto Serif SC',serif;
  background: var(--base); color: var(--cream);
  box-sizing: border-box;
}
*,
*::before,
*::after { box-sizing: inherit; }

.bg {
  position: fixed; inset: 0; z-index: 0;
  background:
      radial-gradient(ellipse at 50% 25%, rgba(212,165,116,0.025) 0%, transparent 55%),
      radial-gradient(ellipse at 30% 70%, rgba(139,126,116,0.02) 0%, transparent 45%),
      var(--base);
}

/* ==================================================================
   STATE
   ================================================================== */
.state {
  position: relative; z-index: 1;
  display: flex; flex-direction: column; align-items: center; justify-content: center;
  gap: 16px; min-height: 100vh;
  color: rgba(237,228,219,0.25); font-size: 14px; letter-spacing: 2px;
}
.state-dot {
  width: 6px; height: 6px; border-radius: 50%; background: #D4A574;
  animation: dotBounce 1.1s ease-in-out infinite;
}
.state-dot:nth-child(2) { animation-delay: 0.15s; }
.state-dot:nth-child(3) { animation-delay: 0.30s; }
@keyframes dotBounce {
  0%,80%,100% { transform: translateY(0); opacity: 0.3; }
  40%         { transform: translateY(-10px); opacity: 1; }
}

/* ==================================================================
   NAV · 右侧导航（提亮版）
   ================================================================== */
.nav {
  position: fixed; right: 20px; top: 50%; transform: translateY(-50%);
  z-index: 50; display: flex; flex-direction: column; align-items: center;
  gap: 22px;
}
.nav-track {
  position: absolute; top: 4px; bottom: 4px; width: 1px;
  /* 轨道可见 */
  background: rgba(237,228,219,0.10);
  border-radius: 1px;
}
.nav-track-fill {
  position: absolute; top: 0; left: 0; right: 0;
  background: rgba(212,165,116,0.55);
  border-radius: 1px;
  box-shadow: 0 0 6px rgba(212,165,116,0.25);
  transition: height 0.25s ease;
}
.nav-dot {
  position: relative; z-index: 1;
  width: 8px; height: 8px; border-radius: 50%;
  /* 默认可见 */
  border: 1.5px solid rgba(237,228,219,0.20);
  background: transparent;
  cursor: pointer; padding: 0;
  transition: all 0.35s var(--ease-out);
}
.nav-dot:hover {
  border-color: rgba(212,165,116,0.5);
  background: rgba(212,165,116,0.2);
}
.nav-dot--active {
  background: #D4A574;
  border-color: #D4A574;
  box-shadow: 0 0 14px rgba(212,165,116,0.55);
}
.nav-dot-label {
  position: absolute; right: 14px; top: 50%; transform: translateY(-50%);
  font-size: 9px;
  /* 标签可见 */
  color: rgba(237,228,219,0);
  letter-spacing: 1px; white-space: nowrap;
  transition: color 0.35s ease;
  pointer-events: none;
}
.nav-dot--active .nav-dot-label,
.nav-dot:hover .nav-dot-label { color: rgba(237,228,219,0.45); }

/* ==================================================================
   DETAIL
   ================================================================== */
.detail {
  position: relative; z-index: 1;
  max-width: 940px; width: 100%;
  margin: 0 auto;
  padding: 48px 28px 80px;
}
.sec { margin-bottom: 48px; }
.sec-divider {
  display: block; height: 1px;
  background: linear-gradient(90deg, transparent, rgba(237,228,219,0.05), transparent);
  margin-bottom: 48px;
}
.sec-head { text-align: center; margin-bottom: 32px; }
.sec-head h2 {
  margin: 0; font-size: 18px; font-weight: 700;
  color: var(--cream); letter-spacing: 4px;
}
.sec-head p {
  margin: 6px 0 0; font-size: 9px; color: var(--cream-dim);
  letter-spacing: 4px; font-family: Georgia,'Times New Roman',serif;
  text-transform: uppercase;
}
.empty-hint {
  text-align: center; padding: 32px 0;
  color: rgba(237,228,219,0.12); font-size: 13px; letter-spacing: 2px;
}

/* ==================================================================
   ONE · HERO
   ================================================================== */
.hero-card {
  background: var(--surface);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(237,228,219,0.07);
  border-radius: 28px;
  padding: 28px 36px 36px;
  box-shadow: 0 24px 72px rgba(0,0,0,0.5), inset 0 1px 0 rgba(255,255,255,0.01);
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

.hero-body { display: flex; gap: 48px; align-items: stretch; }

/* 左：信息 */
.hero-info { flex: 1; display: flex; flex-direction: column; justify-content: center; padding: 12px 0; }

.hero-title-block { display: flex; align-items: flex-start; gap: 20px; margin-bottom: 20px; }
.hero-title-accent {
  flex-shrink: 0; width: 3px; height: 72px;
  background: linear-gradient(180deg, #D4A574 0%, rgba(212,165,116,0.15) 100%);
  border-radius: 2px; margin-top: 6px;
}
.hero-title-block h1 {
  margin: 0;
  font-size: 44px; font-weight: 800;
  color: var(--cream); letter-spacing: 6px; line-height: 1.15;
  text-shadow: 0 2px 12px rgba(0,0,0,0.5), 0 0 60px rgba(212,165,116,0.08);
}
.hero-title-en {
  display: block; margin-top: 4px;
  font-size: 13px; color: rgba(237,228,219,0.25);
  font-family: Georgia,'Times New Roman',serif;
  font-style: italic; letter-spacing: 3px;
}

.hero-tags { display: flex; flex-wrap: wrap; gap: 10px; margin-bottom: 28px; }
.hero-tag {
  padding: 6px 16px; border-radius: 8px;
  font-size: 12px; font-weight: 500;
  color: var(--cream-dim); letter-spacing: 1.5px;
  background: rgba(237,228,219,0.04);
  border: 1px solid rgba(237,228,219,0.08);
}

.hero-credits { display: flex; flex-direction: column; gap: 16px; }
.hero-credit {
  display: flex; align-items: baseline; gap: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid rgba(237,228,219,0.04);
}
.hero-credit-label {
  flex-shrink: 0; width: 42px;
  font-size: 10px; color: rgba(237,228,219,0.3);
  letter-spacing: 6px; text-transform: uppercase;
}
.hero-credit-name {
  font-size: 17px; font-weight: 500; color: var(--cream);
  letter-spacing: 2px;
}

/* 右：海报 */
.hero-poster { flex-shrink: 0; width: 380px; display: flex; align-items: stretch; }
.hero-poster-frame {
  width: 100%;
  border-radius: 14px; overflow: hidden;
  box-shadow: 0 16px 48px rgba(0,0,0,0.6), 0 0 0 1px rgba(237,228,219,0.04);
}
.hero-poster-frame img { width: 100%; height: 100%; object-fit: cover; display: block; }
.hero-poster-fb {
  width: 100%; min-height: 520px;
  display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 12px;
  background: rgba(212,165,116,0.03);
  color: rgba(212,165,116,0.12);
  font-size: 13px; letter-spacing: 2px;
}

/* ==================================================================
   TWO · QUOTES
   ================================================================== */
.quotes-grid { display: flex; flex-wrap: wrap; justify-content: center; gap: 20px; perspective: 1000px; }
.quote-card:nth-child(3n+1) { transform: rotate(-0.6deg); }
.quote-card:nth-child(3n+2) { transform: rotate(0deg) translateY(14px); }
.quote-card:nth-child(3n)   { transform: rotate(0.6deg); }
.quote-card { width: 260px; height: 170px; perspective: 800px; }
.quote-inner {
  position: relative; width: 100%; height: 100%;
  transition: transform 0.65s cubic-bezier(0.4,0,0.2,1);
  transform-style: preserve-3d;
}
.quote-card:hover .quote-inner { transform: rotateY(180deg); }
.quote-front, .quote-back {
  position: absolute; inset: 0;
  display: flex; flex-direction: column; align-items: center; justify-content: center;
  padding: 24px 20px; border-radius: 14px;
  backface-visibility: hidden; -webkit-backface-visibility: hidden;
  border: 1px solid rgba(237,228,219,0.06);
  background: rgba(23,20,26,0.65);
  backdrop-filter: blur(12px); -webkit-backdrop-filter: blur(12px);
  box-shadow: 0 12px 32px rgba(0,0,0,0.4);
}
.quote-front { text-align: center; }
.quote-mark {
  position: absolute; top: 10px; left: 20px;
  font-size: 42px; font-family: Georgia,serif;
  color: rgba(212,165,116,0.25); line-height: 1;
}
.quote-text {
  font-size: 15px; color: var(--cream);
  font-family: Georgia,'Times New Roman',serif;
  line-height: 1.7; letter-spacing: 0.5px;
  text-shadow: 0 2px 6px rgba(0,0,0,0.3);
}
.quote-back {
  transform: rotateY(180deg);
  border-left: 3px solid rgba(212,165,116,0.3);
}
.quote-text-zh { font-size: 15px; color: var(--cream); line-height: 1.8; letter-spacing: 1px; }

/* ==================================================================
   THREE · SCENES · 瀑布流（修正版）
   ================================================================== */
.scenes-wall {
  /* 自适应列宽，始终居中 */
  column-width: 220px;
  column-gap: 10px;
}
.scene-brick {
  break-inside: avoid;
  margin-bottom: 10px;
  border-radius: 6px;
  overflow: hidden;
  background: rgba(23,20,26,0.5);
  border: 1px solid rgba(237,228,219,0.03);
  transition: transform 0.35s var(--ease-out), border-color 0.35s var(--ease-out);
  position: relative;
}
.scene-brick:hover {
  transform: scale(1.02); z-index: 2;
  border-color: rgba(212,165,116,0.2);
  box-shadow: 0 8px 28px rgba(0,0,0,0.55);
}
.scene-brick img {
  width: 100%;
  height: auto;
  max-height: 400px;
  display: block;
  object-fit: cover;
  object-position: center top;
}
.scene-cap {
  position: absolute; bottom: 0; left: 0; right: 0;
  padding: 10px 12px;
  font-size: 11px; color: var(--cream);
  letter-spacing: 1px;
  background: linear-gradient(0deg, rgba(0,0,0,0.75), transparent);
  text-shadow: 0 1px 3px rgba(0,0,0,0.5);
}

/* ==================================================================
   FOUR · LINKS
   ================================================================== */
.links-street { display: flex; flex-wrap: wrap; gap: 12px; justify-content: center; }
.link-card {
  display: inline-flex; align-items: center; gap: 8px;
  padding: 10px 18px; border-radius: 20px;
  border: 1px solid rgba(237,228,219,0.08);
  background: rgba(23,20,26,0.5);
  backdrop-filter: blur(8px); -webkit-backdrop-filter: blur(8px);
  color: var(--cream-dim);
  text-decoration: none; font-size: 13px; letter-spacing: 1px;
  animation-name: stroll;
  animation-timing-function: ease-in-out;
  animation-iteration-count: infinite;
  animation-duration: 4s;
  animation-delay: 0s;
  transition: all 0.3s var(--ease-out);
  cursor: pointer;
}
.link-card:hover {
  border-color: rgba(212,165,116,0.35);
  background: rgba(212,165,116,0.08);
  color: #D4A574;
  box-shadow: 0 0 20px rgba(212,165,116,0.1);
  animation-play-state: paused;
  transform: translateY(-2px);
}
@keyframes stroll {
  0%,100% { transform: translateX(0); }
  30%     { transform: translateX(10px); }
  70%     { transform: translateX(-10px); }
}
.link-card-icon { flex-shrink: 0; opacity: 0.5; transition: opacity 0.3s; }
.link-card:hover .link-card-icon { opacity: 1; }
.link-card-text { white-space: nowrap; }

/* ==================================================================
   FIVE · COMMENTS
   ================================================================== */
.comment-form { display: flex; gap: 10px; margin-bottom: 28px; }
.comment-input {
  padding: 11px 14px;
  border: 1px solid rgba(237,228,219,0.08);
  border-radius: 10px;
  background: rgba(237,228,219,0.03);
  color: var(--cream);
  font-size: 13px; font-family: inherit; letter-spacing: 1px;
  outline: none; caret-color: #D4A574;
  transition: border-color 0.3s var(--ease-out);
}
.comment-input::placeholder { color: rgba(237,228,219,0.15); }
.comment-input:focus { border-color: rgba(212,165,116,0.3); }
.comment-input--name { flex: 1; }
.comment-input--body { flex: 3; }
.comment-submit {
  flex-shrink: 0; padding: 11px 22px;
  border: 1px solid rgba(212,165,116,0.2);
  border-radius: 10px;
  background: rgba(212,165,116,0.08);
  color: #D4A574;
  font-size: 13px; font-family: inherit; font-weight: 600;
  letter-spacing: 2px; cursor: pointer;
  transition: all 0.3s var(--ease-out);
}
.comment-submit:hover {
  background: rgba(212,165,116,0.16);
  border-color: rgba(212,165,116,0.4);
  box-shadow: 0 0 16px rgba(212,165,116,0.1);
}
.comment-list { display: flex; flex-direction: column; gap: 10px; }
.comment-item {
  padding: 14px 18px; border-radius: 12px;
  background: rgba(237,228,219,0.02);
  border: 1px solid rgba(237,228,219,0.04);
}
.comment-item-head {
  display: flex; justify-content: space-between;
  font-size: 12px; color: var(--cream-dim);
  margin-bottom: 6px;
}
.comment-item-head strong { color: var(--cream); font-weight: 600; }
.comment-item p { margin: 0; font-size: 13px; color: var(--cream-dim); line-height: 1.6; letter-spacing: 0.5px; }

/* ==================================================================
   RESPONSIVE
   ================================================================== */
@media (max-width: 900px) {
  .detail { padding: 40px 20px 60px; }
  .hero-card { padding: 20px 20px 28px; border-radius: 20px; }
  .hero-body { flex-direction: column-reverse; gap: 28px; }
  .hero-poster { width: 100%; }
  .hero-poster-fb { min-height: 360px; }
  .hero-title-block h1 { font-size: 32px; letter-spacing: 3px; }
  .hero-title-accent { height: 52px; }
  .hero-credit-name { font-size: 15px; }
  .quotes-grid { flex-direction: column; align-items: center; }
  .quote-card { width: 100%; max-width: 360px; }
  .quote-card:nth-child(3n+1),
  .quote-card:nth-child(3n+2),
  .quote-card:nth-child(3n) { transform: none; }
  .scenes-wall { column-width: 160px; column-gap: 6px; }
  .scene-brick { margin-bottom: 6px; }
  .scene-brick img { max-height: 300px; }
  .comment-form { flex-direction: column; }
  .nav { right: 6px; gap: 18px; }
  .nav-dot { width: 6px; height: 6px; }
  .nav-dot-label { display: none; }
}

@media (max-width: 480px) {
  .scenes-wall { column-width: 130px; column-gap: 4px; }
  .scene-brick { margin-bottom: 4px; }
  .scene-brick img { max-height: 220px; }
}
</style>
