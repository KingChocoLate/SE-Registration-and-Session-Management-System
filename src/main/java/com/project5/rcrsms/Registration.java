package com.project5.rcrsms;

import org.apache.catalina.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.websocket.Session;

@Entity
@Table(name = "registration")
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registration_id")
    private Long registrationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    @NotNull(message = "Session is required")
    private Session session;

    // Constructors
    public Registration() {}

    // Getters and Setters
    public Long getRegistrationId() { return registrationId; }
    public void setRegistrationId(Long registrationId) { this.registrationId = registrationId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Session getSession() { return session; }
    public void setSession(Session session) { this.session = session; }
}