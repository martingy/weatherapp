package com.martingy.weatherapp.service.mapper;

import com.martingy.weatherapp.domain.Alert;
import com.martingy.weatherapp.service.dto.NotificationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Alert and DTO OpenWeatherMapCityDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AlertNotificationDTOMapper {

    @Mapping(source = "id", target = "alertId")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "cityId", target = "cityId")
    @Mapping(source = "thresholdTemperature", target = "thresholdTemperature")
    @Mapping(source = "temperature", target = "temperature")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "login")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    NotificationDTO alertToNotificationDTO(Alert alert);

}
