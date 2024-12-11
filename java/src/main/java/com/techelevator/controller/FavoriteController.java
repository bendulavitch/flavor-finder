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

        // Validate the payload
        String placeId = payload.get("place_id");
        if (placeId == null || placeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Place ID is required.");
        }

        // Extract the username from the JWT token
        String token = authHeader.replace("Bearer ", ""); // Remove "Bearer " prefix
        String username = tokenProvider.getAuthentication(token).getName();

        // Ensure username is valid
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid token: Username cannot be determined.");
        }

        // Convert place_id to endpoint URL (if needed)
        String restaurantEndpoint = "https://maps.googleapis.com/maps/api/place/details/json?place_id=" + placeId;

        // Call the service method to add the favorite
        favoriteService.addFavorite(username, restaurantEndpoint);

        // Return HTTP 200 OK
        return ResponseEntity.ok().build();
    }




    @GetMapping
    public ResponseEntity<List<String>> getFavorites(@RequestHeader("Authorization") String authHeader) {
        // Extract username from the JWT token
        String token = authHeader.replace("Bearer ", ""); // Remove "Bearer " prefix
        String username = tokenProvider.getAuthentication(token).getName();

        // Ensure username is valid
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid token: Username cannot be determined.");
        }

        // Retrieve favorite endpoints for the user
        List<String> favorites = favoriteService.getFavoriteEndpoints(username);
        return ResponseEntity.ok(favorites); // Return the list of favorite endpoints
    }



    @DeleteMapping
    public ResponseEntity<Void> removeFavorite(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String endpoint) {
        // Extract username from the JWT token
        String token = authHeader.replace("Bearer ", ""); // Remove "Bearer " prefix
        String username = tokenProvider.getAuthentication(token).getName();

        // Ensure username is valid
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid token: Username cannot be determined.");
        }

        // Call the service method to remove the favorite
        favoriteService.removeFavorite(username, endpoint);

        // Return HTTP 204 No Content
        return ResponseEntity.noContent().build();
    }


}
