<script>
import apploader from './components/apploader.vue'
import books from './components/books.vue'

export default {
  data() {
    return {
      title: 'Books',
      loading: false
    }
  },
  components: {apploader, books}
}
</script>

<template>
    <apploader v-if="loading" @notloaded="loading=true"/>
    <books v-else :BookTitle="title" @loaded="loading=false"/> 
</template>

<style scoped>
div{
  display: flex;
  min-height:100vh;
}  
</style>
