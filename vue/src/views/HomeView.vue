<template>
  <div class="home-view">
    <h2>Find Your Next Favorite Restaurant</h2>
    <div class="filters">
      <input type="text" v-model="searchQuery" placeholder="Search restaurants..." />
      <button @click="surpriseMe">Surprise Me!</button>
    </div>
    <div class="stack-interface">
      <div
        v-for="(restaurant, index) in filteredRestaurants"
        :key="restaurant.id"
        class="restaurant-card"
        :style="getCardStyle(index)"
        ref="cards"
      >
        <RestaurantCard :restaurant="restaurant" />
      </div>
    </div>
  </div>
</template>

<script>
import RestaurantCard from '../components/RestaurantCard.vue';

export default {
  components: { RestaurantCard },
  data() {
    return {
      searchQuery: '',
      restaurants: [
        {
          id: '1',
          name: 'The Tasty Spoon',
          image: '../images/spoon.jpg',
          rating: 4.5,
          price: 2,
          isKidFriendly: true,
          isOpenNow: true,
          distance: 1.2,
          cuisine: 'Italian',
        },
        {
          id: '2',
          name: 'Burger Bliss',
          image: '../images/burger.jpg',
          rating: 4.2,
          price: 1,
          isKidFriendly: false,
          isOpenNow: false,
          distance: 2.8,
          cuisine: 'American',
        },
        {
          id: '3',
          name: 'Sushi Heaven',
          image: '../images/sushi.jpg',
          rating: 4.8,
          price: 3,
          isKidFriendly: true,
          isOpenNow: true,
          distance: 0.5,
          cuisine: 'Japanese',
        },
        {
          id: '4',
          name: 'Bens Buffet',
          image: '../images/ben.jpg',
          rating: 2.1,
          price: 3,
          isKidFriendly: true,
          isOpenNow: true,
          distance: 0.5,
          cuisine: 'Chinese',
        },
        {
          id: '5',
          name: 'Karyns Cafe',
          image: '../images/karyn.jpg',
          rating: 4.8,
          price: 4,
          isKidFriendly: false,
          isOpenNow: false,
          distance: 0.5,
          cuisine: 'Euro-Bistro',
        },
      ],
    };
  },
  computed: {
    filteredRestaurants() {
      return this.restaurants.filter((r) =>
        r.name.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    },
  },
  watch: {
    restaurants: {
      handler() {
        this.reinitializeListeners(); // Reattach listeners when restaurants array changes
      },
      deep: true,
    },
  },
  methods: {
    surpriseMe() {
      const randomIndex = Math.floor(Math.random() * this.restaurants.length);
      alert(`How about ${this.restaurants[randomIndex].name}?`);
    },
    getCardStyle(index) {
      return {
        zIndex: this.restaurants.length - index,
        transform: index === 0 ? 'translateX(0)' : 'translateY(10px) scale(0.95)',
        opacity: index === 0 ? 1 : 0.8,
      };
    },
    handleSwipe(index, direction) {
      console.log(`Restaurant ${this.restaurants[index].id} swiped ${direction}`);
      this.restaurants.splice(index, 1); // Remove the swiped restaurant
    },
    addDragListeners() {
      const cards = Array.from(this.$refs.cards);
      if (cards.length === 0) return;

      const topCard = cards[0]; // Only attach listeners to the top card
      let startX = 0;
      let currentX = 0;
      let isDragging = false;

      const handleMouseDown = (e) => {
        startX = e.clientX;
        isDragging = true;
        topCard.style.transition = 'none'; // Disable transitions while dragging
      };

      const handleMouseMove = (e) => {
        if (!isDragging) return;
        currentX = e.clientX - startX;
        topCard.style.transform = `translateX(${currentX}px) rotate(${currentX / 20}deg)`;
      };

      const handleMouseUp = () => {
        if (!isDragging) return;
        isDragging = false;
        topCard.style.transition = 'transform 0.3s ease';

        if (currentX > 100) {
          // Swipe right
          this.handleSwipe(0, 'right');
        } else if (currentX < -100) {
          // Swipe left
          this.handleSwipe(0, 'left');
        } else {
          // Reset position
          topCard.style.transform = 'translateX(0) rotate(0)';
        }
      };

      topCard.addEventListener('mousedown', handleMouseDown);
      document.addEventListener('mousemove', handleMouseMove);
      document.addEventListener('mouseup', handleMouseUp);

      // Cleanup previous listeners to avoid duplication
      this.cleanupListeners = () => {
        topCard.removeEventListener('mousedown', handleMouseDown);
        document.removeEventListener('mousemove', handleMouseMove);
        document.removeEventListener('mouseup', handleMouseUp);
      };
    },
    reinitializeListeners() {
      if (this.cleanupListeners) this.cleanupListeners(); // Cleanup existing listeners
      this.addDragListeners(); // Add listeners for the new top card
    },
  },
  mounted() {
    this.addDragListeners(); // Initialize drag listeners
  },
};
</script>

<style scoped>
.home-view {
  padding: 1rem;
  max-width: 900px;
  margin: 0 auto;
}

.filters {
  display: flex;
  justify-content: space-between;
  margin-bottom: 1rem;
}

.filters input {
  flex: 1;
  margin-right: 1rem;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.filters button {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  background-color: #007bff;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
}

.filters button:hover {
  background-color: #0056b3;
}

.stack-interface {
  position: relative;
  width: 100%;
  height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.restaurant-card {
  position: absolute;
  width: 300px;
  max-width: 100%;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  background: white; /* Prevent overlapping text visibility */
  transition: transform 0.3s ease, opacity 0.3s ease;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.restaurant-card:hover {
  box-shadow: 0 8px 12px rgba(0, 0, 0, 0.2);
}
</style>
