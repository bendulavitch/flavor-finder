# Flavor Finder

Flavor Finder is a web application designed to help users discover nearby restaurants and make group decisions easily. The app allows users to register, log in, search for nearby restaurants, and use a swipe feature to mark favorites. These favorites can be saved and later used for group decision-making. This project was co-developed with Danny Ambrose and Karyn Miles.

### Technologies Used:
- **Backend**: Java (Spring Boot)
- **Frontend**: Vue.js
- **Database**: PostgreSQL
- **External API**: Google Places API
- **Authentication**: JWT (JSON Web Token)

## Features:
- **Account Registration & Login**: Users can create accounts, log in, and manage their sessions.
- **Restaurant Search**: Users can search for nearby restaurants using Google Places API.
- **Swipe Feature**: Users can swipe through restaurants, marking them as "Yes" or "No" to add them to their favorites.
- **Favorites Page**: A page where users can view and manage their favorite restaurants.
- **Group Voting**: Users can participate in a voting session to help groups decide on a restaurant using a shared passcode.

### Prerequisites:
To run the project locally, make sure you have the following installed:
- **Java** (version 11 or above)
- **Node.js** (version 14 or above)
- **PostgreSQL** (for the database)

### Installation:

1. Clone the repository:
   ```bash
   git clone https://github.com/bendulavitch/flavor-finder.git
   cd flavor-finder/java

2. Set up your database:
   - Create a PostgreSQL database for your project.
   - Update the `application.properties` file with your database credentials.

3. Start the backend:
    ```bash
    mvn spring-boot:run
    ```

### Frontend Setup
1. Navigate to the frontend directory:
    ```bash
    cd flavor-finder/vue
    ```

2. Install dependencies:
    ```bash
    npm install
    ```

3. Run the frontend:
    ```bash
    npm run dev
    ```

### Running the Application
1. Open the frontend in your browser.
2. Register a new account or log in with an existing one.
3. Once logged in, navigate to the "Home" page to enter a city name or zip code and swipe through restaurants.
