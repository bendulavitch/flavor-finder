<template>
    <br>
    <br>
    <div class="container">
      <h1>Contact Us</h1>
      <p>
        We'd love to hear from you! If you have any questions, feedback, or suggestions, please feel free to reach out to us using the form below.
      </p>
      <form action="https://formspree.io/f/xeoqwgyn" method="POST">
        <div class="form-input-group">
          <label for="name">Name:</label>
          <input type="text" id="name" name="name" placeholder="Your Name" required />
        </div>
        <div class="form-input-group">
          <label for="email">Email:</label>
          <input
            type="email"
            id="email"
            name="email"
            placeholder="Your Email Address"
            required
          />
        </div>
        <div class="form-input-group">
          <label for="subject">Subject:</label>
          <input
            type="text"
            id="subject"
            name="subject"
            placeholder="Subject"
            required
          />
        </div>
        <div class="form-input-group">
          <label for="message">Message:</label>
          <textarea
            id="message"
            name="message"
            rows="5"
            placeholder="Type your message here..."
            required
          ></textarea>
        </div>
        <div class="form-input-group">
          <button type="submit" class="btn-submit">Submit</button>
        </div>
      </form>
    </div>
  </template>
  
  <style scoped>
  .container {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    font-family: Arial, sans-serif;
    margin-top: 40px;
    margin-bottom: 40px;

  }
  
  h1 {
    text-align: center;
    color: #333;
  }
  
  p {
    text-align: center;
    color: #555;
    margin-bottom: 20px;
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
  
  input,
  textarea {
    width: 100%;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 16px;
  }
  
  input:focus,
  textarea:focus {
    border-color: #007bff;
    outline: none;
  }
  
  .btn-submit {
    display: block;
    width: 100%;
    padding: 10px;
    background-color: #007bff;
    color: #fff;
    font-size: 16px;
    font-weight: bold;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    text-align: center;
  }
  
  .btn-submit:hover {
    background-color: #0056b3;
  }
  </style>
  