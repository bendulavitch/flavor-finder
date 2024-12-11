package com.techelevator.service;

import com.techelevator.dao.FavoriteDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.User;
import com.techelevator.security.jwt.TokenProvider;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final FavoriteDao favoriteDao;
    private final UserDao userDao;
    private final TokenProvider tokenProvider;


    public FavoriteService(FavoriteDao favoriteDao, UserDao userDao, TokenProvider tokenProvider ) {
        this.favoriteDao = favoriteDao;
        this.userDao = userDao;
        this.tokenProvider = tokenProvider;
    }

    public void addFavorite(String username, String restaurantEndpoint) {
        // Fetch the user ID using the username
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }

        int userId = user.getId();

        // Save the endpoint to the favorites table
        favoriteDao.addFavorite(userId, restaurantEndpoint);
    }





    public List<String> getFavoriteEndpoints(String username) {
        // Fetch the user by username
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }
        int userId = user.getId();
        // Retrieve favorite endpoints for the user
        List<String> endpoints = favoriteDao.getFavoriteEndpoints(userId);

        // Append the API key to each endpoint
        String apiKey = "AIzaSyDW3C7apil1_X7QUme8pTwdTgX8lMiuMys";
        return endpoints.stream()
                .map(endpoint -> endpoint + "&key=" + apiKey) // Append API key
                .collect(Collectors.toList());
    }


    public void removeFavorite(String username, String endpoint) {
        // Fetch the user by username
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }

        int userId = user.getId(); // Ensure the numeric ID
        favoriteDao.removeFavorite(userId, endpoint); // Remove the restaurant endpoint
    }

}
