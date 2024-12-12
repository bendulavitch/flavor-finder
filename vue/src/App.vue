
<template>
  <header>
    <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@400;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
<nav>
  <div class="logo">
    <img class="headlogo" src="../Images/headerlogo.png" alt="Flavor Finder Logo" />
    <a href="/">&nbsp;  Flavor Finder</a>
  </div>
  <ul class="nav-links">
    <li>
      <router-link v-bind:to="{ name: 'home' }">Home</router-link>
    </li>
    <li>
      <router-link v-bind:to="{ name: 'favorites' }">Favorites</router-link>
    </li>
    <li v-if="isAuthenticated">
          <span class="user-indicator">Welcome, {{ username }}</span>
        </li>
    <li v-else>
      <router-link v-bind:to="{ name: 'login' }">Login</router-link>
    </li>


    <li v-if="isAuthenticated">
      <button @click="logout" class="nav-link btn-logout">Log Out</button>
    </li>
    <li v-else>
      <router-link v-bind:to="{ name: 'register' }">Register</router-link>
    </li>
  </ul>
  
</nav>

    </header>
    <router-view />

    <footer id="footer">
      <div class="footer-container">
        <div class="footer-section">
          <h3>Flavor Finder</h3>
          <p>Discover your next favorite restaurant effortlessly.</p>
          <img class="footlogo" src="../Images/FFOCLOGO.png" alt="Flavor Finder Logo" />
        </div>
        <div class="footer-section">
          <h3>Quick Links</h3>
          <ul>
            <li><router-link v-bind:to="{ name: 'home' }">Home</router-link></li>
            <li><router-link v-bind:to="{ name: 'about' }">About</router-link></li>
            <li><router-link v-bind:to="{ name: 'contact' }">Contact</router-link></li>
            <li><router-link v-bind:to="{ name: 'terms' }">Terms & Conditions</router-link></li>
          </ul>
        </div>
        <div class="footer-section">
          <h3>Contact Us</h3>
          <p>Email: support@flavorfinder.com</p>
          <p>Phone: (555) 123-4567</p>
          <div class="social-icons">
            <a href="#"><i class="fab fa-facebook"></i></a>
            <a href="#"><i class="fab fa-twitter"></i></a>
            <a href="#"><i class="fab fa-instagram"></i></a>
          </div>
        </div>
      </div>
      <div class="footer-bottom">
        <p>&copy; 2024 Flavor Finder. All rights reserved.</p>
      </div>
    </footer>
  
</template>

<script>
export default {
  computed: {
    isAuthenticated() {
      return this.$store.state.token !== ''; // Check if token exists
    },
    username() {
      return this.$store.state.user?.username || 'Guest'; // Display username or default to 'Guest'
    },
  },
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
    logout() {
      this.$store.commit("LOGOUT");
      this.$router.push("/login");
    },
  },
};
</script>


<!-- Nav Bar CSS -->
<style scoped>

.headlogo {
  width: 40px; /* Set the width */
  height: 32px; /* Set the height */
}

.footlogo {
  width: 100px; /* Set the width */
  height: 80px; /* Set the height */
}

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
  justify-content: space-between; 
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
  align-items: center; /* Vertically align items */
  list-style: none; /* Remove default bullet points */
  gap: 20px; /* Space between links */
  margin: 0; /* Remove default margin */
  padding: 0; /* Remove default padding */
}

/* Nav Links Styling */
nav ul li a
 {
  color: #5c5c5c; /* Muted dark gray text */
  text-decoration: none; /* Remove underline */
  font-size: 1.2em; /* Match font size */
  font-family: "Lato", sans-serif; /* Consistent font */
  padding: 10px 15px; /* Add consistent padding */
  border-radius: 5px; /* Rounded corners for both links and button */
  background: none; /* Remove default button background */
  border: none; /* Remove default button border */
  cursor: pointer; /* Pointer cursor for both links and button */
  transition: background-color 0.3s ease, color 0.3s ease; /* Smooth transitions */
  display: flex; /* Ensure uniform flex behavior */
  align-items: center; /* Vertical alignment within each element */
}

.user-indicator {
  color: #c88f67; /* Muted orange color */
  font-weight: bold;
  font-size: 1.1em;
  margin-left: 10px;
}


/* Hover Effect */
nav ul li a:hover{
  background-color: #e0c9a6; 
  color: #ffffff; 
}

/* Active Link Styling */
nav ul li a.router-link-active {
  background-color: #e0c9a6; 
  color: #ffffff; /* White text for active link */
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2); /* Highlight active with shadow */
}

/* Logout Button Styling */
.btn-logout {
  color: #5c5c5c; /* Muted dark gray text */
  text-decoration: none; /* Remove underline */
  font-size: 1.2em; /* Match font size of nav links */
  font-family: "Lato", sans-serif; /* Same font as nav links */
  padding: 10px 15px; /* Same padding for alignment */
  border-radius: 5px; /* Rounded corners for consistency */
  background: none; /* Remove default button background */
  border: none; /* Remove default button border */
  cursor: pointer; /* Pointer cursor for buttons */
  transition: background-color 0.3s ease, color 0.3s ease; /* Smooth transitions */
}

/* Hover Effect for Logout Button */
.btn-logout:hover {
  background-color: #e0c9a6; /* Same hover background as nav links */
  color: #ffffff; /* White text on hover */
}


/* Footer Styles */
#footer {
  background-color: #fff7ed; /* Light cream background */
  color: #5c5c5c; /* Muted dark gray text */
  padding: 40px 20px;
  font-family: 'Poppins', sans-serif;
  border-top: 1px solid #e0c9a6; /* Subtle top border */
}

.footer-container {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap; /* Make footer responsive */
  gap: 20px;
}

.footer-section {
  flex: 1;
  min-width: 200px; /* Minimum width for sections */
}

.footer-section h3 {
  font-size: 1.5rem;
  margin-bottom: 15px;
  color: #c88f67; /* Muted orange for section titles */
}

.footer-section p,
.footer-section ul,
.footer-section ul li {
  font-size: 1rem;
  margin: 5px 0;
}

.footer-section ul {
  list-style: none;
  padding: 0;
}

.footer-section ul li {
  margin-bottom: 8px;
}

.footer-section ul li a {
  text-decoration: none;
  color: #5c5c5c; /* Muted gray for links */
  transition: color 0.3s ease;
}

.footer-section ul li a:hover {
  color: #c88f67; /* Orange hover effect */
}

.social-icons a {
  margin-right: 10px;
  color: #5c5c5c;
  font-size: 1.2rem;
  transition: color 0.3s ease;
}

.social-icons a:hover {
  color: #c88f67;
}

.footer-bottom {
  text-align: center;
  margin-top: 20px;
  border-top: 1px solid #e0c9a6;
  padding-top: 10px;
  font-size: 0.9rem;
  color: #666;
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
  .nav-links {
    flex-direction: column;
    align-items: center;
    gap: 10px; /* Adjust spacing */
  }
}



</style>
