
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
          <InputText type="text"/>
        </div>
      </div> 

      <div class="div-table-row">
        <div class="div-table-col">
          <label>Author: </label>
        </div>
        <div class="div-table-col">
          <InputText type="text"/>
        </div>
      </div> 

      <div class="div-table-row">
        <div class="div-table-col">
          <label>Genre: </label>
        </div>  
        <div class="div-table-col">
          <InputText type="text"/>
        </div>  
      </div>  
    </div>
    <div class="div-table">
      <div class="div-table-row">
        <div class="div-table-col">   
            <Button label="Save" icon="pi pi-check" @click="dtSaveClick(book);"/>
        </div>
        <div class="div-table-col">   
            <Button label="Cancel" icon="pi pi-times"/>
        </div> 
      </div>  
    </div>
  </form>
</template>

<script>
export default {
  name: 'book',
  props: ['BookTitle', 'book'],
  methods: {
    dtSaveClick(book) {
        alert("Edit book:" + JSON.stringify(book))
        this.editing = false
        
      }
  }  
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
