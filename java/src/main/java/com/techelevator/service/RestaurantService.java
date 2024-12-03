package com.techelevator.service;

import com.techelevator.dao.RestaurantDao;
import com.techelevator.model.Restaurant;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantDao restaurantDao;

    public RestaurantService(RestaurantDao restaurantDao) {
        this.restaurantDao = restaurantDao;
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantDao.getAllRestaurants();
    }

    public void addFavorite(int userId, int restaurantId) {
        restaurantDao.addFavorite(userId, restaurantId);
    }

    public void skipRestaurant(int userId, int restaurantId) {
        restaurantDao.skipRestaurant(userId, restaurantId);
    }
}
