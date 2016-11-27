package com.martingy.weatherapp.repository;

import com.martingy.weatherapp.domain.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the Alert entity.
 */
@SuppressWarnings("unused")
public interface AlertRepository extends JpaRepository<Alert,Long> {

    @Query("select alert from Alert alert where alert.user.login = ?#{principal.username}")
    List<Alert> findByUserIsCurrentUser();

    @Query("select alert from Alert alert where alert.user.login = ?#{principal.username}")
    Page<Alert> findByUserIsCurrentUser(Pageable var1);

    @Query("select alert from Alert alert where alert.user.login = ?#{principal.username} and alert.city = ?1")
    Alert findByUserIsCurrentUserAndCity(String city);

}
