<template>
  <div class="app">

    <!-- ================================================================
         背景
         ================================================================ -->
    <div class="bg"></div>

    <!-- ================================================================
         微光粒子（预计算，无随机数）
         ================================================================ -->
    <div class="particles">
      <i v-for="p in particles" :key="p.id"
         :style="{
           left:p.x+'%', top:p.y+'%',
           width:p.s+'px', height:p.s+'px',
           animationDuration:p.d+'s',
           animationDelay:p.dl+'s',
         }"></i>
    </div>

    <!-- ================================================================
         主视觉：文字标识
         ================================================================ -->
    <div class="hero">
      <h1>CINEMATICS</h1>
      <p>电影资料馆 · Film Archive</p>
      <span class="hero-line"></span>
    </div>

    <!-- ================================================================
         两个入口
         ================================================================ -->
    <!-- 管理员入口——左下（仅管理员可见） -->
    <div v-if="isAdminUser" class="portal portal--admin" @click.stop="toggleAdmin">
      <span class="portal-ring"></span>
      <span class="portal-icon">☰</span>
      <span class="portal-label">管理</span>
    </div>

    <!-- 用户入口——右上 -->
    <div class="portal portal--user" @click.stop="toggleUser">
      <span class="portal-ring"></span>
      <span class="portal-icon">⌕</span>
      <span class="portal-label">探索</span>
    </div>

    <!-- 用户信息 -->
    <div class="user-info">
      <span class="user-info-name">{{ username }}</span>
      <button class="user-info-logout" @click="handleLogout">退出</button>
    </div>

    <!-- ================================================================
         管理员菜单
         ================================================================ -->
    <transition name="panel">
      <div v-if="showAdmin" class="menu-panel menu-panel--admin" @click.stop>
        <div class="menu-header">Administrator Menu</div>
        <div class="menu-cards">
          <button class="g-card" style="--stagger:0" @click="goTo('/admin/movies/query')">
            <span class="g-card-icon">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.35-4.35"/><line x1="8" y1="11" x2="14" y2="11"/></svg>
            </span>
            <span class="g-card-text">
              <em>查询信息</em>
              <small>Query</small>
            </span>
          </button>
          <button class="g-card" style="--stagger:1" @click="goTo('/admin/movies/create')">
            <span class="g-card-icon">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="16"/><line x1="8" y1="12" x2="16" y2="12"/></svg>
            </span>
            <span class="g-card-text">
              <em>添加信息</em>
              <small>Create</small>
            </span>
          </button>
          <button class="g-card" style="--stagger:2" @click="goTo('/admin/movies/update/1')">
            <span class="g-card-icon">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"><path d="M12 20h9"/><path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z"/></svg>
            </span>
            <span class="g-card-text">
              <em>修改信息</em>
              <small>Update</small>
            </span>
          </button>
          <button class="g-card g-card--danger" style="--stagger:3" @click="goTo('/admin/movies/delete/1')">
            <span class="g-card-icon">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"><polyline points="3 6 5 6 21 6"/><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/></svg>
            </span>
            <span class="g-card-text">
              <em>删除信息</em>
              <small>Delete</small>
            </span>
          </button>
        </div>
      </div>
    </transition>

    <!-- ================================================================
         用户菜单
         ================================================================ -->
    <transition name="panel">
      <div v-if="showUser" class="menu-panel menu-panel--user" @click.stop>
        <div class="menu-header">User Menu</div>
        <div class="menu-cards">
          <button class="g-card" style="--stagger:0" @click="goTo('/multi-search')">
            <span class="g-card-icon">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.35-4.35"/></svg>
            </span>
            <span class="g-card-text">
              <em>博海拾贝</em>
              <small>Explorer</small>
            </span>
          </button>
          <button class="g-card" style="--stagger:1" @click="goTo('/my-comments')">
            <span class="g-card-icon">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>
            </span>
            <span class="g-card-text">
              <em>生生不息</em>
              <small>Comments</small>
            </span>
          </button>

        </div>
      </div>
    </transition>

    <!-- ================================================================
         遮罩
         ================================================================ -->
    <transition name="fade">
      <div v-if="showAdmin || showUser" class="overlay" @click="closeAll"></div>
    </transition>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { isAdmin, getUsername, clearAuth } from '@/auth.js'

const router = useRouter()
const showAdmin = ref(false)
const showUser  = ref(false)
const isAdminUser = ref(isAdmin())
const username = ref(getUsername())

function toggleAdmin() { showAdmin.value = !showAdmin.value; showUser.value = false }
function toggleUser()  { showUser.value  = !showUser.value;  showAdmin.value = false }
function goTo(path)    { showAdmin.value = false; showUser.value = false; router.push(path) }
function closeAll()    { showAdmin.value = false; showUser.value = false }
function handleLogout() { clearAuth(); router.push('/login') }

/* 粒子参数——确定性伪随机，不在模板里 Math.random */
const particles = computed(() => {
  let s = 271
  const r = () => { s = (s * 16807) % 2147483647; return (s - 1) / 2147483646 }
  return Array.from({ length: 30 }, (_, i) => ({
    id: i,
    x:  Math.round(r() * 96),
    y:  Math.round(r() * 96),
    s:  +(0.8 + r() * 2.0).toFixed(1),
    d:  +(6 + r() * 10).toFixed(1),
    dl: +(r() * 6).toFixed(1),
  }))
})
</script>

<style scoped>
/* ==================================================================
   TOKENS
   ================================================================== */
.app {
  --amber:       #D4A574;
  --amber-soft:  rgba(212,165,116,0.25);
  --amber-dim:   rgba(212,165,116,0.10);
  --amber-glow:  rgba(212,165,116,0.06);
  --stone:       #8B7E74;
  --stone-soft:  rgba(139,126,116,0.20);
  --cream:       #EDE4DB;
  --cream-dim:   rgba(237,228,219,0.50);
  --cream-faint: rgba(237,228,219,0.12);
  --rose:        #C97D7D;
  --rose-soft:   rgba(201,125,125,0.20);
  --base:        #0D0B0F;
  --surface:     #17141A;
  --surface2:    #1E1B22;
  --ease-out:    cubic-bezier(0,0,0.2,1);
  --ease-spring: cubic-bezier(0.34,1.56,0.64,1);

  position: relative;
  width: 100vw; height: 100vh;
  overflow: hidden;
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
      radial-gradient(ellipse at 50% 30%, rgba(212,165,116,0.03) 0%, transparent 50%),
      radial-gradient(ellipse at 20% 80%, rgba(139,126,116,0.03) 0%, transparent 40%),
      radial-gradient(ellipse at 80% 20%, rgba(201,125,125,0.02) 0%, transparent 35%),
      var(--base);
}

/* ==================================================================
   PARTICLES
   ================================================================== */
.particles {
  position: absolute; inset: 0; z-index: 1; pointer-events: none;
}
.particles i {
  position: absolute;
  border-radius: 50%;
  background: var(--cream-dim);
  animation: particleFloat 8s ease-in-out infinite;
}
.particles i:nth-child(odd)  { background: rgba(212,165,116,0.3); }
.particles i:nth-child(3n)   { animation-delay: 0.8s; }
.particles i:nth-child(5n+1) { animation-delay: 1.6s; }
@keyframes particleFloat {
  0%     { transform: translateY(0) scale(1);    opacity: 0; }
  15%    { opacity: 0.6; }
  50%    { transform: translateY(-45px) scale(0.4); opacity: 0.2; }
  85%    { opacity: 0.5; }
  100%   { transform: translateY(5px) scale(1);  opacity: 0; }
}

/* ==================================================================
   HERO
   ================================================================== */
.hero {
  position: absolute; top: 38%; left: 50%; transform: translate(-50%, -50%);
  z-index: 5; text-align: center; pointer-events: none;
}
.hero h1 {
  margin: 0;
  font-size: clamp(36px, 7vw, 72px);
  font-weight: 700;
  letter-spacing: clamp(10px, 2.5vw, 22px);
  color: var(--cream);
  text-shadow: 0 0 80px var(--amber-glow), 0 0 160px rgba(212,165,116,0.04);
}
.hero p {
  margin: 14px 0 0;
  font-size: clamp(11px, 1.4vw, 14px);
  color: var(--cream-dim);
  letter-spacing: 6px;
  font-family: Georgia,'Times New Roman',serif;
}
.hero-line {
  display: block; width: 60px; height: 1px;
  margin: 22px auto 0;
  background: linear-gradient(90deg, transparent, var(--amber-soft), transparent);
}

/* ==================================================================
   PORTALS — 入口
   ================================================================== */
.portal {
  position: fixed; z-index: 20;
  display: flex; flex-direction: column; align-items: center; gap: 10px;
  cursor: pointer;
}
.portal-ring {
  width: 52px; height: 52px; border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  border: 1px solid var(--cream-faint);
  background: rgba(23,20,26,0.6);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  transition: all 0.5s var(--ease-out);
}
.portal-icon {
  position: absolute;
  font-size: 20px; color: var(--cream-dim); line-height: 1;
  transition: all 0.5s var(--ease-out);
  pointer-events: none;
}
.portal-label {
  font-size: 10px; color: var(--cream-dim);
  letter-spacing: 3px;
  transition: color 0.5s var(--ease-out);
}
.portal:hover .portal-ring {
  border-color: var(--amber-soft);
  background: rgba(212,165,116,0.08);
  box-shadow: 0 0 36px var(--amber-soft), inset 0 0 20px var(--amber-glow);
  transform: scale(1.08);
}
.portal:hover .portal-icon { color: var(--cream); }
.portal:hover .portal-label { color: var(--amber); }

.portal--admin { bottom: 36px; left: 36px; }
.portal--user  { top: 36px; right: 36px; }

/* ==================================================================
   USER INFO
   ================================================================== */
.user-info {
  position: fixed; top: 40px; right: 108px; z-index: 25;
  display: flex; align-items: center; gap: 12px;
}
.user-info-name {
  font-size: 12px; color: var(--cream-dim); letter-spacing: 2px;
}
.user-info-logout {
  padding: 4px 12px;
  border: 1px solid rgba(237,228,219,0.12);
  border-radius: 6px;
  background: rgba(237,228,219,0.04);
  color: var(--cream-dim);
  font-size: 11px; font-family: inherit; letter-spacing: 1px; cursor: pointer;
  transition: all 0.3s var(--ease-out);
}
.user-info-logout:hover {
  border-color: rgba(201,125,125,0.3);
  color: #C97D7D;
  background: rgba(201,125,125,0.06);
}

/* ==================================================================
   MENU PANELS
   ================================================================== */
.menu-panel {
  position: fixed; z-index: 200;
  padding: 28px;
  background: rgba(23,20,26,0.75);
  backdrop-filter: blur(24px);
  -webkit-backdrop-filter: blur(24px);
  border: 1px solid rgba(237,228,219,0.08);
  border-radius: 20px;
  box-shadow:
      0 24px 64px rgba(0,0,0,0.55),
      inset 0 1px 0 rgba(255,255,255,0.02);
}

.menu-panel--admin {
  bottom: 108px; left: 28px;
  min-width: 260px;
}
.menu-panel--user {
  top: 108px; right: 28px;
  min-width: 260px;
}

.menu-header {
  font-size: 11px; color: var(--cream-dim);
  letter-spacing: 3px; text-transform: uppercase;
  margin-bottom: 16px; padding-bottom: 12px;
  border-bottom: 1px solid rgba(237,228,219,0.06);
  font-family: Georgia,serif;
}

.menu-cards {
  display: flex; flex-direction: column; gap: 6px;
}

/* ==================================================================
   GLASS CARDS
   ================================================================== */
.g-card {
  display: flex; align-items: center; gap: 14px;
  width: 100%; padding: 13px 16px;
  border: none; outline: none; cursor: pointer;
  border-radius: 12px;
  background: transparent;
  text-align: left;
  /* 动画初态 */
  opacity: 0;
  transform: translateY(10px);
  animation: cardSlideUp 0.5s var(--ease-spring) calc(var(--stagger,0)*0.06s) both;
  transition: background 0.3s var(--ease-out), box-shadow 0.3s var(--ease-out);
}
.g-card:hover {
  background: rgba(212,165,116,0.07);
  box-shadow: inset 0 0 0 1px rgba(212,165,116,0.12);
}
.g-card:active { transform: scale(0.97); transition: transform 0.08s ease; }

.g-card--danger:hover {
  background: rgba(201,125,125,0.08);
  box-shadow: inset 0 0 0 1px rgba(201,125,125,0.15);
}

.g-card-icon {
  width: 38px; height: 38px; border-radius: 10px;
  display: flex; align-items: center; justify-content: center;
  flex-shrink: 0;
  background: rgba(237,228,219,0.05);
  color: var(--cream-dim);
  transition: all 0.3s var(--ease-out);
}
.g-card:hover .g-card-icon {
  background: rgba(212,165,116,0.12);
  color: var(--amber);
  box-shadow: 0 0 16px var(--amber-dim);
}
.g-card--danger:hover .g-card-icon {
  background: rgba(201,125,125,0.12);
  color: var(--rose);
  box-shadow: 0 0 16px var(--rose-soft);
}

.g-card-text { display: flex; flex-direction: column; gap: 2px; }
.g-card-text em {
  font-style: normal; font-size: 13px; font-weight: 600;
  color: var(--cream); letter-spacing: 1px;
}
.g-card-text small {
  font-size: 10px; color: var(--cream-dim);
  letter-spacing: 0.5px; font-family: Georgia,serif;
}

@keyframes cardSlideUp {
  from { opacity: 0; transform: translateY(10px); }
  to   { opacity: 1; transform: translateY(0); }
}

/* ==================================================================
   VUE TRANSITIONS
   ================================================================== */
.panel-enter-active { animation: panelIn 0.35s var(--ease-out) both; }
.panel-leave-active { animation: panelOut 0.2s ease-in both; }
@keyframes panelIn {
  from { opacity: 0; filter: blur(4px); transform: translateY(8px); }
  to   { opacity: 1; filter: blur(0);  transform: translateY(0); }
}
@keyframes panelOut {
  to { opacity: 0; filter: blur(3px); transform: translateY(-8px); }
}

.fade-enter-active { animation: fadeIn 0.3s ease both; }
.fade-leave-active { animation: fadeOut 0.2s ease both; }
@keyframes fadeIn  { from { opacity: 0; } to { opacity: 1; } }
@keyframes fadeOut { to { opacity: 0; } }

/* ==================================================================
   OVERLAY
   ================================================================== */
.overlay {
  position: fixed; inset: 0; z-index: 150;
  background: rgba(0,0,0,0.18);
}

/* ==================================================================
   RESPONSIVE
   ================================================================== */
@media (max-width: 768px) {
  .menu-panel {
    left: 16px; right: 16px;
    bottom: auto; top: auto;
    min-width: auto; padding: 20px;
    border-radius: 16px;
  }
  .menu-panel--admin { bottom: 96px; }
  .menu-panel--user  { top: 96px; }
  .portal-ring { width: 42px; height: 42px; }
  .portal-icon { font-size: 16px; }
  .portal--admin { bottom: 20px; left: 20px; }
  .portal--user  { top: 20px; right: 20px; }
  .g-card { padding: 11px 14px; gap: 10px; }
  .g-card-icon { width: 32px; height: 32px; border-radius: 8px; }
  .user-info { top: 20px; right: 80px; gap: 6px; }
  .user-info-name { font-size: 10px; }
}
</style>
