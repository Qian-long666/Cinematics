<template>
  <div class="page">
    <div class="bg"></div>

    <div class="container">
      <button class="back-btn" @click="$router.push('/')">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor"
             stroke-width="1.5" stroke-linecap="round"><path d="M19 12H5"/><path d="m12 19-7-7 7-7"/></svg>
        返回
      </button>

      <div class="head">
        <h1>生生不息</h1>
        <p>My Comments · Timeline</p>
        <span class="head-line"></span>
      </div>

      <!-- 加载 -->
      <div v-if="loading" class="state">
        <span class="state-dot"></span><span class="state-dot"></span><span class="state-dot"></span>
        <p>加载中...</p>
      </div>

      <!-- 时间轴 -->
      <div v-else-if="comments.length" class="timeline">
        <div v-for="c in comments" :key="c.id" class="tl-item">
          <!-- 左侧日期 -->
          <div class="tl-date">
            <span class="tl-date-day">{{ c.createdAt }}</span>
          </div>
          <!-- 中线 -->
          <div class="tl-line">
            <span class="tl-dot"></span>
          </div>
          <!-- 右侧卡片 -->
          <div class="tl-card" @click="goMovie(c.movieId)">
            <div class="tl-card-meta">
              <span class="tl-card-movie">{{ c.movieName || '未知电影' }}</span>
              <span class="tl-card-role" :class="{ 'tl-card-role--admin': c.role === 'ADMIN' }">
                {{ c.role === 'ADMIN' ? '管理员' : '用户' }}
              </span>
              <button class="tl-card-del" @click.stop="deleteComment(c.id)">删除</button>
            </div>
            <p class="tl-card-content">{{ c.content }}</p>
          </div>
        </div>
      </div>

      <!-- 空态 -->
      <div v-else class="empty-hint">还没有发表过评论</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { movieApi } from '@/api/movieApi.js'

const router = useRouter()
const comments = ref([])
const loading = ref(true)

onMounted(async () => {
  await loadComments()
})

async function loadComments() {
  try {
    comments.value = await movieApi.getMyComments()
  } catch {
    comments.value = []
  } finally {
    loading.value = false
  }
}

async function deleteComment(id) {
  try {
    await movieApi.deleteComment(id)
    comments.value = comments.value.filter(c => c.id !== id)
  } catch { /* 静默失败 */ }
}

function goMovie(id) {
  router.push(`/movie/${id}`)
}
</script>

<style scoped>
.page {
  --amber:        #D4A574;
  --amber-soft:   rgba(212,165,116,0.25);
  --amber-dim:    rgba(212,165,116,0.10);
  --cream:        #EDE4DB;
  --cream-dim:    rgba(237,228,219,0.50);
  --cream-faint:  rgba(237,228,219,0.12);
  --base:         #0D0B0F;
  --surface:      rgba(23,20,26,0.50);
  --ease-out:     cubic-bezier(0,0,0.2,1);
  --ease-spring:  cubic-bezier(0.34,1.56,0.64,1);

  position: relative; width: 100%; min-height: 100vh;
  overflow-x: hidden;
  font-family: 'PingFang SC','Microsoft YaHei',Georgia,'Noto Serif SC',serif;
  background: var(--base); color: var(--cream);
}
*,
*::before,
*::after { box-sizing: border-box; }

.bg {
  position: fixed; inset: 0; z-index: 0;
  background:
      radial-gradient(ellipse at 50% 25%, rgba(212,165,116,0.025) 0%, transparent 55%),
      radial-gradient(ellipse at 30% 70%, rgba(139,126,116,0.02) 0%, transparent 45%),
      var(--base);
}

.container {
  position: relative; z-index: 1;
  max-width: 660px; margin: 0 auto;
  padding: 48px 28px 80px;
}

.back-btn {
  display: inline-flex; align-items: center; gap: 6px;
  padding: 7px 16px; margin-bottom: 28px;
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

.head { text-align: center; margin-bottom: 40px; }
.head h1 { margin: 0; font-size: 24px; font-weight: 700; color: var(--cream); letter-spacing: 6px; }
.head p {
  margin: 6px 0 0; font-size: 10px; color: var(--cream-dim);
  letter-spacing: 4px; font-family: Georgia,'Times New Roman',serif; text-transform: uppercase;
}
.head-line {
  display: block; width: 48px; height: 1px; margin: 16px auto 0;
  background: linear-gradient(90deg, transparent, var(--amber-soft), transparent);
}

/* 状态 */
.state {
  display: flex; flex-direction: column; align-items: center; gap: 12px;
  color: rgba(237,228,219,0.25); font-size: 14px; letter-spacing: 2px;
  padding: 60px 0;
}
.state-dot { width: 6px; height: 6px; border-radius: 50%; background: #D4A574; animation: dotBounce 1.1s ease-in-out infinite; }
.state-dot:nth-child(2) { animation-delay: 0.15s; }
.state-dot:nth-child(3) { animation-delay: 0.30s; }
@keyframes dotBounce {
  0%,80%,100% { transform: translateY(0); opacity: 0.3; }
  40%         { transform: translateY(-10px); opacity: 1; }
}
.empty-hint { text-align: center; padding: 60px 0; color: rgba(237,228,219,0.12); font-size: 13px; letter-spacing: 2px; }

/* ===================== 时间轴 ===================== */
.timeline { display: flex; flex-direction: column; }

.tl-item { display: flex; align-items: flex-start; gap: 0; }

/* 日期（左侧） */
.tl-date {
  width: 72px; flex-shrink: 0; text-align: right; padding-right: 18px; padding-top: 14px;
}

/* 中线 */
.tl-line {
  width: 1px; flex-shrink: 0; position: relative;
  background: rgba(237,228,219,0.08);
}
.tl-item:last-child .tl-line { background: transparent; }
.tl-dot {
  position: absolute; top: 20px; left: 50%; transform: translate(-50%, -50%);
  width: 7px; height: 7px; border-radius: 50%;
  background: rgba(212,165,116,0.5);
  box-shadow: 0 0 8px rgba(212,165,116,0.2);
}
.tl-item:first-child .tl-dot {
  background: #D4A574;
  box-shadow: 0 0 14px rgba(212,165,116,0.4);
}

/* 卡片（右侧） */
.tl-card {
  flex: 1; margin-left: 18px; margin-bottom: 28px;
  padding: 14px 18px;
  background: rgba(23,20,26,0.45);
  backdrop-filter: blur(10px); -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(237,228,219,0.05);
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.35s var(--ease-out);
}
.tl-card:hover {
  border-color: rgba(212,165,116,0.2);
  background: rgba(212,165,116,0.05);
  box-shadow: 0 0 24px rgba(212,165,116,0.06);
  transform: translateX(4px);
}
.tl-card-meta {
  display: flex; align-items: center; gap: 10px; margin-bottom: 8px;
}
.tl-card-movie {
  font-size: 13px; font-weight: 600; color: var(--amber); letter-spacing: 1px;
}
.tl-card-role {
  font-size: 10px; padding: 2px 8px; border-radius: 4px;
  background: rgba(237,228,219,0.06); color: var(--cream-dim);
  letter-spacing: 1px;
}
.tl-card-role--admin {
  background: rgba(212,165,116,0.1); color: var(--amber);
}
.tl-card-del {
  margin-left: auto; padding: 2px 8px;
  border: 1px solid rgba(201,125,125,0.15);
  border-radius: 4px;
  background: transparent;
  color: rgba(201,125,125,0.5);
  font-size: 10px; font-family: inherit; letter-spacing: 1px; cursor: pointer;
  transition: all 0.3s;
}
.tl-card-del:hover {
  background: rgba(201,125,125,0.1);
  border-color: rgba(201,125,125,0.35);
  color: #C97D7D;
}
.tl-card-content {
  margin: 0; font-size: 13px; color: var(--cream-dim); line-height: 1.7; letter-spacing: 0.5px;
}
.tl-date-day {
  font-size: 12px; color: rgba(237,228,219,0.35);
  font-family: Georgia,serif; letter-spacing: 1px;
  white-space: nowrap;
}

@media (max-width: 600px) {
  .container { padding: 28px 16px 60px; }
  .tl-date { width: 52px; padding-right: 10px; }
  .tl-date-day { font-size: 10px; }
  .tl-card { margin-left: 10px; padding: 12px 14px; }
}
</style>
