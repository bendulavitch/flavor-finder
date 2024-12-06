package com.techelevator.dao;

import com.techelevator.model.Restaurant;
import com.techelevator.service.RestaurantApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RestaurantApiServiceTest {

    @Autowired
    private RestaurantApiService restaurantApiService;

    @Test
    void fetchRestaurantsFromApi_ValidZipCode_ShouldReturnRestaurants() {
        // Valid zip code query
        String queryType = "zipcode";
        String queryValue = "90210"; // Example zip code

        List<Restaurant> restaurants = restaurantApiService.fetchRestaurantsFromApi(queryType, queryValue);

        // Assertions
        assertNotNull(restaurants);
        assertFalse(restaurants.isEmpty());
        System.out.println("Fetched restaurants: " + restaurants);
    }

    @Test
    void fetchRestaurantsFromApi_InvalidZipCode_ShouldReturnEmptyList() {
        // Invalid zip code query
        String queryType = "zipcode";
        String queryValue = "00000"; // Example invalid zip code

        List<Restaurant> restaurants = restaurantApiService.fetchRestaurantsFromApi(queryType, queryValue);

        // Assertions
        assertNotNull(restaurants);
        assertTrue(restaurants.isEmpty());
        System.out.println("No restaurants found for invalid zip code.");
    }
    @Test
    void fetchRestaurantsFromApi_ValidCity_ShouldReturnRestaurants() {
        String queryType = "city";
        String queryValue = "Los Angeles";

        List<Restaurant> restaurants = restaurantApiService.fetchRestaurantsFromApi(queryType, queryValue);

        assertNotNull(restaurants);
        assertFalse(restaurants.isEmpty());
        System.out.println("Fetched restaurants in city: " + restaurants);
    }
}
