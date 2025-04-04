import { createStore as _createStore } from 'vuex';
import axios from 'axios';

export function createStore(currentToken, currentUser) {
  let store = _createStore({
    state: {
      token: currentToken || '',
      user: currentUser || {},
      restaurants: [], // Initialize restaurants as an empty array
    },
    mutations: {
      SET_AUTH_TOKEN(state, token) {
        state.token = token;
        localStorage.setItem('token', token);
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      },
      SET_USER(state, user) {
        state.user = user;
        localStorage.setItem('user', JSON.stringify(user));
      },
      LOGOUT(state) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        state.token = '';
        state.user = {};
        state.restaurants = []; // Clear restaurants on logout
        axios.defaults.headers.common = {};
      },
      SET_RESTAURANTS(state, restaurants) {
        state.restaurants = restaurants;
      },
    },
    actions: {
      fetchRestaurants({ commit }) {
        axios
          .get('/api/restaurants')
          .then((response) => {
            console.log(response.date);
            commit('SET_RESTAURANTS', response.data);
          })
          .catch((error) => {
            console.error('Error fetching restaurants:', error);
          });
      },
    },
  });

  return store;
}
