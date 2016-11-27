package com.martingy.weatherapp.service.mapper;

import com.martingy.weatherapp.domain.Alert;
import com.martingy.weatherapp.service.dto.OpenWeatherMapCityDTO;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;

/**
 * Decorator class to map values from weather list.
 */
public abstract class AlertOpenWeatherMapCityDTOMapperDecorator implements AlertOpenWeatherMapCityDTOMapper {

    @Inject
    @Qualifier("delegate")
    private AlertOpenWeatherMapCityDTOMapper delegate;

    @Override
    public void openWeatherMapCityDTOToAlert(OpenWeatherMapCityDTO openWeatherMapCityDTO, @MappingTarget Alert alert) {
        delegate.openWeatherMapCityDTOToAlert(openWeatherMapCityDTO, alert);
        if (openWeatherMapCityDTO.getWeather() != null && !openWeatherMapCityDTO.getWeather().isEmpty()) {
            alert.setWeatherDescription(openWeatherMapCityDTO.getWeather().get(0).getDescription());
            alert.setIcon(openWeatherMapCityDTO.getWeather().get(0).getIcon());
        }
    }

    @Override
    public Alert openWeatherMapCityDTOToAlert(OpenWeatherMapCityDTO openWeatherMapCityDTO) {
        Alert ret = delegate.openWeatherMapCityDTOToAlert(openWeatherMapCityDTO);
        if (openWeatherMapCityDTO.getWeather() != null && !openWeatherMapCityDTO.getWeather().isEmpty()) {
            ret.setWeatherDescription(openWeatherMapCityDTO.getWeather().get(0).getDescription());
            ret.setIcon(openWeatherMapCityDTO.getWeather().get(0).getIcon());
        }
        return ret;
    }
}
