package com.techelevator.dao;

import com.techelevator.model.RestaurantOption;
import com.techelevator.model.VoteSession;

import java.util.List;

public interface VoteSessionDao {
    Long createVoteSession(VoteSession session);
    VoteSession getVoteSessionById(Long id);
    VoteSession getVoteSessionByRoomCode(String roomCode);
    void addRestaurantOptions(Long voteSessionId, List<RestaurantOption> options);
    void incrementVote(Long restaurantOptionId, int round, boolean inFavor);
    void updateRound(Long voteSessionId, int round);
    List<RestaurantOption> getRestaurantOptions(Long voteSessionId);
}
