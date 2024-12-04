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
        class="restaurant-card-wrapper"
        :style="getCardStyle(index)"
        ref="cards"
      >
        <RestaurantCard :restaurant="restaurant" />
      </div>
    </div>

    <!-- Slide-Out Messaging Panel -->
    <div id="messaging-panel" :class="{ visible: isMessagingOpen }">
      <header class="messaging-header">
        <h3>Inbox</h3>
        <button @click="toggleMessaging" class="close-btn">X</button>
      </header>
      <div class="messaging-content">
        <ul class="message-list">
          <li v-for="message in messages" :key="message.id" @click="openChat(message.id)">
            <h4>{{ message.title }}</h4>
            <p>{{ message.preview }}</p>
          </li>
        </ul>
        <button class="new-chat-btn" @click="createNewChat">+ New Chat</button>
      </div>
    </div>
    <!-- Open Messaging Button -->
    <button id="open-messaging-btn" @click="toggleMessaging">Messages</button>
  </div>
</template>

<script>
import RestaurantCard from '../components/RestaurantCard.vue';

export default {
  components: { RestaurantCard },
  data() {
    return {
      searchQuery: '',
      isMessagingOpen: false,
      messages: [
        { id: 1, title: "Group 1", preview: "Last message in Group 1" },
        { id: 2, title: "Group 2", preview: "Last message in Group 2" },
      ],
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
      isDragging: false,
      startX: 0,
      currentX: 0,
      activeIndex: 0,
    };
  },
  computed: {
    filteredRestaurants() {
      return this.restaurants.filter((r) =>
        r.name.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    },
  },
  
  methods: {
    surpriseMe() {
      const randomIndex = Math.floor(Math.random() * this.restaurants.length);
      alert(`How about ${this.restaurants[randomIndex].name}?`);
    },
    getCardStyle(index) {
      if (index === this.activeIndex) {
        return { zIndex: 100, transform: "translateX(0)", opacity: 1 };
      }
      return {
        zIndex: this.filteredRestaurants.length - index,
        transform: `translateY(${10 * index}px) scale(${1 - index * 0.05})`,
        opacity: 0.8 - index * 0.1,
      };
    },
    toggleMessaging() {
      this.isMessagingOpen = !this.isMessagingOpen;
    },
    openChat(chatId) {
      alert(`Opening chat with ID: ${chatId}`);
    },
    createNewChat() {
      alert('Creating a new chat...');
    },
    handleMouseDown(e) {
      this.startX = e.clientX;
      this.isDragging = true;
    },
    handleMouseMove(e) {
      if (!this.isDragging) return;
      this.currentX = e.clientX - this.startX;
      const topCard = this.$refs.cards[this.activeIndex];
      if (topCard) {
        topCard.style.transition = "none";
        topCard.style.transform = `translateX(${this.currentX}px) rotate(${this.currentX / 20}deg)`;
      }
    },
    handleMouseUp() {
      if (!this.isDragging) return;
      this.isDragging = false;
      const topCard = this.$refs.cards[this.activeIndex];
      if (!topCard) return;

      if (this.currentX > 100) {
        // Swipe right
        this.restaurants.splice(this.activeIndex, 1);
      } else if (this.currentX < -100) {
        // Swipe left
        this.restaurants.splice(this.activeIndex, 1);
      } else {
        // Reset position
        topCard.style.transition = "transform 0.3s ease";
        topCard.style.transform = "translateX(0) rotate(0)";
      }
      this.currentX = 0;
    },
  },
  mounted() {
    const cards = this.$refs.cards;
    cards.forEach((card) => {
      card.addEventListener("mousedown", this.handleMouseDown);
      document.addEventListener("mousemove", this.handleMouseMove);
      document.addEventListener("mouseup", this.handleMouseUp);
    });
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

.restaurant-card-wrapper {
  position: absolute;
  width: 300px;
  max-width: 100%;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  background: white;
  transition: transform 0.3s ease, opacity 0.3s ease;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.restaurant-card-wrapper:not(:nth-child(1)) {
  visibility: hidden; /* Hide cards beneath to prevent overlapping content */
}

.restaurant-card-wrapper.active {
  visibility: visible;
}

/* Slide-Out Messaging Panel */
#messaging-panel {
  position: fixed;
  top: 0;
  right: -300px; /* Initially hidden */
  width: 300px;
  height: 100%;
  background-color: #fff7ed; /* Light cream */
  box-shadow: -2px 0 5px rgba(0, 0, 0, 0.1);
  transition: right 0.3s ease-in-out; /* Slide-in animation */
  z-index: 1001;
}

#messaging-panel.visible {
  right: 0; /* Slide into view */
}

.messaging-header {
  background-color: #c88f67; /* Muted orange */
  color: white;
  padding: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.messaging-content {
  padding: 15px;
  overflow-y: auto;
}

.message-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.message-list li {
  padding: 10px;
  margin-bottom: 10px;
  background-color: #f9f1e7; /* Soft beige */
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.message-list li:hover {
  background-color: #e0c9a6; /* Hover effect */
}

.new-chat-btn {
  display: block;
  width: 100%;
  padding: 10px;
  margin-top: 10px;
  background-color: #c88f67; /* Muted orange */
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.new-chat-btn:hover {
  background-color: #a56d4e; /* Darker orange */
}

/* Open Messaging Button */
#open-messaging-btn {
  position: fixed;
  bottom: 20px;
  right: 20px;
  background-color: #c88f67; /* Muted orange */
  color: white;
  padding: 10px 15px;
  border-radius: 50%;
  border: none;
  font-size: 1.2rem;
  cursor: pointer;
  transition: transform 0.2s, background-color 0.3s;
}

#open-messaging-btn:hover {
  transform: scale(1.1);
  background-color: #a56d4e; /* Darker orange */
}

</style>
