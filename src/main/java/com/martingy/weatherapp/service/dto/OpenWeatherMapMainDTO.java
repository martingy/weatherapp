package com.martingy.weatherapp.service.dto;

import java.io.Serializable;

/**
 * A DTO representing the main weather data of a city
 */
public class OpenWeatherMapMainDTO implements Serializable {

    private Double temp;

    public OpenWeatherMapMainDTO() {
    }

    public OpenWeatherMapMainDTO(Double temp) {
        this.temp = temp;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    @Override
    public String toString() {
        return "OpenWeatherMapMainDTO{" +
            "temp=" + temp +
            '}';
    }
}
