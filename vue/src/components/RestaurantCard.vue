<template>
  <div class="restaurant-card">
    <img :src="restaurant.image" alt="Restaurant Image" class="restaurant-image" />
    <div class="restaurant-details">
      <h2>{{ restaurant.name }}</h2>
      <div class="rating-price">
        <span class="rating">⭐ {{ restaurant.rating }}</span>
        <span class="price">{{ "$".repeat(restaurant.price || 0) }}</span>
      </div>
      <p>{{ restaurant.distance.toFixed(1) }} miles away</p>
      <p v-if="restaurant.kidFriendly">Kid-Friendly</p>
      <p :class="{ open: restaurant.isOpenNow, closed: !restaurant.isOpenNow }">
        {{ restaurant.isOpenNow ? "Open Now" : "Closed" }}
      </p>
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
}
.restaurant-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}
.restaurant-details {
  padding: 1rem;
}
.rating-price {
  display: flex;
  justify-content: space-between;
  font-size: 0.9rem;
  margin: 0.5rem 0;
}
.open {
  color: green;
}
.closed {
  color: red;
}
</style>
