package com.techelevator.model;

public class Restaurant {

    private int id; // Optional, for database reference
    private String name;
    private String address;
    private String zipCode;
    private String phone; // Optional, may not always be available
    private String website; // Optional
    private String latitude;
    private String longitude;
    private String stateName; // State
    private String cityName; // City
    private String hoursInterval; // Business hours, optional
    private String cuisineType; // Cuisine type, optional
    private double rating; // Rating from Google Places API
    private String image; // URL for the restaurant image
    private double distance; // Distance (added)
    private boolean kidFriendly; // Kid-friendly (added)

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getHoursInterval() {
        return hoursInterval;
    }

    public void setHoursInterval(String hoursInterval) {
        this.hoursInterval = hoursInterval;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getDistance() { // Getter for distance
        return distance;
    }

    public void setDistance(double distance) { // Setter for distance
        this.distance = distance;
    }

    public boolean isKidFriendly() { // Getter for kid-friendly
        return kidFriendly;
    }

    public void setKidFriendly(boolean kidFriendly) { // Setter for kid-friendly
        this.kidFriendly = kidFriendly;
    }
}
