package com.techelevator.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcFavoriteDao implements FavoriteDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcFavoriteDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    @Override
    public void addFavorite(int userId, String endpoint) {
        String sql = "INSERT INTO favorites (user_id, endpoint) VALUES (?, ?) ON CONFLICT DO NOTHING";
        jdbcTemplate.update(sql, userId, endpoint);
    }

    @Override
    public List<String> getFavoriteEndpoints(int userId) {
        String sql = "SELECT endpoint FROM favorites WHERE user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("endpoint"), userId);
    }

    @Override
    public void removeFavorite(int userId, String endpoint) {
        String sql = "DELETE FROM favorites WHERE user_id = ? AND endpoint = ?";
        jdbcTemplate.update(sql, userId, endpoint);
    }
}
