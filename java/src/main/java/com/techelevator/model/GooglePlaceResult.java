package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GooglePlaceResult {
    private String name;

    @JsonProperty("formatted_address")
    private String formattedAddress;

    @JsonProperty("business_status")
    private String businessStatus;

    private Double rating;

    @JsonProperty("user_ratings_total")
    private Integer userRatingsTotal;

    private List<GooglePlacePhoto> photos;

    @JsonProperty("opening_hours")
    private OpeningHours openingHours;

    @JsonProperty("formatted_phone_number")
    private String formattedPhoneNumber;

    @JsonProperty("delivery")
    private Boolean delivery;

    @JsonProperty("dine_in")
    private Boolean dineIn;

    @JsonProperty("serves_beer")
    private Boolean servesBeer;

    @JsonProperty("serves_wine")
    private Boolean servesWine;

    private String website;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getUserRatingsTotal() {
        return userRatingsTotal;
    }

    public void setUserRatingsTotal(Integer userRatingsTotal) {
        this.userRatingsTotal = userRatingsTotal;
    }

    public List<GooglePlacePhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<GooglePlacePhoto> photos) {
        this.photos = photos;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    public Boolean getDelivery() {
        return delivery;
    }

    public void setDelivery(Boolean delivery) {
        this.delivery = delivery;
    }

    public Boolean getDineIn() {
        return dineIn;
    }

    public void setDineIn(Boolean dineIn) {
        this.dineIn = dineIn;
    }

    public Boolean getServesBeer() {
        return servesBeer;
    }

    public void setServesBeer(Boolean servesBeer) {
        this.servesBeer = servesBeer;
    }

    public Boolean getServesWine() {
        return servesWine;
    }

    public void setServesWine(Boolean servesWine) {
        this.servesWine = servesWine;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
