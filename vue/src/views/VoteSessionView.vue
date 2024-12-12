<template>
    <body>
        <br>
        <br>
      <div class="vote-session">
        <h2>Group Voting Session</h2>
        
        <!-- Conditional Rendering -->
        <div v-if="loading" class="loading">Loading session...</div>
        <div v-else-if="errorMessage" class="error-message">{{ errorMessage }}</div>
        <div v-else-if="voteSession">
          <!-- <p><strong>Location:</strong> {{ voteSession.queryValue }}</p>
          <p><strong>Current Round:</strong> {{ voteSession.round }}</p> -->
  
          <div v-if="voteSession.round === 1">
            <h3>Round 1: Swipe or Click to Vote</h3>
            <p class="round-info">Right Swipe/Click = In Favor (✔), Left Swipe = Against (✘)</p>
            <div class="stack-interface">
              <div
                v-for="(option, index) in remainingOptions"
                :key="option.id"
                class="restaurant-card-wrapper"
                :style="getCardStyle(index)"
                ref="cards"
                @mousedown="handleMouseDown"
              >
                <div class="overlay" v-if="index === 0 && (swipeDirection === 'right' || swipeDirection === 'left')">
                  <div v-if="swipeDirection === 'right'" class="check-mark">✔</div>
                  <div v-if="swipeDirection === 'left'" class="x-mark">✘</div>
                </div>
                <div class="restaurant-card" @click="handleCardClick(index)">
                  <img 
                    :src="option.image || 'ben.jpg'" 
                    alt="Restaurant Image" 
                    class="restaurant-image" 
                  />
                  <div class="restaurant-details">
                    <h2>{{ option.name }}</h2>
                    <p class="address">{{ option.address }}</p>
                    <p v-if="option.hoursInterval">🕒 {{ option.hoursInterval }}</p>
                    <p>
                      <span v-if="option.openNow" class="open-now">✅ Open Now</span>
                      <span v-else class="closed">❌ Closed</span>
                    </p>
                  </div>
                </div>
              </div>
            </div>
            <div v-if="remainingOptions.length === 0">
              <button @click="startRoundTwo" class="btn-proceed">Proceed to Round 2</button>
            </div>
          </div>
  
          <div v-if="voteSession.round === 2">
            <h3>Round 2: Vote Finalists</h3>
            <p class="round-info">Click "Vote For" (✔) or "Vote Against" (✘) to finalize your choice.</p>
            <div class="finalists-grid">
              <div v-for="option in finalists" :key="option.id" class="finalist-card">
                <h4>{{ option.name }}</h4>
                <p>Round 1 Votes: {{ option.votesRound1 }}</p>
                <p>Round 2 Votes: {{ option.votesRound2 }}</p>
                <div class="final-actions">
                  <button @click="castFinalVote(option.id, true)" class="vote-for-btn">✔</button>
                  <button @click="castFinalVote(option.id, false)" class="vote-against-btn">✘</button>
                </div>
                <div class="overlay" v-if="finalVoteOverlay[option.id]">
                  <div v-if="finalVoteOverlay[option.id] === 'for'" class="check-mark">✔</div>
                  <div v-if="finalVoteOverlay[option.id] === 'against'" class="x-mark">✘</div>
                </div>
              </div>
            </div>
            <div class="done-button" v-if="finalists.length > 0 && !winner && !tieWinners.length">
              <button @click="showWinners" class="btn-show-winner">Show Winner</button>
            </div>
          </div>
  
          <!-- Tie scenario -->
          <div v-if="tieWinners.length > 1">
            <h3>Tied Restaurants:</h3>
            <div class="finalists-grid">
              <div v-for="tw in tieWinners" :key="tw.id" class="finalist-card">
                <h4>{{ tw.name }}</h4>
              </div>
            </div>
            <p class="tie-info">Multiple top choices are tied! Click "Pick For Me" to select one at random.</p>
            <button @click="pickForMe" class="btn-pick-for-me">Pick For Me</button>
          </div>
  
          <div v-if="winner && tieWinners.length <= 1">
            <h3>Winner Selected:</h3>
            <p class="winner-name">{{ winner.name }}</p>
          </div>
        </div>
  
        <!-- Audio elements for swipe sounds -->
        <audio ref="successSound" src="/Images/check.mp3" preload="auto"></audio>
        <audio ref="rejectSound" src="/Images/x.mp3" preload="auto"></audio>
      </div>
      <br>
      <br>
    </body>
  </template>
  
  <script>
  export default {
    data() {
      return {
        voteSession: null,
        errorMessage: "",
        loading: true,
        activeIndex: 0,
        currentX: 0,
        startX: 0,
        isDragging: false,
        swipeDirection: null,
        finalists: [],
        winner: null,
        tieWinners: [],
        finalVoteOverlay: {} 
      }
    },
    computed: {
      remainingOptions() {
        if (!this.voteSession) return [];
        return this.voteSession.restaurantOptions.slice(this.activeIndex);
      }
    },
    created() {
      const roomCode = this.$route.params.roomCode;
      fetch(`http://localhost:9000/api/votes/code/${roomCode}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      })
      .then(res => res.ok ? res.json() : Promise.reject('Session not found'))
      .then(data => {
        this.voteSession = data;
        this.loading = false;
        if (this.voteSession.round === 2) {
          this.loadFinalists();
        } else {
          this.fetchDetailsForRoundOne();
        }
      })
      .catch(err => {
        console.error(err);
        this.errorMessage = "Could not load vote session.";
        this.loading = false;
      });
    },
    methods: {
      fetchDetailsForRoundOne() {
        this.voteSession.restaurantOptions.forEach(opt => {
          if (!opt.image || opt.image.includes('default-image-path')) {
            console.log("Attempting to fetch detailed image for", opt.name);
            // Here you'd call a detail endpoint if implemented.
          }
        });
      },
      handleMouseDown(e) {
        this.startX = e.clientX;
        this.isDragging = true;
        this.swipeDirection = null;
        document.addEventListener("mousemove", this.handleMouseMove);
        document.addEventListener("mouseup", this.handleMouseUp);
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
        document.removeEventListener("mousemove", this.handleMouseMove);
        document.removeEventListener("mouseup", this.handleMouseUp);
  
        const topCard = this.$refs.cards[this.activeIndex];
        if (!topCard) return;
  
        const currentOption = this.remainingOptions[0];
        if (this.currentX > 100) {
          // Right swipe = in favor
          this.swipeDirection = 'right';
          this.$refs.successSound.play();
          this.castVote(currentOption.id, 1, true);
          this.animateCardRemoval(topCard);
        } else if (this.currentX < -100) {
          // Left swipe = against
          this.swipeDirection = 'left';
          this.$refs.rejectSound.play();
          this.castVote(currentOption.id, 1, false);
          this.animateCardRemoval(topCard);
        } else {
          topCard.style.transition = "transform 0.3s ease";
          topCard.style.transform = "translateX(0) rotate(0)";
        }
  
        this.currentX = 0;
      },
      // handleCardClick(index) {
      //   // Optional: clicking card can count as in favor
      //   const currentOption = this.remainingOptions[0];
      //   this.swipeDirection = 'right';
      //   this.$refs.successSound.play();
      //   this.castVote(currentOption.id, 1, true);
      //   const topCard = this.$refs.cards[this.activeIndex];
      //   if (topCard) {
      //     this.animateCardRemoval(topCard);
      //   }
      // },
      animateCardRemoval(card) {
        card.style.transition = "transform 0.5s ease, opacity 0.5s ease";
        card.style.transform += " translateY(-200px)";
        card.style.opacity = "0";
        setTimeout(() => {
          this.voteSession.restaurantOptions.splice(this.activeIndex, 1);
          this.swipeDirection = null;
        }, 500);
      },
      castVote(optionId, round, inFavor) {
        fetch(`http://localhost:9000/api/votes/${this.voteSession.id}/vote`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem('token')}`
          },
          body: JSON.stringify({ restaurantOptionId: optionId, round: round, inFavor: inFavor })
        }).catch(err => console.error('Vote failed:', err));
      },
      startRoundTwo() {
        fetch(`http://localhost:9000/api/votes/${this.voteSession.id}/round2`, {
          method: 'POST',
          headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
        })
        .then(res => res.ok ? res.text() : Promise.reject('Failed to start round 2'))
        .then(() => {
          this.voteSession.round = 2;
          this.loadFinalists();
        })
        .catch(err => console.error(err));
      },
      loadFinalists() {
        fetch(`http://localhost:9000/api/votes/${this.voteSession.id}/finalists?limit=5`, {
          headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
        })
        .then(res => res.ok ? res.json() : Promise.reject('No finalists'))
        .then(data => {
          this.finalists = data;
        })
        .catch(err => console.error(err));
      },
      castFinalVote(optionId, inFavor) {
        // Show overlay and sound
        this.finalVoteOverlay = { ...this.finalVoteOverlay, [optionId]: inFavor ? 'for' : 'against' };
        if (inFavor) {
          this.$refs.successSound.play();
        } else {
          this.$refs.rejectSound.play();
        }
        setTimeout(() => {
          this.finalVoteOverlay = { ...this.finalVoteOverlay, [optionId]: null };
        }, 600);
  
        fetch(`http://localhost:9000/api/votes/${this.voteSession.id}/vote`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem('token')}`
          },
          body: JSON.stringify({ restaurantOptionId: optionId, round: 2, inFavor: inFavor })
        })
        .then(res => {
          if(!res.ok) throw new Error('Failed to vote');
          return this.refreshSession();
        })
        .catch(err => console.warn('Final vote failed or refresh failed:', err));
      },
      async refreshSession() {
        try {
          const res = await fetch(`http://localhost:9000/api/votes/${this.voteSession.id}`, {
            headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
          });
          if (!res.ok) throw new Error("Cannot refresh session");
          const data = await res.json();
          this.voteSession = data;
          if (this.voteSession.round === 2) {
            this.loadFinalists();
          }
        } catch (err) {
          console.warn("Refresh session failed:", err);
        }
      },
      showWinners() {
        // Instead of a single winner endpoint returning one or multiple, we assume it returns a list.
        fetch(`http://localhost:9000/api/votes/${this.voteSession.id}/winner`, {
          headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
        })
        .then(res => res.ok ? res.json() : Promise.reject('No winner found'))
        .then(data => {
          // If data is array and length > 1 => tie scenario
          if (Array.isArray(data)) {
            if (data.length === 1) {
              this.winner = data[0];
            } else if (data.length > 1) {
              this.tieWinners = data;
            }
          } else {
            // single object
            this.winner = data;
          }
        })
        .catch(err => console.error(err));
      },
      pickForMe() {
        // pickForMe just calls the same endpoint again or simulate random pick from tieWinners
        if (this.tieWinners.length > 1) {
          const rand = Math.floor(Math.random() * this.tieWinners.length);
          this.winner = this.tieWinners[rand];
          this.tieWinners = [];
        }
      },
      getCardStyle(index) {
        if (index === 0) {
          return { zIndex: 100, transform: "translateX(0)", opacity: 1 };
        }
        return {
          zIndex: this.remainingOptions.length - index,
          transform: `translateY(${10 * index}px) scale(${1 - index * 0.05})`,
          opacity: 0.8 - index * 0.1,
        };
      },
    }
  }
  </script>
  
  <style scoped>
  body {
    background-color: #fff7ed;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    margin: 0;
    padding: 0;
  }
  
  .vote-session {
    max-width: 900px;
    margin: 2rem auto;
    padding: 1.5rem;
    background-color: #fff;
    border-radius: 12px;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
  }
  
  .vote-session h2 {
    text-align: center;
    font-size: 2rem;
    color: #333;
    margin-bottom: 1.5rem;
  }
  
  .loading {
    font-size: 1rem;
    color: #555;
    text-align: center;
    margin-top: 0.5rem;
  }
  
  .error-message {
    color: #e74c3c;
    text-align: center;
    font-weight: bold;
    margin-bottom: 1.5rem;
  }
  
  .round-info {
    text-align: center;
    font-size: 1rem;
    margin-bottom: 1.5rem;
    color: #555;
  }
  
  .vote-session h3 {
    text-align: center;
    color: #333;
    margin-bottom: 1rem;
    font-size: 1.5rem;
  }
  
  .stack-interface {
    position: relative;
    width: 100%;
    height: 450px;
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
  }
  
  .restaurant-card-wrapper {
    position: absolute;
    width: 320px;
    max-width: 90%;
    top: 0;
    left: 50%;
    transform: translateX(-50%);
    background: white;
    transition: transform 0.3s ease, opacity 0.3s ease;
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
    border-radius: 12px;
    cursor: grab;
  }
  
  .restaurant-card {
    border: 1px solid #ddd;
    border-radius: 12px;
    overflow: hidden;
    text-align: center;
    background-color: #fff;
    transition: background-color 0.3s ease;
  }
  
  .restaurant-card:hover {
    background-color: #f9f9f9;
  }
  
  .restaurant-image {
    width: 100%;
    height: 220px;
    object-fit: cover;
  }
  
  .restaurant-details {
    padding: 1.2rem;
  }
  
  .restaurant-details h2 {
    font-size: 1.25rem;
    color: #333;
    margin-bottom: 0.5rem;
  }
  
  .address {
    font-size: 0.95rem;
    color: #777;
    margin-bottom: 0.5rem;
  }
  
  .open-now {
    color: #27ae60;
    font-weight: bold;
  }
  
  .closed {
    color: #c0392b;
    font-weight: bold;
  }
  
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
    border-radius: 12px;
    background: rgba(0, 0, 0, 0.3);
  }
  
  .check-mark {
    color: #27ae60;
    font-size: 4rem;
  }
  
  .x-mark {
    color: #c0392b;
    font-size: 4rem;
  }
  
  .btn-proceed {
    background: #e0c9a6;
    color: #fff;
    border: none;
    padding: 0.8rem 1.5rem;
    border-radius: 6px;
    cursor: pointer;
    display: block;
    margin: 2.5rem auto 0;
    font-weight: bold;
    font-size: 1rem;
    transition: background 0.3s ease, transform 0.2s ease;
  }
  
  .btn-proceed:hover {
    background: #e7b76b;
    transform: translateY(-2px);
  }
  
  /* Round 2 Styles */
  .finalists-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(220px,1fr));
    gap: 1.5rem;
    margin-top: 1.5rem;
  }
  
  .finalist-card {
    background: #ffffff;
    border: 1px solid #eee;
    border-radius: 12px;
    padding: 1.5rem;
    box-shadow: 0 2px 8px rgba(0,0,0,0.05);
    text-align: center;
    position: relative;
    transition: transform 0.3s ease, box-shadow 0.3s ease;
  }
  
  .finalist-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 4px 16px rgba(0,0,0,0.1);
  }
  
  .finalist-card h4 {
    margin-bottom: 0.75rem;
    color: #333;
  }
  
  .final-actions {
    margin-top: 1rem;
  }
  
  .vote-for-btn, .vote-against-btn {
    background: #e0c9a6;
    color: #fff;
    border: none;
    padding: 0.6rem 1.2rem;
    margin: 0.3rem;
    border-radius: 4px;
    cursor: pointer;
    font-size: 1.3rem;
    font-weight: bold;
    transition: background 0.3s ease, transform 0.2s ease;
  }
  
  .vote-for-btn:hover, .vote-against-btn:hover {
    background: #e7b76b;
    transform: translateY(-2px);
  }
  
  .done-button {
    text-align: center;
    margin-top: 2rem;
  }
  
  .btn-show-winner {
    background: #737f9a;
    color: #fff;
    border: none;
    padding: 0.8rem 1.6rem;
    border-radius: 6px;
    font-size: 1rem;
    font-weight: bold;
    cursor: pointer;
    transition: background 0.3s ease, transform 0.2s ease;
  }
  
  .btn-show-winner:hover {
    background: #5a6e9c;
    transform: translateY(-2px);
  }
  
  /* Tie scenario */
  .btn-pick-for-me {
    background: #e0c9a6;
    color: #fff;
    border: none;
    padding: 0.8rem 1.6rem;
    border-radius: 6px;
    font-weight: bold;
    margin-top: 1.5rem;
    cursor: pointer;
    transition: background 0.3s ease, transform 0.2s ease;
  }
  
  .btn-pick-for-me:hover {
    background: #e7b76b;
    transform: translateY(-2px);
  }
  
  .tie-info {
    text-align: center;
    font-size: 1rem;
    color: #555;
    margin: 1rem 0;
  }
  
  .winner-name {
    text-align: center;
    font-size: 1.75rem;
    margin-top: 1.5rem;
    font-weight: bold;
    color: #333;
  }
  
  /* Responsive Adjustments */
  @media (max-width: 768px) {
    .stack-interface {
      height: 350px;
    }
  
    .restaurant-card-wrapper {
      width: 280px;
    }
  
    .restaurant-image {
      height: 180px;
    }
  
    .finalists-grid {
      grid-template-columns: repeat(auto-fit, minmax(180px,1fr));
      gap: 1rem;
    }
  
    .finalist-card {
      padding: 1rem;
    }
  
    .btn-proceed, .btn-show-winner, .btn-pick-for-me {
      width: 80%;
      padding: 0.6rem 1rem;
    }
  }
  </style>
  