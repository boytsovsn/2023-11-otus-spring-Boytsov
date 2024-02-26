<template>
  <p>
     <h3>{{ BookTitle }}</h3>
  </p>
  <p>
    <DataTable :value="books" tableStyle="min-width: 50rem">
      <Column field="id" header="Id">{{ id }}</Column>
      <Column field="title" header="Title">{{ title }}</Column>
      <Column field="authorId" header="Author">{{ authorId }}</Column>
      <Column field="genreId" header="Genre">{{ genreId }}</Column>
    </DataTable>
  </p>
</template>

<script>
export default {
  props: ['BookTitle'],
    data(){
        return{
            books:[],
            loading: false
        }
    },
    async created () {
      this.loadBooks()
    },
    methods: {
      async loadBooks() {
        this.loading = true
        const res = await fetch('http://localhost:8080/api/books')
        this.books = await res.json()
        this.loading = false
        console.log(JSON.parse(JSON.stringify(this.books)))  
        console.log(this.books)
      }
    }
}  

</script>

<style scoped>

</style>
