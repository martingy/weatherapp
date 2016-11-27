package com.martingy.weatherapp.service.dto;

import java.io.Serializable;
import java.util.List;

/**
 * A DTO representing a City
 */
public class OpenWeatherMapCityDTO implements Serializable {

    private Integer id;

    private String name;

    private List<OpenWeatherMapWeatherDTO> weather;

    private OpenWeatherMapMainDTO main;

    public OpenWeatherMapCityDTO() {
    }

    public OpenWeatherMapCityDTO(Integer id) {
        this.id = id;
    }

    public OpenWeatherMapCityDTO(Integer id, String name, List<OpenWeatherMapWeatherDTO> weather, OpenWeatherMapMainDTO main) {
        this.id = id;
        this.name = name;
        this.weather = weather;
        this.main = main;
    }

    public List<OpenWeatherMapWeatherDTO> getWeather() {
        return weather;
    }

    public void setWeather(List<OpenWeatherMapWeatherDTO> weather) {
        this.weather = weather;
    }

    public OpenWeatherMapMainDTO getMain() {
        return main;
    }

    public void setMain(OpenWeatherMapMainDTO main) {
        this.main = main;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "OpenWeatherMapCityDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", weather=" + weather +
            ", main=" + main +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OpenWeatherMapCityDTO that = (OpenWeatherMapCityDTO) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
