package com.techelevator.model;

public class Restaurant {
    private int id;
    private String name;
    private String image;
    private double rating;
    private double distance;
    private boolean kidFriendly;

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }

    public boolean isKidFriendly() { return kidFriendly; }
    public void setKidFriendly(boolean kidFriendly) { this.kidFriendly = kidFriendly; }
}
