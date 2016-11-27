package com.martingy.weatherapp.service.mapper;

import com.martingy.weatherapp.domain.Alert;
import com.martingy.weatherapp.service.dto.OpenWeatherMapCityDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity Alert and DTO OpenWeatherMapCityDTO.
 */
@Mapper(componentModel = "spring", uses = {})
@DecoratedWith(AlertOpenWeatherMapCityDTOMapperDecorator.class)
public interface AlertOpenWeatherMapCityDTOMapper {

    @Mapping(source = "id", target = "cityId")
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "city")
    @Mapping(source = "main.temp", target = "temperature")
    void openWeatherMapCityDTOToAlert(OpenWeatherMapCityDTO openWeatherMapCityDTO, @MappingTarget Alert alert);

    @Mapping(source = "id", target = "cityId")
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "city")
    @Mapping(source = "main.temp", target = "temperature")
    Alert openWeatherMapCityDTOToAlert(OpenWeatherMapCityDTO openWeatherMapCityDTO);

}
