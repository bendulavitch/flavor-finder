<template>
  <body>
  <div class="favorites-view">
    <h2>Your Favorite Restaurants</h2>

     <!-- Loader: Display when isLoading is true -->
     <div v-if="isLoading" class="loader-container">
      <div class="loader">
        <div class="circle"></div>
        <div class="circle"></div>
        <div class="circle"></div>
        <div class="circle"></div>
      </div>
    </div>

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
  </body>
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

h2 {
  text-align: center;
  margin-bottom: 2rem;
  font-family: "Playfair Display", serif;
  font-size: 2rem;
  color: #5c5c5c;
}


body {
  background-color: #fff7ed;
}

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




/* Loader by AbanoubMagdy1 */
.loader-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #fff7ed; /* Optional: Add a semi-transparent background */
  z-index: 1000; /* Ensure it appears above other content */
}

.loader {
  --dim: 6rem;
  width: var(--dim);
  height: var(--dim);
  position: relative;
  animation: spin988 2s linear infinite;


}

.loader .circle {
  --color: #e0c9a6;
  --dim: 2rem;
  width: var(--dim);
  height: var(--dim);
  background-color: var(--color);
  border-radius: 50%;
  position: absolute;
}

.loader .circle:nth-child(1) {
  top: 0;
  left: 0;
}

.loader .circle:nth-child(2) {
  top: 0;
  right: 0;
}

.loader .circle:nth-child(3) {
  bottom: 0;
  left: 0;
}

.loader .circle:nth-child(4) {
  bottom: 0;
  right: 0;
}

@keyframes spin988 {
  0% {
    transform: scale(1) rotate(0);
  }

  20%, 25% {
    transform: scale(1.3) rotate(90deg);
  }

  45%, 50% {
    transform: scale(1) rotate(180deg);
  }

  70%, 75% {
    transform: scale(1.3) rotate(270deg);
  }

  95%, 100% {
    transform: scale(1) rotate(360deg);
  }
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
