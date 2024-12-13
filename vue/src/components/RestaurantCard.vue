<template>
  <div class="restaurant-card" @click="goToWebsite">
    <img 
      :src="restaurant.image || 'ben.jpg'" 
      alt="Restaurant Image" 
      class="restaurant-image" 
    />
    <div class="restaurant-details">
      <h2>{{ restaurant.name }}</h2>
      <p class="address">{{ restaurant.address }}</p>
      
 

      <!-- Rating -->
      <div class="rating-section">
        <span v-if="restaurant.rating" class="rating">⭐ {{ restaurant.rating }}</span>
        <span v-else>No Rating Available</span>
      </div>

      <!-- Phone -->
      <p v-if="restaurant.phone">📞 {{ restaurant.phone }}</p>

      <!-- Hours -->
      <p v-if="restaurant.hoursInterval">🕒 {{ restaurant.hoursInterval }}</p>
      
      <!-- Is Open Now -->
      <!-- <p>
  <span v-if="isOpenNow" class="open-now">✅ Open Now</span>
  <span v-else class="closed">❌ Closed</span>
</p> -->


      <!-- Beer and Wine -->
      <div class="beverages">
        <span v-if="restaurant.servesBeer" class="beer-icon">🍺 Serves Beer</span>
        <span v-if="restaurant.servesWine" class="wine-icon">🍷 Serves Wine</span>
      </div>

      <!-- Website Link -->
      <a 
        v-if="restaurant.website" 
        :href="restaurant.website" 
        target="_blank" 
        class="website-link"
      >
        Visit Website
      </a>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    restaurant: {
      type: Object,
      required: true,
      default: () => ({
      name: "Unknown Restaurant",
      address: "Unknown Address",
      rating: "N/A",
      photo: null,
      opening_hours: null,
    }),
    },
  },
  computed: {
    isOpenNow() {
      // Check if `opening_hours` or `openNow` is available
      if (this.restaurant.openNow !== undefined) {
        return this.restaurant.openNow; // Use provided boolean
      }
      // If detailed opening hours are available, process them
      if (this.restaurant.opening_hours?.periods) {
        return this.checkOpenNow(this.restaurant.opening_hours.periods);
      }
      return false; // Default to closed
    },
  },
  methods: {
    checkOpenNow(periods) {
      const now = new Date();
      const currentDay = now.getDay(); // Day of week (0=Sunday)
      const currentTime = now.getHours() * 100 + now.getMinutes(); // Time in HHMM format

      // Find today's opening and closing times
      const todayPeriod = periods.find((period) => period.day === currentDay);
      if (!todayPeriod) return false;

      const { open, close } = todayPeriod;
      return currentTime >= open && currentTime < close;
    },
    goToWebsite() {
      if (this.restaurant.website) {
        window.open(this.restaurant.website, "_blank");
      }
    },
  },
};
</script>

<style scoped>
.restaurant-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  text-align: center;
  max-width: 400px;
  margin: auto;
  background-color: #fff; /* Optional: Add background for better readability */
  cursor: pointer;
}
.restaurant-card:hover {
  transform: scale(1.02);
  transition: transform 0.3s ease;
}
.restaurant-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}
.restaurant-details {
  padding: 1rem;
}
.rating-section {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 1rem;
  margin: 0.5rem 0;
}
.website-link {
  display: inline-block;
  color: #007bff;
  text-decoration: none;
  margin: 0.5rem 0;
  font-size: 0.9rem;
  font-weight: bold;
}
.website-link:hover {
  text-decoration: underline;
}
.address {
  font-size: 0.9rem;
  margin: 0.5rem 0;
  color: #555;
}
.open-now {
  color: green;
  font-weight: bold;
}
.closed {
  color: red;
  font-weight: bold;
}
.beverages {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-top: 0.5rem;
}
.beer-icon,
.wine-icon {
  display: inline-flex;
  align-items: center;
  font-size: 0.9rem;
  color: #555;
}
</style>
