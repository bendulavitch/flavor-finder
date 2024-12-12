package com.techelevator.dao;

import com.techelevator.model.RestaurantOption;
import com.techelevator.model.VoteSession;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcVoteSessionDao implements VoteSessionDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcVoteSessionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long createVoteSession(VoteSession session) {
        String sql = "INSERT INTO vote_sessions (creator_username, query_type, query_value, room_code, round, created_at) " +
                "VALUES (?, ?, ?, ?, 1, NOW()) RETURNING id";
        return jdbcTemplate.queryForObject(sql, Long.class,
                session.getCreatorUsername(), session.getQueryType(), session.getQueryValue(), session.getRoomCode());
    }

    @Override
    public VoteSession getVoteSessionById(Long id) {
        String sql = "SELECT * FROM vote_sessions WHERE id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        if (rs.next()) {
            return mapRowToVoteSession(rs);
        }
        return null;
    }

    @Override
    public VoteSession getVoteSessionByRoomCode(String roomCode) {
        String sql = "SELECT * FROM vote_sessions WHERE room_code = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, roomCode);
        if (rs.next()) {
            return mapRowToVoteSession(rs);
        }
        return null;
    }

    @Override
    public void addRestaurantOptions(Long voteSessionId, List<RestaurantOption> options) {
        String sql = "INSERT INTO restaurant_options (vote_session_id, name, place_id) VALUES (?, ?, ?)";
        for (RestaurantOption ro : options) {
            jdbcTemplate.update(sql, voteSessionId, ro.getName(), ro.getPlaceId());
        }
    }

    @Override
    public void incrementVote(Long restaurantOptionId, int round, boolean inFavor) {
        String column = round == 1 ? "votes_round1" : "votes_round2";
        String sql = "UPDATE restaurant_options SET " + column + " = " + column + " + ? WHERE id = ?";
        jdbcTemplate.update(sql, inFavor ? 1 : 0, restaurantOptionId);
    }

    @Override
    public void updateRound(Long voteSessionId, int round) {
        String sql = "UPDATE vote_sessions SET round = ? WHERE id = ?";
        jdbcTemplate.update(sql, round, voteSessionId);
    }

    @Override
    public List<RestaurantOption> getRestaurantOptions(Long voteSessionId) {
        String sql = "SELECT * FROM restaurant_options WHERE vote_session_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            RestaurantOption ro = new RestaurantOption();
            ro.setId(rs.getLong("id"));
            ro.setVoteSessionId(rs.getLong("vote_session_id"));
            ro.setName(rs.getString("name"));
            ro.setPlaceId(rs.getString("place_id"));
            ro.setVotesRound1(rs.getInt("votes_round1"));
            ro.setVotesRound2(rs.getInt("votes_round2"));
            return ro;
        }, voteSessionId);
    }

    private VoteSession mapRowToVoteSession(SqlRowSet rs) {
        VoteSession session = new VoteSession();
        session.setId(rs.getLong("id"));
        session.setCreatorUsername(rs.getString("creator_username"));
        session.setQueryType(rs.getString("query_type"));
        session.setQueryValue(rs.getString("query_value"));
        session.setRoomCode(rs.getString("room_code"));
        session.setRound(rs.getInt("round"));
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        session.setCreatedAt(createdAt);


        return session;
    }
}
