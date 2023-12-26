package org.example.tgpocodabot.pagoda.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeatherResponseDTO {
    private LocationDTO location;
    private CurrentWeatherDTO current;
    private ForecastDTO forecast;

    private static final Map<String, String> conditionTranslations = new HashMap<>();

    static {
        conditionTranslations.put("Sunny", "Quyoshli ☀️");
        conditionTranslations.put("Rain", "Yomg'ir \uD83C\uDF27");
        conditionTranslations.put("Partly cloudy", "Qisman bulutli ☁\uFE0F");
        conditionTranslations.put("Light rain shower", "Yengil yomg'ir ☁\uFE0F");
    }

    public static String translateCondition(String condition) {
        return conditionTranslations.getOrDefault(condition, condition);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM", new Locale("uz", "UZ"));
        String formattedDate = LocalDate.now().format(formatter);


        return "📆 Bugun, " + formattedDate + "\n" +
                "📍 " + location.getName() + " hududida kutilayotgan ob-havo.\n\n" +
                "🌡️ " + forecast.getForecastDay().get(0).getDay().getMinTempC() + "°...+" + forecast.getForecastDay().get(0).getDay().getMaxTempC() +
                "°, " + translateCondition(current.getCondition().getText()) + "\n\n" +
                "🌅 Ertalab: " + forecast.getForecastDay().get(0).getDay().getMinTempC() + "°\n" +
                "☀️ Kunduzi: " + forecast.getForecastDay().get(0).getDay().getMaxTempC() + "°\n" +
                "🌙 Kechqurun: " + forecast.getForecastDay().get(0).getDay().getAvgTempC() + "°\n\n" +
                "-------------------------\n\n" +
                "💧 Namlik: " + (current.getHumidity() != null ? current.getHumidity() + "%" : "N/A") + "\n" +
                "🎈 Bosim: " + (current.getPressureMb() != null ? current.getPressureMb() + " mm sim. ust." : "N/A") + "\n" +
                "☀️ Quyosh chiqishi: " + forecast.getForecastDay().get(0).getAstro().getSunrise() + "\n" +
                "🌙 Quyosh botishi: " + forecast.getForecastDay().get(0).getAstro().getSunset();
    }
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class LocationDTO {
    private String name;
    private String region;
    private String country;
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class CurrentWeatherDTO {
    @JsonProperty("temp_c")
    private Double tempC;
    @JsonProperty("temp_f")
    private Double tempF;
    @JsonProperty("condition")
    private WeatherConditionDTO condition;
    @JsonProperty("humidity")
    private Integer humidity;
    @JsonProperty("wind")
    private WindDTO wind;
    @JsonProperty("pressure_mb")
    private Double pressureMb;
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class WindDTO {
    private String direction;
    private Double speed;
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class WeatherConditionDTO {
    private String text;
    private String icon;
    private Integer code;
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class ForecastDTO {
    @JsonProperty("forecastday")
    private List<ForecastDayDTO> forecastDay;
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class ForecastDayDTO {
    private String date;
    private DayDTO day;
    private AstroDTO astro;
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class DayDTO {
    @JsonProperty("maxtemp_c")
    private Double maxTempC;
    @JsonProperty("mintemp_c")
    private Double minTempC;
    @JsonProperty("avgtemp_c")
    private Double avgTempC;
    @JsonProperty("condition")
    private WeatherConditionDTO condition;
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class AstroDTO {
    private String sunrise;
    private String sunset;
}
