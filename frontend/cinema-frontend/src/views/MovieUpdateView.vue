<template>
  <div class="page">

    <div class="bg"></div>

    <div class="container">
      <div class="head">
        <h1>修改信息</h1>
        <p>Edit Movie</p>
        <span class="head-line"></span>
      </div>

      <div class="form">
        <!-- ID 定位字段 —— 视觉区分 -->
        <div class="field field--id">
          <input v-model="form.id" type="number" placeholder="Entry movie ID"
                 class="field-input field-input--id" />
          <span class="field-line field-line--id"></span>
        </div>

        <div class="form-sep">
          <span class="form-sep-text">修改以下字段</span>
        </div>

        <div class="form-row">
          <div class="field">
            <input v-model="form.name" placeholder="name" class="field-input" />
            <span class="field-line"></span>
          </div>
          <div class="field field--sm">
            <input v-model="form.time" type="number" placeholder="time" class="field-input" />
            <span class="field-line"></span>
          </div>
        </div>
        <div class="form-row">
          <div class="field">
            <input v-model="form.type" placeholder="type" class="field-input" />
            <span class="field-line"></span>
          </div>
          <div class="field field--sm">
            <input v-model="form.nation" placeholder="新国家" class="field-input" />
            <span class="field-line"></span>
          </div>
        </div>
        <div class="form-row">
          <div class="field">
            <input v-model="form.director" placeholder="director" class="field-input" />
            <span class="field-line"></span>
          </div>
          <div class="field">
            <input v-model="form.actor" placeholder="actor" class="field-input" />
            <span class="field-line"></span>
          </div>
        </div>

        <transition name="toast">
          <div v-if="msg.text" class="toast" :class="'toast--' + msg.type">{{ msg.text }}</div>
        </transition>

        <button class="submit-btn" @click="handleUpdate" :disabled="submitting">
          <template v-if="!submitting">Update</template>
          <span v-else class="submit-btn-spinner"></span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { movieService } from '@/services/movieService'

const router = useRouter()
const form = ref({ id: '', name: '', time: '', type: '', director: '', actor: '', nation: '' })
const submitting = ref(false)
const msg = reactive({ text: '', type: 'success' })

async function handleUpdate() {
  if (!form.value.id) {
    msg.text = '请输入要修改的电影 ID'
    msg.type = 'warn'
    return
  }
  submitting.value = true
  msg.text = ''
  try {
    await movieService.updateMovie(form.value.id, form.value)
    msg.text = '修改成功'
    msg.type = 'success'
    setTimeout(() => { router.push(`/movie/${form.value.id}`) }, 800)
  } catch {
    msg.text = '修改失败，请重试'
    msg.type = 'fail'
  } finally {
    submitting.value = false
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
  --amber-dim:    rgba(212,165,116,0.10);
  --rose:         #C97D7D;
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
  padding: 80px 20px 60px;
  font-family: 'PingFang SC','Microsoft YaHei',Georgia,'Noto Serif SC',serif;
  user-select: none;
  background: var(--base);
}

.bg {
  position: fixed; inset: 0; z-index: 0;
  background:
      radial-gradient(ellipse at 50% 25%, rgba(212,165,116,0.025) 0%, transparent 55%),
      radial-gradient(ellipse at 30% 70%, rgba(139,126,116,0.02) 0%, transparent 45%),
      var(--base);
}

.container {
  position: relative; z-index: 1;
  width: 100%; max-width: 540px;
  padding: 40px 44px;
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
.head { text-align: center; margin-bottom: 36px; }
.head h1 { margin: 0; font-size: 24px; font-weight: 700; color: var(--cream); letter-spacing: 4px; }
.head p {
  margin: 6px 0 0; font-size: 10px; color: var(--cream-dim);
  letter-spacing: 5px; font-family: Georgia,'Times New Roman',serif; text-transform: uppercase;
}
.head-line {
  display: block; width: 48px; height: 1px; margin: 16px auto 0;
  background: linear-gradient(90deg, transparent, var(--amber-soft), transparent);
}

/* ==================================================================
   FORM
   ================================================================== */
.form { display: flex; flex-direction: column; gap: 8px; }

/* ID 字段 */
.field--id { margin-bottom: 4px; }
.field-input--id {
  font-size: 18px; font-weight: 600; letter-spacing: 2px;
  text-align: center;
}
.field-input--id::placeholder { font-size: 14px; font-weight: 400; color: rgba(237,228,219,0.26); }
.field-line--id { background: rgba(212,165,116,0.12); }
.field-input--id:focus + .field-line--id::after { background: #D4A574; }

/* 分隔 */
.form-sep {
  display: flex; align-items: center; gap: 12px; margin: 8px 0 4px;
}
.form-sep::before,
.form-sep::after {
  content: ''; flex: 1; height: 1px;
  background: linear-gradient(90deg, transparent, var(--cream-clear), transparent);
}
.form-sep-text {
  font-size: 10px; color: var(--cream-dim); letter-spacing: 2px;
  font-family: Georgia,serif; white-space: nowrap;
}

.form-row { display: flex; gap: 20px; }
.field { flex: 1; position: relative; }
.field--sm { flex: 0.6; }

.field-input {
  width: 100%; padding: 16px 0 10px;
  border: none; outline: none; background: none;
  font-size: 14px; color: var(--cream);
  font-family: inherit; letter-spacing: 1px;
  caret-color: #D4A574;
}
.field-input::placeholder { color: rgba(237,228,219,0.17); letter-spacing: 1px; font-size: 13px; }
.field-input::-webkit-outer-spin-button,
.field-input::-webkit-inner-spin-button { -webkit-appearance: none; margin: 0; }
.field-input[type=number] { -moz-appearance: textfield; }

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
.toast--warn    { background: rgba(212,165,116,0.05); border: 1px solid rgba(212,165,116,0.1); color: var(--cream-dim); }

.toast-enter-active { animation: toastIn 0.35s var(--ease-spring) both; }
.toast-leave-active { animation: toastOut 0.2s ease-in both; }
@keyframes toastIn  { from { opacity: 0; transform: translateY(-8px); } to { opacity: 1; transform: translateY(0); } }
@keyframes toastOut { to { opacity: 0; transform: translateY(-4px); } }

/* ==================================================================
   SUBMIT
   ================================================================== */
.submit-btn {
  width: 100%; margin-top: 20px; position: relative;
  padding: 14px;
  border: 1px solid rgba(212,165,116,0.25);
  border-radius: 14px;
  background: rgba(212,165,116,0.10);
  color: #D4A574;
  font-size: 15px; font-family: inherit; font-weight: 600;
  letter-spacing: 3px; cursor: pointer;
  transition: all 0.35s var(--ease-out);
}
.submit-btn:hover {
  background: rgba(212,165,116,0.18);
  border-color: rgba(212,165,116,0.45);
  box-shadow: 0 0 28px rgba(212,165,116,0.14);
}
.submit-btn:active { transform: scale(0.97); transition: transform 0.08s ease; }
.submit-btn:disabled { opacity: 0.5; cursor: not-allowed; pointer-events: none; }

.submit-btn-spinner {
  display: block; width: 18px; height: 18px; margin: 0 auto;
  border: 2px solid rgba(212,165,116,0.2);
  border-top-color: #D4A574;
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }

@media (max-width: 640px) {
  .page { padding: 40px 12px 40px; }
  .container { padding: 28px 22px; border-radius: 18px; }
  .head h1 { font-size: 20px; letter-spacing: 3px; }
  .form-row { flex-direction: column; gap: 4px; }
  .field--sm { flex: 1; }
}
</style>
