package com.example.WeatherApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    @Autowired
    private GeoService geoService;

    @GetMapping("/api/location")
    public GeoLocation getLocation(
            @RequestParam String zipCode,
            @RequestParam String countryCode) {

        try {
            return geoService.fetchGeoLocation(zipCode, countryCode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/weather")
    public String getWeather(
            @RequestParam String zipCode,
            @RequestParam String countryCode) {
        try {
            GeoLocation location = geoService.fetchGeoLocation(zipCode, countryCode);

            if (location != null) {
                return geoService.fetchWeatherForecast(location.getLat(),
                        location.getLon());
            } else {
                return "Unable to fetch location data.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occured while fetching the weather data.";
        }
    }
}
