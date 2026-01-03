package com.project5.rcrsms.controller;

import com.project5.rcrsms.Entity.Registration;
import com.project5.rcrsms.Entity.UserEntity;
import com.project5.rcrsms.Service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.security.Principal;
import com.project5.rcrsms.Repository.RegistrationRepository;
import com.project5.rcrsms.Repository.SessionRepository;
import com.project5.rcrsms.Repository.UserRepository;

@Controller
@RequestMapping("/api/registrations")
public class RegistrationController {

    @Autowired
    private RegistrationRepository registrationRepo;

    @Autowired
    private SessionRepository sessionRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RegistrationService registrationService;

    /**
     * Register an attendee for a session
     */
    @PostMapping("/add")
    public String register(@RequestParam("sessionId") Long sessionId, Principal principal, RedirectAttributes ra){
        UserEntity user = userRepo.findByUsername(principal.getName()).orElseThrow(() -> new IllegalStateException("Authenticated user not found: " + principal.getName()));;

        // LOGIC: Prevent duplicate
        if (registrationRepo.existsByUser_userIdAndSession_sessionId(user.getUserId(), sessionId)) {
            ra.addFlashAttribute("error", "You are already registered for this session.");
            return "redirect:/sessions";
        }

        Registration reg = new Registration();
        reg.setUser(user);
        reg.setSession(sessionRepo.findById(sessionId).orElse(null));
        registrationRepo.save(reg);

        ra.addFlashAttribute("success", "Registration successful!");
        return "redirect:/sessions";
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

    @GetMapping("/view/{id}")
        public String viewParticipants(@PathVariable("id") Long id, Model model) {
            model.addAttribute("participants", registrationRepo.findBySession_sessionId(id));
            return "registration-view";
        }   
}