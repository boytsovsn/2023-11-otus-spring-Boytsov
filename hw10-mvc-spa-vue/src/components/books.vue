<template>
  <p>
     <h3>{{ BookTitle }}</h3>
  </p>
  <p>
    <table >
      <th>Id</th>
      <th>Title</th>
      <th>Author</th>
      <th>Genre</th>
      <th>Edit</th>
      <th>Delete</th>
      <tr v-for="book in bookstable">
        <td>{{ book.id }}</td>
        <td>{{ book.title }}</td>
        <td>{{ book.authorName }}</td>
        <td>{{ book.gengreName }}</td>
        <td>
          <Button label="Edit" icon="pi pi-check"  @click="dtEditClick(book);"/>
        </td>
        <td>
          <Button label="Delete" icon="pi pi-trash"  @click="dtDeleteClick(book);"/>
        </td>
      </tr>
    </table>
  </p>
  
  <book v-if="editing" :book="book" :BookTitle = "title"/>

</template>

<script>
import book from './book.vue'

export default {
    name: 'books',
    props: ['BookTitle'],
    data() {
        return {
            title: 'Book Edit',
            books:[],
            book: [],
            bookstable:[],
            authors: new Map(),
            genres: new Map(),
            editing: false
        }
    },
    async created () {
      this.loadBooks()
    },
    methods: {
      async loadBooks() {
        this.loading = true
        const res = await fetch('http://localhost:8080/api/book')
        this.books = await res.json()
        for (let author of this.books[0].authors) {
           this.authors.set(author.id, author.fullName);
        }
        for (let genre of this.books[0].genres) {
           this.genres.set(genre.id, genre.name);
        }
        for (let book of this.books) {
          let booktable = {id: book.id, title: book.title, authorName: this.authors.get(book.authorId), gengreName: this.genres.get(book.genreId)}
          this.bookstable.push(booktable);
        }
        this.loading = false
        //console.log(JSON.parse(JSON.stringify(this.books)))  
        //console.log(this.books)
        //console.log(this.bookstable)
      },
      dtEditClick(book) {
        //alert("Edit book:" + JSON.stringify(book))
        this.editing = true
        this.book = book
      },
      dtDeleteClick(book) {
        alert("Delete book:" + JSON.stringify(book))
      }  
    },
    components: {book}
}  
</script>

<style scoped>

</style>
