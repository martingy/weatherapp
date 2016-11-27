package com.martingy.weatherapp.service.dto;

import java.io.Serializable;
import java.util.List;

/**
 * A DTO representing the response of OpenWeatherMap Api endpoint find
 */
public class OpenWeatherMapGetResponse extends OpenWeatherMapCityDTO {

    @Override
    public String toString() {
        return "OpenWeatherMapGetResponse{} " + super.toString();
    }
}
