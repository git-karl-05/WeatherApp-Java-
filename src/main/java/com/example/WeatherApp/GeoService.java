package com.example.WeatherApp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Service
public class GeoService {

    @Value("${weather.api.url.zip}")
    private String apiUrlByZip;

    @Value("${weather.api.url.coord}")
    private String apiUrlByCoord;
    /*
    In application.properties: http://api.openweathermap.org/geo/1.0/zip?
    full api code: http://api.openweathermap.org/geo/1.0/zip?zip={zip code},{country
    code}&appid={API key}
     */
    @Value("${weather.api.key}")
    private String apiKey;


    public GeoLocation fetchGeoLocation(String zipCode, String countryCode) throws Exception {
        String urlString =
                apiUrlByZip + "zip=" + zipCode + "," + countryCode + "&appid=" + apiKey;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString)) // sets the URI for request, similar as creating
                // URL object
                .GET() //sets the HTTP request method as GET
                .build(); // Build the HttpRequest object

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200 && response.body() != null && !response.body().isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), GeoLocation.class);
        } else {
            System.out.println("Error: " + response.statusCode());
            return null;
        }
    }

    public String fetchWeatherForecast(double lat, double lon) throws Exception {


        //https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
        String urlString = apiUrlByCoord + "lat=" + lat + "&lon=" + lon + "&appid=" + apiKey;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Weather Forecast Response Status Code: " + response.statusCode());
        System.out.println("Weather Forecast Response Body: " + response.body());

        if (response.statusCode() == 200 && response.body() != null && !response.body().isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.body());
            JsonNode mainNode = rootNode.path("main");
            return mainNode.toPrettyString();
        } else {
            System.out.println("Error: " + response.statusCode());
            return null;
        }
    }
}

