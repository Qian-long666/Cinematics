<template>
  <div style="padding:20px; max-width:900px; margin:0 auto; font-family:Arial,sans-serif;">
    <h1>🎬 Cinematic Archives</h1>

    <!-- 查询 -->
    <div style="border:1px solid #ddd; padding:16px; border-radius:8px; margin-bottom:20px;">
      <h3>查询电影</h3>
      <input v-model="searchId" type="number" placeholder="输入ID" style="padding:6px 10px; margin-right:8px;" />
      <button @click="fetchMovie" style="padding:6px 16px; background:#42b883; color:#fff; border:none; border-radius:4px; cursor:pointer;">查询</button>
      <div v-if="movie" style="margin-top:10px; background:#f5f5f5; padding:12px; border-radius:4px;">
        <p><strong>名称：</strong>{{ movie.name }}</p>
        <p><strong>上映时间：</strong>{{ movie.time }} </p>
        <p><strong>类型：</strong>{{ movie.type || '未知' }}</p>
        <p><strong>导演：</strong>{{ movie.director || '未知' }}</p>
        <p><strong>主演：</strong>{{ movie.actor || '未知' }}</p>
        <p><strong>国家：</strong>{{ movie.nation || '未知' }}</p>
      </div>
      <div v-if="notFound" style="color:red; margin-top:10px;">未找到该电影</div>
    </div>

    <!-- 新增 -->
    <div style="border:1px solid #ddd; padding:16px; border-radius:8px; margin-bottom:20px;">
      <h3>添加电影</h3>
      <div style="display:flex; flex-wrap:wrap; gap:8px;">
        <input v-model="newMovie.name" placeholder="名称" style="padding:6px 10px;" />
        <input v-model="newMovie.time" type="number" placeholder="上映时间" style="padding:6px 10px;" />
        <input v-model="newMovie.type" placeholder="类型" style="padding:6px 10px;" />
        <input v-model="newMovie.director" placeholder="导演" style="padding:6px 10px;" />
        <input v-model="newMovie.actor" placeholder="主演" style="padding:6px 10px;" />
        <input v-model="newMovie.nation" placeholder="国家" style="padding:6px 10px;" />
        <button @click="addNewMovie" style="padding:6px 16px; background:#42b883; color:#fff; border:none; border-radius:4px; cursor:pointer;">添加</button>
      </div>
    </div>

    <!-- 更新 -->
    <div style="border:1px solid #ddd; padding:16px; border-radius:8px; margin-bottom:20px;">
      <h3>更新电影</h3>
      <div style="display:flex; flex-wrap:wrap; gap:8px;">
        <input v-model="updateForm.id" type="number" placeholder="要更新的ID" style="padding:6px 10px;" />
        <input v-model="updateForm.name" placeholder="名称" style="padding:6px 10px;" />
        <input v-model="updateForm.time" type="number" placeholder="上映时间" style="padding:6px 10px;" />
        <input v-model="updateForm.type" placeholder="类型" style="padding:6px 10px;" />
        <input v-model="updateForm.director" placeholder="导演" style="padding:6px 10px;" />
        <input v-model="updateForm.actor" placeholder="主演" style="padding:6px 10px;" />
        <input v-model="updateForm.nation" placeholder="国家" style="padding:6px 10px;" />
        <button @click="doUpdateMovie" style="padding:6px 16px; background:#42b883; color:#fff; border:none; border-radius:4px; cursor:pointer;">更新</button>
      </div>
    </div>

    <!-- 删除 -->
    <div style="border:1px solid #ddd; padding:16px; border-radius:8px; margin-bottom:20px;">
      <h3>删除电影</h3>
      <input v-model="deleteId" type="number" placeholder="输入要删除的ID" style="padding:6px 10px; margin-right:8px;" />
      <button @click="doDeleteMovie" style="padding:6px 16px; background:#e74c3c; color:#fff; border:none; border-radius:4px; cursor:pointer;">删除</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { movieService } from '@/services/movieService'

const searchId = ref('')
const movie = ref(null)
const notFound = ref(false)

const newMovie = ref({
  name: '',
  time: '',
  type: '',
  director: '',
  actor: '',
  nation: ''
})

const updateForm = ref({
  id: '',
  name: '',
  time: '',
  type: '',
  director: '',
  actor: '',
  nation: ''
})

const deleteId = ref('')

async function fetchMovie() {
  if (!searchId.value) return alert('请输入ID')
  try {
    const result = await movieService.getMovieById(searchId.value)
    console.log('成功拿到数据：', result)
    movie.value = result
    notFound.value = false
  } catch (e) {
    console.log('捕获到的异常：', e)      // ← 看这行
    console.log('异常消息：', e.message)  // ← 看这行
    movie.value = null
    notFound.value = true
  }
}

async function addNewMovie() {
  if (!newMovie.value.name) return alert('请输入名称')
  try {
    await movieService.addMovie(newMovie.value)
    alert('添加成功')
    newMovie.value = { name: '', time: '', type: '', director: '', actor: '', nation: '' }
  } catch (e) {
    alert('添加失败：' + e.message)
  }
}

async function doUpdateMovie() {
  if (!updateForm.value.id) return alert('请输入要更新的ID')
  try {
    await movieService.updateMovie(updateForm.value.id, updateForm.value)
    alert('更新成功')
    updateForm.value = { id: '', name: '', time: '', type: '', director: '', actor: '', nation: '' }
  } catch (e) {
    alert('更新失败：' + e.message)
  }
}

async function doDeleteMovie() {
  if (!deleteId.value) return alert('请输入ID')
  if (!confirm('确定删除吗？')) return
  try {
    await movieService.deleteMovie(deleteId.value)
    alert('删除成功')
    deleteId.value = ''
  } catch (e) {
    alert('删除失败：' + e.message)
  }
}
</script>