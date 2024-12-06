package com.techelevator.service;

import com.techelevator.model.GooglePlaceResult;
import com.techelevator.model.GooglePlacesResponse;
import com.techelevator.model.Restaurant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantApiService {

    private final RestTemplate restTemplate;

    @Value("${google.places.baseurl}")
    private String baseUrl;

    @Value("${google.places.key}")
    private String apiKey;

    public RestaurantApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Restaurant> fetchRestaurantsFromApi(String queryType, String queryValue) {
        String query = queryType.equalsIgnoreCase("zipcode") ?
                "restaurants+in+" + queryValue :
                "restaurants+in+" + queryValue.replace(" ", "+");

        String url = String.format("%s?query=%s&key=%s", baseUrl, query, apiKey);

        try {
            ResponseEntity<GooglePlacesResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    GooglePlacesResponse.class
            );

            if (response.getBody() == null || response.getBody().getResults().isEmpty()) {
                System.err.println("No restaurants found for the given query: " + queryValue);
                return List.of();
            }

            return mapToRestaurantList(response.getBody().getResults());
        } catch (Exception e) {
            System.err.println("Error fetching restaurants from Google Places API: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    private List<Restaurant> mapToRestaurantList(List<GooglePlaceResult> results) {
        List<Restaurant> restaurants = new ArrayList<>();
        for (GooglePlaceResult result : results) {
            Restaurant restaurant = new Restaurant();
            restaurant.setName(result.getName());
            restaurant.setAddress(result.getFormattedAddress());
            restaurant.setRating(result.getRating());
            restaurant.setImage(result.getPhotos() != null && !result.getPhotos().isEmpty()
                    ? String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=%s&key=%s",
                    result.getPhotos().get(0).getPhotoReference(), apiKey)
                    : null);
            restaurants.add(restaurant);
        }
        return restaurants;
    }
}
