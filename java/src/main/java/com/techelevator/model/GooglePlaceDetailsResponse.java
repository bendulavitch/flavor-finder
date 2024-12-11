package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GooglePlaceDetailsResponse {
    private GooglePlaceResult result;

    public GooglePlaceResult getResult() {
        return result;
    }

    public void setResult(GooglePlaceResult result) {
        this.result = result;
    }
}
