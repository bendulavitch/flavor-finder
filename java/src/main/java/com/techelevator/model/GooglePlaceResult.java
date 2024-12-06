package com.techelevator.model;

import java.util.List;

public class GooglePlaceResult {
    private String name;
    private String formatted_address;
    private float rating;
    private List<GooglePlacePhoto> photos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormattedAddress() {
        return formatted_address;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formatted_address = formattedAddress;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public List<GooglePlacePhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<GooglePlacePhoto> photos) {
        this.photos = photos;
    }
}
