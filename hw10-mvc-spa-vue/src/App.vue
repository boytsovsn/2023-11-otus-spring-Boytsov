<script>
import apploader from './components/apploader.vue'
import books from './components/books.vue'
//import book from './components/book.vue'

export default {
  data() {
    return {
      title: 'Books',
      title1: 'Book',
      loading: false
    }
  },
  methods: {

  },

  components: {books, apploader}
}
</script>

<template>
    <apploader v-if="loading" />
    <!-- <books v-else :BookTitle="title" /> -->
    <books v-else :BookTitle="title" />
</template>

<style scoped>
div{
  display: flex;
  min-height:100vh;
}  
</style>
