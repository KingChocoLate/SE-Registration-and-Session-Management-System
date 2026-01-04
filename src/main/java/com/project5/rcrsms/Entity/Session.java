package com.project5.rcrsms.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*; // Import for validation annotations
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long sessionId;

    // REAL WORLD: A title should be descriptive. 3 chars is reasonable.
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String title;

    // --- FIX: REMOVED @NotNull. These can be empty now. ---
    
    @Column(name = "session_time", nullable = true) 
    private LocalDateTime sessionTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conference_id", nullable = true) 
    private Conference conference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = true) 
    private Room room;

    // ------------------------------------------------------

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chair_id")
    private UserEntity chair;

    @Column(columnDefinition = "TEXT")
    private String proposalAbstract;

    @Enumerated(EnumType.STRING)
    private SessionStatus status = SessionStatus.PENDING;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Registration> registrations = new ArrayList<>();

    public enum SessionStatus { PENDING, APPROVED, REJECTED }

    public Session() {}

    // --- GETTERS AND SETTERS ---
    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDateTime getSessionTime() { return sessionTime; }
    public void setSessionTime(LocalDateTime sessionTime) { this.sessionTime = sessionTime; }

    public Conference getConference() { return conference; }
    public void setConference(Conference conference) { this.conference = conference; }

    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }

    public UserEntity getChair() { return chair; }
    public void setChair(UserEntity chair) { this.chair = chair; }

    public String getProposalAbstract() { return proposalAbstract; }
    public void setProposalAbstract(String proposalAbstract) { this.proposalAbstract = proposalAbstract; }

    public SessionStatus getStatus() { return status; }
    public void setStatus(SessionStatus status) { this.status = status; }

    public List<Registration> getRegistrations() { return registrations; }
    public void setRegistrations(List<Registration> registrations) { this.registrations = registrations; }
}