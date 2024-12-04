
<template>
  <header>
    <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@400;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
<nav>
  <div class="logo">
    <a href="index.html">Flavor Finder</a>
  </div>
  <ul class="nav-links">
    <li>
      <router-link v-bind:to="{ name: 'home' }">Home</router-link>
    </li>
    <li>
      <router-link v-bind:to="{ name: 'home' }">Messaging</router-link>
    </li>
    <li>
      <router-link v-bind:to="{ name: 'login' }">Login</router-link>
    </li>
    <li>
      <router-link v-bind:to="{ name: 'logout' }">Log Out</router-link>
    </li>
  </ul>
</nav>

    </header>
    <router-view />

</template>

<script>
export default {
  data() {
    return {
      theme: localStorage.getItem('theme') || 'light',
    };
  },
  methods: {
    toggleTheme() {
      this.theme = this.theme === 'light' ? 'dark' : 'light';
      localStorage.setItem('theme', this.theme);
    },
  },
};
</script>
<!-- Nav Bar CSS -->
<style>
/* General Body Styling */
body {
  background-color: #fff7ed; /* Light cream background */
  color: #5c5c5c; /* Muted dark gray text */
  font-family: "Lato", sans-serif; /* Default font for body text */
  margin: 0; /* Remove default body margin */
  padding: 0; /* Remove default body padding */
}

/* Nav Bar Styles */
nav {
  display: flex;
  justify-content: space-between; /* Space between logo and links */
  align-items: center;
  background-color: #fff7ed; /* Creamy light background */
  padding: 0 20px; /* Padding on left and right */
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000; /* Ensure nav stays on top */
  width: 100%; /* Stretch across full width */
  border-bottom: 1px solid #e0c9a6; /* Subtle divider */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* Subtle shadow */
  font-family: "Playfair Display", serif; /* Elegant serif font for logo */
  box-sizing: border-box; /* Include padding in width calculation */
}

/* Logo Styling */
nav .logo a {
  color: #5c5c5c; /* Dark muted gray text */
  text-decoration: none; /* Remove underline */
  font-size: 1.8em; /* Larger font size for logo */
  font-weight: bold; /* Emphasize logo */
  letter-spacing: 0.05em; /* Slight spacing for elegance */
  padding: 10px 0; /* Add vertical padding for alignment */
}

/* Nav Links Container */
nav ul {
  display: flex; /* Flexbox layout for horizontal alignment */
  list-style: none; /* Remove default bullet points */
  gap: 20px; /* Space between links */
  margin: 0; /* Remove default margin */
  padding: 0; /* Remove default padding */
}

/* Nav Links Styling */
nav ul li a {
  color: #5c5c5c; 
  text-decoration: none; 
  font-size: 1.2em; 
  font-family: "Lato", sans-serif; 
  padding: 10px 15px; 
  border-radius: 5px; 
  transition: background-color 0.3s, color 0.3s; 
}

/* Hover Effect */
nav ul li a:hover {
  background-color: #e0c9a6; 
  color: #ffffff; 
}

/* Active Link Styling */
nav ul li a.router-link-active {
  background-color: #e0c9a6; 
  color: #ffffff; /* White text for active link */
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2); /* Highlight active with shadow */
}

/* Mobile Responsiveness */
@media (max-width: 768px) {
  nav ul {
    flex-direction: column; /* Stack links vertically on smaller screens */
    gap: 15px; /* Adjust spacing between links */
    align-items: center; /* Center links in mobile layout */
  }

  nav .logo {
    text-align: center; /* Center-align logo on smaller screens */
    width: 100%; /* Full width for logo */
    margin-bottom: 10px; /* Add space below logo */
  }
}



</style>
