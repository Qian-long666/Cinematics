<template>
  <div class="page">

    <div class="bg"></div>

    <div class="container">
      <div class="head">
        <h1>删除信息</h1>
        <p>Delete Movie</p>
        <span class="head-line"></span>
      </div>

      <div class="form">
        <!-- 警示图标 -->
        <div class="warn-icon">
          <svg width="36" height="36" viewBox="0 0 24 24" fill="none"
               stroke="currentColor" stroke-width="1.2" stroke-linecap="round">
            <circle cx="12" cy="12" r="10"/>
            <line x1="12" y1="8" x2="12" y2="12"/>
            <line x1="12" y1="16" x2="12.01" y2="16"/>
          </svg>
        </div>

        <!-- ID 输入 -->
        <div class="field">
          <input v-model="deleteId" type="number"
                 placeholder="Entry Movie ID"
                 class="field-input"
                 @keyup.enter="openConfirm" />
          <span class="field-line"></span>
        </div>

        <!-- 反馈 -->
        <transition name="toast">
          <div v-if="msg.text" class="toast" :class="'toast--' + msg.type">{{ msg.text }}</div>
        </transition>

        <!-- 删除按钮 -->
        <button class="delete-btn" @click="openConfirm" :disabled="deleting">
          <template v-if="!deleting">Submit</template>
          <span v-else class="delete-btn-spinner"></span>
        </button>
      </div>
    </div>

    <!-- ================================================================
         自定义确认弹窗
         ================================================================ -->
    <transition name="dialog">
      <div v-if="showConfirm" class="dialog-overlay" @click.self="showConfirm = false">
        <div class="dialog-card">
          <div class="dialog-icon">
            <svg width="40" height="40" viewBox="0 0 24 24" fill="none"
                 stroke="currentColor" stroke-width="1.2" stroke-linecap="round">
              <circle cx="12" cy="12" r="10"/>
              <line x1="12" y1="8" x2="12" y2="12"/>
              <line x1="12" y1="16" x2="12.01" y2="16"/>
            </svg>
          </div>
          <div class="dialog-body">
            <strong>确认删除</strong>
            <p>确定要删除 ID 为 <em>{{ deleteId }}</em> 的电影吗？<br/>此操作不可撤销。</p>
          </div>
          <div class="dialog-actions">
            <button class="dialog-btn dialog-btn--cancel" @click="showConfirm = false">
              取消
            </button>
            <button class="dialog-btn dialog-btn--confirm" @click="executeDelete">
              确认删除
            </button>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { movieService } from '@/services/movieService'

const router = useRouter()
const deleteId = ref('')
const deleting = ref(false)
const showConfirm = ref(false)
const msg = reactive({ text: '', type: 'success' })

function openConfirm() {
  if (!deleteId.value) {
    msg.text = '请输入要删除的电影 ID'
    msg.type = 'warn'
    return
  }
  msg.text = ''
  showConfirm.value = true
}

async function executeDelete() {
  showConfirm.value = false
  deleting.value = true
  msg.text = ''
  try {
    await movieService.deleteMovie(deleteId.value)
    msg.text = '删除成功'
    msg.type = 'success'
    setTimeout(() => { router.push('/') }, 800)
  } catch {
    msg.text = '删除失败，请重试'
    msg.type = 'fail'
  } finally {
    deleting.value = false
  }
}
</script>

<style scoped>
/* ==================================================================
   TOKENS
   ================================================================== */
.page {
  --amber:        #D4A574;
  --amber-soft:   rgba(212,165,116,0.25);
  --rose:         #C97D7D;
  --rose-soft:    rgba(201,125,125,0.30);
  --rose-dim:     rgba(201,125,125,0.10);
  --rose-glow:    rgba(201,125,125,0.06);
  --cream:        #EDE4DB;
  --cream-dim:    rgba(237,228,219,0.50);
  --cream-faint:  rgba(237,228,219,0.12);
  --base:         #0D0B0F;
  --surface:      rgba(23,20,26,0.55);
  --ease-out:     cubic-bezier(0,0,0.2,1);
  --ease-spring:  cubic-bezier(0.34,1.56,0.64,1);

  position: relative;
  width: 100vw; min-height: 100vh;
  display: flex; align-items: flex-start; justify-content: center;
  padding: 100px 20px 60px;
  font-family: 'PingFang SC','Microsoft YaHei',Georgia,'Noto Serif SC',serif;
  user-select: none;
  background: var(--base);
}

.bg {
  position: fixed; inset: 0; z-index: 0;
  background:
      radial-gradient(ellipse at 50% 25%, rgba(201,125,125,0.02) 0%, transparent 55%),
      radial-gradient(ellipse at 30% 70%, rgba(139,126,116,0.02) 0%, transparent 45%),
      var(--base);
}

.container {
  position: relative; z-index: 1;
  width: 100%; max-width: 420px;
  padding: 40px 36px;
  background: var(--surface);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(237,228,219,0.06);
  border-radius: 24px;
  box-shadow: 0 20px 60px rgba(0,0,0,0.45), inset 0 1px 0 rgba(255,255,255,0.015);
}

/* ==================================================================
   HEAD
   ================================================================== */
.head { text-align: center; margin-bottom: 32px; }
.head h1 { margin: 0; font-size: 24px; font-weight: 700; color: var(--cream); letter-spacing: 4px; }
.head p {
  margin: 6px 0 0; font-size: 10px; color: var(--cream-dim);
  letter-spacing: 5px; font-family: Georgia,'Times New Roman',serif; text-transform: uppercase;
}
.head-line {
  display: block; width: 48px; height: 1px; margin: 16px auto 0;
  background: linear-gradient(90deg, transparent, var(--rose-soft), transparent);
}

/* ==================================================================
   WARN ICON
   ================================================================== */
.warn-icon {
  display: flex; justify-content: center;
  color: rgba(201,125,125,0.35);
  margin-bottom: 20px;
  animation: warnPulse 2.5s ease-in-out infinite;
}
@keyframes warnPulse {
  0%,100% { opacity: 0.5; }
  50%     { opacity: 1; }
}

/* ==================================================================
   FORM
   ================================================================== */
.form { display: flex; flex-direction: column; gap: 8px; }

.field { position: relative; }

.field-input {
  width: 100%; padding: 16px 0 10px;
  border: none; outline: none; background: none;
  font-size: 18px; font-weight: 600; color: var(--cream);
  font-family: inherit; text-align: center; letter-spacing: 2px;
  caret-color: #C97D7D;
}
.field-input::placeholder {
  font-size: 14px; font-weight: 400; color: rgba(237,228,219,0.17); letter-spacing: 1px;
}
.field-input::-webkit-outer-spin-button,
.field-input::-webkit-inner-spin-button { -webkit-appearance: none; margin: 0; }
.field-input[type=number] { -moz-appearance: textfield; }

.field-line {
  position: absolute; bottom: 0; left: 0; right: 0; height: 1px;
  background: var(--cream-faint);
  transition: background 0.3s var(--ease-out);
}
.field-input:focus + .field-line { background: rgba(201,125,125,0.25); }
.field-input:focus + .field-line::after {
  content: '';
  position: absolute; bottom: 0; left: 50%; transform: translateX(-50%);
  width: 100%; height: 1.5px;
  background: #C97D7D;
  animation: lineExpand 0.4s var(--ease-out) both;
}
@keyframes lineExpand { from { width: 0; } to { width: 100%; } }

/* ==================================================================
   TOAST
   ================================================================== */
.toast {
  padding: 12px 16px; border-radius: 10px;
  font-size: 13px; letter-spacing: 1px; text-align: center;
  animation: toastIn 0.35s var(--ease-spring) both;
}
.toast--success { background: rgba(212,165,116,0.08); border: 1px solid rgba(212,165,116,0.2); color: #D4A574; }
.toast--fail    { background: rgba(201,125,125,0.08); border: 1px solid rgba(201,125,125,0.2); color: #C97D7D; }
.toast--warn    { background: rgba(201,125,125,0.05); border: 1px solid rgba(201,125,125,0.1); color: var(--cream-dim); }

.toast-enter-active { animation: toastIn 0.35s var(--ease-spring) both; }
.toast-leave-active { animation: toastOut 0.2s ease-in both; }
@keyframes toastIn  { from { opacity: 0; transform: translateY(-8px); } to { opacity: 1; transform: translateY(0); } }
@keyframes toastOut { to { opacity: 0; transform: translateY(-4px); } }

/* ==================================================================
   DELETE BUTTON
   ================================================================== */
.delete-btn {
  width: 100%; margin-top: 20px; position: relative;
  padding: 14px;
  border: 1px solid rgba(201,125,125,0.3);
  border-radius: 14px;
  background: rgba(201,125,125,0.10);
  color: #C97D7D;
  font-size: 15px; font-family: inherit; font-weight: 600;
  letter-spacing: 3px; cursor: pointer;
  transition: all 0.35s var(--ease-out);
}
.delete-btn:hover {
  background: rgba(201,125,125,0.20);
  border-color: rgba(201,125,125,0.5);
  box-shadow: 0 0 28px rgba(201,125,125,0.16);
}
.delete-btn:active { transform: scale(0.97); transition: transform 0.08s ease; }
.delete-btn:disabled { opacity: 0.5; cursor: not-allowed; pointer-events: none; }

.delete-btn-spinner {
  display: block; width: 18px; height: 18px; margin: 0 auto;
  border: 2px solid rgba(201,125,125,0.2);
  border-top-color: #C97D7D;
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }

/* ==================================================================
   CONFIRM DIALOG — 自定义确认弹窗
   ================================================================== */
.dialog-overlay {
  position: fixed; inset: 0; z-index: 300;
  display: flex; align-items: center; justify-content: center;
  background: rgba(0,0,0,0.35);
  backdrop-filter: blur(3px);
  -webkit-backdrop-filter: blur(3px);
}

.dialog-card {
  width: 340px; max-width: 90vw;
  padding: 32px 28px 24px;
  background: rgba(23,20,26,0.85);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(201,125,125,0.15);
  border-radius: 20px;
  box-shadow:
      0 24px 64px rgba(0,0,0,0.6),
      0 0 0 1px rgba(201,125,125,0.05),
      inset 0 1px 0 rgba(255,255,255,0.01);
  text-align: center;
}

.dialog-icon {
  color: rgba(201,125,125,0.5);
  margin-bottom: 16px;
  animation: warnPulse 2.5s ease-in-out infinite;
}

.dialog-body { margin-bottom: 24px; }
.dialog-body strong {
  display: block;
  font-size: 16px; font-weight: 700; color: var(--cream);
  letter-spacing: 2px; margin-bottom: 8px;
}
.dialog-body p {
  margin: 0;
  font-size: 12px; color: var(--cream-dim);
  letter-spacing: 1px; line-height: 1.7;
}
.dialog-body em {
  font-style: normal; color: #C97D7D; font-weight: 600;
}

.dialog-actions { display: flex; gap: 10px; }
.dialog-btn {
  flex: 1; padding: 11px 0;
  border-radius: 10px;
  font-size: 13px; font-family: inherit; font-weight: 600;
  letter-spacing: 1px; cursor: pointer;
  transition: all 0.3s var(--ease-out);
}
.dialog-btn--cancel {
  background: transparent;
  border: 1px solid rgba(237,228,219,0.10);
  color: var(--cream-dim);
}
.dialog-btn--cancel:hover {
  background: rgba(237,228,219,0.04);
  border-color: rgba(237,228,219,0.2);
}
.dialog-btn--confirm {
  background: rgba(201,125,125,0.12);
  border: 1px solid rgba(201,125,125,0.3);
  color: #C97D7D;
}
.dialog-btn--confirm:hover {
  background: rgba(201,125,125,0.22);
  border-color: rgba(201,125,125,0.5);
  box-shadow: 0 0 20px rgba(201,125,125,0.14);
}
.dialog-btn:active { transform: scale(0.96); transition: transform 0.08s ease; }

/* dialog 动画 */
.dialog-enter-active { animation: dialogIn 0.35s var(--ease-spring) both; }
.dialog-leave-active { animation: dialogOut 0.2s ease-in both; }
@keyframes dialogIn {
  from { opacity: 0; transform: scale(0.92); filter: blur(4px); }
  to   { opacity: 1; transform: scale(1);    filter: blur(0); }
}
@keyframes dialogOut {
  to { opacity: 0; transform: scale(0.95); filter: blur(3px); }
}

@media (max-width: 640px) {
  .page { padding: 60px 12px 40px; }
  .container { padding: 28px 22px; border-radius: 18px; }
  .head h1 { font-size: 20px; letter-spacing: 3px; }
}
</style>
