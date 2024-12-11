package com.techelevator.controller;

import com.techelevator.model.Restaurant;
import com.techelevator.security.jwt.TokenProvider;
import com.techelevator.service.FavoriteService;
import com.techelevator.dao.FavoriteDao;
import com.techelevator.dao.UserDao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "http://localhost:5173") // Allow requests from your local frontend dev server
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final TokenProvider tokenProvider;
    private final UserDao userDao;
    private final FavoriteDao favoriteDao;

    public FavoriteController(FavoriteService favoriteService, TokenProvider tokenProvider, UserDao userDao, FavoriteDao favoriteDao) {
        this.favoriteService = favoriteService;
        this.tokenProvider = tokenProvider;
        this.userDao = userDao;
        this.favoriteDao = favoriteDao;
    }

    @PostMapping
    public ResponseEntity<Void> addFavorite(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> payload) {

        String placeId = payload.get("place_id");
        if (placeId == null || placeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Place ID is required.");
        }

        // Extract the username from the JWT token
        String token = authHeader.replace("Bearer ", "");
        String username = tokenProvider.getAuthentication(token).getName();

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid token: Username cannot be determined.");
        }

        // Store only the endpoint without the key
        String restaurantEndpoint = "https://maps.googleapis.com/maps/api/place/details/json?place_id=" + placeId;
        favoriteService.addFavorite(username, restaurantEndpoint);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getFavorites(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = tokenProvider.getAuthentication(token).getName();
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid token: Username cannot be determined.");
        }

        // Return the actual restaurant objects
        List<Restaurant> favoriteRestaurants = favoriteService.getFavoriteRestaurants(username);
        return ResponseEntity.ok(favoriteRestaurants);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFavorite(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String endpoint) {
        String token = authHeader.replace("Bearer ", "");
        String username = tokenProvider.getAuthentication(token).getName();

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid token: Username cannot be determined.");
        }

        favoriteService.removeFavorite(username, endpoint);
        return ResponseEntity.noContent().build();
    }
}
