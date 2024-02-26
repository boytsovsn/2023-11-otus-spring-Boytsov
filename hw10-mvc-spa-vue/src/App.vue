<script>
import apploader from './components/apploader.vue'
//import books from './components/books.vue'
import book from './components/book.vue'

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

  components: {book, apploader}
}
</script>

<template>
    <apploader v-if="loading" />
    <!-- <books v-else :BookTitle="title" /> -->
    <book v-else :BookTitle="title1" />
</template>

<style scoped>
div{
  display: flex;
  min-height:100vh;
}  
</style>
