
<template>
  <p>
    <h3>{{ BookTitle }}</h3>
  </p>
  <form>
    <div class="div-table">
      <div class="div-table-row">
        <div class="div-table-col">
          <label>Title: </label>
        </div>
        <div class="div-table-col">
          <InputText type="text" v-model="book.title"/>
        </div>
      </div> 

      <div class="div-table-row">
        <div class="div-table-col">
          <label>Author: </label>
        </div>
        <div class="div-table-col">
          <Dropdown v-model="book.authorId" :options="getAuthors" optionLabel="name" optionValue="id"/>
        </div>
      </div> 

      <div class="div-table-row">
        <div class="div-table-col">
          <label>Genre: </label>
        </div>  
        <div class="div-table-col">
          <Dropdown v-model="book.genreId" :options="getGenres" optionLabel="name" optionValue="id"/>
        </div>  
      </div>  
    </div>
    <div class="div-table">
      <div class="div-table-row">
        <div class="div-table-col">   
            <Button label="Save" icon="pi pi-check" @click="dtSaveClick(book);$emit('edtitingOff');"/>
        </div>
        <div class="div-table-col">   
            <Button label="Cancel" icon="pi pi-times" @click="$emit('edtitingOff');"/>
        </div> 
      </div>  
    </div>
  </form>
</template>

<script>
export default {
  name: 'book',
  props: ['BookTitle', 'book', 'authorsMap', 'genresMap'],
  methods: {
    dtSaveClick(book) {
      alert("Edit book:" + JSON.stringify(book))
    }
  },
  computed: {
    getAuthors() {
      let ret = []
      for (let entry of this.authorsMap.entries()) {
        ret.push({id: entry[0], name: entry[1]})
      }
      return ret;
    },
    getGenres() {
      let ret = []
      for (let entry of this.genresMap.entries()) {
        ret.push({id: entry[0], name: entry[1]})
      }
      return ret;
    }
  },
  emits: ["edtitingOff"]  
}
</script>

<style scoped>

.div-table {
  display: table;         
  width: auto;         
  border-spacing: 5px; /* cellspacing:poor IE support for  this */
}
.div-table-row {
  display: table-row;
  width: auto;
  clear: both;
}
.div-table-col {
  float: left; /* fix for  buggy browsers */
  display: table-column;         
  width: 200px;         
}

</style>
