<template>
  <div class="page">
    <div class="bg"></div>

    <div class="card">
      <!-- 标题 -->
      <div class="head">
        <h1 v-if="isLogin">CINEMATICS</h1>
        <h1 v-else>注册账号</h1>
        <p v-if="isLogin">电影资料馆 · 登录</p>
        <p v-else>Sign Up</p>
        <span class="head-line"></span>
      </div>

      <!-- 表单 -->
      <div class="form">
        <div class="field">
          <input v-model="username" placeholder="用户名" class="field-input"
                 @keyup.enter="submit" />
          <span class="field-line"></span>
        </div>
        <div class="field">
          <input v-model="password" type="password" placeholder="密码" class="field-input"
                 @keyup.enter="submit" />
          <span class="field-line"></span>
        </div>

        <transition name="toast">
          <div v-if="msg.text" class="toast" :class="'toast--' + msg.type">{{ msg.text }}</div>
        </transition>

        <button class="submit-btn" @click="submit" :disabled="loading">
          <template v-if="!loading">{{ isLogin ? '登 录' : '注 册' }}</template>
          <span v-else class="submit-btn-spinner"></span>
        </button>

        <button class="switch-btn" @click="toggleMode">
          {{ isLogin ? '没有账号？点击注册' : '已有账号？点击登录' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { movieApi } from '@/api/movieApi.js'
import { setAuth } from '@/auth.js'

const router = useRouter()
const isLogin = ref(true)
const username = ref('')
const password = ref('')
const loading = ref(false)
const msg = ref({ text: '', type: 'success' })

function toggleMode() {
  isLogin.value = !isLogin.value
  msg.value = { text: '', type: 'success' }
}

async function submit() {
  if (!username.value || !password.value) {
    msg.value = { text: '请填写用户名和密码', type: 'warn' }
    return
  }
  loading.value = true
  msg.value = { text: '', type: 'success' }
  try {
    const api = isLogin.value ? movieApi.login : movieApi.register
    const res = await api(username.value, password.value)
    setAuth(res.token, res.username, res.role)
    router.push('/')
  } catch (e) {
    msg.value = { text: e.message || '操作失败', type: 'fail' }
  } finally {
    loading.value = false
  }
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
  --surface:      rgba(23,20,26,0.55);
  --ease-out:     cubic-bezier(0,0,0.2,1);
  --ease-spring:  cubic-bezier(0.34,1.56,0.64,1);

  position: relative; width: 100vw; min-height: 100vh;
  display: flex; align-items: center; justify-content: center;
  padding: 40px 20px;
  font-family: 'PingFang SC','Microsoft YaHei',Georgia,'Noto Serif SC',serif;
  background: var(--base); overflow: hidden;
}
.bg {
  position: fixed; inset: 0; z-index: 0;
  background:
      radial-gradient(ellipse at 50% 25%, rgba(212,165,116,0.025) 0%, transparent 55%),
      radial-gradient(ellipse at 30% 70%, rgba(139,126,116,0.02) 0%, transparent 45%),
      var(--base);
}

.card {
  position: relative; z-index: 1; width: 100%; max-width: 400px;
  padding: 44px 36px;
  background: var(--surface);
  backdrop-filter: blur(20px); -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(237,228,219,0.06);
  border-radius: 24px;
  box-shadow: 0 24px 64px rgba(0,0,0,0.5), inset 0 1px 0 rgba(255,255,255,0.01);
}
.head { text-align: center; margin-bottom: 36px; }
.head h1 { margin: 0; font-size: 26px; font-weight: 700; color: var(--cream); letter-spacing: 6px; }
.head p {
  margin: 8px 0 0; font-size: 10px; color: var(--cream-dim);
  letter-spacing: 4px; font-family: Georgia,'Times New Roman',serif; text-transform: uppercase;
}
.head-line {
  display: block; width: 48px; height: 1px; margin: 16px auto 0;
  background: linear-gradient(90deg, transparent, var(--amber-soft), transparent);
}

.form { display: flex; flex-direction: column; gap: 16px; }
.field { position: relative; }
.field-input {
  width: 100%; padding: 16px 0 10px;
  border: none; outline: none; background: none;
  font-size: 15px; color: var(--cream);
  font-family: inherit; letter-spacing: 1px;
  caret-color: #D4A574;
}
.field-input::placeholder { color: rgba(237,228,219,0.18); letter-spacing: 1px; }
.field-line {
  position: absolute; bottom: 0; left: 0; right: 0; height: 1px;
  background: var(--cream-faint);
  transition: background 0.3s var(--ease-out);
}
.field-input:focus + .field-line { background: rgba(212,165,116,0.3); }
.field-input:focus + .field-line::after {
  content: ''; position: absolute; bottom: 0; left: 50%; transform: translateX(-50%);
  width: 100%; height: 1.5px; background: #D4A574;
  animation: lineExpand 0.4s var(--ease-out) both;
}
@keyframes lineExpand { from { width: 0; } to { width: 100%; } }

.toast {
  padding: 10px 14px; border-radius: 8px;
  font-size: 13px; letter-spacing: 1px; text-align: center;
}
.toast--success { background: rgba(212,165,116,0.08); border: 1px solid rgba(212,165,116,0.2); color: #D4A574; }
.toast--fail    { background: rgba(201,125,125,0.08); border: 1px solid rgba(201,125,125,0.2); color: #C97D7D; }
.toast--warn    { background: rgba(212,165,116,0.05); border: 1px solid rgba(212,165,116,0.1); color: var(--cream-dim); }
.toast-enter-active { animation: toastIn 0.35s var(--ease-spring) both; }
.toast-leave-active { animation: toastOut 0.2s ease-in both; }
@keyframes toastIn  { from { opacity: 0; transform: translateY(-8px); } to { opacity: 1; transform: translateY(0); } }
@keyframes toastOut { to { opacity: 0; transform: translateY(-4px); } }

.submit-btn {
  width: 100%; margin-top: 8px; position: relative;
  padding: 14px;
  border: 1px solid rgba(212,165,116,0.25);
  border-radius: 14px;
  background: rgba(212,165,116,0.10);
  color: #D4A574;
  font-size: 15px; font-family: inherit; font-weight: 600;
  letter-spacing: 4px; cursor: pointer;
  transition: all 0.35s var(--ease-out);
}
.submit-btn:hover {
  background: rgba(212,165,116,0.18);
  border-color: rgba(212,165,116,0.45);
  box-shadow: 0 0 28px rgba(212,165,116,0.14);
}
.submit-btn:active { transform: scale(0.97); }
.submit-btn:disabled { opacity: 0.5; cursor: not-allowed; }

.submit-btn-spinner {
  display: block; width: 18px; height: 18px; margin: 0 auto;
  border: 2px solid rgba(212,165,116,0.2);
  border-top-color: #D4A574; border-radius: 50%;
  animation: spin 0.7s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }

.switch-btn {
  background: none; border: none;
  color: var(--cream-dim); font-size: 12px; font-family: inherit;
  letter-spacing: 1px; cursor: pointer; padding: 8px;
  transition: color 0.3s;
}
.switch-btn:hover { color: var(--cream); }
</style>
