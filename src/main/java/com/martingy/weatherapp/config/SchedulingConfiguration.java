package com.martingy.weatherapp.config;

import com.martingy.weatherapp.service.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
public class SchedulingConfiguration implements SchedulingConfigurer {

    private final Logger log = LoggerFactory.getLogger(SchedulingConfiguration.class);

    @Inject
    private JHipsterProperties jHipsterProperties;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        log.debug("Configuring scheduled tasks");
        taskRegistrar.setScheduler(taskScheduler());
        taskRegistrar.addTriggerTask(
            () -> alertService().updateWeatherAndNotify(),
            triggerContext -> {
                Calendar nextExecutionTime =  new GregorianCalendar();
                Date lastActualExecutionTime = triggerContext.lastActualExecutionTime();
                nextExecutionTime.setTime(lastActualExecutionTime != null ? lastActualExecutionTime : new Date());
                nextExecutionTime.add(Calendar.MINUTE, jHipsterProperties.getWeatherApi().getWeatherCheckFrequencyInMins());
                log.debug("updateWeatherAndNotify next run: {} ", nextExecutionTime.getTime());
                return nextExecutionTime.getTime();
            }
        );
    }

    @Bean(destroyMethod="shutdown")
    public Executor taskScheduler() {
        return Executors.newScheduledThreadPool(42);
    }

    @Bean
    public AlertService alertService() {
        return new AlertService();
    }
}
