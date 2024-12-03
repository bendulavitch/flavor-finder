package com.techelevator.controller;

import com.techelevator.model.FavoriteRequest;
import com.techelevator.model.Restaurant;
import com.techelevator.model.SkipRequest;
import com.techelevator.service.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(restaurants); // Returns HTTP 200 with restaurant data
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> addFavorite(@RequestBody FavoriteRequest request) {
        restaurantService.addFavorite(request.getUserId(), request.getRestaurantId());
        return ResponseEntity.ok().build(); // Returns HTTP 200 with no body
    }

    @PostMapping("/skipped")
    public ResponseEntity<Void> skipRestaurant(@RequestBody SkipRequest request) {
        restaurantService.skipRestaurant(request.getUserId(), request.getRestaurantId());
        return ResponseEntity.ok().build(); // Returns HTTP 200 with no body
    }

}
