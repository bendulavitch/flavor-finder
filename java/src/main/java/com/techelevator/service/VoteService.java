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
        // Fetch basic restaurants from the API
        List<Restaurant> restaurants = restaurantApiService.fetchRestaurantsFromApi(queryType, queryValue);
        String roomCode = generateRoomCode();

        VoteSession session = new VoteSession();
        session.setCreatorUsername(username);
        session.setQueryType(queryType);
        session.setQueryValue(queryValue);
        session.setRoomCode(roomCode);
        session.setRound(1);

        Long sessionId = voteSessionDao.createVoteSession(session);

        // Convert Restaurant to RestaurantOption with minimal fields
        List<RestaurantOption> options = restaurants.stream().map(r -> {
            RestaurantOption ro = new RestaurantOption();
            ro.setVoteSessionId(sessionId);
            ro.setName(r.getName());
            ro.setPlaceId(r.getPlaceId());
            // votes are initially 0
            ro.setVotesRound1(0);
            ro.setVotesRound2(0);
            // We'll enrich these options later in getVoteSessionById or getVoteSessionByRoomCode
            return ro;
        }).collect(Collectors.toList());

        voteSessionDao.addRestaurantOptions(sessionId, options);
        return sessionId;
    }

    public VoteSession getVoteSessionById(Long id) {
        VoteSession session = voteSessionDao.getVoteSessionById(id);
        if (session != null) {
            List<RestaurantOption> options = voteSessionDao.getRestaurantOptions(id);
            // Enrich the options with full details
            enrichOptionsWithDetails(options);
            session.setRestaurantOptions(options);
        }
        return session;
    }

    public VoteSession getVoteSessionByRoomCode(String roomCode) {
        VoteSession session = voteSessionDao.getVoteSessionByRoomCode(roomCode);
        if (session != null) {
            List<RestaurantOption> options = voteSessionDao.getRestaurantOptions(session.getId());
            // Enrich the options with full details
            enrichOptionsWithDetails(options);
            session.setRestaurantOptions(options);
        }
        return session;
    }

    public void castVote(Long voteSessionId, Long optionId, int round, boolean inFavor) {
        voteSessionDao.incrementVote(optionId, round, inFavor);
    }

    public void proceedToRoundTwo(Long voteSessionId) {
        voteSessionDao.updateRound(voteSessionId, 2);
    }

    public List<RestaurantOption> getFinalists(Long voteSessionId, int limit) {
        List<RestaurantOption> options = voteSessionDao.getRestaurantOptions(voteSessionId);
        // Ensure finalists are also enriched (in case they are accessed here directly)
        enrichOptionsWithDetails(options);

        return options.stream()
                .sorted(Comparator.comparingInt(RestaurantOption::getVotesRound1).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public RestaurantOption pickWinner(Long voteSessionId) {
        List<RestaurantOption> options = voteSessionDao.getRestaurantOptions(voteSessionId);
        // Ensure winner also has details
        enrichOptionsWithDetails(options);

        int maxVotes = options.stream().mapToInt(RestaurantOption::getVotesRound2).max().orElse(0);
        List<RestaurantOption> topChoices = options.stream()
                .filter(o -> o.getVotesRound2() == maxVotes)
                .collect(Collectors.toList());

        if (topChoices.size() == 1) {
            return topChoices.get(0);
        } else {
            Random random = new SecureRandom();
            return topChoices.get(random.nextInt(topChoices.size()));
        }
    }

    private String generateRoomCode() {
        Random random = new SecureRandom();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    /**
     * Enrich each RestaurantOption by fetching full details via RestaurantApiService.
     * This mirrors what the favorites feature does.
     * For each option:
     *  - Call fetchRestaurantDetailsByPlaceId(option.getPlaceId())
     *  - Update fields: image, rating, phone, website, openNow, hoursInterval, servesBeer, servesWine, address
     */
    private void enrichOptionsWithDetails(List<RestaurantOption> options) {
        for (RestaurantOption opt : options) {
            Restaurant detailed = restaurantApiService.fetchRestaurantDetailsByPlaceId(opt.getPlaceId());
            if (detailed != null) {
                // Map from Restaurant to RestaurantOption fields
                opt.setImage(detailed.getImage());
                opt.setRating(detailed.getRating());
                opt.setPhone(detailed.getPhone());
                opt.setWebsite(detailed.getWebsite());
                opt.setOpenNow(detailed.OpenNow());
                opt.setHoursInterval(detailed.getHoursInterval());
                opt.setServesBeer(detailed.isServesBeer());
                opt.setServesWine(detailed.isServesWine());
                opt.setAddress(detailed.getAddress());
            }
        }
    }
}
