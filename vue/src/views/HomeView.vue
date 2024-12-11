<template>
  <body>
    <div class="home-view">
      <h2>Find Restaurants Near You</h2>

      <!-- Location Filters -->
      <div class="filter-card">
        <h3 class="filter-title">Search by Location</h3>
        <p class="filter-subtitle">
          Quickly find restaurants by providing a Zip Code or City and State.
        </p>
        <div class="location-filters">
          <div class="location-inputs">
            <div class="input-group" v-if="useZipCode">
              <label for="zip-code">Zip Code</label>
              <input
                id="zip-code"
                type="text"
                v-model="zipCode"
                placeholder="e.g. 90210"
                @input="clearError"
              />
            </div>

            <div class="city-state-inputs" v-else>
              <div class="input-group">
                <label for="city">City</label>
                <input
                  id="city"
                  type="text"
                  v-model="city"
                  placeholder="e.g. Los Angeles"
                  @input="clearError"
                />
              </div>
              <div class="input-group">
                <label for="state">State</label>
                <input
                  id="state"
                  type="text"
                  v-model="state"
                  placeholder="CA"
                  maxlength="2"
                  @input="clearError"
                />
              </div>
            </div>
          </div>

          <div class="location-buttons">
            <button class="btn-switch" @click="toggleLocationType">
              {{ useZipCode ? "Switch to City/State" : "Switch to Zip Code" }}
            </button>
            <button class="btn-search" @click="fetchRestaurants">Search</button>
            <button class="btn-more-filters" @click="toggleFiltersPanel">
              More Filters
            </button>
          </div>
        </div>

        <!-- Dynamic Filters Panel -->
        <transition name="slide-fade">
          <div v-if="filtersPanelOpen" class="dynamic-filters-panel">
            <h4 class="panel-title">Refine Your Search</h4>
            <p class="panel-subtitle">
              Narrow down results and find exactly what you’re craving.
            </p>

            <div class="secondary-filters">
              <input 
                type="text" 
                v-model="searchQuery" 
                placeholder="Search by name..."
                class="search-box"
              />

              <div class="checkbox-filters">
                <label>
                  <input type="checkbox" v-model="filterOpenNow" /> Open Now
                </label>
                <label>
                  <input type="checkbox" v-model="filterHighRating" /> 4★ & Up
                </label>
                <label>
                  <input type="checkbox" v-model="filterKidFriendly" /> Kid Friendly
                </label>
              </div>

              <button 
                class="btn-surprise" 
                @click="surpriseMe" 
                :disabled="!restaurants.length"
              >
                Surprise Me!
              </button>
            </div>
          </div>
        </transition>
      </div>

      <!-- Error Message -->
      <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>

      <!-- Restaurant Card Stack -->
      <div v-if="filteredRestaurants.length" class="stack-interface">
        <div
          v-for="(restaurant, index) in filteredRestaurants"
          :key="restaurant.id || index"
          class="restaurant-card-wrapper"
          :style="getCardStyle(index)"
          ref="cards"
          :class="{
            'swipe-right': index === activeIndex && swipeDirection === 'right',
            'swipe-left': index === activeIndex && swipeDirection === 'left'
          }"
        >
          <div class="overlay" v-if="index === activeIndex && (swipeDirection === 'right' || swipeDirection === 'left')">
            <div v-if="swipeDirection === 'right'" class="check-mark">✔</div>
            <div v-if="swipeDirection === 'left'" class="x-mark">✘</div>
          </div>
          <RestaurantCard :restaurant="restaurant" />
        </div>
      </div>

      <div v-else-if="!isLoading && hasSearched" class="no-results">
        No restaurants found. Try another search or adjust your filters.
      </div>
      <div v-if="isLoading" class="loading">Loading restaurants...</div>

      <!-- Voting Trigger Button -->
      <button id="open-voting-btn" @click="toggleVotingPanel">
        <span class="icon">🗳️</span> Vote
      </button>

      <!-- Slide-Out Voting Panel -->
      <div id="voting-panel" :class="{ visible: isVotingOpen }">
        <header class="voting-header">
          <h3>Local Picks</h3>
          <p class="subtitle">Vote for your favorite spot below</p>
          <button @click="toggleVotingPanel" class="close-btn">×</button>
        </header>
        <div class="voting-content">
          <ul class="voting-list">
            <li v-for="(option, index) in votingOptions" :key="index" :class="{voted: justVotedIndex === index}">
              <div class="voting-item">
                <div class="info">
                  <span class="restaurant-icon">🍽️</span>
                  <span class="restaurant-name">{{ option.name }}</span>
                </div>
                <div class="actions">
                  <span class="votes">Votes: <strong>{{ option.votes }}</strong></span>
                  <button @click="voteForOption(index)" class="vote-btn">Vote</button>
                </div>
              </div>
            </li>
          </ul>
        </div>
      </div>

      <!-- Audio elements for swipe sounds -->
      <audio ref="successSound" src="/Images/check.mp3" preload="auto"></audio>
      <audio ref="rejectSound" src="/Images/x.mp3" preload="auto"></audio>
    </div>
    <br>
  </body>
  
</template>

<script>
import RestaurantCard from "../components/RestaurantCard.vue";

export default {
  components: { RestaurantCard },
  data() {
    return {
      searchQuery: "",
      isVotingOpen: false,
      filtersPanelOpen: false,
      votingOptions: [
        { name: "Pizza Palace", votes: 0 },
        { name: "Sushi Spot", votes: 0 },
        { name: "Taco Time", votes: 0 },
      ],
      justVotedIndex: null,
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
      errorMessage: "",
      swipeDirection: null, // 'right' or 'left'
      // Additional filters
      filterOpenNow: false,
      filterHighRating: false,
      filterKidFriendly: false,
    };
  },
  computed: {
    filteredRestaurants() {
      return this.restaurants
        .filter(r => r.name.toLowerCase().includes(this.searchQuery.toLowerCase()))
        .filter(r => !this.filterOpenNow || r.openNow === true)
        .filter(r => !this.filterHighRating || (r.rating && r.rating >= 4))
        .filter(r => !this.filterKidFriendly || r.kidFriendly === true);
    },
    isAuthenticated() {
      return this.$store.state.token !== "";
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
    toggleFiltersPanel() {
      this.filtersPanelOpen = !this.filtersPanelOpen;
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
          if (!response.ok) {
            throw new Error("Failed to fetch restaurants");
          }
          return response.json();
        })
        .then((data) => {
          // Randomly assign kidFriendly to each restaurant if not already present
          this.restaurants = data.map(r => ({
            ...r,
            kidFriendly: Math.random() > 0.5 // Random true/false
          }));
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
    toggleVotingPanel() {
      this.isVotingOpen = !this.isVotingOpen;
    },
    voteForOption(index) {
      this.votingOptions[index].votes += 1;
      this.justVotedIndex = index;
      setTimeout(() => {
        this.justVotedIndex = null;
      }, 800);
    },
    handleMouseDown(e) {
      this.startX = e.clientX;
      this.isDragging = true;
      this.swipeDirection = null;
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

      const favoritedRestaurant = this.filteredRestaurants[this.activeIndex];
      if (this.currentX > 100) {
        // Swipe right - favorite
        this.swipeDirection = 'right';
        this.$refs.successSound.play();

        if (favoritedRestaurant && favoritedRestaurant.placeId) {
          this.addToFavorites(favoritedRestaurant.placeId);
        } else {
          console.error("No place_id found for the selected restaurant.");
        }

        this.animateCardRemoval(topCard, () => {
          const removeIndex = this.restaurants.indexOf(favoritedRestaurant);
          if (removeIndex !== -1) this.restaurants.splice(removeIndex, 1);
          this.highlightFavoritesLink();
        });
      } else if (this.currentX < -100) {
        // Swipe left - skip
        this.swipeDirection = 'left';
        this.$refs.rejectSound.play();

        this.animateCardRemoval(topCard, () => {
          const removeIndex = this.restaurants.indexOf(favoritedRestaurant);
          if (removeIndex !== -1) this.restaurants.splice(removeIndex, 1);
        });
      } else {
        // Reset position
        topCard.style.transition = "transform 0.3s ease";
        topCard.style.transform = "translateX(0) rotate(0)";
      }

      this.currentX = 0;
    },
    animateCardRemoval(card, callback) {
      card.style.transition = "transform 0.5s ease, opacity 0.5s ease";
      card.style.transform += " translateY(-200px)";
      card.style.opacity = "0";
      setTimeout(() => {
        callback();
        this.swipeDirection = null;
      }, 500);
    },
    addToFavorites(placeId) {
      if (!this.isAuthenticated) {
        console.error("User is not authenticated.");
        return;
      }

      fetch("http://localhost:9000/api/favorites", {
        method: "POST",
        headers: {
          Authorization: `Bearer ${this.$store.state.token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ place_id: placeId }),
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
    },
    highlightFavoritesLink() {
      const navLinks = document.querySelectorAll('.nav-links li a');
      const favoritesLink = Array.from(navLinks).find(link => link.textContent.trim() === 'Favorites');
      if (favoritesLink) {
        favoritesLink.classList.add('highlight');
        setTimeout(() => {
          favoritesLink.classList.remove('highlight');
        }, 1500);
      }
    },
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

body {
  background-color: #fff7ed;
}
.home-view {
  padding: 1rem;
  max-width: 900px;
  margin: 0 auto;
  font-family: "Lato", sans-serif;
}

h2 {
  text-align: center;
  margin-bottom: 2rem;
  font-family: "Playfair Display", serif;
  font-size: 2rem;
}

/* Filter Cards */
.filter-card {
  background: #fff;
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 1.5rem;
  margin-bottom: 1.5rem;
  box-shadow: 0 2px 6px rgba(0,0,0,0.05);
}

.filter-title {
  margin-top: 0;
  font-family: "Playfair Display", serif;
  font-size: 1.4rem;
  margin-bottom: 0.5rem;
  text-align: center;
}

.filter-subtitle {
  font-size: 0.9rem;
  color: #555;
  text-align: center;
  margin-bottom: 1.5rem;
}

/* Location Filters Layout */
.location-filters {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  align-items: flex-end;
  justify-content: space-between;
}

.location-inputs {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
  align-items: flex-end;
}

.city-state-inputs {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
  align-items: flex-end;
}

.input-group {
  display: flex;
  flex-direction: column;
}

.input-group label {
  font-size: 0.85rem;
  margin-bottom: 0.3rem;
  font-weight: bold;
  color: #333;
}

.location-inputs input {
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 0.9rem;
}

.location-buttons {
  display: flex;
  gap: 0.5rem;
}

.btn-switch, .btn-search, .btn-more-filters {
  background: #e0c9a6;
  color: #fff;
  padding: 0.6rem 1rem;
  border: none;
  border-radius: 4px;
  font-size: 0.9rem;
  font-weight: bold;
  cursor: pointer;
  white-space: nowrap;
  transition: background 0.3s ease;
}
.btn-switch:hover, .btn-search:hover, .btn-more-filters:hover {
  background: #e7b76b;
}

/* Dynamic Filters Panel */
.dynamic-filters-panel {
  margin-top: 1rem;
  background: #fff;
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 1rem;
  box-shadow: 0 1px 4px rgba(0,0,0,0.05);
}

.panel-title {
  font-family: "Playfair Display", serif;
  font-size: 1.2rem;
  margin-bottom: 0.5rem;
  text-align: center;
}

.panel-subtitle {
  font-size: 0.85rem;
  color: #555;
  text-align: center;
  margin-bottom: 1rem;
}

/* Secondary Filters Inside Panel */
.secondary-filters {
  display: flex;
  align-items: center;
  gap: 1rem;
  justify-content: center;
  flex-wrap: wrap;
}

.search-box {
  flex: 1;
  max-width: 180px;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 0.9rem;
}

.checkbox-filters {
  display: flex;
  align-items: center;
  gap: 1rem;
  font-size: 0.9rem;
}

.checkbox-filters label {
  display: flex;
  align-items: center;
  gap: 0.3rem;
  font-weight: bold;
  color: #555;
}

.btn-surprise {
  background: #737f9a;
  color: #fff;
  padding: 0.6rem 1.2rem;
  border: none;
  border-radius: 4px;
  font-size: 0.9rem;
  font-weight: bold;
  cursor: pointer;
  white-space: nowrap;
  transition: background 0.3s ease;
}
.btn-surprise:hover {
  background: #0056b3;
}
.btn-surprise:disabled {
  background: #ccc;
  cursor: not-allowed;
}

/* Error Message */
.error-message {
  color: red;
  text-align: center;
  margin-bottom: 1rem;
  font-weight: bold;
}

/* No Results */
.no-results {
  text-align: center;
  font-size: 1.2rem;
  color: gray;
  margin-bottom: 1rem;
}

/* Loading */
.loading {
  text-align: center;
  font-size: 1.5rem;
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
  cursor: grab;
}

/* Swipe Overlays */
.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display:flex;
  justify-content: center;
  align-items: center;
  font-size: 4rem;
  color: white;
  z-index: 10;
  pointer-events: none;
}
.check-mark {
  color: green;
  font-size: 4rem;
}
.x-mark {
  color: red;
  font-size: 4rem;
}

/* Highlight for Favorites Link */
.nav-links li a.highlight {
  background-color: #c8e6c9 !important;
  color: #2e7d32 !important;
  box-shadow: 0 0 10px 2px #c8e6c9;
}

/* Voting Panel Styles */
#voting-panel {
  position: fixed;
  top: 0;
  right: -320px;
  width: 320px;
  height: 100%;
  background-color: #fff7ed;
  box-shadow: -2px 0 8px rgba(0, 0, 0, 0.15);
  transition: right 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 1001;
  display: flex;
  flex-direction: column;
}

#voting-panel.visible {
  right: 0;
}

.voting-header {
  background-color: #e0c9a6;
  color: white;
  padding: 20px;
  position: relative;
  text-align: center;
}
.voting-header h3 {
  margin: 0;
  font-size: 1.5rem;
  font-family: 'Playfair Display', serif;
}
.voting-header .subtitle {
  margin: 5px 0 0;
  font-size: 0.9rem;
  font-family: 'Lato', sans-serif;
  opacity: 0.9;
}

.close-btn {
  background: none;
  border: none;
  color: white;
  font-size: 1.6rem;
  position: absolute;
  top: 10px;
  right: 15px;
  cursor: pointer;
  font-weight: bold;
  line-height: 1;
  transition: transform 0.2s;
}
.close-btn:hover {
  transform: scale(1.2);
}

.voting-content {
  padding: 15px;
  overflow-y: auto;
  flex: 1;
  font-family: 'Lato', sans-serif;
}

.voting-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.voting-list li {
  background: #ffffff;
  border-radius: 8px;
  margin-bottom: 10px;
  padding: 10px;
  transition: background-color 0.3s ease;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.voting-list li.voted {
  background-color: #e0f7df;
}

.voting-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.voting-item .info {
  display: flex;
  align-items: center;
  gap: 8px;
}
.restaurant-icon {
  font-size: 1.2rem;
}
.restaurant-name {
  font-weight: bold;
  font-size: 1rem;
  color: #5c5c5c;
}

.actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.votes {
  font-size: 0.9rem;
  color: #555;
}
.votes strong {
  color: #333;
}

.vote-btn {
  background-color: #e0c9a6;
  color: #fff;
  border: none;
  border-radius: 5px;
  padding: 5px 12px;
  cursor: pointer;
  font-size: 0.9rem;
  font-weight: bold;
  transition: background-color 0.3s ease, transform 0.1s ease;
}
.vote-btn:hover {
  background-color: #e0c9a6;
}
.vote-btn:active {
  transform: scale(0.95);
}

/* Voting Trigger Button */
#open-voting-btn {
  position: fixed;
  bottom: 40px;
  right: 20px;
  background-color: #e0c9a6;
  color: white;
  padding: 10px 15px;
  border-radius: 8px;
  border: none;
  font-size: 1rem;
  cursor: pointer;
  transition: transform 0.2s, background-color 0.3s;
  display: flex;
  align-items: center;
  gap: 8px;
  font-family: 'Lato', sans-serif;
  box-shadow: 0 2px 4px rgba(0,0,0,0.2);
}
#open-voting-btn:hover {
  transform: scale(1.05);
  background-color: #a56d4e;
}
#open-voting-btn .icon {
  font-size: 1.2rem;
}

/* Transitions */
.slide-fade-enter-active, .slide-fade-leave-active {
  transition: all 0.3s ease;
}
.slide-fade-enter, .slide-fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

</style>
