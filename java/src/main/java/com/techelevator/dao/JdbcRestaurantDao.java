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
            restaurant.setId(rs.getInt("restaurant_id"));
            restaurant.setName(rs.getString("name"));
            restaurant.setAddress(rs.getString("address"));
            restaurant.setImage(rs.getString("image"));
            restaurant.setRating(rs.getDouble("rating"));


            restaurant.setOpenNow(rs.getBoolean("open_now"));

            restaurant.setPhone(rs.getString("phone"));
            restaurant.setServesBeer(rs.getBoolean("serves_beer"));
            restaurant.setServesWine(rs.getBoolean("serves_wine"));
            restaurant.setWebsite(rs.getString("website"));

            return restaurant;
        });
    }

    @Override
    public void addFavorite(int userId, int restaurantId) {
        String sql = "INSERT INTO favorites (user_id, restaurant_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        jdbcTemplate.update(sql, userId, restaurantId);
    }

    @Override
    public void skipRestaurant(int userId, int restaurantId) {
        String sql = "INSERT INTO skipped (user_id, restaurant_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        jdbcTemplate.update(sql, userId, restaurantId);
    }

    @Override
    public List<Restaurant> getFavoritesByUserId(int userId) {
        String sql = """
            SELECT r.* FROM restaurants r
            JOIN favorites f ON r.restaurant_id = f.restaurant_id
            WHERE f.user_id = ?
        """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Restaurant restaurant = new Restaurant();
            restaurant.setId(rs.getInt("restaurant_id"));
            restaurant.setName(rs.getString("name"));
            restaurant.setAddress(rs.getString("address"));
            restaurant.setImage(rs.getString("image"));
            restaurant.setRating(rs.getDouble("rating"));

            restaurant.setOpenNow(rs.getBoolean("open_now"));

            restaurant.setPhone(rs.getString("phone"));
            restaurant.setServesBeer(rs.getBoolean("serves_beer"));
            restaurant.setServesWine(rs.getBoolean("serves_wine"));
            restaurant.setWebsite(rs.getString("website"));

            return restaurant;
        }, userId);
    }

    @Override
    public List<Restaurant> getSkippedByUserId(int userId) {
        String sql = """
            SELECT r.* FROM restaurants r
            JOIN skipped s ON r.restaurant_id = s.restaurant_id
            WHERE s.user_id = ?
        """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Restaurant restaurant = new Restaurant();
            restaurant.setId(rs.getInt("restaurant_id"));
            restaurant.setName(rs.getString("name"));
            restaurant.setAddress(rs.getString("address"));
            restaurant.setImage(rs.getString("image"));
            restaurant.setRating(rs.getDouble("rating"));

            restaurant.setOpenNow(rs.getBoolean("open_now"));

            restaurant.setPhone(rs.getString("phone"));
            restaurant.setServesBeer(rs.getBoolean("serves_beer"));
            restaurant.setServesWine(rs.getBoolean("serves_wine"));
            restaurant.setWebsite(rs.getString("website"));

            return restaurant;
        }, userId);
    }

    public boolean isRestaurantSkipped(int userId, int restaurantId) {
        String sql = "SELECT COUNT(*) FROM skipped WHERE user_id = ? AND restaurant_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, restaurantId);
        return count != null && count > 0;
    }

    public boolean isRestaurantFavorited(int userId, int restaurantId) {
        String sql = "SELECT COUNT(*) FROM favorites WHERE user_id = ? AND restaurant_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, restaurantId);
        return count != null && count > 0;
    }
}
