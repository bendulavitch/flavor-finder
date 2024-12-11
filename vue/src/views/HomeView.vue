<template>
  <div class="home-view">
    <h2>Find Restaurants Near You</h2>
    <div class="filters">
      <!-- Location Filters for Zip Code or City/State -->
      <div v-if="useZipCode">
        <input
          type="text"
          v-model="zipCode"
          placeholder="Enter Zip Code"
          @input="clearError"
        />
      </div>
      <div v-else>
        <input
          type="text"
          v-model="city"
          placeholder="Enter City"
          @input="clearError"
        />
        <input
          type="text"
          v-model="state"
          placeholder="Enter State"
          maxlength="2"
          @input="clearError"
        />
      </div >
      <button class="filters" @click="toggleLocationType">
        {{ useZipCode ? "Switch to City/State" : "Switch to Zip Code" }}
      </button>
      <button class="filters" @click="fetchRestaurants">Search</button>
    </div>

    <!-- Error Message -->
    <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>

    <!-- Filters for Searching Restaurants -->
    <div class="filters">
      <input type="text" v-model="searchQuery" placeholder="Search restaurants..." />
      <button @click="surpriseMe" :disabled="!restaurants.length">Surprise Me!</button>
    </div>

    <!-- Restaurant Card Stack -->
    <div v-if="restaurants.length" class="stack-interface">
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
    <div v-else-if="!isLoading && hasSearched" class="no-results">
      No restaurants found. Try another search.
    </div>
    <div v-if="isLoading" class="loading">Loading restaurants...</div>

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
import RestaurantCard from "../components/RestaurantCard.vue";

export default {
  components: { RestaurantCard },
  data() {
    return {
      searchQuery: "",
      isMessagingOpen: false,
      messages: [
        { id: 1, title: "Group 1", preview: "Last message in Group 1" },
        { id: 2, title: "Group 2", preview: "Last message in Group 2" },
      ],
      restaurants: [],
      isDragging: false,
      startX: 0,
      currentX: 0,
      activeIndex: 0,
      useZipCode: true,
      zipCode: "",
      city: "",
      state: "",
      isLoading: false,
      hasSearched: false,
      errorMessage: "", // To display user-friendly errors
    };
  },
  computed: {
    filteredRestaurants() {
      return this.restaurants.filter((r) =>
        r.name.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    },
    isAuthenticated() {
      return this.$store.state.token !== ""; // Check if the user is authenticated
    },
  },
  methods: {
    toggleLocationType() {
      this.useZipCode = !this.useZipCode;
      this.zipCode = "";
      this.city = "";
      this.state = "";
      this.clearError();
    },
    clearError() {
      this.errorMessage = "";
    },
    fetchRestaurants() {
      this.errorMessage = "";
      this.isLoading = true;
      this.hasSearched = true;

      // Validate Input
      if (this.useZipCode && !this.zipCode.trim()) {
        this.errorMessage = "Please enter a valid Zip Code.";
        this.isLoading = false;
        return;
      } else if (!this.useZipCode && (!this.city.trim() || !this.state.trim())) {
        this.errorMessage = "Please enter a valid City and State.";
        this.isLoading = false;
        return;
      }

      const queryType = this.useZipCode ? "zipcode" : "city";
      const queryValue = this.useZipCode
        ? this.zipCode.trim()
        : `${this.city.trim()},${this.state.trim()}`;

      fetch(
        `http://localhost:9000/api/restaurants/fetch?queryType=${queryType}&queryValue=${queryValue}`
      )
        .then((response) => {
          console.log(response);
          if (!response.ok) {
            throw new Error("Failed to fetch restaurants");
          }
          return response.json();
        })
        .then((data) => {
          this.restaurants = data;
          if (!data.length) {
            this.errorMessage = "No restaurants found. Try another search.";
          }
          this.isLoading = false;
        })
        .catch((error) => {
          console.error("Error fetching restaurants:", error);
          this.errorMessage = "An error occurred while fetching restaurants.";
          this.isLoading = false;
        });
    },
    surpriseMe() {
      if (!this.restaurants.length) return;
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
      alert("Creating a new chat...");
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

  const topCard = this.$refs.cards[this.activeIndex]; // Get the current top card
  if (!topCard) return;

  const favoritedRestaurant = this.restaurants[this.activeIndex]; // Get the restaurant corresponding to the top card
      console.log(this.activeIndex.placeId);
  if (this.currentX > 100) {
    // Swipe right to add to favorites
    if (favoritedRestaurant && favoritedRestaurant.placeId) {
      this.addToFavorites(favoritedRestaurant.placeId); // Call addToFavorites with place_id
    } else {
      console.error("No place_id found for the selected restaurant.");
    }
    this.restaurants.splice(this.activeIndex, 1); // Remove card from stack
  } else if (this.currentX < -100) {
    // Swipe left to skip
    this.restaurants.splice(this.activeIndex, 1); // Remove card from stack
  } else {
    // Reset position
    topCard.style.transition = "transform 0.3s ease";
    topCard.style.transform = "translateX(0) rotate(0)";
  }

  this.currentX = 0; // Reset drag distance
}


,
    attachCardListeners() {
      const cards = this.$refs.cards || [];
      cards.forEach((card) => {
        if (card) {
          card.addEventListener("mousedown", this.handleMouseDown);
          document.addEventListener("mousemove", this.handleMouseMove);
          document.addEventListener("mouseup", this.handleMouseUp);
        }
      });
    },
    addToFavorites(placeId) {
  if (!this.isAuthenticated) {
    console.error("User is not authenticated.");
    return;
  }

  console.log("Sending payload with place_id:", { place_id: placeId });

  fetch("http://localhost:9000/api/favorites", {
    method: "POST",
    headers: {
      Authorization: `Bearer ${this.$store.state.token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ place_id: placeId }), // Send place_id
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Failed to add to favorites");
      }
      console.log(`Place ID ${placeId} added to favorites!`);
    })
    .catch((error) => {
      console.error("Error adding to favorites:", error);
    });
}


,
  },
  watch: {
    restaurants(newRestaurants) {
      if (newRestaurants.length) {
        this.$nextTick(() => {
          this.attachCardListeners();
        });
      }
    },
  },
  mounted() {
    this.attachCardListeners();
  },
};



</script>



<style scoped>
.home-view {
  padding: 1rem;
  max-width: 900px;
  margin: 0 auto;
}

/* Filters Section */
.filters {
  display: flex;
  flex-direction: column; /* Adjusted for vertical layout with location input */
  gap: 1rem;
  margin-bottom: 1rem;
}

.filters .location-inputs {
  display: flex;
  flex-wrap: wrap; /* Wrap inputs on smaller screens */
  gap: 1rem;
}

.filters button {
  padding: 0.6rem 1.2rem;
  font-size: 1rem;
  font-weight: bold;
  background-color: #f49f0a; /* Attractive orange */
  color: #fff;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  text-transform: uppercase;
}

.filters button:hover {
  background-color: #d9890c; /* Darker orange */
  transform: scale(1.05);
}

.filters input {
  padding: 0.6rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 1rem;
  width: calc(100% - 2rem); /* Ensure proper width with padding */
}
/* Loading Indicator */
.filters .loading-indicator {
  text-align: center;
  font-size: 1.2rem;
  color: #555;
}

/* Stack Interface */
.stack-interface {
  position: relative;
  width: 100%;
  height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

/* Restaurant Card Wrapper */
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
