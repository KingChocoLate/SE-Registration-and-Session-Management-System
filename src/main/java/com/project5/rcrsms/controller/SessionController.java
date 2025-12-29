package com.project5.rcrsms.controller;

import com.project5.rcrsms.Entity.Session;
import com.project5.rcrsms.Repository.ConferenceRepository;
import com.project5.rcrsms.Repository.SessionRepository;
import com.project5.rcrsms.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/sessions")
public class SessionController {

    @Autowired
    private SessionRepository sessionRepo;

    @Autowired
    private ConferenceRepository conferenceRepo;

    @Autowired
    private UserRepository userRepo;

    // 1. List all sessions (Public View)
    @GetMapping("/list")
    public String listSessions(Model model) {
        model.addAttribute("sessions", sessionRepo.findAll());
        return "session/list"; // Matches templates/session/list.html
    }

    // 2. Show Create Form
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("session", new Session());
        // We need lists of Conferences and Chairs so the user can select them in the dropdowns
        model.addAttribute("conferences", conferenceRepo.findAll());
        model.addAttribute("chairs", userRepo.findAll()); // Ideally filter by Role.CHAIR
        return "session/create"; // Matches templates/session/create.html
    }

    // 3. Handle Create/Update Submission
    @PostMapping("/save")
    public String saveSession(@ModelAttribute("session") Session session) {
        // Basic validation or setting default time if needed
        if (session.getSessionTime() == null) {
            session.setSessionTime(LocalDateTime.now());
        }
        
        sessionRepo.save(session);
        return "redirect:/admin/schedule"; // Redirect back to admin schedule
    }

    // 4. Show Edit Form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Session session = sessionRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid session Id:" + id));
        
        model.addAttribute("session", session);
        model.addAttribute("conferences", conferenceRepo.findAll());
        model.addAttribute("chairs", userRepo.findAll());
        
        return "session/create"; // We reuse the create form for editing!
    }

    // 5. Delete Session
    @GetMapping("/delete/{id}")
    public String deleteSession(@PathVariable("id") Long id) {
        Session session = sessionRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid session Id:" + id));
        
        sessionRepo.delete(session);
        return "redirect:/admin/schedule";
    }
}