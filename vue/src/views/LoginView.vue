<template>
  <!-- Hero Section -->
  <section id="hero">
    <video autoplay muted id="hero-video" @ended="fadeOutVideo">
      <source src="/hero.mp4" type="video/mp4" />
      Your browser does not support the video tag.
    </video>
    <div class="hero-content" v-bind:class="{'visible': textVisible}" data-aos="fade-up">
      <h1>Welcome to <span class="highlight">Flavor Finder</span></h1>
      <p class="hero-subheading">Discover your next favorite restaurant effortlessly.</p>
      <router-link v-bind:to="{ name: 'register' }" class="btn-primary">Sign Up</router-link>
    </div>
  </section>

  <!-- Login Section -->
  <section id="login-section">
    <div class="login-container">
      <form v-on:submit.prevent="login">
        <h2>Sign In to Continue</h2>
        <div class="alert alert-danger" v-if="invalidCredentials">
          Invalid username and password!
        </div>
        <div class="alert alert-success" v-if="this.$route.query.registration">
          Thank you for registering! Please sign in.
        </div>
        <div class="form-input-group">
          <label for="username">Username</label>
          <input
            type="text"
            id="username"
            v-model="user.username"
            placeholder="Enter your username"
            required
            autofocus
          />
        </div>
        <div class="form-input-group">
          <label for="password">Password</label>
          <input
            type="password"
            id="password"
            v-model="user.password"
            placeholder="Enter your password"
            required
          />
        </div>
        <button type="submit" class="btn-primary">Sign In</button>
        <p class="sign-up-prompt">
          Don't have an account?
          <router-link v-bind:to="{ name: 'register' }">Sign up here.</router-link>
        </p>
      </form>
    </div>
  </section>
</template>

<script>
import authService from "../services/AuthService";

export default {
  data() {
    return {
      user: {
        username: "",
        password: ""
      },
      invalidCredentials: false,
      textVisible: false // Track text visibility
    };
  },

  mounted() {
    // Scroll to the top of the page on component load
    window.scrollTo(0, 0);
  },
  
  methods: {
    login() {
      authService
        .login(this.user)
        .then(response => {
          if (response.status == 200) {
            this.$store.commit("SET_AUTH_TOKEN", response.data.token);
            this.$store.commit("SET_USER", response.data.user);
            this.$router.push("/");
          }
        })
        .catch(error => {
          const response = error.response;
          if (response.status === 401) {
            this.invalidCredentials = true;
          }
        });
    },
    
    fadeOutVideo() {
      const video = document.getElementById('hero-video');
      video.style.transition = "opacity 2s ease-out";
      video.style.opacity = 0;
      setTimeout(() => {
        video.style.display = "none";
        this.fadeInText(); // Trigger text fade-in after video ends
      }, 2000); // Hide the video after fading out
    },
    
    fadeInText() {
      this.textVisible = true; // Show the text after video finishes
    }
  }
};
</script>

<style scoped>
/* Fade-up animation */
@keyframes fadeUp {
  0% {
    opacity: 0;
    transform: translateY(20px); /* Start slightly below */
  }
  100% {
    opacity: 1;
    transform: translateY(0); /* Move to normal position */
  }
}

/* Animated Floating Background */
@keyframes floatBackground {
  0% {
    background-color: #fff7ed;
  }
  50% {
    background-color: #e0c9a6;
  }
  100% {
    background-color: #fff7ed;
  }
}

/* Hero Section */
#hero {
  position: relative;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  overflow: hidden;
  color: #e0c9a6;
  background: #fff7ed; /* Initial background color */
  animation: floatBackground 8s ease-in-out infinite; /* Add floating animation */
}

#hero-video {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover; /* Ensures the video covers the section without distortion */
  z-index: 0;
}

#hero .hero-content {
  z-index: 1;
  text-align: center;
  padding: 20px;
  max-width: 600px;
  opacity: 0; /* Initially hide the text */
  transition: opacity 2s ease-in-out; /* Smooth transition for fading in */
}

#hero .hero-content.visible {
  opacity: 1; /* Make text visible after video ends */
  animation: fadeUp 1.5s ease-in-out;
}

#hero .hero-content {
  background: none; /* No background */
  border-radius: 0; /* No border radius needed */
}

#hero h1 {
  font-family: 'Poppins', sans-serif;
  font-size: 3.5rem; /* Larger heading */
  font-weight: 600;
  margin-bottom: 15px;
  color: #fff;
  
}

#hero h1 .highlight {
  color: #5c5c5c; /* Gold color to emphasize 'Flavor Finder' */
}

.hero-subheading {
  font-family: 'Poppins', sans-serif;
  font-size: 1.5rem; /* Slightly smaller than the heading */
  margin-bottom: 20px;
  color: #f5f5f5;
  text-shadow: 1px 1px 5px rgba(0, 0, 0, 0.6);
}

#hero p {
  font-size: 1.5rem;
  margin-bottom: 20px;
}

.btn-primary {
  padding: 12px 24px;
  background-color: #5c5c5c; /* Gold button color to match theme */
  color: #fff; /* Black text for contrast */
  font-weight: bold;
  text-decoration: none;
  border-radius: 25px; /* Make it more button-like */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.btn-primary:hover {
  background-color: #636772; /* Slightly lighter gold on hover */
  transform: translateY(-2px); /* Subtle hover effect */
}

/* Login Section */
#login-section {
  padding: 40px 20px;
  background: #e0c9a6;
}

.login-container {
  max-width: 400px;
  margin: 0 auto;
  padding: 30px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
}

.login-container h2 {
  font-size: 1.8rem;
  margin-bottom: 20px;
  color: #333;
  text-align: center;
}

.alert {
  padding: 10px;
  margin-bottom: 15px;
  border-radius: 5px;
  text-align: center;
}

.alert-danger {
  background: #f8d7da;
  color: #721c24;
}

.alert-success {
  background: #d4edda;
  color: #155724;
}

.form-input-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
  color: #333;
}

input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 1rem;
}

input:focus {
  outline: none;
  border-color: #007bff;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #007bff;
  color: #fff;
  font-weight: bold;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #0056b3;
}

.sign-up-prompt {
  margin-top: 15px;
  text-align: center;
  color: #666;
}

.sign-up-prompt a {
  color: #007bff;
  font-weight: bold;
  text-decoration: none;
}

.sign-up-prompt a:hover {
  text-decoration: underline;
}
</style>