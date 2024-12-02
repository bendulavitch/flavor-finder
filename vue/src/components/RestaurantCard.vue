<template>
    <div class="restaurant-card">
      <img :src="restaurant.image" alt="Restaurant Image" class="restaurant-image" />
      <div class="restaurant-details">
        <h3>{{ restaurant.name }}</h3>
        <p>Rating: {{ restaurant.rating }} ⭐</p>
        <p>Distance: {{ restaurant.distance }} miles</p>
        <p v-if="restaurant.kidFriendly">Kid-Friendly</p>
        <button @click="$emit('add-favorite', restaurant)">Add to Favorites</button>
        <button @click="$emit('skip', restaurant)">Skip</button>
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
  
  <style>
  .restaurant-card {
    border: 1px solid #ddd;
    border-radius: 8px;
    padding: 1rem;
    display: flex;
    flex-direction: column;
    align-items: center;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }
  .restaurant-image {
    width: 100%;
    height: auto;
    border-radius: 8px;
  }
  .restaurant-details {
    text-align: center;
    margin-top: 1rem;
  }
  button {
    margin: 0.5rem;
    padding: 0.5rem 1rem;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  button:hover {
    background-color: #ddd;
  }
  </style>
  