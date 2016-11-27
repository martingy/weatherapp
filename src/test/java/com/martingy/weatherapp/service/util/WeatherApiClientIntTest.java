package com.martingy.weatherapp.service.util;

import com.martingy.weatherapp.WeatherappApp;
import com.martingy.weatherapp.service.dto.OpenWeatherMapCityDTO;
import com.martingy.weatherapp.service.dto.OpenWeatherMapFindResponse;
import com.martingy.weatherapp.service.dto.OpenWeatherMapGetResponse;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

/**
 * Test class for the AlertResource REST controller.
 *
 * @see WeatherApiClient
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WeatherappApp.class)
public class WeatherApiClientIntTest {

    private static final int LONDON_CITY_ID = 2643743;

    @Inject
    private WeatherApiClient weatherApiClient;

    @Test
    public void testFindCities() {
        OpenWeatherMapFindResponse lon = weatherApiClient.findCities("lon");
        Assertions.assertThat(lon.getList()).contains(new OpenWeatherMapCityDTO(LONDON_CITY_ID));
    }

    @Test
    public void testGetCity() {
        OpenWeatherMapGetResponse city = weatherApiClient.getCity(LONDON_CITY_ID);
        Assertions.assertThat(city.getId()).isEqualTo(LONDON_CITY_ID);
    }

}
