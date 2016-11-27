package com.martingy.weatherapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.martingy.weatherapp.domain.Alert;
import com.martingy.weatherapp.service.AlertService;
import com.martingy.weatherapp.service.dto.OpenWeatherMapCityDTO;
import com.martingy.weatherapp.web.rest.util.HeaderUtil;
import com.martingy.weatherapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Alert.
 */
@RestController
@RequestMapping("/api")
public class AlertResource {

    private final Logger log = LoggerFactory.getLogger(AlertResource.class);

    @Inject
    private AlertService alertService;

    /**
     * POST  /alerts : Create a new alert.
     *
     * @param alert the alert to create
     * @return the ResponseEntity with status 201 (Created) and with body the new alert, or with status 400 (Bad Request) if the alert has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/alerts")
    @Timed
    public ResponseEntity<Alert> createAlert(@Valid @RequestBody Alert alert) throws Exception {
        log.debug("REST request to save Alert : {}", alert);
        if (alert.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("alert", "idexists", "A new alert cannot already have an ID")).body(null);
        }
        Alert result = alertService.create(alert);
        return ResponseEntity.created(new URI("/api/alerts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("alert", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /alerts : Updates an existing alert.
     *
     * @param alert the alert to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated alert,
     * or with status 400 (Bad Request) if the alert is not valid,
     * or with status 500 (Internal Server Error) if the alert couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/alerts")
    @Timed
    public ResponseEntity<Alert> updateAlert(@Valid @RequestBody Alert alert) throws Exception {
        log.debug("REST request to update Alert : {}", alert);
        if (alert.getId() == null) {
            return createAlert(alert);
        }
        Alert result = alertService.save(alert);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("alert", alert.getId().toString()))
            .body(result);
    }

    /**
     * GET  /alerts : get all the alerts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of alerts in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/alerts")
    @Timed
    public ResponseEntity<List<Alert>> getAllAlerts(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Alerts");
        Page<Alert> page = alertService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/alerts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /alerts/:id : get the "id" alert.
     *
     * @param id the id of the alert to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the alert, or with status 404 (Not Found)
     */
    @GetMapping("/alerts/{id}")
    @Timed
    public ResponseEntity<Alert> getAlert(@PathVariable Long id) {
        log.debug("REST request to get Alert : {}", id);
        Alert alert = alertService.findOne(id);
        return Optional.ofNullable(alert)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /alerts/:id : delete the "id" alert.
     *
     * @param id the id of the alert to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/alerts/{id}")
    @Timed
    public ResponseEntity<Void> deleteAlert(@PathVariable Long id) {
        log.debug("REST request to delete Alert : {}", id);
        alertService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("alert", id.toString())).build();
    }

    /**
     * GET  /alerts/cities : query cities by q query String from OpenWeatherMap.
     *
     * @param q query String to find cities in OpenWeatherMap
     * @return the ResponseEntity with status 200 (OK) and the list of cities in body
     */
    @GetMapping("/alerts/cities")
    @Timed
    public ResponseEntity<List<OpenWeatherMapCityDTO>> getCities(@RequestParam("q") String q) {
        log.debug("REST request to find cities");
        List<OpenWeatherMapCityDTO> res = alertService.getCities(q);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * GET  /alerts/city=:city : get the "city" alert.
     *
     * @param city the city of the alert to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the alert
     */
    @GetMapping("/alerts/city/{city}")
    @Timed
    public ResponseEntity<Alert> getAlertByCity(@PathVariable String city) {
        log.debug("REST request to get Alert by cityId : {}", city);
        Alert alert = alertService.findByUserIsCurrentUserAndCity(city);
        return new ResponseEntity<>(alert, HttpStatus.OK);
    }
}
