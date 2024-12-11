package com.techelevator.service;

import com.techelevator.dao.FavoriteDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.Restaurant;
import com.techelevator.model.User;
import com.techelevator.security.jwt.TokenProvider;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteService {
    private final FavoriteDao favoriteDao;
    private final UserDao userDao;
    private final TokenProvider tokenProvider;
    private final RestaurantApiService restaurantApiService;

    public FavoriteService(FavoriteDao favoriteDao, UserDao userDao, TokenProvider tokenProvider, RestaurantApiService restaurantApiService) {
        this.favoriteDao = favoriteDao;
        this.userDao = userDao;
        this.tokenProvider = tokenProvider;
        this.restaurantApiService = restaurantApiService;
    }

    public void addFavorite(String username, String restaurantEndpoint) {
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }
        int userId = user.getId();
        favoriteDao.addFavorite(userId, restaurantEndpoint);
    }

    public void removeFavorite(String username, String endpoint) {
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }

        int userId = user.getId();
        favoriteDao.removeFavorite(userId, endpoint);
    }

    /**
     * Retrieve the user's favorite endpoints, extract place_id, fetch details from Google,
     * and return a list of Restaurant objects.
     */
    public List<Restaurant> getFavoriteRestaurants(String username) {
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }
        int userId = user.getId();
        List<String> endpoints = favoriteDao.getFavoriteEndpoints(userId);

        List<Restaurant> favorites = new ArrayList<>();
        for (String endpoint : endpoints) {
            String placeId = extractPlaceId(endpoint);
            if (placeId != null && !placeId.isEmpty()) {
                Restaurant restaurant = restaurantApiService.fetchRestaurantDetailsByPlaceId(placeId);
                if (restaurant != null) {
                    favorites.add(restaurant);
                }
            }
        }

        return favorites;
    }

    private String extractPlaceId(String endpoint) {
        // endpoint looks like: "https://maps.googleapis.com/maps/api/place/details/json?place_id=ChIJ..."
        // Extract the value after 'place_id=' until '&' or end of string
        int idx = endpoint.indexOf("place_id=");
        if (idx == -1) return null;
        String substring = endpoint.substring(idx + "place_id=".length());
        int ampersandIdx = substring.indexOf('&');
        if (ampersandIdx != -1) {
            substring = substring.substring(0, ampersandIdx);
        }
        return substring;
    }
}
