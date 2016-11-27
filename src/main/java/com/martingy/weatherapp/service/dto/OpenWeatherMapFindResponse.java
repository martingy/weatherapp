package com.martingy.weatherapp.service.dto;

import java.io.Serializable;
import java.util.List;

/**
 * A DTO representing the response of OpenWeatherMap Api endpoint find
 */
public class OpenWeatherMapFindResponse implements Serializable {

    private String message;

    private String cod;

    private int count;

    private List<OpenWeatherMapCityDTO> list;

    public OpenWeatherMapFindResponse(String message, String cod, int count, List<OpenWeatherMapCityDTO> list) {
        this.message = message;
        this.cod = cod;
        this.count = count;
        this.list = list;
    }

    public OpenWeatherMapFindResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<OpenWeatherMapCityDTO> getList() {
        return list;
    }

    public void setList(List<OpenWeatherMapCityDTO> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "OpenWeatherMapFindResponse{" +
            "message='" + message + '\'' +
            ", cod='" + cod + '\'' +
            ", count=" + count +
            ", list=" + list +
            '}';
    }
}
