package com.project5.rcrsms.Repository;

import com.project5.rcrsms.Entity.Conference;
import com.project5.rcrsms.Entity.Session;
import com.project5.rcrsms.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByConference(Conference conference);
    List<Session> findByConferenceConferenceId(Long conferenceId);
    List<Session> findByConferenceConferenceIdAndSessionTimeGreaterThanEqual(Long conferenceId, LocalDateTime sessionTime);
    List<Session> findByChair(UserEntity chair);
    List<Session> findByChairId(Long userId);
    List<Session> findByTitleContainingIgnoreCase(String keyword);
    List<Session> findBySessionTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Session> findBySessionTimeGreaterThanEqualOrderBySessionTimeAsc(LocalDateTime time);
}
