<template>
  <div class="book-form">
    <h2>Add New Book</h2>
    <form @submit.prevent="addBook">
      <div class="form-row">
        <div class="form-group">
          <label for="title">Title</label>
          <input
            id="title"
            v-model="newBook.title"
            type="text"
            required
            placeholder="Enter book title"
          />
        </div>

        <div class="form-group">
          <label for="author">Author</label>
          <input
            id="author"
            v-model="newBook.author"
            type="text"
            required
            placeholder="Enter author name"
          />
        </div>
      </div>

      <div class="form-row">
        <div class="form-group">
          <label for="isbn">ISBN</label>
          <input
            id="isbn"
            v-model="newBook.isbn"
            type="text"
            placeholder="Enter ISBN"
          />
        </div>

        <div class="form-group">
          <label for="year">Publication Year</label>
          <input
            id="year"
            v-model.number="newBook.publicationYear"
            type="number"
            placeholder="Enter year"
          />
        </div>
      </div>

      <button type="submit" class="btn-primary">Add Book</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const emit = defineEmits(['book-added'])

const newBook = ref({
  title: '',
  author: '',
  isbn: '',
  publicationYear: null
})

const addBook = async () => {
  try {
    const response = await fetch('/api/books', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(newBook.value)
    })

    if (response.ok) {
      newBook.value = {
        title: '',
        author: '',
        isbn: '',
        publicationYear: null
      }
      emit('book-added')
    }
  } catch (error) {
    console.error('Failed to add book:', error)
  }
}
</script>

<style scoped>
.book-form {
  background: #f8f9fa;
  padding: 2rem;
  border-radius: 8px;
  margin-bottom: 2rem;
}

h2 {
  margin-top: 0;
  color: #2c3e50;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  margin-bottom: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
}

label {
  font-weight: 600;
  margin-bottom: 0.5rem;
  color: #34495e;
}

input {
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

input:focus {
  outline: none;
  border-color: #3498db;
}

.btn-primary {
  background: #3498db;
  color: white;
  padding: 0.75rem 2rem;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background 0.2s;
}

.btn-primary:hover {
  background: #2980b9;
}

@media (max-width: 768px) {
  .form-row {
    grid-template-columns: 1fr;
  }
}
</style>
