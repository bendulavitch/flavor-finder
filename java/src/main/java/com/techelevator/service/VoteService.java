package com.techelevator.service;

import com.techelevator.dao.UserDao;
import com.techelevator.dao.VoteSessionDao;
import com.techelevator.model.Restaurant;
import com.techelevator.model.RestaurantOption;
import com.techelevator.model.User;
import com.techelevator.model.VoteSession;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class VoteService {
    private final VoteSessionDao voteSessionDao;
    private final RestaurantApiService restaurantApiService;
    private final UserDao userDao;

    public VoteService(VoteSessionDao voteSessionDao, RestaurantApiService restaurantApiService, UserDao userDao) {
        this.voteSessionDao = voteSessionDao;
        this.restaurantApiService = restaurantApiService;
        this.userDao = userDao;
    }

    public Long createVoteSession(String username, String queryType, String queryValue) {
        // Fetch restaurants
        List<Restaurant> restaurants = restaurantApiService.fetchRestaurantsFromApi(queryType, queryValue);
        // Generate a room code
        String roomCode = generateRoomCode();

        VoteSession session = new VoteSession();
        session.setCreatorUsername(username);
        session.setQueryType(queryType);
        session.setQueryValue(queryValue);
        session.setRoomCode(roomCode);
        session.setRound(1);

        Long sessionId = voteSessionDao.createVoteSession(session);

        List<RestaurantOption> options = restaurants.stream().map(r -> {
            RestaurantOption ro = new RestaurantOption();
            ro.setVoteSessionId(sessionId);
            ro.setName(r.getName());
            ro.setPlaceId(r.getPlaceId());
            return ro;
        }).collect(Collectors.toList());

        voteSessionDao.addRestaurantOptions(sessionId, options);
        return sessionId;
    }

    public VoteSession getVoteSessionById(Long id) {
        VoteSession session = voteSessionDao.getVoteSessionById(id);
        if (session != null) {
            List<RestaurantOption> options = voteSessionDao.getRestaurantOptions(id);
            session.setRestaurantOptions(options);
        }
        return session;
    }

    public VoteSession getVoteSessionByRoomCode(String roomCode) {
        VoteSession session = voteSessionDao.getVoteSessionByRoomCode(roomCode);
        if (session != null) {
            List<RestaurantOption> options = voteSessionDao.getRestaurantOptions(session.getId());
            session.setRestaurantOptions(options);
        }
        return session;
    }

    public void castVote(Long voteSessionId, Long optionId, int round, boolean inFavor) {
        voteSessionDao.incrementVote(optionId, round, inFavor);
    }

    public void proceedToRoundTwo(Long voteSessionId) {
        // Move from round 1 to round 2
        voteSessionDao.updateRound(voteSessionId, 2);
    }

    public List<RestaurantOption> getFinalists(Long voteSessionId, int limit) {
        // Finalists are top rated from round 1
        List<RestaurantOption> options = voteSessionDao.getRestaurantOptions(voteSessionId);
        return options.stream()
                .sorted(Comparator.comparingInt(RestaurantOption::getVotesRound1).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public RestaurantOption pickWinner(Long voteSessionId) {
        // After round 2 voting, pick highest votes_round2 winner
        List<RestaurantOption> options = voteSessionDao.getRestaurantOptions(voteSessionId);
        int maxVotes = options.stream().mapToInt(RestaurantOption::getVotesRound2).max().orElse(0);
        List<RestaurantOption> topChoices = options.stream()
                .filter(o -> o.getVotesRound2() == maxVotes)
                .collect(Collectors.toList());

        if (topChoices.size() == 1) {
            return topChoices.get(0);
        } else {
            // Multiple winners - pick one at random
            Random random = new SecureRandom();
            return topChoices.get(random.nextInt(topChoices.size()));
        }
    }

    private String generateRoomCode() {
        // simple numeric code
        Random random = new SecureRandom();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
