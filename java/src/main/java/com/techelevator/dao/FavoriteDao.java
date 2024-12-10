package com.techelevator.dao;

import java.util.List;

public interface FavoriteDao {
    void addFavorite(int userId, String endpoint);
    List<String> getFavoriteEndpoints(int userId);
    void removeFavorite(int userId, String endpoint);
}
