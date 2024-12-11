<template>
  <body>
  <br>
  <br>
  <br>
  <div id="register" class="register-container text-center">
    <form v-on:submit.prevent="register" class="register-form">
      <h1>Create Account</h1>

      <div v-if="registrationErrors" role="alert" class="error-message">
        {{ registrationErrorMsg }}
      </div>
      

      <div class="form-input-group">
        <label for="username">Username</label>
        <input 
          type="text" 
          id="username" 
          v-model="user.username" 
          required 
          autofocus 
          placeholder="Enter your username"
        />
      </div>

      <div class="form-input-group">
        <label for="password">Password</label>
        <input 
          type="password" 
          id="password" 
          v-model="user.password" 
          required 
          placeholder="Enter your password"
        />
      </div>

      <div class="form-input-group">
        <label for="confirmPassword">Confirm Password</label>
        <input 
          type="password" 
          id="confirmPassword" 
          v-model="user.confirmPassword" 
          required 
          placeholder="Re-enter your password"
        />
      </div>

      <button type="submit" class="btn-submit">Create Account</button>

      <p class="login-link">
        <router-link v-bind:to="{ name: 'login' }">
          Already have an account? Log in.
        </router-link>
      </p>
    </form>
  </div>
  <br>
  <br>
  </body>
</template>

<script>
import authService from '../services/AuthService';

export default {
  data() {
    return {
      user: {
        username: '',
        password: '',
        confirmPassword: '',
        role: 'user',
      },
      registrationErrors: false,
      registrationErrorMsg: 'There were problems registering this user.',
    };
  },
  methods: {
    register() {
      if (this.user.password != this.user.confirmPassword) {
        this.registrationErrors = true;
        this.registrationErrorMsg = 'Password & Confirm Password do not match.';
      } else {
        authService
          .register(this.user)
          .then((response) => {
            if (response.status == 201) {
              this.$router.push({
                path: '/login',
                query: { registration: 'success' },
              });
            }
          })
          .catch((error) => {
            const response = error.response;
            this.registrationErrors = true;
            if (response.status === 400) {
              this.registrationErrorMsg = 'Bad Request: Validation Errors';
            }
          });
      }
    },
    clearErrors() {
      this.registrationErrors = false;
      this.registrationErrorMsg = 'There were problems registering this user.';
    },
  },
};
</script>

<style scoped>
body {
  background-color: #fff7ed;
}

.register-container {
  max-width: 400px;
  margin: 0 auto;
  padding: 2rem;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  align-items: center; /* Center the form elements horizontally */
}

.register-form {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center; /* Center the form elements horizontally */
}

.register-form h1 {
  font-size: 2rem;
  margin-bottom: 1.5rem;
  color: #333;
  text-align: center; /* Center the text */
}

.form-input-group {
  margin-bottom: 1.5rem;
  display: flex;
  flex-direction: column;
  width: 100%; /* Ensure the input groups take full width */
  align-items: center; /* Center the input groups horizontally */
}

.form-input-group label,
.form-input-group input {
  width: 100%; /* Ensure the label and input take full width */
}

label {
  margin-bottom: 0.5rem;
  font-weight: bold;
  color: #555;
}

input {
  padding: 0.8rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  outline: none;
  transition: border-color 0.2s;
}

input:focus {
  border-color: #007bff;
}

.btn-submit {
  padding: 0.8rem 1.5rem;
  font-size: 1rem;
  color: #fff;
  background-color: #5c5c5c;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
  width: 100%; /* Ensure the button takes full width */
  text-align: center; /* Center the button text */
}

.btn-submit:hover {
  background-color: #636772;
}

.error-message {
  color: #d9534f;
  margin-bottom: 1rem;
  text-align: center; /* Center the error message text */
}

.login-link {
  margin-top: 1rem;
  font-size: 0.9rem;
  text-align: center; /* Center the login link text */
}

.login-link a {
  color: #007bff;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>
