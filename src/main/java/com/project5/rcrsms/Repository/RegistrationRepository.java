package com.project5.rcrsms.Repository;

import com.project5.rcrsms.Entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    // View participants per session
    List<Registration> findBySession_SessionId(Long sessionId);
    // Prevent duplicate registration
    boolean existsByUser_UserIdAndSession_SessionId(Long userId, Long sessionId);
}