package com.martingy.weatherapp.service;

import com.martingy.weatherapp.domain.Alert;
import com.martingy.weatherapp.domain.User;
import com.martingy.weatherapp.repository.AlertRepository;
import com.martingy.weatherapp.service.dto.NotificationDTO;
import com.martingy.weatherapp.service.dto.OpenWeatherMapCityDTO;
import com.martingy.weatherapp.service.dto.OpenWeatherMapGetResponse;
import com.martingy.weatherapp.service.mapper.AlertNotificationDTOMapper;
import com.martingy.weatherapp.service.mapper.AlertOpenWeatherMapCityDTOMapper;
import com.martingy.weatherapp.service.util.WeatherApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service Implementation for managing Alert.
 */
@Service
@Transactional
public class AlertService {

    private final Logger log = LoggerFactory.getLogger(AlertService.class);

    @Inject
    private AlertRepository alertRepository;

    @Inject
    private UserService userService;

    @Inject
    private WeatherApiClient weatherApiClient;

    @Inject
    private AlertOpenWeatherMapCityDTOMapper alertOpenWeatherMapCityDTOMapper;

    @Inject
    private NotificationService notificationService;

    @Inject
    private AlertNotificationDTOMapper alertNotificationDTOMapper;

    /**
     * Find an Alert by city
     *
     * @param city the city of the Alert
     * @return found Alert
     */
    public Alert findByUserIsCurrentUserAndCity(String city) {
        return alertRepository.findByUserIsCurrentUserAndCity(city);
    }

    /**
     * Creates a new Alert, but checks if it already exists or not.
     *
     * @param alert the entity to save
     * @return the persisted entity
     * @throws Exception if Alert already exists
     */
    public Alert create(Alert alert) throws Exception {
        Alert byUserIsCurrentUserAndCity = findByUserIsCurrentUserAndCity(alert.getCity());
        if (byUserIsCurrentUserAndCity != null) {
            throw new Exception("Alert already exists");
        }

        Alert result = save(alert);

        return result;
    }

    /**
     * Save a alert.
     *
     * @param alert the entity to save
     * @return the persisted entity
     */
    public Alert save(Alert alert) {
        User user = userService.getUserWithAuthorities();
        alert.setUser(user);
        log.debug("Request to save Alert : {}", alert);
        Alert result = alertRepository.save(alert);
        return result;
    }

    /**
     *  Get all the alerts to the current user
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Alert> findAll(Pageable pageable) {
        log.debug("Request to get all Alerts to the current user");
        Page<Alert> result = alertRepository.findByUserIsCurrentUser(pageable);
        return result;
    }

    /**
     *  Get one alert by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Alert findOne(Long id) {
        log.debug("Request to get Alert : {}", id);
        Alert alert = alertRepository.findOne(id);
        return alert;
    }

    /**
     *  Delete the  alert by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Alert : {}", id);
        alertRepository.delete(id);
    }

    /**
     *  Get cities by q query String from OpenWeatherMap.
     *
     *  @param q query String to find cities in OpenWeatherMap
     *  @return found cities
     */
    public List<OpenWeatherMapCityDTO> getCities(String q) {
        log.debug("Request to query cities : {}", q);
        return weatherApiClient.findCities(q).getList();
    }

    /**
     * Updates weather by pulling data from openWeatherMap, and notifies users if there is any triggered alert
     */
    public void updateWeatherAndNotify() {
        log.debug("Checking weather started");

        ZonedDateTime currentDate = ZonedDateTime.now();
        List<Alert> alerts = alertRepository.findAll();
        // <cityId, <userId, alert>>
        Map<Integer, Map<Long, Alert>> triggeredAlerts = new HashMap<>();

        for (Alert alert : alerts) {
            updateWeatherInAlert(alert);
            prepareTriggeredAlerts(alert, triggeredAlerts, currentDate);
        }
        sendNotifications(triggeredAlerts);

        log.debug("Checking weather finished");
    }

    private void sendNotifications(Map<Integer, Map<Long, Alert>> triggeredAlerts) {
        for (Integer cityId : triggeredAlerts.keySet()) {
            Map<Long, Alert> userIdAlertMapForCity = triggeredAlerts.get(cityId);
            for (Long userId : userIdAlertMapForCity.keySet()) {
                Alert alert = userIdAlertMapForCity.get(userId);
                sendNotification(alert);
            }
        }
    }

    private void sendNotification(Alert alert) {
        if (alert.isEmail() || alert.isPopup()) {
            try {
                NotificationDTO notification = alertNotificationDTOMapper.alertToNotificationDTO(alert);
                if (alert.isEmail()) {
                    notificationService.sendEmailNotification(notification);
                }
                if (alert.isPopup()) {
                    notificationService.sendPopupNotification(notification);
                }
                alert.setLastNotificationDate(ZonedDateTime.now());
                alertRepository.save(alert);
                log.debug("Notification sent for alert: {} ", alert);
            } catch (Exception e) {
                log.error("Sending notification failed for alert: {} ", alert);
            }
        } else {
            log.debug("Non of the notification methods are enabled for alert: {} ", alert);
        }
    }

    private void prepareTriggeredAlerts(Alert currentAlert, Map<Integer, Map<Long, Alert>> triggeredAlerts, ZonedDateTime currentDate) {
        if (isAlertTriggered(currentAlert, currentDate)) {

            // Check triggeredAlerts if it has one already with this city and user data
            if (triggeredAlerts.containsKey(currentAlert.getCityId())) {

                Map<Long, Alert> userIdAlertMapForCity = triggeredAlerts.get(currentAlert.getCityId());
                if (!userIdAlertMapForCity.containsKey(currentAlert.getUser().getId())) {
                    // UserId-Alert entry does not exist yet for the city

                    userIdAlertMapForCity.put(currentAlert.getUser().getId(), currentAlert);
                }
            } else {
                // CityId and UserId-Alert map does not exist yet

                Map<Long, Alert> userIdAlertMapForCity = new HashMap<>();
                userIdAlertMapForCity.put(currentAlert.getUser().getId(), currentAlert);
                triggeredAlerts.put(currentAlert.getCityId(), userIdAlertMapForCity);
            }
        }
    }

    private boolean isAlertTriggered(Alert alert, ZonedDateTime currentDate) {

        ZonedDateTime lastNotificationDate = alert.getLastNotificationDate();
        boolean wasNotNotifiedTodayYet = true;
        if (lastNotificationDate != null) {
            long until = lastNotificationDate.until(currentDate, ChronoUnit.DAYS);
            wasNotNotifiedTodayYet = until != 0;
        }

        // If thresholdTemperature is reached,
        // at least one of the notification methods is enabled,
        // and wasn't notified today yet
        return alert.getThresholdTemperature() < alert.getTemperature()
            && (alert.isPopup() || alert.isEmail())
            && wasNotNotifiedTodayYet;
    }

    private void updateWeatherInAlert(Alert alert) {
        OpenWeatherMapGetResponse city = weatherApiClient.getCity(alert.getCityId());
        alertOpenWeatherMapCityDTOMapper.openWeatherMapCityDTOToAlert(city, alert);
        alertRepository.save(alert);
    }
}
