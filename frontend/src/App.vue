<template>
  <div class="min-h-screen bg-gray-50">
    <TheHeader />
    <main class="container mx-auto px-4 py-6">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import TheHeader from './components/TheHeader.vue'
</script>
