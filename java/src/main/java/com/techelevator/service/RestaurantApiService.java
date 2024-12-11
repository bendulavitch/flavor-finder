package com.techelevator.service;

import com.techelevator.model.GooglePlaceDetailsResponse;
import com.techelevator.model.GooglePlaceResult;
import com.techelevator.model.GooglePlacesResponse;
import com.techelevator.model.Restaurant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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

    // Use this for text search (multiple results)
    public List<Restaurant> fetchRestaurantsFromApi(String queryType, String queryValue) {
        String query = queryType.equalsIgnoreCase("zipcode")
                ? "restaurants+in+" + queryValue
                : "restaurants+in+" + queryValue.replace(" ", "+");

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

    // Use this for a single place details fetch
    public Restaurant fetchRestaurantDetailsByPlaceId(String placeId) {
        String url = "https://maps.googleapis.com/maps/api/place/details/json?place_id=" + placeId + "&key=" + apiKey;

        try {
            ResponseEntity<GooglePlaceDetailsResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    GooglePlaceDetailsResponse.class
            );

            if (response.getBody() == null || response.getBody().getResult() == null) {
                System.err.println("No details found for place_id: " + placeId);
                return null;
            }

            GooglePlaceResult result = response.getBody().getResult();
            return mapToRestaurant(result);
        } catch (Exception e) {
            System.err.println("Error fetching details from Google Places Details API: " + e.getMessage());
            return null;
        }
    }

    private List<Restaurant> mapToRestaurantList(List<GooglePlaceResult> results) {
        List<Restaurant> restaurants = new ArrayList<>();
        for (GooglePlaceResult result : results) {
            restaurants.add(mapToRestaurant(result));
        }
        return restaurants;
    }

    private Restaurant mapToRestaurant(GooglePlaceResult result) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(result.getName());
        restaurant.setAddress(result.getFormattedAddress());
        restaurant.setRating(result.getRating() != null ? result.getRating() : 0.0);
        restaurant.setWebsite(result.getWebsite());
        restaurant.setPhone(result.getFormattedPhoneNumber());
        restaurant.setPlaceId(result.getPlaceId());
        restaurant.setOpenNow(result.getOpeningHours() != null && result.getOpeningHours().isOpenNow());

        if (result.getOpeningHours() != null && result.getOpeningHours().getWeekdayText() != null) {
            restaurant.setHoursInterval(String.join(", ", result.getOpeningHours().getWeekdayText()));
        } else {
            restaurant.setHoursInterval("Hours not available");
        }

        if (result.getPhotos() != null && !result.getPhotos().isEmpty()) {
            restaurant.setImage(String.format(
                    "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=%s&key=%s",
                    result.getPhotos().get(0).getPhotoReference(), apiKey));
        } else {
            restaurant.setImage("default-image-path.jpg");
        }

        restaurant.setServesBeer(Boolean.TRUE.equals(result.getServesBeer()));
        restaurant.setServesWine(Boolean.TRUE.equals(result.getServesWine()));

        return restaurant;
    }
}
