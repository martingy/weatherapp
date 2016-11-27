package com.martingy.weatherapp.service.dto;

import java.io.Serializable;

/**
 * A DTO representing a notification about weather alert.
 */
public class NotificationDTO implements Serializable {


    private Long alertId;

    private String city;

    private Integer cityId;

    private Double thresholdTemperature;

    private Double temperature;


    private Long userId;

    private String login;

    private String email;

    private String firstName;

    private String lastName;


    public NotificationDTO(Long alertId, String city, Integer cityId, Double thresholdTemperature, Double temperature, Long userId, String login, String email, String firstName, String lastName) {
        this.alertId = alertId;
        this.city = city;
        this.cityId = cityId;
        this.thresholdTemperature = thresholdTemperature;
        this.temperature = temperature;
        this.userId = userId;
        this.login = login;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public NotificationDTO() {
    }

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Double getThresholdTemperature() {
        return thresholdTemperature;
    }

    public void setThresholdTemperature(Double thresholdTemperature) {
        this.thresholdTemperature = thresholdTemperature;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
            "alertId=" + alertId +
            ", city='" + city + '\'' +
            ", cityId=" + cityId +
            ", thresholdTemperature=" + thresholdTemperature +
            ", temperature=" + temperature +
            ", userId=" + userId +
            ", login='" + login + '\'' +
            ", email='" + email + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            '}';
    }
}
