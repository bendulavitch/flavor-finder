package com.techelevator.dao;

import com.techelevator.model.Restaurant;

import java.util.List;

public interface RestaurantDao {
    List<Restaurant> getAllRestaurants();
    void addFavorite(int userId, int restaurantId);
    void skipRestaurant(int userId, int restaurantId);
    List<Restaurant> getFavoritesByUserId(int userId);
    List<Restaurant> getSkippedByUserId(int userId);
}
