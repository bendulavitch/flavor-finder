package com.techelevator.model;

public class RestaurantOption {
    private Long id;
    private Long voteSessionId;
    private String name;
    private String placeId;
    private int votesRound1;
    private int votesRound2;

    // Newly added fields for full enrichment
    private String image;
    private double rating;
    private String phone;
    private String website;
    private boolean openNow;
    private String hoursInterval;
    private boolean servesBeer;
    private boolean servesWine;
    private String address; // Also add address to match Restaurant fields

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

    // New getters/setters
    public String getImage() {return image;}
    public void setImage(String image) {this.image = image;}

    public double getRating() {return rating;}
    public void setRating(double rating) {this.rating = rating;}

    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}

    public String getWebsite() {return website;}
    public void setWebsite(String website) {this.website = website;}

    public boolean isOpenNow() {return openNow;}
    public void setOpenNow(boolean openNow) {this.openNow = openNow;}

    public String getHoursInterval() {return hoursInterval;}
    public void setHoursInterval(String hoursInterval) {this.hoursInterval = hoursInterval;}

    public boolean isServesBeer() {return servesBeer;}
    public void setServesBeer(boolean servesBeer) {this.servesBeer = servesBeer;}

    public boolean isServesWine() {return servesWine;}
    public void setServesWine(boolean servesWine) {this.servesWine = servesWine;}

    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}
}
