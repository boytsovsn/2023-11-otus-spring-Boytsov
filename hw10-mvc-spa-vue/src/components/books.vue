<template>
  <p>
     <h3>{{ BookTitle }}</h3>
  </p>
  <p>
    <Button label="Create" icon="pi pi-check"  @click="dtAddClick();"/>
    <table >
      <!-- <th>Id</th> -->
      <th>Title</th>
      <th>Author</th>
      <th>Genre</th>
      <th>Edit</th>
      <th>Delete</th>
      <tr v-for="bookTable in booksTable">
        <!-- <td>{{ bookTable.id }}</td> -->
        <td>{{ bookTable.title }}</td>
        <td>{{ bookTable.authorName }}</td>
        <td>{{ bookTable.genreName }}</td>
        <td>
          <Button label="Edit" icon="pi pi-check"  @click="dtEditClick(bookTable);"/>
        </td>
        <td>
          <Button label="Delete" icon="pi pi-trash"  @click="dtDeleteClick(bookTable);"/>
        </td>
      </tr>
    </table>
  </p>
  
  <book v-if="editing" :book="book" :BookTitle="title"  :authorsMap="authors" :genresMap="genres" @reloadBooks="loadBooks();" @edtitingOff="editing=false"/>

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
            booksTable:[],
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
        this.$emit('notloaded');
        this.books = [];
        this.authors.clear();
        this.genres.clear();
        this.booksTable = [];
        this.book = [];
        const res = await fetch('http://localhost:8080/api/book');
        this.books = await res.json();
        for (let author of this.books[0].authors) {
           this.authors.set(author.id, author.fullName);
        }
        for (let genre of this.books[0].genres) {
           this.genres.set(genre.id, genre.name);
        }
        for (let book of this.books) {
          let booktable = {id: book.id, title: book.title, authorName: this.authors.get(book.authorId), genreName: this.genres.get(book.genreId)};
          this.booksTable.push(booktable);
        }
        this.$emit('loaded');
        //console.log(JSON.parse(JSON.stringify(this.books)))  
        //console.log(this.books)
        //console.log(this.bookstable)
      },
      findBookEntity(bookTable) {
        let foundOriginBook;
        for (let originBook of this.books) {
          if (bookTable.id == originBook.id) {
            foundOriginBook = originBook;
          }    
        }
        return foundOriginBook;
      },
      dtEditClick(bookTable) {
        //alert("Edit book:" + JSON.stringify(bookTable))
        this.book=this.findBookEntity(bookTable);
        this.editing = true;
      },
      dtAddClick(bookTable) {
        //alert("Edit book:" + JSON.stringify(bookTable))
        this.book={id: [], title: [], authorId: [], genreId: [], authors: [], genres: []};
        this.editing = true;
      },
      async dtDeleteClick(bookTable) {
        //  this.book=this.findBookEntity(bookTable);
        //  alert("Delete book:" + JSON.stringify(this.book));
        const res = await fetch('http://localhost:8080/api/book/'+bookTable.id, {method: 'DELETE'});
        await this.loadBooks();
      }  
    },
    emits: ['loaded', 'notloaded'],
    components: {book}
}  
</script>

<style scoped>

</style>
