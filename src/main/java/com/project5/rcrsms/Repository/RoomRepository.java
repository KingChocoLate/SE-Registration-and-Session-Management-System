package com.project5.rcrsms.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project5.rcrsms.Entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByName(String name);
    long countBySession_SessionId(Long sessionId);
    boolean existsByAttendee_UserIdAndSession_SessionId(Long userId, Long sessionId);
    boolean existsByRoom_RoomIdAndSessionTime(Long roomId, LocalDateTime sessionTime);
}