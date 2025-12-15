package com.project5.rcrsms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByUserUserId(Long userId);
    List<Registration> findBySessionSessionId(Long sessionId);
}