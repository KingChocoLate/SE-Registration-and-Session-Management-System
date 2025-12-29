package com.conference.service;

import com.conference.model.Registration;
import com.conference.model.Session;
import com.conference.model.Room;
import com.conference.repository.RegistrationRepository;
import com.conference.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private SessionRepository sessionRepository;

    /**
     * Register an attendee for a session with capacity validation
     */
    @Transactional
    public Registration registerAttendee(Registration registration) {
        // Validate session exists
        Session session = sessionRepository.findById(registration.getSession().getId())
            .orElseThrow(() -> new IllegalArgumentException(
                "Session not found with ID: " + registration.getSession().getId()));

        // Get the room for this session
        Room room = session.getRoom();
        if (room == null) {
            throw new IllegalStateException(
                "Session does not have an assigned room");
        }

        // Count current registrations for this session
        long currentCount = registrationRepository.countBySessionId(session.getId());

        // Check room capacity
        if (currentCount >= room.getCapacity()) {
            throw new IllegalStateException(
                String.format("Session '%s' is at full capacity (%d/%d)",
                    session.getTitle(),
                    currentCount,
                    room.getCapacity()));
        }

        // Check for duplicate registration (same attendee, same session)
        boolean alreadyRegistered = registrationRepository
            .existsByAttendeeIdAndSessionId(
                registration.getAttendee().getId(),
                session.getId());

        if (alreadyRegistered) {
            throw new IllegalStateException(
                "Attendee is already registered for this session");
        }

        // Save the registration
        return registrationRepository.save(registration);
    }

    /**
     * Get all registrations for a specific session
     */
    @Transactional(readOnly = true)
    public List<Registration> getRegistrationsBySession(Long sessionId) {
        return registrationRepository.findBySessionId(sessionId);
    }

    /**
     * Get all registrations for a specific attendee
     */
    @Transactional(readOnly = true)
    public List<Registration> getRegistrationsByAttendee(Long attendeeId) {
        return registrationRepository.findByAttendeeId(attendeeId);
    }

    /**
     * Cancel a registration
     */
    @Transactional
    public void cancelRegistration(Long registrationId) {
        if (!registrationRepository.existsById(registrationId)) {
            throw new IllegalArgumentException(
                "Registration not found with ID: " + registrationId);
        }
        registrationRepository.deleteById(registrationId);
    }

    /**
     * Get available spots for a session
     */
    @Transactional(readOnly = true)
    public int getAvailableSpots(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException(
                "Session not found with ID: " + sessionId));

        Room room = session.getRoom();
        if (room == null) {
            return 0;
        }

        long currentCount = registrationRepository.countBySessionId(sessionId);
        return Math.max(0, room.getCapacity() - (int) currentCount);
    }
}