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
@CrossOrigin
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<List<Restaurant>> getRestaurantsFromApi(
            @RequestParam String queryType,
            @RequestParam String queryValue
    ) {
        List<Restaurant> restaurants = restaurantService.getRestaurantsFromApi(queryType, queryValue);
        return ResponseEntity.ok(restaurants);
    }


    @PostMapping("/skipped")
    public ResponseEntity<Void> skipRestaurant(@RequestBody SkipRequest request) {
        restaurantService.skipRestaurant(request.getUserId(), request.getRestaurantId());
        return ResponseEntity.ok().build();
    }
}
