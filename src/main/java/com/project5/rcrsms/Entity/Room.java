package com.project5.rcrsms.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer capacity;

    // --- ADD THIS FIELD OR THE APP WILL CRASH ---
    @Column(nullable = false)
    private String location; 

    public Room() {}

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    // --- ADD GETTER AND SETTER ---
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}