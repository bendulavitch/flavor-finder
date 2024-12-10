package com.techelevator.service;

import com.techelevator.dao.FavoriteDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.User;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FavoriteService {
    private final FavoriteDao favoriteDao;
    private final UserDao userDao;

    public FavoriteService(FavoriteDao favoriteDao, UserDao userDao) {
        this.favoriteDao = favoriteDao;
        this.userDao = userDao;
    }

    public void addFavorite(String username, String restaurantEndpoint) {
        // Fetch the user ID using the username
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }

        int userId = user.getId(); // Ensure you're using the numeric ID
        favoriteDao.addFavorite(userId, restaurantEndpoint); // Pass correct ID
    }



    public List<String> getFavoriteEndpoints(int userId) {
        // Fetch the user ID using the username
      //  User user = userDao.getUserByUsername(username);
      //  if (user == null) {
      //      throw new IllegalArgumentException("User not found: " + username);
       // }

        //int userId = user.getId(); // Ensure  the numeric ID

        // Retrieve favorite endpoints for the user
        return favoriteDao.getFavoriteEndpoints(userId);
    }


    public void removeFavorite(int userId, String endpoint) {
        // Fetch the user ID using the username
//        User user = userDao.getUserByUsername(username);
//        if (user == null) {
//            throw new IllegalArgumentException("User not found: " + username);
//        }

      //  int userId = user.getId(); // Ensure the numeric ID

        // Remove the restaurant endpoint from the user's favorites
        favoriteDao.removeFavorite(userId, endpoint);
    }

}
