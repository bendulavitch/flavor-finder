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
        .then((endpoints) => {
          if (!endpoints.length) {
            throw new Error("No endpoints returned from backend");
          }

          const fetchDetails = endpoints.map((endpoint) =>
            fetch(endpoint)
              .then((res) => {
                if (!res.ok) {
                  throw new Error(`Failed to fetch details for ${endpoint}`);
                }
                return res.json();
              })
              .then((data) => {
                // Ensure we have a valid structure
                if (!data || !data.result) {
                  throw new Error(`Invalid response from API for ${endpoint}`);
                }

                const result = data.result;

                // Transform the Google Places data into the shape needed by RestaurantCard
                const imageUrl =
                  result.photos && result.photos.length > 0
                    ? `https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=${result.photos[0].photo_reference}&key=AIzaSyDW3C7apil1_X7QUme8pTwdTgX8lMiuMys`
                    : null;

                return {
                  name: result.name || "Unknown Name",
                  address: result.formatted_address || "No Address Available",
                  rating: result.rating || "N/A",
                  image: imageUrl,
                  placeId: result.place_id || null,
                  website: result.website || null,
                  phone: result.formatted_phone_number || null,
                  openNow: result.opening_hours ? result.opening_hours.open_now : false,
                  servesBeer: !!result.serves_beer,
                  servesWine: !!result.serves_wine,
                  // Join weekday_text for a simple hours interval display (optional)
                  hoursInterval: result.opening_hours && result.opening_hours.weekday_text
                    ? result.opening_hours.weekday_text.join(", ")
                    : null,
                };
              })
              .catch((error) => {
                console.error(`Error fetching details for ${endpoint}:`, error);
                return null; // Skip invalid or failed responses
              })
          );

          return Promise.all(fetchDetails);
        })
        .then((restaurants) => {
          this.favorites = restaurants.filter((restaurant) => restaurant !== null);

          if (!this.favorites.length) {
            this.errorMessage = "No valid favorites found.";
          }
        })
        .catch((error) => {
          console.error("Error fetching favorites:", error);
          this.errorMessage = "Failed to load favorites.";
        })
        .finally(() => {
          this.isLoading = false;
        });
    },
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
