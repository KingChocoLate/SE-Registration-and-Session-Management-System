package com.project5.rcrsms.controller;

import com.project5.rcrsms.Entity.Session;
import com.project5.rcrsms.Entity.Session.SessionStatus;
import com.project5.rcrsms.Entity.Role;
import com.project5.rcrsms.Repository.ConferenceRepository;
import com.project5.rcrsms.Repository.RoomRepository;
import com.project5.rcrsms.Repository.SessionRepository;
import com.project5.rcrsms.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')") // Security: Only Admins allowed
public class AdminController {

    @Autowired private SessionRepository sessionRepo;
    @Autowired private ConferenceRepository conferenceRepo;
    @Autowired private RoomRepository roomRepo;
    @Autowired private UserRepository userRepo;

    // --- DASHBOARD ---
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalSessions", sessionRepo.count());
        model.addAttribute("totalConferences", conferenceRepo.count());
        model.addAttribute("totalUsers", userRepo.count());
        model.addAttribute("recentSessions", sessionRepo.findAll());
        return "admin/dashboard";
    }

    // --- ROOM MANAGEMENT ---
    @GetMapping("/rooms")
    public String rooms(Model model) {
        model.addAttribute("rooms", roomRepo.findAll());
        return "admin/rooms";
    }

    // --- 1. SHOW CREATE FORM ---
    @GetMapping("/sessions/create")
    public String showCreateSessionForm(Model model) {
        model.addAttribute("session", new Session());
        model.addAttribute("conferences", conferenceRepo.findAll());
        model.addAttribute("rooms", roomRepo.findAll());
        
        // This uses the NEW method we added to UserRepository
        model.addAttribute("chairs", userRepo.findByRole(Role.CHAIR)); 
        
        return "admin/session_form"; 
    }

    // --- 2. SAVE SESSION ---
    @PostMapping("/sessions/save")
    public String saveSession(@ModelAttribute Session session) {
        // Admin created it -> Automatically APPROVED
        session.setStatus(SessionStatus.APPROVED);
        sessionRepo.save(session);
        return "redirect:/admin/dashboard?success=Session Created";
    }
}