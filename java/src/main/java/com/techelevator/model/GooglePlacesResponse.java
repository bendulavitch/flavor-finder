package com.techelevator.model;

import java.util.List;

public class GooglePlacesResponse {
    private List<GooglePlaceResult> results;

    public List<GooglePlaceResult> getResults() {
        return results;
    }

    public void setResults(List<GooglePlaceResult> results) {
        this.results = results;
    }
}
