package com.martingy.weatherapp.service.util;

import com.martingy.weatherapp.config.JHipsterProperties;
import com.martingy.weatherapp.service.dto.OpenWeatherMapFindResponse;
import com.martingy.weatherapp.service.dto.OpenWeatherMapGetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;

/**
 * Client class for WeatherApi
 */
@Service
public class WeatherApiClient {

    private final Logger log = LoggerFactory.getLogger(WeatherApiClient.class);

    @Inject
    private JHipsterProperties jHipsterProperties;

    /**
     * Queries cities by the given q String
     * @param q query String for city name
     * @return found cities
     */
    public OpenWeatherMapFindResponse findCities(String q) {
        StringBuilder urlBuilder = new StringBuilder(jHipsterProperties.getWeatherApi().getOpenWeatherMapUrl());
        urlBuilder.append("find");
        urlBuilder.append("?appid=");
        urlBuilder.append(jHipsterProperties.getWeatherApi().getOpenWeatherMapAppId());
        urlBuilder.append("&type=like");
        urlBuilder.append("&mode=json");
        urlBuilder.append("&units=");
        urlBuilder.append(jHipsterProperties.getWeatherApi().getGetOpenWeatherMapUnits());
        urlBuilder.append("&q=");
        urlBuilder.append(q);

        RestTemplate restTemplate = new RestTemplate();
        OpenWeatherMapFindResponse res = restTemplate.getForObject(urlBuilder.toString(), OpenWeatherMapFindResponse.class);

        log.debug("Find cities response: {} ", res);

        return res;
    }

    /**
     * Gets a city by id
     * @param id id of the city
     * @return city by id
     */
    public OpenWeatherMapGetResponse getCity(Integer id) {
        StringBuilder urlBuilder = new StringBuilder(jHipsterProperties.getWeatherApi().getOpenWeatherMapUrl());
        urlBuilder.append("weather");
        urlBuilder.append("?appid=");
        urlBuilder.append(jHipsterProperties.getWeatherApi().getOpenWeatherMapAppId());
        urlBuilder.append("&mode=json");
        urlBuilder.append("&units=");
        urlBuilder.append(jHipsterProperties.getWeatherApi().getGetOpenWeatherMapUnits());
        urlBuilder.append("&id=");
        urlBuilder.append(id);

        RestTemplate restTemplate = new RestTemplate();
        OpenWeatherMapGetResponse res = restTemplate.getForObject(urlBuilder.toString(), OpenWeatherMapGetResponse.class);

        log.debug("Get city response: {} ", res);

        return res;
    }
}
