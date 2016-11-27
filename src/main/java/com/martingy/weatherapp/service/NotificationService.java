package com.martingy.weatherapp.service;

import com.martingy.weatherapp.service.dto.NotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * Service class for sending notifications.
 */
@Service
public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Inject
    private SimpMessageSendingOperations messagingTemplate;

    @Inject
    private MailService mailService;

    private static final String EMAIL_TEMPLATE = "" +
        "<!DOCTYPE html>" +
        "<html lang=\"en\">" +
        "<head>" +
        "    <meta charset=\"UTF-8\">" +
        "    <title>Weatherapp</title>" +
        "</head>" +
        "<body>" +
        "<div>" +
        "    <div>" +
        "        <h2>Weatherapp weather alert for #city#.</h2>" +
        "        <br>" +
        "        <p>Current temperature is <b>#temperature#</b> C°.</p>" +
        "        <p>Threshold temperature is <b>#thresholdTemperature#</b> C°.</p>" +
        "        <p>Current temperature is higher than the preset threshold temperature.</p>" +
        "        <br>" +
        "        <p>" +
        "            <span>Regards,</span>" +
        "            <br>" +
        "            <em>weatherapp Team.</em>" +
        "        </p>" +
        "    </div>" +
        "</div>" +
        "</body>" +
        "</html>";

    public void sendEmailNotification(NotificationDTO notification) {
        String emailTemplate = EMAIL_TEMPLATE;
        emailTemplate = emailTemplate.replace("#city#", notification.getCity());
        emailTemplate = emailTemplate.replace("#temperature#", Double.toString(notification.getTemperature()));
        emailTemplate = emailTemplate.replace("#thresholdTemperature#", Double.toString(notification.getThresholdTemperature()));

        mailService.sendEmail(notification.getEmail(), "Weather alert for " + notification.getCity(), emailTemplate, false, true);

        log.debug("Notification email is sent: {} ", notification);
    }

    public void sendPopupNotification(NotificationDTO notification) {
        messagingTemplate.convertAndSendToUser(notification.getLogin(), "/queue/notification", notification);
        log.debug("Notification popup is sent: {} ", notification);
    }
}
