package com.techelevator.service;

import com.techelevator.model.Restaurant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Service
public class RestaurantApiService {

    private final RestTemplate restTemplate;

    @Value("${rapidapi.key}")
    private String apiKey;

    @Value("${rapidapi.host}")
    private String apiHost;

    @Value("${rapidapi.baseurl}")
    private String baseUrl;

    public RestaurantApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Restaurant> fetchRestaurantsFromApi(String queryType, String queryValue) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", apiKey);
        headers.set("X-RapidAPI-Host", apiHost);

        HttpEntity<Void> entity = new HttpEntity<>(headers);


        String url = switch (queryType.toLowerCase()) {
            case "zipcode" -> baseUrl + "/location/zipcode/" + queryValue + "/0";
            case "state" -> baseUrl + "/location/state/" + queryValue + "/0";
            case "city" -> baseUrl + "/all-city?city=" + queryValue;
            default -> throw new IllegalArgumentException("Invalid query type");
        };

        ResponseEntity<Restaurant[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Restaurant[].class
        );

        return List.of(response.getBody());
    }
}