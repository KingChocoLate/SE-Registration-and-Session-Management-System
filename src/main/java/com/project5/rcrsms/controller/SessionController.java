package com.project5.rcrsms.controller;

import com.project5.rcrsms.Entity.Session;
import com.project5.rcrsms.Entity.UserEntity;
import com.project5.rcrsms.Repository.ConferenceRepository;
import com.project5.rcrsms.Repository.UserRepository;
import com.project5.rcrsms.Repository.RoomRepository;
import com.project5.rcrsms.Service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sessions")
public class SessionController {

    @Autowired private SessionService sessionService;
    @Autowired private ConferenceRepository conferenceRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private RoomRepository roomRepo;

    @GetMapping({"", "/", "/list"}) 
    public String listSessions(Model model) {
        model.addAttribute("sessions", sessionService.getAllSessions());
        return "session/list";
    }

    @PreAuthorize("hasRole('ADMIN')") 
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("session", new Session());
        setupFormModels(model); 
        return "session/create";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Session session = sessionService.getSessionById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid session Id:" + id));
        
        model.addAttribute("session", session);
        setupFormModels(model); 
        return "session/create";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String saveSession(@ModelAttribute("session") Session session, Model model) { // Added Model
        // 1. Set Defaults
        if (session.getSessionTime() == null) {
            session.setSessionTime(LocalDateTime.now());
        }
        if (session.getStatus() == null) {
            session.setStatus(com.project5.rcrsms.Entity.Session.SessionStatus.APPROVED); 
        }

        try {
            // 2. Try to Save
            if (session.getSessionId() != null) {
                sessionService.updateSession(session.getSessionId(), session);
            } else {
                sessionService.createSession(session);
            }
            return "redirect:/admin/schedule";

        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            setupFormModels(model);
            return "session/create";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteSession(@PathVariable("id") Long id) {
        sessionService.deleteSession(id);
        return "redirect:/admin/schedule";
    }

    private void setupFormModels(Model model) {
        model.addAttribute("conferences", conferenceRepo.findAll());
        model.addAttribute("rooms", roomRepo.findAll());

        List<UserEntity> potentialChairs = userRepo.findAll().stream()
            .filter(u -> {
                var role = u.getRole();
                // CORRECTED: Check for "CHAIR" and "ADMIN" (matching your database)
                return role != null && (role.name().equals("CHAIR"));
            })
            .collect(Collectors.toList());

        model.addAttribute("chairs", potentialChairs);
    }
}