import { createApp } from 'vue';
import CapstoneApp from './App.vue';
import { createStore } from './store';
import router from './router';
import axios from 'axios';

/* Set base URL for Axios */
axios.defaults.baseURL = import.meta.env.VITE_REMOTE_API;

/* Retrieve stored token and user */
const currentToken = localStorage.getItem('token');
const currentUser = JSON.parse(localStorage.getItem('user'));

if (currentToken) {
  // Set the Authorization header
  axios.defaults.headers.common['Authorization'] = `Bearer ${currentToken}`;
}

// Create the Vuex store with credentials
const store = createStore(currentToken, currentUser);

// Create and mount the Vue app
const app = createApp(CapstoneApp);
app.use(store);
app.use(router);
app.mount('#app');
