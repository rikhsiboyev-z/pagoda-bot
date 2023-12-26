package org.example.tgpocodabot.pagoda;

import org.example.tgpocodabot.pagoda.dto.WeatherResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "weather-api", url = "https://api.weatherapi.com/v1")
public interface PagodaFeign {
    @GetMapping("/forecast.json")
    WeatherResponseDTO getWeatherForecast(
            @RequestParam("key") String apiKey,
            @RequestParam("q") String location,
            @RequestParam("dt") String date,
            @RequestParam("hour") String hour
    );

    @GetMapping("/forecast.json")
    WeatherResponseDTO getWeatherDaysForecast(
            @RequestParam("key") String apiKey,
            @RequestParam("q") String location,
            @RequestParam("days") int days,
            @RequestParam("hour") String hour

    );
}
