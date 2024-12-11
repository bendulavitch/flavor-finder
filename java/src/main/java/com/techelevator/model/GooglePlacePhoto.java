package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GooglePlacePhoto {

    @JsonProperty("photo_reference")
    private String photo_reference;

    public String getPhotoReference() {
        return photo_reference;
    }

    public void setPhotoReference(String photoReference) {
        this.photo_reference = photoReference;
    }
}
