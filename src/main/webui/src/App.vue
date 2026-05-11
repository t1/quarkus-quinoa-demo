<template>
  <div class="container">
    <header>
      <h1>📚 Book Catalog</h1>
      <p class="subtitle">Manage your book collection</p>
    </header>

    <main>
      <BookForm @book-added="loadBooks" />
      <BookList :books="books" @book-updated="loadBooks" @book-deleted="loadBooks" />
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import BookForm from './components/BookForm.vue'
import BookList from './components/BookList.vue'

const books = ref([])

const loadBooks = async () => {
  try {
    const response = await fetch('/api/books')
    books.value = await response.json()
  } catch (error) {
    console.error('Failed to load books:', error)
  }
}

onMounted(() => {
  loadBooks()
})
</script>

<style scoped>
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

header {
  text-align: center;
  margin-bottom: 3rem;
}

h1 {
  color: #2c3e50;
  margin-bottom: 0.5rem;
}

.subtitle {
  color: #7f8c8d;
  font-size: 1.1rem;
}
</style>
