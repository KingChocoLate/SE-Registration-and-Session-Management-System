package com.project5.rcrsms.controller;

import com.project5.rcrsms.Entity.Session;
import com.project5.rcrsms.Repository.ConferenceRepository;
import com.project5.rcrsms.Repository.SessionRepository;
import com.project5.rcrsms.Repository.UserRepository;
import com.project5.rcrsms.Repository.RoomRepository; // <--- 1. NEW IMPORT
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

    // 2. NEW: Inject the RoomRepository
    @Autowired
    private RoomRepository roomRepo; 

    // 1. List all sessions (Public View)
    @GetMapping("/list")
    public String listSessions(Model model) {
        model.addAttribute("sessions", sessionRepo.findAll());
        return "session/list";
    }

    // 2. Show Create Form/
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("session", new Session());
        model.addAttribute("conferences", conferenceRepo.findAll());
        model.addAttribute("chairs", userRepo.findAll());
        
        // 3. NEW: Add Rooms to the model so the dropdown works
        model.addAttribute("rooms", roomRepo.findAll()); 
        
        return "session/create";
    }

    // 3. Handle Create/Update Submission
    @PostMapping("/save")
    public String saveSession(@ModelAttribute("session") Session session) {
        if (session.getSessionTime() == null) {
            session.setSessionTime(LocalDateTime.now());
        }
        
        sessionRepo.save(session);
        return "redirect:/admin/schedule";
    }

    // 4. Show Edit Form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Session session = sessionRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid session Id:" + id));
        
        model.addAttribute("session", session);
        model.addAttribute("conferences", conferenceRepo.findAll());
        model.addAttribute("chairs", userRepo.findAll());
        
        // 3. NEW: Add Rooms here too so editing works
        model.addAttribute("rooms", roomRepo.findAll());
        
        return "session/create";
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