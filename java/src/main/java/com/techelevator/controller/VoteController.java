package com.techelevator.controller;

import com.techelevator.model.RestaurantOption;
import com.techelevator.model.User;
import com.techelevator.model.VoteSession;
import com.techelevator.security.jwt.TokenProvider;
import com.techelevator.service.VoteService;
import com.techelevator.dao.UserDao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/votes")
@CrossOrigin
public class VoteController {

    private final VoteService voteService;
    private final TokenProvider tokenProvider;
    private final UserDao userDao;

    public VoteController(VoteService voteService, TokenProvider tokenProvider, UserDao userDao) {
        this.voteService = voteService;
        this.tokenProvider = tokenProvider;
        this.userDao = userDao;
    }

    @PostMapping
    public ResponseEntity<?> createVoteSession(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String queryType,
            @RequestParam String queryValue
    ) {
        String token = authHeader.replace("Bearer ", "");
        String username = tokenProvider.getAuthentication(token).getName();
        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid token: Username not found.");
        }

        Long sessionId = voteService.createVoteSession(username, queryType, queryValue);
        VoteSession session = voteService.getVoteSessionById(sessionId);

        return ResponseEntity.ok(session.getRoomCode());
    }

    @GetMapping("/code/{roomCode}")
    public ResponseEntity<VoteSession> getSessionByCode(@PathVariable String roomCode) {
        VoteSession session = voteService.getVoteSessionByRoomCode(roomCode);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(session);
    }

    @PostMapping("/{id}/vote")
    public ResponseEntity<?> castVote(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @RequestBody Map<String,Object> payload
    ) {
        // payload: { "restaurantOptionId": X, "round": 1 or 2, "inFavor": true/false }
        Long optionId = Long.valueOf(payload.get("restaurantOptionId").toString());
        int round = Integer.parseInt(payload.get("round").toString());
        boolean inFavor = Boolean.parseBoolean(payload.get("inFavor").toString());

        voteService.castVote(id, optionId, round, inFavor);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/round2")
    public ResponseEntity<?> proceedToRoundTwo(@PathVariable Long id) {
        voteService.proceedToRoundTwo(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/finalists")
    public ResponseEntity<List<RestaurantOption>> getFinalists(@PathVariable Long id, @RequestParam(defaultValue="5") int limit) {
        List<RestaurantOption> finalists = voteService.getFinalists(id, limit);
        return ResponseEntity.ok(finalists);
    }

    @GetMapping("/{id}/winner")
    public ResponseEntity<RestaurantOption> getWinner(@PathVariable Long id) {
        RestaurantOption winner = voteService.pickWinner(id);
        return ResponseEntity.ok(winner);
    }
}
