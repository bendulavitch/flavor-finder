package com.techelevator.controller;

import com.techelevator.dao.FavoriteDao;
import com.techelevator.dao.JdbcFavoriteDao;
import com.techelevator.dao.JdbcUserDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.User;
import com.techelevator.security.jwt.TokenProvider;
import com.techelevator.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final TokenProvider tokenProvider;
    private final UserDao userDao;
    private final FavoriteDao favoriteDao;
    private final JdbcFavoriteDao jdbcFavoriteDao;

    public FavoriteController(FavoriteService favoriteService, TokenProvider tokenProvider, UserDao userDao, FavoriteDao favoriteDao, JdbcFavoriteDao jdbcFavoriteDao) {
        this.favoriteService = favoriteService;
        this.tokenProvider = tokenProvider;
        this.userDao = userDao;
        this.favoriteDao = favoriteDao;
        this.jdbcFavoriteDao = jdbcFavoriteDao;

    }

    @PostMapping
    public ResponseEntity<Void> addFavorite(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> payload) {
        String restaurantEndpoint = payload.get("endpoint");
        

        if (restaurantEndpoint == null || restaurantEndpoint.trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant endpoint is required.");
        }

        // Extract username from the JWT token
        String token = authHeader.replace("Bearer ", ""); // Remove "Bearer " prefix
        String username = tokenProvider.getAuthentication(token).getName();

        // Call the service method with the username and restaurant endpoint
        favoriteService.addFavorite(username, restaurantEndpoint);

        return ResponseEntity.ok().build();
    }



    @GetMapping
    public ResponseEntity<List<String>> getFavorites(@RequestHeader("Authorization") String authHeader) {
        // Extract username from the JWT token
        String username = tokenProvider.getAuthentication(authHeader.replace("Bearer ", "")).getName();

        // Fetch user by username
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found for username: " + username);
        }
        // Use the userId from the User object
        int userId = user.getId();
        // Retrieve favorite endpoints for the user
        List<String> favorites = favoriteService.getFavoriteEndpoints(userId);
        return ResponseEntity.ok(favorites);
    }


    @DeleteMapping
    public ResponseEntity<Void> removeFavorite(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String endpoint) {
        // Extract username from the JWT token
        String username = tokenProvider.getAuthentication(authHeader.replace("Bearer ", "")).getName();
        // Fetch user by username
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found for username: " + username);
        }
        // Use the userId from the User object
        int userId = user.getId();
        // Remove the restaurant endpoint from the user's favorites
        favoriteService.removeFavorite(userId, endpoint);
        return ResponseEntity.noContent().build();
    }

}
