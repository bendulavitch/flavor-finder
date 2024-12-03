package com.techelevator.dao;

import com.techelevator.model.Restaurant;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcRestaurantDao implements RestaurantDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcRestaurantDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        String sql = "SELECT * FROM restaurants";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Restaurant restaurant = new Restaurant();
            restaurant.setId(rs.getInt("id"));
            restaurant.setName(rs.getString("name"));
            restaurant.setImage(rs.getString("image"));
            restaurant.setRating(rs.getDouble("rating"));
            restaurant.setDistance(rs.getDouble("distance"));
            restaurant.setKidFriendly(rs.getBoolean("kid_friendly"));
            return restaurant;
        });
    }

    @Override
    public void addFavorite(int userId, int restaurantId) {
        String sql = "INSERT INTO favorites (user_id, restaurant_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, restaurantId);
    }

    @Override
    public void skipRestaurant(int userId, int restaurantId) {
        String sql = "INSERT INTO skipped (user_id, restaurant_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, restaurantId);
    }
}
