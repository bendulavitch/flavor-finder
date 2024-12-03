<template>
  <div class="home-view">
    <h2>Find Your Next Favorite Restaurant</h2>
    <div class="filters">
      <input type="text" v-model="searchQuery" placeholder="Search restaurants..." />
      <button @click="surpriseMe">Surprise Me!</button>
    </div>
    <div class="swipe-interface">
      <RestaurantCard
        v-for="restaurant in filteredRestaurants"
        :key="restaurant.id"
        :restaurant="restaurant"
        @add-favorite="addFavorite"
        @skip="skipRestaurant"
      />
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import RestaurantCard from '../components/RestaurantCard.vue';

export default {
  components: { RestaurantCard },
  data() {
    return {
      searchQuery: '',
    };
  },
  computed: {
    ...mapState(['restaurants']),
    filteredRestaurants() {
      return this.restaurants.filter((r) =>
        r.name.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    },
  },
  methods: {
    addFavorite(restaurant) {
      this.$store.commit('ADD_FAVORITE', restaurant);
    },
    skipRestaurant(restaurant) {
      this.$store.commit('SKIP_RESTAURANT', restaurant);
    },
    surpriseMe() {
      const randomIndex = Math.floor(Math.random() * this.restaurants.length);
      alert(`How about ${this.restaurants[randomIndex].name}?`);
    },
  },
};
</script>

<style>
.home-view {
  padding: 1rem;
}
.filters {
  display: flex;
  justify-content: space-between;
  margin-bottom: 1rem;
}
.swipe-interface {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
}
</style>
