import { createStore as _createStore } from 'vuex';
import axios from 'axios';

export function createStore(currentToken, currentUser) {
  let store = _createStore({
    state: {
      token: currentToken || '',
      user: currentUser || {},
      restaurants: [ {
        id: 1,
        name: "Joe's Diner",
        image: "https://via.placeholder.com/150",
        rating: 4.5,
        distance: 2.3,
        kidFriendly: true,
      },
      {
        id: 2,
        name: "Pasta Palace",
        image: "https://via.placeholder.com/150",
        rating: 4.0,
        distance: 5.1,
        kidFriendly: false,
      },
      {
        id: 3,
        name: "Burger Barn",
        image: "https://via.placeholder.com/150",
        rating: 4.8,
        distance: 1.2,
        kidFriendly: true,
      },], // Initialize restaurants as an empty array
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
