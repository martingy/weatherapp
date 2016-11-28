package com.martingy.weatherapp.web.rest;

import com.martingy.weatherapp.WeatherappApp;
import com.martingy.weatherapp.domain.Alert;
import com.martingy.weatherapp.repository.AlertRepository;
import com.martingy.weatherapp.service.AlertService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AlertResource REST controller.
 *
 * @see AlertResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WeatherappApp.class)
public class AlertResourceIntTest {

    private static final Double DEFAULT_THRESHOLD_TEMPERATURE = 1D;
    private static final Double UPDATED_THRESHOLD_TEMPERATURE = 2D;

    private static final Double DEFAULT_TEMPERATURE = 1D;
    private static final Double UPDATED_TEMPERATURE = 2D;

    private static final String DEFAULT_WEATHER_DESCRIPTION = "AAAAA";
    private static final String UPDATED_WEATHER_DESCRIPTION = "BBBBB";

    private static final String DEFAULT_ICON = "AAAAA";
    private static final String UPDATED_ICON = "BBBBB";

    private static final Boolean DEFAULT_EMAIL = false;
    private static final Boolean UPDATED_EMAIL = true;

    private static final Boolean DEFAULT_POPUP = false;
    private static final Boolean UPDATED_POPUP = true;

    private static final String DEFAULT_CITY = "AAA";
    private static final String UPDATED_CITY = "BBB";

    private static final Integer DEFAULT_CITY_ID = 1;
    private static final Integer UPDATED_CITY_ID = 2;

    private static final ZonedDateTime DEFAULT_LAST_NOTIFICATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LAST_NOTIFICATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LAST_NOTIFICATION_DATE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_LAST_NOTIFICATION_DATE);

    @Inject
    private AlertRepository alertRepository;

    @Inject
    private AlertService alertService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAlertMockMvc;

    private Alert alert;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);

        Authentication authentication = Mockito.mock(Authentication.class);

        User user = new User("admin", "", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        Mockito.when(authentication.getPrincipal()).thenReturn(user);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        AlertResource alertResource = new AlertResource();
        ReflectionTestUtils.setField(alertResource, "alertService", alertService);
        this.restAlertMockMvc = MockMvcBuilders.standaloneSetup(alertResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alert createEntity(EntityManager em) {
        Alert alert = new Alert()
                .thresholdTemperature(DEFAULT_THRESHOLD_TEMPERATURE)
                .temperature(DEFAULT_TEMPERATURE)
                .weatherDescription(DEFAULT_WEATHER_DESCRIPTION)
                .icon(DEFAULT_ICON)
                .email(DEFAULT_EMAIL)
                .popup(DEFAULT_POPUP)
                .city(DEFAULT_CITY)
                .cityId(DEFAULT_CITY_ID)
                .lastNotificationDate(DEFAULT_LAST_NOTIFICATION_DATE);
        return alert;
    }

    @Before
    public void initTest() {
        alert = createEntity(em);
    }

    @Test
    @Transactional
    public void createAlert() throws Exception {
        int databaseSizeBeforeCreate = alertRepository.findAll().size();

        // Create the Alert

        restAlertMockMvc.perform(post("/api/alerts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(alert)))
                .andExpect(status().isCreated());

        // Validate the Alert in the database
        List<Alert> alerts = alertRepository.findAll();
        assertThat(alerts).hasSize(databaseSizeBeforeCreate + 1);
        Alert testAlert = alerts.get(alerts.size() - 1);
        assertThat(testAlert.getThresholdTemperature()).isEqualTo(DEFAULT_THRESHOLD_TEMPERATURE);
        assertThat(testAlert.getTemperature()).isEqualTo(DEFAULT_TEMPERATURE);
        assertThat(testAlert.getWeatherDescription()).isEqualTo(DEFAULT_WEATHER_DESCRIPTION);
        assertThat(testAlert.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testAlert.isEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAlert.isPopup()).isEqualTo(DEFAULT_POPUP);
        assertThat(testAlert.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testAlert.getCityId()).isEqualTo(DEFAULT_CITY_ID);
        assertThat(testAlert.getLastNotificationDate()).isEqualTo(DEFAULT_LAST_NOTIFICATION_DATE);
    }

    @Test
    @Transactional
    public void checkThresholdTemperatureIsRequired() throws Exception {
        int databaseSizeBeforeTest = alertRepository.findAll().size();
        // set the field null
        alert.setThresholdTemperature(null);

        // Create the Alert, which fails.

        restAlertMockMvc.perform(post("/api/alerts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(alert)))
                .andExpect(status().isBadRequest());

        List<Alert> alerts = alertRepository.findAll();
        assertThat(alerts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = alertRepository.findAll().size();
        // set the field null
        alert.setEmail(null);

        // Create the Alert, which fails.

        restAlertMockMvc.perform(post("/api/alerts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(alert)))
                .andExpect(status().isBadRequest());

        List<Alert> alerts = alertRepository.findAll();
        assertThat(alerts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPopupIsRequired() throws Exception {
        int databaseSizeBeforeTest = alertRepository.findAll().size();
        // set the field null
        alert.setPopup(null);

        // Create the Alert, which fails.

        restAlertMockMvc.perform(post("/api/alerts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(alert)))
                .andExpect(status().isBadRequest());

        List<Alert> alerts = alertRepository.findAll();
        assertThat(alerts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = alertRepository.findAll().size();
        // set the field null
        alert.setCity(null);

        // Create the Alert, which fails.

        restAlertMockMvc.perform(post("/api/alerts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(alert)))
                .andExpect(status().isBadRequest());

        List<Alert> alerts = alertRepository.findAll();
        assertThat(alerts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAlerts() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alerts
        restAlertMockMvc.perform(get("/api/alerts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(alert.getId().intValue())))
                .andExpect(jsonPath("$.[*].thresholdTemperature").value(hasItem(DEFAULT_THRESHOLD_TEMPERATURE.doubleValue())))
                .andExpect(jsonPath("$.[*].temperature").value(hasItem(DEFAULT_TEMPERATURE.doubleValue())))
                .andExpect(jsonPath("$.[*].weatherDescription").value(hasItem(DEFAULT_WEATHER_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.booleanValue())))
                .andExpect(jsonPath("$.[*].popup").value(hasItem(DEFAULT_POPUP.booleanValue())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].cityId").value(hasItem(DEFAULT_CITY_ID)))
                .andExpect(jsonPath("$.[*].lastNotificationDate").value(hasItem(DEFAULT_LAST_NOTIFICATION_DATE_STR)));
    }

    @Test
    @Transactional
    public void getAlert() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get the alert
        restAlertMockMvc.perform(get("/api/alerts/{id}", alert.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(alert.getId().intValue()))
            .andExpect(jsonPath("$.thresholdTemperature").value(DEFAULT_THRESHOLD_TEMPERATURE.doubleValue()))
            .andExpect(jsonPath("$.temperature").value(DEFAULT_TEMPERATURE.doubleValue()))
            .andExpect(jsonPath("$.weatherDescription").value(DEFAULT_WEATHER_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.booleanValue()))
            .andExpect(jsonPath("$.popup").value(DEFAULT_POPUP.booleanValue()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.cityId").value(DEFAULT_CITY_ID))
            .andExpect(jsonPath("$.lastNotificationDate").value(DEFAULT_LAST_NOTIFICATION_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingAlert() throws Exception {
        // Get the alert
        restAlertMockMvc.perform(get("/api/alerts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAlert() throws Exception {
        // Initialize the database
        alertService.save(alert);

        int databaseSizeBeforeUpdate = alertRepository.findAll().size();

        // Update the alert
        Alert updatedAlert = alertRepository.findOne(alert.getId());
        updatedAlert
                .thresholdTemperature(UPDATED_THRESHOLD_TEMPERATURE)
                .temperature(UPDATED_TEMPERATURE)
                .weatherDescription(UPDATED_WEATHER_DESCRIPTION)
                .icon(UPDATED_ICON)
                .email(UPDATED_EMAIL)
                .popup(UPDATED_POPUP)
                .city(UPDATED_CITY)
                .cityId(UPDATED_CITY_ID)
                .lastNotificationDate(UPDATED_LAST_NOTIFICATION_DATE);

        restAlertMockMvc.perform(put("/api/alerts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAlert)))
                .andExpect(status().isOk());

        // Validate the Alert in the database
        List<Alert> alerts = alertRepository.findAll();
        assertThat(alerts).hasSize(databaseSizeBeforeUpdate);
        Alert testAlert = alerts.get(alerts.size() - 1);
        assertThat(testAlert.getThresholdTemperature()).isEqualTo(UPDATED_THRESHOLD_TEMPERATURE);
        assertThat(testAlert.getTemperature()).isEqualTo(UPDATED_TEMPERATURE);
        assertThat(testAlert.getWeatherDescription()).isEqualTo(UPDATED_WEATHER_DESCRIPTION);
        assertThat(testAlert.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testAlert.isEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAlert.isPopup()).isEqualTo(UPDATED_POPUP);
        assertThat(testAlert.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAlert.getCityId()).isEqualTo(UPDATED_CITY_ID);
        assertThat(testAlert.getLastNotificationDate()).isEqualTo(UPDATED_LAST_NOTIFICATION_DATE);
    }

    @Test
    @Transactional
    public void deleteAlert() throws Exception {
        // Initialize the database
        alertService.save(alert);

        int databaseSizeBeforeDelete = alertRepository.findAll().size();

        // Get the alert
        restAlertMockMvc.perform(delete("/api/alerts/{id}", alert.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Alert> alerts = alertRepository.findAll();
        assertThat(alerts).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void findCities() throws Exception {
        // requires connection to the weather api
        restAlertMockMvc.perform(get("/api/alerts/cities")
            .param("q", "lon"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$", hasItem(Matchers.hasKey("id"))))
            .andExpect(jsonPath("$", hasItem(Matchers.hasKey("name"))))
            .andExpect(jsonPath("$", hasItem(Matchers.hasValue(2643743))))
            .andExpect(jsonPath("$", hasItem(Matchers.hasValue("London"))));
    }
}
