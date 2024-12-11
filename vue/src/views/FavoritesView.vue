<template>
  <div class="favorites-view">
    <h2>Your Favorite Restaurants</h2>

    <div v-if="isLoading" class="loading">Loading favorites...</div>

    <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>

    <div v-if="favorites.length" class="favorites-grid">
      <!-- Display all cards in a grid -->
      <RestaurantCard
        v-for="(restaurant, index) in favorites"
        :key="index"
        :restaurant="restaurant"
        class="restaurant-card"
      />
    </div>

    <div v-else-if="!isLoading && !errorMessage" class="no-favorites">
      <p>You don't have any favorites yet!</p>
    </div>
  </div>
</template>

<script>
import RestaurantCard from "../components/RestaurantCard.vue";

export default {
  components: { RestaurantCard },
  data() {
    return {
      favorites: [], // List of transformed favorite restaurants
      isLoading: false,
      errorMessage: "",
    };
  },
  methods: {
    fetchFavorites() {
  this.isLoading = true;
  this.errorMessage = "";

  fetch("http://localhost:9000/api/favorites", {
    method: "GET",
    headers: {
      Authorization: `Bearer ${this.$store.state.token}`,
    },
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Failed to fetch favorites");
      }
      return response.json();
    })
    .then((restaurants) => {
      // Now `restaurants` is already a list of Restaurant objects
      if (!restaurants.length) {
        this.errorMessage = "No valid favorites found.";
      } else {
        this.favorites = restaurants;
      }
    })
    .catch((error) => {
      console.error("Error fetching favorites:", error);
      this.errorMessage = "Failed to load favorites.";
    })
    .finally(() => {
      this.isLoading = false;
    });
}
,
  },
  mounted() {
    this.fetchFavorites();
  },
};
</script>

<style scoped>
.favorites-view {
  padding: 1rem;
}

.favorites-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1rem;
}

.restaurant-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  padding: 1rem;
  background-color: #fff;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.restaurant-card:hover {
  transform: scale(1.05);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
}

.loading {
  text-align: center;
  font-size: 1.5rem;
}

.error-message {
  color: red;
  text-align: center;
}

.no-favorites {
  text-align: center;
  font-size: 1.2rem;
  color: gray;
}
</style>
