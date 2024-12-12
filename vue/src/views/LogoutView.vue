<template>
  <div>
    <!-- Animation Overlay -->
    <div v-if="showAnimation" class="logout-overlay">
      <video ref="logoutVideo" class="logout-animation" autoplay muted>
        <source src="/load.mp4" type="video/mp4" />
        Your browser does not support the video tag.
      </video>
    </div>
    
    <!-- Fallback Text (optional for testing) -->
    <h1 v-else>Redirecting to Login...</h1>
  </div>
</template>

<script>
export default {
  data() {
    return {
      showAnimation: true // Show animation initially
     
    };
  },
  mounted() {
    // Add an event listener to handle the end of the video
    const videoElement = this.$refs.logoutVideo;
    if (videoElement) {
      videoElement.onended = () => {
        this.redirectToLogin();
      };
    }
  },
  methods: {
    redirectToLogin() {
      // Commit logout mutation
      // Redirect to login page
      this.$router.push("/favorites");
    },
  },
};
</script>

<style scoped>
.logout-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: #E3C9A4; /* Optional background */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000; /* Make sure it’s above all other content */
}

.logout-animation {
  width: 100%; /* Adjust size as needed */
  height: auto;
  max-width: 600px; /* Optional max size */
  border-radius: 10px;
}

</style>
