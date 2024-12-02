
<template>
  <div id="app" :class="theme">
    <header>
      <h1>FlavorFinder</h1>
      <button @click="toggleTheme">Toggle {{ theme === 'light' ? 'Dark' : 'Light' }} Mode</button>
    </header>
    <router-view />
  </div>
</template>

<script>
export default {
  data() {
    return {
      theme: localStorage.getItem('theme') || 'light',
    };
  },
  methods: {
    toggleTheme() {
      this.theme = this.theme === 'light' ? 'dark' : 'light';
      localStorage.setItem('theme', this.theme);
    },
  },
};
</script>

<style>
.light {
  --background-color: #ffffff;
  --text-color: #000000;
}
.dark {
  --background-color: #121212;
  --text-color: #ffffff;
}
body {
  background-color: var(--background-color);
  color: var(--text-color);
}
header {
  display: flex;
  justify-content: space-between;
  padding: 1rem;
  background-color: var(--background-color);
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}
</style>
