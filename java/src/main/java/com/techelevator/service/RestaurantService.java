package com.techelevator.service;

import com.techelevator.dao.RestaurantDao;
import com.techelevator.model.Restaurant;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantDao restaurantDao;
    private final RestaurantApiService restaurantApiService;

    public RestaurantService(RestaurantDao restaurantDao, RestaurantApiService restaurantApiService) {
        this.restaurantDao = restaurantDao;
        this.restaurantApiService = restaurantApiService;
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantDao.getAllRestaurants();
    }

    public List<Restaurant> getRestaurantsFromApi(String queryType, String queryValue) {
        return restaurantApiService.fetchRestaurantsFromApi(queryType, queryValue);
    }

    public void addFavorite(int userId, int restaurantId) {
        restaurantDao.addFavorite(userId, restaurantId);
    }

    public void skipRestaurant(int userId, int restaurantId) {
        restaurantDao.skipRestaurant(userId, restaurantId);
    }
}
