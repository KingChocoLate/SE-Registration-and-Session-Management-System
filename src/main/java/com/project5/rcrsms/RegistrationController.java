package com.conference.controller;

import com.conference.model.Registration;
import com.conference.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    /**
     * Register an attendee for a session
     */
    @PostMapping
    public ResponseEntity<?> registerAttendee(@RequestBody Registration registration) {
        try {
            Registration savedRegistration = registrationService.registerAttendee(registration);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRegistration);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * Get all registrations for a session
     */
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<Registration>> getRegistrationsBySession(
            @PathVariable Long sessionId) {
        List<Registration> registrations = registrationService.getRegistrationsBySession(sessionId);
        return ResponseEntity.ok(registrations);
    }

    /**
     * Get all registrations for an attendee
     */
    @GetMapping("/attendee/{attendeeId}")
    public ResponseEntity<List<Registration>> getRegistrationsByAttendee(
            @PathVariable Long attendeeId) {
        List<Registration> registrations = registrationService.getRegistrationsByAttendee(attendeeId);
        return ResponseEntity.ok(registrations);
    }

    /**
     * Cancel a registration
     */
    @DeleteMapping("/{registrationId}")
    public ResponseEntity<?> cancelRegistration(@PathVariable Long registrationId) {
        try {
            registrationService.cancelRegistration(registrationId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get available spots for a session
     */
    @GetMapping("/session/{sessionId}/available")
    public ResponseEntity<Integer> getAvailableSpots(@PathVariable Long sessionId) {
        try {
            int availableSpots = registrationService.getAvailableSpots(sessionId);
            return ResponseEntity.ok(availableSpots);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(0);
        }
    }
}