<template>
    <div class="about-container">
      <div class="content">
        <h1>About Flavor Finder</h1>
        <p>
          Welcome to <strong>Flavor Finder</strong>! Our web application is designed to help you discover your next favorite restaurant effortlessly. 
          With an intuitive interface, you can search for restaurants tailored to your preferences. Whether you're in need of a 
          kid-friendly spot, a place that's open now, or a specific cuisine, Flavor Finder is here to make your dining experience seamless.
        </p>
        <p>
          At Flavor Finder, our mission is simple: to connect you with the perfect dining experience for any occasion. We believe 
          that great food is for everyone, and we're dedicated to helping you find it. From casual lunches to special celebrations, 
          let us guide you to your ideal restaurant.
        </p>
        <p>
          Have questions, feedback, or suggestions? We'd love to hear from you! Feel free to 
          <router-link class="contact-link" to="/contact">contact us</router-link>. Your input helps us improve and better serve food lovers like you.
        </p>
      </div>
    </div>
  </template>
  
  <style scoped>
  .about-container {
    padding: 80px 20px; /* Adds space for the inherited fixed header */
    margin-top: 20px;
    margin-bottom: 40px; /* Adds space for the footer */
    background-color: #fff7ed; /* Light cream background */
    font-family: 'Lato', sans-serif;
  }
  
  .content {
    max-width: 800px; /* Limits the width for better readability */
    margin: 0 auto; /* Centers the content */
    padding: 20px;
    background: #ffffff; /* White background for contrast */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Subtle shadow for depth */
    border-radius: 8px; /* Rounded corners for elegance */
  }
  
  h1 {
    text-align: center;
    color: #c88f67; /* Muted orange color for title */
    font-family: 'Playfair Display', serif;
    margin-bottom: 20px;
  }
  
  p {
    line-height: 1.8; /* Improves readability */
    color: #5c5c5c; /* Muted dark gray text */
    margin-bottom: 20px;
    font-size: 1.1em;
  }
  
  p strong {
    color: #c88f67; /* Highlighted color for emphasis */
  }
  
  .contact-link {
    color: #c88f67; /* Muted orange color */
    text-decoration: none; /* Removes underline */
    font-weight: bold;
    transition: color 0.3s ease;
  }
  
  .contact-link:hover {
    color: #e09a73; /* Darker orange on hover */
  }
  </style>
  