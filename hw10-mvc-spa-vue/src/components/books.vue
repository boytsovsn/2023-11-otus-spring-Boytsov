<template>
  <p>
     <h3>{{ BookTitle }}</h3>
  </p>
  <p>
    <DataTable :value="bookstable" tableStyle="min-width: 50rem">
      <Column field="id" header="Id"></Column>
      <Column field="title" header="Title"></Column>
      <Column field="authorName" header="Author"></Column>
      <Column field="gengreName" header="Genre"></Column>
    </DataTable>
  </p>
  <!-- <p>
    <DataTable :value="authors" tableStyle="min-width: 50rem">
      <Column field="id" header="Id">{{ id }}</Column>
      <Column field="fullName" header="FullName">{{ title }}</Column>
    </DataTable>
  </p>
  <p>
    <DataTable :value="genres" tableStyle="min-width: 50rem">
      <Column field="id" header="Id">{{ id }}</Column>
      <Column field="name" header="Name">{{ title }}</Column>
    </DataTable>
  </p> -->

</template>

<script>
export default {
  props: ['BookTitle'],
    data(){
        return{
            books:[],
            bookstable:[],
            authors: new Map(),
            genres: new Map(),
            loading: false
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
        console.log(this.bookstable)
      }
    }
}  

</script>

<style scoped>

</style>
