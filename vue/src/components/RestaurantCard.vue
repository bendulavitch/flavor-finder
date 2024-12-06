<template>
  <div class="restaurant-card">
    <img 
      :src="restaurant.image || 'ben.jpg'" 
      alt="Restaurant Image" 
      class="restaurant-image" 
    />
    <div class="restaurant-details">
      <h2>{{ restaurant.name }}</h2>
      <p class="address">{{ restaurant.address }}</p>
      <div class="rating-section">
        <span v-if="restaurant.rating" class="rating">⭐ {{ restaurant.rating }}</span>
        <span v-else>No Rating Available</span>
      </div>
      <p v-if="restaurant.cityName && restaurant.stateName">
        {{ restaurant.cityName }}, {{ restaurant.stateName }}
      </p>
      <p v-if="restaurant.phone">📞 {{ restaurant.phone }}</p>
      <a 
        v-if="restaurant.website" 
        :href="restaurant.website" 
        target="_blank" 
        class="website-link"
      >
        Visit Website
      </a>
      <p v-if="restaurant.cuisineType">Cuisine: {{ restaurant.cuisineType }}</p>
      <p v-if="restaurant.hoursInterval">Hours: {{ restaurant.hoursInterval }}</p>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    restaurant: {
      type: Object,
      required: true,
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
</style>
