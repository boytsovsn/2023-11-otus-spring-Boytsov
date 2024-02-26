import { createApp } from 'vue'
//import './style.css'
import App from './App.vue'
import PrimeVue from 'primevue/config'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import ColumnGroup from 'primevue/columngroup'   // optional
import Row from 'primevue/row'                   // optional

import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import 'primevue/resources/primevue.min.css'
import 'primeicons/primeicons.css'

import 'primevue/resources/themes/aura-light-cyan/theme.css'

//import 'primevue/resources/themes/md-light-indigo/theme.css'
// import 'primevue/resources/themes/md-light-deeppurple/theme.css'
// import 'primevue/resources/themes/md-dark-indigo/theme.css'
// import 'primevue/resources/themes/md-dark-deeppurple/theme.css'
// import 'primevue/resources/themes/mdc-light-indigo/theme.css'
// import 'primevue/resources/themes/mdc-light-deeppurple/theme.css'
// import 'primevue/resources/themes/mdc-dark-indigo/theme.css'
// import 'primevue/resources/themes/mdc-dark-deeppurple/theme.css'
// import 'primevue/resources/themes/aura-light-blue/theme.css'
// import 'primevue/resources/themes/aura-light-indigo/theme.css'
// import 'primevue/resources/themes/aura-light-purple/theme.css'
// import 'primevue/resources/themes/aura-light-teal/theme.css'
// import 'primevue/resources/themes/aura-light-green/theme.css'
// import 'primevue/resources/themes/aura-light-amber/theme.css'
// import 'primevue/resources/themes/aura-light-pink/theme.css'
// import 'primevue/resources/themes/aura-light-lime/theme.css'
// import 'primevue/resources/themes/aura-light-noir/theme.css'

//import 'primevue/resources/themes/aura-dark-blue/theme.css'
// import 'primevue/resources/themes/aura-dark-indigo/theme.css'
// import 'primevue/resources/themes/aura-dark-purple/theme.css'
// import 'primevue/resources/themes/aura-dark-teal/theme.css'
// import 'primevue/resources/themes/aura-dark-green/theme.css'
// import 'primevue/resources/themes/aura-dark-amber/theme.css'
// import 'primevue/resources/themes/aura-dark-cyan/theme.css'
// import 'primevue/resources/themes/aura-dark-pink/theme.css'
// import 'primevue/resources/themes/aura-dark-lime/theme.css'
// import 'primevue/resources/themes/aura-dark-noir/theme.css'
// import 'primevue/resources/themes/lara-light-blue/theme.css'
// import 'primevue/resources/themes/lara-light-indigo/theme.css'
// import 'primevue/resources/themes/lara-light-purple/theme.css'
// import 'primevue/resources/themes/lara-light-teal/theme.css'
// import 'primevue/resources/themes/lara-light-green/theme.css'
// import 'primevue/resources/themes/lara-light-amber/theme.css'
// import 'primevue/resources/themes/lara-light-cyan/theme.css'
// import 'primevue/resources/themes/lara-light-pink/theme.css'
// import 'primevue/resources/themes/lara-dark-blue/theme.css'
// import 'primevue/resources/themes/lara-dark-indigo/theme.css'
// import 'primevue/resources/themes/lara-dark-purple/theme.css'
// import 'primevue/resources/themes/lara-dark-teal/theme.css'
// import 'primevue/resources/themes/lara-dark-green/theme.css'
// import 'primevue/resources/themes/lara-dark-amber/theme.css'
// import 'primevue/resources/themes/lara-dark-cyan/theme.css'
// import 'primevue/resources/themes/lara-dark-pink/theme.css'

const app = createApp(App)
app.use(PrimeVue)
app.component('Button', Button)
app.component('InputText', InputText)
app.component('DataTable', DataTable)
app.component('Column', Column)
app.component('ColumnGroup', ColumnGroup)
app.component('Row', Row)
app.mount('#app')
