<template>
  <div id="app">
    <RouterView />
  </div>
</template>

<script>
import { RouterView } from 'vue-router'

export default {
  components: {
    RouterView
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
body {
  font-family: Arial, sans-serif;
  background-color: #f0f2f5;
}

/* 隐藏滚动条，但依然可以滚动 */
::-webkit-scrollbar {
  width: 0;
  background: transparent;
}

/* 对 Firefox 也生效 */
* {
  scrollbar-width: none;
}
</style>