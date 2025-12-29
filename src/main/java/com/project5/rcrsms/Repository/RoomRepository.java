package com.project5.rcrsms.Repository;

import com.project5.rcrsms.Entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}