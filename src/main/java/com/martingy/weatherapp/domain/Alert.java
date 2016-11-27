package com.martingy.weatherapp.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Alert.
 */
@Entity
@Table(name = "alert")
public class Alert implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "threshold_temperature", nullable = false)
    private Double thresholdTemperature;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "weather_description")
    private String weatherDescription;

    @Column(name = "icon")
    private String icon;

    @NotNull
    @Column(name = "email", nullable = false)
    private Boolean email;

    @NotNull
    @Column(name = "popup", nullable = false)
    private Boolean popup;

    @NotNull
    @Size(min = 3)
    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "city_id")
    private Integer cityId;

    @Column(name = "last_notification_date")
    private ZonedDateTime lastNotificationDate;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getThresholdTemperature() {
        return thresholdTemperature;
    }

    public Alert thresholdTemperature(Double thresholdTemperature) {
        this.thresholdTemperature = thresholdTemperature;
        return this;
    }

    public void setThresholdTemperature(Double thresholdTemperature) {
        this.thresholdTemperature = thresholdTemperature;
    }

    public Double getTemperature() {
        return temperature;
    }

    public Alert temperature(Double temperature) {
        this.temperature = temperature;
        return this;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public Alert weatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
        return this;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getIcon() {
        return icon;
    }

    public Alert icon(String icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean isEmail() {
        return email;
    }

    public Alert email(Boolean email) {
        this.email = email;
        return this;
    }

    public void setEmail(Boolean email) {
        this.email = email;
    }

    public Boolean isPopup() {
        return popup;
    }

    public Alert popup(Boolean popup) {
        this.popup = popup;
        return this;
    }

    public void setPopup(Boolean popup) {
        this.popup = popup;
    }

    public String getCity() {
        return city;
    }

    public Alert city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCityId() {
        return cityId;
    }

    public Alert cityId(Integer cityId) {
        this.cityId = cityId;
        return this;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public ZonedDateTime getLastNotificationDate() {
        return lastNotificationDate;
    }

    public Alert lastNotificationDate(ZonedDateTime lastNotificationDate) {
        this.lastNotificationDate = lastNotificationDate;
        return this;
    }

    public void setLastNotificationDate(ZonedDateTime lastNotificationDate) {
        this.lastNotificationDate = lastNotificationDate;
    }

    public User getUser() {
        return user;
    }

    public Alert user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Alert alert = (Alert) o;
        if(alert.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, alert.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Alert{" +
            "id=" + id +
            ", thresholdTemperature='" + thresholdTemperature + "'" +
            ", temperature='" + temperature + "'" +
            ", weatherDescription='" + weatherDescription + "'" +
            ", icon='" + icon + "'" +
            ", email='" + email + "'" +
            ", popup='" + popup + "'" +
            ", city='" + city + "'" +
            ", cityId='" + cityId + "'" +
            ", lastNotificationDate='" + lastNotificationDate + "'" +
            '}';
    }
}
