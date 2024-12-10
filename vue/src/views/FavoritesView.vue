<template>
  <div class="favorites-page">
    <h2>Your Favorites</h2>
    <div v-if="favorites.length" class="favorites-list">
      <RestaurantCard
        v-for="(endpoint, index) in favorites"
        :key="index"
        :restaurant="null"
        :endpoint="endpoint"
        @removeFavorite="removeFavorite(endpoint)"
      />
    </div>
    <div v-else>
      <p>No favorites yet. Swipe right on restaurants to add them here!</p>
    </div>
  </div>
</template>

<script>
import RestaurantCard from "../components/RestaurantCard.vue";

export default {
  components: { RestaurantCard },
  data() {
    return {
      favorites: [],
    };
  },
  methods: {
    fetchFavorites() {
      if (!this.isAuthenticated) {
        console.error("User is not authenticated.");
        return;
      }

      fetch("http://localhost:9000/api/favorites", {
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
        .then((data) => {
          this.favorites = data;
        })
        .catch((error) => console.error("Error fetching favorites:", error));
    },
  },
  computed: {
    isAuthenticated() {
      return this.$store.state.token !== ''; // Same as App.vue
    },
  },
  mounted() {
    this.fetchFavorites();
  },
};
</script>

<style scoped>
.favorites-page {
  padding: 1rem;
  max-width: 900px;
  margin: 0 auto;
}

.favorites-list {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  justify-content: center;
}
</style>
