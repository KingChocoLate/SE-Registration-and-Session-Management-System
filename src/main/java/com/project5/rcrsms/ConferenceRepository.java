package com.project5.rcrsms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {

    Optional<Conference> findByName(String name);

    List<Conference> findByLocation(String location);

    List<Conference> findByNameContainingIgnoreCase(String keyword);

    List<Conference> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

    List<Conference> findByStartDateGreaterThanEqualOrderByStartDateAsc(LocalDate today);

    List<Conference> findByEndDateLessThanOrderByEndDateDesc(LocalDate today);
}