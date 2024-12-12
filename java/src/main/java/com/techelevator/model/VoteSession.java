package com.techelevator.model;

import java.time.LocalDateTime;
import java.util.List;

public class VoteSession {
    private Long id;
    private String creatorUsername;
    private String queryType;
    private String queryValue;
    private String roomCode;
    private int round; // 1 or 2
    private LocalDateTime createdAt;
    private List<RestaurantOption> restaurantOptions;

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCreatorUsername() { return creatorUsername; }
    public void setCreatorUsername(String creatorUsername) { this.creatorUsername = creatorUsername; }

    public String getQueryType() { return queryType; }
    public void setQueryType(String queryType) { this.queryType = queryType; }

    public String getQueryValue() { return queryValue; }
    public void setQueryValue(String queryValue) { this.queryValue = queryValue; }

    public String getRoomCode() { return roomCode; }
    public void setRoomCode(String roomCode) { this.roomCode = roomCode; }

    public int getRound() { return round; }
    public void setRound(int round) { this.round = round; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<RestaurantOption> getRestaurantOptions() { return restaurantOptions; }
    public void setRestaurantOptions(List<RestaurantOption> restaurantOptions) { this.restaurantOptions = restaurantOptions; }
}
