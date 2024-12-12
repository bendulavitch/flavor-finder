package com.techelevator.model;

public class RestaurantOption {
    private Long id;
    private Long voteSessionId;
    private String name;
    private String placeId;
    private int votesRound1;
    private int votesRound2;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Long getVoteSessionId() {return voteSessionId;}
    public void setVoteSessionId(Long voteSessionId) {this.voteSessionId = voteSessionId;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getPlaceId() {return placeId;}
    public void setPlaceId(String placeId) {this.placeId = placeId;}

    public int getVotesRound1() {return votesRound1;}
    public void setVotesRound1(int votesRound1) {this.votesRound1 = votesRound1;}

    public int getVotesRound2() {return votesRound2;}
    public void setVotesRound2(int votesRound2) {this.votesRound2 = votesRound2;}
}
