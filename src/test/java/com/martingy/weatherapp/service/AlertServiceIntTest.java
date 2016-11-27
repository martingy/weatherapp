package com.martingy.weatherapp.service;

import com.martingy.weatherapp.WeatherappApp;
import com.martingy.weatherapp.domain.Alert;
import com.martingy.weatherapp.domain.User;
import com.martingy.weatherapp.repository.AlertRepository;
import com.martingy.weatherapp.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the AlertService Service.
 *
 * @see AlertService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WeatherappApp.class)
@Transactional
public class AlertServiceIntTest {

    @Inject
    private UserRepository userRepository;

    @Inject
    private AlertRepository alertRepository;

    @Inject
    private AlertService alertService;

    private NotificationService notificationService;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        notificationService = Mockito.mock(NotificationService.class);
    }

    @Before
    public void initTest() {
        ReflectionTestUtils.setField(alertService, "notificationService", notificationService);
    }

    private User getUser() {
        User user = new User();
        user.setLogin("test");
        user.setEmail("test@email.cc");
        user.setPassword("testpwdtestpwdtestpwdtestpwdtestpwdtestpwdtestpwdtestpwdtest");
        return user;
    }

    private Alert getAlert(User user) {
        Alert alert = new Alert()
            .thresholdTemperature(-100d)
            .temperature(10d)
            .weatherDescription("")
            .icon("")
            .email(true)
            .popup(true)
            .city("London")
            .cityId(2643743)
            .user(user);
        return alert;
    }

    @Test
    @Transactional
    public void testAlertBeingUpdated() {
        // Initialize the database
        User user = userRepository.saveAndFlush(getUser());
        Alert alert = alertRepository.saveAndFlush(getAlert(user));

        alertService.updateWeatherAndNotify();

        Alert updatedAlert = alertService.findOne(alert.getId());

        assertThat(updatedAlert.getIcon()).isNotNull();
        assertThat(updatedAlert.getWeatherDescription()).isNotNull();
    }

    @Test
    @Transactional
    public void testNotificationBeingSent() {
        // Initialize the database
        User user = userRepository.saveAndFlush(getUser());
        Alert alert = alertRepository.saveAndFlush(getAlert(user));

        alertService.updateWeatherAndNotify();

        Alert updatedAlert = alertService.findOne(alert.getId());

        assertThat(updatedAlert.getLastNotificationDate()).isNotNull();

        Mockito.verify(notificationService, Mockito.times(1)).sendEmailNotification(Mockito.any());
        Mockito.verify(notificationService, Mockito.times(1)).sendPopupNotification(Mockito.any());
    }

    @Test
    @Transactional
    public void testSecondNotificationNotBeingSentOnTheSameDay() {
        // Initialize the database
        User user = userRepository.saveAndFlush(getUser());
        Alert alert = alertRepository.saveAndFlush(getAlert(user));

        alertService.updateWeatherAndNotify();

        Alert updatedAlert = alertService.findOne(alert.getId());

        assertThat(updatedAlert.getLastNotificationDate()).isNotNull();

        Mockito.verify(notificationService, Mockito.times(1)).sendEmailNotification(Mockito.any());
        Mockito.verify(notificationService, Mockito.times(1)).sendPopupNotification(Mockito.any());

        alertService.updateWeatherAndNotify();

        Alert updatedAlert2 = alertService.findOne(alert.getId());

        assertThat(updatedAlert2.getLastNotificationDate()).isNotNull();
        assertThat(updatedAlert2.getLastNotificationDate()).isEqualTo(updatedAlert.getLastNotificationDate());

        Mockito.verify(notificationService, Mockito.times(1)).sendEmailNotification(Mockito.any());
        Mockito.verify(notificationService, Mockito.times(1)).sendPopupNotification(Mockito.any());
    }
}
