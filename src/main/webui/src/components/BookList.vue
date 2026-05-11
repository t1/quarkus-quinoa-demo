<template>
  <div class="book-list">
    <h2>Book Collection ({{ books.length }})</h2>

    <div v-if="books.length === 0" class="empty-state">
      <p>No books in the catalog yet. Add your first book above!</p>
    </div>

    <div v-else class="books-grid">
      <div v-for="book in books" :key="book.id" class="book-card">
        <div v-if="editingId === book.id" class="book-edit">
          <input v-model="editForm.title" placeholder="Title" />
          <input v-model="editForm.author" placeholder="Author" />
          <input v-model="editForm.isbn" placeholder="ISBN" />
          <input v-model.number="editForm.publicationYear" type="number" placeholder="Year" />

          <div class="edit-actions">
            <button @click="saveEdit(book.id)" class="btn-save">Save</button>
            <button @click="cancelEdit" class="btn-cancel">Cancel</button>
          </div>
        </div>

        <div v-else class="book-info">
          <h3>{{ book.title }}</h3>
          <p class="author">by {{ book.author }}</p>
          <div class="book-details">
            <span v-if="book.isbn" class="isbn">ISBN: {{ book.isbn }}</span>
            <span v-if="book.publicationYear" class="year">{{ book.publicationYear }}</span>
          </div>

          <div class="book-actions">
            <button @click="startEdit(book)" class="btn-edit">Edit</button>
            <button @click="deleteBook(book.id)" class="btn-delete">Delete</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  books: {
    type: Array,
    required: true
  }
})

const emit = defineEmits(['book-updated', 'book-deleted'])

const editingId = ref(null)
const editForm = ref({
  title: '',
  author: '',
  isbn: '',
  publicationYear: null
})

const startEdit = (book) => {
  editingId.value = book.id
  editForm.value = {
    title: book.title,
    author: book.author,
    isbn: book.isbn,
    publicationYear: book.publicationYear
  }
}

const cancelEdit = () => {
  editingId.value = null
  editForm.value = {
    title: '',
    author: '',
    isbn: '',
    publicationYear: null
  }
}

const saveEdit = async (id) => {
  try {
    const response = await fetch(`/api/books/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(editForm.value)
    })

    if (response.ok) {
      cancelEdit()
      emit('book-updated')
    }
  } catch (error) {
    console.error('Failed to update book:', error)
  }
}

const deleteBook = async (id) => {
  if (!confirm('Are you sure you want to delete this book?')) {
    return
  }

  try {
    const response = await fetch(`/api/books/${id}`, {
      method: 'DELETE'
    })

    if (response.ok) {
      emit('book-deleted')
    }
  } catch (error) {
    console.error('Failed to delete book:', error)
  }
}
</script>

<style scoped>
.book-list {
  margin-top: 2rem;
}

h2 {
  color: #2c3e50;
  margin-bottom: 1.5rem;
}

.empty-state {
  text-align: center;
  padding: 3rem;
  color: #7f8c8d;
}

.books-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
}

.book-card {
  background: white;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 1.5rem;
  transition: box-shadow 0.2s;
}

.book-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.book-info h3 {
  margin: 0 0 0.5rem 0;
  color: #2c3e50;
}

.author {
  color: #7f8c8d;
  margin: 0 0 1rem 0;
  font-style: italic;
}

.book-details {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
  font-size: 0.9rem;
  color: #95a5a6;
}

.book-actions {
  display: flex;
  gap: 0.5rem;
}

.btn-edit, .btn-delete {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: opacity 0.2s;
}

.btn-edit {
  background: #3498db;
  color: white;
}

.btn-edit:hover {
  opacity: 0.8;
}

.btn-delete {
  background: #e74c3c;
  color: white;
}

.btn-delete:hover {
  opacity: 0.8;
}

.book-edit input {
  width: 100%;
  padding: 0.5rem;
  margin-bottom: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.edit-actions {
  display: flex;
  gap: 0.5rem;
  margin-top: 1rem;
}

.btn-save, .btn-cancel {
  flex: 1;
  padding: 0.5rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-save {
  background: #2ecc71;
  color: white;
}

.btn-cancel {
  background: #95a5a6;
  color: white;
}

@media (max-width: 768px) {
  .books-grid {
    grid-template-columns: 1fr;
  }
}
</style>
